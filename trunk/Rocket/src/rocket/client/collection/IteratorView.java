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
import java.util.NoSuchElementException;

/**
 * This iterator provides a fast failsafe view of another collection. It lso provides automatic support for switching based the type of view
 * for the collection. THis is allows a single iterator class implementation for a view over a map requiring only a switch based on the type
 * to fetch the next object when {@link #next} is called.
 * 
 * Immediately after creating an instance {@link #syncModificationCounters() } must be called to synchronize modification counters so that
 * the iterator may fail fast at the right time.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class IteratorView implements Iterator {

    protected IteratorView() {
    }

    public boolean hasNext() {
        this.modificationGuard();
        return hasNext0();
    }

    protected abstract boolean hasNext0();

    public Object next() {
        this.modificationGuard();
        if (false == this.hasNext()) {
            throw new NoSuchElementException();
        }

        final Object nexted = this.next0(this.getViewType());
        this.leavingNext();
        this.syncModificationCounters();
        return nexted;
    }

    protected abstract Object next0(final int type);

    protected abstract void leavingNext();

    /**
     * This enum keeps track of whether keys/views or entries are being viewed.
     */
    private int viewType;

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(final int viewType) {
        this.viewType = viewType;
    }

    public void remove() {
        modificationGuard();
        this.remove0();
        this.syncModificationCounters();
    }

    protected abstract void remove0();

    /**
     * Helps keep track of concurrent modification of the parent.
     */
    private int modificationCounter;

    protected int getModificationCounter() {
        return this.modificationCounter;
    }

    public void setModificationCounter(final int modificationCounter) {
        this.modificationCounter = modificationCounter;
    }

    protected void modificationGuard() {
        if (this.getParentModificationCounter() != this.getModificationCounter()) {
            throw new RuntimeException("ConcurrentModification");
        }
    }

    public void syncModificationCounters() {
        this.setModificationCounter(this.getParentModificationCounter());
    }

    protected abstract int getParentModificationCounter();

    public String toString() {
        return super.toString() + ", modificationCounter: " + modificationCounter + ", viewType: " + viewType;
    }
}