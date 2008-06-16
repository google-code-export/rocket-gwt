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
package rocket.serialization.test.server.reader;

import java.util.Date;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.server.reader.DateReader;
import rocket.serialization.test.server.ServerTestCase;

public class DateReaderTestCase extends ServerTestCase {

	final static String DATE = Date.class.getName();

	final static long DATE_VALUE = 789;

	public void testReadDate() {
		// final String stream = "[1,\"" + DATE + "\",1,2," + DATE_VALUE + "]";
		final String stream = "[1,\"" + DATE + "\",1,2,0," + DATE_VALUE + "]";
		final ObjectInputStream input = createObjectInputStream(stream, DateReader.instance);

		final Date date = (Date) input.readObject();
		assertNotNull(stream, date);

		assertEquals(DATE_VALUE, date.getTime());

		this.verifyFurtherReadsFail(input);
	}
}
