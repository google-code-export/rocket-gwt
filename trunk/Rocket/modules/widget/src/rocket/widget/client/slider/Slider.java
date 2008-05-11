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

import rocket.event.client.ChangeEventListener;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.EventPreviewAdapter;
import rocket.event.client.FocusEventListener;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseUpEvent;
import rocket.selection.client.Selection;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;
import rocket.widget.client.CompositeWidget;
import rocket.widget.client.Html;
import rocket.widget.client.Panel;
import rocket.widget.client.Widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

/**
 * Common base class for both the Horizontal and Vertical Slider widgets.
 * 
 * It contains both common properties and behaviour with sub-classes required to
 * implement a bare minimum.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class Slider extends CompositeWidget {

	/**
	 * Sub classes must return the significant mouse coordinate.
	 * 
	 * @param event
	 * @return
	 */
	abstract protected int getMousePageCoordinate(final MouseEvent event);

	/**
	 * Sub classes must return the significant widget coordinate
	 * 
	 * @return
	 */
	abstract protected int getAbsoluteWidgetCoordinate();

	/**
	 * Sub classes must return the name of the style property coordinate
	 * 
	 * @return Css.LEFT or Css.TOP
	 */
	abstract protected String getHandleCoordinateStylePropertyName();

	/**
	 * Sub classes must return the slider widget length of the significant
	 * dimension
	 * 
	 * @return
	 */
	abstract protected int getSliderLength();

	/**
	 * Sub classes must return the length of the handle of the significant
	 * dimension
	 * 
	 * @return
	 */
	abstract protected int getHandleLength();

	protected Widget createWidget() {
		return this.createPanel();
	}

	protected Panel getPanel() {
		return (Panel) this.getWidget();
	}

	protected Panel createPanel() {
		return new InternalSliderPanel() {
			protected String getBackgroundStyleName() {
				return Slider.this.getBackgroundStyleName();
			}

			protected String getHandleStyleName() {
				return Slider.this.getHandleStyleName();
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
		return Widgets.createHtml();
	}

	abstract protected String getHandleStyleName();

	public Widget getBackground() {
		return this.getPanel().get(Constants.BACKGROUND_WIDGET_INDEX);
	}

	public void setBackground(final Widget background) {
		final Panel panel = this.getPanel();
		panel.remove(Constants.BACKGROUND_WIDGET_INDEX);
		panel.insert(background, Constants.BACKGROUND_WIDGET_INDEX);
	}

	protected Widget createBackground() {
		return Widgets.createHtml();
	}

	abstract protected String getBackgroundStyleName();

	protected void afterCreateWidget() {
		super.afterCreateWidget();

		this.getEventListenerDispatcher().addMouseEventListener(
				new MouseEventAdapter() {
					public void onMouseDown(final MouseDownEvent event) {
						Slider.this.onMouseDown(event);
					}
				});

		// now add the handle and background widgets..
		this.setHandle(this.createHandle());
		this.setBackground(this.createBackground());
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS
				| EventBitMaskConstants.CHANGE
				| EventBitMaskConstants.MOUSE_DOWN
				| EventBitMaskConstants.MOUSE_UP
				| EventBitMaskConstants.MOUSE_OUT
				| EventBitMaskConstants.MOUSE_MOVE;
	}

	public void onAttach() {
		final int value = this.getValue();
		super.onAttach();
		this.setValue(value);
	}

	// EVENT HANDLING
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * Dispatches the event to the respective handler depending on whether the
	 * handle or the slider background was clicked.
	 * 
	 * @param event
	 */
	protected void onMouseDown(final MouseDownEvent event) {
		Checker.notNull("parameter:event", event);

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

			// unknown mouseTarget do nothing...
			break;
		}
	}

	/**
	 * Initiates the dragging of the handle until it is released.
	 * 
	 * @param event
	 */
	protected void onHandleMouseDown(final MouseDownEvent event) {
		Checker.notNull("parameter:event", event);

		if (false == this.hasDraggingEventPreview()) {
			Selection.clearAnySelectedText();
			Selection.disableTextSelection();

			final EventPreview eventPreview = this.createDraggingEventPreview();
			this.setDraggingEventPreview(eventPreview);
			DOM.addEventPreview(eventPreview);

			this.getHandle().addStyleName(this.getSliderDraggingStyleName());
		}
	}

	/**
	 * The EventPreview object that is following the handle whilst it is being
	 * dragged.
	 */
	private EventPreview draggingEventPreview;

	protected EventPreview getDraggingEventPreview() {
		Checker.notNull("field:draggingEventPreview",
				draggingEventPreview);
		return this.draggingEventPreview;
	}

	protected boolean hasDraggingEventPreview() {
		return null != this.draggingEventPreview;
	}

	protected void setDraggingEventPreview(
			final EventPreview draggingEventPreview) {
		Checker.notNull("parameter:draggingEventPreview",
				draggingEventPreview);
		this.draggingEventPreview = draggingEventPreview;
	}

	protected void clearDraggingEventPreview() {
		this.draggingEventPreview = null;
	}

	/**
	 * Creates an EventPreview that simple delegates to appropriately called
	 * handler event methods on the outter class.
	 * 
	 * @return
	 */
	protected EventPreview createDraggingEventPreview() {
		return new EventPreviewAdapter() {

			public void onMouseMove(final MouseMoveEvent event) {
				Slider.this.onMouseMove(event);
			}

			public void onMouseUp(final MouseUpEvent event) {
				Slider.this.onHandleMouseUp(event);
			}
		};
	}

	protected void onMouseMove(final MouseMoveEvent event) {
		final int range = this.getSliderLength() - this.getHandleLength();

		int value = this.getMousePageCoordinate(event)
				- this.getAbsoluteWidgetCoordinate();
		if (value < 0) {
			value = 0;
		}
		if (value > range) {
			value = range;
		}

		final float newValue = (float) value / range * this.getMaximumValue()
				+ 0.5f;
		this.setValue((int) newValue);

		this.clearTimer();

		event.cancelBubble(true);
		event.stop(); // stops text selection in Opera.
	}

	protected void onBackgroundMouseDown(final MouseDownEvent event) {
		final int mouse = this.getMousePageCoordinate(event)
				- this.getAbsoluteWidgetCoordinate() - this.getHandleLength()
				/ 2;
		final HandleSlidingTimer timer = this.getTimer();
		timer.setMouse(mouse);

		onBackgroundMouseDown(mouse);
	}

	protected void onBackgroundMouseDown(final int mouse) {
		while (true) {
			final int handleBeforeUpdate = this.getRelativeHandleCoordinate();
			//final int valueBefore = this.getValue();

			if (mouse == handleBeforeUpdate) {
				this.clearTimer();
				break;
			}
			final boolean handleBeforeMouseBefore = handleBeforeUpdate <= mouse;
			if (handleBeforeMouseBefore) {
				this.onAfterHandleMouseDown();
			} else {
				this.onBeforeHandleMouseDown();
			}
			final int handleAfterUpdate = this.getRelativeHandleCoordinate();
			//final int valueAfter = this.getValue();

			final boolean handleBeforeMouseAfter = handleAfterUpdate <= mouse;
			if (handleBeforeMouseBefore != handleBeforeMouseAfter) {
				this.setRelativeHandleCoordinate(mouse);
				this.clearTimer();
			}
			break;
		}
	}

	/**
	 * Decreases the value of this slider ensuring that it does not underflow
	 * the minimum value of this slider.
	 */
	protected void onBeforeHandleMouseDown() {
		final int newValue = Math.max(0, this.getValue() - this.getDelta());
		final int coordinate = this.getRelativeHandleCoordinate() - 1;
		this.setValue(newValue);
		this.setRelativeHandleCoordinate(coordinate);
	}

	/**
	 * Increases the value of this slider ensuring that it does not exceed the
	 * maximum value of this slider.
	 */
	protected void onAfterHandleMouseDown() {
		final int newValue = Math.min(this.getValue() + this.getDelta(), this
				.getMaximumValue());
		final int coordinate = this.getRelativeHandleCoordinate() + 1;
		this.setValue(newValue);
		this.setRelativeHandleCoordinate(coordinate);
	}

	/**
	 * This method is called when the mouseTarget button is let go whilst
	 * dragging the slider handle.
	 * 
	 * @param event
	 */
	protected void onHandleMouseUp(final MouseUpEvent event) {
		this.getHandle().removeStyleName(this.getSliderDraggingStyleName());

		DOM.removeEventPreview(this.getDraggingEventPreview());
		this.clearDraggingEventPreview();
		Selection.enableTextSelection();

		event.cancelBubble(true);
	}

	/**
	 * Sub-classes need to return the style that is added to the handle widget
	 * when it is being dragged or removed when the dragging is stopped.
	 * 
	 * @return
	 */
	protected abstract String getSliderDraggingStyleName();

	/**
	 * A timer is used to simulate multiple clicks when holding down the
	 * mouseTarget button
	 */
	private HandleSlidingTimer timer;

	protected HandleSlidingTimer getTimer() {
		if (false == this.hasTimer()) {
			final HandleSlidingTimer timer = createTimer();
			timer.scheduleRepeating(this.getMouseDownRepeatRate());
			this.setTimer(timer);
		}

		Checker.notNull("field:timer", timer);
		return timer;
	}

	protected boolean hasTimer() {
		return null != this.timer;
	}

	protected void setTimer(final HandleSlidingTimer timer) {
		Checker.notNull("parameter:timer", timer);
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

	protected HandleSlidingTimer createTimer() {
		return new HandleSlidingTimer();
	}

	protected class HandleSlidingTimer extends Timer {

		public void run() {
			Slider.this.onBackgroundMouseDown(this.getMouse());
		}

		private int mouse;

		int getMouse() {
			return this.mouse;
		}

		void setMouse(final int target) {
			this.mouse = target;
		}
	}

	/**
	 * This value in milliseconds controls the repetition of mouseTarget down
	 * events within the background area of the slider.
	 * 
	 * Smaller values result in a faster glide of the handle towards the mouse,
	 * whilst larger values result in a slow movement.
	 */
	private int mouseDownRepeatRate;

	public int getMouseDownRepeatRate() {
		Checker.greaterThan("field:mouseDownRepeatRate", 0,
				mouseDownRepeatRate);
		return this.mouseDownRepeatRate;
	}

	public void setMouseDownRepeatRate(final int mouseDownRepeatRate) {
		Checker.greaterThan("parameter:mouseDownRepeatRate", 0,
				mouseDownRepeatRate);
		this.mouseDownRepeatRate = mouseDownRepeatRate;
	}

	// SLIDER ::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * When attached the handle coordinates within the slider is used to
	 * calculate the actual value of the slider. This field is constantly
	 * updated and is used when the widget is not attached.
	 */
	private int value;

	public int getValue() {
		return this.value;
	}

	public void setValue(final int value) {
		final int maximumValue = this.getMaximumValue();
		Checker.between("parameter:value", value, 0,
				maximumValue + 1);

		this.value = value;

		final int sliderLength = this.getSliderLength()
				- this.getHandleLength();
		final int coordinate = Math.round(1.0f * this.getValue() * sliderLength
				/ this.getMaximumValue());
		this.setRelativeHandleCoordinate(coordinate);

		this.getEventListenerDispatcher().getChangeEventListeners().fireChange(
				this);
	}

	/**
	 * The maximum value of the slider. The minimum value is defaulted to 0.
	 * Clients must adjust this value if they wish to use a different range of
	 * values.
	 */
	private int maximumValue;

	public int getMaximumValue() {
		Checker.greaterThan("field:maximumValue", 0, maximumValue);
		return this.maximumValue;
	}

	public void setMaximumValue(final int maximumValue) {
		Checker.greaterThan("parameter:maximumValue", 0,
				maximumValue);
		this.maximumValue = maximumValue;
	}

	/**
	 * The amount the value jumps. The value must be 1 or more.
	 */
	private int delta;

	public int getDelta() {
		Checker.greaterThan("field:delta", 0, delta);
		return this.delta;
	}

	public void setDelta(final int delta) {
		Checker.greaterThan("parameter:delta", 0, delta);
		this.delta = delta;
	}

	public void addChangeEventListener(
			final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().addChangeEventListener(
				changeEventListener);
	}

	public void removeChangeEventListener(
			final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().removeChangeEventListener(
				changeEventListener);
	}

	public void addFocusEventListener(
			final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().addFocusEventListener(
				focusEventListener);
	}

	public void removeFocusEventListener(
			final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().removeFocusEventListener(
				focusEventListener);
	}

	protected int getRelativeHandleCoordinate() {
		final Element element = this.getHandle().getElement();
		return InlineStyle.getInteger(element, this
				.getHandleCoordinateStylePropertyName(), CssUnit.PX, 0);
	}

	protected void setRelativeHandleCoordinate(final int coordinate) {
		final Element element = this.getHandle().getElement();
		InlineStyle.setString(element, Css.POSITION, "absolute");
		InlineStyle.setString(element, Css.LEFT, "0px");
		InlineStyle.setString(element, Css.TOP, "0px");
		InlineStyle
				.setInteger(element, this
						.getHandleCoordinateStylePropertyName(), coordinate,
						CssUnit.PX);
	}

	public String toString() {
		return super.toString() + ", value: " + this.value;
	}
}
