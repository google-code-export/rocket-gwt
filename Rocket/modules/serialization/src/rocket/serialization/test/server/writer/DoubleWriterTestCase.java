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

import rocket.serialization.server.writer.DoubleWriter;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class DoubleWriterTestCase extends ServerTestCase {

	final static String DOUBLE = Double.class.getName();

	final static double DOUBLE_VALUE = 1234.5;

	public void testWriteDouble() {
		final TestServerObjectOutputStream output = createObjectOutputStream(DoubleWriter.instance);

		final Double object = new Double(DOUBLE_VALUE);
		output.writeObject(object);

		final String expectedValues = "1,2," + DOUBLE_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + DOUBLE + "\"," + expectedValues + "]", text);
	}
}
