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

import rocket.util.client.Checker;

import com.google.gwt.user.client.ui.Widget;

/**
 * This common base class contains stuff common to all menu events.
 * 
 * @author Miroslav Pokorny
 */
abstract class MenuEvent {

	/**
	 * Contains the parent menu of the item/sub item that was selected.
	 */
	private Menu menu;

	public Menu getMenu() {
		Checker.notNull("field:menu", menu);
		return menu;
	}

	void setMenu(final Menu menu) {
		Checker.notNull("parameter:menu", menu);
		this.menu = menu;
	}

	/**
	 * The menu widget that is being opened, typically a menu list of some sort.
	 */
	private Widget widget;

	Widget getWidget() {
		Checker.notNull("field:widget", widget);
		return this.widget;
	}

	void setWidget(final Widget widget) {
		Checker.notNull("parameter:widget", widget);
		this.widget = widget;
	}

	/**
	 * May be used to test and retrieve the MenuItem that was opened/selected.
	 * 
	 * @return May be null if the selected widget was a SubMenuItem
	 */
	public MenuItem getMenuItem() {
		final Widget widget = this.getWidget();
		return widget instanceof MenuItem ? (MenuItem) widget : null;
	}

	/**
	 * May be used to test and retrieve the SubMenuItem that was
	 * opened/selected.
	 * 
	 * @return May be null if the selected widget was a MenuItem
	 */
	public SubMenuItem getSubMenuItem() {
		final Widget widget = this.getWidget();
		return widget instanceof SubMenuItem ? (SubMenuItem) widget : null;
	}
}
