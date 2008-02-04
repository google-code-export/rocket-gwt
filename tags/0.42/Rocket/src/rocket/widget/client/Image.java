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

import rocket.dom.client.Dom;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.FocusEventListener;
import rocket.event.client.ImageLoadEventListener;
import rocket.event.client.MouseEventListener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * An image widget which the same capabilities of GWT's image with the ability
 * to hijack existing DOM img elements.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * Image.
 * 
 * TODO When upgrading GWT version reapply changes to this class.
 * 
 * @author Miroslav Pokorny
 */
public class Image extends FocusWidget {

	/**
	 * Causes the browser to pre-fetch the image at a given URL.
	 * 
	 * @param url
	 *            the URL of the image to be prefetched
	 */
	public static void prefetch(final String url) {
		com.google.gwt.user.client.ui.Image.prefetch(url);
	}

	public Image() {
		super();
	}

	public Image(final String url) {
		this();

		this.setUrl(url);
	}

	public Image(final Element element) {
		super(element);
	}

	protected void checkElement(Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.IMAGE_TAG);
	}

	protected Element createElement() {
		return DOM.createImg();
	}

	protected void afterCreateElement() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);

		dispatcher.setFocusEventListeners(dispatcher.createFocusEventListeners());
		dispatcher.setImageLoadEventListeners(dispatcher.createImageLoadEventListeners());
		dispatcher.setMouseEventListeners(dispatcher.createMouseEventListeners());
	}

	protected String getInitialStyleName() {
		return WidgetConstants.IMAGE_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.MOUSE_EVENTS | EventBitMaskConstants.IMAGE_EVENTS;
	}

	public String getUrl() {
		return DOM.getImgSrc(this.getElement());
	}

	public void setUrl(final String url) {
		DOM.setImgSrc(this.getElement(), url);
	}

	public void addFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().addFocusEventListener(focusEventListener);
	}

	public void removeFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().removeFocusEventListener(focusEventListener);
	}

	public void addImageLoadEventListener(final ImageLoadEventListener imageLoadEventListener) {
		this.getEventListenerDispatcher().addImageLoadEventListener(imageLoadEventListener);
	}

	public void removeImageLoadEventListener(final ImageLoadEventListener imageLoadEventListener) {
		this.getEventListenerDispatcher().removeImageLoadEventListener(imageLoadEventListener);
	}

	public void addMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getEventListenerDispatcher().addMouseEventListener(mouseEventListener);
	}

	public void removeMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getEventListenerDispatcher().removeMouseEventListener(mouseEventListener);
	}
}
