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
import rocket.serialization.client.writer.LongArrayWriter;
import rocket.serialization.test.client.ClientGwtTestCase;

public class LongArrayWriterGwtTestCase extends ClientGwtTestCase {

	final static String LONG_ARRAY = "[J";

	final static long LONG_VALUE = 34567;

	public void testWriteLong() {
		final ClientObjectOutputStream output = createObjectOutputStream(LONG_ARRAY, LongArrayWriter.instance);

		final long[] array = new long[1];
		array[0] = LONG_VALUE;
		output.writeObject(array);

		final String expectedValues = "1,2,1,0," + LONG_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + LONG_ARRAY + "\"," + expectedValues+"]", text);
	}
}
