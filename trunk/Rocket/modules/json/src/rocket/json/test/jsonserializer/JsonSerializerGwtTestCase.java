/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.json.test.jsonserializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;
import rocket.json.client.JsonSerializable;
import rocket.json.client.JsonSerializer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * A series of unit tests for the Json package.
 * 
 * @author Miroslav Pokorny
 */
public class JsonSerializerGwtTestCase extends GeneratorGwtTestCase {

	final static String JSON_SERIALIZER_GENERATOR_EXCEPTION = "rocket.json.rebind.JsonSerializerGeneratorException";

	public String getModuleName() {
		return "rocket.json.test.jsonserializer.JsonSerializerGwtTestCase";
	}

	public void testMissingNoArgumentsConstructor() {
		try {
			final Object object = GWT.create(MissingNoArgumentsConstructor.class);
			assertBindingFailed(object);
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(JSON_SERIALIZER_GENERATOR_EXCEPTION));
		}
	}

	static class MissingNoArgumentsConstructor implements JsonSerializable {
		public MissingNoArgumentsConstructor(String string) {
			super();
		}
	}

	public void testReadFinalField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonBoolean);

		try {
			final Object object = GWT.create(HasFinalField.class);
			assertBindingFailed(object);
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(JSON_SERIALIZER_GENERATOR_EXCEPTION));
		}
	}

	static class HasFinalField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		final boolean field = true;
	}

	public void testReadTransientField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasTransientField.class);
		final HasTransientField instance = (HasTransientField) serializer.readObject(jsonObject);

		TestCase.assertTrue(value != instance.field);
	}

	static class HasTransientField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		transient boolean field;
	}

	public void testReadStaticField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStaticField.class);
		final HasStaticField instance = (HasStaticField) serializer.readObject(jsonObject);

		TestCase.assertTrue(value != instance.field);
	}

	static class HasStaticField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		static boolean field;
	}

	public void testReadBooleanField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasBooleanField.class);
		final HasBooleanField instance = (HasBooleanField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteBooleanField() {
		final boolean value = true;

		final HasBooleanField instance = new HasBooleanField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasBooleanField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, jsonObject.get("field").isBoolean().booleanValue());
	}

	static class HasBooleanField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		boolean field;
	}

	public void testReadByteField() {
		final byte value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasByteField.class);
		final HasByteField instance = (HasByteField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteByteField() {
		final byte value = 123;

		final HasByteField instance = new HasByteField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasByteField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (byte) jsonObject.get("field").isNumber().getValue());
	}

	static class HasByteField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		byte field;
	}

	public void testReadShortField() {
		final short value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasShortField.class);
		final HasShortField instance = (HasShortField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteShortField() {
		final short value = 123;

		final HasShortField instance = new HasShortField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasShortField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (short) jsonObject.get("field").isNumber().getValue());
	}

	static class HasShortField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		short field;
	}

	public void testReadIntField() {
		final int value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasIntField.class);
		final HasIntField instance = (HasIntField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteIntField() {
		final int value = 123;

		final HasIntField instance = new HasIntField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasIntField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (int) jsonObject.get("field").isNumber().getValue());
	}

	static class HasIntField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		int field;
	}

	public void testReadLongField() {
		final long value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasLongField.class);
		final HasLongField instance = (HasLongField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteLongField() {
		final long value = 123;

		final HasLongField instance = new HasLongField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasLongField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (long) jsonObject.get("field").isNumber().getValue());
	}

	static class HasLongField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		long field;
	}

	public void testReadFloatField() {
		final float value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasFloatField.class);
		final HasFloatField instance = (HasFloatField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field, 0.01f);
	}

	public void testWriteFloatField() {
		final float value = 123;

		final HasFloatField instance = new HasFloatField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasFloatField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (float) jsonObject.get("field").isNumber().getValue(), 0.1f);
	}

	static class HasFloatField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		float field;
	}

	public void testReadDoubleField() {
		final double value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDoubleField.class);
		final HasDoubleField instance = (HasDoubleField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field, 0.01f);
	}

	public void testWriteDoubleField() {
		final double value = 123;

		final HasDoubleField instance = new HasDoubleField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDoubleField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (double) jsonObject.get("field").isNumber().getValue(), 0.1f);
	}

	static class HasDoubleField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		double field;
	}

	public void testReadCharField() {
		final char value = 'a';

		final JSONString jsonString = new JSONString(Character.toString(value));
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonString);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasCharField.class);
		final HasCharField instance = (HasCharField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteCharField() {
		final char value = 'a';

		final HasCharField instance = new HasCharField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasCharField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals("" + value, jsonObject.get("field").isString().stringValue());
	}

	static class HasCharField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		char field;
	}

	public void testReadMissingNullStringField() {
		final JSONObject jsonObject = new JSONObject();

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringField.class);
		final HasStringField instance = (HasStringField) serializer.readObject(jsonObject);

		assertNull(instance.field);
	}

	public void testReadNullStringField() {
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", JSONNull.getInstance());

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringField.class);
		final HasStringField instance = (HasStringField) serializer.readObject(jsonObject);

		assertNull(instance.field);
	}

	public void testWriteNullStringField() {
		final String value = null;

		final HasStringField instance = new HasStringField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertNull(jsonObject.get("field").isString());
	}

	public void testReadStringField() {
		final String value = "apple";

		final JSONString jsonString = new JSONString(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonString);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringField.class);
		final HasStringField instance = (HasStringField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteStringField() {
		final String value = "apple";

		final HasStringField instance = new HasStringField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, jsonObject.get("field").isString().stringValue());
	}

	static class HasStringField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		String field;
	}

	public void testReadInstanceWithNullField() {
		final JSONObject outter = new JSONObject();
		outter.put("inner", null);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasAnotherClassField.class);
		final HasAnotherClassField instance = (HasAnotherClassField) serializer.readObject(outter);

		assertNull(instance.inner);
	}

	public void testWriteInstanceWithNullField() {
		final HasAnotherClassField outter = new HasAnotherClassField();
		outter.inner = null;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasAnotherClassField.class);
		final JSONObject jsonObjectOutter = serializer.writeJson(outter).isObject();

		assertNotNull(jsonObjectOutter);

		final JSONObject jsonObjectInner = jsonObjectOutter.get("inner").isObject();
		assertNull(jsonObjectInner);
	}

	public void testReadGraph() {
		final String value = "apple";

		final JSONObject outter = new JSONObject();

		final JSONString jsonString = new JSONString(value);
		final JSONObject inner = new JSONObject();
		inner.put("field", jsonString);

		outter.put("inner", inner);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasAnotherClassField.class);
		final HasAnotherClassField instance = (HasAnotherClassField) serializer.readObject(outter);

		assertNotNull(instance.inner);
		assertEquals(value, instance.inner.field);
	}

	public void testWriteGraph() {
		final String value = "apple";

		final HasStringField inner = new HasStringField();
		inner.field = value;
		final HasAnotherClassField outter = new HasAnotherClassField();
		outter.inner = inner;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasAnotherClassField.class);
		final JSONObject jsonObjectOutter = serializer.writeJson(outter).isObject();

		assertNotNull(jsonObjectOutter);

		final JSONObject jsonObjectInner = jsonObjectOutter.get("inner").isObject();
		assertNotNull(jsonObjectInner);
	}

	static class HasAnotherClassField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName inner
		 */
		HasStringField inner;
	}

	public void testReadHasHeirarchy() {
		final String superValue = "superValue1";
		final String subValue = "subValue2";

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("superField", new JSONString(superValue));
		jsonObject.put("subField", new JSONString(subValue));

		final JsonSerializer serializer = (JsonSerializer) GWT.create(SubClass.class);
		final SubClass instance = (SubClass) serializer.readObject(jsonObject);

		assertEquals(subValue, instance.subField);
		assertEquals(superValue, ((SuperClass) instance).superField);
	}

	public void testWriteHasHeirarchy() {
		final String superValue = "superValue1";
		final String subValue = "subValue2";

		final SubClass instance = new SubClass();
		instance.subField = subValue;
		instance.superField = superValue;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(SubClass.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(2, jsonObject.size());

		assertEquals(instance.subField, jsonObject.get("subField").isString().stringValue());
		assertEquals(instance.superField, jsonObject.get("superField").isString().stringValue());
	}

	static class SuperClass {
		/**
		 * @jsonSerialization-javascriptPropertyName superField
		 */
		String superField;
	}

	static class SubClass extends SuperClass implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName subField
		 */
		String subField;
	}

	public void testReadBooleanList() {
		final boolean value = true;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, JSONBoolean.getInstance(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasBooleanListField.class);
		final HasBooleanListField instance = (HasBooleanListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Boolean booleanWrapper = (Boolean) instance.field.get(0);
		assertEquals(value, booleanWrapper.booleanValue());
	}

	public void testWriteBooleanList() {
		final List<Boolean> list = new ArrayList<Boolean>();
		list.add(new Boolean(true));
		list.add(new Boolean(false));

		final HasBooleanListField instance = new HasBooleanListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasBooleanListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals(true, jsonArray.get(0).isBoolean().booleanValue());
		assertEquals(false, jsonArray.get(1).isBoolean().booleanValue());
	}

	static class HasBooleanListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.Boolean
		 */
		List<Boolean> field;
	}

	public void testReadByteList() {
		final byte value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasByteListField.class);
		final HasByteListField instance = (HasByteListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Byte wrapper = (Byte) instance.field.get(0);
		assertEquals(value, wrapper.byteValue());
	}

	public void testWriteByteList() {
		final List<Byte> list = new ArrayList<Byte>();
		list.add((byte) 0);
		list.add((byte) 1);

		final HasByteListField instance = new HasByteListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasByteListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((byte) 0, (byte) jsonArray.get(0).isNumber().getValue());
		assertEquals((byte) 1, (byte) jsonArray.get(1).isNumber().getValue());
	}

	static class HasByteListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.Byte
		 */
		List<Byte> field;
	}

	public void testReadShortList() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasShortListField.class);
		final HasShortListField instance = (HasShortListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Short wrapper = (Short) instance.field.get(0);
		assertEquals(value, wrapper.shortValue());
	}

	public void testWriteShortList() {
		final List<Short> list = new ArrayList<Short>();
		list.add((short) 0);
		list.add((short) 1);

		final HasShortListField instance = new HasShortListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasShortListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((short) 0, (short) jsonArray.get(0).isNumber().getValue());
		assertEquals((short) 1, (short) jsonArray.get(1).isNumber().getValue());
	}

	static class HasShortListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.Short
		 */
		List<Short> field;
	}

	public void testReadIntegerList() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasIntegerListField.class);
		final HasIntegerListField instance = (HasIntegerListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Integer wrapper = (Integer) instance.field.get(0);
		assertEquals(value, wrapper.intValue());
	}

	public void testWriteIntList() {
		final List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);

		final HasIntegerListField instance = new HasIntegerListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasIntegerListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((int) 0, (int) jsonArray.get(0).isNumber().getValue());
		assertEquals((int) 1, (int) jsonArray.get(1).isNumber().getValue());
	}

	static class HasIntegerListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.Integer
		 */
		List<Integer> field;
	}

	public void testReadLongList() {
		final long value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasLongListField.class);
		final HasLongListField instance = (HasLongListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Long wrapper = (Long) instance.field.get(0);
		assertEquals(value, wrapper.longValue());
	}

	public void testWriteLongList() {
		final List<Long> list = new ArrayList<Long>();
		list.add(0L);
		list.add(1L);

		final HasLongListField instance = new HasLongListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasLongListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((long) 0, (long) jsonArray.get(0).isNumber().getValue());
		assertEquals((long) 1, (long) jsonArray.get(1).isNumber().getValue());
	}

	static class HasLongListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.Long
		 */
		List<Long> field;
	}

	public void testReadFloatList() {
		final float value = 123.4f;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasFloatListField.class);
		final HasFloatListField instance = (HasFloatListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Float wrapper = (Float) instance.field.get(0);
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	public void testWriteFloatList() {
		final List<Float> list = new ArrayList<Float>();
		list.add(0.0f);
		list.add(1.0f);

		final HasFloatListField instance = new HasFloatListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasFloatListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((float) 0, (float) jsonArray.get(0).isNumber().getValue(), 0.1f);
		assertEquals((float) 1, (float) jsonArray.get(1).isNumber().getValue(), 0.1f);
	}

	static class HasFloatListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.Float
		 */
		List<Float> field;
	}

	public void testReadDoubleList() {
		final double value = 123.4;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDoubleListField.class);
		final HasDoubleListField instance = (HasDoubleListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Double wrapper = (Double) instance.field.get(0);
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	public void testWriteDoubleList() {
		final List<Double> list = new ArrayList<Double>();
		list.add(0.0);
		list.add(1.0);

		final HasDoubleListField instance = new HasDoubleListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDoubleListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((double) 0, (double) jsonArray.get(0).isNumber().getValue(), 0.1);
		assertEquals((double) 1, (double) jsonArray.get(1).isNumber().getValue(), 0.1);
	}

	static class HasDoubleListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.Double
		 */
		List<Double> field;
	}

	public void testReadCharacterList() {
		final char value = 'a';

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasCharacterListField.class);
		final HasCharacterListField instance = (HasCharacterListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Character wrapper = (Character) instance.field.get(0);
		assertEquals(value, wrapper.charValue());
	}

	public void testWriteCharacterList() {
		final List<Character> list = new ArrayList<Character>();
		list.add('a');
		list.add('b');

		final HasCharacterListField instance = new HasCharacterListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasCharacterListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals("a", jsonArray.get(0).isString().stringValue());
		assertEquals("b", jsonArray.get(1).isString().stringValue());
	}

	static class HasCharacterListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.Character
		 */
		List<Character> field;
	}

	public void testReadStringList() {
		final String value = "Apple";

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringListField.class);
		final HasStringListField instance = (HasStringListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final String string = (String) instance.field.get(0);
		assertEquals(value, string);
	}

	public void testWriteStringList() {
		final List<String> list = new ArrayList<String>();
		list.add("apple");
		list.add("banana");

		final HasStringListField instance = new HasStringListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals(list.get(0), jsonArray.get(0).isString().stringValue());
		assertEquals(list.get(1), jsonArray.get(1).isString().stringValue());
	}

	static class HasStringListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.lang.String
		 */
		List<String> field;
	}

	public void testReadObjectList() {
		final String value = "apple";

		final JSONObject inner = new JSONObject();
		inner.put("list", new JSONString(value));

		final JSONArray array = new JSONArray();
		array.set(0, inner);

		final JSONObject outter = new JSONObject();
		outter.put("list", array);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasObjectList.class);
		final HasObjectList instance = (HasObjectList) serializer.readObject(outter);

		assertNotNull(instance.list);

		final List list = instance.list;
		final HasObjectListElement element = (HasObjectListElement) list.get(0);
		assertNotNull(element);

		assertEquals(value, element.field);
	}

	public void testWriteObjectList() {
		final List<HasObjectListElement> list = new ArrayList<HasObjectListElement>();
		list.add(new HasObjectListElement("apple"));
		list.add(new HasObjectListElement("banana"));

		final HasObjectList instance = new HasObjectList();
		instance.list = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasObjectList.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		assertEquals("apple", jsonArray.get(0).isObject().get("list").isString().stringValue());
		assertEquals("banana", jsonArray.get(1).isObject().get("list").isString().stringValue());
	}

	static class HasObjectList implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type rocket.json.test.jsonserializer.JsonSerializerGwtTestCase.HasObjectListElement
		 */
		List<HasObjectListElement> list;
	}

	static class HasObjectListElement implements JsonSerializable {
		public HasObjectListElement() {
		}

		HasObjectListElement(final String field) {
			this.field = field;
		}

		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		String field;
	}

	public void testReadBooleanSet() {
		final boolean value = true;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, JSONBoolean.getInstance(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasBooleanSetField.class);
		final HasBooleanSetField instance = (HasBooleanSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Boolean booleanWrapper = (Boolean) instance.field.iterator().next();
		assertEquals(value, booleanWrapper.booleanValue());
	}

	public void testWriteBooleanSet() {
		final Set<Boolean> set = new HashSet<Boolean>();
		set.add(new Boolean(true));
		set.add(new Boolean(false));

		final HasBooleanSetField instance = new HasBooleanSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasBooleanSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator<Boolean> iterator = set.iterator();
		assertEquals((boolean) (iterator.next()).booleanValue(), (boolean) jsonArray.get(0).isBoolean().booleanValue());
		assertEquals((boolean) (iterator.next()).booleanValue(), (boolean) jsonArray.get(1).isBoolean().booleanValue());
	}

	static class HasBooleanSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.Boolean
		 */
		Set<Boolean> field;
	}

	public void testReadByteSet() {
		final byte value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasByteSetField.class);
		final HasByteSetField instance = (HasByteSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Byte wrapper = (Byte) instance.field.iterator().next();
		assertEquals(value, wrapper.byteValue());
	}

	public void testWriteByteSet() {
		final Set<Byte> set = new HashSet<Byte>();
		set.add((byte) 0);
		set.add((byte) 1);

		final HasByteSetField instance = new HasByteSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasByteSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator<Byte> iterator = set.iterator();
		assertEquals((byte) ((Byte) iterator.next()).byteValue(), (byte) jsonArray.get(0).isNumber().getValue());
		assertEquals((byte) ((Byte) iterator.next()).byteValue(), (byte) jsonArray.get(1).isNumber().getValue());
	}

	static class HasByteSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.Byte
		 */
		Set<Byte> field;
	}

	public void testReadShortSet() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasShortSetField.class);
		final HasShortSetField instance = (HasShortSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Short wrapper = (Short) instance.field.iterator().next();
		assertEquals(value, wrapper.shortValue());
	}

	public void testWriteShortSet() {
		final Set<Short> set = new HashSet<Short>();
		set.add((short) 0);
		set.add((short) 1);

		final HasShortSetField instance = new HasShortSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasShortSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator<Short> iterator = set.iterator();
		assertEquals((short) (iterator.next()).shortValue(), (short) jsonArray.get(0).isNumber().getValue());
		assertEquals((short) (iterator.next()).shortValue(), (short) jsonArray.get(1).isNumber().getValue());
	}

	static class HasShortSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.Short
		 */
		Set<Short> field;
	}

	public void testReadIntegerSet() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasIntegerSetField.class);
		final HasIntegerSetField instance = (HasIntegerSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Integer wrapper = (Integer) instance.field.iterator().next();
		assertEquals(value, wrapper.intValue());
	}

	public void testWriteIntSet() {
		final Set<Integer> set = new HashSet<Integer>();
		set.add(0);
		set.add(1);

		final HasIntegerSetField instance = new HasIntegerSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasIntegerSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator<Integer> iterator = set.iterator();
		assertEquals((int) ((Integer) iterator.next()).intValue(), (int) jsonArray.get(0).isNumber().getValue());
		assertEquals((int) ((Integer) iterator.next()).intValue(), (int) jsonArray.get(1).isNumber().getValue());
	}

	static class HasIntegerSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.Integer
		 */
		Set<Integer> field;
	}

	public void testReadLongSet() {
		final long value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasLongSetField.class);
		final HasLongSetField instance = (HasLongSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Long wrapper = (Long) instance.field.iterator().next();
		assertEquals(value, wrapper.longValue());
	}

	public void testWriteLongSet() {
		final Set set = new HashSet();
		set.add(new Long((long) 0));
		set.add(new Long((long) 1));

		final HasLongSetField instance = new HasLongSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasLongSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator iterator = set.iterator();
		assertEquals((long) ((Long) iterator.next()).longValue(), (long) jsonArray.get(0).isNumber().getValue());
		assertEquals((long) ((Long) iterator.next()).longValue(), (long) jsonArray.get(1).isNumber().getValue());
	}

	static class HasLongSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.Long
		 */
		Set<Long> field;
	}

	public void testReadFloatSet() {
		final float value = 123.4f;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasFloatSetField.class);
		final HasFloatSetField instance = (HasFloatSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Float wrapper = (Float) instance.field.iterator().next();
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	public void testWriteFloatSet() {
		final Set set = new HashSet();
		set.add(new Float((float) 0));
		set.add(new Float((float) 1));

		final HasFloatSetField instance = new HasFloatSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasFloatSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		final Iterator iterator = set.iterator();
		assertEquals((float) ((Float) iterator.next()).floatValue(), (float) jsonArray.get(0).isNumber().getValue(), 0.1f);
		assertEquals((float) ((Float) iterator.next()).floatValue(), (float) jsonArray.get(1).isNumber().getValue(), 0.1f);
	}

	static class HasFloatSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.Float
		 */
		Set<Float> field;
	}

	public void testReadDoubleSet() {
		final double value = 123.4;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDoubleSetField.class);
		final HasDoubleSetField instance = (HasDoubleSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Double wrapper = (Double) instance.field.iterator().next();
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	public void testWriteDoubleSet() {
		final Set<Double> set = new HashSet<Double>();
		set.add(0.0);
		set.add(1.0);

		final HasDoubleSetField instance = new HasDoubleSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDoubleSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator<Double> iterator = set.iterator();
		assertEquals((double) ((Double) iterator.next()).doubleValue(), (double) jsonArray.get(0).isNumber().getValue(), 0.1f);
		assertEquals((double) ((Double) iterator.next()).doubleValue(), (double) jsonArray.get(1).isNumber().getValue(), 0.1f);
	}

	static class HasDoubleSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.Double
		 */
		Set<Double> field;
	}

	public void testReadCharacterSet() {
		final char value = 'a';

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasCharacterSetField.class);
		final HasCharacterSetField instance = (HasCharacterSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Character wrapper = (Character) instance.field.iterator().next();
		assertEquals(value, wrapper.charValue());
	}

	public void testWriteCharacterSet() {
		final Set<Character> set = new HashSet<Character>();
		set.add('a');
		set.add('b');

		final HasCharacterSetField instance = new HasCharacterSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasCharacterSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals("a", jsonArray.get(0).isString().stringValue());
		assertEquals("b", jsonArray.get(1).isString().stringValue());

		final Iterator<Character> iterator = set.iterator();
		assertEquals(iterator.next().toString(), jsonArray.get(0).isString().stringValue());
		assertEquals(iterator.next().toString(), jsonArray.get(1).isString().stringValue());
	}

	static class HasCharacterSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.Character
		 */
		Set<Character> field;
	}

	public void testReadStringSet() {
		final String value = "Apple";

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringSetField.class);
		final HasStringSetField instance = (HasStringSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final String string = (String) instance.field.iterator().next();
		assertEquals(value, string);
	}

	public void testWriteStringSet() {
		final Set<String> set = new HashSet<String>();
		set.add("apple");
		set.add("banana");

		final HasStringSetField instance = new HasStringSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator<String> iterator = set.iterator();
		assertEquals(iterator.next(), jsonArray.get(0).isString().stringValue());
		assertEquals(iterator.next(), jsonArray.get(1).isString().stringValue());
	}

	static class HasStringSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.lang.String
		 */
		Set<String> field;
	}

	public void testReadObjectSet() {
		final String value = "apple";

		final JSONObject inner = new JSONObject();
		inner.put("set", new JSONString(value));

		final JSONArray array = new JSONArray();
		array.set(0, inner);

		final JSONObject outter = new JSONObject();
		outter.put("set", array);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasObjectSet.class);
		final HasObjectSet instance = (HasObjectSet) serializer.readObject(outter);

		assertNotNull(instance.set);

		final Set set = instance.set;
		final HasObjectSetElement element = (HasObjectSetElement) set.iterator().next();
		assertNotNull(element);

		assertEquals(value, element.field);
	}

	public void testWriteObjectSet() {
		final Set<HasObjectSetElement> set = new HashSet<HasObjectSetElement>();
		set.add(new HasObjectSetElement("apple"));
		set.add(new HasObjectSetElement("banana"));

		final HasObjectSet instance = new HasObjectSet();
		instance.set = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasObjectSet.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Set<String> actual = new HashSet<String>();
		actual.add(jsonArray.get(0).isObject().get("set").isString().stringValue());
		actual.add(jsonArray.get(1).isObject().get("set").isString().stringValue());

		assertTrue("apple", actual.contains("apple"));
		assertTrue("banana", actual.contains("banana"));
	}

	static class HasObjectSet implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type rocket.json.test.jsonserializer.JsonSerializerGwtTestCase.HasObjectSetElement
		 */
		Set<HasObjectSetElement> set;
	}

	static class HasObjectSetElement implements JsonSerializable {
		public HasObjectSetElement() {
		}

		HasObjectSetElement(final String field) {
			this.field = field;
		}

		/**
		 * @jsonSerialization-javascriptPropertyName set
		 */
		String field;
	}

	public void testReadBooleanMap() {
		final boolean value = true;

		final JSONObject map = new JSONObject();
		map.put("0", JSONBoolean.getInstance(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasBooleanMapField.class);
		final HasBooleanMapField instance = (HasBooleanMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Boolean(value), instance.field.get("0"));
	}

	public void testWriteBooleanMap() {
		final Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("0", true);
		map.put("1", false);

		final HasBooleanMapField instance = new HasBooleanMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasBooleanMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Boolean) map.get("0")).booleanValue(), actualMap.get("0").isBoolean().booleanValue());
		assertEquals(((Boolean) map.get("1")).booleanValue(), actualMap.get("1").isBoolean().booleanValue());
	}

	static class HasBooleanMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.Boolean
		 */
		Map field;
	}

	public void testReadByteMap() {
		final byte value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasByteMapField.class);
		final HasByteMapField instance = (HasByteMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Byte(value), instance.field.get("0"));
	}

	public void testWriteByteMap() {
		final Map<String, Byte> map = new HashMap<String, Byte>();
		map.put("0", (byte) 123);
		map.put("1", (byte) 456);

		final HasByteMapField instance = new HasByteMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasByteMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(map.get("0").byteValue(), (byte) actualMap.get("0").isNumber().getValue());
		assertEquals(map.get("1").byteValue(), (byte) actualMap.get("1").isNumber().getValue());
	}

	static class HasByteMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.Byte
		 */
		Map field;
	}

	public void testReadShortMap() {
		final short value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasShortMapField.class);
		final HasShortMapField instance = (HasShortMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Short(value), instance.field.get("0"));
	}

	public void testWriteShortMap() {
		final Map<String, Short> map = new HashMap<String, Short>();
		map.put("0", (short) 123);
		map.put("1", (short) 456);

		final HasShortMapField instance = new HasShortMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasShortMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Short) map.get("0")).shortValue(), (short) actualMap.get("0").isNumber().getValue());
		assertEquals(((Short) map.get("1")).shortValue(), (short) actualMap.get("1").isNumber().getValue());
	}

	static class HasShortMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.Short
		 */
		Map field;
	}

	public void testReadIntMap() {
		final int value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasIntMapField.class);
		final HasIntMapField instance = (HasIntMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Integer(value), instance.field.get("0"));
	}

	public void testWriteIntMap() {
		final Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("0", 123);
		map.put("1", 456);

		final HasIntMapField instance = new HasIntMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasIntMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(map.get("0").intValue(), (int) actualMap.get("0").isNumber().getValue());
		assertEquals(map.get("1").intValue(), (int) actualMap.get("1").isNumber().getValue());
	}

	static class HasIntMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.Integer
		 */
		Map<String, Integer> field;
	}

	public void testReadLongMap() {
		final long value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasLongMapField.class);
		final HasLongMapField instance = (HasLongMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Long(value), instance.field.get("0"));
	}

	public void testWriteLongMap() {
		final Map<String, Long> map = new HashMap<String, Long>();
		map.put("0", 123L);
		map.put("1", 456L);

		final HasLongMapField instance = new HasLongMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasLongMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(map.get("0").longValue(), (long) actualMap.get("0").isNumber().getValue());
		assertEquals(map.get("1").longValue(), (long) actualMap.get("1").isNumber().getValue());
	}

	static class HasLongMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.Long
		 */
		Map field;
	}

	public void testReadFloatMap() {
		final float value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasFloatMapField.class);
		final HasFloatMapField instance = (HasFloatMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Float(value), instance.field.get("0"));
	}

	public void testWriteFloatMap() {
		final Map<String, Float> map = new HashMap<String, Float>();
		map.put("0", 123f);
		map.put("1", 456f);

		final HasFloatMapField instance = new HasFloatMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasFloatMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(map.get("0").floatValue(), (float) actualMap.get("0").isNumber().getValue(), 0.1f);
		assertEquals(map.get("1").floatValue(), (float) actualMap.get("1").isNumber().getValue(), 0.1f);
	}

	static class HasFloatMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.Float
		 */
		Map<String, Float> field;
	}

	public void testReadDoubleMap() {
		final double value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDoubleMapField.class);
		final HasDoubleMapField instance = (HasDoubleMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Double(value), instance.field.get("0"));
	}

	public void testWriteDoubleMap() {
		final Map<String, Double> map = new HashMap<String, Double>();
		map.put("0", 123.0);
		map.put("1", 456.0);

		final HasDoubleMapField instance = new HasDoubleMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDoubleMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(map.get("0").doubleValue(), (double) actualMap.get("0").isNumber().getValue(), 0.1);
		assertEquals(map.get("1").doubleValue(), (double) actualMap.get("1").isNumber().getValue(), 0.1);
	}

	static class HasDoubleMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.Double
		 */
		Map<String, Double> field;
	}

	public void testReadCharacterMap() {
		final char value = 'a';

		final JSONObject map = new JSONObject();
		map.put("0", new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasCharacterMapField.class);
		final HasCharacterMapField instance = (HasCharacterMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Character(value), instance.field.get("0"));
	}

	public void testWriteCharacterMap() {
		final Map<String, Character> map = new HashMap<String, Character>();
		map.put("0", 'a');
		map.put("1", 'b');

		final HasCharacterMapField instance = new HasCharacterMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasCharacterMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals("" + map.get("0"), actualMap.get("0").isString().stringValue());
		assertEquals("" + map.get("1"), actualMap.get("1").isString().stringValue());
	}

	static class HasCharacterMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.Character
		 */
		Map field;
	}

	public void testReadStringMap() {
		final String value = "apple";

		final JSONObject map = new JSONObject();
		map.put("0", new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringMapField.class);
		final HasStringMapField instance = (HasStringMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(value, instance.field.get("0"));
	}

	public void testWriteStringMap() {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("0", "apple");
		map.put("1", "banana");

		final HasStringMapField instance = new HasStringMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasStringMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals("" + map.get("0"), actualMap.get("0").isString().stringValue());
		assertEquals("" + map.get("1"), actualMap.get("1").isString().stringValue());
	}

	static class HasStringMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.lang.String
		 */
		Map field;
	}

	public void testReadObjectMap() {
		final String stringValue = "apple";

		final JSONObject inner = new JSONObject();
		inner.put("field", new JSONString(stringValue));

		final JSONObject map = new JSONObject();
		map.put("0", inner);

		final JSONObject outter = new JSONObject();
		outter.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasObjectMap.class);
		final HasObjectMap instance = (HasObjectMap) serializer.readObject(outter);

		assertNotNull(instance.map);

		final Map actualMap = instance.map;
		assertEquals(1, actualMap.size());

		final HasObjectMapValue value = (HasObjectMapValue) actualMap.get("0");
		assertNotNull(value);
		assertEquals(stringValue, value.field);
	}

	public void testWriteObjectMap() {
		final Map<String, HasObjectMapValue> map = new HashMap<String, HasObjectMapValue>();
		map.put("0", new HasObjectMapValue("apple"));
		map.put("1", new HasObjectMapValue("banana"));

		final HasObjectMap instance = new HasObjectMap();
		instance.map = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasObjectMap.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals("apple", actualMap.get("0").isObject().get("field").isString().stringValue());
		assertEquals("banana", actualMap.get("1").isObject().get("field").isString().stringValue());
	}

	static class HasObjectMap implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type rocket.json.test.jsonserializer.JsonSerializerGwtTestCase.HasObjectMapValue
		 */
		Map map;
	}

	static class HasObjectMapValue implements JsonSerializable {
		public HasObjectMapValue() {
		}

		HasObjectMapValue(final String field) {
			this.field = field;
		}

		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		String field;
	}

	public void testReadDateField() {
		final Date value = new Date(12345678);

		final JSONNumber jsonNumber = new JSONNumber(value.getTime());
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDateField.class);
		final HasDateField instance = (HasDateField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteDateField() {
		final Date value = new Date(12345678);

		final HasDateField instance = new HasDateField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDateField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value.getTime(), (long) jsonObject.get("field").isNumber().getValue());
	}

	static class HasDateField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		Date field;
	}

	public void testReadDateList() {
		final Date date = new Date(1234567);

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(date.getTime()));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDateListField.class);
		final HasDateListField instance = (HasDateListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Date readDate = (Date) instance.field.get(0);
		assertEquals(date, readDate);
	}

	public void testWriteDateList() {
		final List<Date> list = new ArrayList<Date>();
		final Date date = new Date(1234567);
		list.add(date);

		final HasDateListField instance = new HasDateListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDateListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(1, jsonArray.size());
		assertEquals(date.getTime(), (long) jsonArray.get(0).isNumber().getValue());
	}

	static class HasDateListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-type java.util.Date
		 */
		List<Date> field;
	}

	public void testReadDateSet() {
		final Date date = new Date(1234567);

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(date.getTime()));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDateSetField.class);
		final HasDateSetField instance = (HasDateSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Date readDate = (Date) instance.field.iterator().next();
		assertEquals(date, readDate);
	}

	public void testWriteDateSet() {
		final Set<Date> set = new HashSet<Date>();
		final Date date = new Date(1234567);
		set.add(date);

		final HasDateSetField instance = new HasDateSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDateSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(1, jsonArray.size());

		assertEquals(1, jsonArray.size());
		assertEquals(date.getTime(), (long) jsonArray.get(0).isNumber().getValue());
	}

	static class HasDateSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-type java.util.Date
		 */
		Set<Date> field;
	}

	public void testReadDateMap() {
		final Date date = new Date(1234567);

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(date.getTime()));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDateMapField.class);
		final HasDateMapField instance = (HasDateMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(date, instance.field.get("0"));
	}

	public void testWriteDateMap() {
		final Map<String, Date> map = new HashMap<String, Date>();
		final Date date = new Date(1234567);
		map.put("0", date);

		final HasDateMapField instance = new HasDateMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(HasDateMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(1, actualMap.size());

		assertEquals(date.getTime(), ((JSONNumber) actualMap.get("0")).isNumber().getValue(), 0.1f);
	}

	static class HasDateMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-type java.util.Date
		 */
		Map<String, Date> field;
	}
}
