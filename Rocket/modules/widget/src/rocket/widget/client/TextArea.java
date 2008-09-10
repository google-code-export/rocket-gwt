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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A simple widget that contains the same capabilities of the GWT TextArea
 * widget but also adds the ability to hijack textarea elements from the dom.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * TextArea widget.
 * 
 * ROCKET When upgrading from GWT 1.5.2 reapply changes
 * 
 * @author Miroslav Pokorny
 */
public class TextArea extends TextEntryWidget {

	public TextArea() {
		super();
	}

	public TextArea(Element element) {
		super(element);
	}

	@Override
	protected void checkElement(Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.TEXTAREA_TAG);
	}

	@Override
	protected Element createElement() {
		return DOM.createTextArea();
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
	protected String getInitialStyleName() {
		return WidgetConstants.TEXTAREA_STYLE;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.KEY_EVENTS | EventBitMaskConstants.CHANGE;
	}

	protected String getReadOnlyStyleName() {
		return WidgetConstants.TEXTAREA_READONLY;
	}

	public int getCursorPos() {
		return getTextBoxSupport().getTextAreaCursorPos(getElement());
	}

	public int getSelectionLength() {
		return this.getTextBoxSupport().getTextAreaSelectionLength(getElement());
	}

	public int getColumns() {
		return this.getElement().getPropertyInt("cols");
	}

	public void setColumns(final int columns) {
		DOM.setElementPropertyInt(getElement(), "cols", columns);
	}

	public int getRows() {
		return getElement().getPropertyInt("rows");
	}

	public void setRows(final int rows) {
		getElement().setPropertyInt("rows", rows);
	}
}
