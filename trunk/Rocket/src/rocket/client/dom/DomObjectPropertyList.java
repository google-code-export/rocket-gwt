package rocket.client.dom;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.client.collection.IteratorView;
import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Creates a list view given a property belonging to a DOM object. The DOM object's property must only be updated via this list, otherwise
 * the list will be corrupted and invalid.
 * 
 * Sub-classes are required to override the various wrapping/unwrapping methods that convert between string tokens and wrappers and vice
 * versa.
 * 
 * @author Miroslv Pokorny (mP)
 */
public abstract class DomObjectPropertyList extends AbstractList {
    protected DomObjectPropertyList() {
        super();

        this.createList();
    }

    /**
     * Sub-classes should override this method to check that the element object be it new or existing is of the type handled by this List.
     * 
     * @param object
     */
    protected abstract void checkElementType(final Object object);

    /**
     * Factory method which tokenises the given value. These tokens will be used by elements belonging to this list.
     * 
     * @param value
     * @return
     */
    protected abstract String[] createTokens(final String value);

    /**
     * Factory method which creates the wrapper for the given element on demand.
     * 
     * @return
     */
    protected abstract DomObjectPropertyListElement createWrapper();

    /**
     * This method double checks that the string value used and represented by this list matches the object being wrapped. If they dont an
     * exception is thrown.
     * 
     */
    protected void stalenessGuard() {
        if (this.isStale()) {
            this.updateElementsFromObjectProperty();
        }
    }

    /**
     * This method checks that the cached string value represented by this list and its elements matches the actual value held by the DOM
     * object property that it is supposed to be reprsenting.
     */
    protected boolean isStale() {
        final String actualValue = this.getObjectPropertyValue();
        final String value = this.getPropertyValue();
        return false == actualValue.equals(value);
    }

    /**
     * This method may be used to update the object property being represented by this list.
     */
    protected void updateObjectPropertyValue() {
        final String newValue = this.rebuildStringForm();
        this.setPropertyValue(newValue);
        this.setObjectPropertyValue(newValue);

        if (this.shouldResyncAfterObjectValueSet() && this.isStale()) {
            this.updateElementsFromObjectProperty();
        }
    }

    /**
     * Iterates thru all elements belonging to this list updating their string value.
     */
    protected void updateElementsFromObjectProperty() {
        final String[] tokens = this.createTokens(this.getObjectPropertyValue());

        // loop thru visiting all elements updating the string or string value
        final List list = this.getList();
        int size = list.size();
        int tokenCount = tokens.length;

        // update the string or string value for each existing element...
        int updateCount = size < tokenCount ? size : tokenCount;
        for (int i = 0; i < updateCount; i++) {
            final String newValue = tokens[i];

            final Object element = list.get(i);
            if (element instanceof String) {
                list.set(i, newValue);
                continue;
            }

            final DomObjectPropertyListElement wrapper = (DomObjectPropertyListElement) element;
            wrapper.setValueQuickly(newValue);
        }

        // need to either save extra tokens or delete extra elements...
        if (size < tokenCount) {
            // got extra tokens save them...
            for (int i = size; i < tokenCount; i++) {
                final String newValue = tokens[i];
                list.add(newValue);
            }
        } else {
            // need to remove or disconnect previous elements...
            for (int i = tokenCount; i < size; i++) {
                final Object removed = list.remove(tokenCount);
                if (removed instanceof DomObjectPropertyListElement) {
                    final DomObjectPropertyListElement wrapper = (DomObjectPropertyListElement) removed;
                    wrapper.clearList();
                }
            }
        }
    }

    /**
     * In some cases the value that gets set upon the object property may be altered and needs to be re-read and elements resynchronized.
     * 
     * @return True if the String value should be re-read and elements updated or false if this step can be skipped.
     */
    protected boolean shouldResyncAfterObjectValueSet() {
        return true;
    }

    /**
     * This method is called to rebuild the string form of all the elements within this List.
     * 
     * A convenient {@link #rebuildStringForm(String)} is provided which merely concatenates all elements string form separated by a
     * separator.
     * 
     * @return
     */
    protected abstract String rebuildStringForm();

    /**
     * Rebuilds a string that creates the values of each and every element of this list.
     * 
     * Sub-classes may wish to override this method if some logic more complex than simple concatenation with a separator is required.
     * 
     * @param separator
     * @return
     */
    protected String rebuildStringForm(final String separator) {
        final StringBuffer buf = new StringBuffer();
        boolean addSeparator = false;
        final Iterator iterator = this.getList().iterator();
        while (iterator.hasNext()) {
            if (addSeparator) {
                buf.append(separator);
            }
            addSeparator = true;

            final Object element = iterator.next();
            if (element instanceof String) {
                buf.append((String) element);
                continue;
            }

            final DomObjectPropertyListElement wrapper = (DomObjectPropertyListElement) element;
            buf.append(wrapper.getValueQuickly());
        }

        return buf.toString();
    }

    // LIST
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public int size() {
        this.stalenessGuard();
        return this.getList().size();
    }

    public boolean add(final Object element) {
        this.checkElementType(element);

        this.stalenessGuard();
        final boolean added = this.getList().add(element);
        if (added) {
            // Updates the object property value.
            this.adopt((DomObjectPropertyListElement) element);
            this.updateObjectPropertyValue();
            this.increaseModificationCounter();
        }
        return added;
    }

