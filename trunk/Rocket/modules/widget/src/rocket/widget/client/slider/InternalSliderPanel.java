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
import rocket.style.client.ComputedStyle;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.JavaScript;
import rocket.widget.client.Widgets;

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

		final InlineStyle panelInlineStyle = InlineStyle.getInlineStyle( panel );
		panelInlineStyle.setString(Css.POSITION, "relative");
		panelInlineStyle.setInteger(Css.LEFT, 0, CssUnit.PX);
		panelInlineStyle.setInteger(Css.TOP, 0, CssUnit.PX);

		final Element background = DOM.createSpan();
		background.setClassName(this.getBackgroundStyleName());
		panel.appendChild(background);

		final Element handle = DOM.createDiv();
		handle.setClassName(this.getHandleStyleName());
		
		final InlineStyle handleInlineStyle = InlineStyle.getInlineStyle( handle );
		handleInlineStyle.setString( Css.Z_INDEX, "1");
		handleInlineStyle.setString( Css.OVERFLOW_X, "hidden");
		handleInlineStyle.setString(Css.OVERFLOW_Y, "hidden");
		panel.appendChild(handle);

		return panel;
	}

	abstract protected String getBackgroundStyleName();

	abstract protected String getHandleStyleName();

	protected void checkElement(final Element element) {
		throw new UnsupportedOperationException("checkElement");
	}

	protected void afterCreatePanelElement() {
		super.afterCreatePanelElement();

		this.add(Widgets.createHtml());
		this.add(Widgets.createHtml());
	}

	protected String getInitialStyleName() {
		return "";
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	protected void insert0(final Element element, final int index) {
		final Element childParent = DOM.getChild(this.getElement(), index);

		if (index == Constants.BACKGROUND_WIDGET_INDEX) {
			if (this.isAttached()) {
				this.updateBackgroundDimensions();
			}
		}

		DOM.appendChild(childParent, element);
	}

	protected void updateBackgroundDimensions() {
		final Element element = this.getBackgroundWidgetElement();
		final InlineStyle backgroundInlineStyle = InlineStyle.getInlineStyle(element);
		final String originalWidth = backgroundInlineStyle.getString(Css.WIDTH);
		final String originalHeight = backgroundInlineStyle.getString(Css.HEIGHT);

		JavaScript.setString(element, "_" + Css.WIDTH, originalWidth);
		JavaScript.setString(element, "_" + Css.HEIGHT, originalHeight);

		final ComputedStyle elementComputedStyle = ComputedStyle.getComputedStyle( this.getElement() );
		final int widthInPixels = elementComputedStyle.getInteger(Css.WIDTH, CssUnit.PX, 0);
		backgroundInlineStyle.setInteger(Css.WIDTH, widthInPixels, CssUnit.PX);

		final int heightInPixels = elementComputedStyle.getInteger(Css.HEIGHT, CssUnit.PX, 0);
		backgroundInlineStyle.setInteger(Css.HEIGHT, heightInPixels, CssUnit.PX);
	}

	protected void remove0(final Element element, final int index) {
		if (index == Constants.BACKGROUND_WIDGET_INDEX) {
			this.restoreBackgroundWidgetDimensions();
		}
		Dom.removeFromParent(element);
	}

	protected void restoreBackgroundWidgetDimensions() {
		final Element element = this.getBackgroundWidgetElement();
		final String width = JavaScript.getString(element, "_" + Css.WIDTH);
		final String height = JavaScript.getString(element, "_" + Css.HEIGHT);

		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(element);
		if( null != width ){
			inlineStyle.setString(Css.WIDTH, width);
		}
		if( null != height ){
			inlineStyle.setString(Css.HEIGHT, height);
		}
	}

	protected Element getBackgroundWidgetElement() {
		final Element parent = DOM.getChild(this.getElement(), Constants.BACKGROUND_WIDGET_INDEX);
		return DOM.getChild(parent, 0);
	}

	@Override
	public void onAttach() {
		super.onAttach();

		if (this.getWidgetCount() > Constants.BACKGROUND_WIDGET_INDEX) {
			this.updateBackgroundDimensions();
		}
	}
}
