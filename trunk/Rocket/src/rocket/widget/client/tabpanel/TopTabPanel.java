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
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * A TopTabPanel arranges its tab titles along the top edge with the remainder
 * allocated to the tab contents of the active tab.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class TopTabPanel extends HorizonalTabPanel {

	public TopTabPanel() {
		super();
	}

	DockPanel.DockLayoutConstant getTabBarDockPanelConstants(){
		return DockPanel.NORTH;
	}
	
	protected TabPanel.TabBarPanel createTabBarPanel() {
		return this.createTabBarPanel(HasVerticalAlignment.ALIGN_BOTTOM);
	}

	protected String getInitialStyleName() {
		return Constants.TOP_TAB_PANEL_STYLE;
	}

	protected String getTabBarStyleName() {
		return Constants.TOP_TAB_BAR_STYLE;
	}

	protected String getTabBarBeforeSpacerStyleName() {
		return Constants.TOP_TAB_BAR_BEFORE_SPACER_STYLE;
	}

	protected String getTabBarAfterSpacerStyleName() {
		return Constants.TOP_TAB_BAR_AFTER_SPACER_STYLE;
	}

	protected String getTabBarItemStyleName() {
		return Constants.TOP_TAB_BAR_ITEM_STYLE;
	}

	protected String getTabBarItemLabelStyleName() {
		return Constants.TOP_TAB_BAR_ITEM_LABEL_STYLE;
	}

	protected String getTabBarItemWidgetStyleName() {
		return Constants.TOP_TAB_BAR_ITEM_WIDGET_STYLE;
	}

	protected String getTabBarItemSelectedStyleName() {
		return Constants.TOP_TAB_BAR_ITEM_SELECTED_STYLE;
	}

	protected String getContentPanelStyleName() {
		return Constants.TOP_TAB_CONTENT_STYLE;
	}

}
