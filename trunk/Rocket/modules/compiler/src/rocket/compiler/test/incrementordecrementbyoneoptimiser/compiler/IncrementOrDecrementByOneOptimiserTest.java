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
package rocket.compiler.test.incrementordecrementbyoneoptimiser.compiler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import rocket.compiler.IncrementOrDecrementByOneOptimiser;
import rocket.util.client.Utilities;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JUnaryOperator;
import com.google.gwt.dev.jjs.ast.JVariableRef;

public class IncrementOrDecrementByOneOptimiserTest extends IncrementOrDecrementByOneOptimiser {

	final static String MODIFIED = "mustBeOptimised0,mustBeOptimised1,mustBeOptimised2";
	final static String UNMODIFIED = "leaveAlone0,leaveAlone1,leaveAlone2";

	/**
	 * After the optimiser has run checks that the expected binary operations that should have been updated were updated.
	 */
	public boolean work(final JProgram program, final TreeLogger logger) {
		final boolean changed = super.work(program, logger);
		this.checkExpectedVariablesWereModified();
		return changed;
	}

	/**
	 * Verifies that all expected variables that should have been modified were indeed updated.
	 */
	protected void checkExpectedVariablesWereModified() {
		final Set expected = this.getModifiedVariables();
		if (false == expected.isEmpty()) {
			throw new AssertionError(
					"Several variables that should have been removed were found unmodified, variable names: "
							+ expected);
		}
	}
	
	protected void replaceWithIncrementorDecrementByOne(final JVariableRef reference, final Context context, final JUnaryOperator unaryOperator, final TreeLogger logger) {
		final String variableName = reference.getTarget().getName();

		final Iterator unmodified = this.getUnmodifiedVariables().iterator();
		while (unmodified.hasNext()) {
			final String string = (String) unmodified.next();
			if (variableName.contains(string)) {
				throw new AssertionError("The variable \"" + reference.getSourceInfo() + "\" should not have been modified, source:\n\t" + variableName.replaceAll("\n", "\n\t"));
			}
		}

		final Iterator modified = this.getModifiedVariables().iterator();
		while (modified.hasNext()) {
			final String string = (String) modified.next();
			if (variableName.contains(string)) {
				modified.remove();
				logger.log(TreeLogger.DEBUG, "Expected variable \"" + reference.getSourceInfo() + "\" expression was modified.", null);
			}
		}
		
		super.replaceWithIncrementorDecrementByOne(reference, context, unaryOperator, logger);
	}
	

	/**
	 * THis collection contains all the variables that should have been
	 * updated, as they are updated in turn the collection shrinks and should be
	 * empty by the time the optimiser completes.
	 */
	protected Set modifiedVariables = asSet(MODIFIED);

	protected Set getModifiedVariables() {
		return this.modifiedVariables;
	}

	/**
	 * A list of variable names that should not be modified by this optimiser.
	 */
	protected Set unmodifiedVariables = asSet(UNMODIFIED);

	protected Set getUnmodifiedVariables() {
		return this.unmodifiedVariables;
	}

	protected Set asSet(final String commaSeparated) {
		return new TreeSet(Arrays.asList(Utilities.split(commaSeparated, ",", true)));
	}

}
