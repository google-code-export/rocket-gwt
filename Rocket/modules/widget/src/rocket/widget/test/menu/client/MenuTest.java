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
package rocket.widget.test.menu.client;

import rocket.util.client.Checker;
import rocket.widget.client.Html;
import rocket.widget.client.Label;
import rocket.widget.client.menu.ContextMenu;
import rocket.widget.client.menu.ContextMenuOpenEvent;
import rocket.widget.client.menu.HorizontalMenuBar;
import rocket.widget.client.menu.Menu;
import rocket.widget.client.menu.MenuItem;
import rocket.widget.client.menu.MenuListOpenDirection;
import rocket.widget.client.menu.MenuListener;
import rocket.widget.client.menu.MenuOpenEvent;
import rocket.widget.client.menu.MenuSpacer;
import rocket.widget.client.menu.SubMenuItem;
import rocket.widget.client.menu.VerticalMenuBar;
import rocket.widget.client.menu.VerticalMenuList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuTest implements EntryPoint {

	final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("" + caught);
			}
		});

		final RootPanel rootPanel = RootPanel.get( "main");

		final Counter menuOpenedEvent = this.createMouseOpenEvent();
		this.setMouseOpenEvent(menuOpenedEvent);
		rootPanel.add(menuOpenedEvent);

		final Grid grid = new Grid(5, 2);
		grid.setBorderWidth(1);

		grid.setText(0, 0, "Menu opens downwards (standard menu behaviour).");
		grid.setWidget(0, 1, createDownOpeningMenu());

		grid.setText(1, 0, "Menu opens upwards(aka windows start menu).");
		grid.setWidget(1, 1, createUpOpeningMenu());

		grid.setText(2, 0, "Menu that opens to the right.");
		grid.setWidget(2, 1, createRightOpeningMenu());

		grid.setText(3, 0, "Menu that opens to the left.");
		grid.setWidget(3, 1, createLeftOpeningMenu());

		grid.setText(4, 0, "Grid with ContextMenu.");
		grid.setWidget(4, 1, createContextMenu());

		rootPanel.add(grid);

		final SimplePanel panel = new SimplePanel();
		panel.setWidget(new Html("."));
		panel.setSize("90%", "500px");
		rootPanel.add(panel);

		rootPanel.getElement().scrollIntoView();
	}

	protected Widget createDownOpeningMenu() {
		return this.createHorizontalMenuBar(MenuListOpenDirection.DOWN);
	}

	protected Widget createUpOpeningMenu() {
		return this.createHorizontalMenuBar(MenuListOpenDirection.UP);
	}

	protected Widget createRightOpeningMenu() {
		return this.createVerticalMenuBar(MenuListOpenDirection.RIGHT);
	}

	protected Widget createLeftOpeningMenu() {
		return this.createVerticalMenuBar(MenuListOpenDirection.LEFT);
	}

	protected Widget createContextMenu() {
		final int rows = 5;
		final int columns = 8;

		final Grid grid = new Grid(rows, columns);
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				grid.setText(row, column, row + "," + column);
			}
		}
		grid.setStyleName("MenuTest-grid");

		final ContextMenu contextMenu = new ContextMenu();
		contextMenu.addMenuListener(new MenuListener() {
			public void onOpen(final MenuOpenEvent event) {
				onOpen((ContextMenuOpenEvent) event);
			}

			void onOpen(final ContextMenuOpenEvent event) {
				final MenuItem menuItem = event.getMenuItem();
				if (menuItem != null) {
					Window.alert("SELECTED\n" + menuItem.getText() + "\nORIGINAL TARGET ELEMENT\n"
							+ DOM.getInnerText(event.getInitialTargetElement()) + "\nORIGINAL TARGET WIDGET\n"
							+ event.getInitialTargetWidget());
				}
			}
		});
		contextMenu.setWidget(grid);

		this.buildMenu(contextMenu, MenuListOpenDirection.RIGHT);
		return contextMenu;
	}

	Menu createHorizontalMenuBar(final MenuListOpenDirection openDirection) {
		final HorizontalMenuBar menuBar = new HorizontalMenuBar();
		menuBar.addMenuListener(this.createMenuListener());
		this.buildMenu(menuBar, openDirection);
		return menuBar;
	}

	Menu createVerticalMenuBar(final MenuListOpenDirection openDirection) {
		final VerticalMenuBar menuBar = new VerticalMenuBar();
		menuBar.addMenuListener(this.createMenuListener());
		this.buildMenu(menuBar, openDirection);
		return menuBar;
	}

	MenuListener createMenuListener() {
		return new MenuListener() {
			public void onOpen(final MenuOpenEvent event) {
				MenuTest.this.getMouseOpenEvent().increment();

				MenuItem menuItem = event.getMenuItem();
				if (null != menuItem) {
					Window.alert("Selected " + menuItem);
				}
			}
		};
	}

	VerticalMenuList createVerticalMenuList(final boolean hideable, final MenuListOpenDirection openDirection) {
		final VerticalMenuList list = new VerticalMenuList();
		list.setOpenDirection(openDirection);
		list.setHideable(hideable);
		return list;
	}

	void buildMenu(final Menu menu, final MenuListOpenDirection openDirection) {
		Checker.notNull("parameter:menu", menu);
		Checker.notNull("parameter:openDirection", openDirection);

		final String[] sizes = new String[] { "Big", "Medium", "Small" };

		for (int i = 0; i < sizes.length; i++) {
			final SubMenuItem subMenu = new SubMenuItem();

			subMenu.setText(sizes[i]);
			subMenu.setDisabled(false);
			subMenu.setAutoOpen(false);
			subMenu.setMenuList(createVerticalMenuList(true, openDirection));
			menu.add(subMenu);

			this.buildSubMenu(subMenu, openDirection);
		}
	}

	void buildSubMenu(final SubMenuItem subMenuItem, final MenuListOpenDirection openDirection) {
		Checker.notNull("parameter:subMenuItem", subMenuItem);
		Checker.notNull("parameter:openDirection", openDirection);

		final String[] colours = new String[] { "Red", "Blue", "Green", "Yellow", "Orange", "Pink", "White", "Black" };

		for (int i = 0; i < colours.length; i++) {
			final SubMenuItem subMenu = new SubMenuItem();

			subMenu.setText(colours[i]);
			subMenu.setDisabled(false);
			subMenu.setAutoOpen(false);
			subMenu.setMenuList(createVerticalMenuList(true, MenuListOpenDirection.LEFT == openDirection ? MenuListOpenDirection.LEFT
					: MenuListOpenDirection.RIGHT));
			subMenuItem.add(subMenu);

			this.buildSubSubMenu(subMenu, openDirection);
		}

		subMenuItem.add(new MenuSpacer());

		final MenuItem menuItem = new MenuItem();
		menuItem.setText("menuItem");
		menuItem.setDisabled(false);
		subMenuItem.add(menuItem);

		final MenuItem disabledMenuItem = new MenuItem();
		disabledMenuItem.setText("disabled MenuItem");
		disabledMenuItem.setDisabled(true);
		subMenuItem.add(disabledMenuItem);

		final SubMenuItem disabledSubMenuItem = new SubMenuItem();
		disabledSubMenuItem.setText("disabled SubMenuItem");
		disabledSubMenuItem.setDisabled(true);
		disabledSubMenuItem.setMenuList(this.createVerticalMenuList(true, openDirection));
		subMenuItem.add(disabledSubMenuItem);
	}

	void buildSubSubMenu(final SubMenuItem subMenuItem, final MenuListOpenDirection openDirection) {
		Checker.notNull("parameter:subMenuItem", subMenuItem);
		Checker.notNull("parameter:openDirection", openDirection);

		for (int i = 0; i < 5; i++) {
			final SubMenuItem subMenu = new SubMenuItem();

			final boolean disabled = i % 3 == 0;

			subMenu.setText(disabled ? "disabled SubMenu" : "" + i);
			subMenu.setDisabled(disabled);
			subMenu.setAutoOpen(false);
			subMenu.setMenuList(createVerticalMenuList(true, MenuListOpenDirection.LEFT == openDirection ? MenuListOpenDirection.LEFT
					: MenuListOpenDirection.RIGHT));
			subMenuItem.add(subMenu);

			this.buildSubSubSubMenu(subMenu, openDirection);
		}

		subMenuItem.add(new MenuSpacer());

		final MenuItem none = new MenuItem();
		none.setText("none");
		none.setDisabled(false);
		subMenuItem.add(none);
	}

	void buildSubSubSubMenu(final SubMenuItem subMenuItem, final MenuListOpenDirection openDirection) {
		Checker.notNull("parameter:subMenuItem", subMenuItem);
		Checker.notNull("parameter:openDirection", openDirection);

		subMenuItem.add(new MenuSpacer());

		final MenuItem apple = new MenuItem();
		apple.setText("apple");
		apple.setDisabled(false);
		subMenuItem.add(apple);

		final MenuItem banana = new MenuItem();
		banana.setText("banana");
		banana.setDisabled(false);
		subMenuItem.add(banana);

		final MenuItem carrot = new MenuItem();
		carrot.setText("carrot");
		carrot.setDisabled(false);
		subMenuItem.add(carrot);

		subMenuItem.add(new MenuSpacer());

		final MenuItem disabled = new MenuItem();
		disabled.setText("disabled");
		disabled.setDisabled(true);
		subMenuItem.add(disabled);
	}

	private Counter mouseOpenEvent;

	Counter getMouseOpenEvent() {
		return this.mouseOpenEvent;
	}

	void setMouseOpenEvent(final Counter mouseOpenEvent) {
		this.mouseOpenEvent = mouseOpenEvent;
	}

	Counter createMouseOpenEvent() {
		return new Counter("MouseOpenEvents: ");
	}

	/**
	 * A specialised label that includes a counter with a prefix.
	 */
	static class Counter extends Label {
		Counter(String prefix) {
			this.prefix = prefix;
			this.setText(prefix + " ?");
		}

		String prefix;

		void increment() {
			this.counter++;
			this.setText(prefix + counter);
		}

		int counter = 0;

	}
}