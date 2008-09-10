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
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.AbstractType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;
import rocket.util.client.Tester;

import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * An adapter between the Type and GWT JArrayType classes.
 * 
 * Array types arent proper types so they dont have constructors, fields,
 * implement interfaces, methods or contain nested methods therefore many of
 * these methods throw an {@link UnsupportedOperationException} when attempts
 * are made to access them.
 * 
 * @author Miroslav Pokorny
 */
public class JArrayTypeTypeAdapter extends AbstractType {

	public JArrayTypeTypeAdapter( final JArrayType jArrayType, final TypeOracleGeneratorContext context ){
		super();
		
		this.setGeneratorContext(context);
		this.setJArrayType(jArrayType);
	}
	
	public Visibility getVisibility() {
		return Visibility.PUBLIC;
	}

	/**
	 * Array types are never abstract
	 */
	public boolean isAbstract() {
		return false;
	}

	public boolean isFinal() {
		return false;
	}

	public boolean isInterface() {
		return false;
	}

	public boolean isPrimitive() {
		return false;
	}
	
	public String getJsniNotation() {
		return this.getJArrayType().getJNISignature();
	}

	public String getName() {
		return this.getJArrayType().getQualifiedSourceName();
	}


	public String getSimpleName() {
		return this.getJArrayType().getSimpleSourceName();
	}

	/**
	 * Returns the runtime name of the class. This method is only necessary due
	 * to the use of dollar signs "$" within inner classes rather than dot ".".
	 */
	public String getRuntimeName() {
		// for java.lang.String array the runtime name or signature is
		// [Ljava.lang.String;
		// for a two dimensioned String array the runtime name is
		// [[Ljava.lang.String;
		final Type componentType = this.getComponentType();
		final boolean primitiveComponentType = componentType.isPrimitive();

		final StringBuffer runtimeName = new StringBuffer();
		final JArrayType jArrayType = this.getJArrayType();

		// prefix a [ for each rank.
		final int rank = jArrayType.getRank();
		for (int i = 0; i < rank; i++) {
			runtimeName.append('[');
		}

		if (false == primitiveComponentType) {
			runtimeName.append("L");
		}

		// insert the name.
		final String name = componentType.getRuntimeName();
		final Package packagee = componentType.getPackage();
		final String packageName = null == packagee ? null : packagee.getName();
		String nameLessPackageName = name;

		if (false == Tester.isNullOrEmpty(packageName)) {
			runtimeName.append(packageName);
			runtimeName.append('.');

			nameLessPackageName = name.substring(packageName.length() + 1);
		}

		nameLessPackageName = nameLessPackageName.replace('.', '$');
		runtimeName.append(nameLessPackageName);

		// append a semi-colon
		if (false == primitiveComponentType) {
			runtimeName.append(';');
		}

		return runtimeName.toString();
	}

	/**
	 * Array types always extend Object
	 */
	public Type getSuperType() {
		return this.getObject();
	}

	/**
	 * Arrays cant be sub classed
	 */
	public Set<Type> getSubTypes() {
		return Collections.<Type>emptySet();
	}

	protected Set<Type> createSubTypes() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Arrays dont implement interfaces
	 */
	public Set<Type> getInterfaces() {
		return Collections.<Type>emptySet();
	}

	@Override
	protected Set<Type> createInterfaces() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Arrays dont have constructors.
	 */
	public Set<Constructor> getConstructors() {
		return Collections.<Constructor>emptySet();
	}

	protected Set<Constructor> createConstructors() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Arrays dont have fields, dont worry about length its a special case
	 */

	public Set<Field> getFields() {
		return Collections.<Field>emptySet();
	}

	protected Set<Field> createFields() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Arrays dont have methods
	 */
	public Set<Method> getMethods() {
		return Collections.<Method>emptySet();
	}

	@Override
	protected Set<Method> createMethods() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Arrays cant have nested types.
	 */
	public Set<Type> getNestedTypes() {
		return Collections.<Type>emptySet();
	}

	protected Set<Type> createNestedTypes() {
		throw new UnsupportedOperationException();
	}

	public boolean isArray() {
		return true;
	}

	/**
	 * All array types have a component type.
	 */
	private Type componentType;

	public Type getComponentType() {
		if (false == hasComponentType()) {
			this.setComponentType(this.createComponentType());
		}
		Checker.notNull("field:componentType", componentType);
		return componentType;
	}

	protected boolean hasComponentType() {
		return null != this.componentType;
	}

	protected void setComponentType(final Type componentType) {
		Checker.notNull("parameter:componentType", componentType);
		this.componentType = componentType;
	}

	protected Type createComponentType() {
		final JArrayType array = this.getJArrayType().isArray();
		Checker.notNull("The " + this.getName() + " is an array.", array);

		final JType componentType = array.getComponentType();
		return this.getTypeOracleGeneratorContext().getType( componentType );
	}

	public Package getPackage() {
		return this.getComponentType().getPackage();
	}

	final protected Package findPackage(final String packageName) {
		return this.getTypeOracleGeneratorContext().findPackage(packageName);
	}
	
	/**
	 * Array types dont have a wrapper type
	 */
	public Type getWrapper() {
		return null;
	}

	/**
	 * Array types can only be assigned from their own type without casting.
	 */
	public boolean isAssignableFrom(final Type type) {
		return this.equals(type);
	}

	/**
	 * Array types can only be assigned to their own type or Object
	 */
	public boolean isAssignableTo(final Type type) {
		return this.equals(type) || type.equals(this.getObject());
	}

	/**
	 * Array types never have annotations as they are actually created at
	 * runtime by the runtime and not taken from source.
	 */
	public List<String> getMetadataValues(String name) {
		return null;
	}

	/**
	 * The JArrayType providing the source for type and related data.
	 */
	private JArrayType jArrayType;

	protected JArrayType getJArrayType() {
		Checker.notNull("field:jArrayType", jArrayType);
		return jArrayType;
	}

	protected void setJArrayType(final JArrayType jArrayType) {
		Checker.notNull("parameter:jArrayType", jArrayType);
		this.jArrayType = jArrayType;
	}

	protected TypeOracleGeneratorContext getTypeOracleGeneratorContext() {
		return (TypeOracleGeneratorContext) this.getGeneratorContext();
	}

	@Override
	public String toString() {
		return "" + this.jArrayType;
	}
}
