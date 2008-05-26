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
package rocket.widget.client.tabpanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.util.client.Checker;
import rocket.widget.client.CompositeWidget;
import rocket.widget.client.Html;
import rocket.widget.client.Image;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A TabPanel provides the capability to manage a number of tabs along with
 * providing listeners for the major events that may occur to an individual tab,
 * selection and closing/removal.
 * 
 * This class combines a DeckPanel to hold tab contents and a HorizontalPanel to
 * hold the tabs themselves. This class is a rewrite of the GWT provided one
 * adding a few features and fixing bugs.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class TabPanel extends CompositeWidget {

	protected TabPanel() {
		super();
	}

	protected void beforeCreateWidget() {
		super.beforeCreateWidget();

		this.setItems(createItems());
		this.setTabListeners(createTabListeners());
	}

	protected Widget createWidget() {
		return this.createDockPanel();
	}
	
	protected DockPanel getDockPanel(){
		return (DockPanel)this.getWidget();
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	public int getCount() {
		return this.getItems().size();
	}

	public TabItem get(final int index) {
		return (TabItem) this.getItems().get(index);
	}

	public int indexOf(final TabItem item) {
		Checker.notNull("parameter:item", item);

		return this.getItems().indexOf(item);
	}

	public void add(final TabItem item, final boolean closable) {
		this.insert(this.getCount(), item, closable);
	}

	public void insert(final int beforeIndex, final TabItem item, final boolean closable) {
		Checker.notNull("parameter:item", item);

		final Widget content = item.getContent();

		final Widget tab = this.getTabBarItemWidget(item, closable);
		this.getTabBarPanel().insert(tab, 1 + beforeIndex);
		this.getContentPanel().insert(content, beforeIndex);
		this.getItems().add(beforeIndex, item);
		item.setTabPanel(this);

		// if first tab auto select it...
		if (this.getCount() == 1) {
			this.select(0);
		}
	}

	/**
	 * Factory method which creates the widget that will appear within the
	 * tabbar
	 * 
	 * @param item
	 *            The item
	 * @param closable
	 *            A flag indicating whether or not a close button will appear
	 *            within the tab.
	 * @return The created widget
	 */
	protected Widget getTabBarItemWidget(final TabItem item, final boolean closable) {
		Checker.notNull("parameter:item", item);

		final HorizontalPanel panel = item.getTabWidgetPanel();
		panel.setStyleName(this.getTabBarItemStyleName());

		if (closable) {
			final Html html = new Html( "&nbsp;");
			html.setStyleName("");
			panel.add( html );

			final Image closeButton = this.createCloseButton();
			closeButton.addMouseEventListener(new MouseEventAdapter() {
				public void onClick(final MouseClickEvent event) {
					TabPanel.this.closeTab(item);
				}
			});
			panel.add(closeButton);
		}
		return panel;
	}

	protected void closeTab(final TabItem tabItem) {
		final TabListenerCollection listeners = this.getTabListeners();

		final BeforeTabCloseEvent beforeClose = new BeforeTabCloseEvent();
		beforeClose.setClosing(tabItem);
		listeners.fireBeforeTabClosed(beforeClose);

		if (false == beforeClose.isCancelled()) {
			this.remove(tabItem);

			final TabCloseEvent closed = new TabCloseEvent();
			closed.setClosed(tabItem);

			listeners.fireTabClosed(closed);
		} // if
	}

	public void remove(final int index) {
		// removing the currently selectedTab and pick another.
		if (index == this.getSelectedIndex()) {
			final int widgetCount = this.getCount();
			if (widgetCount > 1) {
				int newSelected = index + 1;
				if (newSelected == widgetCount) {
					newSelected = index - 1;
				}
				this.select(newSelected);
			}
		}
		final TabBarPanel tabPanel = this.getTabBarPanel();
		tabPanel.remove(tabPanel.getWidget(index + 1));

		final DeckPanel contentPanel = this.getContentPanel();
		contentPanel.remove(contentPanel.getWidget(index));

		final TabItem item = (TabItem) this.getItems().remove(index);
		item.clearTabPanel();
	}

	public boolean remove(final TabItem item) {
		Checker.notNull("parameter:item", item);

		final int index = this.indexOf(item);
		if (-1 != index) {
			this.remove(index);
		}
		return index != -1;
	}

	public Iterator iterator() {
		return this.getItems().iterator();
	}

	/**
	 * This list contains the individual items
	 */
	private List items;

	protected List getItems() {
		Checker.notNull("field:items", this.items);
		return this.items;
	}

	protected void setItems(final List items) {
		Checker.notNull("parameter:items", items);
		this.items = items;
	}

	protected List createItems() {
		return new ArrayList();
	}

	protected abstract String getTabBarItemStyleName();

	protected abstract String getTabBarItemLabelStyleName();

	protected Image createCloseButton() {
		final Image image = new Image();
		image.setStyleName(this.getTabBarItemWidgetStyleName());

		image.setUrl(this.getCloseButtonImageUrl());
		return image;
	}

	protected abstract String getTabBarItemWidgetStyleName();

	/**
	 * The url of the close image.
	 */
	private String closeButtonImageUrl;

	public String getCloseButtonImageUrl() {
		Checker.notEmpty("field:closeButtonImageUrl", closeButtonImageUrl);
		return closeButtonImageUrl;
	}

	public void setCloseButtonImageUrl(final String closeButtonImageUrl) {
		Checker.notEmpty("parameter:closeButtonImageUrl", closeButtonImageUrl);
		this.closeButtonImageUrl = closeButtonImageUrl;
	}

	/**
	 * Retrieves the index of the currently selected tab.
	 */
	public int getSelectedIndex() {
		return this.getContentPanel().getVisibleWidget();
	}

	public void select(final TabItem item) {
		this.select(this.indexOf(item));
	}

	public void select(final int index) {
		final TabItem newlySelectedItem = this.get(index);
		TabItem previousSelection = null;
		final int previouslySelectedIndex = this.getSelectedIndex();
		if (-1 != previouslySelectedIndex) {
			previousSelection = this.get(previouslySelectedIndex);
		}

		final BeforeTabSelectEvent beforeSelected = new BeforeTabSelectEvent();
		beforeSelected.setCurrentSelection(previousSelection);
		beforeSelected.setNewSelection(newlySelectedItem);

		final TabListenerCollection listeners = this.getTabListeners();
		listeners.fireBeforeTabSelected(beforeSelected);

		if (false == beforeSelected.isCancelled()) {
			final String selectedStyle = this.getTabBarItemSelectedStyleName();

			final TabBarPanel tabBarPanel = this.getTabBarPanel();
			final DeckPanel contentPanel = this.getContentPanel();

			// find the previously selected tab. and unselect it.
			final int previousIndex = contentPanel.getVisibleWidget();
			TabItem previouslySelectedTabItem = null;
			if (-1 != previousIndex) {
				final Widget tab = tabBarPanel.getWidget(previousIndex + 1);
				tab.removeStyleName(selectedStyle);
				previouslySelectedTabItem = this.get(previousIndex);
			}

			// apply the style to the new tab.
			final Widget tabBarItemPanel = tabBarPanel.getWidget(index + 1);
			tabBarItemPanel.addStyleName(selectedStyle);

			// tell the deckPanel to select a new sub-widget.
			contentPanel.showWidget(index);

			final TabSelectEvent selectedEvent = new TabSelectEvent();
			selectedEvent.setPreviouslySelected(previouslySelectedTabItem);
			selectedEvent.setCurrentSelection(newlySelectedItem);

			listeners.fireTabSelected(selectedEvent);
		}
	}

	protected abstract String getTabBarItemSelectedStyleName();

	/**
	 * This panel contains both the TabBar and contents panel.
	 */
	protected DockPanel getPanel() {
		return (DockPanel) this.getWidget();
	}

	protected DockPanel createDockPanel() {
		final DockPanel dockPanel = new DockPanel();

		final TabBarPanel tabBarPanel = this.createTabBarPanel();
		dockPanel.add((Widget) tabBarPanel, getTabBarDockPanelConstants());

		final DeckPanel contentPanel = this.createContentPanel();

		dockPanel.add(contentPanel, DockPanel.CENTER);
		dockPanel.setCellHeight(contentPanel, "100%");
		dockPanel.setCellWidth(contentPanel, "100%");

		return dockPanel;
	}

	abstract DockPanel.DockLayoutConstant getTabBarDockPanelConstants();

	/**
	 * This panel is used to house tab title widgets.
	 */

	protected TabBarPanel getTabBarPanel() {
		return (TabBarPanel)this.getDockPanel().getWidget( Constants.TAB_BAR_PANEL_INDEX );
	}

	protected abstract TabBarPanel createTabBarPanel();

	/**
	 * A DeckPanel is used to house all tab content. The selected tab selects
	 * the appropriate item from the deckPanel to be visible.
	 */

	public DeckPanel getContentPanel() {
		return (DeckPanel) this.getDockPanel().getWidget( Constants.DECK_PANEL_INDEX );
	}

	protected DeckPanel createContentPanel() {
		final DeckPanel panel = new DeckPanel();
		panel.setStyleName(getContentPanelStyleName());
		return panel;
	}

	protected abstract String getContentPanelStyleName();

	// LISTENERS
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * A collection of TabListeners who will in turn be notified of all
	 * TabListener events.
	 */
	private TabListenerCollection tabListeners;

	protected TabListenerCollection getTabListeners() {
		Checker.notNull("field:tabListeners", this.tabListeners);
		return this.tabListeners;
	}

	protected void setTabListeners(final TabListenerCollection tabListeners) {
		Checker.notNull("parameter:tabListeners", tabListeners);
		this.tabListeners = tabListeners;
	}

	protected TabListenerCollection createTabListeners() {
		return new TabListenerCollection();
	}

	public void addTabListener(final TabListener listener) {
		Checker.notNull("parameter:listener", listener);
		this.getTabListeners().add(listener);
	}

	public void removeTabListener(final TabListener listener) {
		this.getTabListeners().remove(listener);
	}

	/**
	 * This interface includes the common public methods from the
	 * HorizontalPanel and VerticalPanel classes.
	 * 
	 * @author Miroslav Pokorny (mP)
	 */
	static interface TabBarPanel {
		void add(Widget widget);

		void insert(Widget widget, int beforeIndex);

		Widget getWidget(int index);

		int getWidgetCount();

		int getWidgetIndex(Widget child);

		boolean remove(int index);

		boolean remove(Widget widget);
	}

	/**
	 * The classes below are necessary so that HorizontalPanel and VerticalPanel
	 * can share a common interface.
	 */
	static class HorizontalTabBarPanel extends HorizontalPanel implements TabBarPanel {
	}

	static class VerticalTabBarPanel extends VerticalPanel implements TabBarPanel {

	}
}
