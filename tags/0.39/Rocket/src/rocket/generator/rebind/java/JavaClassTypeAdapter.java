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
package rocket.generator.rebind.java;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.AbstractType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * Provides an adapter between a java class and a Type
 * 
 * @author Miroslav Pokorny
 */
public class JavaClassTypeAdapter extends AbstractType {

	public Visibility getVisibility() {
		return JavaAdapterHelper.getVisibility(this.getJavaClass().getModifiers());
	}

	protected Set createConstructors() {
		final GeneratorContext context = this.getGeneratorContext();

		final Set constructors = new HashSet();

		final java.lang.reflect.Constructor[] javaConstructors = this.getJavaClass().getDeclaredConstructors();
		for (int i = 0; i < javaConstructors.length; i++) {
			final java.lang.reflect.Constructor javaConstructor = javaConstructors[i];

			final JavaConstructorConstructorAdapter adapter = new JavaConstructorConstructorAdapter();
			adapter.setGeneratorContext(context);
			adapter.setEnclosingType(this);
			adapter.setJavaConstructor(javaConstructor);

			constructors.add(adapter);
		}

		return Collections.unmodifiableSet(constructors);
	}

	protected Set createFields() {
		final GeneratorContext context = this.getGeneratorContext();

		final Set fields = new HashSet();

		final java.lang.reflect.Field[] javaFields = this.getJavaClass().getDeclaredFields();
		for (int i = 0; i < javaFields.length; i++) {
			final java.lang.reflect.Field javaField = javaFields[i];

			final JavaFieldFieldAdapter adapter = new JavaFieldFieldAdapter();
			adapter.setGeneratorContext(context);
			adapter.setEnclosingType(this);
			adapter.setJavaField(javaField);

			fields.add(adapter);
		}

		return Collections.unmodifiableSet(fields);
	}

	protected Set createInterfaces() {
		return JavaAdapterHelper.asSetOfTypes(this.getGeneratorContext(), this.getJavaClass().getInterfaces());
	}

	protected Set createMethods() {
		final GeneratorContext context = this.getGeneratorContext();

		final Set methods = new HashSet();
		final java.lang.reflect.Method[] javaMethods = this.getJavaClass().getDeclaredMethods();
		for (int i = 0; i < javaMethods.length; i++) {
			final java.lang.reflect.Method javaMethod = javaMethods[i];

			final JavaMethodMethodAdapter adapter = new JavaMethodMethodAdapter();
			adapter.setGeneratorContext(context);
			adapter.setEnclosingType(this);
			adapter.setJavaMethod(javaMethod);

			methods.add(adapter);
		}

		return Collections.unmodifiableSet(methods);
	}

	protected Set createNestedTypes() {
		throw new UnsupportedOperationException();
	}

	protected Set createSubTypes() {
		throw new UnsupportedOperationException();
	}

	public Type getComponentType() {
		final Class componentType = this.getJavaClass().getComponentType();
		return null == componentType ? null : this.getType(componentType.getName());
	}

	public String getJsniNotation() {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return this.getJavaClass().getName();
	}

	public Package getPackage() {
		return this.getPackage(this.getJavaClass().getPackage().getName());
	}
	final protected Package getPackage(final String packageName) {
		return this.getGeneratorContextImpl().getPackage(packageName);
	}
	protected GeneratorContextImpl getGeneratorContextImpl(){
		return(GeneratorContextImpl) this.getGeneratorContext();
	}

	public String getSimpleName() {
		return this.getJavaClass().getSimpleName();
	}

	public Type getSuperType() {
		final Class superClass = this.getJavaClass().getSuperclass();
		return null == superClass ? null : this.getType(superClass.getName());
	}

	public boolean isAbstract() {
		return Modifier.isAbstract(this.getJavaClass().getModifiers());
	}

	public boolean isArray() {
		return this.getJavaClass().isArray();
	}

	public boolean isAssignableFrom(final Type type) {
		final JavaClassTypeAdapter adapter = (JavaClassTypeAdapter) type;
		return this.getJavaClass().isAssignableFrom(adapter.getJavaClass());
	}

	public boolean isAssignableTo(Type type) {
		return type.isAssignableFrom(this);
	}

	public boolean isFinal() {
		return Modifier.isFinal(this.getJavaClass().getModifiers());
	}

	public boolean isInterface() {
		return this.getJavaClass().isInterface();
	}

	public boolean isPrimitive() {
		return this.getJavaClass().isPrimitive();
	}

	public List getMetadataValues(String name) {
		throw new UnsupportedOperationException();
	}

	public Type getWrapper() {
		return null;
	}

	/**
	 * The source java class
	 */
	protected Class javaClass;

	protected Class getJavaClass() {
		ObjectHelper.checkNotNull("field:type", javaClass);
		return this.javaClass;
	}

	public void setJavaClass(final Class javaClass) {
		ObjectHelper.checkNotNull("parameter:type", javaClass);
		this.javaClass = javaClass;
	}

	public String toString() {
		return "Class: " + this.javaClass;
	}
}
