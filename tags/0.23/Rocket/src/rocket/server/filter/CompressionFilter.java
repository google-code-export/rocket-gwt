/*
 * Copyright Miroslav Pokorny
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
package rocket.server.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import rocket.util.server.UncheckedIOException;

/**
 * This filter detects if the browser supports gzip compressed output substituting a new HttpServletResponse implementation if required.
 * 
 * A request attribute is set to stop multiple installs of this filter which may occur if one resource includes another.
 */
public class CompressionFilter implements Filter {
    /**
     * A local copy of the filterConfig which may be used to read config parameters
     */
    private FilterConfig config;

    /**
     * This method is used by the container when it creates and initializes a filter prior to use.
     * 
     * @param config
     *            A config object
     */
    public void init(final FilterConfig config) {
        this.config = config;
    }

    private static final String ALREADY_INSTALLED = CompressionFilter.class.getName() + ".ALREADY_INSTALLED";

    /**
     * The centralized service method that is invoked when filters are actioned.
     * 
     * @param request
     *            The original request object
     * @param response
     *            The original response object
     * @param chain
     *            A chain implementation from the container
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        boolean compress = false;
        if (request.getAttribute(ALREADY_INSTALLED) == null && request instanceof HttpServletRequest)

            if (false == (response instanceof CompressionFilterHttpServletResponse)
                    && request instanceof HttpServletRequest)

            {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                Enumeration headers = httpRequest.getHeaders("Accept-Encoding");
                while (headers.hasMoreElements()) {
                    String value = (String) headers.nextElement();
                    if (value.indexOf("gzip") != -1) {
                        compress = true;
                    }
                }
            }
        request.setAttribute(ALREADY_INSTALLED, "true");
        if (compress) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.addHeader("Content-Encoding", "gzip");
            CompressionFilterHttpServletResponse compressionResponse = new CompressionFilterHttpServletResponse(
                    httpResponse);
            chain.doFilter(request, compressionResponse);
            compressionResponse.finish();
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * This method is invoked when the filter is decommissioned and destroyed.
     */
    public void destroy() {
        config = null;
    }

    class CompressionFilterServletOutputStream extends ServletOutputStream {
        /**
         * The original ServletOutputStream
         */
        private ServletOutputStream out;

        /**
         * The compressed stream
         */
        private GZIPOutputStream gzip;

        /**
         * Compresses the specified stream using the specified initial buffer.
         * 
         * @param out
         *            the stream to compress.
         * @throws IOException
         *             if an error occurs with the {@link GZIPOutputStream}.
         */
        CompressionFilterServletOutputStream(final ServletOutputStream out) throws IOException {
            this.out = out;
            reset();
        }

        /** @see ServletOutputStream * */
        public void close() throws IOException {
            gzip.close();
        }

        /** @see ServletOutputStream * */
        public void flush() throws IOException {
            gzip.flush();
        }

        /** @see ServletOutputStream * */
        public void write(byte[] b) throws IOException {
            this.write(b, 0, b.length);
        }

        /** @see ServletOutputStream * */
        public void write(byte[] b, int off, int len) throws IOException {
            gzip.write(b, off, len);
        }

        /** @see ServletOutputStream * */
        public void write(int b) throws IOException {
            gzip.write(b);
        }

        /**
         * Resets the stream.
         * 
         * @throws IOException
         *             if an I/O error occurs.
         */
        public void reset() throws IOException {
            gzip = new GZIPOutputStream(out);
        }
    }

    class CompressionFilterHttpServletResponse extends HttpServletResponseWrapper {
        /**
         * The wrapped or original response
         */
        private HttpServletResponse response;

        /**
         * A copy of the ServletOutputStream
         */
        private ServletOutputStream out;

        /**
         * The stream which gzips anything written to it.
         */
        private CompressionFilterServletOutputStream outputStream;

        /**
         * A copy of a writer
         */
        private PrintWriter writer;

        /**
         * Content length
         */
        private int contentLength;

        /**
         * Creates a new compressed response wrapping the specified HTTP response.
         * 
         * @param response
         *            the HTTP response to wrap.
         * @throws IOException
         *             if an I/O error occurs.
         */
        public CompressionFilterHttpServletResponse(final HttpServletResponse response) throws IOException {
            super(response);
            this.response = response;
            outputStream = new CompressionFilterServletOutputStream(response.getOutputStream());
        }

        /**
         * Ignore attempts to set the content length since the actual content length will be determined by the GZIP compression.
         * 
         * @param len
         *            the content length
         */
        public void setContentLength(int len) {
            contentLength = len;
        }

        /**
         * Retrieve the output stream which may be used to write out binary type data.
         * 
         * @return The output stream
         * @throws IOException
         *             if something goes wrong
         */
        public ServletOutputStream getOutputStream() throws IOException {
            if (null == out) {
                if (null != writer) {
                    throw new IllegalStateException("getWriter() has already been called on this response.");
                }
                out = outputStream;
            }
            return out;
        }

        /**
         * Retrieve a printwriter which may be used to write out text type output.
         * 
         * @return The writer
         * @throws IOException
         *             if something goes wrong while returning the writer
         */
        public PrintWriter getWriter() throws IOException {
            if (null == writer) {
                if (null != out) {
                    throw new IllegalStateException("getOutputStream() has " + "already been called on this response.");
                }
                writer = new PrintWriter(outputStream);
            }
            return writer;
        }

        /**
         * Flushes the buffers of the writer or output stream.
         */
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if (out != null) {
                out.flush();
            }
        }

        /**
         * Resets the stream
         */
        public void reset() {
            super.reset();
            try {
                outputStream.reset();
            } catch (final IOException caught) {
                throw new UncheckedIOException(caught);
            }
        }

        /**
         * Resets the out going buffer
         */
        public void resetBuffer() {
            super.resetBuffer();
            try {
                outputStream.reset();
            } catch (final IOException caught) {
                throw new UncheckedIOException(caught);
            }
        }

        /**
         * Finishes writing the compressed data to the output stream. Note: this closes the underlying output stream.
         * 
         * @throws IOException
         *             if an I/O error occurs.
         */
        void finish() throws IOException {
            outputStream.close();
        }
    }
}