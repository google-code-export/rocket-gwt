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
package rocket.client.style;

import java.util.Collection;
import java.util.List;

import rocket.client.browser.BrowserHelper;
import rocket.client.dom.DomCollectionList;
import rocket.client.dom.DomHelper;
import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * A variety of helper methods related to css/stylesheets and widgets/html.
 * 
 * This helper provides support for changing styles/classes for widgets that use a heirarchical manner to name their composite
 * widgets/elements.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleHelper extends ObjectHelper {

    /**
     * Singleton containing the StyleSheetCollection.
     */
    private static Collection styleSheetsCollection;

    public static Collection getStyleSheetsCollection() {
        if (false == StyleHelper.hasStyleSheetsCollection()) {
            StyleHelper.setStyleSheetsCollection(new StyleSheetsCollection());
        }
        ObjectHelper.checkNotNull("styleSheetsCollection", StyleHelper.styleSheetsCollection);
        return StyleHelper.styleSheetsCollection;
    }

    protected static boolean hasStyleSheetsCollection() {
        return null != StyleHelper.styleSheetsCollection;
    }

    protected static void setStyleSheetsCollection(final Collection styleSheetsCollection) {
        ObjectHelper.checkNotNull("parameter:styleSheetsCollection", styleSheetsCollection);
        StyleHelper.styleSheetsCollection = styleSheetsCollection;
    }

    /**
     * Package private class which provides a collection view of all stylesheets currently available.
     * 
     * @author Miroslav Pokorny (mP)
     */
    static class StyleSheetsCollection extends DomCollectionList implements List {

        StyleSheetsCollection() {
            super();

            this.setCollection(DomHelper.getStyleSheetsCollection());
        }

        /**
         * Creates the StyleSheet wrapper that matches the give element.
         * 
         * It uses a cache of styleSheet objects.
         */
        protected Object createWrapper(final JavaScriptObject element) {
            ObjectHelper.checkNotNull("parameter:element", element);

            final StyleSheet styleSheet = new StyleSheet();
            styleSheet.setElement((Element) element);
            return styleSheet;
        }

        protected void checkElementType(final Object wrapper) {
            ObjectHelper.checkNotNull("parameter:wrapper", wrapper);
            if (false == (wrapper instanceof StyleSheet)) {
                BrowserHelper.handleAssertFailure("parameter:wrapper",
                        "All elements of this List must be of StyleSheet and not elementType["
                                + GWT.getTypeName(wrapper));
            }
        }

        protected void add0(final JavaScriptObject collection, final JavaScriptObject element) {
            throw new UnsupportedOperationException(GWT.getTypeName(this) + "add0()");
        }

        protected void insert0(final JavaScriptObject collection, final int index, final JavaScriptObject element) {
            throw new UnsupportedOperationException(GWT.getTypeName(this) + "insert0()");
        }

        protected JavaScriptObject remove0(final JavaScriptObject collection, final int index) {
            throw new UnsupportedOperationException(GWT.getTypeName(this) + "remove0()");
        }

    }// StyleSheetsCollection

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
     * Converts a cssPropertyName into a javascript propertyName. eg
     * 
     * <pre>
     * String css = &quot;background-color&quot;;
     * String js = toJavascriptPropertyName(css);
     * System.out.println(css + &quot;&gt;&quot; + js); // prints [[[background-color &gt; backgroundColor.]]] without the brackets. 
     * </pre>
     * 
     * @param cssPropertyName
     * @return
     */
    public static String toJavascriptPropertyName(final String cssPropertyName) {
        final StringBuffer buf = new StringBuffer();
        final int length = cssPropertyName.length();
        boolean capitalizeNext = false;

        for (int i = 0; i < length; i++) {
            char c = cssPropertyName.charAt(i);
            if ('-' == c) {
                capitalizeNext = true;
                continue;
            }
            if (capitalizeNext) {
                c = Character.toUpperCase(c);
                capitalizeNext = false;
            }
            buf.append(c);
        }
        return buf.toString();
    }

    /**
     * Handy method for retrieving any style property value in a browser independent manner.
     * 
     * @param element
     * @param propertyName
     *            The css property name (background-color) NOT the javascript version (backgroundColour).
     * @return The String value of the property or null if it wasnt found.
     */
    public static String getCurrentStyleProperty(final Element element, final String propertyName) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);

        return getCurrentStylePropertyName0(element, propertyName);
    }

    private static native String getCurrentStylePropertyName0(final Element element, final String propertyName)/*-{
     var value = null;
     
     while( true ){
     // firefox ......................................................................................
     if( $wnd.getComputedStyle ) {
     var element0 = element;
     while( element0 ){
     value = $wnd.getComputedStyle(element0,null).getPropertyValue( propertyName );
     if( value != "transparent" ){
     break;
     }
     element0 = element0.parentNode;
     }
     break;
     }	
     
     // internet explorer ..................................................................................
     if( element.currentStyle ){
     // translate css property name into a javascript property name...
     var propertyName0 = @rocket.client.style.StyleHelper::toJavascriptPropertyName(Ljava/lang/String;)(propertyName);
     
     // loop until non transparent value found or root of document is found.
     var element0 = element;
     while( element0 ){
     value = element0.currentStyle[ propertyName0 ];
     if( value != "transparent" ){
     break;
     }
     element0 = element0.parentNode;
     }
     break;
     } 
     
     break;
     }
     
     return value ? value : null;
     }-*/;

    public static void handleDisconnected(final String name) {
        StyleHelper.handleAssertFailure("The " + name + " disconnected from parent Rule.");
    }

    /**
     * Retrieves the rules array of Rules objects from the given object in a browser independent manner.
     * 
     * @param object
     * @return
     */
    public static JavaScriptObject getRules(final JavaScriptObject object) {
        final JavaScriptObject rules = DomHelper.hasProperty(object, StyleConstants.CSS_RULES_PROPERTY_NAME) ? DomHelper
                .getPropertyAsJavaScriptObject(object, StyleConstants.CSS_RULES_PROPERTY_NAME)
                : DomHelper.getPropertyAsJavaScriptObject(object,
                        StyleConstants.CSS_RULES_INTERNET_EXPLORER_6_PROPERTY_NAME);

        ObjectHelper.checkNotNull("rules", rules);
        return rules;
    }
}