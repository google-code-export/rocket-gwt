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

import java.util.Iterator;

import rocket.util.client.ObjectHelper;
import rocket.widget.client.VerticalPanel;

import com.google.gwt.user.client.ui.Widget;

/**
 * A VerticalMenuList contains menu items that grow down the screen. VerticalMenuLists are mostly only used as the MenuList for non Menu
 * widgets.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class VerticalMenuList extends MenuList {

    public VerticalMenuList() {
        super();

        this.initWidget(this.createVerticalPanel());
    }

    // PANEL
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public Widget get(final int index) {
        return this.getVerticalPanel().getWidget(index);
    }

    public void insert(final Widget widget, final int beforeIndex) {
        this.getVerticalPanel().insert(widget, beforeIndex);
        this.afterInsert(widget);
    }

    protected boolean remove0(final Widget widget) {
        return this.getVerticalPanel().remove(widget);
    }

    public int getWidgetCount() {
        return this.getVerticalPanel().getWidgetCount();
    }

    public Iterator iterator() {
        return this.getVerticalPanel().iterator();
    }

    // PROPERTIES :::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * A verticalPanel is used as the container for all child widgets.
     */
    private VerticalPanel verticalPanel;

    protected VerticalPanel getVerticalPanel() {
        ObjectHelper.checkNotNull("field:verticalPanel", verticalPanel);
        return this.verticalPanel;
    }

    protected void setVerticalPanel(final VerticalPanel verticalPanel) {
        ObjectHelper.checkNotNull("parameter:verticalPanel", verticalPanel);
        this.verticalPanel = verticalPanel;
    }

    protected VerticalPanel createVerticalPanel() {
        final VerticalPanel panel = new VerticalPanel();
        panel.addStyleName(MenuConstants.VERTICAL_MENU_LIST_STYLE);

        this.setVerticalPanel(panel);
        return panel;
    }
}
