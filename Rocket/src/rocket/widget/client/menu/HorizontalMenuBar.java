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

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A standard horizontal menu.The top level menus are layed out horizontally and is always visible.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HorizontalMenuBar extends Menu {
    public HorizontalMenuBar() {
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

        final HorizontalMenuList list = new HorizontalMenuList();
        list.addStyleName(MenuConstants.HORIZONTAL_MENU_BAR_STYLE);
        list.setHideable(false);
        list.setMenu(this);

        this.setMenuList(list);
        return list;
    }
}
