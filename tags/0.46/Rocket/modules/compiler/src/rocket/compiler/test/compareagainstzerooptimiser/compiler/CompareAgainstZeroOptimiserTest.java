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
package rocket.compiler.test.compareagainstzerooptimiser.compiler;

import rocket.compiler.CompareAgainstZeroOptimiser;
import rocket.compiler.Compiler;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.js.ast.JsBinaryOperation;
import com.google.gwt.dev.js.ast.JsContext;
import com.google.gwt.dev.js.ast.JsExpression;
import com.google.gwt.dev.js.ast.JsProgram;

/**
 * This test class should only be run within a CompareAgainstZeroOptimiserGwtTestCase test.
 * 
 * Because it relies on variable names the test should be executed with style=DETAILED 
 * @author Miroslav Pokorny
 */
public class CompareAgainstZeroOptimiserTest extends CompareAgainstZeroOptimiser {

	public boolean work(final JsProgram jsprogram, final TreeLogger logger) {
		final boolean changed = super.work(jsprogram, logger);

		if (!changed) {
			throw new AssertionError("At least 2 tests against 0 should have been modified.");
		}

		if (false == this.equalToZero) {
			throw new AssertionError("The \"if( equalToZero == 0 ){\" should have been optimised to \"if( equalToZero )\".");
		}
		if (false == this.notEqualToZero) {
			throw new AssertionError("The \"if( notEqualToZero == 0 ){\" should have been optimised to \"if( notEqualToZero )\".");
		}
		return changed;
	}

	protected void replaceEqualsZeroTest(final JsBinaryOperation binaryOperation, final JsContext context,	final JsExpression expression, final TreeLogger logger) {
		final String source = Compiler.getSource(expression);
		if (source.contains("notEqualToZero") || source.contains("greaterThanZero")) {
			throw new AssertionError("This binary operation \"" + source + "\" should not have been modified.");
		}

		if (source.contains("equalToZero")) {
			this.equalToZero = true;
		}
		
		super.replaceEqualsZeroTest(binaryOperation, context, expression, logger);
	}

	protected void replaceNotEqualsZeroTest(final JsBinaryOperation binaryOperation, final JsContext context, final JsExpression expression, final TreeLogger logger) {
		final String source = Compiler.getSource(expression);
		if (source.contains("equals") || source.contains("greaterThanZero")) {
			throw new AssertionError("This binary operation \"" + source + "\" should not have been modified.");
		}

		if (source.contains("notEqualToZero")) {
			this.notEqualToZero = true;
		}
		
		super.replaceNotEqualsZeroTest(binaryOperation, context, expression, logger);
	}

	boolean notEqualToZero = false;
	boolean equalToZero = false;
}
