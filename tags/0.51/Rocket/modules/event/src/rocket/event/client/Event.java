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
package rocket.event.client;

import rocket.util.client.Checker;
import rocket.util.client.Destroyable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides an object oriented view of all browser events.
 * 
 * @author Miroslav Pokorny
 */
abstract public class Event implements Destroyable {

	/**
	 * Disables the browser's built in context menu.
	 */
	private static boolean disabled = false;

	/**
	 * This method includes a guard that ensures the context menu disabler is
	 * only installed once.
	 */
	static public void disableContextMenu() {
		if (false == disabled) {
			disableContextMenu0();
			disabled = true;

			GWT.log("Browser context menu's has been disabled for the entire window...", null);
		}
	}

	native static void disableContextMenu0()/*-{
		 var block = function( event ){
		 	return false;
		 };

		 var body = $doc.body;
		 if( body.attachEvent ){
		 	body.attachEvent( "oncontextmenu", block );
		 }else {
		 	body.addEventListener( "contextmenu", block, false );
		 }
		 body.oncontextmenu = block;
		 }-*/;

	static public Event getEvent(final Widget widget, final int bitMask) {
		Checker.notNull("parameter:widget", widget);

		final Event event = getEvent(bitMask);
		event.setTarget(widget.getElement());
		event.setWidget(widget);
		return event;
	}

	/**
	 * Factory method which creates a Event object which adds behaviour to the
	 * given raw GWT event object. Depending on the event type the appropriate
	 * Event sub type is created each exposing methods appropriate for that
	 * event.
	 * 
	 * @param rawEvent
	 *            The raw GWT event object.
	 * @return
	 */
	static public Event getEvent(final com.google.gwt.user.client.Event rawEvent) {
		Checker.notNull("parameter:rawEvent", rawEvent);

		final Event event = getEvent(rawEvent.getTypeInt());
		event.setEvent(rawEvent);
		return event;
	}

	/**
	 * Factory method which creates a Event object which adds behaviour to the
	 * given raw GWT event object. Depending on the event type the appropriate
	 * Event sub type is created each exposing methods appropriate for that
	 * event.
	 * 
	 * @param rawEvent
	 *            The raw GWT event object.
	 * @return
	 */
	static public Event getEvent(final int mask) {

		Event event = null;

		while (true) {
			if (EventBitMaskConstants.BLUR == mask) {
				event = new BlurEvent();
				break;
			}
			if (EventBitMaskConstants.FOCUS == mask) {
				event = new FocusEvent();
				break;
			}
			if (EventBitMaskConstants.KEY_DOWN == mask) {
				event = new KeyDownEvent();
				break;
			}
			if (EventBitMaskConstants.KEY_PRESS == mask) {
				event = new KeyPressEvent();
				break;
			}
			if (EventBitMaskConstants.KEY_UP == mask) {
				event = new KeyUpEvent();
				break;
			}
			if (EventBitMaskConstants.MOUSE_CLICK == mask) {
				event = new MouseClickEvent();
				break;
			}
			if (EventBitMaskConstants.MOUSE_DOUBLE_CLICK == mask) {
				event = new MouseDoubleClickEvent();
				break;
			}
			if (EventBitMaskConstants.MOUSE_DOWN == mask) {
				event = new MouseDownEvent();
				break;
			}
			if (EventBitMaskConstants.MOUSE_MOVE == mask) {
				event = new MouseMoveEvent();
				break;
			}
			if (EventBitMaskConstants.MOUSE_OUT == mask) {
				event = new MouseOutEvent();
				break;
			}
			if (EventBitMaskConstants.MOUSE_OVER == mask) {
				event = new MouseOverEvent();
				break;
			}
			if (EventBitMaskConstants.MOUSE_UP == mask) {
				event = new MouseUpEvent();
				break;
			}
			if (EventBitMaskConstants.MOUSE_WHEEL == mask) {
				event = new MouseWheelEvent();
				break;
			}
			if (EventBitMaskConstants.SCROLL == mask) {
				event = new ScrollEvent();
				break;
			}
			if (EventBitMaskConstants.IMAGE_LOAD_SUCCESSFUL == mask) {
				event = new ImageLoadSuccessEvent();
				break;
			}
			if (EventBitMaskConstants.IMAGE_LOAD_FAILED == mask) {
				event = new ImageLoadFailedEvent();
				break;
			}
			if (EventBitMaskConstants.CHANGE == mask) {
				event = new ChangeEvent();
				break;
			}
			throw new UnsupportedOperationException("Unknown event bitMask: 0x" + Integer.toHexString(mask));
		}

		return event;
	}

