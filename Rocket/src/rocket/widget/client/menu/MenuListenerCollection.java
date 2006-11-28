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
package rocket.widget.client.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.ui.Widget;

public class MenuListenerCollection {

    public MenuListenerCollection() {
        this.setListeners(new ArrayList());
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

    public void add(final MenuListener verticalListListener) {
        ObjectHelper.checkNotNull("parameter:verticalListListener", verticalListListener);

        this.getListeners().add(verticalListListener);
    }

    public void remove(final MenuListener verticalListListener) {
        ObjectHelper.checkNotNull("parameter:verticalListListener", verticalListListener);

        this.getListeners().remove(verticalListListener);
    }

    // FIRE EVENTS ::::::::::::::::::::::::::::::::::::::

    public void fireMenuCancelled(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final Iterator listeners = this.getListeners().iterator();

        while (listeners.hasNext()) {
            final MenuListener listener = (MenuListener) listeners.next();
            listener.onMenuCancelled(widget);
        }
    }

    public boolean fireBeforeMenuOpened(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final Iterator listeners = this.getListeners().iterator();
        boolean open = true;

        while (listeners.hasNext()) {
            final MenuListener listener = (MenuListener) listeners.next();
            if (!listener.onBeforeMenuOpened(widget)) {
                open = false;
                break;
            }
        }
        return open;
    }

    public void fireMenuOpened(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final Iterator listeners = this.getListeners().iterator();

        while (listeners.hasNext()) {
            final MenuListener listener = (MenuListener) listeners.next();
            listener.onMenuOpened(widget);
        }
    }

}
