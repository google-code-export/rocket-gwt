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

import rocket.beans.test.BeanFactoryTestCase;
import rocket.beans.test.PlaceHolderResolverTestCase;
import rocket.beans.test.PrototypeFactoryBeanTestCase;
import rocket.beans.test.SingletonFactoryBeanTestCase;
import rocket.collection.test.IteratorViewTestCase;
import rocket.collection.test.SkippingIteratorTestCase;
import rocket.collection.test.VisitRememberingIteratorTestCase;
import rocket.util.test.Base64EncoderTestCase;
import rocket.util.test.ColourTestCase;
import rocket.util.test.HttpHelperTestCase;
import rocket.util.test.StringHelperTestCase;
import rocket.util.test.ThrowableHelperTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestSuite that executes all unit tests.
 * @author Miroslav Pokorny
 */
public class RocketTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket");
		//$JUnit-BEGIN$
		suite.addTestSuite(SingletonFactoryBeanTestCase.class);
		suite.addTestSuite(BeanFactoryTestCase.class);
		suite.addTestSuite(PrototypeFactoryBeanTestCase.class);
		suite.addTestSuite(PlaceHolderResolverTestCase.class );
		
		suite.addTestSuite(SkippingIteratorTestCase.class);
		suite.addTestSuite(VisitRememberingIteratorTestCase.class);
		suite.addTestSuite(IteratorViewTestCase.class);

		suite.addTestSuite(HttpHelperTestCase.class);
		suite.addTestSuite(Base64EncoderTestCase.class);
		suite.addTestSuite(ColourTestCase.class);
		suite.addTestSuite(ThrowableHelperTestCase.class);
		suite.addTestSuite(StringHelperTestCase.class);
		
		//$JUnit-END$
		return suite;
	}

}
