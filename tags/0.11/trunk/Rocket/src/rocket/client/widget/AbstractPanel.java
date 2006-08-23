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

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenient base class for all Panels, adding support for automatic creation of fail fast iterators.
 * @author Miroslav Pokorny (mP)
 */
public abstract class AbstractPanel extends Composite implements HasWidgets, Panel{

    protected AbstractPanel(){
    }

    public abstract boolean add( Widget widget );
    public abstract boolean remove( Widget widget );
    public abstract Iterator iterator();

    public int getIndex( final Widget widget ){
        ObjectHelper.checkNotNull( "parameter:widget", widget );
        int index = -1;
        final Iterator iterator = this.iterator();
        int i = 0;
        while( iterator.hasNext() ){
            final Widget otherWidget = (Widget) iterator.next();
            if( widget == otherWidget ){
                index = i;
                break;
            }
            i++;
        }
        return index;
    }

    public void clear(){
        final Iterator iterator = this.iterator();
        while( iterator.hasNext() ){
            this.remove( (Widget) iterator.next() );
        }
    }

    /**
     * This counter should be incremented each time a modification is made to this container.
     * It exists to help any iterators fail fast.
     */
    private int modificationCount;

    protected int getModificationCount() {
        return this.modificationCount;
    }

    public void setModificationCount(final int modificationCount) {
        this.modificationCount = modificationCount;
    }

    public String toString() {
        return super.toString() + ", modificationCount: " + modificationCount;
    }
}
