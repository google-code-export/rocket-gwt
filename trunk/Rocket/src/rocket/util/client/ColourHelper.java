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
package rocket.util.client;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * THis map is used to lookup rgb values for a colour using its name. THe key is the lowercased form of the colour name.
     */
    private static Map namedColours;

    static {
        final Map named = new HashMap();

        named.put("aliceblue", new Integer(0xf0f8ff));
        named.put("antiquewhite", new Integer(0xfaebd7));
        named.put("aqua", new Integer(0x00ffff));
        named.put("aquamarine", new Integer(0x7fffd4));
        named.put("azure", new Integer(0xf0ffff));
        named.put("beige", new Integer(0xf5f5dc));
        named.put("bisque", new Integer(0xffe4c4));
        named.put("black", new Integer(0x000000));
        named.put("blanchedalmond", new Integer(0xffebcd));
        named.put("blue", new Integer(0x0000ff));
        named.put("blueviolet", new Integer(0x8a2be2));
        named.put("brown", new Integer(0xa52a2a));
        named.put("burlywood", new Integer(0xdeb887));
        named.put("cadetblue", new Integer(0x5f9ea0));
        named.put("chartreuse", new Integer(0x7fff00));
        named.put("chocolate", new Integer(0xd2691e));
        named.put("coral", new Integer(0xff7f50));
        named.put("cornflowerblue", new Integer(0x6495ed));
        named.put("cornsilk", new Integer(0xfff8dc));
        named.put("crimson", new Integer(0xdc143c));
        named.put("cyan", new Integer(0x00ffff));
        named.put("darkblue", new Integer(0x00008b));
        named.put("darkcyan", new Integer(0x008b8b));
        named.put("darkgoldenrod", new Integer(0xb8860b));
        named.put("darkgray", new Integer(0xa9a9a9));
        named.put("darkgreen", new Integer(0x006400));
        named.put("darkkhaki", new Integer(0xbdb76b));
        named.put("darkmagenta", new Integer(0x8b008b));
        named.put("darkolivegreen", new Integer(0x556b2f));
        named.put("darkorange", new Integer(0xff8c00));
        named.put("darkorchid", new Integer(0x9932cc));
        named.put("darkred", new Integer(0x8b0000));
        named.put("darksalmon", new Integer(0xe9967a));
        named.put("darkseagreen", new Integer(0x8fbc8f));
        named.put("darkslateblue", new Integer(0x483d8b));
        named.put("darkslategray", new Integer(0x2f4f4f));
        named.put("darkturquoise", new Integer(0x00ced1));
        named.put("darkviolet", new Integer(0x9400d3));
        named.put("deeppink", new Integer(0xff1493));
        named.put("deepskyblue", new Integer(0x00bfff));
        named.put("dimgray", new Integer(0x696969));
        named.put("dodgerblue", new Integer(0x1e90ff));
        named.put("feldspar", new Integer(0xd19275));
        named.put("firebrick", new Integer(0xb22222));
        named.put("floralwhite", new Integer(0xfffaf0));
        named.put("forestgreen", new Integer(0x228b22));
        named.put("fuchsia", new Integer(0xff00ff));
        named.put("gainsboro", new Integer(0xdcdcdc));
        named.put("ghostwhite", new Integer(0xf8f8ff));
        named.put("gold", new Integer(0xffd700));
        named.put("goldenrod", new Integer(0xdaa520));
        named.put("gray", new Integer(0x808080));
        named.put("green", new Integer(0x008000));
        named.put("greenyellow", new Integer(0xadff2f));
        named.put("honeydew", new Integer(0xf0fff0));
        named.put("hotpink", new Integer(0xff69b4));
        named.put("indianred ", new Integer(0xcd5c5c));
        named.put("indigo ", new Integer(0x4b0082));
        named.put("ivory", new Integer(0xfffff0));
        named.put("khaki", new Integer(0xf0e68c));
        named.put("lavender", new Integer(0xe6e6fa));
        named.put("lavenderblush", new Integer(0xfff0f5));
        named.put("lawngreen", new Integer(0x7cfc00));
        named.put("lemonchiffon", new Integer(0xfffacd));
        named.put("lightblue", new Integer(0xadd8e6));
        named.put("lightcoral", new Integer(0xf08080));
        named.put("lightcyan", new Integer(0xe0ffff));
        named.put("lightgoldenrodyellow", new Integer(0xfafad2));
        named.put("lightgrey", new Integer(0xd3d3d3));
        named.put("lightgreen", new Integer(0x90ee90));
        named.put("lightpink", new Integer(0xffb6c1));
        named.put("lightsalmon", new Integer(0xffa07a));
        named.put("lightseagreen", new Integer(0x20b2aa));
        named.put("lightskyblue", new Integer(0x87cefa));
        named.put("lightslateblue", new Integer(0x8470ff));
        named.put("lightslategray", new Integer(0x778899));
        named.put("lightsteelblue", new Integer(0xb0c4de));
        named.put("lightyellow", new Integer(0xffffe0));
        named.put("lime", new Integer(0x00ff00));
        named.put("limegreen", new Integer(0x32cd32));
        named.put("linen", new Integer(0xfaf0e6));
        named.put("magenta", new Integer(0xff00ff));
        named.put("maroon", new Integer(0x800000));
        named.put("mediumaquamarine", new Integer(0x66cdaa));
        named.put("mediumblue", new Integer(0x0000cd));
        named.put("mediumorchid", new Integer(0xba55d3));
        named.put("mediumpurple", new Integer(0x9370d8));
        named.put("mediumseagreen", new Integer(0x3cb371));
        named.put("mediumslateblue", new Integer(0x7b68ee));
        named.put("mediumspringgreen", new Integer(0x00fa9a));
        named.put("mediumturquoise", new Integer(0x48d1cc));
        named.put("mediumvioletred", new Integer(0xc71585));
        named.put("midnightblue", new Integer(0x191970));
        named.put("mintcream", new Integer(0xf5fffa));
        named.put("mistyrose", new Integer(0xffe4e1));
        named.put("moccasin", new Integer(0xffe4b5));
        named.put("navajowhite", new Integer(0xffdead));
        named.put("navy", new Integer(0x000080));
        named.put("oldlace", new Integer(0xfdf5e6));
        named.put("olive", new Integer(0x808000));
        named.put("olivedrab", new Integer(0x6b8e23));
        named.put("orange", new Integer(0xffa500));
        named.put("orangered", new Integer(0xff4500));
        named.put("orchid", new Integer(0xda70d6));
        named.put("palegoldenrod", new Integer(0xeee8aa));
        named.put("palegreen", new Integer(0x98fb98));
        named.put("paleturquoise", new Integer(0xafeeee));
        named.put("palevioletred", new Integer(0xd87093));
        named.put("papayawhip", new Integer(0xffefd5));
        named.put("peachpuff", new Integer(0xffdab9));
        named.put("peru", new Integer(0xcd853f));
        named.put("pink", new Integer(0xffc0cb));
        named.put("plum", new Integer(0xdda0dd));
        named.put("powderblue", new Integer(0xb0e0e6));
        named.put("purple", new Integer(0x800080));
        named.put("red", new Integer(0xff0000));
        named.put("rosybrown", new Integer(0xbc8f8f));
        named.put("royalblue", new Integer(0x4169e1));
        named.put("saddlebrown", new Integer(0x8b4513));
        named.put("salmon", new Integer(0xfa8072));
        named.put("sandybrown", new Integer(0xf4a460));
        named.put("seagreen", new Integer(0x2e8b57));
        named.put("seashell", new Integer(0xfff5ee));
        named.put("sienna", new Integer(0xa0522d));
        named.put("silver", new Integer(0xc0c0c0));
        named.put("skyblue", new Integer(0x87ceeb));
        named.put("slateblue", new Integer(0x6a5acd));
        named.put("slategray", new Integer(0x708090));
        named.put("snow", new Integer(0xfffafa));
        named.put("springgreen", new Integer(0x00ff7f));
        named.put("steelblue", new Integer(0x4682b4));
        named.put("tan", new Integer(0xd2b48c));
        named.put("teal", new Integer(0x008080));
        named.put("thistle", new Integer(0xd8bfd8));
        named.put("tomato", new Integer(0xff6347));
        named.put("turquoise", new Integer(0x40e0d0));
        named.put("violet", new Integer(0xee82ee));
        named.put("violetred", new Integer(0xd02090));
        named.put("wheat", new Integer(0xf5deb3));
        named.put("white", new Integer(0xffffff));
        named.put("whitesmoke", new Integer(0xf5f5f5));
        named.put("yellow", new Integer(0xffff00));
        named.put("yellowgreen", new Integer(0x9acd32));

        ColourHelper.namedColours = named;
    }

    /**
     * Returns the red/green/blue colour value given a web colour name.
     * 
     * @param The
     *            standard colour name.
     * @return -1 indicates an unknown name or the rgb value.
     */
    public static int getColour(final String namedColour) {
        final Integer rgbValue = (Integer) ColourHelper.namedColours.get(namedColour.toLowerCase());
        return rgbValue == null ? -1 : rgbValue.intValue();
    }
}
