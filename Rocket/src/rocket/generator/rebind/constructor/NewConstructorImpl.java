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
package rocket.generator.rebind.constructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.constructorparameter.NewConstructorParameter;
import rocket.generator.rebind.constructorparameter.NewConstructorParameterImpl;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * Convenient base class for any new constructor
 * 
 * @author Miroslav Pokorny
 */
public class NewConstructorImpl extends AbstractConstructor implements NewConstructor {

	public NewConstructorImpl() {
		super();
	}

	public void setVisibility(final Visibility visibility) {
		super.setVisibility(visibility);
	}

	protected List createParameters() {
		return new ArrayList();
	}

	public NewConstructorParameter newParameter() {
		final NewConstructorParameterImpl parameter = new NewConstructorParameterImpl();
		parameter.setGeneratorContext(this.getGeneratorContext());

		this.addParameter(parameter);

		return parameter;
	}

	public void addParameter(final NewConstructorParameter parameter) {
		ObjectHelper.checkNotNull("parameter:parameter", parameter);

		this.getParameters().add(parameter);
		parameter.setEnclosingConstructor(this);
	}

	protected Set createThrownTypes() {
		return new HashSet();
	}

	public void addThrownType(final Type thrownTypes) {
		ObjectHelper.checkNotNull("thrownTypes:thrownTypes", thrownTypes);
		this.getThrownTypes().add(thrownTypes);
	}

	/**
	 * Generates the method
	 * 
	 * @param writer
	 */
	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		this.log();

		this.writeDeclaration(writer);

		this.writeBodyOpen(writer);

		writer.indent();
		this.writeBody(writer);
		writer.outdent();

		this.writeBodyClose(writer);
	}

	protected void log() {
		this.getGeneratorContext().branch("Constructor with parameters: " + this.getParameters());
	}

	/**
	 * Writes the method declaration of this constructor. ${visibility} ${name} (
	 * ${parameter-type parameter-name} )
	 * 
	 * @param writer
	 */
	protected void writeDeclaration(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		writer.print(this.getVisibility().getJavaName());
		writer.print(" ");
		writer.print(this.getEnclosingType().getSimpleName());
		writer.print("(");

		this.writeParameters(writer);

		writer.print(")");

		GeneratorHelper.writeThrownTypes(this.getThrownTypes(), writer);
	}

	/**
	 * Writes out the parameters belonging to this method as a comma separated
	 * list.
	 * 
	 * @param writer
	 */
	protected void writeParameters(final SourceWriter writer) {
		GeneratorHelper.writeClassComponents(this.getParameters(), writer, true, false);
	}

	protected void writeBodyOpen(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		writer.println("{");
	}

	/**
	 * Writes the body of this constructor.
	 * 
	 * @param writer
	 */
	public void writeBody(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final CodeBlock body = this.getBody();
		if (false == body.isEmpty()) {
			GeneratorHelper.writeClassComponent(body, writer);
		}
	}

	protected void writeBodyClose(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		writer.println("} // " + this.getEnclosingType().getName());
	}

	/**
	 * A code block which may or may not statements etc that hold the body of
	 * this constructor
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

	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NewConstructor ");

		if (this.hasEnclosingType()) {
			builder.append(this.getEnclosingType());
			builder.append(' ');
		}

		if (this.hasParameters()) {
			final Iterator parameters = this.getParameters().iterator();
			while (parameters.hasNext()) {
				final NewConstructorParameter parameter = (NewConstructorParameter) parameters.next();
				builder.append(parameter.getType());

				if (parameters.hasNext()) {
					builder.append(", ");
				}
			}

			builder.append(this.getParameters());
		}

		return builder.toString();
	}
}
