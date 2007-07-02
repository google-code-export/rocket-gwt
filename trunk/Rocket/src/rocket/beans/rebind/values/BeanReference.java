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
package rocket.beans.rebind.values;

import rocket.beans.rebind.bean.BeanDefinition;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 * Holder for any bean-reference tag encountered during parsing.
 * 
 * @author Miroslav Pokorny
 */
public class BeanReference extends PropertyValueDefinition {

	/**
	 * If the property is a primitive report false
	 * 
	 * @return false if its not a primitive true otherwise.
	 */
	public boolean isCompatibleWith() {
		return false == this.getType() instanceof JPrimitiveType;
	}

	public String generatePropertyValueCodeBlock() {
		final String id = this.getId();
		final BeanDefinition beanDefinition = (BeanDefinition) this.getBeanFactoryGeneratorContext().getBeanDefinitions().get(id);
		final String beanType = beanDefinition.getType().getQualifiedSourceName();

		return "(" + beanType + ") getBeanFactory().getBean( \"" + id + "\")";
	}

	/**
	 * The id of the referenced bean
	 */
	private String id;

	public String getId() {
		StringHelper.checkNotEmpty("field:id", id);
		return id;
	}

	public void setId(final String id) {
		StringHelper.checkNotEmpty("parameter:id", id);
		this.id = id;
	}
}
