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
package rocket.beans.rebind.properties;

import java.io.InputStream;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.util.client.Checker;

/**
 * An abstraction for the set property template
 * 
 * @author Miroslav Pokorny
 */
class SetPropertyTemplatedFile extends TemplatedFileCodeBlock {

	public SetPropertyTemplatedFile() {
		super();
	}

	/**
	 * The setter method used to set the property
	 */
	private Method setter;

	protected Method getSetter() {
		Checker.notNull("field:setter", setter);
		return this.setter;
	}

	public void setSetter(final Method setter) {
		Checker.notNull("setter:setter", setter);
		this.setter = setter;
	}

	/**
	 * The value
	 */
	private Value value;

	protected Value getValue() {
		Checker.notNull("field:value", value);
		return this.value;
	}

	public void setValue(final Value bean) {
		Checker.notNull("value:value", bean);
		this.value = bean;
	}

	@Override
	protected String getResourceName() {
		return Constants.SET_PROPERTY_TEMPLATE;
	}

	@Override
	public InputStream getInputStream(){
		return super.getInputStream();
	}
	
	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.SET_PROPERTY_SETTER.equals(name)) {
				value = this.getSetter();
				break;
			}
			if (Constants.SET_PROPERTY_VALUE.equals(name)) {
				value = this.getValue();
				break;
			}
			break;
		}
		return value;
	}
}
