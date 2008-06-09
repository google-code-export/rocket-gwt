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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.util.client.Checker;

/**
 * An abstraction for the map template
 * 
 * @author Miroslav Pokorny
 */
public class MapTemplatedFile extends TemplatedFileCodeBlock {

	public MapTemplatedFile() {
		super();
		this.setEntries(this.createEntries());
	}

	private Map<String, Value> entries;

	protected Map<String, Value> getEntries() {
		Checker.notNull("field:entries", entries);
		return this.entries;
	}

	protected void setEntries(final Map<String, Value> entries) {
		Checker.notNull("parameter:entries", entries);
		this.entries = entries;
	}

	protected Map<String, Value> createEntries() {
		return new HashMap<String, Value>();
	}

	public void add(final String key, final Value value) {
		final Map<String, Value> entries = this.getEntries();
		if (entries.containsKey(key)) {
			this.throwMapEntryAlreadyUsed(key);
		}

		entries.put(key, value);
	}

	protected void throwMapEntryAlreadyUsed(final String key) {
		throw new MapEntryAlreadyUsedException("A map entry with a key of \"" + key + "\" has already been defined");
	}

	@Override
	protected String getResourceName() {
		return Constants.MAP_TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.MAP_ADD_ENTRIES.equals(name)) {
				value = this.getEntriesCodeBlock();
				break;
			}
			break;
		}
		return value;
	}

	protected CodeBlock getEntriesCodeBlock() {
		final MapAddEntryTemplatedFile template = new MapAddEntryTemplatedFile();
		final Map<String, Value> entries = this.getEntries();

		return new CollectionTemplatedCodeBlock<Map.Entry<String, Value>>() {
			@Override
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			@Override
			@SuppressWarnings("unchecked")
			protected Collection<Map.Entry<String, Value>> getCollection() {
				return entries.entrySet();
			}

			@Override
			protected void prepareToWrite(final Map.Entry<String, Value> entry ) {
				template.setKey(entry.getKey());
				template.setValue(entry.getValue());
			}

			@Override
			protected void writeBetweenElements(SourceWriter writer) {
			}
		};
	}
}
