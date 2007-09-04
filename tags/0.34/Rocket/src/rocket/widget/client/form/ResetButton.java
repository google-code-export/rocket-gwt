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
package rocket.widget.client.form;

import rocket.dom.client.Dom;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ButtonBase;

/**
 * The ResetButton widget represents a button abstraction of a Reset button that
 * is typically part of a form.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ResetButton extends ButtonBase {
	public ResetButton(final Element element) {
		super(element);
		sinkEvents(Event.ONCLICK);
	}

	static Element createElement() {
		final Element element = DOM.createElement(FormConstants.INPUT_TAG);
		DOM.setElementProperty(element, FormConstants.INPUT_TAG_TYPE, FormConstants.RESET_BUTTON_TYPE);
		return element;
	}

	public ResetButton() {
		this(createElement());
	}

	protected void setElement(final Element element) {
		Dom.checkInput("parameter:element", element, FormConstants.RESET_BUTTON_TYPE);
		super.setElement(element);
	}

	public void reset() {
		this.reset0(this.getElement());
	}

	native private void reset0(final Element element)/*-{
	 element.reset();
	 }-*/;

	public boolean equals(final Object other) {
		return other instanceof ResetButton && DOM.compare(this.getElement(), ((ResetButton) other).getElement());
	}

	public int hashCode() {
		return this.getElement().hashCode();
	}
}
