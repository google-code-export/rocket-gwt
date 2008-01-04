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
package rocket.remoting.client;

import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * There should only ever be one instance of this class which is used to receive
 * streamed objects from a server.
 * 
 * <h6>Gotchas</h6>
 * <ul>
 * <li> When compiling/translated to javascript the Rocket.jar must be included
 * in the classpath before any google classes so that the custom ProxyGenerator
 * is used instead of the regular class. </li>
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class GwtSerializationCometClient extends CometClient{

	public GwtSerializationCometClient() {
		super();
	}

	/**
	 * This method takes care of unmarshalling the given object and then calling the appropriate callback method.
	 */
	public void dispatch(final String serializedForm){
		final AsyncCallback callback = this.getCallback();
		
		Throwable caught = null;
		try{
			final String serializedForm0 = StringHelper.htmlDecode(serializedForm);
			final boolean failed = serializedForm0.startsWith("{EX}");
			final Object object = deserialize(serializedForm0.substring(4));
		
			if (failed) {
				caught = (Throwable) object;
			} 
			callback.onSuccess(object);
		
		} catch ( final SerializationException serializationException ){
			caught = serializationException;
		} catch ( final RuntimeException runtimeException ){
			caught = runtimeException;
		}
		
		if( null != caught ){
			callback.onFailure( new CometException( caught ));
		}
	}

	/**
	 * Uses a hacked GwtRpc Proxy to deserialize the serialized form of the comet payload.
	 * 
	 * @param serializedForm A string containing the serialized object graph
	 * @return The deserialized object
	 * @throws SerializationException This exception is thrown by the Gwt deserialization process if something goes wrong.
	 */
	protected Object deserialize(final String serializedForm) throws SerializationException {
		StringHelper.checkNotEmpty("parameter:serializedForm", serializedForm);

		final Object proxy = this.createGwtRpcProxy();
		if (false == GWT.isScript() && false == (proxy instanceof HasSerializer)) {
			this.throwRocketJarClasspathProblem();
		}

		final HasSerializer serializerHost = (HasSerializer) proxy;
		final Serializer serializer = serializerHost.getSerializer();

		final ClientSerializationStreamReader deserializer = new ClientSerializationStreamReader(serializer);
		deserializer.prepareToRead(serializedForm);
		return deserializer.readObject();
	}
	
	protected void throwRocketJarClasspathProblem(){
		throw new CometException( "The rocket.jar appears not to be in front of gwt-user.jar which has resulted in the standard ProxyCreator to be used. Fix the problem by placing rocket.jar in front of gwt-user.jar and try again.");
	}

	/**
	 * This method will be implemented by the generator to return a hacked RpcProxy.
	 * @return The rpc proxy
	 */
	protected abstract Object createGwtRpcProxy();
}