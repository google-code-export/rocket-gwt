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
package rocket.widget.client.slider;

import rocket.dom.client.Dom;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.widget.client.Html;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple panel that wraps two elements with divs, the first being the handle,
 * the second the background widget. It is used to house the handle and
 * background widget for all Slider widgets.
 * 
 * @author Miroslav Pokorny
 */
abstract class InternalSliderPanel extends rocket.widget.client.Panel {

	InternalSliderPanel() {
		super();
	}

	protected Element createPanelElement() {
		final Element panel = DOM.createDiv();

		InlineStyle.setString(panel, StyleConstants.POSITION, "relative");
		InlineStyle.setInteger(panel, StyleConstants.LEFT, 0, CssUnit.PX);
		InlineStyle.setInteger(panel, StyleConstants.TOP, 0, CssUnit.PX);

		final Element background = DOM.createSpan();
		ObjectHelper.setString(background, "className", this
				.getBackgroundStyleName());
		DOM.appendChild(panel, background);

		final Element handle = DOM.createDiv();
		ObjectHelper.setString(handle, "className", this.getHandleStyleName());
		InlineStyle.setString(background, StyleConstants.Z_INDEX, "1");
		DOM.appendChild(panel, handle);

		return panel;
	}

	abstract protected String getBackgroundStyleName();

	abstract protected String getHandleStyleName();

	protected void checkElement(final Element element) {
		throw new UnsupportedOperationException("checkElement");
	}

	protected void afterCreatePanelElement() {
		super.afterCreatePanelElement();

		this.add(new Html());
		this.add(new Html());
	}

	protected String getInitialStyleName() {
		return "";
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	protected Element insert0(final Element element, final int index) {
		final Element childParent = DOM.getChild(this.getElement(), index);

		if (index == Constants.BACKGROUND_WIDGET_INDEX) {
			// backup width, height, overflow...
			final String width = InlineStyle.getString(element,
					StyleConstants.WIDTH);
			final String height = InlineStyle.getString(element,
					StyleConstants.HEIGHT);
			final String overflowX = InlineStyle.getString(element,
					StyleConstants.OVERFLOW_X);
			final String overflowY = ObjectHelper.getString(element,
					StyleConstants.OVERFLOW_Y);

			ObjectHelper.setString(element, "_" + StyleConstants.WIDTH, width);
			ObjectHelper
					.setString(element, "_" + StyleConstants.HEIGHT, height);
			ObjectHelper.setString(element, "_" + StyleConstants.OVERFLOW_X,
					overflowX);
			ObjectHelper.setString(element, "_" + StyleConstants.OVERFLOW_Y,
					overflowY);

			InlineStyle.setString(element, StyleConstants.WIDTH, "100%");
			InlineStyle.setString(element, StyleConstants.HEIGHT, "100%");
			InlineStyle.setString(element, StyleConstants.OVERFLOW_X, "hidden");
			InlineStyle.setString(element, StyleConstants.OVERFLOW_Y, "hidden");
		}

		DOM.appendChild(childParent, element);

		return element;
	}

	protected void remove0(final Element element, final int index) {
		Dom.removeFromParent(element);

		if (index == Constants.BACKGROUND_WIDGET_INDEX) {
			final String width = ObjectHelper.getString(element, "_"
					+ StyleConstants.WIDTH);
			final String height = ObjectHelper.getString(element, "_"
					+ StyleConstants.HEIGHT);
			final String overflowX = ObjectHelper.getString(element, "_"
					+ StyleConstants.OVERFLOW_X);
			final String overflowY = ObjectHelper.getString(element, "_"
					+ StyleConstants.OVERFLOW_Y);

			InlineStyle.setString(element, StyleConstants.WIDTH, width);
			InlineStyle.setString(element, StyleConstants.HEIGHT, height);
			InlineStyle
					.setString(element, StyleConstants.OVERFLOW_X, overflowX);
			InlineStyle
					.setString(element, StyleConstants.OVERFLOW_Y, overflowY);
		}
	}
}
