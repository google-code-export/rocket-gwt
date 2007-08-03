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
package rocket.generator.rebind.initializer;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * Represents a concrete initializer.
 * 
 * @author Miroslav Pokorny
 */
public class InitializerImpl implements Initializer {

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		this.writeLogger();

		if (this.isStatic()) {
			writer.print("static ");
		}
		writer.println("{");

		writer.indent();
		this.getBody().write( writer );
		writer.outdent();

		writer.println("};");
	}

	protected void writeLogger() {
		this.getGeneratorContext().debug("initializer");
	}

	/**
	 * When true indicates that this initializer
	 */
	private boolean staticc;

	public boolean isStatic() {
		return this.staticc;
	}

	public void setStatic(boolean staticc) {
		this.staticc = staticc;
	}

	/**
	 * The body of this initializer.
	 */
	private CodeBlock body;

	public CodeBlock getBody() {
		ObjectHelper.checkNotNull("field:body", body);
		return this.body;
	}

	public void setBody(final CodeBlock body) {
		ObjectHelper.checkNotNull("parameter:body", body);
		this.body = body;
	}

	/**
	 * The type that this method belongs too.
	 */
	private Type enclosingType;

	public Type getEnclosingType() {
		ObjectHelper.checkNotNull("field:enclosingType", enclosingType);
		return enclosingType;
	}

	protected boolean hasEnclosingType() {
		return this.enclosingType != null;
	}

	public void setEnclosingType(final Type enclosingType) {
		ObjectHelper.checkNotNull("field:enclosingType", enclosingType);
		this.enclosingType = enclosingType;
	}

	private GeneratorContext generatorContext;

	public GeneratorContext getGeneratorContext() {
		ObjectHelper.checkNotNull("field:generatorContext", generatorContext);
		return this.generatorContext;
	}

	public void setGeneratorContext(final GeneratorContext generatorContext) {
		ObjectHelper.checkNotNull("parameter:generatorContext", generatorContext);
		this.generatorContext = generatorContext;
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder();

		if (this.isStatic()) {
			builder.append("Static ");
		}

		builder.append("Initializer");

		if (this.hasEnclosingType()) {
			builder.append(' ');
			builder.append(this.getEnclosingType());
		}

		return builder.toString();
	}
}
