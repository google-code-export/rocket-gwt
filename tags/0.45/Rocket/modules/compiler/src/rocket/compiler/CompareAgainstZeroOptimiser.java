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

import java.math.BigInteger;

import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.js.ast.JsBinaryOperation;
import com.google.gwt.dev.js.ast.JsBinaryOperator;
import com.google.gwt.dev.js.ast.JsContext;
import com.google.gwt.dev.js.ast.JsDecimalLiteral;
import com.google.gwt.dev.js.ast.JsExpression;
import com.google.gwt.dev.js.ast.JsIntegralLiteral;
import com.google.gwt.dev.js.ast.JsModVisitor;
import com.google.gwt.dev.js.ast.JsPrefixOperation;
import com.google.gwt.dev.js.ast.JsProgram;
import com.google.gwt.dev.js.ast.JsUnaryOperator;

/**
 * This optimiser attempts to use briefer javascript forms to compare a variable against 0 for equality or non equality.
 * 
 * if( i == 0 ){
 * becomes
 * if( !i ){
 * 
 * and
 * 
 * if( i != 0 ){
 * becomes
 * if( i ){
 * 
 * @author Miroslav Pokorny
 */
public class CompareAgainstZeroOptimiser implements JavaScriptCompilationWorker {

	final static boolean VISIT_CHILD_NODES = true;

	/**
	 * Visits all binary operations attempting to locate and replace tests against 0 with an optimised javascript form.
	 */
	public boolean work(final JsProgram jsprogram, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.DEBUG, this.getClass().getName(), null);

		final JsModVisitor visitor = new JsModVisitor() {
			public boolean visit(final JsBinaryOperation binaryOperation, final JsContext context) {
				CompareAgainstZeroOptimiser.this.visitBinaryOperation(binaryOperation, context, branch);
				return VISIT_CHILD_NODES;
			}
		};
		visitor.accept(jsprogram);

