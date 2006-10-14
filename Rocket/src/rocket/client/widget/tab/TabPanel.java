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
package rocket.client.widget.tab;

import java.util.Iterator;

import rocket.client.collection.IteratorView;
import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
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

    public TabPanel() {
        super();

        this.initWidget((Widget) this.createPanel());
        this.createTabListeners();
    }

    /**
     * Returns the number of tabs currently contained.
     * 
     * @return
     */
    public int getTabCount() {
        return this.getContentPanel().getWidgetCount();
    }

    public void addTab(final String title, final boolean closable, final Widget content) {
        StringHelper.checkNotEmpty("parameter:title", title);
        ObjectHelper.checkNotNull("parameter:content", content);

        this.insertTab(title, closable, content, this.getTabCount());
    }

    public void insertTab(final String title, final boolean closable, final Widget content, final int beforeIndex) {
        StringHelper.checkNotEmpty("parameter:title", title);
        ObjectHelper.checkNotNull("parameter:content", content);

        final Widget tab = this.createTab(title, closable);
        this.getTabBarPanel().insert(tab, 1 + beforeIndex);
        this.getContentPanel().insert(content, beforeIndex);

        // if first tab auto select it...
        if (this.getTabCount() == 1) {
            this.selectTab(0);
        }

        this.increaseModificationCount();
    }

    /**
     * Factory method which creates the widget that will appear within the tabs title.
     * 
     * @param title
     *            The title
     * @param closable
     *            A flag indicating whether or not a close button will appear within the tab.
     * @return The created widget
     */
    protected Widget createTab(final String title, final boolean closable) {
        StringHelper.checkNotEmpty("parameter:title", title);

        final HorizontalPanel panel = new HorizontalPanel();
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        panel.addStyleName(this.getTabBarItemStyleName());

        final TabPanel that = this;

        final Label label = (Label) this.createLabel(title);
        label.addClickListener(new ClickListener() {
            public void onClick(final Widget widget) {
                that.selectTab(that.getIndex(label.getText()));
            }
        });
        panel.add(label);

        if (closable) {
            final HTML spacer = new HTML("&nbsp;");
            panel.add(spacer);

            final Image closeButton = this.createCloseButton();
            closeButton.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    that.removeTab(that.getIndex(label.getText()));
                }
            });
            panel.add(closeButton);
        }
        return panel;
    }

    protected abstract String getTabBarItemStyleName();

    protected Widget createLabel(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);

        final Label label = new Label(text);
        label.addStyleName(this.getTabBarItemLabelStyleName());
        return label;
    }

    protected abstract String getTabBarItemLabelStyleName();

    protected Image createCloseButton() {
        final Image image = new Image();
        image.addStyleName(this.getTabBarItemWidgetStyleName());

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

    public void removeTab(final int index) {
        final String title = this.getTabTitle(index);
        final Widget content = this.getContent(index);

        final TabListenerCollection listeners = this.getTabListeners();
        if (listeners.fireBeforeTabClosed(title, content)) {

            // removing the currently selectedTab pick another.
            if (title.equals(this.getSelectedTabTitle())) {
                final int widgetCount = this.getTabCount();
                if (widgetCount > 1) {
                    int newSelected = index + 1;
                    if (newSelected == widgetCount) {
                        newSelected = index - 1;
                    }
                    this.selectTab(newSelected);
                }
            }

            final HorizontalOrVerticalPanel tabPanel = this.getTabBarPanel();
            tabPanel.remove(tabPanel.getWidget(index + 1));

            final DeckPanel contentPanel = this.getContentPanel();
            contentPanel.remove(contentPanel.getWidget(index));

            this.increaseModificationCount();

            listeners.fireTabClosed(title, content);
        } // if
    }

    public void selectTab(final int index) {
        final String title = this.getTabTitle(index);
        final TabListenerCollection listeners = this.getTabListeners();

        final Widget content = this.getContent(index);
        if (listeners.fireBeforeTabSelected(title, content)) {
            final String selectedStyle = this.getTabBarItemSelectedStyleName();

            final DeckPanel contentPanel = this.getContentPanel();

            // find the previously selected tab. and unselect it.
            final int previousIndex = contentPanel.getVisibleWidget();
            if (-1 != previousIndex) {
                final Panel tab = this.getTabPanel(previousIndex);
                tab.removeStyleName(selectedStyle);
            }

            // apply the style to the new tab.
            final int newIndex = this.getIndex(title);
            final Widget tabTitlePanel = this.getTabPanel(newIndex);
            tabTitlePanel.addStyleName(selectedStyle);

            // tell the deckPanel to select a new sub-widget.
            contentPanel.showWidget(newIndex);

            listeners.fireTabSelected(title, content);
        }
    }

    protected abstract String getTabBarItemSelectedStyleName();

    protected int getTabIndex(final String title) {
        StringHelper.checkNotEmpty("parameter:title", title);

        int index = -1;

        final Iterator iterator = this.tabTitlesIterator();
        int i = 0;
        while (iterator.hasNext()) {
            final String otherTabTitle = (String) iterator.next();
            if (title.equals(otherTabTitle)) {
                index = i;
                break;
            }
            i++;
        }

        return index;
    }

    protected HorizontalPanel getTabPanel(final int index) {
        return (HorizontalPanel) this.getTabBarPanel().getWidget(index + 1);// SKIP
        // the
        // leading
        // Label.
    }

    public String getTabTitle(final int index) {
        final Label label = this.getTabTitleLabel(index);
        return label.getText();
    }

    public void setTabTitle(final int index, final String title) {
        StringHelper.checkNotEmpty("parameter:title", title);

        final Label label = this.getTabTitleLabel(index);
        label.setText(title);
    }

    protected Label getTabTitleLabel(final int index) {
        final HorizontalPanel panel = this.getTabPanel(index);
        final Label label = (Label) panel.getWidget(0);
        return label;
    }

    public Widget getContent(final int index) {
        return (Widget) this.getContentPanel().getWidget(index);
    }

    /**
     * Retrieves the index of a tab given its title.
     * 
     * @param title
     * @return The index of the tab if it is found or -1 if one wasnt.
     */
    public int getIndex(final String title) {
        StringHelper.checkNotEmpty("parameter:title", title);

        int index = -1;

        final Iterator names = this.tabTitlesIterator();
        int i = 0;

        while (names.hasNext()) {
            final String otherTitle = (String) names.next();
            if (title.equals(otherTitle)) {
                index = i;
                break;
            }
            i++;
        }
        return index;
    }

    /**
     * Retrieves the index of a tab given its widget.
     * 
     * @param content
     * @return
     */
    public int getIndex(final Widget content) {
        ObjectHelper.checkNotNull("parameter:content", content);

        return this.getContentPanel().getWidgetIndex(content);
    }

    /**
     * Returns the title of the selected tab.
     * 
     * @return
     */
    public String getSelectedTabTitle() {
        return this.getTabTitle(this.getSelectedTabIndex());
    }

    public int getSelectedTabIndex() {
        return this.getContentPanel().getVisibleWidget();
    }

    /**
     * This verticalPanel contains both the TabBar and contents panel.
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
        this.setContentPanel(panel);

        panel.addStyleName(getContentPanelStyleName());
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

    protected void createTabListeners() {
        this.setTabListeners(new TabListenerCollection());
    }

    public void addTabListener(final TabListener listener) {
        ObjectHelper.checkNotNull("parameter:listener", listener);
        this.getTabListeners().add(listener);
    }

    public void removeTabListener(final TabListener listener) {
        this.getTabListeners().remove(listener);
    }

    // ITERATORS
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public Iterator tabPanelsIterator() {
        final TabPanelIterator iterator = new TabPanelIterator();
        iterator.setTabPanel(this);
        iterator.syncModificationCounters();
        iterator.setViewType(TabConstants.ITERATOR_TAB_PANELS_VIEW);
        return iterator;
    }

    public Iterator tabTitlesIterator() {
        final TabPanelIterator iterator = new TabPanelIterator();
        iterator.setTabPanel(this);
        iterator.setModificationCounter(this.getModificationCounter());
        iterator.setViewType(TabConstants.ITERATOR_TITLES_VIEW);
        return iterator;
    }

    public Iterator tabContentsIterator() {
        final TabPanelIterator iterator = new TabPanelIterator();
        iterator.setTabPanel(this);
        iterator.setModificationCounter(this.getModificationCounter());
        iterator.setViewType(TabConstants.ITERATOR_CONTENTS_VIEW);
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
     * This iterator provides a view of a TabPanel.
     * 
     * @author Miroslav Pokorny (mP)
     */
    class TabPanelIterator extends IteratorView {

        protected boolean hasNext0() {
            return this.getIndex() < this.getTabPanel().getTabCount();
        }

        protected Object next0(final int type) {
            Object object = null;
            final TabPanel tabPanel = this.getTabPanel();
            final int index = this.getIndex();
            while (true) {
                if (TabConstants.ITERATOR_TITLES_VIEW == type) {
                    object = tabPanel.getTabTitle(index);
                    break;
                }
                if (TabConstants.ITERATOR_CONTENTS_VIEW == type) {
                    object = tabPanel.getContent(index);
                    break;
                }
                if (TabConstants.ITERATOR_TAB_PANELS_VIEW == type) {
                    object = tabPanel.getTabPanel(index);
                    break;
                }
                break;
            }

            return object;
        }

        protected void leavingNext() {
            this.setIndex(this.getIndex() + 1);
        }

        protected void remove0() {
            final TabPanel parent = this.getTabPanel();
            final int index = this.getIndex() - 1;
            parent.removeTab(index);
            this.setIndex(index);
        }

        protected int getParentModificationCounter() {
            return this.getTabPanel().getModificationCounter();
        }

        /**
         * The parent tabpanel being iterated over.
         */
        TabPanel tabPanel;

        TabPanel getTabPanel() {
            ObjectHelper.checkNotNull("field:tabPanel", tabPanel);
            return this.tabPanel;
        }

        void setTabPanel(final TabPanel tabPanel) {
            ObjectHelper.checkNotNull("parameter:tabPanel", tabPanel);
            this.tabPanel = tabPanel;
        }

        int index;

        int getIndex() {
            return index;
        }

        void setIndex(final int index) {
            this.index = index;
        }

        public String toString() {
            return super.toString() + ", tabPanel: " + tabPanel + ", index: " + index;
        }
    }

    /**
     * This interface includes the common public methods from the HorizontalPanel and VerticalPanel classes.
     * 
     * @author mP
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
     * This class is necessary so that HorizontalPanel and VerticalPanel can share a common interface.
     */
    class HorizontalPanelImpl extends HorizontalPanel implements HorizontalOrVerticalPanel {
    }

    class VerticalPanelImpl extends VerticalPanel implements HorizontalOrVerticalPanel {

    }
}
