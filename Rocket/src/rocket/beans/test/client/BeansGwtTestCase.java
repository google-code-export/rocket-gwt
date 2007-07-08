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
package rocket.beans.test.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.beans.client.BeanFactory;
import rocket.beans.test.client.beanclassnotfound.BeanClassNotFoundBeanFactory;
import rocket.beans.test.client.beantypenotconcrete.BeanTypeThatIsNotConcreteBeanFactory;
import rocket.beans.test.client.booleanproperty.BeanWithBooleanPropertyBeanFactory;
import rocket.beans.test.client.booleanproperty.ClassWithBooleanProperty;
import rocket.beans.test.client.byteproperty.BeanWithBytePropertyBeanFactory;
import rocket.beans.test.client.byteproperty.ClassWithByteProperty;
import rocket.beans.test.client.charproperty.BeanWithCharPropertyBeanFactory;
import rocket.beans.test.client.charproperty.ClassWithCharProperty;
import rocket.beans.test.client.classisnotabeanfactory.ClassIsNotABeanFactory;
import rocket.beans.test.client.constructornotfound.ConstructorNotFoundBeanFactory;
import rocket.beans.test.client.doubleproperty.BeanWithDoublePropertyBeanFactory;
import rocket.beans.test.client.doubleproperty.ClassWithDoubleProperty;
import rocket.beans.test.client.factorymethod.Bean;
import rocket.beans.test.client.factorymethod.FactoryMethodBeanFactory;
import rocket.beans.test.client.factorymethodnotfound.FactoryMethodNotFoundBeanFactory;
import rocket.beans.test.client.floatproperty.BeanWithFloatPropertyBeanFactory;
import rocket.beans.test.client.floatproperty.ClassWithFloatProperty;
import rocket.beans.test.client.initmethod.BeanWithCustomInit;
import rocket.beans.test.client.initmethod.BeanWithCustomInitMethodBeanFactory;
import rocket.beans.test.client.initmethodnotfound.BeanWithMissingInitMethodBeanFactory;
import rocket.beans.test.client.interfacedoesntimplementbeanfactory.InterfaceDoesntImplementBeanFactory;
import rocket.beans.test.client.intproperty.BeanWithIntPropertyBeanFactory;
import rocket.beans.test.client.intproperty.ClassWithIntProperty;
import rocket.beans.test.client.invalidbeanscope.BeanWithInvalidScopeBeanFactory;
import rocket.beans.test.client.json.RemoteJsonServiceAsync;
import rocket.beans.test.client.json.RemoteJsonServiceBeanFactory;
import rocket.beans.test.client.listproperty.BeanWithListPropertyBeanFactory;
import rocket.beans.test.client.listproperty.ClassWithListProperty;
import rocket.beans.test.client.longproperty.BeanWithLongPropertyBeanFactory;
import rocket.beans.test.client.longproperty.ClassWithLongProperty;
import rocket.beans.test.client.manyvalues.BeanWithManyValuesBeanFactory;
import rocket.beans.test.client.manyvalues.ClassWithManyValues;
import rocket.beans.test.client.mapproperty.BeanWithMapPropertyBeanFactory;
import rocket.beans.test.client.mapproperty.ClassWithMapProperty;
import rocket.beans.test.client.missingbeanid.MissingBeanIdBeanFactory;
import rocket.beans.test.client.multipleargumentsconstructor.BeanWithMultipleArgumentsConstructorBeanFactory;
import rocket.beans.test.client.multipleargumentsconstructor.ClassWithMultipleArgumentsConstructor;
import rocket.beans.test.client.noargumentsconstructor.BeanWithNoArgumentsConstructorBeanFactory;
import rocket.beans.test.client.noargumentsconstructor.ClassWithNoArgumentsConstructor;
import rocket.beans.test.client.referencesanotherbean.BeanWithAnotherBeanReferenceBeanFactory;
import rocket.beans.test.client.referencesanotherbean.ClassWithBeanReference;
import rocket.beans.test.client.rpc.RemoteRpcServiceAsync;
import rocket.beans.test.client.rpc.RemoteRpcServiceBeanFactory;
import rocket.beans.test.client.setproperty.BeanWithSetPropertyBeanFactory;
import rocket.beans.test.client.setproperty.ClassWithSetProperty;
import rocket.beans.test.client.shortproperty.BeanWithShortPropertyBeanFactory;
import rocket.beans.test.client.shortproperty.ClassWithShortProperty;
import rocket.beans.test.client.stringproperty.BeanWithStringPropertyBeanFactory;
import rocket.beans.test.client.stringproperty.ClassWithStringProperty;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A series of tests for the BeanFactoryGenerator classs.
 * 
 * @author Miroslav Pokorny
 */
