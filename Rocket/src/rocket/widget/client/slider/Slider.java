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

import rocket.dom.client.DomHelper;
import rocket.dragndrop.client.DragNDropHelper;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.widget.client.AbstractNumberHolder;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Common base class for both the Horizontal and Vertical Slider widgets.
 * 
 * It contains both common properties and behaviour with sub-classes required to implement a bare minimum.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class Slider extends AbstractNumberHolder {

    protected void onAttach() {
        super.onAttach();

        DOM.setEventListener(this.getElement(), this);
        this.unsinkEvents(-1);
        this.sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOUT | Event.ONMOUSEMOVE);
        this.updateWidget();
    }

    /**
     * Sub-classes need to override this method to update the coordinates of the handle widget based on the sliders value.
     */
    protected abstract void updateWidget();

    // EVENT HANDLING
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Dispatches to the appropriate method depending on the event type.
     */
    public void onBrowserEvent(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final int eventType = DOM.eventGetType(event);
            if (eventType == Event.ONMOUSEDOWN) {
                handleMouseDown(event);
                break;
            }

            if (eventType == Event.ONMOUSEOUT) {
                this.handleMouseOut(event);
                break;
            }
            break;
        }
    }

    /**
     * If the mouse has moved away from the slider cancel any active timer.
     * 
     * @param event
     */
    protected void handleMouseOut(final Event event) {
        this.clearTimer();
    }

    /**
     * Dispatches the event to the respective handler depending on whether the handle or the slider background was clicked.
     * 
     * @param event
     */
    protected void handleMouseDown(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            final Element target = DOM.eventGetTarget(event);

            // check if the handle widget has been clicked...
            if (DOM.isOrHasChild(this.getHandle().getElement(), target)) {
                this.handleHandleMouseDown(event);
                break;
            }

            // was the slider background itself clicked ?
            if (DOM.isOrHasChild(this.getElement(), target)) {
                this.handleBackgroundMouseDown(event);
                break;
            }

            // unknown target do nothing...
            break;
        }
    }

    protected abstract void handleBackgroundMouseDown(Event event);

    /**
     * Initiates the dragging of the handle until it is released.
     * 
     * @param event
     */
    protected void handleHandleMouseDown(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        if (false == this.hasDraggingEventPreview()) {
            DragNDropHelper.clearAnySelectedText();
            DragNDropHelper.disableTextSelection(DomHelper.getBody());
            DOM.addEventPreview(this.createDraggingEventPreview());
            this.getHandle().addStyleName(this.getSliderDraggingStyleName());
        }
    }

    /**
     * The EventPreview object that is following the handle whilst it is being dragged.
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
     * Manages the event type removing the EventPreview when the mouse button is released and updating the handle via
     * {@link #handleHandleMouseMove(Event)}
     * 
     * @param event
     */
    protected boolean handleDraggingEventPreview(final Event event) {
        boolean cancelEvent = true;

        while (true) {
            final int type = DOM.eventGetType(event);
            if (type == Event.ONMOUSEMOVE) {
                handleHandleMouseMove(event);
                cancelEvent = true;
                break;
            }
            if (type == Event.ONMOUSEUP) {
                this.handleHandleMouseUp(event);
                cancelEvent = false;
                break;
            }
            cancelEvent = true;
            break;
        }
        return !cancelEvent;
    }

    /**
     * This method is called when the mouse button is let go whilst dragging the slider handle.
     * 
     * @param event
     */
    protected void handleHandleMouseUp(final Event event) {
        this.getHandle().removeStyleName(this.getSliderDraggingStyleName());

        DOM.removeEventPreview(this.getDraggingEventPreview());
        this.clearDraggingEventPreview();
        DragNDropHelper.enableTextSelection(DomHelper.getBody());
    }

    /**
     * Sub-classes need to return the style that is added to the handle widget when it is being dragged or removed when the dragging is
     * stopped.
     * 
     * @return
     */
    protected abstract String getSliderDraggingStyleName();

    protected abstract void handleHandleMouseMove(Event event);

    protected void handleMouseMove(final int widgetCoordinate, final int mouseCoordinate, final int sliderLength,
            final int handleLength) {
        final int range = sliderLength - handleLength;

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
            this.handleBeforeHandleClick();
        } else {
            this.handleAfterHandleClick();
        }
    }

    /**
     * Decreases the value of this slider ensuring that it does not underflow the minimum value of this slider.
     */
    protected void handleBeforeHandleClick() {
        int newValue = this.getValue() - this.getBigDelta();
        if (newValue < 0) {
            newValue = 0;
        }
        this.setValue(newValue);

        // if a timer is not already running create one...
        if (false == this.hasTimer()) {
            final Timer timer = new Timer() {
                public void run() {
                    Slider.this.handleBeforeHandleClick();
                }
            };
            timer.scheduleRepeating(Slider.this.getMouseDownRepeatRate());
            Slider.this.setTimer(timer);
        }
    }

    /**
     * Increases the value of this slider ensuring that it does not exceed the maximum value of this slider.
     */
    protected void handleAfterHandleClick() {
        int newValue = this.getValue() + this.getBigDelta();
        final int maximumValue = this.getMaximumValue();
        if (newValue > maximumValue) {
            newValue = maximumValue;
        }
        this.setValue(newValue);

        // if a timer is not already running create one...
        if (false == this.hasTimer()) {
            final Timer timer = new Timer() {
                public void run() {
                    Slider.this.handleAfterHandleClick();
                }
            };
            timer.scheduleRepeating(Slider.this.getMouseDownRepeatRate());
            Slider.this.setTimer(timer);
        }
    }

    /**
     * A timer is used to simulate multiple clicks when holding down the mouse button
     */
    private Timer timer;

    protected Timer getTimer() {
        ObjectHelper.checkNotNull("field:timer", timer);
        return timer;
    }

    protected boolean hasTimer() {
        return null != this.timer;
    }

    protected void setTimer(final Timer timer) {
        ObjectHelper.checkNotNull("parameter:timer", timer);
        this.timer = timer;
    }

    /**
     * Clears any active timer.
     */
    protected void clearTimer() {
        if (this.hasTimer()) {
            this.timer.cancel();
        }
        this.timer = null;
    }

    /**
     * This value in milliseconds controls the repetition of mouse down events within the background area of the slider.
     */
    private int mouseDownRepeatRate;

    public int getMouseDownRepeatRate() {
        PrimitiveHelper.checkGreaterThan("field:mouseDownRepeatRate", mouseDownRepeatRate, 0);
        return this.mouseDownRepeatRate;
    }

    public void setMouseDownRepeatRate(final int mouseDownRepeatRate) {
        PrimitiveHelper.checkGreaterThan("parameter:mouseDownRepeatRate", mouseDownRepeatRate, 0);
        this.mouseDownRepeatRate = mouseDownRepeatRate;
    }

    // WIDGET :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * A panel is used to hold both the sliders element and house the handle widget.
     */
    private SimplePanel panel;

    protected SimplePanel getPanel() {
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
        final Element element = panel.getElement();
        DOM.setStyleAttribute(element, StyleConstants.POSITION, "relative");
        DOM.setStyleAttribute(element, StyleConstants.LEFT, "0px");
        DOM.setStyleAttribute(element, StyleConstants.TOP, "0px");
        this.setPanel(panel);
        return panel;
    }

    public Widget getHandle() {
        return this.getPanel().getWidget();
    }

    public void setHandle(final Widget handle) {
        ObjectHelper.checkNotNull("parameter:handle", handle);
        this.getPanel().setWidget(handle);
    }

    // SLIDER ::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * The current value of the slider
     */
    private int value;

    public int getValue() {
        PrimitiveHelper.checkBetween("field:value", value, 0, this.maximumValue + 1);
        return this.value;
    }

    public void setValue(final int value) {
        PrimitiveHelper.checkBetween("parameter:value", value, 0, this.maximumValue + 1);
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
     * The amount the slider value jumps when the mouse is clicked on the area before or after the handle thingo.
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
