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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.util.client.Checker;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A Map like class that records multiple values for a key. Duplicate values are
 * accepted.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MultiValueMap<K,V> implements IsSerializable {

	public MultiValueMap() {
		this.setMap(new HashMap<K,List<V>>());
	}

	private Map<K,List<V>> map;

	protected Map<K,List<V>> getMap() {
		Checker.notNull("field:map", map);

		return map;
	}

	protected void setMap(final Map<K,List<V>> map) {
		Checker.notNull("parameter:map", map);

		this.map = map;
	}

	public Iterator<K> keys() {
		return this.getMap().keySet().iterator();
	}

	public boolean contains(final K key) {
		return this.getMap().containsKey(key);
	}

	public Object getFirstValue(final K key) {
		final Object[] values = this.getValues(key);
		return values != null && values.length > 0 ? values[0] : null;
	}

	public Object[] getValues(final K key) {
		Checker.notNull("parameter:key", key);

		Object[] values = null;
		List<V> list = (List<V>) this.getMap().get(key);
		if (null != list && false == list.isEmpty()) {
			final int size = list.size();
			values = new Object[size];

			final Iterator<V> iterator = list.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				values[i++] = iterator.next();
			}
		}

		return values;
	}

	public List<V> getValuesList(final K key) {
		Checker.notNull("parameter:key", key);

		return (List<V>) this.getMap().get(key);
	}

	public void add(final K key, final V value) {
		final Map<K,List<V>> map = this.getMap();
		List<V> values = (List<V>) this.getValuesList(key);
		if (values == null) {
			values = new ArrayList<V>();
			map.put(key, values);
		}
		values.add(value);
	}

	public void clear() {
		this.getMap().clear();
	}

	public List<V> remove(final K key) {
		return (List<V>) this.getMap().remove(key);
	}

	public String toString() {
		return super.toString() + ", map: " + map;
	}
}