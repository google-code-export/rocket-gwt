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
package rocket.widget.client;

import java.util.Iterator;

import rocket.collection.client.IteratorView;

import com.google.gwt.user.client.ui.Widget;

/**
 * Fixes the iterator returned by HorizontalPanel so that remove() works and being failFast. All other HorizontalPanel functionality remains
 * unchanged.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class HorizontalPanel extends com.google.gwt.user.client.ui.HorizontalPanel {

    public void insert(final Widget widget, final int beforeIndex) {
        super.insert(widget, beforeIndex);
        this.incrementModificationCounter();
    }

    public boolean remove(final Widget widget) {
        final boolean removed = super.remove(widget);
        if (removed) {
            this.incrementModificationCounter();
        }
        return removed;
    }

    public Iterator iterator() {
        final HorizontalPanel that = this;

        final Iterator wrapped = super.iterator();
        final IteratorView iterator = new IteratorView() {
            // ITERATOR VIEW :::::::::::::::::::::::::::::::::::::::::::::::
            protected boolean hasNext0() {
                // return this.getIndex() < that.getWidgetCount();
                return wrapped.hasNext();
            }

            protected Object next0() {
                // final Widget widget = that.getWidget( this.getIndex() );
                // this.setLastVisited( widget );
                // return widget;
                return wrapped.next();
            }

            protected void afterNext() {
                // this.setIndex( this.getIndex() + 1 );
            }

            protected void remove0() {
                // if( ! this.hasLastVisited() ){
                // throw new UnsupportedOperationException("Attempt to remove
                // before calling next()");
                // }
                // if( ! that.remove( this.getLastVisited())){
                // throw new RuntimeException( "Unable to remove widget from " +
                // GWT.getTypeName( that ));
                // }
                // this.clearLastVisited();
                wrapped.remove();
                // this.setIndex( this.getIndex() - 1 );
            }

            protected int getModificationCounter() {
                return that.getModificationCounter();
            }        
        };

        iterator.syncModificationCounters();
        return iterator;
    }

    /**
     * Helps keep track of concurrent modification of the parent.
     */
    private int modificationCounter;

    protected int getModificationCounter() {
        return this.modificationCounter;
    }

    protected void setModificationCounter(final int modificationCounter) {
        this.modificationCounter = modificationCounter;
    }

    protected void incrementModificationCounter() {
        this.setModificationCounter(this.getModificationCounter() + 1);
    }
}
