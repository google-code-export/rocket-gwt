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
package rocket.compiler.test.unusedlocalvariableremover.compiler;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import rocket.compiler.UnusedLocalVariableRemover;
import rocket.util.client.Utilities;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JVariable;

/**
 * This class decorates its super class adding some checks to ensure expected local variable declarations and assignments are removed.
 * 
 * @author Miroslav Pokorny
 */
public class UnusedLocalVariableRemoverTest extends UnusedLocalVariableRemover {
	final static String REMOVABLE = "mustBeRemoved0,mustBeRemoved1,mustBeRemoved2,mustBeRemoved3,mustBeRemoved4,mustBeRemoved5,mustBeRemoved6";
	final static String MUST_NOT_BE_REMOVED = "mustNotBeRemoved0,mustNotBeRemoved1";

	/**
	 * After the optimiser has run checks that the expected binary operation that should have been updated were updated.
	 */
	public boolean work(final JProgram program, final TreeLogger logger) {
		final boolean changed = super.work(program, logger);

		if( this.pass == 1 ){
			this.checkLocalVariablesWereRemoved();
		}
		this.pass++;
		return changed;
	}
	
	int pass = 0;
	

	/**
	 * Verifies that the BinaryOperations that should have been modified were in
	 * deed updated.
	 */
	protected void checkLocalVariablesWereRemoved() {
		final Set expected = this.getRemovedLocalVariables();
		if (false == expected.isEmpty()) {
			throw new AssertionError("Several local variables should have been removed but werent: " + expected);
		}
	}

	/**
	 * This method guards against attempts to remove variables that shouldnt as well as logging variables that were removed.
	 */
	protected boolean removeLocalVariable(final JMethod method, final JVariable variable, final TreeLogger logger) {
		final String name = variable.getName();

		// double check this variable shouldnt be removed.
		if (this.getUnremovableLocalVariables().contains(name)) {
			throw new AssertionError("An attempt has been made to remove a local variable that should not have been removed, name \""
					+ name + "\".");
		}

		// remove it
		this.getRemovedLocalVariables().remove(name);

		// let the remove happen...
		return super.removeLocalVariable(method, variable, logger);
	}

	/**
	 * This collection contains a list of local variables that must be removed.
	 */
	protected Set removedLocalVariables = asSet(REMOVABLE);

	protected Set getRemovedLocalVariables() {
		return this.removedLocalVariables;
	}

	/**
	 * A list of local variables that should be left alone.
	 */
	protected Set unremovableLocalVariables = asSet(MUST_NOT_BE_REMOVED);

	protected Set getUnremovableLocalVariables() {
		return this.unremovableLocalVariables;
	}

	protected Set asSet(final String commaSeparated) {
		return new TreeSet(Arrays.asList(Utilities.split(commaSeparated, ",", true)));
	}
}
