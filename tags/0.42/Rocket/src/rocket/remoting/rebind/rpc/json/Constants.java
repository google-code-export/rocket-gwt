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
package rocket.remoting.rebind.rpc.json;

import rocket.json.client.JsonSerializer;
import rocket.remoting.client.JsonRpcService;
import rocket.remoting.client.support.rpc.GetJsonRpcServiceMethodInvoker;
import rocket.remoting.client.support.rpc.JsonRpcServiceClient;
import rocket.remoting.client.support.rpc.JsonServiceMethodInvoker;
import rocket.remoting.client.support.rpc.PostJsonServiceMethodInvoker;

import com.google.gwt.json.client.JSONValue;

/**
 * A collection of constants for this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	static final String CLIENT_SUFFIX = "__JsonRpcClient";

	static final String JSON_RPC_ANNOTATION_BASE = "jsonRpc";

	static final String INPUT_ARGUMENTS_ANNOTATION = JSON_RPC_ANNOTATION_BASE + "-inputArguments";

	static final String INPUT_ARGUMENTS_JSON_RPC = "jsonRpc";

	static final String INPUT_ARGUMENTS_REQUEST_PARAMETERS = "requestParameters";

	static final String HTTP_REQUEST_METHOD_ANNOTATION = JSON_RPC_ANNOTATION_BASE + "-httpMethod";

	static final String HTTP_REQUEST_PARAMETER_NAME_ANNOTATION = JSON_RPC_ANNOTATION_BASE + "-parameterName";

	static final String JSON_RPC_SERVICE_CLIENT = JsonRpcServiceClient.class.getName();

	static final String JSON_SERVICE_METHOD_INVOKER = JsonServiceMethodInvoker.class.getName();

	static final String GET_JSON_RPC_SERVICE_INVOKER = GetJsonRpcServiceMethodInvoker.class.getName();

	static final String POST_JSON_RPC_SERVICE_INVOKER = PostJsonServiceMethodInvoker.class.getName();

	static final String REMOTE_JSON_SERVICE = JsonRpcService.class.getName();

	static final String JSON_VALUE = JSONValue.class.getName();

	static final String JSON_SERIALIZER = JsonSerializer.class.getName();
}
