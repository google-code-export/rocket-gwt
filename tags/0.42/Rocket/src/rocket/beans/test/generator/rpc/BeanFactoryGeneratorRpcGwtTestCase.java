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
package rocket.beans.test.generator.rpc;

import rocket.beans.test.generator.rpc.gwt.GwtRpcServiceAsync;
import rocket.beans.test.generator.rpc.gwt.GwtRpcServiceBeanFactory;
import rocket.beans.test.generator.rpc.json.JsonRpcServiceAsync;
import rocket.beans.test.generator.rpc.json.JsonRpcServiceBeanFactory;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorRpcGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.rpc.BeanFactoryGeneratorRpc";
	}
	
	public void testGwtRpcService() {
		final GwtRpcServiceBeanFactory factory = (GwtRpcServiceBeanFactory) GWT.create(GwtRpcServiceBeanFactory.class);
		final GwtRpcServiceAsync bean = (GwtRpcServiceAsync) factory.getBean(BEAN);
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/rpc", serviceDefTarget.getServiceEntryPoint().endsWith("/rpc"));
	}

	public void testJsonRpcService() {
		final JsonRpcServiceBeanFactory factory = (JsonRpcServiceBeanFactory) GWT.create(JsonRpcServiceBeanFactory.class);
		final JsonRpcServiceAsync bean = (JsonRpcServiceAsync) factory.getBean(BEAN);
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/json", serviceDefTarget.getServiceEntryPoint().endsWith("/json"));
	}
}
