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
package rocket.json.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * This interface is implemented by all JsonSerializers that can map a JSONValue
 * to a java object and back.
 * 
 * <ul>
 * <li>All serializable objects must implement {@link JsonSerializable}</li>
 * <li>All primitive types and String are automatically serializable</li>
 * <li>Static and transient fields are skipped just like
 * {@link java.io.Serializable}</li>
 * <li>For {@link java.util.List}, {@link java.util.Set} or {@link java.util.Map} fields a jsonSerializer-type </li>
 * <li>Map keys are assumed to always be {@link java.lang.String}
 * </ul>
 * 
 * This class should not be referenced in code and only exists as a convenient
 * base class for the JsonSerializerGenerator.
 * 
 * @author Miroslav Pokorny
 */
abstract public class JsonSerializer {

	/**
	 * Deserializes the given jsonValue into a java object
	 * 
	 * @param jsonValue
	 * @return The deserialized read object.
	 */
	abstract public Object readObject(JSONValue jsonValue);

	public List readList(final JSONValue jsonValue) {
		final List list = new ArrayList();
		this.readCollection(jsonValue.isArray(), list);
		return list;
	}

	public Set readSet(final JSONValue jsonValue) {
		final Set set = new HashSet();
		this.readCollection(jsonValue.isArray(), set);
		return set;
	}

	protected void readCollection(final JSONArray jsonArray, final Collection collection) {
		if (null != jsonArray) {
			final int size = jsonArray.size();
			for (int i = 0; i < size; i++) {
				final JSONValue element = jsonArray.get(i);
				collection.add(this.readObject(element));
			}
		}
	}

	public Map readMap(final JSONValue jsonValue) {
		final Map map = new HashMap();
		if (null != jsonValue) {
			final JSONObject object = jsonValue.isObject();
			final Iterator keys = object.keySet().iterator();
			while (keys.hasNext()) {
				final String key = (String) keys.next();
				final JSONValue value = (JSONValue) object.get(key);

				final Object mapValue = this.readObject(value);

				map.put(key, mapValue);
			}
		}
		return map;
	}

	/**
	 * Sub classes must override this method to read all the properties from the
	 * json value and set fields upon instance.
	 * 
	 * @param jsonValue
	 * @param instance
	 */
	protected void readFields(final JSONValue jsonValue, final Object instance) {
	}

	/**
	 * Serializes a java object into a jsonObject
	 * 
	 * @param instance
	 * @return A JSONValue holding the json form.
	 */
	abstract public JSONValue writeJson(Object instance);

	public JSONValue writeJson(final Collection collection) {
		JSONValue jsonValue = null;
		if (null == collection) {
			jsonValue = JSONNull.getInstance();
		} else {
			final JSONArray array = new JSONArray();
			int i = 0;

			final Iterator iterator = collection.iterator();
			while (iterator.hasNext()) {
				final Object element = iterator.next();

				array.set(i, this.writeJson(element));
				i++;
			}

			jsonValue = array;
		}
		return jsonValue;
	}

	public JSONValue writeJson(final Map map) {
		JSONValue jsonValue = null;
		if (null == map) {
			jsonValue = JSONNull.getInstance();
		} else {
			final JSONObject object = new JSONObject();

			final Iterator entries = map.entrySet().iterator();
			while (entries.hasNext()) {
				final Map.Entry entry = (Map.Entry) entries.next();
				final String key = (String) entry.getKey();
				final Object element = entry.getValue();

				object.put(key, this.writeJson(element));
			}

			jsonValue = object;
		}
		return jsonValue;
	}

	/**
	 * Sub classes must override this method to add all the fields from the
	 * given instance to the json object.
	 * 
	 * @param instance
	 * @param jsonObject
	 */
	protected void writeFields(final Object instance, final JSONObject jsonObject) {
	}

	/**
	 * Null safe method to retrieve the double value from a jsonValue which may
	 * or may not be a JSONNumber. If its null or not a JSONNumber 0 is
	 * returned.
	 * 
	 * @param jsonValue
	 * @return
	 */
	double readDouble(final JSONValue jsonValue) {
		double value = 0;

		while (true) {
			if (null == jsonValue) {
				break;
			}
			final JSONNumber number = jsonValue.isNumber();
			if (null == number) {
				break;
			}

			value = number.getValue();
			break;
		}

		return value;
	}
}