		final boolean changed = visitor.didChange();
		logger.log(TreeLogger.DEBUG, changed ? "One or more changes were made." : "No changes were committed.", null);
		return changed;
	}

	/**
	 * Tests if the given binary operation is a comparison between a number and zero for equality or non equality. 
	 * @param binaryOperation
	 * @param context
	 * @param logger
	 */
	protected void visitBinaryOperation(final JsBinaryOperation binaryOperation, final JsContext context, final TreeLogger logger) {

		while (true) {
			final JsBinaryOperator binaryOperator = binaryOperation.getOperator();

			TreeLogger branch = TreeLogger.NULL;
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				branch = logger.branch(TreeLogger.DEBUG, "Processing binary operation, source: " + Compiler.getSource(binaryOperation), null);
			}
			
			// equals something
			if (binaryOperator.equals(JsBinaryOperator.EQ)) {
				this.processEqualsTest(binaryOperation, context, branch);
				break;
			}

			// not equals something...
			if (binaryOperator.equals(JsBinaryOperator.NEQ)) {
				this.processNotEqualsTest(binaryOperation, context, branch);
				break;
			}

			if (logger.isLoggable(TreeLogger.DEBUG)) {
				branch.log(TreeLogger.DEBUG, "Not optimised because not an equal/not equals test but a '" + binaryOperator.getSymbol() + "'", null);
			}
			break;
		}
	}

	/**
	 * This method double checks that one of the operators is 0 and substitutes the binary operation with the optimised form if possible.
	 * @param binaryOperation
	 * @param context
	 * @param logger
	 */
	protected void processEqualsTest(final JsBinaryOperation binaryOperation, final JsContext context, final TreeLogger logger) {
		while (true) {
			final JsExpression left = binaryOperation.getArg1();
			final JsExpression right = binaryOperation.getArg2();

			if (this.isZero(left)) {
				this.replaceEqualsZeroTest(binaryOperation, context, right, logger);
				break;
			}

			if (this.isZero(right)) {
				this.replaceEqualsZeroTest(binaryOperation, context, left, logger);
				break;
			}

			// neither side is the zero literal...
			if (logger.isLoggable(TreeLogger.DEBUG)) {
				logger.log(TreeLogger.DEBUG, "Neither operand is the zero literal, optimisation not possible.", null);
			}
			break;
		}
	}

	/**
	 * Replaces the given binary operation (which should be an equals 0) with a briefer javascript equalivant of !expression
	 * @param binaryOperation
	 * @param context
	 * @param expression
	 * @param logger
	 */
	protected void replaceEqualsZeroTest(final JsBinaryOperation binaryOperation, final JsContext context, final JsExpression expression, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:expression", expression);
		Checker.notNull("parameter:logger", logger);

		if (false == binaryOperation.getOperator().equals(JsBinaryOperator.EQ)) {
			throw new AssertionError("The parameter:binaryOperation is not an equals test but a " + binaryOperation);
		}
		
		final JsPrefixOperation inverted = new JsPrefixOperation(JsUnaryOperator.NOT, expression);
		context.replaceMe(inverted);

		if (logger.isLoggable(TreeLogger.DEBUG)) {
			logger.log(TreeLogger.DEBUG, "Replaced with \""	+ Compiler.getSource(inverted) + "\"", null);
		}
	}

	/**
	 * Given that the binary operation is not equality test attempt to replace it with an optimised javascript form if one of the arguments is a zero literal.
	 * @param binaryOperation
	 * @param context
	 * @param logger
	 */
	protected void processNotEqualsTest(final JsBinaryOperation binaryOperation, final JsContext context, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:logger", logger);

		if ( false == binaryOperation.getOperator().equals(JsBinaryOperator.NEQ)) {
			throw new AssertionError("The parameter:binaryOperation is not an not equals test but a " + binaryOperation);
		}
		
		while( true ){
			final JsExpression left = binaryOperation.getArg1();
			final JsExpression right = binaryOperation.getArg2();
			
			if( this.isZero( left )){
				this.replaceNotEqualsZeroTest(binaryOperation, context, right, logger);
				break;
			}
			
			if( this.isZero( right )){
				this.replaceNotEqualsZeroTest(binaryOperation, context, left, logger);
				break;				
			}
			
			// neither side is the zero literal...
			if( logger.isLoggable( TreeLogger.DEBUG )){
				logger.log( TreeLogger.DEBUG, "Neither operand is the zero literal - not optimised.", null );
			}			
			break;
		}
	}

	/**
	 * Replaces the given binary operation (which should be an equals 0) with a briefer javascript equalivant of !expression
	 * @param binaryOperation
	 * @param context
	 * @param expression
	 * @param logger
	 */
	protected void replaceNotEqualsZeroTest(final JsBinaryOperation binaryOperation, final JsContext context,
			final JsExpression expression, final TreeLogger logger) {
		Checker.notNull("parameter:binaryOperation", binaryOperation);
		Checker.notNull("parameter:context", context);
		Checker.notNull("parameter:expression", expression);
		Checker.notNull("parameter:logger", logger);

		if (false == binaryOperation.getOperator().equals(JsBinaryOperator.NEQ)) {
			throw new AssertionError("The parameter:binaryOperation is not an not equals test but a " + binaryOperation);
		}

		context.replaceMe(expression);

		if (logger.isLoggable(TreeLogger.DEBUG)) {
			logger.log(TreeLogger.DEBUG, "Replaced with \""	+ Compiler.getSource(expression) + "\"", null);
		}
	}
	
	/**
	 * Tests if the given expression represents the zero literal.
	 * @param expression
	 * @return true if the expression is the zero literal otherwise returns false.
	 */
	protected boolean isZero(final JsExpression expression) {
		Checker.notNull("parameter:expression", expression);
		
		boolean zero = false;

		while (true) {
			if (expression instanceof JsDecimalLiteral) {
				final JsDecimalLiteral decimal = (JsDecimalLiteral) expression;
				zero = decimal.getValue().equals("0");
				break;
			}

			// not really sure what a JsIntegralLiteral holds...
			if (expression instanceof JsIntegralLiteral) {
				final JsIntegralLiteral integral = (JsIntegralLiteral) expression;
				zero = BigInteger.ZERO.equals(integral.getValue());
				break;
			}

			// not a numeric literal...
			zero = false;
			break;
		}

		return zero;
	}
}
