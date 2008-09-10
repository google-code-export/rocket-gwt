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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.generator.rebind.constructorparameter.NewConstructorParameter;
import rocket.generator.rebind.metadata.MetaData;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.ConstructorParameterVisitor;
import rocket.generator.rebind.visitor.MethodParameterVisitor;
import rocket.util.client.Checker;
import rocket.util.client.Tester;
import rocket.util.client.Utilities;

/**
 * A collection of helper methods used throughout this package.
 * 
 * @author Miroslav Pokorny
 */
public class GeneratorHelper {
	/**
	 * Helper which returns the setter name for the given property.
	 * 
	 * @param propertyName
	 * @return The setter name.
	 */
	static public String buildSetterName(final String propertyName) {
		StringBuilder builder = new StringBuilder();

		builder.append("set");

		builder.append(Character.toUpperCase(propertyName.charAt(0)));
		final int length = propertyName.length();
		if (length > 1) {
			builder.append(propertyName, 1, length);
		}

		return builder.toString();
	}

	/**
	 * Convenience method which calculates the setter name for a given field.
	 * Because no property type is passed all names will start with get and not
	 * is for boolean properties.
	 * 
	 * @param propertyName
	 * @return The getter name.
	 */
	static public String buildGetterName(final String propertyName) {
		StringBuilder builder = new StringBuilder();

		builder.append("get");

		builder.append(Character.toUpperCase(propertyName.charAt(0)));
		final int length = propertyName.length();
		if (length > 1) {
			builder.append(propertyName, 1, length);
		}

		return builder.toString();
	}

	/**
	 * A set that contains all java keywords and is used as a black list of
	 * acceptable java identifiers.
	 */
	private static Set javaIdentifiersBlacklist = createSetFromCommaDelimiteredString(GeneratorConstants.JAVA_RESERVED_KEYWORDS);

	/**
	 * This method exists purely as a way to easily load a set with String
	 * values.
	 * 
	 * @param input
	 * @return
	 */
	private static Set createSetFromCommaDelimiteredString(final String input) {
		final Set set = new HashSet();
		final String[] literals = Utilities.split(input, ",", true);
		for (int i = 0; i < literals.length; i++) {
			set.add(literals[i]);
		}
		return set;
	}

	static protected boolean isValidJavaIdentifier(final String name) {
		boolean valid = false;

		while (true) {
			if (GeneratorHelper.javaIdentifiersBlacklist.contains(name)) {
				break;
			}

			if (false == Character.isJavaIdentifierStart(name.charAt(0))) {
				break;
			}

			valid = true;
			final int count = name.length();
			for (int i = 1; i < count; i++) {
				if (false == Character.isJavaIdentifierPart(name.charAt(i))) {
					valid = false;
					break;
				}
			}
			break;
		}

		return valid;
	}

	static public boolean isValidJavaFieldName(final String name) {
		return isValidJavaIdentifier(name);
	}

	static public boolean isValidJavaMethodName(final String name) {
		return isValidJavaIdentifier(name);
	}

	static public boolean isValidJavaVariableName(final String name) {
		return isValidJavaIdentifier(name);
	}

	static public boolean isValidJavaTypeName(final String name) {
		boolean valid = false;

		if (false == Tester.isNullOrEmpty(name)) {
			valid = true;
			final String[] components = Utilities.split(name, ".$", true);
			for (int i = 0; i < components.length; i++) {
				if (false == isValidJavaIdentifier(components[i])) {
					valid = false;
					break;
				}
			}
		}
		return valid;
	}

	static public void checkJavaFieldName(final String name, final String fieldName) {
		if (false == isValidJavaFieldName(fieldName)) {
			Checker.fail(name, "The name \"" + fieldName + "\" is not a valid java field name.");
		}
	}

	static public void checkJavaMethodName(final String name, final String methodName) {
		if (false == isValidJavaTypeName(methodName)) {
			Checker.fail(name, "The name \"" + methodName + "\" is not a valid java method name.");
		}
	}

