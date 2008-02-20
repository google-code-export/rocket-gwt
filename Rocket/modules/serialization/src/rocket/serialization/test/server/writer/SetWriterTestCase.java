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

import java.util.HashSet;
import java.util.Set;

import rocket.serialization.server.writer.SetWriter;
import rocket.serialization.test.server.ConcreteClass;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class SetWriterTestCase extends ServerTestCase {

	final static String HASHSET = HashSet.class.getName();

	final static String SET = Set.class.getName();

	public void testWriteNullElement() {
		final Set set = new HashSet();
		set.add(null);

		final TestServerObjectOutputStream output = this.createObjectOutputStream(SetWriter.instance);
		output.writeObject(set);

		//final String values = output.getValuesText();
		final String expectedValues = "1,2,1,0";
//
//		assertEquals("values", expectedValues, values);
//
//		final Map strings = output.getStringTable();
//		assertEquals("" + strings, 1, strings.size());
//		assertEquals("" + strings, 2, strings.get(HASHSET));
//
//		final Map objects = swapKeyWithValues(output.getObjectTable());
//		assertEquals("processed objects: " + objects, 1, objects.size());
//
//		final Object first = objects.get("-1");
//		assertTrue("" + objects, first instanceof HashSet);

		final String text = output.getText();
		assertEquals("[1,\"" + HASHSET + "\"," + expectedValues + "]", text);
	}

	public void testSingleObjectElement() {
		final Set set = new HashSet();
		final ConcreteClass concreteClass = createConcreteClass();
		set.add(concreteClass);

		final TestServerObjectOutputStream output = this.createObjectOutputStream(SetWriter.instance);

		output.writeObject(set);

//		final String values = output.getValuesText();
		final String expectedValues = "1,2,1,1,3," + ConcreteClass.VALUE;

	//	assertEquals("values", expectedValues, values);

//		final Map strings = output.getStringTable();
//		assertEquals("" + strings, 2, strings.size());
//		assertEquals("" + strings, 2, strings.get(HASHSET));
//		assertEquals("" + strings, 3, strings.get(CONCRETE_CLASS));
//
//		final Map objects = swapKeyWithValues(output.getObjectTable());
//		assertEquals("processed objects: " + objects, 2, objects.size());
//
//		final Object first = objects.get("-1");
//		assertTrue("" + objects, first instanceof HashSet);
//		final Object second = objects.get("-2");
//		assertTrue("" + objects, second instanceof ConcreteClass);

		final String text = output.getText();
		assertEquals("[2,\"" + HASHSET + "\",\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

	public void testSeveralObjectsAndNullElements() {
		final Set set = new HashSet();
		set.add(createConcreteClass());
		set.add(createConcreteClass());

		final TestServerObjectOutputStream output = this.createObjectOutputStream(SetWriter.instance);

		output.writeObject(set);

//		final String values = output.getValuesText();
		final String expectedValues = "1,2,2,1,3," + ConcreteClass.VALUE + ",1,3," + ConcreteClass.VALUE;
//
//		assertEquals("values", expectedValues, values);
//
//		final Map strings = output.getStringTable();
//		assertEquals("" + strings, 2, strings.size());
//		assertEquals("" + strings, 2, strings.get(HASHSET));
//		assertEquals("" + strings, 3, strings.get(CONCRETE_CLASS));
//
//		final Map objects = swapKeyWithValues(output.getObjectTable());
//		assertEquals("processed objects: " + objects, 3, objects.size());
//
//		final Object first = objects.get("-1");
//		assertTrue("" + objects, first instanceof HashSet);
//		final Object second = objects.get("-2");
//		assertTrue("" + objects, second instanceof ConcreteClass);
//		final Object third = objects.get("-3");
//		assertTrue("" + objects, third instanceof ConcreteClass);

		final String text = output.getText();
		assertEquals("[2,\"" + HASHSET + "\",\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

}
