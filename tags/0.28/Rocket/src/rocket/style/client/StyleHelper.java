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

import rocket.style.client.support.StyleSupport;
import rocket.util.client.Colour;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A variety of helper methods related to css/stylesheets and widgets/html.
 * 
 * This helper provides support for changing styles/classes for widgets that use a heirarchical manner to name their composite
 * widgets/elements.
 * 
 * The {@link #getComputedStyleProperty(Element, String) and {@link #getInlineStyleProperty(Element, String)} methods return null rather
 * than none when the value indicates a particular property is missing.
 * 
 * In order to enable correct simulation of fixed positioning the coordinates must be set prior to setting the position.
 * 
 * <h3>Gotchas</h3>
 * <ul>
 * <li>Fixed positioning under Internet Explorer is only supported for inline css properties set via {@link StyleHelper} and not
 * {@link DOM#setStyleAttribute(Element, String, String)}.</li>
 * <li>It is not possible to disable text selection via css, it may only be achieved by setting a inlineStyle belonging to an element.</li>
 * <li>A stylesheet rules list is normalized that is rules with multiple selectors are normalized when the each selector given its own rule
 * with the same style properties. This is done to present a common view as Internet Explorer does not allow rules to have more than one
 * selector</li>
 * <li>
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * TODO add support to converting % values into pixels and other absolute units. Would require multiplying % value again contentbox
 * width/height This is definitely needed for IE.
 */
public class StyleHelper {

    /**
     * An StyleSupport instance is used to provide support of Browser specific features.
     */
    private static final StyleSupport support = (StyleSupport) GWT.create(StyleSupport.class);

    protected static StyleSupport getSupport() {
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

    static JavaScriptObject getStyleSheetCollection() {
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
    static void checkSelector(final String name, final String selectorText) {
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

    public static Colour getComputedColourStyleProperty(final Element element, final String propertyName) {
        Colour value = null;
        final String string = StyleHelper.getComputedStyleProperty(element, propertyName);
        if (false == StringHelper.isNullOrEmpty(string)) {
            value = Colour.parse(string);
        }
        return value;
    }

    public static double getComputedDoubleStyleProperty(final Element element, final String propertyName,
            final CssUnit unit, final double defaultValue) {
        double value = defaultValue;
        final String string = StyleHelper.getComputedStyleProperty(element, propertyName);
        if (false == StringHelper.isNullOrEmpty(string)) {
            value = StyleHelper.convertValue(string, unit);
        }
        return value;
    }

    public static int getComputedIntegerStyleProperty(final Element element, final String propertyName,
            final CssUnit unit, final int defaultValue) {
        int value = defaultValue;
        final String string = StyleHelper.getComputedStyleProperty(element, propertyName);
        if (false == StringHelper.isNullOrEmpty(string)) {
            value = (int) StyleHelper.convertValue(string, unit);
        }
        return value;
    }

    public static String getComputedUrlStyleProperty(final Element element, final String propertyName) {
        String string = StyleHelper.getComputedStyleProperty(element, propertyName);
        if (false == StringHelper.isNullOrEmpty(string)) {
            string = StyleHelper.getUrl(string);
        }
        return string;
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
        style.setElement(element);
        return style;
    }

    /**
     * Retrieves an inline style property by name.
     * 
     * @param element
     * @param name
     * @return
     */
    static public String getInlineStyleProperty(final Element element, final String name) {
        return StyleHelper.getSupport().getInlineStyleProperty(element, name);
    }

    public static Colour getInlineColourStyleProperty(final Element element, final String propertyName) {
        Colour value = null;
        final String string = StyleHelper.getInlineStyleProperty(element, propertyName);
        if (false == StringHelper.isNullOrEmpty(string)) {
            value = Colour.parse(string);
        }
        return value;
    }

    public static double getInlineDoubleStyleProperty(final Element element, final String propertyName,
            final CssUnit unit, final double defaultValue) {
        double value = defaultValue;
        final String string = StyleHelper.getInlineStyleProperty(element, propertyName);
        if (false == StringHelper.isNullOrEmpty(string)) {
            value = StyleHelper.convertValue(string, unit);
        }
        return value;
    }

    public static int getInlineIntegerStyleProperty(final Element element, final String propertyName,
            final CssUnit unit, final int defaultValue) {
        int value = defaultValue;
        final String string = StyleHelper.getInlineStyleProperty(element, propertyName);
        if (false == StringHelper.isNullOrEmpty(string)) {
            value = (int) StyleHelper.convertValue(string, unit);
        }
        return value;
    }

    public static String getInlineUrlStyleProperty(final Element element, final String propertyName) {
        String string = StyleHelper.getInlineStyleProperty(element, propertyName);
        if (false == StringHelper.isNullOrEmpty(string)) {
            string = StyleHelper.getUrl(string);
        }
        return string;
    }

    /**
     * Sets an inline style property name with a new value.
     * 
     * @param element
     * @param propertyName
     * @param propertyValue
     */
    static public void setInlineStyleProperty(final Element element, final String propertyName,
            final String propertyValue) {
        StyleHelper.getSupport().setInlineStyleProperty(element, propertyName, propertyValue);
    }

    static public void setInlineColourStyleProperty(final Element element, final String propertyName,
            final Colour colour) {
        StyleHelper.setInlineStyleProperty(element, propertyName, colour.toCssColour());
    }

    static public void setInlineDoubleStyleProperty(final Element element, final String propertyName,
            final double value, final CssUnit unit) {
        // drop any trailing decimal 0's.
        final String valueAsAString = Math.round(value) == value ? String.valueOf((int) value) : String.valueOf(value);

        StyleHelper.setInlineStyleProperty(element, propertyName, valueAsAString);
    }

    static public void setInlineIntegerStyleProperty(final Element element, final String propertyName, final int value,
            final CssUnit unit) {
        StyleHelper.setInlineStyleProperty(element, propertyName, "" + value + unit.getValue());
    }

    static public void setInlineUrlStyleProperty(final Element element, final String propertyName, final String url) {
        StyleHelper.setInlineStyleProperty(element, propertyName, "url('" + url + "')");
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
    static String getRuleStyleProperty(final JavaScriptObject rule, final String name) {
        return StyleHelper.getSupport().getRuleStyleProperty(rule, name);
    }

    /**
     * Sets an inline style property name with a new value.
     * 
     * @param rule
     * @param propertyName
     * @param propertyValue
     */
    static void setRuleStyleProperty(final JavaScriptObject rule, final String propertyName, final String propertyValue) {
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
     * 
     * @param element
     * @return
     */
    static public String[] getComputedStylePropertyNames(final Element element) {
        return StyleHelper.getSupport().getComputedStylePropertyNames(element);
    }

    /**
     * Extracts the unit portion as a CssUnit instance given a length.
     * 
     * @param If
     *            value is empty or null null will be returned.
     * @return
     */
    static public CssUnit getUnit(final String value) {
        CssUnit unit = CssUnit.NONE;
        while (true) {
            // defensive test.
            if (StringHelper.isNullOrEmpty(value)) {
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
     * Relative units such as em/ex and percentage will fail and result in a {@link java.lang.UnsupportedOperationExceptions} being thrown.
     * 
     * @param value
     * @param targetUnit
     * @return
     */
    static public float convertValue(final String value, final CssUnit targetUnit) {
        StringHelper.checkNotEmpty("parameter:value", value);
        ObjectHelper.checkNotNull("parameter:targetUnit", targetUnit);

        float length = 0;
        while (true) {
            if (value.equals("0" ) || value.equals( "auto")) {
                break;
            }

            final CssUnit unit = StyleHelper.getUnit(value);
            final String numberString = value.substring(0, value.length() - unit.getValue().length());

            // convert value into a number
            length = Float.parseFloat(numberString);

            // if the unit and target unit are the same do nothing...
            if (unit == targetUnit) {
                break;
            }

            length = unit.toPixels(length);
            length = targetUnit.fromPixels(length);
            break;
        }

        return length;
    }

    /**
     * Helper which removes the decorating url, brackets and quotes from a string returning just the url.
     * 
     * @param value
     * @return
     */
    static String getUrl(final String value) {
        String url = value;
        if (null != url) {
            int first = "url(".length();
            int last = url.length() - 1 - 1;
            if (url.charAt(first) == '\'') {
                first++;
            }
            if (url.charAt(first) == '"') {
                first++;
            }
            if (url.charAt(last) == '\'') {
                last--;
            }
            if (url.charAt(last) == '"') {
                last--;
            }
            url = url.substring(first, last + 1);
        }
        return url;
    }

    /**
     * Adds a new rule to the given stylesheet.
     * 
     * @param styleSheet
     * @param selectorText
     * @param styleText
     */
    static void addRule(final JavaScriptObject styleSheet, final String selectorText, final String styleText) {
        StyleHelper.getSupport().addRule(styleSheet, selectorText, styleText);
    }

    /**
     * Inserts a new rule at the given slot in the given stylesheet
     * 
     * @param styleSheet
     * @param index
     * @param selectorText
     * @param styleText
     */
    static void insertRule(final JavaScriptObject styleSheet, final int index, final String selectorText,
            final String styleText) {
        StyleHelper.getSupport().insertRule(styleSheet, index, selectorText, styleText);
    }

    /**
     * Removes an existing rule from the given stylesheet
     * 
     * @param styleSheet
     * @param index
     */
    static void removeRule(final JavaScriptObject styleSheet, final int index) {
        StyleHelper.getSupport().removeRule(styleSheet, index);
    }

    /**
     * Normalizes all the rules belonging to the given stylesheet. Normalizing is the process whereby any rules with more than one selector
     * are duplicated so that each rule has only one selector.
     * 
     * @param styleSheet
     */
    static void normalize(final JavaScriptObject styleSheet) {
        StyleHelper.getSupport().normalize(styleSheet);
    }

    /**
     * Retrieves the collection of rules belonging to a stylesheet.
     * 
     * @param styleSheet
     * @return
     */
    static JavaScriptObject getRules(final JavaScriptObject styleSheet) {
        return StyleHelper.getSupport().getRulesCollection(styleSheet);
    }

    /**
     * Adds a new class (styleName) to the given element only if it is not present.
     * 
     * @param element
     * @param className
     */
    static public void addClass(final Element element, final String className) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StyleHelper.checkClass("parameter:className", className);

        // only add if not already present.
        if (false == hasClass(element, className)) {
            String newValue = ObjectHelper.getString(element, StyleConstants.CLASS_NAME);
            if (newValue.length() > 0) {
                newValue = newValue + " ";
            }
            newValue = newValue + className;
            ObjectHelper.setString(element, StyleConstants.CLASS_NAME, newValue);
        }

    }

    /**
     * Tests if the given element already has the given class.
     * 
     * @param element
     * @param className
     * @return
     */
    static public boolean hasClass(final Element element, final String className) {
        return -1 != indexOfClass(element, className);
    }

    /**
     * Helper method used internally to locate an existing classname within an existing classname.
     * 
     * @param element
     * @param className
     * @return
     */
    static int indexOfClass(final Element element, final String className) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StyleHelper.checkClass("parameter:className", className);

        int found = -1;

        final String classNameAttribute = ObjectHelper.getString(element, StyleConstants.CLASS_NAME);
        if (false == StringHelper.isNullOrEmpty(classNameAttribute)) {
            final String[] classNames = StringHelper.split(classNameAttribute, " ", true);
            int index = 0;

            for (int i = 0; i < classNames.length; i++) {
                final String otherClassName = classNames[i];
                if (className.equals(otherClassName)) {
                    found = index;
                    break;
                }
                index = index + otherClassName.length();
            }
        }

        return found;
    }

    /**
     * Removes an existing class(styleName) from the given element.
     * 
     * @param element
     * @param className
     */
    static public void removeClass(final Element element, final String className) {
        final int index = indexOfClass(element, className);
        if (-1 != index) {
            String classes = ObjectHelper.getString(element, StyleConstants.CLASS_NAME);

            int beforeIndex = index;
            int afterIndex = index + className.length();

            // if NOT first remove the space before...
            if (index > 0) {
                afterIndex++;
            }

            // if className is the last remove the space before...
            classes = classes.substring(0, beforeIndex) + classes.substring(afterIndex);
            ObjectHelper.setString(element, StyleConstants.CLASS_NAME, classes);
        }
    }

    /**
     * Verifies that the given class name is valid.
     * 
     * @param name
     * @param className
     */
    static void checkClass(final String name, final String className) {
        StringHelper.checkNotEmpty(name, className);
    }
}