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
package rocket.dom.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.browser.client.BrowserHelper;
import rocket.collection.client.CollectionHelper;
import rocket.style.client.StyleConstants;
import rocket.util.client.ColourHelper;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A collection of useful methods relating to manipulating the DOM
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DomHelper extends ObjectHelper {
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

    // CSS POSITIONING :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

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
     // stop if this element is absolutely/relative positioned. 
     var position = element0.style.position.toLowerCase();
     if( "absolute" == position || "relative" == position ){
     break;
     }
     left = left + element0.offsetLeft;
     element0 = element0.offsetParent;
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
     // stop if this element is absolutely/relative positioned. 
     var position = element0.style.position.toLowerCase();
     if( "absolute" == position || "relative" == position ){
     break;
     }
     top = top + element0.offsetTop;
     element0 = element0.offsetParent;
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
        DOM.setStyleAttribute(element, StyleConstants.POSITION, "absolute");
        DOM.setStyleAttribute(element, StyleConstants.LEFT, x + "px");
        DOM.setStyleAttribute(element, StyleConstants.TOP, y + "px");
    }

    // TAG NAME ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

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

    // VIEWS OF DOM COLLECTIONS ::::::::::::::::::::::::::::::::::::::::::::::::::::::

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
        return CollectionHelper.unmodifiableList(found);
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

    // TODO Should probably be deprecated when rocket.style.client.* is completed.
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

    // DEPRECATED ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Tests if an element is disabled.
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
     * Calls the destroy method on the given object if it is destroyable.
     * 
     * @param object
     */
    public static void destroyIfNecessary(final Object object) {
        if (object instanceof Destroyable) {
            final Destroyable destroyable = (Destroyable) object;
            destroyable.destroy();
        }
    }

    /**
     * Makes a clone of the given element.
     * 
     * @param element
     * @param deepCopy
     *            When true performs a deep copy (ie children are also cloned).
     * @return
     */
    public static Element cloneElement(final Element element, final boolean deepCopy) {
        DomHelper.checkNotNull("parameter:element", element);
        return cloneElement0(element, deepCopy);
    }

    native protected static Element cloneElement0(final Element element, final boolean deepCopy)/*-{
     return element.cloneNode( deepCopy );
     }-*/;

    native public static Element getBody()/*-{
     return $doc.body;
     }-*/;
}