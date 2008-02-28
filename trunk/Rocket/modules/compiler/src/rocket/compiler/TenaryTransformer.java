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
import com.google.gwt.dev.jjs.ast.JBlock;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JExpressionStatement;
import com.google.gwt.dev.jjs.ast.JField;
import com.google.gwt.dev.jjs.ast.JFieldRef;
import com.google.gwt.dev.jjs.ast.JIfStatement;
import com.google.gwt.dev.jjs.ast.JLocal;
import com.google.gwt.dev.jjs.ast.JLocalRef;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JNode;
import com.google.gwt.dev.jjs.ast.JParameter;
import com.google.gwt.dev.jjs.ast.JParameterRef;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReferenceType;
import com.google.gwt.dev.jjs.ast.JStatement;
import com.google.gwt.dev.jjs.ast.JVariableRef;

/**
 * Common base class for any optimiser that attempts to convert if statements into a tenary or conditional statement
 * equivalent.
 * @author Miroslav Pokorny
 */
abstract class TenaryTransformer {

	/**
	 * Visits all if statements passing each one to be processed by the {@link #visitIfStatement(JIfStatement, Context, TreeLogger)}.
	 * @param jprogram
	 * @param logger
	 * @return
	 */
	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);

		final JModVisitor visitor = new JModVisitor() {
			public boolean visit(final JIfStatement ifStatement, final Context context) {
				TenaryTransformer.this.visitIfStatement(ifStatement, context, branch);
				return false;// dont visit child nodes.
			}
		};
		visitor.accept(jprogram);
		return visitor.didChange();
	}

	/**
	 * Sub classes must override this method to do something with the given if statement.
	 * @param ifStatement
	 * @param context
	 * @param logger
	 */
	abstract protected void visitIfStatement( JIfStatement ifStatement, Context context, TreeLogger logger );
	
	/**
	 * Helper which tests if the given if statement has an accompanying else.
	 * 
	 * @param ifStatement
	 * @return
	 */
	protected boolean hasElseStatement(final JIfStatement ifStatement) {
		return ifStatement.getElseStmt() != null;
	}

	/**
	 * This method starts at a statement skipping over blocks of one statement
	 * attempting to find the first assignment. If any block has more than one
	 * statement the search stops.
	 * 
	 * @param statement
	 * @param logger
	 * @return
	 */
	protected JBinaryOperation findAssignment(final JStatement statement, final TreeLogger logger) {
		JBinaryOperation assignment = null;
	
		JNode node = statement;
		while (true) {
			if (node instanceof JExpressionStatement) {
				final JExpressionStatement expressionStatement = (JExpressionStatement) node;
				final JExpression expression = expressionStatement.getExpr();
				if (false == expression instanceof JBinaryOperation) {
					logger.log( TreeLogger.DEBUG, "Content of statement is not an expression but a " + expression.getClass().getName() + " - not modified.", null );
					break;
				}
	
				// lhs has to be a JVariableReference, op JBinaryOperator "=",
				// rhs ignore
				final JBinaryOperation binaryOperation = (JBinaryOperation) expression;
				final JExpression leftHandSide = binaryOperation.getLhs();
				if (false == leftHandSide instanceof JVariableRef) {
					logger.log( TreeLogger.DEBUG, "Left hand side of BinaryOperator is not a variable reference - not modified.", null );
					break;
				}
	
				final JBinaryOperator binaryOperator = binaryOperation.getOp();
				if (false == this.isCompatibleAssignment( binaryOperator, logger )) {
					break;
				}
	
				// found assignment stop searching...
				assignment = binaryOperation;
				break;
			}
	
			if (false == (node instanceof JBlock)) {
				logger.log( TreeLogger.DEBUG, "Found a non assignment statement, type: " + node.getClass().getName() + " - not modified.", null );
				break;
			}
	
			final JBlock block = (JBlock) node;
			final int blockStatementCount = block.statements.size(); 
			if ( blockStatementCount != 1 ) {
				logger.log( TreeLogger.DEBUG, "Nested blocked contains " + blockStatementCount + " statements when 1 is expected - not modified.", null );
				break;
			}
	
			// try again within the statement inside the block.
			node = (JStatement) block.statements.get(0);
		}
	
		return assignment;
	}
	
	abstract protected boolean isCompatibleAssignment( JBinaryOperator operator, TreeLogger logger ); 

	/**
	 * This method clones the given variable reference, returning a new instance of the same type.
	 * 
	 * @param source
	 * @return
	 */
	protected JVariableRef clone(final JVariableRef source) {
		JVariableRef copy = null;
	
		final JProgram program = source.getJProgram();
		final SourceInfo sourceInfo = source.getSourceInfo();
	
		while (true) {
			if (source instanceof JFieldRef) {
				final JFieldRef sourceField = (JFieldRef) source;
				final JExpression instance = sourceField.getInstance();
				final JField field = sourceField.getField();
				final JReferenceType enclosingType = sourceField.getEnclosingType();
				copy = new JFieldRef(program, sourceInfo, instance, field, enclosingType);
	
				break;
			}
	
			if (source instanceof JLocalRef) {
				final JLocalRef sourceLocal = (JLocalRef) source;
				final JLocal local = sourceLocal.getLocal();
				copy = new JLocalRef(program, sourceInfo, local);
				break;
			}
	
			if (source instanceof JParameterRef) {
				final JParameterRef sourceParameter = (JParameterRef) source;
				final JParameter parameter = sourceParameter.getParameter();
	
				copy = new JParameterRef(program, sourceInfo, parameter);
				break;
			}
	
			throw new AssertionError("Then statement belonging to an optimisable if contains an unknown JVariableRef type: "+ source.getClass());
		}
	
		return copy;
	}
}
