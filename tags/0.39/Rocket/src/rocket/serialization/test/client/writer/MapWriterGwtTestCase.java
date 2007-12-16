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
import java.util.Map;

import rocket.serialization.client.writer.MapWriter;
import rocket.serialization.test.client.ClientGwtTestCase;
import rocket.serialization.client.ClientObjectOutputStream;

public class MapWriterGwtTestCase extends ClientGwtTestCase {

	final static String HASHMAP = "java.util.HashMap";

	public void testWriteEntryWithNullValue() {
		final Map map = new HashMap();
		map.put(APPLE, null);

		final ClientObjectOutputStream output = createObjectOutputStream(HASHMAP, MapWriter.instance);

		output.writeObject(map);

		final String expectedValues = "1,2,1,3,0";
		final String text = output.getText();
		assertEquals("[2,\"" + HASHMAP + "\",\"" + APPLE + "\"," + expectedValues + "]", text);
	}

	public void testWriteEntry() {
		final Map map = new HashMap();
		map.put(APPLE, BANANA);

		final ClientObjectOutputStream output = createObjectOutputStream(HASHMAP, MapWriter.instance);

		output.writeObject(map);

		final String expectedValues = "1,2,1,3,4";
		final String text = output.getText();
		assertEquals("[3,\"" + HASHMAP + "\",\"" + APPLE + "\",\"" + BANANA + "\"," + expectedValues + "]", text);
	}

	public void testWriteSeveralEntries() {
		final Map map = new HashMap();
		map.put(APPLE, BANANA);
		map.put(CARROT, null);

		final ClientObjectOutputStream output = createObjectOutputStream(HASHMAP, MapWriter.instance);

		output.writeObject(map);

		final String expectedValues = "1,2,2,3,0,4,5"; // Relies on ordering of
		final String text = output.getText();
		assertEquals("[4,\"" + HASHMAP + "\",\"" + CARROT + "\",\"" + APPLE + "\",\"" + BANANA + "\"," + expectedValues + "]", text);
	}
}
