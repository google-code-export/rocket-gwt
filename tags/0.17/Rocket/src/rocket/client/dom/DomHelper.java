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
package rocket.client.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.client.browser.BrowserHelper;
import rocket.client.style.StyleConstants;
import rocket.client.util.ColourHelper;
import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A collection of useful methods relating to manipulating the DOM
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DomHelper extends ObjectHelper {

    public native static Element castToElement(final JavaScriptObject object)/*-{
     return object;
     }-*/;

    /**
     * Removes all the child Elements belonging to the given element.
     * 
     * @param parent
     */
    public static void removeChildren(final Element parent) {
        final int childCount = DOM.getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            DOM.removeChild(parent, DOM.getChild(parent, i));
        }
    }

    public static void checkTagName(final String name, final Element element, final String expectedTagName) {
        ObjectHelper.checkNotNull(name, element);
        StringHelper.checkNotEmpty(name, expectedTagName);

        if (false == isTag(element, expectedTagName)) {
            SystemHelper.handleAssertFailure(name, "The " + name + " is not of the expected tag type, expected["
                    + expectedTagName + "], got[" + getTagName(element) + "]");
        }
    }

    public static void checkInputElement(final String name, final Element element, final String type) {
        checkTagName(name, element, DomConstants.INPUT_TAG);
        final String actualType = DOM.getAttribute(element, DomConstants.INPUT_TAG_TYPE);
        if (false == type.equalsIgnoreCase(actualType)) {
            SystemHelper.handleAssertFailure("parameter:element",
                    "The input field parameter:element is not of the expected type, type[" + type + "], element: "
                            + toString(element));
        }
    }

    public static boolean compareTagNames(final String tagName, final String otherTagName) {
        StringHelper.checkNotNull("parameter:tagName", tagName);
        StringHelper.checkNotNull("parameter:otherTagName", otherTagName);

        return tagName.equalsIgnoreCase(otherTagName);
    }

    /**
     * TODO not yet implemented
     * 
     * @param element
     * @return
     */
    public static boolean isDisabled(final Element element) {
        throw new UnsupportedOperationException("DomHelper.isDisabled");
    }

    public static void setDisabled(final Element element, final boolean newDisabledFlag) {
        DOM.setAttribute(element, "disabled", newDisabledFlag ? "true" : "false");
    }

    /**
     * This method may be used to find the first child of the same tag type as specified by the parameter:childTagName. If none is found a
     * null is returned.
     * 
     * @param parent
     * @param childTagNameToFind
     * @return
     */
    public static Element findFirstChildOfType(final Element parent, final String childTagNameToFind) {
        ObjectHelper.checkNotNull("parameter:parent", parent);
        StringHelper.checkNotEmpty("parameter:childTagNameToFind", childTagNameToFind);

        Element found = null;
        final int childCount = DOM.getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            final Element child = DOM.getChild(parent, i);
            if (isTag(child, childTagNameToFind)) {
                found = child;
                break;
            }
        }

        return found;
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

    /**
     * Helper which tests if the given element is of the specified tag.
     * 
     * @param element
     * @param tagName
     * @return
     */
    public static boolean isTag(final Element element, final String tagName) {
        ObjectHelper.checkNotNull("parameter:element", element);
        StringHelper.checkNotEmpty("parameter:tagName", tagName);

        final String actualTagName = getTagName(element);
        return actualTagName == null ? false : compareTagNames(actualTagName, tagName);
    }

    public static String getTagName(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        final String tagName = DOM.getAttribute(element, DomConstants.TAG_NAME);
        return tagName;
    }

    /**
     * Creates a list and populates it with all immediate child elements of that are of the specificed tag
     * 
     * @param parent
     * @param childTagNameToFind
     * @return
     */
    public static List findAllChildrenOfType(final Element parent, final String childTagNameToFind) {
        ObjectHelper.checkNotNull("parameter:parent", parent);
        StringHelper.checkNotEmpty("parameter:childTagNameToFind", childTagNameToFind);

        final List found = new ArrayList();
        final int childCount = DOM.getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            final Element child = DOM.getChild(parent, i);
            if (isTag(child, childTagNameToFind)) {
                found.add(child);
            }
        }
        return found;
    }

    /**
     * Builds a string which contains the innerText of each and every element.
     * 
     * @param elements
     * @return
     */
    public static String innerText(final List elements) {
        ObjectHelper.checkNotNull("parameter:elements", elements);

        final StringBuffer text = new StringBuffer();

        final Iterator iterator = elements.iterator();
        while (iterator.hasNext()) {
            final Element element = (Element) iterator.next();

            text.append(DOM.getInnerText(element));
        }

        return text.toString();
    }

    /**
     * Convenient method which replaces all nbsp with a regular space.
     * 
     * @param text
     * @return
     */
    public static String changeNonBreakingSpaceToSpace(final String text) {
        return text.replaceAll("&nbsp;", " ");
    }

    // TODO Should probably be deprecated.
    final static String BACKGROUND_COLOUR_ATTRIBUTE = "backgroundColor";

    public static int getBackgroundColour(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        final String stringValue = DOM.getStyleAttribute(element, BACKGROUND_COLOUR_ATTRIBUTE);
        return stringValue == null ? 0 : 0xffffff & Integer.parseInt(stringValue.substring(1), 16);
    }

    public static void setBackgroundColour(final Element element, final int colour) {
        ObjectHelper.checkNotNull("parameter:element", element);

        DOM.setStyleAttribute(element, BACKGROUND_COLOUR_ATTRIBUTE, ColourHelper.toCssColour(colour));
    }

    public static void removeBackgroundColour(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        DOM.setStyleAttribute(element, BACKGROUND_COLOUR_ATTRIBUTE, "");
    };

    public static void handleUnableToSetProperty(final JavaScriptObject object, final String name) {
        DomHelper.handleAssertFailure("Unable to set property, name[" + name + "], element: " + object);
    }

    public static void handleUnableToGetProperty(final JavaScriptObject object, final String name) {
        DomHelper.handleAssertFailure("Property does not exist, name[" + name + "], element: " + object);
    }

    public static void handleUnableToSetCollectionItem(final JavaScriptObject collection, final int index) {
        DomHelper.handleAssertFailure("Unable to set collection item, collection: " + collection + ", index: " + index);
    }

    public static void handleUnableToRemoveCollectionItem(final JavaScriptObject collection, final int index) {
        DomHelper.handleAssertFailure("Unable to remove collection item, collection: " + collection + ", index: "
                + index);
    }

    public static native JavaScriptObject getStyleSheetsCollection()/*-{
     return $doc.styleSheets;
     }-*/;

    public static boolean hasProperty(final JavaScriptObject object, final String name) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        return hasProperty0(object, name);
    }

    /**
     * Tests if a particular property is present on the given object.
     * 
     * @param object
     * @param name
     * @return
     */
    protected static native boolean hasProperty0(final JavaScriptObject object, final String name)/*-{
     var value = object[ name ];
     
     // return true if value is not UNDEFINED or NULL
     return !( typeof( value ) == "undefined" || typeof( value ) == "object" && ! value ); 
     }-*/;

    public static String getProperty(final JavaScriptObject object, final String name) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        return getProperty0(object, name);
    }

    protected static native String getProperty0(final JavaScriptObject object, final String name)/*-{
     var value = object[ name ];
     if( typeof( value ) == "undefined" || typeof( value ) == "object" && ! value ){
     @rocket.client.dom.DomHelper::handleUnableToGetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
     }
     return value;
     }-*/;

    /**
     * Retrieves the value for the given property.
     * 
     * @param object
     * @param name
     * @return May return null if the property value is null.
     */
    public static JavaScriptObject getPropertyAsJavaScriptObject(final JavaScriptObject object, final String name) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        return getPropertyAsJavaScriptObject0(object, name);
    }

    protected static native JavaScriptObject getPropertyAsJavaScriptObject0(final JavaScriptObject object,
            final String name)/*-{
     var value = object[ name ];
     
     // value shouldnt be undefined or null.
     if( typeof( value ) == "undefined" || ( typeof( value ) == "object" && ! value )){
     @rocket.client.dom.DomHelper::handleUnableToGetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
     }
     return value;
     }-*/;

    public static boolean getBooleanProperty(final JavaScriptObject object, final String name) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        return getBooleanProperty0(object, name);
    }

    protected native static boolean getBooleanProperty0(final JavaScriptObject object, final String name)/*-{
     var value = object[ name ];
     if( typeof( value ) == "undefined" || ( typeof( value ) == "object" && ! value )){
     @rocket.client.dom.DomHelper::handleUnableToGetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
     }
     return value;
     }-*/;

    public static int getIntProperty(final JavaScriptObject object, final String name) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        return getIntProperty0(object, name);
    }

    public native static int getIntProperty0(final JavaScriptObject object, final String name)/*-{
     var value = object[ name ];
     if( typeof( value ) == "undefined" || ( typeof( value ) == "object" && ! value )){
     @rocket.client.dom.DomHelper::handleUnableToGetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
     }
     return value;
     }-*/;

    public static void setProperty(final JavaScriptObject object, final String name, final String value) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        setProperty0(object, name, value);
    }

    protected static native String setProperty0(final JavaScriptObject object, final String name, final String value)/*-{
     var previousValue = object[ name ];
     object[ name ] = value;
     }-*/;

    public static void setProperty(final JavaScriptObject object, final String name, final boolean booleanValue) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        setProperty0(object, name, booleanValue);
    }

    protected static native void setProperty0(final JavaScriptObject object, final String name,
            final boolean booleanValue)/*-{
     var previousValue = object[ name ];
     object[ name ] = booleanValue;
     }-*/;

    public static void setProperty(final JavaScriptObject object, final String name, final int intValue) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        setProperty0(object, name, intValue);
    }

    protected static native void setProperty0(final JavaScriptObject object, final String name, final int intValue)/*-{
     var previousValue = object[ name ];
     object[ name ] = intValue;
     }-*/;

    public static void setProperty(final JavaScriptObject object, final String name, final JavaScriptObject value) {
        ObjectHelper.checkNotNull("parameter:object", object);
        StringHelper.checkNotEmpty("parameter:name", name);
        setProperty0(object, name, value);
    }

    protected static native void setProperty0(final JavaScriptObject object, final String name,
            final JavaScriptObject value)/*-{
     var previousValue = object[ name ];
     object[ name ] = value;
     }-*/;

    /**
     * Requests the browser to set focus on the given element.
     * 
     * @param focusElement
     *            the element to receive focus.
     */
    public static void setFocus(final Element focusElement) {
        ObjectHelper.checkNotNull("paraemter:focusElement", focusElement);

        setFocus0(focusElement);
    }

    public native static void setFocus0(final Element element)/*-{
     if( element.focus ){
     element.focus();
     };
     }-*/;

    public static String toString(Element element) {
        return element == null ? "null" : DOM.getAttribute(element, "OuterHTML");
    }

    /**
     * Retrieve the absolute left or x coordinates for the given element
     * 
     * @param element
     * @return
     */
    public static int getAbsoluteLeft(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return getAbsoluteLeft0(element) + BrowserHelper.getScrollX();
    }

    protected static native int getAbsoluteLeft0(final Element element) /*-{
     var left = 0;
     while (element) {
     left += element.offsetLeft - element.scrollLeft;
     element = element.offsetParent;
     }
     return left;
     }-*/;

    /**
     * Retrieve the absolute top or y coordinates for the given element.
     * 
     * @param element
     * @return
     */
    public static int getAbsoluteTop(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return getAbsoluteTop0(element) + BrowserHelper.getScrollY();
    }

    protected static native int getAbsoluteTop0(final Element element) /*-{
     var top = 0;
     while (element) {
     top += element.offsetTop - element.scrollTop;
     element = element.offsetParent;
     }
     return top;
     }-*/;

    /**
     * Retrieves the relative x/left coordinates of the given element relative to its parent container element. This is particularly useful
     * if one wishes to absolutely position a widget having added it to the dom.
     * 
     * @param element
     * @return The pixel value
     */
    public static int getParentContainerLeft(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return getParentContainerLeft0(element);
    }

    protected static native int getParentContainerLeft0(final Element element) /*-{
     var left = 0;
     var element0 = element;
     while( element0 ){
     // stop if this element is absolutely positioned.
     if( "absolute" == element0.style[ "position" ].toLowerCase() ){
     break;
     }
     left = left + element0[ "offsetLeft" ];
     element0 = element0[ "offsetParent" ];
     }
     return left;
     }-*/;

    /**
     * Retrieves the relative y/top coordinates of the given element relative to its parent container element. This is particularly useful
     * if one wishes to absolutely position a widget having added it to the dom.
     * 
     * @param element
     * @return The pixel value
     */
    public static int getParentContainerTop(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return getParentContainerTop0(element);
    }

    protected static native int getParentContainerTop0(final Element element) /*-{
     var top = 0;
     var element0 = element;
     while( element0 ){
     // stop if this element is absolutely positioned. 
     if( "absolute" == element0.style[ "position" ].toLowerCase() ){
     break;
     }
     top = top + element0[ "offsetTop" ];
     element0 = element0[ "offsetParent" ];
     }
     return top;
     }-*/;

    /**
     * Performs two tasks positioning the given element absolutely and also setting its x/y coordinates.
     * 
     * @param element
     * @param x
     * @param y
     */
    public static void setAbsolutePosition(final Element element, final int x, final int y) {
        ObjectHelper.checkNotNull("parameter:element", element);
        DOM.setStyleAttribute(element, StyleConstants.CSS_POSITION, "absolute");
        DOM.setStyleAttribute(element, StyleConstants.CSS_LEFT, x + "px");
        DOM.setStyleAttribute(element, StyleConstants.CSS_TOP, y + "px");
    }

    /**
     * This method may be used to retrieve the toElement for a mouseout event. The FF implementation was reading the wrong event property
     * resulting in null always being returned.
     * 
     * @param event
     * @return
     */
    public static Element eventGetToElement(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);
        final int type = DOM.eventGetType(event);
        if (Event.ONMOUSEOUT != type) {
            DomHelper
                    .handleAssertFailure("DomHelper.eventGetToElement() may only be called on mouseOut events and not "
                            + DOM.eventGetTypeString(event) + " events.");
        }
        return BrowserHelper.isFireFox() ? fireFoxEventGetToElement(event) : DOM.eventGetToElement(event);
    }

    protected native static Element fireFoxEventGetToElement(final Event event)/*-{
     var target = event.relatedTarget;	 		
     return target ? target : null;
     }-*/;
}