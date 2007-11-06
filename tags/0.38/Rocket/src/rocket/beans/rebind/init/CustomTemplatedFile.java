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
package rocket.beans.rebind.init;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the custom template
 * 
 * @author Miroslav Pokorny
 */
public class CustomTemplatedFile extends TemplatedCodeBlock {

	public CustomTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The bean that should have the custom method
	 */
	private Type bean;

	protected Type getBean() {
		ObjectHelper.checkNotNull("field:bean", bean);
		return this.bean;
	}

	public void setBean(final Type bean) {
		ObjectHelper.checkNotNull("bean:bean", bean);
		this.bean = bean;
	}

	/**
	 * The method that will be invoked after all properties are set.
	 */
	private Method customMethod;

	protected Method getCustomMethod() {
		ObjectHelper.checkNotNull("field:customMethod", customMethod);
		return this.customMethod;
	}

	public void setCustomMethod(final Method customMethod) {
		ObjectHelper.checkNotNull("customMethod:customMethod", customMethod);
		this.customMethod = customMethod;
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
			if (Constants.BEAN_TYPE.equals(name)) {
				value = this.getBean();
				break;
			}
			if (Constants.METHOD.equals(name)) {
				value = this.getCustomMethod();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found, template file [" + Constants.TEMPLATE + "]");
	}
}
