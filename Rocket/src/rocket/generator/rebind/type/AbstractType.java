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

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructor.ConstructorNotFoundException;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.field.FieldNotFoundException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.MethodNotFoundException;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.util.AbstractClassComponent;
import rocket.generator.rebind.visitor.SuperTypesVisitor;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Abstract class that includes facilities for implementing a type.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractType extends AbstractClassComponent implements Type {

	/**
	 * Returns the runtime name of the class. This method is only necessary due to the use of dollar signs "$"
	 * within inner classes rather than dot ".".
	 */
	public String getRuntimeName(){
		final StringBuffer runtimeName = new StringBuffer();
		final String name = this.getName();
		final Package packagee = this.getPackage();
		final String packageName = null == packagee ? null : packagee.getName();
		String nameLessPackageName = name;
		
		if( false == StringHelper.isNullOrEmpty( packageName ) ){
			runtimeName.append( packageName );
			runtimeName.append( '.');
			
			nameLessPackageName = name.substring( packageName.length() + 1 );
		}
		
		nameLessPackageName = nameLessPackageName.replace( '.', '$');
		runtimeName.append( nameLessPackageName );
		
		return runtimeName.toString();
	}
	
	/**
	 * A lazy loaded set containing all the interfaces implemented by this type
	 */
	private Set interfaces;

	public Set getInterfaces() {
		if (false == hasInterfaces()) {
			this.setInterfaces(this.createInterfaces());
		}
		return this.interfaces;
	}

	protected boolean hasInterfaces() {
		return this.interfaces != null;
	}

	protected void setInterfaces(final Set interfaces) {
		ObjectHelper.checkNotNull("parameter:interfaces", interfaces);
		this.interfaces = interfaces;
	}

	abstract protected Set createInterfaces();

	/**
	 * A lazy loaded set containing all the declared constructor for this type.
	 */
	private Set constructors;

	public Set getConstructors() {
		if (false == this.hasConstructors()) {
			this.setConstructors(this.createConstructors());
		}

		return this.constructors;
	}

	protected boolean hasConstructors() {
		return this.constructors != null;
	}

	protected void setConstructors(final Set constructors) {
		ObjectHelper.checkNotNull("parameter:constructors", constructors);
		this.constructors = constructors;
	}

	abstract protected Set createConstructors();

	/**
	 * Finds a matching constructor given its parameter types.
	 * 
	 * @param an
	 *            array of parameter types
	 * @return The matching constructor or null if none was found.
	 */
	public Constructor findConstructor(final List parameterTypes) {
		ObjectHelper.checkNotNull("parameter:parameterTypes", parameterTypes);

		Constructor found = null;

		final Iterator constructors = this.getConstructors().iterator();

		while (constructors.hasNext()) {
			final Constructor constructor = (Constructor) constructors.next();

			final List constructorParameterTypes = constructor.getParameters();
			if (constructorParameterTypes.size() != parameterTypes.size()) {
				continue;
			}

			found = constructor;

			final Iterator constructorParameterTypesIterator = constructorParameterTypes.iterator();
			final Iterator parameterTypesIterator = parameterTypes.iterator();

			while (parameterTypesIterator.hasNext()) {
				final ConstructorParameter constructorParameter = (ConstructorParameter) constructorParameterTypesIterator.next();
				final Type parameterType = (Type) parameterTypesIterator.next();
				if (false == constructorParameter.getType().equals(parameterType)) {
					found = null;
				}
			}

			if (null != found) {
				break;
			}
		}
		return found;
	}

	public Constructor getConstructor(final List parameterTypes) {
		Constructor constructor = this.findConstructor(parameterTypes);
		if (null == constructor) {
			this.throwConstructorNotFoundException(parameterTypes);
		}
		return constructor;
	}

	protected void throwConstructorNotFoundException(final List parameterTypes) {
		throw new ConstructorNotFoundException("Unable to find a constructor belonging to " + this.getName() + " with parameters "
				+ parameterTypes);
	}

	public boolean hasNoArgumentsConstructor() {
		return null != this.findConstructor(Collections.EMPTY_LIST);
	}

	/**
	 * A lazy loaded set containing all the declared fields for this type.
	 */
	private Set fields;

	public Set getFields() {
		if (false == this.hasFields()) {
			this.setFields(this.createFields());
		}

		return this.fields;
	}

	protected boolean hasFields() {
		return this.fields != null;
	}

	protected void setFields(final Set fields) {
		ObjectHelper.checkNotNull("parameter:fields", fields);
		this.fields = fields;
	}

	abstract protected Set createFields();

	/**
	 * Finds a field by name.
	 * 
	 * @param The
	 *            name of the field to find
	 */
	public Field findField(final String name) {
		GeneratorHelper.checkJavaFieldName("parameter:name", name);

		Field found = null;

		final Iterator iterator = this.getFields().iterator();

		while (iterator.hasNext()) {
			final Field field = (Field) iterator.next();
			if (field.getName().equals(name)) {
				found = field;
				break;
			}
		}
		return found;
	}

	public Field getField(final String name) {
		final Field method = this.findField(name);
		if (null == method) {
			throw new FieldNotFoundException("Unable to find a field called [" + name + "] within " + this.getName());
		}
		return method;
	}

	/**
	 * A lazy loaded set containing all the methods declared by this type
	 */
	private Set methods;

	public Set getMethods() {
		if (false == hasMethods()) {
			this.setMethods(this.createMethods());
		}
		return this.methods;
	}

	protected boolean hasMethods() {
		return this.methods != null;
	}

	protected void setMethods(final Set methods) {
		ObjectHelper.checkNotNull("parameter:methods", methods);
		this.methods = methods;
	}

	/**
	 * Factory method which creates a set of methods lazily.
	 * 
	 * @return
	 */
	abstract protected Set createMethods();

	/**
	 * Finds a method using its name and parameter types.
	 * 
	 * @param The
	 *            name of the method
	 * @param an
	 *            array of parameter types.
	 * @return The found method or null if none was found.
	 */
	public Method findMethod(final String methodName, final List parameterTypes) {
		GeneratorHelper.checkJavaMethodName("parameter:methodName", methodName);
		ObjectHelper.checkNotNull("parameter:parameterTypes", parameterTypes);

		Method found = null;

		final Iterator methods = this.getMethods().iterator();

		while (methods.hasNext()) {
			final Method method = (Method) methods.next();
			if (false == method.getName().equals(methodName)) {
				continue;
			}

			final List methodParameters = method.getParameters();
			if (methodParameters.size() != parameterTypes.size()) {
				continue;
			}

			found = method;

			final Iterator methodParametersIterator = methodParameters.iterator();
			final Iterator parameterTypesIterator = parameterTypes.iterator();

			while (parameterTypesIterator.hasNext()) {
				final Type type = (Type) parameterTypesIterator.next();
				final MethodParameter parameter = (MethodParameter) methodParametersIterator.next();
				if (false == type.equals(parameter.getType())) {
					found = null;
					break;
				}
			}

			if (null != found) {
				break;
			}
		}
		return found;
	}

	public Method getMethod(final String methodName, final List parameterTypes) {
		final Method method = this.findMethod(methodName, parameterTypes);
		if (null == method) {
			this.throwMethodNotFoundException(methodName, parameterTypes);
		}
		return method;
	}

	/**
	 * Searches this type for the most derived method with a signature that
	 * matches the given parameters
	 * 
	 * @param The
	 *            name of the method to search for.
	 * @param The
	 *            parameter types to search for
	 */
	public Method findMostDerivedMethod(final String methodName, final List parameterTypes) {
		ObjectHelper.checkNotNull("parameter:methodName", methodName);
		ObjectHelper.checkNotNull("parameter:parameterTypes", parameterTypes);

		final MostDerivedMethodFinder finder = new MostDerivedMethodFinder();
		finder.setMethodName(methodName);
		finder.setParameterTypes(parameterTypes);
		finder.start(this);
		return finder.getFound();
	}

	class MostDerivedMethodFinder extends SuperTypesVisitor {
		/**
		 * Each type belonging to the given super type is presented to this
		 * type.
		 * 
		 * @param type
		 * @return return true to skip remaining types.
		 */
		protected boolean visit(final Type type) {
			ObjectHelper.checkNotNull("parameter:type", type);

			final Method method = type.findMethod(this.getMethodName(), this.getParameterTypes());
			final boolean skipRemaining = method != null;
			if (skipRemaining) {
				this.setFound(method);
			}
			return skipRemaining;
		}

		/**
		 * Dont skip the most derived test when searching for the most derived
		 * method.
		 */
		protected boolean skipInitialType() {
			return false;
		}

		/**
		 * The name of the method being searched for.
		 */
		private String methodName;

		protected String getMethodName() {
			GeneratorHelper.checkJavaMethodName("field:methodName", methodName);
			return this.methodName;
		}

		protected void setMethodName(final String methodName) {
			GeneratorHelper.checkJavaMethodName("parameter:methodName", methodName);
			this.methodName = methodName;
		}

		private List parameterTypes;

		protected List getParameterTypes() {
			ObjectHelper.checkNotNull("field:parameterTypes", parameterTypes);
			return this.parameterTypes;
		}

		protected void setParameterTypes(final List parameterTypes) {
			ObjectHelper.checkNotNull("parameter:parameterTypes", parameterTypes);
			this.parameterTypes = parameterTypes;
		}

		private Method found;

		protected Method getFound() {
			return found;
		}

		protected void setFound(final Method found) {
			ObjectHelper.checkNotNull("parameter:found", found);
			this.found = found;
		}
	}

	/**
	 * Searches this type for the most derived method.
	 * 
	 * @param The
	 *            name of the method to search for.
	 * @param The
	 *            parameter types to search for
	 */
	public Method getMostDerivedMethod(final String methodName, final List parameterTypes) {
		final Method method = this.findMostDerivedMethod(methodName, parameterTypes);
		if (null == method) {
			this.throwMethodNotFoundException(methodName, parameterTypes);
		}
		return method;
	}

	protected void throwMethodNotFoundException(final String methodName, final List parameterTypes) {
		throw new MethodNotFoundException("Unable to find a method called [" + methodName + "] within " + this.getName()
				+ " with parameters " + parameterTypes + " upon " + this.getName());
	}

	/**
	 * Retrieves a Set containing all the SubTypes of this type.
	 */
	public Set getSubTypes() {
		final Set subTypes = this.createSubTypes();

		final Set merged = new HashSet();
		merged.addAll(subTypes);
		merged.addAll(this.getNewSubTypes());

		return Collections.unmodifiableSet(merged);
	}

	abstract protected Set createSubTypes();

	/**
	 * Searches all NewTypes super type chain to determine if any of them are
	 * sub types of this type.
	 * 
	 * @return a Set of NewType sub types
	 */
	protected Set getNewSubTypes() {
		final Set subTypes = new HashSet();

		final GeneratorContext context = this.getGeneratorContext();
		final Set newTypes = context.getNewTypes();
		final Iterator newTypesIterator = newTypes.iterator();
		
		while (newTypesIterator.hasNext()) {
			final Type newType = (Type) newTypesIterator.next();
			if( newType.getSuperType() == this ){
				subTypes.add( newType );
			}			
		}

		return subTypes;
	}

	/**
	 * A lazy loaded set containing all the declared nested types for this type.
	 */
	private Set nestedTypes;

	public Set getNestedTypes() {
		if (false == this.hasNestedTypes()) {
			this.setNestedTypes(this.createNestedTypes());
		}

		return this.nestedTypes;
	}

	protected boolean hasNestedTypes() {
		return this.nestedTypes != null;
	}

	protected void setNestedTypes(final Set nestedTypes) {
		ObjectHelper.checkNotNull("parameter:nestedTypes", nestedTypes);
		this.nestedTypes = nestedTypes;
	}

	abstract protected Set createNestedTypes();

	protected Type getBoolean() {
		return this.getGeneratorContext().getBoolean();
	}

	protected Type getByte() {
		return this.getGeneratorContext().getByte();
	}

	protected Type getShort() {
		return this.getGeneratorContext().getShort();
	}

	protected Type getInt() {
		return this.getGeneratorContext().getInt();
	}

	protected Type getLong() {
		return this.getGeneratorContext().getLong();
	}

	protected Type getFloat() {
		return this.getGeneratorContext().getFloat();
	}

	protected Type getDouble() {
		return this.getGeneratorContext().getDouble();
	}

	protected Type getChar() {
		return this.getGeneratorContext().getChar();
	}

	protected Type getVoid() {
		return this.getGeneratorContext().getVoid();
	}

	protected Type getObject() {
		return this.getGeneratorContext().getObject();
	}
}
