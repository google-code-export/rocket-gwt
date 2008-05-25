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
package rocket.serialization.test.client.reader;

import rocket.serialization.client.ClientObjectInputStream;
import rocket.serialization.client.reader.IntArrayReader;
import rocket.serialization.test.client.ClientGwtTestCase;

public class IntArrayReaderGwtTestCase extends ClientGwtTestCase {

	final static String INTEGER_ARRAY = "[I";

	final static int INTEGER_VALUE = 345;

	public void testReadInt() {
		final String stream = "[1,\"" + INTEGER_ARRAY + "\",1,2,1," + INTEGER_VALUE + "]";
		final ClientObjectInputStream input = createObjectInputStream(stream, INTEGER_ARRAY, IntArrayReader.instance);

		final int[] array = (int[]) input.readObject();
		assertNotNull(stream, array);
		assertEquals("size", 1, array.length);

		assertEquals(INTEGER_VALUE, array[0]);

		this.verifyFurtherReadsFail(input);
	}
}
