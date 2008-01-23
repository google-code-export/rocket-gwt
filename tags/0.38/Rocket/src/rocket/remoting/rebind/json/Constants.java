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

	static final String ASYNC_INTERFACE_SUFFIX = "Async";

	static final String JSON_RPC_ANNOTATION_BASE = "jsonRpc";

	static final String INPUT_ARGUMENTS_ANNOTATION = JSON_RPC_ANNOTATION_BASE + "-inputArguments";

	static final String INPUT_ARGUMENTS_JSON_RPC = "jsonRpc";

	static final String INPUT_ARGUMENTS_REQUEST_PARAMETERS = "requestParameters";

	static final String HTTP_REQUEST_METHOD_ANNOTATION = JSON_RPC_ANNOTATION_BASE + "-httpMethod";

	static final String HTTP_REQUEST_PARAMETER_NAME_ANNOTATION = JSON_RPC_ANNOTATION_BASE + "-parameterName";

	static final String REQUEST_PARAMETERS_INVOKER_TEMPLATE = "request-parameters-invoker.txt";

	static final String REQUEST_PARAMETERS_INVOKER_VARIABLE = "invoker";

	static final String REQUEST_PARAMETERS_INVOKER_INVOKER_TYPE = "invokerType";

	static final String REQUEST_PARAMETERS_INVOKER_PAYLOAD_TYPE = "payloadType";

	static final String REQUEST_PARAMETERS_INVOKER_CALLBACK_PARAMETER = "callback";

	static final String REQUEST_PARAMETERS_INVOKER_ADD_PARAMETERS = "addParameters";

	static final String REQUEST_PARAMETERS_INVOKER_ADD_PARAMETER_TEMPLATE = "request-parameters-invoker-add-parameter.txt";

	static final String REQUEST_PARAMETERS_INVOKER_ADD_PARAMETER_HTTP_REQUEST_PARAMETER_NAME = "httpRequestParameterName";

	static final String REQUEST_PARAMETERS_INVOKER_ADD_PARAMETER_PARAMETER = "parameter";

	static final String REMOTE_JSON_SERVICE_CLIENT_SUPER = RemoteJsonServiceClient.class.getName();

	static final String REMOTE_JSON_SERVICE_INVOKER = RemoteJsonServiceInvoker.class.getName();

	static final String REMOTE_GET_JSON_SERVICE_INVOKER = RemoteGetJsonServiceInvoker.class.getName();

	static final String REMOTE_POST_JSON_SERVICE_INVOKER = RemotePostJsonServiceInvoker.class.getName();

	static final String JSON_RPC_INVOKER_TEMPLATE = "json-rpc-invoker.txt";

	static final String JSON_RPC_INVOKER_PARAMETER_TYPE = "parameterType";

	static final String JSON_RPC_INVOKER_PAYLOAD_TYPE = "payloadType";

	static final String ASYNC_CALLBACK = AsyncCallback.class.getName();

	static final String REMOTE_JSON_SERVICE = RemoteJsonService.class.getName();

	static final String SERVICE_DEF_TARGET = ServiceDefTarget.class.getName();

	static final String JSON_VALUE = JSONValue.class.getName();

	static final String JSON_SERIALIZER = JsonSerializer.class.getName();

	static final String GWT = GWT.class.getName();
}