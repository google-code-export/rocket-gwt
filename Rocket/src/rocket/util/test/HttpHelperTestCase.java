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
import rocket.util.client.HttpHelper;

/**
 * Unit tests for HttpHelper
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HttpHelperTestCase extends TestCase {
    public void testUrlEncode0() {
        final String unencoded = "abcdef";
        final String encoded = HttpHelper.urlEncode(unencoded);
        final String expected = unencoded;

        assertEquals(expected, encoded);
    }

    public void testUrlEncode1() {
        final String unencoded = "1234567890";
        final String encoded = HttpHelper.urlEncode(unencoded);
        final String expected = unencoded;

        assertEquals(expected, encoded);
    }

    public void testUrlEncode2() {
        final String unencoded = " +!";
        final String encoded = HttpHelper.urlEncode(unencoded);
        final String expected = "+%" + Integer.toHexString('+') + "!";

        assertEquals(expected, encoded);
    }

    public void testUrlDecode0() {
        final String encoded = "abcdef";
        final String decoded = HttpHelper.urlDecode(encoded);
        final String expected = encoded;

        assertEquals(expected, decoded);
    }

    public void testUrlDecode1() {
        final String encoded = "1234567890";
        final String decoded = HttpHelper.urlDecode(encoded);
        final String expected = encoded;

        assertEquals(expected, decoded);
    }

    public void testUrlDecode2() {
        final String encoded = "+%" + Integer.toHexString((int) '+') + "!";
        final String decoded = HttpHelper.urlDecode(encoded);
        final String expected = " +!";

        assertEquals(expected, decoded);
    }
}
