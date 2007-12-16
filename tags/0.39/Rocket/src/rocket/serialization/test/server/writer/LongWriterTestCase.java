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

import java.util.Map;

import rocket.serialization.server.writer.LongWriter;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class LongWriterTestCase extends ServerTestCase {

	final static String LONG = Long.class.getName();

	final static long LONG_VALUE = 1234567;

	public void testWriteLong() {
		final TestServerObjectOutputStream output = createObjectOutputStream(LongWriter.instance);

		final Long object = new Long(LONG_VALUE);
		output.writeObject(object);

		final String expectedValues = "1,2," + LONG_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + LONG + "\"," + expectedValues + "]", text);
	}
}
