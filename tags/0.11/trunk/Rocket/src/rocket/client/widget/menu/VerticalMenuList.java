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
package rocket.client.widget.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rocket.client.collection.CollectionHelper;
import rocket.client.collection.SkippingIterator;
import rocket.client.util.ObjectHelper;
import rocket.client.widget.VerticalPanel;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
/**
 * A vertical menu list is used as part of the menu ui to display all the children for a particular
 * SubMenu.
 * @author Miroslav Pokorny (mP)
 */
public class VerticalMenuList extends AbstractMenuList implements MenuList {

    public VerticalMenuList() {
        this.setWidget(this.createVerticalPanel());
    }

    public void open() {
        final List widgets = this.getPossibleShownMenuWidgets();
        this.sortByPriority(widgets);

        final int listLimit = this.getMenu().getListLimit();

        if (listLimit < widgets.size()) {
            final MenuWidget widget = (MenuWidget) widgets.get(listLimit);
            this.makeWidgetsVisible(widget.getPriority());
        } else {
            this.makeAllWidgetsVisible();
            this.getExpander().setVisible(false);
        }

        super.open();
    }

    protected void makeWidgetsVisible(final int hideIfLessThanPriority) {
        int hiddenItemCount = 0;
        final Widget lastOpened = this.hasLastOpened() ? this.getLastOpened() : null;

        // iterate showing/hiding all widgets...
        final Iterator children = this.iterator();
        while (children.hasNext()) {
            final Object object = children.next();
            if (false == (object instanceof MenuWidget)) {
                continue;
            }

            boolean show = true;
            while (true) {
                final MenuWidget menuWidget = (MenuWidget) object;

                // if the alwaysShown flag is true show the widget regardless.
                if (menuWidget.isAlwaysShown()) {
                    show = true;
                    break;
                }
                if (menuWidget.isDisabled()) {
                    show = false;
                    break;
                }

                // since it was the lastOpened widget show it anyway.
                if (lastOpened == menuWidget) {
                    show = true;
                    break;
                }

                // show it if its priority is gt $hideIfLessThanPriority
                show = menuWidget.getPriority() > hideIfLessThanPriority;
                break;
            }
            final Widget widget = (Widget) object;
            widget.setVisible(show);
            if (!show) {
                hiddenItemCount++;
            }
        }

        // if one widget was hidden need to enable expander...
        this.getExpander().setVisible(hiddenItemCount > 0);
    }

    protected void sortByPriority(final List widgets) {
        Collections.sort(widgets, new Comparator() {
            public int compare(final Object object, final Object otherObject) {
                final MenuWidget menuWidget = (MenuWidget) object;
                final MenuWidget otherMenuWidget = (MenuWidget) otherObject;

                return (otherMenuWidget.isAlwaysShown() ? Integer.MAX_VALUE : otherMenuWidget.getPriority())
                        - (menuWidget.isAlwaysShown() ? Integer.MAX_VALUE : menuWidget.getPriority());
            }
        });
    }

    /**
     * Returns a new list that includes all MenuWidgets that are not disabled.
     *
     * @return
     */
    protected List getPossibleShownMenuWidgets() {
        final List list = new ArrayList();
        final Iterator children = this.iterator();
        while (children.hasNext()) {
            final Object widget = children.next();
            if (widget instanceof MenuWidget) {
                final MenuWidget menuWidget = (MenuWidget) widget;
                if (false == menuWidget.isDisabled() || menuWidget.isAlwaysShown()) {
                    list.add(widget);
                }
            }
        }
        return list;
    }

    /**
     * Makes all menu items visible including those that are disabled.
     */
    protected void makeAllWidgetsVisible() {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            final Widget widget = (Widget) iterator.next();
            widget.setVisible(true);
        }
    }

    public void close() {
        super.close();

        this.setVisible(false);
    }

    // PANEL ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public boolean insert(final Widget widget, final int beforeIndex) {
        final boolean inserted = this.getVerticalPanel().insert(widget, beforeIndex);
        if (inserted) {
            this.afterInsert(widget);
        }
        return inserted;
    }

    public boolean remove(final Widget widget) {
        final boolean removed = this.getVerticalPanel().remove(widget);
        if (removed) {
            afterRemove(widget);
        }
        return removed;
    }

    public int getCount() {
        return this.getVerticalPanel().getWidgetCount() - 1;
    }

    public Iterator iterator() {
        final Widget expander = this.getExpander();

        // create a skippingIterator which returns all of verticalPanel's widgets except for the expander.
        final SkippingIterator iterator = new SkippingIterator() {
            protected boolean skip(final Object object) {
                return expander == object;
            }
        };
        iterator.setIterator(this.getVerticalPanel().iterator());
        return iterator;
    }

    public void clear() {
        CollectionHelper.removeAll(this.iterator());
    }

    /**
     * A verticalPanel is used as the container for all child widgets.
     */
    private VerticalPanel verticalPanel;

    protected VerticalPanel getVerticalPanel() {
        ObjectHelper.checkNotNull("field:verticalPanel", verticalPanel);
        return this.verticalPanel;
    }

    protected void setVerticalPanel(final VerticalPanel verticalPanel) {
        ObjectHelper.checkNotNull("parameter:verticalPanel", verticalPanel);
        this.verticalPanel = verticalPanel;
    }

    protected VerticalPanel createVerticalPanel() {
        final VerticalPanel panel = new VerticalPanel();
        panel.addStyleName(MenuConstants.VERTICAL_MENU_LIST_STYLE);
        panel.add(this.createExpander());
        this.setVerticalPanel(panel);
        return panel;
    }

    public boolean isHorizontalLayout() {
        return false;
    }

    // VERTICAL MENU LIST ::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * Records the last opened widget. The lastOpened widget will always be shown regardless of its disabled/priority.
     */
    private Widget lastOpened;

    protected Widget getLastOpened() {
        ObjectHelper.checkNotNull("field:lastOpened", lastOpened);
        return lastOpened;
    }

    protected boolean hasLastOpened() {
        return null != this.lastOpened;
    }

    protected void setLastOpened(final Widget lastOpened) {
        ObjectHelper.checkNotNull("parameter:lastOpened", lastOpened);
        this.lastOpened = lastOpened;
    }

    /**
     * The expander control which when clicked shows all items within this menu list.
     */
    private Widget expander;

    protected Widget getExpander() {
        ObjectHelper.checkNotNull("field:expander", expander);
        return expander;
    }

    protected boolean hasExpander() {
        return null != this.expander;
    }

    protected void setExpander(final Widget expander) {
        ObjectHelper.checkNotNull("parameter:expander", expander);
        this.expander = expander;
    }

    protected Widget createExpander() {
        WidgetHelper.checkNotAlreadyCreated("expander", this.hasExpander());

        final VerticalMenuList that = this;
        final Image expander = new Image() {
            public void onBrowserEvent(final Event event) {
                final int eventType = DOM.eventGetType(event);
                if (eventType == Event.ONCLICK || (eventType == Event.ONMOUSEOVER && that.getMenu().isAutoOpen())) {
                    that.makeAllWidgetsVisible();
                    that.getExpander().setVisible(false);
                }
            }
        };
        expander.setUrl(MenuConstants.EXPANDER_IMAGE_URL);
        expander.addStyleName(MenuConstants.VERTICAL_MENU_LIST_EXPANDER_STYLE);
        expander.setVisible(false);
        this.setExpander(expander);
        return expander;
    }
}
