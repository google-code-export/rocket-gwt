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
package rocket.beans.client;

import rocket.browser.client.BrowserHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * The base class for RpcServiceFactoryBean and JsonServiceFactoryBean
 * 
 * @author Miroslav Pokorny
 */
abstract public class RemoteRpcOrJsonServiceFactoryBean extends SingletonFactoryBean {

	/**
	 * Sub-classes will include a call to {@link GWT#create} with the interface
	 * class literal.
	 */
	abstract protected Object createInstance();

	protected void satisfyProperties(Object instance) {
		final ServiceDefTarget service = (ServiceDefTarget) instance;
		service.setServiceEntryPoint(this.addBrowserContextIfNotAbsolute(this.getAddress()));
	}

	protected String addBrowserContextIfNotAbsolute(final String address) {
		String address0 = address;
		if (false == this.isAbsoluteUrl(address)) {
			address0 = BrowserHelper.getContextPath() + address0;
		}
		return address0;
	}

	protected boolean isAbsoluteUrl(final String url) {
		final String url0 = url.toLowerCase();
		return url0.startsWith("http://") || url0.startsWith("https://");
	}

	private String address;

	protected String getAddress() {
		StringHelper.checkNotEmpty("field:address", address);
		return address;
	}

	public void setAddress(final String address) {
		StringHelper.checkNotEmpty("parameter:address", address);
		this.address = address;
	}

}
