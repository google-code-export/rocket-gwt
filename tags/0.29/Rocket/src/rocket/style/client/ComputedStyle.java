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
package rocket.style.client;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import rocket.util.client.Destroyable;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.Element;

/**
 * Presents a Map view of all the computed styles that apply to an element.
 * 
 * Using keys is always safe whilst searching for a value is not recommended.
 * <ul>
 * <li>entrySet</li>
 * <li>keySet</li>
 * <li>containsValue</li>
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ComputedStyle extends AbstractMap implements Destroyable {

    public int size() {
        int counter = 0;
        final Iterator iterator = this.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        return counter;
    }

    public Object put(final Object key, final Object value) {
        throw new UnsupportedOperationException("put");
    }

    public Object remove(final Object key) {
        throw new UnsupportedOperationException("remove");
    }

    public Object get(final Object key) {
        return this.getStylePropertyValue((String) key);
    }

    /**
     * Factory method which creates a new StylePropertyValue and populates it with a string value if the computed style property is
     * available.
     * 
     * @param propertyName
     * @return
     */
    protected StylePropertyValue getStylePropertyValue(final String propertyName) {
        StylePropertyValue value = null;
        String stringValue = StyleHelper.getComputedStyleProperty(this.getElement(), propertyName);
        if (false == StringHelper.isNullOrEmpty(stringValue)) {
            value = new StylePropertyValue();
            value.setString(stringValue);
        }
        return value;
    }

    public boolean containsKey(final Object key) {
        return null != this.get(key);
    }

    public Set entrySet() {
        return new ComputedStyleEntrySet();
    }

    /**
     * Implements a Set view of all the computed styles belonging to an Element.
     */
    class ComputedStyleEntrySet extends AbstractSet {
        public int size() {
            int counter = 0;
            final Iterator iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                counter++;
            }
            return counter;
        }

        public Iterator iterator() {
            return new ComputedStyleEntrySetIterator();
        }
    }

    /**
     * This iterator may be used to visit all computed style entries.
     */
    class ComputedStyleEntrySetIterator implements Iterator {

        {
            this.setCursor(0);
        }

        public boolean hasNext() {
            return this.getCursor() < this.getPropertyNames().length;
        }

        public Object next() {
            final int cursor = this.getCursor();
            final String[] propertyNames = this.getPropertyNames();
            if (cursor >= propertyNames.length) {
                throw new NoSuchElementException();
            }
            final String key = propertyNames[cursor];
            final Object value = ComputedStyle.this.get(key);
            this.setCursor(cursor + 1);
            return new Map.Entry() {
                public Object getKey() {
                    return key;
                }

                public Object getValue() {
                    return value;
                }

                public Object setValue(final Object newValue) {
                    return ComputedStyle.this.put(key, newValue);
                }
            };
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * An array containing all the property names for the native object.
         */
        String[] propertyNames;

        String[] getPropertyNames() {
            if (false == this.hasPropertyNames()) {
                this.setPropertyNames(StyleHelper.getComputedStylePropertyNames(ComputedStyle.this.getElement()));
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
         * This cursor points to the next visitable element.
         */
        int cursor = 0;

        int getCursor() {
            return this.cursor;
        }

        void setCursor(final int cursor) {
            this.cursor = cursor;
        }
    }

    public void destroy() {
        this.clearElement();
    }

    /**
     * The native element being viewed as a Map
     */
    private Element element;

    public Element getElement() {
        ObjectHelper.checkNotNull("field:element", element);
        return element;
    }

    public boolean hasElement() {
        return null != this.element;
    }

    public void setElement(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        this.element = element;
    }

    public void clearElement() {
        this.element = null;
    }

    public String toString() {
        return super.toString() + ", element: " + element;
    }
}
