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
package rocket.serialization.test.clientobjectoutputstream.client;

import java.util.HashMap;
import java.util.Map;

import rocket.serialization.client.ClientObjectOutputStream;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectWriter;
import rocket.util.client.Checker;

import com.google.gwt.junit.client.GWTTestCase;

public class ClientObjectOutputStreamGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.serialization.test.clientobjectoutputstream.ClientObjectOutputStream";
	}

	final String APPLE = "apple";

	final String BANANA = "banana";

	final String CARROT = "carrot";

	final static String CONCRETE_CLASS = "rocket.serialization.test.clientobjectoutputstream.client.ConcreteClass";

	final static String HASHSET = "java.util.Hashset";

	final static String SET = "java.util.Set";

	final static String CONCRETE_SUBCLASS = "rocket.serialization.test.clientobjectoutputstream.client.ConcreteSubClass";

	final static String HASHMAP = "java.util.HashMap";

	final static String STRING = "java.lang.String";

	public void testEscape() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		final String input = "apple";
		final String actual = output.escape(input);
		final String expected = actual;

		assertEquals(input, expected, actual);
	}

	public void testEscapeDoubleQuote() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		final String input = "\"apple\"";
		final String actual = output.escape(input);
		final String expected = "\\\"apple\\\"";

		assertEquals(input, expected, actual);
	}

	public void testWriteBoolean() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeBoolean(true);
		output.writeBoolean(false);

		final String text = output.getText();
		assertEquals("" + output, "[0,true,false]", text);
	}

	//
	public void testWriteByte() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeByte((byte) 0);
		output.writeByte((byte) 1);
		output.writeByte((byte) 2);
		output.writeByte((byte) -3);

		final String text = output.getText();
		assertEquals("" + output, "[0,0,1,2,-3]", text);
	}

	public void testWriteShort() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeShort((short) 0);
		output.writeShort((short) 1);
		output.writeShort((short) 2);
		output.writeShort((short) -3);

		final String text = output.getText();
		assertEquals("" + output, "[0,0,1,2,-3]", text);
	}

	public void testWriteInt() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeInt((int) 0);
		output.writeInt((int) 1);
		output.writeInt((int) 2);
		output.writeInt((int) -3);

		final String text = output.getText();
		assertEquals("" + output, "[0,0,1,2,-3]", text);
	}

	public void testWriteLong() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeLong((long) 0);
		output.writeLong((long) 1);
		output.writeLong((long) 2);
		output.writeLong((long) -3);

		final String text = output.getText();
		assertEquals("" + output, "[0,0,0,0,1,0,2,-1,-3]", text);
	}

	public void testWriteFloat() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeFloat((float) 0);
		output.writeFloat((float) 1.25);
		output.writeFloat((float) 2.5);
		output.writeFloat((float) -3.75);

		final String text = output.getText();
		assertEquals("" + output, "[0,0,1.25,2.5,-3.75]", text);
	}

	public void testWriteDouble() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeDouble((double) 0);
		output.writeDouble((double) 1.25);
		output.writeDouble((double) 2.5);
		output.writeDouble((double) -3.75);

		final String text = output.getText();
		assertEquals("" + output, "[0,0,1.25,2.5,-3.75]", text);
	}

	public void testWriteChar() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeChar('\0');
		output.writeChar('\t');
		output.writeChar('a');
		output.writeChar('A');

		final String expectedValues = "0," + (int) '\t' + "," + (int) 'a' + "," + (int) 'A';

		final String text = output.getText();
		assertEquals("" + output, "[0," + expectedValues + "]", text);
	}

	//
	public void testWriteNull() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeObject(null);

		final String text = output.getText();
		assertEquals("" + output, "[0,0]", text);
	}

	public void testWriteStringReferences() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeObject(APPLE);
		output.writeObject(BANANA);
		output.writeObject(CARROT);

		final String text = output.getText();
		assertEquals("" + output, "[3,\"" + APPLE + "\",\"" + BANANA + "\",\"" + CARROT + "\",2,3,4]", text);
	}

	public void testWriteStringWithDuplicateStrings() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeObject(APPLE);
		output.writeObject(BANANA);
		output.writeObject(CARROT);
		output.writeObject(APPLE);
		output.writeObject(BANANA);
		output.writeObject(CARROT);

		final String text = output.getText();
		assertEquals("" + output, "[3,\"" + APPLE + "\",\"" + BANANA + "\",\"" + CARROT + "\",2,3,4,2,3,4]", text);
	}

	public void testWriteUnserializableType() {
		final TestClientObjectOutputStream output = createObjectOutputStream();
		try {
			output.writeObject(new Unserializable());
			fail("An exception should have been thrown when attempting to serialize a type that doesnt not implement Serializable");
		} catch (Exception expected) {

		}
	}

	static class Unserializable {
	}

	public void testWriteSingleInstance() {
		final ConcreteClass concreteClass = createConcreteClass();

		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeObject(concreteClass);

		final String text = output.getText();
		assertEquals("" + output, "[1,\"" + CONCRETE_CLASS + "\",1,2," + ConcreteClass.VALUE + "]", text);
	}

	public void testWriteTheSameSameInstanceTwice() {

		final TestClientObjectOutputStream output = createObjectOutputStream();

		final ConcreteClass concreteClass = createConcreteClass();

		output.writeObject(concreteClass);
		output.writeObject(concreteClass);

		final String text = output.getText();
		assertEquals("" + output, "[1,\"" + CONCRETE_CLASS + "\",1,2," + concreteClass.value + ",-1]", text);
	}

	public void testWriteTwoDifferentInstances() {
		final TestClientObjectOutputStream output = createObjectOutputStream();

		final ConcreteClass first = createConcreteClass();
		output.writeObject(first);

		final ConcreteClass second = createConcreteClass();
		output.writeObject(second);

		final String text = output.getText();
		assertEquals("" + output, "[1,\"" + CONCRETE_CLASS + "\",1,2," + ConcreteClass.VALUE + ",1,2," + ConcreteClass.VALUE + "]", text);
	}

	public void testWriteTypeWithHeirarchy() {
		final ConcreteSubClass concreteSubClass = createConcreteSubClass();

		final TestClientObjectOutputStream output = createObjectOutputStream();
		output.writeObject(concreteSubClass);

		final String text = output.getText();
		assertEquals("" + output, "[1,\"" + CONCRETE_SUBCLASS + "\",1,2," + ConcreteSubClass.VALUE + "," + ConcreteClass.VALUE + "]",
				text);
	}

	protected ConcreteClass createConcreteClass() {
		final ConcreteClass concreteClass = new ConcreteClass();
		concreteClass.value = ConcreteClass.VALUE;
		return concreteClass;
	}

	protected ConcreteSubClass createConcreteSubClass() {
		final ConcreteSubClass concreteSubClass = new ConcreteSubClass();
		concreteSubClass.value = ConcreteClass.VALUE;
		concreteSubClass.value2 = ConcreteSubClass.VALUE;
		return concreteSubClass;
	}

	protected void checkFurtherReadsFail(final ObjectInputStream reader) {
		try {
			final int got = reader.readInt();
			fail("An exception should have been thrown when attempting to read a consumed reader, but \"" + got + "\" was returned...");
		} catch (final Exception expected) {

		}
	}

	protected TestClientObjectOutputStream createObjectOutputStream() {
		final Map writers = new HashMap();
		writers.put(CONCRETE_CLASS, new ConcreteClassObjectWriter());
		writers.put(CONCRETE_SUBCLASS, new ConcreteSubClassObjectWriter());

		final TestClientObjectOutputStream objectOutputStream = new TestClientObjectOutputStream();
		objectOutputStream.setObjectWriters(writers);
		return objectOutputStream;
	}

	class TestClientObjectOutputStream extends ClientObjectOutputStream {

		public TestClientObjectOutputStream() {
			super();
		}

		public String escape(final String unescaped) {
			return super.escape(unescaped);
		}

		protected ObjectWriter getObjectWriter(final String typeName) {
			return (ObjectWriter) this.getObjectWriters().get(typeName);
		}

		/**
		 * A map that maps type names to ObjectWriters. If an entry is not found
		 * this indicates the type is not serializable or something went wrong
		 * within the generator.
		 */
		private Map objectWriters;

		protected Map getObjectWriters() {
			Checker.notNull("field:objectWriters", objectWriters);
			return this.objectWriters;
		}

		public void setObjectWriters(final Map objectWriters) {
			Checker.notNull("parameter:objectWriters", objectWriters);
			this.objectWriters = objectWriters;
		}
	}
}
