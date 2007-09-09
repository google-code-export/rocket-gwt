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
package rocket.widget.test.form;

import java.util.Iterator;
import java.util.List;

import rocket.dom.client.Dom;
import rocket.widget.client.form.FormConstants;
import rocket.widget.client.form.FormHelper;

import com.google.gwt.core.client.GWT;
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
 * A set of tests for the FormElementList class.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class FormElementListGwtTestCase extends GWTTestCase {

	final static String NAME = "name";

	final static String VALUE = "value";

	public String getModuleName() {
		return "rocket.widget.test.form.FormElementListGwtTestCase";
	}

	public void testSize() {
		final List list = getForm();

		assertEquals(10, list.size());
	}

	public void testGet() {
		final List list = getForm();

		int i = 0;
		final Object button = list.get(i);
		assertTrue("List.get(" + i + ") should be a Button not a " + button, button instanceof Button);
		i++;

		final Object checkBox = list.get(i);
		assertTrue("List.get(" + i + ") should be a CheckBox not a " + checkBox, checkBox instanceof CheckBox);
		i++;

		final Object hiddenFormField = list.get(i);
		assertTrue("List.get(" + i + ") should be a HiddenFormField not a " + hiddenFormField, GWT.getTypeName(hiddenFormField).indexOf(
				"HiddenFormField") != -1);
		i++;

		final Object listBox = list.get(i);
		assertTrue("List.get(" + i + ") should be a ListBox not a " + listBox, listBox instanceof ListBox);
		i++;

		final Object password = list.get(i);
		assertTrue("List.get(" + i + ") should be a PasswordTextBox not a " + password, password instanceof PasswordTextBox);
		i++;

		final Object radioButton = list.get(i);
		assertTrue("List.get(" + i + ") should be a RadioButton not a " + radioButton, radioButton instanceof RadioButton);
		i++;

		final Object resetButton = list.get(i);
		assertTrue("List.get(" + i + ") should be a ResetButton not a " + resetButton,
				GWT.getTypeName(resetButton).indexOf("ResetButton") != -1);
		i++;

		final Object submitButton = list.get(i);
		assertTrue("List.get(" + i + ") should be a SubmitButton not a " + submitButton, GWT.getTypeName(submitButton).indexOf(
				"SubmitButton") != -1);
		i++;

		final Object textArea = list.get(i);
		assertTrue("List.get(" + i + ") should be a TextArea not a " + textArea, textArea instanceof TextArea);
		i++;

		final Object textBox = list.get(i);
		assertTrue("List.get(" + i + ") should be a TextBox not a " + textBox, textBox instanceof TextBox);
		i++;
	}

	public void testSameWrapperReturned() {
		final List list = getForm();

		for (int i = 0; i < list.size(); i++) {
			this.addCheckpoint("element: " + i);

			final Object element = list.get(i);
			final Object elementAgain = list.get(i);
			assertSame("get(" + i + ")", element, elementAgain);
		}
	}

	public void testIterator() {
		final List list = getForm();

		final Iterator iterator = list.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			this.addCheckpoint("element: " + i + " list: " + list);

			final Object element = iterator.next();
			final Object expectedElement = list.get(i);
			assertEquals("element:" + i, expectedElement, element);
			i++;
		}

		assertFalse("Iterator.hasNext() should return false", iterator.hasNext());
	}

	/**
	 * Factory which creates a form with several elements.
	 * 
	 * @return The created unattached form.
	 */
	static Element buildForm() {
		final Element form = DOM.createElement("FORM");
		DOM.appendChild(RootPanel.getBodyElement(), form);

		// 0
		final Element button = DOM.createButton();
		DOM.appendChild(form, button);

		// 1
		final Element checkBox = DOM.createInputCheck();
		DOM.appendChild(form, checkBox);

		// 2
		final Element hidden = DOM.createElement(FormConstants.INPUT_TAG);
		DOM.setElementProperty(hidden, FormConstants.INPUT_TAG_TYPE, FormConstants.HIDDEN_TYPE);
		DOM.appendChild(form, hidden);

		// 3
		final Element list = DOM.createSelect();
		final Element options = DOM.createOptions();
		DOM.appendChild(list, options);
		DOM.appendChild(options, DOM.createElement(FormConstants.OPTION_TAG));
		DOM.appendChild(options, DOM.createElement(FormConstants.OPTION_TAG));
		DOM.appendChild(form, list);

		// 4
		final Element password = DOM.createInputPassword();
		DOM.appendChild(form, password);

		// 5
		final Element radioButton = DOM.createInputRadio("radioGroup");
		DOM.appendChild(form, radioButton);

		// 6
		final Element resetButton = DOM.createElement(FormConstants.INPUT_TAG);
		DOM.setElementProperty(resetButton, FormConstants.INPUT_TAG_TYPE, FormConstants.RESET_BUTTON_TYPE);
		DOM.appendChild(form, resetButton);

		// 7
		final Element submitButton = DOM.createElement(FormConstants.INPUT_TAG);
		DOM.setElementProperty(submitButton, FormConstants.INPUT_TAG_TYPE, FormConstants.SUBMIT_BUTTON_TYPE);
		DOM.appendChild(form, submitButton);

		// 8
		final Element textArea = DOM.createElement(FormConstants.TEXTAREA_TAG);
		DOM.appendChild(form, textArea);

		// 9
		final Element text = DOM.createInputText();
		DOM.appendChild(form, text);

		return form;
	}

	/**
	 * Convenient method to retrieve a list view of a collection of form
	 * elements.
	 * 
	 * @return The list
	 */
	static List getForm() {
		final Element form = buildForm();

		DOM.appendChild(Dom.getBody(), form);
		return FormHelper.createFormElementsList(form);
	}
}
