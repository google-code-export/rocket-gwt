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
import rocket.beans.test.AfterFinallyAdviceMethodInterceptorTestCase;
import rocket.beans.test.AfterReturningAdviceMethodInterceptorTestCase;
import rocket.beans.test.AfterThrowingAdviceMethodInterceptorTestCase;
import rocket.beans.test.BeanFactoryTestCase;
import rocket.beans.test.BeforeAdviceMethodInterceptorTestCase;
import rocket.beans.test.InterceptorChainTestCase;
import rocket.beans.test.MethodMatcherTestCase;
import rocket.beans.test.PlaceHolderResolverTestCase;
import rocket.beans.test.PrototypeFactoryBeanTestCase;
import rocket.beans.test.ProxyFactoryBeanTestCase;
import rocket.beans.test.SingletonFactoryBeanTestCase;
import rocket.beans.test.beans.client.BeansGwtTestCase;
import rocket.collection.test.IteratorViewTestCase;
import rocket.collection.test.SkippingIteratorTestCase;
import rocket.collection.test.VisitRememberingIteratorTestCase;
import rocket.generator.test.AllMethodsVisitorTestCase;
import rocket.generator.test.CollectionTemplatedCodeBlockTestCase;
import rocket.generator.test.GeneratorHelperTestCase;
import rocket.generator.test.MethodTestCase;
import rocket.generator.test.StringBufferSourceWriterTestCase;
import rocket.generator.test.TypeTestCase;
import rocket.generator.test.VirtualMethodTestCase;
import rocket.generator.test.generator.client.GeneratorGwtTestCase;
import rocket.generator.test.templatedfilecodeblock.client.TemplatedFileCodeBlockGwtTestCase;
import rocket.remoting.test.remotejsonservice.client.RemoteJsonServiceGwtTestCase;
import rocket.style.test.computedstyle.ComputedStyleGwtTestCase;
import rocket.style.test.dynamicexpression.DynamicExpressionTestCase;
import rocket.style.test.inlinestyle.InlineStyleGwtTestCase;
import rocket.style.test.stylepropertyvalue.StylePropertyValueGwtTestCase;
import rocket.text.test.IndexedPlaceHolderReplacerTestCase;
import rocket.text.test.NamedPlaceHolderReplacerTestCase;
import rocket.util.test.Base64EncoderTestCase;
import rocket.util.test.ColourTestCase;
import rocket.util.test.HttpHelperTestCase;
import rocket.util.test.HueSaturationValueTestCase;
import rocket.util.test.StringHelperTestCase;
import rocket.util.test.ThrowableHelperTestCase;
import rocket.util.test.stacktrace.test.StackTraceGwtTestCase;
import rocket.widget.test.htmltemplatefactory.client.HtmlTemplateFactoryGwtTestCase;

/**
 * TestSuite that executes all unit tests.
 * 
 * @author Miroslav Pokorny
 */
public class RocketTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket");

		// rocket.beans
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

		suite.addTestSuite(BeansGwtTestCase.class);

		// rocket.collection
		suite.addTestSuite(SkippingIteratorTestCase.class);
		suite.addTestSuite(VisitRememberingIteratorTestCase.class);
		suite.addTestSuite(IteratorViewTestCase.class);

		// rocket.cookie.*
		// suite.addTestSuite(CookiesGwtTestCase.class );

		// rocket.generator
		suite.addTestSuite(AllMethodsVisitorTestCase.class);
		suite.addTestSuite(MethodTestCase.class);
		suite.addTestSuite(GeneratorHelperTestCase.class);
		suite.addTestSuite(StringBufferSourceWriterTestCase.class);
		suite.addTestSuite(GeneratorGwtTestCase.class);
		suite.addTestSuite(TypeTestCase.class);
		suite.addTestSuite(TemplatedFileCodeBlockGwtTestCase.class);
		suite.addTestSuite(VirtualMethodTestCase.class);
		suite.addTestSuite(CollectionTemplatedCodeBlockTestCase.class);

		// rocket.remoting
		suite.addTestSuite(RemoteJsonServiceGwtTestCase.class);

		// rocket.style
		suite.addTestSuite(ComputedStyleGwtTestCase.class);
		suite.addTestSuite(DynamicExpressionTestCase.class);
		suite.addTestSuite(InlineStyleGwtTestCase.class);
		suite.addTestSuite(StylePropertyValueGwtTestCase.class);

		// rocket.text
		suite.addTestSuite(IndexedPlaceHolderReplacerTestCase.class);
		suite.addTestSuite(NamedPlaceHolderReplacerTestCase.class);

		// rocket.util
		suite.addTestSuite(HttpHelperTestCase.class);
		suite.addTestSuite(Base64EncoderTestCase.class);
		suite.addTestSuite(ColourTestCase.class);
		suite.addTestSuite(HueSaturationValueTestCase.class);
		suite.addTestSuite(ThrowableHelperTestCase.class);
		suite.addTestSuite(StringHelperTestCase.class);
		suite.addTestSuite(StackTraceGwtTestCase.class);

		// rocket.widget
		suite.addTestSuite(HtmlTemplateFactoryGwtTestCase.class);

		return suite;
	}
}
