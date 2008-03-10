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
import rocket.serialization.client.writer.ShortArrayWriter;
import rocket.serialization.test.client.ClientGwtTestCase;

public class ShortArrayWriterGwtTestCase extends ClientGwtTestCase {

	final static String SHORT_ARRAY = "[S";

	final static short SHORT_VALUE = 34;

	public void testWriteShort() {
		final ClientObjectOutputStream output = createObjectOutputStream(SHORT_ARRAY, ShortArrayWriter.instance);

		final short[] array = new short[1];
		array[0] = SHORT_VALUE;
		output.writeObject(array);

		final String expectedValues = "1,2,1," + SHORT_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + SHORT_ARRAY + "\"," + expectedValues+"]", text);
	}
}
