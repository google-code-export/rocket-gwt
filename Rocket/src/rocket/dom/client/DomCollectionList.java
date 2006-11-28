/*
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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import rocket.collection.client.IteratorView;
import rocket.util.client.ObjectHelper;
import rocket.util.client.ObjectWrapper;
import rocket.util.client.PrimitiveHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * This base class is used to build a list view of a DomCollection with various list methods working with ObjectWrapper implementations
 * which in turn wrap an element belonging to the wrapped DomCollection.
 * 
 * Client code must take care of destroying any returned elements when they are no longer needed to break / release cyclic browser object
 * references between the wrapper and the native object.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class DomCollectionList extends AbstractList implements Destroyable {

    protected DomCollectionList() {
        super();
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

    /**
     * Extracts the object being wrapped from the given wrapper.
     * 
     * Typically sub-classes do not need to override this method but some might find it necessary.
     * 
     * @param wrapper
     * @return
     */
    protected JavaScriptObject unwrapWrapper(final Object wrapper) {
        ObjectHelper.checkNotNull("parameter:wrapper", wrapper);

        final ObjectWrapper wrapper0 = (ObjectWrapper) wrapper;
        return wrapper0.getObject();
    }

    /**
     * Various portions of this class verify attempts from client code to pass in elements to the List. This method is responsible for
     * verifying the type and possible the state of the potential element.
     * 
     * Sub-classes typically test the type of the wrapper and throw an exception if its the wrong type.
     * 
     * @param wrapper
     */
    protected abstract void checkElementType(final Object wrapper);

    // LIST::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public int size() {
        return ObjectHelper.getInteger(this.getCollection(), "length");
    }

    public int indexOf(final Object object) {
        this.checkElementType(object);

        final ObjectWrapper wrapper = (ObjectWrapper) object;
        return wrapper.hasObject() ? this.indexOf0(this.getCollection(), wrapper.getObject()) : -1;
    }

    protected native int indexOf0(final JavaScriptObject collection, final JavaScriptObject element)/*-{
     var index = -1;
     for( var i = 0; i < collection.length; i++ ){
     if( collection[ i ] == element ){
     index = i;
     break;
     }
     }
     return index;    
     }-*/;

    public int lastIndexOf(final Object object) {
        this.checkElementType(object);

        final ObjectWrapper wrapper = (ObjectWrapper) object;
        return this.lastIndexOf0(this.getCollection(), wrapper.getObject());
    }

    protected native int lastIndexOf0(final JavaScriptObject collection, final JavaScriptObject element)/*-{
     var index = -1;
     for( var i = collection.length -1; i >= 0; i-- ){
     if( collection[ i ] == element ){
     index = i;
     break;
     }
     }
     return index;    
     }-*/;

    public Object get(final int index) {
        PrimitiveHelper.checkBetween("parameter:index", index, 0, size());

        Object wrapper = null;

        final JavaScriptObject element = this.get0(index);
        if (null != element) {
            wrapper = this.createWrapper(element);
            this.adopt(wrapper);
        }

        return wrapper;
    }

    protected JavaScriptObject get0(final int index) {
        return this.get1(this.getCollection(), index);
    }

    protected native JavaScriptObject get1(final JavaScriptObject collection, final int index)/*-{
     var element = collection[ index ];
     return element ? element : 0;
     }-*/;

    public Object set(final int index, final Object element) {
        this.checkElementType(element);

        Object previousWrapper = null;

        // update the collection...
        final ObjectWrapper wrapper = (ObjectWrapper) element;
        final JavaScriptObject previous = this.set0(wrapper, index);

        // wrap if necessary...
        if (null != previous) {
            previousWrapper = this.createWrapper(previous);
        }

        this.adopt(element);
        return previousWrapper;
    }

    /**
     * Delegates to a javascript method to do the actual set after preparing parameters.
     * 
     * @param wrapper
     * @param index
     * @return
     */
    protected JavaScriptObject set0(final ObjectWrapper wrapper, final int index) {
        ObjectHelper.checkNotNull("parameter:wrapper", wrapper);

        return this.set1(wrapper.getObject(), this.getCollection(), index);
    }

    /**
     * Sets or overwrites the object at the given slot.
     * 
     * @param element
     * @param collection
     * @param index
     * @return
     */
    protected native JavaScriptObject set1(final JavaScriptObject element, final JavaScriptObject collection,
            final int index)/*-{
     var previousElement = collection[ index ];
     collection[ index ] = element;

     if( collection[ index ] != element ){
     throw "Failed to update collection element at index " + index;
     }
     return previousElement ? previousElement : null;
     }-*/;

    // ADDITIONAL / OPTIONAL METHODS ADD, INSERT, REMOVE
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public boolean add(final Object element) {
        this.checkElementType(element);

        final ObjectWrapper wrapper = (ObjectWrapper) element;
        this.add0(wrapper);
        this.adopt(element);

        this.incrementModificationCounter();
        return true;
    }

    protected void add0(final ObjectWrapper wrapper) {
        ObjectHelper.checkNotNull("parameter:wrapper", wrapper);

        this.add1(this.getCollection(), this.unwrapWrapper(wrapper));
    }

    /**
     * Sub-classes typically escape to javascript code to add a new element to the collection.
     * 
     * @param collection
     * @param element
     */
    protected abstract void add1(final JavaScriptObject collection, final JavaScriptObject element);

    public void add(final int index, final Object element) {
        this.checkElementType(element);

        final ObjectWrapper wrapper = (ObjectWrapper) element;
        this.insert0(wrapper, index);
        this.adopt(element);

        this.incrementModificationCounter();
    }

    protected void insert0(final ObjectWrapper wrapper, final int index) {
        this.insert1(this.getCollection(), index, this.unwrapWrapper(wrapper));
    }

    /**
     * Sub-classes typically escape to javascript code to insert a new element to the collection.
     * 
     * @param collection
     * @param index
     * @param element
     */
    protected abstract void insert1(final JavaScriptObject collection, final int index, final JavaScriptObject element);

    public boolean remove(final Object element) {
        final int index = this.indexOf(element);
        final boolean removable = index != -1;
        if (removable) {
            this.disown(element);
            final JavaScriptObject removed = this.remove0(this.getCollection(), index);
            this.incrementModificationCounter();
        }
        return removable;
    }

    public Object remove(final int index) {
        final JavaScriptObject removed = this.remove0(this.getCollection(), index);

        // create a dettached wrapper...
        final Object wrapper = this.createWrapper(removed);
        this.disown(wrapper);
        this.incrementModificationCounter();
        return wrapper;
    }

    /**
     * Sub-classes typically escape to javascript code to remove an existing element belonging to this collection.
     * 
     * @param collection
     * @param index
     * @param element
     * @return The element just removed.
     */
    protected abstract JavaScriptObject remove0(final JavaScriptObject collection, final int index);

    // ITERATOR
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public Iterator iterator() {
        final DomCollectionListIterator iterator = new DomCollectionListIterator();
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
            return this.getCursor() < DomCollectionList.this.size();
        }

        protected Object next0() {
            return DomCollectionList.this.get(this.getCursor());
        }

        protected void afterNext() {
            this.setCursor(this.getCursor() + 1);
        }

        protected void remove0() {
            DomCollectionList.this.remove(this.getCursor() - 1);
        }

        protected int getModificationCounter() {
            return DomCollectionList.this.getModificationCounter();
        }

        int cursor;

        int getCursor() {
            return this.cursor;
        }

        void setCursor(final int cursor) {
            this.cursor = cursor;
        }
    };

    /**
     * The native collection object itself being viewed as a List
     */
    private JavaScriptObject collection;

    public JavaScriptObject getCollection() {
        ObjectHelper.checkNotNull("field:collection", collection);
        return collection;
    }

    public void setCollection(final JavaScriptObject collection) {
        ObjectHelper.checkNotNull("parameter:collection", collection);
        this.collection = collection;
    }

    protected void clearCollection() {
        this.collection = null;
    }

    // BULK ::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public boolean containsAll(final Collection otherCollection) {
        ObjectHelper.checkNotNull("parameter:otherCollection", otherCollection);

        boolean all = false;
        final Iterator iterator = otherCollection.iterator();
        while (iterator.hasNext()) {
            Object object = null;
            try {
                object = iterator.next();
                all = all | this.contains(object);
            } finally {
                DomHelper.destroyIfNecessary(object);
            }
        }
        return all;
    }

    public boolean removeAll(final Collection otherCollection) {
        ObjectHelper.checkNotNull("parameter:otherCollection", otherCollection);

        boolean modified = false;
        final Iterator iterator = this.iterator();

        while (iterator.hasNext()) {
            Object object = null;
            try {
                object = iterator.next();
                if (this.contains(object)) {
                    iterator.remove();
                    modified = true;
                }
            } finally {
                DomHelper.destroyIfNecessary(object);
            }
        }
        return modified;
    }

    public boolean retainAll(final Collection otherCollection) {
        ObjectHelper.checkNotNull("parameter:otherCollection", otherCollection);

        boolean modified = false;
        final Iterator iterator = this.iterator();

        while (iterator.hasNext()) {
            Object object = null;
            try {
                object = iterator.next();
                if (false == this.contains(object)) {
                    iterator.remove();
                    modified = true;
                }
            } finally {
                DomHelper.destroyIfNecessary(object);
            }
        }
        return modified;
    }

    /**
     * Adds all the element from the given collection to this list.
     * 
     * @param otherCollection
     * @return
     */
    public boolean addAll(final Collection otherCollection) {
        return this.addAll(this.size(), otherCollection);
    }

    public boolean addAll(final int index, final Collection otherCollection) {
        ObjectHelper.checkNotNull("parameter:otherCollection", collection);

        final Iterator iterator = otherCollection.iterator();
        int index0 = index;

        while (iterator.hasNext()) {
            Object object = null;
            try {
                object = iterator.next();
                this.add(index, object);
                index0++;
            } finally {
                DomHelper.destroyIfNecessary(object);
            }
        }
        return index0 != index;
    }

    /**
     * Returns all the native objects belonging to this list.
     */
    public Object[] toArray() {
        final List list = new ArrayList();
        list.addAll(this);
        return list.toArray();
    }

    // REFERENCE MANAGEMENT ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This method is called each time an object is added to this list.
     * 
     * @param object
     */
    protected abstract void adopt(final Object object);

    /**
     * THis method is called each time an object is removed from this list.
     * 
     * @param object
     */
    protected abstract void disown(final Object object);

    // DESTROYABLE :::::::::::::::::::::::::::::::::

    /**
     * Requests that this collection or list view releases all handles to native objects.
     */
    public void destroy() {
        this.clearCollection();
    }

    public String toString() {
        return super.toString() + ", collection: " + collection + ", modificationCounter: " + modificationCounter;
    }
}