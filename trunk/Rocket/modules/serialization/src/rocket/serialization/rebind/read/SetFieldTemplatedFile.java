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

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

public class SetFieldTemplatedFile extends TemplatedFileCodeBlock {

	protected String getResourceName() {
		return Constants.SET_FIELD_TEMPLATE;
	}

	protected Object getValue0(final String name) {
		Object value = null;

		while (true) {
			if (Constants.SET_FIELD_FIELD.equals(name)) {
				value = this.getField();
				break;
			}
			if (Constants.SET_FIELD_FIELD_TYPE.equals(name)) {
				value = this.getFieldType();
			}
			break;
		}

		return value;
	}

	/**
	 * The field being updated
	 */
	private Field field;

	protected Field getField() {
		ObjectHelper.checkNotNull("field:field", field);
		return this.field;
	}

	public void setField(final Field field) {
		ObjectHelper.checkNotNull("parameter:field", field);
		this.field = field;
	}

	protected Type getFieldType() {
		return this.getField().getType();
	}
}
