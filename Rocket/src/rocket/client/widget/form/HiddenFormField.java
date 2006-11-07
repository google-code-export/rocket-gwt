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
import rocket.client.util.StringHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget class represents a hidden form field to make manipulation of the value a bit easier.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HiddenFormField extends Widget {
    public HiddenFormField(final Element element) {
        super();

        this.setElement(element);
    }

    protected void setElement(final Element element) {
        DomHelper.checkInputElement("parameter:element", element, FormConstants.HIDDEN_TYPE);
        super.setElement(element);
    }

    public String getName() {
        return DOM.getAttribute(this.getElement(), "name");
    }

    public void setName(final String name) {
        StringHelper.checkNotNull("parameter:name", name);
        DOM.setAttribute(this.getElement(), "name", name);
    }

    public String getValue() {
        return DOM.getAttribute(this.getElement(), "value");
    }

    public void setValue(final String value) {
        StringHelper.checkNotNull("parameter:value", value);
        DOM.setAttribute(this.getElement(), "value", value);
    }

    public boolean equals(final Object other) {
        return other instanceof HiddenFormField
                && DOM.compare(this.getElement(), ((HiddenFormField) other).getElement());
    }
}