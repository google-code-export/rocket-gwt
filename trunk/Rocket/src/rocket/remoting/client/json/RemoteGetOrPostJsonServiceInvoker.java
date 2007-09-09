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
package rocket.remoting.client.json;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.http.client.URL;

/**
 * Adds the capability to accumulate parameters which are either added to the url or post data.
 * @author Miroslav Pokorny
 */
abstract public class RemoteGetOrPostJsonServiceInvoker extends RemoteJsonServiceInvoker{
	/**
	 * Accumulates any parameters that accompany the request.
	 */
	private StringBuffer parameters = new StringBuffer();

	protected StringBuffer getParameters() {
		ObjectHelper.checkNotNull("field:parameters", parameters);
		return this.parameters;
	}

	protected void setParameter(final StringBuffer parameters) {
		ObjectHelper.checkNotNull("parameter:parameters", parameters);
		this.parameters = parameters;
	}

	/**
	 * Adds a new boolean value parameter to the parameters that will be sent
	 * when the request is made.
	 * 
	 * @param name
	 * @param booleanValue
	 */
	public void addParameter(final String name, final boolean booleanValue) {
		this.addParameter(name, Boolean.toString(booleanValue));
	}

	public void addParameter(final String name, final byte byteValue) {
		this.addParameter(name, Byte.toString(byteValue));
	}

	public void addParameter(final String name, final short shortValue) {
		this.addParameter(name, Short.toString(shortValue));
	}

	public void addParameter(final String name, final int intValue) {
		this.addParameter(name, Integer.toString(intValue));
	}

	public void addParameter(final String name, final long longValue) {
		this.addParameter(name, Long.toString(longValue));
	}

	public void addParameter(final String name, final float floatValue) {
		this.addParameter(name, Float.toString(floatValue));
	}

	public void addParameter(final String name, final double doubleValue) {
		this.addParameter(name, Double.toString(doubleValue));
	}

	public void addParameter(final String name, final char charValue) {
		this.addParameter(name, Character.toString(charValue));
	}

	public void addParameter(final String name, final String value) {
		StringHelper.checkNotEmpty("parameter:name", name);
		StringHelper.checkNotNull("parameter:value", value);

		final StringBuffer parameters = this.getParameters();

		if (parameters.length() > 0) {
			parameters.append('&');
		}

		parameters.append(name);
		parameters.append('=');
		parameters.append(URL.encode(value));
	}
}
