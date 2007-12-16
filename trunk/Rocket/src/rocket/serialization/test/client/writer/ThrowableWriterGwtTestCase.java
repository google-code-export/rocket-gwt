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

import java.util.Date;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import rocket.serialization.client.writer.DateWriter;
import rocket.serialization.client.writer.ThrowableWriter;
import rocket.serialization.test.client.ClientGwtTestCase;
import rocket.serialization.client.ClientObjectOutputStream;

public class ThrowableWriterGwtTestCase extends ClientGwtTestCase {

	final static String THROWABLE = "java.lang.Throwable";
	
	public void testWriteThrowableWithNoMessage() {
		final ClientObjectOutputStream output = createObjectOutputStream( THROWABLE, ThrowableWriter.instance);

		final Throwable throwable= new Throwable();
		output.writeObject(throwable);

		final String expectedValues = "1,2,0,0,0";

		final String text = output.getText();
		assertEquals("[1,\"" + THROWABLE + "\","+ expectedValues + "]", text);
	}
	
	/**
	 * This test will fail in hosted mode because the emulated Throwable will not be used.
	 */
	public void testWriteThrowableWithMessage() {		
		final ClientObjectOutputStream output = createObjectOutputStream( THROWABLE, ThrowableWriter.instance);
		final String message = "apple";
		
		final Throwable throwable= new Throwable( message );
		output.writeObject(throwable);

		final String expectedValues = "1,2,3,0,0";

		final String text = output.getText();
		assertEquals("[2,\"" + THROWABLE + "\",\"" + message + "\","+ expectedValues + "]", text);		
	}
}
