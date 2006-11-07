package rocket.client.widget.tab;

import rocket.client.util.ObjectHelper;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A tab item contains all aspects related to a tab item including its caption and content.
 * 
 * The content widget must be set before adding a TabItem to a TabPanel.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class TabItem {

    public TabItem() {
        super();

        this.createCaptionWidget();
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
            WidgetHelper.handleAssertFailure("This TabItem already belongs to a TabPanel, tabPanel: " + tabPanel);
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

    protected void createCaptionWidget() {
        final HTML html = new HTML();
        html.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                TabItem.this.select();
            }
        });
        this.setCaptionWidget(html);
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
}
