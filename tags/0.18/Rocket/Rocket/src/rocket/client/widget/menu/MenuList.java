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

import rocket.client.collection.CollectionHelper;
import rocket.client.dom.DomHelper;
import rocket.client.style.StyleConstants;
import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class containing common behaviour for both types of MenuLists.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class MenuList extends MenuWidget implements HasWidgets {

    protected MenuList() {
        super();
    }

    // EVENTS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected void handleMouseClick(final Event event) {
        DOM.eventCancelBubble(event, true);
    }

    protected void handleMouseOver(final Event event) {
        DOM.eventCancelBubble(event, true);
    }

    protected void handleMouseOut(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final Element targetElement = DomHelper.eventGetToElement(event);
            if (DOM.isOrHasChild(this.getElement(), targetElement)) {
                DOM.eventCancelBubble(event, true);
                break;
            }
            this.hideOpened();
            break;
        }
    }

    // ACTIONS
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void open() {
        if (this.isHideable()) {
            DOM.setStyleAttribute(this.getElement(), StyleConstants.CSS_DISPLAY, "block");
        }
    }

    public void hide() {
        if (this.isHideable()) {
            DOM.setStyleAttribute(this.getElement(), StyleConstants.CSS_DISPLAY, "none");
        }
        this.hideOpened();
    }

    /**
     * This flag indicates whether or not this list shoudl be made invisible (display:none) when this list is asked to hide
     * 
     * The HorizontalMenuList hanging off a HorizontalMenuBar should not be made invisible whilst its child sub menu menu lists probably
     * should.
     */
    private boolean hideable;

    public boolean isHideable() {
        return this.hideable;
    }

    public void setHideable(final boolean hideable) {
        this.hideable = hideable;
    }

    // PANEL
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public abstract int getWidgetCount();

    public void add(final Widget widget) {
        this.insert(widget, this.getWidgetCount());
    }

    public abstract void insert(final Widget widget, final int beforeIndex);

    protected void afterInsert(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final AbstractMenuItem menuItem = (AbstractMenuItem) widget;
        menuItem.setParentMenuList(this);
    }

    public abstract Widget get(final int index);

    public void clear() {
        CollectionHelper.removeAll(this.iterator());
    }

    public abstract Iterator iterator();

    public boolean remove(Widget widget) {
        final boolean removed = this.remove0(widget);
        if (removed) {
            if (this.hasOpened() && widget == this.getOpened()) {
                this.clearOpened();
            }
        }
        return removed;
    }

    /**
     * Sub-classes must attempt to remove the given widget
     * 
     * @param widget
     * @return
     */
    protected abstract boolean remove0(Widget widget);

    // MENU LIST ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * If this is the topmost menuList this property will be set otherwise children will need to check their parent until the top is
     * reached.
     */
    private Menu menu;

    public Menu getMenu() {
        Menu menu = this.menu;

        // if this widget doesnt have a menu property set check its parent...
        if (false == this.hasMenu()) {
            menu = this.getParentMenuList().getMenu();
        }

        ObjectHelper.checkNotNull("menu", menu);
        return menu;
    }

    protected boolean hasMenu() {
        return null != this.menu;
    }

    public void setMenu(final Menu menu) {
        ObjectHelper.checkNotNull("parameter:menu", menu);
        this.menu = menu;
    }

    /**
     * All menuLists will have a parent except if they have been added to a menu.
     */
    private MenuList parentMenuList;

    public MenuList getParentMenuList() {
        ObjectHelper.checkNotNull("field:parentMenuList", parentMenuList);
        return this.parentMenuList;
    }

    public boolean hasParentMenuList() {
        return null != this.parentMenuList;
    }

    public void setParentMenuList(final MenuList parentMenuList) {
        ObjectHelper.checkNotNull("parameter:parentMenuList", parentMenuList);
        this.parentMenuList = parentMenuList;
    }

    /**
     * This controls which direction the list is opened.
     */
    private MenuListOpenDirection openDirection;

    public MenuListOpenDirection getOpenDirection() {
        ObjectHelper.checkNotNull("field:openDirection", this.openDirection);
        return this.openDirection;
    }

    public void setOpenDirection(final MenuListOpenDirection openDirection) {
        ObjectHelper.checkNotNull("parameter:openDirection", openDirection);
        this.openDirection = openDirection;
    }

    /**
     * This property will contain the SubMenuItem item that is currently open. It will be cleared whenever another child item is selected or
     * this list itself is hidden.
     */
    private SubMenuItem opened;

    protected SubMenuItem getOpened() {
        ObjectHelper.checkNotNull("field:opened", opened);
        return this.opened;
    }

    protected boolean hasOpened() {
        return null != this.opened;
    }

    protected void setOpened(final SubMenuItem opened) {
        ObjectHelper.checkNotNull("parameter:opened", opened);
        this.opened = opened;
    }

    protected void clearOpened() {
        this.opened = null;
    }

    /**
     * Hides any opened SubMenuItem if one is present.
     */
    protected void hideOpened() {
        if (this.hasOpened()) {
            this.getOpened().hide();
            this.clearOpened();
        }
    }

    public String toString() {
        String html = DOM.getInnerText(this.getElement());
        html = html.replace('\n', ' ');
        html = html.replace('\r', ' ');
        return super.toString() + "[" + html + "]";
    }
}
