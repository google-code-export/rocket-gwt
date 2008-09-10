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

import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.FocusEventListener;
import rocket.event.client.MouseEventListener;
import rocket.style.client.ComputedStyle;
import rocket.style.client.Css;
import rocket.style.client.InlineStyle;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple widget that provides the common functionality contains the same
 * capabilities of the GWT HTML widget but also adds the ability to hijack any
 * elements from the dom.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * Label widget.
 * 
 * ROCKET When upgrading from GWT 1.5.2 reapply changes
 * 
 * @author Miroslav Pokorny
 */
abstract class HtmlOrLabel extends FocusWidget {

	public HtmlOrLabel() {
		super();
	}

	public HtmlOrLabel(final Element element) {
		super(element);
	}

	@Override
	protected void checkElement(Element element) {
	}

	@Override
	protected Element createElement() {
		return DOM.createDiv();
	}

	protected void afterCreateElement() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);

		dispatcher.setFocusEventListeners(dispatcher.createFocusEventListeners());
		dispatcher.setMouseEventListeners(dispatcher.createMouseEventListeners());
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.MOUSE_EVENTS;
	}

	public String getText() {
		return this.getElement().getInnerText();
	}

	public void setText(final String text) {
		this.getElement().setInnerText(text);
	}

	public boolean getWordWrap() {
		return !ComputedStyle.getComputedStyle( this.getElement() ).getString( "whiteSpace").equals("nowrap");
	}

	public void setWordWrap(boolean wrap) {
		InlineStyle.getInlineStyle( this.getElement() ).setString( Css.WHITE_SPACE, wrap ? "normal" : "nowrap");
	}

	public HorizontalAlignment getHorizontalAligment() {
		HorizontalAlignment textAlignment = HorizontalAlignment.LEFT;

		while (true) {
			final String property = ComputedStyle.getComputedStyle( this.getElement() ).getString(Css.TEXT_ALIGN);
			if (HorizontalAlignment.CENTER.getValue().equals(property)) {
				textAlignment = HorizontalAlignment.CENTER;
				break;
			}

			if (HorizontalAlignment.RIGHT.getValue().equals(property)) {
				textAlignment = HorizontalAlignment.RIGHT;
				break;
			}

			if (HorizontalAlignment.JUSTIFY.getValue().equals(property)) {
				textAlignment = HorizontalAlignment.JUSTIFY;
				break;
			}
			break;
		}

		return textAlignment;
	}

	public void setHorizontalAlignment(final HorizontalAlignment textAligment) {
		InlineStyle.getInlineStyle( this.getElement() ).setString(Css.TEXT_ALIGN, textAligment.getValue());
	}

	public void addFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().addFocusEventListener(focusEventListener);
	}

	public void removeFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().removeFocusEventListener(focusEventListener);
	}

	public void addMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getEventListenerDispatcher().addMouseEventListener(mouseEventListener);
	}

	public void removeMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getEventListenerDispatcher().removeMouseEventListener(mouseEventListener);
	}
}
