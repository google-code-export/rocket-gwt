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
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JIntLiteral;
import com.google.gwt.dev.jjs.ast.JLongLiteral;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JPrefixOperation;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JUnaryOperator;
import com.google.gwt.dev.jjs.ast.JVariableRef;

/**
 * This optimiser attempts to find locations that increment or decrement a variable by 1 and replace them with the more concise/shorter javascript form.
 * 
 * Eg
 * i =+ 1
 * becomes
 * i++
 * 
 * and
 * i =- 1
 * becomes
 * i--
 * 
 * This optimiser assumes that VariableUpdaterOptimiser has been run beforehand so that expression like
 * i = i + expression
 * have already been converted
 * to 
 * i =+ expression
 * 
 * @author Miroslav Pokorny
 */
public class IncrementOrDecrementByOneOptimiser implements JavaCompilationWorker{
	private final static boolean VISIT_CHILD_NODES = true;

	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);

		final JModVisitor visitor = new JModVisitor() {
			public boolean visit(final JBinaryOperation binaryOperation, final Context context) {
				IncrementOrDecrementByOneOptimiser.this.visitBinaryOperation(binaryOperation, context, branch );
				return VISIT_CHILD_NODES;
			}
		};
		visitor.accept(jprogram);

		final boolean changed = visitor.didChange();
		branch.log(TreeLogger.DEBUG, changed ? "One or more changes were made." : "No changes were committed.", null);
		return changed;
	}

	/**
	 * Checks that the given binary operation can be optimised and then proceeds
	 * to replace it with its shorten form if possible.
	 * 
	 * @param binaryOperation
	 * @param context
	 */
	protected void visitBinaryOperation(final JBinaryOperation binaryOperation, final Context context, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		while (true) {
			TreeLogger branch = TreeLogger.NULL;
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				branch = logger.branch(TreeLogger.DEBUG, Compiler.getSource(binaryOperation), null);
			}

			// if the left hand side is not a variable exit..
			final JExpression left = binaryOperation.getLhs();
			if (false == (left instanceof JVariableRef)) {
				if (logger.isLoggable(TreeLogger.DEBUG)) {
					branch.log(TreeLogger.DEBUG, "Cannot be optimised because is not a variable assignment.", null);
				}
				break;
			}
			final JVariableRef reference = (JVariableRef)left;

			// time to check the operator... must be a = or =+
			final JBinaryOperator binaryOperator = binaryOperation.getOp();
			final JExpression expression = binaryOperation.getRhs();
			
			// only process =+1 or =-1
			if (binaryOperator.equals(JBinaryOperator.ASG_ADD)) {
				this.processAdd(reference, expression, context, branch);
				break;
			}
			if (binaryOperator.equals(JBinaryOperator.ASG_SUB)) {
				this.processSubtract(reference, expression, context, branch);
				break;
			}

			// cant optimise in all other cases.
			if (branch.isLoggable(TreeLogger.DEBUG)) {
				branch.log(TreeLogger.DEBUG, "Not optimisable.", null);
			}
			break;
		}
	}

	/**
	 * Providing the right hand side of the expression is the literal one its
	 * optimisable. If the right hand side of the expression is -1 change the
	 * operator to =-
	 * 
	 * @param reference
	 *            The variable from the left hand side of the expression
	 * @param expression
	 * @param context
	 * @param logger
	 */
	protected void processAdd(final JVariableRef reference, final JExpression expression, final Context context, final TreeLogger logger) {
		Checker.notNull("parameter:reference", reference);
		Checker.notNull("parameter:expression", expression);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		while (true) {
			long value = 0;

			if (expression instanceof JIntLiteral) {
				final JIntLiteral integerLiteral = (JIntLiteral) expression;
				value = integerLiteral.getValue();
			}

			if (expression instanceof JLongLiteral) {
				final JLongLiteral longLiteral = (JLongLiteral) expression;
				value = longLiteral.getValue();
			}

			// if value is +1 replace with =+
			if (1 == value) {
				replaceWithIncrementByOne(reference, context, logger);
				break;
			}

			// if value is -1 replace with =-
			if (-1 == value) {
				replaceWithDecrementByOne(reference, context, logger);
				break;
			}

			// else complain because literal is not +/-1
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				logger.log(TreeLogger.DEBUG, "Optimisation failed because right hand side of expression is not 1 or -1.", null);
			}
			break;
		}
	}

	/**
	 * Providing the right hand side of the expression is the literal one its
	 * optimisable. If the right hand side of the expression is +1 change the
	 * operator to =+
	 * 
	 * @param reference
	 *            The variable from the left hand side of the expression
	 * @param expression
	 * @param context
	 * @param logger
	 */
	protected void processSubtract(final JVariableRef reference, final JExpression expression, final Context context,final TreeLogger logger) {
		Checker.notNull("parameter:reference", reference);
		Checker.notNull("parameter:expression", expression);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		while (true) {
			long value = 0;

			if (expression instanceof JIntLiteral) {
				final JIntLiteral integerLiteral = (JIntLiteral) expression;
				value = integerLiteral.getValue();
			}

			if (expression instanceof JLongLiteral) {
				final JLongLiteral longLiteral = (JLongLiteral) expression;
				value = longLiteral.getValue();
			}

			// if value is +1 replace with =-
			if (1 == value) {
				replaceWithDecrementByOne(reference, context, logger);
				break;
			}

			// if value is -1 replace with =+
			if (-1 == value) {
				replaceWithIncrementByOne(reference, context, logger);
				break;
			}

			// else complain because literal is not +/-1
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				logger.log(TreeLogger.DEBUG, "Optimisation failed because right hand side of expression is not 1 or -1.", null);
			}
			break;
		}
	}

	/**
	 * Replaces the current binary operation with a variable++
	 * 
	 * @param reference
	 * @param context
	 * @param logger
	 */
	protected void replaceWithIncrementByOne(final JVariableRef reference, final Context context, final TreeLogger logger) {
		this.replaceWithIncrementorDecrementByOne(reference, context, JUnaryOperator.INC, logger);
	}

	/**
	 * Replaces the current binary operation with a variable--
	 * 
	 * @param reference
	 * @param context
	 * @param logger
	 */
	protected void replaceWithDecrementByOne(final JVariableRef reference, final Context context, final TreeLogger logger) {
		this.replaceWithIncrementorDecrementByOne(reference, context, JUnaryOperator.DEC, logger);
	}

	/**
	 * Replaces the given binary operation with a variable++ or variable-- the
	 * actual operator is received in the parameter unaryOperator.
	 * 
	 * @param reference
	 * @param context
	 * @param unaryOperator
	 * @param logger
	 */
	protected void replaceWithIncrementorDecrementByOne(final JVariableRef reference, final Context context,
			final JUnaryOperator unaryOperator, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", reference);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:unaryOperator", unaryOperator);
		Checker.notNull("parameter:logger", logger);

		final JProgram program = reference.getJProgram();
		final SourceInfo sourceInfo = reference.getSourceInfo();

		// use prefix just incase assignment is within an expression.
		final JPrefixOperation incrementOrDecrementByOne = new JPrefixOperation(program, sourceInfo, unaryOperator, reference);
		context.replaceMe(incrementOrDecrementByOne);
		
		if (logger.isLoggable(TreeLogger.DEBUG)) {
			logger.log(TreeLogger.DEBUG, "Replaced with \"" + Compiler.getSource(incrementOrDecrementByOne) + "\"", null);
		}
	}
}
