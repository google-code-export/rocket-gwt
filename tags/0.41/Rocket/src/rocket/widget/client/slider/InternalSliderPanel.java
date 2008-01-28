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

		InlineStyle.setString(panel, Css.POSITION, "relative");
		InlineStyle.setInteger(panel, Css.LEFT, 0, CssUnit.PX);
		InlineStyle.setInteger(panel, Css.TOP, 0, CssUnit.PX);

		final Element background = DOM.createSpan();
		JavaScript.setString(background, "className", this.getBackgroundStyleName());
		DOM.appendChild(panel, background);

		final Element handle = DOM.createDiv();
		JavaScript.setString(handle, "className", this.getHandleStyleName());
		InlineStyle.setString(background, Css.Z_INDEX, "1");
		DOM.appendChild(panel, handle);

		InlineStyle.setString(panel, Css.OVERFLOW_X, "hidden");
		InlineStyle.setString(panel, Css.OVERFLOW_Y, "hidden");

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

	protected void insert0(final Element element, final int index) {
		final Element childParent = DOM.getChild(this.getElement(), index);

		if (index == Constants.BACKGROUND_WIDGET_INDEX) {
			if( this.isAttached() ){
				this.updateBackgroundDimensions();
			}
		}

		DOM.appendChild(childParent, element);
	}

	protected void updateBackgroundDimensions(){
		final Element element = this.getBackgroundWidgetElement();
		
		final String originalWidth = InlineStyle.getString(element, Css.WIDTH);
		final String originalHeight = InlineStyle.getString(element, Css.HEIGHT);

		JavaScript.setString(element, "_" + Css.WIDTH, originalWidth);
		JavaScript.setString(element, "_" + Css.HEIGHT, originalHeight);

		final int widthInPixels = ComputedStyle.getInteger( this.getElement(), Css.WIDTH, CssUnit.PX, 0 );
		InlineStyle.setInteger(element, Css.WIDTH, widthInPixels, CssUnit.PX );
		
		final int heightInPixels = ComputedStyle.getInteger( this.getElement(), Css.HEIGHT, CssUnit.PX, 0 );
		InlineStyle.setInteger(element, Css.HEIGHT, heightInPixels, CssUnit.PX );
	}
	
	protected void remove0(final Element element, final int index) {
		if (index == Constants.BACKGROUND_WIDGET_INDEX) {
			this.restoreBackgroundWidgetDimensions();
		}
		Dom.removeFromParent(element);
	}
	
	protected void restoreBackgroundWidgetDimensions(){
		final Element element = this.getBackgroundWidgetElement();
		final String width = JavaScript.getString(element, "_" + Css.WIDTH);
		final String height = JavaScript.getString(element, "_" + Css.HEIGHT);

		InlineStyle.setString(element, Css.WIDTH, width);
		InlineStyle.setString(element, Css.HEIGHT, height);
	}

	protected Element getBackgroundWidgetElement(){
		final Element parent = DOM.getChild(this.getElement(), Constants.BACKGROUND_WIDGET_INDEX);
		return DOM.getChild(parent, 0 );
	}
	
	public void onAttach(){
		super.onAttach();
		
		if( this.getWidgetCount() > Constants.BACKGROUND_WIDGET_INDEX ){
			this.updateBackgroundDimensions();
		}
	}
}
