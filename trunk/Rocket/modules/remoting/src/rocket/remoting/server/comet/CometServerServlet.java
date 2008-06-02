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
package rocket.remoting.server.comet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.util.client.Checker;
import rocket.util.client.Tester;
import rocket.util.client.Utilities;
import rocket.util.server.InputOutput;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;

/**
 * This servlet forms the basis of any comet client. The {@link
 * 
 * @author Miroslav Pokorny
 */
public abstract class CometServerServlet extends HttpServlet {

	/**
	 * This reads and saves the maximumBytesWritten and connectionTimeout init
	 * parameters.
	 */
	@Override
	public void init() throws ServletException {
		final int maximumBytesWritten = this.getPositiveNumberInitParameter(Constants.MAXIMUM_BYTES_WRITTEN_INIT_PARAMETER);
		this.setMaximumBytesWritten(maximumBytesWritten);

		final int connectionTimeout = this.getPositiveNumberInitParameter(Constants.CONNECTION_TIME_OUT_INIT_PARAMETER);
		this.setConnectionTimeout(connectionTimeout);
	}

	/**
	 * Convenience method that fetches the named init parameter and then
	 * converts it into a number.
	 * 
	 * @param name
	 * @return
	 * @throws ServletException
	 */
	protected int getPositiveNumberInitParameter(final String name) throws ServletException {
		final String value = this.getInitParameter(name);
		if (Tester.isNullOrEmpty(value)) {
			this.throwInitParameterIsMissing(name);
		}

		int number = 0;
		try {
			number = Integer.parseInt(value);

			if (number < 0) {
				this.throwInitParameterDoesntContainAPositiveNumber(name, value);
			}

		} catch (final NumberFormatException badNumber) {
			this.throwInitParameterDoesntContainAPositiveNumber(name, value);
		}

		return number;
	}

	protected void throwInitParameterIsMissing(final String name) throws ServletException {
		this.throwServletException("The required init parameter \"" + name + "\" is missing,");
	}

	protected void throwInitParameterDoesntContainAPositiveNumber(final String name, final String value) throws ServletException {
		this.throwServletException("The required init parameter \"" + name + "\" with a value of \"" + value + "\" doesnt contain a positive number,");
	}

	protected void throwServletException(final String message) throws ServletException {
		throw new ServletException(message + " from the servlet \"" + this.getServletName() + "\".");
	}

	/**
	 * This servlet will drop the connection when more than this number of bytes
	 * is written.
	 */
	private int maximumBytesWritten;

	protected int getMaximumBytesWritten() {
		Checker.greaterThan("field:maximumBytesWritten", 0, this.maximumBytesWritten);
		return this.maximumBytesWritten;
	}

	protected void setMaximumBytesWritten(final int maximumBytesWritten) {
		Checker.greaterThan("parameter:maximumBytesWritten", 0, maximumBytesWritten);
		this.maximumBytesWritten = maximumBytesWritten;
	}

	/**
	 * This servlet will drop the connection when the connection has been open
	 * for more than this amount of time in milliseconds.
	 */
	private int connectionTimeout;

	protected int getConnectionTimeout() {
		Checker.greaterThan("field:connectionTimeout", 0, this.connectionTimeout);
		return this.connectionTimeout;
	}

	protected void setConnectionTimeout(final int connectionTimeout) {
		Checker.greaterThan("parameter:connectionTimeout", 0, connectionTimeout);
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * Post requests are not supported by this servlet, this method responds
	 * with a METHOD NOT ALLOWED error code.
	 */
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/**
	 * The main method that takes care of initiating a comet session.
	 */
	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		try {
			this.setHttpServletRequest(request);
			this.setHttpServletResponse(response);
			this.poller(response);
		} catch (final IOException ioException) {
			ioException.printStackTrace();
			this.log(ioException.getMessage(), ioException);
		} catch (final ServletException servletException) {
			servletException.printStackTrace();
			this.log(servletException.getMessage(), servletException);
		} finally {
			this.clearHttpServletRequest();
			this.clearHttpServletResponse();
		}
	}

