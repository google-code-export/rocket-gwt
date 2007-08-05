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

import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.util.AbstractConstructorOrMethod;
import rocket.generator.rebind.util.Parameter;
import rocket.generator.rebind.visitor.SuperTypesVisitor;
import rocket.util.client.ObjectHelper;

/**
 * Convenient base class for any Method implementation.
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractMethod extends AbstractConstructorOrMethod implements Method {

	public String getJsniNotation() {
		final StringBuilder jsni = new StringBuilder();
		jsni.append('@');
		jsni.append(this.getEnclosingType().getName());
		jsni.append("::");
		jsni.append(this.getName());
		jsni.append('(');

		final Iterator parameters = this.getParameters().iterator();
		while (parameters.hasNext()) {
			final Parameter parameter = (Parameter) parameters.next();
			jsni.append(parameter.getJsniNotation());
		}

		jsni.append(')');

		return jsni.toString();
	}

	private Type returnType;

	public Type getReturnType() {
		ObjectHelper.checkNotNull("field:returnType", returnType);
		return this.returnType;
	}

	protected boolean hasReturnType() {
		return null != returnType;
	}

	protected void setReturnType(final Type returnType) {
		ObjectHelper.checkNotNull("parameter:returnType", returnType);
		this.returnType = returnType;
	}

	public boolean returnsVoid(){
		return this.getReturnType().equals( this.getGeneratorContext().getVoid() );
	}
	
	/**
	 * GeneratorHelper which checks the super type heirarchy to test if this
	 * method actually overrides another.
	 */
	public Method getOverriddenMethod() {
		Method method = null;

		while (true) {
			// static and methods are not virtual.
			if (this.isStatic()) {
				break;
			}
			final Visibility visibility = this.getVisibility();
			if (Visibility.PRIVATE == visibility) {
				break;
			}
			method = this.findOverriddenMethod();
			break;
		}

		if (null == method) {
			this.throwMethodNotFoundException();
		}

		return method;
	}

	protected void throwMethodNotFoundException() {
		throw new MethodNotFoundException("Unable to find overridden method for " + this);
	}

	public Method findOverriddenMethod() {
		final OverriddenMethodSearcher searcher = new OverriddenMethodSearcher();
		searcher.start(this.getEnclosingType());
		return searcher.getMethod();
	}

	/**
	 * This visitor is used to search all the super types methods for a method.
	 * 
	 * It works by searching each type that is visited for a method with the
	 * same name and parameters (signature).
	 */
	class OverriddenMethodSearcher extends SuperTypesVisitor {

		protected boolean visit(final Type type) {
			boolean stopSearching = false;

			while (true) {
				final Visibility visibility = AbstractMethod.this.getVisibility();
				if (visibility == Visibility.PACKAGE_PRIVATE) {
					// different package so fail..
					if (false == type.getPackage().equals(AbstractMethod.this.getEnclosingType().getPackage())) {
						break;
					}
				}

				final String methodName = AbstractMethod.this.getName();
				Method method = type.findMethod(methodName, AbstractMethod.this.getParameters());
				if (null == method) {
					break;
				}
				if (method.isStatic()) {
					break;
				}
				if (method.getVisibility() == Visibility.PRIVATE) {
					break;
				}

				this.setMethod(method);
				stopSearching = true;
				break;
			}
			return stopSearching;
		}

		protected boolean skipInitialType() {
			return true;
		}

		/**
		 * This property will contain the found method if one is found.
		 */
		Method method;

		protected Method getMethod() {
			return method;
		}

		protected void setMethod(final Method method) {
			this.method = method;
		}
	}

	/**
	 * Copies all the properties from this method into a new method.
	 * 
	 * @param newType
	 * @return The new method
	 */
	public NewMethod copy(final NewType newType) {
		ObjectHelper.checkNotNull("parameter:newType", newType);

		final NewMethod method = newType.newMethod();
		method.setAbstract(this.isAbstract());
		method.setFinal(this.isFinal());
		method.setName(this.getName());
		method.setNative( false );

		final Iterator parameters = this.getParameters().iterator();
		while (parameters.hasNext()) {
			final MethodParameter parameter = (MethodParameter) parameters.next();
			method.addParameter(parameter.copy());
		}

		method.setReturnType(this.getReturnType());
		method.setStatic(this.isStatic());

		final Iterator thrownTypes = this.getThrownTypes().iterator();
		while (thrownTypes.hasNext()) {
			method.addThrownTypes((Type) thrownTypes.next());
		}

		method.setVisibility(this.getVisibility());
		return method;
	}
	
	public boolean hasSameSignature( final Method otherMethod ){
		ObjectHelper.checkNotNull( "parameter:otherMethod", otherMethod );
		
		boolean same = false;
		
		while( true ){			
			// name must match
			if( false == this.getName().equals( otherMethod.getName() )){
				break;
			}
			// return type must match
			if( false == this.getReturnType().equals( otherMethod.getReturnType() )){
				break;
			}
			if( this.isStatic() != otherMethod.isStatic() ){
				break;
			}
			
			// parameter types must match.
			final List parameters = this.getParameters();
			final List otherParameters = otherMethod.getParameters();
			if( parameters.size() != otherParameters.size() ){
				break;
			}
			same = true;
			final Iterator parametersIterator = parameters.iterator();
			final Iterator otherParametersIterator = otherParameters.iterator();

			while (parametersIterator.hasNext()) {
				final MethodParameter parameter = (MethodParameter )parametersIterator.next();
				final MethodParameter otherParameter = (MethodParameter )otherParametersIterator.next();
				
				if( parameter.getType().equals( otherParameter.getType() )){
					same = false;
					break;
				}	
			}			
			
			break;
		}
		
		return same;
	}
}
