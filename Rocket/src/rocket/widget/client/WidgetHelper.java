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
package rocket.widget.client;

import java.util.Iterator;
import java.util.Map;

import rocket.dom.client.Dom;
import rocket.dom.client.DomConstants;
import rocket.util.client.Colour;
import rocket.util.client.Destroyable;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * This Helper contains a number of useful methods related to working with GWT
 * widgets and the browser in general.
 * 
 * This helper also contains a number of factories for creating various google
 * widgets and should be used as they attempt to fix various issues/bugs within
 * GWT. Sometimes this is as simple as setting a default styleName.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WidgetHelper extends SystemHelper {

	/**
	 * Given an element attempts to find which widget it is a child of. This is
	 * particularly useful when a panel contains many widgets which in
	 * themselves are made up of many elements and one needs to determine which
	 * widget the event belongs too.
	 * 
	 * @param target
	 * @param widgets
	 * @return The widget or null if a match was not possible.
	 */
	public static Widget findWidget(final Element target, final Iterator widgets) {
		ObjectHelper.checkNotNull("parameter:target", target);
		ObjectHelper.checkNotNull("parameter:widgets", widgets);

		Widget widget = null;
		while (widgets.hasNext()) {
			final Widget otherWidget = (Widget) widgets.next();
			if (DOM.isOrHasChild(otherWidget.getElement(), target)) {
				widget = otherWidget;
				break;
			}
		}
		return widget;
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
	public static void populateMapFromForm(final Map map, final Element form) {
		ObjectHelper.checkNotNull("parameter:map", map);
		ObjectHelper.checkNotNull("parameter:form", form);

		final Iterator formElements = WidgetHelper.getFormElements(form);
		while (formElements.hasNext()) {
			final Element formElement = (Element) formElements.next();
			final String name = DOM.getElementProperty(formElement, DomConstants.NAME);
			if (null == name) {
				continue;
			}
			final String value = WidgetHelper.getFormSubmitValue(formElement);

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
	public static String urlEncodeForm(final Element form) {
		ObjectHelper.checkNotNull("parameter:form", form);

		final StringBuffer urlEncoded = new StringBuffer();
		boolean addSeparator = false;

		final Iterator formElements = WidgetHelper.getFormElements(form);
		while (formElements.hasNext()) {
			if (addSeparator) {
				urlEncoded.append('&');
			}

			final Element formElement = (Element) formElements.next();
			final String name = ObjectHelper.getString(ObjectHelper.castFromElement(formElement), DomConstants.NAME);
			final String value = URL.encodeComponent(WidgetHelper.getFormSubmitValue(formElement));
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
		ObjectHelper.checkNotNull("parameter:element", element);

		String value = null;
		while (true) {
			if (Dom.isTag(element, WidgetConstants.LISTBOX_TAG)) {
				value = DOM.getElementProperty(element, DomConstants.VALUE);
				break;
			}
			if (Dom.isTag(element, WidgetConstants.TEXTAREA_TAG)) {
				value = DOM.getInnerText(element);
				break;
			}

			if (Dom.isTag(element, DomConstants.INPUT_TAG)) {
				value = DOM.getElementProperty(element, DomConstants.VALUE);
				break;
			}

			throw new UnsupportedOperationException("Cannot get the formSubmitValue for the element, element: " + DOM.toString(element));
		}

		return value;
	}

	/**
	 * Returns an iterator which may be used to visit all the elements for a
	 * particular form. Because the form.elements collection cannot have
	 * elements added / removed this iterator is read only(aka the remove() )
	 * doesnt work. The iterator is also not fail safe.
	 * 
	 * @param form
	 * @return
	 */
	public static Iterator getFormElements(final Element form) {
		Dom.checkTagName("parameter:form", form, DomConstants.FORM_TAG);

		final FormElementsIterator iterator = new FormElementsIterator();
		iterator.setForm(form);
		return iterator;
	}

	/**
	 * This iterator also implements Destroyable. This faciliates allowing the
	 * user to cleanup once the iterator has been used/exhausted.
	 */
	static class FormElementsIterator implements Iterator, Destroyable {

		public boolean hasNext() {
			return this.getCursor() < DOM.getElementPropertyInt(this.getForm(), DomConstants.LENGTH_PROPERTY);
		}

		public Object next() {
			final int cursor = this.getCursor();
			final Object object = this.next0(this.getForm(), cursor);
			this.setCursor(cursor + 1);
			return object;
		}

		native private Element next0(final Element form, final int index)/*-{
		 var element = form.elements[ index ];
		 return element ? element : null;
		 }-*/;

		public void remove() {
			throw new UnsupportedOperationException("Form elements may not be removed using this iterator. this: " + this);
		}

		public void destroy() {
			this.clearForm();
		}

		Element form;

		Element getForm() {
			return form;
		}

		void setForm(final Element form) {
			this.form = form;
		}

		void clearForm() {
			this.form = null;
		}

		int cursor = 0;

		int getCursor() {
			return cursor;
		}

		void setCursor(final int cursor) {
			this.cursor = cursor;
		}
	}

	/**
	 * Helper which attempts to find and fetch an element belonging to the given
	 * form by name.
	 * 
	 * @param form
	 * @param elementName
	 * @return
	 */
	public static Element findElement(final Element form, final String elementName) {
		ObjectHelper.checkNotNull("parameter:form", form);
		StringHelper.checkNotEmpty("parameter:elementName", elementName);

		return findElement0(form, elementName);
	}

	native private static Element findElement0(final Element form, final String elementName)/*-{
	 var element = null;

	 element = form.elements[ elementName ];
	 return element ? element : null;}-*/;
	
	/**
	 * Helper used by CompositePanel to invoke the non visible Widget.setParent() method.
	 * @param widget
	 * @param panel
	 */
	static native void widgetSetParent( final com.google.gwt.user.client.ui.Widget widget, final com.google.gwt.user.client.ui.Panel panel )/*-{
	widget.@com.google.gwt.user.client.ui.Widget::setParent(Lcom/google/gwt/user/client/ui/Widget;)(panel);
 }-*/;

}