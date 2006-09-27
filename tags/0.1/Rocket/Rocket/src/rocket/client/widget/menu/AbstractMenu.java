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
package rocket.client.widget.menu;

import java.util.Iterator;

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractMenu extends Composite implements HasWidgets {

    protected AbstractMenu() {
        this.setMenuListeners(new MenuListenerCollection());
    }

    public void onBrowserEvent( final Event event ){
    	this.getMenuList().onBrowserEvent( event );
    }

    public void close() {
        this.getMenuList().close();
    }

    /**
     * WHen true indicates that a subMenu may be opened by simply having the mouse hover over a subMenuItem. When false the user must click
     * on the item to view the subMenu list.
     */
    private boolean autoOpen;

    public boolean isAutoOpen() {
        return autoOpen;
    }

    public void setAutoOpen(final boolean autoOpen) {
        this.autoOpen = autoOpen;
    }

    /**
     * This value determines the number of items that will appear in a list along with a expand control being visibile.
     */
    private int listLimit;

    public int getListLimit() {
        return this.listLimit;
    }

    public void setListLimit(final int listLimit) {
        this.listLimit = listLimit;
    }

    // PANEL :::::::::::::::::::::::::::::::::::::::::::::::

    protected void onDetatch() {
        super.onDetach();

        this.close();
    }

    public void add(final Widget widget) {
        this.insert(widget, this.getCount());
    }

    public void insert(final Widget widget, final int beforeIndex) {
        this.getMenuList().insert(widget, beforeIndex);
    }
    
    public boolean remove(final Widget widget) {
        return this.getMenuList().remove(widget);
    }

    public int getCount() {
        return this.getMenuList().getCount();
    }

    public Iterator iterator() {
        return this.getMenuList().iterator();
    }

    public void clear() {
        this.getMenuList().clear();
    }

    /**
     * A menuList is used as the container for all child menu widgets.
     */
    private MenuList menuList;

    protected MenuList getMenuList() {
        ObjectHelper.checkNotNull("field:menuList", menuList);
        return this.menuList;
    }

    protected boolean hasMenuList() {
        return null != this.menuList;
    }

    protected void setMenuList(final MenuList menuList) {
        ObjectHelper.checkNotNull("parameter:menuList", menuList);
        this.menuList = menuList;
        menuList.setMenu( this );
    }

    // MENU LISTENER HANDLING ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * A list of objects interested in menu events.
     */
    private MenuListenerCollection menuListeners;

    protected MenuListenerCollection getMenuListeners() {
        ObjectHelper.checkNotNull("field:menuListeners", this.menuListeners);
        return this.menuListeners;
    }

    protected void setMenuListeners(final MenuListenerCollection menuListeners) {
        ObjectHelper.checkNotNull("parameter:menuListeners", menuListeners);
        this.menuListeners = menuListeners;
    }

    public void addMenuListener(final MenuListener listener) {
        ObjectHelper.checkNotNull("parameter:listener", listener);
        this.getMenuListeners().add(listener);
    }

    public void removeMenuListener(final MenuListener listener) {
        this.getMenuListeners().remove(listener);
    }

    public String toString() {
        return super.toString() + ", autoOpen: " + autoOpen + ", listLimit: " + listLimit;
    }
}
