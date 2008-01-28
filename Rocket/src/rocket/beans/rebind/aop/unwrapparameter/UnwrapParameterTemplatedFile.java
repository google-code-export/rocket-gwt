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
package rocket.beans.rebind.aop.unwrapparameter;

import java.io.InputStream;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.IntLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the unwrap parameter template
 *
 * @author Miroslav Pokorny
 */
public class UnwrapParameterTemplatedFile extends TemplatedFileCodeBlock {

	public UnwrapParameterTemplatedFile() {
		super();
	}

	/**
	 * The method parameter being unwrapped
	 */
	private MethodParameter parameter;

	protected MethodParameter getParameter() {
		Checker.notNull("field:parameter", parameter);
		return this.parameter;
	}

	public void setParameter(final MethodParameter parameter) {
		Checker.notNull("parameter:parameter", parameter);
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
	protected String getResourceName() {
		String fileName = null;

		while (true) {
			final MethodParameter parameter = this.getParameter();
			final GeneratorContext context = parameter.getGeneratorContext();
			final Type type = parameter.getType();
			if (type.equals(context.getBoolean())) {
				fileName = Constants.BOOLEAN;
				break;
			}
			if (type.equals(context.getByte())) {
				fileName = Constants.BYTE;
				break;
			}
			if (type.equals(context.getShort())) {
				fileName = Constants.SHORT;
				break;
			}
			if (type.equals(context.getInt())) {
				fileName = Constants.INT;
				break;
			}
			if (type.equals(context.getLong())) {
				fileName = Constants.LONG;
				break;
			}
			if (type.equals(context.getFloat())) {
				fileName = Constants.FLOAT;
				break;
			}
			if (type.equals(context.getDouble())) {
				fileName = Constants.DOUBLE;
				break;
			}
			if (type.equals(context.getChar())) {
				fileName = Constants.CHAR;
				break;
			}
			fileName = Constants.OBJECT;
			break;
		}
		return fileName;
	}

	public InputStream getInputStream(){
		return super.getInputStream(); // TODO Delete when merged into same package as parent template.
	}

	public Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.PARAMETER_INDEX.equals(name)) {
				value = this.getParameterIndex();
				break;
			}
			if (Constants.PARAMETER_TYPE.equals(name)) {
				value = this.getParameterType();
				break;
			}
			break;
		}
		return value;
	}
}
