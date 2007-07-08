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
package rocket.beans.rebind;

import rocket.util.client.ObjectHelper;

/**
 * A base class for anything that generates a part of the BeanFactoryImpl
 * 
 * @author Miroslav Pokorny
 */
abstract public class HasBeanFactoryGeneratorContext {

	/**
	 * A reference to the context for this code generation session.
	 */
	private BeanFactoryGeneratorContext beanFactoryGeneratorContext;

	protected BeanFactoryGeneratorContext getBeanFactoryGeneratorContext() {
		ObjectHelper.checkNotNull("field:beanFactoryGeneratorContext",
				beanFactoryGeneratorContext);
		return this.beanFactoryGeneratorContext;
	}

	public void setBeanFactoryGeneratorContext(
			final BeanFactoryGeneratorContext beanFactoryGeneratorContext) {
		ObjectHelper.checkNotNull("parameter:beanFactoryGeneratorContext",
				beanFactoryGeneratorContext);
		this.beanFactoryGeneratorContext = beanFactoryGeneratorContext;
	}
}
