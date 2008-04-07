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
package rocket.compiler.test.alternatevaluesreturnedoptimiser.client;

import com.google.gwt.junit.client.GWTTestCase;

public class AlternateValuesReturnedOptimiserGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.alternatevaluesreturnedoptimiser.AlternateValuesReturnedOptimiser";
	}

	/**
	 * Calls a method that has a lone return.
	 */
	public void testCallsMethodWithOnlyReturn(){
		this.methodWithOnlyReturn();
	}
	
	void methodWithOnlyReturn(){
		return;
	}
	
	/**
	 * Calls a method that has a if statement without returns.
	 */
	public void testIfStatementWithoutReturns() {
		final int i = this.ifStatementWithoutReturns(1);
		assertEquals(1, i);
	}

	int ifStatementWithoutReturns(final int i) {
		final boolean shouldNotBeModified0 = this.echo(true);

		if (shouldNotBeModified0) {
			int j = 0;
		}
		return i;
	}

	/**
	 * Calls a method that contains an if containing both a then and else with return statements. It is not possible to convert to
	 * a tenary statement because the then statement contains an intermediate method call.
	 */
	public void testIfStatementWithUnconvertableReturn() {
		final int i = this.ifStatementWithUnconvertableReturn();

		assertEquals(1, i);
	}

	int ifStatementWithUnconvertableReturn() {
		final boolean shouldNotBeModified1 = this.echo(true);
		if (echo(shouldNotBeModified1)) {
			this.dummy();
			return 1;
		} else {
			return 2;
		}
	}
	
	/**
	 * Contains a call to a method with if statements that can be converted to a 
	 */
	public void testIfStatementThatCanBeConverted(){
		final int i = this.ifStatementThatCanBeConverted( 1, 2, true );
		assertEquals( 1, i );
		
		final int j = this.ifStatementThatCanBeConverted( 1, 2, false );
		assertEquals( 2, j );
	}

	int ifStatementThatCanBeConverted( final int trueValue, final int falseValue, final boolean mustChange ){
		if( mustChange ){
			return trueValue;
		} else {
			return falseValue;
		}
	}
	
	/**
	 * This method simply returns the given parameter, it is used to prevent
	 * inlining and other optimisations.
	 * 
	 * @param b
	 * @return
	 */
	boolean echo(final boolean b) {
		dummy();
		return b;
	}

	/**
	 * By calling this method other methods avoid being completely removed by
	 * various optimisers.
	 */
	static void dummy() {
		for (int i = 0; i < 1; i++) {
			new Object(); // stops inlining...
		}
	}
}
