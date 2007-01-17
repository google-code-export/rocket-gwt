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

import java.util.List;
import java.util.Map;

import rocket.style.client.support.StyleHelperSupport;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * A variety of helper methods related to css/stylesheets and widgets/html.
 * 
 * This helper provides support for changing styles/classes for widgets that use a heirarchical manner to name their composite
 * widgets/elements.
 * 
 * The {@link #getComputedStyleProperty(Element, String) and {@link #getInlineStyleProperty(Element, String)}
 * methods return null rather than none when the value indicates a particular property is missing.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * TODO add support to converting % values into pixels and other absolute units. 
 *   Would require multiplying % value again contentbox width/height
 *   This is definitely needed for IE.
 */
public class StyleHelper {

    /**
     * An StyleHelperSupport instance is used to provide support of Browser specific features.
     */
    private static final StyleHelperSupport support = (StyleHelperSupport) GWT.create(StyleHelperSupport.class);

    protected static StyleHelperSupport getSupport() {
        return StyleHelper.support;
    }

    /**
     * A cached copy of the StyleSheetList.
     */
    private static StyleSheetList styleSheetList;

    /**
     * Factory method which creates a StyleSheetCollection.
     * 
     * @return
     */
    public static List getStyleSheets() {
        StyleSheetList styleSheets = null;
        if (StyleHelper.hasStyleSheets()) {
            styleSheets = StyleHelper.styleSheetList;
        } else {
            styleSheets = new StyleSheetList();
            StyleHelper.setStyleSheets(styleSheets);
        }
        return styleSheets;
    }
    
    public static JavaScriptObject getStyleSheetCollection(){
        return StyleHelper.getSupport().getStyleSheetCollection();
    }
    
    protected static boolean hasStyleSheets() {
        return null != styleSheetList;
    }

    protected static void setStyleSheets(final StyleSheetList styleSheetList) {
        ObjectHelper.checkNotNull("parameter:styleSheetList", styleSheetList);
        StyleHelper.styleSheetList = styleSheetList;
    }

    /**
     * Verifies that the given selectorText contains only a single selector.
     * 
     * @param name
     * @param selectorText
     */
    public static void checkSelector(final String name, final String selectorText) {
        if (StringHelper.isNullOrEmpty(selectorText) | -1 != selectorText.indexOf(StyleConstants.SELECTOR_SEPARATOR)) {
            ObjectHelper.fail("The " + name + " contains more than one selector, selectorText[" + selectorText + "]");
        }
    }

    /**
     * Concatenates or builds a complete stylename given a prefix and a suffix.
     * 
     * @param prefix
     * @param suffix
     * @return
     */
    public static String buildCompound(final String prefix, final String suffix) {
        StringHelper.checkNotEmpty("parameter:prefix", prefix);
        StringHelper.checkNotEmpty("parameter:suffix", suffix);

        return prefix + StyleConstants.COMPOUND + suffix;
    }

    /**
     * Checks and that the given style property name is valid, throwing an exception if it is not
     * 
     * @param name
     * @param propertyName
     */
    public static void checkPropertyName(final String name, final String propertyName) {
        StringHelper.checkNotEmpty(name, propertyName);
    }

    /**
     * Checks that the style property value is valid, throwing an exception if it is not
     * 
     * @param name
     * @param propertyValue
     */
    public static void checkPropertyValue(final String name, final String propertyValue) {
        StringHelper.checkNotNull(name, propertyValue);
    }

    /**
     * This method retrieves a concrete value(it ignores inherited, transparent etc) given a propertyName for any element.
     * 
     * @param element
     * @param propertyName
     *            The javascript form of the css property (ie backgroundColor NOT background-color).
     * @return The String value of the property or null if it wasnt found. Unless the propertyName is not a valid style some default will
     *         always be returned.
     */
    public static String getComputedStyleProperty(final Element element, final String propertyName) {
        return StyleHelper.getSupport().getComputedStyleProperty(element, propertyName);
    }

    /**
     * Factory method which returns a view of all current Styles for the given element. The Style object returned must be destroyed when no
     * longer needed.
     * 
     * @param element
     * @return
     */
    static public Map getComputedStyle(final Element element) {
        final ComputedStyle style = new ComputedStyle();
        style.setElement(element);
        return style;
    }

    /**
     * Factory method which returns a view of all the inline style object for the given element. The Style object returned must be destroyed
     * when no longer needed.
     * 
     * @param element
     * @return
     */
    static public Map getInlineStyle(final Element element) {
        final InlineStyle style = new InlineStyle();
        style.setElement( element );
        return style;
    }

    /**
     * Retrieves an inline style property by name.
     * 
     * @param element
     * @param name
     * @return
     */    
    static public String getInlineStyleProperty( final Element element, final String name ){
        return StyleHelper.getSupport().getInlineStyleProperty( element, name );
    }

    /**
     * Sets an inline style property name with a new value.
     * 
     * @param element
     * @param propertyName
     * @param propertyValue
     */
    static public void setInlineStyleProperty(final Element element, final String propertyName, final String propertyValue) {
        StyleHelper.getSupport().setInlineStyleProperty(element, propertyName, propertyValue);
    }
    
    /**
     * This helper may be used to remove an existing Style's property. If the property does not exist nothing happens.
     * 
     * @param element
     * @param propertyName
     */
    static public void removeInlineStyleProperty(final Element element, final String propertyName) {
        StyleHelper.getSupport().removeInlineStyleProperty(element, propertyName);
    }


    /**
     * Retrieves an inline style property by name.
     * 
     * @param rule
     * @param name
     * @return
     */    
    static public String getRuleStyleProperty( final JavaScriptObject rule, final String name ){
        return StyleHelper.getSupport().getRuleStyleProperty( rule, name );
    }

    /**
     * Sets an inline style property name with a new value.
     * 
     * @param rule
     * @param propertyName
     * @param propertyValue
     */
    static public void setRuleStyleProperty(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
        StyleHelper.getSupport().setRuleStyleProperty(rule, propertyName, propertyValue);
    }
    
    /**
     * This helper may be used to remove an existing Style's property. If the property does not exist nothing happens.
     * 
     * @param rule
     * @param propertyName
     */
    static public void removeRuleStyleProperty(final JavaScriptObject rule, final String propertyName) {
        StyleHelper.getSupport().removeRuleStyleProperty(rule, propertyName);
    }

    /**
     * Retrieves the names of all the computed styles available for the given element.
     * @param element
     * @return
     */
    static public String[] getComputedStylePropertyNames( final Element element ){
        return StyleHelper.getSupport().getComputedStylePropertyNames( element );
    }
    
    /**
     * Extracts the unit portion as a CssUnit instance given a length.
     * 
     * @param If value is empty or null null will be returned.
     * @return 
     */
    static public CssUnit getUnit(final String value) {
        CssUnit unit = CssUnit.NONE;
        while (true) {
            // defensive test.
            if( StringHelper.isNullOrEmpty( value ) ){
                break;
            }

            if (value.endsWith("%")) {
                unit = CssUnit.PERCENTAGE;
                break;
            }

            final int valueLength = value.length();
            if (valueLength < 3) {
                unit = CssUnit.NONE;
                break;
            }
            // if the third last char is not a number then value isnt number-unit.
            final char thirdLastChar = value.charAt(valueLength - 3);
            if (false == Character.isDigit(thirdLastChar)) {
                unit = CssUnit.NONE;
                break;
            }

            unit = CssUnit.toCssUnit(value.substring(valueLength - 2));
            break;
        }
        return unit;
    }
    
    /**
     * Attempts to translate a length with units into another unit.
     * 
     * Relative units such as em/ex and percentage will fail and result in a 
     * {@link java.lang.UnsupportedOperationExceptions} being thrown.
     * 
     * @param value
     * @param targetUnit
     * @return
     */
    static public float convertValue( final String value, final CssUnit targetUnit ){
        StringHelper.checkNotEmpty( "parameter:value", value );
        ObjectHelper.checkNotNull( "parameter:targetUnit", targetUnit);
        
        float length = 0;
        while( true ){
            if( value.equals("0")){
                break;
            }
            
            final CssUnit unit = StyleHelper.getUnit( value );
            final String numberString = value.substring( 0, value.length() - unit.getValue().length() );
                        
            // convert value into a number
            length = Float.parseFloat( numberString );
            
            // if the unit and target unit are the same do nothing...
            if( unit == targetUnit ){
                break;
            }
            
            length = unit.toPixels( length );
            length = targetUnit.fromPixels( length );
            break;
        }

        return length;
    }
    
    /**
     * Helper which removes the decorating url, brackets and quotes from a string returning just the url.
     * @param value
     * @return
     */
    static public String getUrl( final String value ){
        String url = value;
        if( null != url ){
            int first = "url(".length();
            int last = url.length() - 1 -1;
            if( url.charAt( first ) == '\''){
                first++;
            }
            if( url.charAt( first ) == '"'){
                first++;
            }
            if( url.charAt( last ) == '\''){
                last--;
            }
            if( url.charAt( last ) == '"'){
                last--;
            }
            url = url.substring( first, last + 1 );
        }
        return url;
    }
    
    /**
     * Adds a new rule to the given stylesheet.
     * @param styleSheet
     * @param selectorText
     * @param styleText
     */
    static public void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText){
        StyleHelper.getSupport().addRule( styleSheet, selectorText, styleText );
    }
    
    /**
     * Inserts a new rule at the given slot in the given stylesheet
     * @param styleSheet
     * @param index
     * @param selectorText
     * @param styleText
     */
    static public void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText){
        StyleHelper.getSupport().insertRule( styleSheet, index, selectorText, styleText );    
    }
    
    /**
     * Removes an existing rule from the given stylesheet
     * @param styleSheet
     * @param index
     */
    static public void removeRule( final JavaScriptObject styleSheet, final int index ){
        StyleHelper.getSupport().removeRule( styleSheet, index );
}

    /**
     * Normalizes all the rules belonging to the given stylesheet.
     * Normalizing is the process whereby any rules with more than one selector are duplicated so that each rule has only one selector.
     * @param styleSheet
     */
    static public void normalize(final JavaScriptObject styleSheet ){
        StyleHelper.getSupport().normalize( styleSheet );
    }
    /**
     * Retrieves the collection of rules belonging to a stylesheet.
     * @param styleSheet
     * @return
     */
    static public JavaScriptObject getRules( final JavaScriptObject styleSheet ){
        return StyleHelper.getSupport().getRulesCollection(styleSheet);
    }
}