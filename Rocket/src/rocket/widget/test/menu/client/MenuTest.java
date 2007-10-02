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

import rocket.browser.client.Browser;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.widget.client.Label;
import rocket.widget.client.menu.ContextMenu;
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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public class MenuTest implements EntryPoint {

	final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("" + caught);
			}
		});

		final RootPanel rootPanel = RootPanel.get();

		final Counter menusOpened = new Counter("Menus opened: ");
		rootPanel.add(menusOpened);

		final MenuListener listener = new MenuListener() {
			public void onOpen(final MenuOpenEvent event) {
				menusOpened.increment();

				final MenuItem menuItem = event.getMenuItem();
				if ( null != menuItem ) {
					Window.alert("Selected menu item...\n" + menuItem);
				}
			}
		};

		final Grid grid = new Grid(3, 3);
		grid.setWidth("100%");
		grid.setHeight("100%");
		grid.setBorderWidth(0);

		final Menu horizontalMenuBar = this.createHorizontalMenuBar(MenuListOpenDirection.DOWN, listener);
		grid.setWidget(0, 1, horizontalMenuBar);
		grid.setWidget(2, 1, this.createHorizontalMenuBar(MenuListOpenDirection.UP, listener));
		grid.setWidget(1, 0, this.createVerticalMenuBar(MenuListOpenDirection.RIGHT, listener));
		grid.setWidget(1, 2, this.createVerticalMenuBar(MenuListOpenDirection.LEFT, listener));

		final CellFormatter cellFormatter = grid.getCellFormatter();
		cellFormatter.setStyleName(0, 1, "green");
		cellFormatter.setStyleName(2, 1, "yellow");
		cellFormatter.setStyleName(1, 0, "red");
		cellFormatter.setStyleName(1, 2, "blue");

		rootPanel.add(grid);

		final Button getItem = new Button("Get menu item");
		getItem.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final SubMenuItem subMenuItem = (SubMenuItem) horizontalMenuBar.get(0);

				final String text = "index (must be between 0 and " + subMenuItem.getCount() + ")";
				final int index = Integer.parseInt(Browser.prompt(text, "0"));

				Window.alert(index + "=" + subMenuItem.get(index));
			}
		});
		rootPanel.add(getItem);

		final Button addMenuItem = new Button("Add menu item");
		addMenuItem.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final MenuItem menuItem = new MenuItem();
				menuItem.setDisabled(false);
				menuItem.setText(Browser.prompt("New menu item text", "xxx"));

				final SubMenuItem subMenuItem = (SubMenuItem) horizontalMenuBar.get(0);
				subMenuItem.add(menuItem);
			}
		});
		rootPanel.add(addMenuItem);

		final Button removeItem = new Button("Remove menu item");
		removeItem.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final SubMenuItem subMenuItem = (SubMenuItem) horizontalMenuBar.get(0);

				final String text = "index (must be between 0 and " + subMenuItem.getCount() + ")";
				final int index = Integer.parseInt(Browser.prompt(text, "0"));

				final boolean removed = subMenuItem.remove(index);
				Window.alert("Result of removing(" + index + ") > " + (removed ? "successful" : "failed"));
			}
		});
		rootPanel.add(removeItem);

		final String[] words = StringHelper.split(LOREM, " ,.", false);
		final FlowPanel flowPanel = new FlowPanel();
		for (int i = 0; i < words.length; i++) {
			final String word = words[i];

			final Label label = new Label(word) {
				protected Element createElement() {
					return DOM.createSpan();
				}
			};
			Widget widget = label;
			if (word.length() > 1) {
				if (i % 9 == 0) {
					InlineStyle.setString(label.getElement(), StyleConstants.BACKGROUND_COLOR, "yellow");

					final ContextMenu contextMenu = new ContextMenu();
					contextMenu.addMenuListener(listener);
					contextMenu.setWidget(label);
					this.buildMenuBar(contextMenu, MenuListOpenDirection.RIGHT);

					widget = contextMenu;
				}
			}
			flowPanel.add(widget);
		}

		rootPanel.add(flowPanel);
	}

	Menu createHorizontalMenuBar(final MenuListOpenDirection openDirection, final MenuListener listener) {
		final HorizontalMenuBar menuBar = new HorizontalMenuBar();
		menuBar.addMenuListener(listener);
		this.buildMenuBar(menuBar, openDirection);
		return menuBar;
	}

	Menu createVerticalMenuBar(final MenuListOpenDirection openDirection, final MenuListener listener) {
		final VerticalMenuBar menuBar = new VerticalMenuBar();
		menuBar.addMenuListener(listener);
		this.buildMenuBar(menuBar, openDirection);
		return menuBar;
	}

	VerticalMenuList createVerticalMenuList(final boolean hideable, final MenuListOpenDirection openDirection) {
		final VerticalMenuList list = new VerticalMenuList();
		list.setOpenDirection(openDirection);
		list.setHideable(hideable);
		return list;
	}

	void buildMenuBar(final Menu menuBar, final MenuListOpenDirection openDirection) {
		ObjectHelper.checkNotNull("parameter:menuBar", menuBar);
		ObjectHelper.checkNotNull("parameter:openDirection", openDirection);

		final SubMenuItem fruits = new SubMenuItem();
		fruits.setText("fruits(subMenu)");
		fruits.setDisabled(false);
		fruits.setAutoOpen(false);
		fruits.setMenuList(createVerticalMenuList(true, openDirection));
		menuBar.add(fruits);

		this.buildSubMenu(fruits, MenuListOpenDirection.LEFT == openDirection ? MenuListOpenDirection.LEFT : MenuListOpenDirection.RIGHT);

		final SubMenuItem disabledFruit = new SubMenuItem();
		disabledFruit.setText("disabledFruit(disabled subMenu)");
		disabledFruit.setDisabled(true);
		disabledFruit.setAutoOpen(false);
		disabledFruit.setMenuList(createVerticalMenuList(true, MenuListOpenDirection.LEFT == openDirection ? MenuListOpenDirection.LEFT
				: MenuListOpenDirection.RIGHT));
		menuBar.add(disabledFruit);

		menuBar.add(new MenuSpacer());

		final MenuItem iceCream = new MenuItem();
		iceCream.setText("iceCream(menuItem)");
		iceCream.setDisabled(false);
		menuBar.add(iceCream);

		final MenuItem disabledIceCream = new MenuItem();
		disabledIceCream.setText("iceCream(disabled menuItem)");
		disabledIceCream.setDisabled(true);
		menuBar.add(disabledIceCream);
	}

	void buildSubMenu(final SubMenuItem subMenuItem, final MenuListOpenDirection openDirection) {
		ObjectHelper.checkNotNull("parameter:subMenuItem", subMenuItem);
		ObjectHelper.checkNotNull("parameter:openDirection", openDirection);

		subMenuItem.add(new MenuSpacer());

		final SubMenuItem apples = new SubMenuItem();
		apples.setText("apples(subMenuItem)");
		apples.setDisabled(false);
		apples.setAutoOpen(false);
		apples.setMenuList(createVerticalMenuList(true, MenuListOpenDirection.LEFT == openDirection ? MenuListOpenDirection.LEFT
				: MenuListOpenDirection.RIGHT));
		subMenuItem.add(apples);

		this.buildSubSubMenu(apples, openDirection);

		final SubMenuItem disabledApples = new SubMenuItem();
		disabledApples.setText("disabledApples(disabled subMenuItem)");
		disabledApples.setDisabled(true);
		disabledApples.setAutoOpen(false);
		disabledApples.setMenuList(createVerticalMenuList(true, MenuListOpenDirection.LEFT == openDirection ? MenuListOpenDirection.LEFT
				: MenuListOpenDirection.RIGHT));
		subMenuItem.add(disabledApples);

		this.buildSubSubMenu(disabledApples, openDirection);

		subMenuItem.add(new MenuSpacer());

		final MenuItem banana = new MenuItem();
		banana.setText("banana(menuItem)");
		banana.setDisabled(false);
		subMenuItem.add(banana);

		final MenuItem disabledBanana = new MenuItem();
		disabledBanana.setText("banana(disabled menuItem)");
		disabledBanana.setDisabled(true);
		subMenuItem.add(disabledBanana);
	}

	void buildSubSubMenu(final SubMenuItem subMenuItem, final MenuListOpenDirection openDirection) {
		ObjectHelper.checkNotNull("parameter:subMenuItem", subMenuItem);
		ObjectHelper.checkNotNull("parameter:openDirection", openDirection);

		subMenuItem.add(new MenuSpacer());

		final SubMenuItem apple = new SubMenuItem();
		apple.setText("hybrid(subMenuItem)");
		apple.setDisabled(false);
		apple.setAutoOpen(false);
		apple.setMenuList(createVerticalMenuList(true, MenuListOpenDirection.LEFT == openDirection ? MenuListOpenDirection.LEFT
				: MenuListOpenDirection.RIGHT));
		subMenuItem.add(apple);

		this.buildSubSubSubMenu(apple, openDirection);

		subMenuItem.add(new MenuSpacer());

		final MenuItem big = new MenuItem();
		big.setText("big(menuItem)");
		big.setDisabled(false);
		subMenuItem.add(big);

		final MenuItem medium = new MenuItem();
		medium.setText("medium(menuItem)");
		medium.setDisabled(false);
		subMenuItem.add(medium);

		final MenuItem small = new MenuItem();
		small.setText("small(menuItem)");
		small.setDisabled(false);
		subMenuItem.add(small);

		final MenuItem disabledBig = new MenuItem();
		disabledBig.setText("big(disabled menuItem)");
		disabledBig.setDisabled(true);
		subMenuItem.add(disabledBig);
	}

	void buildSubSubSubMenu(final SubMenuItem subMenuItem, final MenuListOpenDirection openDirection) {
		ObjectHelper.checkNotNull("parameter:subMenuItem", subMenuItem);
		ObjectHelper.checkNotNull("parameter:openDirection", openDirection);

		subMenuItem.add(new MenuSpacer());

		final MenuItem cube = new MenuItem();
		cube.setText("cube(menuItem)");
		cube.setDisabled(false);
		subMenuItem.add(cube);

		final MenuItem pyramid = new MenuItem();
		pyramid.setText("pyramid(menuItem)");
		pyramid.setDisabled(false);
		subMenuItem.add(pyramid);

		final MenuItem flat = new MenuItem();
		flat.setText("flat(menuItem)");
		flat.setDisabled(false);
		subMenuItem.add(flat);
	}

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