	/**
	 * This method is responsible for calling the {@link #poll} method and also
	 * includes the logic to push objects and drop connections that have written
	 * too many bytes or been open too long.
	 * 
	 * @param servletOutputStream
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void poller(final HttpServletResponse response) throws IOException, ServletException {
		Checker.notNull("parameter:response", response);

		int bytesWritten = 0;
		final int byteWriteLimit = this.getMaximumBytesWritten();

		long connectedAt = System.currentTimeMillis();
		final long connectionTimeout = this.getConnectionTimeout();

		ServletOutputStream servletOutputStream = null;

		try {
			servletOutputStream = response.getOutputStream();
			final String before = this.getDocumentStartHtml();
			servletOutputStream.println(before);
			bytesWritten = bytesWritten + before.length() * 2;

			final CometConnectionImpl cometConnection = new CometConnectionImpl();

			while (true) {
				if (cometConnection.isTerminated()) {
					break;
				}

				// have too many bytes been written ?
				if (bytesWritten > byteWriteLimit) {
					this.onByteWriteLimitExceeded(bytesWritten);
					break;
				}
				// has the connection been open too long ???
				final long openDuration = System.currentTimeMillis() - connectedAt;
				if (openDuration >= connectionTimeout) {
					this.onConnectionOpenTooLong(openDuration);
					break;
				}

				this.poll(cometConnection);

				// write out any new messages.
				final Iterator messages = cometConnection.getMessages().iterator();
				while (messages.hasNext()) {
					final Message message = (Message) messages.next();

					final int command = message.getCommand();
					final Object object = message.getObject();

					final String serialized = this.serialize(command, object);
					final String escaped = this.preparePayload(serialized);
					servletOutputStream.print(escaped);
					servletOutputStream.println();

					// guess that 2 bytes were written for every char...
					bytesWritten = bytesWritten + escaped.length() * 2;
				}

				this.flush(response);
			}

		} finally {
			try {
				servletOutputStream.print(this.getDocumentEndHtml());
				this.flush(response);

			} catch (final IOException ignored) {
			}
			InputOutput.closeIfNecessary(servletOutputStream);
		}
	}

	/**
	 * This method is called before any payloads are written.
	 * 
	 * @return
	 */
	protected String getDocumentStartHtml() {
		return Constants.DOCUMENT_START_HTML;
	}

	/**
	 * Uses the GWT serialization sub-system to convert the given object into a
	 * String. This same object will be deserialized on the client using the GWT
	 * deserialization sub-system.
	 * 
	 * @param command
	 *            The command to send.
	 * @param object
	 *            Its okay to push null objects.
	 * @return The serialized form of both the command and object.
	 */
	protected String serialize(final int command, final Object object) {
		try {
			ServerSerializationStreamWriter streamWriter = new ServerSerializationStreamWriter(this.createSerializationPolicy());
			streamWriter.prepareToWrite();
			streamWriter.writeInt(command);
			streamWriter.writeObject(object);
			return streamWriter.toString();
		} catch (final SerializationException serializationException) {
			throw new RuntimeException("Unable to serialize object, message: " + serializationException.getMessage());
		}
	}

	/**
	 * Creates a default SerializationPolicy that doesnt complain when asked to
	 * serialize objects.
	 * 
	 * @return
	 */
	protected SerializationPolicy createSerializationPolicy() {
		return new SerializationPolicy() {
			public boolean shouldDeserializeFields(final Class clazz) {
				throw new UnsupportedOperationException("shouldDeserializeFields");
			}

			public boolean shouldSerializeFields(final Class clazz) {
				return Object.class != clazz;
			}

			public void validateDeserialize(final Class clazz) {
				throw new UnsupportedOperationException("validateDeserialize");
			}

			public void validateSerialize(final Class clazz) {
			}
		};
	}

	/**
	 * This step generates the script tag within embedded javascript that will
	 * written to the client and then executed. The client will need to unescape
	 * the encoded String prior to deserializing.
	 * 
	 * @param serializedForm
	 * @return
	 */
	protected String preparePayload(final String serializedForm) {
		final String escaped = Utilities.htmlEncode(serializedForm);

		return Constants.SCRIPT_TAG_OPEN + escaped + Constants.SCRIPT_TAG_CLOSE;
	}

	/**
	 * This method is called just before the server side socket is closed.
	 * 
	 * @return
	 */
	protected String getDocumentEndHtml() {
		return Constants.DOCUMENT_END_HTML;
	}

	protected void flush(final HttpServletResponse response) throws IOException {
		Checker.notNull("parameter:response", response);

		response.getOutputStream().flush();
		response.flushBuffer();
	}

	/**
	 * Sub classes must override this method to include any object pushing as
	 * well as a delay before returning.
	 */
	abstract protected void poll(CometConnection cometConnection);

	/**
	 * This method is called when the server drops the connection because too
	 * many bytes have been written
	 * 
	 * @param byteWriteCount
	 *            The total number of bytes written
	 */
	protected void onByteWriteLimitExceeded(final int byteWriteCount) {
	}

	/**
	 * This method is invoked whenever the server drops the connection because
	 * it has been open for too long.
	 * 
	 * @param milliseconds
	 *            The total time the connection was open.
	 */
	protected void onConnectionOpenTooLong(final long milliseconds) {
	}
	
	
	final static ThreadLocal httpServletRequests = new ThreadLocal();
	
	public HttpServletRequest getHttpServletRequest(){
		return (HttpServletRequest) CometServerServlet.httpServletRequests.get();
	}
	
	void setHttpServletRequest( final HttpServletRequest httpServletRequests ){
		CometServerServlet.httpServletRequests.set(httpServletRequests);
	}
	
	void clearHttpServletRequest(){
		CometServerServlet.httpServletRequests.remove();
	}
	
	final static ThreadLocal httpServletResponses = new ThreadLocal();
	
	public HttpServletResponse getHttpServletResponse(){
		return (HttpServletResponse) CometServerServlet.httpServletResponses.get();
	}
	
	void setHttpServletResponse( final HttpServletResponse httpServletResponses ){
		CometServerServlet.httpServletResponses.set(httpServletResponses);
	}
	
	void clearHttpServletResponse(){
		CometServerServlet.httpServletResponses.remove();
	}
}
