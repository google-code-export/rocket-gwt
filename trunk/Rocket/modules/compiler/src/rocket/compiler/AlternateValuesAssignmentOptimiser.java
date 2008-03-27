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
import com.google.gwt.dev.jjs.ast.JBinaryOperation;
import com.google.gwt.dev.jjs.ast.JBinaryOperator;
import com.google.gwt.dev.jjs.ast.JConditional;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JIfStatement;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JStatement;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.jjs.ast.JVariableRef;

/**
 * This optimiser attempts to spot if/then/else statements that update a single variable with alternate values, into
 * a tenary statement equivalent.
 * 
 * @author Miroslav Pokorny
 */
public class AlternateValuesAssignmentOptimiser extends TenaryTransformer implements JavaCompilationWorker {
	
	protected String getInitialMessage(){
		return "Locating if statements that update a variable with a new value in order to replace with a briefer form.";
	}
	/**
	 * Tests if the given if statement can be transformed into its tenary
	 * equivalent.
	 * 
	 * @param ifStatement
	 * @param context
	 * @param logger
	 */
	protected void visitIfStatement(final JIfStatement ifStatement, final Context context, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.DEBUG, rocket.compiler.Compiler.getSource( ifStatement ), null);

		while (true) {
			// must have an else
			if (false == this.hasElseStatement(ifStatement)) {
				branch.log(TreeLogger.DEBUG, "If statement is missing else - not optimised", null);
				break;
			}

			// if the heart of the then assignment is an assignment its
			// convertable.
			final JBinaryOperation thenAssignment = this.findAssignment(ifStatement.getThenStmt(), branch);
			if (null == thenAssignment) {
				break;
			}
			final JBinaryOperation elseAssignment = this.findAssignment(ifStatement.getElseStmt(), branch);
			if (null == elseAssignment) {
				break;
			}
			
			// verify that both assignments are the same type or assignment...
			final JBinaryOperator thenBinaryOperator = thenAssignment.getOp();
			final JBinaryOperator elseBinaryOperator = elseAssignment.getOp();
			if( false == thenBinaryOperator.equals( elseBinaryOperator )){
				if( branch.isLoggable( TreeLogger.DEBUG )){
				branch.log( TreeLogger.DEBUG, "Then and else assignments are not the same type of assignment \"" + new String( thenBinaryOperator.getSymbol() ) + "\" vs \"" + new String( elseBinaryOperator.getSymbol() ) +  "\" - not modified.", null );
				}
				break;
			}
			
			// do the then and else assignments update the same variable ?
			if( false == this.doBothThenAndElseStatementsUpdateSameVariable( thenAssignment, elseAssignment )){
				branch.log( TreeLogger.DEBUG, "Then and else assignments are not to the same variable - not modified.", null );
				break;
			}
			
			this.convertToTenary(ifStatement, context, thenAssignment, elseAssignment, branch);			
			break;
		}
	}
	
	protected boolean isCompatibleAssignment( final JBinaryOperator operator, final TreeLogger logger ){
		boolean compat = operator.isAssignment();
		if( false == compat ){
			if( logger.isLoggable( TreeLogger.DEBUG )){
			logger.log( TreeLogger.DEBUG, "BinaryOperator is not an assignment, operator is \"" + new String( operator.getSymbol() ) + "\" - not modified.", null );
			}
		}
		return compat;
	}
	
	/**
	 * Tests if both assignments are updating the same variable reference.
	 * @param thenAssignment
	 * @param elseAssignment
	 * @return True if they are both updating the same variable, false otherwise.
	 */
	protected boolean doBothThenAndElseStatementsUpdateSameVariable( final JBinaryOperation thenAssignment, final JBinaryOperation elseAssignment ){
		Checker.trueValue( "Then assignment is not an assignment", thenAssignment.getOp().isAssignment() );
		Checker.trueValue( "Else assignment is not an assignment", elseAssignment.getOp().isAssignment());
		
		final JVariableRef thenVariableRef = (JVariableRef)thenAssignment.getLhs();
		final JVariableRef elseVariableRef = (JVariableRef)elseAssignment.getLhs();
		return thenVariableRef.getTarget().equals( elseVariableRef.getTarget() );	
	}
	
	/**
	 * Replaces the given if statement with a tenary statement that is
	 * functionally equivalent.
	 * 
	 * @param ifStatement
	 * @param context
	 * @param thenAssignment
	 * @param elseAssignment
	 * @param logger
	 */
	protected void convertToTenary(final JIfStatement ifStatement, final Context context, final JBinaryOperation thenAssignment, final JBinaryOperation elseAssignment, final TreeLogger logger) {
		final JExpression ifExpression = ifStatement.getIfExpr(); // this expression becomes the condition part of the conditional
		final JStatement thenExpression = ifStatement.getThenStmt(); // the first value of the conditional
	
		// b = c;
		final JProgram program = ifStatement.getJProgram();
		final SourceInfo sourceInfo = ifExpression.getSourceInfo();
		final JType type = thenAssignment.getType();
		
		final JVariableRef variableReference = (JVariableRef)thenAssignment.getLhs();		
		
		// create the replacement assignment via a conditional.
		final JBinaryOperator equalsSign = thenAssignment.getOp();
		final JExpression newExpression = thenAssignment.getRhs();		
		final JExpression newAlternateExpression = elseAssignment.getRhs();		
		
		final JConditional tenary = new JConditional( program, sourceInfo, type, ifExpression, newExpression, newAlternateExpression );		
		
		// A = B ? C : A;
		final JVariableRef targetVariableReference = this.clone( variableReference );		
		final JBinaryOperation newAssignment = new JBinaryOperation( program, sourceInfo, type, equalsSign, targetVariableReference, tenary );
			
		final JStatement newStatement = newAssignment.makeStatement();
		context.replaceMe( newStatement );
				
		if( logger.isLoggable( TreeLogger.DEBUG )){
			logger.log( TreeLogger.DEBUG, "Converted into equivalent tenary statement \"" + Compiler.getSource( newStatement ) + "\"...", null );
		}
	}
}
