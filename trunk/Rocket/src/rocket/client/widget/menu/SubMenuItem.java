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

import rocket.client.dom.DomHelper;
import rocket.client.style.StyleConstants;
import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A SubMenuItem contains a text label and a list of further items.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class SubMenuItem extends AbstractMenuItem implements HasWidgets {

    public SubMenuItem() {
        super();

        this.initWidget(this.createSimplePanel());
    }

    // PANEL
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void add(final Widget widget) {
        this.insert(widget, this.getCount());
    }

    public void insert(final Widget widget, final int beforeIndex) {
        this.getMenuList().insert(widget, beforeIndex);
    }

    public Widget get(final int index) {
        return this.getMenuList().get(index);
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

    // ACTIONS
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void open() {
        final MenuList menuList = this.getMenuList();

        final Menu menu = menuList.getMenu();
        final MenuListenerCollection listeners = menu.getMenuListeners();
        if (listeners.fireBeforeMenuOpened(this)) {

            // open the list belonging to this item...
            menuList.open();
            this.getParentMenuList().setOpened(this);

            // Must set absolute coordinates in order to read the coordinates of
            // element accurately IE6 bug
            final Element menuListElement = menuList.getElement();
            DomHelper.setAbsolutePosition(menuListElement, 0, 0);

            final Element element = this.getElement();
            int x = DomHelper.getParentContainerLeft(element);
            int y = DomHelper.getParentContainerTop(element);

            while (true) {
                final MenuListOpenDirection openDirection = menuList.getOpenDirection();
                final MenuList parentMenuList = this.getParentMenuList();

                if (MenuListOpenDirection.LEFT == openDirection) {
                    x = x - menuList.getOffsetWidth() + 1;
                    break;
                }
                if (MenuListOpenDirection.UP == openDirection) {
                    y = y - menuList.getOffsetHeight() + 1;
                    break;
                }
                if (MenuListOpenDirection.RIGHT == openDirection) {
                    x = x + parentMenuList.getOffsetWidth() - 1;
                    break;
                }
                if (MenuListOpenDirection.DOWN == openDirection) {
                    y = y + parentMenuList.getOffsetHeight() - 1;
                    break;
                }
                WidgetHelper.handleAssertFailure("Unknown openDirection, " + openDirection);
            }
            DomHelper.setAbsolutePosition(menuListElement, x, y);

            listeners.fireMenuOpened(this);
        }
    }

    /**
     * Hides its child menuList if it has one and then makes it invisible. After that this widget is unhighlighted.
     */
    public void hide() {
        this.getMenuList().hide();
        this.removeHighlight();
    }

    protected String getSelectedStyle() {
        return MenuConstants.SUB_MENU_ITEM_SELECTED_STYLE;
    }

    protected String getDisabledStyle() {
        return MenuConstants.SUB_MENU_ITEM_DISABLED_STYLE;
    }

    // EVENT HANDLING
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This event is only fired if the SubMenuItem is not disabled.
     */
    protected void handleMouseClick(final Event event) {
        // ignore event if menu list is already opened...
        if (false == this.isDisabled()) {
            if (DOM.getStyleAttribute(this.getMenuList().getElement(), StyleConstants.DISPLAY).equalsIgnoreCase(
                    "none")) {
                this.open();
            }
        }
        DOM.eventCancelBubble(event, true);
    }

    /**
     * Highlights this widget and possibly opens the attached menuList if the parent menu has its autoOpen property set to true.
     */
    protected void handleMouseOver(final Event event) {
        if (false == this.isDisabled()) {
            this.addHighlight();

            if (this.isAutoOpen()) {
                this.open();
            }
        }
        DOM.eventCancelBubble(event, true);
    }

    /**
     * If the target element is a child of this widget do nothing and cancel bubbling. Otherwise let the event bubble up and let the parnet
     * (menuList) handle the event.
     */
    protected void handleMouseOut(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final Element targetElement = DomHelper.eventGetToElement(event);
            if (DOM.isOrHasChild(this.getElement(), targetElement)) {
                DOM.eventCancelBubble(event, true);
                break;
            }
            this.hide();
            break;
        }
    }

    // COMPOSITE
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public String getText() {
        return this.getHtml().getText();
    }

    public void setText(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);
        this.getHtml().setText(text);
    }

    /**
     * A SimplePanel widget contains the menuItem text as its primary widget and also supports the addition of the sub menu list.
     */
    private SimplePanel simplePanel;

    protected SimplePanel getSimplePanel() {
        ObjectHelper.checkNotNull("field:simplePanel", simplePanel);
        return simplePanel;
    }

    protected void setSimplePanel(final SimplePanel simplePanel) {
        ObjectHelper.checkNotNull("parameter:simplePanel", simplePanel);
        this.simplePanel = simplePanel;
    }

    protected SimplePanel createSimplePanel() {
        final SimplePanel simplePanel = new SimplePanel();
        simplePanel.setWidth("100%");
        DOM.appendChild(simplePanel.getElement(), this.createHtml().getElement());
        simplePanel.addStyleName(MenuConstants.SUB_MENU_ITEM_STYLE);
        this.setSimplePanel(simplePanel);
        return simplePanel;
    }

    /**
     * A HTML widget contains the text or label of this item.
     */
    private HTML html;

    protected HTML getHtml() {
        ObjectHelper.checkNotNull("field:html", html);
        return html;
    }

    protected void setHtml(final HTML html) {
        ObjectHelper.checkNotNull("parameter:html", html);
        this.html = html;
    }

    protected HTML createHtml() {
        final HTML html = new HTML();
        html.setWidth("100%");
        this.setHtml(html);
        return html;
    }

    public void setParentMenuList(final MenuList parentMenuList) {
        super.setParentMenuList(parentMenuList);
        if (this.hasMenuList()) {
            this.getMenuList().setParentMenuList(parentMenuList);
        }
    }

    /**
     * The child menu list that is displayed when this sub menu is opened.
     */
    private MenuList menuList;

    public MenuList getMenuList() {
        ObjectHelper.checkNotNull("field:menuList", menuList);
        return this.menuList;
    }

    public boolean hasMenuList() {
        return null != menuList;
    }

    public void setMenuList(final MenuList menuList) {
        ObjectHelper.checkNotNull("parameter:menuList", menuList);
        this.menuList = menuList;
        DOM.setStyleAttribute(menuList.getElement(), StyleConstants.DISPLAY, "none");
        this.getSimplePanel().add(menuList);
    }

    /**
     * When true indicates that this subMenuItem opens and displays its menuList when the mouse hovers over it. When false the user must
     * click on the subMenuItem
     */
    private boolean autoOpen;

    public boolean isAutoOpen() {
        return autoOpen;
    }

    public void setAutoOpen(final boolean autoOpen) {
        this.autoOpen = autoOpen;
    }

    public String toString() {
        return ObjectHelper.defaultToString(this) + ", text[" + this.getText() + "], autoOpen: " + autoOpen;
    }
}