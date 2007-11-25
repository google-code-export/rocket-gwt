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
package rocket.beans.client;

import java.util.Iterator;
import java.util.Map;

import rocket.util.client.ObjectHelper;

/**
 * This class contains a number of common properties and methods that will be
 * required by all generated {@link BeanFactory} implementations.
 * 
 * @author Miroslav Pokorny
 */
abstract public class BeanFactoryImpl implements BeanFactory {

	public BeanFactoryImpl() {
		super();

		this.setFactoryBeans(this.buildFactoryBeans());
		this.prepareFactoryBeans();
	}

	/**
	 * Visits all factory beans setting the bean factory for BeanFactoryAware
	 * objects.
	 */
	protected void prepareFactoryBeans() {
		final Iterator factoryBeans = this.getFactoryBeans().values().iterator();
		while (factoryBeans.hasNext()) {
			final Object factoryBean = factoryBeans.next();
			if (factoryBean instanceof BeanFactoryAware) {
				final BeanFactoryAware beanFactoryAware = (BeanFactoryAware) factoryBean;
				beanFactoryAware.setBeanFactory(this);
			}
		}
	}

	/**
	 * This method is implemented by the code generator to create BeanFactory
	 * instances for each bean defined.
	 * 
	 * @return
	 */
	abstract protected Map buildFactoryBeans();

	/**
	 * This map consists of all the bean factories that will return bean
	 * instances.
	 */
	private Map factoryBeans;

	protected Map getFactoryBeans() {
		ObjectHelper.checkNotNull("field:factoryBeans", factoryBeans);
		return this.factoryBeans;
	}

	protected void setFactoryBeans(final Map factoryBeans) {
		ObjectHelper.checkNotNull("parameter:factoryBeans", factoryBeans);
		this.factoryBeans = factoryBeans;
	}

	public Object getBean(String name) {
		Object bean = this.getFactoryBean(name).getObject();
		while (true) {
			if (false == bean instanceof FactoryBean) {
				break;
			}
			final FactoryBean factoryBean = (FactoryBean) bean;
			bean = factoryBean.getObject();
		}
		return bean;
	}

	public boolean isSingleton(String name) {
		return this.getFactoryBean(name).isSingleton();
	}

	/**
	 * Attempts to get the FactoryMethodBean given a name. If the factory is not
	 * found an exception is thrown.
	 * 
	 * @param name
	 * @return
	 * @throws UnableToFindBeanException
	 *             if the bean doesnt exist.
	 */
	protected FactoryBean getFactoryBean(final String name) throws UnableToFindBeanException {
		final FactoryBean factory = (FactoryBean) this.getFactoryBeans().get(name);
		if (null == factory) {
			throwUnableToFindBean("Unable to find bean \"" + name + "\".");
		}
		return factory;
	}

	protected void throwUnableToFindBean(final String message) throws UnableToFindBeanException {
		throw new UnableToFindBeanException(message);
	}
}
