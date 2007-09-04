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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.field.NewFieldImpl;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.method.NewMethodImpl;
import rocket.generator.rebind.packagee.Package;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * A convenient base class for any type being generated.
 * 
 * @author Miroslav Pokorny
 */
abstract public class NewTypeImpl extends AbstractType implements NewType {

	public NewTypeImpl() {
		super();
	}

	public Package getPackage() {
		Package packagee = null;

		String name = this.getName();
		while (true) {
			final int lastDot = name.lastIndexOf('.');
			if (-1 == lastDot) {
				break;
			}
			name = name.substring(0, lastDot);
			packagee = this.findPackage(name);
			if (null != packagee) {
				break;
			}
		}

		return packagee;
	}

	public String getSimpleName() {
		String name = this.getName();
		final Package packagee = this.getPackage();
		return packagee.isUnnamed() ? name : name.substring(packagee.getName().length() + 1);
	}

	protected Set createInterfaces() {
		return new HashSet();
	}

	public void addInterface(final Type interfacee) {
		ObjectHelper.checkNotNull("parameter:interface", interfacee);

		if (false == interfacee.isInterface()) {
			throwNotAnInterfaceException(interfacee);
		}

		this.addInterface0(interfacee);
	}

	/**
	 * Sub-classes must implement this method
	 * 
	 * @param interfacee
	 */
	abstract protected void addInterface0(Type interfacee);

	protected void throwNotAnInterfaceException(final Type interfacee) {
		throw new NotAnInterfaceException(interfacee.getName() + " is not an interface.");
	}

	protected Set createConstructors() {
		return new HashSet();
	}

	protected Set createFields() {
		return new HashSet();
	}

	public void addField(final NewField field) {
		ObjectHelper.checkNotNull("parameter:field", field);

		this.getFields().add(field);
		field.setEnclosingType(this);
	}

	public NewField newField() {
		final NewFieldImpl field = new NewFieldImpl();
		field.setGeneratorContext(this.getGeneratorContext());

		this.addField(field);

		return field;
	}

	protected Set createMethods() {
		return new HashSet();
	}

	public void addMethod(final NewMethod method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		this.getMethods().add(method);
		method.setEnclosingType(this);
	}

	public NewMethod newMethod() {
		final NewMethodImpl method = new NewMethodImpl();
		method.setGeneratorContext(this.getGeneratorContext());

		this.addMethod(method);

		return method;
	}

	protected Set createNestedTypes() {
		return new HashSet();
	}

	/**
	 * Generated types never have sub types.
	 */
	protected Set createSubTypes() {
		return Collections.EMPTY_SET;
	}

	/**
	 * Generated types are never arrays.
	 */
	public Type getComponentType() {
		return null;
	}

	/**
	 * The super type or type that this new class extends.
	 */
	private Type superType;

	public Type getSuperType() {
		ObjectHelper.checkNotNull("field:superType", superType);
		return this.superType;
	}

	public boolean hasSuperType() {
		return null != superType;
	}

	public void setSuperType(final Type superType) {
		ObjectHelper.checkNotNull("parameter:superType", superType);
		this.superType = superType;
	}

	public NewNestedType newNestedType() {
		final NewNestedTypeImpl type = new NewNestedTypeImpl();
		final GeneratorContext context = this.getGeneratorContext();
		type.setGeneratorContext(context);
		type.setSuperType(context.getObject());

		this.addNestedType(type);
		return type;
	}

	public void addNestedType(final NewNestedType nestedType) {
		ObjectHelper.checkNotNull("parameter:nestedType", nestedType);

		final NewNestedTypeImpl newNestedTypeImpl = (NewNestedTypeImpl) nestedType;
		this.getNestedTypes().add(nestedType);
		newNestedTypeImpl.setEnclosingType(this);

		this.getGeneratorContext().addNewType(nestedType);
	}

	public NewAnonymousNestedType newAnonymousNestedType() {
		final NewAnonymousNestedTypeImpl type = new NewAnonymousNestedTypeImpl();
		final GeneratorContext context = this.getGeneratorContext();
		type.setGeneratorContext(context);
		type.setEnclosingType(type);
		type.setSuperType(context.getObject());
		context.addNewType(type);
		return type;
	}

	/**
	 * Generated types are concrete, only primitives have wrappers.
	 */
	public Type getWrapper() {
		return null;
	}

	/**
	 * Generated types are never an array type.
	 */
	public boolean isArray() {
		return false;
	}

	public boolean isAssignableFrom(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		boolean assignable = false;

		while (true) {
			if (this.equals(type)) {
				break;
			}

			assignable = this.getInterfaces().contains(type);
			if (assignable) {
				break;
			}

			assignable = this.getSuperType().isAssignableFrom(type);
			break;
		}

		return assignable;
	}

	public boolean isAssignableTo(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		boolean assignable = false;

		while (true) {
			if (this.equals(type)) {
				assignable = true;
				break;
			}

			if (type.equals(this.getType(Object.class.getName()))) {
				assignable = true;
				break;
			}

			assignable = this.getInterfaces().contains(type);
			if (assignable) {
				break;
			}

			assignable = this.getSuperType().isAssignableTo(type);
			break;
		}

		return assignable;
	}

	/**
	 * Generated types must be concrete.
	 */
	public boolean isInterface() {
		return false;
	}

	/**
	 * Generated types must be concrete classes and not primitives
	 */
	public boolean isPrimitive() {
		return false;
	}

	/**
	 * Generated types by definition never have meta data values.
	 */
	public List getMetadataValues(String name) {
		return null;
	}

	protected void writeConstructors(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final Set constructors = this.getConstructors();

		writer.beginJavaDocComment();
		writer.print("Constructors (" + constructors.size() + ")");
		writer.endJavaDocComment();

		writer.println();
		GeneratorHelper.writeClassComponents(constructors, writer, false, true);
		writer.println();
	}

	protected void writeFields(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final Set fields = this.getFields();

		writer.beginJavaDocComment();
		writer.print("Fields (" + fields.size() + ")");
		writer.endJavaDocComment();

		writer.println();
		GeneratorHelper.writeClassComponents(fields, writer, false, true);
		writer.println();
	}

	protected void writeMethods(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final Set methods = this.getMethods();

		writer.beginJavaDocComment();
		writer.print("Methods (" + methods.size() + ")");
		writer.endJavaDocComment();

		writer.println();
		GeneratorHelper.writeClassComponents(methods, writer, false, true);
		writer.println();
	}

	protected void writeNestedTypes(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final Set nestedTypes = this.getNestedTypes();

		writer.beginJavaDocComment();
		writer.print("Nested Types (" + nestedTypes.size() + ")");
		writer.endJavaDocComment();

		writer.println();
		GeneratorHelper.writeClassComponents(nestedTypes, writer, false, true);
		writer.println();
	}

	protected void throwTypeAlreadyExistsException() {
		throw new TypeAlreadyExistsException("A type with the name [" + this.getName() + "] already exists, code generation failed.");
	}
}
