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

import rocket.browser.client.Browser;
import rocket.dom.client.Dom;
import rocket.style.client.StyleConstants;

import com.google.gwt.user.client.Event;

/**
 * A VerticalSlider is a widget which allows a user to manipulate number value
 * by clicking on different areas of the widget moving along the y-axis.
 * 
 * @author Miroslav (mP)
 */
public class VerticalSlider extends Slider {
	public VerticalSlider() {
		super();

		this.setStyleName(Constants.VERTICAL_SLIDER_STYLE);
	}

	protected String getHandleStyleName() {
		return Constants.VERTICAL_SLIDER_HANDLE_STYLE;
	}

	protected String getSliderDraggingStyleName() {
		return Constants.VERTICAL_SLIDER_DRAGGING_STYLE;
	}

	protected int getMousePageCoordinate(final Event event) {
		return Browser.getMousePageY(event);
	}

	protected int getAbsoluteWidgetCoordinate() {
		return Dom.getAbsoluteTop(this.getElement());
	}

	protected String getHandleCoordinateStylePropertyName() {
		return StyleConstants.TOP;
	}

	protected int getSliderLength() {
		return this.getOffsetHeight();
	}

	protected int getHandleLength() {
		return this.getHandle().getOffsetHeight();
	}
}
