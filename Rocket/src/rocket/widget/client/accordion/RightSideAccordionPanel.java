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

import rocket.widget.client.DivPanel;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * A RightSideAccordion compromises a menu on the right with the visible content on the right.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RightSideAccordionPanel extends TwoColumnAccordionPanel {

    public RightSideAccordionPanel() {
    	final HorizontalPanel panel = this.createPanel();
    	this.setPanel(panel);
        this.initWidget( panel);
    }

    protected HorizontalPanel createPanel() {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName(AccordionConstants.RIGHT_SIDE_ACCORDION_PANEL_STYLE);
        
        final DivPanel captionsPanel = this.createCaptionsPanel();
        this.setCaptionsPanel(captionsPanel);
        panel.add(captionsPanel);
        
        final DeckPanel contentsPanel = this.createContentsPanel();
        this.setContentsPanel(contentsPanel);
        panel.add(contentsPanel);
        
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
