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
 * This optimiser attempts to spot all if statements with a single statement
 * that updates a reference of some sort with a new value into a tenary
 * statement equivalent
 * 
 * @author Miroslav Pokorny
 */
public class ConditionalAssignmentOptimiser extends TenaryTransformer implements JavaCompilationWorker {

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
			// must NOT have an else
			if (this.hasElseStatement(ifStatement)) {
				branch.log(TreeLogger.DEBUG, "If statement has else - not optimised", null);
				break;
			}

			// if the heart of the then assignment is an assignment its
			// convertable.
			final JBinaryOperation assignment = this.findAssignment(ifStatement.getThenStmt(), branch);
			if (null != assignment) {
				this.convertToTenary(ifStatement, context, assignment, branch);
			}
			break;
		}
	}
	
	protected boolean isCompatibleAssignment( final JBinaryOperator operator, final TreeLogger logger ){	
		final boolean compat = operator.equals( JBinaryOperator.ASG );
		if( false == compat ){
			if( logger.isLoggable( TreeLogger.DEBUG )){
			logger.log( TreeLogger.DEBUG, "BinaryOperator is not an \"=\" assignment, operator is \"" + new String( operator.getSymbol() ) + "\" - not modified.", null );
			}
		}
		return compat;
	}

	/**
	 * Replaces the given if statement with a tenary statement that is
	 * functionally equivalent.
	 * 
	 * @param ifStatement
	 * @param context
	 * @param logger
	 */
	protected void convertToTenary(final JIfStatement ifStatement, final Context context, final JBinaryOperation assignment, final TreeLogger logger) {		
		final JExpression ifExpression = ifStatement.getIfExpr(); // this
																	// expression
																	// becomes
																	// the
																	// condition
																	// part of
																	// the
																	// conditional

		final JProgram program = ifStatement.getJProgram();
		final SourceInfo sourceInfo = ifExpression.getSourceInfo();
		final JType type = assignment.getType();

		final JVariableRef variableReference = (JVariableRef) assignment.getLhs();

		// the second value of the conditional must be variable itself.
		final JVariableRef sourceVariableReference = this.clone(variableReference); // TODO
																					// prolly
																					// can
																					// get
																					// rid
																					// of
																					// and
																					// use
																					// variableReference
																					// directly
		final JVariableRef targetVariableReference = this.clone(variableReference);

		// create the replacement assignment via a conditional.
		final JBinaryOperator equalsSign = assignment.getOp();
		final JExpression newExpression = assignment.getRhs();
		final JConditional tenary = new JConditional(program, sourceInfo, type, ifExpression, newExpression, sourceVariableReference);

		// A = B ? C : A;

		final JBinaryOperation newAssignment = new JBinaryOperation(program, sourceInfo, type, equalsSign, targetVariableReference,	tenary);

		final JStatement newStatement = newAssignment.makeStatement();
		context.replaceMe(newStatement);
		
		if( logger.isLoggable( TreeLogger.DEBUG )){
			logger.log( TreeLogger.DEBUG, "Converted into equivalent tenary statement \"" + Compiler.getSource( newStatement ) + "\"...", null );
		}
	}

}
