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

import rocket.serialization.client.ClientObjectOutputStream;
import rocket.serialization.client.writer.FloatArrayWriter;
import rocket.serialization.test.client.ClientGwtTestCase;

public class FloatArrayWriterGwtTestCase extends ClientGwtTestCase {

	final static String FLOAT_ARRAY = "[F";

	final static float FLOAT_VALUE = 34.5f;

	public void testWriteFloat() {
		final ClientObjectOutputStream output = createObjectOutputStream(FLOAT_ARRAY, FloatArrayWriter.instance);

		final float[] array = new float[1];
		array[0] = FLOAT_VALUE;
		output.writeObject(array);

		final String expectedValues = "1,2,1," + FLOAT_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + FLOAT_ARRAY + "\"," + expectedValues+"]", text);
	}
}
