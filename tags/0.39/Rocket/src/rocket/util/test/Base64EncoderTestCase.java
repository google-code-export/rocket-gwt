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
package rocket.util.test;

import junit.framework.TestCase;
import rocket.util.server.Base64Encoder;

/**
 * A series of unit tests for the base64 encoder class
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Base64EncoderTestCase extends TestCase {

	static public void assertEquals(final String comment, final String string, final String other) {
		TestCase.assertEquals(comment, removeNonBase64EncodedCharacters(string), removeNonBase64EncodedCharacters(other));
	}

	static String removeNonBase64EncodedCharacters(final String in) {
		final StringBuffer buf = new StringBuffer();
		final char[] chars = in.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			final char c = chars[i];
			if (false == Character.isSpace(c)) {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public void test1() throws Exception {
		final String in = "A";
		final byte[] inBytes = in.getBytes();

		final String expected = "QQ==";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test2() throws Exception {
		final String in = "AZ";
		final byte[] inBytes = in.getBytes();

		final String expected = "QVo=";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test3() throws Exception {
		final String in = "AZM";
		final byte[] inBytes = in.getBytes();

		final String expected = "QVpN";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test4() throws Exception {
		final String in = "AZMQ";
		final byte[] inBytes = in.getBytes();

		final String expected = "QVpNUQ==";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test5() throws Exception {
		final String in = "The quick brown fox jumped over the lazy dog.";
		final byte[] inBytes = in.getBytes();

		final String expected = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cu";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test6() throws Exception {
		final String in = "The quick brown fox jumped over the lazy dog.1";
		final byte[] inBytes = in.getBytes();

		final String expected = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cuMQ==";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test7() throws Exception {
		final String in = "The quick brown fox jumped over the lazy dog.12";
		final byte[] inBytes = in.getBytes();

		final String expected = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cuMTI=";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test8() throws Exception {
		final String in = "The quick brown fox jumped over the lazy dog.123";
		final byte[] inBytes = in.getBytes();

		final String expected = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cuMTIz";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test9() throws Exception {
		final String in = "The quick brown fox jumped over the lazy dog.1234";
		final byte[] inBytes = in.getBytes();

		final String expected = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wZWQgb3ZlciB0aGUgbGF6eSBkb2cuMTIzNA==";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test10() throws Exception {
		final String in = "Sydney Brisbane Melbourne Canberra";
		final byte[] inBytes = in.getBytes();

		final String expected = "U3lkbmV5IEJyaXNiYW5lIE1lbGJvdXJuZSBDYW5iZXJyYQ==";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test11() throws Exception {
		final String in = "Australia Sydney Brisbane Melbourne Canberra Perth1234567890 Adelaide";
		final byte[] inBytes = in.getBytes();

		final String expected = "QXVzdHJhbGlhIFN5ZG5leSBCcmlzYmFuZSBNZWxib3VybmUgQ2FuYmVycmEgUGVydGgxMjM0NTY3ODkwIEFkZWxhaWRl";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test12() throws Exception {
		final String in = "Australia Sydney Brisbane Melbourne Canberra Perth1234567890 Adelaide1";
		final byte[] inBytes = in.getBytes();

		final String expected = "QXVzdHJhbGlhIFN5ZG5leSBCcmlzYmFuZSBNZWxib3VybmUgQ2FuYmVycmEgUGVydGgxMjM0NTY3ODkwIEFkZWxhaWRlMQ==";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test13() throws Exception {
		final String in = "Australia Sydney Brisbane Melbourne Canberra Perth1234567890 Adelaide12";
		final byte[] inBytes = in.getBytes();

		final String expected = "QXVzdHJhbGlhIFN5ZG5leSBCcmlzYmFuZSBNZWxib3VybmUgQ2FuYmVycmEgUGVydGgxMjM0NTY3ODkwIEFkZWxhaWRlMTI=";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test14() throws Exception {
		final String in = "Australia Sydney Brisbane Melbourne Canberra Perth1234567890 Adelaide123";
		final byte[] inBytes = in.getBytes();

		final String expected = "QXVzdHJhbGlhIFN5ZG5leSBCcmlzYmFuZSBNZWxib3VybmUgQ2FuYmVycmEgUGVydGgxMjM0NTY3ODkwIEFkZWxhaWRlMTIz";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}

	public void test15() throws Exception {
		final String in = "Australia Sydney Brisbane Melbourne Canberra Perth1234567890 Adelaide1234";
		final byte[] inBytes = in.getBytes();

		final String expected = "QXVzdHJhbGlhIFN5ZG5leSBCcmlzYmFuZSBNZWxib3VybmUgQ2FuYmVycmEgUGVydGgxMjM0NTY3ODkwIEFkZWxhaWRlMTIzNA==";
		final String actual = Base64Encoder.encode(inBytes);

		assertEquals("in\t\"" + in + "\"\nexpect\t\"" + expected + "\"\nactual\t\"" + actual + "\".", expected, actual);
	}
}