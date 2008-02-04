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
package rocket.remoting.rebind.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A collection of constants for this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	static final String ASYNC_INTERFACE_SUFFIX = "Async";

	static final String ASYNC_CALLBACK = AsyncCallback.class.getName();

	static final String SERVICE_DEF_TARGET = ServiceDefTarget.class.getName();

	static final String CALLBACK_PARAMETER = "callback";
}
