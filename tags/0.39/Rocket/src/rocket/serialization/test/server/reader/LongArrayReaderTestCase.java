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

import rocket.serialization.server.reader.LongArrayReader;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.client.ObjectInputStream;

public class LongArrayReaderTestCase extends ServerTestCase {

	final static String LONG_ARRAY = long[].class.getName();

	final static long LONG_VALUE = 34567;

	public void testReadLongArray() {
		final String stream = "[1,\"" + LONG_ARRAY + "\",1,2,1," + LONG_VALUE + "]";
		final ObjectInputStream input = createObjectInputStream(stream, LongArrayReader.instance);

		final long[] array = (long[]) input.readObject();
		assertNotNull(stream, array);
		assertEquals("size", 1, array.length);

		assertEquals(LONG_VALUE, array[0]);

		this.verifyFurtherReadsFail(input);
	}
}
