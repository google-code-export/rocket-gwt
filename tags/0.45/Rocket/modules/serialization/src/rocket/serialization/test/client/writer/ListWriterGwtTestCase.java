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
package rocket.serialization.test.client.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rocket.serialization.client.ClientObjectOutputStream;
import rocket.serialization.client.writer.ListWriter;
import rocket.serialization.test.client.ClientGwtTestCase;
import rocket.serialization.test.client.ConcreteClass;
import rocket.serialization.test.client.ConcreteClassObjectWriter;

public class ListWriterGwtTestCase extends ClientGwtTestCase {

	final static String ARRAYLIST = "java.util.ArrayList";

	final static String LIST = "java.util.List";

	public void testWriteNullElement() {
		final List list = new ArrayList();
		list.add(null);

		final ClientObjectOutputStream output = createObjectOutputStream(ARRAYLIST, ListWriter.instance);

		output.writeObject(list);

		final String expectedValues = "1,2,1,0";
		final String text = output.getText();
		assertEquals("[1,\"" + ARRAYLIST + "\"," + expectedValues + "]", text);
	}

	public void testWriteSeveralNullElements() {
		final List list = new ArrayList();
		list.add(null);
		list.add(null);
		list.add(null);
		list.add(null);
		list.add(null);

		final ClientObjectOutputStream output = createObjectOutputStream(ARRAYLIST, ListWriter.instance);

		output.writeObject(list);

		final String expectedValues = "1,2,5,0,0,0,0,0";
		final String text = output.getText();
		assertEquals("[1,\"" + ARRAYLIST + "\"," + expectedValues+"]", text);
	}

	public void testSingleObjectElement() {
		final List list = new ArrayList();

		final ConcreteClass concreteClass = createConcreteClass();
		list.add(concreteClass);

		final Map writers = new HashMap();
		writers.put(ARRAYLIST, ListWriter.instance);
		writers.put(CONCRETE_CLASS, new ConcreteClassObjectWriter());

		final ClientObjectOutputStream output = createObjectOutputStream(writers);

		output.writeObject(list);

		final String expectedValues = "1,2,1,1,3," + ConcreteClass.VALUE;
		final String text = output.getText();
		assertEquals("[2,\"" + ARRAYLIST + "\",\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

	public void testSeveralObjectsAndNullElements() {
		final List list = new ArrayList();

		final ConcreteClass concreteClass = new ConcreteClass();
		concreteClass.value = ConcreteClass.VALUE;

		list.add(concreteClass);
		list.add(concreteClass);
		list.add(null);

		final Map writers = new HashMap();
		writers.put(ARRAYLIST, ListWriter.instance);
		writers.put(CONCRETE_CLASS, new ConcreteClassObjectWriter());

		final ClientObjectOutputStream output = createObjectOutputStream(writers);
		output.writeObject(list);

		final String expectedValues = "1,2,3,1,3," + ConcreteClass.VALUE + ",-2,0";
		final String text = output.getText();
		assertEquals("[2,\"" + ARRAYLIST + "\",\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}
}
