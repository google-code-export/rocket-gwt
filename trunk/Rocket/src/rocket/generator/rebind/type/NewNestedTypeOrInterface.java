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
package rocket.generator.rebind.type;

import java.util.Iterator;

import rocket.util.client.ObjectHelper;

import rocket.generator.rebind.SourceWriter;

/**
 * Convenient base class for the nested type and nested interface classes.
 * 
 * @author Miroslav Pokorny
 */
abstract class NewNestedTypeOrInterface extends NewConcreteNestedTypeOrInterfaceType {
	/**
	 * The outter class containing this nested class.
	 */
	private Type enclosingType;

	public Type getEnclosingType() {
		ObjectHelper.checkNotNull("field:enclosingType", enclosingType);
		return this.enclosingType;
	}

	public void setEnclosingType(Type enclosingType) {
		ObjectHelper.checkNotNull("parameter:enclosingType", enclosingType);
		this.enclosingType = enclosingType;
	}

	/**
	 * When true indicates that this class is static
	 */
	private boolean staticc;

	public boolean isStatic() {
		return staticc;
	}

	public void setStatic(final boolean staticc) {
		this.staticc = staticc;
	}

	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		this.log();

		this.writeDeclaration(writer);

		writer.indent();
		this.writeInitializers(writer);
		this.writeConstructors(writer);
		this.writeFields(writer);
		this.writeMethods(writer);
		this.writeNestedTypes(writer);
		writer.outdent();

		writer.println("} // " + this.getName());
	}

	protected void writeDeclaration(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		if (this.isStatic()) {
			writer.print("static ");
		}
		if (this.isAbstract()) {
			writer.print("abstract ");
		}
		if (this.isFinal()) {
			writer.print("final ");
		}

		writer.print(this.getVisibility().getJavaName());
		writer.print(" ");
		writer.print(this.isInterface() ? "interface" : "class");
		writer.print(" ");

		String name = this.getName();
		final int lastDot = name.lastIndexOf('.');
		name = name.substring(lastDot + 1);
		writer.print(name);

		if (this.hasSuperType()) {
			final Type superType = this.getSuperType();
			if (false == superType.equals(superType.getGeneratorContext().getObject())) {
				writer.print(" extends ");
				writer.print(superType.getName());
			}
		}

		int i = 0;
		final Iterator interfaces = this.getInterfaces().iterator();
		while (interfaces.hasNext()) {
			if (0 == i) {
				writer.print(" implements ");
			}
			i++;
			final Type interfacee = (Type) interfaces.next();
			writer.print(interfacee.getName());

			if (interfaces.hasNext()) {
				writer.print(",");
			}
		}
		writer.println("{");
	}

	protected void log() {
		this.getGeneratorContext().branch(
				"Writing nested " + (this.isInterface() ? "interface" : "class") + this.getName() + " enclosed type "
						+ this.getEnclosingType().getName());
	}
}
