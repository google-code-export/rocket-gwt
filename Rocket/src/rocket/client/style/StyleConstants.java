package rocket.client.style;

import rocket.client.dom.DomConstants;

/**
 * A collection of constants used within the this style package.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleConstants {
    public final static char COMPOUND = '-';

    // style
    // attributes...........................................................................................
    public final static String TYPE_ATTRIBUTE = DomConstants.TYPE_ATTRIBUTE;

    public final static String HREF_ATTRIBUTE = DomConstants.HREF_ATTRIBUTE;

    public final static String DISABLED_ATTRIBUTE = "disabled";

    public final static String STYLE_SHEET = "stylesheet";

    public final static String SELECTOR_TEXT_PROPERTY_NAME = "selectorText";

    public final static String CSS_RULES_PROPERTY_NAME = "cssRules";

    public final static String CSS_RULES_INTERNET_EXPLORER_6_PROPERTY_NAME = "cssRules";

    // css unit
    // types..........................................................................................

    public final static int UNKNOWN = 0;

    public final static int NUMBER = 1;

    // relative units
    public final static int PERCENTAGE = 2;

    public final static int EMS = 3;

    public final static int EXS = 4;

    public final static int PX = 5;

    // absolute units
    public final static int CM = 6;

    public final static int MM = 7;

    public final static int INCH = 8;

    public final static int POINT = 9;

    public final static int PICA = 10;

    // angles
    public final static int DEG = 11;

    public final static int RAD = 12;

    public final static int GRAD = 13;

    // time
    public final static int MS = 14;

    public final static int S = 15;

    // frequencies
    public final static int HZ = 16;

    public final static int KHZ = 17;

    // dimension, string, uri, ident, attr
    public final static int DIMENSION = 18;

    public final static int STRING = 19;

    public final static int URI = 20;

    public final static int IDENT = 21;

    public final static int ATTR = 22;

    public final static int COUNTER = 23;

    // rect
    public final static int RECT = 24;

    // rgbcolor.
    public final static int RGBCOLOR = 25;

    // css unit
    // description..........................................................................................

    public final static String UNKNOWN_DESCRIPTION = "Unknown";

    public final static String NUMBER_DESCRIPTION = "Number";

    // relative units
    public final static String PERCENTAGE_DESCRIPTION = "Percentage";

    public final static String EMS_DESCRIPTION = "Ems";

    public final static String EXS_DESCRIPTION = "Exs";

    public final static String PX_DESCRIPTION = "Pixels";

    public final static String CM_DESCRIPTION = "Cms";

    public final static String MM_DESCRIPTION = "Mms";

    public final static String INCH_DESCRIPTION = "Inches";

    public final static String POINT_DESCRIPTION = "Points";

    public final static String PICA_DESCRIPTION = "Picas";

    // angles
    public final static String DEG_DESCRIPTION = "Degrees";

    public final static String RAD_DESCRIPTION = "Radians";

    public final static String GRAD_DESCRIPTION = "Gradians";

    // time
    public final static String MS_DESCRIPTION = "Milliseconds";

    public final static String S_DESCRIPTION = "Seconds";

    // frequencies
    public final static String HZ_DESCRIPTION = "Hertz";

    public final static String KHZ_DESCRIPTION = "KiloHertz";

    // dimension, string, uri, ident, attr
    public final static String DIMENSION_DESCRIPTION = "Dimension";

    public final static String STRING_DESCRIPTION = "String";

    public final static String URI_DESCRIPTION = "Uri";

    public final static String IDENT_DESCRIPTION = "Ident";

    public final static String ATTR_DESCRIPTION = "Attribute";

    public final static String COUNTER_DESCRIPTION = "Counter";

    // rect
    public final static String RECT_DESCRIPTION = "Rectangle";

    // rgbcolor.
    public final static String RGBCOLOR_DESCRIPTION = "RgbColour";

    // unit
    // suffixes..............................................................................................................
    public final static String PERCENTAGE_UNIT = "px";

    public final static String EMS_UNIT = "ems";

    public final static String EX_UNIT = "ex";

    public final static String PIXEL_UNIT = "px";

    public final static String CM_SUFFIX = "cm";

    public final static String MM_SUFFIX = "mm";

    public final static String INCH_SUFFIX = "in";

    public final static String POINT_SUFFIX = "pt";

    public final static String PICA_SUFFIX = "pc";

    // angles
    public final static String DEG_SUFFIX = "deg";

    public final static String RAD_SUFFIX = "rad";

    public final static String GRAD_SUFFIX = "grad";

    // time
    public final static String MS_SUFFIX = "ms";

    public final static String S_SUFFIX = "s";

    // frequencies
    public final static String HZ_SUFFIX = "hz";

    public final static String KHZ_SUFFIX = "khz";

    // conversion
    // factors.....................................................................................................
    public final static float CM_TO_MM = 10f;

    public final static float INCHES_TO_MM = 25.4f;

    public final static float POINT_TO_MM = INCHES_TO_MM / 72f;

    public final static float PICA_TO_MM = 4.23333333f;

    public final static float RAD_TO_DEG = 2 * 3.1415926535897932384626433832795f / 360;

    public final static float GRAD_TO_DEG = 400f / 360f;

    public static final String CSS_AZIMUTH = "azimuth";

    public static final String CSS_BACKGROUND = "background";

    public static final String CSS_BACKGROUND_ATTACHMENT = "backgroundAttachment";

    public static final String CSS_BACKGROUND_COLOR = "backgroundColor";

    public static final String CSS_BACKGROUND_IMAGE = "backgroundImage";

    public static final String CSS_BACKGROUND_POSITION = "backgroundPosition";

    public static final String CSS_BACKGROUND_REPEAT = "backgroundRepeat";

    public static final String CSS_BORDER = "border";

    public static final String CSS_BORDER_COLLAPSE = "borderCollapse";

    public static final String CSS_BORDER_COLOR = "borderColor";

    public static final String CSS_BORDER_SPACING = "borderSpacing";

    public static final String CSS_BORDER_STYLE = "borderStyle";

    public static final String CSS_BORDER_TOP = "borderTop";

    public static final String CSS_BORDER_RIGHT = "borderRight";

    public static final String CSS_BORDER_BOTTOM = "borderBottom";

    public static final String CSS_BORDER_LEFT = "borderLeft";

    public static final String CSS_BORDER_TOP_COLOR = "borderTopColor";

    public static final String CSS_BORDER_RIGHT_COLOR = "borderRightColor";

    public static final String CSS_BORDER_BOTTOM_COLOR = "borderBottomColor";

    public static final String CSS_BORDER_LEFT_COLOR = "borderLeftColor";

    public static final String CSS_BORDER_TOP_STYLE = "borderTopStyle";

    public static final String CSS_BORDER_RIGHT_STYLE = "borderRightStyle";

    public static final String CSS_BORDER_BOTTOM_STYLE = "borderBottomStyle";

    public static final String CSS_BORDER_LEFT_STYLE = "borderLeftStyle";

    public static final String CSS_BORDER_TOP_WIDTH = "borderTopWidth";

    public static final String CSS_BORDER_RIGHT_WIDTH = "borderRightWidth";

    public static final String CSS_BORDER_BOTTOM_WIDTH = "borderBottomWidth";

    public static final String CSS_BORDER_LEFT_WIDTH = "borderLeftWidth";

    public static final String CSS_BORDER_WIDTH = "borderWidth";

    public static final String CSS_BOTTOM = "bottom";

    public static final String CSS_CAPTION_SIDE = "captionSide";

    public static final String CSS_CLEAR = "clear";

    public static final String CSS_CLIP = "clip";

    public static final String CSS_COLOR = "color";

    public static final String CSS_CONTENT = "content";

    public static final String CSS_COUNTER_INCREMENT = "counterIncrement";

    public static final String CSS_COUNTER_RESET = "counterReset";

    public static final String CSS_CUE = "cue";

    public static final String CSS_CUE_AFTER = "cueAfter";

    public static final String CSS_CUE_BEFORE = "cueBefore";

    public static final String CSS_CURSOR = "cursor";

    public static final String CSS_DIRECTION = "direction";

    public static final String CSS_DISPLAY = "display";

    public static final String CSS_ELEVATION = "elevation";

    public static final String CSS_EMPTY_CELLS = "emptyCells";

    public static final String CSS_FLOAT = "cssFloat";

    public static final String CSS_FONT = "font";

    public static final String CSS_FONT_FAMiLY = "fontFamily";

    public static final String CSS_FONT_SIZE = "fontSize";

    public static final String CSS_FONT_SIZE_ADJUSTED = "fontSizeAdjust";

    public static final String CSS_FONT_STRETCH = "fontStretch";

    public static final String CSS_FONT_STYLE = "fontStyle";

    public static final String CSS_FONT_VARIANT = "fontVariant";

    public static final String CSS_FONT_WEIGHT = "fontWeight";

    public static final String CSS_HEIGHT = "height";

    public static final String CSS_LEFT = "left";

    public static final String CSS_LETTER_SPACING = "letterSpacing";

    public static final String CSS_LINE_HEIGHT = "lineHeight";

    public static final String CSS_LIST_STYLE = "listStyle";

    public static final String CSS_LIST_IMAGE = "listStyleImage";

    public static final String CSS_LIST_STYLE_POSITION = "listStylePosition";

    public static final String CSS_LIST_STYLE_TYPE = "listStyleType";

    public static final String CSS_MARGIN = "margin";

    public static final String CSS_MARGIN_TOP = "marginTop";

    public static final String CSS_MARGIN_RIGHT = "marginRight";

    public static final String CSS_MARGIN_BOTTOM = "marginBottom";

    public static final String CSS_MARGIN_LEFT = "marginLeft";

    public static final String CSS_MARKER_OFFSET = "markerOffset";

    public static final String CSS_MARKS = "marks";

    public static final String CSS_MAX_HEIGHT = "maxHeight";

    public static final String CSS_MAX_WIDTH = "maxWidth";

    public static final String CSS_MIN_HEIGHT = "minHeight";

    public static final String CSS_MIN_WIDTH = "minWidth";

    public static final String CSS_ORPHANS = "orphans";

    public static final String CSS_OUTLINE = "outline";

    public static final String CSS_OUTLINE_COLOR = "outlineColor";

    public static final String CSS_OUTLINE_STYLE = "outlineStyle";

    public static final String CSS_OUTLINE_WIDTH = "outlineWidth";

    public static final String CSS_OVERFLOW = "overflow";

    public static final String CSS_PADDING = "padding";

    public static final String CSS_PADDING_TOP = "paddingTop";

    public static final String CSS_PADDING_RIGHT = "paddingRight";

    public static final String CSS_PADDING_BOTTOM = "paddingBottom";

    public static final String CSS_PADDING_LEFT = "paddingLeft";

    public static final String CSS_PAGE = "page";

    public static final String CSS_BREAK_AFTER = "pageBreakAfter";

    public static final String CSS_BREAK_BEFORE = "pageBreakBefore";

    public static final String CSS_BREAK_INSIIDE = "pageBreakInside";

    public static final String CSS_PAUSE = "pause";

    public static final String CSS_PAUSE_AFTER = "pauseAfter";

    public static final String CSS_PAUSE_BEFORE = "pauseBefore";

    public static final String CSS_PITCH = "pitch";

    public static final String CSS_PITCH_RANGE = "pitchRange";

    public static final String CSS_PLAY_DURING = "playDuring";

    public static final String CSS_POSITION = "position";

    public static final String CSS_QUOTES = "quotes";

    public static final String CSS_RICHNESS = "richness";

    public static final String CSS_RIGHT = "right";

    public static final String CSS_SIZE = "size";

    public static final String CSS_SPEAK = "speak";

    public static final String CSS_SPEAK_HEADER = "speakHeader";

    public static final String CSS_SPEAK_NUMERICAL = "speakNumeral";

    public static final String CSS_SPEAK_PUNCTUATION = "speakPunctuation";

    public static final String CSS_SPEECH_RATE = "speechRate";

    public static final String CSS_STRESS = "stress";

    public static final String CSS_TABLE_LAYOUT = "tableLayout";

    public static final String CSS_TEXT_ALIGN = "textAlign";

    public static final String CSS_TEXT_DECORATION = "textDecoration";

    public static final String CSS_TEXT_INDENT = "textIndent";

    public static final String CSS_TEXT_SHADOW = "textShadow";

    public static final String CSS_TEXT_TRANSFORM = "textTransform";

    public static final String CSS_TOP = "top";

    public static final String CSS_UNICODE_BIDI = "unicodeBidi";

    public static final String CSS_VERTICAL_ALIGN = "verticalAlign";

    public static final String CSS_VISIBILITY = "visibility";

    public static final String CSS_VOICE_FAMILY = "voiceFamily";

    public static final String CSS_VOLUME = "volume";

    public static final String CSS_WHITE_SPACE = "whiteSpace";

    public static final String CSS_WIDOWS = "widows";

    public static final String CSS_WIDTH = "width";

    public static final String CSS_WORD_SPACING = "wordSpacing";

    public static final String CSS_Z_INDEX = "zIndex";
}
