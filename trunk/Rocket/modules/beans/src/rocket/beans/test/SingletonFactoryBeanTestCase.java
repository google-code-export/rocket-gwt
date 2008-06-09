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
import rocket.beans.client.DisposableBean;
import rocket.beans.client.InitializingBean;
import rocket.beans.client.PrototypeFactoryBean;
import rocket.beans.client.SingletonFactoryBean;

/**
 * A set of unit tests for a SingleFactoryBean.
 * 
 * @author Miroslav Pokorny
 */
public class SingletonFactoryBeanTestCase extends TestCase {

	final static String STRING_VALUE = "apple";

	public void testGetBean() {
		final ClassWithStringPropertySingletonFactoryBean factoryBean = new ClassWithStringPropertySingletonFactoryBean();
		final Object bean = factoryBean.getObject();
		assertTrue("" + bean, bean instanceof ClassWithStringProperty);

		final ClassWithStringProperty bean0 = (ClassWithStringProperty) bean;
		assertEquals("stringProperty", STRING_VALUE, bean0.getStringProperty());
	}

	public void testGetSingletonBeanAlwaysReturnsSameInstance() {
		final ClassWithStringPropertySingletonFactoryBean factoryBean = new ClassWithStringPropertySingletonFactoryBean();
		final Object first = factoryBean.getObject();
		final Object second = factoryBean.getObject();
		assertSame(first, second);
	}

	static class ClassWithStringPropertySingletonFactoryBean extends SingletonFactoryBean<ClassWithStringProperty> {

		protected ClassWithStringProperty createInstance() {
			return new ClassWithStringProperty();
		}

		protected void satisfyProperties(ClassWithStringProperty instance) {
			this.satisfyProperties0( instance);
		}

		protected void satisfyProperties0(final ClassWithStringProperty instance) {
			instance.setStringProperty(STRING_VALUE);
		}

		protected void satisfyInit(final ClassWithStringProperty instance) {
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

	static class ClassWithAnotherBeanReferenceSingleFactoryBean extends SingletonFactoryBean<ClassWithAnotherBeanReference> {
		protected ClassWithAnotherBeanReference createInstance() {
			return new ClassWithAnotherBeanReference();
		}

		protected void satisfyProperties(ClassWithAnotherBeanReference instance) {
			instance.setClassWithStringProperty((ClassWithStringProperty) this.getBeanFactory().getBean("classWithStringProperty"));
		}

		protected void satisfyInit(final ClassWithAnotherBeanReference instance) {
		}

		protected BeanFactory getBeanFactory() {
			return new BeanFactory() {
				public Object getBean(String name) {
					if (false == name.equals("classWithStringProperty")) {
						throw new UnsupportedOperationException("Unknown bean \"" + name + "\".");
					}
					return new ClassWithStringPropertySingletonFactoryBean().getObject();
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

	public void testSingletonFactoryBeanHoldingAnotherSingletonFactoryBean() {
		final SingletonFactoryBean<Bean> factoryBean = new SingletonFactoryBean() {
			@Override
			protected Object createInstance() {
				return new SingletonFactoryBean<Bean>() {
					protected Bean createInstance() {
						return new Bean();
					}
				};
			}
		};
		factoryBean.setBeanFactory(this.createBeanFactory());
		factoryBean.setBeanName("bean");

		final Object bean = factoryBean.getObject();
		assertNotNull(bean);
		assertTrue("" + bean, bean instanceof Bean);

		final Object secondBean = factoryBean.getObject();
		assertSame(bean, secondBean);
	}

	public void testSingletonFactoryBeanHoldingAnotherPrototypeFactoryBean() {
		final SingletonFactoryBean factoryBean = new SingletonFactoryBean() {
			@Override
			protected Object createInstance() {
				return new PrototypeFactoryBean<Bean>() {
					
					@Override
					protected Bean createInstance() {
						return new Bean();
					}
				};
			}
		};
		factoryBean.setBeanFactory(this.createBeanFactory());
		factoryBean.setBeanName("bean");

		final Object bean = factoryBean.getObject();
		assertNotNull(bean);
		assertTrue("" + bean, bean instanceof Bean);

		final Object secondBean = factoryBean.getObject();
		assertSame(bean, secondBean);
	}

	public void testInitializingBean() {
		final SingletonFactoryBean<ImplementsInitializingBean> factoryBean = new SingletonFactoryBean<ImplementsInitializingBean>() {
			
			@Override
			protected ImplementsInitializingBean createInstance() {
				return new ImplementsInitializingBean();
			}
		};
		factoryBean.setBeanFactory(this.createBeanFactory());
		factoryBean.setBeanName("bean");

		final ImplementsInitializingBean bean = (ImplementsInitializingBean) factoryBean.getObject();
		assertEquals(1, bean.initialized);
	}

	static class ImplementsInitializingBean implements InitializingBean {
		public void afterPropertiesSet() {
			this.initialized++;
		}

		int initialized = 0;
	}

	public void testDisposableBean() {
		final SingletonFactoryBean<ImplementsDisposableBean> factoryBean = new SingletonFactoryBean<ImplementsDisposableBean>() {
			protected ImplementsDisposableBean createInstance() {
				return new ImplementsDisposableBean();
			}
		};
		factoryBean.setBeanFactory(this.createBeanFactory());
		factoryBean.setBeanName("bean");

		final ImplementsDisposableBean bean = (ImplementsDisposableBean) factoryBean.getObject();

		factoryBean.destroy();

		assertEquals(1, bean.destroyed);
	}

	static class ImplementsDisposableBean implements DisposableBean {
		public void destroy() {
			this.destroyed++;
		}

		int destroyed = 0;
	}

	BeanFactory createBeanFactory() {
		return new BeanFactory() {
			public Object getBean(final String name) {
				throw new UnsupportedOperationException();
			}

			public boolean isSingleton(final String name) {
				throw new UnsupportedOperationException();
			}
		};
	}

	static class Bean {
	}
}
