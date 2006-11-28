package rocket.client.widget.tab;

import rocket.client.widget.tab.TabPanel.HorizontalOrVerticalPanel;
import rocket.client.widget.tab.TabPanel.VerticalPanelImpl;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * A RightTabPanel arranges its tab titles along the Right edge with the remainder allocated to the tab contents of the active
 * tab.
 * @author Miroslav Pokorny (mP)
 */
public class RightTabPanel extends VerticalTabPanel {

	public RightTabPanel(){
		super();
	}
	
	protected HorizontalOrVerticalPanel createPanel() {
		final HorizontalPanelImpl panel = new HorizontalPanelImpl();
		this.setPanel(panel);

		panel.addStyleName(this.getPanelStyleName());

		final Widget contentPanel = this.createContentPanel();
		panel.add(contentPanel);
		panel.setCellHeight(contentPanel, "100%");

		panel.add((Widget) this.createTabBarPanel());
		return panel;
	}
	
	protected String getPanelStyleName() {
		return TabConstants.RIGHT_TAB_PANEL_STYLE;
	}
	protected HorizontalOrVerticalPanel createTabBarPanel() {
		final VerticalPanelImpl panel = new VerticalPanelImpl();
		this.setTabBarPanel(panel);
		
		panel.addStyleName( this.getTabBarStyleName() );
		panel.sinkEvents(Event.ONCLICK);
		panel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_LEFT);

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
		return TabConstants.RIGHT_TAB_BAR_STYLE;
	}

	protected String getTabBarBeforeSpacerStyleName() {
		return TabConstants.RIGHT_TAB_BAR_BEFORE_SPACER_STYLE;
	}

	protected String getTabBarAfterSpacerStyleName() {
		return TabConstants.RIGHT_TAB_BAR_AFTER_SPACER_STYLE;
	}

	protected String getTabBarItemStyleName() {
		return TabConstants.RIGHT_TAB_BAR_ITEM_STYLE;
	}

	protected String getTabBarItemLabelStyleName() {
		return TabConstants.RIGHT_TAB_BAR_ITEM_LABEL_STYLE;
	}

	protected String getTabBarItemWidgetStyleName() {
		return TabConstants.RIGHT_TAB_BAR_ITEM_WIDGET_STYLE;
	}

	protected String getTabBarItemSelectedStyleName() {
		return TabConstants.RIGHT_TAB_BAR_ITEM_SELECTED_STYLE;
	}

	protected String getContentPanelStyleName() {
		return TabConstants.RIGHT_TAB_CONTENT_STYLE;
	}
}