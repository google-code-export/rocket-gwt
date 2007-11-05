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

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the inserting statements that set a json object property
 * from a java instance list
 * 
 * @author Miroslav Pokorny
 */
public class WriteFieldTemplatedFile extends TemplatedCodeBlock {

	public WriteFieldTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The method which will return the value of a list.
	 */
	private Method fieldGetter;

	protected Method getFieldGetter() {
		ObjectHelper.checkNotNull("list:fieldGetter", fieldGetter);
		return this.fieldGetter;
	}

	public void setFieldGetter(final Method fieldSetter) {
		ObjectHelper.checkNotNull("parameter:fieldGetter", fieldSetter);
		this.fieldGetter = fieldSetter;
	}

	/**
	 * The name of the javascript property that will recieve the list
	 */
	private String javascriptPropertyName;

	protected String getJavascriptPropertyName() {
		ObjectHelper.checkNotNull("list:javascriptPropertyName", javascriptPropertyName);
		return this.javascriptPropertyName;
	}

	public void setJavascriptPropertyName(final String javascriptPropertyName) {
		ObjectHelper.checkNotNull("parameter:javascriptPropertyName", javascriptPropertyName);
		this.javascriptPropertyName = javascriptPropertyName;
	}

	/**
	 * A serializer which can serialize the list type
	 */
	private Type serializer;

	protected Type getSerializer() {
		ObjectHelper.checkNotNull("list:serializer", serializer);
		return this.serializer;
	}

	public void setSerializer(final Type serializer) {
		ObjectHelper.checkNotNull("parameter:serializer", serializer);
		this.serializer = serializer;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.WRITE_FIELD_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.WRITE_FIELD_FIELD_GETTER.equals(name)) {
				value = this.getFieldGetter();
				break;
			}
			if (Constants.WRITE_FIELD_JAVASCRIPT_PROPERTY_NAME.equals(name)) {
				value = new StringLiteral(this.getJavascriptPropertyName());
				break;
			}
			if (Constants.WRITE_FIELD_SERIALIZER.equals(name)) {
				value = this.getSerializer();
				break;
			}

			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found in file [" + Constants.WRITE_FIELD_TEMPLATE
				+ "]");
	}
}
