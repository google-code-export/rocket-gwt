/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.beans.test.generator.constructor;

import rocket.beans.client.BeanFactory;
import rocket.beans.test.generator.constructor.ambiguous.AmbiguousConstructorsBeanFactory;
import rocket.beans.test.generator.constructor.multiplearguments.HasMultipleArgumentsConstructor;
import rocket.beans.test.generator.constructor.multiplearguments.MultipleArgumentsConstructorBeanFactory;
import rocket.beans.test.generator.constructor.noarguments.HasNoArgumentsConstructor;
import rocket.beans.test.generator.constructor.noarguments.NoArgumentsConstructorBeanFactory;
import rocket.beans.test.generator.constructor.notfound.NotFoundBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorConstructorGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.constructor.BeanFactoryGeneratorConstructor";
	}
	
	public void testConstructorNotFound() {
		try {
			assertBindingFailed(GWT.create(NotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testAmbiguousConstructors() {
		try {
			assertBindingFailed(GWT.create(AmbiguousConstructorsBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testNoArgumentsConstructor() {
		final BeanFactory factory = (BeanFactory) GWT.create(NoArgumentsConstructorBeanFactory.class);
		final HasNoArgumentsConstructor bean = (HasNoArgumentsConstructor) factory.getBean(BEAN);
		assertNotNull(bean);
	}

	public void testConstructorWithMultipleParameters() {
		final BeanFactory factory = (BeanFactory) GWT.create(MultipleArgumentsConstructorBeanFactory.class);
		final HasMultipleArgumentsConstructor bean = (HasMultipleArgumentsConstructor) factory.getBean(BEAN);
		assertNotNull(bean);

		assertEquals("foo", bean.getFirst());
		assertEquals("bar", bean.getSecond());
	}

}
