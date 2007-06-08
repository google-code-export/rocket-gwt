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
package rocket.style.client;

/**
 * A collection of constants used within the this style package and css in general.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleConstants {
    public final static String CLASS_NAME = "className";

    public final static char COMPOUND = '-';

    public final static String RULES_LIST_PROPERTY = "cssRules";

    public final static String RULES_LIST_PROPERTY_IE6 = "rules";

    /**
     * A psuedo css property that is translated into browser specific properties and may be used to control whether text selection for this
     * element is possible.
     */
    public final static String USER_SELECT = "UserSelect";
    public final static String USER_SELECT_DISABLED = "none";
    public final static String USER_SELECT_ENABLED = "";
    
    // style
    // attributes...........................................................................................
    public final static String DISABLED_ATTRIBUTE = "disabled";

    public final static String STYLE_SHEET = "stylesheet";

    public final static String SELECTOR_TEXT_PROPERTY_NAME = "selectorText";

    public final static String SELECTOR_SEPARATOR = ",";

    /**
     * Each rule has a style object which contains a read/writable cssText property.
     */
    public final static String CSS_STYLE_TEXT_PROPERTY_NAME = "cssText";

    public static final String AZIMUTH = "azimuth";

    public static final String BACKGROUND = "background";

    public static final String BACKGROUND_ATTACHMENT = "backgroundAttachment";

    public static final String BACKGROUND_COLOR = "backgroundColor";

    public static final String BACKGROUND_IMAGE = "backgroundImage";

    public static final String BACKGROUND_POSITION = "backgroundPosition";

    public static final String BACKGROUND_REPEAT = "backgroundRepeat";

    public static final String BORDER = "border";

    public static final String BORDER_COLLAPSE = "borderCollapse";

    public static final String BORDER_COLOR = "borderColor";

    public static final String BORDER_SPACING = "borderSpacing";

    public static final String BORDER_STYLE = "borderStyle";

    public static final String BORDER_TOP = "borderTop";

    public static final String BORDER_RIGHT = "borderRight";

    public static final String BORDER_BOTTOM = "borderBottom";

    public static final String BORDER_LEFT = "borderLeft";

    public static final String BORDER_TOP_COLOR = "borderTopColor";

    public static final String BORDER_RIGHT_COLOR = "borderRightColor";

    public static final String BORDER_BOTTOM_COLOR = "borderBottomColor";

    public static final String BORDER_LEFT_COLOR = "borderLeftColor";

    public static final String BORDER_TOP_STYLE = "borderTopStyle";

    public static final String BORDER_RIGHT_STYLE = "borderRightStyle";

    public static final String BORDER_BOTTOM_STYLE = "borderBottomStyle";

    public static final String BORDER_LEFT_STYLE = "borderLeftStyle";

    public static final String BORDER_TOP_WIDTH = "borderTopWidth";

    public static final String BORDER_RIGHT_WIDTH = "borderRightWidth";

    public static final String BORDER_BOTTOM_WIDTH = "borderBottomWidth";

    public static final String BORDER_LEFT_WIDTH = "borderLeftWidth";

    public static final String BORDER_WIDTH = "borderWidth";

    public static final String BOTTOM = "bottom";

    public static final String CAPTION_SIDE = "captionSide";

    public static final String CLEAR = "clear";

    public static final String CLIP = "clip";

    public static final String COLOR = "color";

    public static final String CONTENT = "content";

    public static final String COUNTER_INCREMENT = "counterIncrement";

    public static final String COUNTER_RESET = "counterReset";

    public static final String CUE = "cue";

    public static final String CUE_AFTER = "cueAfter";

    public static final String CUE_BEFORE = "cueBefore";

    public static final String CURSOR = "cursor";

    public static final String DIRECTION = "direction";

    public static final String DISPLAY = "display";

    public static final String ELEVATION = "elevation";

    public static final String EMPTY_CELLS = "emptyCells";

    public static final String FLOAT = "cssFloat";

    public static final String FONT = "font";

    public static final String FONT_FAMILY = "fontFamily";

    public static final String FONT_SIZE = "fontSize";

    public static final String FONT_SIZE_ADJUSTED = "fontSizeAdjust";

    public static final String FONT_STRETCH = "fontStretch";

    public static final String FONT_STYLE = "fontStyle";

    public static final String FONT_VARIANT = "fontVariant";

    public static final String FONT_WEIGHT = "fontWeight";

    public static final String HEIGHT = "height";

    public static final String LEFT = "left";

    public static final String LETTER_SPACING = "letterSpacing";

    public static final String LINE_HEIGHT = "lineHeight";

    public static final String LIST_STYLE = "listStyle";

    public static final String LIST_IMAGE = "listStyleImage";

    public static final String LIST_STYLE_POSITION = "listStylePosition";

    public static final String LIST_STYLE_TYPE = "listStyleType";

    public static final String MARGIN = "margin";

    public static final String MARGIN_TOP = "marginTop";

    public static final String MARGIN_RIGHT = "marginRight";

    public static final String MARGIN_BOTTOM = "marginBottom";

    public static final String MARGIN_LEFT = "marginLeft";

    public static final String MARKER_OFFSET = "markerOffset";

    public static final String MARKS = "marks";

    public static final String MAX_HEIGHT = "maxHeight";

    public static final String MAX_WIDTH = "maxWidth";

    public static final String MIN_HEIGHT = "minHeight";

    public static final String MIN_WIDTH = "minWidth";

    public static final String ORPHANS = "orphans";

    public static final String OUTLINE = "outline";

    public static final String OUTLINE_COLOR = "outlineColor";

    public static final String OUTLINE_STYLE = "outlineStyle";

    public static final String OUTLINE_WIDTH = "outlineWidth";

    public static final String OVERFLOW = "overflow";

    public static final String OVERFLOW_X = "overflowX";

    public static final String OVERFLOW_Y = "overflowY";

    public static final String PADDING = "padding";

    public static final String PADDING_TOP = "paddingTop";

    public static final String PADDING_RIGHT = "paddingRight";

    public static final String PADDING_BOTTOM = "paddingBottom";

    public static final String PADDING_LEFT = "paddingLeft";

    public static final String PAGE = "page";

    public static final String BREAK_AFTER = "pageBreakAfter";

    public static final String BREAK_BEFORE = "pageBreakBefore";

    public static final String BREAK_INSIDE = "pageBreakInside";

    public static final String PAUSE = "pause";

    public static final String PAUSE_AFTER = "pauseAfter";

    public static final String PAUSE_BEFORE = "pauseBefore";

    public static final String PITCH = "pitch";

    public static final String PITCH_RANGE = "pitchRange";

    public static final String PLAY_DURING = "playDuring";

    public static final String POSITION = "position";

    public static final String QUOTES = "quotes";

    public static final String RICHNESS = "richness";

    public static final String RIGHT = "right";

    public static final String SIZE = "size";

    public static final String SPEAK = "speak";

    public static final String SPEAK_HEADER = "speakHeader";

    public static final String SPEAK_NUMERICAL = "speakNumeral";

    public static final String SPEAK_PUNCTUATION = "speakPunctuation";

    public static final String SPEECH_RATE = "speechRate";

    public static final String STRESS = "stress";

    public static final String TABLE_LAYOUT = "tableLayout";

    public static final String TEXT_ALIGN = "textAlign";

    public static final String TEXT_DECORATION = "textDecoration";

    public static final String TEXT_INDENT = "textIndent";

    public static final String TEXT_SHADOW = "textShadow";

    public static final String TEXT_TRANSFORM = "textTransform";

    public static final String TOP = "top";

    public static final String UNICODE_BIDI = "unicodeBidi";

    public static final String VERTICAL_ALIGN = "verticalAlign";

    public static final String VISIBILITY = "visibility";

    public static final String VOICE_FAMILY = "voiceFamily";

    public static final String VOLUME = "volume";

    public static final String WHITE_SPACE = "whiteSpace";

    public static final String WIDOWS = "widows";

    public static final String WIDTH = "width";

    public static final String WORD_SPACING = "wordSpacing";

    public static final String Z_INDEX = "zIndex";

    /**
     * THe w3c name for the opacity property.
     */
    public final static String OPACITY = "opacity";

    /**
     * A scaling factor that may be used to convert inches into pixels
     */
    public final static float IN_TO_PX = 1 / 96f;

    public final static float PX_TO_IN = 1 / IN_TO_PX;

    public final static float CM_TO_PX = IN_TO_PX * 2.54f;

    public final static float PX_TO_CM = 1 / CM_TO_PX;

    public final static float MM_TO_PX = 10f * CM_TO_PX;

    public final static float PX_TO_MM = 1 / MM_TO_PX;

    public final static float PC_TO_PX = 16f;

    public final static float PX_TO_PC = 1 / PC_TO_PX;

    public final static float PT_TO_PX = 96f / 72f;

    public final static float PX_TO_PT = 1 / PT_TO_PX;
}
