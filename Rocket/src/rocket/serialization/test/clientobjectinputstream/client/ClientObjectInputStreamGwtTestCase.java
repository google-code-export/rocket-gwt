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
package rocket.serialization.test.clientobjectinputstream.client;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectReader;
import rocket.serialization.client.ObjectWriter;
import rocket.serialization.client.SerializationFactory;

import com.google.gwt.junit.client.GWTTestCase;

public class ClientObjectInputStreamGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.serialization.test.clientobjectinputstream.ClientObjectInputStream";
	}

	final String APPLE = "apple";

	final String BANANA = "banana";

	final String CARROT = "carrot";

	final static String CONCRETE_CLASS = "rocket.serialization.test.clientobjectinputstream.client.ConcreteClass";

	final static String HASHSET = "java.util.Hashset";

	final static String SET = "java.util.Set";

	final static String CONCRETE_SUBCLASS = "rocket.serialization.test.clientobjectinputstream.client.ConcreteSubClass";

	final static String HASHMAP = "java.util.HashMap";

	final static String STRING = "java.lang.String";

	public void testReadBoolean() {
		final String stream = "[0,true,false]";
		final ObjectInputStream reader = createSerializationFactory().createObjectInputStream(stream);
		assertTrue(reader.readBoolean());
		assertFalse(reader.readBoolean());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadByte() {
		final String stream = "[0,1,2,-3]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, 1, reader.readByte());
		assertEquals(stream, 2, reader.readByte());
		assertEquals(stream, -3, reader.readByte());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadShort() {
		final String stream = "[0,1,2,-3]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, 1, reader.readShort());
		assertEquals(stream, 2, reader.readShort());
		assertEquals(stream, -3, reader.readShort());

		this.checkFurtherReadsFail(reader);		
	}

	public void testReadInt() {
		final String stream = "[0,1,2,-3]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, 1, reader.readInt());
		assertEquals(stream, 2, reader.readInt());
		assertEquals(stream, -3, reader.readInt());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadLong() {
		final String stream = "[0,1,2,-3]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, 1, reader.readLong());
		assertEquals(stream, 2, reader.readLong());
		assertEquals(stream, -3, reader.readLong());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadFloat() {
		final String stream = "[0,1.25,2.5,-3.75]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, 1.25, reader.readFloat(), 0.1);
		assertEquals(stream, 2.5, reader.readFloat(), 0.1);
		assertEquals(stream, -3.75, reader.readFloat(), 0.1);

		this.checkFurtherReadsFail(reader);
	}

	public void testReadDouble() {
		final String stream = "[0,1.25,2.5,-3.75]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, 1.25, reader.readDouble(), 0.1f);
		assertEquals(stream, 2.5, reader.readDouble(), 0.1f);
		assertEquals(stream, -3.75, reader.readDouble(), 0.1f);

		this.checkFurtherReadsFail(reader);
	}

	public void testReadChar() {
		final String stream = "[0," + (int) 'a' + "]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, 'a', reader.readChar());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadNullString() {
		final String stream = "[0,0]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, null, reader.readObject());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadAString() {
		final String stream = "[1,\"apple\",2]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, "apple", reader.readObject());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadTheSameStringTwice() {
		final String stream = "[1,\"apple\",2,2]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, "apple", reader.readObject());
		assertEquals(stream, "apple", reader.readObject());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadTwoDifferentStrings() {
		final String stream = "[2,\"apple\",\"banana\",2,3]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		assertEquals(stream, "apple", reader.readObject());
		assertEquals(stream, "banana", reader.readObject());

		this.checkFurtherReadsFail(reader);
	}

	public void testReadInstance() {
		final String stream = "[1,\"" + CONCRETE_CLASS + "\",1,2," + ConcreteClass.VALUE + "]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		final ConcreteClass concreteClass = (ConcreteClass) reader.readObject();
		assertNotNull(concreteClass);
		assertEquals("concrete.value", ConcreteClass.VALUE, concreteClass.value);

		this.checkFurtherReadsFail(reader);
	}

	public void testReadTheSameInstanceTwice() {
		final String stream = "[1,\"" + CONCRETE_CLASS + "\",1,2," + ConcreteClass.VALUE + ",-1]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		final ConcreteClass concreteClass = (ConcreteClass) reader.readObject();
		assertNotNull(concreteClass);
		assertEquals("concrete.value", ConcreteClass.VALUE, concreteClass.value);

		final ConcreteClass secondConcreteClass = (ConcreteClass) reader.readObject();
		assertSame(concreteClass, secondConcreteClass);

		this.checkFurtherReadsFail(reader);
	}

	public void testReadTwoDifferentInstances() {
		final String stream = "[1,\"" + CONCRETE_CLASS + "\",1,2," + ConcreteClass.VALUE + ",1,2," + ConcreteClass.VALUE + "]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		final ConcreteClass concreteClass = (ConcreteClass) reader.readObject();
		assertNotNull(concreteClass);
		assertEquals("concrete.value", ConcreteClass.VALUE, concreteClass.value);

		final ConcreteClass secondConcreteClass = (ConcreteClass) reader.readObject();
		assertNotSame(concreteClass, secondConcreteClass);
		assertNotNull(concreteClass);
		assertEquals("secondConcreteClass.value", ConcreteClass.VALUE, secondConcreteClass.value);

		this.checkFurtherReadsFail(reader);
	}

	public void testReadSubClass() {
		final String stream = "[1,\"" + CONCRETE_SUBCLASS + "\",1,2," + ConcreteSubClass.VALUE + "," + ConcreteClass.VALUE + "]";
		final ObjectInputStream reader = this.createSerializationFactory().createObjectInputStream(stream);
		final ConcreteSubClass instance = (ConcreteSubClass) reader.readObject();
		assertNotNull(instance);
		assertEquals("subclass.value2", ConcreteSubClass.VALUE, instance.value2);
		assertEquals("subclass.value", ConcreteClass.VALUE, instance.value);

		this.checkFurtherReadsFail(reader);
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

	protected SerializationFactory createSerializationFactory() {
		return new SerializationFactory() {

			public ObjectOutputStream createObjectOutputStream() {
				throw new UnsupportedOperationException();
			}

			public ObjectReader getObjectReader( final String typeName ){
				ObjectReader objectReader = null;
				
				while( true ){
					if( CONCRETE_CLASS.equals( typeName )){
						objectReader = new ConcreteClassObjectReader();
						break;
					}
					if( CONCRETE_SUBCLASS.equals( typeName )){
						objectReader = new ConcreteSubClassObjectReader();
						break;
					}
				}
				
				return objectReader;
			}

			public ObjectWriter getObjectWriter( final String typeName ){
				return null;
			}
		};
	}
}
