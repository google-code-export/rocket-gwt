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
 * A simple widget that contains the same capabilities of the GWT Label widget
 * but also adds the ability to hijack input type=file elements from the dom.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * FileUpload widget.
 * 
 * ROCKET When upgrading from GWT 1.5 RC1 reapply changes
 * 
 * @author Miroslav Pokorny
 */
public class FileUpload extends FocusWidget {

	public FileUpload() {
		super();
	}

	public FileUpload(final Element element) {
		super(element);
	}

	@Override
	protected void checkElement(Element element) {
		Dom.checkInput("parameter:element", element, WidgetConstants.FILE_UPLOAD_INPUT_TYPE);
	}

	@Override
	protected Element createElement() {
		final Element element = DOM.createElement("input");
		DOM.setElementProperty(element, "type", WidgetConstants.FILE_UPLOAD_INPUT_TYPE);
		return element;
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
		return WidgetConstants.FILE_UPLOAD_STYLE;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.CHANGE;
	}

	public String getFilename() {
		return DOM.getElementProperty(getElement(), "value");
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
