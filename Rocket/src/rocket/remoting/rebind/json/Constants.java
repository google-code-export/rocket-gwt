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
package rocket.remoting.rebind.json;

import rocket.json.client.JsonSerializer;
import rocket.remoting.client.json.RemoteGetJsonServiceInvoker;
import rocket.remoting.client.json.RemoteJsonService;
import rocket.remoting.client.json.RemoteJsonServiceClient;
import rocket.remoting.client.json.RemoteJsonServiceInvoker;
import rocket.remoting.client.json.RemotePostJsonServiceInvoker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A collection of constants for this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	static final String CLIENT_SUFFIX = "__RemoteJsonServiceClient";

	static final String ASYNC_CALLBACK = AsyncCallback.class.getName();

	static final String REMOTE_JSON_SERVICE = RemoteJsonService.class.getName();

	static final String SERVICE_DEF_TARGET = ServiceDefTarget.class.getName();

	static final String ASYNC_INTERFACE_SUFFIX = "Async";

	static final String HTTP_REQUEST_METHOD = "jsonRpc-httpMethod";

	static final String HTTP_REQUEST_PARAMETER_NAME = "jsonRpc-parameterName";

	static final String JSON_VALUE_TYPE = JSONValue.class.getName();

	static final String JSON_SERIALIZER_TYPE = JsonSerializer.class.getName();

	static final String GWT_TYPE = GWT.class.getName();

	static final String INVOKER_VARIABLE = "invoker";

	static final String INVOKER_TEMPLATE = "invoker.txt";

	static final String INVOKER_INVOKER_TYPE = "invokerType";

	static final String INVOKER_METHOD_RETURN_TYPE = "methodReturnType";

	static final String INVOKER_CALLBACK_PARAMETER = "callbackParameter";

	static final String INVOKER_ADD_PARAMETERS = "addParameters";

	static final String INVOKER_ADD_PARAMETER_TEMPLATE = "invoker-add-parameter.txt";

	static final String INVOKER_ADD_PARAMETER_HTTP_REQUEST_PARAMETER_NAME = "httpRequestParameterName";

	static final String INVOKER_ADD_PARAMETER_PARAMETER = "parameter";

	static final String CLIENT_SUPER_TYPE = RemoteJsonServiceClient.class.getName();

	static final String REMOTE_JSON_SERVICE_INVOKER_TYPE = RemoteJsonServiceInvoker.class.getName();

	static final String REMOTE_GET_JSON_SERVICE_INVOKER_TYPE = RemoteGetJsonServiceInvoker.class.getName();

	static final String REMOTE_POST_JSON_SERVICE_INVOKER_TYPE = RemotePostJsonServiceInvoker.class.getName();
}
