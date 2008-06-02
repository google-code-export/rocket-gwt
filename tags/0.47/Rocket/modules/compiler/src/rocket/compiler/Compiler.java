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
package rocket.compiler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import rocket.util.client.Checker;

import com.google.gwt.dev.jjs.ast.JField;
import com.google.gwt.dev.jjs.ast.JFieldRef;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JNode;
import com.google.gwt.dev.jjs.ast.JReferenceType;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.js.ast.JsFunction;
import com.google.gwt.dev.js.ast.JsNode;

/**
 * Convenient base class with some common functionality
 * @author Miroslav Pokorny
 */
public class Compiler {
	/**
	 * Extracts the source equivalent of the given node replacing newlines and carriage returns with their escaped slash equivalent.
	 * @param node
	 * @return
	 */
	static public String getSource(final JNode node) {
		Checker.notNull("parameter:node", node);

		return Compiler.inline(node.toSource());
	}
	
	/**
	 * Returns a string containing the method name followed by any parameter types.
	 * @param method
	 * @return
	 */
	static public String getMethodName(final JMethod method) {
		Checker.notNull("parameter:method", method);

		final String methodName = method.getName();

		final StringBuffer buf = new StringBuffer();
		buf.append(methodName);
		buf.append('(');

		final Iterator p = method.getOriginalParamTypes().iterator();
		while (p.hasNext()) {
			final JType parameter = (JType) p.next();
			buf.append(parameter.getName());

			if (p.hasNext()) {
				buf.append(",");
			}
		}

		buf.append(')');

		if (methodName.equals(Constants.STATIC_INITIALIZER_METHOD_NAME)) {
			buf.append(Constants.STATIC_INITIALIZER_SUFFIX);
		}
		if (methodName.equals(Constants.INITIALIZER_METHOD_NAME)) {
			buf.append( Constants.INITIALIZER_SUFFIX);
		}
		if (method.isConstructor()) {
			buf.append(Constants.CONSTRUCTOR_SUFFIX);
		}

		return buf.toString();
	}

	/**
	 * Returns a string that includes the enclosing type and method including parameters.
	 * @param method
	 * @return
	 */
	static public String getFullMethodName(final JMethod method) {
		StringBuffer buf = new StringBuffer();
		final JReferenceType enclosingType = method.getEnclosingType();
		if (null != enclosingType) {
			buf.append(enclosingType.getName());
			buf.append('.');
		}
		buf.append(Compiler.getMethodName(method));

		return buf.toString();
	}

	/**
	 * Extracts the source equivalent of the given node replacing newlines and carriage returns with their escaped slash equivalent.
	 * @param node
	 * @return
	 */
	static public String getSource(final JsNode node) {
		Checker.notNull("parameter:node", node);

		return inline(node.toSource());
	}

	/**
	 * Takes a string which may or may not contain nl / cr and makes a single line.
	 * @param string
	 * @return
	 */
	static public String inline(final String string) {
		return string.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r");
	}

	static final String ENABLED = "enabled";
	static final String DISABLED = "disabled";
	static final String PACKAGE = Compiler.class.getPackage().getName();

	/**
	 * Tests if a particular optimiser is enabled by checking if a system property with the same name has a value of enabled.
	 * @param className
	 * @return
	 */
	static public boolean isEnabled(final String className) {
		boolean enabled = false;

		while (true) {
			final String individual = System.getProperty(className);
			if (ENABLED.equals(individual)) {
				enabled = true;
				break;
			}
			if (DISABLED.equals(individual)) {
				enabled = false;
				break;
			}

			// global enable/disable...
			final String global = System.getProperty(PACKAGE);
			if (ENABLED.equals(global)) {
				enabled = true;
				break;
			}
			if (DISABLED.equals(global)) {
				enabled = false;
				break;
			}

			// defaults to disabled...
			break;
		}
		return enabled;
	}

	/**
	 * Tests if a feature belonging to a specific class is enabled by testing for both the class and the feature being enabled.
	 * @param className
	 * @param feature
	 * @return
	 */
	static public boolean isEnabled(final String className, final String feature) {
		boolean enabled = false;

		while (true) {
			final String featureProperty = System.getProperty(className + '.' + feature);
			if (ENABLED.equals(featureProperty)) {
				enabled = true;
				break;
			}
			if (DISABLED.equals(featureProperty)) {
				enabled = false;
				break;
			}

			enabled = isEnabled(className);
			break;
		}
		return enabled;
	}

	/**
	 * This set aggregates all the static methods that dont require a clinit to be inserted by GenerateJavaScriptAST.
	 */
	static private Set staticMethodsNotRequiringClint;

	private static void resetStaticMethodsNotRequiringClinit() {
		Compiler.staticMethodsNotRequiringClint = new HashSet();
	}

	public static void addStaticMethodNotRequiringClinit(final JMethod method) {
		Compiler.staticMethodsNotRequiringClint.add(method);
	}

	public static boolean requiresClinit(final JMethod method) {
		Checker.notNull("parameter:method", method);
		
		return Compiler.staticMethodsNotRequiringClint == null ? true : false == Compiler.staticMethodsNotRequiringClint
				.contains(method);
	}

	/**
	 * This set aggregates all static field references that require a clinit to be inserted by GenerateJavaScriptAST.
	 */
	static private Set staticFieldReferencesNotRequiringClinits;

