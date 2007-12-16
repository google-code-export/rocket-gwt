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

import java.util.Map;

import rocket.serialization.client.writer.CharacterWriter;
import rocket.serialization.test.client.ClientGwtTestCase;
import rocket.serialization.client.ClientObjectOutputStream;

public class CharacterWriterGwtTestCase extends ClientGwtTestCase {

	final static String CHARACTER = "java.lang.Character";

	final static char CHARACTER_VALUE = 'a';

	public void testWriteCharacter() {
		final ClientObjectOutputStream output = createObjectOutputStream(CHARACTER, CharacterWriter.instance);

		final Character object = new Character(CHARACTER_VALUE);
		output.writeObject(object);

		final String expectedValues = "1,2," + (int) CHARACTER_VALUE;
		final String text = output.getText();
		assertEquals("[1,\"" + CHARACTER + "\"," + expectedValues+"]", text);
	}
}
