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

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the json-rpc-invoker template file.
 * 
 * @author Miroslav Pokorny
 */
class JsonRpcInvokerTemplatedFile extends TemplatedCodeBlock {

	public JsonRpcInvokerTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * THe return type of the service.
	 */
	private Type payloadType;

	protected Type getPayloadType() {
		ObjectHelper.checkNotNull("returnType:payloadType", payloadType);
		return this.payloadType;
	}

	public void setPayloadType(final Type payloadType) {
		ObjectHelper.checkNotNull("parameter:payloadType", payloadType);
		this.payloadType = payloadType;
	}

	/**
	 * THe return type of the service.
	 */
	private Type parameterType;

	protected Type getParameterType() {
		ObjectHelper.checkNotNull("returnType:parameterType", parameterType);
		return this.parameterType;
	}

	public void setParameterType(final Type parameterType) {
		ObjectHelper.checkNotNull("parameter:parameterType", parameterType);
		this.parameterType = parameterType;
	}
	
	protected InputStream getInputStream() {
		final String filename = Constants.JSON_RPC_INVOKER_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.JSON_RPC_INVOKER_PARAMETER_TYPE.equals(name)) {
				value = this.getParameterType();
				break;
			}
			if (Constants.JSON_RPC_INVOKER_PAYLOAD_TYPE.equals(name)) {
				value = this.getPayloadType();
				break;
			}
			break;
		}
		return value;
	}
}
