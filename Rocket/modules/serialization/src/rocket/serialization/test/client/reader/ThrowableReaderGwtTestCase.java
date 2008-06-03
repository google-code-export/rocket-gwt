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

import rocket.serialization.client.ClientObjectInputStream;
import rocket.serialization.client.reader.ThrowableReader;
import rocket.serialization.test.client.ClientGwtTestCase;

/**
 * These tests will fail in hosted mode, because the jdk Throwable rather than
 * emulated Throwable will be used.
 * 
 * @author Miroslav Pokorny
 */
public class ThrowableReaderGwtTestCase extends ClientGwtTestCase {

	final static String THROWABLE = "java.lang.Throwable";

	public void testReadThrowableWithNoMessage() {
		final String stream = "[1,\"" + THROWABLE + "\",1,2,0,0,0]";
		final ClientObjectInputStream input = this.createObjectInputStream(stream, THROWABLE, ThrowableReader.instance);
		final Throwable instance = (Throwable) input.readObject();

		assertNull(instance.getMessage());
		assertNull(instance.getCause());

		// final StackTraceElement[] elements = instance.getStackTrace();
		// assertNotNull( elements );
		// assertEquals( 0, elements.length );

		// this.verifyFurtherReadsFail(input);
	}

	public void testReadThrowableWithMessage() {
		final String message = "apple";
		final String stream = "[2,\"" + THROWABLE + "\",\"" + message + "\",1,2,3,0,0]";
		final ClientObjectInputStream input = this.createObjectInputStream(stream, THROWABLE, ThrowableReader.instance);
		final Throwable instance = (Throwable) input.readObject();
		assertEquals(message, instance.getMessage());
		assertNull(instance.getCause());

		final StackTraceElement[] elements = instance.getStackTrace();
		assertNotNull(elements);
		assertEquals(0, elements.length);

		// this.verifyFurtherReadsFail(input);
	}
}
