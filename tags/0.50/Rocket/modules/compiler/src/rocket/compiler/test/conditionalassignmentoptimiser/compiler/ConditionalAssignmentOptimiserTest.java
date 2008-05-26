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
package rocket.compiler.test.conditionalassignmentoptimiser.compiler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JBinaryOperation;
import com.google.gwt.dev.jjs.ast.JIfStatement;
import com.google.gwt.dev.jjs.ast.JProgram;

import rocket.compiler.ConditionalAssignmentOptimiser;
import rocket.util.client.Utilities;

public class ConditionalAssignmentOptimiserTest extends ConditionalAssignmentOptimiser {
	final static String MODIFIED = "mustChange0,mustChange1";
	final static String UNMODIFIED = "mustNotChange";

	/**
	 * After the optimiser has run checks that the expected binary operation that should have been updated were updated.
	 */
	public boolean work(final JProgram program, final TreeLogger logger) {
		final boolean changed = super.work(program, logger);
		this.checkExpectedBinaryOperationsWereModified();
		return changed;
	}

	/**
	 * Verifies that the BinaryOperations that should have been modified were in
	 * deed updated.
	 */
	protected void checkExpectedBinaryOperationsWereModified() {
		final Set expected = this.getModifiedIfStatements();
		if (false == expected.isEmpty()) {
			throw new AssertionError(
					"Several If statements containing variable assignments that should have been made into tenary statement were found unmodified, if statements: "
							+ expected);
		}
	}

	protected void convertToTenary(final JIfStatement ifStatement, final Context context, final JBinaryOperation assignment, final TreeLogger logger) {
		// convert the if statement to source and scan for magic text
		final String sourceForm = ifStatement.toSource();		
		
		if (false == assignment.isAssignment()) {
			throw new AssertionError("The binary operation " + assignment.getSourceInfo()
					+ " should not have been modified because it is not an assignment, source:\n\t"
					+ sourceForm.replaceAll("\n", "\n\t"));
		}

		final Iterator unmodified = this.getUnmodifiedIfStatements().iterator();
		while (unmodified.hasNext()) {
			final String string = (String) unmodified.next();
			if (sourceForm.contains(string)) {
				throw new AssertionError("The if statement " + ifStatement.getSourceInfo()
						+ " should not have been modified, source:\n\t" + sourceForm.replaceAll("\n", "\n\t"));
			}
		}

		final Iterator modified = this.getModifiedIfStatements().iterator();
		while (modified.hasNext()) {
			final String string = (String) modified.next();
			if (sourceForm.contains(string)) {
				modified.remove();
				logger.log(TreeLogger.DEBUG, "Expected if statement was modified.", null);
			}
		}

		super.convertToTenary(ifStatement, context, assignment, logger);
	}

	/**
	 * THis collection contains all the BinaryOperations that should have been
	 * updated, as they are updated in turn the collection shrinks and should be
	 * empty by the time the optimiser completes.
	 */
	protected Set modifiedIfStatements = asSet(MODIFIED);

	protected Set getModifiedIfStatements() {
		return this.modifiedIfStatements;
	}

	/**
	 * A list of binary operation localVariables that should not be modified by this optimiser.
	 */
	protected Set unmodifiedIfStatements = asSet(UNMODIFIED);

	protected Set getUnmodifiedIfStatements() {
		return this.unmodifiedIfStatements;
	}

	protected Set asSet(final String commaSeparated) {
		return new TreeSet(Arrays.asList(Utilities.split(commaSeparated, ",", true)));
	}
}
