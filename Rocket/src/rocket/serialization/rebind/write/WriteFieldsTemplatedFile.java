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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the write0.txt tempalte
 * 
 * @author Miroslav Pokorny
 */
public class WriteFieldsTemplatedFile extends TemplatedCodeBlock {

	public WriteFieldsTemplatedFile() {
		super();
		setNative(false);
		this.setFieldGetters(this.createFieldGetters());
	}

	/**
	 * The type having its fields written
	 */
	private Type type;

	protected Type getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);
		this.type = type;
	}

	/**
	 * A list of all getters for all the fields about to serialized.
	 */
	private List fieldGetters;

	protected List getFieldGetters() {
		ObjectHelper.checkNotNull("field:getters", fieldGetters);
		return this.fieldGetters;
	}

	protected void setFieldGetters(final List fieldGetters) {
		ObjectHelper.checkNotNull("parameter:fieldGetters", fieldGetters);
		this.fieldGetters = fieldGetters;
	}

	protected List createFieldGetters() {
		return new ArrayList();
	}

	public void addFieldGetter(final Method getter) {
		ObjectHelper.checkNotNull("parameter:getter", getter);

		this.getFieldGetters().add(getter);
	}

	protected CodeBlock getWriteIndividualFields() {
		final WriteFieldTemplatedFile template = new WriteFieldTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return template.getInputStream();
			}

			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			protected Collection getCollection() {
				return WriteFieldsTemplatedFile.this.getFieldGetters();
			}

			protected void prepareToWrite(final Object element) {
				template.setGetter((Method) element);
			}

			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}

	protected InputStream getInputStream() {
		final String filename = Constants.WRITE_FIELDS_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.WRITE_FIELDS_TYPE.equals(name)) {
				value = this.getType();
				break;
			}
			if (Constants.WRITE_FIELDS_GET_AND_WRITE_INDIVIDUAL_FIELDS.equals(name)) {
				value = this.getWriteIndividualFields();
				break;
			}

			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \"" + Constants.WRITE_FIELDS_TEMPLATE
				+ "\".");
	}
};
