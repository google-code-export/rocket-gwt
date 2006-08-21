/*
 * Copyright 2006 NSW Police Government Australia
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.server.browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.StringHelper;
import rocket.server.util.IoHelper;

/**
 * This file servlet supports scaling of numeric values found within a css.
 *
 * The scaling factor is passed as a queryParameter, the pathInfo locates the css file. An initParameter is used to select which properties
 * are checked and possibly scaled.
 *
 * Pixel values (pxs) scaling are the only values which may be scaled. All other unit types are ignored and served unchanged.
 *
 * @author Miroslav Pokorny (mP)
 */
public class CssUnitScalingFileServlet extends HttpServlet {

    private ThreadLocal request = new ThreadLocal();

    protected HttpServletRequest getRequest() {
        return (HttpServletRequest) this.request.get();
    }

    protected void setRequest(final HttpServletRequest request) {
        ObjectHelper.checkNotNull("parameter:request", request);
        this.request.set(request);
    }

    public void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        this.setRequest(request);

        while (true) {
            // check that the file exists...
            InputStream input = null;
            PrintWriter writer = null;
            try {
                final String filename = this.getFilename(request, response);
                if (null == filename) {
                    break;
                }

                input = locateFile(filename);
                if (null == input) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "file[" + filename + "]");
                    break;
                }

                final float scalingFactor = this.getScalingFactor(request,
                        response);
                if (scalingFactor == Float.NaN) {
                    break;
                }