	protected Event() {
		super();
	}

	/**
	 * The GWT event being wrapped.
	 */
	private com.google.gwt.user.client.Event event;

	public com.google.gwt.user.client.Event getEvent() {
		Checker.notNull("field:event", event);
		return event;
	}

	private void setEvent(final com.google.gwt.user.client.Event event) {
		Checker.notNull("parameter:event", event);
		this.event = event;
	}

	private void clearEvent() {
		this.event = null;
	}

	/**
	 * Prevents the browser from completing the default action for this event.
	 */
	public void stop() {
		this.getEvent().preventDefault();
	}

	private boolean cancelled = false;

	/**
	 * Cancels event bubbling.
	 * 
	 * @param cancel
	 */
	public void cancelBubble(final boolean cancel) {
		DOM.eventCancelBubble(this.getEvent(), cancel);
		this.cancelled = this.cancelled | cancel;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * This field will only be set when synthetizing an event.
	 */
	private Element target;

	/**
	 * Returns the target element of this event.
	 * 
	 * @return
	 */
	public Element getTarget() {
		return this.hasTarget() ? this.target : (Element) this.getEvent().getTarget().cast();
	}

	protected boolean hasTarget() {
		return null != this.target;
	}

	public void setTarget(final Element target) {
		this.target = target;
	}

	/**
	 * The widget whose element was the target of this event.
	 */
	private Widget widget;

	public Widget getWidget() {
		Checker.notNull("field:widget", this.widget);
		return this.widget;
	}

	public boolean hasWidget() {
		return null != this.widget;
	}

	public void setWidget(final Widget widget) {
		Checker.notNull("parameter:widget", widget);
		this.widget = widget;
	}

	void clearWidget() {
		this.widget = null;
	}

	public BlurEvent asBlurEvent() {
		return null;
	}

	public FocusEvent asFocusEvent() {
		return null;
	}

	public KeyDownEvent asKeyDownEvent() {
		return null;
	}

	public KeyPressEvent asKeyPressEvent() {
		return null;
	}

	public KeyUpEvent asKeyUpEvent() {
		return null;
	}

	public ImageLoadFailedEvent asImageLoadFailedEvent() {
		return null;
	}

	public ImageLoadSuccessEvent asImageLoadSuccessEvent() {
		return null;
	}

	public MouseClickEvent asMouseClickEvent() {
		return null;
	}

	public MouseDoubleClickEvent asMouseDoubleClickEvent() {
		return null;
	}

	public MouseDownEvent asMouseDownEvent() {
		return null;
	}

	public MouseMoveEvent asMouseMoveEvent() {
		return null;
	}

	public MouseOutEvent asMouseOutEvent() {
		return null;
	}

	public MouseOverEvent asMouseOverEvent() {
		return null;
	}

	public MouseUpEvent asMouseUpEvent() {
		return null;
	}

	public MouseWheelEvent asMouseWheelEvent() {
		return null;
	}

	public ChangeEvent asChangeEvent() {
		return null;
	}

	public ScrollEvent asScrollEvent() {
		return null;
	}

	public String toString() {
		return this.getEvent().getType();
	}

	public void destroy() {
		this.clearEvent();
		this.clearWidget();
	}

	public int getBitMask() {
		return this.getEvent().getTypeInt();
	}
}
