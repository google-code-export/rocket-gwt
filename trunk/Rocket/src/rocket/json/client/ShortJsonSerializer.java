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
package rocket.json.client;

import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONValue;

/**
 * Reads and writes short values.
 * 
 * @author Miroslav Pokorny
 * @author Vincente Ferrer
 */
public class ShortJsonSerializer extends JsonSerializer {
	public final static ShortJsonSerializer serializer = new ShortJsonSerializer();

	private ShortJsonSerializer() {
		super();
	}

	public Object readObject(final JSONValue jsonValue) {
		return new Short(this.read(jsonValue));
	}

	public JSONValue writeJson(final Object instance) {
		final Short wrapper = (Short) instance;
		return wrapper == null ? (JSONValue)JSONNull.getInstance() : new JSONNumber(wrapper.shortValue());
	}

	public short read(final JSONValue jsonValue) {
		return (short) this.readDouble(jsonValue);
	}

	public JSONValue writeJson(final short shortValue) {
		return new JSONNumber(shortValue);
	}
}
