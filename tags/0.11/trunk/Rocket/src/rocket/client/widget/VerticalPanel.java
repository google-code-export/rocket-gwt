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
package rocket.client.widget;

import java.util.Iterator;

import rocket.client.collection.IteratorView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

/**
 * Fixes the iterator returned by VerticalPanel so that remove() works and being fail fast
 * All other VerticalPanel functionality remains unchanged.
 * @author Miroslav Pokorny (mP)
 */
public class VerticalPanel extends com.google.gwt.user.client.ui.VerticalPanel {

    public boolean insert( final Widget widget, final int beforeIndex ){
        final boolean inserted = super.insert( widget, beforeIndex );
        if( inserted ){
            this.incrementModificationCounter();
        }
        return inserted;
    }

    public boolean remove( final Widget widget ){
        final boolean removed = super.remove( widget );
        if( removed ){
            this.incrementModificationCounter();
        }
        return removed;
    }

    public Iterator iterator(){
        final VerticalPanel that = this;

        final IteratorView iterator = new IteratorView() {
            // ITERATOR VIEW :::::::::::::::::::::::::::::::::::::::::::::::
            protected boolean hasNext0() {
                return this.getIndex() < that.getWidgetCount();
            }

            protected Object next0(int type) {
                final Widget widget = that.getWidget( this.getIndex() );
                this.setLastVisited( widget );
                return widget;
            }

            protected void leavingNext() {
                this.setIndex( this.getIndex() + 1 );
            }

            protected void remove0() {
                if( ! this.hasLastVisited() ){
                    throw new UnsupportedOperationException("Attempt to remove before calling next()");
                }
                if( ! that.remove( this.getLastVisited())){
                    throw new RuntimeException( "Unable to remove widget from " + GWT.getTypeName( that ));
                }
                this.clearLastVisited();

                this.setIndex( this.getIndex() - 1 );
            }

            protected int getParentModificationCounter() {
                return that.getModificationCounter();
            }
            // IMPL
            protected int index;

            protected int getIndex(){
                return index;
            }
            protected void setIndex( final int index ){
                this.index = index;
            }

            Widget lastVisited;

            protected Widget getLastVisited(){
                return lastVisited;
            }
            protected boolean hasLastVisited(){
                return null != lastVisited;
            }
            protected void setLastVisited( final Widget lastVisited ){
                this.lastVisited = lastVisited;
            }
            protected void clearLastVisited(){
                this.lastVisited = null;
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

    protected void incrementModificationCounter(){
        this.setModificationCounter( this.getModificationCounter() + 1 );
    }
}
