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
package rocket.json.rebind;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the asObject method body templated file
 * 
 * @author Miroslav Pokorny
 */
public class AsObjectTemplatedFile extends TemplatedCodeBlock {

	public AsObjectTemplatedFile() {
		super();
		setNative(false);
	}

	private Type deserializerType;

	protected Type getDeserializerType() {
		ObjectHelper.checkNotNull("deserializerType:deserializerType", deserializerType);
		return this.deserializerType;
	}

	public void setDeserializerType(final Type deserializerType) {
		ObjectHelper.checkNotNull("parameter:deserializerType", deserializerType);
		this.deserializerType = deserializerType;
	}

	private MethodParameter jsonValue;

	protected MethodParameter getJsonValue() {
		ObjectHelper.checkNotNull("field:jsonValue", jsonValue);
		return this.jsonValue;
	}

	public void setJsonValue(final MethodParameter jsonValue) {
		ObjectHelper.checkNotNull("parameter:jsonValue", jsonValue);
		this.jsonValue = jsonValue;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.AS_OBJECT_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.AS_OBJECT_DESERIALIZER_TYPE.equals(name)) {
				value = this.getDeserializerType();
				break;
			}
			if (Constants.AS_OBJECT_JSON_VALUE_PARAMETER.equals(name)) {
				value = this.getJsonValue();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found in file [" + Constants.AS_OBJECT_TEMPLATE
				+ "]");
	}
}
