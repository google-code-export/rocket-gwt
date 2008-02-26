package rocket.compiler;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JBinaryOperation;
import com.google.gwt.dev.jjs.ast.JBinaryOperator;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JNode;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JVariableRef;
import com.google.gwt.dev.jjs.ast.js.JMultiExpression;

/**
 * This optimiser attempts to remove any localVariables that are assigned to
 * themselves. eg a = a;
 * 
 * @author Miroslav Pokorny
 */
public class VariableAssignedToSelfRemover implements CompilationWorker {

	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		final TreeLogger branchLogger = logger.branch(TreeLogger.INFO, this.getClass().getName(), null);

		final JModVisitor visitor = new JModVisitor() {

			public boolean visit(final JBinaryOperation binaryOperation, final Context context) {
				VariableAssignedToSelfRemover.this.attemptToRemoveBinaryOperation(binaryOperation, context, branchLogger);
				return false; // dont visit children.
			}
		};
		visitor.accept(jprogram);

		final boolean changed = visitor.didChange();
		logger.log(TreeLogger.DEBUG, changed ? "One or more changes were made." : "No changes were committed.", null);
		return changed;
	}

	/**
	 * Checks each and every binary operation and finds those that are variable
	 * assignments to self and removes them.
	 * 
	 * @param binaryOperation
	 * @param context
	 * @param logger
	 */
	protected void attemptToRemoveBinaryOperation(final JBinaryOperation binaryOperation, final Context context, final TreeLogger logger) {
		final TreeLogger branch = logger.branch(TreeLogger.DEBUG, binaryOperation.getSourceInfo().toString(), null);

		if (this.isVariableAssignmentToSelf(binaryOperation, context, branch)) {
			this.removeAssignment(binaryOperation, context, branch);
		}
	}

	/**
	 * Tests if the binary operation contains an assignment of a variable with itself.
	 * If any tests fail messages are logged to the given logger
	 * @param binaryOperation
	 * @param context
	 * @param logger
	 * @return
	 */
	protected boolean isVariableAssignmentToSelf(final JBinaryOperation binaryOperation, final Context context, final TreeLogger logger) {
		boolean assignmentToSelf = false;

		while (true) {
			final JExpression left = binaryOperation.getLhs();
			final JBinaryOperator op = binaryOperation.getOp();
			final JExpression right = binaryOperation.getRhs();

			// if its not a variable (parameter/parametersLocalsAndLiterals/field) not optimisable.
			if (false == this.isVariableReference(left)) {
				logger.log(TreeLogger.DEBUG, "Target of expression is not a variable - not modified", null);
				break;
			}

			// if its not an assign operation not optimisable.
			if (false == op.isAssignment()) {
				logger.log(TreeLogger.DEBUG, "Expression is not an assignment - not modified", null);
				break;
			}

			// if the right side of the expression is not a param/parametersLocalsAndLiterals/field
			// opt fails.
			if (false == this.isVariableReference(right)) {
				logger.log(TreeLogger.DEBUG, "Expression source is not a variable - not modified", null);
				break;
			}

			if (false == this.isSameVariableReference((JVariableRef) left, (JVariableRef) right)) {
				logger.log(TreeLogger.DEBUG, "Expression source and target localVariables are not the same - not modified", null);
				break;
			}

			// the same variable is getting assigned with its own value.
			assignmentToSelf = true;
			break;
		}

		return assignmentToSelf;
	}

	/**
	 * Helper which tests if the given node is in fact a variable reference.
	 * 
	 * @param node
	 * @return
	 */
	protected boolean isVariableReference(final JNode node) {
		return node instanceof JVariableRef;
	}

	/**
	 * Tests if two variable references refer to the same variable.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	protected boolean isSameVariableReference(final JVariableRef first, final JVariableRef second) {
		return (first.getTarget().equals(second.getTarget()));
	}

	/**
	 * Attempts to remove a binary operation that consists of a variable being
	 * updated with itself.
	 * 
	 * @param binaryOperation
	 * @param context
	 * @param logger
	 * @return
	 */
	protected boolean removeAssignment(final JBinaryOperation binaryOperation, final Context context, final TreeLogger logger) {
		if (context.canRemove()) {
			context.removeMe();
			logger.log(TreeLogger.DEBUG, "Removed variable being assigned to itself- modified", null);
		} else {
			final JProgram program = binaryOperation.getJProgram();
			final SourceInfo sourceInfo = binaryOperation.getSourceInfo();
			final JMultiExpression empty = new JMultiExpression(program, sourceInfo);
			context.replaceMe(empty);

			logger.log(TreeLogger.DEBUG,
					"Removed variable being assigned to itself by replacing it with an empty expression. - modified", null);
		}
		return false;
	}

}