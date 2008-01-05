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
import rocket.event.client.KeyEventListener;
import rocket.event.client.MouseEventListener;
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple widget that contains the same capabilities of the GWT Button widget
 * but also adds the ability to hijack any of the following button like elements
 * from the dom.
 * <ul>
 * <li>tag = "button"</li>
 * <li>input type="reset"</li>
 * <li>input type="submit"</li>
 * </li>
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * Button widget.
 * 
 * TODO When upgrading GWT version reapply changes to this class.
 * 
 * @author Miroslav Pokorny
 */
public class Button extends FocusWidget {

	public Button() {
		super();
	}

	public Button(final String html) {
		this();

		this.setHtml(html);
	}

	public Button(Element element) {
		super(element);
	}

	protected void checkElement(Element element) {
		while (true) {
			if (Dom.isTag(element, WidgetConstants.BUTTON_TAG)) {
				break;
			}
			if (Dom.isInput(element, WidgetConstants.BUTTON_INPUT_RESET_TYPE)) {
				break;
			}
			if (Dom.isInput(element, WidgetConstants.BUTTON_INPUT_SUBMIT_TYPE)) {
				break;
			}
			Checker.fail("The parameter:element is not a button or input of type=radio/submit, but rather is a " + element);
		}
	}

	protected Element createElement() {
		return DOM.createButton();
	}

	protected void afterCreateElement() {
		final Element element = this.getElement();
		if (Dom.isTag(element, WidgetConstants.BUTTON_TAG)) {
			this.adjustType(this.getElement());
		}
	}

	native private void adjustType(final Element button) /*-{
	 // Check before setting this attribute, as not all browsers define it.
	 if (button.type == 'submit') {
	 try { 
	 button.setAttribute("type", "button"); 
	 } catch (e) { 
	 }
	 }
	 }-*/;

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.KEY_EVENTS | EventBitMaskConstants.MOUSE_CLICK
				| EventBitMaskConstants.MOUSE_DOUBLE_CLICK;
	}

	protected String getInitialStyleName() {
		return WidgetConstants.BUTTON_STYLE;
	}

	public String getHtml() {
		return DOM.getInnerHTML(getElement());
	}

	public void setHtml(final String html) {
		DOM.setInnerHTML(getElement(), html);
	}

	public String getText() {
		return DOM.getInnerText(getElement());
	}

	public void setText(final String text) {
		DOM.setInnerText(getElement(), text);
	}

	/**
	 * Programmatic equivalent of the user clicking the button.
	 */
	public void click() {
		click0(getElement());
	}

	native private void click0(final Element button) /*-{
	 button.click();
	 }-*/;

	public void addFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().addFocusEventListener(focusEventListener);
	}

	public void removeFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().removeFocusEventListener(focusEventListener);
	}

	public void addKeyEventListener(final KeyEventListener keyEventListener) {
		this.getEventListenerDispatcher().addKeyEventListener(keyEventListener);
	}

	public void removeKeyEventListener(final KeyEventListener keyEventListener) {
		this.getEventListenerDispatcher().removeKeyEventListener(keyEventListener);
	}

	public void addMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getEventListenerDispatcher().addMouseEventListener(mouseEventListener);
	}

	public void removeMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getEventListenerDispatcher().removeMouseEventListener(mouseEventListener);
	}
}
