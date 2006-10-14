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
package rocket.client.util;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Random;

/**
 * A helper which assists with the management of colours
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ColourHelper extends PrimitiveHelper {

    public static void checkColour(final String name, final int colour) {
        PrimitiveHelper.checkBetween(name, colour, 0, 0xffffff);
    }

    public static int makeLighter(final int rgb) {
        return makeLighter(rgb, (float) Random.nextDouble() * 0.5f);
    }

    public static int makeLighter(final int rgb, final float whiteness) {
        final int red = getRed(rgb);
        final int green = getGreen(rgb);
        final int blue = getBlue(rgb);

        final int lighterRed0 = (int) ((0xff - red) * whiteness);
        final int lighterGreen0 = (int) ((0xff - green) * whiteness);
        final int lighterBlue0 = (int) ((0xff - blue) * whiteness);

        final int lighterRed1 = Math.min(red + lighterRed0, 0xff);
        final int lighterGreen1 = Math.min(green + lighterGreen0, 0xff);
        final int lighterBlue1 = Math.min(blue + lighterBlue0, 0xff);

        return makeColour(lighterRed1, lighterGreen1, lighterBlue1);
    }

    public static int makeDarker(final int rgb) {
        return makeDarker(rgb, (float) Random.nextDouble() * 0.5f);
    }

    public static int makeDarker(final int rgb, final float darkness) {
        final int red = getRed(rgb);
        final int green = getGreen(rgb);
        final int blue = getBlue(rgb);

        final int darkerRed0 = (int) (red * darkness);
        final int darkerGreen0 = (int) (green * darkness);
        final int darkerBlue0 = (int) (blue * darkness);

        final int darkerRed1 = Math.max(red - darkerRed0, 0x0);
        final int darkerGreen1 = Math.max(green - darkerGreen0, 0x0);
        final int darkerBlue1 = Math.max(blue - darkerBlue0, 0x0);

        return makeColour(darkerRed1, darkerGreen1, darkerBlue1);
    }

    public static int getRed(final int rgb) {
        return (rgb >> 16) & 0xff;
    }

    public static int getGreen(final int rgb) {
        return (rgb >> 8) & 0xff;
    }

    public static int getBlue(final int rgb) {
        return (rgb >> 0) & 0xff;
    }

    public static int makeColour(final int red, final int green, final int blue) {
        return (red << 16) | (green << 8) | (blue << 0);
    }

    public static void setBackgroundColour(final Element element, final int colour) {
        ObjectHelper.checkNotNull("parameter:element", element);
        ColourHelper.checkColour("parameter:colour", colour);

        DOM.setStyleAttribute(element, "backgroundColor", toCssColour(colour));
    }

    public static String toCssColour(final int rgb) {
        final String rgbHexString = Integer.toHexString(rgb);
        final String string = '#' + StringHelper.padLeft(rgbHexString, 6, '0');
        return string;
    }

    public static int mix(final int colour, final int otherColour, final float mixRatio) {
        final int red = getRed(colour);
        final int green = getGreen(colour);
        final int blue = getBlue(colour);

        final int otherRed = getRed(otherColour);
        final int otherGreen = getGreen(otherColour);
        final int otherBlue = getBlue(otherColour);

        final float otherRatio = 1.0f - mixRatio;

        final int mixedRed = (int) (red * mixRatio + otherRed * otherRatio);
        final int mixedGreen = (int) (green * mixRatio + otherGreen * otherRatio);
        final int mixedBlue = (int) (blue * mixRatio + otherBlue * otherRatio);

        return makeColour(mixedRed, mixedGreen, mixedBlue);
    }
}
