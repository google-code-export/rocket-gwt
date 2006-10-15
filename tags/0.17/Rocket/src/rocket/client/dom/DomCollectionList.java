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

import java.util.AbstractList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.client.collection.IteratorView;
import rocket.client.util.ObjectHelper;
import rocket.client.util.ObjectWrapper;
import rocket.client.util.PrimitiveHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * This list represents a list of DomElements. Rather than directly working with the actual DOM element. this list automatically wraps and
 * unwraps them as elements are set/getted. This allows a user to work with nice bean classes rather than nasty manipulation of an element
 * using DOM.setAttribute etc.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class DomCollectionList extends AbstractList {

    protected DomCollectionList() {
        super();

        this.createWrappers();
    }

    // WRAP / UNWRAP METHODS
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This map maintains an association between elements and wrappers
     */
    private Map wrappers;

    protected Map getWrappers() {
        ObjectHelper.checkNotNull("field:wrappers", wrappers);
        return wrappers;
    }

    protected void setWrappers(final Map wrappers) {
        ObjectHelper.checkNotNull("parameter:wrappers", wrappers);
        this.wrappers = wrappers;
    }

    protected void createWrappers() {
        this.setWrappers(new HashMap());
    }

    /**
     * Factory method which sub-classes must create the wrapper for the given element.
     * 
     * The list maintains a cache of wrappers and their corresponding DOM node only invoking this method when it does not have a previously
     * created wrapper in its cache.
     * 
     * @param element
     * @return
     */
    protected abstract Object createWrapper(final JavaScriptObject element);

    protected JavaScriptObject unwrapWrapper(final ObjectWrapper wrapper) {
        ObjectHelper.checkNotNull("parameter:wrapper", wrapper);

        final ElementWrapper wrapper0 = (ElementWrapper) wrapper;
        return wrapper0.getElement();
    }

    protected abstract void checkElementType(final Object wrapper);

    // LIST::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public int size() {
        return DomHelper.getIntProperty(this.getCollection(), "length");
    }

    public Object get(final int index) {
        PrimitiveHelper.checkBetween("parameter:index", index, 0, size());

        Object wrapper = null;
        final JavaScriptObject element = this.get0(this.getCollection(), index);
        if (null != element) {
            wrapper = this.getWrappers().get(element);

            // cache does not contain wrapper - need to create & save wrapper
            if (null == wrapper) {
                wrapper = this.createWrapper(element);
                this.getWrappers().put(element, wrapper);
            }
        }
        return wrapper;
    }

    protected native JavaScriptObject get0(final JavaScriptObject collection, final int index)/*-{
     return collection[ index ];
     }-*/;

    public Object set(final int index, final Object object) {
        this.checkElementType(object);

        final ObjectWrapper wrapper = (ObjectWrapper) object;
        final JavaScriptObject wrappedObject = wrapper.getObject();

        final Map wrappers = this.getWrappers();
        final JavaScriptObject previous = this.set0(wrappedObject, this.getCollection(), index);
        Object previousWrapper = null;

        // if there was an element at the given index locate/create a wrapper.
        if (null != previous) {
            previousWrapper = wrappers.get(previous);
            if (null != previousWrapper) {
                previousWrapper = this.createWrapper(previous);
                wrappers.remove(previous);
            }
        }

        wrappers.put(wrappedObject, wrapper);
        return previousWrapper;
    }

    protected native JavaScriptObject set0(final JavaScriptObject element, final JavaScriptObject collection,
            final int index)/*-{
     var previousElement = collection[ index ];
     collection[ index ] = element;

     // check item was actually modified.
     if( collection[ index ] != element ){
     @rocket.client.dom.DomHelper::handleUnableToSetCollectionItem(Lcom/google/gwt/core/client/JavaScriptObject;I)(collection,index);
     }
     return previousElement;
     }-*/;

    // ADDITIONAL / OPTIONAL METHODS
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public boolean add(final Object element) {
        this.checkElementType(element);

        final ObjectWrapper wrapper = (ObjectWrapper) element;
        this.add0(this.getCollection(), this.unwrapWrapper(wrapper));
        this.getWrappers().put(wrapper.getObject(), wrapper);
        this.incrementModificationCounter();
        return true;
    }

    protected abstract void add0(final JavaScriptObject collection, final JavaScriptObject element);

    public void add(final int index, final Object element) {
        this.checkElementType(element);

        final ObjectWrapper wrapper = (ObjectWrapper) element;
        this.insert0(this.getCollection(), index, this.unwrapWrapper(wrapper));
        this.getWrappers().put(wrapper.getObject(), wrapper);
        this.incrementModificationCounter();
    }

    protected abstract void insert0(final JavaScriptObject collection, final int index, final JavaScriptObject element);

    public boolean remove(final Object element) {
        final int index = this.indexOf(element);
        final boolean removable = index != -1;
        if (removable) {
            final JavaScriptObject removed = remove0(this.getCollection(), index);
            this.getWrappers().remove(removed);
            this.incrementModificationCounter();
        }
        return removable;
    }

    protected abstract JavaScriptObject remove0(final JavaScriptObject collection, final int index);

    // ITERATOR
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public Iterator iterator() {
        final DomCollectionListIterator iterator = new DomCollectionListIterator();
        iterator.setList(this);
        iterator.syncModificationCounters();
        return iterator;
    }

    /**
     * This counter is used by iterators to keep track of concurrent modifications. Each time the list is modified this counter is
     * incremented.
     */
    private int modificationCounter;

    protected int getModificationCounter() {
        return modificationCounter;
    }

    protected void setModificationCounter(final int modificationCounter) {
        this.modificationCounter = modificationCounter;
    }

    protected void incrementModificationCounter() {
        this.setModificationCounter(this.getModificationCounter() + 1);
    }

    /**
     * Provides the fail fast iterator view of this List
     */
    class DomCollectionListIterator extends IteratorView {
        protected boolean hasNext0() {
            return this.getIndex() < this.getList().size();
        }

        protected Object next0(int type) {
            return this.getList().get(this.getIndex());
        }

        protected void leavingNext() {
            this.setIndex(this.getIndex() + 1);
        }

        protected void remove0() {
            final DomCollectionList list = this.getList();
            list.remove(this.getIndex() - 1);
        }

        protected int getParentModificationCounter() {
            return this.getList().getModificationCounter();
        }

        DomCollectionList list;

        DomCollectionList getList() {
            ObjectHelper.checkNotNull("field:list", list);
            return this.list;
        }

        void setList(final DomCollectionList list) {
            ObjectHelper.checkNotNull("parameter:list", list);
            this.list = list;
        }

        int index;

        int getIndex() {
            return this.index;
        }

        void setIndex(final int index) {
            this.index = index;
        }
    };

    /**
     * The javascript collection or collection object itself
     */
    private JavaScriptObject collection;

    public JavaScriptObject getCollection() {
        final JavaScriptObject collection = this.getCollection0();
        ObjectHelper.checkNotNull("field:collection", collection);
        return collection;
    }

    private native JavaScriptObject getCollection0()/*-{
     return this.@rocket.client.dom.DomCollectionList::collection;
     }-*/;

    public void setCollection(final JavaScriptObject collection) {
        ObjectHelper.checkNotNull("parameter:collection", collection);
        this.setCollection0(collection);
    }

    private native void setCollection0(final JavaScriptObject collection)/*-{
     this.@rocket.client.dom.DomCollectionList::collection = collection;
     }-*/;

    public String toString() {
        return super.toString() + ", collection: " + collection + ", modificationCounter: " + modificationCounter;
    }
}