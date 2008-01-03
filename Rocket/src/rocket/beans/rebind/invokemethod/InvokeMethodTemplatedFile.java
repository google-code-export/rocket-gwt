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
package rocket.beans.rebind.invokemethod;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the invoke-method.txt template
 * 
 * @author Miroslav Pokorny
 */
public class InvokeMethodTemplatedFile extends TemplatedCodeBlock {

	public InvokeMethodTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The type 
	 */
	private Type type;

	protected Type getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		ObjectHelper.checkNotNull("type:type", type);
		this.type = type;
	}

	/**
	 * The method to be invoked.
	 */
	private Method method;

	protected Method getMethod() {
		ObjectHelper.checkNotNull("field:method", method);
		return this.method;
	}

	public void setMethod(final Method method) {
		ObjectHelper.checkNotNull("method:method", method);
		this.method = method;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.TYPE.equals(name)) {
				value = this.getType();
				break;
			}
			if (Constants.METHOD.equals(name)) {
				value = this.getMethod();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \"" + Constants.TEMPLATE + "\".");
	}
}