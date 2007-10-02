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

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONValue;

/**
 * Reads and writes boolean values.
 * 
 * @author Miroslav Pokorny
 */
public class BooleanJsonSerializer extends JsonSerializer {

	public final static BooleanJsonSerializer serializer = new BooleanJsonSerializer();

	private BooleanJsonSerializer() {
		super();
	}

	public Object readObject(final JSONValue jsonValue) {
		return read( jsonValue ) ? Boolean.TRUE : Boolean.FALSE;
	}

	public JSONValue writeJson(final Object instance) {
		final Boolean wrapper = (Boolean) instance;
		return JSONBoolean.getInstance(wrapper.booleanValue());
	}

	public boolean read(final JSONValue jsonValue) {
		boolean value = false;

		while (true) {
			if (null == jsonValue) {
				break;
			}
			final JSONBoolean jsonBoolean = jsonValue.isBoolean();
			if (null == jsonBoolean) {
				break;
			}

			value = jsonBoolean.booleanValue();
			break;
		}

		return value;		
	}

	public JSONValue writeJson(final boolean booleanValue) {
		return JSONBoolean.getInstance(booleanValue);
	}
}
