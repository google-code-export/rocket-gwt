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

/**
 * Template FactoryBean that creates prototypes on demand. All generated
 * BeanFactories use anonymous PrototypeFactoryBean classes within factory
 * methods to create prototype beans
 * 
 * @author Miroslav Pokorny
 */
abstract public class PrototypeFactoryBean extends SingletonOrPrototypeFactoryBean implements FactoryBean {

	public Object getObject() {
		Object object = null;
		try {
			object = this.createObject();
			this.postCreate(object);

			object = this.getObject(object);
		} catch (final Throwable caught) {
			throwBeanException("Unable to create bean, because " + caught.getMessage(), caught);
			return null;
		}
		return object;
	}

	/**
	 * Handles the final step involved in initializing a bean. If the bean is
	 * actually a FactoryBean ask it for its actual Object, this will continue
	 * until no more FactoryBeans are present in the chain and a true bean is
	 * located.
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	protected Object getObject(final Object object) throws Exception {
		Object returned = object;

		if (returned instanceof FactoryBean) {
			final FactoryBean factoryBean = (FactoryBean) object;
			returned = factoryBean.getObject();
		}

		return returned;
	}

	/**
	 * Prototypes are not singletons.
	 * 
	 * @return always returns false.
	 */
	public boolean isSingleton() {
		return false;
	}
}
