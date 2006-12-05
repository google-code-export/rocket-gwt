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

import rocket.collection.client.SkippingIterator;
import rocket.util.client.ObjectHelper;
import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A HorizontalMenuList contains menu items that grow across the screen. HorizontalMenuLists are mostly only used as the MenuList for Menu
 * widgets.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HorizontalMenuList extends MenuList {

    public HorizontalMenuList() {
        super();

        this.initWidget(this.createHorizontalPanel());
    }

    // PANEL
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public Widget get(final int index) {
        return this.getHorizontalPanel().getWidget(index);
    }

    public void insert(final Widget widget, final int beforeIndex) {
        this.getHorizontalPanel().insert(widget, beforeIndex);
        this.afterInsert(widget);
    }

    protected boolean remove0(final Widget widget) {
        return this.getHorizontalPanel().remove(widget);
    }

    public int getWidgetCount() {
        return this.getHorizontalPanel().getWidgetCount() - 1;
    }

    public Iterator iterator() {
        // create a skippingIterator which returns all of horizontalPanel's
        // widgets except for the padder.
        final SkippingIterator iterator = new SkippingIterator() {
            protected boolean skip(final Object object) {
                return getPadder() == object;
            }
        };
        iterator.setIterator(this.getHorizontalPanel().iterator());
        return iterator;
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
        panel.setWidth("100%");
        panel.addStyleName(MenuConstants.HORIZONTAL_MENU_LIST_STYLE);

        final Widget padder = this.createPadder();
        panel.add(padder);
        panel.setCellWidth(padder, "100%");

        this.setHorizontalPanel(panel);
        return panel;
    }

    /**
     * An extra padding widget is added as the last item in the HorizontalPanel.
     */
    private Widget padder;

    protected Widget getPadder() {
        ObjectHelper.checkNotNull("field:padder", padder);
        return padder;
    }

    protected void setPadder(final Widget padder) {
        ObjectHelper.checkNotNull("parameter:padder", padder);
        this.padder = padder;
    }

    protected Widget createPadder() {
        final Widget padder = new HTML("&nbsp;");
        this.setPadder(padder);
        return padder;
    }
}
