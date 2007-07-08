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
package rocket.beans.rebind.values;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * A tuple that holds a map entry, including the key as a string and the value
 * 
 * @author Miroslav Pokorny
 */
public class MapEntry {
	/**
	 * The key
	 */
	private String key;

	public String getKey() {
		StringHelper.checkNotEmpty("field:key", key);
		return key;
	}

	public void setKey(final String key) {
		StringHelper.checkNotEmpty("parameter:key", key);
		this.key = key;
	}

	/**
	 * The value
	 */
	private Value value;

	public Value getValue() {
		ObjectHelper.checkNotNull("field:value", value);
		return value;
	}

	public void setValue(final Value value) {
		ObjectHelper.checkNotNull("parameter:value", value);
		this.value = value;
	}
	
	public String toString(){
		return super.toString() + ", key[" + key + "] value: " + value;
	}
}
