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
import rocket.event.client.ChangeEventListener;
import rocket.event.client.Event;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.EventPreviewAdapter;
import rocket.event.client.FocusEventListener;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseUpEvent;
import rocket.selection.client.Selection;
import rocket.style.client.ComputedStyle;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.widget.client.CompositeWidget;
import rocket.widget.client.Html;
import rocket.widget.client.Panel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

/**
 * This slider allows the handle to be dragged or moved in two directions. As
 * such it holds two values one for the horizontal and the other the vertical
 * plane.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class FloatingSlider extends CompositeWidget {

	public FloatingSlider() {
		super();
	}

	protected Widget createWidget() {
		return this.createPanel();
	}

	protected Panel getPanel() {
		return (Panel) this.getWidget();
	}

	protected Panel createPanel() {
		return new InternalSliderPanel() {
			protected String getBackgroundStyleName() {
				return FloatingSlider.this.getBackgroundStyleName();
			}

			protected String getHandleStyleName() {
				return FloatingSlider.this.getHandleStyleName();
			}
		};
	}

	public Widget getHandle() {
		return this.getPanel().get(Constants.HANDLE_WIDGET_INDEX);
	}

	public void setHandle(final Widget handle) {
		final Panel panel = this.getPanel();
		panel.remove(Constants.HANDLE_WIDGET_INDEX);
		panel.insert(handle, Constants.HANDLE_WIDGET_INDEX);
	}

	protected Widget createHandle() {
		return new Html();
	}

	protected String getHandleStyleName() {
		return Constants.FLOATING_SLIDER_HANDLE_STYLE;
	}

	public Widget getBackground() {
		return this.getPanel().get(Constants.BACKGROUND_WIDGET_INDEX);
	}

	public void setBackground(final Widget background) {
		final Panel panel = this.getPanel();
		panel.remove(Constants.BACKGROUND_WIDGET_INDEX);
		panel.insert(background, Constants.BACKGROUND_WIDGET_INDEX);
	}

	protected Widget createBackground() {
		return new Html();
	}

	protected String getBackgroundStyleName() {
		return Constants.FLOATING_SLIDER_BACKGROUND_STYLE;
	}

	protected void afterCreateWidget() {
		super.afterCreateWidget();

		this.getEventListenerDispatcher().addMouseEventListener(new MouseEventAdapter() {
			public void onMouseDown(final MouseDownEvent event) {
				FloatingSlider.this.onMouseDown(event);
			}
		});
		this.setHandle(this.createHandle());
		this.setBackground(this.createBackground());		
	}

	protected String getInitialStyleName() {
		return Constants.FLOATING_SLIDER_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.CHANGE | EventBitMaskConstants.MOUSE_EVENTS;
	}

	/**
	 * Sub classes must invoke this method if they choose to override.
	 */
	protected void onAttach() {
		final int x = this.getXValue();
		final int y = this.getYValue();

		super.onAttach();

		this.setXValue(x);
		this.setYValue(y);
	}

	/**
	 * Dispatches the event to the respective handler depending on whether the
	 * handle or the slider background was clicked.
	 * 
	 * @param event
	 *            The cause event
	 */
	protected void onMouseDown(final MouseDownEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final Element target = event.getTarget();

			// check if the handle widget has been clicked...
			if (DOM.isOrHasChild(this.getHandle().getElement(), target)) {
				this.onHandleMouseDown(event);
				break;
			}

			// was the slider background itself clicked ?
			if (DOM.isOrHasChild(this.getElement(), target)) {
				this.onBackgroundMouseDown(event);
				break;
			}

			// unknown target do nothing...
			break;
		}
	}

	protected void onBackgroundMouseDown(final MouseDownEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		final Widget handle = this.getHandle();
		final Element widget = this.getElement();
		final int mouseX = event.getPageX() - Dom.getAbsoluteLeft(widget) - handle.getOffsetWidth() / 2;
		final int mouseY = event.getPageY() - Dom.getAbsoluteTop(widget) - handle.getOffsetHeight() / 2;

		this.onBackgroundMouseDown(mouseX, mouseY);

		final HandleSlidingTimer timer = this.getTimer();
		timer.setMouseX(mouseX);
		timer.setMouseY(mouseY);
	}

	/**
	 * Initiates the dragging of the handle until it is released.
	 * 
	 * @param event
	 */
	protected void onHandleMouseDown(final MouseDownEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		if (false == this.hasDraggingEventPreview()) {
			Selection.clearAnySelectedText();
			Selection.disableTextSelection();

			final EventPreview eventPreview = this.createDraggingEventPreview();
			this.setDraggingEventPreview(eventPreview);
			DOM.addEventPreview(eventPreview);

			this.getHandle().addStyleName(this.getDraggingStyle());
		}
	}

	protected String getDraggingStyle() {
		return Constants.FLOATING_SLIDER_DRAGGING_STYLE;
	}

	/**
	 * The EventPreview object that is following the handle whilst it is being
	 * dragged.
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
	 * This EventPreview anonymous class merely delegates to
	 * {@link #handleDraggingEventPreview(Event)}
	 * 
	 * @return
	 */
	protected EventPreview createDraggingEventPreview() {
		return new EventPreviewAdapter() {
			protected void onMouseMove(final MouseMoveEvent event) {
				FloatingSlider.this.onHandleMouseMove(event);
			}

			protected void onMouseUp(final MouseUpEvent event) {
				FloatingSlider.this.onHandleMouseUp(event);
			}
		};
	}

	/**
	 * This method is called when the mouse button is let go whilst dragging the
	 * slider handle.
	 * 
	 * @param event
	 */
	protected void onHandleMouseUp(final MouseUpEvent event) {
		this.getHandle().removeStyleName(this.getDraggingStyle());

		DOM.removeEventPreview(this.getDraggingEventPreview());
		this.clearDraggingEventPreview();
		Selection.enableTextSelection();
	}

	protected void onBackgroundMouseDown(final int mouseX, final int mouseY) {
		final Element handle = this.getHandle().getElement();
		boolean killTimer = false;

		final int x = this.getXValue();
		final int handleX = ComputedStyle.getInteger(handle, Css.LEFT, CssUnit.PX, 0);
		int deltaX = mouseX - handleX;
		if (0 != deltaX) {
			if (deltaX < 0) {
				this.onBeforeHandleXMouseDown();
			} else {
				this.onAfterHandleXMouseDown();
			}
			final int handleXAfter = ComputedStyle.getInteger(handle, Css.LEFT, CssUnit.PX, 0);
			final int deltaXAfter = mouseX - handleXAfter;

			if (deltaX < 0 ^ deltaXAfter < 0) {
				this.setXValue(x);
				InlineStyle.setInteger(handle, Css.LEFT, mouseX, CssUnit.PX);
				deltaX = 0;
			}
		}

		final int y = this.getYValue();
		final int handleY = ComputedStyle.getInteger(handle, Css.TOP, CssUnit.PX, 0);
		int deltaY = mouseY - handleY;
		if (0 != deltaY) {
			if (deltaY < 0) {
				this.onBeforeHandleYMouseDown();
			} else {
				this.onAfterHandleYMouseDown();
			}
			final int handleYAfter = ComputedStyle.getInteger(handle, Css.TOP, CssUnit.PX, 0);
			final int deltaYAfter = mouseY - handleYAfter;

			if (deltaY < 0 ^ deltaYAfter < 0) {
				this.setYValue(y);
				InlineStyle.setInteger(handle, Css.TOP, mouseY, CssUnit.PX);
				deltaY = 0;
			}
		}
		if (killTimer || deltaX == 0 && deltaY == 0) {
			this.clearTimer();
		}
	}

	/**
	 * Decreases the xValue of this slider ensuring that it does not underflow
	 * the minimum xValue of this slider.
	 */
	protected void onBeforeHandleXMouseDown() {
		final int newValue = Math.max(0, this.getXValue() - this.getDeltaX());

		final Element handle = this.getHandle().getElement();
		final int left = InlineStyle.getInteger(handle, Css.LEFT, CssUnit.PX, 0);
		this.setXValue(newValue);
		InlineStyle.setInteger(handle, Css.LEFT, left - 1, CssUnit.PX);
	}

	/**
	 * Increases the xValue of this slider ensuring that it does not exceed the
	 * maximum xValue of this slider.
	 */
	protected void onAfterHandleXMouseDown() {
		final int newValue = Math.min(this.getXValue() + this.getDeltaX(), this.getMaximumXValue());
		final Element handle = this.getHandle().getElement();
		final int left = InlineStyle.getInteger(handle, Css.LEFT, CssUnit.PX, 0);
		this.setXValue(newValue);
		InlineStyle.setInteger(handle, Css.LEFT, left + 1, CssUnit.PX);
	}

	/**
	 * Decreases the yValue of this slider ensuring that it does not underflow
	 * the minimum yValue of this slider.
	 */
	protected void onBeforeHandleYMouseDown() {
		final int newValue = Math.max(0, this.getYValue() - this.getDeltaY());

		final Element handle = this.getHandle().getElement();
		final int top = InlineStyle.getInteger(handle, Css.TOP, CssUnit.PX, 0);
		this.setYValue(newValue);
		InlineStyle.setInteger(handle, Css.TOP, top - 1, CssUnit.PX);
	}

	/**
	 * Increases the yValue of this slider ensuring that it does not exceed the
	 * maximum yValue of this slider.
	 */
	protected void onAfterHandleYMouseDown() {
		final int newValue = Math.min(this.getYValue() + this.getDeltaY(), this.getMaximumYValue());

		final Element handle = this.getHandle().getElement();
		final int top = InlineStyle.getInteger(handle, Css.TOP, CssUnit.PX, 0);
		this.setYValue(newValue);
		InlineStyle.setInteger(handle, Css.TOP, top + 1, CssUnit.PX);
	}

	protected void onHandleMouseMove(final MouseMoveEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		final Element sliderElement = this.getElement();
		final int widgetX = Dom.getAbsoluteLeft(sliderElement);
		final int widgetY = Dom.getAbsoluteTop(sliderElement);
		final int sliderWidth = this.getOffsetWidth();
		final int sliderHeight = this.getOffsetHeight();

		final Widget handle = this.getHandle();
		final Element handleElement = handle.getElement();
		final int handleWidth = handle.getOffsetWidth();
		final int handleHeight = handle.getOffsetHeight();

		// make mouse coordinates relative to top/left of slider.
		int mouseX = event.getPageX() - widgetX;
		mouseX = Math.max(0, Math.min(mouseX, sliderWidth - handleWidth / 2));

		final int newX = this.updateSliderValue(mouseX, this.getMaximumXValue(), sliderWidth, handleWidth);
		this.setXValue(newX);

		int left = mouseX - handleWidth / 2;
		InlineStyle.setInteger(handleElement, Css.LEFT, left, CssUnit.PX);

		int mouseY = event.getPageY() - widgetY;
		mouseY = Math.max(0, Math.min(mouseY, sliderHeight - handleHeight / 2));
		final int newY = this.updateSliderValue(mouseY, this.getMaximumYValue(), sliderHeight, handleHeight);

		this.setYValue(newY);

		final int top = mouseY - handleHeight / 2;
		InlineStyle.setInteger(handleElement, Css.TOP, top, CssUnit.PX);

		event.cancelBubble(true);
		event.stop();// stops text selection in Opera
	}

	protected int updateSliderValue(final int mouseCoordinate, final int maximumValue, final int sliderLength, final int handleLength) {
		final int range = sliderLength - handleLength;

		int value = mouseCoordinate;
		if (value < 0) {
			value = 0;
		}
		if (value > range) {
			value = range;
		}
		return (int) ((float) value / range * maximumValue + 0.5f);
	}

	/**
	 * A timer is used to simulate multiple clicks when holding down the mouse
	 * button
	 */
	private HandleSlidingTimer timer;

	protected HandleSlidingTimer getTimer() {
		if (false == this.hasTimer()) {
			final HandleSlidingTimer timer = new HandleSlidingTimer();
			timer.scheduleRepeating(this.getMouseDownRepeatRate());
			this.setTimer(timer);
		}

		ObjectHelper.checkNotNull("field:timer", timer);
		return timer;
	}

	protected boolean hasTimer() {
		return null != this.timer;
	}

	protected void setTimer(final HandleSlidingTimer timer) {
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
	 * This timer is activated whenever the user clicks and holds on a
	 * background portion of the slider. Every mouseDownRepeatRate the slider
	 * moves towards to the target point.
	 */
	private class HandleSlidingTimer extends Timer {
		public void run() {
			FloatingSlider.this.onBackgroundMouseDown(this.getMouseX(), this.getMouseY());
		}

		private int mouseX;

		int getMouseX() {
			return mouseX;
		}

		void setMouseX(final int mouseX) {
			this.mouseX = mouseX;
		}

		private int mouseY;

		int getMouseY() {
			return mouseY;
		}

		void setMouseY(final int mouseY) {
			this.mouseY = mouseY;
		}
	}

	/**
	 * This value in milliseconds controls the repetition of mouse down events
	 * within the background area of the slider. Smaller values result in a
	 * faster glide of the handle towards the mouse, whilst larger values result
	 * in a slow movement.
	 */
	private int mouseDownRepeatRate;

	public int getMouseDownRepeatRate() {
		PrimitiveHelper.checkGreaterThan("field:mouseDownRepeatRate", 0, mouseDownRepeatRate);
		return this.mouseDownRepeatRate;
	}

	public void setMouseDownRepeatRate(final int mouseDownRepeatRate) {
		PrimitiveHelper.checkGreaterThan("parameter:mouseDownRepeatRate", 0, mouseDownRepeatRate);
		this.mouseDownRepeatRate = mouseDownRepeatRate;
	}

	/**
	 * If the mouse has moved away from the slider cancel any active timer.
	 * 
	 * @param event
	 */
	protected void onMouseOut(final MouseOutEvent event) {
		this.clearTimer();
	}

	/**
	 * This method handles any mouse up events.
	 * 
	 * @param event
	 */
	protected void onMouseUp(final MouseUpEvent event) {
		this.clearTimer();
	}

	// WIDGET :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	private int xValue;

	public int getXValue() {
		return this.xValue;
	}

	public void setXValue(final int xValue) {
		final int maximumValue = this.getMaximumXValue();
		PrimitiveHelper.checkBetween("parameter:xValue", xValue, 0, maximumValue + 1);

		this.xValue = xValue;

		final Widget handle = this.getHandle();
		final int sliderLength = this.getOffsetWidth() - handle.getOffsetWidth();
		final int newLeft = Math.round(1.0f * this.getXValue() * sliderLength / this.getMaximumXValue());

		final Element element = handle.getElement();
		InlineStyle.setString(element, Css.POSITION, "absolute");
		InlineStyle.setInteger(element, Css.LEFT, newLeft, CssUnit.PX);

		this.getEventListenerDispatcher().getChangeEventListeners().fireChange(this);
	}

	/**
	 * The maximum xValue of the slider. The minimum xValue is defaulted to 0.
	 * Clients must adjust this xValue if they wish to use a different range of
	 * values.
	 */
	private int maximumXValue;

	public int getMaximumXValue() {
		PrimitiveHelper.checkGreaterThanOrEqual("field:maximumXValue", 0, maximumXValue);
		return this.maximumXValue;
	}

	public void setMaximumXValue(final int maximumXValue) {
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:maximumXValue", 0, maximumXValue);
		this.maximumXValue = maximumXValue;
	}

	/**
	 * The amount the xValue jumps. The xValue must be 1 or more.
	 */
	private int deltaX;

	public int getDeltaX() {
		PrimitiveHelper.checkGreaterThan("field:deltaX", 0, deltaX);
		return this.deltaX;
	}

	public void setDeltaX(final int xDelta) {
		PrimitiveHelper.checkGreaterThan("parameter:deltaX", 0, xDelta);
		this.deltaX = xDelta;
	}

	/**
	 * The current yValue of the slider
	 */
	private int yValue;

	public int getYValue() {
		return this.yValue;
	}

	public void setYValue(final int yValue) {
		final int maximumValue = this.getMaximumYValue();
		PrimitiveHelper.checkBetween("parameter:yValue", yValue, 0, maximumValue + 1);

		this.yValue = yValue;

		final Widget handle = this.getHandle();
		final int sliderLength = this.getOffsetHeight() - handle.getOffsetHeight();
		final int newTop = Math.round(1.0f * this.getYValue() * sliderLength / this.getMaximumYValue());

		final Element element = handle.getElement();
		InlineStyle.setString(element, Css.POSITION, "absolute");
		InlineStyle.setInteger(element, Css.TOP, newTop, CssUnit.PX);

		this.getEventListenerDispatcher().getChangeEventListeners().fireChange(this);
	}

	/**
	 * The maximum xValue of the slider. The minimum xValue is defaulted to 0.
	 * Clients must adjust this xValue if they wish to use a different range of
	 * values.
	 */
	private int maximumYValue;

	public int getMaximumYValue() {
		PrimitiveHelper.checkGreaterThanOrEqual("field:maximumYValue", 0, this.maximumYValue);
		return this.maximumYValue;
	}

	public void setMaximumYValue(final int maximumYValue) {
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:maximumYValue", 0, maximumYValue);
		this.maximumYValue = maximumYValue;
	}

	/**
	 * The amount the yValue jumps. The yValue must be 1 or more.
	 */
	private int deltaY;

	public int getDeltaY() {
		PrimitiveHelper.checkGreaterThan("field:deltaY", 0, deltaY);
		return this.deltaY;
	}

	public void setDeltaY(final int yDelta) {
		PrimitiveHelper.checkGreaterThan("parameter:deltaY", 0, yDelta);
		this.deltaY = yDelta;
	}

	public void addChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().addChangeEventListener(changeEventListener);
	}

	public void removeChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().removeChangeEventListener(changeEventListener);
	}

	public void addFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().addFocusEventListener(focusEventListener);
	}

	public void removeFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().removeFocusEventListener(focusEventListener);
	}

	public String toString() {
		return super.toString() + ", values: " + xValue + "," + yValue;
	}
}
