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

import java.util.List;

import rocket.client.browser.BrowserHelper;
import rocket.client.dom.DomCollectionList;
import rocket.client.dom.DomHelper;
import rocket.client.util.ObjectHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a handle to a StyleSheetList collection.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheetsCollection extends DomCollectionList implements List {

    public StyleSheetsCollection() {
        super();

        this.setCollection(DomHelper.getStyleSheetsCollection());
    }

    protected Object createWrapper(final JavaScriptObject element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        final StyleSheet wrapper = new StyleSheet();
        wrapper.setObject(element);
        return wrapper;
    }

    protected void checkElementType(final Object wrapper) {
        ObjectHelper.checkNotNull("parameter:wrapper", wrapper);
        if (false == (wrapper instanceof StyleSheet)) {
            BrowserHelper.handleAssertFailure("parameter:wrapper",
                    "All elements of this List must be of StyleSheet and not elementType[" + GWT.getTypeName(wrapper));
        }
    }

    protected void add0(final JavaScriptObject collection, final JavaScriptObject element) {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + "add0()");
    }

    protected void insert0(final JavaScriptObject collection, final int index, final JavaScriptObject element) {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + "insert0()");
    }

    protected JavaScriptObject remove0(final JavaScriptObject collection, final int index) {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + "remove0()");
    }
}
