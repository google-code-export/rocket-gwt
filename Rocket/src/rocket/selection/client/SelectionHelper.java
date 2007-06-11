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

import rocket.selection.client.support.SelectionSupport;
import rocket.style.client.StyleConstants;
import rocket.style.client.StyleHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * A collection of methods related to working with selections typically made by the user selecting parts of the document using the mouse.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SelectionHelper {

    static Selection selection = new Selection();

    /**
     * Returns the Document Selection singleton
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
        return support;
    }

    /**
     * Updates the given element so that mouse selection is re-enabled.
     * 
     * @param element
     */
    static public void setEnabled(final Element element, final boolean enabled) {
        SelectionHelper.getSupport().setEnabled(element, enabled);
    }

    static public boolean isEnabled(final Element element) {
        return SelectionHelper.getSupport().isEnabled(element);
    }

    static public SelectionEndPoint getStart() {
        return SelectionHelper.getSupport().getStart();
    }

    static public void setStart(final SelectionEndPoint start) {
        SelectionHelper.getSupport().setStart(start);
    }

    static public SelectionEndPoint getEnd() {
        return SelectionHelper.getSupport().getEnd();
    }

    static public void setEnd(final SelectionEndPoint end) {
        SelectionHelper.getSupport().setEnd(end);
    }

    static public boolean isEmpty() {
        return SelectionHelper.getSupport().isEmpty();
    }

    static public void clear() {
        SelectionHelper.getSupport().clear();
    }

    /**
     * Moves the selection so that it now becomes the only child of the given element.
     * 
     * @return An element holding the selection.
     */
    static protected Element extract() {
        return SelectionHelper.getSupport().extract();
    }

    /**
     * Surrounds the current selection ( making its a child of the given element ).
     * 
     * @param element
     */
    static protected void surroundSelection(final Element element) {
        SelectionHelper.getSupport().surround(element);
    }

    static public void delete() {
        SelectionHelper.getSupport().delete();
    }

    /**
     * Clears or removes any current text selection.
     * 
     */
    public static void clearAnySelectedText() {
        SelectionHelper.getSupport().clearAnySelectedText();
    }

    /**
     * Disables text selection for this element and all its child elements.
     * 
     * It is not possible to re enable text selection for a child using {@link #enableTextSelection(Element)}
     * 
     * @param element
     */
    public static void disableTextSelection(final Element element) {
        StyleHelper.setInlineStyleProperty(element, StyleConstants.USER_SELECT, StyleConstants.USER_SELECT_DISABLED);
    }

    /**
     * Enables text selection for an element that was possibly previously disabled.
     * 
     * It is not possible to re enable text selection for a child if a ancestor has been disabled using
     * {@link #disableTextSelection(Element)}
     * 
     * @param element
     */
    public static void enableTextSelection(final Element element) {
        StyleHelper.setInlineStyleProperty(element, StyleConstants.USER_SELECT, StyleConstants.USER_SELECT_ENABLED);
    }
}
