/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.widget.form;

import rocket.client.dom.DomHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ButtonBase;

public class SubmitButton extends ButtonBase {
    public SubmitButton(final Element element) {
        super(element);
        sinkEvents(Event.ONCLICK);
    }

    static Element createElement() {
        final Element element = DOM.createElement(FormConstants.INPUT_TAG);
        DOM.setAttribute(element, FormConstants.INPUT_TAG_TYPE, FormConstants.SUBMIT_BUTTON_TYPE);
        return element;
    }

    public SubmitButton() {
        this(createElement());
    }

    protected void setElement(final Element element) {
        DomHelper.checkInputElement("parameter:element", element, FormConstants.SUBMIT_BUTTON_TYPE);
        super.setElement(element);
    }

    public void submit() {

    }
}