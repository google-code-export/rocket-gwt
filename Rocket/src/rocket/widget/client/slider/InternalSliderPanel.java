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
		ObjectHelper.setString(background, "className", this.getBackgroundStyleName());
		DOM.appendChild(panel, background);

		final Element handle = DOM.createDiv();
		ObjectHelper.setString(handle, "className", this.getHandleStyleName());
		InlineStyle.setString(background, StyleConstants.Z_INDEX, "1");
		DOM.appendChild(panel, handle);

		InlineStyle.setString(panel, StyleConstants.OVERFLOW_X, "hidden");
		InlineStyle.setString(panel, StyleConstants.OVERFLOW_Y, "hidden");

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
		
		final String originalWidth = InlineStyle.getString(element, StyleConstants.WIDTH);
		final String originalHeight = InlineStyle.getString(element, StyleConstants.HEIGHT);

		ObjectHelper.setString(element, "_" + StyleConstants.WIDTH, originalWidth);
		ObjectHelper.setString(element, "_" + StyleConstants.HEIGHT, originalHeight);

		final int widthInPixels = ComputedStyle.getInteger( this.getElement(), StyleConstants.WIDTH, CssUnit.PX, 0 );
		InlineStyle.setInteger(element, StyleConstants.WIDTH, widthInPixels, CssUnit.PX );
		
		final int heightInPixels = ComputedStyle.getInteger( this.getElement(), StyleConstants.HEIGHT, CssUnit.PX, 0 );
		InlineStyle.setInteger(element, StyleConstants.HEIGHT, heightInPixels, CssUnit.PX );
	}
	
	protected void remove0(final Element element, final int index) {
		if (index == Constants.BACKGROUND_WIDGET_INDEX) {
			this.restoreBackgroundWidgetDimensions();
		}
		Dom.removeFromParent(element);
	}
	
	protected void restoreBackgroundWidgetDimensions(){
		final Element element = this.getBackgroundWidgetElement();
		final String width = ObjectHelper.getString(element, "_" + StyleConstants.WIDTH);
		final String height = ObjectHelper.getString(element, "_" + StyleConstants.HEIGHT);

		InlineStyle.setString(element, StyleConstants.WIDTH, width);
		InlineStyle.setString(element, StyleConstants.HEIGHT, height);
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
