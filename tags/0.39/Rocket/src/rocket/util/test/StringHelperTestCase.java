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
import rocket.util.client.StringHelper;

/**
 * A series of unit tests for public methods of StringHelper.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StringHelperTestCase extends TestCase {

	public void testIsNullOrEmpty0() {
		final String in = "a";
		final boolean expected = false;
		final boolean actual = StringHelper.isNullOrEmpty(in);
		assertEquals("StringHelper.isNullOrEmpty( \"" + in + "\" )", expected, actual);
	}

	public void testIsNullOrEmpty1() {
		final String in = "";
		final boolean expected = true;
		final boolean actual = StringHelper.isNullOrEmpty(in);
		assertEquals("StringHelper.isNullOrEmpty( \"" + in + "\" )", expected, actual);
	}

	public void testIsNullOrEmpty2() {
		final String in = null;
		final boolean expected = true;
		final boolean actual = StringHelper.isNullOrEmpty(in);
		assertEquals("StringHelper.isNullOrEmpty( \"" + in + "\" )", expected, actual);
	}

	public void testPadLeft0() {
		final String in = "apple";
		final int paddedLength = 8;
		final char c = '.';
		final String actual = StringHelper.padLeft(in, paddedLength, c);
		final String expected = "..." + in;
		assertEquals("StringHelper.padLeft( \"" + in + "\"." + paddedLength + ", \"" + c + "\" )", expected, actual);
	}

	public void testPadLeft1() {
		final String in = "apple";
		final int paddedLength = 5;
		final char c = '.';
		final String actual = StringHelper.padLeft(in, paddedLength, c);
		final String expected = in;
		assertEquals("StringHelper.padLeft( \"" + in + "\"." + paddedLength + ", \"" + c + "\" )", expected, actual);
	}

	public void testPadLeft2() {
		final String in = "apple";
		final int paddedLength = 10;
		final char c = '.';
		final String actual = StringHelper.padLeft(in, paddedLength, c);
		final String expected = "....." + in;
		assertEquals("StringHelper.padLeft( \"" + in + "\"." + paddedLength + ", \"" + c + "\" )", expected, actual);
	}

	public void testPadRight0() {
		final String in = "apple";
		final int paddedLength = 8;
		final char c = '.';
		final String actual = StringHelper.padRight(in, paddedLength, c);
		final String expected = in + "...";
		assertEquals("StringHelper.padRight( \"" + in + "\"." + paddedLength + ", \"" + c + "\" )", expected, actual);
	}

	public void testPadRight1() {
		final String in = "apple";
		final int paddedLength = 5;
		final char c = '.';
		final String actual = StringHelper.padRight(in, paddedLength, c);
		final String expected = in;
		assertEquals("StringHelper.padRight( \"" + in + "\"." + paddedLength + ", \"" + c + "\" )", expected, actual);
	}

	public void testPadRight2() {
		final String in = "apple";
		final int paddedLength = 10;
		final char c = '.';
		final String actual = StringHelper.padRight(in, paddedLength, c);
		final String expected = in + ".....";
		assertEquals("StringHelper.padRight( \"" + in + "\"." + paddedLength + ", \"" + c + "\" )", expected, actual);
	}

	public void testStartsWithIgnoreCase0() {
		final String test = "apple";
		final String startsWith = "ap";
		final boolean actual = StringHelper.startsWithIgnoringCase(test, startsWith);
		final boolean expected = true;
		assertEquals("StringHelper.startsWithIgnoringCase( \"" + test + "\",[}" + startsWith + "\")", expected, actual);
	}

	public void testStartsWithIgnoreCase1() {
		final String test = "apple";
		final String startsWith = "AP";
		final boolean actual = StringHelper.startsWithIgnoringCase(test, startsWith);
		final boolean expected = true;
		assertEquals("StringHelper.startsWithIgnoringCase( \"" + test + "\",[}" + startsWith + "\")", expected, actual);
	}

	public void testStartsWithIgnoreCase2() {
		final String test = "Apple";
		final String startsWith = "aP";
		final boolean actual = StringHelper.startsWithIgnoringCase(test, startsWith);
		final boolean expected = true;
		assertEquals("StringHelper.startsWithIgnoringCase( \"" + test + "\",[}" + startsWith + "\")", expected, actual);
	}

	public void testStartsWithIgnoreCase3() {
		final String test = "Apple";
		final String startsWith = test + "Banana";
		final boolean actual = StringHelper.startsWithIgnoringCase(test, startsWith);
		final boolean expected = false;
		assertEquals("StringHelper.startsWithIgnoringCase( \"" + test + "\",[}" + startsWith + "\")", expected, actual);
	}

	public void testEndsWithIgnoreCase0() {
		final String test = "apple";
		final String endsWith = "le";
		final boolean actual = StringHelper.endsWithIgnoringCase(test, endsWith);
		final boolean expected = true;
		assertEquals("StringHelper.endsWithIgnoringCase( \"" + test + "\",[}" + endsWith + "\")", expected, actual);
	}

	public void testEndsWithIgnoreCase1() {
		final String test = "apple";
		final String endsWith = "LE";
		final boolean actual = StringHelper.endsWithIgnoringCase(test, endsWith);
		final boolean expected = true;
		assertEquals("StringHelper.endsWithIgnoringCase( \"" + test + "\",[}" + endsWith + "\")", expected, actual);
	}

	public void testEndsWithIgnoreCase2() {
		final String test = "Apple";
		final String endsWith = "LE";
		final boolean actual = StringHelper.endsWithIgnoringCase(test, endsWith);
		final boolean expected = true;
		assertEquals("StringHelper.endsWithIgnoringCase( \"" + test + "\",[}" + endsWith + "\")", expected, actual);
	}

	public void testEndsWithIgnoreCase3() {
		final String test = "Apple";
		final String endsWith = "Banana" + test;
		final boolean actual = StringHelper.endsWithIgnoringCase(test, endsWith);
		final boolean expected = false;
		assertEquals("StringHelper.endsWithIgnoringCase( \"" + test + "\",[}" + endsWith + "\")", expected, actual);
	}

	public void testSplitIgnoringDelimiters0() {
		final String first = "the";
		final String second = "quick";
		final String third = "brown";

		final String delimiters = " ";
		final String input = first + delimiters + second + delimiters + third;
		boolean ignoreDelimiters = true;

		final String[] tokens = StringHelper.split(input, delimiters, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 3 tokens", 3, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", second, tokens[1]);
		assertEquals("The third token should be", third, tokens[2]);
	}

	public void testSplitIgnoringDelimiters1() {
		final String first = "a";
		final String second = "b";
		final String third = "c";

		final String delimiters = " ";
		final String input = first + delimiters + second + delimiters + third;
		boolean ignoreDelimiters = true;

		final String[] tokens = StringHelper.split(input, delimiters, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 3 tokens", 3, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", second, tokens[1]);
		assertEquals("The third token should be", third, tokens[2]);
	}

	public void testSplitIgnoringDelimitersWithEmptyTokens() {
		final String first = "a";
		final String second = "";
		final String third = "c";

		final String delimiters = " ";
		final String input = first + delimiters + second + delimiters + third;
		boolean ignoreDelimiters = true;

		final String[] tokens = StringHelper.split(input, delimiters, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 3 tokens", 3, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", second, tokens[1]);
		assertEquals("The third token should be", third, tokens[2]);
	}

	public void testSplitIgnoringDelimitersWithEmptyFirstToken() {
		final String first = "";
		final String second = "b";
		final String third = "c";

		final String delimiters = " ";
		final String input = first + delimiters + second + delimiters + third;
		boolean ignoreDelimiters = true;

		final String[] tokens = StringHelper.split(input, delimiters, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 3 tokens", 3, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", second, tokens[1]);
		assertEquals("The third token should be", third, tokens[2]);
	}
	
	public void testSplitIgnoringDelimitersWithEmptyLastToken() {
		final String first = "a";
		final String second = "b";
		final String third = "";

		final String delimiters = " ";
		final String input = first + delimiters + second + delimiters + third;
		boolean ignoreDelimiters = true;

		final String[] tokens = StringHelper.split(input, delimiters, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 3 tokens", 3, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", second, tokens[1]);
		assertEquals("The third token should be", third, tokens[2]);
	}

	
	public void testSplitIgnoringDelimitersWithTrailingDelimiter() {
		final String first = "the";
		final String second = "quick";
		final String third = "brown";

		final String delimiters = " ";
		final String input = first + delimiters + second + delimiters + third;
		boolean ignoreDelimiters = true;

		final String[] tokens = StringHelper.split(input, delimiters, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 3 tokens", 3, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", second, tokens[1]);
		assertEquals("The third token should be", third, tokens[2]);
	}

	public void testSplitUsingDifferentDelimitersIgnoringDelimiters() {
		final String first = "the";
		final String second = "quick";
		final String third = "brown";

		final String delimiter0 = " ";
		final String delimiter1 = ",";
		final String input = first + delimiter0 + second + delimiter1 + third;
		boolean ignoreDelimiters = true;

		final String[] tokens = StringHelper.split(input, delimiter0 + delimiter1, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 3 tokens", 3, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", second, tokens[1]);
		assertEquals("The third token should be", third, tokens[2]);
	}

	public void testSplitUsingDifferentDelimitersIgnoringDelimitersWithTrailingDelimiter() {
		final String first = "the";
		final String second = "quick";
		final String third = "brown";

		final String delimiter0 = " ";
		final String delimiter1 = ",";
		final String input = first + delimiter0 + second + delimiter1 + third + delimiter0;
		boolean ignoreDelimiters = true;

		final String[] tokens = StringHelper.split(input, delimiter0 + delimiter1, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 4 tokens\"" + input + "\".", 4, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", second, tokens[1]);
		assertEquals("The third token should be", third, tokens[2]);
		assertEquals("The fourth token should be", "", tokens[3]);
	}

	public void testSplitKeepingDelimiters() {
		final String first = "the";
		final String second = "quick";
		final String third = "brown";

		final String delimiters = " ";
		final String input = first + delimiters + second + delimiters + third;
		boolean ignoreDelimiters = false;

		final String[] tokens = StringHelper.split(input, delimiters, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 5 tokens", 5, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", delimiters, tokens[1]);
		assertEquals("The third token should be", second, tokens[2]);
		assertEquals("The fourth token should be", delimiters, tokens[3]);
		assertEquals("The fifth token should be", third, tokens[4]);
	}

	public void testSplitWithTrailingDelimiterKeepingDelimiters() {
		final String first = "the";
		final String second = "quick";
		final String third = "brown";

		final String delimiters = " ";
		final String input = first + delimiters + second + delimiters + third + delimiters;
		boolean ignoreDelimiters = false;

		final String[] tokens = StringHelper.split(input, delimiters, ignoreDelimiters);
		assertNotNull("The returned tokens array should not be null", tokens);
		assertEquals("There should be 7 tokens \"" + input + "\".", 7, tokens.length);

		assertEquals("The first token should be", first, tokens[0]);
		assertEquals("The second token should be", delimiters, tokens[1]);
		assertEquals("The third token should be", second, tokens[2]);
		assertEquals("The fourth token should be", delimiters, tokens[3]);
		assertEquals("The fifth token should be", third, tokens[4]);
		assertEquals("The sixth token should be", delimiters, tokens[5]);
		assertEquals("The seventh token should be", "", tokens[6]);
	}

	public void testStartsWithIgnoringCaseWhichSucceeds() {
		final String first = "green.apple";
		final String second = "green";

		assertTrue(
				"StringHelper.startsWithIgnoringCase() should return true when querying if \"" + first + "\" starts with \"" + second + "\".",
				StringHelper.startsWithIgnoringCase(first, second));
	}

	public void testStartsWithIgnoringCaseWhichFails() {
		final String first = "green.apple";
		final String second = "zebra";

		assertFalse("StringHelper.startsWithIgnoringCase() should return true when querying if \"" + first + "\" starts with \"" + second
				+ "\".", StringHelper.startsWithIgnoringCase(first, second));
	}

	public void testStartsWithIgnoringCaseWhichFailsBecauseSecondIsLongerThanFirst() {
		final String first = "green.apple";
		final String second = "green.apple.big";

		assertFalse("StringHelper.startsWithIgnoringCase() should return true when querying if \"" + first + "\" starts with \"" + second
				+ "\".", StringHelper.startsWithIgnoringCase(first, second));
	}

	public void testIndexOfIgnoringCaseWhichFails() {
		final String string = "apple";
		final String search = "banana";

		assertEquals(-1, StringHelper.indexOfIgnoringCase(string, search));
	}

	public void testIndexOfIgnoringCaseWhichMatchesWhereCasesAreIdentical() {
		final String string = "apple banana carrot";
		final String search = "banana";

		assertEquals(6, StringHelper.indexOfIgnoringCase(string, search));
	}

	public void testIndexOfIgnoringCaseWhichMatchesButCasesAreDifferent() {
		final String string = "apple banana carrot";
		final String search = "BANAna";

		assertEquals(6, StringHelper.indexOfIgnoringCase(string, search));
	}

	public void testIndexOfIgnoringCaseWhichFailsEvenThoughPartialMatchExists() {
		final String string = "apple banana carrot";
		final String search = "BANANARAMAMA";

		assertEquals(-1, StringHelper.indexOfIgnoringCase(string, search));
	}

	public void testIndexOfIgnoringCaseMatchesWordAtStart() {
		final String string = "apple banana carrot";
		final String search = "ApPlE";

		assertEquals(0, StringHelper.indexOfIgnoringCase(string, search));
	}

	public void testIndexOfIgnoringCaseMatchesWordAtEnd() {
		final String string = "apple banana carrot";
		final String search = "CARROT";

		assertEquals("apple banana ".length(), StringHelper.indexOfIgnoringCase(string, search));
	}

	public void testHtmlEncodeAlpha() {
		final String plainText = "abc";
		final String actual = StringHelper.htmlEncode(plainText);
		final String expected = plainText;
		assertEquals(expected, actual);
	}

	public void testHtmlEncodeNumber() {
		final String plainText = "123";
		final String actual = StringHelper.htmlEncode(plainText);
		final String expected = plainText;
		assertEquals(expected, actual);
	}

	public void testHtmlEncodeEntityCharsWhichShouldBeEscaped() {
		final String plainText = "<>\"&";
		final String actual = StringHelper.htmlEncode(plainText);
		final String expected = "&lt;&gt;&quot;&amp;";
		assertEquals(expected, actual);
	}

	public void testHtmlDecodeAlpha() {
		final String plainText = "abc";
		final String actual = StringHelper.htmlDecode(plainText);
		final String expected = plainText;
		assertEquals(expected, actual);
	}

	public void testHtmlDecodeNumber() {
		final String plainText = "123";
		final String actual = StringHelper.htmlDecode(plainText);
		final String expected = plainText;
		assertEquals(expected, actual);
	}

	public void testHtmlDecodeEntities() {
		final String plainText = "&lt;&gt;&quot;&amp;";
		final String actual = StringHelper.htmlDecode(plainText);
		final String expected = "<>\"&";
		assertEquals(expected, actual);
	}
}