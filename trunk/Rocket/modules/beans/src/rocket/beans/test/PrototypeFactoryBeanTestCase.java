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

import junit.framework.TestCase;
import rocket.beans.client.BeanFactory;
import rocket.beans.client.InitializingBean;
import rocket.beans.client.PrototypeFactoryBean;

/**
 * A set of unit tests for a SingleFactoryBean.
 * 
 * @author Miroslav Pokorny
 */
public class PrototypeFactoryBeanTestCase extends TestCase {

	final static String STRING_VALUE = "apple";

	public void testGetBean() {
		final ClassWithStringPropertyPrototypeFactoryBean factoryBean = new ClassWithStringPropertyPrototypeFactoryBean();
		final Object bean = factoryBean.getObject();
		assertTrue("" + bean, bean instanceof ClassWithStringProperty);

		final ClassWithStringProperty bean0 = (ClassWithStringProperty) bean;
		assertEquals("stringProperty", STRING_VALUE, bean0.getStringProperty());
	}

	public void testGetBeanReturnsDifferentInstance() {
		final ClassWithStringPropertyPrototypeFactoryBean factoryBean = new ClassWithStringPropertyPrototypeFactoryBean();
		final Object first = factoryBean.getObject();
		final Object second = factoryBean.getObject();
		assertNotSame(first, second);
	}

	static class ClassWithStringPropertyPrototypeFactoryBean extends PrototypeFactoryBean {

		protected Object createInstance() {
			return new ClassWithStringProperty();
		}

		protected void satisfyProperties(Object instance) {
			this.satisfyProperties0((ClassWithStringProperty) instance);
		}

		protected void satisfyProperties0(final ClassWithStringProperty instance) {
			instance.setStringProperty(STRING_VALUE);
		}

		protected void satisfyInit(final Object instance) {
		}
	}

	static class ClassWithStringProperty {
		private String stringProperty;

		public String getStringProperty() {
			return this.stringProperty;
		}

		public void setStringProperty(final String stringProperty) {
			this.stringProperty = stringProperty;
		}
	}

	static class ClassWithAnotherBeanReferenceSingleFactoryBean extends PrototypeFactoryBean {
		protected Object createInstance() {
			return new ClassWithAnotherBeanReference();
		}

		protected void satisfyProperties(Object instance) {
			final ClassWithAnotherBeanReference instance0 = (ClassWithAnotherBeanReference) instance;
			instance0.setClassWithStringProperty((ClassWithStringProperty) this.getBeanFactory().getBean("classWithStringProperty"));
		}

		protected void satisfyInit(final Object instance) {
		}

		protected BeanFactory getBeanFactory() {
			return new BeanFactory() {
				public Object getBean(String name) {
					if (false == name.equals("classWithStringProperty")) {
						throw new UnsupportedOperationException("Unknown bean \"" + name + "\".");
					}
					return new ClassWithStringPropertyPrototypeFactoryBean().getObject();
				}

				public boolean isSingleton(String name) {
					throw new UnsupportedOperationException();
				}
			};
		}
	}

	static class ClassWithAnotherBeanReference {
		private ClassWithStringProperty classWithStringProperty;

		public ClassWithStringProperty getClassWithStringProperty() {
			return this.classWithStringProperty;
		}

		public void setClassWithStringProperty(final ClassWithStringProperty classWithStringProperty) {
			this.classWithStringProperty = classWithStringProperty;
		}
	}

	public void testInitializingBean() {
		final PrototypeFactoryBean factoryBean = new PrototypeFactoryBean() {
			protected Object createInstance() {
				return new ImplementsInitializingBean();
			}
		};
		final ImplementsInitializingBean bean = (ImplementsInitializingBean) factoryBean.getObject();
		assertEquals(1, bean.initialized);
	}

	static class ImplementsInitializingBean implements InitializingBean {
		public void afterPropertiesSet() {
			this.initialized++;
		}

		int initialized = 0;
	}
}
