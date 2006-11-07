package rocket.client.dom;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

import rocket.client.collection.IteratorView;
import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Convenient base class for any list view of a native dom list.
 * 
 * Invoking {@link #destroy} ]also destroys any cached DomObjectMapValue instances.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class DomObjectList extends AbstractList {

    protected DomObjectList() {
        super();

        this.createWrappers();
    }

    /**
     * Factory method which sub-classes must create the appropriate wrapper for a given slot.
     * 
     * @param index
     */
    protected abstract DomObjectListElement createWrapper(final int index);

    protected void checkElementType(final Object wrapper) {
        if (false == this.isValidType(wrapper)) {
            DomHelper.handleAssertFailure("Invalid wrapper type");
        }
    }

    /**
     * Tests if the given wrapper is valid for this type of list.
     * 
     * @param wrapper
     * @return
     */
    protected abstract boolean isValidType(final Object wrapper);

    /**
     * This method is called each time an object is added to this list.
     * 
     * @param object
     */
    protected void adopt(final DomObjectListElement object) {
    }

    /**
     * THis method is called each time an object is removed from this list.
     * 
     * @param object
     */
    protected void disown(final DomObjectListElement object) {
        ObjectHelper.checkNotNull("parameter:object", object);
        object.destroy();
    }

    /**
     * This list contains a cache of wrappers belonging to this list.
     */
    private WrapperList wrappers;

    protected WrapperList getWrappers() {
        ObjectHelper.checkNotNull("field:wrappers", this.wrappers);
        return this.wrappers;
    }

    protected void setWrappers(final WrapperList wrappers) {
        ObjectHelper.checkNotNull("parameter:wrappers", wrappers);
        this.wrappers = wrappers;
    }

    protected void createWrappers() {
        this.setWrappers(new WrapperList());
    }

    /**
     * Visits all the wrapper elements starting with startIndex adding the delta value to their indexes...
     * 
     * Null wrapper elements are skipped.
     * 
     * @param startIndex
     * @param delta
     */
    protected void modifyElementIndicies(final int startIndex, final int delta) {
        final ArrayList wrappers = this.getWrappers();
        final int size = wrappers.size();
        for (int i = startIndex; i < size; i++) {
            final DomObjectListElement element = (DomObjectListElement) wrappers.get(i);
            if (null == element) {
                continue;
            }
            element.setIndex(element.getIndex() + delta);
        }
    }

    // LIST::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public int size() {
        return ObjectHelper.getInteger(this.getObject(), "length");
    }

    public int indexOf(final Object object) {
        int index = -1;
        if (this.isValidType(object)) {
            final DomObjectListElement element = (DomObjectListElement) object;
            index = element.getIndex();
        }
        return index;
    }

    public int lastIndexOf(final Object object) {
        int index = -1;
        if (this.isValidType(object)) {
            final DomObjectListElement element = (DomObjectListElement) object;
            index = element.getIndex();
        }
        return index;
    }

    public Object get(final int index) {
        PrimitiveHelper.checkBetween("parameter:index", index, 0, size());

        final WrapperList wrappers = this.getWrappers();
        DomObjectListElement element = index < wrappers.size() ? (DomObjectListElement) wrappers.get(index) : null;
        if (null == element) {
            element = this.createWrapper(index);
            wrappers.ensureListCapacity(index + 1);
            wrappers.set(index, element);
        }
        return element;
    }

    public Object set(final int index, final Object element) {
        this.checkElementType(element);

        // replaces the previous wrapper...
        final WrapperList wrappers = this.getWrappers();
        wrappers.ensureListCapacity(index + 1);
        final DomObjectListElement replaced = (DomObjectListElement) wrappers.set(index, element);

        this.adopt((DomObjectListElement) element);
        if (null != replaced) {
            this.disown(replaced);
        }
        return replaced;
    }

    public boolean add(final Object element) {
        this.checkElementType(element);

        final DomObjectListElement wrapper = (DomObjectListElement) element;
        final WrapperList wrappers = this.getWrappers();
        wrapper.setParent(this);
        wrapper.clearIndex();

        // create the native object...
        this.add0(wrapper);

        // adopt
        final int index = this.size() - 1;
        wrapper.setIndex(index);

        this.adopt((DomObjectListElement) element);
        wrappers.ensureListCapacity(index);
        wrappers.add(wrapper);

        this.incrementModificationCounter();
        return true;
    }

    /**
     * This method needs to be implemented by sub-classes and presents an opportunity for the native object to be added to the end of this
     * list.
     * 
     * @param element
     */
    protected abstract void add0(final DomObjectListElement element);

    public void add(final int index, final Object element) {
        this.checkElementType(element);

        final DomObjectListElement wrapper = (DomObjectListElement) element;
        final WrapperList wrappers = this.getWrappers();

        // half adopt...
        wrapper.setParent(this);
        wrapper.clearIndex();

        // create the native object...
        this.insert0(index, wrapper);

        // adopt
        wrapper.setIndex(index);
        this.adopt((DomObjectListElement) element);
        wrappers.ensureListCapacity(index + 1);
        wrappers.add(index, wrapper);

        // add 1 to all the wrapper list siblings that follow...
        this.modifyElementIndicies(index + 1, 1);

        this.incrementModificationCounter();
    }

    /**
     * Sub-classes need to insert a new native object in the native list.
     * 
     * @param element
     */
    protected abstract void insert0(final int index, final DomObjectListElement element);

    public boolean remove(final Object element) {
        boolean removed = false;
        if (this.isValidType(element)) {
            DomObjectListElement wrapper = (DomObjectListElement) element;
            if (wrapper.hasParent() && wrapper.getParent() == this) {
                final int index = wrapper.getIndex();
                this.remove0(wrapper);
                this.disown(wrapper);

                // remove from wrappers list also fix up indexes of following wrappers...
                final ArrayList wrappers = this.getWrappers();
                wrappers.remove(index);
                this.modifyElementIndicies(index, -1);

                this.incrementModificationCounter();

                removed = true;
            }
        }
        return removed;
    }

    /**
     * Sub-classes need to remove the native object from the native list.
     * 
     * @param element
     */
    protected abstract void remove0(final DomObjectListElement element);

    // ITERATOR
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public Iterator iterator() {
        final DomObjectArrayListIterator iterator = new DomObjectArrayListIterator();
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
     * Provides the fail fast iterator view of this ArrayList
     */
    class DomObjectArrayListIterator extends IteratorView {
        protected boolean hasNext0() {
            return this.getCursor() < DomObjectList.this.size();
        }

        protected Object next0() {
            return DomObjectList.this.get(this.getCursor());
        }

        protected void afterNext() {
            this.setCursor(this.getCursor() + 1);
        }

        protected void remove0() {
            DomObjectList.this.remove(this.getCursor() - 1);
        }

        protected int getModificationCounter() {
            return DomObjectList.this.getModificationCounter();
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
     * The native list object being wrapped
     */
    private JavaScriptObject object;

    public JavaScriptObject getObject() {
        ObjectHelper.checkNotNull("field:object", object);
        return object;
    }

    public boolean hasObject() {
        return null != this.object;
    }

    public void setObject(final JavaScriptObject object) {
        ObjectHelper.checkNotNull("parameter:object", object);
        this.object = object;
    }

    public void clearObject() {
        this.object = null;
    }

    /**
     * Returns the string form of the object being wrapped.
     * 
     * @return
     */
    protected native String toStringObject()/*-{
     var object = this.@rocket.client.dom.DomObjectList::object;
     return object ? object : "null";
     }-*/;

    // DESTROYABLE :::::::::::::::::::::::::::::::::

    /**
     * Requests that this collection or list view releases all handles to native objects. After calling this method the list should not be
     * used.
     */
    public void destroy() {
        this.clearObject();

        final Iterator iterator = this.getWrappers().iterator();
        while (iterator.hasNext()) {
            final DomObjectListElement element = (DomObjectListElement) iterator.next();
            if (null == element) {
                continue;
            }
            element.destroy();
            iterator.remove();
        }
    }

    public String toString() {
        return super.toString() + ", object: " + this.toStringObject() + ", modificationCounter: "
                + modificationCounter;
    }

    /**
     * This list adds a ensureListCapacity method to allow clients to control the size of a list.
     * 
     * To avoid recursive calls from add to ensureCapacity the method is called ensureListCapacity.
     */
    class WrapperList extends ArrayList {
        public void ensureListCapacity(final int requiredSize) {
            final int add = requiredSize - this.size();
            for (int i = 0; i < add; i++) {
                this.add(null);
            }
        }
    }
}