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

import rocket.remoting.client.JsonRpcService;

/**
 * This is the base class for all generated client proxies created by the
 * RpcClientGenerator tool.
 *
 * It holds a number of properties which will be common to all generated
 * clients. The tool will create sub-classes of this class and add additional
 * bridge methods between the service interface being implement and
 * RemoteJsonServiceMethodInvokers which take care of the json to java.
 *
 * A number of limitations are imposed on all RemoteJsonServiceClients
 * <ul>
 * <li>The service interface may have only one method</li>
 * <li>All parameters for the method must be either primitives and String.</li>
 * <li>The return type must be an Object which is serializable.</li>
 * </ul>
 *
 * @author Miroslav Pokorny
 */
abstract public class JsonRpcServiceClient extends RpcServiceClient implements JsonRpcService{

	protected JsonRpcServiceClient() {
		super();
	}
}
