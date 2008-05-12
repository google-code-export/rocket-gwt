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

import rocket.serialization.client.ClientObjectOutputStream;
import rocket.serialization.client.writer.ByteWriter;
import rocket.serialization.test.client.ClientGwtTestCase;

public class ByteWriterGwtTestCase extends ClientGwtTestCase {

	final static String BYTE = "java.lang.Byte";

	final static byte BYTE_VALUE = 123;

	public void testWriteByte() {
		final ClientObjectOutputStream output = createObjectOutputStream(BYTE, ByteWriter.instance);

		final Byte object = new Byte(BYTE_VALUE);
		output.writeObject(object);

		final String expectedValues = "1,2," + BYTE_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + BYTE + "\"," + expectedValues +"]", text);
	}
}
