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
package rocket.serialization.test.server.writer;

import java.io.Serializable;

import rocket.serialization.server.writer.ReflectiveWriter;
import rocket.serialization.test.server.ServerTestCase;
import rocket.serialization.test.server.TestServerObjectOutputStream;

public class ReflectiveWriterTestCase extends ServerTestCase {

	public void testWriteObject() {
		final TestServerObjectOutputStream output = createObjectOutputStream(ReflectiveWriter.instance);

		final String STRING = "hello";

		final Test test = new Test();
		test.string = STRING;
		output.writeObject(test);

		final String expectedValues = "1,2,3";
		final String text = output.getText();
		assertEquals("[2,\"" + Test.class.getName() + "\",\"" + STRING + "\"," + expectedValues + "]", text);
	}

	static public class Test implements Serializable {
		String string;
	}
}
