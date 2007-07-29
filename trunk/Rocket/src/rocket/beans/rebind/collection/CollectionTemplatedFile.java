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

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.codeblock.ManyCodeBlocks;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the list and set templates
 * 
 * @author Miroslav Pokorny
 */
abstract public class CollectionTemplatedFile extends TemplatedCodeBlock {

	public CollectionTemplatedFile() {
		super();
		setNative(false);
		this.setElements(this.createElements());
	}

	private ManyCodeBlocks elements;

	protected ManyCodeBlocks getElements() {
		ObjectHelper.checkNotNull("field:elements", elements);
		return this.elements;
	}

	protected void setElements(final ManyCodeBlocks entries) {
		ObjectHelper.checkNotNull("parameter:elements", entries);
		this.elements = entries;
	}

	protected ManyCodeBlocks createElements() {
		return new ManyCodeBlocks();
	}

	public void add(final Value value) {
		ObjectHelper.checkNotNull("parameter:value", value);

		final CollectionElementAddTemplatedFile template = new CollectionElementAddTemplatedFile();
		template.setValue(value);

		this.getElements().add(template);
	}

	protected InputStream getInputStream() {
		final String filename = this.getTemplate();
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	abstract protected String getTemplate();

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (this.getElementsPlaceHolder().equals(name)) {
				value = this.getElements();
				break;
			}
			break;
		}
		return value;
	}

	abstract protected String getElementsPlaceHolder();

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found in [" + this.getTemplate() + "]");
	}
}
