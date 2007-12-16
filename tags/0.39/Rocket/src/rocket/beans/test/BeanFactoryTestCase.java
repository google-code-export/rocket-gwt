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
import rocket.util.client.StringHelper;

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
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(SINGLETON, createSingletonFactoryBean());
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[0];
			}
		};
		
		final Object bean = beanFactory.getBean(SINGLETON);
		assertTrue("" + bean, bean instanceof Singleton);
	}

	public void testLazyLoadedSingletonBean() {
		Singleton.loaded = false;
		
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(SINGLETON, createSingletonFactoryBean());
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[0];
			}
		};
		assertFalse( "The lazy singleton bean should NOT have been loaded", Singleton.loaded );
		
		final Object bean = beanFactory.getBean(SINGLETON);
		assertTrue("" + bean, bean instanceof Singleton);
	}
	
	public void testEagerlyLoadedSingletonBean() {
		Singleton.loaded = false;
		
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(SINGLETON, createSingletonFactoryBean());
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[] { SINGLETON };
			}
		};		
		assertTrue( "An eaglerly loaded singleton bean should have been loaded", Singleton.loaded );
		
		final Object bean = beanFactory.getBean(SINGLETON);
		assertTrue("" + bean, bean instanceof Singleton);
	}

	public void testIfASingletonIsSingleton() {
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(SINGLETON, createSingletonFactoryBean());
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[ 0 ];
			}
		};			
		assertTrue(beanFactory.isSingleton(SINGLETON));
	}

	public void testIfAPrototypeIsSingleton() {
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(PROTOTYPE, createPrototypeFactoryBean());
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[ 0 ];
			}
		};			
		assertFalse(beanFactory.isSingleton(PROTOTYPE));
	}

	public void testRatherThanReturningAFactoryBeanCallItsGetObject() {
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(PROTOTYPE, createPrototypeFactoryBean());
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[ 0 ];
			}
		};			
		
		final Object bean = beanFactory.getBean(PROTOTYPE);
		assertTrue("" + bean, bean instanceof Prototype);
	}

	public void testBeanFactoryAware() {
		final String BEAN_FACTORY_AWARE = "bean";
		
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(BEAN_FACTORY_AWARE, new SingletonFactoryBean() {
					public Object createInstance() {
						return new ImplementsBeanFactoryAware();
					}

					protected void satisfyProperties(Object instance) {
					}
				});
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[ 0 ];
			}
		};						
		
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
	
	public void testBeanWithReferenceToAnotherBean() {
		final String INCLUDES_ANOTHER_BEAN = "IncludesAnotherBean";
		
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final BeanFactory that = this;
				
				final Map map = new HashMap();
				map.put(INCLUDES_ANOTHER_BEAN, new SingletonFactoryBean() {
					public Object createInstance() {
						return new SingletonFactoryBean() {
							protected Object createInstance() {
								return new IncludesAnotherBean();
							}

							protected void satisfyProperties(final Object instance) {
								final IncludesAnotherBean instance0 = (IncludesAnotherBean) instance;
								instance0.anotherBean = (Singleton) that.getBean(SINGLETON);
							}
						};
					}

					protected void satisfyProperties(Object instance) {
					}
				});
				map.put(SINGLETON, createSingletonFactoryBean());
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[ 0 ];
			}
		};						
		
		final Object bean = beanFactory.getBean(INCLUDES_ANOTHER_BEAN);
		assertTrue("" + bean, bean instanceof IncludesAnotherBean);
	}

	static class IncludesAnotherBean{
		Singleton anotherBean;
	}

	public void testInitializingBean() {
		final String INITIALIZING_BEAN = "bean";
		
		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(INITIALIZING_BEAN, new SingletonFactoryBean() {
					public Object createInstance() {
						return new ImplementsInitializingBean();
					}

					protected void satisfyProperties(Object instance) {
					}
				});
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[ 0 ];
			}
		};			
		
		final ImplementsInitializingBean bean = (ImplementsInitializingBean) beanFactory.getBean(INITIALIZING_BEAN);
		assertTrue("" + bean, bean.initialized);
	}

	static class ImplementsInitializingBean implements InitializingBean {
		public void afterPropertiesSet() {
			this.initialized = true;
		}

		boolean initialized = false;
	}

	public void testCycle() {
		final String CYCLE1 = "cycle1";
		final String CYCLE2 = "cycle2";

		final BeanFactory beanFactory = new BeanFactoryImpl(){
			protected Map buildFactoryBeans(){
				final Map map = new HashMap();
				map.put(CYCLE1, new SingletonFactoryBean() {
					public Object createInstance() {
						return new CycleSingleton1();
					}

					protected void satisfyProperties(Object instance) {
						final CycleSingleton1 instance0 = (CycleSingleton1) instance;
						instance0.otherCycleSingleton2 = (CycleSingleton2) this.getBeanFactory().getBean(CYCLE2);						
					}
				});
				map.put(CYCLE2, new SingletonFactoryBean() {
					public Object createInstance() {
						return new CycleSingleton2();
					}

					protected void satisfyProperties(Object instance) {
						final CycleSingleton2 instance0 = (CycleSingleton2) instance;
						instance0.otherCycleSingleton1 = (CycleSingleton1) this.getBeanFactory().getBean(CYCLE1);						
					}
				});
				return map;
			}
			protected String[] getEagerSingletonBeanNames(){
				return new String[ 0 ];
			}
		};		
		
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
		
		static public boolean loaded = false;
		
		Singleton(){
			super();
			loaded = true;
		}
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
