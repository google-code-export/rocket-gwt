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
package rocket.serialization.test.rebind.serializationfactorygenerator.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.SerializationException;
import rocket.serialization.client.SerializationFactory;
import rocket.serialization.client.SerializationFactoryComposer;

import com.google.gwt.core.client.GWT;

public class SerializationFactoryGeneratorGwtTestCase extends GeneratorGwtTestCase {

	static final boolean BOOLEAN_VALUE = true;

	static final byte BYTE_VALUE = 1;

	static final short SHORT_VALUE = 23;

	static final int INT_VALUE = 456;

	static final long LONG_VALUE = 7890;

	static final float FLOAT_VALUE = 123.5f;

	static final double DOUBLE_VALUE = 1234.5;

	static final char CHAR_VALUE = 'a';

	static final String STRING_VALUE = "apple";

	static final String SERIALIZATION_FACTORY_GENERATOR_EXCEPTION = "rocket.serialization.rebind.SerializationFactoryGeneratorException";

	public String getModuleName() {
		return "rocket.serialization.test.rebind.serializationfactorygenerator.SerializationFactoryGenerator";
	}

	public void testClassThatIsItselfNotSerializable() {
		try {
			assertBindingFailed(GWT.create(UnserializableSerializationFactoryComposer.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(SERIALIZATION_FACTORY_GENERATOR_EXCEPTION));
		} catch (final Exception failed ) {
			assertTrue("" + failed, failed.getCause().getClass().getName().equals(SERIALIZATION_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.Unserializable
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.Unserializable 
	 */
	static class UnserializableSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	public void testClassThatHasAnUnserializableField() {
		try {
			final Object object = GWT.create(ClassWithUnserializableFieldSerializationFactoryComposer.class);
			assertBindingFailed( object );
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(SERIALIZATION_FACTORY_GENERATOR_EXCEPTION));
		} catch (final Exception failed ) {
			assertTrue("" + failed, failed.getCause().getClass().getName().equals(SERIALIZATION_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithUnserializableField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithUnserializableField
	 */
	static class ClassWithUnserializableFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithUnserializableField implements Serializable{
		Unserializable field;
	}
	
	static class Unserializable {
	}
	
	public void testClassWithBooleanField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithBooleanFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithBooleanField writeInstance = new ClassWithBooleanField();
		writeInstance.booleanValue = BOOLEAN_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithBooleanField readInstance = (ClassWithBooleanField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, BOOLEAN_VALUE, readInstance.booleanValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithBooleanField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithBooleanField 
	 */
	static class ClassWithBooleanFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithBooleanField implements Serializable {
		boolean booleanValue;

		public String toString() {
			return super.toString() + ", booleanValue: " + booleanValue;
		}
	}

	public void testClassWithByteField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithByteFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithByteField writeInstance = new ClassWithByteField();
		writeInstance.byteValue = BYTE_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithByteField readInstance = (ClassWithByteField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, BYTE_VALUE, readInstance.byteValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithByteField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithByteField 
	 */
	static class ClassWithByteFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithByteField implements Serializable {
		byte byteValue;

		public String toString() {
			return super.toString() + ", byteValue: " + byteValue;
		}
	}

	public void testClassWithShortField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithShortFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithShortField writeInstance = new ClassWithShortField();
		writeInstance.shortValue = SHORT_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithShortField readInstance = (ClassWithShortField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, SHORT_VALUE, readInstance.shortValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithShortField 
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithShortField
	 */
	static class ClassWithShortFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithShortField implements Serializable {
		short shortValue;

		public String toString() {
			return super.toString() + ", shortValue: " + shortValue;
		}
	}

	public void testClassWithIntField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithIntFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithIntField writeInstance = new ClassWithIntField();
		writeInstance.intValue = INT_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithIntField readInstance = (ClassWithIntField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, INT_VALUE, readInstance.intValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithIntField 
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithIntField
	 */
	static class ClassWithIntFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithIntField implements Serializable {
		int intValue;

		public String toString() {
			return super.toString() + ", intValue: " + intValue;
		}
	}

	public void testClassWithLongField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithLongFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithLongField writeInstance = new ClassWithLongField();
		writeInstance.longValue = LONG_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithLongField readInstance = (ClassWithLongField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, LONG_VALUE, readInstance.longValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithLongField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithLongField 
	 */
	static class ClassWithLongFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithLongField implements Serializable {
		long longValue;

		public String toString() {
			return super.toString() + ", longValue: " + longValue;
		}
	}

	public void testClassWithFloatField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithFloatFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithFloatField writeInstance = new ClassWithFloatField();
		writeInstance.floatValue = FLOAT_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithFloatField readInstance = (ClassWithFloatField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, FLOAT_VALUE, readInstance.floatValue, 0.1f);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithFloatField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithFloatField 
	 */
	static class ClassWithFloatFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithFloatField implements Serializable {
		float floatValue;

		public String toString() {
			return super.toString() + ", floatValue: " + floatValue;
		}
	}

	public void testClassWithDoubleField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithDoubleFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithDoubleField writeInstance = new ClassWithDoubleField();
		writeInstance.doubleValue = DOUBLE_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithDoubleField readInstance = (ClassWithDoubleField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, DOUBLE_VALUE, readInstance.doubleValue, 0.1);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithDoubleField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithDoubleField 
	 */
	static class ClassWithDoubleFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithDoubleField implements Serializable {
		double doubleValue;

		public String toString() {
			return super.toString() + ", doubleValue: " + doubleValue;
		}
	}

	public void testClassWithCharField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithCharFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithCharField writeInstance = new ClassWithCharField();
		writeInstance.charValue = CHAR_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithCharField readInstance = (ClassWithCharField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, CHAR_VALUE, readInstance.charValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithCharField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithCharField 
	 */
	static class ClassWithCharFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithCharField implements Serializable {
		char charValue;

		public String toString() {
			return super.toString() + ", charValue: " + charValue;
		}
	}

	public void testClassWithAnotherClassField() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithAnotherClassFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithAnotherClassField writeInstance = new ClassWithAnotherClassField();
		writeInstance.anotherClass = new AnotherClass();
		writeInstance.anotherClass.intValue = INT_VALUE;

		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithAnotherClassField readInstance = (ClassWithAnotherClassField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.field", readInstance.anotherClass);
		assertEquals("" + readInstance, INT_VALUE, readInstance.anotherClass.intValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithAnotherClassField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithAnotherClassField 
	 */
	static class ClassWithAnotherClassFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithAnotherClassField implements Serializable {
		AnotherClass anotherClass;

		public String toString() {
			return super.toString() + ", field: " + anotherClass;
		}
	}

	static class AnotherClass implements Serializable {
		int intValue;

		public String toString() {
			return super.toString() + ", field: " + intValue;
		}
	}

	public void testClassWithListField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithListFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithListField writeInstance = new ClassWithListField();
		writeInstance.list = new ArrayList();
		final ListElement writeListElement = new ListElement();
		writeListElement.intValue = INT_VALUE;
		writeInstance.list.add(writeListElement);

		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithListField readInstance = (ClassWithListField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.list", readInstance.list);
		assertEquals("readInstance.list.size", 1, readInstance.list.size());
		final ListElement readListElement = (ListElement) readInstance.list.get(0);
		assertEquals(INT_VALUE, readListElement.intValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithListField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithListField
	 */
	static class ClassWithListFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithListField implements Serializable {
		/**
		 * @serialization-type rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ListElement
		 */
		List list;

		public String toString() {
			return super.toString() + ", list: " + list;
		}
	}

	static class ListElement implements Serializable {
		int intValue;

		public String toString() {
			return super.toString() + " intValue: " + intValue;
		}
	}

	public void testClassWithSetField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithSetFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithSetField writeInstance = new ClassWithSetField();
		writeInstance.set = new HashSet();
		final SetElement writeSetElement = new SetElement();
		writeSetElement.intValue = INT_VALUE;
		writeInstance.set.add(writeSetElement);

		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithSetField readInstance = (ClassWithSetField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.set", readInstance.set);
		assertEquals("readInstance.set.size", 1, readInstance.set.size());
		final SetElement readSetElement = (SetElement) readInstance.set.iterator().next();
		assertEquals(INT_VALUE, readSetElement.intValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithSetField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithSetField 
	 */
	static class ClassWithSetFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithSetField implements Serializable {
		/**
		 * @serialization-type rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.SetElement
		 */
		Set set;
	}

	static class SetElement implements Serializable {
		int intValue;
	}

	public void testClassWithMapField() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithMapFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithMapField writeInstance = new ClassWithMapField();
		writeInstance.map = new HashMap();
		final MapKey writeMapKey = new MapKey();
		writeMapKey.string = "apple";
		final MapValue writeMapValue = new MapValue();
		writeMapValue.intValue = INT_VALUE;
		writeInstance.map.put(writeMapKey, writeMapValue);

		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithMapField readInstance = (ClassWithMapField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.map", readInstance.map);
		assertEquals("readInstance.map.size", 1, readInstance.map.size());

		final MapKey readMapKey = (MapKey) readInstance.map.keySet().iterator().next();
		assertNotNull(readMapKey);
		assertEquals("apple", readMapKey.string);

		final MapValue readMapValue = (MapValue) readInstance.map.values().iterator().next();
		assertEquals(INT_VALUE, readMapValue.intValue);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithMapField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithMapField 
	 */
	static class ClassWithMapFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithMapField implements Serializable {
		/**
		 * @serialization-type rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.MapKey
		 * @serialization-type rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.MapValue
		 */
		Map map;
	}

	static class MapKey implements Serializable {
		String string;
	}

	static class MapValue implements Serializable {
		int intValue;
	}

	public void testClassWithBooleanWrapper() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithBooleanWrapperFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithBooleanWrapperField writeInstance = new ClassWithBooleanWrapperField();
		writeInstance.booleanWrapper = BOOLEAN_VALUE ? Boolean.TRUE : Boolean.FALSE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithBooleanWrapperField readInstance = (ClassWithBooleanWrapperField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.booleanWrapper", readInstance.booleanWrapper);
		assertEquals("" + readInstance, BOOLEAN_VALUE, readInstance.booleanWrapper.booleanValue());
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithBooleanWrapperField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithBooleanWrapperField 
	 */
	static class ClassWithBooleanWrapperFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithBooleanWrapperField implements Serializable {
		Boolean booleanWrapper;

		public String toString() {
			return super.toString() + ", booleanWrapper: " + booleanWrapper;
		}
	}

	public void testClassWithByteWrapper() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithByteWrapperFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithByteWrapperField writeInstance = new ClassWithByteWrapperField();
		writeInstance.byteWrapper = new Byte(BYTE_VALUE);
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithByteWrapperField readInstance = (ClassWithByteWrapperField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.byteWrapper", readInstance.byteWrapper);

		assertEquals("" + readInstance, BYTE_VALUE, readInstance.byteWrapper.byteValue());
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithByteWrapperField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithByteWrapperField 
	 */
	static class ClassWithByteWrapperFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithByteWrapperField implements Serializable {
		Byte byteWrapper;

		public String toString() {
			return super.toString() + ", byteWrapper: " + byteWrapper;
		}
	}

	public void testClassWithShortWrapper() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithShortWrapperFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithShortWrapperField writeInstance = new ClassWithShortWrapperField();
		writeInstance.shortWrapper = new Short(SHORT_VALUE);
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithShortWrapperField readInstance = (ClassWithShortWrapperField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.shortWrapper", readInstance.shortWrapper);

		assertEquals("" + readInstance, SHORT_VALUE, readInstance.shortWrapper.shortValue());
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithShortWrapperField 
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithShortWrapperField
	 */
	static class ClassWithShortWrapperFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithShortWrapperField implements Serializable {
		Short shortWrapper;

		public String toString() {
			return super.toString() + ", shortWrapper: " + shortWrapper;
		}
	}

	public void testClassWithIntegerWrapper() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithIntegerWrapperFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithIntegerWrapperField writeInstance = new ClassWithIntegerWrapperField();
		writeInstance.integerWrapper = new Integer(INT_VALUE);
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithIntegerWrapperField readInstance = (ClassWithIntegerWrapperField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.integerWrapper", readInstance.integerWrapper);

		assertEquals("" + readInstance, INT_VALUE, readInstance.integerWrapper.intValue());
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithIntegerWrapperField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithIntegerWrapperField 
	 */
	static class ClassWithIntegerWrapperFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithIntegerWrapperField implements Serializable {
		Integer integerWrapper;

		public String toString() {
			return super.toString() + ", integerWrapper: " + integerWrapper;
		}
	}

	public void testClassWithLongWrapper() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithLongWrapperFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithLongWrapperField writeInstance = new ClassWithLongWrapperField();
		writeInstance.longWrapper = new Long(LONG_VALUE);
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithLongWrapperField readInstance = (ClassWithLongWrapperField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.longWrapper", readInstance.longWrapper);

		assertEquals("" + readInstance, LONG_VALUE, readInstance.longWrapper.longValue());
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithLongWrapperField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithLongWrapperField 
	 */
	static class ClassWithLongWrapperFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithLongWrapperField implements Serializable {
		Long longWrapper;

		public String toString() {
			return super.toString() + ", longWrapper: " + longWrapper;
		}
	}

	public void testClassWithFloatWrapper() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithFloatWrapperFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithFloatWrapperField writeInstance = new ClassWithFloatWrapperField();
		writeInstance.floatWrapper = new Float(FLOAT_VALUE);
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithFloatWrapperField readInstance = (ClassWithFloatWrapperField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.floatWrapper", readInstance.floatWrapper);

		assertEquals("" + readInstance, FLOAT_VALUE, readInstance.floatWrapper.floatValue(), 0.1f);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithFloatWrapperField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithFloatWrapperField 
	 */
	static class ClassWithFloatWrapperFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithFloatWrapperField implements Serializable {
		Float floatWrapper;

		public String toString() {
			return super.toString() + ", floatWrapper: " + floatWrapper;
		}
	}

	public void testClassWithDoubleWrapper() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithDoubleWrapperFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithDoubleWrapperField writeInstance = new ClassWithDoubleWrapperField();
		writeInstance.doubleWrapper = new Double(DOUBLE_VALUE);
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithDoubleWrapperField readInstance = (ClassWithDoubleWrapperField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.doubleWrapper", readInstance.doubleWrapper);

		assertEquals("" + readInstance, DOUBLE_VALUE, readInstance.doubleWrapper.doubleValue(), 0.1);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithDoubleWrapperField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithDoubleWrapperField 
	 */
	static class ClassWithDoubleWrapperFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithDoubleWrapperField implements Serializable {
		Double doubleWrapper;

		public String toString() {
			return super.toString() + ", doubleWrapper: " + doubleWrapper;
		}
	}

	public void testClassWithCharacterWrapper() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithCharacterWrapperFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithCharacterWrapperField writeInstance = new ClassWithCharacterWrapperField();
		writeInstance.characterWrapper = new Character(CHAR_VALUE);
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithCharacterWrapperField readInstance = (ClassWithCharacterWrapperField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance.characterWrapper", readInstance.characterWrapper);

		assertEquals("" + readInstance, CHAR_VALUE, readInstance.characterWrapper.charValue());
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithCharacterWrapperField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithCharacterWrapperField 
	 */
	static class ClassWithCharacterWrapperFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithCharacterWrapperField implements Serializable {
		Character characterWrapper;

		public String toString() {
			return super.toString() + ", characterWrapper: " + characterWrapper;
		}
	}

	public void testClassWithBooleanArray() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithBooleanArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithBooleanArrayField writeInstance = new ClassWithBooleanArrayField();
		writeInstance.booleanArray = new boolean[] { BOOLEAN_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithBooleanArrayField readInstance = (ClassWithBooleanArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.booleanArray);

		assertEquals("" + readInstance, writeInstance.booleanArray.length, readInstance.booleanArray.length);
		assertEquals("" + readInstance, BOOLEAN_VALUE, readInstance.booleanArray[0]);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithBooleanArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithBooleanArrayField 
	 */
	static class ClassWithBooleanArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithBooleanArrayField implements Serializable {
		boolean[] booleanArray;

		public String toString() {
			return super.toString() + ", booleanArray: " + booleanArray;
		}
	}

	public void testClassWithByteArray() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithByteArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithByteArrayField writeInstance = new ClassWithByteArrayField();
		writeInstance.byteArray = new byte[] { BYTE_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithByteArrayField readInstance = (ClassWithByteArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.byteArray);

		assertEquals("" + readInstance, writeInstance.byteArray.length, readInstance.byteArray.length);
		assertEquals("" + readInstance, BYTE_VALUE, readInstance.byteArray[0]);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithByteArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithByteArrayField 
	 */
	static class ClassWithByteArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithByteArrayField implements Serializable {
		byte[] byteArray;

		public String toString() {
			return super.toString() + ", byteArray: " + byteArray;
		}
	}

	public void testClassWithShortArray() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithShortArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithShortArrayField writeInstance = new ClassWithShortArrayField();
		writeInstance.shortArray = new short[] { SHORT_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithShortArrayField readInstance = (ClassWithShortArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.shortArray);

		assertEquals("" + readInstance, writeInstance.shortArray.length, readInstance.shortArray.length);
		assertEquals("" + readInstance, SHORT_VALUE, readInstance.shortArray[0], 0.1f);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithShortArrayField 
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithShortArrayField
	 */
	static class ClassWithShortArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {

	}

	static class ClassWithShortArrayField implements Serializable {
		short[] shortArray;

		public String toString() {
			return super.toString() + ", shortArray: " + shortArray;
		}
	}

	public void testClassWithIntArray() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithIntegerArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithIntegerArrayField writeInstance = new ClassWithIntegerArrayField();
		writeInstance.intArray = new int[] { INT_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithIntegerArrayField readInstance = (ClassWithIntegerArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.intArray);

		assertEquals("" + readInstance, writeInstance.intArray.length, readInstance.intArray.length);
		assertEquals("" + readInstance, INT_VALUE, readInstance.intArray[0]);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithIntegerArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithIntegerArrayField 
	 */
	static class ClassWithIntegerArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithIntegerArrayField implements Serializable {
		int[] intArray;

		public String toString() {
			return super.toString() + ", intArray: " + intArray;
		}
	}

	public void testClassWithLongArray() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithLongArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithLongArrayField writeInstance = new ClassWithLongArrayField();
		writeInstance.longArray = new long[] { LONG_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithLongArrayField readInstance = (ClassWithLongArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance.longArray);

		assertEquals("" + readInstance, writeInstance.longArray.length, readInstance.longArray.length);
		assertEquals("" + readInstance, LONG_VALUE, readInstance.longArray[0]);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithLongArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithLongArrayField 
	 */
	static class ClassWithLongArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithLongArrayField implements Serializable {
		long[] longArray;

		public String toString() {
			return super.toString() + ", longArray: " + longArray;
		}
	}

	public void testClassWithFloatArray() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithFloatArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithFloatArrayField writeInstance = new ClassWithFloatArrayField();
		writeInstance.floatArray = new float[] { FLOAT_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithFloatArrayField readInstance = (ClassWithFloatArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.floatArray);

		assertEquals("" + readInstance, writeInstance.floatArray.length, readInstance.floatArray.length);
		assertEquals("" + readInstance, FLOAT_VALUE, readInstance.floatArray[0], 0.1f);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithFloatArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithFloatArrayField 
	 */
	static class ClassWithFloatArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithFloatArrayField implements Serializable {
		float[] floatArray;

		public String toString() {
			return super.toString() + ", floatArray: " + floatArray;
		}
	}

	public void testClassWithDoubleArray() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithDoubleArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithDoubleArrayField writeInstance = new ClassWithDoubleArrayField();
		writeInstance.doubleArray = new double[] { DOUBLE_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithDoubleArrayField readInstance = (ClassWithDoubleArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.doubleArray);

		assertEquals("" + readInstance, writeInstance.doubleArray.length, readInstance.doubleArray.length);
		assertEquals("" + readInstance, DOUBLE_VALUE, readInstance.doubleArray[0], 0.1);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithDoubleArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithDoubleArrayField 
	 */
	static class ClassWithDoubleArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithDoubleArrayField implements Serializable {
		double[] doubleArray;

		public String toString() {
			return super.toString() + ", doubleArray: " + doubleArray;
		}
	}

	public void testClassWithCharacterArray() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithCharacterArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithCharacterArrayField writeInstance = new ClassWithCharacterArrayField();
		writeInstance.charArray = new char[] { CHAR_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithCharacterArrayField readInstance = (ClassWithCharacterArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.charArray);

		assertEquals("" + readInstance, writeInstance.charArray.length, readInstance.charArray.length);
		assertEquals("" + readInstance, CHAR_VALUE, readInstance.charArray[0]);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithCharacterArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithCharacterArrayField 
	 */
	static class ClassWithCharacterArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithCharacterArrayField implements Serializable {
		char[] charArray;

		public String toString() {
			return super.toString() + ", charArray: " + charArray;
		}
	}

	public void testClassWithString() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithStringFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithStringField writeInstance = new ClassWithStringField();
		writeInstance.string = STRING_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithStringField readInstance = (ClassWithStringField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, STRING_VALUE, readInstance.string);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithStringField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithStringField 
	 */
	static class ClassWithStringFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithStringField implements Serializable {
		String string;

		public String toString() {
			return super.toString() + ", string: " + string;
		}
	}

	public void testClassWithStringArray() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(ClassWithStringArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithStringArrayField writeInstance = new ClassWithStringArrayField();
		writeInstance.stringArray = new String[] { STRING_VALUE };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithStringArrayField readInstance = (ClassWithStringArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.stringArray);

		assertEquals("" + readInstance, writeInstance.stringArray.length, readInstance.stringArray.length);
		assertEquals("" + readInstance, STRING_VALUE, readInstance.stringArray[0]);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithStringArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithStringArrayField 
	 */
	static class ClassWithStringArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithStringArrayField implements Serializable {
		String[] stringArray;

		public String toString() {
			return super.toString() + ", stringArray: " + stringArray;
		}
	}

	public void testClassWithConcreteClassArray() {
		final SerializationFactory factory = (SerializationFactory) GWT
				.create(ClassWithConcreteClassArrayFieldSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final ClassWithConcreteClassArrayField writeInstance = new ClassWithConcreteClassArrayField();
		writeInstance.concreteClassArray = new ConcreteClass[] { new ConcreteClass() };
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final ClassWithConcreteClassArrayField readInstance = (ClassWithConcreteClassArrayField) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);
		assertNotNull("readInstance", readInstance.concreteClassArray);

		assertEquals("" + readInstance, writeInstance.concreteClassArray.length, readInstance.concreteClassArray.length);
		assertNotNull("" + readInstance, readInstance.concreteClassArray[0]);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithConcreteClassArrayField
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.ClassWithConcreteClassArrayField 
	 */
	static class ClassWithConcreteClassArrayFieldSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class ClassWithConcreteClassArrayField implements Serializable {
		ConcreteClass[] concreteClassArray;

		public String toString() {
			return super.toString() + ", concreteClassArray: " + concreteClassArray;
		}
	}

	static class ConcreteClass implements Serializable {
	}

	final static int SUB_CLASS_VALUE = 123;

	final static int SUPER_CLASS_VALUE = 456;
	
	public void testSubClassWithSuperTypeThatIsNotSerializable() {
		final SerializationFactory factory = (SerializationFactory) GWT.create(SubClassWithUnserializableSuperTypeSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final SubClassWithUnserializableSuperType writeInstance = new SubClassWithUnserializableSuperType();
		writeInstance.subClassValue = SUB_CLASS_VALUE;
		writeInstance.superClassValue = SUPER_CLASS_VALUE;
		objectOutputStream.writeObject(writeInstance);

		final String stream = objectOutputStream.getText();
		final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
		assertNotNull("objectInputStream", objectInputStream);

		final SubClassWithUnserializableSuperType readInstance = (SubClassWithUnserializableSuperType) objectInputStream.readObject();
		assertNotNull("readInstance", readInstance);

		assertEquals("" + readInstance, SUPER_CLASS_VALUE, readInstance.superClassValue);
		assertEquals("" + readInstance, SUB_CLASS_VALUE, readInstance.subClassValue);		
	}
	
	public void testAttemptToWriteUnserializableTypeFails(){
		final SerializationFactory factory = (SerializationFactory) GWT.create(SubClassWithUnserializableSuperTypeSerializationFactoryComposer.class);
		assertNotNull("factory", factory);

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		assertNotNull("objectOutputStream", objectOutputStream);

		final UnserializableConcreteClass writeInstance = new UnserializableConcreteClass();
		writeInstance.superClassValue = SUPER_CLASS_VALUE;
		
		try{
			objectOutputStream.writeObject(writeInstance);
			fail( "A SerializationException should have been thrown because " + writeInstance + " is not serializable.");
		} catch ( SerializationException expected ){			
		}
	}

	/**
	 * @serialization-readableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.SubClassWithUnserializableSuperType 
	 * @serialization-writableTypes rocket.serialization.test.rebind.serializationfactorygenerator.client.SerializationFactoryGeneratorGwtTestCase.SubClassWithUnserializableSuperType
	 */
	static class SubClassWithUnserializableSuperTypeSerializationFactoryComposer implements SerializationFactoryComposer {
	}

	static class SubClassWithUnserializableSuperType extends UnserializableConcreteClass implements Serializable {
		int subClassValue;

		@Override
		public String toString() {
			return super.toString() + ", subClassValue: " + subClassValue;
		}
	}

	static class UnserializableConcreteClass {
		int superClassValue;

		@Override
		public String toString() {
			return super.toString() + ", superClassValue: " + superClassValue;
		}
	}

	protected void checkFurtherReadsFail(final ObjectInputStream reader) {
		try {
			final int got = reader.readInt();
			fail("An exception should have been thrown when attempting to read an already completely consumed reader, but \"" + got
					+ "\" was returned...");
		} catch (final Exception expected) {

		}
	}

	protected ObjectInputStream createObjectInputStream(final String stream) {
		throw new UnsupportedOperationException("createObjectInputStream");
	}

}
