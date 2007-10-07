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
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.widget.client.CompositeWidget;
import rocket.widget.client.DivPanel;
import rocket.widget.client.Panel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * This slider allows the handle to be dragged or moved in two directions. As
 * such it holds two values one for the horizontal and the other the vertical
 * plane.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HorizontalVerticalSlider extends CompositeWidget {

	public HorizontalVerticalSlider() {
		super();
	}

	protected Widget createWidget() {
		final Panel panel = this.createPanel();
		this.setPanel(panel);
		return panel;
	}

	protected void afterCreateWidget() {
		super.afterCreateWidget();

		this.getEventListenerDispatcher().addMouseEventListener(new MouseEventAdapter() {
			public void onMouseDown(final MouseDownEvent event) {
				HorizontalVerticalSlider.this.handleMouseDown(event);
			}
		});
	}

	protected String getInitialStyleName() {
		return Constants.HORIZONTAL_VERTICAL_SLIDER_STYLE;
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
	protected void handleMouseDown(final MouseDownEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final Element target = event.getTarget();

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

	protected void handleBackgroundMouseDown(final MouseDownEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		final Widget handle = this.getHandle();
		final Element widget = this.getElement();
		final int mouseX = event.getPageX() - Dom.getAbsoluteLeft(widget) - handle.getOffsetWidth() / 2;
		final int mouseY = event.getPageY() - Dom.getAbsoluteTop(widget) - handle.getOffsetHeight() / 2;

		this.handleBackgroundMouseDown(mouseX, mouseY);

		final HandleSlidingTimer timer = this.getTimer();
		timer.setMouseX(mouseX);
		timer.setMouseY(mouseY);
	}

	/**
	 * Initiates the dragging of the handle until it is released.
	 * 
	 * @param event
	 */
	protected void handleHandleMouseDown(final MouseDownEvent event) {
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
		return Constants.HORIZONTAL_VERTICAL_SLIDER_DRAGGING_STYLE;
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
				HorizontalVerticalSlider.this.handleHandleMouseMove(event);
			}

			protected void onMouseUp(final MouseUpEvent event) {
				HorizontalVerticalSlider.this.handleHandleMouseUp(event);
			}
		};
	}

	/**
	 * This method is called when the mouse button is let go whilst dragging the
	 * slider handle.
	 * 
	 * @param event
	 */
	protected void handleHandleMouseUp(final MouseUpEvent event) {
		this.getHandle().removeStyleName(this.getDraggingStyle());

		DOM.removeEventPreview(this.getDraggingEventPreview());
		this.clearDraggingEventPreview();
		Selection.enableTextSelection();
	}

	protected void handleBackgroundMouseDown(final int mouseX, final int mouseY) {
		final Element handle = this.getHandle().getElement();
		boolean killTimer = false;

		final int x = this.getXValue();
		final int handleX = ComputedStyle.getInteger(handle, StyleConstants.LEFT, CssUnit.PX, 0);
		int deltaX = mouseX - handleX;
		if (0 != deltaX) {
			if (deltaX < 0) {
				this.handleBeforeHandleXMouseDown();
			} else {
				this.handleAfterHandleXMouseDown();
			}
			final int handleXAfter = ComputedStyle.getInteger(handle, StyleConstants.LEFT, CssUnit.PX, 0);
			final int deltaXAfter = mouseX - handleXAfter;

			if (deltaX < 0 ^ deltaXAfter < 0) {
				this.setXValue(x);
				InlineStyle.setInteger(handle, StyleConstants.LEFT, mouseX, CssUnit.PX);
				deltaX = 0;
			}
		}

		final int y = this.getYValue();
		final int handleY = ComputedStyle.getInteger(handle, StyleConstants.TOP, CssUnit.PX, 0);
		int deltaY = mouseY - handleY;
		if (0 != deltaY) {
			if (deltaY < 0) {
				this.handleBeforeHandleYMouseDown();
			} else {
				this.handleAfterHandleYMouseDown();
			}
			final int handleYAfter = ComputedStyle.getInteger(handle, StyleConstants.TOP, CssUnit.PX, 0);
			final int deltaYAfter = mouseY - handleYAfter;

			if (deltaY < 0 ^ deltaYAfter < 0) {
				this.setYValue(y);
				InlineStyle.setInteger(handle, StyleConstants.TOP, mouseY, CssUnit.PX);
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
	protected void handleBeforeHandleXMouseDown() {
		int newValue = this.getXValue() - this.getDeltaX();
		if (newValue < 0) {
			newValue = 0;
		}
		final Element handle = this.getHandle().getElement();
		final int left = InlineStyle.getInteger(handle, StyleConstants.LEFT, CssUnit.PX, 0);
		this.setXValue(newValue);
		InlineStyle.setInteger(handle, StyleConstants.LEFT, left - 1, CssUnit.PX);
	}

	/**
	 * Increases the xValue of this slider ensuring that it does not exceed the
	 * maximum xValue of this slider.
	 */
	protected void handleAfterHandleXMouseDown() {
		int newValue = this.getXValue() + this.getDeltaX();
		final int maximumValue = this.getMaximumXValue();
		if (newValue > maximumValue) {
			newValue = maximumValue;
		}
		final Element handle = this.getHandle().getElement();
		final int left = InlineStyle.getInteger(handle, StyleConstants.LEFT, CssUnit.PX, 0);
		this.setXValue(newValue);
		InlineStyle.setInteger(handle, StyleConstants.LEFT, left + 1, CssUnit.PX);
	}

	/**
	 * Decreases the yValue of this slider ensuring that it does not underflow
	 * the minimum yValue of this slider.
	 */
	protected void handleBeforeHandleYMouseDown() {
		int newValue = this.getYValue() - this.getDeltaY();
		if (newValue < 0) {
			newValue = 0;
		}
		final Element handle = this.getHandle().getElement();
		final int top = InlineStyle.getInteger(handle, StyleConstants.TOP, CssUnit.PX, 0);
		this.setYValue(newValue);
		InlineStyle.setInteger(handle, StyleConstants.TOP, top - 1, CssUnit.PX);
	}

	/**
	 * Increases the yValue of this slider ensuring that it does not exceed the
	 * maximum yValue of this slider.
	 */
	protected void handleAfterHandleYMouseDown() {
		int newValue = this.getYValue() + this.getDeltaY();
		final int maximumValue = this.getMaximumYValue();
		if (newValue > maximumValue) {
			newValue = maximumValue;
		}
		final Element handle = this.getHandle().getElement();
		final int top = InlineStyle.getInteger(handle, StyleConstants.TOP, CssUnit.PX, 0);
		this.setYValue(newValue);
		InlineStyle.setInteger(handle, StyleConstants.TOP, top + 1, CssUnit.PX);
	}

	protected void handleHandleMouseMove(final MouseMoveEvent event) {
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
		InlineStyle.setInteger(handleElement, StyleConstants.LEFT, left, CssUnit.PX);

		int mouseY = event.getPageY() - widgetY;
		mouseY = Math.max(0, Math.min(mouseY, sliderHeight - handleHeight / 2));
		final int newY = this.updateSliderValue(mouseY, this.getMaximumYValue(), sliderHeight, handleHeight);

		this.setYValue(newY);

		final int top = mouseY - handleHeight / 2;
		InlineStyle.setInteger(handleElement, StyleConstants.TOP, top, CssUnit.PX);

		event.cancelBubble(true);
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
			HorizontalVerticalSlider.this.handleBackgroundMouseDown(this.getMouseX(), this.getMouseY());
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
	protected void handleMouseOut(final MouseOutEvent event) {
		this.clearTimer();
	}

	/**
	 * This method handles any mouse up events.
	 * 
	 * @param event
	 */
	protected void handleMouseUp(final MouseUpEvent event) {
		this.clearTimer();
	}

	// WIDGET :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * A panel is used to hold both the sliders element and house the handle
	 * widget.
	 */
	private Panel panel;

	protected Panel getPanel() {
		ObjectHelper.checkNotNull("field:panel", panel);
		return panel;
	}

	protected boolean hasPanel() {
		return null != this.panel;
	}

	protected void setPanel(final Panel panel) {
		ObjectHelper.checkNotNull("parameter:panel", panel);

		this.panel = panel;
	}

	protected Panel createPanel() {
		final Panel panel = new DivPanel();
		final Element element = panel.getElement();
		InlineStyle.setString(element, StyleConstants.POSITION, "relative");
		InlineStyle.setInteger(element, StyleConstants.LEFT, 0, CssUnit.PX);
		InlineStyle.setInteger(element, StyleConstants.TOP, 0, CssUnit.PX);

		panel.add(new HTML());
		return panel;
	}

	public Widget getHandle() {
		return this.getPanel().get(0);
	}

	public void setHandle(final Widget handle) {
		ObjectHelper.checkNotNull("parameter:handle", handle);

		final Panel panel = this.getPanel();
		final String handleStyle = this.getHandleStyle();

		final Widget previous = panel.get(0);
		previous.removeStyleName(handleStyle);
		panel.remove(0);

		panel.insert(handle, 0);
		handle.addStyleName(handleStyle);
	}

	protected String getHandleStyle() {
		return Constants.HORIZONTAL_VERTICAL_SLIDER_HANDLE_STYLE;
	}

	// SLIDER ::::::::::::::::::::::::::::::::::::::::::::::::::::::

	private int xValue;

	public int getXValue() {
		return this.xValue;
	}

	public void setXValue(final int xValue) {
		final int maximumValue = this.getMaximumXValue();
		PrimitiveHelper.checkBetween("parameter:xValue", xValue, 0, maximumValue + 1);

		if (this.isAttached()) {
			if (this.isAttached()) {
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						HorizontalVerticalSlider.this.setXValue0();
					}
				});
			}
		}
		this.xValue = xValue;

		this.getEventListenerDispatcher().getChangeEventListeners().fireChange(this);
	}

	protected void setXValue0() {
		final Widget handle = this.getHandle();
		final int sliderLength = this.getOffsetWidth() - handle.getOffsetWidth();
		final int newLeft = this.getXValue() * sliderLength / this.getMaximumXValue();

		final Element element = handle.getElement();
		InlineStyle.setString(element, StyleConstants.POSITION, "absolute");
		InlineStyle.setInteger(element, StyleConstants.LEFT, newLeft, CssUnit.PX);
	}

	/**
	 * The maximum xValue of the slider. The minimum xValue is defaulted to 0.
	 * Clients must adjust this xValue if they wish to use a different range of
	 * values.
	 */
	private int maximumXValue;

	public int getMaximumXValue() {
		PrimitiveHelper.checkGreaterThanOrEqual("field:maximumXValue", 0, maximumXValue );
		return this.maximumXValue;
	}

	public void setMaximumXValue(final int maximumXValue) {
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:maximumXValue", 0, maximumXValue );
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

		if (this.isAttached()) {
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					HorizontalVerticalSlider.this.setYValue0();
				}
			});
		}
		this.yValue = yValue;
		this.getEventListenerDispatcher().getChangeEventListeners().fireChange(this);
	}

	protected void setYValue0() {
		final Widget handle = this.getHandle();
		final int sliderLength = this.getOffsetHeight() - handle.getOffsetHeight();
		final int newTop = this.getYValue() * sliderLength / this.getMaximumYValue();

		final Element element = handle.getElement();
		InlineStyle.setString(element, StyleConstants.POSITION, "absolute");
		InlineStyle.setInteger(element, StyleConstants.TOP, newTop, CssUnit.PX);
	}

	/**
	 * The maximum xValue of the slider. The minimum xValue is defaulted to 0.
	 * Clients must adjust this xValue if they wish to use a different range of
	 * values.
	 */
	private int maximumYValue;

	public int getMaximumYValue() {
		PrimitiveHelper.checkGreaterThanOrEqual("field:maximumYValue", 0, this.maximumYValue );
		return this.maximumYValue;
	}

	public void setMaximumYValue(final int maximumYValue) {
		PrimitiveHelper.checkGreaterThanOrEqual("parameter:maximumYValue", 0, maximumYValue );
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
		PrimitiveHelper.checkGreaterThan("parameter:deltaY", 0, yDelta );
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
		return super.toString() + ", xValue: " + xValue + ", yValue: " + yValue;
	}
}
