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
package rocket.style.client.support;

public class StyleSupportConstants {
    /**
     * THe internet explorer name for the filter property which includes various sub-properties including opacity
     */
    public final static String FILTER = "filter";

    /**
     * Auto is sometimes a magic value that indicates the browser will calculate a real value when it sees fit.
     */
    public final static String AUTO = "auto";

    /**
     * A variety of measurements relating to word values for borders.
     */
    public final static String BORDER_WIDTH_THIN = "thin";

    public final static int BORDER_WIDTH_THIN_PX = 1;

    public final static int BORDER_WIDTH_THIN_PX_IE6 = 2;

    public final static String BORDER_WIDTH_MEDIUM = "medium";

    public final static int BORDER_WIDTH_MEDIUM_PX = 3;

    public final static int BORDER_WIDTH_MEDIUM_PX_IE6 = 4;

    public final static String BORDER_WIDTH_THICK = "thick";

    public final static int BORDER_WIDTH_THICK_PX = 5;;

    public final static int BORDER_WIDTH_THICK_PX_IE6 = 6;

    /**
     * A variety of named font size constants.
     */
    public final static String FONT_SIZE_X_SMALL = "x-small";

    public final static int FONT_SIZE_X_SMALL_PX = 10;

    public final static String FONT_SIZE_SMALL = "small";

    public final static int FONT_SIZE_SMALL_PX = 13;

    public final static String FONT_SIZE_MEDIUM = "medium";

    public final static int FONT_SIZE_MEDIUM_PX = 16;

    public final static String FONT_SIZE_LARGE = "large";

    public final static int FONT_SIZE_LARGE_PX = 18;

    public final static String FONT_SIZE_X_LARGE = "x-large";

    public final static int FONT_SIZE_X_LARGE_PX = 24;

    public final static String FONT_SIZE_XX_LARGE = "xx-large";

    public final static int FONT_SIZE_XX_LARGE_PX = 32;

    public final static String FONT_SIZE_SMALLER = "smaller";

    public final static String FONT_SIZE_LARGER = "larger";

    public final static float SMALLER_SCALING_FACTOR = 1 / 1.2f;

    public final static float LARGER_SCALING_FACTOR = 1.2f;

    /**
     * A variety of named font weight constants.
     */
    public final static String FONT_WEIGHT_NORMAL = "normal";

    public final static int FONT_WEIGHT_NORMAL_VALUE = 400;

    public final static String FONT_WEIGHT_BOLD = "bold";

    public final static int FONT_WEIGHT_BOLD_VALUE = 700;

    public final static String FONT_WEIGHT_BOLDER = "bolder";

    public final static String FONT_WEIGHT_LIGHTER = "lighter";

    public final static String BACKGROUND_POSITION_X_IE6 = "backgroundPositionX";

    public final static String BACKGROUND_POSITION_Y_IE6 = "backgroundPositionY";

    public final static String DEFAULT_BACKGROUND_POSITION_X_IE6 = "center";

    public final static String DEFAULT_BACKGROUND_POSITION_Y_IE6 = "center";

}
