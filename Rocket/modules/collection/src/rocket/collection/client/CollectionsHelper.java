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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.util.client.Checker;

/**
 * A collection of missing methods that have not yet been implemented in the
 * emulated Collections as well as other useful collection related methods.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CollectionsHelper {

	/**
	 * Removes all the remaining elements of the given iterator by visiting and
	 * removing each and every element.
	 * 
	 * @param iterator
	 *            The iterator being cleared
	 */
	public static void removeAll(final Iterator iterator) {
		Checker.notNull("parameter:iterator", iterator);

		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	/**
	 * Copies all the elements from the iterator into an array.
	 * 
	 * @param iterator
	 *            The source
	 * @return The new array containing the values
	 */
	public static Object[] copyIntoArray(final Iterator iterator) {
		return CollectionsHelper.copyIntoList(iterator).toArray();
	}

	/**
	 * Copies all the elements from the iterator into a List
	 * 
	 * @param iterator
	 *            The source
	 * @return The filled List
	 */
	public static List copyIntoList(final Iterator iterator) {
		Checker.notNull("parameter:iterator", iterator);

		final List list = new ArrayList();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	/**
	 * Searches the given map for the key that contains the parameter value
	 * 
	 * @param map
	 *            The map
	 * @param value
	 *            The value being searched for
	 * @return The key or null if value is not present in the parameter:map
	 */
	public static Object getKey(final Map map, final Object value) {
		Checker.notNull("parameter:map", map);
		Checker.notNull("parameter:value", value);

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
