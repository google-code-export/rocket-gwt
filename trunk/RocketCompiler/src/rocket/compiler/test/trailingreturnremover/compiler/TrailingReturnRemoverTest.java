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
package rocket.compiler.test.trailingreturnremover.compiler;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import rocket.compiler.TrailingReturnRemover;
import rocket.util.client.Utilities;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReturnStatement;

/**
 * This class decorates the original {@link TrailingReturnRemover} adding some
 * tests to check that the appropriate return statements were removed or left
 * alone.
 * 
 * @author Miroslav Pokorny
 */
public class TrailingReturnRemoverTest extends TrailingReturnRemover {

	final static String MODIFIED = "hasLoneReturn";
	final static String UNMODIFIED = "returnsLiteral,returnsValueFromAnotherMethodCall";

	/**
	 * After the optimiser has run checks that the methods that should have been
	 * updated were updated.
	 */
	public boolean work(final JProgram program, final TreeLogger logger) {
		final boolean changed = super.work(program, logger);
		this.checkExpectedMethodsWereModified();
		return changed;
	}

	/**
	 * Verifies that the methods that should have been modified were in deed
	 * updated.
	 */
	protected void checkExpectedMethodsWereModified() {
		final Set expected = this.getModifiedMethods();
		if (false == expected.isEmpty()) {
			throw new AssertionError(
					"Several methods containing return statements that should have been removed were found unmodified, methods: "
							+ expected);
		}
	}

	protected boolean attemptToRemoveReturnStatements(final JMethod method, final Context context, final TreeLogger logger) {
		try {
			this.setCurrentMethod(method);
			return super.attemptToRemoveReturnStatements(method, context, logger);
		} finally {
			this.clearCurrentMethod();
		}
	}

	JMethod currentMethod;

	JMethod getCurrentMethod() {
		return this.currentMethod;
	}

	void setCurrentMethod(final JMethod currentMethod) {
		this.currentMethod = currentMethod;
	}

	void clearCurrentMethod() {
		this.currentMethod = null;
	}

	protected void removeReturnStatement(final JReturnStatement returnStatement, final Context context, final TreeLogger logger) {
		final JMethod method = this.getCurrentMethod();
		final String methodName = method.getName();

		// double check this method should not have been updated
		if (this.getUnmodifiedMethods().contains(methodName) || methodName.startsWith("test")) {
			throw new AssertionError("The method " + method.getSourceInfo() + " should not have been modified, source:\n\t"
					+ method.toSource().replaceAll("\n", "\n\t"));
		}

		// if the method is on the watch list remove it.
		final Set expected = this.getModifiedMethods();
		if (expected.contains(methodName)) {
			expected.remove(methodName);
			logger.log(TreeLogger.DEBUG, "Expected method was modified.", null);
		}

		super.removeReturnStatement(returnStatement, context, logger);
	}

	/**
	 * THis collection contains all the methods that should have been updated,
	 * as they are updated in turn the collection shrinks and should be empty by
	 * the time the optimiser completes.
	 */
	protected Set modifiedMethods = asSet(MODIFIED);

	protected Set getModifiedMethods() {
		return this.modifiedMethods;
	}

	/**
	 * A list of method names that should not be modified by this optimiser.
	 */
	protected Set unmodifiedMethods = asSet(UNMODIFIED);

	protected Set getUnmodifiedMethods() {
		return this.unmodifiedMethods;
	}

	protected Set asSet(final String commaSeparated) {
		return new TreeSet(Arrays.asList(Utilities.split(commaSeparated, ",", true)));
	}

}
