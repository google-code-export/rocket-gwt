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
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the invoke interceptor chain proceed template
 * 
 * @author Miroslav Pokorny
 */
public class InvokeInterceptorChainProceedTemplatedFile extends TemplatedCodeBlock {

	public InvokeInterceptorChainProceedTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The method's return type
	 */
	private Type methodReturnType;

	protected Type getMethodReturnType() {
		ObjectHelper.checkNotNull("field:methodReturnType", methodReturnType);
		return this.methodReturnType;
	}

	public void setMethodReturnType(final Type methodReturnType) {
		ObjectHelper.checkNotNull("parameter:methodReturnType", methodReturnType);
		this.methodReturnType = methodReturnType;
	}

	/**
	 * The actual template file is selected based on the parameter type.
	 * 
	 * @return
	 */
	protected String getFileName() {
		String fileName = null;

		while (true) {
			final Type returnType = this.getMethodReturnType();
			final GeneratorContext context = returnType.getGeneratorContext();

			if (returnType.equals(context.getBoolean())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_BOOLEAN_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getByte())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_BYTE_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getShort())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_SHORT_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getInt())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_INT_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getLong())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_LONG_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getFloat())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_FLOAT_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getDouble())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_DOUBLE_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getChar())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_CHAR_TEMPLATE;
				break;
			}
			if (returnType.equals(context.getVoid())) {
				fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_VOID_TEMPLATE;
				break;
			}
			fileName = Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_OBJECT_TEMPLATE;
			break;
		}
		return fileName;
	}

	protected InputStream getInputStream() {
		final String filename = this.getFileName();
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.INVOKE_INTERCEPTOR_CHAIN_PROCEED_METHOD_RETURN_TYPE.equals(name)) {
				value = this.getMethodReturnType();
				break;
			}
		}
		return value;
	}
}
