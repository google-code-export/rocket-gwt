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
package rocket.beans.test.generator.alias;

import rocket.beans.test.generator.alias.alias.AliasBeanFactory;
import rocket.beans.test.generator.alias.duplicate.AliasNotUniqueBeanFactory;
import rocket.beans.test.generator.alias.notfound.AliasedBeanNotFoundBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorAliasGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.alias.BeanFactoryGeneratorAlias";
	}

	public void testAlias(){
		final AliasBeanFactory factory = (AliasBeanFactory) GWT.create(AliasBeanFactory.class);
		final Bean aliased = (Bean)factory.getBean( "alias");
		assertNotNull(aliased);
		
		final Object bean = factory.getBean( BEAN );
		assertNotNull(bean);
		assertSame( bean, aliased );
	}
	
	public void testAliasNameIsNotUnique(){
		try {
			assertBindingFailed(GWT.create(AliasNotUniqueBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}		
	}

	public void testAliasToBeanIdDoesntExist(){
		try {
			assertBindingFailed(GWT.create(AliasedBeanNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}		
	}

}
