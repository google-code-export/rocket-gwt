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
	 * This method is invoked before dispatching the event to the appropriate method. 
	 * @param event
	 */
	protected void beforeDispatching( final Event event ){		
	}
	
	protected void dispatch( final Event event ){
		while (true) {

			
			if( event.isCancelled() ){
				break;
			}
			
			final MouseClickEvent mouseClickEvent = event.asMouseClickEvent();
			if (mouseClickEvent != null) {
				this.handleMouseClickEvent(mouseClickEvent);
				break;
			}
			final MouseDoubleClickEvent mouseDoubleClickEvent = event.asMouseDoubleClickEvent();
			if (mouseDoubleClickEvent != null) {
				this.handleMouseDoubleClickEvent(mouseDoubleClickEvent);
				break;
			}

			final MouseDownEvent mouseDownEvent = event.asMouseDownEvent();
			if (mouseDownEvent != null) {
				this.handleMouseDownEvent(mouseDownEvent);
				break;
			}
			final MouseMoveEvent mouseMoveEvent = event.asMouseMoveEvent();
			if (mouseMoveEvent != null) {
				this.handleMouseMoveEvent(mouseMoveEvent);
				break;
			}
			final MouseOutEvent mouseOutEvent = event.asMouseOutEvent();
			if (mouseOutEvent != null) {
				this.handleMouseOutEvent(mouseOutEvent);
				break;
			}
			final MouseOverEvent mouseOverEvent = event.asMouseOverEvent();
			if (mouseOverEvent != null) {
				this.handleMouseOverEvent(mouseOverEvent);
				break;
			}
			final MouseUpEvent mouseUpEvent = event.asMouseUpEvent();
			if (mouseUpEvent != null) {
				this.handleMouseUpEvent(mouseUpEvent);
				break;
			}
			final MouseWheelEvent mouseWheelEvent = event.asMouseWheelEvent();
			if (mouseWheelEvent != null) {
				this.handleMouseWheelEvent(mouseWheelEvent);
				break;
			}

			final KeyDownEvent keyDownEvent = event.asKeyDownEvent();
			if (keyDownEvent != null) {
				this.handleKeyDownEvent(keyDownEvent);
				break;
			}
			final KeyPressEvent keyPressEvent = event.asKeyPressEvent();
			if (keyPressEvent != null) {
				this.handleKeyPressEvent(keyPressEvent);
				break;
			}
			final KeyUpEvent keyUpEvent = event.asKeyUpEvent();
			if (keyUpEvent != null) {
				this.handleKeyUpEvent(keyUpEvent);
				break;
			}

			final FocusEvent focusEvent = event.asFocusEvent();
			if (focusEvent != null) {
				this.handleFocusEvent(focusEvent);
				break;
			}

			final BlurEvent blurEvent = event.asBlurEvent();
			if (blurEvent != null) {
				this.handleBlurEvent(blurEvent);
				break;
			}

			final ChangeEvent changeEvent = event.asChangeEvent();
			if (changeEvent != null) {
				this.handleChangeEvent(changeEvent);
				break;
			}

			final ScrollEvent scrollEvent = event.asScrollEvent();
			if (scrollEvent != null) {
				this.handleScrollEvent(scrollEvent);
				break;
			}
			final ImageLoadSuccessEvent imageLoadSuccessEvent = event.asImageLoadSuccessEvent();
			if (imageLoadSuccessEvent != null) {
				this.handleImageLoadSuccessEvent(imageLoadSuccessEvent);
				break;
			}
			final ImageLoadFailedEvent imageLoadFailedEvent = event.asImageLoadFailedEvent();
			if (imageLoadFailedEvent != null) {
				this.handleImageLoadFailedEvent(imageLoadFailedEvent);
				break;
			}

			break;
		}
		
	}
	
	protected void handleMouseClickEvent(final MouseClickEvent event) {
	}

	protected void handleMouseDoubleClickEvent(final MouseDoubleClickEvent event) {
	}

	protected void handleMouseDownEvent(final MouseDownEvent event) {
	}

	protected void handleMouseMoveEvent(final MouseMoveEvent event) {
	}

	protected void handleMouseOutEvent(final MouseOutEvent event) {
	}

	protected void handleMouseOverEvent(final MouseOverEvent event) {
	}

	protected void handleMouseUpEvent(final MouseUpEvent event) {
	}

	protected void handleMouseWheelEvent(final MouseWheelEvent event) {
	}

	protected void handleKeyDownEvent(final KeyDownEvent event) {
	}

	protected void handleKeyPressEvent(final KeyPressEvent event) {
	}

	protected void handleKeyUpEvent(final KeyUpEvent event) {
	}

	protected void handleImageLoadSuccessEvent(final ImageLoadSuccessEvent event) {
	}

	protected void handleImageLoadFailedEvent(final ImageLoadFailedEvent event) {
	}

	protected void handleFocusEvent(final FocusEvent event) {
	}

	protected void handleBlurEvent(final BlurEvent event) {
	}

	protected void handleChangeEvent(final ChangeEvent event) {
	}

	protected void handleScrollEvent(final ScrollEvent event) {
	}
	
	/**
	 * This method is invoked after the event is dispatched to one of the overloaded protected methods even if the event is cancelled.
	 * @param event
	 */
	protected void afterDispatching( final Event event ){		
	}	
}
