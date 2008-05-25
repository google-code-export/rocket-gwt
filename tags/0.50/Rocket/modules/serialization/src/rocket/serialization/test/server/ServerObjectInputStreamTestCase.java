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

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;

public class ServerObjectInputStreamTestCase extends ServerTestCase {

	public void testReadBoolean() {
		final String stream = "[0,true,false]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertTrue(reader.readBoolean());
		assertFalse(reader.readBoolean());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadByte() {
		final String stream = "[0,1,2,-3]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, 1, reader.readByte());
		assertEquals(stream, 2, reader.readByte());
		assertEquals(stream, -3, reader.readByte());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadShort() {
		final String stream = "[0,1,2,-3]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, 1, reader.readShort());
		assertEquals(stream, 2, reader.readShort());
		assertEquals(stream, -3, reader.readShort());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadInt() {
		final String stream = "[0,1,2,-3]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, 1, reader.readInt());
		assertEquals(stream, 2, reader.readInt());
		assertEquals(stream, -3, reader.readInt());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadLong() {
		final String stream = "[0,1,2,-3]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, 1, reader.readLong());
		assertEquals(stream, 2, reader.readLong());
		assertEquals(stream, -3, reader.readLong());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadFloat() {
		final String stream = "[0,1.25,2.5,-3.75]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, 1.25, reader.readFloat(), 0.1);
		assertEquals(stream, 2.5, reader.readFloat(), 0.1);
		assertEquals(stream, -3.75, reader.readFloat(), 0.1);

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadDouble() {
		final String stream = "[0,1.25,2.5,-3.75]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, 1.25, reader.readDouble(), 0.1);
		assertEquals(stream, 2.5, reader.readDouble(), 0.1);
		assertEquals(stream, -3.75, reader.readDouble(), 0.1);

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadChar() {
		final String stream = "[0," + (int) 'a' + "]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, 'a', reader.readChar());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadNullString() {
		final String stream = "[0,0]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, null, reader.readObject());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadString() {
		final String stream = "[1,\"apple\",2]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, "apple", reader.readObject());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadSameStringTwice() {
		final String stream = "[1,\"apple\",2,2]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, "apple", reader.readObject());
		assertEquals(stream, "apple", reader.readObject());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadTwoDifferentStrings() {
		final String stream = "[2,\"apple\",\"banana\",2,3]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		assertEquals(stream, "apple", reader.readObject());
		assertEquals(stream, "banana", reader.readObject());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadConcreteClass() {
		final String stream = "[1,\"" + CONCRETE_CLASS + "\",1,2," + ConcreteClass.VALUE + "]";
		final ObjectInputStream reader = createObjectInputStream(stream, new ConcreteClassObjectReader());
		final ConcreteClass concreteClass = (ConcreteClass) reader.readObject();
		assertNotNull(concreteClass);
		assertEquals("concreteClass.value", ConcreteClass.VALUE, concreteClass.value);

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadTheSameInstanceTwice() {
		final String stream = "[1,\"" + CONCRETE_CLASS + "\",1,2," + ConcreteClass.VALUE + ",-1]";
		final ObjectInputStream reader = createObjectInputStream(stream, new ConcreteClassObjectReader());
		final ConcreteClass concreteClass = (ConcreteClass) reader.readObject();
		assertNotNull(concreteClass);
		assertEquals("concreteClass.value", ConcreteClass.VALUE, concreteClass.value);

		final ConcreteClass secondConcreteClass = (ConcreteClass) reader.readObject();
		assertSame(concreteClass, secondConcreteClass);

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadTwoDifferentInstances() {
		final String stream = "[1,\"" + CONCRETE_CLASS + "\",1,2," + ConcreteClass.VALUE + ",1,2," + ConcreteClass.VALUE + "]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		final ConcreteClass concreteClass = (ConcreteClass) reader.readObject();
		assertNotNull(concreteClass);
		assertEquals("concreteClass.value", ConcreteClass.VALUE, concreteClass.value);

		final ConcreteClass secondConcreteClass = (ConcreteClass) reader.readObject();
		assertNotSame(concreteClass, secondConcreteClass);
		assertNotNull(concreteClass);
		assertEquals("secondConcreteClass.value", ConcreteClass.VALUE, secondConcreteClass.value);

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadConcreteSubClass() {
		final String stream = "[1,\"" + CONCRETE_SUBCLASS + "\",1,2," + ConcreteSubClass.VALUE + "," + ConcreteClass.VALUE + "]";
		final ObjectInputStream reader = createObjectInputStream(stream);
		final ConcreteSubClass instance = (ConcreteSubClass) reader.readObject();
		assertNotNull(instance);
		assertEquals("subclass.value2", ConcreteSubClass.VALUE, instance.value2);
		assertEquals("subclass.value", ConcreteClass.VALUE, instance.value);

		this.verifyFurtherReadsFail(reader);
	}

	public void testWriteThenReadInstance() {
		final ObjectOutputStream writer = this.createObjectOutputStream(new ConcreteClassObjectWriter());

		final ConcreteClass concreteClass = createConcreteClass();
		writer.writeObject(concreteClass);

		final ObjectInputStream reader = createObjectInputStream(writer.getText());
		final ConcreteClass readFlat = (ConcreteClass) reader.readObject();

		assertNotNull(readFlat);
		assertEquals(ConcreteClass.VALUE, readFlat.value);

		this.verifyFurtherReadsFail(reader);
	}

	public void testWriteTwiceThenReadInstanceTwice() {
		final ObjectOutputStream writer = this.createObjectOutputStream(new ConcreteClassObjectWriter());

		final ConcreteClass concreteClass = createConcreteClass();
		writer.writeObject(concreteClass);
		writer.writeObject(concreteClass);

		final ObjectInputStream reader = this.createObjectInputStream(writer.getText());
		final ConcreteClass readFlat = (ConcreteClass) reader.readObject();

		assertNotNull(readFlat);
		assertEquals(ConcreteClass.VALUE, readFlat.value);

		final ConcreteClass secondReadFlat = (ConcreteClass) reader.readObject();
		assertSame(readFlat, secondReadFlat);

		this.verifyFurtherReadsFail(reader);
	}

	public void testWriteThenReadAllPrimitives() {
		final ObjectOutputStream writer = this.createObjectOutputStream();
		writer.writeBoolean(true);
		writer.writeByte((byte) 1);
		writer.writeShort((short) 2);
		writer.writeInt(3);
		writer.writeLong(4);
		writer.writeFloat(5f);
		writer.writeDouble(6);
		writer.writeChar('a');

		final ObjectInputStream reader = this.createObjectInputStream(writer.getText());
		assertTrue(reader.readBoolean());
		assertEquals(1, reader.readByte());
		assertEquals(2, reader.readShort());
		assertEquals(3, reader.readInt());
		assertEquals(4, reader.readLong());
		assertEquals(5, reader.readFloat(), 0.1);
		assertEquals(6, reader.readDouble(), 0.1);
		assertEquals('a', reader.readChar());

		this.verifyFurtherReadsFail(reader);
	}

	public void testWriteThenReadAllPrimitivesAndAObject() {
		final ObjectOutputStream writer = this.createObjectOutputStream(new ConcreteClassObjectWriter());
		writer.writeBoolean(true);
		writer.writeByte((byte) 1);
		writer.writeShort((short) 2);
		writer.writeInt(3);
		writer.writeLong(4);
		writer.writeFloat(5f);
		writer.writeDouble(6);
		writer.writeChar('a');

		final ConcreteClass instance = createConcreteClass();
		writer.writeObject(instance);

		writer.writeBoolean(false);

		final ObjectInputStream reader = this.createObjectInputStream(writer.getText(), new ConcreteClassObjectReader());
		assertTrue(reader.readBoolean());
		assertEquals(1, reader.readByte());
		assertEquals(2, reader.readShort());
		assertEquals(3, reader.readInt());
		assertEquals(4, reader.readLong());
		assertEquals(5, reader.readFloat(), 0.1);
		assertEquals(6, reader.readDouble(), 0.1);
		assertEquals('a', reader.readChar());

		final ConcreteClass readInstance = (ConcreteClass) reader.readObject();
		assertNotNull(readInstance);
		assertEquals(ConcreteClass.VALUE, readInstance.value);

		assertFalse(reader.readBoolean());

		this.verifyFurtherReadsFail(reader);
	}
}
