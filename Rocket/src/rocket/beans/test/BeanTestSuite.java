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

import junit.framework.Test;
import junit.framework.TestSuite;
import rocket.beans.test.generator.alias.BeanFactoryGeneratorAliasGwtTestCase;
import rocket.beans.test.generator.aspects.client.BeanFactoryGeneratorAspectsGwtTestCase;
import rocket.beans.test.generator.bean.BeanFactoryGeneratorBeanGwtTestCase;
import rocket.beans.test.generator.beanreference.BeanFactoryGeneratorBeanReferenceGwtTestCase;
import rocket.beans.test.generator.constructor.BeanFactoryGeneratorConstructorGwtTestCase;
import rocket.beans.test.generator.include.BeanFactoryGeneratorIncludeGwtTestCase;
import rocket.beans.test.generator.placeholder.BeanFactoryGeneratorPlaceHolderGwtTestCase;
import rocket.beans.test.generator.properties.BeanFactoryGeneratorPropertiesGwtTestCase;
import rocket.beans.test.generator.rpc.BeanFactoryGeneratorRpcGwtTestCase;

/**
 * TestSuite that executes all unit tests relating to the Beans module
 * 
 * @author Miroslav Pokorny
 */
public class BeanTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket.Beans");
		addTests( suite );
		return suite;
	}

	public static void addTests(TestSuite suite) {
		suite.addTestSuite(SingletonFactoryBeanTestCase.class);
		suite.addTestSuite(BeanFactoryTestCase.class);
		suite.addTestSuite(PrototypeFactoryBeanTestCase.class);
		suite.addTestSuite(PlaceHolderResolverTestCase.class);
		suite.addTestSuite(InterceptorChainTestCase.class);
		suite.addTestSuite(AfterReturningAdviceMethodInterceptorTestCase.class);
		suite.addTestSuite(BeforeAdviceMethodInterceptorTestCase.class);
		suite.addTestSuite(AfterThrowingAdviceMethodInterceptorTestCase.class);
		suite.addTestSuite(AfterFinallyAdviceMethodInterceptorTestCase.class);
		suite.addTestSuite(MethodMatcherTestCase.class);
		suite.addTestSuite(ProxyFactoryBeanTestCase.class);

		suite.addTestSuite(BeanFactoryGeneratorBeanGwtTestCase.class);
		suite.addTestSuite(BeanFactoryGeneratorIncludeGwtTestCase.class);
		suite.addTestSuite(BeanFactoryGeneratorPlaceHolderGwtTestCase.class);
		suite.addTestSuite(BeanFactoryGeneratorAliasGwtTestCase.class);
		suite.addTestSuite(BeanFactoryGeneratorConstructorGwtTestCase.class);
		suite.addTestSuite(BeanFactoryGeneratorPropertiesGwtTestCase.class);
		suite.addTestSuite(BeanFactoryGeneratorRpcGwtTestCase.class);
		suite.addTestSuite(BeanFactoryGeneratorBeanReferenceGwtTestCase.class);		
		suite.addTestSuite(BeanFactoryGeneratorAspectsGwtTestCase.class);		
	}
}
