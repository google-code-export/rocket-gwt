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
package rocket.remoting.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;
import rocket.util.server.IoHelper;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;

/**
 * This servlet provides a mechanism to write objects to the client aka COMET.
 * Sub-classes need to implement {@link #queryObjectSource()} which may block
 * for a short period of time to check if a new object should be streamed.
 * 
 * This servlet uses two strategies to determine when a connect should be
 * dropped,
 * <ul>
 * <li>it has been opened too long {@link #connectionTimeout}</li>
 * <li>too many bytes have been written (the hidden iframe becomes too long}{@link #maximumBytesWritten}</li>
 * </ul>
 * 
 * To change the serialization policy override the
 * {@link #createSerializationPolicy()} method. Refer to GWT doco to learn more
 * about serialization policies.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class CometServerServlet extends HttpServlet {

	protected CometServerServlet() {
		this.setSerializationPolicy(this.createSerializationPolicy());
	}

	/**
	 * This reads and saves the maximumBytesWritten and connectionTimeout init
	 * parameters.
	 */
	public void init() throws ServletException {
		// read and save the maximumBytesWritten init parameter...
		final String maximumBytesWritten = this.getInitParameter(Constants.MAXIMUM_BYTES_WRITTEN_INIT_PARAMETER);
		if (StringHelper.isNullOrEmpty(maximumBytesWritten)) {
			throw new ServletException("The servlet \"" + this.getServletName() + "\" init parameter \""
					+ Constants.MAXIMUM_BYTES_WRITTEN_INIT_PARAMETER + "\" is required and missing.");
		}
		try {
			this.setMaximumBytesWritten(Integer.parseInt(maximumBytesWritten));
		} catch (final Exception caught) {
			throw new ServletException("The servlet \"" + this.getServletName() + "\" init parameter \""
					+ Constants.MAXIMUM_BYTES_WRITTEN_INIT_PARAMETER + "\" contains an invalid value \"" + maximumBytesWritten + "\".");
		}

		// read and save the connectionTimeout init parameter...
		final String connectionTimeout = this.getInitParameter(Constants.CONNECTION_TIME_OUT_INIT_PARAMETER);
		if (StringHelper.isNullOrEmpty(connectionTimeout)) {
			throw new ServletException("The servlet \"" + this.getServletName() + "\" init parameter \""
					+ Constants.CONNECTION_TIME_OUT_INIT_PARAMETER + "\" is required and missing.");
		}
		try {
			this.setConnectionTimeout(Integer.parseInt(connectionTimeout));
		} catch (final Exception caught) {
			throw new ServletException("The servlet \"" + this.getServletName() + "\" init parameter \""
					+ Constants.CONNECTION_TIME_OUT_INIT_PARAMETER + "\" contains an invalid value \"" + connectionTimeout + "\".");
		}
	}

	/**
	 * This servlet will drop the connection when more than this number of bytes
	 * is written.
	 */
	private int maximumBytesWritten;

	protected int getMaximumBytesWritten() {
		PrimitiveHelper.checkGreaterThan("field:maximumBytesWritten", 0, this.maximumBytesWritten);
		return this.maximumBytesWritten;
	}

	protected void setMaximumBytesWritten(final int maximumBytesWritten) {
		PrimitiveHelper.checkGreaterThan("parameter:maximumBytesWritten", 0, maximumBytesWritten);
		this.maximumBytesWritten = maximumBytesWritten;
	}

	/**
	 * This servlet will drop the connection when the connection has been open
	 * for more than this amount of time in milliseconds.
	 */
	private int connectionTimeout;

	protected int getConnectionTimeout() {
		PrimitiveHelper.checkGreaterThan("field:connectionTimeout", 0, this.connectionTimeout);
		return this.connectionTimeout;
	}

	protected void setConnectionTimeout(final int connectionTimeout) {
		PrimitiveHelper.checkGreaterThan("parameter:connectionTimeout", 0, connectionTimeout);
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * The SerializationPolicy which is used during serialization.
	 */
	private SerializationPolicy serializationPolicy;

	protected SerializationPolicy getSerializationPolicy() {
		ObjectHelper.checkNotNull("field:serializationPolicy", serializationPolicy);
		return this.serializationPolicy;
	}

	protected void setSerializationPolicy(final SerializationPolicy serializationPolicy) {
		ObjectHelper.checkNotNull("parameter:serializationPolicy", serializationPolicy);
		this.serializationPolicy = serializationPolicy;
	}

	/**
	 * Creates a default SerializationPolicy that doesnt complain about
	 * anything.
	 * 
	 * @return
	 */
	protected SerializationPolicy createSerializationPolicy() {
		return new SerializationPolicy() {
			public boolean shouldDeserializeFields(Class clazz) {
				throw new UnsupportedOperationException("SerializationPolicy.shouldDeserializeFields");
			}

			public boolean shouldSerializeFields(Class clazz) {
				return Object.class != clazz;
			}

			public void validateDeserialize(Class clazz) {
				throw new UnsupportedOperationException("SerializationPolicy.validateDeserialize");
			}

			public void validateSerialize(Class clazz) {
			}
		};
	}

	/**
	 * This abstract method must be implemented by sub-classes in which they
	 * check and possibly return any candidate object to be streamed.
	 * 
	 * Sub-classes may wait/block for short periods of time before retuning a
	 * result.
	 * 
	 * @return An object to be written or null if none exists.
	 */
	protected abstract Object queryObjectSource();

	/**
	 * Post requests are not supported by this servlet, this method responds
	 * with a METHOD NOT ALLOWED error code.
	 */
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/**
	 * Handles any get requests. This method enteres a loop which polls the
	 * {@link #queryObjectSource()} method to determine if more objects should
	 * be sent to the client. WHen the bytes written or connection open time
	 * threashholds have been reached the connection is dropped.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		perThreadRequest.set(request);
		perThreadResponse.set(response);
		ServletOutputStream output = null;

		long start = System.currentTimeMillis();
		int bytesWritten = 0;
		int byteWriteLimit = this.getMaximumBytesWritten();
		long timeOut = start + this.getConnectionTimeout();

		try {
			output = response.getOutputStream();
			response.setContentType(Constants.COMET_SERVER_RESPONSE_CONTENT_TYPE);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setBufferSize(512);

			final String before = this.getDocumentStartHtml();
			output.print(before);
			bytesWritten = bytesWritten + before.length();

			while (true) {
				// check if this connection should be dropped...
				if (bytesWritten > byteWriteLimit) {
					break;
				}
				if (System.currentTimeMillis() > timeOut) {
					break;
				}

				boolean isException = false;
				Object object = null;
				try {
					// check if there are any new objects to stream down to the
					// client...
					object = this.queryObjectSource();
					if (null == object) {
						continue;
					}
				} catch (final Throwable caught) {
					isException = true;

					object = caught;
					// not all exceptions are serializable...
					if (false == caught.getClass().getName().startsWith("java.lang")) {
						object = new RuntimeException(caught.getMessage());
					}
				}

				// serialize...
				final String serializedForm = this.serialize(object);

				// prepare payload...
				final String responsePayload = this.preparePayload(isException, serializedForm);

				// write the response payload
				output.print(responsePayload);

				bytesWritten = bytesWritten + responsePayload.length();

				// flush so the new objects are processed by the client...
				output.flush();
			}

		} finally {
			try {
				output.print(this.getDocumentEndHtml());
			} catch (final IOException ignored) {

			}
			IoHelper.closeIfNecessary(output);
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
	 * This method is called just before the server side socket is closed.
	 * 
	 * @return
	 */
	protected String getDocumentEndHtml() {
		return Constants.DOCUMENT_END_HTML;
	}

	/**
	 * Uses the GWT serialization sub-system to convert the given object into a
	 * String. This same object will be deserialized on the client using the GWT
	 * deserialization sub-system.
	 * 
	 * @param object
	 * @return
	 */
	protected String serialize(final Object object) {

		try {
			ServerSerializationStreamWriter streamWriter = new ServerSerializationStreamWriter(this.createSerializationPolicy());
			streamWriter.prepareToWrite();
			streamWriter.serializeValue(object, Object.class);
			return streamWriter.toString();
		} catch (final SerializationException serializationException) {
			throw new RuntimeException("Unable to serialize object, message: " + serializationException.getMessage());
		}
	}

	/**
	 * This step generates the script tag within embedded javascript that will
	 * written to the client and then executed. The client will need to unescape
	 * the encoded String prior to deserializing.
	 * 
	 * @param isException
	 * @param serializedForm
	 * @return
	 */
	protected String preparePayload(final boolean isException, final String serializedForm) {
		final String serializedForm0 = StringHelper.htmlEncode(serializedForm);

		return "<script>try{window.parent.__cometDispatch('" + (isException ? "{EX}" : "{OK}") + serializedForm0
				+ "');}catch(e){}</script>\n";
	}

	/**
	 * Gets the <code>HttpServletRequest</code> object for the current call.
	 * It is stored thread-locally so that simultaneous invocations can have
	 * different request objects.
	 */
	protected final HttpServletRequest getThreadLocalRequest() {
		return (HttpServletRequest) perThreadRequest.get();
	}

	/**
	 * Gets the <code>HttpServletResponse</code> object for the current call.
	 * It is stored thread-locally so that simultaneous invocations can have
	 * different response objects.
	 */
	protected final HttpServletResponse getThreadLocalResponse() {
		return (HttpServletResponse) perThreadResponse.get();
	}

	private final ThreadLocal perThreadRequest = new ThreadLocal();

	private final ThreadLocal perThreadResponse = new ThreadLocal();

}