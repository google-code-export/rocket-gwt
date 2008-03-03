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
import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JBinaryOperator;
import com.google.gwt.dev.jjs.ast.JBlock;
import com.google.gwt.dev.jjs.ast.JConditional;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JIfStatement;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReturnStatement;
import com.google.gwt.dev.jjs.ast.JStatement;
import com.google.gwt.dev.jjs.ast.JType;

/**
 * This optimiser attempts to spot if/then/else statements that return values transforming them into a tenary statement equivalent.
 * 
 * eg
 * if( some condition ){
 * 		return a value;
 * } else {
 * 		return alternate value;
 * }
 * 
 * becomes
 * 
 * return some condition ? a value : alternate value;
 * 
 * @author Miroslav Pokorny
 */
public class AlternateValuesReturnedOptimiser extends TenaryTransformer implements CompilationWorker {
	/**
	 * Tests if the given if statement can be transformed into its tenary
	 * equivalent.
	 * 
	 * @param ifStatement
	 * @param context
	 * @param logger
	 */
	protected void visitIfStatement(final JIfStatement ifStatement, final Context context, final TreeLogger logger) {

		final TreeLogger branch = logger.branch(TreeLogger.DEBUG, ifStatement.getSourceInfo().toString(), null);

		while (true) {
			// must have an else
			if (false == this.hasElseStatement(ifStatement)) {
				branch.log(TreeLogger.DEBUG, "If statement is missing else - not optimised", null);
				break;
			}

			final JReturnStatement thenReturnStatement = this.findReturnStatement( ifStatement.getThenStmt() );
			if( null == thenReturnStatement ){
				branch.log( TreeLogger.DEBUG, "Then statement does not contain a solitary return statement - not optimised.", null );
				break;
			}
			
			final JReturnStatement elseReturnStatement = this.findReturnStatement( ifStatement.getElseStmt() );
			if( null == elseReturnStatement ){
				branch.log( TreeLogger.DEBUG, "Else statement does not contain a solitary return statement - not optimised.", null );
				break;
			}
			
			this.convertToTenary(ifStatement, context, thenReturnStatement, elseReturnStatement, branch);
			break;
		}
	}
	
	/**
	 * Helper which attempts to find a solitary return statement skipping blocks. It fails if it finds a block with more than one statement. 
	 * @param statement
	 * @return null if unable to find the return statement.
	 */
	protected JReturnStatement findReturnStatement( final JStatement statement ){
		JReturnStatement returnStatement = null;
		
		JStatement t = statement;
		while( true ){
			if( t instanceof JReturnStatement ){
				returnStatement = (JReturnStatement) t;
				break;
			}
			if( false == ( t instanceof JBlock )){
				break;
			}
			
			final JBlock block = (JBlock) t;
			if( block.statements.size() != 1 ){
				break;
			}
			
			t = (JStatement)block.statements.get( 0 );
		}
		
		return returnStatement;
	}
	
	protected boolean isCompatibleAssignment( JBinaryOperator operator, TreeLogger logger ){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Performs the actual task of transforming the give if statement into a tenary statement equivalent.
	 * @param ifStatement
	 * @param context
	 * @param thenReturnStatement
	 * @param elseReturnStatement
	 * @param logger
	 */
	protected void convertToTenary( final JIfStatement ifStatement, final Context context, final JReturnStatement thenReturnStatement, final JReturnStatement elseReturnStatement, final TreeLogger logger ){
		final JExpression ifExpression = ifStatement.getIfExpr();
			
		final JExpression thenExpression = thenReturnStatement.getExpr();
		final JExpression elseExpression = elseReturnStatement.getExpr();
				
		final JProgram program = ifStatement.getJProgram();
		final SourceInfo sourceInfo = ifExpression.getSourceInfo();
		final JType type = thenExpression.getType();
		
		final JConditional tenary = new JConditional( program, sourceInfo, type, ifExpression, thenExpression, elseExpression );
		
		final JReturnStatement returnTenary = new JReturnStatement( program, sourceInfo, tenary );
		
		context.replaceMe( returnTenary );
		
		logger.log( TreeLogger.DEBUG, "Converted from \"" + Compiler.getSource( ifExpression ) + "\" to \"" + Compiler.getSource(returnTenary ) + "\" - optimised.", null );
	}
}
