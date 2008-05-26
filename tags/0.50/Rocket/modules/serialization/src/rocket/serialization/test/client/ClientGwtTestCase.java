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
package rocket.serialization.test.client;

import java.util.HashMap;
import java.util.Map;

import rocket.serialization.client.ClientObjectInputStream;
import rocket.serialization.client.ClientObjectOutputStream;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectReader;
import rocket.serialization.client.ObjectWriter;
import rocket.util.client.Checker;

import com.google.gwt.junit.client.GWTTestCase;

abstract public class ClientGwtTestCase extends GWTTestCase {

	public final String APPLE = "apple";

	public final String BANANA = "banana";

	public final String CARROT = "carrot";

	public final static String CONCRETE_CLASS = "rocket.serialization.test.client.ConcreteClass";

	public final static String HASHSET = "java.util.HashSet";

	public final static String SET = "java.util.Set";

	public final static String CONCRETE_SUBCLASS = "rocket.serialization.test.client.ConcreteSubClass";

	public final static String HASHMAP = "java.util.HashMap";

	public final static String STRING = "java.lang.String";

	public String getModuleName() {
		return "rocket.serialization.test.ClientGwtTestCase";
	}

	protected ConcreteClass createConcreteClass() {
		final ConcreteClass concreteClass = new ConcreteClass();
		concreteClass.value = ConcreteClass.VALUE;
		return concreteClass;
	}

	protected ConcreteSubClass createSubclass() {
		final ConcreteSubClass subclass = new ConcreteSubClass();
		subclass.value = ConcreteClass.VALUE;
		subclass.value2 = ConcreteSubClass.VALUE;
		return subclass;
	}
	
	protected ClientObjectOutputStream createObjectOutputStream(final String typeName, final ObjectWriter writer) {
		final Map writers = new HashMap();
		writers.put(typeName, writer);
		return this.createObjectOutputStream(writers);
	}

	protected ClientObjectOutputStream createObjectOutputStream(final Map writers) {
		return new ClientObjectOutputStream(){
			protected ObjectWriter getObjectWriter( final String typeName ){
				return (ObjectWriter)writers.get( typeName );
			}
		};
	}

	protected void verifyFurtherReadsFail(final ObjectInputStream inputStream) {
		Checker.notNull("parameter:inputStream", inputStream );
		try {
			final int got = inputStream.readInt();
			fail("An exception should have been thrown when attempting to read a consumed reader, but \"" + got + "\" was returned...");
		} catch (final Exception expected) {

		}
	}

	protected ClientObjectInputStream createObjectInputStream(final String stream, final String typeName, final ObjectReader reader) {
		final Map readers = new HashMap();
		readers.put(typeName, reader);
		return this.createObjectInputStream(stream, readers);
	}

	protected ClientObjectInputStream createObjectInputStream(final String stream, final Map readers) {
		final ClientObjectInputStream objectInputStream = new ClientObjectInputStream(){
			public ObjectReader getObjectReader( final String typeName ){
				return (ObjectReader)readers.get( typeName );
			}
		};
		objectInputStream.prepare(stream);
		return objectInputStream;
	}
}
