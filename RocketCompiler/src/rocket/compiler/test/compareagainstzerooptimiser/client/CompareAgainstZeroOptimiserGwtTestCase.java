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
package rocket.compiler.test.compareagainstzerooptimiser.client;

import com.google.gwt.junit.client.GWTTestCase;

public class CompareAgainstZeroOptimiserGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.compareagainstzerooptimiser.CompareAgainstZeroOptimiser";
	}

	/**
	 * The if statement within this method must be changed to the optimal javascript form.
	 */
	public void testIfVariableEqualsZero(){
		int i = 1;
		
		final int equalToZero = this.echo( 0 );
		if( equalToZero == 0 ){
			i = 2;
		}
		
		assertEquals( 2, i );
	}

	/**
	 * The if statement within this method must be changed to the optimal javascript form.
	 */
	public void testIfVariableNotEqualsZero(){
		int i = 1;
		
		final int notEqualToZero = this.echo( 0 );
		if( notEqualToZero != 0 ){
			i = 2;
		}
		
		assertEquals( 1, i );
	}
	
	/**
	 * The if statement within this method must be left alone
	 */
	public void testIfVariableGreaterThanZero(){
		int i = 1;
		
		final int greaterThanZero = this.echo( 0 );
		if( greaterThanZero > 0 ){
			i = 2;
		}
		
		assertEquals( 1, i );		
	}
	
	/**
	 * This method simply returns the given parameter, it is used to prevent inlining and other optimisations.
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
