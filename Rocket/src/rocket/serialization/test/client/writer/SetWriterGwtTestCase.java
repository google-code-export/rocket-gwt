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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rocket.serialization.client.writer.SetWriter;
import rocket.serialization.test.client.ClientGwtTestCase;
import rocket.serialization.test.client.ConcreteClass;
import rocket.serialization.test.client.ConcreteClassObjectWriter;
import rocket.serialization.client.ClientObjectOutputStream;

public class SetWriterGwtTestCase extends ClientGwtTestCase {

	final static String HASHSET = "java.util.HashSet";

	final static String SET = "java.util.Set";

	public void testWriteNullElement() {
		final Set set = new HashSet();
		set.add(null);

		final ClientObjectOutputStream output = this.createObjectOutputStream(HASHSET, SetWriter.instance);
		output.writeObject(set);

		final String expectedValues = "1,2,1,0";
		final String text = output.getText();
		assertEquals("[1,\"" + HASHSET + "\"," + expectedValues + "]", text);
	}

	public void testSingleObjectElement() {
		final Set set = new HashSet();
		final ConcreteClass concreteClass = createConcreteClass();
		set.add(concreteClass);

		final Map writers = new HashMap();
		writers.put(HASHSET, SetWriter.instance);
		writers.put(CONCRETE_CLASS, new ConcreteClassObjectWriter());

		final ClientObjectOutputStream output = this.createObjectOutputStream(writers);

		output.writeObject(set);

		final String expectedValues = "1,2,1,1,3," + ConcreteClass.VALUE;
		final String text = output.getText();
		assertEquals("[2,\"" + HASHSET + "\",\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

	public void testSeveralObjectsAndNullElements() {
		final Set set = new HashSet();
		set.add(createConcreteClass());
		set.add(createConcreteClass());

		final Map writers = new HashMap();
		writers.put(HASHSET, SetWriter.instance);
		writers.put(CONCRETE_CLASS, new ConcreteClassObjectWriter());

		final ClientObjectOutputStream output = this.createObjectOutputStream(writers);

		output.writeObject(set);

		final String expectedValues = "1,2,2,1,3," + ConcreteClass.VALUE + ",1,3," + ConcreteClass.VALUE;

		final String text = output.getText();
		assertEquals("[2,\"" + HASHSET + "\",\"" + CONCRETE_CLASS + "\"," + expectedValues + "]", text);
	}

}
