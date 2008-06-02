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
package rocket.beans.rebind.map;

import java.io.InputStream;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.util.client.Checker;

/**
 * An abstraction for the map-add-entry template
 * 
 * @author Miroslav Pokorny
 */
public class MapAddEntryTemplatedFile extends TemplatedFileCodeBlock {

	public MapAddEntryTemplatedFile() {
		super();
	}

	/**
	 * The key of the entry.
	 */
	private String key;

	protected String getKey() {
		Checker.notNull("field:key", key);
		return this.key;
	}

	public void setKey(final String key) {
		Checker.notNull("parameter:key", key);
		this.key = key;
	}

	private Value value;

	protected Value getValue() {
		Checker.notNull("field:value", value);
		return this.value;
	}

	public void setValue(final Value value) {
		Checker.notNull("parameter:value", value);
		this.value = value;
	}

	@Override
	protected String getResourceName() {
		return Constants.MAP_ENTRY_ADD_TEMPLATE;
	}
	
	@Override
	public InputStream getInputStream(){
		return super.getInputStream();
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.MAP_ENTRY_ADD_KEY.equals(name)) {
				value = new StringLiteral(this.getKey());
				break;
			}
			if (Constants.MAP_ENTRY_ADD_VALUE.equals(name)) {
				value = this.getValue();
				break;
			}
			break;
		}
		return value;
	}
}
