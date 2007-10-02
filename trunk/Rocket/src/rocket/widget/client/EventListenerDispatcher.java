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
package rocket.widget.client;

import rocket.event.client.BlurEvent;
import rocket.event.client.ChangeEvent;
import rocket.event.client.ChangeEventListener;
import rocket.event.client.ChangeEventListenerCollection;
import rocket.event.client.Event;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.EventListener;
import rocket.event.client.FocusEvent;
import rocket.event.client.FocusEventListener;
import rocket.event.client.FocusEventListenerCollection;
import rocket.event.client.ImageLoadEventListener;
import rocket.event.client.ImageLoadEventListenerCollection;
import rocket.event.client.ImageLoadFailedEvent;
import rocket.event.client.ImageLoadSuccessEvent;
import rocket.event.client.KeyDownEvent;
import rocket.event.client.KeyEventListener;
import rocket.event.client.KeyEventListenerCollection;
import rocket.event.client.KeyPressEvent;
import rocket.event.client.KeyUpEvent;
import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseDoubleClickEvent;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseEventListener;
import rocket.event.client.MouseEventListenerCollection;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseOverEvent;
import rocket.event.client.MouseUpEvent;
import rocket.event.client.MouseWheelEvent;
import rocket.event.client.ScrollEvent;
import rocket.event.client.ScrollEventListener;
import rocket.event.client.ScrollEventListenerCollection;
import rocket.util.client.ObjectHelper;

/**
 * This class contains all listener types and collections. It also contains
 * logic to dispatch an event to the registered listeners. Widget, CompositeWidget and
 * Panel's simply need to expose the listeners they sink add/removeXXXListener
 * methods and have them to delegate to an instance of this class.
 * 
 * The enclosing widget, composite or panel will still need to create and then
 * set the listener collections via {@link #prepareListenerCollections(int)} for
 * the events it sinks.
 * 
 * @author Miroslav Pokorny
 */
public class EventListenerDispatcher implements EventListener {

	/**
	 * This constructor should be used when creating a new widget from scratch
	 */
	public EventListenerDispatcher() {
	}

	/**
	 * Convenience method that automates the process of creating listener
	 * collections.
	 * 
	 * @param bitMask
	 *            Typically this matches the bit mask for the events sunk for a
	 *            particular widget/element.
	 */
	public void prepareListenerCollections(final int bitMask) {
		if ((bitMask & EventBitMaskConstants.FOCUS_EVENTS) != 0) {
			this.setFocusEventListeners(this.createFocusEventListeners());
		}
		if ((bitMask & EventBitMaskConstants.IMAGE_EVENTS) != 0) {
			this.setImageLoadEventListeners(this.createImageLoadEventListeners());
		}
		if ((bitMask & EventBitMaskConstants.KEY_EVENTS) != 0) {
			this.setKeyEventListeners(this.createKeyEventListeners());
		}
		if ((bitMask & EventBitMaskConstants.MOUSE_EVENTS) != 0) {
			this.setMouseEventListeners(this.createMouseEventListeners());
		}
		if ((bitMask & EventBitMaskConstants.CHANGE) != 0) {
			this.setChangeEventListeners(this.createChangeEventListeners());
		}
		if ((bitMask & EventBitMaskConstants.SCROLL) != 0) {
			this.setScrollEventListeners(this.createScrollEventListeners());
		}
	}

	/**
	 * Dispatches the and fires the appropriate listeners based on the event
	 * type
	 * 
	 * @param event
	 *            The event to be fired.
	 */

