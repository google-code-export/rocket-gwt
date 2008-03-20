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
package rocket.generator.test;

import junit.framework.TestCase;
import rocket.generator.rebind.util.StringBufferSourceWriter;

public class StringBufferSourceWriterTestCase extends TestCase {

	protected final static String EOL = System.getProperty("line.separator");

	public void testJavadocWithPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.beginJavaDocComment();
		writer.println("123");
		writer.endJavaDocComment();

		final String actual = writer.getBuffer();
		final String expected = "/**" + EOL + " * 123" + EOL + " */" + EOL;
		assertEquals(expected, actual);
	}

	public void testJavadocWithPrintPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.beginJavaDocComment();
		writer.print("12");
		writer.println("3");
		writer.endJavaDocComment();

		final String actual = writer.getBuffer();
		final String expected = "/**" + EOL + " * 123" + EOL + " */" + EOL;
		assertEquals(expected, actual);
	}

	public void testJavadocWithIndentationAndPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.beginJavaDocComment();
		writer.indent();
		writer.println("123");
		writer.endJavaDocComment();

		final String actual = writer.getBuffer();
		final String expected = "/**" + EOL + " *   123" + EOL + " */" + EOL;
		assertEquals(expected, actual);
	}

	public void testJavadocWithIndentationAndPrintPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.beginJavaDocComment();
		writer.indent();
		writer.print("1");
		writer.println("23");
		writer.endJavaDocComment();

		final String actual = writer.getBuffer();
		final String expected = "/**" + EOL + " *   123" + EOL + " */" + EOL;
		assertEquals(expected, actual);
	}

	public void testJavadocWithIndentationAndPrintPrintPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.beginJavaDocComment();
		writer.indent();
		writer.print("1");
		writer.print("2");
		writer.println("3");
		writer.endJavaDocComment();

		final String actual = writer.getBuffer();
		final String expected = "/**" + EOL + " *   123" + EOL + " */" + EOL;
		assertEquals(expected, actual);
	}

	public void testPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.println("123");

		final String actual = writer.getBuffer();
		final String expected = "123" + EOL;
		assertEquals(expected, actual);
	}

	public void testPrintPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.print("12");
		writer.println("3");

		final String actual = writer.getBuffer();
		final String expected = "123" + EOL;
		assertEquals(expected, actual);
	}

	public void testIndentPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.println("123");

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL;
		assertEquals(expected, actual);
	}

	public void testIndentWithPrintContainingLineBreaks() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.print("1" + EOL + "2" + EOL + "3" + EOL);

		final String actual = writer.getBuffer();
		final String expected = "  1" + EOL + "  2" + EOL + "  3" + EOL;
		assertEquals(expected, actual);
	}

	public void testIndentPrintPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.print("12");
		writer.println("3");

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL;
		assertEquals(expected, actual);
	}

	public void testIndentPrintlnPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.println("123");
		writer.println("456");

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL + "  456" + EOL;
		assertEquals(expected, actual);
	}

	public void testIndentPrintlnIndentPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.println("123");
		writer.indent();
		writer.println("456");

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL + "    456" + EOL;
		assertEquals(expected, actual);
	}

	public void testIndentPrintlnIndentPrintlnOutdentPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.println("123");
		writer.indent();
		writer.println("456");
		writer.outdent();
		writer.println("789");

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL + "    456" + EOL + "  789" + EOL;
		assertEquals(expected, actual);
	}

	public void testIndentPrintlnIndentPrintlnOutdentPrintlnOutdentPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.println("123");
		writer.indent();
		writer.println("456");
		writer.outdent();
		writer.println("789");
		writer.outdent();
		writer.println("0");

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL + "    456" + EOL + "  789" + EOL + "0" + EOL;
		assertEquals(expected, actual);
	}

	public void testIndentPrintlnOutdentPrintlnOutdent() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.println("123");
		writer.outdent();
		writer.println("456");
		writer.outdent();
		writer.println("789");

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL + "456" + EOL + "789" + EOL;
		assertEquals(expected, actual);
	}

	public void testOutsideInsideOutsideInsideJavadoc() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.indent();
		writer.println("123");
		writer.outdent();

		writer.beginJavaDocComment();
		writer.println("456");
		writer.endJavaDocComment();

		writer.indent();
		writer.println("789");
		writer.outdent();

		writer.indent();
		writer.beginJavaDocComment();
		writer.println("0");
		writer.endJavaDocComment();

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL + "/**" + EOL + " * 456" + EOL + " */" + EOL + "  789" + EOL + "  /**" + EOL + "   * 0" + EOL
				+ "   */" + EOL;
		assertEquals(expected, actual);
	}

	public void testOutdentIndentPrintln() {
		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		writer.outdent();
		writer.indent();
		writer.println("123");

		final String actual = writer.getBuffer();
		final String expected = "  123" + EOL;
		assertEquals(expected, actual);
	}
}
