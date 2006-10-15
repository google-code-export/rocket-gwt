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

import rocket.client.util.ObjectHelper;

/**
 * This iterator provides automatic support that may be queried for the last element to visited via {@link #next}. This is particularly
 * useful within the a subclass implementation of {@link #remove}.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class VisitedRememberingIterator extends IteratorWrapper implements Iterator {

    // ITERATOR
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public boolean hasNext() {
        return this.getIterator().hasNext();
    }

    public Object next() {
        final Object lastVisited = this.getIterator().next();
        this.setLastVisited(lastVisited);
        return lastVisited;
    }

    public void remove() {
        this.getIterator().remove();
        this.clearLastVisited();
    }

    // IMPL
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * The object that was last visited ie the object returned by the last call to {@link #next}
     */
    private Object lastVisited;

    private boolean lastVisitedSet;

    public Object getLastVisited() {
        ObjectHelper.checkPropertySet("lastVisited", this, this.lastVisitedSet);
        return lastVisited;
    }

    public boolean hasLastVisited() {
        return this.lastVisitedSet;
    }

    public void setLastVisited(final Object lastVisited) {
        this.lastVisited = lastVisited;
        this.lastVisitedSet = true;
    }

    public void clearLastVisited() {
        this.lastVisited = null;
        this.lastVisitedSet = false;
    }
}
