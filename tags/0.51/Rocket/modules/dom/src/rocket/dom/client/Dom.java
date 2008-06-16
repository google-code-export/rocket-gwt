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

import java.util.Map;

import rocket.util.client.Checker;
import rocket.util.client.JavaScript;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.NodeCollection;
import com.google.gwt.http.client.URL;

/**
 * A collection of useful methods relating to manipulating the DOM
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Dom {

	/**
	 * Convenience method that removes an element from its parent.
	 * 
	 * @param element
	 */
	public static void removeFromParent(final Element element) {
		element.getParentNode().removeChild(element);
	}

	/**
	 * Retrieves the container element which contains this child element. This
	 * is particularly useful when calculating coordinates for positioned
	 * element.
	 * 
	 * @param element
	 * @return
	 */
	public static Element getContainer(final Element element) {
		Checker.notNull("parameter:element", element);
		return Dom.getContainer0(element);
	}

	native static private Element getContainer0(final Element element)/*-{
		 var container = null;
		 var element0 = element;
		 while( element0 ){
		 // stop if this element is absolutely/relative positioned. 
		 var position = element0.style.position.toLowerCase();
		 if( "absolute" == position || "relative" == position ){
		 container = element0;
		 break;
		 }             
		 element0 = element0.offsetParent;
		 }
		 return container;    
		 }-*/;

	/**
	 * Retrieves the relative x/left coordinates of the given element relative
	 * to its parent container element. This is particularly useful if one
	 * wishes to absolutely position a widget having added it to the dom.
	 * 
	 * @param element
	 * @return The pixel value
	 */
	public static int getContainerLeftOffset(final Element element) {
		Checker.notNull("parameter:element", element);
		return getContainerLeftOffset0(element);
	}

	native static private int getContainerLeftOffset0(final Element element) /*-{
	 var left = 0;
	 var element0 = element;
	 
	 while( element0 ){ 
	 var position = element.style.position;
	 if( "absolute" == position || "relative" == position ){
	 break;
	 }		
	 
	 left = left + element0.offsetLeft;
	 element0 = element0.offsetParent;  
	 }
	 
	 return left;	 
	 }-*/;

	/**
	 * Retrieves the relative y/top coordinates of the given element relative to
	 * its parent container element. This is particularly useful if one wishes
	 * to absolutely position a widget having added it to the dom.
	 * 
	 * @param element
	 * @return The pixel value
	 */
	public static int getContainerTopOffset(final Element element) {
		Checker.notNull("parameter:element", element);
		return getContainerTopOffset0(element);
	}

	native static private int getContainerTopOffset0(final Element element) /*-{
	 var top = 0;
	 var element0 = element;
	 
	 while( element0 ){ 
	 var position = element.style.position;
	 if( "absolute" == position || "relative" == position ){
	 break;
	 }		
	 
	 top = top + element0.offsetTop;
	 element0 = element0.offsetParent;  
	 }
	 
	 return top;	 
	 }-*/;

	/**
	 * Helper which tests if the given element is of the specified tag.
	 * 
	 * @param element
	 * @param tagName
	 * @return
	 */
	public static boolean isTag(final Element element, final String tagName) {
		Checker.notNull("parameter:element", element);
		Checker.notEmpty("parameter:tagName", tagName);

		final String actualTagName = element.getTagName();
		return actualTagName == null ? false : compareTagNames(actualTagName, tagName);
	}

	public static void checkTagName(final String name, final Element element, final String expectedTagName) {
		Checker.notNull(name, element);
		Checker.notEmpty(name, expectedTagName);

		if (false == isTag(element, expectedTagName)) {
			Checker.fail(name, "The " + name + " is not of the expected tag type, expected \"" + expectedTagName + "\", but got \""
					+ element.getTagName() + "\".");
		}
	}

	public static void checkInput(final String name, final Element element, final String type) {
		if (false == isInput(element, type)) {
			String message = name;
			if (message.startsWith("parameter:") || message.startsWith("field:")) {
				message = "The element which is a " + element.getTagName() + " is not an input with a type of \"" + type + "\".";
			}

			Checker.fail(name, message);
		}
	}

	/**
	 * Tests if the given element is an INPUT tag of the requested type.
	 * 
	 * @param element
	 *            The element being tested.
	 * @param type
	 *            The type attribute
	 * @return True if the element is the specified INPUT tag.
	 */
	public static boolean isInput(final Element element, final String type) {
		Checker.notNull("parameter:element", element);
		Checker.notEmpty("parameter:type", type);

		boolean is = false;
		while (true) {
			if (false == isTag(element, DomConstants.INPUT_TAG)) {
				is = false;
				break;
			}

			final String actualType = element.getAttribute(DomConstants.INPUT_TAG_TYPE);
			is = type.equalsIgnoreCase(actualType);
			break;
		}
		return is;
	}

	public static boolean compareTagNames(final String tagName, final String otherTagName) {
		Checker.notNull("parameter:tagName", tagName);
		Checker.notNull("parameter:otherTagName", otherTagName);

		return tagName.equalsIgnoreCase(otherTagName);
	}

	/**
	 * Retrieves the client width of the given element. This is width in pixels
	 * less any decorations such as border or margins.
	 * 
	 * @param element
	 * @return The value in pixels
	 */
	public static int getClientWidth(final Element element) {
		return JavaScript.getInteger(element, "clientWidth");
	}

	/**
	 * Retrieves the client height of the given element. This is height in
	 * pixels less any decorations such as border or margins.
	 * 
	 * @param element
	 * @return The value in pixels
	 */
	public static int getClientHeight(final Element element) {
		return JavaScript.getInteger(element, "clientHeight");
	}

	/**
	 * Populates the given map with the values of the elements belonging to the
	 * given form. The element name becomes the key and teh value the entry
	 * value.
	 * 
	 * FormElements without a name are skipped.
	 * 
	 * @param map
	 *            The destination map
	 * @param form
	 *            The form containing the elements.
	 */
	public static void populateMapFromForm(final Map<String, String> map, final FormElement form) {
		Checker.notNull("parameter:map", map);
		Checker.notNull("parameter:form", form);

		final NodeCollection<Element> formElements = form.getElements();
		final int count = formElements.getLength();
		for (int i = 0; i < count; i++) {
			final Element element = formElements.getItem(i);
			final String name = element.getAttribute(DomConstants.NAME);
			if (null == name) {
				continue;
			}

			final String value = Dom.getFormSubmitValue(element);
			map.put(name, value);
		}
	}

	/**
	 * Encodes all the elements belonging to form into a safe url encoded
	 * String.
	 * 
	 * @param form
	 * @return
	 */
	public static String urlEncodeForm(final FormElement form) {
		Checker.notNull("parameter:form", form);

		final StringBuffer urlEncoded = new StringBuffer();
		boolean addSeparator = false;

		final NodeCollection<Element> formElements = form.getElements();
		final int count = formElements.getLength();
		for (int i = 0; i < count; i++) {
			final Element element = formElements.getItem(i);
			final String name = element.getAttribute(DomConstants.NAME);
			if (null == name) {
				continue;
			}

			if (addSeparator) {
				urlEncoded.append('&');
			}

			final String value = URL.encodeComponent(Dom.getFormSubmitValue(element));
			urlEncoded.append(name);
			urlEncoded.append('=');
			urlEncoded.append(value);

			addSeparator = true;
		}

		return urlEncoded.toString();
	}

	/**
	 * This method is smart in that it tests the tag type of the given element
	 * and reads the appropriate attribute that contains the textual value of
	 * this element. This is the value that would have been submitted for this
	 * field if its parent form was submitted.
	 * 
	 * @param element
	 * @return
	 */
	public static String getFormSubmitValue(final Element element) {
		Checker.notNull("parameter:element", element);

		String value = null;
		while (true) {
			final String tagName = element.getTagName();
			if (DomConstants.LISTBOX_TAG.equals(tagName)) {
				value = element.getAttribute(DomConstants.VALUE);
				break;
			}
			if (DomConstants.TEXTAREA_TAG.equals(tagName)) {
				value = element.getInnerText();
				break;
			}

			if (DomConstants.INPUT_TAG.equals(tagName)) {
				value = element.getAttribute(DomConstants.VALUE);
				break;
			}

			throw new UnsupportedOperationException("Cannot get the formSubmitValue for the element, element: " + element.toString());
		}

		return value;
	}
}