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
package rocket.serialization.test.server.writer;

import rocket.serialization.server.writer.BooleanArrayWriter;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class BooleanArrayWriterTestCase extends ServerTestCase {

	final static String BOOLEAN_ARRAY = boolean[].class.getName();

	final static boolean BOOLEAN_VALUE = true;

	public void testWriteBooleanArray() {
		final TestServerObjectOutputStream output = createObjectOutputStream(BooleanArrayWriter.instance);

		final boolean[] array = new boolean[1];
		array[0] = BOOLEAN_VALUE;
		output.writeObject(array);

		final String expectedValues = "1,2,1," + BOOLEAN_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + BOOLEAN_ARRAY + "\"," + expectedValues + "]", text);
	}
}
