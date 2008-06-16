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
abstract public class CollectionBuilder<E> {

	protected CollectionBuilder() {
		super();

		this.setCollection(this.createCollection());
	}

	public CollectionBuilder<E> add(final boolean booleanValue) {
		return this.add(Boolean.valueOf(booleanValue));
	}

	public CollectionBuilder<E> add(final byte byteValue) {
		return this.add(new Byte(byteValue));
	}

	public CollectionBuilder<E> add(final short shortValue) {
		return this.add(new Short(shortValue));
	}

	public CollectionBuilder<E> add(final int intValue) {
		return this.add(new Integer(intValue));
	}

	public CollectionBuilder<E> add(final long longValue) {
		return this.add(new Long(longValue));
	}

	public CollectionBuilder<E> add(final float floatValue) {
		return this.add(new Float(floatValue));
	}

	public CollectionBuilder<E> add(final double doubleValue) {
		return this.add(new Double(doubleValue));
	}

	public CollectionBuilder<E> add(final char charValue) {
		return this.add(new Character(charValue));
	}

	public CollectionBuilder<E> add(final E object) {
		this.getCollection().add(object);
		return this;
	}

	/**
	 * A collection (list or set) containing values.
	 */
	private Collection<E> collection;

	protected Collection<E> getCollection() {
		Checker.notNull("field:collection", collection);
		return this.collection;
	}

	protected void setCollection(final Collection<E> collection) {
		Checker.notNull("parameter:collection", collection);
		this.collection = collection;
	}

	abstract protected Collection<E> createCollection();

	public List<E> getList() {
		return (List<E>) this.getCollection();
	}

	public Set<E> getSet() {
		return (Set<E>) this.getCollection();
	}
}
