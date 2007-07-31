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
 * A VerticalSlider is a widget which allows a user to manipulate number value by clicking on different areas of the widget moving along the
 * y-axis.
 * 
 * @author Miroslav (mP)
 */
public class VerticalSlider extends Slider {
    public VerticalSlider() {
        super();

        final SimplePanel panel = this.createPanel();
        this.setPanel(panel);
        this.initWidget(panel);
        
        this.setStyleName(SliderConstants.VERTICAL_SLIDER_STYLE);
    }

    /**
     * Calculates and sets the relative position of the handle widget according to the value of this slider.
     */
    protected void updateWidget() {
        final int sliderHeight = this.getOffsetHeight();
        final Widget handle = this.getHandle();
        final int handleHeight = handle.getOffsetHeight();
        final int spacerHeight = sliderHeight - handleHeight;

        final float ratio = (float) this.getValue() / this.getMaximumValue();
        final int newTop = (int) (ratio * spacerHeight);

        final Element handleElement = handle.getElement();
        DOM.setStyleAttribute(handleElement, "position", "absolute");
        DOM.setStyleAttribute(handleElement, "top", newTop + "px");
    }

    protected String getSliderDraggingStyleName() {
        return SliderConstants.VERTICAL_SLIDER_DRAGGING_STYLE;
    }

    /**
     * Tests if the mouse click occured in the slider area before or after the handle widget.
     * 
     * @param event
     */
    protected void handleBackgroundMouseDown(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        final int mouseY = BrowserHelper.getScrollY() + DOM.eventGetClientY(event);
        final int widgetY = DomHelper.getAbsoluteTop(this.getHandle().getElement());
        this.handleBackgroundClick(mouseY, widgetY);
    }

    /**
     * Interprets any dragging mouse movements updating the handle accordingly.
     * 
     * @param event
     */
    protected void handleHandleMouseMove(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        final int widgetY = DomHelper.getAbsoluteTop(this.getElement());
        final int mouseY = BrowserHelper.getScrollY() + DOM.eventGetClientY(event);

        final int sliderHeight = this.getOffsetHeight();
        final int handleHeight = this.getHandle().getOffsetHeight();

        this.handleMouseMove(widgetY, mouseY, sliderHeight, handleHeight);
    }
}
