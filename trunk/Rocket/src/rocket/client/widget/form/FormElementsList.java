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

import rocket.client.dom.DomCollectionList;
import rocket.client.dom.DomHelper;
import rocket.client.util.ObjectHelper;
import rocket.client.util.SystemHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * This list provides a view of a live form and its elements returning the appropriate widget rather than elements given an index.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class FormElementsList extends DomCollectionList {

    protected void checkElementType(Object wrapper) {
    }

    protected Object createWrapper(final JavaScriptObject object) {
        return this.visitElement(DomHelper.castToElement(object));
    }

    /**
     * This method dispatches to the appropriate factory method depending on the type of element passed.
     * 
     * @param element
     * @return
     */
    protected Widget visitElement(final Element element) {
        Widget widget = null;
        while (true) {
            if (DomHelper.isTag(element, FormConstants.INPUT_TAG)) {
                final String type = DOM.getAttribute(element, FormConstants.INPUT_TAG_TYPE);
                if (FormConstants.TEXT_TYPE.equalsIgnoreCase(type)) {
                    widget = visitTextBox(element);
                    break;
                }
                if (FormConstants.PASSWORD_TYPE.equalsIgnoreCase(type)) {
                    widget = this.visitPasswordTextBox(element);
                    break;
                }
                if (FormConstants.HIDDEN_TYPE.equalsIgnoreCase(type)) {
                    widget = visitHidden(element);
                    break;
                }
                if (FormConstants.RADIO_BUTTON_TYPE.equalsIgnoreCase(type)) {
                    widget = visitRadioButton(element);
                    break;
                }
                if (FormConstants.CHECKBOX_TYPE.equalsIgnoreCase(type)) {
                    widget = visitCheckBox(element);
                    break;
                }
                if (FormConstants.LIST_TAG.equalsIgnoreCase(type)) {
                    widget = visitList(element);
                    break;
                }
                if (FormConstants.SUBMIT_BUTTON_TYPE.equalsIgnoreCase(type)) {
                    widget = visitSubmitButton(element);
                    break;
                }
                if (FormConstants.RESET_BUTTON_TYPE.equalsIgnoreCase(type)) {
                    widget = visitResetButton(element);
                    break;
                }
                // unknown input type.

                break;
            }

            if (DomHelper.isTag(element, FormConstants.BUTTON_TAG)) {
                widget = visitButton(element);
                break;
            }
            if (DomHelper.isTag(element, FormConstants.TEXTAREA_TAG)) {
                widget = visitTextArea(element);
                break;
            }
            if (DomHelper.isTag(element, FormConstants.LIST_TAG)) {
                widget = this.visitList(element);
                break;
            }
        }

        if (null == widget) {
            SystemHelper.handleAssertFailure("Widget not created for unknown element " + DomHelper.toString(element));
        }
        return widget;
    }

    protected Widget visitTextBox(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new TextBox(element);
    }

    class TextBox extends com.google.gwt.user.client.ui.TextBox {
        TextBox(final Element element) {
            super();

            this.setElement(element);
        }

        protected void setElement(final Element element) {
            DomHelper.checkInputElement("parameter:element", element, FormConstants.TEXT_TYPE);
            super.setElement(element);
        }

        public boolean equals(final Object other) {
            return other instanceof TextBox && DOM.compare(this.getElement(), ((TextBox) other).getElement());
        }
    }

    protected Widget visitPasswordTextBox(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new Password(element);
    }

    class Password extends com.google.gwt.user.client.ui.PasswordTextBox {
        Password(final Element element) {
            super();

            this.setElement(element);
        }

        protected void setElement(final Element element) {
            DomHelper.checkInputElement("parameter:element", element, FormConstants.PASSWORD_TYPE);
            super.setElement(element);
        }

        public boolean equals(final Object other) {
            return other instanceof Password && DOM.compare(this.getElement(), ((Password) other).getElement());
        }
    }

    protected Widget visitTextArea(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new TextArea(element);
    }

    class TextArea extends com.google.gwt.user.client.ui.TextArea {
        TextArea(final Element element) {
            super();

            this.setElement(element);
        }

        protected void setElement(final Element element) {
            DomHelper.checkTagName("parameter:element", element, FormConstants.TEXTAREA_TAG);
            super.setElement(element);
        }

        public boolean equals(final Object other) {
            return other instanceof TextArea && DOM.compare(this.getElement(), ((TextArea) other).getElement());
        }
    }

    protected Widget visitHidden(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new HiddenFormField(element);
    }

    protected Widget visitRadioButton(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new RadioButton(element);
    }

    class RadioButton extends com.google.gwt.user.client.ui.RadioButton {
        RadioButton(final Element element) {
            super("mP");

            this.setElement(element);
        }

        public boolean equals(final Object other) {
            return other instanceof RadioButton && DOM.compare(this.getElement(), ((RadioButton) other).getElement());
        }
    }

    protected Widget visitCheckBox(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new CheckBox(element);
    }

    class CheckBox extends com.google.gwt.user.client.ui.CheckBox {
        CheckBox(final Element element) {
            super();

            this.setElement(element);
        }

        // protected void setElement( final Element element ){
        // DomHelper.checkInputElement( "parameter:element", element,
        // FormConstants.CHECKBOX_TYPE);
        // super.setElement( element );
        // }
        public boolean equals(final Object other) {
            return other instanceof CheckBox && DOM.compare(this.getElement(), ((CheckBox) other).getElement());
        }
    }

    protected Widget visitButton(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new Button(element);
    }

    class Button extends com.google.gwt.user.client.ui.Button {
        Button(final Element element) {
            super();

            this.setElement(element);
        }

        protected void setElement(final Element element) {
            DomHelper.checkTagName("parameter:element", element, FormConstants.BUTTON_TAG);
            super.setElement(element);
        }

        public boolean equals(final Object other) {
            return other instanceof Button && DOM.compare(this.getElement(), ((Button) other).getElement());
        }
    }

    protected Widget visitList(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);

        return new ListBox(element);
    }

    class ListBox extends com.google.gwt.user.client.ui.ListBox {
        ListBox(final Element element) {
            super();

            this.setElement(element);
        }

        // protected void setElement( final Element element ){
        // DomHelper.checkTagName( "parameter:element", element,
        // FormConstants.LIST_TAG);
        // super.setElement( element );
        // }
        public boolean equals(final Object other) {
            return other instanceof ListBox && DOM.compare(this.getElement(), ((ListBox) other).getElement());
        }
    }

    protected Widget visitSubmitButton(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new SubmitButton(element);
    }

    protected Widget visitResetButton(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new ResetButton(element);
    }

    // DOM COLLECTION LIST ::::::::::::::::::::::

    protected void add1(final JavaScriptObject collection, final JavaScriptObject element) {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + "add0()");
    }

    protected void insert1(final JavaScriptObject collection, final int index, final JavaScriptObject element) {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + "insert0()");
    }

    protected JavaScriptObject remove0(final JavaScriptObject collection, final int index) {
        throw new UnsupportedOperationException(GWT.getTypeName(this) + "remove0()");
    }

    protected void adopt(final Object object) {
    }

    protected void disown(final Object object) {
    }
}
