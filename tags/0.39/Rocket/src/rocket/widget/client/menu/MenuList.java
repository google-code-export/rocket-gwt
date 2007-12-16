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

import java.util.Stack;

import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseOverEvent;
import rocket.style.client.Css;
import rocket.style.client.InlineStyle;
import rocket.util.client.ObjectHelper;
import rocket.widget.client.CompositePanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class containing common behaviour for both types of MenuLists.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract class MenuList extends CompositePanel implements HasWidgets {

	protected MenuList() {
		super();
	}

	protected void checkPanel(final Panel panel) {
		throw new UnsupportedOperationException("checkPanel");
	}

	abstract protected Panel createPanel();

	protected void afterCreatePanel() {
		this.getEventListenerDispatcher().addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				MenuList.this.handleMouseClick(event);
			}

			public void onMouseOut(final MouseOutEvent event) {
				MenuList.this.handleMouseOut(event);
			}

			public void onMouseOver(final MouseOverEvent event) {
				MenuList.this.handleMouseOver(event);
			}
		});
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.MOUSE_CLICK | EventBitMaskConstants.MOUSE_OVER | EventBitMaskConstants.MOUSE_OUT;
	}

	// EVENTS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	protected void handleMouseClick(final MouseClickEvent event) {
		event.cancelBubble(true);
	}

	protected void handleMouseOver(final MouseOverEvent event) {
		event.cancelBubble(true);
	}

	protected void handleMouseOut(final MouseOutEvent event) {
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

	// ACTIONS
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	abstract protected void open();

	protected void hide() {
		if (this.isHideable()) {
			final Element element = this.getElement();
			InlineStyle.setString(element, Css.VISIBILITY, "hidden");
			InlineStyle.setString(element, Css.DISPLAY, "none");
		}

		if (this.hasOpened()) {
			this.getOpened().hide();
			this.clearOpened();
		}
	}

	/**
	 * This flag indicates whether or not this list shoudl be made invisible
	 * (display:none) when this list is asked to hide
	 * 
	 * The HorizontalMenuList hanging off a HorizontalMenuBar should not be made
	 * invisible whilst its child sub menu menu lists probably should.
	 */
	private boolean hideable;

	public boolean isHideable() {
		return this.hideable;
	}

	public void setHideable(final boolean hideable) {
		this.hideable = hideable;
	}

	// PANEL
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	public abstract void insert(final Widget widget, final int beforeIndex);

	public boolean remove(final int index) {
		return this.remove(this.get(index));
	}

	public boolean remove(final Widget widget) {
		final boolean removed = this.remove0(widget);
		if (removed) {
			if (this.hasOpened() && widget == this.getOpened()) {
				this.clearOpened();
			}
			final MenuWidget menuItem = (MenuWidget) widget;
			menuItem.clearParentMenuList();
		}
		return removed;
	}

	/**
	 * Sub-classes must attempt to remove the given widget
	 * 
	 * @param widget
	 * @return
	 */
	protected abstract boolean remove0(Widget widget);

	// MENU LIST ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * If this is the topmost menuList this property will be set otherwise
	 * children will need to check their parent until the top is reached.
	 */
	private Menu menu;

	public Menu getMenu() {
		Menu menu = this.menu;

		// if this widget doesnt have a menu property set check its parent...
		if (false == this.hasMenu()) {
			menu = this.getParentMenuList().getMenu();
		}

		ObjectHelper.checkNotNull("menu", menu);
		return menu;
	}

	protected boolean hasMenu() {
		return null != this.menu;
	}

	public void setMenu(final Menu menu) {
		ObjectHelper.checkNotNull("parameter:menu", menu);
		this.menu = menu;
	}

	/**
	 * All menuLists will have a parent except if they have been added to a
	 * menu.
	 */
	private MenuList parentMenuList;

	public MenuList getParentMenuList() {
		ObjectHelper.checkNotNull("field:parentMenuList", parentMenuList);
		return this.parentMenuList;
	}

	protected boolean hasParentMenuList() {
		return null != this.parentMenuList;
	}

	public void setParentMenuList(final MenuList parentMenuList) {
		ObjectHelper.checkNotNull("parameter:parentMenuList", parentMenuList);
		this.parentMenuList = parentMenuList;
	}

	/**
	 * This controls which direction the list is opened.
	 */
	private MenuListOpenDirection openDirection;

	public MenuListOpenDirection getOpenDirection() {
		ObjectHelper.checkNotNull("field:openDirection", this.openDirection);
		return this.openDirection;
	}

	public void setOpenDirection(final MenuListOpenDirection openDirection) {
		ObjectHelper.checkNotNull("parameter:openDirection", openDirection);
		this.openDirection = openDirection;
	}

	/**
	 * This property will contain the SubMenuItem item that is currently open.
	 * It will be cleared whenever another child item is selected or this list
	 * itself is hidden.
	 */
	private SubMenuItem opened;

	protected SubMenuItem getOpened() {
		ObjectHelper.checkNotNull("field:opened", opened);
		return this.opened;
	}

	protected boolean hasOpened() {
		return null != this.opened;
	}

	protected void setOpened(final SubMenuItem opened) {
		ObjectHelper.checkNotNull("parameter:opened", opened);
		this.opened = opened;
	}

	protected void clearOpened() {
		this.opened = null;
	}

	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(ObjectHelper.defaultToString(this));
		buf.append(" \"");

		if (this.hasParentMenuList()) {
			MenuList parent = this.getParentMenuList();

			final Stack stack = new Stack();
			while (true) {
				if (parent.hasOpened()) {
					stack.push(parent.getOpened().getText());
				}
				if (false == parent.hasParentMenuList()) {
					break;
				}
				parent = parent.getParentMenuList();
			}

			while (false == stack.isEmpty()) {
				buf.append(stack.pop());
				buf.append(">");
			}
		}

		buf.append("\".");
		return buf.toString();
	}
}
