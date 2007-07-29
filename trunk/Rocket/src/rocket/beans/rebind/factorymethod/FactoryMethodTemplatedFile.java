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

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the create instance factory method template
 * 
 * @author Miroslav Pokorny
 */
public class FactoryMethodTemplatedFile extends TemplatedCodeBlock {

	public FactoryMethodTemplatedFile() {
		super();
		setNative(false);
	}

	private Type factoryType;

	protected Type getFactoryType() {
		ObjectHelper.checkNotNull("field:factoryType", factoryType);
		return this.factoryType;
	}

	public void setFactoryType(final Type factoryType) {
		ObjectHelper.checkNotNull("factoryType:factoryType", factoryType);
		this.factoryType = factoryType;
	}

	private Method factoryMethod;

	protected Method getFactoryMethod() {
		ObjectHelper.checkNotNull("field:factoryMethod", factoryMethod);
		return this.factoryMethod;
	}

	public void setFactoryMethod(final Method factoryMethod) {
		ObjectHelper.checkNotNull("factoryMethod:factoryMethod", factoryMethod);
		this.factoryMethod = factoryMethod;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
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

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found in [" + Constants.TEMPLATE + "]");
	}
}
