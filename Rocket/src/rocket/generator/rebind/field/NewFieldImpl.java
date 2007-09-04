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
package rocket.generator.rebind.field;

import java.util.List;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * A holder for a new field.
 * 
 * @author Miroslav Pokorny
 */
public class NewFieldImpl extends AbstractField implements NewField {

	public NewFieldImpl() {
	}

	public void setVisibility(final Visibility visibility) {
		super.setVisibility(visibility);
	}

	/**
	 * When true indicates that this field is final.
	 */
	private boolean finall;

	public boolean isFinal() {
		return finall;
	}

	public void setFinal(final boolean finall) {
		this.finall = finall;
	}

	/**
	 * WHen true indicates that this field is static.
	 */
	private boolean staticc;

	public boolean isStatic() {
		return staticc;
	}

	public void setStatic(final boolean staticc) {
		this.staticc = staticc;
	}

	/**
	 * A flag that indicates this field is transient
	 */
	private boolean transientt;

	public boolean isTransient() {
		return transientt;
	}

	public void setTransient(final boolean transientt) {
		this.transientt = transientt;
	}

	/**
	 * The field type
	 */
	private Type type;

	public Type getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return type;
	}

	public void setType(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);
		this.type = type;
	}

	/**
	 * The name of this field.
	 */
	private String name;

	public String getName() {
		GeneratorHelper.checkJavaFieldName("field:name", name);
		return name;
	}

	public void setName(final String name) {
		GeneratorHelper.checkJavaFieldName("parameter:name", name);
		this.name = name;
	}

	public String getJsniNotation() {
		return '@' + this.getEnclosingType().getName() + "::" + this.getName();
	}

	/**
	 * Generated fields never have meta data associated with them.
	 */
	public List getMetadataValues(String name) {
		return null;
	}

	/**
	 * A code block which may or may not contain a literal, method etc that sets
	 * a value upon this field.
	 */
	private CodeBlock value;

	public CodeBlock getValue() {
		ObjectHelper.checkNotNull("field:value", value);
		return this.value;
	}

	public void setValue(final CodeBlock value) {
		ObjectHelper.checkNotNull("parameter:value", value);
		this.value = value;
	}

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		this.writeLogger();

		this.writeDeclaration(writer);
		this.writeValue(writer);
		writer.println(";");
	}

	protected void writeLogger() {
		this.getGeneratorContext().debug("field " + this.getType().getName() + ": " + this.getName());
	}

	protected void writeDeclaration(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		if (this.isFinal()) {
			writer.print("final ");
		}
		if (this.isStatic()) {
			writer.print("static ");
		}
		if (this.isTransient()) {
			writer.print("transient ");
		}
		writer.print(this.getVisibility().getJavaName());
		writer.print(" ");
		writer.print(this.getType().getName());
		writer.print(" ");
		writer.print(this.getName());
	}

	protected void writeValue(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final CodeBlock codeBlock = this.getValue();
		if (false == codeBlock.isEmpty()) {
			writer.print("=");
			codeBlock.write(writer);
		}
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NewFieldImpl ");

		if (null != this.type) {
			builder.append(type.getName());
			builder.append(' ');
		}
		builder.append(this.name);
		return builder.toString();
	}
}
