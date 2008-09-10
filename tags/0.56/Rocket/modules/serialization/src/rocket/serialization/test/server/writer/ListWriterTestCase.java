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

import rocket.serialization.server.writer.ListWriter;
import rocket.serialization.test.server.ConcreteClass;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class ListWriterTestCase extends ServerTestCase {

	final static String ARRAYLIST = ArrayList.class.getName();

	final static String LIST = List.class.getName();

	public void testWriteNullElement() {
		final List<Object> list = new ArrayList<Object>();
		list.add(null);

		final TestServerObjectOutputStream output = createObjectOutputStream(ListWriter.instance);

		output.writeObject(list);

		final String expectedValues = "1,2,1,0";
		final String text = output.getText();
		assertEquals("[1,\"" + ARRAYLIST + "\"," + expectedValues + "]", text);
	}

	public void testWriteSeveralNullElements() {
		final List<Object> list = new ArrayList<Object>();
		list.add(null);
		list.add(null);
		list.add(null);
		list.add(null);
		list.add(null);

		final TestServerObjectOutputStream output = createObjectOutputStream(ListWriter.instance);

		output.writeObject(list);

		final String expectedValues = "1,2,5,0,0,0,0,0";
		
		final String text = output.getText();
		assertEquals("[1,\"" + ARRAYLIST + "\"," + expectedValues + "]", text);
	}

	public void testSingleObjectElement() {
		final List<ConcreteClass> list = new ArrayList<ConcreteClass>();

		final ConcreteClass concreteClass = createConcreteClass();
		list.add(concreteClass);

		final TestServerObjectOutputStream output = createObjectOutputStream(ListWriter.instance);

		output.writeObject(list);


		final String expectedValues = "1,2,1,1,3," + ConcreteClass.VALUE;
		final String text = output.getText();
		assertEquals("[2,\"" + ARRAYLIST + "\",\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

	public void testSeveralObjectsAndNullElements() {
		final List<ConcreteClass> list = new ArrayList<ConcreteClass>();

		final ConcreteClass concreteClass = new ConcreteClass();
		concreteClass.value = ConcreteClass.VALUE;

		list.add(concreteClass);
		list.add(concreteClass);
		list.add(null);

		final TestServerObjectOutputStream output = createObjectOutputStream(ListWriter.instance);
		output.writeObject(list);

		final String expectedValues = "1,2,3,1,3," + ConcreteClass.VALUE + ",-2,0";
		final String text = output.getText();
		assertEquals("[2,\"" + ARRAYLIST + "\",\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}
}
