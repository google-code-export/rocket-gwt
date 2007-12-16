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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.server.ServerObjectInputStream;
import rocket.serialization.server.ServerObjectOutputStream;
import rocket.serialization.server.ServerObjectReader;
import rocket.serialization.server.ServerObjectWriter;
import rocket.serialization.server.reader.ReflectiveReader;
import rocket.serialization.server.writer.ReflectiveWriter;

abstract public class ServerTestCase extends TestCase {

	public final static String APPLE = "apple";

	public final static String BANANA = "banana";

	public final static String CARROT = "carrot";

	public final static String CONCRETE_CLASS = ConcreteClass.class.getName();

	public final static String HASHSET = HashSet.class.getName();

	public final static String SET = Set.class.getName();

	public final static String CONCRETE_SUBCLASS = ConcreteSubClass.class.getName();

	public final static String HASHMAP = HashMap.class.getName();

	public final static String STRING = String.class.getName();
	
//	static public void assertEquals( final String message, final int integer, final Object objectInteger ){
//		int otherInt = 0;
//		if( objectInteger instanceof Integer ){
//			otherInt = ((Integer)objectInteger).intValue();
//		}
//		
//		assertEquals( message, integer, otherInt );
//	}

	protected ConcreteClass createConcreteClass() {
		final ConcreteClass concreteClass = new ConcreteClass();
		concreteClass.value = ConcreteClass.VALUE;
		return concreteClass;
	}

	protected ConcreteSubClass createConcreteSubClass() {
		final ConcreteSubClass subclass = new ConcreteSubClass();
		subclass.value = ConcreteClass.VALUE;
		subclass.value2 = ConcreteSubClass.VALUE;
		return subclass;
	}

//	protected Map swapKeyWithValues(final Map map) {
//		final Map otherMap = new HashMap();
//		final Iterator entries = map.entrySet().iterator();
//		while (entries.hasNext()) {
//			final Map.Entry entry = (Map.Entry) entries.next();
//			final Object value = entry.getKey();
//			final Integer key = (Integer)entry.getValue();			
//			otherMap.put( "" + key.intValue(), value );
//		}
//
//		return otherMap;
//	}

	protected void verifyFurtherReadsFail(final ObjectInputStream reader) {
		try {
			final int got = reader.readInt();
			fail("An exception should have been thrown when attempting to read a consumed reader, but \"" + got + "\" was returned...");
		} catch (final Exception expected) {

		}
	}

	protected TestServerObjectOutputStream createObjectOutputStream() {
		return this.createObjectOutputStream(Collections.EMPTY_LIST);
	}

	protected TestServerObjectOutputStream createObjectOutputStream(final ServerObjectWriter writer) {
		return this.createObjectOutputStream(Collections.nCopies(1, writer));
	}

	protected TestServerObjectOutputStream createObjectOutputStream(final List writers) {
		final List allWriters = new ArrayList();
		allWriters.addAll( writers );
		allWriters.add( ReflectiveWriter.instance );
		
		return new TestServerObjectOutputStream() {
			protected List createObjectWriters() {
				return allWriters;
			}
		};
	}

	protected ObjectInputStream createObjectInputStream(final String stream) {
		return this.createObjectInputStream(stream, Collections.EMPTY_LIST);
	}

	protected ObjectInputStream createObjectInputStream(final String stream, final ServerObjectReader reader) {
		return this.createObjectInputStream(stream, Collections.nCopies(1, reader));
	}

	protected ObjectInputStream createObjectInputStream(final String stream, final List readers) {
		final List allReaders = new ArrayList();
		allReaders.addAll( readers );
		allReaders.add( ReflectiveReader.instance );
		return new ServerObjectInputStream(stream) {
			
			{
				this.setObjectReaders( this.createObjectReaders() );
			}
			
			protected List createObjectReaders() {
				return allReaders;
			}
		};
	}
}
