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
package rocket.logging.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import rocket.logging.test.loggerfactorygenerator.client.LoggerFactoryGeneratorGwtTestCase;
import rocket.logging.test.serverlogger.client.ServerLoggerGwtTestCase;

public class LoggingTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket.logging.Logging");
		addTests(suite);
		return suite;
	}

	public static void addTests(TestSuite suite) {
		suite.addTestSuite(LoggerFactoryImplTestCase.class);
		suite.addTestSuite(LoggingLevelTestCase.class);
		suite.addTestSuite(PropertiesFileLoggingFactoryConfigTestCase.class);

		// FIXME unsure why doenst work within TestSuite but works as a
		// standalone.
		suite.addTestSuite(LoggerFactoryGeneratorGwtTestCase.class);
		suite.addTestSuite(ServerLoggerGwtTestCase.class);
	}
}
