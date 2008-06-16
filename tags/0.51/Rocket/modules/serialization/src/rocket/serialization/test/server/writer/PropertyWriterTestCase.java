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
package rocket.serialization.test.server.writer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.server.writer.PropertyWriter;

public class PropertyWriterTestCase extends TestCase {

	static final byte BYTE = 1;
	static final short SHORT = 2;
	static final int INT = 3;
	static final long LONG = 4;
	static final float FLOAT = 5.5f;
	static final double DOUBLE = 6.75f;
	static final char CHAR = 'a';
	static final Object OBJECT = "apple";
	static final String STRING = "apple";

	public void testStaticProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithStaticProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 1, values.size());
	}

	static class ConcreteClassWithStaticProperty {

		static public Object getProperty() {
			return null;
		}
	}

	public void testNotPublicProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithNotPublicPropertyGetter();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 1, values.size());
	}

	static class ConcreteClassWithNotPublicPropertyGetter {

		protected Object getProperty() {
			return null;
		}
	}

	public void testNotAPropertyGetter() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithNotAPropertyGetter();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 1, values.size());
	}

	static class ConcreteClassWithNotAPropertyGetter {

		protected Object getProperty() {
			return null;
		}
	}

	public void testBooleanProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithBooleanProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 2, values.size());
		assertEquals("" + values, Boolean.TRUE, values.get(1));
	}

	static class ConcreteClassWithBooleanProperty {
		public boolean isBoolean() {
			return true;
		}
	}

	public void testByteProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithByteProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 2, values.size());
		assertEquals("" + values, new Byte(BYTE), values.get(1));
	}

	static class ConcreteClassWithByteProperty {
		public byte getByte() {
			return BYTE;
		}
	}

	public void testShortProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithShortProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 2, values.size());
		assertEquals("" + values, new Short(SHORT), values.get(1));
	}

	static class ConcreteClassWithShortProperty {
		public short getShort() {
			return SHORT;
		}
	}

	public void testIntProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithIntProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 2, values.size());
		assertEquals("" + values, new Integer(INT), values.get(1));
	}

	static class ConcreteClassWithIntProperty {
		public int getInt() {
			return INT;
		}
	}

	public void testLongProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithLongProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 2, values.size());
		assertEquals("" + values, new Long(LONG), values.get(1));
	}

	static class ConcreteClassWithLongProperty {
		public long getLong() {
			return LONG;
		}
	}

	public void testFloatProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithFloatProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 2, values.size());
		assertEquals("" + values, new Float(FLOAT), values.get(1));
	}

	static class ConcreteClassWithFloatProperty {
		public float getFloat() {
			return FLOAT;
		}
	}

	public void testDoubleProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithDoubleProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 2, values.size());
		assertEquals("" + values, new Double(DOUBLE), values.get(1));
	}

	static class ConcreteClassWithDoubleProperty {
		public double getDouble() {
			return DOUBLE;
		}
	}

	public void testCharProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithCharProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 2, values.size());
		assertEquals("" + values, new Character(CHAR), values.get(1));
	}

	static class ConcreteClassWithCharProperty {
		public char getChar() {
			return CHAR;
		}
	}

	public void testObjectProperty() {
		final PropertyWriter writer = this.createPropertyWriter();
		final TestObjectOutputStream outputStream = this.createObjectOutputStream();

		final Object instance = new ConcreteClassWithObjectProperty();
		writer.write(instance, outputStream);

		final List<Object> values = outputStream.getValues();
		assertNotNull(values);
		assertEquals("" + values, 3, values.size());
		assertTrue("" + values, values.remove(STRING));
		assertEquals("" + values, OBJECT, values.get(1));
	}

	static class ConcreteClassWithObjectProperty {
		public Object getObject() {
			return OBJECT;
		}

		public String getString() {
			return STRING;
		}
	}

	PropertyWriter createPropertyWriter() {
		return new PropertyWriter() {
			public boolean canWrite(final Object instance) {
				return true;
			}
		};
	}

	final TestObjectOutputStream createObjectOutputStream() {
		return new TestObjectOutputStream();
	}

	static class TestObjectOutputStream implements ObjectOutputStream {
		public void writeBoolean(boolean booleanValue) {
			this.getValues().add(Boolean.valueOf(booleanValue));
		}

		public void writeByte(byte byteValue) {
			this.getValues().add(new Byte(byteValue));
		}

		public void writeShort(short shortValue) {
			this.getValues().add(new Short(shortValue));
		}

		public void writeInt(int intValue) {
			this.getValues().add(new Integer(intValue));
		}

		public void writeLong(final long longValue) {
			this.getValues().add(new Long(longValue));
		}

		public void writeFloat(float floatValue) {
			this.getValues().add(new Float(floatValue));
		}

		public void writeDouble(double doubleValue) {
			this.getValues().add(new Double(doubleValue));
		}

		public void writeChar(char charValue) {
			this.getValues().add(new Character(charValue));
		}

		public void writeObject(Object object) {
			this.getValues().add(object);
		}

		private List<Object> values = new ArrayList<Object>();

		public List<Object> getValues() {
			return this.values;
		}

		public String getText() {
			throw new UnsupportedOperationException();
		}
	}
}
