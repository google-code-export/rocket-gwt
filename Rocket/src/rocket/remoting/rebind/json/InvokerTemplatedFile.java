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
package rocket.remoting.rebind.json;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.ManyCodeBlocks;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the invoker template file.
 * 
 * @author Miroslav Pokorny
 */
public class InvokerTemplatedFile extends TemplatedCodeBlock {

	public InvokerTemplatedFile() {
		super();
		setNative(false);
		this.setAddParameters(this.createAddParameters());
	}

	private ManyCodeBlocks addParameters;

	protected ManyCodeBlocks getAddParameters() {
		ObjectHelper.checkNotNull("field:addParameters", addParameters);
		return this.addParameters;
	}

	protected void setAddParameters(final ManyCodeBlocks addParameters) {
		ObjectHelper.checkNotNull("parameter:addParameters", addParameters);
		this.addParameters = addParameters;
	}

	protected ManyCodeBlocks createAddParameters() {
		return new ManyCodeBlocks();
	}

	public void addParameter(final CodeBlock parameter) {
		this.getAddParameters().add(parameter);
	}

	private MethodParameter callback;

	protected MethodParameter getCallback() {
		ObjectHelper.checkNotNull("field:callback", callback);
		return this.callback;
	}

	public void setCallback(final MethodParameter callback) {
		ObjectHelper.checkNotNull("parameter:callback", callback);
		this.callback = callback;
	}

	private Type invokerType;

	protected Type getInvokerType() {
		ObjectHelper.checkNotNull("invokerType:invokerType", invokerType);
		return this.invokerType;
	}

	public void setInvokerType(final Type invokerType) {
		ObjectHelper.checkNotNull("parameter:invokerType", invokerType);
		this.invokerType = invokerType;
	}

	private Type methodReturnType;

	protected Type getMethodReturnType() {
		ObjectHelper.checkNotNull("methodReturnType:methodReturnType", methodReturnType);
		return this.methodReturnType;
	}

	public void setMethodReturnType(final Type methodReturnType) {
		ObjectHelper.checkNotNull("parameter:methodReturnType", methodReturnType);
		this.methodReturnType = methodReturnType;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.INVOKER_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.INVOKER_ADD_PARAMETERS.equals(name)) {
				value = this.getAddParameters();
				break;
			}
			if (Constants.INVOKER_CALLBACK_PARAMETER.equals(name)) {
				value = this.getCallback();
				break;
			}
			if (Constants.INVOKER_INVOKER_TYPE.equals(name)) {
				value = this.getInvokerType();
				break;
			}
			if (Constants.INVOKER_METHOD_RETURN_TYPE.equals(name)) {
				value = this.getMethodReturnType();
				break;
			}
			break;
		}
		return value;
	}
}
