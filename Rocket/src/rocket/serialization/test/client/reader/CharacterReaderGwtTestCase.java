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

import rocket.serialization.client.reader.CharacterReader;
import rocket.serialization.test.client.ClientGwtTestCase;
import rocket.serialization.client.ClientObjectInputStream;

public class CharacterReaderGwtTestCase extends ClientGwtTestCase {

	final static String CHARACTER = "java.lang.Character";

	final static char CHARACTER_VALUE = 'a';

	public void testReadCharacter() {
		final String stream = "[1,\"" + CHARACTER + "\",1,2," + (int) CHARACTER_VALUE + "]";
		final ClientObjectInputStream input = createObjectInputStream(stream, CHARACTER, CharacterReader.instance);

		final Character object = (Character) input.readObject();
		assertNotNull(stream, object);

		assertEquals(CHARACTER_VALUE, object.charValue());

		this.verifyFurtherReadsFail(input);
	}
}