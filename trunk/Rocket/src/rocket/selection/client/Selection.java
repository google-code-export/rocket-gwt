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
package rocket.selection.client;

import rocket.dom.client.Dom;
import rocket.selection.client.support.SelectionSupport;
import rocket.style.client.InlineStyle;
import rocket.style.client.Css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * The Selection class is a singleton that represents any selection made by the
 * user typically done with the mouse.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Selection {

	final static Selection selection = new Selection();

	/**
	 * Returns the document Selection singleton
	 * 
	 * @return
	 */
	static public Selection getSelection() {
		return selection;
	}

	/**
	 * The browser aware support that takes care of browser difference nasties.
	 */
	static private SelectionSupport support = (SelectionSupport) GWT.create(SelectionSupport.class);

	static SelectionSupport getSupport() {
		return Selection.support;
	}

	/**
	 * Disables text selection for this element and all its child elements.
	 * 
	 * It is not possible to re enable text selection for a child using
	 * {@link #enableTextSelection(Element)}
	 * 
	 * @param element
	 */
	public static void disableTextSelection(final Element element) {
		InlineStyle.setString(element, Css.USER_SELECT, Css.USER_SELECT_DISABLED);
	}

	public static void disableTextSelection() {
		disableTextSelection(Dom.getBody());
	}

	/**
	 * Enables text selection for an element that was possibly previously
	 * disabled.
	 * 
	 * It is not possible to re enable text selection for a child if a ancestor
	 * has been disabled using {@link #disableTextSelection(Element)}
	 * 
	 * @param element
	 */
	public static void enableTextSelection(final Element element) {
		InlineStyle.setString(element, Css.USER_SELECT, Css.USER_SELECT_ENABLED);
	}

	public static void enableTextSelection() {
		enableTextSelection(Dom.getBody());
	}

	/**
	 * Clears or removes any current text selection.
	 * 
	 */
	public static void clearAnySelectedText() {
		Selection.getSelection().clear();
	}

	/**
	 * Updates the given element so that mouse selection is re-enabled.
	 * 
	 * @param element
	 */
	static public void setEnabled(final Element element, final boolean enabled) {
		Selection.getSupport().setEnabled(element, enabled);
	}

	static public boolean isEnabled(final Element element) {
		return Selection.getSupport().isEnabled(element);
	}

	Selection() {
		super();
	}

	public SelectionEndPoint getStart() {
		return Selection.getSupport().getStart();
	}

	public void setStart(final SelectionEndPoint start) {
		Selection.getSupport().setStart(start);
	}

	public SelectionEndPoint getEnd() {
		return Selection.getSupport().getEnd();
	}

	public void setEnd(final SelectionEndPoint end) {
		Selection.getSupport().setEnd(end);
	}

	/**
	 * Tests if anything is currently being selected
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return Selection.getSupport().isEmpty();
	}

	/**
	 * Clears any current selection.
	 */
	public void clear() {
		Selection.getSupport().clear();
	}

	/**
	 * Extracts the selection and moves it to become the only child of a new
	 * element.
	 * 
	 * If not selection is active the returned element will have no child / its
	 * innerHTML property will be an empty String.
	 */
	public Element extract() {
		return Selection.getSupport().extract();
	}

	/**
	 * Inserts the given element into the dom so that it is a child of the given
	 * element and yet contains the selected area.
	 * 
	 * This class includes a guard to ensure that a selection exists if not an
	 * exception is thrown.
	 * 
	 * @param element
	 */
	public void surround(final Element element) {
		Selection.getSupport().surround(element);
	}

	/**
	 * Delets the selection's content from the document.
	 */
	public void delete() {
		Selection.getSupport().delete();
	}
}
