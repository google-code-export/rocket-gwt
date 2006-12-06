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

import rocket.dom.client.DomHelper;
import rocket.util.client.ObjectHelper;
import rocket.widget.client.SpanPanel;
import rocket.widget.client.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Context menus are a simple panel that includes a single widget. When a right mouse click occurs on this widget the menu is activated. A
 * context menu displays a VerticalMenuList with the immediate child items when right mouse clicked.
 * 
 * The only reliable way to stop the default browser behaviour( in IE6 and FF) seems to be to add a oncontextmenu=return false as part of
 * the body tag of the application's start page.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ContextMenu extends Menu {

    public ContextMenu() {
        super();

        this.initWidget(this.createWidget());
        this.addStyleName(MenuConstants.CONTEXT_MENU_STYLE);
    }

    // COMPOSITE :::::::::::::::::::::::::::::::::::::::::

    /**
     * Factory method which eventually creates the VerticalMenuList.
     * 
     * @return
     */
    protected Widget createWidget() {
        final SpanPanel panel = new SpanPanel();
        panel.add(new HTML(""));
        panel.add(this.createMenuList());
        this.setPanel(panel);
        return panel;
    }

    /**
     * A simplepanel is used as the destination panel which wraps the given wrapped Widget. The first slot contains the widget and the
     * second slot is used to house the menuList.
     */
    private SpanPanel panel;

    protected SpanPanel getPanel() {
        ObjectHelper.checkNotNull("field:panel", panel);
        return panel;
    }

    protected void setPanel(final SpanPanel panel) {
        ObjectHelper.checkNotNull("parameter:panel", panel);
        this.panel = panel;
    }

    protected Widget createMenuList() {
        ObjectHelper.checkPropertyNotSet("menuList", this, this.hasMenuList());
        final VerticalMenuList list = new VerticalMenuList();
        list.addStyleName(MenuConstants.VERTICAL_MENU_LIST_STYLE);
        list.setHideable(true);
        list.setMenu(this);
        list.setOpenDirection(MenuListOpenDirection.DOWN);
        list.hide();

        this.setMenuList(list);
        return list;
    }

    /**
     * Retrieves the widget being wrapped by this menu.
     * 
     * @return
     */
    public Widget getWidget() {
        final SpanPanel panel = this.getPanel();
        return panel.get(0);
    }

    public void setWidget(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final SpanPanel panel = this.getPanel();
        panel.remove(0);
        panel.insert(widget, 0);
    }

    // COMPOSITE
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Re-registers the eventListener for this widget
     */
    protected void onAttach() {
        super.onAttach();
        this.registerEvents();
    }

    protected void registerEvents() {
        this.sinkEvents(Event.ONMOUSEDOWN);
        DOM.setEventListener(this.getElement(), this);
    }

    /**
     * Dispatches to the appropriate method depending on the event type.
     * 
     * @param event
     */
    public void onBrowserEvent(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final int eventType = DOM.eventGetType(event);
            if (Event.ONMOUSEDOWN == eventType && DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
                this.handleMouseDown(event);
                break;
            }
            if (Event.ONMOUSEOUT == eventType) {
                this.handleMouseOut(event);
                break;
            }
            break;
        }
    }

    /**
     * This method is fired whenever this menu widget receives a mouse out event
     * 
     * @param event
     */
    protected void handleMouseDown(final Event event) {
        this.open();
        DOM.eventCancelBubble(event, true);
    }

    /**
     * This method is fired whenever this menu widget receives a mouse out event
     * 
     * @param event
     */
    protected void handleMouseOut(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final Element targetElement = DOM.eventGetToElement(event);
            if (DOM.isOrHasChild(this.getElement(), targetElement)) {
                DOM.eventCancelBubble(event, true);
                break;
            }
            this.hide();
            break;
        }
    }

    /**
     * Opens and positions the context menu relative to the widget being wrapped.
     */
    public void open() {
        final MenuList menuList = this.getMenuList();
        final Menu menu = this;
        final MenuListenerCollection listeners = menu.getMenuListeners();
        if (listeners.fireBeforeMenuOpened(this)) {
            menuList.open();

            // Must set absolute coordinates in order to read the coordinates of
            // element accurately IE6 bug
            final Element menuListElement = menuList.getElement();
            DomHelper.setAbsolutePosition(menuListElement, 0, 0);

            final Widget widget = this.getWidget();
            final Element element = widget.getElement();
            int x = DomHelper.getParentContainerLeft(element);
            int y = DomHelper.getParentContainerTop(element);

            while (true) {
                final MenuListOpenDirection openDirection = menuList.getOpenDirection();

                if (MenuListOpenDirection.LEFT == openDirection) {
                    x = x - menuList.getOffsetWidth() + 1;
                    break;
                }
                if (MenuListOpenDirection.UP == openDirection) {
                    y = y - menuList.getOffsetHeight() + 1;
                    break;
                }
                if (MenuListOpenDirection.RIGHT == openDirection) {
                    x = x + widget.getOffsetWidth() - 1;
                    break;
                }
                if (MenuListOpenDirection.DOWN == openDirection) {
                    y = y + widget.getOffsetHeight() - 1;
                    break;
                }
                WidgetHelper.handleAssertFailure("Unknown openDirection, " + openDirection);
            }
            DomHelper.setAbsolutePosition(menuListElement, x, y);

            listeners.fireMenuOpened(this);
        }
    }
}
