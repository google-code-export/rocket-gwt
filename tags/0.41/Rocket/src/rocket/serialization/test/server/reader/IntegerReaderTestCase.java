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

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.server.reader.IntegerReader;
import rocket.serialization.test.server.ServerTestCase;

public class IntegerReaderTestCase extends ServerTestCase {

	final static String INTEGER = Integer.class.getName();

	final static int INTEGER_VALUE = 12345;

	public void testReadInteger() {
		final String stream = "[1,\"" + INTEGER + "\",1,2," + INTEGER_VALUE + "]";
		final ObjectInputStream input = createObjectInputStream(stream, IntegerReader.instance);

		final Integer object = (Integer) input.readObject();
		assertNotNull(stream, object);

		assertEquals(INTEGER_VALUE, object.intValue());

		this.verifyFurtherReadsFail(input);
	}
}
