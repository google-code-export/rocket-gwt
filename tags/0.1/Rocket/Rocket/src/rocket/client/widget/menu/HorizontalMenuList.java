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

import java.util.Iterator;

import rocket.client.collection.CollectionHelper;
import rocket.client.collection.SkippingIterator;
import rocket.client.util.ObjectHelper;
import rocket.client.widget.HorizontalPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
* A horizontalMenuList provides the widget that implements a menubar.
* @author Miroslav Pokorny (mP)
*/
public class HorizontalMenuList extends AbstractMenuList implements MenuList {

    public HorizontalMenuList() {
    	this.initWidget(this.createHorizontalPanel());
    }

    // PANEL ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void insert(final Widget widget, final int beforeIndex) {
        this.getHorizontalPanel().insert(widget, beforeIndex);
        this.afterInsert(widget);
    }
    

    public boolean remove(final Widget widget) {
        final boolean removed = this.getHorizontalPanel().remove(widget);
        if (removed) {
            afterRemove(widget);
        }
        return removed;
    }

    public int getCount() {
        return this.getHorizontalPanel().getWidgetCount() - 1;
    }

    public Iterator iterator() {
        final HorizontalPanel panel = this.getHorizontalPanel();
        final Widget padder = panel.getWidget(panel.getWidgetCount() - 1);

        // create a skippingIterator which returns all of horizontalPanel's widgets except for the expander.
        final SkippingIterator iterator = new SkippingIterator() {
            protected boolean skip(final Object object) {
                return padder == object;
            }
        };
        iterator.setIterator(panel.iterator());
        return iterator;
    }

    public void clear() {
        CollectionHelper.removeAll(this.iterator());
    }

    /**
     * A horizontalPanel is used as the container for all child widgets.
     */
    private HorizontalPanel horizontalPanel;

    protected HorizontalPanel getHorizontalPanel() {
        ObjectHelper.checkNotNull("field:horizontalPanel", horizontalPanel);
        return this.horizontalPanel;
    }

    protected void setHorizontalPanel(final HorizontalPanel horizontalPanel) {
        ObjectHelper.checkNotNull("parameter:horizontalPanel", horizontalPanel);
        this.horizontalPanel = horizontalPanel;
    }

    protected HorizontalPanel createHorizontalPanel() {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth( "100%");
        panel.addStyleName(MenuConstants.HORIZONTAL_MENU_LIST_STYLE);

        final Widget rightmost = new HTML("&nbsp;");
        panel.add(rightmost);
        panel.setCellWidth(rightmost, "100%");

        this.setHorizontalPanel(panel);
        return panel;
    }

    public boolean isHorizontalLayout() {
        return true;
    }
}
