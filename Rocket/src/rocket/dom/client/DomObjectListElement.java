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
package rocket.dom.client;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Convenient base class for any element belonging to a DomObjectList.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class DomObjectListElement implements Destroyable {

    public DomObjectListElement() {
        super();
    }

    public void destroy() {
        if (this.hasParent()) {
            this.getParent().getWrappers().set(this.getIndex(), null);
        }
        this.clearParent();
        this.clearIndex();
    }

    /**
     * Retrieves the native object at the given slot within the list.
     */
    public JavaScriptObject getObject() {
        return ObjectHelper.getObject(this.getParent().getObject(), this.getIndex());
    }

    public boolean hasObject() {
        return this.hasParent() && this.hasIndex();
    }

    /**
     * The parent list that contains this element.
     */
    private DomObjectList parent;

    public DomObjectList getParent() {
        ObjectHelper.checkNotNull("field:parent", parent);
        return this.parent;
    }

    public boolean hasParent() {
        return null != this.parent;
    }

    public void setParent(final DomObjectList parent) {
        ObjectHelper.checkNotNull("parameter:parent", parent);
        this.parent = parent;
    }

    public void clearParent() {
        this.parent = null;
    }

    /**
     * The index of this item within the list.
     */
    private int index;

    public int getIndex() {
        return index;
    }

    public boolean hasIndex() {
        return -1 != index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public void clearIndex() {
        this.index = -1;
    }
}