    public void add(final int index, final Object element) {
        this.checkElementType(element);

        this.stalenessGuard();
        this.getList().add(index, element);

        this.adopt((DomObjectPropertyListElement) element);
        this.updateObjectPropertyValue();
        this.increaseModificationCounter();
    }

    protected void adopt(final DomObjectPropertyListElement element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        element.setList(this);
    }

    public Object remove(final int index) {
        this.stalenessGuard();

        final List list = this.getList();
        Object removed = list.remove(index);

        while (true) {
            if (removed instanceof String) {
                final DomObjectPropertyListElement wrapper = this.createWrapper();
                wrapper.setValue((String) removed);
                removed = wrapper;
                break;
            }
            final DomObjectPropertyListElement wrapper = (DomObjectPropertyListElement) removed;
            this.disown(wrapper);
            break;
        }

        // update the backing property value.
        this.updateObjectPropertyValue();
        this.increaseModificationCounter();
        return removed;
    }

    protected void disown(final DomObjectPropertyListElement element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        element.clearList();
    }

    public Object get(final int index) {
        PrimitiveHelper.checkBetween("parameter:index", index, 0, size());

        this.stalenessGuard();

        Object value = null;
        while (true) {
            final List list = this.getList();
            value = list.get(index);

            // if value hasnt been wrapped better wrap it...
            if (value instanceof String) {
                final DomObjectPropertyListElement wrapper = this.createWrapper();
                this.adopt(wrapper);
                wrapper.setValueQuickly((String) value);

                value = wrapper;
                list.set(index, value);
                break;
            }

            this.checkElementType(value);
            break;
        }

        return value;
    }

    public Object set(final int index, final Object element) {
        this.checkElementType(element);

        this.adopt((DomObjectPropertyListElement) element);

        Object previous = this.get(index);
        this.getList().set(index, element);

        // updates the property value of the wrapped object.
        this.updateObjectPropertyValue();
        return previous;
    }

    public Iterator iterator() {

        final IteratorView iterator = new IteratorView() {
            protected boolean hasNext0() {
                final int index = this.getIndex();
                final int size = size();
                return index < size;
            }

            protected Object next0(final int type) {
                final int index = this.getIndex();
                return get(index);
            }

            protected void leavingNext() {
                this.setIndex(this.getIndex() + 1);
            }

            protected void remove0() {
                // because index was advanced by next() finishes the actual
                // index is the one before the current.
                final int index = this.getIndex() - 1;
                DomObjectPropertyList.this.remove(index);

                // because element was removed need to take one from index.
                this.setIndex(index);
            }

            protected int getParentModificationCounter() {
                return DomObjectPropertyList.this.getModificationCounter();
            }

            /**
             * This index points to the element within the parent list pointed to by this iterator.
             */
            int index;

            int getIndex() {
                return index;
            }

            void setIndex(final int index) {
                this.index = index;
            }
        };

        iterator.syncModificationCounters();
        return iterator;
    }

    /**
     * Helps keep track of concurrent modification between an iterator and its parent container instance
     */
    private int modificationCounter;

    protected int getModificationCounter() {
        return this.modificationCounter;
    }

    public void setModificationCounter(final int modificationCounter) {
        this.modificationCounter = modificationCounter;
    }

    protected void increaseModificationCounter() {
        this.setModificationCounter(this.getModificationCounter() + 1);
    }

    // PROPERTIES :::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * The javascript object containing the property.
     */
    private JavaScriptObject object;

    public JavaScriptObject getObject() {
        ObjectHelper.checkNotNull("field:object", object);
        return object;
    }

    public void setObject(final JavaScriptObject object) {
        ObjectHelper.checkNotNull("parameter:object", object);
        this.object = object;
    }

    /**
     * The name of the property belonging to object that contains the source of the List.
     */
    private String propertyName;

    public String getPropertyName() {
        StringHelper.checkNotEmpty("field:propertyName", propertyName);
        return propertyName;
    }

    public void setPropertyName(final String propertyName) {
        StringHelper.checkNotEmpty("parameter:propertyName", propertyName);
        this.propertyName = propertyName;
    }

    /**
     * The cached copy of the values referenced by this List
     */
    private String propertyValue;

    protected String getPropertyValue() {
        StringHelper.checkNotNull("field:propertyValue", propertyValue);
        return propertyValue;
    }

    protected boolean hasPropertyValue() {
        return null != propertyValue;
    }

    protected void setPropertyValue(final String propertyValue) {
        StringHelper.checkNotNull("parameter:propertyValue", propertyValue);
        this.propertyValue = propertyValue;
    }

    protected String getObjectPropertyValue() {
        return DomHelper.getProperty(this.getObject(), this.getPropertyName());
    }

    protected void setObjectPropertyValue(final String objectPropertyValue) {
        DomHelper.setProperty(this.getObject(), this.getPropertyName(), objectPropertyValue);
        this.increaseModificationCounter();
    }

    /**
     * This list contains the individual string or element value objects.
     */
    private List list;

    protected List getList() {
        ObjectHelper.checkNotNull("field:list", list);
        return this.list;
    }

    protected void setList(final List list) {
        ObjectHelper.checkNotNull("parameter:list", list);
        this.list = list;
    }

    protected void createList() {
        this.setList(new ArrayList());
    }

    public String toString() {
        return super.toString() + ", object: " + object + ", propertyName[" + propertyName + "], propertyValue["
                + propertyValue + "], list: " + list + ", modificationCounter: " + modificationCounter;
    }
}
