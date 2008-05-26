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
package rocket.compiler.test.alternatevaluesassignmentoptimiser.client;

import com.google.gwt.junit.client.GWTTestCase;

public class AlternateValuesAssignmentOptimiserGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.alternatevaluesassignmentoptimiser.AlternateValuesAssignmentOptimiser";
	}


	/**
	 * The if statement below must not be converted because it is missing an else.
	 */
	public void testIfWithoutElse(){
		final boolean mustNotChange0 = this.echo( true );
		
		int i = 0;
		if( mustNotChange0 ) {
			i = 1;
		} 
		assertEquals( 1, i );
	}
	
	/**
	 * The then statement below cannot be expressed as a tenary statement because it contains an interrupting method call.
	 */
	public void testThenThatCannotBeExpressedAsTenary(){
		final boolean mustNotChange1 = this.echo( true );
		
		int i = 0;
		if( mustNotChange1 ) {
			this.dummy();
			i = 1;
		} else {
			i = 2;
		}
		assertEquals( 1, i );
	}
	
	/**
	 * The if statement within this method should not be converted to a tenary statement because different variables are being updated by the then and else blocks.
	 */
	public void testThenAndElseUpdateDifferentVariables(){
		final boolean mustNotChange2 = this.echo( true );
		
		int i = 0;
		int j = 0;
		if( mustNotChange2 ){
			i = 1;
		} else {
			j = 1;
		}
		
		assertEquals( 1, i );
		assertEquals( 0, j );
	}

	/**
	 * The if statement within the body of this method should be updated.
	 */
	public void testIfThenElseDifferentAssignment(){
		final boolean mustNotChange3 = this.echo(true);
		
		int i = 1;
		if( mustNotChange3 ){
			i += 2;
		} else {
			i -= 3;
		}
		assertEquals( 3, i );
	}

	
	/**
	 * The if statement within the body of this method should be updated.
	 */
	public void testIfThenElseUpdatingSameVariable(){
		final boolean mustChange0 = this.echo(true);
		
		int i = 0;
		if( mustChange0 ){
			i = 1;
		} else {
			i = 2;
		}
		
		assertEquals( 1, i );
	}
	
	/**
	 * The if statement within the body of this method should be updated.
	 */
	public void testBlockessIfThenElseUpdatingSameVariable(){
		final boolean mustChange1 = this.echo(true);
		
		int i = 0;
		if( mustChange1 )
			i = 1;
		else
			i = 2;
		
		assertEquals( 1, i );
	}
	
	/**
	 * The if statement within the body of this method should be updated.
	 */
	public void testIfThenElsePlusAssignment(){
		final boolean mustChange2 = this.echo(true);
		
		int i = 1;
		if( mustChange2 ){
			i += 2;
		} else {
			i += 3;
		}
		assertEquals( 3, i );
	}
		
	/**
	 * This method simply returns the given parameter, it is used to prevent
	 * inlining and other optimisations.
	 * 
	 * @param b
	 * @return
	 */
	boolean echo(final boolean b ) {
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
