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

import java.util.Set;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.initializer.Initializer;
import rocket.util.client.Checker;

/**
 * Represents an anonymous inner class. Methods, Fields and more nested types
 * may be added.
 * 
 * @author Miroslav Pokorny
 */
public class NewAnonymousNestedTypeImpl extends NewTypeImpl implements NewAnonymousNestedType {

	public NewAnonymousNestedTypeImpl() {
		super();
	}

	public Visibility getVisibility() {
		return Visibility.PRIVATE;
	}

	private Type interfacee;

	protected Type getInterface() {
		Checker.notNull("field:interface", interfacee);
		return this.interfacee;
	}

	protected boolean hasInterface() {
		return null != interfacee;
	}

	protected void setInterface(final Type interfacee) {
		Checker.notNull("parameter:interface", interfacee);
		this.interfacee = interfacee;
	}

	protected void addInterface0(final Type interfacee) {
		if (this.hasSuperType() && false == this.getSuperType().equals(this.getObject()) || this.hasInterface()) {
			this.throwAnonymousNestedTypeException("Unable to add the interface " + interfacee.getName()
					+ " because anonymous inner classes can only extend or implement a single class.");
		}
		this.setInterface(interfacee);
	}

	@Override
	public void setSuperType(final Type superType) {
		if (this.hasInterface()) {
			this.throwAnonymousNestedTypeException("Unable to set the super type " + superType.getName()
					+ " because anonymous inner types can only extend or implement a single class.");
		}
		super.setSuperType(superType);
	}

	protected void throwAnonymousNestedTypeException(final String message) {
		throw new AnonymousNestedTypeException(message);
	}

	public void addInitializer(final Initializer initializer) {
		throw new UnsupportedOperationException("Initializers cannot be added to AnonymousNestedTypes");
	}

	public void addConstructor(final NewConstructor constructor) {
		throw new UnsupportedOperationException("Constructors cannot be added to AnonymousNestedTypes");
	}

	public NewConstructor newConstructor() {
		throw new UnsupportedOperationException("newConstructor");
	}

	/**
	 * Even though anonymous inner classes cant have constructors this method
	 * must be implemented to satisfy {@link AbstractType}
	 */
	protected Set createConstructors() {
		throw new UnsupportedOperationException("createConstructors");
	}

	/**
	 * Anonymous nested types are always invoked with no arguments but thats not
	 * technically true, throw an exception for now.
	 */
	public boolean hasNoArgumentsConstructor() {
		throw new UnsupportedOperationException("hasNoArgumentsConstructor");
	}

	public String getName() {
		throw new UnsupportedOperationException("getName()");
	}

	public boolean hasName() {
		return false;
	}

	public void setName(final String name) {
		throw new UnsupportedOperationException("setName()");
	}

	public String getJsniNotation() {
		throw new UnsupportedOperationException("getJsniNotation()");
	}

	/**
	 * Anonymous inner classes cannot be abstract
	 */
	public boolean isAbstract() {
		return false;
	}

	/**
	 * Anonymous inner classes cant be sub-classed.
	 */
	public boolean isFinal() {
		return true;
	}

	public void setAbstract(final boolean abstractt) {
		throw new UnsupportedOperationException();
	}

	public void setFinal(final boolean finall) {
		throw new UnsupportedOperationException();
	}

	public void setStatic(final boolean staticc) {
		throw new UnsupportedOperationException();
	}

	private Type enclosingType;

	public Type getEnclosingType() {
		Checker.notNull("field:enclosingType", enclosingType);
		return this.enclosingType;
	}

	public void setEnclosingType(Type enclosingType) {
		Checker.notNull("parameter:enclosingType", enclosingType);
		this.enclosingType = enclosingType;
	}

	public boolean isEmpty() {
		return false;
	}

	public void write(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final Type type = this.hasInterface() ? this.getInterface() : this.getSuperType();
		final String name = type.getName();
		writer.print(name);
		writer.println("(){");
		writer.indent();

		this.writeFields(writer);
		this.writeMethods(writer);
		this.writeNestedTypes(writer);

		writer.outdent();
		writer.println("}// " + name);
	}

	@Override
	public void addMetaData(final String name, final String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getComments() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setComments(final String comments) {
		throw new UnsupportedOperationException();
	}
}
