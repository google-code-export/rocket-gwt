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
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the inserting field setter statements for fields that are
 * Objects.
 * 
 * @author Miroslav Pokorny
 */
public class InvokeObjectFieldSetterTemplatedFile extends TemplatedCodeBlock {

	public InvokeObjectFieldSetterTemplatedFile() {
		super();
		setNative(false);
	}

	private Method asMethod;

	protected Method getAsMethod() {
		ObjectHelper.checkNotNull("field:asMethod", asMethod);
		return this.asMethod;
	}

	public void setAsMethod(final Method asMethod) {
		ObjectHelper.checkNotNull("parameter:asMethod", asMethod);
		this.asMethod = asMethod;
	}

	private Method fieldSetter;

	protected Method getFieldSetter() {
		ObjectHelper.checkNotNull("field:fieldSetter", fieldSetter);
		return this.fieldSetter;
	}

	public void setFieldSetter(final Method fieldSetter) {
		ObjectHelper.checkNotNull("parameter:fieldSetter", fieldSetter);
		this.fieldSetter = fieldSetter;
	}

	private Type fieldType;

	protected Type getFieldType() {
		ObjectHelper.checkNotNull("fieldType:fieldType", fieldType);
		return this.fieldType;
	}

	public void setFieldType(final Type fieldType) {
		ObjectHelper.checkNotNull("parameter:fieldType", fieldType);
		this.fieldType = fieldType;
	}

	private Type fieldTypeDeserializer;

	protected Type getFieldTypeDeserializer() {
		ObjectHelper.checkNotNull("deserializerFieldType:deserializerFieldType", fieldTypeDeserializer);
		return this.fieldTypeDeserializer;
	}

	public void setFieldTypeDeserializer(final Type fieldTypeDeserializer) {
		ObjectHelper.checkNotNull("parameter:fieldTypeDeserializer", fieldTypeDeserializer);
		this.fieldTypeDeserializer = fieldTypeDeserializer;
	}

	private MethodParameter instance;

	protected MethodParameter getInstance() {
		ObjectHelper.checkNotNull("field:instance", instance);
		return this.instance;
	}

	public void setInstance(final MethodParameter instance) {
		ObjectHelper.checkNotNull("parameter:instance", instance);
		this.instance = instance;
	}

	private String javascriptPropertyName;

	protected String getJavascriptPropertyName() {
		ObjectHelper.checkNotNull("field:javascriptPropertyName", javascriptPropertyName);
		return this.javascriptPropertyName;
	}

	public void setJavascriptPropertyName(final String javascriptPropertyName) {
		ObjectHelper.checkNotNull("parameter:javascriptPropertyName", javascriptPropertyName);
		this.javascriptPropertyName = javascriptPropertyName;
	}

	private MethodParameter jsonObject;

	protected MethodParameter getJsonObject() {
		ObjectHelper.checkNotNull("field:jsonObject", jsonObject);
		return this.jsonObject;
	}

	public void setJsonObject(final MethodParameter jsonObject) {
		ObjectHelper.checkNotNull("parameter:jsonObject", jsonObject);
		this.jsonObject = jsonObject;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.INVOKE_OBJECT_FIELD_SETTER_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.INVOKE_OBJECT_FIELD_SETTER_AS_METHOD.equals(name)) {
				value = this.getAsMethod();
				break;
			}

			if (Constants.INVOKE_OBJECT_FIELD_SETTER_FIELD_SETTER.equals(name)) {
				value = this.getFieldSetter();
				break;
			}
			if (Constants.INVOKE_OBJECT_FIELD_SETTER_FIELD_TYPE.equals(name)) {
				value = this.getFieldType();
				break;
			}
			if (Constants.INVOKE_OBJECT_FIELD_SETTER_FIELD_TYPE_DESERIALIZER.equals(name)) {
				value = this.getFieldTypeDeserializer();
				break;
			}
			if (Constants.INVOKE_OBJECT_FIELD_SETTER_INSTANCE.equals(name)) {
				value = this.getInstance();
				break;
			}
			if (Constants.INVOKE_OBJECT_FIELD_SETTER_JAVASCRIPT_PROPERTY_NAME.equals(name)) {
				value = new StringLiteral(this.getJavascriptPropertyName());
				break;
			}
			if (Constants.INVOKE_OBJECT_FIELD_SETTER_JSON_OBJECT.equals(name)) {
				value = this.getJsonObject();
				break;
			}

			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found in file ["
				+ Constants.INVOKE_OBJECT_FIELD_SETTER_TEMPLATE + "]");
	}
}
