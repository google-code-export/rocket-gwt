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
import rocket.serialization.server.reader.FloatArrayReader;
import rocket.serialization.test.server.ServerTestCase;

public class FloatArrayReaderTestCase extends ServerTestCase {

	final static String FLOAT_ARRAY = float[].class.getName();

	final static float FLOAT_VALUE = 34.5f;

	public void testReadFloatArray() {
		final String stream = "[1,\"" + FLOAT_ARRAY + "\",1,2,1," + FLOAT_VALUE + "]";
		final ObjectInputStream input = createObjectInputStream(stream, FloatArrayReader.instance);

		final float[] array = (float[]) input.readObject();
		assertNotNull(stream, array);
		assertEquals("size", 1, array.length);

		assertEquals(FLOAT_VALUE, array[0], 0.1f );

		this.verifyFurtherReadsFail(input);
	}
}
