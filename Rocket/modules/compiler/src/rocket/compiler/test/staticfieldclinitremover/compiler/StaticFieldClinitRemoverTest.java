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
package rocket.compiler.test.staticfieldclinitremover.compiler;

import rocket.compiler.Compiler;
import rocket.compiler.JavaScriptCompilationWorker;
import rocket.compiler.StaticFieldClinitRemover;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.js.ast.JsContext;
import com.google.gwt.dev.js.ast.JsFunction;
import com.google.gwt.dev.js.ast.JsName;
import com.google.gwt.dev.js.ast.JsProgram;
import com.google.gwt.dev.js.ast.JsVisitor;

/**
 * This class decorates its super class adding some checks to some clints are
 * removed and others are left untouched.
 * 
 * @author Miroslav Pokorny
 */
public class StaticFieldClinitRemoverTest implements JavaScriptCompilationWorker {

	/**
	 * Double checks that some functions contain clint calls and others dont.
	 */
	public boolean work(final JsProgram program, final TreeLogger logger) {
		final JsVisitor visitor = new JsVisitor() {
			public boolean visit(final JsFunction function, final JsContext context) {
				StaticFieldClinitRemoverTest.this.visitFunction(function);

				return false;
			}
		};
		visitor.accept(program);
		return false;
	}

	protected void visitFunction(final JsFunction function) {
		final JsName name = function.getName();
		if (name != null) {

			final String nameText = name.getIdent();
			final String javascript = function.toSource();

			if( nameText.contains("testHasNoStaticInitializer")){
				Compiler.assertClinitCount( function, 0 );
			}
			if( nameText.contains("testSecondFieldAccessNotRequiredDueToPreceedingFieldAccess")){
				Compiler.assertClinitCount( function, 1 );						
			}

			if( nameText.contains("testSecondFieldAccessStillRequiresClinit")){
				Compiler.assertClinitCount( function, 2 );	
			}

			if( nameText.contains("testFieldAccessPreceededByMethodCall")){
				Compiler.assertClinitCount( function, 0 );	
			}

			if( nameText.contains("testFieldAccessClinitStillPreceededByMethodCallIsOutOfScope")){
				Compiler.assertClinitCount( function, 1 );	
			}
		}
	}

}
