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

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A vertical menu bar starts of with a vertical list of menu items. These may be opened and automatically close etc.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class VerticalMenuBar extends Menu {
    public VerticalMenuBar() {
        super();

        this.initWidget(this.createWidget());
    }

    protected Widget createWidget() {
        final SimplePanel panel = new SimplePanel();
        panel.setWidget(this.createMenuList());
        return panel;
    }

    protected Widget createMenuList() {
        ObjectHelper.checkPropertyNotSet("menuList", this, this.hasMenuList());

        final VerticalMenuList list = new VerticalMenuList();
        list.addStyleName(MenuConstants.VERTICAL_MENU_BAR_STYLE);
        list.setHideable(false);
        list.setMenu(this);

        this.setMenuList(list);
        return list;
    }
}