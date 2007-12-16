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
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * This is the base class for all generated client proxies created by the
 * RpcGenerator tool.
 * 
 * @author Miroslav Pokorny
 */
abstract public class RpcServiceClient implements JsonRpcService, ServiceDefTarget {

	protected RpcServiceClient() {
		super();
	}

	/**
	 * When present a username and password is also attached to the request.
	 */
	private String username;

	public String getUsername() {
		StringHelper.checkNotEmpty("field:username", username);
		return this.username;
	}

	public boolean hasUsername() {
		return null != username;
	}

	public void setUsername(final String username) {
		StringHelper.checkNotEmpty("parameter:username", username);
		this.username = username;
	}

	/**
	 * To add authentication to any request both the username and password
	 * properties must be set.
	 */
	private String password;

	public String getPassword() {
		StringHelper.checkNotEmpty("field:password", password);
		return this.password;
	}

	public boolean hasPassword() {
		return null != password;
	}

	public void setPassword(final String password) {
		StringHelper.checkNotEmpty("parameter:password", password);
		this.password = password;
	}

	/**
	 * This property must be set to allow a custom timeout value for this rpc.
	 */
	private int timeout;

	public int getTimeout() {
		PrimitiveHelper.checkGreaterThan("field:timeout", 0, timeout);
		return timeout;
	}

	public boolean hasTimeout() {
		return this.timeout > 0;
	}

	public void setTimeout(final int timeout) {
		PrimitiveHelper.checkGreaterThan("parameter:timeout", 0, timeout );
		this.timeout = timeout;
	}

	/**
	 * The url of the service.
	 */
	private String serviceEntryPoint;

	public String getServiceEntryPoint() {
		return this.serviceEntryPoint;
	}

	public void setServiceEntryPoint(final String serviceEntryPoint) {
		this.serviceEntryPoint = serviceEntryPoint;
	}

	public String toString() {
		return super.toString() + ", username\"" + username + "\", password\"" + password + "\", timeout: " + timeout + ", serviceEntryPoint\""
				+ serviceEntryPoint + "\".";
	}
}