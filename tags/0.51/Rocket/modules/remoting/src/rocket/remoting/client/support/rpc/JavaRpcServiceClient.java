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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Convenient base class for any rpc proxy.
 * 
 * A generator will create methods for each of the public interface methods that
 * handle the task of serializing incoming parameters and invoking the
 * {@link #makeRequest(String, String[], String, AsyncCallback)} method.
 * 
 * @author Miroslav Pokorny
 */
abstract public class JavaRpcServiceClient extends RpcServiceClient implements ServiceDefTarget {

	protected JavaRpcServiceClient() {
		super();
	}
}
