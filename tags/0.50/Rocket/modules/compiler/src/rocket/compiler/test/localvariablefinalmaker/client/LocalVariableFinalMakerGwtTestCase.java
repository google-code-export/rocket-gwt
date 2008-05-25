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
package rocket.compiler.test.localvariablefinalmaker.client;

import com.google.gwt.junit.client.GWTTestCase;

public class LocalVariableFinalMakerGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.localvariablefinalmaker.LocalVariableFinalMaker";
	}
	

	/**
	 * The local variable within this method is set more than once and should be left alone and not made final.
	 */
	public void testSetMoreThanOnce(){
		int shouldBeLeftAlone0;
		
		shouldBeLeftAlone0 = 1;
		shouldBeLeftAlone0 = 2;
		assertEquals( 2, shouldBeLeftAlone0 );
	}
	
	/**
	 * The local variable within this method may be set more than once, ie its value is set in a sub block of the variable declaration.
	 */
	public void testPotentiallySetMoreThanOnce0(){
		int shouldBeLeftAlone1;
		
		for( int i = 0; i < 1; i++ ){
			shouldBeLeftAlone1 = 1;
		}
		
		//assertEquals( 1, shouldBeLeftAlone1 );
	}
	
	/**
	 * The local variable within this method may be set more than once, ie its value is set in a sub block of the variable declaration.
	 */
	public void testPotentiallySetMoreThanOnce1(){
		int shouldBeLeftAlone2 = 1;
		
		final int i = this.echo( 1 );
		if( i == 1 ){
			shouldBeLeftAlone2 = 2;
		}
	
		assertEquals( 2, shouldBeLeftAlone2 );
	}
	
	/**
	 * The only local variable within this test method should be touched as its already final.
	 */
	public void testIsAlreadyFinal(){
		final int shouldBeLeftAlone3 = 1;
		assertEquals( 1, shouldBeLeftAlone3 );
	}
	
	/**
	 * The local variable within this method should be made final as it is only ever assigned a single value.
	 */
	public void testShouldBeMadeFinal(){
		int shouldBeMadeFinal0;
		
		shouldBeMadeFinal0 = 1;
		assertEquals( 1, shouldBeMadeFinal0 );
	}
	
	
	/**
	 * The local variable within this method should be made final as it is only ever assigned a single value (its initializer).
	 */
	public void testShouldBeMadeFinal1(){
		int shouldBeMadeFinal1 = 1;
		assertEquals( 1, shouldBeMadeFinal1 );
	}
	
	
	/**
	 * The shouldBeMadeFinal2 local variable should be made final followed by its value being inlined by the other local.
	 * If the UnusedLocalVariableRemover were running it should have removed the first local variable.
	 * 
	 * In order to confirm that the shouldBeMadeFinal2 has been made final, inlined and removed scan the produced javascript
	 * for this method and confirm the local variable is gone.
	 */
	public void testShouldBeMadeFinalRemovedAndValueInlined(){
		int shouldBeMadeFinal2 = 1;
		
		int i = shouldBeMadeFinal2 + 2;
		assertEquals( 3, i );		
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
