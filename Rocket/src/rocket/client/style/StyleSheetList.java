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

import rocket.client.dom.DomObjectList;
import rocket.client.dom.DomObjectListElement;

/**
 * Provides a list view of a native StyleSheet list.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheetList extends DomObjectList {

    protected StyleSheetList() {
        super();
    }

    // DOM OBJECT LIST :::::::::::::::::::::::::::::::::::::::::::::::::

    protected DomObjectListElement createWrapper(final int index) {
        final StyleSheet styleSheet = new StyleSheet();
        styleSheet.setParent(this);
        styleSheet.setIndex(index);
        return styleSheet;
    }

    protected boolean isValidType(Object wrapper) {
        return wrapper instanceof StyleSheet;
    }

    protected void adopt(final DomObjectListElement object) {
    }

    protected void disown(final DomObjectListElement object) {
    }

    public boolean add(final Object element) {
        throw new UnsupportedOperationException("A StyleSheetList is read only.");
    }

    public void add(final int index, final Object element) {
        throw new UnsupportedOperationException("A StyleSheetList is read only.");
    }

    public Object remove(final int index) {
        throw new UnsupportedOperationException("A StyleSheetList is read only.");
    }

    public boolean remove(final Object object) {
        throw new UnsupportedOperationException("A StyleSheetList is read only.");
    }

    public Object set(final int index, final Object element) {
        throw new UnsupportedOperationException("A StyleSheetList is read only.");
    }

    protected void add0(final DomObjectListElement element) {
        throw new UnsupportedOperationException();
    }

    protected void insert0(final int index, final DomObjectListElement element) {
        throw new UnsupportedOperationException();
    }

    protected void remove0(final DomObjectListElement element) {
        throw new UnsupportedOperationException();
    }
}
