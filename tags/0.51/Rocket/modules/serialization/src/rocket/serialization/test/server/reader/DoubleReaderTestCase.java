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
import rocket.serialization.server.reader.DoubleReader;
import rocket.serialization.test.server.ServerTestCase;

public class DoubleReaderTestCase extends ServerTestCase {

	final static String DOUBLE = Double.class.getName();

	final static double DOUBLE_VALUE = 1234.5;

	public void testReadDouble() {
		final String stream = "[1,\"" + DOUBLE + "\",1,2," + DOUBLE_VALUE + "]";
		final ObjectInputStream input = createObjectInputStream(stream, DoubleReader.instance);

		final Double object = (Double) input.readObject();
		assertNotNull(stream, object);

		assertEquals(DOUBLE_VALUE, object.doubleValue(), 0.1);

		this.verifyFurtherReadsFail(input);
	}
}
