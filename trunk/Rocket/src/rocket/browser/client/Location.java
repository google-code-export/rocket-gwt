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
package rocket.browser.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.collection.client.CollectionsHelper;
import rocket.util.client.StringHelper;

/**
 * The location class is abstraction over the browser location object.
 * 
 * It provides two map values of any parameters found within the query string
 * portion of the location.
 * <ul>
 * <li>The map returned by {@link #getParameters()} only returns the first
 * value.</li>
 * <li>The map returned by {@link #getAllParameters()} returns a list of all
 * values.</li>
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Location {

	/**
	 * The Location singleton
	 */
	static Location location = new Location();

	static public Location getLocation() {
		return location;
	}

	/**
	 * Non public to stop instantiation.
	 * 
	 * Is protected for testing purposes.
	 */
	protected Location() {
		super();
	}

	public native String getHref() /*-{
	 return $wnd.location.href;
	 }-*/;

	public native String getHost() /*-{
	 return $wnd.location.host;
	 }-*/;

	public native String getHostName() /*-{
	 return $wnd.location.hostname;
	 }-*/;

	public native String getProtocol() /*-{
	 return $wnd.location.protocol;
	 }-*/;

	public native String getPort() /*-{
	 return $wnd.location.port;
	 }-*/;

	public native String getPath() /*-{
	 return $wnd.location.pathname;
	 }-*/;

	public native String getQueryString() /*-{
	 return $wnd.location.search;
	 }-*/;

	/**
	 * A readonly map that contains any parameters and the first value
	 * encountered in the location query string.
	 */
	private Map parameters;

	public Map getParameters() {
		if (null == parameters) {
			this.setParameters(createParameters());
		}
		return this.parameters;
	}

	protected void setParameters(final Map parameters) {
		this.parameters = parameters;
	}

	protected Map createParameters() {
		final String[] nameAndValues = this.createParameterNameAndValues();
		final Map map = new HashMap();
		for (int i = 0; i < nameAndValues.length;) {
			final String name = nameAndValues[i++];
			final String value = nameAndValues[i++];
			if (map.containsKey(name)) {
				continue;
			}

			map.put(name, value);
		}

		return map;
	}

	/**
	 * A readonly map containing any parameters and values( as a readonly list).
	 */
	private Map allParameters;

	public Map getAllParameters() {
		if (null == allParameters) {
			this.setAllParameters(createAllParameters());
		}
		return this.allParameters;
	}

	protected void setAllParameters(final Map allParameters) {
		this.allParameters = allParameters;
	}

	protected Map createAllParameters() {
		final String[] nameAndValues = this.createParameterNameAndValues();
		final Map map = new HashMap();
		for (int i = 0; i < nameAndValues.length;) {
			final String name = nameAndValues[i++];
			final String value = nameAndValues[i++];

			List values = (List) map.get(name);
			if (null == values) {
				// create list for $name and save...
				values = new ArrayList();
				map.put(name, values);
			}
			values.add(value);
		}

		final Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();
			final List writableList = (List) entry.getValue();
			entry.setValue(CollectionsHelper.unmodifiableList(writableList));
		}

		return CollectionsHelper.unmodifiableMap(map);
	}

	/**
	 * This helper returns an array with every odd element containing a parameter name and the next or even element containing the value.
	 * @return A string array containing alternating name and values. Values will never be null
	 */
	protected String[] createParameterNameAndValues() {
		final String queryString = this.getQueryString();
		final String[] tokens = StringHelper.split(queryString, "&", true);
		final int tokenCount = tokens.length;
		final String[] nameAndValues = new String[tokenCount * 2];
		int j = 0;
		for (int i = 0; i < tokenCount; i++) {
			final String nameValue = tokens[i];
			String name = nameValue;
			String value = "";
			final int equalsSign = nameValue.indexOf('=');
			if (equalsSign != -1) {
				name = nameValue.substring(0, equalsSign);
				value = nameValue.substring(equalsSign + 1);
			}
			nameAndValues[j++] = name;
			nameAndValues[j++] = value;
		}
		return nameAndValues;
	}

	public native String getHash() /*-{
	 return $wnd.location.hash;
	 }-*/;

	public String toString() {
		return this.getHref();
	}
}
