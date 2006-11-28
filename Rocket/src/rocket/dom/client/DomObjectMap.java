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

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rocket.collection.client.IteratorView;
import rocket.util.client.ObjectHelper;
import rocket.util.client.ObjectWrapper;
import rocket.util.client.SystemHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * This class may be sub-classed to present a Map view of all the propeties belonging to an native browser object.
 * 
 * The keys for such a map are typically strings with the value being a wrapper java bean.
 * 
 * Invoking {@link #destroy} ]also destroys any cached DomObjectMapValue instances.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class DomObjectMap extends AbstractMap implements Destroyable {

    protected DomObjectMap() {
        super();

        this.createWrappers();
    }

    // DOM OBJECT MAP ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Verifies the type of a key belonging to this map
     * 
     * @param propertyName
     */
    protected void checkKeyType(final Object propertyName) {
        if (false == (propertyName instanceof String)) {
            SystemHelper.handleAssertFailure("parameter:key", "The map " + GWT.getTypeName(propertyName)
                    + " only works with String keys and not " + GWT.getTypeName(propertyName));
        }
    }

    /**
     * This method presents an opportunity for various present properties to be ignored from the view.
     * 
     * For example a map may wish to ignore non String values in which case it would return false if the propertyName is not a string.
     * 
     * @param The
     *            property's name
     * @return
     */
    protected abstract boolean hasProperty(final String propertyName);

    /**
     * Verifies the type of a value belonging to this map.
     * 
     * @param value
     */
    protected void checkValueType(final Object value) {
        if (false == this.isValidValueType(value)) {
            SystemHelper.handleAssertFailure("parameter:key", "Invalid value type " + GWT.getTypeName(value));
        }
    }

    protected abstract boolean isValidValueType(final Object value);

    /**
     * Sub-classes must create the wrapper object for a particular value on demand.
     * 
     * @param propertyName
     * @return
     */
    protected abstract DomObjectMapValue createValueWrapper(final String propertyName);

    protected abstract void adopt(final String propertyName, final ObjectWrapper valueWrapper);

    protected abstract void disown(final ObjectWrapper valueWrapper);

    /**
     * Releases the reference to the native object and also destroys any existing wrappers.
     */
    public void destroy() {
        this.clearObject();
        this.clearWrappers();
    }

    // MAP ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public int size() {
        return ObjectHelper.getPropertyCount(this.getObject());
    }

    public boolean containsKey(final Object key) {
        checkKeyType(key);

        final String propertyName = (String) key;
        return this.hasProperty(propertyName);
    }

    /**
     * Retrieves a wrapper for a object property identified by its key.
     */
    public Object get(final Object key) {
        checkKeyType(key);

        DomObjectMapValue value = null;
        while (true) {
            // check if the property exists or should be ignored ?
            final String propertyName = (String) key;
            if (false == this.hasProperty(propertyName)) {
                break;
            }
            final Map wrappers = this.getWrappers();
            value = (DomObjectMapValue) wrappers.get(propertyName);
            if (null != value) {
                break;
            }

            // not found in the cache create wrapper.
            value = this.createValueWrapper(propertyName);
            this.adopt(propertyName, value);
            wrappers.put(propertyName, value);
            break;
        }
        return value;
    }

    /**
     * Puts or replaces a property value for a map. Puts or replaces an existing value
     * 
     * @param key
     *            The property name
     * @param value
     *            The object wrapper.
     * @return The previous value if one was present.
     */
    public Object put(final Object key, final Object value) {
        checkKeyType(key);
        checkValueType(value);

        // use get to fetch the value being replaced.
        final Map wrappers = this.getWrappers();
        final DomObjectMapValue replaced = (DomObjectMapValue) wrappers.put(key, value);

        // disown the replaced object...
        if (null != replaced) {
            this.disown((DomObjectMapValue) replaced);
        }

        // adopting the object should also update the native object
        this.adopt((String) key, (DomObjectMapValue) value);

        this.incrementModificationCounter();
        return replaced;
    }

    /**
     * Removes a property value from this object.
     * 
     * @param key
     *            The property's name.
     */
    public Object remove(final Object key) {
        checkKeyType(key);

        // get the wrapper for the property that is about to be removed...
        final String propertyName = (String) key;
        final DomObjectMapValue removed = (DomObjectMapValue) this.get(propertyName);

        // if get didnt work no need to remove...
        if (null != removed) {
            // remove the property
            this.removeProperty(propertyName);

            // disown
            this.disown(removed);
            this.incrementModificationCounter();
        }
        return removed;
    }

    protected void removeProperty(final String propertyName) {
        ObjectHelper.removeProperty(this.getObject(), propertyName);
    }

    // public void clear() {
    // final Iterator iterator = this.keySet().iterator();
    // while (iterator.hasNext()) {
    // iterator.next();
    // iterator.remove();
    // }
    // }

    // SETS VIEWS
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Returns a Set view of all keys belonging to this Map.
     */
    public Set keySet() {

        return new AbstractSet() {
            public int size() {
                return DomObjectMap.this.size();
            }

            public boolean contains(final Object propertyName) {
                return DomObjectMap.this.containsKey(propertyName);
            }

            public boolean add(final Object propertyName) {
                throw new UnsupportedOperationException("Cannot call add() upon a "
                        + GWT.getTypeName(DomObjectMap.this) + ".keySet() Set");
            }

            public boolean remove(final Object propertyName) {
                return null != DomObjectMap.this.remove(propertyName);
            }

            public void clear() {
                DomObjectMap.this.clear();
            }

            public Iterator iterator() {
                final DomObjectMapIterator iterator = new DomObjectMapIterator() {
                    protected Object next1(final String propertyName) {
                        return propertyName;
                    }
                };

                iterator.syncModificationCounters();
                return iterator;
            }
        };
    }

    /**
     * Returns a collection view of all values belonging to this map.
     */
    public Collection values() {
        return new AbstractCollection() {

            public boolean add(final Object property) {
                throw new UnsupportedOperationException("A " + GWT.getTypeName(DomObjectMap.this)
                        + " values Collection is read only");
            }

            public boolean contains(final Object object) {
                return DomObjectMap.this.containsValue(object);
            }

            public void clear() {
                DomObjectMap.this.clear();
            }

            public Iterator iterator() {
                final DomObjectMapIterator iterator = new DomObjectMapIterator() {
                    protected Object next1(final String propertyName) {
                        return DomObjectMap.this.get(propertyName);
                    }
                };
                iterator.syncModificationCounters();
                return iterator;
            }

            public boolean remove(final Object value) {
                DomObjectMap.this.checkValueType(value);
                final DomObjectMapValue wrapper = (DomObjectMapValue) value;
                return null != DomObjectMap.this.remove(wrapper.getPropertyName());
            }

            public int size() {
                return DomObjectMap.this.size();
            }
        };
    }

    /**
     * Returns a read only set that includes all the entries for this Map.
     */
    public Set entrySet() {
        return new AbstractSet() {
            public int size() {
                return DomObjectMap.this.size();
            }

            public boolean add(final Object propertyName) {
                throw new UnsupportedOperationException("A " + GWT.getTypeName(DomObjectMap.this)
                        + " entrySet is read only");
            }

            public boolean remove(final Object propertyName) {
                return null != DomObjectMap.this.remove(propertyName);
            }

            public void clear() {
                DomObjectMap.this.clear();
            }

            public Iterator iterator() {
                final DomObjectMapIterator iterator = new DomObjectMapIterator() {
                    protected Object next1(final String propertyName) {
                        final DomObjectMapIterator that = this;
                        return new Map.Entry() {
                            public Object getKey() {
                                return propertyName;
                            }

                            public Object getValue() {
                                return DomObjectMap.this.get(propertyName);
                            }

                            public Object setValue(final Object value) {
                                final Object replaced = DomObjectMap.this.put(propertyName, value);
                                that.syncModificationCounters();
                                return replaced;
                            }
                        };
                    }
                };
                iterator.syncModificationCounters();
                return iterator;
            }
        };
    }

    /**
     * The native object being viewed as a Map
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
     * This map is used to cache wrappers for values belonging to this map.
     */
    private Map wrappers;

    protected Map getWrappers() {
        ObjectHelper.checkNotNull("field:wrappers", this.wrappers);
        return this.wrappers;
    }

    protected void setWrappers(final Map wrappers) {
        ObjectHelper.checkNotNull("parameter:wrappers", wrappers);
        this.wrappers = wrappers;
    }

    protected void createWrappers() {
        this.setWrappers(new HashMap());
    }

    protected void clearWrappers() {
        final Iterator values = this.getWrappers().values().iterator();
        while (values.hasNext()) {
            final DomObjectMapValue wrapper = (DomObjectMapValue) values.next();
            wrapper.destroy();
            values.remove();
        }
    }

    // ITERATOR ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
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
     * This iterator is a convenient base class for any iterator view of this Map. The keySet/entrySet/values collection need to implement
     * {@link #next1(String)} to return the appropriate value.
     * 
     * @author Miroslav Pokorny (mP)
     */
    abstract class DomObjectMapIterator extends IteratorView {

        DomObjectMapIterator() {
            super();
        }

        protected boolean hasNext0() {
            boolean more = false;
            int cursor = this.getCursor();
            final String[] propertyNames = this.getPropertyNames();
            final int length = propertyNames.length;
            while (cursor < length) {
                final String propertyName = propertyNames[cursor];
                if (DomObjectMap.this.hasProperty(propertyName)) {
                    more = true;
                    break;
                }
                cursor++;
            }
            this.setCursor(cursor);
            return more;
        }

        protected Object next0() {
            final int cursor = this.getCursor();
            final String propertyName = this.getPropertyNames()[cursor];
            return this.next1(propertyName);
        }

        protected abstract Object next1(final String propertyName);

        protected void afterNext() {
            this.setCursor(this.getCursor() + 1);
        }

        protected void remove0() {
            final int index = this.getCursor() - 1;// necessary because next advances cursor by 1
            final String propertyName = this.getPropertyNames()[index];
            DomObjectMap.this.remove(propertyName);
        }

        protected int getModificationCounter() {
            return DomObjectMap.this.getModificationCounter();
        }

        /**
         * An array containing all the property names for the native object.
         */
        String[] propertyNames;

        String[] getPropertyNames() {
            if (false == this.hasPropertyNames()) {
                this.setPropertyNames(this.createPropertyNames());
            }
            return this.propertyNames;
        }

        boolean hasPropertyNames() {
            return null != this.propertyNames;
        }

        void setPropertyNames(final String[] propertyNames) {
            this.propertyNames = propertyNames;
        }

        /**
         * Returns an array containing all the property names for the native object being wrapped.
         * 
         * @return
         */
        String[] createPropertyNames() {
            final JavaScriptObject propertyNames = copyPropertyNamesIntoArray(DomObjectMap.this.getObject());

            final int length = ObjectHelper.getInteger(propertyNames, "length");
            final String[] stringArray = new String[length];
            for (int i = 0; i < length; i++) {
                stringArray[i] = ObjectHelper.getString(propertyNames, i);
            }
            return stringArray;
        }

        native JavaScriptObject copyPropertyNamesIntoArray(final JavaScriptObject object)/*-{
         var array = new Array();
         var i = 0;
         for( propertyName in object ){
         array[ i ] = propertyName;
         i++;
         }            
         return array;
         }-*/;

        /**
         * This cursor points to the next visitable element.
         */
        int cursor;

        int getCursor() {
            return this.cursor;
        }

        void setCursor(final int cursor) {
            this.cursor = cursor;
        }
    }

    // OBJECT :::::::::::::::::::::::::::::::::::::

    /**
     * The hashcode of this map is built using a summing of all keys.
     */
    public int hashCode() {
        int hashcode = 0;
        final Iterator keys = this.keySet().iterator();
        while (keys.hasNext()) {
            final Object key = keys.next();
            hashcode = hashcode + key.hashCode();
        }
        return hashcode;
    }
}
