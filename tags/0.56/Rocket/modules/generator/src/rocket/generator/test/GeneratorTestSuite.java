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
package rocket.generator.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import rocket.generator.test.generator.client.GeneratorGwtTestCase;
import rocket.generator.test.templatedfilecodeblock.client.TemplatedFileCodeBlockGwtTestCase;

/**
 * TestSuite that executes all unit tests relating to the rocket.Generator
 * module
 * 
 * @author Miroslav Pokorny
 */
public class GeneratorTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket.Generator");
		addTests(suite);
		return suite;
	}

	public static void addTests(TestSuite suite) {
		suite.addTestSuite(AllMethodsVisitorTestCase.class);
		suite.addTestSuite(MethodTestCase.class);
		suite.addTestSuite(GeneratorHelperTestCase.class);
		suite.addTestSuite(StringBufferSourceWriterTestCase.class);
		suite.addTestSuite(TypeTestCase.class);
		suite.addTestSuite(VirtualMethodTestCase.class);
		suite.addTestSuite(CollectionTemplatedCodeBlockTestCase.class);
		suite.addTestSuite(SubClassVisitorTestCase.class);
		suite.addTestSuite(ConcreteTypesImplementingInterfaceVisitorTestCase.class);
		suite.addTestSuite(ReachableTypesVisitorTestCase.class);
		
		suite.addTestSuite(GeneratorGwtTestCase.class);
		suite.addTestSuite(TemplatedFileCodeBlockGwtTestCase.class);
	}
}
