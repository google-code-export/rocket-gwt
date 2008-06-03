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
package rocket.widget.client;

import rocket.dom.client.Dom;
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A SpanPanel is a panel that uses a span as the primary container widget.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SpanPanel extends Panel {

	public SpanPanel() {
		super();
	}

	public SpanPanel(final Element element) {
		super(element);
	}

	@Override
	protected void checkElement(final Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.SPAN_TAG);
	}

	/**
	 * Factory method which creates the parent SPAN element for this entire
	 * panel
	 * 
	 * @return A new span element
	 */
	@Override
	protected Element createPanelElement() {
		return DOM.createSpan();
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.SPAN_PANEL_STYLE;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return 0;
	}

	/**
	 * Returns the element which will house each of the new widget's elements.
	 * 
	 * @return The parent element
	 */
	public Element getParentElement() {
		return this.getElement();
	}

	@Override
	protected void insert0(final Element element, final int indexBefore) {
		Checker.notNull("parameter:element", element);

		DOM.insertChild(this.getParentElement(), element, indexBefore);
	}

	@Override
	protected void remove0(final Element element, final int index) {
		Checker.notNull("parameter:element", element);

		Dom.removeFromParent(element);
	}
}
