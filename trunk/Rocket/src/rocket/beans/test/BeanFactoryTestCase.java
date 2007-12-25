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
import rocket.beans.client.BeanNameAware;
import rocket.beans.client.FactoryBean;
import rocket.beans.client.InitializingBean;
import rocket.beans.client.PrototypeFactoryBean;
import rocket.beans.client.SingletonFactoryBean;

/**
 * A collection of tests for a BeanFactory which mostly involve overriding the various abstract methods of {@link BeanFactoryImpl} just as would be
 * done by the {@link rocket.beans.rebind.BeanFactoryGenerator}.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryTestCase extends TestCase {
	static final String SINGLETON_BEAN = "singletonBean";

	static final String PROTOTYPE_BEAN = "prototypeBean";
		
	public void testGetSingletonBean() {
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(SINGLETON_BEAN, createSingletonFactoryBean());
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
			}
		};
		
		final Object bean = beanFactory.getBean(SINGLETON_BEAN);
		assertTrue("" + bean, bean instanceof Singleton);
	}

	public void testAlias() {
		final String ALIAS = "alias";
		final String ALIAS2 = "alias2";
		
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(SINGLETON_BEAN, createSingletonFactoryBean());
			}
			protected String getAliasesToBeans(){
				return ALIAS + "=" + SINGLETON_BEAN + "," + ALIAS2 + "=" + SINGLETON_BEAN;
			}
			
			protected String getEagerSingletonBeanNames(){
				return "";
			}
		};
		
		final Object bean = beanFactory.getBean(SINGLETON_BEAN);
		assertTrue("" + bean, bean instanceof Singleton);
		
		final Object alias = beanFactory.getBean( ALIAS );
		assertSame( bean, alias );
		
		final Object alias2 = beanFactory.getBean( ALIAS2 );
		assertSame( bean, alias2 );
	}
	
	public void testLazyLoadedSingletonBean() {
		Singleton.loaded = false;
		
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(SINGLETON_BEAN, createSingletonFactoryBean());
			}
			protected String getAliasesToBeans(){
				return "";
			}
			
			protected String getEagerSingletonBeanNames(){
				return "";
			}
		};
		assertFalse( "The lazy singleton bean should NOT have been loaded", Singleton.loaded );
		
		final Object bean = beanFactory.getBean(SINGLETON_BEAN);
		assertTrue("" + bean, bean instanceof Singleton);
	}
	
	public void testEagerlyLoadedSingletonBean() {
		Singleton.loaded = false;
		
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(SINGLETON_BEAN, createSingletonFactoryBean());
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return SINGLETON_BEAN;
			}
		};		
		assertTrue( "An eaglerly loaded singleton bean should have been loaded", Singleton.loaded );
		
		final Object bean = beanFactory.getBean(SINGLETON_BEAN);
		assertTrue("" + bean, bean instanceof Singleton);
	}

	public void testIfASingletonIsSingleton() {
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(SINGLETON_BEAN, createSingletonFactoryBean());
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
			}
		};			
		assertTrue(beanFactory.isSingleton(SINGLETON_BEAN));
	}

	public void testIfAPrototypeIsSingleton() {
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(PROTOTYPE_BEAN, createPrototypeFactoryBean());
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
			}
		};			
		assertFalse(beanFactory.isSingleton(PROTOTYPE_BEAN));
	}

	public void testRatherThanReturningAFactoryBeanCallItsGetObject() {
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(PROTOTYPE_BEAN, createPrototypeFactoryBean());
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
			}
		};			
		
		final Object bean = beanFactory.getBean(PROTOTYPE_BEAN);
		assertTrue("" + bean, bean instanceof Prototype);
	}

	public void testBeanFactoryAware() {
		final String BEAN_FACTORY_AWARE = "bean";
		
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(BEAN_FACTORY_AWARE, new SingletonFactoryBean() {
					public Object createInstance() {
						return new ImplementsBeanFactoryAware();
					}

					protected void satisfyProperties(Object instance) {
					}
				});
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
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
	
	public void testBeanNameAwareBean() {
		final String BEAN_NAME_AWARE_BEAN = "bean";
		
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(BEAN_NAME_AWARE_BEAN, new SingletonFactoryBean() {
					public Object createInstance() {
						return new ImplementsBeanNameAwareBean();
					}

					protected void satisfyProperties(Object instance) {
					}
				});
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
			}
		};			
		
		final ImplementsBeanNameAwareBean bean = (ImplementsBeanNameAwareBean) beanFactory.getBean(BEAN_NAME_AWARE_BEAN);
		assertEquals( BEAN_NAME_AWARE_BEAN, bean.beanName );		
	}

	static class ImplementsBeanNameAwareBean implements BeanNameAware {	
		String beanName;
		
		public void setBeanName( final String beanName ){
			this.beanName=beanName;
		}
	}
	
	public void testBeanWithReferenceToAnotherBean() {
		final String INCLUDES_ANOTHER_BEAN = "IncludesAnotherBean";
		
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				final BeanFactory that = this;
				
				this.registerFactoryBean(INCLUDES_ANOTHER_BEAN, new SingletonFactoryBean() {
					public Object createInstance() {
						return new SingletonFactoryBean() {
							protected Object createInstance() {
								return new IncludesAnotherBean();
							}

							protected void satisfyProperties(final Object instance) {
								final IncludesAnotherBean instance0 = (IncludesAnotherBean) instance;
								instance0.anotherBean = (Singleton) that.getBean(SINGLETON_BEAN);
							}
						};
					}

					protected void satisfyProperties(Object instance) {
					}
				});
				this.registerFactoryBean(SINGLETON_BEAN, createSingletonFactoryBean());
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
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
		
		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(INITIALIZING_BEAN, new SingletonFactoryBean() {
					public Object createInstance() {
						return new ImplementsInitializingBean();
					}

					protected void satisfyProperties(Object instance) {
					}
				});
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
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

		final BeanFactory beanFactory = new TestBeanFactoryImpl(){
			protected void registerFactoryBeans(){
				this.registerFactoryBean(CYCLE1, new SingletonFactoryBean() {
					public Object createInstance() {
						return new CycleSingleton1();
					}

					protected void satisfyProperties(Object instance) {
						final CycleSingleton1 instance0 = (CycleSingleton1) instance;
						instance0.otherCycleSingleton2 = (CycleSingleton2) this.getBeanFactory().getBean(CYCLE2);						
					}
				});
				this.registerFactoryBean(CYCLE2, new SingletonFactoryBean() {
					public Object createInstance() {
						return new CycleSingleton2();
					}

					protected void satisfyProperties(Object instance) {
						final CycleSingleton2 instance0 = (CycleSingleton2) instance;
						instance0.otherCycleSingleton1 = (CycleSingleton1) this.getBeanFactory().getBean(CYCLE1);						
					}
				});
			}
			protected String getAliasesToBeans(){
				return "";
			}
			protected String getEagerSingletonBeanNames(){
				return "";
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
	
	static abstract class TestBeanFactoryImpl extends BeanFactoryImpl{
		/**
		 * Overrides the real method which has dependencies on GWT.
		 */
		protected void registerShutdownHook(){				
		}
	}
}
