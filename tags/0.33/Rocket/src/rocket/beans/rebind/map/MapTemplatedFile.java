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
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * An abstraction for the map template
 * 
 * @author Miroslav Pokorny
 */
public class MapTemplatedFile extends TemplatedCodeBlock{

	public MapTemplatedFile() {
		super();
		setNative(false);
		this.setEntries(this.createEntries());
	}

	private Map entries;

	protected Map getEntries() {
		ObjectHelper.checkNotNull("field:entries", entries);
		return this.entries;
	}

	protected void setEntries(final Map entries) {
		ObjectHelper.checkNotNull("parameter:entries", entries);
		this.entries = entries;
	}

	protected Map createEntries() {
		return new HashMap();
	}

	public void add(final String key, final Value value) {
		final Map entries = this.getEntries();
		if (entries.containsKey(key)) {
			this.throwMapEntryAlreadyUsedException(key);
		}

		entries.put(key, value);
	}

	protected void throwMapEntryAlreadyUsedException(final String key) {
		throw new MapEntryAlreadyUsedException("A map entry with a key of [" + key + "] has already been defined");
	}

	protected InputStream getInputStream() {
		final String filename = Constants.MAP_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

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
		final Map entries = this.getEntries();
		
		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return template.getInputStream();
			}

			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			protected Collection getCollection() {
				return entries.entrySet();
			}

			protected void prepareToWrite(final Object element) {
				final Map.Entry entry = (Map.Entry) element;
				template.setKey( (String) entry.getKey() );
				template.setValue( (Value) entry.getValue() );
			}

			protected void writeBetweenElements(SourceWriter writer) {
			}
		};
	}
	
	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found, template file [" + Constants.MAP_TEMPLATE + "]");
	}
}
