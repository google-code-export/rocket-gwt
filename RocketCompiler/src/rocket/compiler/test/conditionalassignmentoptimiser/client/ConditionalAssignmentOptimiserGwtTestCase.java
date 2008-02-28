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
package rocket.compiler.test.conditionalassignmentoptimiser.client;

import com.google.gwt.junit.client.GWTTestCase;

public class ConditionalAssignmentOptimiserGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.conditionalassignmentoptimiser.ConditionalAssignmentOptimiser";
	}

	/**
	 * The if statement within this method shoulnt be modified because its got an else.
	 */
	public void testBlocklessIfThenElse(){	
		final boolean mustNotChange0 = this.echo( true );
		
		int value = 1;
		if( mustNotChange0 ){
			value = 2;
		} else {
			value = 3;
		}
		
		assertEquals( 2, value );
	}

	/**
	 * The if statement within this method shoulnt be modified because the body of the if is a not an assignment
	 */
	public void testThenWithInterruptingStatement(){
		final boolean mustNotChange1 = this.echo( true );
		
		int value = 1;
		if( mustNotChange1 ){
			dummy();
			value = 2;			
		} else {
			value = 3;
		}
		
		assertEquals( 2, value );
	}

	/**
	 * The if statement within this method shoulnt be modified because the body of the if is a not an assignment
	 */
	public void testElseWithInterruptingStatement(){
		final boolean mustNotChange2 = this.echo( true );
		
		int value = 1;
		if( mustNotChange2 ){
			value = 2;			
		} else {
			dummy();
			value = 3;
		}
		
		assertEquals( 2, value );
	}
	
	/**
	 * The if statement within this method should be changed because its got an assignment.
	 */
	public void testThenWithAssignment(){
		final boolean mustChange0 = this.echo( true );
		
		int value = 1;
		if( mustChange0 ){
			value = 2;			
		} 
		
		assertEquals( 2, value );
	}
	
	/**
	 * The if statement without a block within this method should be changed because its got an assignment.
	 */
	public void testBlocklessThenWithAssignment(){
		final boolean mustChange1 = this.echo( true );
	
		int value = 1;
		if( mustChange1 )
			value = 2;			
		
		assertEquals( 2, value );
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
