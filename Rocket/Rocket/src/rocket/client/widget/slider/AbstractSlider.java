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

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.widget.AbstractNumberHolder;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Common base class for both the Horizontal and Vertical Slider widgets.
 * 
 * It contains both common properties and behaviour with sub-classes required to implement a bare minimum.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class AbstractSlider extends AbstractNumberHolder {

    protected void onAttach() {
        super.onAttach();
        DOM.setEventListener(this.getElement(), this);
        this.updateWidget();
    }

    /**
     * Sub-classes need to override this method to update the coordinates of the thumb widget based on the sliders value.
     * 
     */
    protected abstract void updateWidget();

    // EVENT HANDLING
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void onBrowserEvent(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final int eventType = DOM.eventGetType(event);
            if (eventType == Event.ONMOUSEDOWN) {
                handleMouseClick(event);
                break;
            }
            break;
        }
    }

    /**
     * Dispatches the event to the respective handler depending on whether the thumb or the slider background was clicked.
     * 
     * @param event
     */
    protected void handleMouseClick(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final Element target = DOM.eventGetTarget(event);

            // check if the thumb widget has been clicked...
            if (DOM.isOrHasChild(this.getWidget().getElement(), target)) {
                this.handleThumbClick(event);
                break;
            }

            // was the slider background itself clicked ?
            if (DOM.isOrHasChild(this.getElement(), target)) {
                this.handleBackgroundClick(event);
                break;
            }

            // unknown target do nothing...
            break;
        }
    }

    protected abstract void handleBackgroundClick(Event event);

    /**
     * Initiates the dragging of the thumb until it is released.
     * 
     * @param event
     */
    protected void handleThumbClick(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        if (false == this.hasDraggingEventPreview()) {
            DOM.addEventPreview(this.createDraggingEventPreview());
            this.getWidget().addStyleName(this.getSliderDraggingStyleName());
        }
    }

    /**
     * The EventPreview object that is following the thumb whilst it is being dragged.
     */
    private EventPreview draggingEventPreview;

    protected EventPreview getDraggingEventPreview() {
        ObjectHelper.checkNotNull("field:draggingEventPreview", draggingEventPreview);
        return this.draggingEventPreview;
    }

    protected boolean hasDraggingEventPreview() {
        return null != this.draggingEventPreview;
    }

    protected void setDraggingEventPreview(final EventPreview draggingEventPreview) {
        ObjectHelper.checkNotNull("parameter:draggingEventPreview", draggingEventPreview);
        this.draggingEventPreview = draggingEventPreview;
    }

    protected void clearDraggingEventPreview() {
        this.draggingEventPreview = null;
    }

    /**
     * This EventPreview anonymous class merely delegates to {@link #handleDraggingEventPreview(Event)}
     * 
     * @return
     */
    protected EventPreview createDraggingEventPreview() {
        final EventPreview draggingEventPreview = new EventPreview() {
            public boolean onEventPreview(final Event event) {
                return handleDraggingEventPreview(event);
            }
        };
        this.setDraggingEventPreview(draggingEventPreview);
        return draggingEventPreview;
    }

    /**
     * Manages the event type removing the EventPreview when the mouse button is released and updating the thumb via
     * {@link #handleMouseMove(Event)}
     * 
     * @param event
     */
    protected boolean handleDraggingEventPreview(final Event event) {
        boolean cancelEvent = true;

        while (true) {
            final int type = DOM.eventGetType(event);
            if (type == Event.ONMOUSEMOVE) {
                handleMouseMove(event);
                cancelEvent = true;
                break;
            }
            if (type == Event.ONMOUSEUP || type == Event.ONMOUSEDOWN) {
                DOM.removeEventPreview(this.getDraggingEventPreview());
                this.getWidget().removeStyleName(this.getSliderDraggingStyleName());
                this.clearDraggingEventPreview();
                cancelEvent = false;
                break;
            }
            cancelEvent = true;
            break;
        }
        return !cancelEvent;
    }

    /**
     * Sub-classes need to return the style that is added to the thumb widget when it is being dragged or removed when the dragging is
     * stopped.
     * 
     * @return
     */
    protected abstract String getSliderDraggingStyleName();

    protected abstract void handleMouseMove(Event event);

    protected void handleMouseMove(final int widgetCoordinate, final int mouseCoordinate, final int sliderLength,
            final int thumbLength) {
        final int range = sliderLength - thumbLength;

        int value = mouseCoordinate - widgetCoordinate;
        if (value < 0) {
            value = 0;
        }
        if (value > range) {
            value = range;
        }

        final float value0 = (float) value / range * this.getMaximumValue() + 0.5f;
        final int delta = this.getDelta();
        value = (int) value0 / delta * delta;

        this.setValue(value);
    }

    protected void handleBackgroundClick(final int mouseCoordinate, final int widgetCoordinate) {
        if (mouseCoordinate < widgetCoordinate) {
            this.handleBeforeThumbClick();
        } else {
            this.handleAfterThumbClick();
        }
    }

    /**
     * Decreases the value of this slider ensuring that it does not underflow the minimum value of this slider.
     */
    protected void handleBeforeThumbClick() {
        int newValue = this.getValue() - this.getBigDelta();
        if (newValue < 0) {
            newValue = 0;
        }
        this.setValue(newValue);
    }

    /**
     * Increases the value of this slider ensuring that it does not exceed the maximum value of this slider.
     */
    protected void handleAfterThumbClick() {
        int newValue = this.getValue() + this.getBigDelta();
        final int maximumValue = this.getMaximumValue();
        if (newValue > maximumValue) {
            newValue = maximumValue;
        }
        this.setValue(newValue);
    }

    // WIDGET :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * A non pubic is used to hold both the sliders element and house the thumb widget.
     */
    private SimplePanel panel;

    public SimplePanel getPanel() {
        ObjectHelper.checkNotNull("field:panel", panel);
        return panel;
    }

    protected boolean hasPanel() {
        return null != this.panel;
    }

    protected void setPanel(final SimplePanel panel) {
        ObjectHelper.checkNotNull("parameter:panel", panel);

        this.panel = panel;
    }

    protected SimplePanel createPanel() {
        final SimplePanel panel = new SimplePanel();
        this.setPanel(panel);
        return panel;
    }

    public Widget getWidget() {
        return this.getPanel().getWidget();
    }

    public void setWidget(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);
        this.getPanel().setWidget(widget);
    }

    // SLIDER ::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * The current value of the slider
     */
    private int value;

    public int getValue() {
        PrimitiveHelper.checkGreaterThanOrEqual("field:value", value, 0);
        return this.value;
    }

    public void setValue(int value) {
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:value", value, 0);
        this.value = value;
        this.updateWidget();
        this.fireValueChanged();
    }

    /**
     * The maximum value of the slider. The minimum value is defaulted to 0. Clients must adjust this value if they wish to use a different
     * range of values.
     */
    private int maximumValue;

    public int getMaximumValue() {
        PrimitiveHelper.checkGreaterThanOrEqual("field:maximumValue", maximumValue, 0);
        return this.maximumValue;
    }

    public void setMaximumValue(int maximumValue) {
        PrimitiveHelper.checkGreaterThanOrEqual("parameter:maximumValue", maximumValue, 0);
        this.maximumValue = maximumValue;
    }

    /**
     * The amount the value jumps. The value must be 1 or more.
     */
    private int delta;

    public int getDelta() {
        PrimitiveHelper.checkGreaterThan("field:delta", delta, 0);
        return this.delta;
    }

    public void setDelta(int delta) {
        PrimitiveHelper.checkGreaterThan("parameter:delta", delta, 0);
        this.delta = delta;
    }

    /**
     * The amount the slider value jumps when the mouse is clicked on the area before or after the thumb thingo.
     * 
     * This value is typically larger than delta.
     */
    private int bigDelta;

    public int getBigDelta() {
        PrimitiveHelper.checkGreaterThan("field:bigDelta", bigDelta, 0);
        return this.bigDelta;
    }

    public void setBigDelta(int bigDelta) {
        PrimitiveHelper.checkGreaterThan("parameter:bigDelta", bigDelta, 0);
        this.bigDelta = bigDelta;
    }

    public String toString() {
        return super.toString() + ", value: " + value + ", maximumValue: " + maximumValue + ", delta: " + delta
                + ", bigDelta: " + bigDelta;
    }
}
