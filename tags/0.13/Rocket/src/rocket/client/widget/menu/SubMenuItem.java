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
import rocket.client.util.StringHelper;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SubMenuItem extends AbstractMenuWidget implements MenuWidget {

    public SubMenuItem() {
        super();

        this.setWidget(this.createHtml());
        this.createMenuList();
    }

    // MENU WIDGET :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void open() {
        final AbstractMenu menu = this.getMenuList().getMenu();
        final MenuListenerCollection listeners = menu.getMenuListeners();
        if (listeners.fireBeforeMenuOpened(this)) {
        	// calculate the coordinates of the subMenuList...
        	final MenuList parentMenuList = this.getParentMenuList();
            int top = this.getAbsoluteTop();
            int left = this.getAbsoluteLeft();

            if (parentMenuList.isHorizontalLayout()) {
                top = top + this.getOffsetHeight() - MenuConstants.DOWN_OVERLAP;
                left = left + 3;
            } else {
                left = left + this.getOffsetWidth() - MenuConstants.RIGHT_OVERLAP;
                top = top - 3;
            }

            final MenuList menuList = this.getMenuList();
            final Widget menuListWidget = (Widget) menuList;
            menuListWidget.setVisible(true);
            RootPanel.get().add(menuListWidget, left, top);
            DOM.setStyleAttribute( menuListWidget.getElement(), "left", left + "px"); // FIX for FF without the px the values seem to be ignored.
            DOM.setStyleAttribute( menuListWidget.getElement(), "top", top + "px");
            menuList.open();

            listeners.fireMenuOpened(this);
        }
        this.select();
    }

    public void close() {
        this.unselect();
        this.getMenuList().close();
    }

    public void select() {
        this.addStyleName(MenuConstants.SUB_MENU_ITEM_SELECTED_STYLE);
    }

    public void unselect() {
        this.removeStyleName(MenuConstants.SUB_MENU_ITEM_SELECTED_STYLE);
    }

    public void setDisabled(final boolean disabled) {
        if (this.isDisabled()) {
            this.removeStyleName(MenuConstants.SUB_MENU_ITEM_DISABLED_STYLE);
        }
        super.setDisabled(disabled);
        if (disabled) {
            this.addStyleName(MenuConstants.SUB_MENU_ITEM_DISABLED_STYLE);
        }
    }

    // PANEL ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public boolean add(final Widget widget) {
        return this.insert(widget, this.getCount());
    }

    public boolean insert(final Widget widget, final int beforeIndex) {
        return this.getMenuList().insert(widget, beforeIndex);
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

    private MenuList menuList;

    protected MenuList getMenuList() {
        ObjectHelper.checkNotNull("field:menuList", menuList);
        return this.menuList;
    }

    protected boolean hasMenuList() {
        return null != menuList;
    }

    protected void setMenuList(final MenuList menuList) {
        ObjectHelper.checkNotNull("parameter:menuList", menuList);
        this.menuList = menuList;
    }

    protected MenuList createMenuList() {
        WidgetHelper.checkNotAlreadyCreated("menuList", this.hasMenuList());

        final VerticalMenuList list = new VerticalMenuList();
        this.setMenuList(list);
        return list;
    }

    /**
     * In addition to updating the parentMenuList property for this widget the menuList property
     * for the internal MenuList is also updated.
     */
    public void setParentMenuList(MenuList parentMenuList) {
        super.setParentMenuList( parentMenuList );
        this.getMenuList().setParentMenuList( parentMenuList );
    }
    
    // COMPOSITE ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public String getText() {
        return this.getHtml().getText();
    }

    public void setText(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);
        this.getHtml().setText(text);
    }

    /**
     * A HTML widget contains the menuItem.
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
        html.addStyleName(MenuConstants.SUB_MENU_ITEM_STYLE);
        this.setHtml(html);
        return html;
    }
}