	public void onBrowserEvent(final Event event) {
		while (true) {
			final int eventType = event.getBitMask();
			if (eventType == EventBitMaskConstants.MOUSE_CLICK) {
				if (this.hasMouseEventListeners()) {
					this.getMouseEventListeners().fireClick((MouseClickEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.MOUSE_DOUBLE_CLICK) {
				if (this.hasMouseEventListeners()) {
					this.getMouseEventListeners().fireDoubleClick((MouseDoubleClickEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.MOUSE_DOWN) {
				if (this.hasMouseEventListeners()) {
					this.getMouseEventListeners().fireMouseDown((MouseDownEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.MOUSE_MOVE) {
				if (this.hasMouseEventListeners()) {
					this.getMouseEventListeners().fireMouseMove((MouseMoveEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.MOUSE_OUT) {
				if (this.hasMouseEventListeners()) {
					this.getMouseEventListeners().fireMouseOut((MouseOutEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.MOUSE_OVER) {
				if (this.hasMouseEventListeners()) {
					this.getMouseEventListeners().fireMouseOver((MouseOverEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.MOUSE_UP) {
				if (this.hasMouseEventListeners()) {
					this.getMouseEventListeners().fireMouseUp((MouseUpEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.MOUSE_WHEEL) {
				if (this.hasMouseEventListeners()) {
					this.getMouseEventListeners().fireMouseWheel((MouseWheelEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.KEY_DOWN) {
				if (this.hasKeyEventListeners()) {
					this.getKeyEventListeners().fireKeyDown((KeyDownEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.KEY_PRESS) {
				if (this.hasKeyEventListeners()) {
					this.getKeyEventListeners().fireKeyPress((KeyPressEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.KEY_UP) {
				if (this.hasKeyEventListeners()) {
					this.getKeyEventListeners().fireKeyUp((KeyUpEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.BLUR) {
				if (this.hasFocusEventListeners()) {
					this.getFocusEventListeners().fireBlur((BlurEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.FOCUS) {
				if (this.hasFocusEventListeners()) {
					this.getFocusEventListeners().fireFocus((FocusEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.CHANGE) {
				if (this.hasChangeEventListeners()) {
					this.getChangeEventListeners().fireChange((ChangeEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.SCROLL) {
				if (this.hasScrollEventListeners()) {
					this.getScrollEventListeners().fireScroll((ScrollEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.IMAGE_LOAD_SUCCESSFUL) {
				if (this.hasImageLoadEventListeners()) {
					this.getImageLoadEventListeners().fireImageLoadCompleted((ImageLoadSuccessEvent) event);
					break;
				}
			}
			if (eventType == EventBitMaskConstants.IMAGE_LOAD_FAILED) {
				if (this.hasImageLoadEventListeners()) {
					this.getImageLoadEventListeners().fireImageLoadFailed((ImageLoadFailedEvent) event);
					break;
				}
			}
			break;
		}
	}

	/**
	 * A collection of change listeners interested in change events for this
	 * widget.
	 */
	private ChangeEventListenerCollection changeEventListeners;

	public ChangeEventListenerCollection getChangeEventListeners() {
		ObjectHelper.checkNotNull("field:changeEventListeners", changeEventListeners);
		return this.changeEventListeners;
	}

	protected boolean hasChangeEventListeners() {
		return null != this.changeEventListeners;
	}

	protected void setChangeEventListeners(final ChangeEventListenerCollection changeEventListeners) {
		ObjectHelper.checkNotNull("parameter:changeEventListeners", changeEventListeners);
		this.changeEventListeners = changeEventListeners;
	}

	protected ChangeEventListenerCollection createChangeEventListeners() {
		return new ChangeEventListenerCollection();
	}

	public void addChangeEventListener(final ChangeEventListener changeListener) {
		this.getChangeEventListeners().add(changeListener);
	}

	public boolean removeChangeEventListener(final ChangeEventListener changeListener) {
		return this.getChangeEventListeners().remove(changeListener);
	}

	/**
	 * A collection of focus listeners interested in focus events for this
	 * widget.
	 */
	private FocusEventListenerCollection focusEventListeners;

	protected FocusEventListenerCollection getFocusEventListeners() {
		ObjectHelper.checkNotNull("field:focusEventListeners", focusEventListeners);
		return this.focusEventListeners;
	}

	protected boolean hasFocusEventListeners() {
		return null != this.focusEventListeners;
	}

	protected void setFocusEventListeners(final FocusEventListenerCollection focusEventListeners) {
		ObjectHelper.checkNotNull("parameter:focusEventListeners", focusEventListeners);
		this.focusEventListeners = focusEventListeners;
	}

	protected FocusEventListenerCollection createFocusEventListeners() {
		return new FocusEventListenerCollection();
	}

	public void addFocusEventListener(final FocusEventListener focusEventListener) {
		this.getFocusEventListeners().add(focusEventListener);
	}

	public boolean removeFocusEventListener(final FocusEventListener focusEventListener) {
		return this.getFocusEventListeners().remove(focusEventListener);
	}

	/**
	 * A collection of key listeners interested in key events for this widget.
	 */
	private KeyEventListenerCollection keyEventListeners;

	protected KeyEventListenerCollection getKeyEventListeners() {
		ObjectHelper.checkNotNull("field:keyEventListeners", keyEventListeners);
		return this.keyEventListeners;
	}

	protected boolean hasKeyEventListeners() {
		return null != this.keyEventListeners;
	}

	protected void setKeyEventListeners(final KeyEventListenerCollection keyEventListeners) {
		ObjectHelper.checkNotNull("parameter:keyEventListeners", keyEventListeners);
		this.keyEventListeners = keyEventListeners;
	}

	protected KeyEventListenerCollection createKeyEventListeners() {
		return new KeyEventListenerCollection();
	}

	public void addKeyEventListener(final KeyEventListener keyEventListener) {
		this.getKeyEventListeners().add(keyEventListener);
	}

	public boolean removeKeyEventListener(final KeyEventListener keyEventListener) {
		return this.getKeyEventListeners().remove(keyEventListener);
	}

	/**
	 * A collection of mouse listeners interested in mouse events for this
	 * widget.
	 */
	private MouseEventListenerCollection mouseEventListeners;

	protected MouseEventListenerCollection getMouseEventListeners() {
		ObjectHelper.checkNotNull("field:mouseEventListeners", mouseEventListeners);
		return this.mouseEventListeners;
	}

	protected boolean hasMouseEventListeners() {
		return null != this.mouseEventListeners;
	}

	protected void setMouseEventListeners(final MouseEventListenerCollection mouseEventListeners) {
		ObjectHelper.checkNotNull("parameter:mouseEventListeners", mouseEventListeners);
		this.mouseEventListeners = mouseEventListeners;
	}

	protected MouseEventListenerCollection createMouseEventListeners() {
		return new MouseEventListenerCollection();
	}

	public void addMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getMouseEventListeners().add(mouseEventListener);
	}

	public boolean removeMouseEventListener(final MouseEventListener mouseEventListener) {
		return this.getMouseEventListeners().remove(mouseEventListener);
	}

	/**
	 * A collection of scroll listeners interested in scroll events for this
	 * widget.
	 */
	private ScrollEventListenerCollection scrollEventListeners;

	protected ScrollEventListenerCollection getScrollEventListeners() {
		ObjectHelper.checkNotNull("field:scrollEventListeners", scrollEventListeners);
		return this.scrollEventListeners;
	}

	protected boolean hasScrollEventListeners() {
		return null != this.scrollEventListeners;
	}

	protected void setScrollEventListeners(final ScrollEventListenerCollection scrollEventListeners) {
		ObjectHelper.checkNotNull("parameter:scrollEventListeners", scrollEventListeners);
		this.scrollEventListeners = scrollEventListeners;
	}

	protected ScrollEventListenerCollection createScrollEventListeners() {
		return new ScrollEventListenerCollection();
	}

	public void addScrollEventListener(final ScrollEventListener scrollEventListener) {
		this.getScrollEventListeners().add(scrollEventListener);
	}

	public void removeScrollEventListener(final ScrollEventListener scrollEventListener) {
		this.getScrollEventListeners().remove(scrollEventListener);
	}

	/**
	 * A collection of ImageLoadEvent listeners interested in image load events
	 * for this widget.
	 */
	private ImageLoadEventListenerCollection imageLoadEventListeners;

	protected ImageLoadEventListenerCollection getImageLoadEventListeners() {
		ObjectHelper.checkNotNull("field:imageLoadEventListeners", imageLoadEventListeners);
		return this.imageLoadEventListeners;
	}

	protected boolean hasImageLoadEventListeners() {
		return null != this.imageLoadEventListeners;
	}

	protected void setImageLoadEventListeners(final ImageLoadEventListenerCollection imageLoadEventListeners) {
		ObjectHelper.checkNotNull("parameter:imageLoadEventListeners", imageLoadEventListeners);
		this.imageLoadEventListeners = imageLoadEventListeners;
	}

	protected ImageLoadEventListenerCollection createImageLoadEventListeners() {
		return new ImageLoadEventListenerCollection();
	}

	public void addImageLoadEventListener(final ImageLoadEventListener imageLoadEventListener) {
		this.getImageLoadEventListeners().add(imageLoadEventListener);
	}

	public boolean removeImageLoadEventListener(final ImageLoadEventListener imageLoadEventListener) {
		return this.getImageLoadEventListeners().remove(imageLoadEventListener);
	}
}
