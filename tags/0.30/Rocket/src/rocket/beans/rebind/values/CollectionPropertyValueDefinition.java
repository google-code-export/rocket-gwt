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

import java.util.Collection;
import java.util.Iterator;

import rocket.util.client.ObjectHelper;

/**
 * Convenient base class for the list and set property definitions.
 * 
 * @author Miroslav Pokory
 */
abstract public class CollectionPropertyValueDefinition extends PropertyValueDefinition {

	protected CollectionPropertyValueDefinition() {
		super();

		this.setValues(this.createCollection());
	}

	/**
	 * Accumulates values for this collection(aka List or Set)
	 */
	private Collection values;

	public Collection getValues() {
		ObjectHelper.checkNotNull("field:values", values);
		return this.values;
	}

	public void setValues(final Collection values) {
		ObjectHelper.checkNotNull("parameter:values", values);
		this.values = values;
	}

	abstract protected Collection createCollection();

	public void add(final PropertyValueDefinition value) {
		this.getValues().add(value);
	}

	public String generatePropertyValueCodeBlock() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("new ");
		buffer.append(this.getBuilderTypeName());
		buffer.append("()");

		final Iterator values = this.getValues().iterator();
		while (values.hasNext()) {
			buffer.append(".add(");

			final PropertyValueDefinition value = (PropertyValueDefinition) values.next();
			buffer.append(value.generatePropertyValueCodeBlock());

			buffer.append(")");
		}

		buffer.append(".");
		buffer.append(this.getCollectionGetterName());
		buffer.append("()");

		return buffer.toString();
	}

	abstract protected String getBuilderTypeName();

	abstract protected String getCollectionGetterName();
}
