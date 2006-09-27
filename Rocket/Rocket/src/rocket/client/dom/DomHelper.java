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

import rocket.client.util.ColourHelper;
import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A collection of useful methods relating to manipulating the DOM
 * @author Miroslav Pokorny (mP)
 */
public class DomHelper extends ObjectHelper {

	public native static Element castToElement( final Object object )/*-{
	 return object;
	 }-*/;

    /**
     * Removes all the child Elements belonging to the given element.
     * @param parent
     */
    public static void removeChildren( final Element parent ){
        final int childCount = DOM.getChildCount( parent );
        for( int i = 0; i < childCount; i++ ){
            DOM.removeChild( parent, DOM.getChild( parent, i ));
        }
    }

    public static void checkTagName(final String name, final Element element, final String expectedTagName) {
        ObjectHelper.checkNotNull(name, element);
        StringHelper.checkNotEmpty(name, expectedTagName);

        if ( false == isTag( element, expectedTagName)){
            SystemHelper.handleAssertFailure(name, "The " + name + " is not of the expected tag type, expected["
                    + expectedTagName + "], got[" + getTagName( element ) + "]");
        }
    }

    public static void checkInputElement(final String name, final Element element, final String type ) {
    	checkTagName( name, element, DomConstants.INPUT_TAG );
    	final String actualType = DOM.getAttribute( element, DomConstants.INPUT_TAG_TYPE );
    	if( false == type.equalsIgnoreCase( actualType )){
    		SystemHelper.handleAssertFailure( "parameter:element", "The input field parameter:element is not of the expected type, type[" + type + "], element: " + toString( element ));
    	}
    }

    public static boolean compareTagNames( final String tagName, final String otherTagName ){
    	StringHelper.checkNotNull( "parameter:tagName", tagName );
    	StringHelper.checkNotNull( "parameter:otherTagName", otherTagName );

    	return tagName.equalsIgnoreCase( otherTagName );
    }

    /**
     * TODO not yet implemented
     * @param element
     * @return
     */
    public static boolean isDisabled( final Element element ){
    	throw new UnsupportedOperationException( "DomHelper.isDisabled");
    }

