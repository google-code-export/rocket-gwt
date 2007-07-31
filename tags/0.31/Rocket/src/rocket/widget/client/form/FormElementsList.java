package rocket.widget.client.form;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.Map;

import rocket.dom.client.DomHelper;
import rocket.util.client.Destroyable;
import rocket.util.client.ObjectHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides a list view of all a form's elements. It also includes a centralised factory method which creates various widgets depending on
 * which tag was encountered.
 * 
 * Because the form elements array is read only this list is also read only and any attempts to use any of Lists modifying methods which
 * result in an exception being thrown.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class FormElementsList extends AbstractList implements Destroyable {

    public int size() {
        return ObjectHelper.getPropertyCount(this.getFormElements());
    }

    public Object get(final int index) {
        Widget widget = null;

        while (true) {
            final Element element = (Element) ObjectHelper.getObject(this.getFormElements(), index);

            // check cache...
            final Map widgets = this.getWidgets();
            widget = (Widget) widgets.get(element);
            if (null != widget) {
                break;
            }

            widget = this.createWidget(element);
            widgets.put(element, widget);
            break;
        }

        return widget;
    }

    /**
     * Factory method which creates a Widget from the given Element.
     * 
     * @param element
     * @return
     */
    protected Widget createWidget(final Element element) {
        Widget widget = null;

        while (true) {
            if (DomHelper.isTag(element, FormConstants.INPUT_TAG)) {
                final String type = DOM.getAttribute(element, FormConstants.INPUT_TAG_TYPE);
                if (FormConstants.TEXT_TYPE.equalsIgnoreCase(type)) {
                    widget = createTextBox(element);
                    break;
                }
                if (FormConstants.PASSWORD_TYPE.equalsIgnoreCase(type)) {
                    widget = this.createPasswordTextBox(element);
                    break;
                }
                if (FormConstants.HIDDEN_TYPE.equalsIgnoreCase(type)) {
                    widget = createHidden(element);
                    break;
                }
                if (FormConstants.RADIO_BUTTON_TYPE.equalsIgnoreCase(type)) {
                    widget = createRadioButton(element);
                    break;
                }
                if (FormConstants.CHECKBOX_TYPE.equalsIgnoreCase(type)) {
                    widget = createCheckBox(element);
                    break;
                }
                if (FormConstants.SUBMIT_BUTTON_TYPE.equalsIgnoreCase(type)) {
                    widget = createSubmitButton(element);
                    break;
                }
                if (FormConstants.RESET_BUTTON_TYPE.equalsIgnoreCase(type)) {
                    widget = createResetButton(element);
                    break;
                }
                // unknown input type.
                this.createUnknownInputType(element);
                break;
            }
            if (DomHelper.isTag(element, FormConstants.BUTTON_TAG)) {
                widget = createButton(element);
                break;
            }
            if (DomHelper.isTag(element, FormConstants.TEXTAREA_TAG)) {
                widget = createTextArea(element);
                break;
            }
            if (DomHelper.isTag(element, FormConstants.LIST_TAG)) {
                widget = this.createList(element);
                break;
            }
            this.createUnknownElementType(element);
            break;
        }

        return widget;
    }

    /**
     * This method is called whenever an unknown/unhandled input element is encountered.
     * 
     * @param element
     */
    protected void createUnknownInputType(final Element element) {
        final String type = DOM.getAttribute(element, FormConstants.INPUT_TAG_TYPE);
        SystemHelper.fail("parameter:element", "Unknown input type [" + type + "] found within form collection.");
    }

    /**
     * This method is called whenever an unknown/unhandled element is encountered.
     * 
     * @param element
     */
    protected void createUnknownElementType(final Element element) {
        SystemHelper.fail("parameter:element", "Unknown element found within form collection, element: " + element);
    }

    /**
     * Factory method which creates a TextBox given a input element of type text.
     * 
     * @param element
     * @return
     */
    protected Widget createTextBox(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new TextBox(element);
    }

    class TextBox extends com.google.gwt.user.client.ui.TextBox {
        TextBox(final Element element) {
            super();

            this.setElement(element);
        }

        protected void setElement(final Element element) {
            DomHelper.checkInput("parameter:element", element, FormConstants.TEXT_TYPE);
            super.setElement(element);
        }

        public boolean equals(final Object other) {
            return other instanceof TextBox && DOM.compare(this.getElement(), ((TextBox) other).getElement());
        }
    }

    /**
     * Factory method which creates a PasswordTextBox given a input element of type password.
     * 
     * @param element
     * @return
     */
    protected Widget createPasswordTextBox(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new Password(element);
    }

    class Password extends com.google.gwt.user.client.ui.PasswordTextBox {
        Password(final Element element) {
            super();

            this.setElement(element);
        }

        protected void setElement(final Element element) {
            DomHelper.checkInput("parameter:element", element, FormConstants.PASSWORD_TYPE);
            super.setElement(element);
        }

        public boolean equals(final Object other) {
            return other instanceof Password && DOM.compare(this.getElement(), ((Password) other).getElement());
        }
    }

    /**
     * Factory method which creates a TextArea given a text area element
     * 
     * @param element
     * @return
     */
    protected Widget createTextArea(final Element element) {
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

    protected Widget createHidden(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new HiddenFormField(element);
    }

    /**
     * Factory method which creates a RadioButton given a radio button element.
     * 
     * @param element
     * @return
     */
    protected Widget createRadioButton(final Element element) {
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

    /**
     * Factory method which creates a CheckBox given a checkBox element
     * 
     * @param element
     * @return
     */
    protected Widget createCheckBox(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new CheckBox(element);
    }

    class CheckBox extends com.google.gwt.user.client.ui.CheckBox {
        CheckBox(final Element element) {
            super();

            this.setElement(element);
        }

        // protected void setElement( final Element element ){
        // DomHelper.checkInput( "parameter:element", element,
        // FormConstants.CHECKBOX_TYPE);
        // super.setElement( element );
        // }
        public boolean equals(final Object other) {
            return other instanceof CheckBox && DOM.compare(this.getElement(), ((CheckBox) other).getElement());
        }
    }

    /**
     * Factory method which creates a Button given a button element.
     * 
     * @param element
     * @return
     */
    protected Widget createButton(final Element element) {
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

    /**
     * Factory method which creates a ListBox given a list element.
     * 
     * @param element
     * @return
     */
    protected Widget createList(final Element element) {
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

    protected Widget createSubmitButton(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new SubmitButton(element);
    }

    protected Widget createResetButton(final Element element) {
        ObjectHelper.checkNotNull("parameter:element", element);
        return new ResetButton(element);
    }

    /**
     * A cache that maps form elements to widgets
     */
    private Map widgets;

    protected Map getWidgets() {
        ObjectHelper.checkNotNull("field:widgets", widgets);
        return this.widgets;
    }

    protected void setWidgets(final Map widgets) {
        ObjectHelper.checkNotNull("parameter:widgets", widgets);
        this.widgets = widgets;
    }

    protected Map createWidgets() {
        return new HashMap();
    }

    /**
     * The form whose collection is being viewed as a List
     */
    private JavaScriptObject form;

    public JavaScriptObject getForm() {
        ObjectHelper.checkNotNull("field:form", form);
        return form;
    }

    protected void setForm(final JavaScriptObject form) {
        ObjectHelper.checkNotNull("parameter:form", form);
        this.form = form;
    }

    protected void clearForm() {
        this.form = null;
    }

    protected JavaScriptObject getFormElements() {
        return ObjectHelper.getObject(this.getForm(), "elements");
    }

    public void destroy() {
        this.clearForm();
    }
}
