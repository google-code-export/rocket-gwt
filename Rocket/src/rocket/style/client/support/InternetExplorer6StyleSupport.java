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

import rocket.style.client.CssUnit;
import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Provides the IE6 specific implementation of various StyleHelper support methods.
 * 
 * Each of the public getters/setters include workarounds to successfully/accurately retrieve a particular value.
 * 
 * <h6>Gotchas</h6>
 * <ul>
 * <li> In order to simulate the UserSelect css property text selection is disabled by controlling an elements ondrag and onselectstart
 * event listeners. </li>
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * FIX add special case for weight / normal = 400 / bold = 700 / lighter/bolder.
 */
public class InternetExplorer6StyleSupport extends StyleSupport {

    protected String getUserSelectPropertyName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves a style property from the given style
     * 
     * @param element
     * @param propertyName
     */
    public String getRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
        StyleHelper.checkPropertyName("parameter:propertyName", propertyName);

        String propertyValue = null;
        while (true) {
            if (StyleConstants.OPACITY.equals(propertyName)) {
                propertyValue = this.getRuleOpacity(rule);
                break;
            }
            if (StyleConstants.USER_SELECT.equals(propertyName)) {
                propertyValue = this.getRuleUserSelect(rule);
                break;
            }

            propertyValue = super.getRuleStyleProperty(rule, propertyName);
            break;
        }
        return propertyValue;
    }

    protected String getRuleOpacity(final JavaScriptObject rule) {
        return this.getStyleOpacity(this.getStyle(rule));
    }

    protected String getRuleUserSelect(final JavaScriptObject rule) {
        throw new UnsupportedOperationException();
    }

    public void setRuleStyleProperty(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
        while (true) {
            if (StyleConstants.BACKGROUND_IMAGE.equals(propertyName)) {
                this.setRuleBackgroundImage(rule, propertyValue);
                break;
            }
            if (StyleConstants.OPACITY.equals(propertyName)) {
                this.setRuleOpacity(rule, propertyValue);
                break;
            }
            if (StyleConstants.USER_SELECT.equals(propertyName)) {
                this.setRuleUserSelect(rule, propertyValue);
                break;
            }
            super.setRuleStyleProperty(rule, propertyName, propertyValue);
        }
    }

    protected void setRuleBackgroundImage(final JavaScriptObject rule, final String value) {
        this.setStyleBackgroundImage(this.getStyle(rule), value);
    }

    protected void setRuleOpacity(final JavaScriptObject rule, final String value) {
        this.setStyleOpacity(this.getStyle(rule), value);
    }

    protected void setRuleUserSelect(final JavaScriptObject rule, final String value) {
        throw new UnsupportedOperationException("It is not possible to control user select from a rule in IE");
    }

    /**
     * Removes a style property belonging to a Rule
     * 
     * @param rule
     * @param propertyName
     */
    public void removeRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
        final JavaScriptObject nativeRule = this.getStyle(rule);
        ObjectHelper.setString(nativeRule, propertyName, "");
    }

    /**
     * Retrieves a style property from the given style
     * 
     * @param element
     * @param propertyName
     */
    public String getInlineStyleProperty(final Element element, final String propertyName) {
        StyleHelper.checkPropertyName("parameter:propertyName", propertyName);

        String propertyValue = null;
        while (true) {
            if (StyleConstants.OPACITY.equals(propertyName)) {
                propertyValue = this.getInlineOpacity(element);
                break;
            }

            propertyValue = super.getInlineStyleProperty(element, propertyName);
            break;
        }
        return propertyValue;
    }

    protected String getInlineOpacity(final Element element) {
        return this.getStyleOpacity(this.getStyle(element));
    }

    protected String getInlineUserSelect(final Element element) {
        return this.getInlineUserSelect0(element);
    }

    native String getInlineUserSelect0(final Element element)/*-{
     var result = true;
     
     var f = element.ondrag;
     if( f ){
     result = f();
     }
     
     // disabled == "none" // enabled = null
     return result ? null : "none";        
     }-*/;

    public void setInlineStyleProperty(final Element element, final String propertyName, final String propertyValue) {
        while (true) {
            if (StyleConstants.BACKGROUND_IMAGE.equals(propertyName)) {
                this.setInlineBackgroundImage(element, propertyValue);
                break;
            }
            if (StyleConstants.OPACITY.equals(propertyName)) {
                this.setInlineOpacity(element, propertyValue);
                break;
            }

            super.setInlineStyleProperty(element, propertyName, propertyValue);
            break;
        }
    }

    protected void setInlineBackgroundImage(final Element element, final String value) {
        this.setStyleBackgroundImage(this.getStyle(element), value);
    }

    protected void setInlineOpacity(final Element element, final String value) {
        this.setStyleOpacity(this.getStyle(element), value);
    }

    protected void setInlineUserSelect(final Element element, final String value) {
        final boolean enable = false == "none".equals(value);
        this.setInlineUserSelect0(element, enable);
    }

    native private void setInlineUserSelect0(final Element element, final boolean enable)/*-{    
     var f = function(){ return enable };
     element.ondrag = f;
     element.onselectstart = f;
     }-*/;

    protected String getStyleOpacity(final JavaScriptObject style) {
        final String propertyValue = ObjectHelper.getString(style, StyleSupportConstants.FILTER);
        return this.translateFromOpacity(propertyValue);
    }

    /**
     * Special case to handle the setting of a background-image upon an element.
     * 
     * @param element
     * @param url
     */
    protected void setStyleBackgroundImage(final JavaScriptObject style, final String url) {
        ObjectHelper.checkNotNull("parameter:style", style);
        StringHelper.checkNotEmpty("parameter:url", url);

        // backup other background properties that will get lost when background shortcut with image is written.
        final String colour = ObjectHelper.getString(style, StyleConstants.BACKGROUND_COLOR);
        final String attachment = ObjectHelper.getString(style, StyleConstants.BACKGROUND_ATTACHMENT);
        final String position = ObjectHelper.getString(style, StyleConstants.BACKGROUND_POSITION);
        final String repeat = ObjectHelper.getString(style, StyleConstants.BACKGROUND_REPEAT);

        ObjectHelper.setString(style, StyleConstants.BACKGROUND, url);

        // restore other background properties...
        if (false == StringHelper.isNullOrEmpty(colour)) {
            ObjectHelper.setString(style, StyleConstants.BACKGROUND_COLOR, colour);
        }
        if (false == StringHelper.isNullOrEmpty(attachment)) {
            ObjectHelper.setString(style, StyleConstants.BACKGROUND_ATTACHMENT, attachment);
        }
        if (false == StringHelper.isNullOrEmpty(position)) {
            ObjectHelper.setString(style, StyleConstants.BACKGROUND_POSITION, position);
        }
        if (false == StringHelper.isNullOrEmpty(repeat)) {
            ObjectHelper.setString(style, StyleConstants.BACKGROUND_REPEAT, repeat);
        }
    }

    protected void setStyleOpacity(final JavaScriptObject style, final String value) {
        final String propertyName0 = StyleSupportConstants.FILTER;
        final String propertyValue0 = this.translateToOpacity(value);

        ObjectHelper.setString(style, propertyName0, propertyValue0);
    }

    /**
     * Translates an css opacity value to an InternetExplorer 6.x filter style property. Does the opposite of
     * {@link #translateFromOpacity(String)} an opacity value.
     * 
     * @param value
     * @return
     */
    protected String translateToOpacity(final String value) {
        StringHelper.checkNotEmpty("parameter:value", value);

        final double doubleValue = Double.parseDouble(value);
        final int percentageValue = (int) (doubleValue * 100);

        return "alpha(opacity=" + percentageValue + ")";
    }

    /**
     * Retrieves the computed style for the given element.
     * 
     * <h6>Special cases</h6>
     * <ul>
     * <li>Attempts to retrieve the css opacity property is converted into filter, the value is converted back into a decimal value. </li>
     * <li>If requests for width or height are made and auto returned calculate the actual width/height in pixels using
     * {@link #getComputedWidth(Element)} and {@link #getComputedHeight(Element)}</li>
     * </ul>
     */
    public String getComputedStyleProperty(final Element element, final String propertyName) {
        String propertyValue = null;
        while (true) {
            if (StyleConstants.BACKGROUND_POSITION.equals(propertyName)) {
                propertyValue = this.getComputedBackgroundPosition(element);
                break;
            }
            if (propertyName.equals(StyleConstants.FONT_SIZE)) {
                final int fontSize = this.getComputedFontSize(element);
                if (-1 != fontSize) {
                    propertyValue = fontSize + "px";
                }
                break;
            }
            if (StyleConstants.HEIGHT.equals(propertyName)) {
                propertyValue = this.getComputedHeight(element);
                break;
            }
            // opacity is a special case...
            if (StyleConstants.OPACITY.equals(propertyName)) {
                propertyValue = this.getComputedOpacity(element);
                break;
            }
            if (StyleConstants.WIDTH.equals(propertyName)) {
                propertyValue = this.getComputedWidth(element);
                break;
            }

            propertyValue = super.getComputedStyleProperty(element, propertyName);
            break;
        }

        return propertyValue;
    }

    protected String getComputedStyleProperty0(final Element element, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StyleHelper.checkPropertyName("parameter:propertyName", propertyName);

        return this.getComputedStyleProperty1(element, propertyName);
    }

    /**
     * Uses jsni to search for a concrete value for the given element.
     * 
     * Current property values that are skipped include
     * <ul>
     * <li>inherit</li>
     * <li>transparent</li>
     * </ul>
     * 
     * @param element
     * @param propertyName
     * @return
     */
    private native String getComputedStyleProperty1(final Element element, final String propertyName)/*-{
     var value = null;
     var element0 = element;

     while( element0 && element0.currentStyle ){
     value = element0.currentStyle[ propertyName ];         

     // continue looping until a concrete value is found.
     if( value && value != "inherit" && value != "transparent" ){
     break;
     }
     element0 = element0.parentNode;
     }

     return value ? value : null;
     }-*/;

    protected String getComputedOpacity(final Element element) {
        String value = this.getComputedStyleProperty0(element, StyleSupportConstants.FILTER);
        if (null != value) {
            value = this.translateFromOpacity(value);
        }
        return value;
    }

    /**
     * @param element
     * @return
     */
    protected String getComputedUserSelect(final Element element) {
        return this.getComputedUserSelect0(element);
    }

    native private String getComputedUserSelect0(final Element element)/*-{
     var result = true;
     
     var element0 = element;
     while( null != element0 ){
     var f = element0.ondrag;
     if( f ){
     result = f();
     break;
     }
     
     element0 = element.parentNode;
     }
     // enabled = null // disabled = "none"
     return result ? null : "none";
     }-*/;

    /**
     * Retrieves the content width of the given element
     * 
     * The content width may be calculated using the following formula:
     * 
     * contentWidth = offsetWidth - paddingLeft - paddingRight - borderLeftWidth - borderRightWidth
     * 
     * @param element
     * @return
     */
    protected String getComputedWidth(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        String value = null;
        while (true) {
            value = this.getComputedStyleProperty0(element, StyleConstants.WIDTH);
            // if a % or auto must calculate...
            if (false == (value.endsWith("%") || StyleSupportConstants.AUTO.equals(value))) {
                break;
            }

            final int offsetWidth = ObjectHelper.getInteger(element, "offsetWidth");

            final int borderLeft = this.getComputedBorderWidthInPixels(element, StyleConstants.BORDER_LEFT_WIDTH);
            final int borderRight = this.getComputedBorderWidthInPixels(element, StyleConstants.BORDER_RIGHT_WIDTH);

            final int paddingLeft = this.getComputedStylePropertyInPixels(element, StyleConstants.PADDING_LEFT);
            final int paddingRight = this.getComputedStylePropertyInPixels(element, StyleConstants.PADDING_RIGHT);

            final int width = offsetWidth - borderLeft - borderRight - paddingLeft - paddingRight;
            value = width + "px";
            break;
        }
        return value;
    }

    /**
     * Retrieves the content height of the given element
     * 
     * @param element
     * @return
     */
    protected String getComputedHeight(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        String value = null;
        while (true) {
            value = this.getComputedStyleProperty0(element, StyleConstants.HEIGHT);
            // if a % or auto must calculate...
            if (false == (value.endsWith("%") || StyleSupportConstants.AUTO.equals(value))) {
                break;
            }

            final int offsetHeight = ObjectHelper.getInteger(element, "offsetHeight");

            final int borderTop = this.getComputedBorderWidthInPixels(element, StyleConstants.BORDER_TOP_WIDTH);
            final int borderBottom = this.getComputedBorderWidthInPixels(element, StyleConstants.BORDER_BOTTOM_WIDTH);

            final int paddingTop = this.getComputedStylePropertyInPixels(element, StyleConstants.PADDING_TOP);
            final int paddingBottom = this.getComputedStylePropertyInPixels(element, StyleConstants.PADDING_BOTTOM);

            final int height = offsetHeight - borderTop - borderBottom - paddingTop - paddingBottom;
            value = height + "px";
            break;
        }

        return value;
    }

    /**
     * This method covers a special case only returning non zero values if a border style is also applicable.
     * 
     * @param element
     * @param propertyName
     * @return
     */
    protected int getComputedBorderWidthInPixels(final Element element, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StyleHelper.checkPropertyName("parameter:propertyName", propertyName);

        return this.getComputedBorderWidthInPixels0(element, propertyName);
    }

    native private int getComputedBorderWidthInPixels0(final Element element, final String propertyName)/*-{
     var value = 0;

     while( true ){
     var width = element.currentStyle[ propertyName ];
     if( ! width ){
     value = 0;
     break;
     }

     // if a width value is found check if a style is also set. if not return 0...
     var styleName = propertyName.substring( 0, propertyName.length - 5 ) + "Style";
     var borderStyle = element.currentStyle[ styleName ];

     if( "none" == borderStyle ){
     value = 0;
     break;
     }


     if( isNaN( width )){
     value = this.@rocket.style.client.support.StyleSupport::translateBorderWidthValue(Ljava/lang/String;)( width );
     break;
     }

     value = 0 + width;
     break;
     }
     return value;
     }-*/;

    protected int getComputedStylePropertyInPixels(final Element element, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StyleHelper.checkPropertyName("parameter:propertyName", propertyName);

        return this.getComputedStylePropertyInPixels0(element, propertyName);
    }

    native private int getComputedStylePropertyInPixels0(final Element element, final String propertyName)/*-{
     var value = element.currentStyle[ propertyName ];
     return isNaN( value ) ? parseInt( value ) : 0;
     }-*/;

    /**
     * Retrieves the computed background position of the given element.
     * 
     * @param element
     * @return A string containing the combined values of both the x and y positions.
     */
    public String getComputedBackgroundPosition(final Element element) {
        String value = null;

        while (true) {
            String x = this.getComputedStyleProperty(element, StyleSupportConstants.BACKGROUND_POSITION_X_IE6);
            String y = this.getComputedStyleProperty(element, StyleSupportConstants.BACKGROUND_POSITION_Y_IE6);

            final boolean xMissing = StringHelper.isNullOrEmpty(x);
            final boolean yMissing = StringHelper.isNullOrEmpty(y);
            if (xMissing && yMissing) {
                break;
            }
            if (xMissing) {
                x = StyleSupportConstants.DEFAULT_BACKGROUND_POSITION_X_IE6;
            }
            if (yMissing) {
                y = StyleSupportConstants.DEFAULT_BACKGROUND_POSITION_Y_IE6;
            }
            value = x + ' ' + y;
            break;
        }

        return value;
    }

    /**
     * Retrieves the computed font size for the given element taking care of absolute and relative sizes.
     * 
     * @param element
     * @return
     */
    protected int getComputedFontSize(final Element element) {
        int size = -1;
        while (true) {
            final String propertyValue = getComputedStyleProperty0(element, StyleConstants.FONT_SIZE);
            if (StringHelper.isNullOrEmpty(propertyValue)) {
                size = -1;
                break;
            }
            // absolute sizes...
            if (StyleSupportConstants.FONT_SIZE_X_SMALL.equals(propertyValue)) {
                size = StyleSupportConstants.FONT_SIZE_X_SMALL_PX;
                break;
            }
            if (StyleSupportConstants.FONT_SIZE_SMALL.equals(propertyValue)) {
                size = StyleSupportConstants.FONT_SIZE_SMALL_PX;
                break;
            }
            if (StyleSupportConstants.FONT_SIZE_MEDIUM.equals(propertyValue)) {
                size = StyleSupportConstants.FONT_SIZE_MEDIUM_PX;
                break;
            }
            if (StyleSupportConstants.FONT_SIZE_LARGE.equals(propertyValue)) {
                size = StyleSupportConstants.FONT_SIZE_LARGE_PX;
                break;
            }
            if (StyleSupportConstants.FONT_SIZE_X_LARGE.equals(propertyValue)) {
                size = StyleSupportConstants.FONT_SIZE_X_LARGE_PX;
                break;
            }
            if (StyleSupportConstants.FONT_SIZE_XX_LARGE.equals(propertyValue)) {
                size = StyleSupportConstants.FONT_SIZE_XX_LARGE_PX;
                break;
            }

            // relative sizes.. get size of parent and scale that...
            if (StyleSupportConstants.FONT_SIZE_SMALLER.equals(propertyValue)) {
                size = this.getComputedFontSizeOfParent(element, 1 * StyleSupportConstants.SMALLER_SCALING_FACTOR);
                break;
            }
            if (StyleSupportConstants.FONT_SIZE_LARGER.equals(propertyValue)) {
                size = this.getComputedFontSizeOfParent(element, StyleSupportConstants.LARGER_SCALING_FACTOR);
                break;
            }

            size = (int) StyleHelper.convertValue(propertyValue, CssUnit.PX);
            break;
        }
        return size;
    }

    /**
     * Retrieves the computed font size for the parent of the given element.
     * 
     * This method should only be called by {@link #getComputedFontSize(Element)} when it encounters a font-size of larger or smaller. This
     * method will then attempt to locate a pixel value for the font-size property of a parent(ancestor if recursive). This parent value is
     * then multiplied against the scalingFactor to give the final value.
     * 
     * @param element
     * @param scalingFactor
     * @return
     */
    protected int getComputedFontSizeOfParent(final Element element, final float scalingFactor) {
        ObjectHelper.checkNotNull("parameter:element", element);

        Element parent = DOM.getParent(element);

        int parentSize = this.getComputedFontSize(parent);
        if (-1 == parentSize) {
            parentSize = StyleSupportConstants.FONT_SIZE_MEDIUM_PX;
        }

        return Math.round(parentSize * scalingFactor);
    }

    /**
     * Removes a possibly existing style by setting its value to an empty String.
     */
    public void removeInlineStyleProperty(final Element element, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StyleHelper.checkPropertyName("parameter:propertyName", propertyName);

        while (true) {
            if (StyleConstants.OPACITY.equals(propertyName)) {
                this.removeOpacity(element);
                break;
            }
            if (StyleConstants.USER_SELECT.equals(propertyName)) {
                this.removeInlineUserSelect(element);
                break;
            }
            ObjectHelper.setString(this.getStyle(element), propertyName, "");
            break;
        }
    }

    protected void removeOpacity(final Element element) {
        ObjectHelper.setString(this.getStyle(element), StyleSupportConstants.FILTER, "");
    }

    protected void removeInlineUserSelect(final Element element) {
        this.removeInlineUserSelect0(element);
    }

    native protected void removeInlineUserSelect0(final Element element)/*-{
     element.ondrag=null;
     element.onselectstart=null;
     }-*/;

    protected int getBorderWidthThin() {
        return StyleSupportConstants.BORDER_WIDTH_THIN_PX_IE6;
    }

    protected int getBorderWidthMedium() {
        return StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX_IE6;
    }

    protected int getBorderWidthThick() {
        return StyleSupportConstants.BORDER_WIDTH_THICK_PX_IE6;
    }

    /**
     * Translates an InternetExplorer 6.x filter style property from alpha(opacity=xxx) to an opacity value.
     * 
     * This is necessary in order to present a w3c standards compatible view of all browsers including Internet Explorer.
     * 
     * @param value
     * @return
     */
    protected String translateFromOpacity(final String value) {
        String number = null;
        if (false == StringHelper.isNullOrEmpty(value)) {

            number = value.substring("alpha(opacity=".length(), value.length() - 1);
            if (number.length() < 3) {
                number = "0." + number;
            } else {
                number = number.substring(0, 1) + '.' + number.substring(1, 3);
            }
        }
        return number;
    }

    /**
     * Retrieves the names of all the computed styles available for the given element.
     * 
     * @param element
     * @return
     */
    public String[] getComputedStylePropertyNames(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        final String propertyNames = getComputedStylePropertyNames0(element);
        return StringHelper.split(propertyNames, ",", true);
    }

    /**
     * Iterates thru all the property names build a comma separated list containing all the property names. actually a true array.
     * 
     * @param object
     * @return
     */
    native private String getComputedStylePropertyNames0(final JavaScriptObject object)/*-{
     var currentStyle = object.currentStyle;
     var array = new Array();
     if( currentStyle ){
     var i = 0;
     for( propertyName in currentStyle ){
     array[ i ] = propertyName;
     i++;
     }            
     }
     return array.join(",");
     }-*/;

    /**
     * Helper which retrieves the native rules collection that this instance is presenting as a List
     * 
     * @return
     */
    public JavaScriptObject getRulesCollection(final JavaScriptObject styleSheet) {
        return ObjectHelper.getObject(styleSheet, StyleConstants.RULES_LIST_PROPERTY_IE6);
    }

    public void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText) {
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
        StringHelper.checkNotNull("parameter:selectorText", selectorText);
        StringHelper.checkNotNull("parameter:styleText", styleText);

        this.addRule0(styleSheet, selectorText, styleText);
    }

    private native void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{        
     var index = styleSheet.rules.length;
     var safeStyleText = styleText.length == 0 ? ";" : styleText;

     styleSheet.addRule( selectorText, safeStyleText, index );         
     }-*/;

    public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText,
            final String styleText) {
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
        StringHelper.checkNotNull("parameter:selectorText", selectorText);
        StringHelper.checkNotNull("parameter:styleText", styleText);

        this.insertRule0(styleSheet, index, selectorText, styleText);
    }

    private native void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText,
            final String styleText)/*-{
     styleSheet.addRule( selectorText, styleText.length == 0 ? ";" : styleText, index );         
     }-*/;

    public void removeRule(final JavaScriptObject styleSheet, final int index) {
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);

        this.removeRule0(styleSheet, index);
    }

    /**
     * Escapes to javascript to delete the requested rule.
     */
    native private void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{            
     styleSheet.removeRule( index );
     }-*/;

    public void normalize(final JavaScriptObject styleSheet) {
    }
}