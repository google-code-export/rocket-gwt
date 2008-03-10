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

import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JNode;
import com.google.gwt.dev.jjs.ast.JReferenceType;
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
		return node.toSource().replaceAll( "\n", "\\\\n" ).replaceAll( "\r", "\\\\r" );
	}
	
	/**
	 * Builds the fully qualified name of the method including the enclosing class.
	 * @param method
	 * @return
	 */
	static public String getFullName( final JMethod method ){
		final JReferenceType enclosingType = method.getEnclosingType();
		final String methodName = method.getName();
		return enclosingType == null ? methodName : enclosingType.getName() + '.' + methodName;
	}
	
	/**
	 * Extracts the source equivalent of the given node replacing newlines and carriage returns with their escaped slash equivalent.
	 * @param node
	 * @return
	 */
	static public String getSource(final JsNode node) {
		return node.toSource().replaceAll( "\n", "\\\\n" ).replaceAll( "\r", "\\\\r" );
	}
}
