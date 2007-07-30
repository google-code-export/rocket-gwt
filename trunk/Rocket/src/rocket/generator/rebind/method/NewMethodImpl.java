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
package rocket.generator.rebind.method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameterImpl;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * Represents a new method that will be added to a new class being built
 * 
 * @author Miroslav Pokorny
 */
public class NewMethodImpl extends AbstractMethod implements NewMethod {

	public NewMethodImpl() {
		super();
	}

	/**
	 * When true indicates that this method is abstract
	 */
	private boolean abstractt;

	public boolean isAbstract() {
		return abstractt;
	}

	public void setAbstract(final boolean abstractt) {
		this.abstractt = abstractt;
	}

	/**
	 * When true indicates that this method is final
	 */
	private boolean finall;

	public boolean isFinal() {
		return finall;
	}

	public void setFinal(final boolean finall) {
		this.finall = finall;
	}

	/**
	 * When true indicates that this method is static
	 */
	private boolean staticc;

	public boolean isStatic() {
		return staticc;
	}

	public void setStatic(final boolean staticc) {
		this.staticc = staticc;
	}

	/**
	 * When true indicates that this method is native
	 */
	private boolean nativee;

	public boolean isNative() {
		return nativee;
	}

	public void setNative(final boolean nativee) {
		this.nativee = nativee;
	}

	public void setVisibility(final Visibility visibility) {
		super.setVisibility(visibility);
	}

	/**
	 * The name of the method.
	 */
	private String name;

	public String getName() {
		GeneratorHelper.checkJavaMethodName("field:name", name);
		return name;
	}

	protected boolean hasName() {
		return null != name;
	}

	public void setName(final String name) {
		GeneratorHelper.checkJavaMethodName("parameter:name", name);
		this.name = name;
	}

	protected List createParameters() {
		return new ArrayList();
	}

	public NewMethodParameter newParameter() {
		final NewMethodParameterImpl parameter = new NewMethodParameterImpl();
		parameter.setGeneratorContext(this.getGeneratorContext());

		this.addParameter(parameter);

		return parameter;
	}

	public void addParameter(final NewMethodParameter parameter) {
		ObjectHelper.checkNotNull("parameter:parameter", parameter);

		this.getParameters().add(parameter);
		parameter.setEnclosingMethod(this);
	}

	protected Set createThrownTypes() {
		return new HashSet();
	}

	public void addThrownTypes(final Type thrownTypes) {
		ObjectHelper.checkNotNull("thrownTypes:thrownTypes", thrownTypes);
		this.getThrownTypes().add(thrownTypes);
	}

	public void setReturnType(final Type returnType) {
		super.setReturnType(returnType);
	}

	/**
	 * A code block which may or may not statements etc that hold the body of
	 * this method
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
	 * Writes the method in its entirety to the provided SourceWriter
	 * 
	 * @param writer
	 */
	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		this.writeLogger();

		this.writeDeclaration(writer);

		this.writeBodyOpen(writer);

		writer.indent();
		this.writeBody(writer);
		writer.outdent();

		this.writeBodyClose(writer);
	}

	protected void writeLogger() {
		this.getGeneratorContext().debug("Writing " + this );
	}

	/**
	 * Writes the method declaration of this method. final static ${visibility}
	 * ${name} ( ${parameter-type parameter-name} )
	 * 
	 * @param writer
	 */
	protected void writeDeclaration(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		if (this.isFinal()) {
			writer.print("final ");
		}
		if (this.isStatic()) {
			writer.print("static ");
		}
		if (this.isNative()) {
			writer.print("native ");
		}

		writer.print(this.getVisibility().getJavaName());
		writer.print(" ");
		writer.print(this.getReturnType().getName());
		writer.print(" ");
		writer.print(this.getName());
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

		if (this.isNative()) {
			writer.println("/*-{");
		} else {
			writer.println("{");
		}
	}

	/**
	 * Writes the body of this method.
	 * 
	 * @param writer
	 */
	public void writeBody(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final CodeBlock body = this.getBody();
		if (false == body.isEmpty()) {
			writer.indent();
			body.write(writer);
			writer.outdent();
		}
	}

	protected void writeBodyClose(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		if (this.isNative()) {
			writer.println("}-*/;");
		} else {
			writer.println("} // " + this.getName());
		}
	}

	public List getMetadataValues(final String name) {
		return Collections.EMPTY_LIST;
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NewMethod ");
		
		if( this.hasReturnType() ){
			builder.append( this.getReturnType() );
			builder.append( ' ');
		}
		
		if (this.hasEnclosingType()) {
			builder.append(this.getEnclosingType());
			builder.append(' ');
		}

		if (this.hasName()) {
			builder.append(this.getName());
		}

		builder.append('(');		
		if (this.hasParameters()) {
			final Iterator parameters = this.getParameters().iterator();
			while (parameters.hasNext()) {
				final NewMethodParameter parameter = (NewMethodParameter) parameters.next();
				builder.append(parameter.getType());

				if (parameters.hasNext()) {
					builder.append(", ");
				}
			}
		}
		builder.append( ')');
		
		return builder.toString();
	}
}
