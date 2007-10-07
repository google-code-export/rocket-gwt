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

	protected Widget createWidget() {
		final HorizontalPanel panel = this.createPanel();
		this.setPanel(panel);
		return panel;
	}

	protected HorizontalPanel createPanel() {
		final HorizontalPanel panel = new HorizontalPanel();

		final DeckPanel contentsPanel = this.createContentsPanel();
		this.setContentsPanel(contentsPanel);
		panel.add(contentsPanel);

		final DivPanel captionsPanel = this.createCaptionsPanel();
		this.setCaptionsPanel(captionsPanel);
		panel.add(captionsPanel);

		return panel;
	}

	protected String getInitialStyleName() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_STYLE;
	}

	protected String getCaptionsPanelStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE;
	}

	protected String getContentsPanelStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE;
	}

	protected String getCaptionSelectedStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_SELECTED_STYLE;
	}

	protected String getContentSelectedStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_SELECTED_STYLE;
	}

	protected String getCaptionStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_STYLE;
	}

	protected String getContentStyle() {
		return Constants.RIGHT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_STYLE;
	}
}
