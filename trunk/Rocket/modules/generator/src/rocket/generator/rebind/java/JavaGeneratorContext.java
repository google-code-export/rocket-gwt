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

import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewInterfaceType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.type.TypeNotFoundException;

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
		this.addType(this.createBooleanArrayType());

		this.addType(this.createByteType());
		this.addType(this.createByteArrayType());

		this.addType(this.createShortType());
		this.addType(this.createShortArrayType());

		this.addType(this.createIntType());
		this.addType(this.createIntArrayType());

		this.addType(this.createLongType());
		this.addType(this.createLongArrayType());

		this.addType(this.createFloatType());
		this.addType(this.createFloatArrayType());

		this.addType(this.createDoubleType());
		this.addType(this.createDoubleArrayType());

		this.addType(this.createCharType());
		this.addType(this.createCharArrayType());

		this.addType(this.createVoidType());
	}

	protected Type createBooleanType() {
		final JavaBooleanClassTypeAdapter type = new JavaBooleanClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createBooleanArrayType() {
		return this.createArrayType(new boolean[0].getClass().getName());
	}

	protected Type createByteType() {
		final JavaByteClassTypeAdapter type = new JavaByteClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createByteArrayType() {
		return this.createArrayType(new byte[0].getClass().getName());
	}

	protected Type createShortType() {
		final JavaShortClassTypeAdapter type = new JavaShortClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createShortArrayType() {
		return this.createArrayType(new short[0].getClass().getName());
	}

	protected Type createIntType() {
		final JavaIntClassTypeAdapter type = new JavaIntClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createIntArrayType() {
		return this.createArrayType(new int[0].getClass().getName());
	}

	protected Type createLongType() {
		final JavaLongClassTypeAdapter type = new JavaLongClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createLongArrayType() {
		return this.createArrayType(new long[0].getClass().getName());
	}

	protected Type createFloatType() {
		final JavaFloatClassTypeAdapter type = new JavaFloatClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createFloatArrayType() {
		return this.createArrayType(new float[0].getClass().getName());
	}

	protected Type createDoubleType() {
		final JavaDoubleClassTypeAdapter type = new JavaDoubleClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createDoubleArrayType() {
		return this.createArrayType(new double[0].getClass().getName());
	}

	protected Type createCharType() {
		final JavaCharClassTypeAdapter type = new JavaCharClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createCharArrayType() {
		return this.createArrayType(new char[0].getClass().getName());
	}

	protected Type createVoidType() {
		final JavaVoidClassTypeAdapter type = new JavaVoidClassTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createClassType(final String name) {
		return this.createJavaClassTypeAdapter(name);
	}

	protected void throwTypeNotFoundException(final String name, final Throwable cause) {
		throw new TypeNotFoundException("Unable to find the type \"" + name + "\".", cause);
	}

	protected Type createArrayType(final String name) {
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
}
