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
package rocket.json.test.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import rocket.json.client.JsonSerializable;
import rocket.json.client.JsonSerializer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * A series of unit tests for the Json package.
 * 
 * @author Miroslav Pokorny
 */
public class JsonSerializerGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.json.test.JsonSerializerGwtTestCase";
	}

	public void testNotSerializable() {
		try {
			final Object proxy = GWT.create(NotSerializable.class);
			fail("An exception should have been thrown rocket.json.rebind.JsonSerializerGeneratorException because the NotSerializable class does not implement serializable, and not: "
					+ proxy);
		} catch (final AssertionFailedError error) {
			throw error;
		} catch (final Throwable caught) {
			final String causeType = GWT.getTypeName(caught.getCause());
			assertTrue(causeType, causeType.equals("rocket.json.rebind.JsonSerializerGeneratorException"));
		}
	}

	public void testClassMissingNoArgumentsConstructor() {
		try {
			final Object proxy = GWT.create(MissingNoArgumentsConstructor.class);
			fail("An exception should have been thrown rocket.json.rebind.JsonSerializerGeneratorException because MissingNoArgumentsConstructor does not implement serializable, and not: "
					+ proxy);
		} catch (final AssertionFailedError error) {
			throw error;
		} catch (final Throwable caught) {
			final String causeType = GWT.getTypeName(caught.getCause());
			assertTrue(causeType, causeType.equals("rocket.json.rebind.JsonSerializerGeneratorException"));
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
		jsonObject.put("list", jsonBoolean);

		try {
			final Object proxy = GWT.create(ClassWithFinalField.class);
			fail("An exception should have been thrown rocket.json.rebind.JsonSerializerGeneratorException because the NotSerializable class does not implement serializable, and not: "
					+ proxy);
		} catch (final AssertionFailedError error) {
			throw error;
		} catch (final Throwable caught) {
			final String causeType = GWT.getTypeName(caught.getCause());
			assertTrue(causeType, causeType.equals("rocket.json.rebind.JsonSerializerGeneratorException"));
		}
	}

	static class ClassWithFinalField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		final boolean field = true;
	}

	public void testReadTransientField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithTransientField.class);
		final ClassWithTransientField instance = (ClassWithTransientField) serializer.readObject(jsonObject);

		TestCase.assertTrue(value != instance.field);
	}

	static class ClassWithTransientField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		transient boolean field;
	}

	public void testReadStaticField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStaticField.class);
		final ClassWithStaticField instance = (ClassWithStaticField) serializer.readObject(jsonObject);

		TestCase.assertTrue(value != instance.field);
	}

	static class ClassWithStaticField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		static boolean field;
	}

	public void testReadBooleanField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanField.class);
		final ClassWithBooleanField instance = (ClassWithBooleanField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteBooleanField() {
		final boolean value = true;

		final ClassWithBooleanField instance = new ClassWithBooleanField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, jsonObject.get("list").isBoolean().booleanValue());
	}

	static class ClassWithBooleanField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		boolean field;
	}

	public void testReadByteField() {
		final byte value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteField.class);
		final ClassWithByteField instance = (ClassWithByteField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteByteField() {
		final byte value = 123;

		final ClassWithByteField instance = new ClassWithByteField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (byte) jsonObject.get("list").isNumber().getValue());
	}

	static class ClassWithByteField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		byte field;
	}

	public void testReadShortField() {
		final short value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortField.class);
		final ClassWithShortField instance = (ClassWithShortField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteShortField() {
		final short value = 123;

		final ClassWithShortField instance = new ClassWithShortField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (short) jsonObject.get("list").isNumber().getValue());
	}

	static class ClassWithShortField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		short field;
	}

	public void testReadIntField() {
		final int value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntField.class);
		final ClassWithIntField instance = (ClassWithIntField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteIntField() {
		final int value = 123;

		final ClassWithIntField instance = new ClassWithIntField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (int) jsonObject.get("list").isNumber().getValue());
	}

	static class ClassWithIntField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		int field;
	}

	public void testReadLongField() {
		final long value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongField.class);
		final ClassWithLongField instance = (ClassWithLongField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteLongField() {
		final long value = 123;

		final ClassWithLongField instance = new ClassWithLongField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (long) jsonObject.get("list").isNumber().getValue());
	}

	static class ClassWithLongField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		long field;
	}

	public void testReadFloatField() {
		final float value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatField.class);
		final ClassWithFloatField instance = (ClassWithFloatField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field, 0.01f);
	}

	public void testWriteFloatField() {
		final float value = 123;

		final ClassWithFloatField instance = new ClassWithFloatField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (float) jsonObject.get("list").isNumber().getValue(), 0.1f);
	}

	static class ClassWithFloatField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		float field;
	}

	public void testReadDoubleField() {
		final double value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleField.class);
		final ClassWithDoubleField instance = (ClassWithDoubleField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field, 0.01f);
	}

	public void testWriteDoubleField() {
		final double value = 123;

		final ClassWithDoubleField instance = new ClassWithDoubleField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, (double) jsonObject.get("list").isNumber().getValue(), 0.1f);
	}

	static class ClassWithDoubleField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		double field;
	}

	public void testReadCharField() {
		final char value = 'a';

		final JSONString jsonString = new JSONString(Character.toString(value));
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonString);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharField.class);
		final ClassWithCharField instance = (ClassWithCharField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteCharField() {
		final char value = 'a';

		final ClassWithCharField instance = new ClassWithCharField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals("" + value, jsonObject.get("list").isString().stringValue());
	}

	static class ClassWithCharField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		char field;
	}

	public void testReadNullStringField() {
		final JSONObject jsonObject = new JSONObject();

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringField.class);
		final ClassWithStringField instance = (ClassWithStringField) serializer.readObject(jsonObject);

		assertNull(instance.field);
	}

	public void testWriteNullStringField() {
		final String value = null;

		final ClassWithStringField instance = new ClassWithStringField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertNull(jsonObject.get("list").isString());
	}

	public void testReadStringField() {
		final String value = "apple";

		final JSONString jsonString = new JSONString(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonString);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringField.class);
		final ClassWithStringField instance = (ClassWithStringField) serializer.readObject(jsonObject);

		assertEquals(value, instance.field);
	}

	public void testWriteStringField() {
		final String value = "apple";

		final ClassWithStringField instance = new ClassWithStringField();
		instance.field = value;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertEquals(1, jsonObject.size());
		assertEquals(value, jsonObject.get("list").isString().stringValue());
	}

	static class ClassWithStringField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		String field;
	}

	public void testReadGraph() {
		final String value = "apple";

		final JSONObject outter = new JSONObject();

		final JSONString jsonString = new JSONString(value);
		final JSONObject inner = new JSONObject();
		inner.put("list", jsonString);

		outter.put("list", inner);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithAnotherClassField.class);
		final ClassWithAnotherClassField instance = (ClassWithAnotherClassField) serializer.readObject(outter);

		assertNotNull(instance.field);
		assertEquals(value, instance.field.field);
	}

	public void testWriteGraph() {
		final String value = "apple";

		final ClassWithStringField inner = new ClassWithStringField();
		inner.field = value;
		final ClassWithAnotherClassField outter = new ClassWithAnotherClassField();
		outter.field = inner;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithAnotherClassField.class);
		final JSONObject jsonObjectOutter = serializer.writeJson(outter).isObject();

		assertNotNull(jsonObjectOutter);

		final JSONObject jsonObjectInner = jsonObjectOutter.get("list").isObject();
		assertNotNull(jsonObjectInner);

		assertEquals(value, jsonObjectInner.get("list").isString().stringValue());
	}

	static class ClassWithAnotherClassField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 */
		ClassWithStringField field;
	}

	public void testReadClassWithHeirarchy() {
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

	public void testWriteClassWithHeirarchy() {
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

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanListField.class);
		final ClassWithBooleanListField instance = (ClassWithBooleanListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Boolean booleanWrapper = (Boolean) instance.field.get(0);
		assertEquals(value, booleanWrapper.booleanValue());
	}

	public void testWriteBooleanList() {
		final List list = new ArrayList();
		list.add(new Boolean(true));
		list.add(new Boolean(false));

		final ClassWithBooleanListField instance = new ClassWithBooleanListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals(true, jsonArray.get(0).isBoolean().booleanValue());
		assertEquals(false, jsonArray.get(1).isBoolean().booleanValue());
	}

	static class ClassWithBooleanListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.Boolean
		 */
		List field;
	}

	public void testReadByteList() {
		final byte value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteListField.class);
		final ClassWithByteListField instance = (ClassWithByteListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Byte wrapper = (Byte) instance.field.get(0);
		assertEquals(value, wrapper.byteValue());
	}

	public void testWriteByteList() {
		final List list = new ArrayList();
		list.add(new Byte((byte) 0));
		list.add(new Byte((byte) 1));

		final ClassWithByteListField instance = new ClassWithByteListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((byte) 0, (byte) jsonArray.get(0).isNumber().getValue());
		assertEquals((byte) 1, (byte) jsonArray.get(1).isNumber().getValue());
	}

	static class ClassWithByteListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.Byte
		 */
		List field;
	}

	public void testReadShortList() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortListField.class);
		final ClassWithShortListField instance = (ClassWithShortListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Short wrapper = (Short) instance.field.get(0);
		assertEquals(value, wrapper.shortValue());
	}

	public void testWriteShortList() {
		final List list = new ArrayList();
		list.add(new Short((short) 0));
		list.add(new Short((short) 1));

		final ClassWithShortListField instance = new ClassWithShortListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((short) 0, (short) jsonArray.get(0).isNumber().getValue());
		assertEquals((short) 1, (short) jsonArray.get(1).isNumber().getValue());
	}

	static class ClassWithShortListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.Short
		 */
		List field;
	}

	public void testReadIntegerList() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntegerListField.class);
		final ClassWithIntegerListField instance = (ClassWithIntegerListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Integer wrapper = (Integer) instance.field.get(0);
		assertEquals(value, wrapper.intValue());
	}

	public void testWriteIntList() {
		final List list = new ArrayList();
		list.add(new Integer((int) 0));
		list.add(new Integer((int) 1));

		final ClassWithIntegerListField instance = new ClassWithIntegerListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntegerListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((int) 0, (int) jsonArray.get(0).isNumber().getValue());
		assertEquals((int) 1, (int) jsonArray.get(1).isNumber().getValue());
	}

	static class ClassWithIntegerListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.Integer
		 */
		List field;
	}

	public void testReadLongList() {
		final long value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongListField.class);
		final ClassWithLongListField instance = (ClassWithLongListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Long wrapper = (Long) instance.field.get(0);
		assertEquals(value, wrapper.longValue());
	}

	public void testWriteLongList() {
		final List list = new ArrayList();
		list.add(new Long((long) 0));
		list.add(new Long((long) 1));

		final ClassWithLongListField instance = new ClassWithLongListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((long) 0, (long) jsonArray.get(0).isNumber().getValue());
		assertEquals((long) 1, (long) jsonArray.get(1).isNumber().getValue());
	}

	static class ClassWithLongListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.Long
		 */
		List field;
	}

	public void testReadFloatList() {
		final float value = 123.4f;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatListField.class);
		final ClassWithFloatListField instance = (ClassWithFloatListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Float wrapper = (Float) instance.field.get(0);
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	public void testWriteFloatList() {
		final List list = new ArrayList();
		list.add(new Float((float) 0));
		list.add(new Float((float) 1));

		final ClassWithFloatListField instance = new ClassWithFloatListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((float) 0, (float) jsonArray.get(0).isNumber().getValue(), 0.1f);
		assertEquals((float) 1, (float) jsonArray.get(1).isNumber().getValue(), 0.1f);
	}

	static class ClassWithFloatListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.Float
		 */
		List field;
	}

	public void testReadDoubleList() {
		final double value = 123.4;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleListField.class);
		final ClassWithDoubleListField instance = (ClassWithDoubleListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Double wrapper = (Double) instance.field.get(0);
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	public void testWriteDoubleList() {
		final List list = new ArrayList();
		list.add(new Double((double) 0));
		list.add(new Double((double) 1));

		final ClassWithDoubleListField instance = new ClassWithDoubleListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals((double) 0, (double) jsonArray.get(0).isNumber().getValue(), 0.1);
		assertEquals((double) 1, (double) jsonArray.get(1).isNumber().getValue(), 0.1);
	}

	static class ClassWithDoubleListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.Double
		 */
		List field;
	}

	public void testReadCharacterList() {
		final char value = 'a';

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterListField.class);
		final ClassWithCharacterListField instance = (ClassWithCharacterListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Character wrapper = (Character) instance.field.get(0);
		assertEquals(value, wrapper.charValue());
	}

	public void testWriteCharacterList() {
		final List list = new ArrayList();
		list.add(new Character('a'));
		list.add(new Character('b'));

		final ClassWithCharacterListField instance = new ClassWithCharacterListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals("a", jsonArray.get(0).isString().stringValue());
		assertEquals("b", jsonArray.get(1).isString().stringValue());
	}

	static class ClassWithCharacterListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.Character
		 */
		List field;
	}

	public void testReadStringList() {
		final String value = "Apple";

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringListField.class);
		final ClassWithStringListField instance = (ClassWithStringListField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final String string = (String) instance.field.get(0);
		assertEquals(value, string);
	}

	public void testWriteStringList() {
		final List list = new ArrayList();
		list.add("apple");
		list.add("banana");

		final ClassWithStringListField instance = new ClassWithStringListField();
		instance.field = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringListField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals(list.get(0), jsonArray.get(0).isString().stringValue());
		assertEquals(list.get(1), jsonArray.get(1).isString().stringValue());
	}

	static class ClassWithStringListField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType java.lang.String
		 */
		List field;
	}

	public void testReadObjectList() {
		final String value = "apple";

		final JSONObject inner = new JSONObject();
		inner.put("list", new JSONString(value));

		final JSONArray array = new JSONArray();
		array.set(0, inner);

		final JSONObject outter = new JSONObject();
		outter.put("list", array);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectList.class);
		final ClassWithObjectList instance = (ClassWithObjectList) serializer.readObject(outter);

		assertNotNull(instance.list);

		final List list = instance.list;
		final ClassWithObjectListElement element = (ClassWithObjectListElement) list.get(0);
		assertNotNull(element);

		assertEquals(value, element.field);
	}

	public void testWriteObjectList() {
		final List list = new ArrayList();
		list.add(new ClassWithObjectListElement("apple"));
		list.add(new ClassWithObjectListElement("banana"));

		final ClassWithObjectList instance = new ClassWithObjectList();
		instance.list = list;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectList.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("list").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		assertEquals("apple", jsonArray.get(0).isObject().get("list").isString().stringValue());
		assertEquals("banana", jsonArray.get(1).isObject().get("list").isString().stringValue());
	}

	static class ClassWithObjectList implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName list
		 * @jsonSerialization-listElementType rocket.json.test.client.JsonSerializerGwtTestCase.ClassWithObjectListElement
		 */
		List list;
	}

	static class ClassWithObjectListElement implements JsonSerializable {
		public ClassWithObjectListElement() {
		}

		ClassWithObjectListElement(final String field) {
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

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanSetField.class);
		final ClassWithBooleanSetField instance = (ClassWithBooleanSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Boolean booleanWrapper = (Boolean) instance.field.iterator().next();
		assertEquals(value, booleanWrapper.booleanValue());
	}

	public void testWriteBooleanSet() {
		final Set set = new HashSet();
		set.add(new Boolean(true));
		set.add(new Boolean(false));

		final ClassWithBooleanSetField instance = new ClassWithBooleanSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator iterator = set.iterator();
		assertEquals((boolean) ((Boolean) iterator.next()).booleanValue(), (boolean) jsonArray.get(0).isBoolean().booleanValue());
		assertEquals((boolean) ((Boolean) iterator.next()).booleanValue(), (boolean) jsonArray.get(1).isBoolean().booleanValue());
	}

	static class ClassWithBooleanSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.Boolean
		 */
		Set field;
	}

	public void testReadByteSet() {
		final byte value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteSetField.class);
		final ClassWithByteSetField instance = (ClassWithByteSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Byte wrapper = (Byte) instance.field.iterator().next();
		assertEquals(value, wrapper.byteValue());
	}

	public void testWriteByteSet() {
		final Set set = new HashSet();
		set.add(new Byte((byte) 0));
		set.add(new Byte((byte) 1));

		final ClassWithByteSetField instance = new ClassWithByteSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator iterator = set.iterator();
		assertEquals((byte) ((Byte) iterator.next()).byteValue(), (byte) jsonArray.get(0).isNumber().getValue());
		assertEquals((byte) ((Byte) iterator.next()).byteValue(), (byte) jsonArray.get(1).isNumber().getValue());
	}

	static class ClassWithByteSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.Byte
		 */
		Set field;
	}

	public void testReadShortSet() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortSetField.class);
		final ClassWithShortSetField instance = (ClassWithShortSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Short wrapper = (Short) instance.field.iterator().next();
		assertEquals(value, wrapper.shortValue());
	}

	public void testWriteShortSet() {
		final Set set = new HashSet();
		set.add(new Short((short) 0));
		set.add(new Short((short) 1));

		final ClassWithShortSetField instance = new ClassWithShortSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator iterator = set.iterator();
		assertEquals((short) ((Short) iterator.next()).shortValue(), (short) jsonArray.get(0).isNumber().getValue());
		assertEquals((short) ((Short) iterator.next()).shortValue(), (short) jsonArray.get(1).isNumber().getValue());
	}

	static class ClassWithShortSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.Short
		 */
		Set field;
	}

	public void testReadIntegerSet() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntegerSetField.class);
		final ClassWithIntegerSetField instance = (ClassWithIntegerSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Integer wrapper = (Integer) instance.field.iterator().next();
		assertEquals(value, wrapper.intValue());
	}

	public void testWriteIntSet() {
		final Set set = new HashSet();
		set.add(new Integer((int) 0));
		set.add(new Integer((int) 1));

		final ClassWithIntegerSetField instance = new ClassWithIntegerSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntegerSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator iterator = set.iterator();
		assertEquals((int) ((Integer) iterator.next()).intValue(), (int) jsonArray.get(0).isNumber().getValue());
		assertEquals((int) ((Integer) iterator.next()).intValue(), (int) jsonArray.get(1).isNumber().getValue());
	}

	static class ClassWithIntegerSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.Integer
		 */
		Set field;
	}

	public void testReadLongSet() {
		final long value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongSetField.class);
		final ClassWithLongSetField instance = (ClassWithLongSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Long wrapper = (Long) instance.field.iterator().next();
		assertEquals(value, wrapper.longValue());
	}

	public void testWriteLongSet() {
		final Set set = new HashSet();
		set.add(new Long((long) 0));
		set.add(new Long((long) 1));

		final ClassWithLongSetField instance = new ClassWithLongSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongSetField.class);
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

	static class ClassWithLongSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.Long
		 */
		Set field;
	}

	public void testReadFloatSet() {
		final float value = 123.4f;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatSetField.class);
		final ClassWithFloatSetField instance = (ClassWithFloatSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Float wrapper = (Float) instance.field.iterator().next();
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	public void testWriteFloatSet() {
		final Set set = new HashSet();
		set.add(new Float((float) 0));
		set.add(new Float((float) 1));

		final ClassWithFloatSetField instance = new ClassWithFloatSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		final Iterator iterator = set.iterator();
		assertEquals((float) ((Float) iterator.next()).floatValue(), (float) jsonArray.get(0).isNumber().getValue(), 0.1f);
		assertEquals((float) ((Float) iterator.next()).floatValue(), (float) jsonArray.get(1).isNumber().getValue(), 0.1f);
	}

	static class ClassWithFloatSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.Float
		 */
		Set field;
	}

	public void testReadDoubleSet() {
		final double value = 123.4;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleSetField.class);
		final ClassWithDoubleSetField instance = (ClassWithDoubleSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Double wrapper = (Double) instance.field.iterator().next();
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	public void testWriteDoubleSet() {
		final Set set = new HashSet();
		set.add(new Double((double) 0));
		set.add(new Double((double) 1));

		final ClassWithDoubleSetField instance = new ClassWithDoubleSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator iterator = set.iterator();
		assertEquals((double) ((Double) iterator.next()).doubleValue(), (double) jsonArray.get(0).isNumber().getValue(), 0.1f);
		assertEquals((double) ((Double) iterator.next()).doubleValue(), (double) jsonArray.get(1).isNumber().getValue(), 0.1f);
	}

	static class ClassWithDoubleSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.Double
		 */
		Set field;
	}

	public void testReadCharacterSet() {
		final char value = 'a';

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterSetField.class);
		final ClassWithCharacterSetField instance = (ClassWithCharacterSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final Character wrapper = (Character) instance.field.iterator().next();
		assertEquals(value, wrapper.charValue());
	}

	public void testWriteCharacterSet() {
		final Set set = new HashSet();
		set.add(new Character('a'));
		set.add(new Character('b'));

		final ClassWithCharacterSetField instance = new ClassWithCharacterSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());
		assertEquals("a", jsonArray.get(0).isString().stringValue());
		assertEquals("b", jsonArray.get(1).isString().stringValue());

		final Iterator iterator = set.iterator();
		assertEquals(iterator.next().toString(), jsonArray.get(0).isString().stringValue());
		assertEquals(iterator.next().toString(), jsonArray.get(1).isString().stringValue());
	}

	static class ClassWithCharacterSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.Character
		 */
		Set field;
	}

	public void testReadStringSet() {
		final String value = "Apple";

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("set", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringSetField.class);
		final ClassWithStringSetField instance = (ClassWithStringSetField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		final String string = (String) instance.field.iterator().next();
		assertEquals(value, string);
	}

	public void testWriteStringSet() {
		final Set set = new HashSet();
		set.add("apple");
		set.add("banana");

		final ClassWithStringSetField instance = new ClassWithStringSetField();
		instance.field = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringSetField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		final Iterator iterator = set.iterator();
		assertEquals(iterator.next(), jsonArray.get(0).isString().stringValue());
		assertEquals(iterator.next(), jsonArray.get(1).isString().stringValue());
	}

	static class ClassWithStringSetField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType java.lang.String
		 */
		Set field;
	}

	public void testReadObjectSet() {
		final String value = "apple";

		final JSONObject inner = new JSONObject();
		inner.put("set", new JSONString(value));

		final JSONArray array = new JSONArray();
		array.set(0, inner);

		final JSONObject outter = new JSONObject();
		outter.put("set", array);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectSet.class);
		final ClassWithObjectSet instance = (ClassWithObjectSet) serializer.readObject(outter);

		assertNotNull(instance.set);

		final Set set = instance.set;
		final ClassWithObjectSetElement element = (ClassWithObjectSetElement) set.iterator().next();
		assertNotNull(element);

		assertEquals(value, element.field);
	}

	public void testWriteObjectSet() {
		final Set set = new HashSet();
		set.add(new ClassWithObjectSetElement("apple"));
		set.add(new ClassWithObjectSetElement("banana"));

		final ClassWithObjectSet instance = new ClassWithObjectSet();
		instance.set = set;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectSet.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONArray jsonArray = jsonObject.get("set").isArray();
		assertNotNull(jsonArray);

		assertEquals(2, jsonArray.size());

		assertEquals("apple", jsonArray.get(1).isObject().get("set").isString().stringValue());
		assertEquals("banana", jsonArray.get(0).isObject().get("set").isString().stringValue());
	}

	static class ClassWithObjectSet implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName set
		 * @jsonSerialization-setElementType rocket.json.test.client.JsonSerializerGwtTestCase.ClassWithObjectSetElement
		 */
		Set set;
	}

	static class ClassWithObjectSetElement implements JsonSerializable {
		public ClassWithObjectSetElement() {
		}

		ClassWithObjectSetElement(final String field) {
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

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanMapField.class);
		final ClassWithBooleanMapField instance = (ClassWithBooleanMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Boolean(value), instance.field.get("0"));
	}

	public void testWriteBooleanMap() {
		final Map map = new HashMap();
		map.put("0", new Boolean(true));
		map.put("1", new Boolean(false));

		final ClassWithBooleanMapField instance = new ClassWithBooleanMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Boolean) map.get("0")).booleanValue(), actualMap.get("0").isBoolean().booleanValue());
		assertEquals(((Boolean) map.get("1")).booleanValue(), actualMap.get("1").isBoolean().booleanValue());
	}

	static class ClassWithBooleanMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.Boolean
		 */
		Map field;
	}

	public void testReadByteMap() {
		final byte value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteMapField.class);
		final ClassWithByteMapField instance = (ClassWithByteMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Byte(value), instance.field.get("0"));
	}

	public void testWriteByteMap() {
		final Map map = new HashMap();
		map.put("0", new Byte((byte) 123));
		map.put("1", new Byte((byte) 456));

		final ClassWithByteMapField instance = new ClassWithByteMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Byte) map.get("0")).byteValue(), (byte) actualMap.get("0").isNumber().getValue());
		assertEquals(((Byte) map.get("1")).byteValue(), (byte) actualMap.get("1").isNumber().getValue());
	}

	static class ClassWithByteMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.Byte
		 */
		Map field;
	}

	public void testReadShortMap() {
		final short value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortMapField.class);
		final ClassWithShortMapField instance = (ClassWithShortMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Short(value), instance.field.get("0"));
	}

	public void testWriteShortMap() {
		final Map map = new HashMap();
		map.put("0", new Short((short) 123));
		map.put("1", new Short((short) 456));

		final ClassWithShortMapField instance = new ClassWithShortMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Short) map.get("0")).shortValue(), (short) actualMap.get("0").isNumber().getValue());
		assertEquals(((Short) map.get("1")).shortValue(), (short) actualMap.get("1").isNumber().getValue());
	}

	static class ClassWithShortMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.Short
		 */
		Map field;
	}

	public void testReadIntMap() {
		final int value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntMapField.class);
		final ClassWithIntMapField instance = (ClassWithIntMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Integer(value), instance.field.get("0"));
	}

	public void testWriteIntMap() {
		final Map map = new HashMap();
		map.put("0", new Integer((int) 123));
		map.put("1", new Integer((int) 456));

		final ClassWithIntMapField instance = new ClassWithIntMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Integer) map.get("0")).intValue(), (int) actualMap.get("0").isNumber().getValue());
		assertEquals(((Integer) map.get("1")).intValue(), (int) actualMap.get("1").isNumber().getValue());
	}

	static class ClassWithIntMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.Integer
		 */
		Map field;
	}

	public void testReadLongMap() {
		final long value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongMapField.class);
		final ClassWithLongMapField instance = (ClassWithLongMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Long(value), instance.field.get("0"));
	}

	public void testWriteLongMap() {
		final Map map = new HashMap();
		map.put("0", new Long((long) 123));
		map.put("1", new Long((long) 456));

		final ClassWithLongMapField instance = new ClassWithLongMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Long) map.get("0")).longValue(), (long) actualMap.get("0").isNumber().getValue());
		assertEquals(((Long) map.get("1")).longValue(), (long) actualMap.get("1").isNumber().getValue());
	}

	static class ClassWithLongMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.Long
		 */
		Map field;
	}

	public void testReadFloatMap() {
		final float value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatMapField.class);
		final ClassWithFloatMapField instance = (ClassWithFloatMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Float(value), instance.field.get("0"));
	}

	public void testWriteFloatMap() {
		final Map map = new HashMap();
		map.put("0", new Float((float) 123));
		map.put("1", new Float((float) 456));

		final ClassWithFloatMapField instance = new ClassWithFloatMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Float) map.get("0")).floatValue(), (float) actualMap.get("0").isNumber().getValue(), 0.1f);
		assertEquals(((Float) map.get("1")).floatValue(), (float) actualMap.get("1").isNumber().getValue(), 0.1f);
	}

	static class ClassWithFloatMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.Float
		 */
		Map field;
	}

	public void testReadDoubleMap() {
		final double value = 123;

		final JSONObject map = new JSONObject();
		map.put("0", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleMapField.class);
		final ClassWithDoubleMapField instance = (ClassWithDoubleMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Double(value), instance.field.get("0"));
	}

	public void testWriteDoubleMap() {
		final Map map = new HashMap();
		map.put("0", new Double((double) 123));
		map.put("1", new Double((double) 456));

		final ClassWithDoubleMapField instance = new ClassWithDoubleMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals(((Double) map.get("0")).doubleValue(), (double) actualMap.get("0").isNumber().getValue(), 0.1);
		assertEquals(((Double) map.get("1")).doubleValue(), (double) actualMap.get("1").isNumber().getValue(), 0.1);
	}

	static class ClassWithDoubleMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.Double
		 */
		Map field;
	}

	public void testReadCharacterMap() {
		final char value = 'a';

		final JSONObject map = new JSONObject();
		map.put("0", new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterMapField.class);
		final ClassWithCharacterMapField instance = (ClassWithCharacterMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(new Character(value), instance.field.get("0"));
	}

	public void testWriteCharacterMap() {
		final Map map = new HashMap();
		map.put("0", new Character('a'));
		map.put("1", new Character('b'));

		final ClassWithCharacterMapField instance = new ClassWithCharacterMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals("" + map.get("0"), actualMap.get("0").isString().stringValue());
		assertEquals("" + map.get("1"), actualMap.get("1").isString().stringValue());
	}

	static class ClassWithCharacterMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.Character
		 */
		Map field;
	}

	public void testReadStringMap() {
		final String value = "apple";

		final JSONObject map = new JSONObject();
		map.put("0", new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("map", map);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringMapField.class);
		final ClassWithStringMapField instance = (ClassWithStringMapField) serializer.readObject(jsonObject);

		assertNotNull(instance.field);

		assertEquals(value, instance.field.get("0"));
	}

	public void testWriteStringMap() {
		final Map map = new HashMap();
		map.put("0", "apple");
		map.put("1", "banana");

		final ClassWithStringMapField instance = new ClassWithStringMapField();
		instance.field = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringMapField.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals("" + map.get("0"), actualMap.get("0").isString().stringValue());
		assertEquals("" + map.get("1"), actualMap.get("1").isString().stringValue());
	}

	static class ClassWithStringMapField implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType java.lang.String
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

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectMap.class);
		final ClassWithObjectMap instance = (ClassWithObjectMap) serializer.readObject(outter);

		assertNotNull(instance.map);

		final Map actualMap = instance.map;
		assertEquals(1, actualMap.size());

		final ClassWithObjectMapValue value = (ClassWithObjectMapValue) actualMap.get("0");
		assertNotNull(value);
		assertEquals(stringValue, value.field);
	}

	public void testWriteObjectMap() {
		final Map map = new HashMap();
		map.put("0", new ClassWithObjectMapValue("apple"));
		map.put("1", new ClassWithObjectMapValue("banana"));

		final ClassWithObjectMap instance = new ClassWithObjectMap();
		instance.map = map;

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectMap.class);
		final JSONObject jsonObject = (JSONObject) serializer.writeJson(instance);

		assertNotNull(jsonObject);

		assertEquals(1, jsonObject.size());

		final JSONObject actualMap = jsonObject.get("map").isObject();
		assertNotNull(actualMap);

		assertEquals(2, actualMap.size());

		assertEquals("apple", actualMap.get("0").isObject().get("field").isString().stringValue());
		assertEquals("banana", actualMap.get("1").isObject().get("field").isString().stringValue());
	}

	static class ClassWithObjectMap implements JsonSerializable {
		/**
		 * @jsonSerialization-javascriptPropertyName map
		 * @jsonSerialization-mapValueType rocket.json.test.client.JsonSerializerGwtTestCase.ClassWithObjectMapValue
		 */
		Map map;
	}

	static class ClassWithObjectMapValue implements JsonSerializable {
		public ClassWithObjectMapValue() {
		}

		ClassWithObjectMapValue(final String field) {
			this.field = field;
		}

		/**
		 * @jsonSerialization-javascriptPropertyName field
		 */
		String field;
	}
}
