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
package rocket.beans.rebind.collection;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.util.client.Checker;

/**
 * An abstraction for the list and set templates
 * 
 * @author Miroslav Pokorny
 */
abstract public class CollectionTemplatedFile extends TemplatedFileCodeBlock {

	public CollectionTemplatedFile() {
		super();
		this.setElements(this.createElements());
	}

	private List<Value> elements;

	protected List<Value> getElements() {
		Checker.notNull("field:elements", elements);
		return this.elements;
	}

	protected void setElements(final List<Value> entries) {
		Checker.notNull("parameter:elements", entries);
		this.elements = entries;
	}

	protected List<Value> createElements() {
		return new ArrayList<Value>();
	}

	public void add(final Value value) {
		Checker.notNull("parameter:value", value);

		this.getElements().add(value);
	}

	protected CodeBlock getElementsCodeBlock() {
		final CollectionElementAddTemplatedFile template = new CollectionElementAddTemplatedFile();
		final List<Value> elements = this.getElements();

		return new CollectionTemplatedCodeBlock<Value>() {

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
			protected Collection<Value> getCollection() {
				return elements;
			}

			@Override
			protected void prepareToWrite(final Value ignored ) {
				template.setValue((Value) elements.get(this.getIndex()));
			}

			@Override
			protected void writeBetweenElements(final SourceWriter writer) {
			}
		};
	}

	abstract protected String getResourceName();

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (this.getElementsPlaceHolder().equals(name)) {
				value = this.getElementsCodeBlock();
				break;
			}
			break;
		}
		return value;
	}

	abstract protected String getElementsPlaceHolder();
}
