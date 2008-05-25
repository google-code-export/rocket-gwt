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
package rocket.beans.test.generator.placeholder;

import rocket.beans.test.generator.placeholder.placeholder.PlaceHolderBean;
import rocket.beans.test.generator.placeholder.placeholder.PlaceHolderBeanFactory;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorPlaceHolderGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.placeholder.BeanFactoryGeneratorPlaceHolder";
	}
	
	public void testPlaceHolders() {
		final PlaceHolderBeanFactory factory = (PlaceHolderBeanFactory) GWT.create(PlaceHolderBeanFactory.class);
		final PlaceHolderBean bean = (PlaceHolderBean) factory.getBean(BEAN);
		assertEquals("orange yellow green green", bean.getStringProperty());
	}


}
