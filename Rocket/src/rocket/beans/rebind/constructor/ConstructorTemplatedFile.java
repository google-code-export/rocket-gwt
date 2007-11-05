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
package rocket.beans.rebind.constructor;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.constructor.Constructor;
import rocket.util.client.ObjectHelper;

import rocket.generator.rebind.SourceWriter;

/**
 * An abstraction for the constructor template
 * 
 * @author Miroslav Pokorny
 */
public class ConstructorTemplatedFile extends TemplatedCodeBlock {

	public ConstructorTemplatedFile() {
		super();
		setNative(false);
		this.setArguments(this.createArguments());
	}

	private Constructor bean;

	protected Constructor getBean() {
		ObjectHelper.checkNotNull("field:bean", bean);
		return this.bean;
	}

	public void setBean(final Constructor bean) {
		ObjectHelper.checkNotNull("parameter:bean", bean);
		this.bean = bean;
	}

	private List arguments;

	protected List getArguments() {
		ObjectHelper.checkNotNull("field:addParameters", arguments);
		return this.arguments;
	}

	protected void setArguments(final List arguments) {
		ObjectHelper.checkNotNull("parameter:arguments", arguments);
		this.arguments = arguments;
	}

	protected List createArguments() {
		return new ArrayList();
	}

	public void addArgument(final Value value) {
		ObjectHelper.checkNotNull("parameter:value", value);
		this.getArguments().add(value);
	}

	protected InputStream getInputStream() {
		final String filename = Constants.TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.CONSTRUCTOR.equals(name)) {
				value = this.getBean();
				break;
			}
			if (Constants.ARGUMENTS.equals(name)) {
				value = this.getArgumentsAsCodeBlock();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found, template file [" + Constants.TEMPLATE + "]");
	}

	protected CodeBlock getArgumentsAsCodeBlock() {
		final List parameters = this.getArguments();
		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return new StringBufferInputStream("${value}");
			}

			protected Object getValue0(final String name) {
				return parameters.get(this.getIndex());
			}

			protected Collection getCollection() {
				return parameters;
			}

			protected void prepareToWrite(Object element) {
			}

			protected void writeBetweenElements(SourceWriter writer) {
				writer.print(",");
			}
		};
	}
}
