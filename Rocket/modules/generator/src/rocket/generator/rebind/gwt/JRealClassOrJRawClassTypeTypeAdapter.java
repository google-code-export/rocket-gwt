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
package rocket.generator.rebind.gwt;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.AbstractType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JConstructor;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JRawType;
import com.google.gwt.core.ext.typeinfo.JRealClassType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * Provides a Type view of the given JClassType
 * 
 * @author Miroslav Pokorny
 */
public class JRealClassOrJRawClassTypeTypeAdapter extends AbstractType {

	public JRealClassOrJRawClassTypeTypeAdapter( final JRealClassType jRealClassType, final TypeOracleGeneratorContext context ){
		super();
		
		this.setGeneratorContext(context);
		this.setJClassType(jRealClassType);
	}
	
	public JRealClassOrJRawClassTypeTypeAdapter( final JRawType jRawType, final TypeOracleGeneratorContext context ){
		super();
		
		this.setGeneratorContext(context);
		this.setJClassType(jRawType);
	}
	
	public Package getPackage() {
		return this.findPackage(this.getJClassType().getPackage().getName());
	}

	final protected Package findPackage(final String packageName) {
		return this.getTypeOracleGeneratorContext().findPackage(packageName);
	}
	
	public Visibility getVisibility() {
		Visibility visibility = null;

		while (true) {
			final JClassType type = this.getJClassType();
			if (type.isPrivate()) {
				visibility = Visibility.PRIVATE;
				break;
			}
			if (type.isProtected()) {
				visibility = Visibility.PROTECTED;
				break;
			}
			if (type.isPublic()) {
				visibility = Visibility.PUBLIC;
				break;
			}
			visibility = Visibility.PACKAGE_PRIVATE;
			break;
		}

		return visibility;
	}

	public String getSimpleName() {
		return this.getJClassType().getSimpleSourceName();
	}

	public String getName() {
		return this.getJClassType().getQualifiedSourceName();
	}

	public String getJsniNotation() {
		return this.getJClassType().getJNISignature();
	}

	@Override
	public Set<Type> getInterfaces() {
		return Collections.unmodifiableSet(super.getInterfaces());
	}

	@Override
	protected Set<Type> createInterfaces() {
		return this.getTypeOracleGeneratorContext().asTypes(this.getJClassType().getImplementedInterfaces());
	}

	public Set<Type> getSubType() {
		return Collections.unmodifiableSet(super.getSubTypes());
	}

	public Set<Type> getModifiableSubTypesList() {
		return super.getSubTypes();
	}

	@Override
	protected Set<Type> createSubTypes() {
		return this.getTypeOracleGeneratorContext().asTypes(this.getJClassType().getSubtypes());
	}
	
	protected TypeOracleGeneratorContext getTypeOracleGeneratorContext(){
		return (TypeOracleGeneratorContext) this.getGeneratorContext();
	}

	public Type getSuperType() {
		Type superType = null;
		final JType superJType = this.getJClassType().getSuperclass();
		if (null != superJType) {
			superType = this.getTypeOracleGeneratorContext().getType( superJType ); 
		}

		return superType;
	}
	
	/**
	 * Factory method which creates a set of constructors.
	 */
	protected Set<Constructor> createConstructors() {
		final Set<Constructor> constructors = new HashSet<Constructor>();

		final JConstructor[] jConstructors = this.getJClassType().getConstructors();
		for (int i = 0; i < jConstructors.length; i++) {
			constructors.add(this.createConstructor(jConstructors[i]));
		}

		return Collections.unmodifiableSet(constructors);
	}

	/**
	 * Factory method which creates a {@link JConstructorConstructorAdapter}
	 * from a {@link JConstructor}.
	 * 
	 * @param constructor
	 *            The source JConstructor
	 * @return
	 */
	protected Constructor createConstructor(final JConstructor constructor) {
		final JConstructorConstructorAdapter adapter = new JConstructorConstructorAdapter();
		adapter.setGeneratorContext(this.getGeneratorContext());
		adapter.setJConstructor(constructor);
		adapter.setEnclosingType(this);
		return adapter;
	}

	public boolean hasNoArgumentsConstructor() {
		return this.getJClassType().isDefaultInstantiable();
	}

