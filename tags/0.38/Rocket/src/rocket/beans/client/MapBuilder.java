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

import java.util.HashMap;
import java.util.Map;

import rocket.util.client.ObjectHelper;

/**
 * Builds a map containing values. This is typically used by bean factories to
 * set a map property
 * 
 * @author Miroslav Pokorny
 */
public class MapBuilder {
	public MapBuilder() {
		super();

		this.setMap(this.createMap());
	}

	public MapBuilder add(final String key, final boolean booleanValue) {
		return this.add(key, Boolean.valueOf(booleanValue));
	}

	public MapBuilder add(final String key, final byte byteValue) {
		return this.add(key, new Byte(byteValue));
	}

	public MapBuilder add(final String key, final short shortValue) {
		return this.add(key, new Short(shortValue));
	}

	public MapBuilder add(final String key, final int intValue) {
		return this.add(key, new Integer(intValue));
	}

	public MapBuilder add(final String key, final long longValue) {
		return this.add(key, new Long(longValue));
	}

	public MapBuilder add(final String key, final float floatValue) {
		return this.add(key, new Float(floatValue));
	}

	public MapBuilder add(final String key, final double doubleValue) {
		return this.add(key, new Double(doubleValue));
	}

	public MapBuilder add(final String key, final char charValue) {
		return this.add(key, new Character(charValue));
	}

	public MapBuilder add(final String key, final Object object) {
		this.getMap().put(key, object);
		return this;
	}

	/**
	 * A map of values.
	 */
	private Map map;

	public Map getMap() {
		ObjectHelper.checkNotNull("field:map", map);
		return this.map;
	}

	protected void setMap(final Map map) {
		ObjectHelper.checkNotNull("parameter:map", map);
		this.map = map;
	}

	protected Map createMap() {
		return new HashMap();
	}
}
