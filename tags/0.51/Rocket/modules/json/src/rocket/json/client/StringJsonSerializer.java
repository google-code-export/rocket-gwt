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
 * Reads and writes String values.
 * 
 * @author Miroslav Pokorny
 */
public class StringJsonSerializer extends JsonSerializer {
	public final static StringJsonSerializer serializer = new StringJsonSerializer();

	private StringJsonSerializer() {
		super();
	}

	@Override
	public Object readObject(final JSONValue jsonValue) {
		return this.read(jsonValue);
	}

	@Override
	public JSONValue writeJson(final Object instance) {
		return instance == null ? (JSONValue) JSONNull.getInstance() : (JSONValue) new JSONString((String) instance);
	}

	public String read(final JSONValue jsonValue) {
		String value = null;

		while (true) {
			if (null == jsonValue) {
				break;
			}
			final JSONString jsonString = jsonValue.isString();
			if (null == jsonString) {
				break;
			}

			value = jsonString.stringValue();
			break;
		}

		return value;
	}
}
