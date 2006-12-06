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

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import com.google.gwt.user.client.ui.VerticalPanel;
import rocket.widget.client.menu.ContextMenu;
import rocket.widget.client.menu.HorizontalMenuBar;
import rocket.widget.client.menu.Menu;
import rocket.widget.client.menu.MenuItem;
import rocket.widget.client.menu.MenuListOpenDirection;
import rocket.widget.client.menu.MenuListener;
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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MenuTest implements EntryPoint {

    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();
                Window.alert("" + caught);
            }
        });

        final RootPanel rootPanel = RootPanel.get();
        final DockPanel dockPanel = new DockPanel();
        final VerticalPanel centerPanel = new VerticalPanel();

        final CheckBox autoOpen = new CheckBox("Auto open?");
        this.setAutoOpen(autoOpen);
        centerPanel.add(autoOpen);

        final CheckBox openDownwards = new CheckBox(
                "VerticalMenuLists hanging off HorizontalMenuLists open downwards ?");
        openDownwards.setChecked(true);
        this.setOpenDownwards(openDownwards);
        centerPanel.add(openDownwards);

        final CheckBox openRightwards = new CheckBox(
                "VerticalMenuLists hanging off other VerticalMenuLists open rightwards ?");
        openRightwards.setChecked(true);
        this.setOpenRightwards(openRightwards);
        centerPanel.add(openRightwards);

        final MenuListener listener = new MenuListener() {
            public void onMenuCancelled(Widget widget) {
                System.err.println("menu cancelled widget[" + ObjectHelper.defaultToString(widget) + "]");
                log("<b>onMenuCancelled</b> widget[" + StringHelper.htmlEncode(widget.toString()) + "]");
            }

            public boolean onBeforeMenuOpened(Widget widget) {
                System.err.println("" + System.currentTimeMillis());
                System.err.println("menu about to be opened widget[" + widget + "]");
                log("<b>onBeforeMenuOpened</b> widget[" + StringHelper.htmlEncode(widget.toString()) + "]");
                return true;
            }

            public void onMenuOpened(Widget widget) {
                System.err.println("menu opened...widget[" + widget + "]");
                log("<b>onMenuOpened</b> widget[" + StringHelper.htmlEncode(widget.toString()) + "]");
            }
        };

        final Button createHorizontalMenuBar = new Button("Create HorizontalMenuBar");
        createHorizontalMenuBar.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                try {
                    final HorizontalMenuBar horizontalMenuBar = createHorizontalMenuBar();
                    horizontalMenuBar.addMenuListener(listener);
                    dockPanel
                            .add(horizontalMenuBar, getOpenDownwards().isChecked() ? DockPanel.NORTH : DockPanel.SOUTH);
                } catch (final Exception caught) {
                    Window.alert("" + caught);
                    caught.printStackTrace();
                }
            }
        });
        centerPanel.add(createHorizontalMenuBar);

        final Button createVerticalMenuBar = new Button("Create VerticalMenuBar");
        createVerticalMenuBar.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                try {
                    final VerticalMenuBar verticalMenuBar = createVerticalMenuBar();
                    verticalMenuBar.addMenuListener(listener);
                    dockPanel.add(verticalMenuBar, getOpenRightwards().isChecked() ? DockPanel.WEST : DockPanel.EAST);
                } catch (final Exception caught) {
                    Window.alert("" + caught);
                    caught.printStackTrace();
                }
            }
        });
        centerPanel.add(createVerticalMenuBar);

        final Button createContextMenu = new Button("Create Widget with ContextMenu");
        createContextMenu.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                try {
                    final Widget contextMenuCandidate = new HTML("[ Widget with context menu ]");
                    contextMenuCandidate.setTitle("Right mouse click here to view context menu...");
                    final ContextMenu contextMenu = createContextMenu(contextMenuCandidate);
                    contextMenu.addMenuListener(listener);
                    dockPanel.add(contextMenu, getOpenRightwards().isChecked() ? DockPanel.WEST : DockPanel.EAST);

                } catch (final Exception caught) {
                    Window.alert("" + caught);
                    caught.printStackTrace();
                }
            }
        });
        centerPanel.add(createContextMenu);

        // REMAINING...
        // centerPanel.add(feedback);
        dockPanel.add(centerPanel, DockPanel.CENTER);

        rootPanel.add(dockPanel);
    }

    static void log(final String message) {
        StringHelper.checkNotNull("parameter:message", message);

        final Element log = DOM.getElementById("log");
        DOM.setInnerHTML(log, DOM.getInnerHTML(log) + message + "<br>");
    }

    ContextMenu createContextMenu(final Widget widget) {
        final ContextMenu menu = new ContextMenu();
        final MenuListOpenDirection openDirection = this.getOpenRightwards().isChecked() ? MenuListOpenDirection.RIGHT
                : MenuListOpenDirection.LEFT;
        this.addItems(menu, openDirection);
        menu.setWidget(widget);
        return menu;
    }

    HorizontalMenuBar createHorizontalMenuBar() {
        final HorizontalMenuBar menuBar = new HorizontalMenuBar();
        final MenuListOpenDirection openDirection = this.getOpenDownwards().isChecked() ? MenuListOpenDirection.DOWN
                : MenuListOpenDirection.UP;
        this.addItems(menuBar, openDirection);
        return menuBar;
    }

    VerticalMenuList createVerticalMenuList(final boolean hideable, final MenuListOpenDirection openDirection) {
        final VerticalMenuList list = new VerticalMenuList();
        list.setHideable(hideable);
        list.setOpenDirection(openDirection);
        return list;
    }

    VerticalMenuBar createVerticalMenuBar() {
        final VerticalMenuBar menuBar = new VerticalMenuBar();
        final MenuListOpenDirection openDirection = this.getOpenRightwards().isChecked() ? MenuListOpenDirection.RIGHT
                : MenuListOpenDirection.LEFT;
        this.addItems(menuBar, openDirection);
        return menuBar;
    }

    protected void addItems(final Menu menuBar, final MenuListOpenDirection openDirection) {
        ObjectHelper.checkNotNull("parameter:menuBar", menuBar);
        ObjectHelper.checkNotNull("parameter:openDirection", openDirection);

        final boolean autoOpen = this.getAutoOpen().isChecked();

        final SubMenuItem apples = new SubMenuItem();
        apples.setText("apples(subMenu)");
        apples.setDisabled(false);
        apples.setAutoOpen(autoOpen);
        apples.setMenuList(createVerticalMenuList(true, openDirection));
        this.addItems(apples);
        menuBar.add(apples);

        final SubMenuItem bananas = new SubMenuItem();
        bananas.setText("bananas(subMenu) disabled");
        bananas.setDisabled(true);
        bananas.setAutoOpen(autoOpen);
        bananas.setMenuList(createVerticalMenuList(true, openDirection));
        menuBar.add(bananas);

        final MenuItem carrots = new MenuItem();
        carrots.setText("carrots(menuItem)");
        carrots.setDisabled(false);
        menuBar.add(carrots);

        final MenuItem dog = new MenuItem();
        dog.setText("dog(menuItem) disabled");
        dog.setDisabled(true);
        menuBar.add(dog);
    }

    protected void addItems(final SubMenuItem subMenu) {
        ObjectHelper.checkNotNull("parameter:subMenu", subMenu);

        final String prefix = subMenu.getText();
        final MenuListOpenDirection openDirection = this.getOpenRightwards().isChecked() ? MenuListOpenDirection.RIGHT
                : MenuListOpenDirection.LEFT;
        final boolean autoOpen = this.getAutoOpen().isChecked();

        final SubMenuItem green = new SubMenuItem();
        green.setText(prefix + "-green(subMenu)");
        green.setDisabled(false);
        green.setAutoOpen(autoOpen);
        green.setMenuList(createVerticalMenuList(true, openDirection));
        subMenu.add(green);

        this.addMoreItems(green);

        subMenu.add(new MenuSpacer());

        final SubMenuItem yellow = new SubMenuItem();
        yellow.setText(prefix + "-yellow(subMenu)");
        yellow.setDisabled(false);
        yellow.setAutoOpen(autoOpen);
        yellow.setMenuList(createVerticalMenuList(true, openDirection));
        this.addMoreItems(yellow);
        subMenu.add(yellow);

        final SubMenuItem purple = new SubMenuItem();
        purple.setText(prefix + "-purple(subMenu)");
        purple.setDisabled(false);
        purple.setAutoOpen(autoOpen);
        purple.setMenuList(createVerticalMenuList(true, openDirection));
        this.addMoreItems(purple);
        subMenu.add(purple);

        final SubMenuItem blue = new SubMenuItem();
        blue.setText(prefix + "-blue(subMenu) disabled");
        blue.setDisabled(true);
        blue.setAutoOpen(autoOpen);
        blue.setMenuList(createVerticalMenuList(true, openDirection));
        subMenu.add(blue);

        // subMenu.add( new MenuSpacer() );

        final MenuItem red = new MenuItem();
        red.setText(prefix + "-red(menuItem)");
        red.setDisabled(false);
        subMenu.add(red);

        final MenuItem orange = new MenuItem();
        orange.setText(prefix + "-orange(menuItem) disabled");
        orange.setDisabled(true);
        subMenu.add(orange);
    }

    protected void addMoreItems(final SubMenuItem subMenu) {
        ObjectHelper.checkNotNull("parameter:subMenu", subMenu);

        final String prefix = subMenu.getText();

        final MenuListOpenDirection openDirection = this.getOpenRightwards().isChecked() ? MenuListOpenDirection.RIGHT
                : MenuListOpenDirection.LEFT;
        final boolean autoOpen = this.getAutoOpen().isChecked();

        final SubMenuItem big = new SubMenuItem();
        big.setText(prefix + "-big(subMenu)");
        big.setDisabled(false);
        big.setAutoOpen(autoOpen);
        big.setMenuList(createVerticalMenuList(true, openDirection));

        this.addMoreMoreItems(big);

        subMenu.add(big);

        final SubMenuItem veryBig = new SubMenuItem();
        veryBig.setText(prefix + "-veryBig(subMenu) disabled");
        veryBig.setDisabled(true);
        veryBig.setAutoOpen(autoOpen);
        veryBig.setMenuList(createVerticalMenuList(true, openDirection));
        subMenu.add(veryBig);

        final MenuItem small = new MenuItem();
        small.setText(prefix + "-small(menuItem)");
        small.setDisabled(false);
        subMenu.add(small);

        final MenuItem medium = new MenuItem();
        medium.setText(prefix + "-medium(menuItem) disabled");
        medium.setDisabled(true);
        subMenu.add(medium);
    }

    protected void addMoreMoreItems(final SubMenuItem subMenu) {
        ObjectHelper.checkNotNull("parameter:subMenu", subMenu);

        final String prefix = subMenu.getText();
        final MenuListOpenDirection openDirection = this.getOpenDownwards().isChecked() ? MenuListOpenDirection.DOWN
                : MenuListOpenDirection.UP;

        final SubMenuItem one = new SubMenuItem();
        one.setText(prefix + "-1(subMenu) disabled");
        one.setDisabled(true);
        one.setMenuList(createVerticalMenuList(true, openDirection));
        subMenu.add(one);

        final SubMenuItem two = new SubMenuItem();
        two.setText(prefix + "-2(subMenu) disabled");
        two.setDisabled(true);
        two.setMenuList(createVerticalMenuList(true, openDirection));
        subMenu.add(two);

        final MenuItem three = new MenuItem();
        three.setText(prefix + "-3(menuItem)");
        three.setDisabled(false);
        subMenu.add(three);

        final MenuItem four = new MenuItem();
        four.setText(prefix + "-4(menuItem) disabled");
        four.setDisabled(true);
        subMenu.add(four);

        for (int i = 5; i < 15; i++) {
            final MenuItem item = new MenuItem();
            item.setText(prefix + "-" + i + "(menuItem)");
            item.setDisabled(false);
            subMenu.add(item);
        }
    }

    /**
     * When ticked any new SubMenu's will auto open when hovered over.
     */
    private CheckBox autoOpen;

    CheckBox getAutoOpen() {
        ObjectHelper.checkNotNull("field:autoOpen", autoOpen);
        return autoOpen;
    }

    void setAutoOpen(final CheckBox autoOpen) {
        ObjectHelper.checkNotNull("parameter:autoOpen", autoOpen);
        this.autoOpen = autoOpen;
    }

    /**
     * VerticalMenuLists hanging off HorizontalMenuLists open downwards when this is true...
     */
    private CheckBox openDownwards;

    CheckBox getOpenDownwards() {
        ObjectHelper.checkNotNull("field:openDownwards", openDownwards);
        return openDownwards;
    }

    void setOpenDownwards(final CheckBox openDownwards) {
        ObjectHelper.checkNotNull("parameter:openDownwards", openDownwards);
        this.openDownwards = openDownwards;
    }

    /**
     * VerticalMenuLists hanging off other VerticalMenuLists open to the right when this is true...
     */
    private CheckBox openRightwards;

    CheckBox getOpenRightwards() {
        ObjectHelper.checkNotNull("field:openRightwards", openRightwards);
        return openRightwards;
    }

    void setOpenRightwards(final CheckBox openRightwards) {
        ObjectHelper.checkNotNull("parameter:openRightwards", openRightwards);
        this.openRightwards = openRightwards;
    }
}