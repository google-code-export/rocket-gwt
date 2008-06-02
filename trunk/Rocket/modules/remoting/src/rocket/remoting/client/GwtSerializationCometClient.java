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

import rocket.util.client.Checker;
import rocket.util.client.Utilities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.HasSerializer;
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
public abstract class GwtSerializationCometClient extends CometClient {

	public GwtSerializationCometClient() {
		super();
	}

	/**
	 * This method takes care of unmarshalling the given object and then calling the appropriate callback methods.
	 */
	public void dispatch(final String serializedForm) {
		final CometCallback callback = this.getCallback();

		// if this caught is ever set the onFailure method of the callback is invoked.
		Throwable caught = null;
		while (true) {
			try {
				String serializedForm0 = Utilities.htmlDecode(serializedForm);
				final SerializationStreamReader reader = this.createSerializationStreamReader(serializedForm0);
				final int command = reader.readInt();
				if (command == CometConstants.TERMINATE_COMET_SESSION) {
					this.stop();
					callback.onTerminate();
					break;
				}

				if (command == CometConstants.OBJECT_PAYLOAD) {
					final Object object = reader.readObject();
					callback.onPayload(object);
					break;
				}

				if (command == CometConstants.EXCEPTION_PAYLOAD) {
					caught = (Throwable) reader.readObject();
					break;
				}

				this.onUnknownCommand( command );
				break;

			} catch (final SerializationException serializationException) {
				caught = serializationException;
				break;
			} catch (final RuntimeException runtimeException) {
				caught = runtimeException;
				break;
			}
		}

		if (null != caught) {
			callback.onFailure(new CometException(caught));
		}
	}

	/**
	 * This factory method creates a reader which may be used to deserialize incoming payloads.
	 * @param serializedForm
	 * @return
	 * @throws SerializationException
	 */
	protected SerializationStreamReader createSerializationStreamReader(final String serializedForm) throws SerializationException {
		Checker.notEmpty("parameter:serializedForm", serializedForm);

		final Object proxy = this.createGwtRpcProxy();
		if (false == GWT.isScript() && false == (proxy instanceof HasSerializer)) {
			this.throwRocketJarClasspathProblem();
		}

		final HasSerializer serializerHost = (HasSerializer) proxy;
		final Serializer serializer = serializerHost.getSerializer();

		final ClientSerializationStreamReader deserializer = new ClientSerializationStreamReader(serializer);
		deserializer.prepareToRead(serializedForm);
		return deserializer;
	}

	protected void throwRocketJarClasspathProblem() {
		throw new CometException(
				"The rocket.jar appears not to be in front of gwt-user.jar which has resulted in the standard ProxyCreator to be used. Fix the problem by placing rocket.jar in front of gwt-user.jar and try again.");
	}
	
	/**
	 * This method will be implemented by the generator to return a hacked RpcProxy which has a method available to retrieve the deserializer.
	 * @return The rpc proxy
	 */
	protected abstract Object createGwtRpcProxy();
}