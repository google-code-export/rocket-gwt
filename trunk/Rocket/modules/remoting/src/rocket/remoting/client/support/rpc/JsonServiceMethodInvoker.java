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

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The general base class for all json service stubs. Two sub-classes exist one
 * that handles POST and the other GET requests. The json response is
 * deserialized using the json serialization api, and then the given
 * AsyncCallback is invoked.
 *
 * @author Miroslav Pokorny
 */
abstract public class JsonServiceMethodInvoker<R> extends RpcServiceMethodInvoker<R> implements RequestCallback {

	protected JsonServiceMethodInvoker() {
		super();
	}

	/**
	 * Factory method which creates a new RequestBuilder.
	 *
	 * @return A new RequestBuilder
	 */
	@Override
	protected RequestBuilder createRequestBuilder() {
		return new RequestBuilder(this.getRequestMethod(), this.buildUrl());
	}

	/**
	 * This method will be used to build a url for the request that is about to
	 * be submitted.
	 *
	 * @return The final url given to the RequestBuilder
	 */
	abstract protected String buildUrl();

	/**
	 * Takes the response and converts the json payload to a java instance using
	 * {@link #deserializeJsonPayload(String)}
	 *
	 * @param request The request
	 * @param response The response
	 */
	protected void onSuccessfulResponse(final Request request, final Response response) {
		final AsyncCallback<R> callback = this.getCallback();
		R object = null;
		boolean skipOnFailure = false;

		try {
			final String text = response.getText();
			final JSONValue jsonValue = JSONParser.parse(text);
			if (false == jsonValue.isNull() instanceof JSONNull) {
				object = this.readObject(jsonValue);
			}
			// invoke success! after deserializing...
			skipOnFailure = true;
			callback.onSuccess(object);

		} catch (final Throwable caught) {
			if (false == skipOnFailure) {
				// invoke failure if anything went wrong.
				callback.onFailure(caught);
			}
		}
	}

	/**
	 * This method is implemented by the generator and acts a bridge calling GWT
	 * to create a Serializer which will deserialize the incoming JSON stream
	 * into a java object.
	 *
	 * @param jsonValue The response from the json service.
	 * @return The deserialized object
	 */
	abstract protected R readObject(final JSONValue jsonValue);
}
