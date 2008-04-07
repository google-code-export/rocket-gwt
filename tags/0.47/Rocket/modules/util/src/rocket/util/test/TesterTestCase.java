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
import rocket.util.client.Tester;

public class TesterTestCase extends TestCase {

	public void testIsNullOrEmpty0() {
		final String in = "a";
		final boolean expected = false;
		final boolean actual = Tester.isNullOrEmpty(in);
		assertEquals("Tester.isNullOrEmpty( \"" + in + "\" )", expected, actual);
	}

	public void testIsNullOrEmpty1() {
		final String in = "";
		final boolean expected = true;
		final boolean actual = Tester.isNullOrEmpty(in);
		assertEquals("Tester.isNullOrEmpty( \"" + in + "\" )", expected, actual);
	}

	public void testIsNullOrEmpty2() {
		final String in = null;
		final boolean expected = true;
		final boolean actual = Tester.isNullOrEmpty(in);
		assertEquals("Tester.isNullOrEmpty( \"" + in + "\" )", expected, actual);
	} // 
}
