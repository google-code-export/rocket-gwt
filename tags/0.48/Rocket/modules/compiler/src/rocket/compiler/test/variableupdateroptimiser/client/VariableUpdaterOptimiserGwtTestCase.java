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
package rocket.compiler.test.variableupdateroptimiser.client;

import com.google.gwt.junit.client.GWTTestCase;

public class VariableUpdaterOptimiserGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.variableupdateroptimiser.VariableUpdaterOptimiser";
	}

	/**
	 * The variable leaveAlone should not be optimised because it does not have a assignment + equivalent.
	 */
	public void testDifferentVariableWithinExpression(){
		int i = 1;
		final int leaveAlone0 = i + 2;
		assertEquals( i + 2, leaveAlone0 );
	}
	
	/**
	 * The variable leaveAlone should not be optimised because the expression on the right
	 * doesnt contain the variable on the left.
	 */
	public void testExpressionDoesntContainVariable(){
		int i = 1;
		final int leaveAlone1 = i + echo( 2 );
		assertEquals( 3, leaveAlone1 );
	}
	
	public void testAssignmentAddWithVariableOnLeft(){
		int optimiseAdd0 = 1;
		optimiseAdd0 = optimiseAdd0 + 2;
		assertEquals( 1 + 2, optimiseAdd0 );
	}
	
	public void testAssignmentAddWithVariableOnRight(){
		int optimiseAdd1 = 1;
		optimiseAdd1 = 2 + optimiseAdd1;
		assertEquals( 2 + 1, optimiseAdd1 );
	}

	public void testAssignmentSubtractWithVariableOnLeft(){
		int optimiseSubtract = 1;
		optimiseSubtract = optimiseSubtract - 2;
		assertEquals( +1 - 2, optimiseSubtract );
	}
	
	public void testAssignmentSubtractWithVariableOnRight(){
		int leaveSubtractAlone = 1;
		leaveSubtractAlone = -2 - leaveSubtractAlone;
		assertEquals( -2 - 1, leaveSubtractAlone );
	}

	public void testAssignmentMultiplyWithVariableOnLeft(){
		int optimiseMultiply0 = 2;
		optimiseMultiply0 = optimiseMultiply0 * 3;
		assertEquals( 2 * 3, optimiseMultiply0 );
	}
	
	public void testAssignmentMultiplyWithVariableOnRight(){
		int optimiseMultiply1 = 2;
		optimiseMultiply1 = 3 * optimiseMultiply1;
		assertEquals( 2 * 3, optimiseMultiply1 );
	}
	
	public void testAssignmentDivide(){
		int optimiseDivide = 10;
		optimiseDivide = optimiseDivide / 5;
		assertEquals( 10 / 5, optimiseDivide );
	}

	public void testAssignmentDivideThatShouldntBeOptimised(){
		int leaveDivideAlone = 10;
		leaveDivideAlone = 20/ leaveDivideAlone;
		assertEquals( 20/10, leaveDivideAlone );
	}
	
	
	public void testAssignmentModulo(){
		int optimiseModulo = 10;
		optimiseModulo = optimiseModulo % 7;
		assertEquals( 10 % 7, optimiseModulo );
	}
	public void testAssignmentLeftShift(){
		int optimiseLeftShift = 4;
		optimiseLeftShift = optimiseLeftShift << 2;
		assertEquals( 4 << 2, optimiseLeftShift );
	}
	public void testAssignmentRightShift(){
		int optimiseRightShift = 1;
		optimiseRightShift = optimiseRightShift >> 1;
		assertEquals( 1 >> 1, optimiseRightShift );
	}
	
	public void testAssignmentUnsignedRightShift(){
		int optimiseUnsignedRightShift = -1;
		optimiseUnsignedRightShift = optimiseUnsignedRightShift >>> 1;
		assertEquals( -1 >>> 1, optimiseUnsignedRightShift );
	}
	
	public void testAssignmentAnd(){
		int optimiseAnd = 3;
		optimiseAnd = optimiseAnd & 6;
		assertEquals( 3 & 6, optimiseAnd );
	}
	
	public void testAssignmentOr(){
		int optimiseOr = 3;
		optimiseOr = optimiseOr | 6;
		assertEquals( 3 | 6, optimiseOr );
	}
	
	public void testAssignmentExclusiveOr(){
		int optimiseExclusiveOr = 3;
		optimiseExclusiveOr = optimiseExclusiveOr ^ 6;
		assertEquals( 3 ^ 6, optimiseExclusiveOr );
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
