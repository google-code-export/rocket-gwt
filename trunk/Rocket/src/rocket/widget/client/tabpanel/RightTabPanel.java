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

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * A RightTabPanel arranges its tab titles along the Right edge with the
 * remainder allocated to the tab contents of the active tab.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RightTabPanel extends VerticalTabPanel {

	public RightTabPanel() {
		super();
	}

	DockPanel.DockLayoutConstant getTabBarDockPanelConstants(){
		return DockPanel.EAST;
	}
	
	protected String getInitialStyleName() {
		return Constants.RIGHT_TAB_PANEL_STYLE;
	}

	protected TabBarPanel createTabBarPanel() {
		final VerticalTabBarPanel panel = new VerticalTabBarPanel();
		this.setTabBarPanel(panel);

		panel.setStyleName(this.getTabBarStyleName());
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

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
		return Constants.RIGHT_TAB_BAR_STYLE;
	}

	protected String getTabBarBeforeSpacerStyleName() {
		return Constants.RIGHT_TAB_BAR_BEFORE_SPACER_STYLE;
	}

	protected String getTabBarAfterSpacerStyleName() {
		return Constants.RIGHT_TAB_BAR_AFTER_SPACER_STYLE;
	}

	protected String getTabBarItemStyleName() {
		return Constants.RIGHT_TAB_BAR_ITEM_STYLE;
	}

	protected String getTabBarItemLabelStyleName() {
		return Constants.RIGHT_TAB_BAR_ITEM_LABEL_STYLE;
	}

	protected String getTabBarItemWidgetStyleName() {
		return Constants.RIGHT_TAB_BAR_ITEM_WIDGET_STYLE;
	}

	protected String getTabBarItemSelectedStyleName() {
		return Constants.RIGHT_TAB_BAR_ITEM_SELECTED_STYLE;
	}

	protected String getContentPanelStyleName() {
		return Constants.RIGHT_TAB_CONTENT_STYLE;
	}
}
