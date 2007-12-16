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
package rocket.generator.rebind.codeblock;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Provides the ability to replace placeholders within a text file with values.
 * The values for the placeholders are sourced from a map of values.
 * 
 * @author Miroslav Pokorny
 */
public class TemplatedFileCodeBlock extends TemplatedCodeBlock implements CodeBlock, CodeGenerator {

	public TemplatedFileCodeBlock() {
		super();

		this.setValues(this.createValues());
	}

	public void setNative(final boolean nativee) {
		super.setNative(nativee);
	}

	/**
	 * The text file containing the template.
	 */
	private String filename;

	protected String getFilename() {
		StringHelper.checkNotEmpty("field:filename", filename);
		return this.filename;
	}

	public void setFilename(final String filename) {
		StringHelper.checkNotEmpty("parameter:filename", filename);
		this.filename = filename;
	}

	/**
	 * Retrieves a resource's inputstream using the filename property as a
	 * resource name. The InputStream is loaded from the classpath.
	 * 
	 * @return The located InputStream
	 */
	protected InputStream getInputStream() {
		final String filename = this.getFilename();
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	/**
	 * A map that is used to resolve placeholders to values.
	 */
	private Map values;

	protected Map getValues() {
		ObjectHelper.checkNotNull("field:values", values);
		return this.values;
	}

	protected void setValues(final Map values) {
		ObjectHelper.checkNotNull("parameter:values", values);
		this.values = values;
	}

	protected Map createValues() {
		return new HashMap();
	}

	public void setLiteral(final String name, final Literal literal) {
		this.set(name, literal);
	}

	public void setType(final String name, final Type type) {
		this.set(name, type);
	}

	public void setConstructor(final String name, final Constructor constructor) {
		this.set(name, constructor);
	}

	public void setConstructorParameter(final String name, final ConstructorParameter parameter) {
		this.set(name, parameter);
	}

	public void setMethod(final String name, final Method method) {
		this.set(name, method);
	}

	public void setMethodParameter(final String name, final MethodParameter parameter) {
		this.set(name, parameter);
	}

	public void setField(final String name, final Field field) {
		this.set(name, field);
	}

	public void setCodeBlock(final String name, final CodeBlock codeBlock) {
		this.set(name, codeBlock);
	}

	protected void set(final String name, final Object value) {
		StringHelper.checkNotEmpty("parameter:name", name);
		ObjectHelper.checkNotNull("parameter:value", value);
		this.getValues().put(name, value);
	}

	/**
	 * Resolves placeholders by looking up a map using the name as the key
	 * 
	 * @param name
	 * @return
	 */
	protected Object getValue0(final String name) {
		return this.getValues().get(name);
	}
}
