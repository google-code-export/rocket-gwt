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
package rocket.test.widget.menu.client;

import rocket.client.widget.menu.ContextMenu;
import rocket.client.widget.menu.Menu;
import rocket.client.widget.menu.MenuItem;
import rocket.client.widget.menu.MenuListener;
import rocket.client.widget.menu.MenuSpacer;
import rocket.client.widget.menu.SubMenuItem;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MenuTest implements EntryPoint {
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler( new UncaughtExceptionHandler(){
			public void onUncaughtException(final Throwable caught ){
				caught.printStackTrace();
				Window.alert( "" + caught );
			}
		});

		final RootPanel rootPanel = RootPanel.get();

		final int listLimit = 5;

		final Menu menu = new Menu();
		menu.setAutoOpen(false);
		menu.setListLimit(listLimit);
		rootPanel.add(menu);

		final SubMenuItem fruits = new SubMenuItem();
		fruits.setText("menu/fruits(subMenu)");
		fruits.setDisabled(false);
		fruits.setPriority(0);
		menu.add(fruits);

		final SubMenuItem apples = new SubMenuItem();
		apples.setText("menu/fruits/apples(subMenu)");
		apples.setDisabled(false);
		apples.setPriority(0);
		fruits.add(apples);

		for (int i = 0; i < 10; i++) {
			final String text = "menu/fruits/apples/" + i;
			final boolean disabled = i < 3;
			final int priority = 100 - i;
			final boolean alwaysShown = i == 0;
			apples.add(createMenuItem(text, disabled, priority, alwaysShown));

			if (i % 4 == 3) {
				final MenuSpacer spacer = new MenuSpacer();
				spacer.setPriority(priority);
				apples.add(spacer);
			}
		}

		final SubMenuItem bananas = new SubMenuItem();
		bananas.setText("menu/fruits/bananas(subMenu)");
		bananas.setDisabled(false);
		bananas.setPriority(0);
		fruits.add(bananas);

		final SubMenuItem yellows = new SubMenuItem();
		yellows.setText("menu/fruits/bananas/yellows");
		yellows.setDisabled(false);
		yellows.setPriority(0);
		bananas.add(yellows);

		for (int i = 0; i < 10; i++) {
			final String text = "menu/fruits/bananas/yellows/" + i;
			final boolean disabled = i > 5;
			final int priority = 100 - i;
			final boolean alwaysShown = i == 9;
			yellows.add(createMenuItem(text, disabled, priority, alwaysShown));

			if (i % 4 == 3) {
				final MenuSpacer spacer = new MenuSpacer();
				spacer.setPriority(priority);
				yellows.add(spacer);
			}
		}

		final SubMenuItem oranges = new SubMenuItem();
		oranges.setText("menu/fruits/bananas/oranges");
		oranges.setDisabled(false);
		oranges.setPriority(0);
		bananas.add(oranges);

		for (int i = 0; i < 10; i++) {
			final String text = "menu/fruits/bananas/oranges/" + i;
			final boolean disabled = i > 5;
			final int priority = 100 - i;
			final boolean alwaysShown = i == 0;
			oranges.add(createMenuItem(text, disabled, priority, alwaysShown));

			if (i % 4 == 3) {
				final MenuSpacer spacer = new MenuSpacer();
				spacer.setPriority(priority);
				oranges.add(spacer);
			}
		}

		final SubMenuItem carrots = new SubMenuItem();
		carrots.setText("menu/fruits/carrots(subMenu)");
		carrots.setDisabled(false);
		carrots.setPriority(0);
		fruits.add(carrots);

		final MenuItem orange = new MenuItem();
		orange.setDisabled(true);
		orange.setText("menu/vegetables/carrots/orange(disabled)");
		orange.setPriority(0);
		carrots.add(orange);

		final MenuItem purple = new MenuItem();
		purple.setDisabled(true);
		purple.setText("menu/vegetables/carrots/purple(disabled)");
		purple.setPriority(0);
		carrots.add(purple);

		final SubMenuItem vegetables = new SubMenuItem();
		vegetables.setDisabled(false);
		vegetables.setText("menu/vegetables(SubMenu)");
		vegetables.setPriority(0);
		menu.add(vegetables);

		final MenuItem potato = new MenuItem();
		potato.setDisabled(true);
		potato.setText("menu/vegetables/potato");
		potato.setPriority(0);
		vegetables.add(potato);

		final SubMenuItem leaves = new SubMenuItem();
		leaves.setDisabled(true);
		leaves.setText("menu/leaves(disabled SubMenu)");
		leaves.setPriority(0);
		menu.add(leaves);

		final MenuItem bush = new MenuItem();
		bush.setDisabled(false);
		bush.setText("menu/bush(MenuItem)");
		bush.setPriority(0);
		menu.add(bush);

		final MenuItem tree = new MenuItem();
		tree.setDisabled(true);
		tree.setText("menu/tree(disabled MenuItem)");
		tree.setPriority(0);
		menu.add(tree);

		rootPanel.add(new HTML("<br/><br/><br/>"));

		final ContextMenu contextMenu = new ContextMenu();
		contextMenu.setAutoOpen(false);
		contextMenu.setWidget(new HTML("ContextMenu"));
		contextMenu.addStyleName("contextMenuWidget");
		contextMenu.setListLimit(listLimit);
		contextMenu.setWidth( "1px");
		
		for (int i = 0; i < 10; i++) {
			final String text = "contextMenu/" + i;
			final boolean disabled = i > 5;
			final int priority = 100 - i;
			final boolean alwaysShown = i == 0;
			contextMenu.add(createMenuItem(text, disabled, priority,
					alwaysShown));

			if (i % 4 == 3) {
				final MenuSpacer spacer = new MenuSpacer();
				spacer.setPriority(priority);
				contextMenu.add(spacer);
			}
		}

		rootPanel.add(contextMenu);

		rootPanel.add(new HTML("<br/><br/><br/>"));

		final Label feedback = new Label("Nothing");
		feedback.addStyleName("feedback");
		rootPanel.add(feedback);

		rootPanel.add(new HTML("<br/>"));

		final CheckBox autoOpen = new CheckBox("Auto open ?");
		autoOpen.addClickListener(new ClickListener() {
			public void onClick(final Widget ignored) {
				final boolean newAutoOpen = autoOpen.isChecked();
				menu.setAutoOpen(newAutoOpen);
				contextMenu.setAutoOpen(newAutoOpen);

				feedback.setText("Menu autoOpen property changed to " + newAutoOpen);
			}
		});
		rootPanel.add(autoOpen);
		rootPanel.add(new HTML("<br/>"));

		rootPanel.add(new HTML("ListLimit"));

		final TextBox listLimitTextBox = new TextBox();
		listLimitTextBox.setText("" + listLimit);
		listLimitTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
			public void onKeyPress(final Widget sender, final char keyCode,
					final int modifiers) {
				if (keyCode == KeyboardListener.KEY_ENTER) {
					final int newListLimit = Integer.parseInt(listLimitTextBox
							.getText());
					menu.setListLimit(newListLimit);
					System.out.println("Menu - setting listLimit: "
							+ newListLimit);
				}
			}
		});
		rootPanel.add(listLimitTextBox);
		rootPanel.add(new HTML("<br/>"));

		final Button menuBarAdder = new Button("Add top level menus");
		menuBarAdder.addClickListener(new ClickListener() {
			public void onClick(final Widget widget) {
				for (int i = 0; i < 3; i++) {
					final SubMenuItem subMenu = new SubMenuItem();
					subMenu.setText( "menu/number-" + i + "(subMenu)" );
					subMenu.setDisabled(false);
					subMenu.setPriority(0);
					menu.add(subMenu);

					for (int j = 0; j < 3; j++) {
						final SubMenuItem subSubMenuItem = new SubMenuItem();
						subSubMenuItem.setText( "menu/number-" + i + "/"  + j + "(subMenu)");
						subSubMenuItem.setDisabled(false);
						subSubMenuItem.setPriority(0);
						subMenu.add(subSubMenuItem);

						for (int k = 0; k < 3; k++) {
							final MenuItem menuItem = new MenuItem();
							menuItem.setText( "menu/number-" + i + "/"  + j + "/" + k );
							menuItem.setDisabled(false);
							menuItem.setPriority(0);
							subSubMenuItem.add(menuItem);
						}
					}
				}
			}
		});


        final CheckBox cancelOpenEvents = new CheckBox("Cancel beforeMenuOpen events ?");
        rootPanel.add(cancelOpenEvents);
        rootPanel.add(new HTML("<br/>"));

		final MenuListener listener = new MenuListener() {
            public void onMenuCancelled(final Widget widget) {
                feedback.setText("MenuCancelled: " + widget);
            }


			public boolean onBeforeMenuOpened(final Widget widget) {
				String text = null;
				if (widget instanceof MenuItem) {
					text = ((MenuItem) widget).getText();
				}
				if (widget instanceof SubMenuItem) {
					text = ((SubMenuItem) widget).getText();
				}

                final boolean cancelOpenEvent = cancelOpenEvents.isChecked();
                feedback.setText("BeforeMenuOpened: " + (cancelOpenEvent ? "cancelling " : "opening ") + text );
				return !cancelOpenEvent;
			}

			public void onMenuOpened(final Widget widget) {
				String text = null;
				if (widget instanceof MenuItem) {
					text = ((MenuItem) widget).getText();
				}
				if (widget instanceof SubMenuItem) {
					text = ((SubMenuItem) widget).getText();
				}
				feedback.setText("MenuOpened: " + text);
			}
		};

		menu.addMenuListener(listener);
		contextMenu.addMenuListener(listener);

		rootPanel.add(menuBarAdder);
		rootPanel.add(new HTML("<br/>"));

		final Button menuBarClearer = new Button(
				"Clear both menuBar & contextMenu");
		menuBarClearer.addClickListener(new ClickListener() {
			public void onClick(final Widget widget) {
				menu.clear();
				contextMenu.clear();
			}
		});
		rootPanel.add(menuBarClearer);
		rootPanel.add(new HTML("<br/>"));

		rootPanel.add(new HTML("<br/><hr/><br/>"));
	}

	static MenuItem createMenuItem(final String text, final boolean disabled,
			final int priority, final boolean alwaysShown) {
		final MenuItem menuItem = new MenuItem();
		menuItem.setDisabled(disabled);
		menuItem.setAlwaysShown(alwaysShown);
		menuItem.setText(text + '[' + (disabled ? "disabled " : "")+ " priority:" + priority + (alwaysShown ? " alwaysShown" : "")+ ']');
		menuItem.setPriority(priority);
		return menuItem;
	}
}