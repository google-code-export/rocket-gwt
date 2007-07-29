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

import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Holds a number of properties related to constructing a new type
 * @author Miroslav Pokorny
 */
public class Bean {
	private String id;

	public String getId() {
		StringHelper.checkNotEmpty("field:id", id);
		return this.id;
	}

	public void setId(final String id) {
		StringHelper.checkNotEmpty("parameter:id", id);
		this.id = id;
	}

	/**
	 * The factory type being created
	 */
	private NewNestedType factoryBean;

	public NewNestedType getFactoryBean() {
		ObjectHelper.checkNotNull("field:factoryBean", factoryBean);
		return this.factoryBean;
	}

	public void setFactoryBean(final NewNestedType factoryBean) {
		ObjectHelper.checkNotNull("factoryBean:factoryBean", factoryBean);
		this.factoryBean = factoryBean;
	}

	/**
	 * The type of the bean
	 */
	private Type type;

	public Type getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		ObjectHelper.checkNotNull("type:type", type);
		this.type = type;
	}
	
	public String toString(){
		return "Bean id[" + this.id + "] type[" + this.type + "]";
	}
}
