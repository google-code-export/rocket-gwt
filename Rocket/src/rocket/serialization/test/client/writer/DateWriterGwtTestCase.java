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

import java.util.Date;

import rocket.serialization.client.ClientObjectOutputStream;
import rocket.serialization.client.writer.DateWriter;
import rocket.serialization.test.client.ClientGwtTestCase;

public class DateWriterGwtTestCase extends ClientGwtTestCase {

	final static String DATE = "java.util.Date";

	final static long DATE_VALUE = 123456;

	public void testWriteDate() {
		final ClientObjectOutputStream output = createObjectOutputStream(DATE, DateWriter.instance);

		final Date date = new Date(DATE_VALUE);
		output.writeObject(date);

		final String expectedValues = "1,2," + DATE_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + DATE + "\"," + expectedValues + "]", text);
	}
}
