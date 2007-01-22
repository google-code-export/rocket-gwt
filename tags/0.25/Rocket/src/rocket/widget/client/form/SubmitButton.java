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

import rocket.dom.client.DomHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ButtonBase;

/**
 * The SubmitButton widget represents a button abstraction of a Submit button that is typically part of a form.
 * 
 * @author Miroslav Pokorny (mP)
 */
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
        DomHelper.checkInput("parameter:element", element, FormConstants.SUBMIT_BUTTON_TYPE);
        super.setElement(element);
    }

    public void submit() {
        this.submit0(this.getElement());
    }

    native private void submit0(final Element element)/*-{
     element.submit();
     }-*/;

    public boolean equals(final Object other) {
        return other instanceof SubmitButton && DOM.compare(this.getElement(), ((SubmitButton) other).getElement());
    }

    public int hashCode() {
        return this.getElement().hashCode();
    }
}
