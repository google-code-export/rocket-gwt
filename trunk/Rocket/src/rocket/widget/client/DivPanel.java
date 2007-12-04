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
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * A DivPanel is a panel that uses a div as the primary container widget. Each
 * widget that is added is again wrapped in their own div.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DivPanel extends Panel implements HasWidgets {

	public DivPanel() {
		super();
	}

	public DivPanel(final Element div) {
		super(div);
	}

	protected void checkElement(final Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.DIV_TAG);
	}

	/**
	 * Factory method which creates the parent DIV element for this entire panel
	 * 
	 * @return A new DIV element
	 */
	protected Element createPanelElement() {
		return DOM.createDiv();
	}

	protected String getInitialStyleName() {
		return WidgetConstants.DIV_PANEL_STYLE;
	}

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

	protected void insert0(final Element element, final int indexBefore) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final Element child = this.createElement();
		DOM.insertChild(this.getParentElement(), child, indexBefore);
		DOM.appendChild(child, element);
	}

	protected Element createElement() {
		return DOM.createDiv();
	}

	protected void remove0(final Element element, final int index) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final Element parent = this.getParentElement();
		final Element child = DOM.getChild(parent, index);
		Dom.removeFromParent(child);
	}
}
