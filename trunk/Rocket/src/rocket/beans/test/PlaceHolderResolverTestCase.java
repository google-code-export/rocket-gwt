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
package rocket.beans.test;

import java.util.Properties;

import junit.framework.TestCase;
import rocket.beans.rebind.placeholder.PlaceHolderResolver;

public class PlaceHolderResolverTestCase extends TestCase {

	public void testResolveNoPlaceHolder() {
		final PlaceHolderResolver resolver = new PlaceHolderResolver();

		final String input = "banana";
		final String actual = resolver.resolve(input);
		final String expected = input;

		assertEquals(expected, actual);
	}

	public void testResolveWithPlaceHolder() {
		final String APPLE = "apple";
		final String APPLE_VALUE = "green";

		final PlaceHolderResolver resolver = new PlaceHolderResolver() {
			protected Properties createValues() {
				final Properties properties = new Properties();
				properties.setProperty(APPLE, APPLE_VALUE);
				return properties;
			}
		};

		final String input = "${apple} banana";
		final String actual = resolver.resolve(input);
		final String expected = APPLE_VALUE + " banana";

		assertEquals(expected, actual);
	}

	public void testResolveWithPlaceHolderReferringAnotherPlaceHolder() {
		final String APPLE = "apple";
		final String APPLE_VALUE = "green";

		final String BANANA = "banana";
		final String BANANA_VALUE = "yellow ${apple}";

		final String CARROT = "carrot";
		final String CARROT_VALUE = "orange ${banana}";

		final PlaceHolderResolver resolver = new PlaceHolderResolver() {
			protected Properties createValues() {
				final Properties properties = new Properties();
				properties.setProperty(APPLE, APPLE_VALUE);
				properties.setProperty(BANANA, BANANA_VALUE);
				properties.setProperty(CARROT, CARROT_VALUE);
				return properties;
			}
		};

		final String input = "${carrot} 123";
		final String actual = resolver.resolve(input);
		final String expected = "orange yellow " + APPLE_VALUE + " 123";

		assertEquals(expected, actual);
	}
}
