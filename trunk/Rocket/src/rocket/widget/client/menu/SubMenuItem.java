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

import java.util.Iterator;

import rocket.dom.client.Dom;
import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseOverEvent;
import rocket.style.client.ComputedStyle;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.Css;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.widget.client.DivPanel;
import rocket.widget.client.Html;
import rocket.widget.client.Label;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * A SubMenuItem contains a text label and a MenuList containing the sub menu
 * items.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SubMenuItem extends MenuWidget implements HasWidgets {

	public SubMenuItem() {
		super();
	}

	protected Widget createWidget() {
		final DivPanel panel = this.createDivPanel();

		final Element element = panel.getElement();
		InlineStyle.setString(element, Css.POSITION, "relative");
		InlineStyle.setInteger(element, Css.LEFT, 0, CssUnit.PX);
		InlineStyle.setInteger(element, Css.TOP, 0, CssUnit.PX);

		return panel;
	}

	protected String getInitialStyleName() {
		return Constants.SUB_MENU_ITEM_STYLE;
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

	public boolean remove(final int index) {
		return this.getMenuList().remove(index);
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

		InlineStyle.setInteger(this.getElement(), Css.Z_INDEX, 1, CssUnit.NONE);

		// open the list belonging to this item...
		menuList.open();
		this.getParentMenuList().setOpened(this);

		final Element parentMenuList = this.getParentMenuList().getElement();

		// Must set absolute coordinates in order to read the coordinates of
		// element accurately IE6 bug
		final Element menuListElement = menuList.getElement();
		InlineStyle.setString(menuListElement, Css.POSITION, "absolute");
		InlineStyle.setInteger(menuListElement, Css.LEFT, 0, CssUnit.PX);
		InlineStyle.setInteger(menuListElement, Css.TOP, 0, CssUnit.PX);

		int x = Dom.getContainerLeftOffset(menuListElement);
		int y = Dom.getContainerTopOffset(menuListElement);

		while (true) {
			final MenuListOpenDirection openDirection = menuList.getOpenDirection();

			if (MenuListOpenDirection.LEFT == openDirection) {
				x = x - ComputedStyle.getInteger(menuListElement, Css.WIDTH, CssUnit.PX, 0);
				x = x + ComputedStyle.getInteger(menuListElement, Css.BORDER_RIGHT_WIDTH, CssUnit.PX, 0);
				x++;

				y = y - ComputedStyle.getInteger(menuListElement, Css.BORDER_TOP_WIDTH, CssUnit.PX, 0);
				break;
			}
			if (MenuListOpenDirection.UP == openDirection) {
				x = x - ComputedStyle.getInteger(menuListElement, Css.BORDER_LEFT_WIDTH, CssUnit.PX, 0);

				y = y - ComputedStyle.getInteger(menuListElement, Css.HEIGHT, CssUnit.PX, 0);
				y = y + ComputedStyle.getInteger(menuListElement, Css.BORDER_BOTTOM_WIDTH, CssUnit.PX, 0);
				y--;
				break;
			}
			if (MenuListOpenDirection.RIGHT == openDirection) {
				x = x + ComputedStyle.getInteger(parentMenuList, Css.WIDTH, CssUnit.PX, 0);
				x = x - ComputedStyle.getInteger(menuListElement, Css.BORDER_LEFT_WIDTH, CssUnit.PX, 0);
				x--;

				y = y - ComputedStyle.getInteger(menuListElement, Css.BORDER_TOP_WIDTH, CssUnit.PX, 0);
				break;
			}
			ObjectHelper.checkSame("openDirection", MenuListOpenDirection.DOWN, openDirection);

			x = x - ComputedStyle.getInteger(menuListElement, Css.BORDER_LEFT_WIDTH, CssUnit.PX, 0);

			y = y + ComputedStyle.getInteger(parentMenuList, Css.HEIGHT, CssUnit.PX, 0);
			y = y - ComputedStyle.getInteger(menuListElement, Css.BORDER_TOP_WIDTH, CssUnit.PX, 0);
			y--;
			break;
		}
		InlineStyle.setString(menuListElement, Css.POSITION, "absolute");
		InlineStyle.setInteger(menuListElement, Css.LEFT, x, CssUnit.PX);
		InlineStyle.setInteger(menuListElement, Css.TOP, y, CssUnit.PX);
		InlineStyle.setInteger(menuListElement, Css.Z_INDEX, 1, CssUnit.NONE);

		listeners.fireMenuOpened(this);
	}

	/**
	 * Hides its child menuList if it has one and then makes it invisible. After
	 * that this widget is unhighlighted.
	 */
	public void hide() {
		this.getMenuList().hide();
		this.removeHighlight();

		InlineStyle.setString(this.getElement(), Css.Z_INDEX, "");
	}

	protected String getSelectedStyle() {
		return Constants.SUB_MENU_ITEM_SELECTED_STYLE;
	}

	protected String getDisabledStyle() {
		return Constants.SUB_MENU_ITEM_DISABLED_STYLE;
	}

	// EVENT HANDLING
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This event is only fired if the SubMenuItem is not disabled.
	 */
	protected void onMouseClick(final MouseClickEvent event) {
		// ignore event if menu list is already opened...
		if (false == this.isDisabled()) {
			if ("hidden".equals(ComputedStyle.getString(this.getMenuList().getElement(), Css.VISIBILITY))) {
				this.open();
			}
		}
		event.cancelBubble(true);
	}

	/**
	 * Highlights this widget and possibly opens the attached menuList if the
	 * parent menu has its autoOpen property set to true.
	 */
	protected void onMouseOver(final MouseOverEvent event) {
		if (false == this.isDisabled()) {
			this.addHighlight();

			if (this.isAutoOpen()) {
				this.open();
			}
		}
		event.cancelBubble(true);
	}

	/**
	 * If the target element is a child of this widget do nothing and cancel
	 * bubbling. Otherwise let the event bubble up and let the parnet (menuList)
	 * handle the event.
	 */
	protected void onMouseOut(final MouseOutEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final Element targetElement = event.getTo();

			if (DOM.isOrHasChild(this.getElement(), targetElement)) {
				event.cancelBubble(true);
				break;
			}
			this.hide();
			break;
		}
	}

	public String getText() {
		return this.getHtml().getText();
	}

	public void setText(final String text) {
		StringHelper.checkNotEmpty("parameter:text", text);
		this.getHtml().setText(text);
	}

	protected DivPanel getDivPanel() {
		return (DivPanel) this.getWidget();
	}

	protected DivPanel createDivPanel() {
		final DivPanel divPanel = new DivPanel();

		divPanel.add(new Label()); // placeholder for menu list...

		final Html html = this.createHtml();
		divPanel.add(html);
		this.setHtml(html);

		return divPanel;
	}

	/**
	 * A Html widget contains the text or label of this item.
	 */
	private Html html;

	protected Html getHtml() {
		ObjectHelper.checkNotNull("field:html", html);
		return html;
	}

	protected void setHtml(final Html html) {
		ObjectHelper.checkNotNull("parameter:html", html);
		this.html = html;
	}

	protected Html createHtml() {
		final Html html = new Html();
		html.setWidth("100%");
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

		final Element element = menuList.getElement();
		InlineStyle.setString(element, Css.VISIBILITY, "hidden");
		InlineStyle.setString(element, Css.DISPLAY, "none");

		final DivPanel divPanel = this.getDivPanel();
		divPanel.remove(0);
		divPanel.add(menuList);
	}

	/**
	 * When true indicates that this subMenuItem opens and displays its menuList
	 * when the mouse hovers over it. When false the user must click on the
	 * subMenuItem
	 */
	private boolean autoOpen;

	public boolean isAutoOpen() {
		return autoOpen;
	}

	public void setAutoOpen(final boolean autoOpen) {
		this.autoOpen = autoOpen;
	}

	String toString0() {
		return this.getText();
	}
}