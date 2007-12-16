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
package rocket.remoting.rebind.rpc.java.servicemethodinvoker;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the add parameter typename template
 * 
 * @author Miroslav Pokorny
 */
class AddParameterTypeNameTemplatedFile extends TemplatedCodeBlock {

	public AddParameterTypeNameTemplatedFile() {
		super();
	}

	public boolean isNative() {
		return false;
	}

	public void setNative(final boolean ignored) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The method parameter
	 */
	private MethodParameter parameter;

	protected MethodParameter getParameter() {
		ObjectHelper.checkNotNull("field:parameter", parameter);
		return this.parameter;
	}

	public void setParameter(final MethodParameter parameter) {
		ObjectHelper.checkNotNull("parameter:parameter", parameter);
		this.parameter = parameter;
	}

	protected InputStream getInputStream() {
		final String filename = ServiceMethodInvokerConstants.ADD_PARAMETER_TYPENAME_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (ServiceMethodInvokerConstants.ADD_PARAMETER_TYPE_TYPENAME.equals(name)) {
				value = new StringLiteral(this.getParameter().getType().getName());
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found in template \""
				+ ServiceMethodInvokerConstants.ADD_PARAMETER_TYPENAME_TEMPLATE + "\"");
	}
}
