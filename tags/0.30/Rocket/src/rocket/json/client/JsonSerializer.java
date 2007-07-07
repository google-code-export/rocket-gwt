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
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
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
 * <li>For {@link java.util.List} fields a listElementType annotation must be
 * included to note the element type.</li>
 * <li>For {@link java.util.Set} fields a setElementType annotation must be
 * included to note the element type.</li>
 * <li>For {@link java.util.Map} fields a mapValueType annotation must be
 * included to note the element type. Map keys are always Strings</li>
 * </ul>
 * 
 * @author Miroslav Pokorny
 */
abstract public class JsonSerializer {

	/**
	 * Deserializes the given jsonValue into a java object
	 * 
	 * @param jsonValue
	 * @return
	 */
	abstract public Object asObject(JSONValue jsonValue);

	protected final ListBuilder listBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return JsonSerializer.this.asObject(jsonValue);
		}
	};

	/**
	 * Deserializes a jsonValue into a list of java objects.
	 * 
	 * @param jsonValue
	 * @return
	 */
	public List asList(JSONValue jsonValue) {
		return this.listBuilder.buildList(jsonValue);
	}

	protected final SetBuilder setBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return JsonSerializer.this.asObject(jsonValue);
		}
	};

	/**
	 * Deserializes a jsonValue into a set of java objects.
	 * 
	 * @param jsonValue
	 * @return
	 */
	// abstract public Set asSet(JSONValue jsonValue);
	public Set asSet(final JSONValue jsonValue) {
		return this.setBuilder.buildSet(jsonValue);
	}

	protected final MapBuilder mapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return JsonSerializer.this.asObject(jsonValue);
		}
	};

	/**
	 * Deserializes a jsonValue into a map containing java objects with String
	 * keys
	 * 
	 * @param jsonValue
	 * @return
	 */
	public Map asMap(JSONValue jsonValue) {
		return this.mapBuilder.buildMap(jsonValue);
	}

	final ListBuilder booleanListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return Boolean.valueOf(JsonSerializer.this.asBoolean(jsonValue));
		}
	};

	/**
	 * Copies all the boolean values from the given json array into a new list
	 * after wrapping them in a Boolean
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asBooleanList(final JSONValue jsonValue) {
		return this.booleanListBuilder.buildList(jsonValue);
	}

	final SetBuilder booleanSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return Boolean.valueOf(JsonSerializer.this.asBoolean(jsonValue));
		}
	};

	/**
	 * Copies all the boolean values from the given json array into a new set
	 * after wrapping them in a Boolean
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asBooleanSet(final JSONValue jsonValue) {
		return this.booleanSetBuilder.buildSet(jsonValue);
	}

	final MapBuilder booleanMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return Boolean.valueOf(JsonSerializer.this.asBoolean(jsonValue));
		}
	};

	/**
	 * Copies all the boolean values from the given json object into a new Map
	 * after wrapping all values in a Boolean
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asBooleanMap(final JSONValue jsonValue) {
		return this.booleanMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a boolean from the given JSONBoolean
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected boolean asBoolean(final JSONValue jsonValue) {
		final JSONBoolean jsonBoolean = jsonValue.isBoolean();
		if (null == jsonBoolean) {
			throwIncompatibleJsonValue(jsonValue, "JSONBoolean");
		}

		return jsonBoolean.booleanValue();
	}

	final ListBuilder byteListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Byte(JsonSerializer.this.asByte(jsonValue));
		}
	};

	/**
	 * Copies all the byte values from the given json array into a new list
	 * after wrapping them in a Byte
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asByteList(final JSONValue jsonValue) {
		return this.byteListBuilder.buildList(jsonValue);
	}

	final SetBuilder byteSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Byte(JsonSerializer.this.asByte(jsonValue));
		}
	};

	/**
	 * Copies all the byte values from the given json array into a new set after
	 * wrapping them in a Byte
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asByteSet(final JSONValue jsonValue) {
		return this.byteSetBuilder.buildSet(jsonValue);
	}

	final MapBuilder byteMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return new Byte(JsonSerializer.this.asByte(jsonValue));
		}
	};

	/**
	 * Copies all the byte values from the given json object into a new Map
	 * after wrapping all values in a Byte
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asByteMap(final JSONValue jsonValue) {
		return this.byteMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a byte from the given JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected byte asByte(final JSONValue jsonValue) {
		final JSONNumber jsonNumber = jsonValue.isNumber();
		if (null == jsonNumber) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (byte) jsonNumber.getValue();
	}

	final ListBuilder shortListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Short(JsonSerializer.this.asShort(jsonValue));
		}
	};

	/**
	 * Copies all the short values from the given json array into a new list
	 * after wrapping them in a Short
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asShortList(final JSONValue jsonValue) {
		return this.shortListBuilder.buildList(jsonValue);
	}

	final SetBuilder shortSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Short(JsonSerializer.this.asShort(jsonValue));
		}
	};

	/**
	 * Copies all the short values from the given json array into a new set
	 * after wrapping them in a Short
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asShortSet(final JSONValue jsonValue) {
		return this.shortSetBuilder.buildSet(jsonValue);
	}

	final MapBuilder shortMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return new Short(JsonSerializer.this.asShort(jsonValue));
		}
	};

	/**
	 * Copies all the short values from the given json object into a new Map
	 * after wrapping all values in a Short
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asShortMap(final JSONValue jsonValue) {
		return this.shortMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a short from the given JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected short asShort(final JSONValue jsonValue) {
		final JSONNumber jsonNumber = jsonValue.isNumber();
		if (null == jsonNumber) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (short) jsonNumber.getValue();
	}

	final ListBuilder integerListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Integer(JsonSerializer.this.asInt(jsonValue));
		}
	};

	/**
	 * Copies all the int values from the given json array into a new list after
	 * wrapping them in a Integer
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asIntegerList(final JSONValue jsonValue) {
		return this.integerListBuilder.buildList(jsonValue);
	}

	final SetBuilder integerSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Integer(JsonSerializer.this.asInt(jsonValue));
		}
	};

	/**
	 * Copies all the int values from the given json array into a new set after
	 * wrapping them in a Integer
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asIntegerSet(final JSONValue jsonValue) {
		return this.integerSetBuilder.buildSet(jsonValue);
	}

	protected final MapBuilder integerMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return new Integer(JsonSerializer.this.asInt(jsonValue));
		}
	};

	/**
	 * Copies all the int values from the given json object into a new Map after
	 * wrapping all values in a Integer
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asIntegerMap(final JSONValue jsonValue) {
		return this.integerMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a int from the given JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected int asInt(final JSONValue jsonValue) {
		final JSONNumber jsonNumber = jsonValue.isNumber();
		if (null == jsonNumber) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (int) jsonNumber.getValue();
	}

	final ListBuilder longListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Long(JsonSerializer.this.asLong(jsonValue));
		}
	};

	/**
	 * Copies all the long values from the given json array into a new list
	 * after wrapping them in a Long
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asLongList(final JSONValue jsonValue) {
		return this.longListBuilder.buildList(jsonValue);
	}

	final SetBuilder longSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Long(JsonSerializer.this.asLong(jsonValue));
		}
	};

	/**
	 * Copies all the long values from the given json array into a new set after
	 * wrapping them in a Long
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asLongSet(final JSONValue jsonValue) {
		return this.longSetBuilder.buildSet(jsonValue);
	}

	protected final MapBuilder longMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return new Long(JsonSerializer.this.asLong(jsonValue));
		}
	};

	/**
	 * Copies all the long values from the given json object into a new Map
	 * after wrapping all values in a Long
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asLongMap(final JSONValue jsonValue) {
		return this.longMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a long from the given JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected long asLong(final JSONValue jsonValue) {
		final JSONNumber jsonNumber = jsonValue.isNumber();
		if (null == jsonNumber) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (long) jsonNumber.getValue();
	}

	final ListBuilder floatListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Float(JsonSerializer.this.asFloat(jsonValue));
		}
	};

	/**
	 * Copies all the float values from the given json array into a new list
	 * after wrapping them in a Float
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asFloatList(final JSONValue jsonValue) {
		return this.floatListBuilder.buildList(jsonValue);
	}

	final SetBuilder floatSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Float(JsonSerializer.this.asFloat(jsonValue));
		}
	};

	/**
	 * Copies all the float values from the given json array into a new set
	 * after wrapping them in a Float
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asFloatSet(final JSONValue jsonValue) {
		return this.floatSetBuilder.buildSet(jsonValue);
	}

	protected final MapBuilder floatMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return new Float(JsonSerializer.this.asFloat(jsonValue));
		}
	};

	/**
	 * Copies all the float values from the given json object into a new Map
	 * after wrapping all values in a Float
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asFloatMap(final JSONValue jsonValue) {
		return this.floatMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a float from the given JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected float asFloat(final JSONValue jsonValue) {
		final JSONNumber jsonNumber = jsonValue.isNumber();
		if (null == jsonNumber) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (float) jsonNumber.getValue();
	}

	final ListBuilder doubleListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Double(JsonSerializer.this.asDouble(jsonValue));
		}
	};

	/**
	 * Copies all the double values from the given json array into a new list
	 * after wrapping them in a Double
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asDoubleList(final JSONValue jsonValue) {
		return this.doubleListBuilder.buildList(jsonValue);
	}

	final SetBuilder doubleSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Double(JsonSerializer.this.asDouble(jsonValue));
		}
	};

	/**
	 * Copies all the double values from the given json array into a new set
	 * after wrapping them in a Double
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asDoubleSet(final JSONValue jsonValue) {
		return this.doubleSetBuilder.buildSet(jsonValue);
	}

	protected final MapBuilder doubleMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return new Double(JsonSerializer.this.asDouble(jsonValue));
		}
	};

	/**
	 * Copies all the double values from the given json object into a new Map
	 * after wrapping all values in a Double
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asDoubleMap(final JSONValue jsonValue) {
		return this.doubleMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a double from the given JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected double asDouble(final JSONValue jsonValue) {
		final JSONNumber jsonNumber = jsonValue.isNumber();
		if (null == jsonNumber) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (double) jsonNumber.getValue();
	}

	final ListBuilder charListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Character(JsonSerializer.this.asChar(jsonValue));
		}
	};

	/**
	 * Copies all the char values from the given json array into a new list
	 * after wrapping them in a Character
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asCharacterList(final JSONValue jsonValue) {
		return this.charListBuilder.buildList(jsonValue);
	}

	final SetBuilder charSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return new Character(JsonSerializer.this.asChar(jsonValue));
		}
	};

	/**
	 * Copies all the char values from the given json array into a new set after
	 * wrapping them in a Character
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asCharacterSet(final JSONValue jsonValue) {
		return this.charSetBuilder.buildSet(jsonValue);
	}

	protected final MapBuilder characterMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return new Character(JsonSerializer.this.asChar(jsonValue));
		}
	};

	/**
	 * Copies all the char values from the given json object into a new Map
	 * after wrapping all values in a Character
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asCharacterMap(final JSONValue jsonValue) {
		return this.characterMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a char from the given JSONString
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected char asChar(final JSONValue jsonValue) {
		char charValue = 0;

		if (null == jsonValue.isNull()) {
			final JSONString jsonString = jsonValue.isString();
			if (null == jsonString) {
				throwIncompatibleJsonValue(jsonValue, "JSONString");
			}
			final String stringValue = jsonString.stringValue();
			if (null == stringValue || stringValue.length() != 1) {
				throwIncompatibleJsonValue(jsonValue, "JSONString");
			}
			charValue = stringValue.charAt(0);
		}

		return charValue;
	}

	final ListBuilder stringListBuilder = new ListBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return JsonSerializer.this.asString(jsonValue);
		}
	};

	/**
	 * Copies all the String values from the given json array into a new list
	 * after extracting the string values
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asStringList(final JSONValue jsonValue) {
		return this.stringListBuilder.buildList(jsonValue);
	}

	final SetBuilder stringSetBuilder = new SetBuilder() {
		protected Object visitElement(final JSONValue jsonValue) {
			return JsonSerializer.this.asString(jsonValue);
		}
	};

	/**
	 * Copies all the String values from the given json array into a new set
	 * after extracting the string values
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asStringSet(final JSONValue jsonValue) {
		return this.stringSetBuilder.buildSet(jsonValue);
	}

	protected final MapBuilder stringMapBuilder = new MapBuilder() {
		protected Object visitValue(final JSONValue jsonValue) {
			return JsonSerializer.this.asString(jsonValue);
		}
	};

	/**
	 * Copies all the string values from the given json object into a new Map
	 * after wrapping all values in a String
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asStringMap(final JSONValue jsonValue) {
		return this.stringMapBuilder.buildMap(jsonValue);
	}

	/**
	 * Extracts a string from the given JSONString
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected String asString(final JSONValue jsonValue) {
		String stringValue = null;

		if (null != jsonValue && null == jsonValue.isNull()) {
			final JSONString jsonString = jsonValue.isString();
			if (null == jsonString) {
				throwIncompatibleJsonValue(jsonValue, "JSONString");
			}
			stringValue = jsonString.stringValue();
		}

		return stringValue;
	}

	/**
	 * Convenient method that creates a JsonDeserializationException to report
	 * any problem whilst deserializing a json encoded String into java objects.
	 * 
	 * @param jsonValue
	 * @param expected
	 */
	protected void throwIncompatibleJsonValue(final JSONValue jsonValue, final String expected) {
		throw new JsonDeserializationException("Whilst deserializing found " + jsonValue + " but expected a " + expected);
	}

	/**
	 * A helper which loops over the elements of a jsonArray creating the
	 * corresponding java object.
	 */
	abstract class CollectionBuilder {
		protected Collection buildCollection(final JSONValue jsonValue) {
			Collection collection = null;
			if (null != jsonValue) {
				final JSONArray jsonArray = jsonValue.isArray();
				if (null == jsonArray) {
					JsonSerializer.this.throwIncompatibleJsonValue(jsonValue, "JSONArray");
				}

				collection = this.createCollection();

				final int size = jsonArray.size();
				for (int i = 0; i < size; i++) {
					collection.add((this.visitElement(jsonArray.get(i))));
				}
			}
			return collection;
		}

		abstract protected Collection createCollection();

		abstract protected Object visitElement(final JSONValue jsonValue);
	}

	/**
	 * Defers from CollectionBuilder in that the returned Collection is actually
	 * a List
	 */
	abstract class ListBuilder extends CollectionBuilder {

		public List buildList(final JSONValue jsonValue) {
			return (List) this.buildCollection(jsonValue);
		}

		protected Collection createCollection() {
			return new ArrayList();
		}
	}

	/**
	 * Defers from CollectionBuilder in that the returned Collection is actually
	 * a Set
	 */
	abstract class SetBuilder extends CollectionBuilder {

		public Set buildSet(final JSONValue jsonValue) {
			return (Set) this.buildCollection(jsonValue);
		}

		protected Collection createCollection() {
			return new HashSet();
		}
	}

	/**
	 * Visits all values from the given JSONObject and proceeds to create java
	 * objects for each value. The key stored in the map remains a String.
	 */
	abstract class MapBuilder {
		protected Map buildMap(final JSONValue jsonValue) {
			Map map = null;
			if (null != jsonValue) {

				final JSONObject jsonObject = jsonValue.isObject();
				if (null == jsonObject) {
					throwIncompatibleJsonValue(jsonValue, "JSONObject");
				}

				map = new HashMap();

				final Iterator keys = jsonObject.keySet().iterator();
				while (keys.hasNext()) {
					final String key = (String) keys.next();
					map.put(key, this.visitValue(jsonObject.get(key)));
				}
			}

			return map;
		}

		abstract Object visitValue(JSONValue jsonValue);
	}
}
