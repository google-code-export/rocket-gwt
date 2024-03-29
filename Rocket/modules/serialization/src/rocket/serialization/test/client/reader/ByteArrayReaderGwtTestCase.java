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
import rocket.serialization.client.reader.ByteArrayReader;
import rocket.serialization.test.client.ClientGwtTestCase;

public class ByteArrayReaderGwtTestCase extends ClientGwtTestCase {

	final static String BYTE_ARRAY = "[B";

	final static byte BYTE_VALUE = 34;

	public void testReadByte() {
		final String stream = "[1,\"" + BYTE_ARRAY + "\",1,2,1," + BYTE_VALUE + "]";
		final ClientObjectInputStream input = createObjectInputStream(stream, BYTE_ARRAY, ByteArrayReader.instance);

		final byte[] array = (byte[]) input.readObject();
		assertNotNull(stream, array);
		assertEquals("size", 1, array.length);

		assertEquals(BYTE_VALUE, array[0]);

		this.verifyFurtherReadsFail(input);
	}
}
