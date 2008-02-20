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
package rocket.beans.client;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import rocket.util.client.Checker;

/**
 * This helper class is used to build a list containing values within generated
 * {@link BeanFactory bean factories}
 * 
 * @author Miroslav Pokorny
 */
abstract public class CollectionBuilder {

	protected CollectionBuilder() {
		super();

		this.setCollection(this.createCollection());
	}

	public CollectionBuilder add(final boolean booleanValue) {
		return this.add(Boolean.valueOf(booleanValue));
	}

	public CollectionBuilder add(final byte byteValue) {
		return this.add(new Byte(byteValue));
	}

	public CollectionBuilder add(final short shortValue) {
		return this.add(new Short(shortValue));
	}

	public CollectionBuilder add(final int intValue) {
		return this.add(new Integer(intValue));
	}

	public CollectionBuilder add(final long longValue) {
		return this.add(new Long(longValue));
	}

	public CollectionBuilder add(final float floatValue) {
		return this.add(new Float(floatValue));
	}

	public CollectionBuilder add(final double doubleValue) {
		return this.add(new Double(doubleValue));
	}

	public CollectionBuilder add(final char charValue) {
		return this.add(new Character(charValue));
	}

	public CollectionBuilder add(final Object object) {
		this.getCollection().add(object);
		return this;
	}

	/**
	 * A collection (list or set) containing values.
	 */
	private Collection collection;

	protected Collection getCollection() {
		Checker.notNull("field:collection", collection);
		return this.collection;
	}

	protected void setCollection(final Collection collection) {
		Checker.notNull("parameter:collection", collection);
		this.collection = collection;
	}

	abstract protected Collection createCollection();

	public List getList() {
		return (List) this.getCollection();
	}

	public Set getSet() {
		return (Set) this.getCollection();
	}
}
