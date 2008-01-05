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
package rocket.remoting.client.support.rpc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.SerializationFactory;
import rocket.util.client.Checker;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class is intended to be only used by the rpc generator as a bridge between the service interface method and a request to the server.
 * 
 * The generator should perform the following operations in this order.
 * <ol>
 * <li>set the objectOutputStream</li>
 * <li>set the interface name</li>
 * <li>set the method name</li>
 * <li>add method parameter types in order</li>
 * <li>add parameters in order</li>
 * <li>set the url</li>
 * <li>set the async callback</li>
 * <li>make the request</li>
 * </ol>
 * @author Miroslav Pokorny
 */
public class JavaRpcServiceMethodInvoker extends RpcServiceMethodInvoker implements RequestCallback {
	
	public JavaRpcServiceMethodInvoker(){
		super();
		this.setParameterTypes( this.createParameterTypes());
	}

	/**
	 * Holds the serialization factory which will be used to provide both the ObjectInputStream and ObjectOutputStreams.
	 */
	private SerializationFactory serializationFactory;

	protected SerializationFactory getSerializationFactory() {		
		Checker.notNull("field:serializationFactory", serializationFactory);
		return this.serializationFactory;
	}

	public void setSerializationFactory(final SerializationFactory serializationFactory) {
		Checker.notNull("parameter:serializationFactory", serializationFactory);
		this.serializationFactory = serializationFactory;
	}
	
	/**
	 * The ObjectOutputStream that will be serializing the entire rpc request, this includes the service method and
	 * parameters.
	 */
	private ObjectOutputStream objectOutputStream;
	
	protected ObjectOutputStream getObjectOutputStream(){
		if( false == this.hasObjectOutputStream() ){
			this.setObjectOutputStream( this.getSerializationFactory().createObjectOutputStream() );
		}
		
		return this.objectOutputStream;
	}
	
	protected boolean hasObjectOutputStream(){
		return null != this.objectOutputStream;
	}
	
	protected void setObjectOutputStream( final ObjectOutputStream objectOutputStream ){
		Checker.notNull( "parameter:objectOutputStream", objectOutputStream);
		this.objectOutputStream = objectOutputStream;
	}
	
	/**
	 * The interface name of the rpc service may only be set once at the beginning of building.
	 * @param interfaceName
	 */
	public void setInterfaceName(final String interfaceName ){
		this.getObjectOutputStream().writeObject( interfaceName );
	}

	/**
	 * The method name of the rpc may only be set once.
	 * @param methodName
	 */
	public void setMethodName( final String methodName ){
		this.getObjectOutputStream().writeObject( methodName );
	}
	
	/**
	 * Aggregates all parameter types until a parameter value is committed.
	 */
	private List parameterTypes;
	
	protected List getParameterTypes(){
		return this.parameterTypes;
	}
	protected void setParameterTypes( final List parameterTypes ){
		this.parameterTypes = parameterTypes;
	}
	protected List createParameterTypes(){
		return new ArrayList();
	}
	
	/**
	 * This flag keeps track of whether parameter types have already been written.
	 */
	private boolean parameterTypesWritten;
	
	protected boolean hasParameterTypesWritten(){
		return this.parameterTypesWritten;
	}
	protected void setParameterTypesWritten( final boolean parameterTypesWritten ){
		this.parameterTypesWritten = parameterTypesWritten;
	}
	     
	protected void commitParameterTypesIfNecessary(){
		if( false == this.hasParameterTypesWritten() ){
			this.setParameterTypesWritten( true );
			this.commitParameterTypes();
		}
	}
		
	protected void commitParameterTypes(){		
			final List parameterTypes = this.getParameterTypes();
			
			final ObjectOutputStream outputStream = this.getObjectOutputStream();
			outputStream.writeInt( parameterTypes.size() );
						
			final Iterator iterator = parameterTypes.iterator();			
			while( iterator.hasNext() ){
				outputStream.writeObject( iterator.next() );
			}
	}
	
	/**
	 * Parameter types must be added before parameter values are added.
	 * Upon adding a parameter parameter types will be committed to the underlying ObjectOutputStream once.
	 * @param parameterType
	 */
	public void addParameterType( final String parameterType ){
		Checker.falseValue( "parameterTypesWritten", this.hasParameterTypesWritten() );
		this.getParameterTypes().add( parameterType );
	}
	
	public void addParameter( final boolean booleanValue ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeBoolean(booleanValue);
	}
	
	public void addParameter( final byte byteValue ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeByte(byteValue);
	}
	
	public void addParameter( final short shortValue ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeShort(shortValue);
	}
	
	public void addParameter( final int intValue ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeInt(intValue);
	}
	
	public void addParameter( final long longValue ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeLong(longValue);
	}
	
	public void addParameter( final float floatValue ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeFloat(floatValue);
	}
	
	public void addParameter( final double doubleValue ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeDouble(doubleValue);
	}
	
	public void addParameter( final char charValue ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeChar(charValue);
	}
	
	public void addParameter( final Object object ){
		this.commitParameterTypesIfNecessary();
		this.getObjectOutputStream().writeObject( object);
	}
	
	/**
	 * Factory method which creates a new RequestBuilder.
	 *
	 * @return A new RequestBuilder
	 */
	protected RequestBuilder createRequestBuilder() {
		return new RequestBuilder(this.getRequestMethod(), this.getUrl());
	}
	
	/**
	 * Java rpcs use ajax and are always POSTS...
	 */
	protected Method getRequestMethod(){
		return RequestBuilder.POST;
	}
	
	/**
	 * The post data will contain the serialized form of the rpc request.
	 */
	protected String getRequestData(){
		commitParameterTypesIfNecessary();
		return this.getObjectOutputStream().getText();
	}
	
	protected void setHeaders(final RequestBuilder request) {
		request.setHeader(Constants.CONTENT_TYPE_HEADER, Constants.POST_CONTENT_TYPE);
	}

	protected void onSuccessfulResponse(final Request request, final Response response){
		while (true) {
			
			final int status = response.getStatusCode();
			if (Constants.HTTP_RESPONSE_OK != status) {
				this.onFailedResponse(request, response);
				break;
			}
			final AsyncCallback callback = this.getCallback();
			
			try {
				final SerializationFactory serializationFactory = this.getSerializationFactory();
				final String stream = response.getText();
				final ObjectInputStream objectInputStream = serializationFactory.createObjectInputStream(stream);
				final boolean exceptionWasThrown = objectInputStream.readBoolean();
				final Object result = objectInputStream.readObject();

				if (exceptionWasThrown) {
					callback.onFailure((Throwable) result);
					break;
				}

				callback.onSuccess(result);
				
			} catch (final Throwable throwable) {
				callback.onFailure(throwable);
			}
			break;
		}
	}
}
