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
import rocket.event.client.ChangeEventListener;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.FocusEventListener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple check box widget. It also supports wrapping of existing checkbox
 * elements taken from the dom via the {@link #CheckBox(Element)} constructor.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * CheckBox widget.
 * 
 * TODO When upgrading GWT version reapply changes to this class.
 * 
 * @author Miroslav Pokorny
 */
public class CheckBox extends FocusWidget {

	public CheckBox() {
		super();
	}

	public CheckBox(final Element element) {
		super(element);
	}

	protected void checkElement(Element element) {
		Dom.checkInput("parameter:element", element, WidgetConstants.CHECKBOX_INPUT_TYPE);
	}

	protected Element createElement() {
		return DOM.createInputCheck();
	}

	protected void afterCreateElement() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		dispatcher.setChangeEventListeners(dispatcher.createChangeEventListeners());
		dispatcher.setFocusEventListeners(dispatcher.createFocusEventListeners());
	}

	protected String getInitialStyleName() {
		return WidgetConstants.CHECKBOX_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.CHANGE;
	}

	/**
	 * Determines whether this check box is currently checked.
	 * 
	 * @return <code>true</code> if the check box is checked
	 */
	public boolean isChecked() {
		String propName = isAttached() ? "checked" : "defaultChecked";
		return DOM.getElementPropertyBoolean(this.getElement(), propName);
	}

	/**
	 * Checks or unchecks this check box.
	 * 
	 * @param checked
	 *            <code>true</code> to check the check box
	 */
	public void setChecked(final boolean checked) {
		final Element element = this.getElement();
		DOM.setElementPropertyBoolean(element, "checked", checked);
		DOM.setElementPropertyBoolean(element, "defaultChecked", checked);
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
}
