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

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JField;
import com.google.gwt.dev.jjs.ast.JLocalDeclarationStatement;
import com.google.gwt.dev.jjs.ast.JLongLiteral;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JNode;
import com.google.gwt.dev.jjs.ast.JParameter;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.jjs.ast.JVariable;

/**
 * This compilation worker may be used to output any warnings about any localVariables of type long.
 * If warnings are enabled it will spit out all classes along with a breakdown of fields/methods that include references to long localVariables.
 * 
 * This compilation worker is run just before the steps that transform an optimised AST into javascript.
 * @author Miroslav Pokorny
 */
public class LongNotifier implements JavaCompilationWorker {

	final static boolean VISIT_CHILD_NODES = true;

	/**
	 * If warnings are enabled output all types, methods and fields with long references.
	 */
	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		if (logger.isLoggable(TreeLogger.WARN)) {
			this.listingAllLongReferences(jprogram, logger );
		}
		return false;
	}

	/**
	 * Visits the entire tree attempting to find types that contain methods or fields that have long type references.
	 * @param program
	 * @param logger
	 */
	protected void listingAllLongReferences(final JProgram jprogram, final TreeLogger logger ) {
		// visits all types/fields/methods etc.
		final JType longType = jprogram.getTypePrimitiveLong();
		
		final TreeLogger typeLogger = logger.branch( TreeLogger.WARN, "Listing all long references.", null );
		
		final LoggingJModVisitor visitor = new LoggingJModVisitor(){			
			
			public boolean visit( final JMethod method, final Context context ){
				super.visit( method, context );
				
				final TreeLogger logger = this.getLogger();
				if (method.getType() == longType) {
					logger.log( TreeLogger.WARN, "Return type", null );
				}
				
				return VISIT_CHILD_NODES;
			}
			
			public boolean visit( final JParameter parameter, final Context context ){
				if( parameter.getType() == longType ){
					this.getLogger().log( TreeLogger.WARN, parameter.getName() + " <parameter>", null );
				}
				return VISIT_CHILD_NODES;
			}
			
			public boolean visit( final JLocalDeclarationStatement declaration, final Context context ){
				final JVariable local = declaration.getLocalRef().getTarget();
				if( local.getType() == longType ){
					this.getLogger().log( TreeLogger.WARN, local.getName() + "<local>", null );
				}
				
				return VISIT_CHILD_NODES;
			}
			
			public boolean visit( final JField field, final Context context ){				
				if( field.getType() == longType ){
					this.getLogger().log( TreeLogger.WARN, Compiler.getSource( field ), null );
				}
				return VISIT_CHILD_NODES;
			}
			
			public boolean visit( final JLongLiteral literal, final Context context ){
					this.getLogger().log( TreeLogger.WARN, literal.toSource() + " <literal>", null );
				return VISIT_CHILD_NODES;
			}
			
			protected TreeLogger.Type getLoggingLevel( JNode node ){
				return TreeLogger.WARN;
			}
		};
		visitor.accept(jprogram, typeLogger);		
	}
}
