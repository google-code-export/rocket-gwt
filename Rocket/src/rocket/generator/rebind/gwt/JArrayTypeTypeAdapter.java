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

import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.AbstractType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * An adapter between the Type and GWT JArrayType classes.
 * 
 * Array types arent proper types so they dont have constructors, fields, implement interfaces, methods or contain nested methods therefore many of these methods throw an {@link UnsupportedOperationException} when attempts
 * are made to access them.
 * @author Miroslav Pokorny
 */
public class JArrayTypeTypeAdapter extends AbstractType {

	/**
	 * Arrays dont have constructors.
	 */
	public Set getConstructors(){
		return Collections.EMPTY_SET;
	}

	protected Set createConstructors() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Arrays dont have fields, dont worry about length its a special case
	 */
	
	public Set getFields(){
		return Collections.EMPTY_SET;
	}
	protected Set createFields() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Arrays dont implement interfaces
	 */
	public Set getInterfaces(){
		return Collections.EMPTY_SET;
	}
	
	protected Set createInterfaces() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Arrays dont have methods
	 */
	public Set getMethods() {
		return Collections.EMPTY_SET;
	}
	protected Set createMethods() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Arrays cant have nested types.
	 */
	public Set getNestedTypes() {
		return Collections.EMPTY_SET;
	}

	protected Set createNestedTypes() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Arrays cant be sub classed
	 */
	public Set getSubTypes() {
		return Collections.EMPTY_SET;
	}
	protected Set createSubTypes() {
		throw new UnsupportedOperationException();
	}

	public boolean isArray() {
		return true;
	}
	
	/**
	 * All array types have a component type.
	 */
	public Type getComponentType() {
		final JArrayType array = this.getJArrayType().isArray();
		ObjectHelper.checkNotNull( "The " + this.getName() + " is an array.", array );
		
		final JType componentType = array.getComponentType();
		final String componentTypeName = componentType.getQualifiedSourceName();
		return this.getType( componentTypeName );
	}

	public String getJsniNotation() {
		return this.getJArrayType().getJNISignature();
	}

	public String getName() {
		return this.getJArrayType().getQualifiedSourceName();
	}

	public Package getPackage() {
		return this.getComponentType().getPackage();
	}
	
	final protected Package findPackage(final String packageName) {
		return this.getGeneratorContextImpl().findPackage(packageName);
	}

	public String getSimpleName() {
		return this.getJArrayType().getSimpleSourceName();
	}
	
	/**
	 * Array types always extend Object
	 */
	public Type getSuperType() {
		return this.getObject();
	}

	public Visibility getVisibility() {
		return Visibility.PUBLIC;
	}

	/**
	 * Array types dont have a wrapper type
	 */
	public Type getWrapper() {
		return null;
	}

	/**
	 * Array types are never abstract
	 */
	public boolean isAbstract() {
		return false;
	}	

	/**
	 * Array types can only be assigned from their own type without casting.
	 */
	public boolean isAssignableFrom(final Type type) {
		return this.equals( type );
	}

	/**
	 * Array types can only be assigned to their own type or Object 
	 */
	public boolean isAssignableTo(final Type type) {
		return this.equals( type ) || type.equals( this.getObject() );
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

	/**
	 * Array types never have annotations as they are actually created at runtime by the runtime and not taken from source. 
	 */
	public List getMetadataValues(String name) {
		return null;
	}

	/**
	 * The JArrayType providing the source for type and related data.
	 */
	private JArrayType jArrayType;

	protected JArrayType getJArrayType() {
		ObjectHelper.checkNotNull("field:jArrayType", jArrayType);
		return jArrayType;
	}

	public void setJArrayType(final JArrayType jArrayType) {
		ObjectHelper.checkNotNull("parameter:jArrayType", jArrayType);
		this.jArrayType = jArrayType;
	}

	protected GeneratorContextImpl getGeneratorContextImpl(){
		return (GeneratorContextImpl) this.getGeneratorContext();
	}
	
	
	public String toString() {
		return "" + this.jArrayType;
	}
}
