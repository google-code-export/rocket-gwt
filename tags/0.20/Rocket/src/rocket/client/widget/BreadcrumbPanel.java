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
package rocket.client.widget;

import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

/**
 * This particular panel allows widgets (which should be links) automatically creating a breadcrumb illusion. Whenever a new breadcrumb is
 * added it becomes the new last breadcrumb and as such is made disabled.
 * 
 * This panel functions as a stack of breadcrumbs, thus breadcrumbs must be pushed or popped and cannot be inserted in the middle of the
 * chain.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class BreadcrumbPanel extends Composite {

    public BreadcrumbPanel() {
        this.initWidget(this.createHorizontalPanel());
    }

    /**
     * A horizontal panel contains all the individual breadcrumbs and their corresponding separators.
     */
    private HorizontalPanel horizontalPanel;

    protected HorizontalPanel getHorizontalPanel() {
        ObjectHelper.checkNotNull("field:horizontalPanel", horizontalPanel);
        return this.horizontalPanel;
    }

    protected boolean hasHorizontalPanel() {
        return null != this.horizontalPanel;
    }

    protected void setHorizontalPanel(final HorizontalPanel horizontalPanel) {
        ObjectHelper.checkNotNull("field:horizontalPanel", horizontalPanel);
        this.horizontalPanel = horizontalPanel;
    }

    protected HorizontalPanel createHorizontalPanel() {
        ObjectHelper.checkPropertyNotSet("horizontalPanel", this, this.hasHorizontalPanel());

        final HorizontalPanel panel = new HorizontalPanel();
        panel.addStyleName(WidgetConstants.BREADCRUMBS_STYLE);
        this.setHorizontalPanel(panel);
        return panel;
    }

    /**
     * Adds a new breadcrumb to the panel. The given clickListener will be notified whenever the user selects the corresponding breadcrumb.
     * 
     * @param add
     *            The text that appears within the hyper link
     * @param clickListener
     *            The listener that will be notified.
     */
    public boolean push(final String text, final ClickListener clickListener) {
        StringHelper.checkNotEmpty("parameter:text", text);
        ObjectHelper.checkNotNull("parameter:clickListener", clickListener);

        final HorizontalPanel panel = this.getHorizontalPanel();

        // change the old last breadcrumb to be clickable as it is no longer the
        // last breadcrumb.
        final int widgetCount = panel.getWidgetCount();
        if (widgetCount > 0) {
            this.updateFormerLastBreadcrumb();
        }

        // create and add the new breadcrumb.
        final Breadcrumb breadcrumb = this.createBreadcrumb(text);
        breadcrumb.setBreadcrumbPanel(this);
        final boolean disabled = clickListener == null;
        breadcrumb.setDisabled(disabled);
        if (!disabled) {
            breadcrumb.addClickListener(clickListener);
        }
        panel.add(breadcrumb);
        panel.add(this.createSpacer());

        this.updateLastBreadcrumb();

        return true;
    }

    protected Breadcrumb createBreadcrumb(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);

        final Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setText(text);
        breadcrumb.addStyleName(WidgetConstants.BREADCRUMB_ITEM_STYLE);
        return breadcrumb;
    }

    class Breadcrumb extends Hyperlink {

        public void onBrowserEvent(final Event event) {
            ObjectHelper.checkNotNull("parameter:event", event);

            // if this breadcrumb is the last or disabled cancel any click
            // events.
            if (false == this.isDisabled() && false == this.getBreadcrumbPanel().isLastBreadcrumb(this)) {
                super.onBrowserEvent(event);
            }
        }

        BreadcrumbPanel breadcrumbPanel;

        BreadcrumbPanel getBreadcrumbPanel() {
            ObjectHelper.checkNotNull("field:breadcrumbPanel", breadcrumbPanel);
            return breadcrumbPanel;
        }

        void setBreadcrumbPanel(final BreadcrumbPanel breadcrumbPanel) {
            ObjectHelper.checkNotNull("parameter:breadcrumbPanel", breadcrumbPanel);
            this.breadcrumbPanel = breadcrumbPanel;
        }

        boolean disabled;

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(final boolean disabled) {
            this.disabled = disabled;
        }

        public String toString() {
            return super.toString() + ", disabled: " + disabled;
        }
    }

    protected Widget createSpacer() {
        final HTML spacer = new HTML(WidgetConstants.BREADCRUMB_SEPARATOR_HTML);
        spacer.addStyleName(WidgetConstants.BREADCRUMB_SEPARATOR_STYLE);
        spacer.setVisible(false);
        return spacer;
    }

    /**
     * Removes all breadcrumbs.
     */
    public void clear() {
        final HorizontalPanel panel = this.getHorizontalPanel();
        final int widgetCount = panel.getWidgetCount() / 2;
        for (int i = 0; i < widgetCount; i++) {
            this.pop();
        }
    }

    /**
     * Removes the topmost or last breadcrumb.
     */
    public boolean pop() {
        boolean removed = false;

        final HorizontalPanel panel = this.getHorizontalPanel();
        final int widgetCount = panel.getWidgetCount();
        if (widgetCount == 0) {
            SystemHelper.handleAssertFailure("Unable to pop a breadcrumb - this breadcrumb panel is already empty");
        }

        // remove the breadcrumb and then the spacer.
        final Widget breadcrumb = panel.getWidget(widgetCount - 1 - 1);
        final Widget spacer = panel.getWidget(widgetCount - 1);

        panel.remove(breadcrumb);
        panel.remove(spacer);

        this.updateLastBreadcrumb();
        return removed;
    }

    protected void updateFormerLastBreadcrumb() {
        final HorizontalPanel panel = this.getHorizontalPanel();
        final int index = panel.getWidgetCount() - 1 - 1;
        if (index >= 0) {
            final Widget breadcrumb = panel.getWidget(index);
            breadcrumb.removeStyleName(WidgetConstants.BREADCRUMB_LAST_ITEM_STYLE);

            final Widget spacer = panel.getWidget(index + 1);
            spacer.setVisible(true);
        }
    }

    protected void updateLastBreadcrumb() {
        final HorizontalPanel panel = this.getHorizontalPanel();
        final int index = panel.getWidgetCount() - 1 - 1;
        if (index >= 0) {
            final Widget breadcrumb = panel.getWidget(index);
            breadcrumb.addStyleName(WidgetConstants.BREADCRUMB_LAST_ITEM_STYLE);

            final Widget spacer = panel.getWidget(index + 1);
            spacer.setVisible(false);
        }
    }

    protected boolean isLastBreadcrumb(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final HorizontalPanel panel = this.getHorizontalPanel();
        return widget == panel.getWidget(panel.getWidgetCount() - 2);
    }
}
