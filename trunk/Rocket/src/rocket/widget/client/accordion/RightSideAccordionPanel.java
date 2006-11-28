/*
 * Copyright 2006 NSW Police Government Australia
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

import rocket.widget.client.HorizontalPanel;

/**
 * A RightSideAccordion compromises a menu on the right with the visible content on the right.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RightSideAccordionPanel extends TwoColumnAccordionPanel {

    public RightSideAccordionPanel() {
        this.initWidget(this.createPanel());
    }

    protected HorizontalPanel createPanel() {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.addStyleName(AccordionConstants.RIGHT_SIDE_ACCORDION_PANEL_STYLE);
        this.setPanel(panel);
        panel.add(this.createContentsPanel());
        panel.add(this.createCaptionsPanel());
        return panel;
    }

    protected String getCaptionsPanelStyle() {
        return AccordionConstants.RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE;
    }

    protected String getContentsPanelStyle() {
        return AccordionConstants.RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE;
    }

    protected String getCaptionSelectedStyle() {
        return AccordionConstants.RIGHT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_SELECTED_STYLE;
    }

    protected String getContentSelectedStyle() {
        return AccordionConstants.RIGHT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_SELECTED_STYLE;
    }

    protected String getCaptionStyle() {
        return AccordionConstants.RIGHT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_STYLE;
    }

    protected String getContentStyle() {
        return AccordionConstants.RIGHT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_STYLE;
    }
}
