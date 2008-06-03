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
import rocket.serialization.client.writer.IntegerWriter;
import rocket.serialization.test.client.ClientGwtTestCase;

public class IntegerWriterGwtTestCase extends ClientGwtTestCase {

	final static String INTEGER = "java.lang.Integer";

	final static int INTEGER_VALUE = 12345;

	public void testWriteInteger() {
		final ClientObjectOutputStream output = createObjectOutputStream(INTEGER, IntegerWriter.instance);

		final Integer object = new Integer(INTEGER_VALUE);
		output.writeObject(object);

		final String expectedValues = "1,2," + INTEGER_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + INTEGER + "\"," + expectedValues + "]", text);
	}
}
