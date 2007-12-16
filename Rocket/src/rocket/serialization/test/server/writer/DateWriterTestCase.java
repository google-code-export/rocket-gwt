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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import rocket.serialization.server.writer.DateWriter;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class DateWriterTestCase extends ServerTestCase {

	final static String DATE = Date.class.getName();

	final static long DATE_VALUE = 123456;

	final static String TIMESTAMP = Timestamp.class.getName();

	public void testWriteDate() {
		final TestServerObjectOutputStream output = createObjectOutputStream(DateWriter.instance);

		final Date date = new Date(DATE_VALUE);
		output.writeObject(date);

		final String expectedValues = "1,2," + DATE_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + DATE + "\"," + expectedValues + "]", text);
	}

	public void testWriteTimestamp() {
		final TestServerObjectOutputStream output = createObjectOutputStream(DateWriter.instance);

		final Date date = new Timestamp(DATE_VALUE);
		output.writeObject(date);

		final String expectedValues = "1,2," + DATE_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + DATE + "\"," + expectedValues + "]", text);
	}
}
