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

import java.io.Serializable;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.server.reader.ReflectiveReader;
import rocket.serialization.test.server.ServerTestCase;

public class ReflectiveReaderTestCase extends ServerTestCase {

	public void testReadObject() {
		final String STRING = "hello";
		final String TEST = Test.class.getName();

		final String stream = "[2,\"" + TEST + "\",\"" + STRING + "\",1,2,3]";
		final ObjectInputStream input = createObjectInputStream(stream, ReflectiveReader.instance);

		final Test object = (Test) input.readObject();
		assertNotNull(stream, object);

		assertEquals(STRING, object.string);

		this.verifyFurtherReadsFail(input);
	}

	static public class Test implements Serializable {

		public Test() {
			super();
		}

		String string;
	}
}
