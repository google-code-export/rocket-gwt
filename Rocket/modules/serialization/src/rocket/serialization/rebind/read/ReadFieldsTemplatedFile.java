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
package rocket.serialization.rebind.read;

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
 * An abstraction for the read-fields.txt tempalte
 * 
 * @author Miroslav Pokorny
 */
public class ReadFieldsTemplatedFile extends TemplatedFileCodeBlock {

	public ReadFieldsTemplatedFile() {
		super();
		this.setFieldSetters(this.createFieldSetters());
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
	 * A list of all setters for all the fields being un-serialized.
	 */
	private List<Method> fieldSetters;

	protected List<Method> getFieldSetters() {
		Checker.notNull("field:setters", fieldSetters);
		return this.fieldSetters;
	}

	protected void setFieldSetters(final List<Method> fieldSetters) {
		Checker.notNull("parameter:fieldSetters", fieldSetters);
		this.fieldSetters = fieldSetters;
	}

	protected List<Method> createFieldSetters() {
		return new ArrayList<Method>();
	}

	public void addFieldSetter(final Method setter) {
		Checker.notNull("parameter:setter", setter);

		this.getFieldSetters().add(setter);
	}

	protected CodeBlock getReadAndSetIndividualFields() {
		final ReadFieldTemplatedFile template = new ReadFieldTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			@Override
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			@Override
			protected Collection getCollection() {
				return ReadFieldsTemplatedFile.this.getFieldSetters();
			}

			@Override
			protected void prepareToWrite(final Object element) {
				template.setSetter((Method) element);
			}

			@Override
			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}

	@Override
	protected String getResourceName() {
		return Constants.READ_FIELDS_TEMPLATE;
	}
	
	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.READ_FIELDS_TYPE.equals(name)) {
				value = this.getType();
				break;
			}
			if (Constants.READ_FIELDS_READ_AND_SET_INDIVIDUAL_FIELDS.equals(name)) {
				value = this.getReadAndSetIndividualFields();
				break;
			}

			break;
		}
		return value;
	}
};
