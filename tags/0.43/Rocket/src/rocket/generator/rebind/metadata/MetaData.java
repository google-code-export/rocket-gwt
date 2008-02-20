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
package rocket.generator.rebind.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.util.client.Checker;

/**
 * A container that holds any meta data added to any new types being generated.
 * @author Miroslav Pokorny
 */
public class MetaData implements CodeBlock, HasMetadata {

	public MetaData() {
		super();

		this.setMetaData(this.createMetaData());
	}

	/**
	 * This map holds an arrangment of keys to values in add order. 
	 */
	private Map metaData;

	protected Map getMetaData() {
		Checker.notNull("field:metaData", metaData);
		return this.metaData;
	}

	protected void setMetaData(final Map metaData) {
		Checker.notNull("parameter:metaData", metaData);
		this.metaData = metaData;
	}

	protected Map createMetaData() {
		return new LinkedHashMap();
	}

	public void add(final String key, final String value) {
		List list = this.get(key);
		if (null == list) {
			list = new ArrayList();
			this.getMetaData().put(key, list);
		}

		list.add(value);
	}

	protected List get(final String key) {
		return (List) this.getMetaData().get(key);
	}

	/**
	 * Returns a read only value of all current meta data values for the given name.
	 */
	public List getMetadataValues(final String name) {
		final List values = this.get(name);
		return values == null ? null : Collections.unmodifiableList(values);
	}

	public boolean isEmpty() {
		return this.getMetaData().isEmpty();
	}

	public void write(final SourceWriter writer) {
		final Iterator entries = this.getMetaData().entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();
			final String name = (String) entry.getKey();
			final List values = (List) entry.getValue();

			this.writeMetaDataEntries(name, values, writer);
		}
	}

	protected void writeMetaDataEntries(final String name, final List values, final SourceWriter writer) {

		final Iterator valuesIterator = values.iterator();
		while (valuesIterator.hasNext()) {
			final String value = (String) valuesIterator.next();
			this.writeMetaDataEntry(name, value, writer);
		}
	}

	protected void writeMetaDataEntry(final String name, final String value, final SourceWriter writer) {
		final String line = '@' + name + ' ' + value;
		writer.println(line);
	}

	public String toString() {
		return super.toString() + this.metaData;
	}
}
