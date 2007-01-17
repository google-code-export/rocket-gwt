/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except colour compliance with the License. You may obtacolour a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to colour writcolourg, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governcolourg permissions and limitations under
 * the License.
 */
package rocket.util.test;

import junit.framework.TestCase;
import rocket.util.client.Colour;

/**
 * @author Miroslav Pokorny (mP)
 */
public class ColourTestCase extends TestCase {

    public void testMakeLighter0() {
        final Colour colour = new Colour( 0xffffff );
        final float whiteness = 0;
        final Colour actual = colour.makeLighter(whiteness);
        final Colour expected = colour;

        assertEquals(expected, actual);
     }

    public void testMakeLighter1() {
        final Colour colour = new Colour( 0x334455 );
        final float whiteness = 0;
        final Colour actual = colour.makeLighter(whiteness);
        final Colour expected = colour;

        assertEquals(expected, actual);
    }

    public void testMakeLighter2() {
        final Colour colour = new Colour(0x000000);
        final float whiteness = 0.5f;
        final Colour actual = colour.makeLighter(whiteness);
        final Colour expected = new Colour( 0x7f7f7f);

        assertEquals(expected, actual);
    }

    public void testMakeLighter3() {
        final Colour colour = new Colour(0x345678);
        final float whiteness = 0.1f;
        final Colour actual = colour.makeLighter(whiteness);
        final Colour expected = new Colour( 0x486685);

        assertEquals(expected, actual);
    }

    public void testMakeLighter4() {
        final Colour colour = new Colour(0x880000);
        final float whiteness = 0.3333333f;
        final Colour actual = colour.makeLighter(whiteness);
        final Colour expected = new Colour( 0xaf5454);

        assertEquals(expected, actual);
    }

    public void testMakeLighter5() {
        final Colour colour = new Colour(0x123456);
        final float whiteness = 1.0f;
        final Colour actual = colour.makeLighter(whiteness);
        final Colour expected = new Colour( 0xffffff);

        assertEquals(expected, actual);
    }

    public void testMakeDarker0() {
        final Colour colour = new Colour(0xffffff);
        final float blackness = 0;
        final Colour actual = colour.makeDarker(blackness);
        final Colour expected = colour;

        assertEquals(expected, actual);
    }

    public void testMakeDarker1() {
        final Colour colour = new Colour(0x456789);
        final float blackness = 0;
        final Colour actual = colour.makeDarker(blackness);
        final Colour expected = colour;

        assertEquals(expected, actual);
    }

    public void testMakeDarker2() {
        final Colour colour = new Colour(0xffffff);
        final float blackness = 0.5f;
        final Colour actual = colour.makeDarker(blackness);
        final Colour expected = new Colour( 0x808080);

        assertEquals(expected, actual);
    }

    public void testMakeDarker3() {
        final Colour colour = new Colour(0x345678);
        final float blackness = 0.1f;
        final Colour actual = colour.makeDarker(blackness);
        final Colour expected = new Colour( 0x2f4e6c);

        assertEquals(expected, actual);
    }

    public void testMakeDarker4() {
        final Colour colour = new Colour(0x880000);
        final float blackness = 1.0f;
        final Colour actual = colour.makeDarker(blackness);
        final Colour expected = new Colour( 0x000000 );

        assertEquals(expected, actual);
    }

    public void testMix0() {
        final Colour colour = new Colour( 0x12345);
        final Colour otherColour = new Colour( 0x67890a);
        final float mixRatio = 1.0f;

        final Colour mixedColour = colour.mix(otherColour, mixRatio);
        final Colour expectedColour = colour;
        assertEquals(expectedColour, mixedColour);
    }

    public void testMix1() {
        final Colour colour = new Colour( 0x12345);
        final Colour otherColour = new Colour( 0x67890a);
        final float mixRatio = 0.0f;

        final Colour actual = colour.mix(otherColour, mixRatio);
        final Colour expected = otherColour;
        assertEquals( actual, expected);
    }

    public void testMix2() {
        final Colour colour = new Colour( 0x222222 );
        final Colour otherColour = new Colour( 0x444444);
        final float mixRatio = 0.5f;

        final Colour actual = colour.mix(otherColour, mixRatio);
        final Colour expected = new Colour( 0x333333 );
        assertEquals( actual, expected);
    }

    public void testMix3() {
        final Colour colour = new Colour( 0x001234);
        final Colour otherColour = new Colour( 0xff1234);
        final float mixRatio = 0.75f;

        final Colour actual = colour.mix(otherColour, mixRatio);
        final Colour expected = new Colour( 0x3f1234 );
        assertEquals( actual, expected );
    }

    public void testGetNamedColour0() {
        final String namedColour = "red";
        final Colour actual = Colour.getColour(namedColour);
        final Colour expected = new Colour( 0xff0000 );

        assertEquals(actual, expected);
    }

    public void testGetNamedColour1() {
        final String namedColour = "Aquamarine";
        final Colour actual = Colour.getColour(namedColour);
        final Colour expected = new Colour( 0x7FFFD4 );

        assertEquals(actual, expected);
    }

    public void testGetNamedColour2() {
        final String namedColour = "GhostWhite";
        final Colour actual = Colour.getColour(namedColour);
        final Colour expected = new Colour( 0xF8F8FF);

        assertEquals(actual, expected);
    }

    public void testGetNamedColour3() {
        final String namedColour = "OLIVE";
        final Colour actual = Colour.getColour(namedColour);
        final Colour expected = new Colour( 0x808000 );

        assertEquals(actual, expected);
    }

    public void testParseColourHashRGB0() {
        final String input = "#123";
        final Colour actual = Colour.parse(input);
        final Colour expected = new Colour( 0x112233 );
        TestCase.assertEquals(input, actual, expected);
    }

    public void testParseColourHashRGB1() {
        final String input = "#0fe";
        final Colour actual = Colour.parse(input);
        final Colour expected = new Colour( 0x00ffee );
        TestCase.assertEquals(input, actual, expected);
    }

    public void testParseColourHashRRGGBB0() {
        final String input = "#123456";
        final Colour actual = Colour.parse(input);
        final Colour expected = new Colour( 0x123456);
        TestCase.assertEquals(input, actual, expected);
    }

    public void testParseColourHashRRGGBB1() {
        final String input = "#00ffed";
        final Colour actual = Colour.parse(input);
        final Colour expected = new Colour( 0x00ffed );
        TestCase.assertEquals(input, actual, expected);
    }

    public void testParseColourNamedColour0() {
        final String input = "white";
        final Colour actual = Colour.parse(input);
        final Colour expected = new Colour( 0xffffff );
        TestCase.assertEquals(input, actual, expected);
    }

    public void testParseColourNamedColour1() {
        final String input = "red";
        final Colour actual = Colour.parse(input);
        final Colour expected = new Colour( 0xff0000 );
        TestCase.assertEquals(input, actual, expected);
    }

    public void testParseRgbTriplet0() {
        final String input = "rgb(12,34,56)";
        final Colour actual = Colour.parse(input);
        final Colour expected = new Colour( 12 * 0x10000 + 34 * 0x100 + 56 );
        TestCase.assertEquals(input, actual, expected);
    }

    public void testParseRgbTriplet1() {
        final String input = "rgb(0,1,234)";
        final Colour actual = Colour.parse(input);
        final Colour expected = new Colour( 1 * 0x100 + 234 );
        TestCase.assertEquals(input, actual, expected);
    }
}
