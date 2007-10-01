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

import com.google.gwt.user.client.ui.Panel;

/**
 * A standard horizontal menu.The top level menus are layed out horizontally and
 * are always visible.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HorizontalMenuBar extends Menu {
	public HorizontalMenuBar() {
		super();
	}

	protected Panel createPanel() {
		final MenuList menuList = this.createMenuList();
		this.setMenuList(menuList);
		return menuList;
	}

	protected MenuList createMenuList() {
		final HorizontalMenuList list = new HorizontalMenuList();
		list.setHideable( false );
		list.setMenu(this);
		return list;
	}

	protected String getInitialStyleName() {
		return Constants.HORIZONTAL_MENU_BAR_STYLE;
	}
}
