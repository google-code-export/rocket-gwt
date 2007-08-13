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
package rocket.util.test.stringhelper;

import junit.framework.TestCase;
import rocket.util.client.StringHelper;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * A bunch of unit tests for various javascript powered StringHelper methods
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StringHelperGwtTestCase extends GWTTestCase {

    public String getModuleName() {
        return "rocket.util.test.stringhelper.StringHelperGwtTestCase";
    }

    public void testToCamelCase0() {
        final String input = "color";
        final String actual = StringHelper.toCamelCase(input);
        final String expected = "color";
        TestCase.assertEquals(input, expected, actual);
    }

    public void testToCamelCase1() {
        final String input = "background-color";
        final String actual = StringHelper.toCamelCase(input);
        final String expected = "backgroundColor";
        TestCase.assertEquals(input, expected, actual);
    }

    public void testToCssPropertyName0() {
        final String input = "color";
        final String actual = StringHelper.toCssPropertyName(input);
        final String expected = "color";
        TestCase.assertEquals(input, expected, actual);
    }

    public void testToCssPropertyName1() {
        final String input = "backgroundColor";
        final String actual = StringHelper.toCssPropertyName(input);
        final String expected = "background-color";
        TestCase.assertEquals(input, expected, actual);
    }
}
