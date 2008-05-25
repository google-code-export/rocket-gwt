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

import rocket.style.client.Css;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;
import rocket.widget.client.DivPanel;

import com.google.gwt.user.client.ui.Widget;

/**
 * A VerticalAccordion is a simple vertical list made up of captions and
 * contents. Only one content is ever visible.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class VerticalAccordionPanel extends AccordionPanel {

	public VerticalAccordionPanel() {
		super();
	}

	protected Widget createWidget() {
		return this.createPanel();
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	protected void replaceContentWidget(final AccordionItem item) {
		Checker.notNull("parameter:item", item);

		final DivPanel panel = this.getPanel();
		final int index = this.getIndex(item);
		final DivPanel itemPanel = (DivPanel) panel.get(index);
		itemPanel.remove(1);
		final Widget content = item.getContent();
		itemPanel.add(content);
	}

	/**
	 * A DivPanel is used to house the entire Accordion.
	 */
	protected DivPanel getPanel() {
		return (DivPanel) this.getWidget();
	}

	protected DivPanel createPanel() {
		return new DivPanel();
	}

	protected String getInitialStyleName() {
		return Constants.VERTICAL_ACCORDION_PANEL_STYLE;
	}

	// ACCORDION PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	protected void removeSelectedStyle(final AccordionItem item) {
		Checker.notNull("parameter:item", item);

		final Widget caption = item.getCaptionWidget();
		caption.removeStyleName(Constants.VERTICAL_ACCORDION_PANEL_CAPTION_SELECTED_STYLE);

		InlineStyle.setString( item.getContent().getElement(), Css.DISPLAY, "none" );
	}

	protected void addSelectedStyle(final AccordionItem item) {
		Checker.notNull("parameter:item", item);

		final Widget caption = item.getCaptionWidget();
		caption.addStyleName(Constants.VERTICAL_ACCORDION_PANEL_CAPTION_SELECTED_STYLE);

		InlineStyle.setString( item.getContent().getElement(), Css.DISPLAY, "block" );
	}

	protected void insert0(final int insertBefore, final AccordionItem item) {
		Checker.notNull("parameter:item", item);

		final DivPanel panel = this.getPanel(); 
		final int index = insertBefore * 2;
		
		final Widget caption = item.getCaptionWidget();
		caption.addStyleName(Constants.VERTICAL_ACCORDION_PANEL_CAPTION_STYLE);
		panel.insert( caption, index + 0 );
		
		final Widget content = item.getContent();
		InlineStyle.setString( content.getElement(), Css.DISPLAY, "none" );
		content.addStyleName(Constants.VERTICAL_ACCORDION_PANEL_CONTENT_STYLE);
		panel.insert( content, index + 1 );		
	}

	protected void remove0(final int index) {
		final AccordionItem item = this.get(index);

		final DivPanel panel = this.getPanel();
		final int panelIndex = index * 2;
		panel.remove( panelIndex );
		panel.remove( panelIndex );
		
		item.clearAccordionPanel();
	}
}