                writer = response.getWriter();
                writer
                        .println("/* only pixel units (px) scaled, scalingFactor: "
                                + scalingFactor + "*/");
                this.visitFile(
                        new BufferedReader(new InputStreamReader(input)),
                        scalingFactor, writer);
                writer.flush();
                break;

            } finally {
                IoHelper.closeIfNecessary(input);
                IoHelper.closeIfNecessary(writer);
            }
        } // while
    }

    protected float getScalingFactor(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        ObjectHelper.checkNotNull("parameter:request", request);
        ObjectHelper.checkNotNull("parameter:response", response);

        float scalingFactor = Float.NaN;
        while (true) {
            final String scalingFactorParameterName = this
                    .getScalingFactorParameterName();
            final String scalingFactorString = request
                    .getParameter(scalingFactorParameterName);
            if (StringHelper.isNullOrEmpty(scalingFactorString)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "The scalingFactor parameter["
                                + scalingFactorParameterName
                                + "] is missing, requestUrl["
                                + request.getRequestURL() + "]");
                break;
            }

            try {
                scalingFactor = Float.parseFloat(scalingFactorString);
            } catch (final NumberFormatException bad) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "The scalingFactor parameter["
                                + scalingFactorParameterName + "]=["
                                + scalingFactorString
                                + ", is not a valid number.");
            }
            break;
        }
        return scalingFactor;
    }

    protected String getFilename(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        ObjectHelper.checkNotNull("parameter:request", request);
        ObjectHelper.checkNotNull("parameter:response", response);

        final String parameterName = this.getFilenameParameterName();
        final String filename = request.getParameter(parameterName);
        if (StringHelper.isNullOrEmpty(filename)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "The filename parameter[" + parameterName
                            + "] is missing, url[" + request.getRequestURI()
                            + '?' + request.getQueryString() + "]");
        }
        return filename;
    }

    protected InputStream locateFile(final String path) {
        return this.getServletConfig().getServletContext().getResourceAsStream(
                path);
    }

    /**
     * Reads the css file line by line scanning for css properties.
     *
     * It is not smart enough to skip the processing of comments.
     *
     * @param reader
     * @param scalingFactor
     * @param writer
     * @throws IOException
     */
    protected void visitFile(final BufferedReader reader,
            final float scalingFactor, final PrintWriter writer)
            throws IOException {
        ObjectHelper.checkNotNull("parameter:reader", reader);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:scalingFactor",
                scalingFactor, 0.0);
        ObjectHelper.checkNotNull("parameter:writer", writer);

        boolean ignoreBlankLine = false;
        while (true) {
            final String line = reader.readLine();
            if (null == line) {
                break;
            }
            if (line.length() == 0) {
                if (ignoreBlankLine) {
                    writer.println();
                }
                ignoreBlankLine = true;
                continue;
            }

            final String lineToBeWritten = this.visitLine(line, scalingFactor);
            writer.println(lineToBeWritten);
            ignoreBlankLine = false;
        }
    }

    /**
     * To be recognized as a property with a numeric value that may be scaled the line must be correctly formed. name <colon> value "px"<semicolon>.
     *
     * Whitespace is not significant and is ignored.
     *
     * @param line
     * @param scalingFactor
     *            the scaling factor that is applied to any px units.
     * @return
     */
    protected String visitLine(final String line, final float scalingFactor) {
        StringHelper.checkNotNull("parameter:line", line);
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:scalingFactor",
                scalingFactor, 0.0);

        String visited = null;
        while (true) {
            /* fuirst check if the current line contains an import statement and url */
            final String afterImportVisit = this.visitImportStatement(line,
                    scalingFactor);
            if (null != afterImportVisit) {
                visited = afterImportVisit;
                break;
            }

            // now check if the line contains a property value,
            final String afterPropertyEntryVisit = this.visitPropertyEntry(
                    line, scalingFactor);
            if (null != afterPropertyEntryVisit) {
                visited = afterPropertyEntryVisit;
                break;
            }

            // simply return the line as is.
            visited = line;
            break;
        }

        return visited;
    }

    /**
     * Attempts to rewrite the given line if an import with a url is found.
     * @param line
     * @param scalingFactor
     * @return null if the line didnt contain an import statement otherwise returns the line untouched or possibly the modified import/url
     */
    protected String visitImportStatement(final String line,
            final float scalingFactor) {
        StringHelper.checkNotNull("parameter:line", line);
        PrimitiveHelper.checkGreaterThan("parameter:scalingFactor",
                scalingFactor, 0);

        String visited = null;
        while (true) {
            final String[] tokens = StringHelper.split(line, " ", true);
            if (tokens.length != 4) {
                break;
            }

            if (false == BrowserConstants.IMPORT_DIRECTIVE.equals(tokens[0])) {
                break;
            }

            if (false == "url(".equals(tokens[1])) {
                break;
            }

            final String rightBracket = tokens[3];
            if (false == ");".equals(rightBracket)) {
                break;
            }

            final String url = tokens[2];
            if (StringHelper.isNullOrEmpty(url)) {
                visited = line;
                break;
            }

            // drop any queryString which may be present.
            final StringBuffer buf = new StringBuffer();
            buf.append(BrowserConstants.IMPORT_DIRECTIVE);
            buf.append(" url(");

            final HttpServletRequest request = this.getRequest();
            final String contextPath =request.getContextPath();
            buf.append(contextPath);
            buf.append(request.getServletPath());

            buf.append('?');

            // filename
            buf.append(this.getFilenameParameterName());
            buf.append('=');
            //buf.append( contextPath );
            buf.append(url); // FIX urlEncode

            buf.append('&');

            // scalingFactor
            buf.append(this.getScalingFactorParameterName());
            buf.append('=');
            buf.append(scalingFactor);
            buf.append(");");

            visited = buf.toString();
            break;
        }
        return visited;
    }

    /**
     * Checks and attempts to modify the property entry present within the given line.
     *
     * @return null if the line did not contain a propertyEntry, otherwise it contains the property entry which may have been modified.
     */
    protected String visitPropertyEntry(final String line,
            final float scalingFactor) {
        StringHelper.checkNotNull("parameter:line", line);
        PrimitiveHelper.checkGreaterThan("parameter:scalingFactor",
                scalingFactor, 0);

        String visited = null;
        while (true) {
            /* this attempts to locate a css property entry */
            final int colon = line.indexOf(':');
            if (-1 == colon) {
                break;
            }

            final String propertyName = line.substring(0, colon).trim();
            if (StringHelper.isNullOrEmpty(propertyName)) {
                break;
            }

            final int semiColon = line.indexOf(';', colon);
            if (-1 == semiColon) {
                break;
            }

            // ensure that a value is present.
            final String propertyValue = line.substring(colon + 1, semiColon)
                    .trim();
            if (StringHelper.isNullOrEmpty(propertyValue)) {
                break;
            }

            // make sure the value is a pixel unit.
            if (false == StringHelper.endsWithIgnoringCase(propertyValue,
                    BrowserConstants.PIXEL_UNIT)) {
                visited = line.trim();
                break;
            }

            // extract the numeric value itself.
            final String numberValueString = propertyValue.substring(0,
                    propertyValue.length()
                            - BrowserConstants.PIXEL_UNIT.length());

            // check that numberValue is in fact a positive integer number.
            float numberValue = Float.NaN;
            try {
                numberValue = Float.parseFloat(numberValueString);
            } catch (final NumberFormatException bad) {
                break;
            }

            // scale numberValue.
            final int integerValue = (int) (0.5 + (numberValue * scalingFactor));

            // format and write out the line.
            visited = "   " + propertyName + ": " + integerValue
                    + BrowserConstants.PIXEL_UNIT + ";";
            break;
        }

        return visited;
    }

    public void init() throws ServletException {
        final String scalingFactorParameterName = this
                .getInitParameter(BrowserConstants.SCALING_FACTOR_PARAMETER_NAME);
        if (StringHelper.isNullOrEmpty(scalingFactorParameterName)) {
            throw new ServletException("The servlet[" + this.getServletName()
                    + "]init-parameter["
                    + BrowserConstants.SCALING_FACTOR_PARAMETER_NAME
                    + ") is missing or empty.");
        }
        this.setScalingFactorParameterName(scalingFactorParameterName);

        final String filenameParameterName = this
                .getInitParameter(BrowserConstants.CSS_FILENAME_PARAMETER_NAME);
        if (StringHelper.isNullOrEmpty(filenameParameterName)) {
            throw new ServletException("The servlet[" + this.getServletName()
                    + "]init-parameter["
                    + BrowserConstants.CSS_FILENAME_PARAMETER_NAME
                    + ") is missing or empty.");
        }
        this.setFilenameParameterName(filenameParameterName);
    }

    /**
     * The name of the queryParameter which contains the scaling factor
     */
    private String scalingFactorParameterName;

    protected String getScalingFactorParameterName() {
        StringHelper.checkNotEmpty("field:scalingFactorParameterName",
                scalingFactorParameterName);
        return this.scalingFactorParameterName;
    }

    protected void setScalingFactorParameterName(
            final String scalingFactorParameterName) {
        StringHelper.checkNotEmpty("parameter:scalingFactorParameterName",
                scalingFactorParameterName);
        this.scalingFactorParameterName = scalingFactorParameterName;
    }

    /**
     * The name of the queryParameter which contains the filename of the css to be served
     */
    private String filenameParameterName;

    protected String getFilenameParameterName() {
        StringHelper.checkNotEmpty("field:filenameParameterName",
                filenameParameterName);
        return this.filenameParameterName;
    }

    protected void setFilenameParameterName(final String filenameParameterName) {
        StringHelper.checkNotEmpty("parameter:filenameParameterName",
                filenameParameterName);
        this.filenameParameterName = filenameParameterName;
    }

    public String toString() {
        return super.toString() + ", scalingFactorParameterName["
                + scalingFactorParameterName + "], filenameParameterName["
                + filenameParameterName + "]";
    }
}