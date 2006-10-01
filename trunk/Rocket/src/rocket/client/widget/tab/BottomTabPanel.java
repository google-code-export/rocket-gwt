package rocket.client.widget.tab;

import rocket.client.widget.tab.TabPanel.HorizontalOrVerticalPanel;
import rocket.client.widget.tab.TabPanel.VerticalPanelImpl;

import com.google.gwt.user.client.ui.Widget;

/**
 * A BottomTabPanel arranges its tab titles along the bottom edge with the remainder allocated to the tab contents of the active
 * tab.
 * @author Miroslav Pokorny (mP)
 */
public class BottomTabPanel extends HorizonalTabPanel {

	public BottomTabPanel(){
		super();
	}
	
	protected HorizontalOrVerticalPanel createPanel() {
		final VerticalPanelImpl panel = new VerticalPanelImpl();
		this.setPanel(panel);

		panel.addStyleName(this.getPanelStyleName());

		final Widget contentPanel = this.createContentPanel();
		panel.add(contentPanel);
		panel.setCellHeight(contentPanel, "100%");

		panel.add((Widget) this.createTabBarPanel());
		return panel;
	}
	
	protected String getPanelStyleName() {
		return TabConstants.BOTTOM_TAB_PANEL_STYLE;
	}

	protected String getTabBarStyleName() {
		return TabConstants.BOTTOM_TAB_BAR_STYLE;
	}

	protected String getTabBarBeforeSpacerStyleName() {
		return TabConstants.BOTTOM_TAB_BAR_BEFORE_SPACER_STYLE;
	}

	protected String getTabBarAfterSpacerStyleName() {
		return TabConstants.BOTTOM_TAB_BAR_AFTER_SPACER_STYLE;
	}

	protected String getTabBarItemStyleName() {
		return TabConstants.BOTTOM_TAB_BAR_ITEM_STYLE;
	}

	protected String getTabBarItemLabelStyleName() {
		return TabConstants.BOTTOM_TAB_BAR_ITEM_LABEL_STYLE;
	}

	protected String getTabBarItemWidgetStyleName() {
		return TabConstants.BOTTOM_TAB_BAR_ITEM_WIDGET_STYLE;
	}

	protected String getTabBarItemSelectedStyleName() {
		return TabConstants.BOTTOM_TAB_BAR_ITEM_SELECTED_STYLE;
	}

	protected String getContentPanelStyleName() {
		return TabConstants.BOTTOM_TAB_CONTENT_STYLE;
	}
}
