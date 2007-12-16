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

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.field.Field;
import rocket.util.client.ObjectHelper;

public class GetFieldTemplatedFile extends TemplatedCodeBlock {

	public boolean isNative() {
		return true;
	}

	public void setNative(final boolean nativee) {
		throw new UnsupportedOperationException();
	}

	protected InputStream getInputStream() {
		final String fileName = Constants.GET_FIELD_TEMPLATE;

		final InputStream inputStream = this.getClass().getResourceAsStream(fileName);
		ObjectHelper.checkNotNull(fileName, inputStream);
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;

		if (Constants.GET_FIELD_FIELD.equals(name)) {
			value = this.getField();
		}

		return value;
	}

	/**
	 * The field being fetched
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

}
