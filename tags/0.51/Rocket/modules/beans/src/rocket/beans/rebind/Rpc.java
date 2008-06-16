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
package rocket.beans.rebind;

/**
 * Instances of this class represent a json or java rpc being constructed.
 * 
 * @author Miroslav Pokorny
 */
public class Rpc extends Bean {

	public Rpc() {
		super();
	}

	/**
	 * The service def target url
	 */
	private String serviceEntryPoint;

	public String getServiceEntryPoint() {
		return this.serviceEntryPoint;
	}

	public void setServiceEntryPoint(final String serviceEntryPoint) {
		this.serviceEntryPoint = serviceEntryPoint;
	}

	/**
	 * The service interface
	 */
	private String serviceInterface;

	public String getServiceInterface() {
		return this.serviceInterface;
	}

	public void setServiceInterface(final String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public String toString() {
		return super.toString() + ", serviceEntryPoint: \"" + serviceEntryPoint + "\", serviceInterface: " + serviceInterface;
	}
}
