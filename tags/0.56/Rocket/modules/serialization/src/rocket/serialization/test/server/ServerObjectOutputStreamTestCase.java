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
package rocket.serialization.test.server;

import java.util.Collections;

import rocket.serialization.server.ServerObjectWriter;

public class ServerObjectOutputStreamTestCase extends ServerTestCase {

	public void testEscape() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		final String input = "apple";
		final String actual = output.escape(input);
		final String expected = actual;

		assertEquals(input, expected, actual);
	}

	public void testEscapeTab() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		final String input = "\t";
		final String actual = output.escape(input);
		final String expected = actual;

		assertEquals(input, expected, actual);
	}

	public void testEscapeNewline() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		final String input = "\n";
		final String actual = output.escape(input);
		final String expected = actual;

		assertEquals(input, expected, actual);
	}

	public void testEscapeReturn() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		final String input = "\r";
		final String actual = output.escape(input);
		final String expected = actual;

		assertEquals(input, expected, actual);
	}

	public void testEscapeNullChar() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		final String input = "\0";
		final String actual = output.escape(input);
		final String expected = actual;

		assertEquals(input, expected, actual);
	}

	public void testEscapeFunnyChar() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		final String input = "" + (char) 20;
		final String actual = output.escape(input);
		final String expected = "\\x14;";

		assertEquals(input, expected, actual);
	}

	public void testEscapeDoubleQuote() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		final String input = "\"apple\"";
		final String actual = output.escape(input);
		final String expected = "\\\"apple\\\"";

		assertEquals(input, expected, actual);
	}

	public void testEscapeBackslash() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		final String input = "\\apple";
		final String actual = output.escape(input);
		final String expected = "\\\\apple";

		assertEquals(input, expected, actual);
	}

	public void testWriteBoolean() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeBoolean(true);
		output.writeBoolean(false);

		final String text = output.getText();
		assertEquals("[0,true,false]", text);
	}

	public void testWriteByte() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeByte((byte) 0);
		output.writeByte((byte) 1);
		output.writeByte((byte) 2);
		output.writeByte((byte) -3);

		final String text = output.getText();
		assertEquals("[0,0,1,2,-3]", text);
	}

	public void testWriteShort() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeShort((short) 0);
		output.writeShort((short) 1);
		output.writeShort((short) 2);
		output.writeShort((short) -3);

		final String text = output.getText();
		assertEquals("[0,0,1,2,-3]", text);
	}

	public void testWriteInt() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeInt((int) 0);
		output.writeInt((int) 1);
		output.writeInt((int) 2);
		output.writeInt((int) -3);

		final String text = output.getText();
		assertEquals("[0,0,1,2,-3]", text);
	}

	public void testWriteLong() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeLong((long) 0);
		output.writeLong((long) 1);
		output.writeLong((long) 2);
		output.writeLong((long) -3);

		final String text = output.getText();
		assertEquals("[0,0,0,0,1,0,2,-1,-3]", text);
	}

	public void testWriteFloat() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeFloat((float) 0);
		output.writeFloat((float) 1.25);
		output.writeFloat((float) 2.5);
		output.writeFloat((float) -3.75);

		final String text = output.getText();
		assertEquals("[0,0.0,1.25,2.5,-3.75]", text);
	}

	public void testWriteDouble() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeDouble((double) 0);
		output.writeDouble((double) 1.25);
		output.writeDouble((double) 2.5);
		output.writeDouble((double) -3.75);

		final String text = output.getText();
		assertEquals("[0,0.0,1.25,2.5,-3.75]", text);
	}

	public void testWriteChar() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeChar('\0');
		output.writeChar('\t');
		output.writeChar('a');
		output.writeChar('A');

		final String expectedValues = "0," + (int) '\t' + "," + (int) 'a' + "," + (int) 'A';
		final String text = output.getText();
		assertEquals("[0," + expectedValues + "]", text);
	}

	//
	public void testWriteNull() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeObject(null);

		final String text = output.getText();
		assertEquals("[0,0]", text);
	}

	public void testWriteStringReferences() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeObject(APPLE);
		output.writeObject(BANANA);
		output.writeObject(CARROT);

		final String expectedValues = "2,3,4";
		final String text = output.getText();
		assertEquals("[3,\"" + APPLE + "\",\"" + BANANA + "\",\"" + CARROT + "\"," + expectedValues + "]", text);
	}

	public void testWriteStringWithDuplicateStrings() {
		final TestServerObjectOutputStream output = createObjectOutputStream();
		output.writeObject(APPLE);
		output.writeObject(BANANA);
		output.writeObject(CARROT);
		output.writeObject(APPLE);
		output.writeObject(BANANA);
		output.writeObject(CARROT);

		final String expectedValues = "2,3,4,2,3,4";
		final String text = output.getText();
		assertEquals("[3,\"" + APPLE + "\",\"" + BANANA + "\",\"" + CARROT + "\"," + expectedValues + "]", text);
	}

	public void testWriteSingleInstance() {
		final ConcreteClass concreteClass = createConcreteClass();

		final TestServerObjectOutputStream output = createObjectOutputStream(new ConcreteClassObjectWriter());
		output.writeObject(concreteClass);

		final String expectedValues = "1,2," + ConcreteClass.VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

	public void testWriteTheSameInstanceObjectTwice() {

		final TestServerObjectOutputStream output = createObjectOutputStream(new ConcreteClassObjectWriter());

		final ConcreteClass concreteClass = createConcreteClass();

		output.writeObject(concreteClass);
		output.writeObject(concreteClass);

		final String expectedValues = "1,2," + ConcreteClass.VALUE + ",-1";
		final String text = output.getText();
		assertEquals("[1,\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

	public void testWriteTwoDifferentInstances() {
		final TestServerObjectOutputStream output = createObjectOutputStream(new ConcreteClassObjectWriter());

		final ConcreteClass first = createConcreteClass();
		output.writeObject(first);

		final ConcreteClass second = createConcreteClass();
		output.writeObject(second);

		final String expectedValues = "1,2," + ConcreteClass.VALUE + ",1,2," + ConcreteClass.VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

	public void testWriteTypeWithHeirarchy() {
		final ConcreteSubClass subclass = createConcreteSubClass();

		final TestServerObjectOutputStream output = createObjectOutputStream(new ConcreteSubClassObjectWriter());
		output.writeObject(subclass);

		final String expectedValues = "1,2," + ConcreteSubClass.VALUE + "," + ConcreteClass.VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + CONCRETE_SUBCLASS + "\"," + expectedValues + "]", text);
	}

	protected TestServerObjectOutputStream createObjectOutputStream(final ServerObjectWriter writer) {
		return this.createObjectOutputStream(Collections.nCopies(1, writer));
	}
}
