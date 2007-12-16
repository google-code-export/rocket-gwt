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

import rocket.serialization.server.reader.BooleanArrayReader;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.client.ObjectInputStream;

public class BooleanArrayReaderTestCase extends ServerTestCase {

	final static String BOOLEAN_ARRAY = boolean[].class.getName();

	final static boolean BOOLEAN_VALUE = true;

	public void testReadBooleanArray() {
		final String stream = "[1,\"" + BOOLEAN_ARRAY + "\",1,2,1," + BOOLEAN_VALUE +"]";
		final ObjectInputStream input = createObjectInputStream(stream, BooleanArrayReader.instance);

		final boolean[] array = (boolean[]) input.readObject();
		assertNotNull(stream, array);
		assertEquals("size", 1, array.length);

		assertEquals(BOOLEAN_VALUE, array[0]);

		this.verifyFurtherReadsFail(input);
	}
}
