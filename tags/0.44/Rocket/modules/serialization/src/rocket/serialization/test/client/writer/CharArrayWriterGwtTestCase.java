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
import rocket.serialization.client.writer.CharArrayWriter;
import rocket.serialization.test.client.ClientGwtTestCase;

public class CharArrayWriterGwtTestCase extends ClientGwtTestCase {

	final static String CHAR_ARRAY = "[C";

	final static char CHAR_VALUE = 'z';

	public void testWriteChar() {
		final ClientObjectOutputStream output = createObjectOutputStream(CHAR_ARRAY, CharArrayWriter.instance);

		final char[] array = new char[1];
		array[0] = CHAR_VALUE;
		output.writeObject(array);

		final String expectedValues = "1,2,1," + (int) CHAR_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + CHAR_ARRAY + "\"," + expectedValues+"]", text);
	}
}
