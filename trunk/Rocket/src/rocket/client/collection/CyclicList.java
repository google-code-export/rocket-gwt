package rocket.client.collection;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.SystemHelper;

/**
 * This list does not automatically expand but rather when full overwrites the oldest element first.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * TODO list.add( index, Object ) AND UNIT TEST
 */
public class CyclicList extends AbstractList implements List {

    public CyclicList(final int capacity) {
        this.setElements(new Object[capacity]);
    }

    public int getCapacity() {
        return this.getElements().length;
    }

    public void setCapacity(final int capacity) {
        final Object[] elements = this.getElements();
        final int currentCapacity = elements.length;

        PrimitiveHelper.checkGreaterThanOrEqual("parameter:capacity", capacity, currentCapacity);

        // if capacity has increased allocate a new array and copy the elements.
        if (capacity != currentCapacity) {
            final Object[] newElements = new Object[capacity];

            // copy over elements from the current elements array to the
            // newElements array.
            final Iterator iterator = this.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                newElements[i] = iterator.next();
                i++;
            }

            this.setElements(newElements);
        }
    }

    // ABSTRACT LIST
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * The current size of this list.
     */
    private int size;

    public int size() {
        return this.size;
    }

    protected void setSize(final int size) {
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:size", size, 0);
        this.size = size;
    }

    /**
     * Tests if this list is full. Add operations will cause the now first element to be lost and overwritten.
     * 
     * @return
     */
    protected boolean isFull() {
        return this.size() == this.getCapacity();
    }

    public boolean add(final Object object) {
        this.append(object);
        return true;
    }

    public void add(final int index, final Object element) {
        int size = this.size();
        PrimitiveHelper.checkBetween("parameter:index", index, 0, size + 1);

        // if list is full cant add(insert).
        final int capacity = this.getCapacity();
        if (size >= capacity) {
            SystemHelper.handleUnsupportedOperation("Unable to add(insert) an element to an already full " + this);
        }

        if (index == size) {
            this.append(element);
        } else {
            this.insert(index, element);
        }
    }

    protected void append(final Object object) {
        boolean full = this.isFull();

        final Object[] elements = this.getElements();

        // first save object.
        int lastIndex = this.getLastIndex();
        elements[lastIndex] = object;

        // advance lastIndex
        this.setLastIndex(this.advanceIndexForward(lastIndex));

        // if this list is full advance the firstIndex property as well.
        if (full) {
            this.setFirstIndex(this.advanceIndexForward(this.getFirstIndex()));
        } else {
            this.setSize(this.size() + 1);
        }

        // increment modification counter
        this.setModificationCounter(this.getModificationCounter() + 1);
    }

    protected void insert(final int index, final Object element) {
        int size = this.size();

        // increase size - this should help avoid problems during
        // translateIndex.
        this.setSize(size + 1);

        final Object[] elements = this.getElements();

        final int shiftCount = size - index;
        if (shiftCount > 0) {

            int i = size;
            int destinationIndex = this.translateIndex(i);
            for (int j = 0; j < shiftCount; j++) {
                i--;
                final int sourceIndex = this.translateIndex(i);
                elements[destinationIndex] = elements[sourceIndex];

                destinationIndex = sourceIndex;
            }
        }

        // save the newly inserted element.
        elements[translateIndex(index)] = element;

        // increment modification counter
        this.setModificationCounter(this.getModificationCounter() + 1);
    }

    public Object get(final int index) {
        return this.getElements()[this.translateIndex(index)];
    }

    public Object set(final int index, final Object object) {
        final int translatedIndex = this.translateIndex(index);

        // first fetch the previous element at the given slot.
        final Object[] elements = this.getElements();
        final Object previous = elements[translatedIndex];

        // save object
        elements[translatedIndex] = object;

        // return previous.
        return previous;
    }

    public boolean remove(final Object element) {
        boolean removed = false;

        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            final Object otherElement = iterator.next();
            if (element == otherElement) {
                iterator.remove();
                removed = true;
                this.setModificationCounter(this.getModificationCounter() + 1);
                break;
            }
        }
        return removed;
    }

    public Object remove(final int index) {
        final Object removed = this.removeByIndexWithoutConcurrentModificationCheck(index);

        this.setModificationCounter(this.getModificationCounter() + 1);
        return removed;
    }

    /**
     * This remove method removes and shuffles the internal elements array of the element pointed to by the parameter:index. It does not
     * update the modification counter enabling it to be used by iterator.remove().
     * 
     * @param index
     * @return
     */
    protected Object removeByIndexWithoutConcurrentModificationCheck(final int index) {
        int currentIndex = this.translateIndex(index);

        final Object[] elements = this.getElements();

        // before overwriting the removed element remember it.
        Object removed = elements[currentIndex];

        final int size = this.size();
        // shift elements up by one.
        for (int i = index + 1; i < size; i++) {
            final int nextIndex = this.translateIndex(i);
            elements[currentIndex] = elements[nextIndex];
            currentIndex = nextIndex;
        }

        this.setSize(this.size() - 1);
        return removed;
    }

    /**
     * Returns a fail fast iterator that may be used to visit all elements within this list.
     */
    public Iterator iterator() {

        final CyclicList that = this;

        final IteratorView iterator = new IteratorView() {
            protected boolean hasNext0() {
                return this.getIndex() < that.size();
            }

            protected Object next0(final int ignored) {
                return that.get(this.getIndex());
            }

            protected void leavingNext() {
                this.setIndex(this.getIndex() + 1);
            }

            protected void remove0() {
                final int index = this.getIndex() - 1;
                that.removeByIndexWithoutConcurrentModificationCheck(index);
                this.setIndex(index);
            }

            /**
             * A pointer to the next element.
             */
            int index;

            int getIndex() {
                return index;
            }

            void setIndex(final int index) {
                this.index = index;
            }

            protected int getParentModificationCounter() {
                return that.getModificationCounter();
            }
        };

        iterator.syncModificationCounters();
        return iterator;
    }

    /**
     * This counter keeps track and is increment each time the list has an element added or removed. It is used to provide a failfast
     * iterator.
     */
    private int modificationCounter;

    protected int getModificationCounter() {
        return this.modificationCounter;
    }

    protected void setModificationCounter(final int modificationCounter) {
        this.modificationCounter = modificationCounter;
    }

    // HELPERS :::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected int translateIndex(final int index) {
        this.checkIndex(index);
        final int elementArrayLength = this.getElements().length;
        return (index + this.getFirstIndex() + elementArrayLength) % elementArrayLength;
    }

    protected void checkIndex(final int index) {
        PrimitiveHelper.checkBetween("parameter:index", index, 0, this.size());
    }

    protected int advanceIndexForward(final int index) {
        final int elementArrayLength = this.getElements().length;
        return (index + 1 + elementArrayLength) % elementArrayLength;
    }

    // CYCLIC LIST
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This array contains all elements.
     */
    private Object[] elements;

    protected Object[] getElements() {
        ObjectHelper.checkNotNull("field:elements", elements);
        return elements;
    }

    protected void setElements(final Object[] elements) {
        ObjectHelper.checkNotNull("parameter:elements", elements);
        this.elements = elements;
    }

    /**
     * THis index points to the first element or start of the list.
     */
    private int firstIndex;

    protected int getFirstIndex() {
        return firstIndex;
    }

    protected void setFirstIndex(final int firstIndex) {
        this.firstIndex = firstIndex;
    }

    /**
     * THis index points to the last element or end of the list.
     */
    private int lastIndex;

    protected int getLastIndex() {
        return lastIndex;
    }

    protected void setLastIndex(final int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public String toString() {
        return super.toString() + ", elements: " + this.elements + ", firstIndex: " + firstIndex + ", lastIndex: "
                + lastIndex + ", modificationCounter: " + modificationCounter;
    }
}
