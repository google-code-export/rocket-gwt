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

import rocket.event.client.ChangeEventListener;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.FocusEventListener;
import rocket.event.client.KeyEventListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.impl.TextBoxImpl;

/**
 * Provides a base class that contains the common functionality between the
 * TextBox, PasswordTextBox and TextArea widgets.
 * 
 * @author Miroslav Pokorny
 */
abstract class TextEntryWidget extends FocusWidget {

	/**
	 * Reuse the GWT TextBox support.
	 */
	static private TextBoxImpl textBoxSupport = createTextBoxSupport();

	static TextBoxImpl getTextBoxSupport() {
		return textBoxSupport;
	}

	static TextBoxImpl createTextBoxSupport() {
		return (TextBoxImpl) GWT.create(TextBoxImpl.class);
	}

	public TextEntryWidget() {
		super();
	}

	public TextEntryWidget(Element element) {
		super(element);
	}

	@Override
	protected void afterCreateElement() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		this.setEventListenerDispatcher(dispatcher);

		dispatcher.setChangeEventListeners(dispatcher.createChangeEventListeners());
		dispatcher.setFocusEventListeners(dispatcher.createFocusEventListeners());
		dispatcher.setKeyEventListeners(dispatcher.createKeyEventListeners());
	}

	@Override
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.KEY_EVENTS | EventBitMaskConstants.CHANGE;
	}

	public String getText() {
		return ((InputElement) this.getElement().cast()).getValue();
	}

	public void setText(String text) {
		((InputElement) this.getElement().cast()).setValue(text != null ? text : "");
	}

	/**
	 * Turns read-only mode on or off.
	 * 
	 * @param readOnly
	 *            if <code>true</code>, the widget becomes read-only; if
	 *            <code>false</code> the widget becomes editable
	 */
	public void setReadOnly(final boolean readOnly) {
		((InputElement) this.getElement().cast()).setReadOnly(readOnly);

		final String readOnlyStyle = this.getReadOnlyStyleName();
		if (readOnly) {
			this.addStyleName(readOnlyStyle);
		} else {
			this.removeStyleName(readOnlyStyle);
		}
	}

	abstract protected String getReadOnlyStyleName();

	abstract public int getCursorPos();

	public void setCursorPos(final int pos) {
		this.setSelectionRange(pos, 0);
	}

	abstract protected int getSelectionLength();

	public String getSelectedText() {
		final int start = getCursorPos();
		final int length = getSelectionLength();
		return -1 == length ? "" : getText().substring(start, start + length);
	}

	public void selectAll() {
		int length = getText().length();
		if (length > 0) {
			setSelectionRange(0, length);
		}
	}

	/**
	 * Sets the range of text to be selected.
	 * 
	 * @param pos
	 *            the position of the first character to be selected
	 * @param length
	 *            the number of characters to be selected
	 */
	public void setSelectionRange(final int pos, final int length) {
		if (length < 0) {
			throw new IndexOutOfBoundsException("Length must be a positive integer. Length: " + length);
		}
		if ((pos < 0) || (length + pos > getText().length())) {
			throw new IndexOutOfBoundsException("From Index: " + pos + "  To Index: " + (pos + length) + "  Text Length: "
					+ getText().length());
		}
		TextEntryWidget.getTextBoxSupport().setSelectionRange(getElement(), pos, length);
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

	public void addKeyEventListener(final KeyEventListener keyEventListener) {
		this.getEventListenerDispatcher().addKeyEventListener(keyEventListener);
	}

	public void removeKeyEventListener(final KeyEventListener keyEventListener) {
		this.getEventListenerDispatcher().removeKeyEventListener(keyEventListener);
	}

	@Override
	public String toString() {
		return super.toString() + ", text\"" + this.getText() + "\".";
	}
}
