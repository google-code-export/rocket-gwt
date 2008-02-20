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
import rocket.widget.client.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * An accordion item is added to an accordion.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class AccordionItem {

	public AccordionItem() {
		super();

		this.setCaptionWidget(this.createCaptionWidget());
	}

	/**
	 * Selects or makes this AccordionItem the current or active one.
	 * 
	 */
	public void select() {
		if (false == this.hasAccordionPanel()) {
			throw new UnsupportedOperationException(
					"This accordionItem cannot be selected because it has not yet been added to a AccordionPanel");
		}
		this.getAccordionPanel().select(this);
	}

	/**
	 * Removes this accordionItem from its parent AccordionPanel provided it has
	 * already been added.
	 */
	public void remove() {
		if (false == this.hasAccordionPanel()) {
			throw new UnsupportedOperationException(
					"This accordionItem cannot be removed because it has not yet been added to a AccordionPanel");
		}
		this.getAccordionPanel().remove(this);
	}

	/**
	 * The accordionPanel that this item belongs too.
	 */
	private AccordionPanel accordionPanel;

	protected AccordionPanel getAccordionPanel() {
		ObjectHelper.checkNotNull("field:accordionPanel", accordionPanel);
		return this.accordionPanel;
	}

	protected boolean hasAccordionPanel() {
		return null != this.accordionPanel;
	}

	protected void setAccordionPanel(final AccordionPanel accordionPanel) {
		ObjectHelper.checkNotNull("parameter:accordionPanel", accordionPanel);

		// if it was already attached remove it first...
		if (this.hasAccordionPanel()) {
			WidgetHelper.fail("This AccordionItem already belongs to a AccordionPanel, accordionPanel: " + accordionPanel);
		}

		this.accordionPanel = accordionPanel;
	}

	protected void clearAccordionPanel() {
		this.accordionPanel = null;
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

	protected HTML createCaptionWidget() {
		final HTML html = new HTML();
		DOM.setElementProperty(html.getElement(), "className", "");
		html.setWidth("100%");
		html.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				AccordionItem.this.select();
			}
		});
		return html;
	}

	public String getCaption() {
		return this.getCaptionWidget().getText();
	}

	public void setCaption(final String text) {
		this.getCaptionWidget().setText(text);
	}

	/**
	 * THe content portion which is only visible when this widget is the active
	 * one within the parent Accordion.
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
		if (this.hasAccordionPanel()) {
			final AccordionPanel accordion = this.getAccordionPanel();
			accordion.replaceContentWidget(this);
		}

		this.content = content;
	}
}