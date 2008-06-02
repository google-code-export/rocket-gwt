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
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Reads and writes char values.
 * 
 * @author Miroslav Pokorny
 * @author Vincente Ferrer
 */
public class CharJsonSerializer extends JsonSerializer {

	public final static CharJsonSerializer serializer = new CharJsonSerializer();

	private CharJsonSerializer() {
		super();
	}

	@Override
	public Object readObject(final JSONValue jsonValue) {
		return new Character(read(jsonValue));
	}

	@Override
	public JSONValue writeJson(final Object instance) {
		final Character wrapper = (Character) instance;
		return wrapper == null ? (JSONValue) JSONNull.getInstance() : new JSONString(Character.toString(wrapper.charValue()));
	}

	public char read(final JSONValue jsonValue) {
		char value = 0;

		while (true) {
			if (null == jsonValue) {
				break;
			}
			final JSONString c = jsonValue.isString();
			if (null == c) {
				break;
			}

			value = c.stringValue().charAt(0);
			break;
		}

		return value;
	}

	public JSONValue writeJson(final char charValue) {
		return new JSONString(Character.toString(charValue));
	}
}
