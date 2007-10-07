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

abstract class EventDispatcher {

	/**
	 * This method is invoked before dispatching the event to the appropriate
	 * method.
	 * 
	 * @param event
	 */
	protected void beforeDispatching(final Event event) {
	}

	protected void dispatch(final Event event) {
		while (true) {

			if (event.isCancelled()) {
				break;
			}

			final MouseClickEvent mouseClickEvent = event.asMouseClickEvent();
			if (mouseClickEvent != null) {
				this.onMouseClick(mouseClickEvent);
				break;
			}
			final MouseDoubleClickEvent mouseDoubleClickEvent = event.asMouseDoubleClickEvent();
			if (mouseDoubleClickEvent != null) {
				this.onMouseDoubleClick(mouseDoubleClickEvent);
				break;
			}

			final MouseDownEvent mouseDownEvent = event.asMouseDownEvent();
			if (mouseDownEvent != null) {
				this.onMouseDown(mouseDownEvent);
				break;
			}
			final MouseMoveEvent mouseMoveEvent = event.asMouseMoveEvent();
			if (mouseMoveEvent != null) {
				this.onMouseMove(mouseMoveEvent);
				break;
			}
			final MouseOutEvent mouseOutEvent = event.asMouseOutEvent();
			if (mouseOutEvent != null) {
				this.onMouseOut(mouseOutEvent);
				break;
			}
			final MouseOverEvent mouseOverEvent = event.asMouseOverEvent();
			if (mouseOverEvent != null) {
				this.onMouseOver(mouseOverEvent);
				break;
			}
			final MouseUpEvent mouseUpEvent = event.asMouseUpEvent();
			if (mouseUpEvent != null) {
				this.onMouseUp(mouseUpEvent);
				break;
			}
			final MouseWheelEvent mouseWheelEvent = event.asMouseWheelEvent();
			if (mouseWheelEvent != null) {
				this.onMouseWheel(mouseWheelEvent);
				break;
			}

			final KeyDownEvent keyDownEvent = event.asKeyDownEvent();
			if (keyDownEvent != null) {
				this.onKeyDown(keyDownEvent);
				break;
			}
			final KeyPressEvent keyPressEvent = event.asKeyPressEvent();
			if (keyPressEvent != null) {
				this.onKeyPress(keyPressEvent);
				break;
			}
			final KeyUpEvent keyUpEvent = event.asKeyUpEvent();
			if (keyUpEvent != null) {
				this.onKeyUp(keyUpEvent);
				break;
			}

			final FocusEvent focusEvent = event.asFocusEvent();
			if (focusEvent != null) {
				this.onFocus(focusEvent);
				break;
			}

			final BlurEvent blurEvent = event.asBlurEvent();
			if (blurEvent != null) {
				this.onBlur(blurEvent);
				break;
			}

			final ChangeEvent changeEvent = event.asChangeEvent();
			if (changeEvent != null) {
				this.onChange(changeEvent);
				break;
			}

			final ScrollEvent scrollEvent = event.asScrollEvent();
			if (scrollEvent != null) {
				this.onScroll(scrollEvent);
				break;
			}
			final ImageLoadSuccessEvent imageLoadSuccessEvent = event.asImageLoadSuccessEvent();
			if (imageLoadSuccessEvent != null) {
				this.onImageLoadSuccess(imageLoadSuccessEvent);
				break;
			}
			final ImageLoadFailedEvent imageLoadFailedEvent = event.asImageLoadFailedEvent();
			if (imageLoadFailedEvent != null) {
				this.onImageLoadFailed(imageLoadFailedEvent);
				break;
			}

			break;
		}

	}

	protected void onMouseClick(final MouseClickEvent event) {
	}

	protected void onMouseDoubleClick(final MouseDoubleClickEvent event) {
	}

	protected void onMouseDown(final MouseDownEvent event) {
	}

	protected void onMouseMove(final MouseMoveEvent event) {
	}

	protected void onMouseOut(final MouseOutEvent event) {
	}

	protected void onMouseOver(final MouseOverEvent event) {
	}

	protected void onMouseUp(final MouseUpEvent event) {
	}

	protected void onMouseWheel(final MouseWheelEvent event) {
	}

	protected void onKeyDown(final KeyDownEvent event) {
	}

	protected void onKeyPress(final KeyPressEvent event) {
	}

	protected void onKeyUp(final KeyUpEvent event) {
	}

	protected void onImageLoadSuccess(final ImageLoadSuccessEvent event) {
	}

	protected void onImageLoadFailed(final ImageLoadFailedEvent event) {
	}

	protected void onFocus(final FocusEvent event) {
	}

	protected void onBlur(final BlurEvent event) {
	}

	protected void onChange(final ChangeEvent event) {
	}

	protected void onScroll(final ScrollEvent event) {
	}

	/**
	 * This method is invoked after the event is dispatched to one of the
	 * overloaded protected methods even if the event is cancelled.
	 * 
	 * @param event
	 */
	protected void afterDispatching(final Event event) {
	}
}
