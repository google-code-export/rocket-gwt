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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.util.StringBufferSourceWriter;
import rocket.text.client.PlaceHolderReplacer;
import rocket.util.client.ObjectHelper;
import rocket.util.server.IoHelper;
import rocket.util.server.UncheckedIOException;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * Base class that may be used to resolve placeholders with literals or class
 * components be they types, constructors, methods, parameters or fields. The
 * source of the template is provided by an inputStream {@link #getInputStream}
 * 
 * @author Miroslav Pokorny
 */
abstract public class TemplatedCodeBlock implements CodeBlock {
	/**
	 * When true this code block is a jsni method.
	 */
	private boolean nativee;

	protected boolean isNative() {
		return this.nativee;
	}

	protected void setNative(final boolean nativee) {
		this.nativee = nativee;
	}

	public boolean isEmpty() {
		return false;
	}

	/**
	 * Writes the template after populating values to the given SourceWriter
	 * 
	 * @param writer
	 */
	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		BufferedReader reader = null;
		try {
			final InputStream inputStream = this.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));

			final TemplatedFileCodeBlockPlaceHolderReplacer replacer = new TemplatedFileCodeBlockPlaceHolderReplacer();

			while (true) {
				final String line = reader.readLine();
				if (null == line) {
					break;
				}

				writer.println(replacer.execute(line));
			}

		} catch (final IOException caught) {
			throw new UncheckedIOException(caught);
		} finally {
			IoHelper.closeIfNecessary(reader);
		}
	}

	/**
	 * Sub-classes must return an InputStream that contains the templated text
	 * with placeholders.
	 * 
	 * @return The inputStream.
	 */
	abstract protected InputStream getInputStream();

	/**
	 * A PlaceHolderReplacer with a
	 * {@link TemplatedFileCodeBlockPlaceHolderReplacer#getValue} that delegates
	 * to the outter class's {@link #getValue(String)}
	 */
	private class TemplatedFileCodeBlockPlaceHolderReplacer extends PlaceHolderReplacer {
		public String execute(final String text) {
			return super.execute(text);
		}

		public String getValue(final String name) {
			return TemplatedCodeBlock.this.getValue(name);
		}
	}

	/**
	 * Resolves the given place holder name into a String value.
	 * 
	 * @param name
	 * @return
	 */
	protected String getValue(final String name) {
		String value = null;

		while (true) {
			final Object object = this.getValue0(name);
			if (null == object) {
				throwValueNotFoundException(name);
			}

			if (object instanceof CodeBlock) {
				value = this.asString((CodeBlock) object);
				break;
			}
			if (object instanceof Type) {
				value = this.asString((Type) object);
				break;
			}
			if (object instanceof Constructor) {
				value = this.asString((Constructor) object);
				break;
			}
			if (object instanceof ConstructorParameter) {
				value = this.asString((ConstructorParameter) object);
				break;
			}
			if (object instanceof Method) {
				value = this.asString((Method) object);
				break;
			}
			if (object instanceof MethodParameter) {
				value = this.asString((MethodParameter) object);
				break;
			}
			if (object instanceof Field) {
				value = this.asString((Field) object);
				break;
			}

			this.throwUnhandledValueType(object);
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found.");
	}

	/**
	 * Sub-classes must override this to return the value for the given
	 * placeholder
	 * 
	 * @param name
	 *            The placeholder
	 * @return An object representing the value.
	 */
	abstract protected Object getValue0(String name);

	/**
	 * Writes the given codeBlock and captures its output as a String ready to
	 * be embedded within the template.
	 * 
	 * @param codeBlock
	 * @return
	 */
	protected String asString(final CodeBlock codeBlock) {
		ObjectHelper.checkNotNull("parameter:codeBlock", codeBlock);

		String string = "";
		if (false == codeBlock.isEmpty()) {
			final StringBufferSourceWriter writer = new StringBufferSourceWriter();
			GeneratorHelper.writeClassComponent(codeBlock, writer);
			string = writer.getBuffer();
		}
		return string;
	}

	protected String asString(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		return this.isNative() ? type.getJsniNotation() : type.getName();
	}

	protected String asString(final Constructor constructor) {
		ObjectHelper.checkNotNull("parameter:constructor", constructor);

		return constructor.getEnclosingType().getName();
	}

	protected String asString(final ConstructorParameter constructorParameter) {
		ObjectHelper.checkNotNull("parameter:constructorParameter", constructorParameter);

		return constructorParameter.getName();
	}

	protected String asString(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		return this.isNative() ? method.getJsniNotation() : method.getName();
	}

	protected String asString(final MethodParameter methodParameter) {
		ObjectHelper.checkNotNull("parameter:methodParameter", methodParameter);

		return methodParameter.getName();
	}

	protected String asString(final Field field) {
		ObjectHelper.checkNotNull("parameter:field", field);

		return this.isNative() ? field.getJsniNotation() : field.getName();
	}

	protected void throwUnhandledValueType(final Object object) {
		throw new TemplatedCodeBlockException("Unhandled value type encountered " + object);
	}

}
