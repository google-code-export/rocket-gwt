/*
 * Copyright Miroslav Pokorny
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

import java.util.Iterator;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class containing common properties and behaviour for all menu types, ContextMenu and Horizontal/VerticalMenuBar
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class Menu extends MenuWidget implements HasWidgets {

    public Menu() {
        super();
        this.setMenuListeners( createMenuListenerCollection() );
    }

    protected void handleMouseClick(final Event event) {
        DOM.eventCancelBubble(event, true);
    }

    protected void handleMouseOver(final Event event) {
        DOM.eventCancelBubble(event, true);
    }

    /**
     * If the mouse moves outside the menu hide the event.
     */
    protected void handleMouseOut(final Event event) {
        final Element target = DOM.eventGetToElement(event);
        if (target == null || DOM.isOrHasChild(this.getElement(), target)) {
            this.hide();
            DOM.eventCancelBubble(event, true);
        }
    }

    // ACTIONS
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This hides or closes all menu lists except for the top most one.
     */
    public void hide() {
        this.getMenuList().hide();
    }

    // PANEL ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

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
        return this.getMenuList().getWidgetCount();
    }

    public Iterator iterator() {
        return this.getMenuList().iterator();
    }

    public void clear() {
        this.getMenuList().clear();
    }

    // PROPERTIES :::::::::::::::::::::::::::::::::::::::::::::::::::::

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
        menuList.setMenu(this);
    }

    // MENU LISTENER HANDLING
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

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

    protected MenuListenerCollection createMenuListenerCollection() {
        return new MenuListenerCollection();
    }

    public void addMenuListener(final MenuListener listener) {
        ObjectHelper.checkNotNull("parameter:listener", listener);
        this.getMenuListeners().add(listener);
    }

    public void removeMenuListener(final MenuListener listener) {
        this.getMenuListeners().remove(listener);
    }

    public String toString() {
        return super.toString() + ", menuListeners: " + menuListeners;
    }
}
