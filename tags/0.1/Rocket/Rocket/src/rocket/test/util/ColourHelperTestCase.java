/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.test.util;

import junit.framework.TestCase;
import rocket.client.util.ColourHelper;

/**
 * @author Miroslav Pokorny (mP)
 */
public class ColourHelperTestCase extends TestCase {

    public void testMakeLighter0() {
        final int in = 0xffffff;
        final float whiteness = 0;
        final int out = ColourHelper.makeLighter(in, whiteness);
        final int expected = in;

        assertEquals("makeLighter out: 0x" + Integer.toHexString(out) + ", expected: 0x"
                + Integer.toHexString(expected), expected, out);
    }

    public void testMakeLighter1() {
        final int in = 0x334455;
        final float whiteness = 0;
        final int out = ColourHelper.makeLighter(in, whiteness);
        final int expected = in;

        assertEquals("makeLighter out: 0x" + Integer.toHexString(out) + ", expected: 0x"
                + Integer.toHexString(expected), expected, out);
    }

    public void testMakeLighter2() {
        final int in = 0x000000;
        final float whiteness = 0.5f;
        final int out = ColourHelper.makeLighter(in, whiteness);
        final int expected = 0x7f7f7f;

        assertEquals("makeLighter out: 0x" + Integer.toHexString(out) + ", expected: 0x"
                + Integer.toHexString(expected), expected, out);
    }

    public void testMakeLighter3() {
        final int in = 0x345678;
        final float whiteness = 0.1f;
        final int out = ColourHelper.makeLighter(in, whiteness);
        final int expected = 0x486685;

        assertEquals("makeLighter out: 0x" + Integer.toHexString(out) + ", expected: 0x"
                + Integer.toHexString(expected), expected, out);
    }

    public void testMakeLighter4() {
        final int in = 0x880000;
        final float whiteness = 0.3333333f;
        final int out = ColourHelper.makeLighter(in, whiteness);
        final int expected = 0xaf5454;

        assertEquals("makeLighter out: 0x" + Integer.toHexString(out) + ", expected: 0x"
                + Integer.toHexString(expected), expected, out);
    }

    public void testMakeLighter5() {
        final int in = 0x123456;
        final float whiteness = 1.0f;
        final int out = ColourHelper.makeLighter(in, whiteness);
        final int expected = 0xffffff;

        assertEquals("makeLighter out: 0x" + Integer.toHexString(out) + ", expected: 0x"
                + Integer.toHexString(expected), expected, out);
    }

    public void testMakeDarker0() {
        final int in = 0xffffff;
        final float blackness = 0;
        final int out = ColourHelper.makeDarker(in, blackness);
        final int expected = in;

        assertEquals(
                "makeDarker out: 0x" + Integer.toHexString(out) + ", expected: 0x" + Integer.toHexString(expected),
                expected, out);
    }

    public void testMakeDarker1() {
        final int in = 0x456789;
        final float blackness = 0;
        final int out = ColourHelper.makeDarker(in, blackness);
        final int expected = in;

        assertEquals(
                "makeDarker out: 0x" + Integer.toHexString(out) + ", expected: 0x" + Integer.toHexString(expected),
                expected, out);
    }

    public void testMakeDarker2() {
        final int in = 0xffffff;
        final float blackness = 0.5f;
        final int out = ColourHelper.makeDarker(in, blackness);
        final int expected = 0x808080;

        assertEquals(
                "makeDarker out: 0x" + Integer.toHexString(out) + ", expected: 0x" + Integer.toHexString(expected),
                expected, out);
    }

    public void testMakeDarker3() {
        final int in = 0x345678;
        final float blackness = 0.1f;
        final int out = ColourHelper.makeDarker(in, blackness);
        final int expected = 0x2f4e6c;

        assertEquals(
                "makeDarker out: 0x" + Integer.toHexString(out) + ", expected: 0x" + Integer.toHexString(expected),
                expected, out);
    }

    public void testMakeDarker4() {
        final int in = 0x880000;
        final float blackness = 1.0f;
        final int out = ColourHelper.makeDarker(in, blackness);
        final int expected = 0x000000;

        assertEquals(
                "makeDarker out: 0x" + Integer.toHexString(out) + ", expected: 0x" + Integer.toHexString(expected),
                expected, out);
    }

    public void testMix0(){
        final int colour = 0x12345;
        final int otherColour =  0x67890a;
        final float mixRatio = 1.0f;

        final int mixedColour = ColourHelper.mix( colour, otherColour, mixRatio );
        final int expectedColour = colour;
        assertEquals( expectedColour, mixedColour );
    }
    public void testMix1(){
        final int colour = 0x12345;
        final int otherColour =  0x67890a;
        final float mixRatio = 0.0f;

        final int mixedColour = ColourHelper.mix( colour, otherColour, mixRatio );
        final int expectedColour = otherColour;
        assertEquals( "colour: 0x" + Integer.toHexString(colour )+ ", otherColour: 0x" + Integer.toHexString(otherColour) + ", mixRatio: " + mixRatio +
                ", mixedColour: 0x" + Integer.toHexString( mixedColour ) + ", expectedColour: 0x" + Integer.toHexString( expectedColour ), expectedColour, mixedColour );
    }
    public void testMix2(){
        final int colour = 0x222222;
        final int otherColour =  0x444444;
        final float mixRatio = 0.5f;

        final int mixedColour = ColourHelper.mix( colour, otherColour, mixRatio );
        final int expectedColour = 0x333333;
        assertEquals( "colour: 0x" + Integer.toHexString(colour )+ ", otherColour: 0x" + Integer.toHexString(otherColour) + ", mixRatio: " + mixRatio +
                ", mixedColour: 0x" + Integer.toHexString( mixedColour ) + ", expectedColour: 0x" + Integer.toHexString( expectedColour ), expectedColour, mixedColour );
    }

    public void testMix3(){
        final int colour = 0x001234;
        final int otherColour =  0xff1234;
        final float mixRatio = 0.75f;

        final int mixedColour = ColourHelper.mix( colour, otherColour, mixRatio );
        final int expectedColour = 0x3f1234;
        assertEquals( "colour: 0x" + Integer.toHexString(colour )+ ", otherColour: 0x" + Integer.toHexString(otherColour) + ", mixRatio: " + mixRatio +
                ", mixedColour: 0x" + Integer.toHexString( mixedColour ) + ", expectedColour: 0x" + Integer.toHexString( expectedColour ), expectedColour, mixedColour );
    }
}
