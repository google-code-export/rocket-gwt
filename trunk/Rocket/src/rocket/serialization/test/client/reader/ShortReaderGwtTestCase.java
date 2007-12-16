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

import rocket.serialization.client.reader.ShortReader;
import rocket.serialization.test.client.ClientGwtTestCase;
import rocket.serialization.client.ClientObjectInputStream;

public class ShortReaderGwtTestCase extends ClientGwtTestCase {

	final static String SHORT = "java.lang.Short";

	final static short SHORT_VALUE = 1234;

	public void testReadShort() {
		final String stream = "[1,\"" + SHORT + "\",1,2," + SHORT_VALUE + "]";
		final ClientObjectInputStream input = createObjectInputStream(stream, SHORT, ShortReader.instance);

		final Short object = (Short) input.readObject();
		assertNotNull(stream, object);

		assertEquals(SHORT_VALUE, object.shortValue());

		this.verifyFurtherReadsFail(input);
	}
}
