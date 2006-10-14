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
package rocket.client.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.SystemHelper;

/**
 * A special type of map that only keeps at a maximum a set number of entries.
 * 
 * It automatically removes the oldest item when the map becomes full and a new item is added.
 * 
 * NB Both the keySet and entrySet Sets are not ordered using the key addition order.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class LeastRecentlyUsedMap extends HashMap {

    public LeastRecentlyUsedMap(final int maximumSize) {
        this.setMaximumSize(maximumSize);
        this.setOrder(new Vector());
    }

    public Object remove(final Object key) {
        ObjectHelper.checkNotNull("parameter:key", key);
        this.getOrder().remove(key);

        return super.remove(key);
    }

    public Object put(final Object key, final Object value) {
        ObjectHelper.checkNotNull("parameter:key", key);

        Object replaced = null;
        final int maximumSize = this.getMaximumSize();
        if (0 != maximumSize) {

            final List order = this.getOrder();

            if (this.containsKey(key)) {
                order.remove(key);
            } else {
                // if the map is full removed teh oldest item.
                if (order.size() == maximumSize) {
                    order.remove(0);
                }
            }

            // the most recent put always gets append on the end...
            order.add(key);

            replaced = super.put(key, value);
        }
        return replaced;
    }

    /**
     * This list contains the keys of the added items, older items appear at the ffront of the list, the newest is the last item.
     */
    private List order;

    public List getOrder() {
        ObjectHelper.checkNotNull("field:order", this.order);
        return this.order;
    }

    public void setOrder(final List order) {
        ObjectHelper.checkNotNull("parameter:order", order);
        this.order = order;
    }

    /**
     * This value is the maximum size of this map. When the size of the map is equal to the map the oldest item is dropped.
     */
    private int maximumSize;

    private boolean maximumSizeSet;

    public int getMaximumSize() {
        if (false == maximumSizeSet) {
            SystemHelper.handleAssertFailure("field:maximumSize", "The field:maximumSize hass not yet been set, this: "
                    + this);
        }
        PrimitiveHelper.checkGreaterThanOrEqual("field:maximumSize", maximumSize, 0);
        return this.maximumSize;
    }

    public void setMaximumSize(final int maximumSize) {
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:maximumSize", maximumSize, 0);
        this.maximumSize = maximumSize;
        this.maximumSizeSet = true;
    }

    public String toString() {
        return super.toString() + ", maximumSize: " + maximumSize + ", order: " + order;
    }
}