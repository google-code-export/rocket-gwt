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

	protected void setFields(final Object instance, final JSONValue jsonValue) {
	}

	/**
	 * Deserializes a jsonValue into a list of java objects.
	 * 
	 * @param jsonValue
	 * @return
	 */
	public List asList(final JSONValue jsonValue) {
		return JsonSerializer.listBuilder.buildList(this, jsonValue);
	}

	static private ListBuilder listBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return serializer.asObject(jsonValue);
		}
	};

	/**
	 * Deserializes a jsonValue into a set of java objects.
	 * 
	 * @param jsonValue
	 * @return
	 */
	public Set asSet(final JSONValue jsonValue) {
		return JsonSerializer.setBuilder.buildSet(this, jsonValue);
	}

	static private final SetBuilder setBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return serializer.asObject(jsonValue);
		}
	};

	/**
	 * Deserializes a jsonValue into a map containing java objects with String
	 * keys
	 * 
	 * @param jsonValue
	 * @return
	 */
	public Map asMap(final JSONValue jsonValue) {
		return JsonSerializer.mapBuilder.buildMap(this, jsonValue);
	}

	static private final MapBuilder mapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return serializer.asObject(jsonValue);
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
		return JsonSerializer.booleanListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder booleanListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return Boolean.valueOf(serializer.asBoolean(jsonValue));
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
		return JsonSerializer.booleanSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder booleanSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return Boolean.valueOf(serializer.asBoolean(jsonValue));
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
		return JsonSerializer.booleanMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder booleanMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return Boolean.valueOf(serializer.asBoolean(jsonValue));
		}
	};

	/**
	 * Extracts a double from the given JSONValue which is assumed to be a
	 * JSONBoolean
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

	/**
	 * Copies all the byte values from the given json array into a new list
	 * after wrapping them in a Byte
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asByteList(final JSONValue jsonValue) {
		return JsonSerializer.byteListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder byteListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Byte(serializer.asByte(jsonValue));
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
		return JsonSerializer.byteSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder byteSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Byte(serializer.asByte(jsonValue));
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
		return JsonSerializer.byteMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder byteMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Byte(serializer.asByte(jsonValue));
		}
	};

	/**
	 * Extracts a byte from the given JSONValue which is assumed to be a
	 * JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected byte asByte(final JSONValue jsonValue) {
		final JSONNumber jsonByte = jsonValue.isNumber();
		if (null == jsonByte) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (byte) jsonByte.getValue();
	}

	/**
	 * Copies all the short values from the given json array into a new list
	 * after wrapping them in a Short
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asShortList(final JSONValue jsonValue) {
		return JsonSerializer.shortListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder shortListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Short(serializer.asShort(jsonValue));
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
		return JsonSerializer.shortSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder shortSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Short(serializer.asShort(jsonValue));
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
		return JsonSerializer.shortMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder shortMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Short(serializer.asShort(jsonValue));
		}
	};

	/**
	 * Extracts a short from the given JSONValue which is assumed to be a
	 * JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected short asShort(final JSONValue jsonValue) {
		final JSONNumber jsonShort = jsonValue.isNumber();
		if (null == jsonShort) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (short) jsonShort.getValue();
	}

	/**
	 * Copies all the int values from the given json array into a new list after
	 * wrapping them in a Int
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asIntegerList(final JSONValue jsonValue) {
		return JsonSerializer.intListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder intListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Integer(serializer.asInt(jsonValue));
		}
	};

	/**
	 * Copies all the int values from the given json array into a new set after
	 * wrapping them in a Int
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asIntegerSet(final JSONValue jsonValue) {
		return JsonSerializer.intSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder intSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Integer(serializer.asInt(jsonValue));
		}
	};

	/**
	 * Copies all the int values from the given json object into a new Map after
	 * wrapping all values in a Int
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asIntegerMap(final JSONValue jsonValue) {
		return JsonSerializer.intMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder intMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Integer(serializer.asInt(jsonValue));
		}
	};

	/**
	 * Extracts a int from the given JSONValue which is assumed to be a
	 * JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected int asInt(final JSONValue jsonValue) {
		final JSONNumber jsonInt = jsonValue.isNumber();
		if (null == jsonInt) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (int) jsonInt.getValue();
	}

	/**
	 * Copies all the long values from the given json array longo a new list
	 * after wrapping them in a Long
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asLongList(final JSONValue jsonValue) {
		return JsonSerializer.longListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder longListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Long(serializer.asLong(jsonValue));
		}
	};

	/**
	 * Copies all the long values from the given json array longo a new set
	 * after wrapping them in a Long
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asLongSet(final JSONValue jsonValue) {
		return JsonSerializer.longSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder longSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Long(serializer.asLong(jsonValue));
		}
	};

	/**
	 * Copies all the long values from the given json object longo a new Map
	 * after wrapping all values in a Long
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Map asLongMap(final JSONValue jsonValue) {
		return JsonSerializer.longMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder longMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Long(serializer.asLong(jsonValue));
		}
	};

	/**
	 * Extracts a long from the given JSONValue which is assumed to be a
	 * JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected long asLong(final JSONValue jsonValue) {
		final JSONNumber jsonLong = jsonValue.isNumber();
		if (null == jsonLong) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (long) jsonLong.getValue();
	}

	/**
	 * Copies all the float values from the given json array into a new list
	 * after wrapping them in a Float
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asFloatList(final JSONValue jsonValue) {
		return JsonSerializer.floatListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder floatListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Float(serializer.asFloat(jsonValue));
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
		return JsonSerializer.floatSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder floatSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Float(serializer.asFloat(jsonValue));
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
		return JsonSerializer.floatMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder floatMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Float(serializer.asFloat(jsonValue));
		}
	};

	/**
	 * Extracts a float from the given JSONValue which is assumed to be a
	 * JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected float asFloat(final JSONValue jsonValue) {
		final JSONNumber jsonFloat = jsonValue.isNumber();
		if (null == jsonFloat) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (float) jsonFloat.getValue();
	}

	/**
	 * Copies all the double values from the given json array into a new list
	 * after wrapping them in a Double
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asDoubleList(final JSONValue jsonValue) {
		return JsonSerializer.doubleListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder doubleListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Double(serializer.asDouble(jsonValue));
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
		return JsonSerializer.doubleSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder doubleSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Double(serializer.asDouble(jsonValue));
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
		return JsonSerializer.doubleMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder doubleMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Double(serializer.asDouble(jsonValue));
		}
	};

	/**
	 * Extracts a double from the given JSONValue which is assumed to be a
	 * JSONNumber
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected double asDouble(final JSONValue jsonValue) {
		final JSONNumber jsonDouble = jsonValue.isNumber();
		if (null == jsonDouble) {
			throwIncompatibleJsonValue(jsonValue, "JSONNumber");
		}

		return (double) jsonDouble.getValue();
	}

	/**
	 * Copies all the char values from the given json array into a new list
	 * after wrapping them in a Character
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asCharacterList(final JSONValue jsonValue) {
		return JsonSerializer.charListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder charListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Character(serializer.asChar(jsonValue));
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
		return JsonSerializer.charSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder charSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Character(serializer.asChar(jsonValue));
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
		return JsonSerializer.charMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder charMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new Character(serializer.asChar(jsonValue));
		}
	};

	/**
	 * Extracts a char from the given JSONValue which is assumed to be
	 * JSONString
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

	/**
	 * Copies all the string values from the given json array into a new list
	 * after wrapping them in a String
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected List asStringList(final JSONValue jsonValue) {
		return JsonSerializer.stringListBuilder.buildList(this, jsonValue);
	}

	static final private ListBuilder stringListBuilder = new ListBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new String(serializer.asString(jsonValue));
		}
	};

	/**
	 * Copies all the string values from the given json array into a new set
	 * after wrapping them in a String
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected Set asStringSet(final JSONValue jsonValue) {
		return JsonSerializer.stringSetBuilder.buildSet(this, jsonValue);
	}

	static final private SetBuilder stringSetBuilder = new SetBuilder() {
		protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new String(serializer.asString(jsonValue));
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
		return JsonSerializer.stringMapBuilder.buildMap(this, jsonValue);
	}

	static final private MapBuilder stringMapBuilder = new MapBuilder() {
		protected Object visitValue(final JsonSerializer serializer, final JSONValue jsonValue) {
			return new String(serializer.asString(jsonValue));
		}
	};

	/**
	 * Extracts a string from the given JSONValue which is assumed to be
	 * JSONString
	 * 
	 * @param jsonValue
	 * @return
	 */
	protected String asString(final JSONValue jsonValue) {
		String string = null;

		if (null != jsonValue) {
			final JSONString jsonString = jsonValue.isString();
			if (null != jsonString) {
				string = jsonString.stringValue();
			}
		}
		return string;
	}

	/**
	 * Convenient method that creates a JsonDeserializationException to report
	 * any problem whilst deserializing a json encoded String into java objects.
	 * 
	 * @param jsonValue
	 * @param expected
	 */
	static protected void throwIncompatibleJsonValue(final JSONValue jsonValue, final String expected) {
		throw new JsonDeserializationException("Whilst deserializing found " + jsonValue + " but expected a " + expected);
	}

	/**
	 * A helper which loops over the elements of a jsonArray creating the
	 * corresponding java object.
	 */
	static abstract class CollectionBuilder {
		protected Collection buildCollection(final JsonSerializer serializer, final JSONValue jsonValue) {
			Collection collection = null;
			if (null != jsonValue) {
				final JSONArray jsonArray = jsonValue.isArray();
				if (null == jsonArray) {
					JsonSerializer.throwIncompatibleJsonValue(jsonValue, "JSONArray");
				}

				collection = this.createCollection();

				final int size = jsonArray.size();
				for (int i = 0; i < size; i++) {
					collection.add((this.visitElement(serializer, jsonArray.get(i))));
				}
			}
			return collection;
		}

		abstract protected Collection createCollection();

		abstract protected Object visitElement(final JsonSerializer serializer, final JSONValue jsonValue);
	}

	/**
	 * Defers from CollectionBuilder in that the returned Collection is actually
	 * a List
	 */
	static abstract class ListBuilder extends CollectionBuilder {

		public List buildList(final JsonSerializer serializer, final JSONValue jsonValue) {
			return (List) this.buildCollection(serializer, jsonValue);
		}

		protected Collection createCollection() {
			return new ArrayList();
		}
	}

	/**
	 * Defers from CollectionBuilder in that the returned Collection is actually
	 * a Set
	 */
	static abstract class SetBuilder extends CollectionBuilder {

		public Set buildSet(final JsonSerializer serializer, final JSONValue jsonValue) {
			return (Set) this.buildCollection(serializer, jsonValue);
		}

		protected Collection createCollection() {
			return new HashSet();
		}
	}

	/**
	 * Visits all values from the given JSONObject and proceeds to create java
	 * objects for each value. The key stored in the map remains a String.
	 */
	static abstract class MapBuilder {
		protected Map buildMap(final JsonSerializer serializer, final JSONValue jsonValue) {
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
					map.put(key, this.visitValue(serializer, jsonObject.get(key)));
				}
			}

			return map;
		}

		abstract Object visitValue(JsonSerializer serializer, JSONValue jsonValue);
	}
}
