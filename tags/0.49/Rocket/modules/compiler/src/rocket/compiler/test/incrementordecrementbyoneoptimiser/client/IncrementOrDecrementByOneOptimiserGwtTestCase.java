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
package rocket.compiler.test.incrementordecrementbyoneoptimiser.client;
import com.google.gwt.junit.client.GWTTestCase;

public class IncrementOrDecrementByOneOptimiserGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.incrementordecrementbyoneoptimiser.IncrementOrDecrementByOneOptimiser";
	}
	
	/**
	 * The variable inside this method should not be modified because the 2 and not 1 is being add.
	 */
	public void testPlusTwo(){
		int leaveAlone0 = this.echo( 1 );
		leaveAlone0 += 2;
		
		assertEquals( 1+2, leaveAlone0 );
	}
	
	/**
	 * The variable inside this method should not be modified because the 2 and not 1 is being subtracted.
	 */
	public void testMinusTwo(){
		int leaveAlone1 = this.echo( 1 );
		leaveAlone1 -= 2;
		
		assertEquals( 1-2, leaveAlone1 );
	}
	
	/**
	 * The variable within this method should not be modified because no literal is part of the assignment.
	 */
	public void testPlusNotALiteral(){
		int i = this.echo( 1 );
		int leaveAlone2 = this.echo( 2 );
		
		i += leaveAlone2;
		
		assertEquals( 1+2, i );
	}
	
	/**
	 * The variable within this method should not be modified because no literal is part of the assignment.
	 */
	public void testSubtractNotALiteral(){
		int i = this.echo( 1 );
		int leaveAlone3 = this.echo( 2 );
		
		i += leaveAlone3;
		
		assertEquals( 1+2, i );
	}
	
	
	public void testAssignmentPlusOne(){
		int mustBeOptimised0 = this.echo( 1 );
		mustBeOptimised0 += 1;
		
		assertEquals( 1+1, mustBeOptimised0 );
	}
	
	public void testAssignmentMinusOne(){
		int mustBeOptimised1 = this.echo( 2 );
		mustBeOptimised1 -= 1;
		
		assertEquals( 2-1, mustBeOptimised1 );
	}

	public void testAssignmentPlusMinusOne(){
		int mustBeOptimised2 = this.echo( 2 );
		mustBeOptimised2 += -1;
		
		assertEquals( 2+-1, mustBeOptimised2 );
	}
	
	/**
	 * This method simply returns the given parameter, it is used to prevent
	 * inlining and other optimisations.
	 * 
	 * @param i
	 * @return
	 */
	int echo(final int i) {
		dummy();
		return i;
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
