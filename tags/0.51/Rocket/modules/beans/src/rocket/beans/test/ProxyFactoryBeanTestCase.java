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
import rocket.beans.client.BeanFactoryAware;
import rocket.beans.client.BeanNameAware;
import rocket.beans.client.FactoryBean;
import rocket.beans.client.InitializingBean;
import rocket.beans.client.PrototypeFactoryBean;
import rocket.beans.client.SingletonFactoryBean;
import rocket.beans.client.UnableToFindBeanException;
import rocket.beans.client.aop.ProxyFactoryBean;

@SuppressWarnings("unchecked")
public class ProxyFactoryBeanTestCase extends TestCase {

	final static String BEAN = "bean";

	public void testGetSingleton() {
		final FactoryBean proxyFactoryBean = this.createSingletonProxyFactoryBean();

		final Target proxy = (Target) proxyFactoryBean.getObject();
		assertNotNull("" + proxy, proxy);
		assertTrue("should be a singleton", proxyFactoryBean.isSingleton());

		assertEquals(123 + 456, proxy.add(123, 456));
	}

	public void testGetSingletonShouldReturnSameProxy() {
		final FactoryBean proxyFactoryBean = this.createSingletonProxyFactoryBean();

		final Target proxy0 = (Target) proxyFactoryBean.getObject();
		assertNotNull("" + proxy0, proxy0);
		assertTrue("should be a singleton", proxyFactoryBean.isSingleton());

		assertEquals(123 + 456, proxy0.add(123, 456));

		final Target proxy1 = (Target) proxyFactoryBean.getObject();
		assertSame("" + proxy1, proxy0, proxy1);

		assertEquals(1234 + 5678, proxy0.add(1234, 5678));
	}

	public void testGetPrototype() {
		final FactoryBean proxyFactoryBean = this.createPrototypeProxyFactoryBean();

		final Target proxy = (Target) proxyFactoryBean.getObject();
		assertNotNull("" + proxy, proxy);
		assertFalse("should NOT be a singleton", proxyFactoryBean.isSingleton());

		assertEquals(123 + 456, proxy.add(123, 456));
	}

	public void testGetPrototypeShouldReturnDifferentProxy() {
		final FactoryBean proxyFactoryBean = this.createPrototypeProxyFactoryBean();

		final Target proxy0 = (Target) proxyFactoryBean.getObject();
		assertNotNull("" + proxy0, proxy0);
		assertFalse("should NOT be a singleton", proxyFactoryBean.isSingleton());

		assertEquals(123 + 456, proxy0.add(123, 456));

		final Target proxy1 = (Target) proxyFactoryBean.getObject();
		assertNotSame("" + proxy1, proxy0, proxy1);

		assertEquals(1234 + 5678, proxy0.add(1234, 5678));
	}

	public void testProxiedBeanFactoryAwareBean() {
		final PrototypeFactoryBean prototypeFactoryBean = new PrototypeFactoryBean() {
			protected Object createInstance() throws Exception {
				return new ImplementsBeanFactoryAware();
			}
		};
		prototypeFactoryBean.setBeanName(BEAN);

		final ProxyFactoryBean proxyFactoryBean = createProxyFactoryBean();
		proxyFactoryBean.setBeanName(BEAN);

		final BeanFactory beanFactory = this.createBeanFactory(false, proxyFactoryBean, prototypeFactoryBean);
		proxyFactoryBean.setBeanFactory(beanFactory);
		prototypeFactoryBean.setBeanFactory(beanFactory);

		final ImplementsBeanFactoryAware bean = (ImplementsBeanFactoryAware) prototypeFactoryBean.getObject();
		assertNotNull(bean.beanFactory);
	}

	static class ImplementsBeanFactoryAware implements BeanFactoryAware, InitializingBean {

		public void afterPropertiesSet() {
			assertNotNull("beanFactory not set", this.beanFactory);
		}

		BeanFactory beanFactory;

