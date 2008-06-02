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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.field.NewFieldImpl;
import rocket.generator.rebind.initializer.Initializer;
import rocket.generator.rebind.initializer.InitializerImpl;
import rocket.generator.rebind.metadata.MetaData;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.method.NewMethodImpl;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.util.ConstructorComparator;
import rocket.generator.rebind.util.FieldComparator;
import rocket.generator.rebind.util.MethodComparator;
import rocket.generator.rebind.util.TypeComparator;
import rocket.util.client.Checker;

/**
 * A convenient base class for any type being generated.
 * 
 * @author Miroslav Pokorny
 */
abstract public class NewTypeImpl extends AbstractType implements NewType {

	public NewTypeImpl() {
		super();
	}

	public Initializer newInitializer() {
		final InitializerImpl initializer = new InitializerImpl();
		initializer.setEnclosingType(this);
		initializer.setGeneratorContext(this.getGeneratorContext());
		this.addInitializer(initializer);
		return initializer;
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

	final protected Package findPackage(final String packageName) {
		return this.getGeneratorContextImpl().findPackage(packageName);
	}

	protected GeneratorContextImpl getGeneratorContextImpl() {
		return (GeneratorContextImpl) this.getGeneratorContext();
	}

	public String getSimpleName() {
		String name = this.getName();
		final Package packagee = this.getPackage();
		return packagee.isUnnamed() ? name : name.substring(packagee.getName().length() + 1);
	}

	protected Set<Type> createInterfaces() {
		return new HashSet<Type>();
	}

	public void addInterface(final Type interfacee) {
		Checker.notNull("parameter:interface", interfacee);

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

	protected Set<Constructor> createConstructors() {
		return new HashSet<Constructor>();
	}

	protected Set<Field> createFields() {
		return new HashSet<Field>();
	}

	public void addField(final NewField field) {
		Checker.notNull("parameter:field", field);

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
		Checker.notNull("parameter:method", method);

		this.getMethods().add(method);
		method.setEnclosingType(this);
	}

	public NewMethod newMethod() {
		final NewMethodImpl method = new NewMethodImpl();
		method.setGeneratorContext(this.getGeneratorContext());

		this.addMethod(method);

		return method;
	}

	protected Set<Type> createNestedTypes() {
		return new HashSet<Type>();
	}

	/**
	 * Generated types never have sub types.
	 */
	protected Set<Type> createSubTypes() {
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
		Checker.notNull("field:superType", superType);
		return this.superType;
	}

	public boolean hasSuperType() {
		return null != superType;
	}

	public void setSuperType(final Type superType) {
		Checker.notNull("parameter:superType", superType);
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

	public NewNestedInterfaceType newNestedInterfaceType() {
		final NewNestedInterfaceTypeImpl type = new NewNestedInterfaceTypeImpl();
		final GeneratorContext context = this.getGeneratorContext();
		type.setGeneratorContext(context);
		type.setSuperType(context.getObject());

		this.addNestedInterfaceType(type);
		return type;
	}

	public void addNestedType(final NewNestedType nestedType) {
		Checker.notNull("parameter:nestedType", nestedType);

		final NewNestedTypeImpl newNestedTypeImpl = (NewNestedTypeImpl) nestedType;
		this.getNestedTypes().add(nestedType);
		newNestedTypeImpl.setEnclosingType(this);

		this.getGeneratorContext().addType(nestedType);
	}

	public void addNestedInterfaceType(final NewNestedInterfaceType nestedType) {
		Checker.notNull("parameter:nestedType", nestedType);

		final NewNestedInterfaceTypeImpl newNestedTypeImpl = (NewNestedInterfaceTypeImpl) nestedType;
		this.getNestedTypes().add(nestedType);
		newNestedTypeImpl.setEnclosingType(this);

		this.getGeneratorContext().addType(nestedType);
	}

	public NewAnonymousNestedType newAnonymousNestedType() {
		final NewAnonymousNestedTypeImpl type = new NewAnonymousNestedTypeImpl();
		final GeneratorContext context = this.getGeneratorContext();
		type.setGeneratorContext(context);
		type.setEnclosingType(type);
		type.setSuperType(context.getObject());
		context.addType(type);
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
		Checker.notNull("parameter:type", type);

		boolean assignable = false;

		while (true) {
			if (this.equals(type)) {
				assignable = true;
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
		Checker.notNull("parameter:type", type);

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

	protected void logSuperType() {
		final GeneratorContext context = this.getGeneratorContext();
		if (context.isDebugEnabled()) {
			final Type superType = this.getSuperType();
			final Type object = context.getObject();
			if (false == superType.equals(object)) {
				context.debug("extends " + this.getSuperType().getName());
			}
		}
	}

	protected void logImplementedInterfaces() {
		final GeneratorContext context = this.getGeneratorContext();
		if (context.isDebugEnabled()) {

			final Set interfaces = this.getInterfaces();
			if (false == interfaces.isEmpty()) {
				context.branch();
				context.debug("implements");

				final Iterator interfacesIterator = interfaces.iterator();
				while (interfacesIterator.hasNext()) {
					final Type interfacee = (Type) interfacesIterator.next();
					context.debug(interfacee.getName());
				}

				context.unbranch();
			}
		}
	}

	protected void writeConstructors(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final Set constructors = this.getConstructors();
		if (false == constructors.isEmpty()) {

			final Set sorted = new TreeSet(ConstructorComparator.INSTANCE);
			sorted.addAll(constructors);

			final GeneratorContext context = this.getGeneratorContext();
			context.branch();

			final String message = "Constructors";
			context.debug(message);

			writer.beginJavaDocComment();
			writer.print(message);
			writer.endJavaDocComment();

			writer.println();
			GeneratorHelper.writeClassComponents(sorted, writer, false, true);
			writer.println();

			context.unbranch();
		}
	}

	protected void writeFields(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final Set fields = this.getFields();
		if (false == fields.isEmpty()) {
			final Set sorted = new TreeSet(FieldComparator.INSTANCE);
			sorted.addAll(fields);

			final GeneratorContext context = this.getGeneratorContext();
			context.branch();

			final String message = "Fields";
			context.debug(message);

			writer.beginJavaDocComment();
			writer.print(message);
			writer.endJavaDocComment();

			writer.println();
			GeneratorHelper.writeClassComponents(sorted, writer, false, true);
			writer.println();

			context.unbranch();
		}
	}

	protected void writeMethods(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final Set methods = this.getMethods();
		if (false == methods.isEmpty()) {

			final Set sorted = new TreeSet(MethodComparator.INSTANCE);
			sorted.addAll(methods);

			final GeneratorContext context = this.getGeneratorContext();
			context.branch();

			final String message = "Methods";
			context.debug(message);

			writer.beginJavaDocComment();
			writer.print(message);
			writer.endJavaDocComment();

			writer.println();
			GeneratorHelper.writeClassComponents(sorted, writer, false, true);
			writer.println();

			context.unbranch();
		}
	}

	protected void writeNestedTypes(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final Set types = this.getNestedTypes();
		if (false == types.isEmpty()) {

			final Set sorted = new TreeSet(TypeComparator.INSTANCE);
			sorted.addAll(types);

			final GeneratorContext context = this.getGeneratorContext();
			context.branch();

			final String message = "Nested Types";
			context.debug(message);

			writer.beginJavaDocComment();
			writer.print(message);
			writer.endJavaDocComment();

			writer.println();
			GeneratorHelper.writeClassComponents(sorted, writer, false, true);
			writer.println();

			context.unbranch();
		}
	}

	protected void throwTypeAlreadyExistsException() {
		throw new TypeAlreadyExistsException("A type with the name \"" + this.getName() + "\" already exists, code generation failed.");
	}

	protected void writeComments(final SourceWriter writer) {
		GeneratorHelper.writeComments(this.getComments(), this.getMetaData(), writer);
	}

	/**
	 * Any text which will appear within javadoc comments for this field.
	 */
	private String comments;

	public String getComments() {
		Checker.notNull("field:comments", comments);
		return comments;
	}

	public void setComments(final String comments) {
		Checker.notNull("parameter:comments", comments);
		this.comments = comments;
	}

	public void addMetaData(final String name, final String value) {
		this.getMetaData().add(name, value);
	}

	public List<String> getMetadataValues(final String name) {
		return this.getMetaData().getMetadataValues(name);
	}

	/**
	 * A container which holds any meta data that is added to a new field
	 * instance.
	 */
	private MetaData metaData;

	protected MetaData getMetaData() {
		Checker.notNull("field:metaData", metaData);
		return this.metaData;
	}

	protected void setMetaData(final MetaData metaData) {
		Checker.notNull("field:metaData", metaData);
		this.metaData = metaData;
	}

	protected MetaData createMetaData() {
		return new MetaData();
	}
}
