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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * This and sub-classes contain the browser specific jsni stuff for StyleHelper.
 * 
 * <h6>Gotchas</h6>
 * <p>
 * The content width of a child that is wider than its parent with scrollbars reports different values in FF compared to IE. The width of
 * the child is never greater than the parent whilst IE reports the actual number of pixels that the child is if one peeks inside the
 * scrollable area. The same problem is manifest for content height.
 * 
 * The content width is calculate as the offsetWidth of the element less any padding and borders.
 * 
 * One possible solution to this problem if one wishes to report values that match FF is to check if the child width is greater than the
 * parent. If it is calculate the child's width using the coordinates of the parent. This may be costly as the parent itself could be inside
 * a scrollable area etc.
 * </p>
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSupport {

    public StyleSupport() {
        super();
    }

    /**
     * Retrieves the style sheet collection attached to this document
     * 
     * @return
     */
    native public JavaScriptObject getStyleSheetCollection()/*-{
     var styleSheet = $doc.styleSheets;
     return styleSheet ? styleSheet : null;
     }-*/;

    /**
     * Retrieves a property value from the style belonging to a Rule
     * 
     * @param element
     * @param propertyName
     * @return
     * 
     * FIX check not sure why was converting to cssPropertyName.. prolly can delete.
     */
    public String getRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:rule", rule);

        final String value = ObjectHelper.getString(this.getStyle(rule), propertyName);
        return this.translateNoneValuesToNull(value);
    }

    /**
     * Sets a style property belonging to a rule with a new value.
     * 
     * @param element
     * @param propertyName
     * @param propertyValue
     */
    public void setRuleStyleProperty(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
        ObjectHelper.setString(this.getStyle(rule), propertyName, StringHelper.toCssPropertyName(propertyName));
    }

    /**
     * Removes a style property belonging to a Rule
     * 
     * @param rule
     * @param propertyName
     */
    public void removeRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
        this.removeRuleStyleProperty0(rule, StringHelper.toCssPropertyName(propertyName));
    }

    private native void removeRuleStyleProperty0(final JavaScriptObject rule, final String propertyName)/*-{
     rule.style.removeProperty( propertyName );
     }-*/;

    /**
     * Retrieves an inline style property by name.
     * 
     * @param element
     * @param propertyName
     * @return
     */
    public String getInlineStyleProperty(final Element element, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StyleHelper.checkPropertyName("parameter:propertyName", propertyName);

        final String value = ObjectHelper.getString(this.getStyle(element), propertyName);
        return this.translateNoneValuesToNull(value);
    }

    /**
     * Sets an inline style property name with a new value.
     * 
     * @param element
     * @param propertyName
     * @param propertyValue
     */
    public void setInlineStyleProperty(final Element element, final String propertyName, final String propertyValue) {
        ObjectHelper.setString(this.getStyle(element), propertyName, propertyValue);
    }

    public void removeInlineStyleProperty(final Element element, final String propertyName) {
        this.removeInlineStyleProperty0(element, StringHelper.toCssPropertyName(propertyName));
    }

    private native void removeInlineStyleProperty0(final Element element, final String propertyName)/*-{
     element.style.removeProperty( propertyName );
     }-*/;

    /**
     * Computes the value of the requested propertyName for the given element
     * 
     * @param element
     * @param propertyName
     * @return The value or null if the value was not found.
     */
    public String getComputedStyleProperty(final Element element, final String propertyName) {
        String propertyValue = null;
        while (true) {
            propertyValue = this.getComputedStyleProperty0(element, StringHelper.toCssPropertyName(propertyName));

            if (this.isBorderPropertyName(propertyName)) {
                propertyValue = this.translateBorderWidthValue(propertyValue) + "px";
                break;
            }
            if (StyleConstants.FONT_WEIGHT.equals(propertyName)) {
                propertyValue = "" + this.getComputedFontWeight(element);
                break;
            }

            break;
        }

        return this.translateNoneValuesToNull(propertyValue);
    }

    protected String getComputedStyleProperty0(final Element element, final String propertyName) {
        return this.getComputedStyleProperty1(element, propertyName);
    }

    private native String getComputedStyleProperty1(final Element element, final String propertyName)/*-{
     var value = null;

     var element0 = element;
     var stop = $doc.documentElement;

     // loop until a concrete value is found.
     while( element0 ){
     //value = $wnd.getComputedStyle(element0,null).getPropertyValue( propertyName );
     value = $doc.defaultView.getComputedStyle(element0,null).getPropertyValue( propertyName );

     // continue looping until a concrete value is found.
     if( value && value != "inherit" && value != "transparent" ){
     break;
     }
     if( element0 == stop ){
     value = null;
     break;
     }
     element0 = element0.parentNode;
     } 
     return value;
     }-*/;

    /**
     * Helper which tests if the given propertyName is one of the border xxx width properties.
     * 
     * @param propertyName
     * @return
     */
    protected boolean isBorderPropertyName(final String propertyName) {
        return StyleConstants.BORDER_RIGHT_WIDTH.equals(propertyName)
                || StyleConstants.BORDER_TOP_WIDTH.equals(propertyName)
                || StyleConstants.BORDER_LEFT_WIDTH.equals(propertyName)
                || StyleConstants.BORDER_BOTTOM_WIDTH.equals(propertyName);
    }

    /**
     * Translate one of the three word border values into a string containing the pixel value
     * 
     * @param value
     * @return
     */
    protected int translateBorderWidthValue(final String value) {
        StringHelper.checkNotEmpty("parameter:value", value);

        int number = 0;
        while (true) {
            if (StyleSupportConstants.BORDER_WIDTH_THIN.equals(value)) {
                number = this.getBorderWidthThin();
                break;
            }
            if (StyleSupportConstants.BORDER_WIDTH_MEDIUM.equals(value)) {
                number = this.getBorderWidthMedium();
                break;
            }
            if (StyleSupportConstants.BORDER_WIDTH_THICK.equals(value)) {
                number = this.getBorderWidthThick();
                break;
            }
            number = Integer.parseInt(value.endsWith("px") ? value.substring(0, value.length() - 2) : value);
            break;
        }
        return number;
    }

    /**
     * The three methods below are overridden in the InternetExplorer implementation as the three constants have different pixel values.
     * 
     * @return
     */
    protected int getBorderWidthThin() {
        return StyleSupportConstants.BORDER_WIDTH_THIN_PX;
    }

    protected int getBorderWidthMedium() {
        return StyleSupportConstants.BORDER_WIDTH_MEDIUM_PX;
    }

    protected int getBorderWidthThick() {
        return StyleSupportConstants.BORDER_WIDTH_THICK_PX;
    }

    /**
     * This method transforms any none values into null. Many image related properties such as
     * <ul>
     * <li>background-image</li>
     * <li>list-image</li>
     * </ul>
     * return none when the property is not set.
     * 
     * @param value
     * @return
     */
    protected String translateNoneValuesToNull(final String value) {
        return "none".equals(value) ? null : value;
    }

    /**
     * Helper which retrieves the style object belonging to an Element or Rule.
     * 
     * @param elementOrRule
     * @return
     */
    protected JavaScriptObject getStyle(final JavaScriptObject elementOrRule) {
        return ObjectHelper.getObject(elementOrRule, "style");
    }

    /**
     * Retrieves the names of all the computed styles available for the given element.
     * 
     * @param element
     * @return
     */
    public String[] getComputedStylePropertyNames(final Element element) {
        final JavaScriptObject style = this.getStyle(element);
        final String cssText = ObjectHelper.getString(style, "cssText");

        // remove any quotes...
        final List names = new ArrayList();
        final String[] tokens = StringHelper.split(cssText, " ", false);

        for (int i = 0; i < tokens.length; i++) {
            final String property = tokens[i];
            if (property.endsWith(":")) {
                final String nameLessColon = property.substring(0, property.length() - 1);
                names.add(StringHelper.toCamelCase(nameLessColon));
            }
        }

        // copy from the list into an array.
        final String[] namesArray = new String[names.size()];
        final Iterator iterator = names.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            namesArray[i] = (String) iterator.next();
            i++;
        }
        return namesArray;
    }

    /**
     * Calculates the font weight for the given element translating named values into numeric values.
     * 
     * @param element
     * @return
     */
    protected int getComputedFontWeight(final Element element) {
        int weight = -1;

        while (true) {
            final String propertyValue = getComputedStyleProperty0(element, StyleConstants.FONT_WEIGHT);
            if (StringHelper.isNullOrEmpty(propertyValue)) {
                weight = StyleSupportConstants.FONT_WEIGHT_NORMAL_VALUE;
                break;
            }
            // absolute weights...
            if (StyleSupportConstants.FONT_WEIGHT_NORMAL.equals(propertyValue)) {
                weight = StyleSupportConstants.FONT_WEIGHT_NORMAL_VALUE;
                break;
            }
            if (StyleSupportConstants.FONT_WEIGHT_BOLD.equals(propertyValue)) {
                weight = StyleSupportConstants.FONT_WEIGHT_BOLD_VALUE;
                break;
            }
            // relative weights...
            if (StyleSupportConstants.FONT_WEIGHT_BOLDER.equals(propertyValue)) {
                final Element parent = DOM.getParent(element);
                final int parentWeight = this.getComputedFontWeight(parent);
                weight = parentWeight + 300;
                break;
            }
            if (StyleSupportConstants.FONT_WEIGHT_LIGHTER.equals(propertyValue)) {
                final Element parent = DOM.getParent(element);
                final int parentWeight = this.getComputedFontWeight(parent);
                weight = parentWeight - 300;
                break;
            }
            weight = Integer.parseInt(propertyValue);
            break;
        }
        return weight;
    }

    /**
     * Retrieves the computed font weight for the parent of the given element.
     * 
     * This method should only be called by {@link #getComputedFontWeight(Element)} when it encounters a font-weight of lighter or bolder.
     * 
     * @param element
     * @return
     */
    protected int getComputedFontWeightOfParent(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        Element parent = DOM.getParent(element);
        return this.getComputedFontWeight(parent);
    }

    /**
     * Helper which retrieves the native rules collection that this instance is presenting as a List
     * 
     * @return
     */
    public JavaScriptObject getRulesCollection(final JavaScriptObject styleSheet) {
        return ObjectHelper.getObject(styleSheet, StyleConstants.RULES_LIST_PROPERTY);
    }

    public void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText) {
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
        StringHelper.checkNotNull("parameter:selectorText", selectorText);
        StringHelper.checkNotNull("parameter:styleText", styleText);

        this.addRule0(styleSheet, selectorText, styleText);
    }

    private native void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{
     var cssText = selectorText + "{" + styleText + "}";    
     var index = styleSheet.cssRules.length;
     styleSheet.insertRule( cssText, index );
     }-*/;

    public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText,
            final String styleText) {
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
        StringHelper.checkNotNull("parameter:selectorText", selectorText);
        StringHelper.checkNotNull("parameter:styleText", styleText);

        this.insertRule0(styleSheet, index, selectorText, styleText);
    }

    native private void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText,
            final String styleText)/*-{
     var cssText = selectorText + "{" + styleText + "}";
     styleSheet.insertRule( cssText, index );   
     }-*/;

    public void removeRule(final JavaScriptObject styleSheet, final int index) {
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);

        this.removeRule0(styleSheet, index);
    }

    /**
     * Escapes to javascript to delete the requested rule.
     */
    native private void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{
     styleSheet.deleteRule( index );
     }-*/;

    public void normalize(final JavaScriptObject styleSheet) {
        ObjectHelper.checkNotNull("parameter:styleSheet", styleSheet);
        this.normalize0(styleSheet);
    }

    native private void normalize0(final JavaScriptObject styleSheet) /*-{
     var rules = styleSheet.cssRules;
     var i = 0;

     while( i < rules.length ){
     var rule = rules[ i ];
     var selectorText = rule.selectorText;
     var selectors = selectorText.split( "," );
     var selectorCount = selectors.length;
     if( 1 == selectorCount ){
     i++;   
     continue;
     }
     
     var styleText = rule.style.cssText;
     
     // delete the original rule...
     styleSheet.deleteRule( i );
     
     // recreate n rules one for each selector with the same style value...
     for( var j = 0; j < selectorCount; j++ ){
     var ruleText = selectors[ j ] + "{" + styleText + "}";
     styleSheet.insertRule( ruleText, i );
     i++;            
     } // for j
     } // while
     }-*/;

}
