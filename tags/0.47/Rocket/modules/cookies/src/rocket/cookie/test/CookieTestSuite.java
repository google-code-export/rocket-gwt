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
package rocket.cookie.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import rocket.cookie.test.cookies.CookiesGwtTestCase;

/**
 * TestSuite that executes all unit tests relating to the rocket.Cookies module
 * 
 * @author Miroslav Pokorny
 */
public class CookieTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket.Cookies");
		addTests( suite );
		return suite;
	}

	public static void addTests(TestSuite suite) {
		suite.addTestSuite(CookiesGwtTestCase.class );
	}
}
