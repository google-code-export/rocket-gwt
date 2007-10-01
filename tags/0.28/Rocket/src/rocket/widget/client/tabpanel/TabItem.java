package rocket.widget.client.tabpanel;

import rocket.util.client.ObjectHelper;
import rocket.widget.client.WidgetHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A tab item contains all aspects related to a tab item including its caption and content.
 * 
 * The content widget must be set before adding a TabItem to a TabPanel.
 * 
 * The {@link #addTabWidgetBeforeCaption(Widget)} and {@link #addTabWidgetAfterCaption(Widget)} methods may be used to add additional
 * widgets such as Spinners to a tab item.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class TabItem {

    public TabItem() {
        super();

        this.createTabWidgetPanel();
    }

    /**
     * Selects or makes this TabItem the current or active one.
     * 
     */
    public void select() {
        if (false == this.hasTabPanel()) {
            throw new UnsupportedOperationException(
                    "This tabItem cannot be selected because it has not yet been added to a TabPanel");
        }
        this.getTabPanel().select(this);
    }

    /**
     * Removes this tabItem from its parent TabPanel provided it has already been added.
     */
    public void remove() {
        if (false == this.hasTabPanel()) {
            throw new UnsupportedOperationException(
                    "This tabItem cannot be removed because it has not yet been added to a TabPanel");
        }
        this.getTabPanel().remove(this);
        this.clearTabPanel();
    }

    /**
     * The tabPanel that this item belongs too.
     */
    private TabPanel tabPanel;

    protected TabPanel getTabPanel() {
        ObjectHelper.checkNotNull("field:tabPanel", tabPanel);
        return this.tabPanel;
    }

    protected boolean hasTabPanel() {
        return null != this.tabPanel;
    }

    protected void setTabPanel(final TabPanel tabPanel) {
        ObjectHelper.checkNotNull("parameter:tabPanel", tabPanel);

        // if it was already attached remove it first...
        if (this.hasTabPanel()) {
            WidgetHelper.fail("This TabItem already belongs to a TabPanel, tabPanel: " + tabPanel);
        }

        this.tabPanel = tabPanel;
    }

    protected void clearTabPanel() {
        this.tabPanel = null;
    }

    /**
     * The caption or title that appears above the content.
     */
    private HTML captionWidget;

    protected HTML getCaptionWidget() {
        ObjectHelper.checkNotNull("field:captionWidget", captionWidget);
        return this.captionWidget;
    }

    protected void setCaptionWidget(final HTML captionWidget) {
        ObjectHelper.checkNotNull("field:captionWidget", captionWidget);
        this.captionWidget = captionWidget;
    }

    protected Widget createCaptionWidget() {
        final HTML html = new HTML();
        html.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                TabItem.this.select();
            }
        });
        this.setCaptionWidget(html);
        return html;
    }

    public String getCaption() {
        return this.getCaptionWidget().getText();
    }

    public void setCaption(final String text) {
        this.getCaptionWidget().setText(text);
    }

    /**
     * THe content portion which is only visible when this widget is the active one within the parent Accordion.
     */
    private Widget content;

    public Widget getContent() {
        ObjectHelper.checkNotNull("field:content", content);
        return this.content;
    }

    public boolean hasContent() {
        return null != content;
    }

    public void setContent(final Widget content) {
        ObjectHelper.checkNotNull("parameter:content", content);

        // replace the previous content widget with the new one...
        if (this.hasTabPanel()) {
            final DeckPanel panel = this.getTabPanel().getContentPanel();
            final int index = panel.getWidgetIndex(this.content);
            panel.remove(index);
            panel.insert(content, index);
        }

        this.content = content;
    }

    /**
     * This panel holds all the widgets that have been added to the tab including the label holding the tab text.
     */
    private HorizontalPanel tabWidgetPanel;

    protected HorizontalPanel getTabWidgetPanel() {
        ObjectHelper.checkNotNull("field:tabWidgetPanel", tabWidgetPanel);
        return this.tabWidgetPanel;
    }

    protected void setTabWidgetPanel(final HorizontalPanel tabWidgetPanel) {
        ObjectHelper.checkNotNull("parameter:tabWidgetPanel", tabWidgetPanel);
        this.tabWidgetPanel = tabWidgetPanel;
    }

    protected HorizontalPanel createTabWidgetPanel() {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        this.setTabWidgetPanel(panel);

        panel.add(this.createCaptionWidget());

        return panel;
    }

    /**
     * Adds a widget so that it appears to the left of the caption widget
     * 
     * @param widget
     * @param before
     *            When true adds the widget just before the caption otherwise the widget is added just after.
     */
    public void addTabWidget(final Widget widget, final boolean before) {
        this.insertTabWidget(widget, before ? 0 : +1);
    }

    /**
     * Inserts a widget before the widget
     * 
     * @param widget
     * @param index
     *            A positive value inserts the widget after the caption whilst a negative value positions the new widget before the widget.
     *            A value of 0 is illegal.
     */
    public void insertTabWidget(final Widget widget, final int index) {
        final HorizontalPanel tabPanel = this.getTabWidgetPanel();
        final int captionIndex = tabPanel.getWidgetIndex(this.getCaptionWidget());
        tabPanel.insert(widget, captionIndex + index);
    }

    /**
     * Removes a previously added tab widget
     * 
     * @param widget
     */
    public boolean removeTabWidget(final Widget widget) {
        return this.getTabWidgetPanel().remove(widget);
    }
}