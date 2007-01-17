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
package rocket.widget.client.accordion;

import rocket.util.client.ObjectHelper;
import rocket.widget.client.DivPanel;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A common class for both the LeftSideAccordionPanel and RightSideAccordionPanel classes.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public abstract class TwoColumnAccordionPanel extends AccordionPanel {
    protected TwoColumnAccordionPanel() {
        super();
    }

    protected void replaceContentWidget(final AccordionItem item) {
        ObjectHelper.checkNotNull("parameter:item", item);

        final DeckPanel contents = this.getContentsPanel();
        final int index = this.getIndex(item);
        contents.remove(index);
        final Widget content = item.getContent();
        contents.insert(content, index);
    }

    /**
     * A HorizontalPanel is used to house the entire Accordion.
     */
    private HorizontalPanel panel;

    protected HorizontalPanel getPanel() {
        ObjectHelper.checkNotNull("field:panel", panel);
        return panel;
    }

    protected void setPanel(final HorizontalPanel panel) {
        ObjectHelper.checkNotNull("parameter:panel", panel);
        this.panel = panel;
    }

    /**
     * A DivPanel is used to house the list of all captions.
     */
    private DivPanel captionsPanel;

    protected DivPanel getCaptionsPanel() {
        ObjectHelper.checkNotNull("field:captionsPanel", captionsPanel);
        return captionsPanel;
    }

    protected void setCaptionsPanel(final DivPanel captionsPanel) {
        ObjectHelper.checkNotNull("parameter:captionsPanel", captionsPanel);
        this.captionsPanel = captionsPanel;
    }

    protected DivPanel createCaptionsPanel() {
        final DivPanel captionsPanel = new DivPanel();
        captionsPanel.addStyleName(getCaptionsPanelStyle());
        this.setCaptionsPanel(captionsPanel);
        return captionsPanel;
    }

    protected abstract String getCaptionsPanelStyle();

    /**
     * A DeckPanel is used to house the content widgets.
     */
    private DeckPanel contentsPanel;

    protected DeckPanel getContentsPanel() {
        ObjectHelper.checkNotNull("field:contentsPanel", contentsPanel);
        return contentsPanel;
    }

    protected void setContentsPanel(final DeckPanel contentsPanel) {
        ObjectHelper.checkNotNull("parameter:contentsPanel", contentsPanel);
        this.contentsPanel = contentsPanel;
    }

    protected DeckPanel createContentsPanel() {
        final DeckPanel contentsPanel = new DeckPanel();
        contentsPanel.addStyleName(this.getContentsPanelStyle());
        this.setContentsPanel(contentsPanel);
        return contentsPanel;
    }

    protected abstract String getContentsPanelStyle();

    protected void removeSelectedStyle(final AccordionItem item) {
        ObjectHelper.checkNotNull("parameter:item", item);

        final Widget caption = item.getCaptionWidget();
        caption.removeStyleName(this.getCaptionSelectedStyle());
        final Widget content = item.getContent();
        content.removeStyleName(this.getContentSelectedStyle());
    }

    protected abstract String getCaptionSelectedStyle();

    protected abstract String getContentSelectedStyle();

    protected void addSelectedStyle(AccordionItem item) {
        ObjectHelper.checkNotNull("parameter:item", item);

        final Widget caption = item.getCaptionWidget();
        caption.addStyleName(this.getCaptionSelectedStyle());
        final Widget content = item.getContent();
        content.addStyleName(this.getContentSelectedStyle());

        final int index = this.getIndex(item);
        this.getContentsPanel().showWidget(index);

    }

    protected void insert0(int insertBefore, AccordionItem item) {
        ObjectHelper.checkNotNull("parameter:item", item);

        final Widget caption = item.getCaptionWidget();
        caption.addStyleName(this.getCaptionStyle());
        this.getCaptionsPanel().insert(caption, insertBefore);

        final Widget content = item.getContent();
        content.addStyleName(this.getContentStyle());
        this.getContentsPanel().insert(content, insertBefore);
    }

    protected abstract String getCaptionStyle();

    protected abstract String getContentStyle();

    protected void remove0(final int index) {
        final AccordionItem item = this.get(index);
        item.getCaptionWidget().removeStyleName(this.getCaptionStyle());
        this.getCaptionsPanel().remove(index);
        item.getContent().removeStyleName(this.getContentStyle());
        this.getContentsPanel().remove(index);
    }

}
