/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.server.browser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.w3c.tools.codec.Base64Encoder;

/**
 * A collection of useful methods when working with html and json.
 *
 * @new@
 * @author Miroslav Pokorny
 * @version 1.0
 */
public class HtmlHelper {

	public static String base64Encode(final byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			throw new IllegalArgumentException(
					"The parameter:bytes must not be null or empty.");
		}

		Base64Encoder encoder = null;
		String value = null;

		try {
			// 1. encoded bytes into another byte array which ends up containing
			// the encoded bytes.
			final InputStream input = new ByteArrayInputStream(bytes);
			final ByteArrayOutputStream output = new ByteArrayOutputStream();

			encoder = new Base64Encoder(input, output);
			encoder.process();
			output.flush();
			output.close();

			input.close();

			final InputStreamReader reader = new InputStreamReader(
					new ByteArrayInputStream(output.toByteArray()));
			final StringWriter writer = new StringWriter();

			final char[] buffer = new char[4096];
			while (true) {
				final int readCount = reader.read(buffer);
				if (-1 == readCount) {
					break;
				}
				writer.write(buffer, 0, readCount);
			}

			reader.close();
			writer.flush();
			writer.close();

			value = writer.toString();
		} catch (final IOException io) {
			throw new RuntimeException(
					"Something went wrong while base64 encoding the byte array, message["
							+ io.getMessage() + "]", io);
		}
		return value;
	}

	public static String removeScriptTags(final String html) {
		if (null == html) {
			throw new IllegalArgumentException(
					"The parameter:html must not be null");
		}

		final char[] body = html.toCharArray();
		final StringBuffer output = new StringBuffer();

		boolean save = true;

		int i = 0;
		while (i < body.length) {

			// scan until a lessThan char is found.
			final char c = body[i];
			if (c != '<') {
				if (save) {
					output.append(c);
				}
				i++;
				continue;
			}

			// check if the next few characters match either the START SCRIPT or
			// END SCRIPT tag (ignoring case).
			boolean found = true;
			final char[] find = save ? BrowserConstants.START_SCRIPT
					: BrowserConstants.END_SCRIPT;

			// if there arent enuff characters left in body dont bother to test.
			final int left = body.length - i;
			if (left < find.length) {
				if (save) {
					output.append(body, i, left);
				}
				break;
			}

			// check if the next few characters match find
			for (int j = i; j < find.length; j++) {
				final char cc = body[i + j];
				final char f = find[j];
				if (f != Character.toLowerCase(cc)) {
					if (save) {
						output.append(body, i, j);
					}
					i = i + j;
					found = true;
					break;
				}
			}

			if (found) {
				i = i + find.length;
				save = !save;
			}
		} // while

		return output.toString();
	}

	protected HtmlHelper() {

	}
}