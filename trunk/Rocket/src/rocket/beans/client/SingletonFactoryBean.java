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
 * Template FactoryBean that creates singletons and caches them. All generated
 * BeanFactories use anonymous SingletonFactoryBean classes within factory
 * methods to create singleton beans
 * 
 * @author Miroslav Pokorny
 */
abstract public class SingletonFactoryBean extends SingletonOrPrototypeFactoryBean implements FactoryBean {

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
				this.setObject(object);
				this.postCreate(object);
			} catch (final Throwable caught) {
				throwBeanException("Unable to create bean", caught);
			}
		}
		return object;
	}

	protected boolean hasObject() {
		return null != this.object;
	}

	protected void setObject(final Object object) {
		this.object = object;
	}

	public boolean isSingleton() {
		return true;
	}
}
