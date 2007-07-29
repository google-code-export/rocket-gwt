/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.widget.slider;

import rocket.client.browser.BrowserHelper;
import rocket.client.util.ObjectHelper;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * A HorizontalSlider is a widget which allows a user to manipulate number value by clicking on different areas of the widget along the
 * x-axis.
 * 
 * @author Miroslav (mP)
 */
public class HorizontalSlider extends Slider {
    public HorizontalSlider() {
        super();

        this.initWidget(this.createPanel());
        this.addStyleName(SliderConstants.HORIZONTAL_SLIDER_STYLE);

        DOM.setEventListener(this.getElement(), this);
        this.sinkEvents(Event.ONMOUSEDOWN);
    }

    /**
     * Calculates and sets the relative position of the handle widget according to the value of this slider.
     */
    protected void updateWidget() {
        final int sliderWidth = this.getOffsetWidth();
        final Widget handle = this.getHandle();
        final int handleWidth = handle.getOffsetWidth();
        final int spacerWidth = sliderWidth - handleWidth;

        final float ratio = (float) this.getValue() / this.getMaximumValue();
        final int newLeft = (int) (ratio * spacerWidth);

        final Element handleElement = handle.getElement();
        DOM.setStyleAttribute(handleElement, "position", "relative");
        DOM.setStyleAttribute(handleElement, "left", newLeft + "px");
    }

    protected String getSliderDraggingStyleName() {
        return SliderConstants.HORIZONTAL_SLIDER_DRAGGING_STYLE;
    }

    /**
     * Tests if the mouse click occured in the slider area before or after the handle widget.
     * 
     * @param event
     */
    protected void handleBackgroundMouseDown(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        final int mouseX = DOM.eventGetClientX(event) + BrowserHelper.getScrollX();
        final int widgetX = WidgetHelper.getAbsoluteLeft(this.getHandle());
        this.handleBackgroundClick(mouseX, widgetX);
    }

    /**
     * Interprets any dragging mouse movements updating the handle accordingly.
     * 
     * @param event
     */
    protected void handleMouseMove(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        final int widgetX = WidgetHelper.getAbsoluteLeft(this);
        final int mouseX = DOM.eventGetClientX(event) + BrowserHelper.getScrollX();
        final int sliderWidth = this.getOffsetWidth();
        final int handleWidth = this.getHandle().getOffsetWidth();

        this.handleMouseMove(widgetX, mouseX, sliderWidth, handleWidth);
    }
}