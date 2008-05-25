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
import com.google.gwt.user.client.ui.Widget;

/**
 * A RightSideAccordion compromises a menu on the right with the visible content
 * on the right.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RightSideAccordionPanel extends TwoColumnAccordionPanel {

	public RightSideAccordionPanel() {
	}

	protected HorizontalPanel createPanel() {
		final HorizontalPanel panel = new HorizontalPanel();

		final DeckPanel contentsPanel = this.createContentsPanel();
		panel.add(contentsPanel);

		final DivPanel captionsPanel = this.createCaptionsPanel();
		panel.add(captionsPanel);

		return panel;
	}

	protected String getInitialStyleName() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_STYLE;
	}

	protected DivPanel getCaptionsPanel(){
		return (DivPanel) this.getPanel().getWidget( Constants.RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_PANEL_INDEX );
	}
	
	protected String getCaptionsPanelStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE;
	}
	
	protected String getCaptionStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_CAPTION_STYLE;
	}
	protected String getCaptionSelectedStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_CAPTION_SELECTED_STYLE;
	}

	protected DeckPanel getContentsPanel(){
		return (DeckPanel) this.getPanel().getWidget( Constants.RIGHT_SIDE_ACCORDION_PANEL_CONTENTS_PANEL_INDEX );
	}

	protected String getContentsPanelStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE;
	}
	
	protected String getContentStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_CONTENT_STYLE;
	}
}
