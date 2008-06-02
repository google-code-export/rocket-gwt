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
package rocket.json.rebind.setsimple;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the inserting list setter statements for fields that are
 * primitives or Strings
 * 
 * @author Miroslav Pokorny
 */
public class SetSimpleTemplatedFile extends TemplatedFileCodeBlock {

	public SetSimpleTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * A method on the serializer that is used to write to an instance list.
	 */
	private Method fieldSetter;

	protected Method getFieldSetter() {
		Checker.notNull("list:fieldSetter", fieldSetter);
		return this.fieldSetter;
	}

	public void setFieldSetter(final Method fieldSetter) {
		Checker.notNull("parameter:fieldSetter", fieldSetter);
		this.fieldSetter = fieldSetter;
	}

	/**
	 * The name of the property on jsonObject that contains the new value for
	 * the list
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
	 * The matching serializer for this fields type
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

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.FIELD_SETTER.equals(name)) {
				value = this.getFieldSetter();
				break;
			}
			if (Constants.JAVASCRIPT_PROPERTY_NAME.equals(name)) {
				value = new StringLiteral(this.getJavascriptPropertyName());
				break;
			}
			if (Constants.SERIALIZER.equals(name)) {
				value = this.getSerializer();
				break;
			}
			break;
		}
		return value;
	}
}
