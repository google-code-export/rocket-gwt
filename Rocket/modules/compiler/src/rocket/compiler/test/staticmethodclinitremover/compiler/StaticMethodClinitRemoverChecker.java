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
package rocket.compiler.test.staticmethodclinitremover.compiler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import rocket.compiler.Compiler;
import rocket.compiler.JavaScriptCompilationWorker;
import rocket.compiler.test.staticfieldclinitremover.compiler.StaticFieldClinitRemoverTest;
import rocket.util.client.Utilities;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.js.ast.JsContext;
import com.google.gwt.dev.js.ast.JsFunction;
import com.google.gwt.dev.js.ast.JsName;
import com.google.gwt.dev.js.ast.JsProgram;
import com.google.gwt.dev.js.ast.JsVisitor;

/**
 * This class decorates its super class adding some checks to some clinits are
 * removed and others are left untouched.
 * 
 * @author Miroslav Pokorny
 */
public class StaticMethodClinitRemoverChecker implements JavaScriptCompilationWorker {
	/**
	 * Double checks that some functions contain clint calls and others dont.
	 */
	public boolean work(final JsProgram program, final TreeLogger logger) {
		final JsVisitor visitor = new JsVisitor() {
			public boolean visit(final JsFunction function, final JsContext context) {
				StaticMethodClinitRemoverChecker.this.visitFunction(function);

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

			if( nameText.contains("mustHaveClinit0")){
				Compiler.assertClinitCount( function, 1 );
			}
			if( nameText.contains("mustHaveClinit1")){
				Compiler.assertClinitCount( function, 1 );						
			}
			if( nameText.contains("mustNotHaveClinit0")){
				Compiler.assertClinitCount( function, 0 );	
			}			
			if( nameText.contains("mustHaveClinit2")){
				Compiler.assertClinitCount( function, 1 );	
			}

			if( nameText.contains("mustNotHaveClinit1")){
				Compiler.assertClinitCount( function, 0 );	
			}
		}
	}
}
