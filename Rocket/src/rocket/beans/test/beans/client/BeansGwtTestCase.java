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
package rocket.beans.test.beans.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.beans.client.BeanFactory;
import rocket.beans.test.beans.client.ambiguousconstructors.AmbiguousConstructorsBeanFactory;
import rocket.beans.test.beans.client.ambiguoussetters.AmbiguousSettersBeanFactory;
import rocket.beans.test.beans.client.beanclassnotfound.NotFoundBeanFactory;
import rocket.beans.test.beans.client.beanreference.BeanReferenceBeanFactory;
import rocket.beans.test.beans.client.beanreference.ClassWithBeanReference;
import rocket.beans.test.beans.client.beantypenotconcrete.BeanTypeThatIsNotConcreteBeanFactory;
import rocket.beans.test.beans.client.booleanproperty.BooleanPropertyBeanFactory;
import rocket.beans.test.beans.client.booleanproperty.ClassWithBooleanProperty;
import rocket.beans.test.beans.client.byteproperty.BytePropertyBeanFactory;
import rocket.beans.test.beans.client.byteproperty.ClassWithByteProperty;
import rocket.beans.test.beans.client.charproperty.CharPropertyBeanFactory;
import rocket.beans.test.beans.client.charproperty.ClassWithCharProperty;
import rocket.beans.test.beans.client.constructornotfound.ConstructorNotFoundBeanFactory;
import rocket.beans.test.beans.client.doubleproperty.ClassWithDoubleProperty;
import rocket.beans.test.beans.client.doubleproperty.DoublePropertyBeanFactory;
import rocket.beans.test.beans.client.factorymethod.Bean;
import rocket.beans.test.beans.client.factorymethod.FactoryMethodBeanFactory;
import rocket.beans.test.beans.client.factorymethodnotfound.FactoryMethodNotFoundBeanFactory;
import rocket.beans.test.beans.client.floatproperty.ClassWithFloatProperty;
import rocket.beans.test.beans.client.floatproperty.FloatPropertyBeanFactory;
import rocket.beans.test.beans.client.initmethod.BeanWithCustomInit;
import rocket.beans.test.beans.client.initmethod.InitMethodBeanFactory;
import rocket.beans.test.beans.client.initmethodnotfound.InitMethodNotFoundBeanFactory;
import rocket.beans.test.beans.client.interfacedoesntimplementbeanfactory.InterfaceDoesntImplementBeanFactory;
import rocket.beans.test.beans.client.intproperty.ClassWithIntProperty;
import rocket.beans.test.beans.client.intproperty.IntPropertyBeanFactory;
import rocket.beans.test.beans.client.invalidbeanscope.InvalidScopeBeanFactory;
import rocket.beans.test.beans.client.listproperty.ClassWithListProperty;
import rocket.beans.test.beans.client.listproperty.ListPropertyBeanFactory;
import rocket.beans.test.beans.client.longproperty.ClassWithLongProperty;
import rocket.beans.test.beans.client.longproperty.LongPropertyBeanFactory;
import rocket.beans.test.beans.client.manyvalues.ClassWithManyValues;
import rocket.beans.test.beans.client.manyvalues.ManyValuesBeanFactory;
import rocket.beans.test.beans.client.mapproperty.ClassWithMapProperty;
import rocket.beans.test.beans.client.mapproperty.MapPropertyBeanFactory;
import rocket.beans.test.beans.client.missingbeanid.MissingBeanIdBeanFactory;
import rocket.beans.test.beans.client.multipleargumentsconstructor.ClassWithMultipleArgumentsConstructor;
import rocket.beans.test.beans.client.multipleargumentsconstructor.MultipleArgumentsConstructorBeanFactory;
import rocket.beans.test.beans.client.noargumentsconstructor.ClassWithNoArgumentsConstructor;
import rocket.beans.test.beans.client.noargumentsconstructor.NoArgumentsConstructorBeanFactory;
import rocket.beans.test.beans.client.noproperties.ClassWithNoProperties;
import rocket.beans.test.beans.client.noproperties.NoPropertiesBeanFactory;
import rocket.beans.test.beans.client.notabeanfactory.ClassIsNotABeanFactory;
import rocket.beans.test.beans.client.placeholders.PlaceHolderBean;
import rocket.beans.test.beans.client.placeholders.PlaceHolderBeanFactory;
import rocket.beans.test.beans.client.remotejsonservice.RemoteJsonServiceAsync;
import rocket.beans.test.beans.client.remotejsonservice.RemoteJsonServiceBeanFactory;
import rocket.beans.test.beans.client.remoterpcservice.RemoteRpcServiceAsync;
import rocket.beans.test.beans.client.remoterpcservice.RemoteRpcServiceBeanFactory;
import rocket.beans.test.beans.client.setproperty.ClassWithSetProperty;
import rocket.beans.test.beans.client.setproperty.SetPropertyBeanFactory;
import rocket.beans.test.beans.client.shortproperty.ClassWithShortProperty;
import rocket.beans.test.beans.client.shortproperty.ShortPropertyBeanFactory;
import rocket.beans.test.beans.client.stringproperty.ClassWithStringProperty;
import rocket.beans.test.beans.client.stringproperty.StringPropertyBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A series of tests for the BeanFactoryGenerator type.
 * 
 * @author Miroslav Pokorny
 */
