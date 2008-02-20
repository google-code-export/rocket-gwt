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

import rocket.json.client.JsonSerializer;
import rocket.util.client.Checker;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.json.client.JSONValue;

/**
 * Convenient base class invoker for any json rpc poster.
 *
 * The generator will implement the {@link #readObject(JSONValue)} and
 * {@link #createSerializer()}.
 *
 * @author Miroslav Pokorny
 */
abstract public class JsonRpcServiceInvoker extends JsonServiceMethodInvoker {

	protected String buildUrl() {
		return this.getUrl();
	}

	String getRequestData() {
		final JsonSerializer serializer = this.createSerializer();
		final JSONValue json = serializer.writeJson(this.getParameter());
		return json.toString();
	}

	Method getRequestMethod() {
		return RequestBuilder.POST;
	}

	protected void setHeaders(final RequestBuilder request) {
		request.setHeader(Constants.CONTENT_TYPE_HEADER, Constants.JSON_RPC_CONTENT_TYPE);
		request.setHeader(Constants.CONTENT_LENGTH_HEADER, "" + this.getRequestData().length());
	}

	/**
	 * The parameter that will be serialized and sent to the json rpc service on
	 * the server.
	 */
	private Object parameter;

	public Object getParameter() {
		Checker.notNull("field:parameter", parameter);
		return this.parameter;
	}

	public void setParameter(final Object parameter) {
		Checker.notNull("parameter:parameter", parameter);
		this.parameter = parameter;
	}

	/**
	 * The generator will override this method and use deferred binding to
	 * locate the appropriate json serializer for the parameter.
	 *
	 * @return
	 */
	abstract protected JsonSerializer createSerializer();
}
