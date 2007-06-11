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

import com.google.gwt.user.client.Element;

/**
 * The Selection class is a singleton that represents any selection made by the user typically done with the mouse.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Selection {

    Selection() {
        super();
    }

    public SelectionEndPoint getStart() {
        return SelectionHelper.getStart();
    }

    public void setStart(final SelectionEndPoint start) {
        SelectionHelper.setStart(start);
    }

    public SelectionEndPoint getEnd() {
        return SelectionHelper.getEnd();
    }

    public void setEnd(final SelectionEndPoint end) {
        SelectionHelper.setEnd(end);
    }

    /**
     * Tests if anything is currently being selected
     * 
     * @return
     */
    public boolean isEmpty() {
        return SelectionHelper.isEmpty();
    }

    /**
     * Clears any current selection.
     */
    public void clear() {
        SelectionHelper.clear();
    }

    /**
     * Extracts the selection and moves it to become the only child of a new element.
     * 
     * If not selection is active the returned element will have no child / its innerHTML property will be an empty String.
     */
    public Element extract() {
        return SelectionHelper.extract();
    }

    /**
     * Inserts the given element into the dom so that it is a child of the given element and yet contains the selected area.
     * 
     * This class includes a guard to ensure that a selection exists if not an exception is thrown.
     * 
     * @param element
     */
    public void surround(final Element element) {
        SelectionHelper.surroundSelection(element);
    }

    /**
     * Delets the selection's content from the document.
     */
    public void delete() {
        SelectionHelper.delete();
    }
}
