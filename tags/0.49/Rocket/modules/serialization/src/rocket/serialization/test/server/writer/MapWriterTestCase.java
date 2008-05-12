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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import rocket.serialization.server.writer.MapWriter;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class MapWriterTestCase extends ServerTestCase {

	final static String HASHMAP = HashMap.class.getName();

	public void testWriteEntryWithNullValue() {
		final Map map = new HashMap();
		map.put(APPLE, null);

		final TestServerObjectOutputStream output = createObjectOutputStream(MapWriter.instance);

		output.writeObject(map);

		final String expectedValues = "1,2,1,3,0";
		final String text = output.getText();
		assertEquals("[2,\"" + HASHMAP + "\",\"" + APPLE + "\"," + expectedValues + "]", text);
	}

	public void testWriteEntry() {
		final Map map = new HashMap();
		map.put(APPLE, BANANA);

		final TestServerObjectOutputStream output = createObjectOutputStream(MapWriter.instance);

		output.writeObject(map);

		final String expectedValues = "1,2,1,3,4";
		final String text = output.getText();
		assertEquals("[3,\"" + HASHMAP + "\",\"" + APPLE + "\",\"" + BANANA + "\"," + expectedValues + "]", text);
	}

	public void testWriteSeveralEntries() {
		final Map map = new TreeMap();
		map.put(APPLE, BANANA);
		map.put(CARROT, null);

		final TestServerObjectOutputStream output = createObjectOutputStream(MapWriter.instance);

		output.writeObject(map);

		final String expectedValues = "1,2,2,3,4,5,0";
		final String text = output.getText();
		assertEquals("[4,\"" + HASHMAP + "\",\"" + APPLE + "\",\"" + BANANA + "\",\"" + CARROT + "\"," + expectedValues + "]", text);
	}
}
