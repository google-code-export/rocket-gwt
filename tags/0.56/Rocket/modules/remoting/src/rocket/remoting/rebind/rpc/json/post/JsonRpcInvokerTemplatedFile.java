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
package rocket.remoting.rebind.rpc.json.post;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the json-rpc-invoker template file.
 * 
 * @author Miroslav Pokorny
 */
public class JsonRpcInvokerTemplatedFile extends TemplatedFileCodeBlock {

	public JsonRpcInvokerTemplatedFile() {
		super();
	}

	/**
	 * THe parameter type of the only parameter
	 */
	private Type parameterType;

	protected Type getParameterType() {
		Checker.notNull("field:parameterType", parameterType);
		return this.parameterType;
	}

	public void setParameterType(final Type parameterType) {
		Checker.notNull("parameter:parameterType", parameterType);
		this.parameterType = parameterType;
	}

	/**
	 * THe return type of the service.
	 */
	private Type returnType;

	protected Type getReturnType() {
		Checker.notNull("field:returnType", returnType);
		return this.returnType;
	}

	public void setReturnType(final Type returnType) {
		Checker.notNull("parameter:returnType", returnType);
		this.returnType = returnType;
	}

	@Override
	protected String getResourceName() {
		return JsonConstants.INVOKER_TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (JsonConstants.INVOKER_PARAMETER_TYPE.equals(name)) {
				value = this.getParameterType();
				break;
			}
			if (JsonConstants.INVOKER_RETURN_TYPE.equals(name)) {
				value = this.getReturnType();
				break;
			}
			break;
		}
		return value;
	}
}
