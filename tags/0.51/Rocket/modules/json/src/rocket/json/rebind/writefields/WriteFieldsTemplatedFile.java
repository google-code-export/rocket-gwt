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
package rocket.json.rebind.writefields;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the write fields template
 * 
 * @author Miroslav Pokorny
 */
public class WriteFieldsTemplatedFile extends TemplatedFileCodeBlock {

	public WriteFieldsTemplatedFile() {
		super();
		this.setFields(this.createFields());
	}

	/**
	 * The actual type of the instance parameter
	 */
	private Type instanceType;

	protected Type getInstanceType() {
		Checker.notNull("list:instanceType", instanceType);
		return this.instanceType;
	}

	public void setInstanceType(final Type instanceType) {
		Checker.notNull("parameter:instanceType", instanceType);
		this.instanceType = instanceType;
	}

	/**
	 * A map containing a tuple of javascriptPropertyName and instance list
	 * getter
	 */
	private List fields;

	protected List<Holder> getFields() {
		Checker.notNull("list:fields", fields);
		return this.fields;
	}

	protected void setFields(final List<Holder> fields) {
		Checker.notNull("parameter:fields", fields);
		this.fields = fields;
	}

	protected List<Holder> createFields() {
		return new ArrayList<Holder>();
	}

	public void addField(final String javascriptPropertyName, final Method fieldGetter, final Type serializer) {
		Checker.notNull("parameter:javascriptPropertyName", javascriptPropertyName);
		Checker.notNull("parameter:fieldGetter", fieldGetter);
		Checker.notNull("parameter:serializer", serializer);

		final Holder holder = new Holder();
		holder.setJavascriptPropertyName(javascriptPropertyName);
		holder.setFieldGetter(fieldGetter);
		holder.setSerializer(serializer);

		this.getFields().add(holder);
	}

	protected CodeBlock getFieldsCodeBlock() {
		final WriteFieldTemplatedFile template = new WriteFieldTemplatedFile();

		return new CollectionTemplatedCodeBlock<Holder>() {

			@Override
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			@Override
			protected Collection<Holder> getCollection() {
				return WriteFieldsTemplatedFile.this.getFields();
			}

			@Override
			protected void prepareToWrite(final Holder holder ) {
				template.setJavascriptPropertyName(holder.getJavascriptPropertyName());
				template.setFieldGetter(holder.getFieldGetter());
				template.setSerializer(holder.getSerializer());
			}

			@Override
			protected void writeBetweenElements(SourceWriter writer) {
				writer.println("");
			}
		};
	}

	static class Holder {
		private String javascriptPropertyName;

		String getJavascriptPropertyName() {
			Checker.notNull("list:javascriptPropertyName", javascriptPropertyName);
			return this.javascriptPropertyName;
		}

		void setJavascriptPropertyName(final String javascriptPropertyName) {
			Checker.notNull("parameter:javascriptPropertyName", javascriptPropertyName);
			this.javascriptPropertyName = javascriptPropertyName;
		}

		private Method fieldGetter;

		Method getFieldGetter() {
			Checker.notNull("list:fieldGetter", fieldGetter);
			return this.fieldGetter;
		}

		void setFieldGetter(final Method fieldGetter) {
			Checker.notNull("parameter:fieldGetter", fieldGetter);
			this.fieldGetter = fieldGetter;
		}

		private Type serializer;

		Type getSerializer() {
			Checker.notNull("list:serializer", serializer);
			return this.serializer;
		}

		void setSerializer(final Type serializer) {
			Checker.notNull("parameter:serializer", serializer);
			this.serializer = serializer;
		}
	}

	protected String getResourceName() {
		return Constants.WRITE_FIELDS_TEMPLATE;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
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
};
