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
 * A collection of missing methods that have not yet been implemented in the emulated Collections
 * as well as other useful collection related methods.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CollectionsHelper{
		
	/**
	 * Removes all the remaining elements of the given iterator by visiting and removing each and every element.
	 * 
	 * @param iterator The iterator being cleared
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
	 * @param iterator The source
	 * @return The new array containing the values
	 */
	public static Object[] copyIntoArray(final Iterator iterator) {
		return CollectionsHelper.copyIntoList(iterator).toArray();
	}

	/**
	 * Copies all the elements from the iterator into a List
	 * 
	 * @param iterator The source
	 * @return The filled List
	 */
	public static List copyIntoList(final Iterator iterator) {
		ObjectHelper.checkNotNull("parameter:iterator", iterator);

		final List list = new ArrayList();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	/**
	 * Searches the given map for the key that contains the parameter value
	 * 
	 * @param map The map
	 * @param value The value being searched for
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
	
	/**
	 * Returns an unmodifiable view of a List. 
	 * 
	 * Using this reference the given list may not be modified in any manner, with its references remaining
	 * constant and with the list only being modifiable using the original list
	 * reference.
	 * 
	 * @param list The source list.
	 * @return The read only list. Mutator methods throw UnsupportedOperationException...
	 */
	public static List unmodifiableList(final List list) {
		return new AbstractList() {

			// LIST MODIFIERS
			// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

			public boolean add(Object element) {
				throwUnsupportedOperationException("add(Object)");
				return false;
			}

			public void add(int index, Object element) {
				throwUnsupportedOperationException("add(int,Object)");
			}

			public Object set(final int index, final Object element) {
				throwUnsupportedOperationException("set(int,Object)");
				return null;
			}

			public Object remove(final int index) {
				throwUnsupportedOperationException("remove(int)");
				return null;
			}

			public boolean remove(final Object element) {
				throwUnsupportedOperationException("remove(Object)");
				return false;
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
						throwUnsupportedOperationException( "iterator.remove()");
					}
				};
			}
			
			void throwUnsupportedOperationException( final String methodName ){
				throw new UnsupportedOperationException( "Unmodifiable Lists's do not support the " + methodName + ".");
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
}
