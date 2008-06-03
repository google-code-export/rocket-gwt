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
import rocket.serialization.client.writer.IntArrayWriter;
import rocket.serialization.test.client.ClientGwtTestCase;

public class IntArrayWriterGwtTestCase extends ClientGwtTestCase {

	final static String INT_ARRAY = "[I";

	final static int INT_VALUE = 34;

	public void testWriteInt() {
		final ClientObjectOutputStream output = createObjectOutputStream(INT_ARRAY, IntArrayWriter.instance);

		final int[] array = new int[1];
		array[0] = INT_VALUE;
		output.writeObject(array);

		final String expectedValues = "1,2,1," + INT_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + INT_ARRAY + "\"," + expectedValues + "]", text);
	}
}