public class BeansGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	public String getModuleName() {
		return "rocket.beans.test.Beans";
	}

	public void testNotABeanFactory() {
		try {
			assertBindingFailed(GWT.create(ClassIsNotABeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().endsWith(
					"NotABeanFactoryException"));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void testInterfaceDoesntImplementBeanFactory() {
		try {
			assertBindingFailed(GWT
					.create(InterfaceDoesntImplementBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().endsWith(
					"NotABeanFactoryException"));
		}
	}

	public void testMissingBeanId() {
		try {
			assertBindingFailed(GWT.create(MissingBeanIdBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			final String causeType = failed.getCauseType();
			assertTrue("" + failed, causeType.equals(SAX_PARSE_EXCEPTION)
					|| causeType.endsWith("BeanIdMissingException"));
		}
	}

	public void testInvalidBeanScope() {
		try {
			assertBindingFailed(GWT
					.create(BeanWithInvalidScopeBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			final String causeType = failed.getCauseType();
			assertTrue("" + failed, causeType.equals(SAX_PARSE_EXCEPTION)
					|| causeType.endsWith("InvalidBeanScopeException"));
		}
	}

	public void testBeanClassNotFound() {
		try {
			assertBindingFailed(GWT.create(BeanClassNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().endsWith(
					"BeanTypeNotFoundException"));
		}
	}

	public void testBeanTypeIsNotConcrete() {
		try {
			assertBindingFailed(GWT
					.create(BeanTypeThatIsNotConcreteBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().endsWith(
					"BeanTypeNotConcreteException"));
		}
	}

	public void testFactoryMethodNotFound() {
		try {
			assertBindingFailed(GWT
					.create(FactoryMethodNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().endsWith(
					"FactoryMethodNotFoundException"));
		}
	}

	public void testFactoryMethod() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(FactoryMethodBeanFactory.class);
		final Bean bean = (Bean) factory.getBean("bean");
		assertNotNull(bean);
	}

	public void testInitMethodNotFound() {
		try {
			assertBindingFailed(GWT
					.create(BeanWithMissingInitMethodBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().endsWith(
					"InitMethodNotFoundException"));
		}
	}

	public void testInitMethod() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithCustomInitMethodBeanFactory.class);
		final BeanWithCustomInit bean = (BeanWithCustomInit) factory
				.getBean("bean");
		assertNotNull(bean);
		assertTrue(bean.customInitCalled);
	}

	public void testNoArgumentsConstructor() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithNoArgumentsConstructorBeanFactory.class);
		final ClassWithNoArgumentsConstructor bean = (ClassWithNoArgumentsConstructor) factory
				.getBean("bean");
		assertNotNull(bean);
	}

	public void testConstructorWithMultipleParameters() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithMultipleArgumentsConstructorBeanFactory.class);
		final ClassWithMultipleArgumentsConstructor bean = (ClassWithMultipleArgumentsConstructor) factory
				.getBean("bean");
		assertNotNull(bean);

		assertEquals("foo", bean.getFirst());
		assertEquals("bar", bean.getSecond());
	}

	public void testConstructorNotFound() {
		try {
			assertBindingFailed(GWT
					.create(ConstructorNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().endsWith(
					"ConstructorNotFoundException"));
		}
	}

	public void testBooleanProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithBooleanPropertyBeanFactory.class);
		final ClassWithBooleanProperty bean = (ClassWithBooleanProperty) factory
				.getBean("bean");
		assertTrue(bean.getBooleanProperty());
	}

	public void testByteProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithBytePropertyBeanFactory.class);
		final ClassWithByteProperty bean = (ClassWithByteProperty) factory
				.getBean("bean");
		assertEquals(123, bean.getByteProperty());
	}

	public void testShortProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithShortPropertyBeanFactory.class);
		final ClassWithShortProperty bean = (ClassWithShortProperty) factory
				.getBean("bean");
		assertEquals(123, bean.getShortProperty());
	}

	public void testIntProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithIntPropertyBeanFactory.class);
		final ClassWithIntProperty bean = (ClassWithIntProperty) factory
				.getBean("bean");
		assertEquals(123, bean.getIntProperty());
	}

	public void testLongProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithLongPropertyBeanFactory.class);
		final ClassWithLongProperty bean = (ClassWithLongProperty) factory
				.getBean("bean");
		assertEquals(123, bean.getLongProperty());
	}

	public void testFloatProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithFloatPropertyBeanFactory.class);
		final ClassWithFloatProperty bean = (ClassWithFloatProperty) factory
				.getBean("bean");
		assertEquals(123.45f, bean.getFloatProperty(), 0.01f);
	}

	public void testDoubleProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithDoublePropertyBeanFactory.class);
		final ClassWithDoubleProperty bean = (ClassWithDoubleProperty) factory
				.getBean("bean");
		assertEquals(123.45, bean.getDoubleProperty(), 0.01);
	}

	public void testCharProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithCharPropertyBeanFactory.class);
		final ClassWithCharProperty bean = (ClassWithCharProperty) factory
				.getBean("bean");
		assertEquals('a', bean.getCharProperty());
	}

	public void testStringProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithStringPropertyBeanFactory.class);
		final ClassWithStringProperty bean = (ClassWithStringProperty) factory
				.getBean("bean");
		assertEquals("apple", bean.getStringProperty());
	}

	public void testListProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithListPropertyBeanFactory.class);
		final ClassWithListProperty bean = (ClassWithListProperty) factory
				.getBean("bean");
		final List list = bean.getListProperty();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("apple", list.get(0));
		assertEquals("banana", list.get(1));
		assertEquals("carrot", list.get(2));
	}

	public void testSetProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithSetPropertyBeanFactory.class);
		final ClassWithSetProperty bean = (ClassWithSetProperty) factory
				.getBean("bean");
		final Set set = bean.getSetProperty();
		assertNotNull(set);
		assertEquals(3, set.size());
		assertTrue("apple", set.contains("apple"));
		assertTrue("banana", set.contains("banana"));
		assertTrue("carrot", set.contains("carrot"));
		assertFalse("dog", set.contains("dog"));
	}

	public void testMapProperty() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithMapPropertyBeanFactory.class);
		final ClassWithMapProperty bean = (ClassWithMapProperty) factory
				.getBean("bean");
		final Map map = bean.getMapProperty();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals("apple=green", "green", map.get("apple"));
		assertEquals("banana=yellow", "yellow", map.get("banana"));
		assertEquals("carrot=orange", "orange", map.get("carrot"));
		assertEquals("dog", null, map.get("dog"));
	}

	public void testManyValues() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithManyValuesBeanFactory.class);
		final ClassWithManyValues bean = (ClassWithManyValues) factory
				.getBean("bean");
		assertEquals("green", bean.getApple());
		assertEquals("yellow", bean.getBanana());
		assertEquals("orange", bean.getCarrot());
	}

	public void testBeanReference() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(BeanWithAnotherBeanReferenceBeanFactory.class);
		final ClassWithBeanReference bean = (ClassWithBeanReference) factory
				.getBean("bean");
		assertNotNull(bean);

		assertNotNull("bean reference was not set", bean.getAnotherBean());
	}

	public void testRemoteRpcService() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(RemoteRpcServiceBeanFactory.class);
		final RemoteRpcServiceAsync bean = (RemoteRpcServiceAsync) factory
				.getBean("bean");
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/remoteRpcService", serviceDefTarget.getServiceEntryPoint()
				.endsWith("/remoteRpcService"));
	}

	public void testRemoteJsonService() {
		final BeanFactory factory = (BeanFactory) GWT
				.create(RemoteJsonServiceBeanFactory.class);
		final RemoteJsonServiceAsync bean = (RemoteJsonServiceAsync) factory
				.getBean("bean");
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/remoteJsonService", serviceDefTarget
				.getServiceEntryPoint().endsWith("/remoteJsonService"));
	}
}
