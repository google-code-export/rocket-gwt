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

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple widget that contains the same capabilities of the GWTPasswordTextBox
 * widget but also adds the ability to hijack password elements from the dom.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * RadioButton widget.
 * 
 * TODO ROCKET When upgrading from GWT 1.5.2 reapply changes
 * 
 * @author Miroslav Pokorny
 */
public class RadioButton extends FocusWidget {

	public RadioButton(final String groupName) {
		super();
		this.setGroupName(groupName);
	}

	public RadioButton(final Element element) {
		super(element);
	}

	@Override
	protected void checkElement(final Element element) {
		Dom.checkInput("parameter:element", element, WidgetConstants.RADIO_BUTTON_INPUT_TYPE);
	}

	@Override
	protected Element createElement() {
		return DOM.createInputRadio(this.getGroupName());
	}

	/**
	 * A temporary copy of the groupname parameter passed in the
	 * {@link #RadioButton(String)} constructor.
	 */
	private String groupName;

	String getGroupName() {
		return groupName;
	}

	void setGroupName(final String groupName) {
		this.groupName = groupName;
	}

	@Override
	protected void afterCreateElement() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);

		dispatcher.setChangeEventListeners(dispatcher.createChangeEventListeners());
		dispatcher.setFocusEventListeners(dispatcher.createFocusEventListeners());
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.RADIO_BUTTON_STYLE;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.CHANGE;
	}

	/**
	 * Determines whether this check box is currently checked.
	 * 
	 * @return <code>true</code> if the check box is checked
	 */
	public boolean isChecked() {
		final String property = isAttached() ? "checked" : "defaultChecked";
		return DOM.getElementPropertyBoolean(this.getElement(), property);
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

	/**
	 * Change the group name of this radio button.
	 * 
	 * Radio buttons are grouped by their name attribute, so changing their name
	 * using the setName() method will also change their associated group.
	 * 
	 * If changing this group name results in a new radio group with multiple
	 * radio buttons selected, this radio button will remain selected and the
	 * other radio buttons will be unselected.
	 * 
	 * @param name
	 *            name the group with which to associate the radio button
	 */
	public void setName(String name) {
		this.replaceInputElement(DOM.createInputRadio(name));
	}

	/**
	 * Replace the current input element with a new one.
	 * 
	 * @param newElement
	 *            the new input element
	 */
	protected void replaceInputElement(final Element newElement) {
		// Collect information we need to set

		final InputElement oldInputElement = this.getElement().cast();

		int tabIndex = getTabIndex();
		boolean checked = isChecked();
		boolean enabled = isEnabled();
		String uid = oldInputElement.getId();
		String accessKey = oldInputElement.getAccessKey();

		// Clear out the old input element
		setChecked(false);
		oldInputElement.setId("");
		oldInputElement.setAccessKey("");

		// Quickly do the actual replace
		final Element parent = oldInputElement.getParentElement().cast();
		final int index = DOM.getChildIndex(parent, (Element) oldInputElement.cast());
		parent.removeChild(oldInputElement);
		DOM.insertChild(parent, (Element) newElement.cast(), index);
		this.invokeReplaceElement(newElement);

		// Setup the new element
		DOM.sinkEvents((Element) oldInputElement.cast(), DOM.getEventsSunk(this.getElement()));
		DOM.setEventListener((Element) oldInputElement.cast(), this);
		oldInputElement.setId(uid);
		if (accessKey != "") {
			oldInputElement.setAccessKey(accessKey);
		}
		setTabIndex(tabIndex);
		setChecked(checked);
		setEnabled(enabled);
	}

	/**
	 * A hack to gain access to the package private replaceElement method
	 * 
	 * @param newElement
	 */
	native protected void invokeReplaceElement(final Element newElement)/*-{
			this.@com.google.gwt.user.client.ui.UIObject::replaceElement(Lcom/google/gwt/dom/client/Element;)(newElement);
		}-*/;

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
