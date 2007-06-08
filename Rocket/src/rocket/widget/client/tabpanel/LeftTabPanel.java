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

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * A LeftTopPanel arranges its tab titles along the left edge with the remainder allocated to the tab contents of the active tab.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class LeftTabPanel extends VerticalTabPanel {

    public LeftTabPanel() {
        super();
    }

    protected HorizontalOrVerticalPanel createPanel() {
        final HorizontalPanelImpl panel = new HorizontalPanelImpl();
        this.setPanel(panel);

        panel.addStyleName(this.getPanelStyleName());
        panel.add((Widget) this.createTabBarPanel());

        final Widget contentPanel = this.createContentPanel();
        panel.add(contentPanel);
        panel.setCellHeight(contentPanel, "100%");
        return panel;
    }

    protected String getPanelStyleName() {
        return TabConstants.LEFT_TAB_PANEL_STYLE;
    }

    protected HorizontalOrVerticalPanel createTabBarPanel() {
        final VerticalPanelImpl panel = new VerticalPanelImpl();
        this.setTabBarPanel(panel);

        panel.addStyleName(this.getTabBarStyleName());
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        final Widget first = this.createTabBarBeforeSpacer();
        final Widget rest = this.createTabBarAfterSpacer();
        panel.add(first);
        panel.add(rest);
        panel.setCellHeight(first, "100%");
        panel.setCellWidth(rest, "100%");
        panel.setCellHorizontalAlignment(rest, HasHorizontalAlignment.ALIGN_RIGHT);
        return panel;
    }

    protected String getTabBarStyleName() {
        return TabConstants.LEFT_TAB_BAR_STYLE;
    }

    protected String getTabBarBeforeSpacerStyleName() {
        return TabConstants.LEFT_TAB_BAR_BEFORE_SPACER_STYLE;
    }

    protected String getTabBarAfterSpacerStyleName() {
        return TabConstants.LEFT_TAB_BAR_AFTER_SPACER_STYLE;
    }

    protected String getTabBarItemStyleName() {
        return TabConstants.LEFT_TAB_BAR_ITEM_STYLE;
    }

    protected String getTabBarItemLabelStyleName() {
        return TabConstants.LEFT_TAB_BAR_ITEM_LABEL_STYLE;
    }

    protected String getTabBarItemWidgetStyleName() {
        return TabConstants.LEFT_TAB_BAR_ITEM_WIDGET_STYLE;
    }

    protected String getTabBarItemSelectedStyleName() {
        return TabConstants.LEFT_TAB_BAR_ITEM_SELECTED_STYLE;
    }

    protected String getContentPanelStyleName() {
        return TabConstants.LEFT_TAB_CONTENT_STYLE;
    }
}
