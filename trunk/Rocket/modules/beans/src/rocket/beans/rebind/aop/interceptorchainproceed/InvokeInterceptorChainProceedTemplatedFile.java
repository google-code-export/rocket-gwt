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
package rocket.beans.rebind.aop.interceptorchainproceed;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the invoke interceptor chain proceed template
 * 
 * @author Miroslav Pokorny
 */
public class InvokeInterceptorChainProceedTemplatedFile extends TemplatedFileCodeBlock {

	public InvokeInterceptorChainProceedTemplatedFile() {
		super();
	}

	/**
	 * The method's return type
	 */
	private Type methodReturnType;

	protected Type getMethodReturnType() {
		Checker.notNull("field:methodReturnType", methodReturnType);
		return this.methodReturnType;
	}

	public void setMethodReturnType(final Type methodReturnType) {
		Checker.notNull("parameter:methodReturnType", methodReturnType);
		this.methodReturnType = methodReturnType;
	}

	/**
	 * The actual template file is selected based on the parameter type.
	 * 
	 * @return
	 */
	@Override
	protected String getResourceName() {
		String fileName = null;

		while (true) {
			final Type returnType = this.getMethodReturnType();
			final GeneratorContext context = returnType.getGeneratorContext();

			if (returnType.equals(context.getBoolean())) {
				fileName = Constants.BOOLEAN_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getByte())) {
				fileName = Constants.BYTE_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getShort())) {
				fileName = Constants.SHORT_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getInt())) {
				fileName = Constants.INT_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getLong())) {
				fileName = Constants.LONG_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getFloat())) {
				fileName = Constants.FLOAT_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getDouble())) {
				fileName = Constants.DOUBLE_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getChar())) {
				fileName = Constants.CHAR_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getVoid())) {
				fileName = Constants.VOID_TEMPLATE;
				break;
			}
			fileName = Constants.OBJECT_TEMPLATE;
			break;
		}
		return fileName;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.METHOD_RETURN_TYPE.equals(name)) {
				value = this.getMethodReturnType();
				break;
			}
		}
		return value;
	}
}
