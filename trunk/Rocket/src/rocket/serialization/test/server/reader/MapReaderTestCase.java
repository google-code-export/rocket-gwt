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
package rocket.serialization.test.server.reader;

import java.util.HashMap;
import java.util.Map;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.server.reader.MapReader;
import rocket.serialization.test.server.ServerTestCase;

public class MapReaderTestCase extends ServerTestCase {

	final String HASHMAP = HashMap.class.getName();

	public void testReadEmptyMap() {
		final String stream = "[1,\"" + HASHMAP + "\",1,2,0]";
		final ObjectInputStream reader = createObjectInputStream(stream, MapReader.instance);
		final Map map = (Map) reader.readObject();
		assertNotNull(map);
		assertEquals("" + map, 0, map.size());

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadMapWithStringKeyAndNullValue() {
		final String stream = "[2,\"" + HASHMAP + "\",\"" + APPLE + "\",1,2,1,3,0]";
		final ObjectInputStream reader = createObjectInputStream(stream, MapReader.instance);
		final Map map = (Map) reader.readObject();
		assertNotNull(map);
		assertEquals("" + map, 1, map.size());
		assertNull("" + map, map.get(APPLE));

		this.verifyFurtherReadsFail(reader);
	}

	public void testReadMapWithManyEntries() {
		final String stream = "[4,\"" + HASHMAP + "\",\"" + APPLE + "\",\"" + BANANA + "\",\"" + CARROT + "\",1,2,2,3,4,5,0]";
		final ObjectInputStream reader = createObjectInputStream(stream, MapReader.instance);
		final Map map = (Map) reader.readObject();
		assertNotNull(map);
		assertEquals("" + map, 2, map.size());
		assertEquals("" + map, BANANA, map.get(APPLE));
		assertNull("" + map, map.get(CARROT));

		this.verifyFurtherReadsFail(reader);
	}
}
