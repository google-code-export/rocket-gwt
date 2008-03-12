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

import java.util.HashMap;
import java.util.Map;

import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JBinaryOperation;
import com.google.gwt.dev.jjs.ast.JBinaryOperator;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.jjs.ast.JVariable;
import com.google.gwt.dev.jjs.ast.JVariableRef;

/**
 * This optimiser attempts to replace any expression that matches
 * variable = variable + expression
 * into
 * variable =+ expression
 * 
 * where the operation (in the above case +) is +-/*% etc
 * 
 * @author Miroslav Pokorny
 */
public class VariableUpdaterOptimiser implements JavaCompilationWorker {

	final static boolean VISIT_CHILD_NODES = true;

	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		Checker.notNull("parameter:jprogram", jprogram);
		Checker.notNull("parameter:logger", logger);
		
		final TreeLogger branch = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);

		final JModVisitor visitor = new JModVisitor() {
			public boolean visit(final JBinaryOperation binaryOperation, final Context context) {
				return VariableUpdaterOptimiser.this.visitBinaryOperation(binaryOperation, context, branch);
			}
		};
		visitor.accept(jprogram);

		final boolean changed = visitor.didChange();
		branch.log(TreeLogger.DEBUG, changed ? "One or more changes were made." : "No changes were committed.", null);
		return changed;
	}

	/**
	 * Processes each encountered binary operation determining if its a potential candidate, and if it is calls {@link #replaceWithAssignmentOperator(JBinaryOperation, Context, JBinaryOperator, JExpression, TreeLogger)}
	 * to replace with the shorten optimised form.
	 * @param binaryOperation
	 * @param context
	 * @param logger
	 * @return
	 */
	protected boolean visitBinaryOperation(final JBinaryOperation binaryOperation, final Context context, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		boolean visitChildNodes = VISIT_CHILD_NODES;

		while (true) {
			TreeLogger branch = TreeLogger.NULL;
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				branch = logger.branch(TreeLogger.DEBUG, Compiler.getSource(binaryOperation), null);
			}

			// left hand side must be a variable of some sort...
			final JExpression left = binaryOperation.getLhs();
			if (false == left instanceof JVariableRef) {
				branch.log(TreeLogger.DEBUG, "Not updating a variable - optimisation aborted", null);
				break;
			}
			final JVariableRef reference = (JVariableRef) left;
			final JVariable variable = reference.getTarget();

			visitChildNodes = !VISIT_CHILD_NODES;

			// not an assignment...
			final JBinaryOperator operator = binaryOperation.getOp();
			if (false == operator.equals(JBinaryOperator.ASG)) {
				if (branch.isLoggable(TreeLogger.DEBUG)) {
					branch.log(TreeLogger.DEBUG, "Not updating a variable assignment but a \"" + new String(operator.getSymbol())
							+ "\" - optimisation aborted", null);
				}
				break;
			}

			// the right hand side must be an expression in order to hold variable + expression.
			final JExpression right = binaryOperation.getRhs();
			if (false == right instanceof JBinaryOperation) {
				if (branch.isLoggable(TreeLogger.DEBUG)) {
					branch.log(TreeLogger.DEBUG, "Right hand side is not a binary operation but a " + right.getClass().getSimpleName() + " - optimisation aborted", null);
				}
				break;
			}

			final JBinaryOperation nestedBinaryOperation = (JBinaryOperation) right;
			final JBinaryOperator nestedBinaryOperator = nestedBinaryOperation.getOp();
			// the operator optimisable ?

			final JBinaryOperator assignmentOperator = this.getAssignmentOperator(nestedBinaryOperator);

			if (null == assignmentOperator) {
				if (branch.isLoggable(TreeLogger.DEBUG)) {
					branch.log(TreeLogger.DEBUG, "Nested binary operator contains an operator that is not supported as a \"="
							+ new String(nestedBinaryOperator.getSymbol()) + "\" - optimisation aborted", null);
				}
			}

			// one of the two sides of the rbo must be the same variable as $left.
			final JExpression nestedLeftExpression = nestedBinaryOperation.getLhs();
			final JExpression nestedRightExpression = nestedBinaryOperation.getRhs();

			final JVariable leftVariable = this.getVariable(nestedLeftExpression);
			final JVariable rightVariable = this.getVariable(nestedRightExpression);

			if (variable == leftVariable) {
				this.replaceWithAssignmentOperator(binaryOperation, context, assignmentOperator, nestedRightExpression, branch);
				break;
			}
			if (variable == rightVariable) {
				
				// guard for subtract/divide/modulo because they are left associative...
				if( false == this.canVariableReferenceBeOnRightHandSide( nestedBinaryOperator )){
					if (branch.isLoggable(TreeLogger.DEBUG)) {
						branch.log(TreeLogger.DEBUG, "Because variable appears on the right hand side and the binary operator is \""  + new String(nestedBinaryOperator.getSymbol()) + "\" - optimisation aborted", null);
					}
					break;
				}
				
				// if the operator is subtract switch it to add because the variable is on the right.
				JBinaryOperator actualAssignmentOperator = assignmentOperator;
				if( nestedBinaryOperator.equals( JBinaryOperator.SUB )){
					actualAssignmentOperator = JBinaryOperator.ADD;
					
					if (branch.isLoggable(TreeLogger.DEBUG)) {
						branch.log(TreeLogger.DEBUG, "Because variable appears on the right hand side and the binary operator is a subtract changing to add.", null);
					}
				}
				
				this.replaceWithAssignmentOperator(binaryOperation, context, actualAssignmentOperator, nestedLeftExpression, branch);
				break;
			}

			if (branch.isLoggable(TreeLogger.DEBUG)) {
				branch.log(
								TreeLogger.DEBUG,
								"The nested binary operator references a different variable to the one that appears on the left - optimisation aborted",
								null);
			}
			break;
		}

		return visitChildNodes;
	}

	/**
	 * This helper attempts to extract the target variable of a potential variable reference. If the expression doesnt house a variable null is returned.
	 * @param expression
	 * @return
	 */
	protected JVariable getVariable(final JExpression expression) {
		JVariable variable = null;
		if (expression instanceof JVariableRef) {
			JVariableRef reference = (JVariableRef) expression;
			variable = reference.getTarget();
		}

		return variable;
	}
	
	/**
	 * Contains a map that serves two purposes, the keys are valid operators that may be exist in the nested binary operation and the value contains the equivalent =operator.
	 * 
	 * Eg for the key ADD the value would be the EQUALS ADD. 
	 */
	final static Map assignmentOperators = getAssignmentOperators();

	static Map getAssignmentOperators() {
		final Map map = new HashMap();
		map.put(JBinaryOperator.ADD, JBinaryOperator.ASG_ADD);
		map.put(JBinaryOperator.SUB, JBinaryOperator.ASG_SUB);
		map.put(JBinaryOperator.MUL, JBinaryOperator.ASG_MUL);
		map.put(JBinaryOperator.DIV, JBinaryOperator.ASG_DIV);
		map.put(JBinaryOperator.MOD, JBinaryOperator.ASG_MOD);
		map.put(JBinaryOperator.SHL, JBinaryOperator.ASG_SHL);
		map.put(JBinaryOperator.SHR, JBinaryOperator.ASG_SHR);
		map.put(JBinaryOperator.SHRU, JBinaryOperator.ASG_SHRU);
		map.put(JBinaryOperator.BIT_AND, JBinaryOperator.ASG_BIT_AND);
		map.put(JBinaryOperator.BIT_OR, JBinaryOperator.ASG_BIT_OR);
		map.put(JBinaryOperator.BIT_XOR, JBinaryOperator.ASG_BIT_XOR);
		return map;
	}

	/**
	 * Returns the equivalent assignment operator for any given binary operator. If one doesnt not exist null is returned. 
	 * @param binaryOperator
	 * @return
	 */
	protected JBinaryOperator getAssignmentOperator(final JBinaryOperator binaryOperator) {
		return (JBinaryOperator) VariableUpdaterOptimiser.assignmentOperators.get(binaryOperator);
	}

	/**
	 * Only i=1+i and i=1*i can be translated into the shorten form because i=i-1 is not the equivalent of i=-1+i
	 * @param operator
	 * @return
	 */
	protected boolean canVariableReferenceBeOnRightHandSide( final JBinaryOperator operator ){
		return 
		operator.equals( JBinaryOperator.ADD ) || 
		operator.equals( JBinaryOperator.MUL );
	}
	
	/**
	 * Builds and replaces the given binary operation with its equivalent shorter form.
	 * @param binaryOperation
	 * @param context
	 * @param assignmentOperator
	 * @param right
	 * @param logger
	 */
	protected void replaceWithAssignmentOperator(final JBinaryOperation binaryOperation, final Context context,
			final JBinaryOperator assignmentOperator, final JExpression right, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);

		if (false == binaryOperation.isAssignment()) {
			throw new AssertionError("The parameter:binaryOperation is not an assignment source\"" + Compiler.getSource(binaryOperation)
					+ "\".");
		}

		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:assignmentOperator", assignmentOperator);
		Checker.notNull("parameter:right", right);
		Checker.notNull("parameter:logger", logger);

		final JProgram program = binaryOperation.getJProgram();
				final SourceInfo sourceInfo = binaryOperation.getSourceInfo();
		final JType type = binaryOperation.getType();
		final JBinaryOperator op = assignmentOperator;
		final JExpression left = binaryOperation.getLhs();
		//final JExpression right = right;

		final JBinaryOperation newAssignment = new JBinaryOperation(program, sourceInfo, type, op, left, right);
		context.replaceMe( newAssignment );
		
		if (logger.isLoggable(TreeLogger.DEBUG)) {
			logger.log(TreeLogger.DEBUG, "Replaced with \"" + Compiler.getSource(newAssignment) + "\".", null);
		}
	}
}