	/**
	 * Factory method which creates a set of fields.
	 */
	protected Set<Field> createFields() {
		final Set<Field> fields = new HashSet<Field>();

		final JField[] jFields = this.getJClassType().getFields();
		for (int i = 0; i < jFields.length; i++) {
			fields.add(this.createField(jFields[i]));
		}

		return Collections.unmodifiableSet(fields);
	}

	/**
	 * Factory method which creates a {@link JFieldFieldAdapter} from a
	 * {@link JField}.
	 * 
	 * @param field
	 *            The source JField
	 * @return
	 */
	protected Field createField(final JField field) {
		Checker.notNull("parameter:field", field);

		final JFieldFieldAdapter adapter = new JFieldFieldAdapter();
		adapter.setGeneratorContext(this.getGeneratorContext());
		adapter.setJField(field);
		adapter.setEnclosingType(this);
		return adapter;
	}

	protected Set<Method> createMethods() {
		final Set<Method> methods = new LinkedHashSet<Method>();

		final JMethod[] jMethods = this.getJClassType().getMethods();
		for (int i = 0; i < jMethods.length; i++) {
			methods.add(this.createMethod(jMethods[i]));
		}

		return Collections.unmodifiableSet(methods);
	}

	/**
	 * Factory method which creates a {@link JMethodMethodAdapter} from a
	 * {@link JMethod}.
	 * 
	 * @param method
	 *            The source JMethod
	 * @return
	 */
	protected Method createMethod(final JMethod method) {
		final JMethodMethodAdapter adapter = new JMethodMethodAdapter();
		adapter.setGeneratorContext(this.getGeneratorContext());
		adapter.setJMethod(method);
		adapter.setEnclosingType(this);
		return adapter;
	}

	protected Set<Type> createNestedTypes() {
		return this.getTypeOracleGeneratorContext().asTypes(this.getJClassType().getNestedTypes());
	}

	public boolean isArray() {
		return false;
	}

	/**
	 * This method complains if this adapter is not referring to an actual array
	 * type.
	 * 
	 * This must be tested via {@link #sisArray }
	 */
	public Type getComponentType() {
		throw new UnsupportedOperationException("Only array types can have a component type");
	}

	public boolean isAbstract() {
		return this.getJClassType().isAbstract();
	}

	public boolean isFinal() {
		return this.getJClassType().isFinal();
	}

	public boolean isInterface() {
		return null != this.getJClassType().isInterface();
	}

	public boolean isPrimitive() {
		return false;
	}

	public boolean isAssignableFrom(final Type otherType) {
		boolean assignable = false;

		if (otherType instanceof JRealClassOrJRawClassTypeTypeAdapter) {
			final JRealClassOrJRawClassTypeTypeAdapter otherJClassTypeAdapter = (JRealClassOrJRawClassTypeTypeAdapter) otherType;
			assignable = this.getJClassType().isAssignableFrom(otherJClassTypeAdapter.getJClassType());
		} else {
			assignable = otherType.isAssignableTo(this);
		}
		return assignable;
	}

	public boolean isAssignableTo(final Type otherType) {
		boolean assignable = false;

		if (otherType instanceof JRealClassOrJRawClassTypeTypeAdapter) {
			final JRealClassOrJRawClassTypeTypeAdapter otherJClassTypeAdapter = (JRealClassOrJRawClassTypeTypeAdapter) otherType;
			assignable = this.getJClassType().isAssignableTo(otherJClassTypeAdapter.getJClassType());
		} else {
			assignable = otherType.isAssignableFrom(this);
		}
		return assignable;
	}

	public List<String> getMetadataValues(final String name) {
		return this.getAnnotationValues(this.getJClassType(), name);
	}

	public Type getWrapper() {
		return null;
	}

	/**
	 * The JClassType providing the source for type and related data.
	 */
	private JClassType jClassType;

	protected JClassType getJClassType() {
		Checker.notNull("field:jClassType", jClassType);
		return jClassType;
	}

	protected void setJClassType(final JClassType jClassType) {
		Checker.notNull("parameter:jClassType", jClassType);
		this.jClassType = jClassType;
	}

	@Override
	public String toString() {
		return "" + this.jClassType;
	}
}
