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
package rocket.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import rocket.beans.test.BeanTestSuite;
import rocket.collection.test.CollectionTestSuite;
import rocket.cookie.test.CookieTestSuite;
import rocket.generator.test.GeneratorTestSuite;
import rocket.logging.test.LoggingTestSuite;
import rocket.remoting.test.RemotingTestSuite;
import rocket.serialization.test.SerializationTestSuite;
import rocket.style.test.StyleTestSuite;
import rocket.text.test.TextTestSuite;
import rocket.util.test.UtilTestSuite;
import rocket.widget.test.WidgetTestSuite;

/**
 * TestSuite that executes all unit tests within the entire rocket project.
 * 
 * @author Miroslav Pokorny
 */
public class RocketProjectTestSuite {
	

	public static Test suite() {
		final TestSuite suite = new TestSuite("TestSuite for rocket");
		
		BeanTestSuite.addTests(suite);
		CollectionTestSuite.addTests(suite);
		CookieTestSuite.addTests(suite);
		GeneratorTestSuite.addTests(suite);
		LoggingTestSuite.addTests(suite);
		RemotingTestSuite.addTests(suite);
		SerializationTestSuite.addTests(suite);
		StyleTestSuite.addTests(suite);
		TextTestSuite.addTests(suite);
		UtilTestSuite.addTests(suite);
		WidgetTestSuite.addTests(suite);
		
		return suite;
	}
}
