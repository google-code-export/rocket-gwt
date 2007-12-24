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

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Convenient base class for both Singleton and prototype bean factories. A few
 * template methods remain outstanding and will be implemented by the code
 * generator
 * 
 * @author Miroslav Pokorny
 */
abstract public class SingletonOrPrototypeFactoryBean implements BeanFactoryAware, BeanNameAware {
	
	/**
	 * Creates a new bean instance.
	 * 
	 * @return The new instance
	 * @throws Exception
	 *             any exception thrown whilst creating a new instance.
	 */
	protected Object createObject() throws Exception {
		return this.createInstance();
	}

	protected void postCreate(final Object instance) throws Exception {
		this.satisfyBeanFactoryAwareIfNecessary(instance);
		this.satisfyBeanNameAwareIfNecessary(instance);
		this.satisfyProperties(instance);
		this.satisfyInit(instance);
	}

	protected void throwBeanException(final String message, final Throwable cause) {
		throw new BeanException(message, cause);
	}

	/**
	 * Factory method that creates a new bean instance.
	 * 
	 * @return The new instance
	 * @throws Exception
	 *             any exception thrown when creating a new instance.
	 */
	abstract protected Object createInstance() throws Exception;

	/**
	 * This method must be implemented by sub-classes if the bean has properties
	 * that must be set.
	 * 
	 * Typically the code generator will override this method to invoke the
	 * appropriate setters on the given instance after casting it to its most
	 * derived type. Properties that are primitive values and Strings will be
	 * set using literals whilst other values will be set via either a bean
	 * factory or another factory method ( for sets, lists and maps ).
	 * 
	 * @param instance
	 *            The object or bean being created.
	 * @throws Exception
	 *             any exception thrown by a setter.
	 */
	protected void satisfyProperties(final Object instance) throws Exception {
	}

	/**
	 * If the new instance is a BeanFactoryAware call its
	 * {@link BeanFactoryAware#setBeanFactory(BeanFactory)} otherwise do
	 * nothing.
	 * 
	 * @param instance
	 */
	protected void satisfyBeanFactoryAwareIfNecessary(Object instance) {
		ObjectHelper.checkNotNull("parameter:instance", instance);

		if (instance instanceof BeanFactoryAware) {
			final BeanFactoryAware aware = (BeanFactoryAware) instance;
			aware.setBeanFactory(this.getBeanFactory());
		}
	}

	/**
	 * If the new instance is a BeanNameAware call its
	 * {@link BeanNameAware#setBeanName(BeanName)} otherwise do
	 * nothing.
	 * 
	 * @param instance
	 */
	protected void satisfyBeanNameAwareIfNecessary(Object instance) {
		ObjectHelper.checkNotNull("parameter:instance", instance);

		if (instance instanceof BeanNameAware) {
			final BeanNameAware aware = (BeanNameAware) instance;
			aware.setBeanName(this.getName());
		}
	}
	
	/**
	 * The name of the bean
	 */
	private String name;
	
	protected String getName(){
		StringHelper.checkNotEmpty( "field:name", name );
		return this.name;
	}
	public void setBeanName( final String name ){
		StringHelper.checkNotEmpty( "parameter:name", name );
		this.name = name;
	}
	
	/**
	 * If the given instance implements InitializingBean then a cast is
	 * performed followed by a call to
	 * {@link InitializingBean#afterPropertiesSet()} If a custom init method has
	 * been specified this method must be overridden.
	 * 
	 * @param instance
	 * @throws Exception
	 *             the exception thrown by the bean if it failed during
	 *             initialization.
	 */
	protected void satisfyInit(final Object instance) throws Exception {
		if (instance instanceof InitializingBean) {
			final InitializingBean initializingBean = (InitializingBean) instance;
			initializingBean.afterPropertiesSet();
		}
	}

	/**
	 * A back reference to the parent bean factory
	 */
	private BeanFactory beanFactory;

	public void setBeanFactory(BeanFactory beanFactory) {
		ObjectHelper.checkNotNull("parameter:beanFactory", beanFactory);
		this.beanFactory = beanFactory;
	}

	protected BeanFactory getBeanFactory() {
		ObjectHelper.checkNotNull("field:beanFactory", beanFactory);
		return this.beanFactory;
	}
}
