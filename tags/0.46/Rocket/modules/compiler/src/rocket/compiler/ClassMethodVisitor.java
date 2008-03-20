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

import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JArrayType;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JInterfaceType;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JNullType;
import com.google.gwt.dev.jjs.ast.JPrimitiveType;
import com.google.gwt.dev.jjs.ast.JReferenceType;

/**
 * A simple visitor which weeds out all methods except for class methods.
 */
abstract public class ClassMethodVisitor extends JModVisitor {
	public boolean visit(final JArrayType type, final Context context) {
		return false;
	}

	public boolean visit(final JNullType type, final Context context) {
		return false;
	}

	public boolean visit(final JInterfaceType type, final Context context) {
		return false;
	}

	public boolean visit(final JPrimitiveType type, final Context context) {
		return false;
	}

	final public boolean visit(final JMethod method, final Context context) {
		boolean continueVisitingSubNodes = false;

		final JReferenceType enclosingType = method.getEnclosingType();
		if (enclosingType instanceof JClassType) {
			continueVisitingSubNodes = this.visitClassMethod(method, context);
		}
		return continueVisitingSubNodes;
	}

	abstract public boolean visitClassMethod(final JMethod method, final Context context);

	final public void endVisit(final JMethod method, final Context context) {
		final JReferenceType enclosingType = method.getEnclosingType();
		if (enclosingType instanceof JClassType) {
			this.endVisitClassMethod(method, context);
		}
	}

	abstract public void endVisitClassMethod(JMethod method, Context context);
}