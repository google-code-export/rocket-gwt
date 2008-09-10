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
package rocket.serialization.rebind.write;

import java.io.InputStream;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

public class WriteFieldTemplatedFile extends TemplatedFileCodeBlock {

	protected String getResourceName() {
		String fileName = null;
		while (true) {
			// the return type of the getter is also the field type.
			final Type type = this.getGetter().getReturnType();
			final GeneratorContext context = type.getGeneratorContext();
			if (context.getBoolean().equals(type)) {
				fileName = Constants.WRITE_FIELD_BOOLEAN_FIELD_TEMPLATE;
				break;
			}
			if (context.getByte().equals(type)) {
				fileName = Constants.WRITE_FIELD_BYTE_FIELD_TEMPLATE;
				break;
			}
			if (context.getShort().equals(type)) {
				fileName = Constants.WRITE_FIELD_SHORT_FIELD_TEMPLATE;
				break;
			}
			if (context.getInt().equals(type)) {
				fileName = Constants.WRITE_FIELD_INT_FIELD_TEMPLATE;
				break;
			}
			if (context.getLong().equals(type)) {
				fileName = Constants.WRITE_FIELD_LONG_FIELD_TEMPLATE;
				break;
			}
			if (context.getFloat().equals(type)) {
				fileName = Constants.WRITE_FIELD_FLOAT_FIELD_TEMPLATE;
				break;
			}
			if (context.getDouble().equals(type)) {
				fileName = Constants.WRITE_FIELD_DOUBLE_FIELD_TEMPLATE;
				break;
			}
			if (context.getChar().equals(type)) {
				fileName = Constants.WRITE_FIELD_CHAR_FIELD_TEMPLATE;
				break;
			}
			fileName = Constants.WRITE_FIELD_OBJECT_FIELD_TEMPLATE;
			break;
		}
		return fileName;
	}

	public InputStream getInputStream() {
		return super.getInputStream();
	}

	protected Object getValue0(final String name) {
		Object value = null;

		if (Constants.WRITE_FIELD_GETTER_METHOD.equals(name)) {
			value = this.getGetter();
		}

		return value;
	}

	/**
	 * A getter method which may be used to fetch the field being written
	 */
	private Method getter;

	protected Method getGetter() {
		Checker.notNull("field:field", getter);
		return this.getter;
	}

	protected void setGetter(final Method getter) {
		Checker.notNull("parameter:getter", getter);
		this.getter = getter;
	}
}