	private static void resetFieldReferencesNotRequiringClint() {
		Compiler.staticFieldReferencesNotRequiringClinits = new HashSet();
	}

	/**
	 * Records that a particular doesnt require a clinit.
	 * @param reference
	 */
	public static void addFieldReferenceNotRequiringClinit(final JFieldRef reference) {
		Checker.notNull("parameter:reference", reference);

		Compiler.staticFieldReferencesNotRequiringClinits.add(reference);
	}

	/**
	 * Tests whether a field references requires a clinit.
	 * @param reference
	 * @return
	 */
	public static boolean requiresClinit(final JFieldRef reference) {
		Checker.notNull("parameter:reference", reference);

		return Compiler.staticFieldReferencesNotRequiringClinits == null ? true
				: false == Compiler.staticFieldReferencesNotRequiringClinits.contains(reference);
	}

	/**
	 * Tests whether a field typically belonging to a jsni aka native method requires a clinit to be inserted in front of the reference
	 * to ensure correct semantics guaranteeing static initializers are invoked as required.
	 * @param field
	 * @return
	 */
	public static boolean requiresClinit( final JField field ){
		Checker.notNull("parameter:field", field);
		
		// all fields require a clinit if necessary unless a reference to them is included in $staticFieldReferencesNotRequiringClinits
		boolean requires = true;
		
		final Set references = Compiler.staticFieldReferencesNotRequiringClinits;
		if( null != references ){
			final Iterator i = references.iterator();
			while( i.hasNext() ){
				final JFieldRef reference = (JFieldRef)i.next();
				if( reference.getField().equals( field )){
					requires = false;
					break;
				}
			}
		}
		
		return requires;		
	}
	
	/**
	 * Returns the fullyqualified name of a field which amounts to the class name dot field name.
	 * @param field
	 * @return
	 */
	static public String getFullyQualifiedFieldName(final JField field) {
		Checker.notNull("parameter:field", field);

		final JReferenceType enclosingType = field.getEnclosingType();

		return enclosingType.getName() + '.' + field.getName();
	}

	/**
	 * Asserts that the source of the given javascript function contains the given number of clinit method call sites. It achieves this by scanning for $clinit.
	 * @param function
	 * @param expectedClinitCount
	 */
	public static void assertClinitCount(final JsFunction function, final int expectedClinitCount) {
		final String javascript = function.toSource();
		final int clinitCount = countClinitCallsites(javascript);
		if (clinitCount != expectedClinitCount) {
			throw new AssertionError("The function should have " + expectedClinitCount + " and not " + clinitCount
					+ " clinit function calls within its method body, source\n" + javascript);
		}
	}

	/**
	 * Generates the static initializer function name for the given type when the GWT compiler is in pretty mode. 
	 * @param typeName
	 * @return
	 */
	static public String getStaticInitializerNameForType( final String typeName ){
		return typeName.replace('.', '_') + '_' + Constants.CLINIT + "__();";
	}
	
	/**
	 * Counts the number of clinit references within the given javascript function source.
	 * @param javascript
	 * @return
	 */
	static public int countClinitCallsites(final String javascript) {
		return countOccurances(javascript, Constants.CLINIT);
	}

	/**
	 * Counts the number of occurances for the static initializer for the given class.
	 * @param typeName
	 * @param javascript
	 * @return
	 */
	static public int countClinitCallsites(final String typeName, final String javascript) {
		final String clinitFunctionName = Compiler.getStaticInitializerNameForType(typeName);
		return countOccurances(javascript, clinitFunctionName);
	}

	/**
	 * Helper which counts 
	 * @param javascript
	 * @param text
	 * @return
	 */
	static int countOccurances(final String javascript, final String text) {
		int count = 0;
		int i = 0;
		while (i < javascript.length()) {
			final int index = javascript.indexOf(text, i);
			if (index == -1) {
				break;
			}
			count++;
			i = index + 1;
		}

		return count;
	}

	/**
	 * Tests if a feature is explicitly enabled. Globally enabling all compiler options does not enable this option.
	 * @param className
	 * @param feature
	 * @return
	 */
	static public boolean isExplicitlyEnabled(final String className, final String feature) {
		boolean enabled = false;

		while (true) {

			final String featureProperty = System.getProperty(className + '.' + feature);
			if (ENABLED.equals(featureProperty)) {
				enabled = true;
				break;
			}
			if (DISABLED.equals(featureProperty)) {
				enabled = false;
				break;
			}
			break;
		}

		return enabled;
	}
	
	/**
	 * Resets the internal state of this class.
	 * Typically this should be called at the start of each permutation.
	 */
	static public void reset(){
		Compiler.resetFieldReferencesNotRequiringClint();
		Compiler.resetStaticMethodsNotRequiringClinit();
	}

	/**
	 * Helper which counts the number of times an assignment is made to the given types static initializer with the nullMethod reference.
	 * This only works in pretty mode where the names of said items can be guaranteed.
	 * @param typeName
	 * @param javascript
	 * @return
	 */
	static public int countNullMethodClinitAssignments( final String typeName, final String javascript ){
		final String clinit = Compiler.getStaticInitializerNameForType(typeName);
		final String assignment = clinit + " = " + Constants.NULL_METHOD + ";";
		return Compiler.countOccurances(javascript, assignment );
	}
}