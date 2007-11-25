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

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.IntLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the unwrap parameter template
 * 
 * @author Miroslav Pokorny
 */
public class UnwrapParameterTemplatedFile extends TemplatedCodeBlock {

	public UnwrapParameterTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The method parameter being unwrapped
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

	protected CodeBlock getParameterIndex() {
		return new IntLiteral(this.getParameter().getIndex());
	}

	protected Type getParameterType() {
		return this.getParameter().getType();
	}

	/**
	 * The actual template file is selected based on the parameter type.
	 * 
	 * @return
	 */
	protected String getFileName() {
		String fileName = null;

		while (true) {
			final MethodParameter parameter = this.getParameter();
			final GeneratorContext context = parameter.getGeneratorContext();
			final Type type = parameter.getType();
			if (type.equals(context.getBoolean())) {
				fileName = Constants.UNWRAP_PARAMETER_BOOLEAN;
				break;
			}
			if (type.equals(context.getByte())) {
				fileName = Constants.UNWRAP_PARAMETER_BYTE;
				break;
			}
			if (type.equals(context.getShort())) {
				fileName = Constants.UNWRAP_PARAMETER_SHORT;
				break;
			}
			if (type.equals(context.getInt())) {
				fileName = Constants.UNWRAP_PARAMETER_INT;
				break;
			}
			if (type.equals(context.getLong())) {
				fileName = Constants.UNWRAP_PARAMETER_LONG;
				break;
			}
			if (type.equals(context.getFloat())) {
				fileName = Constants.UNWRAP_PARAMETER_FLOAT;
				break;
			}
			if (type.equals(context.getDouble())) {
				fileName = Constants.UNWRAP_PARAMETER_DOUBLE;
				break;
			}
			if (type.equals(context.getChar())) {
				fileName = Constants.UNWRAP_PARAMETER_CHAR;
				break;
			}
			fileName = Constants.UNWRAP_PARAMETER_OBJECT;
			break;
		}
		return fileName;
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
			if (Constants.UNWRAP_PARAMETER_PARAMETER_INDEX.equals(name)) {
				value = this.getParameterIndex();
				break;
			}
			if (Constants.UNWRAP_PARAMETER_PARAMETER_TYPE.equals(name)) {
				value = this.getParameterType();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \"" + this.getFileName() + "\".");
	}
}
