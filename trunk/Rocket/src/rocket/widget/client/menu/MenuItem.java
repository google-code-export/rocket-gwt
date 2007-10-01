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

import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseOverEvent;
import rocket.widget.client.Html;

import com.google.gwt.user.client.ui.Widget;

/**
 * A menu item is an item that may appear in a list which may be clicked if its
 * not disabled.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MenuItem extends MenuWidget {

	public MenuItem() {
		super();
	}

	protected Widget createWidget() {
		return this.createHtml();
	}

	protected String getInitialStyleName() {
		return Constants.MENU_ITEM_STYLE;
	}

	// ACTIONS :::::::::::::::::::::::::::::::::::::::::::::::

	public void open() {
		final MenuList menuList = this.getParentMenuList();

		final Menu menu = menuList.getMenu();
		final MenuListenerCollection listeners = menu.getMenuListeners();
		if (listeners.fireBeforeMenuOpened(this)) {
			listeners.fireMenuOpened(this);
		}
		menu.hide();
	}

	public void hide() {
		this.removeHighlight();
	}

	protected String getSelectedStyle() {
		return Constants.MENU_ITEM_SELECTED_STYLE;
	}

	protected String getDisabledStyle() {
		return Constants.MENU_ITEM_DISABLED_STYLE;
	}

	// EVENT HANDLING ::::::::::::::::::::::::::::::::::::

	/**
	 * This event is only fired if the MenuItem is not disabled.
	 */
	protected void handleMouseClick(final MouseClickEvent event) {
		if (false == this.isDisabled()) {
			this.open();
		}
	}

	/**
	 * Highlights this widget
	 */
	protected void handleMouseOver(final MouseOverEvent event) {
		if (false == this.isDisabled()) {
			this.addHighlight();
		}
	}

	/**
	 * Unhighlights this widget.
	 */
	protected void handleMouseOut(final MouseOutEvent event) {
		if (false == this.isDisabled()) {
			this.removeHighlight();
		}
	}

	// COMPOSITE
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public String getText() {
		return this.getHtml().getText();
	}

	public void setText(final String text) {
		this.getHtml().setText(text);
	}


	protected Html getHtml() {
		return (Html)this.getWidget();
	}

	protected Html createHtml() {
		return new Html();
	}
	
	String toString0(){
		return this.getText();
	}
}
