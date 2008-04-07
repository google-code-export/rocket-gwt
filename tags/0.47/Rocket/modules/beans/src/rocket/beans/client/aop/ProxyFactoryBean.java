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
package rocket.beans.client.aop;

import rocket.beans.client.BeanFactory;
import rocket.beans.client.BeanFactoryAware;
import rocket.beans.client.BeanNameAware;
import rocket.beans.client.FactoryBean;
import rocket.util.client.Checker;

/**
 * A convenient base class for any FactoryBean that gives out proxies.
 *  
 * @author Miroslav Pokorny
 */
abstract public class ProxyFactoryBean implements FactoryBean, BeanNameAware, BeanFactoryAware{

	/**
	 * Creates a new ProxyFactoryBean
	 */
	public ProxyFactoryBean(){
		super();
	}

	/**
	 * Retrieves a proxy for the target.
	 * 
	 * @return A new proxy
	 */
	public Object getObject() {
		return this.isSingleton() ? this.getSingleton() : this.getPrototype();
	}

	protected Object getSingleton() {
		return this.getProxy();
	}

	protected Object getPrototype() {
		return this.createProxy();
	}

	/**
	 * Tests this factory bean is a singleton by asking the target factory bean
	 * if it is a singleton.
	 */
	public boolean isSingleton() {
		final String name = this.getTargetBeanName();
		return this.getBeanFactory().isSingleton(name);
	}

	/**
	 * A cache copy of the proxy. Generated proxies are stateless and may be
	 * cached.
	 */
	private Object proxy;

	protected Object getProxy() {
		if (false == this.hasProxy()) {
			this.setProxy(this.createProxy());
		}
		Checker.notNull("field:proxy", proxy);
		return this.proxy;
	}

	protected boolean hasProxy() {
		return null != this.proxy;
	}

	protected void setProxy(final Object proxy) {
		Checker.notNull("parameter:proxy", proxy);
		this.proxy = proxy;
	}

	/**
	 * This method will be overridden to fetch the proxy target from the bean factory.
	 * @return
	 */
	protected Object createProxy(){
		final String name = this.getTargetBeanName();
		final Object target = this.getBeanFactory().getBean( name );
		return this.createProxy0( target );
	}
	
	protected String getTargetBeanName(){
		return '$' + this.getBeanName();		
	}

	/**
	 * This method returns a new proxy when invoked. Not only must a new
	 * instance of the proxy be created but its target field must also be set.
	 * 
	 * Generated sub-classes will override this method to create a sub-class of
	 * the target type delegating all public methods to the target. Where
	 * appropriate some methods will be adviced whilst others will be simple
	 * forwards.
	 * 
	 * @param the
	 *            target.
	 * @return A new Proxy
	 */
	abstract protected Object createProxy0(Object target);
	
	private BeanFactory beanFactory;
	
	protected BeanFactory getBeanFactory(){
		Checker.notNull("field:beanFactory", beanFactory);
		return this.beanFactory;
	}
	public void setBeanFactory( final BeanFactory beanFactory ){
		Checker.notNull("parameter:beanFactory", beanFactory);
		this.beanFactory = beanFactory;
	}
	
	private String beanName;
	
	protected String getBeanName(){
		return this.beanName;
	}
	public void setBeanName( final String beanName ){
		this.beanName = beanName;
	}
	
}
