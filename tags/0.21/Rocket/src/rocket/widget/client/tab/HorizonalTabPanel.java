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
package rocket.widget.client.tab;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

/**
 * The HorizontalTabPanel class is the base class for both the TopTabPanel and BottomTabPanel classes.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class HorizonalTabPanel extends TabPanel {

    protected HorizonalTabPanel() {
        super();
    }

    protected HorizontalOrVerticalPanel createTabBarPanel(final VerticalAlignmentConstant alignment) {
        ObjectHelper.checkNotNull("parameter:alignment", alignment);

        final HorizontalPanelImpl panel = new HorizontalPanelImpl();
        this.setTabBarPanel(panel);

        panel.addStyleName(this.getTabBarStyleName());
        panel.setVerticalAlignment(alignment);

        final Widget first = this.createTabBarBeforeSpacer();
        final Widget rest = this.createTabBarAfterSpacer();
        panel.add(first);
        panel.add(rest);
        panel.setCellHeight(first, "100%");
        panel.setCellWidth(rest, "100%");
        panel.setCellHorizontalAlignment(rest, HasHorizontalAlignment.ALIGN_RIGHT);
        return panel;
    }

    protected abstract String getTabBarStyleName();

    protected Widget createTabBarBeforeSpacer() {
        final HTML widget = new HTML("&nbsp;");
        widget.addStyleName(this.getTabBarBeforeSpacerStyleName());
        widget.setHeight("100%");
        return widget;
    }

    protected abstract String getTabBarBeforeSpacerStyleName();

    protected Widget createTabBarAfterSpacer() {
        final HTML widget = new HTML("&nbsp;");
        widget.addStyleName(getTabBarAfterSpacerStyleName());
        widget.setHeight("100%");
        return widget;
    }

    protected abstract String getTabBarAfterSpacerStyleName();
}
