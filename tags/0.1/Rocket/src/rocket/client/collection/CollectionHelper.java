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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.client.util.ObjectHelper;

/**
* A collection of methods that are often used when working with Collections.
* @author Miroslav Pokorny (mP)
*/
public class CollectionHelper {
    public static void removeAll( final Iterator iterator ){
        ObjectHelper.checkNotNull( "parameter:iterator", iterator );

        while( iterator.hasNext() ){
            iterator.next();
            iterator.remove();
        }
    }

    /**
     * Copies all the elements from the iterator into an array.
     * @param iterator
     * @return
     */
    public static Object[] toArray( final Iterator iterator ){
        ObjectHelper.checkNotNull( "parameter:iterator", iterator );

        final List list = new ArrayList();
        while( iterator.hasNext() ){
            list.add( iterator.next() );
        }
        return list.toArray();
    }

    /**
     * Copies all the elements from the iterator into a List
     * @param iterator
     * @return
     */
    public static List toList( final Iterator iterator ){
        ObjectHelper.checkNotNull( "parameter:iterator", iterator );

        final List list = new ArrayList();
        while( iterator.hasNext() ){
            list.add( iterator.next() );
        }
        return list;
    }
}
