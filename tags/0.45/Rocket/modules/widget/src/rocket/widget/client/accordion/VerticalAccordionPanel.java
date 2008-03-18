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
		final DivPanel panel = this.createPanel();
		this.setPanel(panel);
		return panel;
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
	private DivPanel panel;

	protected DivPanel getPanel() {
		Checker.notNull("field:panel", panel);
		return panel;
	}

	protected void setPanel(final DivPanel panel) {
		Checker.notNull("parameter:panel", panel);
		this.panel = panel;
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

		final int index = this.getIndex(item);
		final Widget widget = this.getPanel().get(index);
		widget.removeStyleName(Constants.VERTICAL_ACCORDION_PANEL_ITEM_SELECTED_STYLE);

		item.getContent().setVisible(false);
	}

	protected void addSelectedStyle(final AccordionItem item) {
		Checker.notNull("parameter:item", item);

		final int index = this.getIndex(item);
		final Widget widget = this.getPanel().get(index);
		widget.addStyleName(Constants.VERTICAL_ACCORDION_PANEL_ITEM_SELECTED_STYLE);

		item.getContent().setVisible(true);
	}

	protected void insert0(final int insertBefore, final AccordionItem item) {
		Checker.notNull("parameter:item", item);

		final Widget caption = item.getCaptionWidget();
		caption.addStyleName(Constants.VERTICAL_ACCORDION_PANEL_ITEM_CAPTION_STYLE);

		final Widget content = item.getContent();
		content.addStyleName(Constants.VERTICAL_ACCORDION_PANEL_ITEM_CONTENT_STYLE);

		final DivPanel panel = new DivPanel();
		panel.setStyleName(Constants.VERTICAL_ACCORDION_PANEL_ITEM_STYLE);
		panel.add(caption);
		panel.add(content);
		content.setVisible(false); // content is invisible when initially
		// added.

		this.getPanel().insert(panel, insertBefore);
	}

	protected void remove0(final int index) {
		final AccordionItem item = this.get(index);

		this.getPanel().remove(index);
		item.clearAccordionPanel();
	}

}