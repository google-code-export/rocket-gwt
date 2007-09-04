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

	public void testDeserializeClassWithFinalField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonBoolean);

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
		 * @javascriptPropertyName field
		 */
		final boolean field = true;
	}

	public void testDeserializeClassWithBooleanField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanField.class);
		final ClassWithBooleanField instance = (ClassWithBooleanField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field);
	}

	static class ClassWithBooleanField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		boolean field;
	}

	public void testDeserializeClassWithTransientField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithTransientField.class);
		final ClassWithTransientField instance = (ClassWithTransientField) serializer.asObject(jsonObject);

		TestCase.assertTrue(value != instance.field);
	}

	static class ClassWithTransientField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		transient boolean field;
	}

	public void testDeserializeClassWithStaticField() {
		final boolean value = true;

		final JSONBoolean jsonBoolean = JSONBoolean.getInstance(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonBoolean);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStaticField.class);
		final ClassWithStaticField instance = (ClassWithStaticField) serializer.asObject(jsonObject);

		TestCase.assertTrue(value != instance.field);
	}

	static class ClassWithStaticField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		static boolean field;
	}

	public void testDeserializeClassWithByteField() {
		final byte value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteField.class);
		final ClassWithByteField instance = (ClassWithByteField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field);
	}

	static class ClassWithByteField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		byte field;
	}

	public void testDeserializeClassWithShortField() {
		final short value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortField.class);
		final ClassWithShortField instance = (ClassWithShortField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field);
	}

	static class ClassWithShortField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		short field;
	}

	public void testDeserializeClassWithIntField() {
		final int value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntField.class);
		final ClassWithIntField instance = (ClassWithIntField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field);
	}

	static class ClassWithIntField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		int field;
	}

	public void testDeserializeClassWithLongField() {
		final long value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongField.class);
		final ClassWithLongField instance = (ClassWithLongField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field);
	}

	static class ClassWithLongField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		long field;
	}

	public void testDeserializeClassWithFloatField() {
		final float value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatField.class);
		final ClassWithFloatField instance = (ClassWithFloatField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field, 0.01f);
	}

	static class ClassWithFloatField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		float field;
	}

	public void testDeserializeClassWithDoubleField() {
		final double value = 123;

		final JSONNumber jsonNumber = new JSONNumber(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonNumber);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleField.class);
		final ClassWithDoubleField instance = (ClassWithDoubleField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field, 0.01f);
	}

	static class ClassWithDoubleField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		double field;
	}

	public void testDeserializeClassWithCharField() {
		final char value = 'a';

		final JSONString jsonString = new JSONString(Character.toString(value));
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonString);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharField.class);
		final ClassWithCharField instance = (ClassWithCharField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field);
	}

	static class ClassWithCharField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		char field;
	}

	public void testDeserializeClassWithNullStringField() {
		final JSONObject jsonObject = new JSONObject();

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringField.class);
		final ClassWithStringField instance = (ClassWithStringField) serializer.asObject(jsonObject);

		assertNull(instance.field);
	}

	public void testDeserializeClassWithStringField() {
		final String value = "apple";

		final JSONString jsonString = new JSONString(value);
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonString);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringField.class);
		final ClassWithStringField instance = (ClassWithStringField) serializer.asObject(jsonObject);

		assertEquals(value, instance.field);
	}

	static class ClassWithStringField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		String field;
	}

	public void testDeserializeClassWithGraph() {
		final String value = "apple";

		final JSONObject outter = new JSONObject();

		final JSONString jsonString = new JSONString(value);
		final JSONObject inner = new JSONObject();
		inner.put("field", jsonString);

		outter.put("field", inner);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithAnotherClassField.class);
		final ClassWithAnotherClassField instance = (ClassWithAnotherClassField) serializer.asObject(outter);

		assertNotNull(instance.field);
		assertEquals(value, instance.field.field);
	}

	static class ClassWithAnotherClassField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		ClassWithStringField field;
	}

	public void testDeserializeClassWithFieldWithHeirarchy() {
		final String superValue = "superValue1";
		final String subValue = "subValue2";

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("superField", new JSONString(superValue));
		jsonObject.put("subField", new JSONString(subValue));

		final JsonSerializer serializer = (JsonSerializer) GWT.create(SubClass.class);
		final SubClass instance = (SubClass) serializer.asObject(jsonObject);

		assertEquals(subValue, instance.subField);
		assertEquals(superValue, ((SuperClass) instance).superField);
	}

	static class SuperClass {
		/**
		 * @javascriptPropertyName superField
		 */
		String superField;
	}

	static class SubClass extends SuperClass implements JsonSerializable {
		/**
		 * @javascriptPropertyName subField
		 */
		String subField;
	}

	public void testDeserializeClassWithBooleanList() {
		final boolean value = true;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, JSONBoolean.getInstance(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanListField.class);
		final ClassWithBooleanListField instance = (ClassWithBooleanListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Boolean booleanWrapper = (Boolean) instance.field.get(0);
		assertEquals(value, booleanWrapper.booleanValue());
	}

	static class ClassWithBooleanListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.Boolean
		 */
		List field;
	}

	public void testDeserializeClassWithByteList() {
		final byte value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteListField.class);
		final ClassWithByteListField instance = (ClassWithByteListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Byte wrapper = (Byte) instance.field.get(0);
		assertEquals(value, wrapper.byteValue());
	}

	static class ClassWithByteListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.Byte
		 */
		List field;
	}

	public void testDeserializeClassWithShortList() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortListField.class);
		final ClassWithShortListField instance = (ClassWithShortListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Short wrapper = (Short) instance.field.get(0);
		assertEquals(value, wrapper.shortValue());
	}

	static class ClassWithShortListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.Short
		 */
		List field;
	}

	public void testDeserializeClassWithIntegerList() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntegerListField.class);
		final ClassWithIntegerListField instance = (ClassWithIntegerListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Integer wrapper = (Integer) instance.field.get(0);
		assertEquals(value, wrapper.intValue());
	}

	static class ClassWithIntegerListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.Integer
		 */
		List field;
	}

	public void testDeserializeClassWithLongList() {
		final long value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongListField.class);
		final ClassWithLongListField instance = (ClassWithLongListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Long wrapper = (Long) instance.field.get(0);
		assertEquals(value, wrapper.longValue());
	}

	static class ClassWithLongListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.Long
		 */
		List field;
	}

	public void testDeserializeClassWithFloatList() {
		final float value = 123.4f;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatListField.class);
		final ClassWithFloatListField instance = (ClassWithFloatListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Float wrapper = (Float) instance.field.get(0);
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	static class ClassWithFloatListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.Float
		 */
		List field;
	}

	public void testDeserializeClassWithDoubleList() {
		final double value = 123.4;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleListField.class);
		final ClassWithDoubleListField instance = (ClassWithDoubleListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Double wrapper = (Double) instance.field.get(0);
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	static class ClassWithDoubleListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.Double
		 */
		List field;
	}

	public void testDeserializeClassWithCharacterList() {
		final char value = 'a';

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterListField.class);
		final ClassWithCharacterListField instance = (ClassWithCharacterListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Character wrapper = (Character) instance.field.get(0);
		assertEquals(value, wrapper.charValue());
	}

	static class ClassWithCharacterListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.Character
		 */
		List field;
	}

	public void testDeserializeClassWithStringList() {
		final String value = "Apple";

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringListField.class);
		final ClassWithStringListField instance = (ClassWithStringListField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final String string = (String) instance.field.get(0);
		assertEquals(value, string);
	}

	static class ClassWithStringListField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @listElementType java.lang.String
		 */
		List field;
	}

	public void testDeserializeClassWithObjectList() {
		final String value = "apple";

		final JSONObject inner = new JSONObject();
		inner.put("field", new JSONString(value));

		final JSONArray array = new JSONArray();
		array.set(0, inner);

		final JSONObject outter = new JSONObject();
		outter.put("listField", array);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectList.class);
		final ClassWithObjectList instance = (ClassWithObjectList) serializer.asObject(outter);

		assertNotNull(instance.listField);

		final List list = instance.listField;
		final ClassWithObjectListElement element = (ClassWithObjectListElement) list.get(0);
		assertNotNull(element);

		assertEquals(value, element.field);
	}

	static class ClassWithObjectList implements JsonSerializable {
		/**
		 * @javascriptPropertyName listField
		 * @listElementType rocket.json.test.client.JsonSerializerGwtTestCase.ClassWithObjectListElement
		 */
		List listField;
	}

	static class ClassWithObjectListElement implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		String field;
	}

	public void testDeserializeClassWithBooleanSet() {
		final boolean value = true;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, JSONBoolean.getInstance(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanSetField.class);
		final ClassWithBooleanSetField instance = (ClassWithBooleanSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Boolean booleanWrapper = (Boolean) instance.field.iterator().next();
		assertEquals(value, booleanWrapper.booleanValue());
	}

	static class ClassWithBooleanSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.Boolean
		 */
		Set field;
	}

	public void testDeserializeClassWithByteSet() {
		final byte value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteSetField.class);
		final ClassWithByteSetField instance = (ClassWithByteSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Byte wrapper = (Byte) instance.field.iterator().next();
		assertEquals(value, wrapper.byteValue());
	}

	static class ClassWithByteSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.Byte
		 */
		Set field;
	}

	public void testDeserializeClassWithShortSet() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortSetField.class);
		final ClassWithShortSetField instance = (ClassWithShortSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Short wrapper = (Short) instance.field.iterator().next();
		assertEquals(value, wrapper.shortValue());
	}

	static class ClassWithShortSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.Short
		 */
		Set field;
	}

	public void testDeserializeClassWithIntegerSet() {
		final short value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntegerSetField.class);
		final ClassWithIntegerSetField instance = (ClassWithIntegerSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Integer wrapper = (Integer) instance.field.iterator().next();
		assertEquals(value, wrapper.intValue());
	}

	static class ClassWithIntegerSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.Integer
		 */
		Set field;
	}

	public void testDeserializeClassWithLongSet() {
		final long value = 123;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongSetField.class);
		final ClassWithLongSetField instance = (ClassWithLongSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Long wrapper = (Long) instance.field.iterator().next();
		assertEquals(value, wrapper.longValue());
	}

	static class ClassWithLongSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.Long
		 */
		Set field;
	}

	public void testDeserializeClassWithFloatSet() {
		final float value = 123.4f;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatSetField.class);
		final ClassWithFloatSetField instance = (ClassWithFloatSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Float wrapper = (Float) instance.field.iterator().next();
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	static class ClassWithFloatSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.Float
		 */
		Set field;
	}

	public void testDeserializeClassWithDoubleSet() {
		final double value = 123.4;

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleSetField.class);
		final ClassWithDoubleSetField instance = (ClassWithDoubleSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Double wrapper = (Double) instance.field.iterator().next();
		assertEquals(value, wrapper.floatValue(), 0.1f);
	}

	static class ClassWithDoubleSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.Double
		 */
		Set field;
	}

	public void testDeserializeClassWithCharacterSet() {
		final char value = 'a';

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterSetField.class);
		final ClassWithCharacterSetField instance = (ClassWithCharacterSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Character wrapper = (Character) instance.field.iterator().next();
		assertEquals(value, wrapper.charValue());
	}

	static class ClassWithCharacterSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.Character
		 */
		Set field;
	}

	public void testDeserializeClassWithStringSet() {
		final String value = "Apple";

		final JSONArray jsonArray = new JSONArray();
		jsonArray.set(0, new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonArray);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringSetField.class);
		final ClassWithStringSetField instance = (ClassWithStringSetField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final String string = (String) instance.field.iterator().next();
		assertEquals(value, string);
	}

	static class ClassWithStringSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @setElementType java.lang.String
		 */
		Set field;
	}

	public void testDeserializeClassWithObjectSet() {
		final String value = "apple";

		final JSONObject inner = new JSONObject();
		inner.put("field", new JSONString(value));

		final JSONArray array = new JSONArray();
		array.set(0, inner);

		final JSONObject outter = new JSONObject();
		outter.put("setField", array);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectSetField.class);
		final ClassWithObjectSetField instance = (ClassWithObjectSetField) serializer.asObject(outter);

		assertNotNull(instance.setField);

		final Set set = instance.setField;
		final ClassWithObjectSetFieldElement element = (ClassWithObjectSetFieldElement) set.iterator().next();
		assertNotNull(element);

		assertEquals(value, element.field);
	}

	static class ClassWithObjectSetField implements JsonSerializable {
		/**
		 * @javascriptPropertyName setField
		 * @setElementType rocket.json.test.client.JsonSerializerGwtTestCase.ClassWithObjectSetFieldElement
		 */
		Set setField;
	}

	static class ClassWithObjectSetFieldElement implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 */
		String field;
	}

	public void testDeserializeClassWithBooleanMap() {
		final boolean value = true;

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", JSONBoolean.getInstance(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithBooleanMapField.class);
		final ClassWithBooleanMapField instance = (ClassWithBooleanMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Boolean wrapper = (Boolean) instance.field.get("value");
		assertEquals(value, wrapper.booleanValue());
	}

	static class ClassWithBooleanMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.Boolean
		 */
		Map field;
	}

	public void testDeserializeClassWithByteMap() {
		final byte value = 123;

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithByteMapField.class);
		final ClassWithByteMapField instance = (ClassWithByteMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Byte wrapper = (Byte) instance.field.get("value");
		assertEquals(value, wrapper.byteValue());
	}

	static class ClassWithByteMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.Byte
		 */
		Map field;
	}

	public void testDeserializeClassWithShortMap() {
		final short value = 123;

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithShortMapField.class);
		final ClassWithShortMapField instance = (ClassWithShortMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Short wrapper = (Short) instance.field.get("value");
		assertEquals(value, wrapper.shortValue());
	}

	static class ClassWithShortMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.Short
		 */
		Map field;
	}

	public void testDeserializeClassWithIntegerMap() {
		final int value = 123;

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithIntegerMapField.class);
		final ClassWithIntegerMapField instance = (ClassWithIntegerMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Integer wrapper = (Integer) instance.field.get("value");
		assertEquals(value, wrapper.intValue());
	}

	static class ClassWithIntegerMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.Integer
		 */
		Map field;
	}

	public void testDeserializeClassWithLongMap() {
		final long value = 123;

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithLongMapField.class);
		final ClassWithLongMapField instance = (ClassWithLongMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Long wrapper = (Long) instance.field.get("value");
		assertEquals(value, wrapper.longValue());
	}

	static class ClassWithLongMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.Long
		 */
		Map field;
	}

	public void testDeserializeClassWithFloatMap() {
		final float value = 123.45f;

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithFloatMapField.class);
		final ClassWithFloatMapField instance = (ClassWithFloatMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Float wrapper = (Float) instance.field.get("value");
		assertEquals(value, wrapper.floatValue(), 0.01f);
	}

	static class ClassWithFloatMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.Float
		 */
		Map field;
	}

	public void testDeserializeClassWithDoubleMap() {
		final double value = 123.45f;

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", new JSONNumber(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithDoubleMapField.class);
		final ClassWithDoubleMapField instance = (ClassWithDoubleMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Double wrapper = (Double) instance.field.get("value");
		assertEquals(value, wrapper.doubleValue(), 0.01f);
	}

	static class ClassWithDoubleMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.Double
		 */
		Map field;
	}

	public void testDeserializeClassWithCharacterMap() {
		final char value = 'a';

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", new JSONString("" + value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithCharacterMapField.class);
		final ClassWithCharacterMapField instance = (ClassWithCharacterMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final Character wrapper = (Character) instance.field.get("value");
		assertEquals(value, wrapper.charValue());
	}

	static class ClassWithCharacterMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.Character
		 */
		Map field;
	}

	public void testDeserializeClassWithStringMap() {
		final String value = "apple";

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", new JSONString(value));

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithStringMapField.class);
		final ClassWithStringMapField instance = (ClassWithStringMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final String wrapper = (String) instance.field.get("value");
		assertEquals(value, wrapper);
	}

	static class ClassWithStringMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType java.lang.String
		 */
		Map field;
	}

	public void testDeserializeClassWithObjectMap() {
		final String value = "apple";

		final JSONObject jsonString = new JSONObject();
		jsonString.put("string", new JSONString(value));

		final JSONObject jsonObjectMap = new JSONObject();
		jsonObjectMap.put("value", jsonString);

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("field", jsonObjectMap);

		final JsonSerializer serializer = (JsonSerializer) GWT.create(ClassWithObjectMapField.class);
		final ClassWithObjectMapField instance = (ClassWithObjectMapField) serializer.asObject(jsonObject);

		assertNotNull(instance.field);

		final ClassWithStringField5 wrapper = (ClassWithStringField5) instance.field.get("value");
		assertEquals(value, wrapper.field);
	}

	static class ClassWithObjectMapField implements JsonSerializable {
		/**
		 * @javascriptPropertyName field
		 * @mapValueType rocket.json.test.client.JsonSerializerGwtTestCase.ClassWithStringField5
		 */
		Map field;
	}

	static class ClassWithStringField5 implements JsonSerializable {
		/**
		 * @javascriptPropertyName string
		 */
		String field;
	}
}
