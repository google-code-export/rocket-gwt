package rocket.client.widget.tab;

import rocket.client.widget.tab.TabPanel.HorizontalOrVerticalPanel;
import rocket.client.widget.tab.TabPanel.VerticalPanelImpl;

import com.google.gwt.user.client.ui.Widget;

/**
 * A TopTabPanel arranges its tab titles along the top edge with the remainder allocated to the tab contents of the active
 * tab.
 * @author Miroslav Pokorny (mP)
 */
public class TopTabPanel extends HorizonalTabPanel {

	public TopTabPanel(){
		super();
	}
	
	protected HorizontalOrVerticalPanel createPanel() {
		final VerticalPanelImpl panel = new VerticalPanelImpl();
		this.setPanel(panel);

		panel.addStyleName(this.getPanelStyleName());
		panel.add((Widget) this.createTabBarPanel());

		final Widget contentPanel = this.createContentPanel();
		panel.add(contentPanel);
		panel.setCellHeight(contentPanel, "100%");
		return panel;
	}
	
	protected String getPanelStyleName() {
		return TabConstants.TOP_TAB_PANEL_STYLE;
	}

	protected String getTabBarStyleName() {
		return TabConstants.TOP_TAB_BAR_STYLE;
	}

	protected String getTabBarBeforeSpacerStyleName() {
		return TabConstants.TOP_TAB_BAR_BEFORE_SPACER_STYLE;
	}

	protected String getTabBarAfterSpacerStyleName() {
		return TabConstants.TOP_TAB_BAR_AFTER_SPACER_STYLE;
	}

	protected String getTabBarItemStyleName() {
		return TabConstants.TOP_TAB_BAR_ITEM_STYLE;
	}

	protected String getTabBarItemLabelStyleName() {
		return TabConstants.TOP_TAB_BAR_ITEM_LABEL_STYLE;
	}

	protected String getTabBarItemWidgetStyleName() {
		return TabConstants.TOP_TAB_BAR_ITEM_WIDGET_STYLE;
	}

	protected String getTabBarItemSelectedStyleName() {
		return TabConstants.TOP_TAB_BAR_ITEM_SELECTED_STYLE;
	}

	protected String getContentPanelStyleName() {
		return TabConstants.TOP_TAB_CONTENT_STYLE;
	}

}
