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
package rocket.style.test.stylepropertyvalue;

import junit.framework.TestCase;
import rocket.style.client.CssUnit;
import rocket.style.client.StylePropertyValue;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * UnitTest for StylePropertyValue testing various conversion getters.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StylePropertyValueGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.style.test.stylepropertyvalue.StylePropertyValueGwtTestCase";
	}

	public void testGetNumber() {
		final StylePropertyValue value = new StylePropertyValue();
		value.setString("123px");

		final double actual = value.getDouble(CssUnit.PX);
		final double expected = 123.0;
		TestCase.assertEquals(expected, actual, 0.1);
	}

	public void testSetNumber() {
		final StylePropertyValue value = new StylePropertyValue();
		value.setString("123px");

		final double newValue = 456;
		value.setDouble(newValue, CssUnit.PX);

		final String actual = value.getString();
		final String expected = "456px";
		TestCase.assertEquals(expected, actual);
	}

	public void testGetInteger() {
		final StylePropertyValue value = new StylePropertyValue();
		value.setString("123px");

		final int actual = value.getInteger(CssUnit.PX);
		final int expected = 123;
		TestCase.assertEquals(expected, actual);
	}

	public void testSetInteger() {
		final StylePropertyValue value = new StylePropertyValue();
		value.setString("123px");

		value.setInteger(456, CssUnit.PX);

		final String actual = value.getString();
		final String expected = "456px";
		TestCase.assertEquals(expected, actual);
	}

	public void testGetUnit() {
		final StylePropertyValue value = new StylePropertyValue();
		value.setString("456em");

		final CssUnit actualUnit = value.getUnit();
		final CssUnit expectedUnit = CssUnit.EM;
		TestCase.assertSame(actualUnit, expectedUnit);
	}
}
