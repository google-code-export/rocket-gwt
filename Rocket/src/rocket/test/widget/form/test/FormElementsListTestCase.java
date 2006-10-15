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
package rocket.test.widget.form.test;

import java.util.Iterator;

import rocket.client.dom.DomHelper;
import rocket.client.widget.form.FormConstants;
import rocket.client.widget.form.FormElementsList;
import rocket.client.widget.form.HiddenFormField;
import rocket.client.widget.form.ResetButton;
import rocket.client.widget.form.SubmitButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Tests for FormElementsList a list view of an elements form.
 */
public class FormElementsListTestCase extends GWTTestCase {

    final static String NAME = "name";

    final static String VALUE = "value";

    /**
     * Must refer to a valid module that inherits from com.google.gwt.junit.JUnit
     */
    public String getModuleName() {
        return "rocket.test.form.FormTest";
    }

    public void testSize() {
        final FormElementsList list = new FormElementsList();
        list.setCollection(getFormElements());

        assertEquals(10, list.size());
    }

    public void testGet0OnlyTestsNotNull() {
        final FormElementsList list = new FormElementsList();
        list.setCollection(getFormElements());

        for (int i = 0; i < 10; i++) {
            this.addCheckpoint("element: " + i);

            final Object element = list.get(i);
            assertNotNull("get(" + i + ")[" + element + "]", element);
        }
    }

    public void testGet1ReturnsSameWrapperEachTime() {
        final FormElementsList list = new FormElementsList();
        list.setCollection(getFormElements());

        for (int i = 0; i < 10; i++) {
            this.addCheckpoint("element: " + i);

            final Object element = list.get(i);
            assertSame("get(" + i + ")[" + element + "]", element, list.get(i));
        }
    }

    public void testGet2TestsElementType() {
        final FormElementsList list = new FormElementsList();
        list.setCollection(getFormElements());

        {
            final Object element = list.get(0);
            assertTrue("get(0) should be a TextBox type: " + GWT.getTypeName(element), element instanceof TextBox);
        }
        {
            final Object element = list.get(1);
            assertTrue("get(1) should be a PasswordTextBox type: " + GWT.getTypeName(element),
                    element instanceof PasswordTextBox);
        }
        {
            final Object element = list.get(2);
            assertTrue("get(2) should be a CheckBox type: " + GWT.getTypeName(element), element instanceof CheckBox);
        }
        {
            final Object element = list.get(3);
            assertTrue("get(3) should be a RadioButton type: " + GWT.getTypeName(element),
                    element instanceof RadioButton);
        }
        {
            final Object element = list.get(4);
            assertTrue("get(4) should be a HiddenFormField type: " + GWT.getTypeName(element),
                    element instanceof HiddenFormField);
        }
        {
            final Object element = list.get(5);
            assertTrue("get(5) should be a TextArea type: " + GWT.getTypeName(element), element instanceof TextArea);
        }
        {
            final Object element = list.get(6);
            assertTrue("get(6) should be a Button type: " + GWT.getTypeName(element), element instanceof Button);
        }
        {
            final Object element = list.get(7);
            assertTrue("get(7) should be a ResetButton type: " + GWT.getTypeName(element),
                    element instanceof ResetButton);
        }
        {
            final Object element = list.get(8);
            assertTrue("get(8) should be a SubmitButton type: " + GWT.getTypeName(element),
                    element instanceof SubmitButton);
        }
        {
            final Object element = list.get(9);
            assertTrue("get(9) should be a ListBox type: " + GWT.getTypeName(element), element instanceof ListBox);
        }
    }

    public void testIterator() {
        final FormElementsList list = new FormElementsList();
        list.setCollection(getFormElements());

        final Iterator iterator = list.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            this.addCheckpoint("element: " + i);

            final Object element = iterator.next();
            final Object expectedElement = list.get(i);
            assertSame("element:" + i, expectedElement, element);
            i++;
        }

        assertFalse("Iterator.hasNext() should return false", iterator.hasNext());
        assertFalse("Iterator.hasNext() should return false", iterator.hasNext());
        assertFalse("Iterator.hasNext() should return false", iterator.hasNext());
    }

    /**
     * Factory which creates a form with three elements.
     * 
     * @return
     */
    static JavaScriptObject getFormElements() {
        final Element form = DOM.createElement("FORM");
        DOM.appendChild(RootPanel.getBodyElement(), form);

        // 0
        final Element text = DOM.createInputText();
        DOM.appendChild(form, text);

        // 1
        final Element password = DOM.createInputPassword();
        DOM.appendChild(form, password);

        // 2
        final Element checkBox = DOM.createInputCheck();
        DOM.appendChild(form, checkBox);

        // 3
        final Element radioButton = DOM.createInputRadio("radioGroup");
        DOM.appendChild(form, radioButton);

        // 4
        final Element hidden = DOM.createElement(FormConstants.INPUT_TAG);
        DOM.setAttribute(hidden, FormConstants.INPUT_TAG_TYPE, FormConstants.HIDDEN_TYPE);
        DOM.appendChild(form, hidden);

        // 5
        final Element textArea = DOM.createElement(FormConstants.TEXTAREA_TAG);
        DOM.appendChild(form, textArea);

        // 6
        final Element button = DOM.createButton();
        DOM.appendChild(form, button);

        // 7
        final Element resetButton = DOM.createElement(FormConstants.INPUT_TAG);
        DOM.setAttribute(resetButton, FormConstants.INPUT_TAG_TYPE, FormConstants.RESET_BUTTON_TYPE);
        DOM.appendChild(form, resetButton);

        // 8
        final Element submitButton = DOM.createElement(FormConstants.INPUT_TAG);
        DOM.setAttribute(submitButton, FormConstants.INPUT_TAG_TYPE, FormConstants.SUBMIT_BUTTON_TYPE);
        DOM.appendChild(form, submitButton);

        // 9
        final Element list = DOM.createSelect();
        final Element options = DOM.createOptions();
        DOM.appendChild(list, options);
        DOM.appendChild(options, DOM.createElement(FormConstants.OPTION_TAG));
        DOM.appendChild(options, DOM.createElement(FormConstants.OPTION_TAG));
        DOM.appendChild(form, list);

        return DomHelper.getPropertyAsJavaScriptObject(form, "elements");
    }
}
