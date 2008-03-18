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
package rocket.compiler.test.unusedlocalvariableremover.client;

import com.google.gwt.junit.client.GWTTestCase;

public class UnusedLocalVariableRemoverGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		final int testAgainstZero = this.echo( 0 );
		if( testAgainstZero == 0 ){
			echo( 1 );
		}
		
		return "rocket.compiler.test.unusedlocalvariableremover.UnusedLocalVariableRemover";
	}

	/**
	 * The local variable within this method cannot be removed because it is used as a parameter to
	 * another method.
	 */
	public void testLocalVariableWithInitializerAndReference(){
		final int mustNotBeRemoved0 = this.echo( 0 );

		this.echo( mustNotBeRemoved0 );		
	}

	/**
	 * The local variable within this method cannot be removed because it is used as a parameter to
	 * another method.
	 */
	public void testLocalVariableWithReference(){
		int mustNotBeRemoved1; 
		mustNotBeRemoved1 = this.echo( 1 );
		this.echo( mustNotBeRemoved1 );		
	}
	
	/**
	 * The local variable is assigned any value and never referenced so it must be removed.
	 */
	public void testLocalVariableAssigned(){
		int mustBeRemoved0;
	}
	
	/**
	 * The local variable within this method can be removed because it is not referenced and its assignment
	 * has no side effect thus removing the entire statement doesnt hurt anything.
	 */
	public void testLocalVariableWithInitializerAssignedLiteral(){
		final int mustBeRemoved1 = 0;
	}
	
	/**
	 * The local variable is declared and then assigned a literal at a later stage within this method.
	 */
	public void testLocalVariableAssignedLiteral(){
		int mustBeRemoved2;
		
		mustBeRemoved2 = 0;
	}
	
	/**
	 * The local variable with this method is assigned more than once with values that have no side effects. Removing the declaration and assignment expressions is perfectly safe to do.
	 */
	public void testLocalVariableWithMultipleNoSideEffectsAssignments(){
		int mustBeRemoved3;
		
		mustBeRemoved3 = 0;
		mustBeRemoved3 = 1;
	}
	
	/**
	 * Both locals must be eventually removed. The optimiser must removed the second variable because its assignment has no side effect and it is not referenced.
	 * On the second pass the first local must be removed because it no longer has any references.
	 */
	public void testLocalVariableAssignedAnotherLocal(){
		final int mustBeRemoved4 = this.echo( 1 );
		final int mustBeRemoved5 = mustBeRemoved4;
	}
	
	/**
	 * The local variable within this method may be removed but the expression must be kept because it has side effects.
	 */
	public void testLocalVariableShouldBeRemovedButExpressionIsKept(){
		this.setValue(0 );
		final int mustBeRemoved6 = this.setValue( 1 );
		
		assertEquals( "Appears that rhs expression was eliminated for local variable", 1, this.getValue() );
	}
	
	private int value;
	
	int getValue(){
		this.dummy();
		return this.value;
	}
		
	int setValue( final int value ){
		this.dummy();
		this.value = value;
		return value;
	}	
		
	/**
	 * This method simply returns the given parameter, it is used to prevent
	 * inlining and other optimisations.
	 * 
	 * @param i
	 * @return
	 */
	int echo(final int i ) {
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
