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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.remoting.client.CometConstants;
import rocket.util.client.Checker;
import rocket.util.client.Tester;
import rocket.util.client.Utilities;
import rocket.util.server.InputOutput;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;

/**
 * This servlet provides a mechanism to write objects to the client aka COMET.
 * Sub-classes need to implement {@link #poll()} which may and should block
 * for a short period of time to check if a new object should be streamed.
 * 
 * This servlet uses two strategies to determine when a connect should be
 * dropped,
 * <ul>
 * <li>it has been opened too long {@link #connectionTimeout}</li>
 * <li>too many bytes have been written (the hidden iframe becomes too long}{@link #maximumBytesWritten}</li>
 * </ul>
 * 
 * To change the serialization policy override the {@link #createSerializationPolicy()} method. Refer to GWT doco to learn more
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
		if (Tester.isNullOrEmpty(maximumBytesWritten)) {
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
		if (Tester.isNullOrEmpty(connectionTimeout)) {
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
	 * The SerializationPolicy which is used during serialization.
	 */
	private SerializationPolicy serializationPolicy;

	protected SerializationPolicy getSerializationPolicy() {
		Checker.notNull("field:serializationPolicy", serializationPolicy);
		return this.serializationPolicy;
	}

	protected void setSerializationPolicy(final SerializationPolicy serializationPolicy) {
		Checker.notNull("parameter:serializationPolicy", serializationPolicy);
		this.serializationPolicy = serializationPolicy;
	}

	/**
	 * Creates a default SerializationPolicy that doesnt complain when asked to serialize objects.
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
	 * Post requests are not supported by this servlet, this method responds
	 * with a METHOD NOT ALLOWED error code.
	 */
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/**
	 * The main method that
	 */
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		requests.set(request);
		responses.set(response);		
		
		ServletOutputStream outputStream = null;
		try {
			this.prepare(response);
			outputStream = response.getOutputStream();
			this.poller( outputStream );			
			
		} catch ( final IOException ioException ){
			this.log( ioException.getMessage(), ioException );
		} catch ( final ServletException servletException ){
			this.log( servletException.getMessage(), servletException );
		} 
	}
	
	/**
	 * Sets a number of headers and also sets the buffer size for this particular comet session HTTP connection.
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void prepare( final HttpServletResponse response ) throws IOException, ServletException{
		response.setContentType(Constants.COMET_SERVER_RESPONSE_CONTENT_TYPE);
		response.setStatus( Constants.COMET_SERVLET_STATUS_CODE );
		response.setBufferSize( Constants.COMET_SERVER_BUFFER_SIZE );
	}
	
	/**
	 * This method is responsible for calling the {@link #poll} method and also includes the logic to push objects and drop connections that have written too many bytes or
	 * been open too long.
	 * @param servletOutputStream
	 * @throws IOException
	 * @throws ServletException
	 * 
	 * FIXME seems to be buffering not sure why...
	 */
	protected void poller( final ServletOutputStream servletOutputStream ) throws IOException, ServletException{
		Checker.notNull("parameter:servletOutputStream", servletOutputStream );
		
		int bytesWritten = 0;
		final int byteWriteLimit = this.getMaximumBytesWritten();
		
		long connectedAt = System.currentTimeMillis();
		final long connectionTimeout = this.getConnectionTimeout();
		
		try {
			final String before = this.getDocumentStartHtml();
			servletOutputStream.println(before);
			bytesWritten = bytesWritten + before.length() * 2;

			boolean terminated = false;
			
			while (true) {
				if( terminated ){
					break;
				}
				
				// have too many bytes been written ?
				if (bytesWritten > byteWriteLimit) {
					this.onByteWriteLimitExceeded( bytesWritten );
					break;
				}
				// has the connection been open too long ???
				final long openDuration = System.currentTimeMillis() - connectedAt; 
				if ( openDuration >= connectionTimeout) {
					this.onConnectionOpenTooLong( openDuration );
					break;
				}
				
				// hack use a thread local to aggregates messages.
				final List messagesList = new ArrayList();
				this.messages.set( messagesList );
				try{
					// the sub class can push as many messages aka objects and terminates as they wish.
					this.poll();
				} catch ( final Throwable caught ){
					this.push(caught);
				}								
				
				// write out pending writes...
				final Iterator messagesIterator = messagesList.iterator();
				while( messagesIterator.hasNext() ){
					final Message payload = (Message)messagesIterator.next();
					final int command = payload.getCommand();
					final Object object = payload.getObject();
					
					final String serialized = this.serialize( command, object);
					final String escaped = this.preparePayload(serialized);
					servletOutputStream.print( escaped );
					servletOutputStream.println();
					
					// guess that 2 bytes were written for every char...
					bytesWritten = bytesWritten + escaped.length() * 2;
					
					if( payload instanceof Terminate ){
						terminated = true;
					}
				}
				
				this.flush(servletOutputStream);				
			}

		} finally {
			try {
				servletOutputStream.print(this.getDocumentEndHtml());
				this.flush(servletOutputStream);
			} catch (final IOException ignored) {
			}			
			InputOutput.closeIfNecessary(servletOutputStream);
		}
	}
	
	/**
	 * Sub classes must override this method to include any object pushing as well as a delay before returning.
	 */
	abstract protected void poll();
	
	/**
	 * Sub classes may use this to push a single object to the client.
	 * @param object
	 */
	protected void push( final Object object ){
		this.addMessage( new ObjectPayload( object ));
	}
	
	protected void push( final Throwable throwable ){		
		this.addMessage( new ExceptionPayload( throwable ));
	}
	
	/**
	 * Sub classes may call this method to send a terminate message to the client.
	 */
	protected void terminate(){
		this.addMessage( new Terminate() );
	}
	
	/**
	 * Queues a message to be sent to the client.
	 * @param message A new message which may not be null.
	 */
	protected void addMessage( final Message message ){
		Checker.notNull("parameter:message", message );
		
		final List messages = (List) this.messages.get();
		if( false == messages.isEmpty() ){
			final int lastIndex = messages.size() - 1;
			final Object last = messages.get( lastIndex );
			if( last instanceof Terminate ){
				throw new IllegalStateException( "New items cannot be pushed after a session has been terminated.");
			}
		}	
		
		// save
		messages.add( message );
	}
	
	protected void flush( final ServletOutputStream servletOutputStream ) throws IOException{
		servletOutputStream.flush();
		this.getResponse().flushBuffer();
	}
	
	/**
	 * Aggregates all pending write operations for a given thread.
	 */
	private ThreadLocal messages = new ThreadLocal();
	
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
	 * @param command The command to send.
	 * @param object Its okay to push null objects.
	 * @return The serialized form of both the command and object.
	 */
	protected String serialize(final int command, final Object object) {
		try {
			ServerSerializationStreamWriter streamWriter = new ServerSerializationStreamWriter(this.createSerializationPolicy());
			streamWriter.prepareToWrite();
			streamWriter.writeInt( command );
			streamWriter.writeObject(object );
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
	 * @param serializedForm
	 * @return
	 */
	protected String preparePayload( final String serializedForm) {
		final String escaped = Utilities.htmlEncode(serializedForm);

		return Constants.SCRIPT_TAG_OPEN + escaped + Constants.SCRIPT_TAG_CLOSE; 
	}
	
	/**
	 * This method is called when the server drops the connection because too many bytes have been written
	 * @param byteWriteCount The total number of bytes written
	 */
	protected void onByteWriteLimitExceeded( final int byteWriteCount ){		
	}
	/**
	 * This method is invoked whenever the server drops the connection because it has been open for too long.
	 * @param milliseconds The total time the connection was open.
	 */
	protected void onConnectionOpenTooLong( final long milliseconds ){		
	}	
	
	/**
	 * Gets the <code>HttpServletRequest</code> object for the current call.
	 * It is stored thread-locally so that simultaneous invocations can have
	 * different request objects.
	 */
	protected final HttpServletRequest getRequest() {
		return (HttpServletRequest) requests.get();
	}

	/**
	 * Gets the <code>HttpServletResponse</code> object for the current call.
	 * It is stored thread-locally so that simultaneous invocations can have
	 * different response objects.
	 */
	protected final HttpServletResponse getResponse() {
		return (HttpServletResponse) responses.get();
	}

	/**
	 * A threadlocal is used as a hack to keep track of the current request and response objects without
	 * passing them as method parameters. 
	 */
	private final ThreadLocal requests = new ThreadLocal();

	private final ThreadLocal responses = new ThreadLocal();

	/**
	 * An interface that unifies all the possible server messages to be sent to the client.
	 */
	static interface Message{
		int getCommand();
		Object getObject();
	}
	
	/**
	 * Instances of this class represent a request from the server to terminates an active comet session.
	 */
	static private class Terminate implements Message{
		public int getCommand(){
			return CometConstants.TERMINATE_COMET_SESSION;
		}
		public Object getObject(){
			return null;
		}
	}
	/**
	 * Instances represent a single object being pushed from the server to the client.
	 */
	static private class ObjectPayload implements Message{
		
		ObjectPayload( final Object object ){
			this.setObject(object);
		}
		
		public int getCommand(){
			return CometConstants.OBJECT_PAYLOAD;
		}
		Object object;
		public Object getObject(){
			return object;
		}
		void setObject( final Object object ){
			this.object = object;
		}
	}
	/**
	 * Instances represent a single exception being pushed from the server to the client.
	 */
	static private class ExceptionPayload implements Message{
		
		ExceptionPayload( final Throwable throwable ){
			this.setThrowable(throwable);
		}
		
		public int getCommand(){
			return CometConstants.EXCEPTION_PAYLOAD;
		}
		public Object getObject(){
			return this.getThrowable();
		}
		Throwable throwable;
		Throwable getThrowable(){
			return this.throwable;
		}
		void setThrowable( final Throwable throwable ){
			this.throwable = throwable;
		}
	}
}