    public static void setDisabled(final Element element, final boolean newDisabledFlag ) {
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
	 * Handy method for retrieving any style property value in a browser independent manner. Has been tested against IE 6.0 and Firefox 1.5.
	 *
	 * @param element
	 * @param stylePropertyName
	 * @return
	 */
	public static native String getCurrentStyleProperty(final Element element, final String stylePropertyName)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)("parameter:element", element );
	 @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)("parameter:stylePropertyName", stylePropertyName);

	 var value = null;
	 if( $wnd.getComputedStyle ) {
	 value = $wnd.getComputedStyle(element,null)[ stylePropertyName ];
	 } else if( element.currentStyle ) {
	 value = element.currentStyle[ stylePropertyName ];
	 }

	 return value;
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

        final String actualTagName = getTagName( element );
        return actualTagName == null ? false : compareTagNames( actualTagName, tagName );
    }

    public static String getTagName( final Element element ){
        ObjectHelper.checkNotNull("parameter:element", element);
        final String tagName = DOM.getAttribute(element, DomConstants.TAG_NAME );
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

    final static String BACKGROUND_COLOUR_ATTRIBUTE = "backgroundColor";

    public static int getBackgroundColour( final Element element ){
        ObjectHelper.checkNotNull( "parameter:element", element );

        final String stringValue = DOM.getStyleAttribute( element, BACKGROUND_COLOUR_ATTRIBUTE );
        return stringValue == null ? 0 : 0xffffff & Integer.parseInt( stringValue.substring( 1 ), 16 );
    }

    public static void setBackgroundColour( final Element element, final int colour ){
        ObjectHelper.checkNotNull( "parameter:element", element );

        DOM.setStyleAttribute( element, BACKGROUND_COLOUR_ATTRIBUTE, ColourHelper.toCssColour( colour ));
    }

    public static void removeBackgroundColour( final Element element ){
    	ObjectHelper.checkNotNull( "parameter:element", element );
    	DOM.setStyleAttribute( element, BACKGROUND_COLOUR_ATTRIBUTE, "");
    };

	public static void handleUnableToSetProperty(
			final JavaScriptObject element, final String name) {
		DomHelper.handleAssertFailure("Unable to set property, name["
				+ name + "], element: " + element);
	}

	public static void handleUnableToGetProperty(
			final JavaScriptObject element, final String name) {
		DomHelper.handleAssertFailure("Property does not exist, name["
				+ name + "], element: " + element);
	}

	public static void handleUnableToSetCollectionItem(
			final JavaScriptObject collection, final int index) {
		DomHelper
				.handleAssertFailure("Unable to set collection item, collection: "
						+ collection + ", index: " + index);
	}

	public static void handleUnableToRemoveCollectionItem(
			final JavaScriptObject collection, final int index) {
		DomHelper
				.handleAssertFailure("Unable to remove collection item, collection: "
						+ collection + ", index: " + index);
	}

	public static native JavaScriptObject getStyleSheetsCollection()/*-{
	 return $doc.styleSheets;
	 }-*/;

	public static native boolean hasProperty(final JavaScriptObject object,
			final String name)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:name", object);
	 @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

	 return typeof( object[ name ] ) != "undefined";
	 }-*/;

	public static native String getProperty(final JavaScriptObject object,
			final String name)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:name", object);
	 @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

	 var value = object[ name ];
	 if( typeof( value ) == "undefined" ){
	 @rocket.client.dom.DomHelper::handleUnableToGetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
	 }
	 return value;
	 }-*/;

    public static native JavaScriptObject getPropertyAsJavaScriptObject(final JavaScriptObject object,
            final String name)/*-{
     @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:name", object);
     @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

     var value = object[ name ];
     if( typeof( value ) == "undefined" ){
     @rocket.client.dom.DomHelper::handleUnableToGetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
     }
     return value;
     }-*/;

	public native static boolean getBooleanProperty(
			final JavaScriptObject object, final String name)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:name", object);
	 @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

	 var value = object[ name ];
	 if( typeof( value ) == "undefined" ){
	 @rocket.client.dom.DomHelper::handleUnableToGetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
	 }
	 return value;
	 }-*/;

	public native static int getIntProperty(final JavaScriptObject object,
			final String name)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:name", object);
	 @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

	 var value = object[ name ];
	 if( typeof( value ) == "undefined" ){
	 @rocket.client.dom.DomHelper::handleUnableToGetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
	 }
	 return value;
	 }-*/;

	public static native String setProperty(final JavaScriptObject object,
			final String name, final String value)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:object", object);
	 @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

	 var previousValue = object[ name ];
	 object[ name ] = value;

	 // set failed - must be read only property...
	 if( object[ name ] != value ){
	 @rocket.client.dom.DomHelper::handleUnableToSetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
	 }
	 }-*/;

	public static native void setProperty(final JavaScriptObject object,
			final String name, final boolean booleanValue)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:object", object);
	 @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

	 var previousValue = object[ name ];
	 object[ name ] = booleanValue;

	 // set failed - must be read only property...
	 if( object[ name ] != booleanValue ){
	 @rocket.client.dom.DomHelper::handleUnableToSetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
	 }
	 }-*/;

	public static native void setProperty(final JavaScriptObject object,
			final String name, final int intValue)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:object", object);
	 @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

	 var previousValue = object[ name ];
	 object[ name ] = intValue;

	 // set failed - must be read only property...
	 if( object[ name ] != intValue ){
	 @rocket.client.dom.DomHelper::handleUnableToSetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
	 }
	 }-*/;

    public static native void setProperty(final JavaScriptObject object,
            final String name, final JavaScriptObject value )/*-{
     @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:object", object);
     @rocket.client.util.StringHelper::checkNotEmpty(Ljava/lang/String;Ljava/lang/String;)( "parameter:name", name);

     var previousValue = object[ name ];
     object[ name ] = value;

     // set failed - must be read only property...
     if( object[ name ] != value ){
     @rocket.client.dom.DomHelper::handleUnableToSetProperty(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(object,name);
     }
     }-*/;

	/**
	 * Requests the browser to set focus on the given element.
	 * @param focusElement the element to receive focus.
	 */
	public static void setFocus( final Element focusElement ){
		ObjectHelper.checkNotNull("paraemter:focusElement", focusElement );

		setFocus0( focusElement );
	}
	public native static void setFocus0( final Element element )/*-{
	 if( element.focus ){
	 	element.focus();
	 };
	 }-*/;

    public static String toString( Element element ){
        return element == null ? "null" : DOM.getAttribute( element, "OuterHTML");
    }
}