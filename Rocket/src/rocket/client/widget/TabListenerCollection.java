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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.ui.Widget;

public class TabListenerCollection {
    public TabListenerCollection(){
        this.setListeners( new ArrayList() );
    }
    /**
     * A list containing listeners to the various page change events.
     */
    private List listeners;

    public List getListeners() {
        ObjectHelper.checkNotNull("field:listeners", listeners);
        return listeners;
    }

    public void setListeners(final List listeners) {
        ObjectHelper.checkNotNull("parameter:listeners", listeners);
        this.listeners = listeners;
    }

    public void add(final TabListener tabListener) {
        ObjectHelper.checkNotNull("parameter:tabListener", tabListener);

        this.getListeners().add(tabListener);
    }

    public void remove(final TabListener tabListener) {
        ObjectHelper.checkNotNull("parameter:tabListener", tabListener);

        this.getListeners().remove(tabListener);
    }


    public boolean fireBeforeTabSelected(final String title, final Widget widget) {
        boolean doSelect = true;
        final Iterator listeners = this.getListeners().iterator();

        while (listeners.hasNext()) {
            final TabListener listener = (TabListener) listeners.next();
            if (!listener.onBeforeTabSelected(title, widget )) {
                doSelect = false;
                break;
            }
        }
        return doSelect;
    }

    public void fireTabSelected(final String title, final Widget widget) {
        final Iterator listeners = this.getListeners().iterator();

        while (listeners.hasNext()) {
            final TabListener listener = (TabListener) listeners.next();
            listener.onTabSelected(title, widget);
        }
    }

    public boolean fireBeforeTabClosed(final String title, final Widget widget) {
        boolean removeTab = true;
        final Iterator listeners = this.getListeners().iterator();

        while (listeners.hasNext()) {
            final TabListener listener = (TabListener) listeners.next();
            if (!listener.onBeforeTabClosed(title, widget)) {
                removeTab = false;
                break;
            }
        }
        return removeTab;
    }

    public void fireTabClosed(final String title, final Widget widget) {
        final Iterator listeners = this.getListeners().iterator();

        while (listeners.hasNext()) {
            final TabListener listener = (TabListener) listeners.next();
            listener.onTabClosed(title, widget);
        }
    }
}
