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
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.type.TypeNotFoundException;

import com.google.gwt.core.ext.typeinfo.JPackage;

/**
 * A context that should be used when using the jdk/jre as the source of all
 * type info.
 * 
 * @author Miroslav Pokorny
 */
public class JavaGeneratorContext extends GeneratorContextImpl {

	protected String getGeneratedTypeNameSuffix() {
		throw new UnsupportedOperationException("getGeneratedTypeNameSuffix");
	}

	protected JPackage findJPackage(final String name) {
		throw new UnsupportedOperationException();
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

	protected Type createClassType(final String name) {
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
		throw new TypeNotFoundException("Unable to find the type [" + name + "]", cause);
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
}
