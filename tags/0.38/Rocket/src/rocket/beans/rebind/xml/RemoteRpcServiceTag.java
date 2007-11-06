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
package rocket.beans.rebind.xml;

/**
 * Provides a bean view of the remote rpc service tag.
 * 
 * @author Miroslav Pokorny
 */
public class RemoteRpcServiceTag extends XmlDocumentComponent {

	public String getAddress() {
		return this.getAttribute(Constants.REMOTE_RPC_SERVICE_ADDRESS);
	}

	public String getId() {
		return this.getAttribute(Constants.REMOTE_RPC_SERVICE_ID);
	}

	public String getInterface() {
		return this.getAttribute(Constants.REMOTE_RPC_SERVICE_INTERFACE);
	}
}
