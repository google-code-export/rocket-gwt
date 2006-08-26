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

import rocket.client.util.ObjectHelper;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A contextMenu is a vertical list of menu items possibly with subMenuItems etc that is activated whenever the user right moust clicks on
 * the targetted widget.
 *
 * @author Miroslav Pokorny (mP)
 */
public class ContextMenu extends AbstractMenu {
    public ContextMenu() {
        this.setWidget(new HTML(""));
        this.sinkEvents( Event.MOUSEEVENTS | Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);

        this.createMenuList();
    }

    // COMPOSITE ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Re-registers the eventListener for this widget
     */
    protected void onAttach() {
        super.onAttach();
        DOM.setEventListener(this.getWidget().getElement(), this);
    }

    /**
     * This method handles behaviour reaction to various events that may occur upon a Menu.
     */
    public void onBrowserEvent(Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        if ( DOM.eventGetType( event ) == Event.ONMOUSEDOWN && DOM.eventGetButton(event) == Event.BUTTON_RIGHT || this.isAutoOpen()
                && DOM.eventGetType(event) == Event.ONMOUSEOVER) {
            final MenuList menuList = this.getMenuList();
            final int top = this.getAbsoluteTop() + this.getOffsetHeight() - MenuConstants.DOWN_OVERLAP;
            final int left = this.getAbsoluteLeft() + 3;

            RootPanel.get().add((Widget) menuList, left, top);

            menuList.open();
        }
    }

    protected Widget createMenuList() {
        WidgetHelper.checkNotAlreadyCreated("menuList", this.hasMenuList());
        final VerticalMenuList list = new VerticalMenuList();
        list.addStyleName(MenuConstants.VERTICAL_MENU_LIST_STYLE);
        list.setMenu(this);

        this.setMenuList(list);
        return list;
    }

    /**
     * The widget which when right mouse clicked activates the context menu.
     */
    private Widget widget;

    public Widget getWidget() {
        ObjectHelper.checkNotNull("field:widget", widget);
        return widget;
    }

    public boolean hasWidget() {
        return null != widget;
    }

    public void setWidget(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        super.setWidget(widget);
        this.widget = widget;
    }
}
