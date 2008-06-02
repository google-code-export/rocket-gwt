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
package rocket.serialization.rebind.read;

import java.io.InputStream;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

class ReadFieldTemplatedFile extends TemplatedFileCodeBlock {

	@Override
	protected String getResourceName() {
		String fileName = null;
		while (true) {
			// the return type of the getter is also the field type.
			final Type type = this.getFieldType();
			final GeneratorContext context = type.getGeneratorContext();
			if (context.getBoolean().equals(type)) {
				fileName = Constants.READ_FIELD_BOOLEAN_FIELD_TEMPLATE;
				break;
			}
			if (context.getByte().equals(type)) {
				fileName = Constants.READ_FIELD_BYTE_FIELD_TEMPLATE;
				break;
			}
			if (context.getShort().equals(type)) {
				fileName = Constants.READ_FIELD_SHORT_FIELD_TEMPLATE;
				break;
			}
			if (context.getInt().equals(type)) {
				fileName = Constants.READ_FIELD_INT_FIELD_TEMPLATE;
				break;
			}
			if (context.getLong().equals(type)) {
				fileName = Constants.READ_FIELD_LONG_FIELD_TEMPLATE;
				break;
			}
			if (context.getFloat().equals(type)) {
				fileName = Constants.READ_FIELD_FLOAT_FIELD_TEMPLATE;
				break;
			}
			if (context.getDouble().equals(type)) {
				fileName = Constants.READ_FIELD_DOUBLE_FIELD_TEMPLATE;
				break;
			}
			if (context.getChar().equals(type)) {
				fileName = Constants.READ_FIELD_CHAR_FIELD_TEMPLATE;
				break;
			}
			fileName = Constants.READ_FIELD_OBJECT_FIELD_TEMPLATE;
			break;
		}
		return fileName;
	}

	@Override
	public InputStream getInputStream(){
		return super.getInputStream();
	}
	
	@Override
	protected Object getValue0(final String name) {
		Object value = null;

		while (true) {
			if (Constants.READ_FIELD_SETTER_METHOD.equals(name)) {
				value = this.getSetter();
				break;
			}
			if (Constants.READ_FIELD_FIELD_TYPE.equals(name)) {
				value = this.getFieldType();
			}
			break;
		}

		return value;
	}

	/**
	 * A setter method which is used to update a field.
	 */
	private Method setter;

	protected Method getSetter() {
		Checker.notNull("field:setter", setter);
		return this.setter;
	}

	protected void setSetter(final Method setter) {
		Checker.notNull("parameter:setter", setter);
		this.setter = setter;
	}

	protected Type getFieldType() {
		// the new field value is the 2nd parameter
		final MethodParameter parameter = (MethodParameter) this.getSetter().getParameters().get(1);
		return parameter.getType();
	}
}
