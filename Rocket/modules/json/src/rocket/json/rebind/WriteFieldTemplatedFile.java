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
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the inserting statements that set a json object property
 * from a java instance list
 * 
 * @author Miroslav Pokorny
 */
public class WriteFieldTemplatedFile extends TemplatedFileCodeBlock {

	public WriteFieldTemplatedFile() {
		super();
	}

	/**
	 * The method which will return the value of a list.
	 */
	private Method fieldGetter;

	protected Method getFieldGetter() {
		Checker.notNull("list:fieldGetter", fieldGetter);
		return this.fieldGetter;
	}

	public void setFieldGetter(final Method fieldSetter) {
		Checker.notNull("parameter:fieldGetter", fieldSetter);
		this.fieldGetter = fieldSetter;
	}

	/**
	 * The name of the javascript property that will recieve the list
	 */
	private String javascriptPropertyName;

	protected String getJavascriptPropertyName() {
		Checker.notNull("list:javascriptPropertyName", javascriptPropertyName);
		return this.javascriptPropertyName;
	}

	public void setJavascriptPropertyName(final String javascriptPropertyName) {
		Checker.notNull("parameter:javascriptPropertyName", javascriptPropertyName);
		this.javascriptPropertyName = javascriptPropertyName;
	}

	/**
	 * A serializer which can serialize the list type
	 */
	private Type serializer;

	protected Type getSerializer() {
		Checker.notNull("list:serializer", serializer);
		return this.serializer;
	}

	public void setSerializer(final Type serializer) {
		Checker.notNull("parameter:serializer", serializer);
		this.serializer = serializer;
	}

	protected String getResourceName(){
		return Constants.WRITE_FIELD_TEMPLATE;
	}

	public InputStream getInputStream() {
		return super.getInputStream();
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
}
