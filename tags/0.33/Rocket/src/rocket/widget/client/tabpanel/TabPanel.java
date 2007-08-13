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

import rocket.collection.client.IteratorView;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A TabPanel provides the capability to manage a number of tabs along with providing listeners for the major events that may occur to an
 * individual tab, selection and closing/removal.
 * 
 * This class combines a DeckPanel to hold tab contents and a HorizontalPanel to hold the tabs themselves. This class is a rewrite of the
 * GWT provided one adding a few features and fixing bugs.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class TabPanel extends Composite {

    protected TabPanel() {
        super();

        this.setItems( createItems() );
        this.setTabListeners( createTabListeners() );
        
        final HorizontalOrVerticalPanel panel = this.createPanel();
        this.setPanel(panel);
        this.initWidget((Widget) panel );
    }

    public int getCount() {
        return this.getItems().size();
    }

    public TabItem get(final int index) {
        return (TabItem) this.getItems().get(index);
    }

    public int getIndex(final TabItem item) {
        ObjectHelper.checkNotNull("parameter:item", item);

        return this.getItems().indexOf(item);
    }

    public void add(final TabItem item, final boolean closable) {
        this.insert(this.getCount(), item, closable);
    }

    public void insert(final int beforeIndex, final TabItem item, final boolean closable) {
        ObjectHelper.checkNotNull("parameter:item", item);

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

        this.increaseModificationCount();
    }

    /**
     * Factory method which creates the widget that will appear within the tabbar
     * 
     * @param item
     *            The item
     * @param closable
     *            A flag indicating whether or not a close button will appear within the tab.
     * @return The created widget
     */
    protected Widget getTabBarItemWidget(final TabItem item, final boolean closable) {
        ObjectHelper.checkNotNull("parameter:item", item);

        final HorizontalPanel panel = item.getTabWidgetPanel();
        panel.setStyleName(this.getTabBarItemStyleName());

        if (closable) {
            panel.add(new HTML("&nbsp;"));

            final Image closeButton = this.createCloseButton();
            closeButton.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    TabPanel.this.remove(item);
                }
            });
            panel.add(closeButton);
        }
        return panel;
    }

    public void remove(final int index) {
        final TabListenerCollection listeners = this.getTabListeners();
        final TabItem item = this.get(index);

        if (listeners.fireBeforeTabClosed(item)) {

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

            final HorizontalOrVerticalPanel tabPanel = this.getTabBarPanel();
            tabPanel.remove(tabPanel.getWidget(index + 1));

            final DeckPanel contentPanel = this.getContentPanel();
            contentPanel.remove(contentPanel.getWidget(index));

            this.getItems().remove(index);
            item.clearTabPanel();

            listeners.fireTabClosed(item);
        } // if

        this.increaseModificationCount();
    }

    public boolean remove(final TabItem item) {
        ObjectHelper.checkNotNull("parameter:item", item);

        final int index = this.getIndex(item);
        if (-1 != index) {
            this.remove(index);
        }
        return index != -1;
    }

    /**
     * Returns an iterator of TabItems.
     * 
     * @return
     */
    public Iterator iterator() {
        final IteratorView iterator = new IteratorView() {

            protected boolean hasNext0() {
                return this.getCursor() < TabPanel.this.getCount();
            }

            protected Object next0() {
                final int index = this.getCursor();
                return get(index);
            }

            protected void afterNext() {
                this.setCursor(this.getCursor() + 1);
            }

            protected void remove0() {
                final int index = this.getCursor() - 1;
                TabPanel.this.remove(index);
                this.setCursor(index);
            }

            protected int getModificationCounter() {
                return TabPanel.this.getModificationCounter();
            }

            /**
             * A pointer to the next tab item within the parent TabPanel
             */
            int cursor;

            int getCursor() {
                return cursor;
            }

            void setCursor(final int cursor) {
                this.cursor = cursor;
            }

            public String toString() {
                return super.toString() + ", cursor: " + cursor;
            }
        };

        iterator.syncModificationCounters();
        return iterator;
    }

    /**
     * Helps keep track of concurrent modification of the parent.
     */
    private int modificationCount;

    protected int getModificationCounter() {
        return this.modificationCount;
    }

    public void setModificationCounter(final int modificationCount) {
        this.modificationCount = modificationCount;
    }

    protected void increaseModificationCount() {
        this.setModificationCounter(this.getModificationCounter() + 1);
    }

    /**
     * This list contains the individual items
     */
    private List items;

    protected List getItems() {
        ObjectHelper.checkNotNull("field:items", this.items);
        return this.items;
    }

    protected void setItems(final List items) {
        ObjectHelper.checkNotNull("parameter:items", items);
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
     * . The url of the up icon
     */
    private String closeButtonImageUrl;

    public String getCloseButtonImageUrl() {
        StringHelper.checkNotEmpty("field:closeButtonImageUrl", closeButtonImageUrl);
        return closeButtonImageUrl;
    }

    public void setCloseButtonImageUrl(final String closeButtonImageUrl) {
        StringHelper.checkNotEmpty("parameter:closeButtonImageUrl", closeButtonImageUrl);
        this.closeButtonImageUrl = closeButtonImageUrl;
    }

    /**
     * Retrieves the index of the currently selected tab.
     */
    public int getSelectedIndex() {
        return this.getContentPanel().getVisibleWidget();
    }

    public void select(final TabItem item) {
        this.select(this.getIndex(item));
    }

    public void select(final int index) {
        final TabListenerCollection listeners = this.getTabListeners();
        final TabItem item = this.get(index);

        if (listeners.fireBeforeTabSelected(item)) {
            final String selectedStyle = this.getTabBarItemSelectedStyleName();

            final HorizontalOrVerticalPanel tabBarPanel = this.getTabBarPanel();
            final DeckPanel contentPanel = this.getContentPanel();

            // find the previously selected tab. and unselect it.
            final int previousIndex = contentPanel.getVisibleWidget();
            if (-1 != previousIndex) {
                final Widget tab = tabBarPanel.getWidget(previousIndex + 1);
                tab.removeStyleName(selectedStyle);
            }

            // apply the style to the new tab.
            final Widget tabBarItemPanel = tabBarPanel.getWidget(index + 1);
            tabBarItemPanel.addStyleName(selectedStyle);

            // tell the deckPanel to select a new sub-widget.
            contentPanel.showWidget(index);

            listeners.fireTabSelected(item);
        }
    }

    protected abstract String getTabBarItemSelectedStyleName();

    /**
     * This panel contains both the TabBar and contents panel.
     */
    private HorizontalOrVerticalPanel panel;

    protected HorizontalOrVerticalPanel getPanel() {
        ObjectHelper.checkNotNull("field:panel", panel);
        return panel;
    }

    protected boolean hasPanel() {
        return null == this.panel;
    }

    protected void setPanel(final HorizontalOrVerticalPanel panel) {
        ObjectHelper.checkNotNull("parameter:panel", panel);
        this.panel = panel;
    }

    protected abstract HorizontalOrVerticalPanel createPanel();

    /**
     * This panel is used to house tab title widgets.
     */
    private HorizontalOrVerticalPanel tabBarPanel;

    protected HorizontalOrVerticalPanel getTabBarPanel() {
        ObjectHelper.checkNotNull("field:tabBarPanel", tabBarPanel);
        return tabBarPanel;
    }

    protected void setTabBarPanel(final HorizontalOrVerticalPanel tabBarPanel) {
        ObjectHelper.checkNotNull("parameter:tabBarPanel", tabBarPanel);
        this.tabBarPanel = tabBarPanel;
    }

    protected abstract HorizontalOrVerticalPanel createTabBarPanel();

    /**
     * A DeckPanel is used to house all tab content. The selected tab selects the appropriate item from the deckPanel to be visible.
     */
    private DeckPanel contentPanel;

    public DeckPanel getContentPanel() {
        ObjectHelper.checkNotNull("field:contentPanel", contentPanel);
        return contentPanel;
    }

    public void setContentPanel(final DeckPanel contentPanel) {
        ObjectHelper.checkNotNull("parameter:contentPanel", contentPanel);
        this.contentPanel = contentPanel;
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
     * A collection of TabListeners who will in turn be notified of all TabListener events.
     */
    private TabListenerCollection tabListeners;

    protected TabListenerCollection getTabListeners() {
        ObjectHelper.checkNotNull("field:tabListeners", this.tabListeners);
        return this.tabListeners;
    }

    protected void setTabListeners(final TabListenerCollection tabListeners) {
        ObjectHelper.checkNotNull("parameter:tabListeners", tabListeners);
        this.tabListeners = tabListeners;
    }

    protected TabListenerCollection createTabListeners() {
        return new TabListenerCollection();
    }

    public void addTabListener(final TabListener listener) {
        ObjectHelper.checkNotNull("parameter:listener", listener);
        this.getTabListeners().add(listener);
    }

    public void removeTabListener(final TabListener listener) {
        this.getTabListeners().remove(listener);
    }

    /**
     * This interface includes the common public methods from the HorizontalPanel and VerticalPanel classes.
     * 
     * @author Miroslav Pokorny (mP)
     */
    interface HorizontalOrVerticalPanel {
        void add(Widget widget);

        void insert(Widget widget, int beforeIndex);

        Widget getWidget(int index);

        int getWidgetCount();

        int getWidgetIndex(Widget child);

        boolean remove(int index);

        boolean remove(Widget widget);
    }

    /**
     * The classes below are necessary so that HorizontalPanel and VerticalPanel can share a common interface.
     */
    class HorizontalPanelImpl extends HorizontalPanel implements HorizontalOrVerticalPanel {
    }

    class VerticalPanelImpl extends VerticalPanel implements HorizontalOrVerticalPanel {

    }
}
