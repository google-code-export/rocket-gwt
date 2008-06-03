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

import rocket.util.client.Checker;

/**
 * Template FactoryBean that creates singletons and caches them. All generated
 * BeanFactories use anonymous SingletonFactoryBean classes within factory
 * methods to create singleton beans
 * 
 * @author Miroslav Pokorny
 */
abstract public class SingletonFactoryBean extends SingletonOrPrototypeFactoryBean implements FactoryBean, DisposableBean {

	public SingletonFactoryBean() {
		super();
	}

	/**
	 * The singleton instance.
	 */
	private Object object;

	public Object getObject() {
		if (false == this.hasObject()) {

			try {
				final Object object = this.createObject();
				// Kind of a hack to make cycles between singletons work,
				// potentially fails when one singleton is the product of a
				// FactoryBean
				this.setObject(object);
				this.postCreate(object);
				final Object returned = this.getObject(object);

				if (returned != object) {
					this.setObject(returned);
				}

			} catch (final Throwable caught) {
				throwBeanException("Unable to create bean, because " + caught.getMessage(), caught);
				return null;
			}
		}
		if (this.object instanceof FactoryBean) {
			this.throwBeanException("The FactoryBean of \"" + this.getName()
					+ "\" has not had all its properties set and is involved in a cycle, break cycle.", null);
		}

		return this.object;
	}

	/**
	 * Handles the final step involved in initializing a bean. If the bean is
	 * actually a FactoryBean ask it for its actual Object, this will continue
	 * until no more FactoryBeans are present in the chain and a true bean is
	 * located.
	 * 
	 * @param object
	 * @return The final bean
	 * @throws Exception
	 */
	protected Object getObject(final Object object) throws Exception {
		Object returned = object;

		if (returned instanceof FactoryBean) {
			// temp will be overridden when the true bean comes back.
			final FactoryBean factoryBean = (FactoryBean) object;
			returned = factoryBean.getObject();
		}

		return returned;
	}

	protected boolean hasObject() {
		return null != this.object;
	}

	protected void setObject(final Object object) {
		Checker.notNull("parameter:object", object);
		this.object = object;
	}

	public boolean isSingleton() {
		return true;
	}

	public void destroy() {
		if (this.hasObject()) {
			this.destroy(this.getObject());
		}
	}

	protected void destroy(final Object instance) {
		if (instance instanceof DisposableBean) {
			final DisposableBean disposableBean = (DisposableBean) instance;
			disposableBean.destroy();
		}
	}
}