	static public void checkJavaTypeName(final String name, final String className) {
		if (false == isValidJavaTypeName(className)) {
			Checker.fail(name, "The name \"" + className + "\" is not a valid java class name.");
		}
	}

	static public void checkNestedJavaTypeName(final String name, final String className) {
		if (false == isValidJavaTypeName(className)) {
			Checker.fail(name, "The name \"" + className + "\" is not a valid java class name.");
		}
		if (-1 != className.indexOf('.')) {
			Checker
					.fail(name, "When naming a nested type the name\"" + className
							+ "\" must not be fully qualified (contain dots '.').");
		}
	}

	static public void checkJavaVariableName(final String name, final String variableName) {
		if (false == isValidJavaVariableName(variableName)) {
			Checker.fail(name, "The name \"" + variableName + "\" is not a valid variable name.");
		}
	}

	/**
	 * Renames all the parameters belonging to the constructor in the following
	 * fashion. The first parameter is named "parameter0", the second
	 * "parameter1"
	 * 
	 * @param constructor
	 *            The constructor containing the parameters.
	 */
	public static void renameParametersToParameterN(final NewConstructor constructor) {
		Checker.notNull("parameter:constructor", constructor);

		final ConstructorParameterVisitor visitor = new ConstructorParameterVisitor() {
			protected boolean visit(final ConstructorParameter parameter) {
				final NewConstructorParameter parameterN = (NewConstructorParameter) parameter;
				parameterN.setName("parameter" + counter);
				counter++;
				return false;
			}

			int counter = 0;
		};
		visitor.start(constructor);
	}

	/**
	 * Renames all the parameters belonging to the method in the following
	 * fashion. The first parameter is named "parameter0", the second
	 * "parameter1"
	 * 
	 * @param method
	 *            The method containing the parameters.
	 */
	public static void renameParametersToParameterN(final NewMethod method) {
		Checker.notNull("parameter:method", method);

		final MethodParameterVisitor visitor = new MethodParameterVisitor() {
			protected boolean visit(final MethodParameter parameter) {
				final NewMethodParameter parameter0 = (NewMethodParameter) parameter;
				parameter0.setName("parameter" + counter);
				counter++;
				return false;
			}

			int counter = 0;
		};
		visitor.start(method);
	}

	/**
	 * Writes the set of throwable types for a constructor or method.
	 * 
	 * @param types
	 * @param writer
	 */
	static public void writeThrownTypes(final Set types, final SourceWriter writer) {
		Checker.notNull("parameter:types", types);
		Checker.notNull("parameter:writer", writer);

		if (false == types.isEmpty()) {
			writer.print("throws ");

			final Iterator typesIterator = types.iterator();
			while (typesIterator.hasNext()) {
				final Type type = (Type) typesIterator.next();
				writer.print(type.getName());

				if (typesIterator.hasNext()) {
					writer.print(",");
				}
			}
		}
	}

	/**
	 * GeneratorHelper which takes a set of components all of which should be
	 * {@link CodeGenerators}
	 * 
	 * @param components
	 * @param writer
	 * @param comma
	 *            When true a comma is printed between each written component.
	 * @param eol
	 *            Print a new line after each component.
	 */
	static public void writeClassComponents(final Collection components, final SourceWriter writer, final boolean comma,
			final boolean eol) {
		Checker.notNull("parameter:components", components);
		Checker.notNull("parameter:writer", writer);

		final Iterator componentsIterator = components.iterator();
		while (componentsIterator.hasNext()) {
			final ClassComponent component = (ClassComponent) componentsIterator.next();
			writeClassComponent(component, writer);

			if (componentsIterator.hasNext()) {
				if (comma) {
					writer.print(", ");
				}
				if (eol) {
					writer.println();
				}
			}
		}
	}

	static public void writeClassComponent(final ClassComponent component, final SourceWriter writer) {
		Checker.notNull("parameter:component", component);
		Checker.notNull("parameter:writer", writer);

		if (false == component instanceof CodeGenerator) {
			throwCodeCannotBeGeneratedException(component);
		}
		final CodeGenerator generator = (CodeGenerator) component;
		generator.write(writer);
	}

