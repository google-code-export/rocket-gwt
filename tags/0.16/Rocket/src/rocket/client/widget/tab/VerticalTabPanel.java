package rocket.client.widget.tab;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

import rocket.client.widget.HorizontalPanel;
import rocket.client.widget.tab.TabPanel.HorizontalOrVerticalPanel;

/**
 * The VerticalTabPanel class is the base class for both the LeftTabPanel and RightTabPanel classes.
 * @author Miroslav Pokorny (mP)
 */
public abstract class VerticalTabPanel extends TabPanel {

	protected VerticalTabPanel() {
		super();
	}

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
