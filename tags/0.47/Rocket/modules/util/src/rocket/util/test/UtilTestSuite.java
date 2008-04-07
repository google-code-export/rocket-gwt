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

import junit.framework.Test;
import junit.framework.TestSuite;
import rocket.util.test.stacktrace.test.StackTraceGwtTestCase;

/**
 * TestSuite that executes all unit tests relating to the rocket.Util module
 * 
 * @author Miroslav Pokorny
 */
public class UtilTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket.Util");
		addTests( suite );
		return suite;
	}

	public static void addTests(TestSuite suite) {
		suite.addTestSuite(TesterTestCase.class);
		suite.addTestSuite(UtitiliesTestCase.class);
		suite.addTestSuite(Base64EncoderTestCase.class);
		suite.addTestSuite(ColourTestCase.class);
		suite.addTestSuite(HueSaturationValueTestCase.class);
		suite.addTestSuite(ThrowableHelperTestCase.class);
	
		suite.addTestSuite(StackTraceGwtTestCase.class);
	}
}
