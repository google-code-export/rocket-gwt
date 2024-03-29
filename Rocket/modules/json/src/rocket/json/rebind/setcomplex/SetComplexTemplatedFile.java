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
package rocket.json.rebind.setcomplex;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the inserting list setter statements for fields that are
 * Objects.
 * 
 * @author Miroslav Pokorny
 */
public class SetComplexTemplatedFile extends TemplatedFileCodeBlock {

	public SetComplexTemplatedFile() {
		super();
	}

	private Method readMethod;

	protected Method getReadMethod() {
		Checker.notNull("list:readMethod", readMethod);
		return this.readMethod;
	}

	public void setReadMethod(final Method readMethod) {
		Checker.notNull("parameter:readMethod", readMethod);
		this.readMethod = readMethod;
	}

	/**
	 * This method takes the instance being reconstructed and sets a specific
	 * list.
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
	 * The list type.
	 */
	private Type fieldType;

	protected Type getFieldType() {
		Checker.notNull("fieldType:fieldType", fieldType);
		return this.fieldType;
	}

	public void setFieldType(final Type fieldType) {
		Checker.notNull("parameter:fieldType", fieldType);
		this.fieldType = fieldType;
	}

	/**
	 * The matching serializer for this fields type
	 */
	private Type serializer;

	protected Type getSerializer() {
		Checker.notNull("list:serializer", serializer);
		return this.serializer;
	}

	public void setSerializer(final Type fieldTypeDeserializer) {
		Checker.notNull("parameter:serializer", fieldTypeDeserializer);
		this.serializer = fieldTypeDeserializer;
	}

	/**
	 * A property of json object containing the instance list being set.
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

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.READ_METHOD.equals(name)) {
				value = this.getReadMethod();
				break;
			}
			if (Constants.FIELD_SETTER.equals(name)) {
				value = this.getFieldSetter();
				break;
			}
			if (Constants.FIELD_TYPE.equals(name)) {
				value = this.getFieldType();
				break;
			}
			if (Constants.SERIALIZER.equals(name)) {
				value = this.getSerializer();
				break;
			}
			if (Constants.JAVASCRIPT_PROPERTY_NAME.equals(name)) {
				value = new StringLiteral(this.getJavascriptPropertyName());
				break;
			}
			break;
		}
		return value;
	}
}
