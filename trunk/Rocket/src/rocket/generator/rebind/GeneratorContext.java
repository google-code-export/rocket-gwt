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
package rocket.generator.rebind;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.json.client.JsonSerializable;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Context that accompanies this particular code generation session and also
 * provides an interface with the GWT TypeOracle.
 * 
 * @author Miroslav Pokorny
 * 
 */
abstract public class GeneratorContext {

	/**
	 * Returns the name of the generated json serializer for the given className
	 * 
	 * @param className
	 * @return
	 */
	public String getGeneratedClassname(final String className) {
		final String packageName = this.getPackageName(className);
		final String simpleClassName = this.getSimpleClassName(className);
		return packageName + '.' + simpleClassName + this.getGeneratedClassNameSuffix();
	}

	abstract protected String getGeneratedClassNameSuffix();

	/**
	 * Tests if a particular class has already been generated.
	 * 
	 * @param packageName
	 * @param simpleClassName
	 * @return
	 */
	public PrintWriter tryCreateTypePrintWriter(final String packageName, final String simpleClassName) {
		final PrintWriter printWriter = this.getGeneratorContext().tryCreate(this.getLogger(), packageName, simpleClassName);
		return printWriter;
	}

	/**
	 * Creates a sourceWriter. All attempts to create a SourceWriter eventually
	 * call this method once they have setup the ClassSourceFileComposerFactory
	 * and gotten a PrintWriter
	 * 
	 * @param composerFactory
	 * @param printWriter
	 * @return
	 */
	public SourceWriter createSourceWriter(final ClassSourceFileComposerFactory composerFactory, final PrintWriter printWriter) {
		return composerFactory.createSourceWriter(this.getGeneratorContext(), printWriter);
	}

	/**
	 * Terminates and commits a generated class. When this method is called the
	 * class will be realised into a real type.
	 * 
	 * @param writer
	 */
	public void commitWriter(final SourceWriter writer) {
		writer.commit(this.getLogger());
	}

	/**
	 * Retrieves the package name portion of the fully qualified class name.
	 * 
	 * @param fullyQualifiedClassName
	 * @return
	 */
	public String getPackageName(final String fullyQualifiedClassName) {
		StringHelper.checkNotEmpty("parameter:fullyQualifiedClassName", fullyQualifiedClassName);

		String packageName = null;
		final TypeOracle typeOracle = this.getTypeOracle();
		String name = fullyQualifiedClassName;

		while (true) {
			// appear to be a class in the unnamed package...
			final int simpleNameStartIndex = name.lastIndexOf('.' );
			if (-1 == simpleNameStartIndex) {
				break;
			}

			packageName = name.substring(0, simpleNameStartIndex);
			final JPackage jPackage = typeOracle.findPackage(packageName);
			if (null != jPackage) {
				break;
			}
			name = packageName;
		}

		return packageName;
	}

	/**
	 * Retrieves the simple class name from a fully qualified class name. This
	 * method also converts all inner classes into standalone classes.
	 * 
	 * @param fullyQualifiedClassName
	 * @return the simple classname
	 */
	public String getSimpleClassName(final String fullyQualifiedClassName) {
		StringHelper.checkNotEmpty("parameter:fullyQualifiedClassName", fullyQualifiedClassName);

		final String packageName = this.getPackageName(fullyQualifiedClassName);
		final String simpleClassNameWhichIsPossiblyAInnerClass = fullyQualifiedClassName.substring(packageName.length() + 1);
		return simpleClassNameWhichIsPossiblyAInnerClass.replace('.', '_');
	}

	/**
	 * Takes a type and returns a fully qualified class name even if the input
	 * type is an inner class.
	 * 
	 * @param type
	 * @return
	 */
	protected String makeIntoAPublicClass(final JClassType type) {
		final String packageName = type.getPackage().getName();

		String qualifiedClassName = type.getQualifiedSourceName();
		qualifiedClassName = qualifiedClassName.substring(packageName.length() + 1); // drop
		// the
		// packageName
		// + '.'
		qualifiedClassName = qualifiedClassName.replaceAll("_", "__");

		// make inner class into regular classes.
		qualifiedClassName = qualifiedClassName.replace('.', '_');

		return packageName + '.' + qualifiedClassName;
	}

	public JClassType getJavaLangObject() {
		return (JClassType) this.getTypeOracle().getJavaLangObject();
	}

	public JClassType getJavaLangString() {
		return (JClassType) this.getType(String.class.getName());
	}

	public JClassType getJavaUtilList() {
		return (JClassType) this.getType(List.class.getName());
	}

	public JClassType getJavaLangBoolean() {
		return (JClassType) this.getType(Boolean.class.getName());
	}

	public JClassType getJavaLangByte() {
		return (JClassType) this.getType(Byte.class.getName());
	}

	public JClassType getJavaLangShort() {
		return (JClassType) this.getType(Short.class.getName());
	}

	public JClassType getJavaLangInteger() {
		return (JClassType) this.getType(Integer.class.getName());
	}

	public JClassType getJavaLangLong() {
		return (JClassType) this.getType(Long.class.getName());
	}

	public JClassType getJavaLangFloat() {
		return (JClassType) this.getType(Float.class.getName());
	}

	public JClassType getJavaLangDouble() {
		return (JClassType) this.getType(Double.class.getName());
	}

	public JClassType getJavaLangCharacter() {
		return (JClassType) this.getType(Character.class.getName());
	}

	public JClassType getJavaUtilSet() {
		return (JClassType) this.getType(Set.class.getName());
	}

	public JClassType getJavaUtilMap() {
		return (JClassType) this.getType(Map.class.getName());
	}

	public JClassType getJsonSerializable() {
		return (JClassType) this.getType(JsonSerializable.class.getName());
	}

	/**
	 * Almost the same as findType but an exception is thrown if the type is not
	 * found.
	 * 
	 * @param className
	 * @return
	 */
	public JType getType(final String className) {
		final JType type = this.findType(className);
		if (null == type) {
			throw new GeneratorException("Unable to find type [" + className + "].");
		}
		return type;
	}

	public JPackage findPackage(final String packageName) {
		return this.getTypeOracle().findPackage(packageName);
	}

	public JClassType findType(final String name) {
		return this.getTypeOracle().findType(name);
	}

	/**
	 * The GeneratorContext for this code generation session.
	 */
	private com.google.gwt.core.ext.GeneratorContext generatorContext;

	protected com.google.gwt.core.ext.GeneratorContext getGeneratorContext() {
		ObjectHelper.checkNotNull("field:generatorContext", generatorContext);
		return this.generatorContext;
	}

	public void setGeneratorContext(final com.google.gwt.core.ext.GeneratorContext generatorContext) {
		ObjectHelper.checkNotNull("parameter:generatorContext", generatorContext);
		this.generatorContext = generatorContext;
	}

	protected TypeOracle getTypeOracle() {
		return this.getGeneratorContext().getTypeOracle();
	}

	/**
	 * This treelogger may be used to log messages
	 */
	private TreeLogger logger;

	protected TreeLogger getLogger() {
		return this.logger;
	}

	public void setLogger(final TreeLogger logger) {
		this.logger = logger;
	}
}
