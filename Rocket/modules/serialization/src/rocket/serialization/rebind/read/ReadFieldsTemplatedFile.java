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
import rocket.util.client.ObjectHelper;

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
		ObjectHelper.checkNotNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);
		this.type = type;
	}

	/**
	 * A list of all setters for all the fields being un-serialized.
	 */
	private List fieldSetters;

	protected List getFieldSetters() {
		ObjectHelper.checkNotNull("field:setters", fieldSetters);
		return this.fieldSetters;
	}

	protected void setFieldSetters(final List fieldSetters) {
		ObjectHelper.checkNotNull("parameter:fieldSetters", fieldSetters);
		this.fieldSetters = fieldSetters;
	}

	protected List createFieldSetters() {
		return new ArrayList();
	}

	public void addFieldSetter(final Method setter) {
		ObjectHelper.checkNotNull("parameter:setter", setter);

		this.getFieldSetters().add(setter);
	}

	protected CodeBlock getReadAndSetIndividualFields() {
		final ReadFieldTemplatedFile template = new ReadFieldTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return template.getInputStream();
			}

			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			protected Collection getCollection() {
				return ReadFieldsTemplatedFile.this.getFieldSetters();
			}

			protected void prepareToWrite(final Object element) {
				template.setSetter((Method) element);
			}

			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}

	protected String getResourceName() {
		return Constants.READ_FIELDS_TEMPLATE;
	}
	
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
