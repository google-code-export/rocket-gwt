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
package rocket.style.client;

import java.util.List;

import rocket.browser.client.BrowserHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

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
            final JavaScriptObject nativeStyleSheets = StyleHelper.getStyleSheets0();
            if (null == nativeStyleSheets) {
                throw new UnsupportedOperationException(BrowserHelper.getUserAgent() + " doesnt support StyleSheets");
            }
            styleSheets.setObject(nativeStyleSheets);
            StyleHelper.setStyleSheets(styleSheets);
        }
        return styleSheets;
    }

    protected static native JavaScriptObject getStyleSheets0()/*-{
     var styleSheet = $doc.styleSheets;
     return styleSheet ? styleSheet : null;
     }-*/;

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
            StyleHelper.handleAssertFailure("The " + name + " contains more than one selector, selectorText["
                    + selectorText + "]");
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
     var propertyName0 = @rocket.style.client.StyleHelper::toJavascriptPropertyName(Ljava/lang/String;)(propertyName);
     
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
}