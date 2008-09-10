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
package rocket.serialization.test.client.reader;

import rocket.serialization.client.ClientObjectInputStream;
import rocket.serialization.client.reader.LongReader;
import rocket.serialization.test.client.ClientGwtTestCase;

public class LongReaderGwtTestCase extends ClientGwtTestCase {

	final static String LONG = "java.lang.Long";

	final static long LONG_VALUE = 12345;

	public void testReadLong() {
		final String stream = "[1,\"" + LONG + "\",1,2,0," + LONG_VALUE + "]";
		final ClientObjectInputStream input = createObjectInputStream(stream, LONG, LongReader.instance);

		final Long object = (Long) input.readObject();
		assertNotNull(stream, object);

		assertEquals(LONG_VALUE, object.longValue());

		this.verifyFurtherReadsFail(input);
	}
}
