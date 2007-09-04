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
package rocket.collection.client;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.util.client.ObjectHelper;

/**
 * A collection of methods that are often used when working with Collections.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CollectionHelper {
	/**
	 * Visits all elements returns by the given iterator removing each one.
	 * 
	 * @param iterator
	 */
	public static void removeAll(final Iterator iterator) {
		ObjectHelper.checkNotNull("parameter:iterator", iterator);

		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	/**
	 * Copies all the elements from the iterator into an array.
	 * 
	 * @param iterator
	 * @return
	 */
	public static Object[] toArray(final Iterator iterator) {
		ObjectHelper.checkNotNull("parameter:iterator", iterator);

		final List list = new ArrayList();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list.toArray();
	}

	/**
	 * Copies all the elements from the iterator into a List
	 * 
	 * @param iterator
	 * @return
	 */
	public static List toList(final Iterator iterator) {
		ObjectHelper.checkNotNull("parameter:iterator", iterator);

		final List list = new ArrayList();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	/**
	 * Returns an unmodifiable view of a List. Using this reference the given
	 * list may not be modified in any manner, with its references remaining
	 * constant and with the list only being modifiable using the original list
	 * reference.
	 * 
	 * @param list
	 * @return The read only list. Attempts to modify will throw
	 *         UnsupportedOperationException...
	 */
	public static List unmodifiableList(final List list) {
		return new AbstractList() {

			// LIST MODIFIERS
			// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

			public boolean add(Object element) {
				throw new UnsupportedOperationException("An unmodifable List may not have an element add(Object)");
			}

			public void add(int arg0, Object element) {
				throw new UnsupportedOperationException("An unmodifable List may not have an element add(int,Object)");
			}

			public Object set(final int index, final Object element) {
				throw new UnsupportedOperationException("An unmodifable List may not have an element set()");
			}

			public Object remove(final int index) {
				throw new UnsupportedOperationException("An unmodifable List may not have an element removed(index)");
			}

			public boolean remove(final Object element) {
				throw new UnsupportedOperationException("An unmodifable List may not have an element removed(Object)");
			}

			public Iterator iterator() {
				final Iterator iterator = list.iterator();
				return new Iterator() {
					public boolean hasNext() {
						return iterator.hasNext();
					}

					public Object next() {
						return iterator.next();
					}

					public void remove() {
						throw new UnsupportedOperationException("An unmodifable List's iterator may not have an element removed()");
					}
				};
			}

			// DELEGATE TO ENCAPSULATED LIST::::::::::::::::::

			public int size() {
				return list.size();
			}

			public boolean contains(final Object object) {
				return list.contains(object);
			}

			public Object[] toArray() {
				return list.toArray();
			}

			public boolean containsAll(final Collection collection) {
				return list.containsAll(collection);
			}

			public boolean retainAll(Collection collection) {
				return list.containsAll(collection);
			}

			public Object get(final int index) {
				return list.get(index);
			}

			public int indexOf(final Object object) {
				return list.indexOf(object);
			}

			public int lastIndexOf(Object object) {
				return list.lastIndexOf(object);
			}
		};
	}

	/**
	 * Searches the given map for the key that contains the value
	 * parameter:value.
	 * 
	 * @param map
	 * @param value
	 * @return The key or null if value is not present in the parameter:map
	 */
	public static Object getKey(final Map map, final Object value) {
		ObjectHelper.checkNotNull("parameter:map", map);
		ObjectHelper.checkNotNull("parameter:value", value);

		Object key = null;
		final Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();
			if (value == entry.getValue()) {
				key = entry.getKey();
				break;
			}
		}
		return key;
	}
}
