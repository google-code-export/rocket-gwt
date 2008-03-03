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
package rocket.beans.rebind.factorymethod;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the create instance factory method template
 * 
 * @author Miroslav Pokorny
 */
public class FactoryMethodTemplatedFile extends TemplatedFileCodeBlock {

	public FactoryMethodTemplatedFile() {
		super();
		setNative(false);
	}

	private Type factoryType;

	protected Type getFactoryType() {
		Checker.notNull("field:factoryType", factoryType);
		return this.factoryType;
	}

	public void setFactoryType(final Type factoryType) {
		Checker.notNull("factoryType:factoryType", factoryType);
		this.factoryType = factoryType;
	}

	private Method factoryMethod;

	protected Method getFactoryMethod() {
		Checker.notNull("field:factoryMethod", factoryMethod);
		return this.factoryMethod;
	}

	public void setFactoryMethod(final Method factoryMethod) {
		Checker.notNull("factoryMethod:factoryMethod", factoryMethod);
		this.factoryMethod = factoryMethod;
	}

	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.FACTORY_TYPE.equals(name)) {
				value = this.getFactoryType();
				break;
			}
			if (Constants.FACTORY_METHOD.equals(name)) {
				value = this.getFactoryMethod();
				break;
			}
			break;
		}
		return value;
	}
}
