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
import rocket.style.client.ComputedStyle;
import rocket.style.client.Css;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple widget that contains the same capabilities of the GWT Label widget
 * but also adds the ability to hijack input text and password elements from the
 * dom.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * TextBox widget.
 * 
 * ROCKET When upgrading from GWT 1.5.2 reapply changes
 * 
 * @author Miroslav Pokorny
 */
public class TextBox extends TextEntryWidget {

	public TextBox() {
		super();
	}

	public TextBox(final boolean password) {
		super(password ? DOM.createInputPassword() : DOM.createInputText());
	}

	public TextBox(final Element element) {
		super(element);
	}

	@Override
	protected void checkElement(final Element element) {
		if (false == Dom.isInput(element, WidgetConstants.TEXTBOX_INPUT_TYPE)
				&& false == Dom.isInput(element, WidgetConstants.PASSWORD_TEXTBOX_INPUT_TYPE)) {
			Checker.fail("Incompatible element type passed to constructor: element: " + element);
		}
	}

	@Override
	protected Element createElement() {
		return DOM.createInputText();
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.TEXTBOX_STYLE;
	}

	protected String getReadOnlyStyleName() {
		return WidgetConstants.TEXTBOX_READONLY;
	}

	/**
	 * Gets the maximum allowable length of the text box.
	 * 
	 * @return the maximum length, in characters
	 */
	public int getMaxLength() {
		return DOM.getElementPropertyInt(getElement(), "maxLength");
	}

	/**
	 * Sets the maximum allowable length of the text box.
	 * 
	 * @param length
	 *            the maximum length, in characters
	 */
	public void setMaxLength(int length) {
		DOM.setElementPropertyInt(getElement(), "maxLength", length);
	}

	/**
	 * Gets the number of visible characters in the text box.
	 * 
	 * @return the number of visible characters
	 */
	public int getVisibleLength() {
		return DOM.getElementPropertyInt(getElement(), "size");
	}

	/**
	 * Sets the number of visible characters in the text box.
	 * 
	 * @param length
	 *            the number of visible characters
	 */
	public void setVisibleLength(int length) {
		DOM.setElementPropertyInt(getElement(), "size", length);
	}

	public int getCursorPos() {
		return this.getTextBoxSupport().getCursorPos(getElement());
	}

	public int getSelectionLength() {
		return this.getTextBoxSupport().getSelectionLength(getElement());
	}

	public TextAlignment getTextAligment() {
		TextAlignment textAlignment = TextAlignment.LEFT;

		while (true) {
			final String property = ComputedStyle.getComputedStyle( this.getElement() ).getString(Css.TEXT_ALIGN);
			if (TextAlignment.CENTER.getValue().equals(property)) {
				textAlignment = TextAlignment.CENTER;
				break;
			}

			if (TextAlignment.RIGHT.getValue().equals(property)) {
				textAlignment = TextAlignment.RIGHT;
				break;
			}

			if (TextAlignment.JUSTIFY.getValue().equals(property)) {
				textAlignment = TextAlignment.JUSTIFY;
				break;
			}
			break;
		}

		return textAlignment;
	}

	public void setTextAlignment(final TextAlignment textAligment) {
		InlineStyle.getInlineStyle( this.getElement() ).setString(Css.TEXT_ALIGN, textAligment.getValue());
	}

	/**
	 * Tests if this element is a password text box.
	 * 
	 * @return
	 */
	public boolean isPassword() {
		return Dom.isInput(this.getElement(), WidgetConstants.PASSWORD_TEXTBOX_INPUT_TYPE);
	}
}
