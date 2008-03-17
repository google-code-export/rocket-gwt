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

import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JBlock;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JIfStatement;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JStatement;

/**
 * This optimiser attempts to cleanup or reduce if statements.
 * Currently it removes any else statements that only have an empty JBlock
 * 
 * @author Miroslav Pokorny
 */
public class EmptyBlockRemover implements JavaCompilationWorker {

	private final static boolean VISIT_CHILD_NODES = true;
	
	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);

		final JModVisitor visitor = new JModVisitor() {
			
			public boolean visit(final JIfStatement ifStatement, final Context context) {
				EmptyBlockRemover.this.visitIfStatement(ifStatement, context, branch );
				return VISIT_CHILD_NODES;
			}
		};
		visitor.accept(jprogram);

		final boolean changed = visitor.didChange();
		branch.log(TreeLogger.DEBUG, changed ? "One or more changes were made." : "No changes were committed.", null);
		return changed;
	}
	
	/**
	 * This method is called for each if statement that is encountered in the AST.
	 * @param ifStatement
	 * @param context
	 * @param logger
	 */
	protected void visitIfStatement( final JIfStatement ifStatement, final Context context, final TreeLogger logger ){
		Checker.notNull( "parameter:ifStatement", ifStatement );
		Checker.notNull( "parameter:context", context );
		Checker.notNull( "parameter:logger", logger );
		
		while( true ){
			TreeLogger branch = TreeLogger.NULL;
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				branch = logger.branch(TreeLogger.DEBUG, Compiler.getSource(ifStatement), null);
			}
			
			final JStatement elseStatement = ifStatement.getElseStmt();
			if( null == elseStatement ){
				branch.log( TreeLogger.DEBUG, "Else not present, not modified.", null );			
				break;
			}
			
			if( false == elseStatement instanceof JBlock ){
				branch.log( TreeLogger.DEBUG, "Else statement is not a JBlock, not modified.", null );
				break;
			}
			
			final JBlock block = (JBlock) elseStatement;
			final int statementCount = block.statements.size();
			if( statementCount > 0 ){
				if (logger.isLoggable(TreeLogger.DEBUG)) {
					branch.log(TreeLogger.DEBUG, "Else contains " + statementCount + " statement(s), which cannot be removed.", null);
				}
				break;
			}
			
			this.removeEmptyElseBlock( ifStatement, context, branch );
			break;
		}
	}
	
	/**
	 * This method simply replaces the existing if statement which contains an else with an empty JBlock with 
	 * a new if statement less the else.
	 * @param ifStatement
	 * @param context
	 * @param logger
	 */
	protected void removeEmptyElseBlock( final JIfStatement ifStatement, final Context context, final TreeLogger logger ){
		Checker.notNull( "parameter:ifStatement", ifStatement);
		Checker.notNull( "parameter:context", context);
		Checker.notNull( "parameter:logger", logger);
		
		final JProgram program = ifStatement.getJProgram();
		final SourceInfo sourceInfo = ifStatement.getSourceInfo();
		final JExpression ifExpression = ifStatement.getIfExpr();
		final JStatement thenStatement = ifStatement.getThenStmt();
		final JStatement elseStatement = null;
		
		final JIfStatement newIfStatementWithoutElse = new JIfStatement(program, sourceInfo, ifExpression, thenStatement, elseStatement );
		context.replaceMe( newIfStatementWithoutElse );
		
		if (logger.isLoggable(TreeLogger.DEBUG)) {
			logger.log(TreeLogger.DEBUG, "Replaced with \"" + Compiler.getSource( newIfStatementWithoutElse ) + "\".", null);
		}
	}

}
