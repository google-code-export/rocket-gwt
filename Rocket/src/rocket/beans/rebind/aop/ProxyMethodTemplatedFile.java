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
package rocket.beans.rebind.aop;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the proxy method template
 * 
 * @author Miroslav Pokorny
 */
public class ProxyMethodTemplatedFile extends TemplatedCodeBlock {

	public ProxyMethodTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The method being proxied
	 */
	private NewMethod method;

	protected NewMethod getMethod() {
		ObjectHelper.checkNotNull("field:method", method);
		return this.method;
	}

	public void setMethod(final NewMethod method) {
		ObjectHelper.checkNotNull("parameter:method", method);
		this.method = method;
	}

	/**
	 * The actual template file is selected depending on whether the method
	 * returns void or not.
	 * 
	 * @return
	 */
	protected String getFileName() {
		return this.getMethod().returnsVoid() ? Constants.PROXY_METHOD_VOID_TEMPLATE : Constants.PROXY_METHOD_TEMPLATE;
	}

	protected InputStream getInputStream() {
		final String filename = this.getFileName();
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.PROXY_METHOD_METHOD.equals(name)) {
				value = this.getMethod();
				break;
			}
			if (Constants.PROXY_METHOD_PARAMETERS.equals(name)) {
				value = this.getParameters();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \"" + this.getFileName() + "\".");
	}

	protected CodeBlock getParameters() {
		final List parameters = this.getMethod().getParameters();
		final Map bindings = new HashMap();

		final StringBuilder templateContent = new StringBuilder();
		final Iterator iterator = parameters.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			final MethodParameter parameter = (MethodParameter) iterator.next();

			final String key = "parameter" + i;
			bindings.put(key, parameter);

			templateContent.append("${");
			templateContent.append(key);
			templateContent.append("}");
			if (iterator.hasNext()) {
				templateContent.append(", ");
			}
			i++;
		}

		final TemplatedCodeBlock codeBlock = new TemplatedCodeBlock() {
			protected InputStream getInputStream() {
				return new StringBufferInputStream(templateContent.toString());
			}

			protected Object getValue0(final String name) {
				return bindings.get(name);
			}
		};

		return codeBlock;
	}
}
