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
import java.util.HashSet;
import java.util.Set;

import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewInterfaceType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.type.TypeNotFoundException;
import rocket.util.client.Checker;

/**
 * A context that should be used when using the jdk/jre as the source of all
 * type info.
 * 
 * @author Miroslav Pokorny
 */
public class JavaGeneratorContext extends GeneratorContextImpl {

	public JavaGeneratorContext() {
		super();

		this.preloadTypes();
	}

	@Override
	protected void preloadTypes() {
		this.addType(this.createBooleanType());
		this.addType(this.createByteType());
		this.addType(this.createShortType());
		this.addType(this.createIntType());
		this.addType(this.createLongType());
		this.addType(this.createFloatType());
		this.addType(this.createDoubleType());
		this.addType(this.createCharType());
		this.addType(this.createVoidType());
	}

	protected Type createBooleanType() {
		final JavaBooleanClassTypeAdapter type = new JavaBooleanClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createByteType() {
		final JavaByteClassTypeAdapter type = new JavaByteClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createShortType() {
		final JavaShortClassTypeAdapter type = new JavaShortClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createIntType() {
		final JavaIntClassTypeAdapter type = new JavaIntClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createLongType() {
		final JavaLongClassTypeAdapter type = new JavaLongClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createFloatType() {
		final JavaFloatClassTypeAdapter type = new JavaFloatClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createDoubleType() {
		final JavaDoubleClassTypeAdapter type = new JavaDoubleClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createCharType() {
		final JavaCharClassTypeAdapter type = new JavaCharClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createVoidType() {
		final JavaVoidClassTypeAdapter type = new JavaVoidClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	@Override
	protected Type createType(final String name) {
		return this.createJavaClassTypeAdapter(name);
	}

	@SuppressWarnings("unchecked")
	protected Type createJavaClassTypeAdapter(final String name) {
		JavaClassTypeAdapter adapter = null;

		try {
			final Class javaClass = Class.forName(name);
			adapter = new JavaClassTypeAdapter();
			adapter.setGeneratorContext(this);
			adapter.setJavaClass(javaClass);

		} catch (final ExceptionInInitializerError caught) {
			throwTypeNotFoundException(name, caught);
		} catch (final ClassNotFoundException caught) {
			throwTypeNotFoundException(name, caught);
		} catch (final LinkageError caught) {
			throwTypeNotFoundException(name, caught);
		}

		return adapter;
	}

	protected void throwTypeNotFoundException(final String name, final Throwable cause) {
		throw new TypeNotFoundException("Unable to find the type \"" + name + "\".", cause);
	}
	
	/**
	 * Factory method which creates a package instance the first time a request
	 * is made.
	 * 
	 * @param name
	 * @return
	 */
	protected Package createPackage(final String name) {
		final java.lang.Package javaPackage = java.lang.Package.getPackage(name);
		if (null == javaPackage) {
			this.throwPackageNotFoundException(name);
		}

		JavaPackagePackageAdapter packagee = new JavaPackagePackageAdapter();
		packagee.setGeneratorContext(this);
		packagee.setJavaPackage(javaPackage);
		return packagee;
	}

	public NewConcreteType newConcreteType(final String name) {
		throw new UnsupportedOperationException();
	}

	public NewInterfaceType newInterfaceType(final String name) {
		throw new UnsupportedOperationException();
	}
	
	public Set<Type> asTypes(final Class<? extends Object>[] types) {
		Checker.notNull("parameter:types", types);

		final Set<Type> set = new HashSet<Type>();
		for (int i = 0; i < types.length; i++) {
			set.add( this.findType(types[i].getName()));
		}

		return set;
	}
	
	/**
	 * Helper which takes modifier bits from a constructor, field or method and returns the appropriate Visibility
	 * @param modifiers
	 * @return
	 */
	static public Visibility getVisibility(final int modifiers) {
		Visibility visibility = null;
	
		while (true) {
			if (Modifier.isPublic(modifiers)) {
				visibility = Visibility.PUBLIC;
				break;
			}
			if (Modifier.isProtected(modifiers)) {
				visibility = Visibility.PROTECTED;
				break;
			}
			if (Modifier.isPrivate(modifiers)) {
				visibility = Visibility.PRIVATE;
				break;
			}
			visibility = Visibility.PACKAGE_PRIVATE;
			break;
		}

		return visibility;
	}
}
