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

import java.util.Collection;
import java.util.Iterator;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * Contains a collection ( a list or set ) value for a bean.
 * 
 * @author Miroslav Pokorny
 */
abstract public class CollectionValue extends Value {

	public CollectionValue() {
		this.setElements(this.createElements());
	}

	public void addElement(final Value value) {
		ObjectHelper.checkNotNull("parameter:value", value);

		this.getElements().add(value);
	}

	/**
	 * A elements that accumulates elements elements.
	 */
	private Collection elements;

	public Collection getElements() {
		ObjectHelper.checkNotNull("field:elements", elements);
		return this.elements;
	}

	protected void setElements(final Collection elements) {
		ObjectHelper.checkNotNull("parameter:elements", elements);
		this.elements = elements;
	}

	abstract protected Collection createElements();

	public boolean isCompatibleWith(final Type type) {
		return this.getGeneratorContext().getType(this.getCollectionTypeName()).equals(type);
	}

	abstract protected String getCollectionTypeName();

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final CollectionTemplatedFile template = this.createTemplate();
		final Iterator elements = this.getElements().iterator();

		while (elements.hasNext()) {
			final Value element = (Value) elements.next();

			template.add(element);
		}

		template.write(writer);
	}

	/**
	 * Factory which creates the Template which will be produce a statement that
	 * adds to a Collection all the elements housed by this instance as java
	 * objects.
	 * 
	 * @return
	 */
	abstract protected CollectionTemplatedFile createTemplate();
}