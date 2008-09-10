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
package rocket.beans.test.generator.properties;

import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.beans.client.BeanFactory;
import rocket.beans.test.generator.properties.ambiguous.AmbiguousSettersBeanFactory;
import rocket.beans.test.generator.properties.booleanproperty.BooleanPropertyBeanFactory;
import rocket.beans.test.generator.properties.booleanproperty.HasBooleanProperty;
import rocket.beans.test.generator.properties.byteproperty.BytePropertyBeanFactory;
import rocket.beans.test.generator.properties.byteproperty.HasByteProperty;
import rocket.beans.test.generator.properties.charproperty.CharPropertyBeanFactory;
import rocket.beans.test.generator.properties.charproperty.HasCharProperty;
import rocket.beans.test.generator.properties.doubleproperty.DoublePropertyBeanFactory;
import rocket.beans.test.generator.properties.doubleproperty.HasDoubleProperty;
import rocket.beans.test.generator.properties.floatproperty.FloatPropertyBeanFactory;
import rocket.beans.test.generator.properties.floatproperty.HasFloatProperty;
import rocket.beans.test.generator.properties.intproperty.HasIntProperty;
import rocket.beans.test.generator.properties.intproperty.IntPropertyBeanFactory;
import rocket.beans.test.generator.properties.list.HasListProperty;
import rocket.beans.test.generator.properties.list.ListPropertyBeanFactory;
import rocket.beans.test.generator.properties.longproperty.HasLongProperty;
import rocket.beans.test.generator.properties.longproperty.LongPropertyBeanFactory;
import rocket.beans.test.generator.properties.many.HasManyValues;
import rocket.beans.test.generator.properties.many.ManyValuesBeanFactory;
import rocket.beans.test.generator.properties.map.HasMapProperty;
import rocket.beans.test.generator.properties.map.MapPropertyBeanFactory;
import rocket.beans.test.generator.properties.nullliteral.NullLiteralBeanFactory;
import rocket.beans.test.generator.properties.nullliteral.NullTestBean;
import rocket.beans.test.generator.properties.rpc.HasGwtRpcService;
import rocket.beans.test.generator.properties.rpc.RpcPropertyBeanFactory;
import rocket.beans.test.generator.properties.set.HasSetProperty;
import rocket.beans.test.generator.properties.set.SetPropertyBeanFactory;
import rocket.beans.test.generator.properties.shortproperty.HasShortProperty;
import rocket.beans.test.generator.properties.shortproperty.ShortPropertyBeanFactory;
import rocket.beans.test.generator.properties.string.HasStringProperty;
import rocket.beans.test.generator.properties.string.StringPropertyBeanFactory;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorPropertiesGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	public String getModuleName() {
		return "rocket.beans.test.generator.properties.BeanFactoryGeneratorProperties";
	}

	public void testAmbiguousPropertySetters() {
		try {
			assertBindingFailed(GWT.create(AmbiguousSettersBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testNullLiteral() {
		final BeanFactory factory = (BeanFactory) GWT.create(NullLiteralBeanFactory.class);
		final NullTestBean bean = (NullTestBean) factory.getBean(BEAN);
		assertNull(bean.getProperty());
	}

	public void testBooleanProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BooleanPropertyBeanFactory.class);
		final HasBooleanProperty bean = (HasBooleanProperty) factory.getBean(BEAN);
		assertTrue(bean.getBooleanProperty());
	}

	public void testByteProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(BytePropertyBeanFactory.class);
		final HasByteProperty bean = (HasByteProperty) factory.getBean(BEAN);
		assertEquals(123, bean.getByteProperty());
	}

	public void testShortProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(ShortPropertyBeanFactory.class);
		final HasShortProperty bean = (HasShortProperty) factory.getBean(BEAN);
		assertEquals(123, bean.getShortProperty());
	}

	public void testIntProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(IntPropertyBeanFactory.class);
		final HasIntProperty bean = (HasIntProperty) factory.getBean(BEAN);
		assertEquals(123, bean.getIntProperty());
	}

	public void testLongProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(LongPropertyBeanFactory.class);
		final HasLongProperty bean = (HasLongProperty) factory.getBean(BEAN);
		assertEquals(123, bean.getLongProperty());
	}

	public void testFloatProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(FloatPropertyBeanFactory.class);
		final HasFloatProperty bean = (HasFloatProperty) factory.getBean(BEAN);
		assertEquals(123.45f, bean.getFloatProperty(), 0.01f);
	}

	public void testDoubleProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(DoublePropertyBeanFactory.class);
		final HasDoubleProperty bean = (HasDoubleProperty) factory.getBean(BEAN);
		assertEquals(123.45, bean.getDoubleProperty(), 0.01);
	}

	public void testCharProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(CharPropertyBeanFactory.class);
		final HasCharProperty bean = (HasCharProperty) factory.getBean(BEAN);
		assertEquals('a', bean.getCharProperty());
	}

	public void testStringProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(StringPropertyBeanFactory.class);
		final HasStringProperty bean = (HasStringProperty) factory.getBean(BEAN);
		assertEquals("apple", bean.getStringProperty());
	}

	public void testListProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(ListPropertyBeanFactory.class);
		final HasListProperty bean = (HasListProperty) factory.getBean(BEAN);
		final List list = bean.getListProperty();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("apple", list.get(0));
		assertEquals("banana", list.get(1));
		assertEquals("carrot", list.get(2));
	}

	public void testSetProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(SetPropertyBeanFactory.class);
		final HasSetProperty bean = (HasSetProperty) factory.getBean(BEAN);
		final Set<String> set = bean.getSetProperty();
		assertNotNull(set);
		assertEquals(3, set.size());
		assertTrue("apple", set.contains("apple"));
		assertTrue("banana", set.contains("banana"));
		assertTrue("carrot", set.contains("carrot"));
		assertFalse("dog", set.contains("dog"));
	}

	public void testMapProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(MapPropertyBeanFactory.class);
		final HasMapProperty bean = (HasMapProperty) factory.getBean(BEAN);
		final Map map = bean.getMapProperty();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals("apple=green", "green", map.get("apple"));
		assertEquals("banana=yellow", "yellow", map.get("banana"));
		assertEquals("carrot=orange", "orange", map.get("carrot"));
		assertEquals("dog", null, map.get("dog"));
	}

	public void testManyProperties() {
		final BeanFactory factory = (BeanFactory) GWT.create(ManyValuesBeanFactory.class);
		final HasManyValues bean = (HasManyValues) factory.getBean(BEAN);
		assertEquals("green", bean.getApple());
		assertEquals("yellow", bean.getBanana());
		assertEquals("orange", bean.getCarrot());
	}

	public void testBeanGwtRpcProperty() {
		final BeanFactory factory = (BeanFactory) GWT.create(RpcPropertyBeanFactory.class);
		final HasGwtRpcService bean = (HasGwtRpcService) factory.getBean(BEAN);
		assertNotNull(bean);

		final Object rpc = bean.getRpc();
		assertNotNull("rpc", rpc);

		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) rpc;
		assertTrue("/rpc", serviceDefTarget.getServiceEntryPoint().endsWith("/rpc"));
	}

}
