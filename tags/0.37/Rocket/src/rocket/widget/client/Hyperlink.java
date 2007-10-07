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
import com.google.gwt.user.client.History;

/**
 * A simple widget that contains the same capabilities of the GWT Hyperlink
 * widget but also adds the ability to hijack anchor elements from the dom.
 * Unlike the GWT widget there is the anchor is not enclosed within a div.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * Hyperlink.
 * 
 * TODO When upgrading GWT version reapply changes to this class.
 * 
 * @author Miroslav Pokorny
 */
public class Hyperlink extends FocusWidget {

	public Hyperlink() {
		super();
	}

	public Hyperlink(final Element element) {
		super(element);
	}

	protected void checkElement(Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.HYPERLINK_TAG);
	}

	protected Element createElement() {
		return DOM.createAnchor();
	}

	protected void afterCreateElement() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);

		dispatcher.setFocusEventListeners(dispatcher.createFocusEventListeners());
		dispatcher.setImageLoadEventListeners(dispatcher.createImageLoadEventListeners());
		dispatcher.setMouseEventListeners(dispatcher.createMouseEventListeners());
	}

	protected String getInitialStyleName() {
		return WidgetConstants.HYPERLINK_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.MOUSE_CLICK | EventBitMaskConstants.MOUSE_DOUBLE_CLICK;
	}

	public String getText() {
		return DOM.getInnerText(this.getElement());
	}

	public void setText(final String text) {
		DOM.setInnerText(this.getElement(), text);
	}

	public String getHtml() {
		return DOM.getInnerHTML(this.getElement());
	}

	public void setHtml(final String html) {
		DOM.setInnerHTML(this.getElement(), html);
	}

	private String targetHistoryToken;

	/**
	 * Gets the history token referenced by this hyperlink.
	 * 
	 * @return the target history token
	 * @see #setTargetHistoryToken
	 */
	public String getTargetHistoryToken() {
		return targetHistoryToken;
	}

	/**
	 * Sets the history token referenced by this hyperlink. This is the history
	 * token that will be passed to {@link History#newItem} when this link is
	 * clicked.
	 * 
	 * @param targetHistoryToken
	 *            the new target history token
	 */
	public void setTargetHistoryToken(String targetHistoryToken) {
		this.targetHistoryToken = targetHistoryToken;
		DOM.setElementProperty(this.getElement(), "href", "#" + targetHistoryToken);
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
