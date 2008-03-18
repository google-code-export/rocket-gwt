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
package rocket.compiler.test.variableupdateroptimiser.compiler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import rocket.compiler.VariableUpdaterOptimiser;
import rocket.util.client.Utilities;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JBinaryOperation;
import com.google.gwt.dev.jjs.ast.JBinaryOperator;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JProgram;

public class VariableUpdaterOptimiserTest extends VariableUpdaterOptimiser {

	final static String MODIFIED = "optimiseAdd0,optimiseAdd1,optimiseSubtract,optimiseMultiply0,optimiseMultiply1,optimiseDivide,optimiseModulo,optimiseLeftShift,optimiseRightShift,optimiseUnsignedRightShift,optimiseAnd,optimiseOr,optimiseExclusiveOr";
	final static String UNMODIFIED = "leaveAlone0,leaveAlone1,leaveSubtractAlone,leaveDivideAlone";

	/**
	 * After the optimiser has run checks that the expected binary operations that should have been updated were updated.
	 */
	public boolean work(final JProgram program, final TreeLogger logger) {
		final boolean changed = super.work(program, logger);
		this.checkExpectedBinaryOperationsWereModified();
		return changed;
	}

	/**
	 * Verifies that the BinaryOperations that should have been modified were indeed updated.
	 */
	protected void checkExpectedBinaryOperationsWereModified() {
		final Set expected = this.getModifiedBinaryOperations();
		if (false == expected.isEmpty()) {
			throw new AssertionError(
					"Several BinaryOperations containing variable assignments that should have been removed were found unmodified, BinaryOperations: "
							+ expected);
		}
	}
	
	protected void replaceWithAssignmentOperator(final JBinaryOperation binaryOperation, final Context context, final JBinaryOperator assignmentOperator, final JExpression right, final TreeLogger logger) {
	
		final String sourceForm = binaryOperation.toSource();
		if (false == binaryOperation.isAssignment()) {
			throw new AssertionError("The binary operation " + binaryOperation.getSourceInfo()
					+ " should not have been modified because it is not an assignment, source:\n\t"
					+ sourceForm.replaceAll("\n", "\n\t"));
		}

		final Iterator unmodified = this.getUnmodifiedBinaryOperations().iterator();
		while (unmodified.hasNext()) {
			final String string = (String) unmodified.next();
			if (sourceForm.contains(string)) {
				throw new AssertionError("The binary operation " + binaryOperation.getSourceInfo()
						+ " should not have been modified, source:\n\t" + sourceForm.replaceAll("\n", "\n\t"));
			}
		}

		final Iterator modified = this.getModifiedBinaryOperations().iterator();
		while (modified.hasNext()) {
			final String string = (String) modified.next();
			if (sourceForm.contains(string)) {
				modified.remove();
				logger.log(TreeLogger.DEBUG, "Expected binary operation was modified.", null);
			}
		}
		
		super.replaceWithAssignmentOperator(binaryOperation, context, assignmentOperator, right, logger);
	}
	

	/**
	 * THis collection contains all the BinaryOperations that should have been
	 * updated, as they are updated in turn the collection shrinks and should be
	 * empty by the time the optimiser completes.
	 */
	protected Set modifiedBinaryOperations = asSet(MODIFIED);

	protected Set getModifiedBinaryOperations() {
		return this.modifiedBinaryOperations;
	}

	/**
	 * A list of method names that should not be modified by this optimiser.
	 */
	protected Set unmodifiedBinaryOperations = asSet(UNMODIFIED);

	protected Set getUnmodifiedBinaryOperations() {
		return this.unmodifiedBinaryOperations;
	}

	protected Set asSet(final String commaSeparated) {
		return new TreeSet(Arrays.asList(Utilities.split(commaSeparated, ",", true)));
	}

}
