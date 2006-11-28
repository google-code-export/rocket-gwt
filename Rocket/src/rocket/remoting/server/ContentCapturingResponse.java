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
package rocket.remoting.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import rocket.remoting.client.Headers;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;
import rocket.util.server.ObjectHelper;

/**
 * This response captures all bytes or characters written to it. This is achieved by returning special Writers/OutputStream. Cookies are not
 * captured and are added to the wrapped response, all other headers are simply captured in a map.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ContentCapturingResponse extends HttpServletResponseWrapper {

    public ContentCapturingResponse(final HttpServletResponse response) {
        super(response);

        this.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("OK");
        this.setHeaders(new Headers());
    }

    public byte[] toByteArray() {
        return this.getByteArrayServletOutputStream().toByteArray();
    }

    /**
     * This map is used to track headers that are added /set.
     */
    private Headers headers;

    protected Headers getHeaders() {
        ObjectHelper.checkNotNull("field:headers", headers);
        return headers;
    }

    protected void setHeaders(final Headers headers) {
        ObjectHelper.checkNotNull("parameters:headers", headers);
        this.headers = headers;
    }

    public void addHeader(final String name, final String value) {
        this.getHeaders().add(name, value);
    }

    public void addIntHeader(final String name, final int intValue) {
        this.addHeader(name, String.valueOf(intValue));
    }

    public boolean containsHeader(final String name) {
        return this.getHeaders().contains(name);
    }

    /**
     * TODO not implemented properly.
     */
    public void addDateHeader(final String name, final long date) {
        this.addHeader(name, WebHelper.toString(date));
    }

    public void setDateHeader(final String name, final long date) {
        this.getHeaders().add(name, WebHelper.toString(date));
    }

    public void setHeader(final String name, final String value) {
        this.getHeaders().add(name, value);
    }

    public void setIntHeader(final String name, final int intValue) {
        this.getHeaders().add(name, String.valueOf(intValue));
    }

    private int status;

    public int getStatus() {
        return this.status;
    }

    private String message;

    public String getMessage() {
        StringHelper.checkNotEmpty("field:message", message);
        return message;
    }

    public void setMessage(final String message) {
        StringHelper.checkNotEmpty("parameter:message", message);
        this.message = message;
    }

    public void setStatus(final int code, final String message) {
        super.setStatus(code, message);
    }

    public void setStatus(final int status) {
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:status", status, 0);
        this.status = status;
    }

    public void sendError(final int code, final String message) throws IOException {
        this.setStatus(code);
        this.setMessage(message);
        super.sendError(code, message);
    }

    public void setError(final int code) {
        this.setStatus(code);
    }

    public void flushBuffer() throws IOException {
        if (this.hasWriter()) {
            this.getWriter().flush();
        }

        if (this.hasByteArrayServletOutputStream()) {
            this.getByteArrayServletOutputStream().flush();
        }
    }

    public void resetBuffer() {
        if (this.hasByteArrayServletOutputStream()) {
            this.getByteArrayServletOutputStream().reset();
        }
    }

    public void reset() {
        this.resetBuffer();
        this.getHeaders().clear();
    }

    public boolean isCommitted() {
        return false == this.hasByteArrayServletOutputStream() ? false : this.getByteArrayServletOutputStream()
                .isCommitted();
    }

    private int bufferSize;

    public int getBufferSize() {
        PrimitiveHelper.checkGreaterThan("field:bufferSize", bufferSize, 0);
        return bufferSize;
    }

    public void setBufferSize(final int bufferSize) {
        PrimitiveHelper.checkGreaterThan("parameter:bufferSize", bufferSize, 0);
        this.bufferSize = bufferSize;
    }

    /**
     * This flag keeps track of whether the ServletOutputStream or Writer was given.
     */
    private boolean outputStreamGiven;

    protected boolean hasOutputStreamGiven() {
        return this.outputStreamGiven;
    }

    protected void setHasOutputStreamGiven(final boolean outputStreamGiven) {
        this.outputStreamGiven = outputStreamGiven;
    }

    public ServletOutputStream getOutputStream() {
        if (this.hasWriter()) {
            throw new IllegalStateException("getWriter() has already been called.");
        }

        final ServletOutputStream outputStream = this.getByteArrayServletOutputStream();
        this.setHasOutputStreamGiven(true);
        return outputStream;
    }

    private ByteArrayServletOutputStream byteArrayServletOutputStream;

    public ByteArrayServletOutputStream getByteArrayServletOutputStream() {
        if (false == this.hasByteArrayServletOutputStream()) {
            this.createByteArrayServletOutputStream();
        }

        return byteArrayServletOutputStream;
    }

    protected boolean hasByteArrayServletOutputStream() {
        return this.byteArrayServletOutputStream != null;
    }

    protected void setByteArrayServletOutputStream(final ByteArrayServletOutputStream byteArrayServletOutputStream) {
        ObjectHelper.checkNotNull("parameter:byteArrayServletOutputStream", byteArrayServletOutputStream);
        if (this.hasByteArrayServletOutputStream()) {
            SystemHelper.handleAssertFailure("parameter:byteArrayServletOutputStream",
                    "The field:outputStream has previously been set.");
        }
        this.byteArrayServletOutputStream = byteArrayServletOutputStream;
    }

    protected void createByteArrayServletOutputStream() {
        final int bufferSize = this.getBufferSize();
        final ByteArrayServletOutputStream outputStream = new ByteArrayServletOutputStream(bufferSize);

        this.setByteArrayServletOutputStream(outputStream);
    }

    /**
     * This field is lazy loaded and is initialized when a call to getWriter is made.
     */
    private PrintWriter writer;

    public PrintWriter getWriter() {
        if (this.hasOutputStreamGiven()) {
            throw new IllegalStateException("getOutputStream() has already been called");
        }
        if (false == this.hasWriter()) {
            this.createWriter();
        }
        return this.writer;
    }

    protected boolean hasWriter() {
        return this.writer != null;
    }

    protected void setWriter(final PrintWriter writer) {
        ObjectHelper.checkNotNull("parameter:writer", writer);
        if (this.hasWriter()) {
            SystemHelper.handleAssertFailure("parameter:writer", "The field:writer has previously been set.");
        }
        this.writer = writer;
    }

    protected void createWriter() {
        final PrintWriter writer = new PrintWriter(this.getByteArrayServletOutputStream());
        this.setWriter(writer);
    }

    public String toString() {
        return super.toString() + ", byteArrayServletOutputStream: " + byteArrayServletOutputStream + ", writer: "
                + writer;
    }
}