public class BeansGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_ID = "bean";

	public String getModuleName() {
		return "rocket.beans.test.beans.Beans";
	}

	public void testNotABeanFactory() {
		try {
			assertBindingFailed(GWT.create(ClassIsNotABeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void testInterfaceDoesntImplementBeanFactory() {
		try {
			assertBindingFailed(GWT.create(InterfaceDoesntImplementBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testMissingBeanId() {
		try {
			assertBindingFailed(GWT.create(MissingBeanIdBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			final String causeType = failed.getCauseType();
			assertTrue("" + failed, causeType.equals(SAX_PARSE_EXCEPTION)
					|| causeType.equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testInvalidBeanScope() {
		try {
			assertBindingFailed(GWT.create(InvalidScopeBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			final String causeType = failed.getCauseType();
			assertTrue("" + failed, causeType.equals(SAX_PARSE_EXCEPTION)
					|| causeType.equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testBeanClassNotFound() {
		try {
			assertBindingFailed(GWT.create(NotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testBeanTypeIsNotConcrete() {
		try {
			assertBindingFailed(GWT.create(BeanTypeThatIsNotConcreteBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testFactoryMethodNotFound() {
		try {
			assertBindingFailed(GWT.create(FactoryMethodNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testFactoryMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(FactoryMethodBeanFactory.class);
		final Bean bean = (Bean) factory.getBean(BEAN_ID);
		assertNotNull(bean);
	}

	public void testInitMethodNotFound() {
		try {
			assertBindingFailed(GWT.create(InitMethodNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testInitMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(InitMethodBeanFactory.class);
		final BeanWithCustomInit bean = (BeanWithCustomInit) factory.getBean(BEAN_ID);
		assertNotNull(bean);
		assertTrue(bean.customInitCalled);
	}

	public void testConstructorNotFound() {
		try {
			assertBindingFailed(GWT.create(ConstructorNotFoundBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testAmbiguousConstructors() {
		try {
			assertBindingFailed(GWT.create(AmbiguousConstructorsBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testNoArgumentsConstructor() {
		final BeanFactory factory = (BeanFactory) GWT.create(NoArgumentsConstructorBeanFactory.class);
		final ClassWithNoArgumentsConstructor bean = (ClassWithNoArgumentsConstructor) factory.getBean(BEAN_ID);
		assertNotNull(bean);
	}

	public void testConstructorWithMultipleParameters() {
		final BeanFactory factory = (BeanFactory) GWT.create(MultipleArgumentsConstructorBeanFactory.class);
		final ClassWithMultipleArgumentsConstructor bean = (ClassWithMultipleArgumentsConstructor) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		assertEquals("foo", bean.getFirst());
		assertEquals("bar", bean.getSecond());
	}

	public void testAmbiguousPropertySetters() {
		try {
			assertBindingFailed(GWT.create(AmbiguousSettersBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals("rocket.beans.rebind.BeanFactoryGeneratorException"));
		}
	}

	public void testNoProperties() {
		final BeanFactory factory = (BeanFactory) GWT.create(NoPropertiesBeanFactory.class);
		final ClassWithNoProperties bean = (ClassWithNoProperties) factory.getBean(BEAN_ID);
		assertNotNull(bean);
	}

	public void testBooleanProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BooleanPropertyBeanFactory.class);
		final ClassWithBooleanProperty bean = (ClassWithBooleanProperty) factory.getBean(BEAN_ID);
		assertTrue(bean.getBooleanProperty());
	}

	public void testByteProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BytePropertyBeanFactory.class);
		final ClassWithByteProperty bean = (ClassWithByteProperty) factory.getBean(BEAN_ID);
		assertEquals(123, bean.getByteProperty());
	}

	public void testShortProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(ShortPropertyBeanFactory.class);
		final ClassWithShortProperty bean = (ClassWithShortProperty) factory.getBean(BEAN_ID);
		assertEquals(123, bean.getShortProperty());
	}

	public void testIntProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(IntPropertyBeanFactory.class);
		final ClassWithIntProperty bean = (ClassWithIntProperty) factory.getBean(BEAN_ID);
		assertEquals(123, bean.getIntProperty());
	}

	public void testLongProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(LongPropertyBeanFactory.class);
		final ClassWithLongProperty bean = (ClassWithLongProperty) factory.getBean(BEAN_ID);
		assertEquals(123, bean.getLongProperty());
	}

	public void testFloatProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(FloatPropertyBeanFactory.class);
		final ClassWithFloatProperty bean = (ClassWithFloatProperty) factory.getBean(BEAN_ID);
		assertEquals(123.45f, bean.getFloatProperty(), 0.01f);
	}

	public void testDoubleProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(DoublePropertyBeanFactory.class);
		final ClassWithDoubleProperty bean = (ClassWithDoubleProperty) factory.getBean(BEAN_ID);
		assertEquals(123.45, bean.getDoubleProperty(), 0.01);
	}

	public void testCharProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(CharPropertyBeanFactory.class);
		final ClassWithCharProperty bean = (ClassWithCharProperty) factory.getBean(BEAN_ID);
		assertEquals('a', bean.getCharProperty());
	}

	public void testStringProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(StringPropertyBeanFactory.class);
		final ClassWithStringProperty bean = (ClassWithStringProperty) factory.getBean(BEAN_ID);
		assertEquals("apple", bean.getStringProperty());
	}

	public void testListProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(ListPropertyBeanFactory.class);
		final ClassWithListProperty bean = (ClassWithListProperty) factory.getBean(BEAN_ID);
		final List list = bean.getListProperty();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("apple", list.get(0));
		assertEquals("banana", list.get(1));
		assertEquals("carrot", list.get(2));
	}

	public void testSetProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(SetPropertyBeanFactory.class);
		final ClassWithSetProperty bean = (ClassWithSetProperty) factory.getBean(BEAN_ID);
		final Set set = bean.getSetProperty();
		assertNotNull(set);
		assertEquals(3, set.size());
		assertTrue("apple", set.contains("apple"));
		assertTrue("banana", set.contains("banana"));
		assertTrue("carrot", set.contains("carrot"));
		assertFalse("dog", set.contains("dog"));
	}

	public void testMapProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(MapPropertyBeanFactory.class);
		final ClassWithMapProperty bean = (ClassWithMapProperty) factory.getBean(BEAN_ID);
		final Map map = bean.getMapProperty();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals("apple=green", "green", map.get("apple"));
		assertEquals("banana=yellow", "yellow", map.get("banana"));
		assertEquals("carrot=orange", "orange", map.get("carrot"));
		assertEquals("dog", null, map.get("dog"));
	}

	public void testManyValues() {
		final BeanFactory factory = (BeanFactory) GWT.create(ManyValuesBeanFactory.class);
		final ClassWithManyValues bean = (ClassWithManyValues) factory.getBean(BEAN_ID);
		assertEquals("green", bean.getApple());
		assertEquals("yellow", bean.getBanana());
		assertEquals("orange", bean.getCarrot());
	}

	public void testBeanReference() {
		final BeanFactory factory = (BeanFactory) GWT.create(BeanReferenceBeanFactory.class);
		final ClassWithBeanReference bean = (ClassWithBeanReference) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		assertNotNull("bean reference was not set", bean.getAnotherBean());
	}

	public void testRemoteRpcService() {
		final BeanFactory factory = (BeanFactory) GWT.create(RemoteRpcServiceBeanFactory.class);
		final RemoteRpcServiceAsync bean = (RemoteRpcServiceAsync) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/remoteRpcService", serviceDefTarget.getServiceEntryPoint().endsWith("/remoteRpcService"));
	}

	public void testRemoteJsonService() {
		final BeanFactory factory = (BeanFactory) GWT.create(RemoteJsonServiceBeanFactory.class);
		final RemoteJsonServiceAsync bean = (RemoteJsonServiceAsync) factory.getBean(BEAN_ID);
		assertNotNull(bean);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) bean;
		assertTrue("/remoteJsonService", serviceDefTarget.getServiceEntryPoint().endsWith("/remoteJsonService"));
	}

	public void testPlaceHolders() {
		final BeanFactory factory = (BeanFactory) GWT.create(PlaceHolderBeanFactory.class);
		final PlaceHolderBean bean = (PlaceHolderBean) factory.getBean(BEAN_ID);
		assertEquals("orange yellow green green", bean.getStringProperty());
	}
}
