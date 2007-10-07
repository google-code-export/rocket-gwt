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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import rocket.beans.client.BeanFactory;
import rocket.beans.client.BeanFactoryAware;
import rocket.beans.client.BeanFactoryImpl;
import rocket.beans.client.FactoryBean;
import rocket.beans.client.InitializingBean;
import rocket.beans.client.PrototypeFactoryBean;
import rocket.beans.client.SingletonFactoryBean;

/**
 * A collection of tests for a BeanFactory
 * 
 * @author Miroslav Pokorny
 * 
 */
public class BeanFactoryTestCase extends TestCase {
	static final String SINGLETON = "singleton";

	static final String PROTOTYPE = "prototype";

	public void testGetSingletonBean() {
		final TestBeanFactoryImpl beanFactory = new TestBeanFactoryImpl();
		beanFactory.addBean(SINGLETON, createSingletonFactoryBean());

		final Object bean = beanFactory.getBean(SINGLETON);
		assertTrue("" + bean, bean instanceof Singleton);
	}

	public void testIsSingletonForSingleton() {
		final TestBeanFactoryImpl beanFactory = new TestBeanFactoryImpl();
		beanFactory.addBean(SINGLETON, createSingletonFactoryBean());
		assertTrue(beanFactory.isSingleton(SINGLETON));
	}

	public void testIsSingletonForPrototype() {
		final TestBeanFactoryImpl beanFactory = new TestBeanFactoryImpl();
		beanFactory.addBean(PROTOTYPE, createPrototypeFactoryBean());
		assertFalse(beanFactory.isSingleton(PROTOTYPE));
	}

	public void testRatherThanReturningAFactoryBeanCallItsGetObject() {
		final TestBeanFactoryImpl beanFactory = new TestBeanFactoryImpl();
		beanFactory.addBean(PROTOTYPE, new SingletonFactoryBean() {
			public Object createInstance() {
				return createPrototypeFactoryBean();
			}

			protected void satisfyProperties(Object instance) {
			}
		});

		final Object bean = beanFactory.getBean(PROTOTYPE);
		assertTrue("" + bean, bean instanceof Prototype);
	}

	public void testBeanWithReferenceToAnotherBean() {
		final String INCLUDES_ANOTHER_BEAN = "IncludesAnotherBean";

		final TestBeanFactoryImpl beanFactory = new TestBeanFactoryImpl();

		beanFactory.addBean(INCLUDES_ANOTHER_BEAN, new SingletonFactoryBean() {
			public Object createInstance() {
				return new SingletonFactoryBean() {
					protected Object createInstance() {
						return new IncludesAnotherBean();
					}

					protected void satisfyProperties(Object instance) {
						final IncludesAnotherBean instance0 = (IncludesAnotherBean) instance;
						instance0.anotherBean = (Singleton) beanFactory.getBean(SINGLETON);
					}
				};
			}

			protected void satisfyProperties(Object instance) {
			}
		});
		beanFactory.addBean(SINGLETON, createSingletonFactoryBean());

		final Object bean = beanFactory.getBean(INCLUDES_ANOTHER_BEAN);
		assertTrue("" + bean, bean instanceof IncludesAnotherBean);
	}

	static class IncludesAnotherBean {
		Singleton anotherBean;
	}

	public void testInitializingBean() {
		final String INITIALIZING_BEAN = "bean";
		final TestBeanFactoryImpl beanFactory = new TestBeanFactoryImpl();

		beanFactory.addBean(INITIALIZING_BEAN, new SingletonFactoryBean() {
			public Object createInstance() {
				return new ImplementsInitializingBean();
			}

			protected void satisfyProperties(Object instance) {
			}
		});

		final ImplementsInitializingBean bean = (ImplementsInitializingBean) beanFactory.getBean(INITIALIZING_BEAN);
		assertTrue("" + bean, bean.initialized);
	}

	static class ImplementsInitializingBean implements InitializingBean {
		public void afterPropertiesSet() {
			this.initialized = true;
		}

		boolean initialized = false;
	}

	public void testBeanFactoryAware() {
		final String BEAN_FACTORY_AWARE = "bean";
		final TestBeanFactoryImpl beanFactory = new TestBeanFactoryImpl();

		beanFactory.addBean(BEAN_FACTORY_AWARE, new SingletonFactoryBean() {
			public Object createInstance() {
				return new ImplementsBeanFactoryAware();
			}

			protected void satisfyProperties(Object instance) {
			}
		});

		final ImplementsBeanFactoryAware bean = (ImplementsBeanFactoryAware) beanFactory.getBean(BEAN_FACTORY_AWARE);
		assertNotNull("" + bean, bean.beanFactory);
	}

	static class ImplementsBeanFactoryAware implements BeanFactoryAware, InitializingBean {
		BeanFactory beanFactory;

		public void setBeanFactory(final BeanFactory beanFactory) {
			this.beanFactory = beanFactory;
		}

		public void afterPropertiesSet() {
			assertNotNull("beanFactory properties", beanFactory);
		}
	}

	public void testCycle() {
		final TestBeanFactoryImpl beanFactory = new TestBeanFactoryImpl();
		final String CYCLE1 = "cycle1";
		final String CYCLE2 = "cycle2";

		beanFactory.addBean(CYCLE1, new SingletonFactoryBean() {
			public Object createInstance() {
				return new CycleSingleton1();
			}

			protected void satisfyProperties(Object instance) {
				final CycleSingleton1 instance0 = (CycleSingleton1) instance;
				instance0.otherCycleSingleton2 = (CycleSingleton2) this.getBeanFactory().getBean(CYCLE2);
			}
		});
		beanFactory.addBean(CYCLE2, new SingletonFactoryBean() {
			public Object createInstance() {
				return new CycleSingleton2();
			}

			protected void satisfyProperties(Object instance) {
				final CycleSingleton2 instance0 = (CycleSingleton2) instance;
				instance0.otherCycleSingleton1 = (CycleSingleton1) this.getBeanFactory().getBean(CYCLE1);
			}
		});

		final CycleSingleton1 cycle1 = (CycleSingleton1) beanFactory.getBean(CYCLE1);
		assertNotNull(cycle1.otherCycleSingleton2);

		final CycleSingleton2 cycle2 = (CycleSingleton2) beanFactory.getBean(CYCLE2);
		assertNotNull(cycle2.otherCycleSingleton1);
	}

	static class CycleSingleton1 extends Singleton {
		public CycleSingleton2 otherCycleSingleton2;
	}

	static class CycleSingleton2 extends Singleton {
		public CycleSingleton1 otherCycleSingleton1;
	}

	static class TestBeanFactoryImpl extends BeanFactoryImpl {
		protected Map buildFactoryBeans() {
			return new HashMap();
		}

		public void addBean(final String id, final FactoryBean factoryBean) {
			this.getFactoryBeans().put(id, factoryBean);

			final BeanFactoryAware beanFactoryAware = (BeanFactoryAware) factoryBean;
			beanFactoryAware.setBeanFactory(this);
		}
	}

	static FactoryBean createSingletonFactoryBean() {
		return new SingletonFactoryBean() {
			protected Object createInstance() {
				return new Singleton();
			}

			protected void satisfyProperties(Object instance) {
			}
		};
	}

	static class Singleton {
	}

	static FactoryBean createPrototypeFactoryBean() {
		return new PrototypeFactoryBean() {
			protected Object createInstance() {
				return new Prototype();
			}

			protected void satisfyProperties(Object instance) {
			}
		};
	}

	static class Prototype {
	}
}
