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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * An abstraction for the write fields template
 * 
 * @author Miroslav Pokorny
 */
public class WriteFieldsTemplatedFile extends TemplatedCodeBlock {

	public WriteFieldsTemplatedFile() {
		super();
		setNative(false);
		this.setFields(this.createFields());
	}

	/**
	 * The method parameter that is the source of fields to be serialized
	 */
	private MethodParameter instance;

	protected MethodParameter getInstance() {
		ObjectHelper.checkNotNull("list:instance", instance);
		return this.instance;
	}

	public void setInstance(final MethodParameter instance) {
		ObjectHelper.checkNotNull("parameter:instance", instance);
		this.instance = instance;
	}

	/**
	 * The actual type of the instance parameter
	 */
	private Type instanceType;

	protected Type getInstanceType() {
		ObjectHelper.checkNotNull("list:instanceType", instanceType);
		return this.instanceType;
	}

	public void setInstanceType(final Type instanceType) {
		ObjectHelper.checkNotNull("parameter:instanceType", instanceType);
		this.instanceType = instanceType;
	}

	/**
	 * The parameter holding the jsonObject variable being populated
	 */
	private MethodParameter jsonObject;

	protected MethodParameter getJsonObject() {
		ObjectHelper.checkNotNull("list:jsonObject", jsonObject);
		return this.jsonObject;
	}

	public void setJsonObject(final MethodParameter jsonObject) {
		ObjectHelper.checkNotNull("parameter:jsonObject", jsonObject);
		this.jsonObject = jsonObject;
	}

	/**
	 * A map containing a tuple of javascriptPropertyName and instance list
	 * getter
	 */
	private List fields;

	protected List getFields() {
		ObjectHelper.checkNotNull("list:fields", fields);
		return this.fields;
	}

	protected void setFields(final List fields) {
		ObjectHelper.checkNotNull("parameter:fields", fields);
		this.fields = fields;
	}

	protected List createFields() {
		return new ArrayList();
	}

	public void addField(final String javascriptPropertyName, final Method fieldGetter, final Type serializer) {
		StringHelper.checkNotNull("parameter:javascriptPropertyName", javascriptPropertyName);
		ObjectHelper.checkNotNull("parameter:fieldGetter", fieldGetter);
		ObjectHelper.checkNotNull("parameter:serializer", serializer);

		final Holder holder = new Holder();
		holder.setJavascriptPropertyName(javascriptPropertyName);
		holder.setFieldGetter(fieldGetter);
		holder.setSerializer(serializer);

		this.getFields().add(holder);
	}

	protected CodeBlock getFieldsCodeBlock() {
		final WriteFieldTemplatedFile template = new WriteFieldTemplatedFile();
		template.setJsonObject(this.getJsonObject());

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return template.getInputStream();
			}

			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			protected Collection getCollection() {
				return WriteFieldsTemplatedFile.this.getFields();
			}

			protected void prepareToWrite(Object element) {
				final Holder holder = (Holder) element;
				template.setJavascriptPropertyName(holder.getJavascriptPropertyName());
				template.setFieldGetter(holder.getFieldGetter());
				template.setSerializer(holder.getSerializer());
			}

			protected void writeBetweenElements(SourceWriter writer) {
				writer.println("");
			}
		};
	}

	static class Holder {
		private String javascriptPropertyName;

		String getJavascriptPropertyName() {
			ObjectHelper.checkNotNull("list:javascriptPropertyName", javascriptPropertyName);
			return this.javascriptPropertyName;
		}

		void setJavascriptPropertyName(final String javascriptPropertyName) {
			ObjectHelper.checkNotNull("parameter:javascriptPropertyName", javascriptPropertyName);
			this.javascriptPropertyName = javascriptPropertyName;
		}

		private Method fieldGetter;

		Method getFieldGetter() {
			ObjectHelper.checkNotNull("list:fieldGetter", fieldGetter);
			return this.fieldGetter;
		}

		void setFieldGetter(final Method fieldGetter) {
			ObjectHelper.checkNotNull("parameter:fieldGetter", fieldGetter);
			this.fieldGetter = fieldGetter;
		}

		private Type serializer;

		Type getSerializer() {
			ObjectHelper.checkNotNull("list:serializer", serializer);
			return this.serializer;
		}

		void setSerializer(final Type serializer) {
			ObjectHelper.checkNotNull("parameter:serializer", serializer);
			this.serializer = serializer;
		}
	}

	protected InputStream getInputStream() {
		final String filename = Constants.WRITE_FIELDS_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.WRITE_FIELDS_INSTANCE_PARAMETER.equals(name)) {
				value = this.getInstance();
				break;
			}
			if (Constants.WRITE_FIELDS_INSTANCE_TYPE.equals(name)) {
				value = this.getInstanceType();
				break;
			}
			if (Constants.WRITE_FIELDS_WRITE_METHODS.equals(name)) {
				value = this.getFieldsCodeBlock();
				break;
			}

			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found, template file ["
				+ Constants.WRITE_FIELDS_TEMPLATE + "]");
	}
};
