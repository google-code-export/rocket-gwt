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
package rocket.remoting.rebind.rpc.java;

import rocket.remoting.client.JavaRpcService;
import rocket.remoting.client.RpcException;
import rocket.remoting.client.support.rpc.JavaRpcServiceClient;
import rocket.remoting.client.support.rpc.JavaRpcServiceMethodInvoker;
import rocket.serialization.client.SerializationFactoryComposer;

/**
 * A collection of constants for this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	static final String CLIENT_SUFFIX = "__JavaRpcClient";

	static final String JAVA_RPC_SERVICE_CLIENT = JavaRpcServiceClient.class.getName();

	static final String JAVA_RPC_SERVICE_METHOD_INVOKER = JavaRpcServiceMethodInvoker.class.getName();

	static final String GET_SERVICE_INTERFACE_NAME = "getServiceInterfaceName";

	static final String REMOTE_JAVA_SERVICE = JavaRpcService.class.getName();

	static final String SERIALIZATION_FACTORY_COMPOSER = SerializationFactoryComposer.class.getName();

	static final String NESTED_SERIALIZATION_FACTORY_COMPOSER = "SerializationFactoryComposer";

	static final String RPC_EXCEPTION = RpcException.class.getName();
}
