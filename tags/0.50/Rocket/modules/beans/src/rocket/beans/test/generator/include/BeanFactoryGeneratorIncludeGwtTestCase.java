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
package rocket.beans.test.generator.include;

import rocket.beans.client.BeanFactory;
import rocket.beans.test.generator.include.cycle.CycleBeanFactory;
import rocket.beans.test.generator.include.multiple.MultipleIncludedFilesBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorIncludeGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.include.BeanFactoryGeneratorInclude";
	}
	
	public void testMultipleIncludedFiles() {
		final BeanFactory factory = (BeanFactory) GWT.create(MultipleIncludedFilesBeanFactory.class);
		final Bean bean = (Bean)factory.getBean(BEAN);
		assertNotNull(bean);

		final Bean beanFromSecondFile = (Bean)factory.getBean("beanFromSecondFile");
		assertNotNull(beanFromSecondFile);

		final Bean beanFromThirdFile = (Bean)factory.getBean("beanFromThirdFile");
		assertNotNull(beanFromThirdFile);
	}

	public void testCycle() {
		try {
			assertBindingFailed(GWT.create(CycleBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
			
			final String message = failed.getMessage();
			assertTrue( "" + message, message.indexOf("ActualBeanFactoryWithCycle.xml") != -1 );
		} 
	}


}
