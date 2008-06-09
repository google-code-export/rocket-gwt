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
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the write-fields.txt tempalte
 * 
 * @author Miroslav Pokorny
 */
public class WriteFieldsTemplatedFile extends TemplatedFileCodeBlock {

	public WriteFieldsTemplatedFile() {
		super();
		this.setFieldGetters(this.createFieldGetters());
	}

	/**
	 * The type having its fields written
	 */
	private Type type;

	protected Type getType() {
		Checker.notNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		Checker.notNull("parameter:type", type);
		this.type = type;
	}

	/**
	 * A list of all getters for all the fields about to serialized.
	 */
	private List<Method> fieldGetters;

	protected List<Method> getFieldGetters() {
		Checker.notNull("field:getters", fieldGetters);
		return this.fieldGetters;
	}

	protected void setFieldGetters(final List<Method> fieldGetters) {
		Checker.notNull("parameter:fieldGetters", fieldGetters);
		this.fieldGetters = fieldGetters;
	}

	protected List<Method> createFieldGetters() {
		return new ArrayList<Method>();
	}

	public void addFieldGetter(final Method getter) {
		Checker.notNull("parameter:getter", getter);

		this.getFieldGetters().add(getter);
	}

	protected CodeBlock getWriteIndividualFields() {
		final WriteFieldTemplatedFile template = new WriteFieldTemplatedFile();

		return new CollectionTemplatedCodeBlock<Method>() {

			@Override
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			@Override
			protected Collection<Method> getCollection() {
				return WriteFieldsTemplatedFile.this.getFieldGetters();
			}

			@Override
			protected void prepareToWrite(final Method method ) {
				template.setGetter( method );
			}

			@Override
			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}

	@Override
	protected String getResourceName() {
		return Constants.WRITE_FIELDS_TEMPLATE;
	}

	@Override
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
};