		public void setBeanFactory(final BeanFactory beanFactory) {
			this.beanFactory = beanFactory;
		}
	}

	public void testProxiedBeanNameAwareBean() {
		final PrototypeFactoryBean targetFactoryBean = new PrototypeFactoryBean() {
			protected Object createInstance() throws Exception {
				return new ImplementsBeanNameAware();
			}
		};
		targetFactoryBean.setBeanName('$' + BEAN);

		final ProxyFactoryBean proxyFactoryBean = createProxyFactoryBean();

		final BeanFactory beanFactory = this.createBeanFactory(false, proxyFactoryBean, targetFactoryBean);
		targetFactoryBean.setBeanFactory(beanFactory);
		proxyFactoryBean.setBeanFactory(beanFactory);

		final ImplementsBeanNameAware bean = (ImplementsBeanNameAware) proxyFactoryBean.getObject();
		assertEquals('$' + BEAN, bean.beanName);
	}

	static class ImplementsBeanNameAware implements BeanNameAware, InitializingBean {

		public void afterPropertiesSet() {
			assertNotNull("beanName not set", this.beanName);
		}

		String beanName;

		public void setBeanName(final String beanName) {
			this.beanName = beanName;
		}
	}

	ProxyFactoryBean createSingletonProxyFactoryBean() {
		final SingletonFactoryBean singletonFactoryBean = this.createSingletonFactoryBean();

		final ProxyFactoryBean proxyFactoryBean = this.createProxyFactoryBean();

		final BeanFactory beanFactory = this.createBeanFactory(true, proxyFactoryBean, singletonFactoryBean);

		singletonFactoryBean.setBeanFactory(beanFactory);
		proxyFactoryBean.setBeanFactory(beanFactory);
		return proxyFactoryBean;
	}

	ProxyFactoryBean createPrototypeProxyFactoryBean() {
		final PrototypeFactoryBean prototypeFactoryBean = this.createPrototypeFactoryBean();
		final ProxyFactoryBean proxyFactoryBean = this.createProxyFactoryBean();

		final BeanFactory beanFactory = this.createBeanFactory(false, proxyFactoryBean, prototypeFactoryBean);
		prototypeFactoryBean.setBeanFactory(beanFactory);
		proxyFactoryBean.setBeanFactory(beanFactory);
		return proxyFactoryBean;
	}

	ProxyFactoryBean createProxyFactoryBean() {
		final ProxyFactoryBean proxy = new ProxyFactoryBean() {
			protected Object createProxy0(final Object target) {
				return target;
			}
		};
		proxy.setBeanName(BEAN);
		return proxy;
	}

	SingletonFactoryBean createSingletonFactoryBean() {
		return new SingletonFactoryBean<Target>() {
			protected Target createInstance() {
				return new Target();
			}
		};
	}

	PrototypeFactoryBean createPrototypeFactoryBean() {
		return new PrototypeFactoryBean<Target>() {
			protected Target createInstance() {
				return new Target();
			}
		};
	}

	BeanFactory createBeanFactory(final boolean singleton, final FactoryBean proxyFactoryBean, final FactoryBean factoryBean) {
		return new BeanFactory() {
			public Object getBean(final String name) {
				if (name.equals(BEAN)) {
					return proxyFactoryBean.getObject();
				}
				if (name.equals('$' + BEAN)) {
					return factoryBean.getObject();
				}
				throw new UnableToFindBeanException(name);
			}

			public boolean isSingleton(final String name) {
				return singleton;
			}
		};
	}

	/**
	 * A pretend generated proxy for Target. This is achieved by subclassing and
	 * delegating all public methods to the target.
	 */
	static class TargetProxy extends Target {
		Target target;

		public void setTarget(final Object target) {
			this.target = (Target) target;
		}

		public int add(final int a, final int b) {
			return this.target.add(a, b);
		}
	}

	/**
	 * The target of the proxy
	 */
	static class Target {
		public int add(final int a, final int b) {
			return a + b;
		}
	}
}
