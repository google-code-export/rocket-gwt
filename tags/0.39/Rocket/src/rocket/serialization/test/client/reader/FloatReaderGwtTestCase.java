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

import rocket.serialization.client.reader.FloatReader;
import rocket.serialization.test.client.ClientGwtTestCase;
import rocket.serialization.client.ClientObjectInputStream;

public class FloatReaderGwtTestCase extends ClientGwtTestCase {

	final static String FLOAT = "java.lang.Float";

	final static float FLOAT_VALUE = 1234.5f;

	public void testReadFloat() {
		final String stream = "[1,\"" + FLOAT + "\",1,2," + FLOAT_VALUE + "]";
		final ClientObjectInputStream input = createObjectInputStream(stream, FLOAT, FloatReader.instance);

		final Float object = (Float) input.readObject();
		assertNotNull(stream, object);

		assertEquals(FLOAT_VALUE, object.floatValue(), 0.1f);

		this.verifyFurtherReadsFail(input);
	}
}