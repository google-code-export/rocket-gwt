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
package rocket.style.test;

import junit.framework.TestCase;
import rocket.style.client.CssUnit;
import rocket.testing.client.Test;

public class CssUnitTestCase extends TestCase{

	public void testMissingUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1");
		assertSame( CssUnit.NONE, actual );
	}
	
	public void testPixelUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1px");
		assertSame( CssUnit.PX, actual );
	}
	
	public void testInchesUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1in");
		assertSame( CssUnit.IN, actual );
	}
	
	public void testMillimetersUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1mm");
		assertSame( CssUnit.MM, actual );
	}
	
	public void testCentimetersUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1cm");
		assertSame( CssUnit.CM, actual );
	}
	
	public void testPercentageUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1%");
		assertSame( CssUnit.PERCENTAGE, actual );
	}
	
	public void testPointsUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1pt");
		assertSame( CssUnit.PT, actual );
	}
	
	public void testPicasUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1pc");
		assertSame( CssUnit.PC, actual );
	}
	
	public void testEmUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1em");
		assertSame( CssUnit.EM, actual );
	}
	
	public void testExUnit(){
		final CssUnit actual = CssUnit.toCssUnit( "1ex");
		assertSame( CssUnit.EX, actual );
	}
	
	public void testPixelsToPixels() {
		final float input = 1234;
		final float actual = CssUnit.PX.convert(input + "px");
		final float expected = input;
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testInchesToPixels() {
		final float input = 1234;
		final float actual = CssUnit.PX.convert(input + "in" );
		final float expected = Math.round(input / 96f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testPixelsToInches() {
		final float input = 1234;
		final float actual = CssUnit.IN.convert(input + "px");
		final float expected = Math.round(input * 96f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testCentimetersToPixels() {
		final float input = 1234;
		final float actual = CssUnit.PX.convert(input + "cm");
		final float expected = Math.round(input / 96f * 2.54f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testPixelsToCentimeters() {
		final float input = 1234;
		final float actual = CssUnit.CM.convert(input + "px");
		final float expected = Math.round(input * 96f / 2.54f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testMillimetersToPixels() {
		final float input = 1234;
		final float actual = CssUnit.PX.convert(input + "mm");
		final float expected = Math.round(input / 96f * 25.4f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testPixelsToMillimeters() {
		final float input = 1234;
		final float actual = CssUnit.MM.convert(input + "px");
		final float expected = Math.round(input * 96f / 25.4f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testPointsToPixels() {
		final float input = 1234;
		final float actual = CssUnit.PX.convert(input + "pt");
		final float expected = Math.round(input * 96f / 72f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testPixelsToPoints() {
		final float input = 1234;
		final float actual = CssUnit.PT.convert(input + "px");
		final float expected = Math.round(input / 96f * 72f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testPicasToPixels() {
		final float input = 1234;
		final float actual = CssUnit.PX.convert(input + "pc");
		final float expected = Math.round(input * 96f / 72f * 12f);
		Test.assertEquals(expected, actual, 0.75f);
	}

	public void testPixelsToPicas() {
		final float input = 1234;
		final float actual = CssUnit.PC.convert(input + "px");
		final float expected = Math.round(input / 96f * 72f / 12f);
		Test.assertEquals(expected, actual, 0.75f);
	}

}
