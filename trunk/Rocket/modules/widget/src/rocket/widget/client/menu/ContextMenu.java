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
import rocket.event.client.Event;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseOutEvent;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;
import rocket.widget.client.SpanPanel;
import rocket.widget.client.Widgets;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Context menus are a simple panel that includes a single widget. When a right
 * mouse click occurs on this widget the menu is activated. A context menu
 * displays a VerticalMenuList with the immediate child items when right mouse
 * clicked.
 * 
 * The only reliable way to stop the default browser behaviour( in IE6 and FF)
 * seems to be to add a oncontextmenu=return false as part of the body tag of
 * the application's start page.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ContextMenu extends Menu {

	static {
		Event.disableContextMenu();
	}

	public ContextMenu() {
		super();
	}

	/**
	 * Factory method which eventually creates the VerticalMenuList.
	 * 
	 * @return
	 */
	protected Panel createPanel() {
		return createSpanPanel();
	}

	protected void afterCreatePanel() {
		super.afterCreatePanel();

		this.getEventListenerDispatcher().addMouseEventListener(new MouseEventAdapter() {
			public void onMouseDown(final MouseDownEvent event) {
				ContextMenu.this.onMouseDown(event);
			}

			public void onMouseOut(final MouseOutEvent event) {
				ContextMenu.this.onMouseOut(event);
			}
		});
	}

	protected String getInitialStyleName() {
		return Constants.CONTEXT_MENU_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.MOUSE_DOWN | EventBitMaskConstants.MOUSE_OUT;
	}

	protected SpanPanel createSpanPanel() {
		final SpanPanel panel = new SpanPanel();
		panel.add(Widgets.createHtml());

		final MenuList menuList = this.createMenuList();
		this.setMenuList(menuList);
		panel.add(menuList);

		return panel;
	}

	protected MenuList createMenuList() {
		final VerticalMenuList list = new VerticalMenuList();
		list.setStyleName(Constants.VERTICAL_MENU_LIST_STYLE);
		list.setHideable(true);
		list.setMenu(this);
		list.setOpenDirection(MenuListOpenDirection.DOWN);
		list.setVisible(false);

		return list;
	}

	/**
	 * Retrieves the widget being wrapped by this menu.
	 * 
	 * @return
	 */
	public Widget getWidget() {
		final SpanPanel panel = (SpanPanel) this.getPanel();
		return panel.get(0);
	}

	public void setWidget(final Widget widget) {
		Checker.notNull("parameter:widget", widget);

		final SpanPanel panel = (SpanPanel) this.getPanel();
		panel.remove(0);
		panel.insert(widget, 0);
	}

	/**
	 * This method is fired whenever this menu widget receives a mouse out event
	 * 
	 * @param event
	 */
	protected void onMouseDown(final MouseDownEvent event) {
		if (event.isRightButton()) {
			this.open(event);
			event.cancelBubble(true);
		}
	}

	/**
	 * This method is fired whenever this menu widget receives a mouse out event
	 * 
	 * @param event
	 */
	protected void onMouseOut(final MouseOutEvent event) {
		Checker.notNull("parameter:event", event);

		while (true) {
			final Element targetElement = event.getTo();
			if (this.getElement().isOrHasChild(targetElement)) {
				event.cancelBubble(true);
				break;
			}
			this.hide();
			break;
		}
	}

	/**
	 * Opens and positions the context menu relative to the widget being
	 * wrapped.
	 * 
	 * @param event
	 *            The source mouse event.
	 */
	public void open(final MouseEvent event) {
		final MenuList menuList = this.getMenuList();
		menuList.open();

		// position the menu list...

		// Must set absolute coordinates in order to read the coordinates of
		// element accurately IE6 bug
		final Element menuListElement = menuList.getElement();

		final InlineStyle menuListElementInlineStyle = InlineStyle.getInlineStyle(menuListElement);
		menuListElementInlineStyle.setString(Css.POSITION, "absolute");
		menuListElementInlineStyle.setInteger(Css.LEFT, 0, CssUnit.PX);
		menuListElementInlineStyle.setInteger(Css.TOP, 0, CssUnit.PX);

		// final Widget widget = event.getWidget();
		final Element widgetElement = event.getTarget();
		int x = Dom.getContainerLeftOffset(widgetElement);
		int y = Dom.getContainerTopOffset(widgetElement);

		x = x + event.getTargetElementX();
		y = y + event.getTargetElementY();

		menuListElementInlineStyle.setInteger(Css.LEFT, x, CssUnit.PX);
		menuListElementInlineStyle.setInteger(Css.TOP, y, CssUnit.PX);
		menuListElementInlineStyle.setInteger(Css.Z_INDEX, 1, CssUnit.NONE);

		this.fireMenuOpened(event, this);
	}

	/**
	 * The element that recieved the initial right mouse click. When the
	 * ContextMenu is hidden this field should be cleared.
	 */
	private Element originalTargetElement;

	Element getOriginalTargetElement() {
		Checker.notNull("field:originalTargetElement", originalTargetElement);
		return this.originalTargetElement;
	}

	void setOriginalTargetElement(final Element originalTargetElement) {
		Checker.notNull("parameter:originalTargetElement", originalTargetElement);
		this.originalTargetElement = originalTargetElement;
	}

	void clearOriginalTargetElement() {
		this.originalTargetElement = null;
	}

	/**
	 * The widget that recieved the initial right mouse click. When the
	 * ContextMenu is hidden this field should be cleared.
	 */
	private Widget originalTargetWidget;

	Widget getOriginalTargetWidget() {
		Checker.notNull("field:originalTargetWidget", originalTargetWidget);
		return this.originalTargetWidget;
	}

	void setOriginalTargetWidget(final Widget originalTargetWidget) {
		Checker.notNull("parameter:originalTargetWidget", originalTargetWidget);
		this.originalTargetWidget = originalTargetWidget;
	}

	void clearOriginalTargetWidget() {
		this.originalTargetWidget = null;
	}

	/**
	 * Hides all visible menus as well as clearing the recorded or original
	 * element that was clicked preventing a leak.
	 */
	public void hide() {
		super.hide();

		this.clearOriginalTargetElement();
		this.clearOriginalTargetWidget();
	}

	public Iterator iterator() {
		return this.getMenuList().iterator();
	}

	public void insert(final Widget widget, final int beforeIndex) {
		this.getMenuList().insert(widget, beforeIndex);
	}

	public boolean remove(final Widget widget) {
		return this.getMenuList().remove(widget);
	}

	public int getCount() {
		return this.getMenuList().getWidgetCount();
	}

	/**
	 * A customised fireMenuOpened which creates and fires a
	 * ContextMenuOpenEvent which includes details about the mouse click.
	 * 
	 * THis method also contains logic so that the original element that
	 * recieved the right mouse click is sent with the event.
	 */
	protected void fireMenuOpened(final MouseEvent event, final Widget source) {

		if (source == this) {
			this.setOriginalTargetElement(event.getTarget());
			this.setOriginalTargetWidget(event.getWidget());
		}
		final Element target = this.getOriginalTargetElement();
		final Widget targetWidget = this.getOriginalTargetWidget();

		final ContextMenuOpenEvent contextMenuOpenEvent = new ContextMenuOpenEvent();
		contextMenuOpenEvent.setMenu(this);
		contextMenuOpenEvent.setMouseEvent(event);
		contextMenuOpenEvent.setInitialTargetElement(target);
		contextMenuOpenEvent.setInitialTargetWidget(targetWidget);

		contextMenuOpenEvent.setWidget(source);

		this.getMenuListeners().fireMenuOpened(contextMenuOpenEvent);
	}
}