	static protected void throwCodeCannotBeGeneratedException(final Object component) {
		throw new GeneratorException("The " + component + " is not a code generator.");
	}

	/**
	 * This set contains all reserved javascript keywords.
	 */
	static Set javascriptIdentifierBlacklist = createSetFromCommaDelimiteredString(GeneratorConstants.JAVASCRIPT_RESERVED_KEYWORDS);

	/**
	 * Tests if a given literal is a valid javascript literal.
	 * 
	 * @param name
	 * @return A flag indicating whether the name is valid
	 */
	static public boolean isValidJavascriptIdentifier(final String name) {
		boolean valid = false;

		while (true) {
			if (Tester.isNullOrEmpty(name)) {
				break;
			}
			if (GeneratorHelper.javascriptIdentifierBlacklist.contains(name)) {
				break;
			}

			if (false == isJavascriptIdentifierStart(name.charAt(0))) {
				break;
			}

			valid = true;
			final int count = name.length();
			for (int i = 1; i < count; i++) {
				if (false == isJavascriptIdentifierPart(name.charAt(i))) {
					valid = false;
					break;
				}
			}
			break;
		}

		return valid;
	}

	static private boolean isJavascriptIdentifierStart(final char c) {
		return c == '_' || c == '$' || (c >= 'A' & c <= 'Z') || (c >= 'a' & c <= 'z') || c > 'z';
	}

	static private boolean isJavascriptIdentifierPart(final char c) {
		return c == '_' || c == '$' || (c >= 'A' & c <= 'Z') || (c >= 'a' & c <= 'z') || (c >= '0' & c <= '9') || c > 'z';
	}

	/**
	 * Makes all parameters that belong to the constructor final.
	 * 
	 * @param constructor
	 */
	static public void makeAllParametersFinal(final NewConstructor constructor) {
		Checker.notNull("parameter:constructor", constructor);

		final Iterator<ConstructorParameter> parameters = constructor.getParameters().iterator();
		while (parameters.hasNext()) {
			final NewConstructorParameter parameter = (NewConstructorParameter) parameters.next();
			parameter.setFinal(true);
		}
	}

	/**
	 * Makes all parameters that belong to the method final.
	 * 
	 * @param method
	 */
	static public void makeAllParametersFinal(final NewMethod method) {
		Checker.notNull("parameter:method", method);

		final Iterator<MethodParameter> parameters = method.getParameters().iterator();
		while (parameters.hasNext()) {
			final NewMethodParameter parameter = (NewMethodParameter) parameters.next();
			parameter.setFinal(true);
		}
	}

	/**
	 * Writes out the comments and annotations (metaData) within a javadoc
	 * comment.
	 * 
	 * @param comments
	 *            A string which may be empty of text
	 * @param metaData
	 * @param writer
	 *            The writer typically invoked inside a ClassComponent when its
	 *            writing itself.
	 */
	static public void writeComments(final String comments, final MetaData metaData, final SourceWriter writer) {
		Checker.notNull("parameter:comments", comments);
		Checker.notNull("parameter:metaData", metaData);
		Checker.notNull("parameter:sourceWriter", writer);

		while (true) {
			final boolean noComments = Tester.isNullOrEmpty(comments);
			final boolean noAnnotations = metaData.isEmpty();

			if (noComments && noAnnotations) {
				break;
			}

			// only has annotations...
			if (noComments && false == noAnnotations) {
				writer.beginJavaDocComment();
				metaData.write(writer);
				writer.endJavaDocComment();
				break;
			}
			// only has comments...
			if (noComments && false == noAnnotations) {
				writer.beginJavaDocComment();
				writer.println(comments);
				writer.endJavaDocComment();
				break;
			}

			// must have both annotations and comments...
			writer.beginJavaDocComment();
			writer.println(comments);
			writer.println();
			metaData.write(writer);
			writer.endJavaDocComment();
			break;
		}
	}
}
