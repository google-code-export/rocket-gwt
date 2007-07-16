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

import rocket.browser.client.BrowserHelper;
import rocket.dom.client.DomHelper;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;
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

        final SimplePanel panel = this.createPanel();
        this.setPanel(panel);
        this.initWidget(panel);
        this.setStyleName(SliderConstants.HORIZONTAL_SLIDER_STYLE);
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
        final int newLeft = (int) (ratio * spacerWidth) + BrowserHelper.getScrollX();

        final Element handleElement = handle.getElement();
        DOM.setStyleAttribute(handleElement, "position", "absolute");
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

        final int mouseX = BrowserHelper.getScrollX() + DOM.eventGetClientX(event);
        final int widgetX = DomHelper.getAbsoluteLeft(this.getHandle().getElement());
        this.handleBackgroundClick(mouseX, widgetX);
    }

    /**
     * Interprets any dragging mouse movements updating the handle accordingly.
     * 
     * @param event
     */
    protected void handleHandleMouseMove(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        final int widgetX = DomHelper.getAbsoluteLeft(this.getElement());
        final int mouseX = BrowserHelper.getScrollX() + DOM.eventGetClientX(event);
        final int sliderWidth = this.getOffsetWidth();
        final int handleWidth = this.getHandle().getOffsetWidth();

        this.handleMouseMove(widgetX, mouseX, sliderWidth, handleWidth);
    }
}
