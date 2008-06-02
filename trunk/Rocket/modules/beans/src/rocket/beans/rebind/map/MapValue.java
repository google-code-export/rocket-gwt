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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.beans.rebind.value.AbstractValue;
import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Contains a Map property value for a bean, including the many entries and values that may have been specified.
 * 
 * @author Miroslav Pokorny
 */
public class MapValue extends AbstractValue implements Value{

	public MapValue() {
		this.setEntries(createEntries());
	}

	public void addEntry(final String key, final Value value) {
		final Map entries = this.getEntries();
		if (entries.containsKey(key)) {
			this.throwKeyAlreadyUsed(key);
		}

		entries.put(key, value);
	}

	protected void throwKeyAlreadyUsed(final String key) {
		throw new MapEntryAlreadyUsedException("A entries entry with a key of \"" + key + "\" has already been defined");
	}

	/**
	 * A entries that accumulates entries entries.
	 */
	private Map<String,Value> entries;

	public Map<String,Value> getEntries() {
		Checker.notNull("field:entries", entries);
		return this.entries;
	}

	protected void setEntries(final Map<String,Value> entries) {
		Checker.notNull("parameter:entries", entries);
		this.entries = entries;
	}

	protected Map<String,Value> createEntries() {
		return new HashMap<String,Value>();
	}

	/**
	 * If the property is a not a Map report false
	 * 
	 * @return true if the type is compatible
	 */
	@Override
	public boolean isCompatibleWith(final Type type) {
		Checker.notNull("parameter:type", type);

		return this.getGeneratorContext().getType(Constants.MAP_TYPE).equals(type);
	}

	public void write(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final MapTemplatedFile template = new MapTemplatedFile();
		final Iterator entries = this.getEntries().entrySet().iterator();

		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();
			final String key = (String) entry.getKey();
			final Value value = (Value) entry.getValue();

			template.add(key, value);
		}

		template.write(writer);
	}
	
	@Override
	public String toString(){
		return this.getEntries().toString();
	}
}