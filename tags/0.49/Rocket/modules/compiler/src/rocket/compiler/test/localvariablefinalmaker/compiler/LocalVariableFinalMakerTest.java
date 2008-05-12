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
package rocket.compiler.test.localvariablefinalmaker.compiler;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import rocket.compiler.Compiler;
import rocket.compiler.LocalVariableFinalMaker;
import rocket.util.client.Utilities;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JLocalDeclarationStatement;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JVariable;

/**
 * This class decorates the original {@link LocalVariableFinalMaker} adding some
 * tests to check that the appropriate variable references were made final and others that shouldnt were left alone.
 * 
 * @author Miroslav Pokorny
 */
public class LocalVariableFinalMakerTest extends LocalVariableFinalMaker {

	final static String MODIFIED = "shouldBeMadeFinal0,shouldBeMadeFinal1,shouldBeMadeFinal2";
	final static String UNMODIFIED = "shouldBeLeftAlone0,shouldBeLeftAlone1,shouldBeLeftAlone2,shouldBeLeftAlone3";

	/**
	 * After the optimiser has run checks that the methods that should have been
	 * updated were updated.
	 */
	public boolean work(final JProgram program, final TreeLogger logger) {
		final boolean changed = super.work(program, logger);
		this.checkExpectedLocalVariablesWereMadeFinal();
		return changed;
	}

	/**
	 * Verifies that local variables that should have been modified were indeed updated.
	 */
	protected void checkExpectedLocalVariablesWereMadeFinal() {
		final Set expected = this.getModifiedLocalVariables();
		if (false == expected.isEmpty()) {
			throw new AssertionError(
					"Several local variables that should have been made final but werent, localVariables: "	+ expected);
		}
	}
	
	/**
	 * Double checks that the given variable isnt on the watch list of variables that shouldnt be modified.
	 */
	protected void makeLocalVariableFinal( final JLocalDeclarationStatement declaration, final JExpression value,  final JMethod method, final TreeLogger logger ){
		final JVariable variable = declaration.getLocalRef().getTarget();
		final String name = variable.getName();
		
		if( this.getUnmodifiedLocalVariables().contains( name )){
			throw new AssertionError( "The local variable \"" + name + "\" should not have been modified, variable: " + Compiler.getSource( variable ));
		}
		
		final boolean expected = this.getModifiedLocalVariables().remove( name );
		if( expected ){
			logger.log( TreeLogger.DEBUG, "Expected variable \"" + name + "\" was made final.", null );
		}
		
		super.makeLocalVariableFinal(declaration, value, method, logger);
	}

	/**
	 * This collections contains all that local variables that should have been updated. As the visitor locates and updates local variables
	 * the set is reduced. By the time the optimiser completes all the elements of the set should have been removed.
	 */
	protected Set modifiedLocalVariables = asSet(MODIFIED);

	protected Set getModifiedLocalVariables() {
		return this.modifiedLocalVariables;
	}

	/**
	 * A list specific local variables that should not be modified.
	 */
	protected Set unmodifiedLocalVariables = asSet(UNMODIFIED);

	protected Set getUnmodifiedLocalVariables() {
		return this.unmodifiedLocalVariables;
	}

	protected Set asSet(final String commaSeparated) {
		return new TreeSet(Arrays.asList(Utilities.split(commaSeparated, ",", true)));
	}

}
