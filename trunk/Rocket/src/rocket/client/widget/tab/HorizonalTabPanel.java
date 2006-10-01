package rocket.client.widget.tab;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

import rocket.client.widget.HorizontalPanel;
import rocket.client.widget.tab.TabPanel.HorizontalOrVerticalPanel;

/**
 * The HorizontalTabPanel class is the base class for both the TopTabPanel and BottomTabPanel classes.
 * @author Miroslav Pokorny (mP)
 */
public abstract class HorizonalTabPanel extends TabPanel {

	protected HorizonalTabPanel() {
		super();
	}

	protected HorizontalOrVerticalPanel createTabBarPanel() {
		final HorizontalPanelImpl panel = new HorizontalPanelImpl();
		this.setTabBarPanel(panel);
		
		panel.addStyleName( this.getTabBarStyleName() );
		panel.sinkEvents(Event.ONCLICK);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

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
