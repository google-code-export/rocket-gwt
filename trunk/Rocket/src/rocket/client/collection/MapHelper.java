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

import java.util.Iterator;
import java.util.Map;

import rocket.client.util.ObjectHelper;

/**
 * An assortment of useful methods relating to a map.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MapHelper {

    /**
     * Searches the given map for the key that contains the value parameter:value.
     * 
     * @param map
     * @param value
     * @return The key or null if value is not present in the parameter:map
     */
    public static Object getKey(final Map map, final Object value) {
        ObjectHelper.checkNotNull("parameter:map", map);
        ObjectHelper.checkNotNull("parameter:value", value);

        Object key = null;
        final Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            final Map.Entry entry = (Map.Entry) entries.next();
            if (value == entry.getValue()) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    private MapHelper() {
    }
}