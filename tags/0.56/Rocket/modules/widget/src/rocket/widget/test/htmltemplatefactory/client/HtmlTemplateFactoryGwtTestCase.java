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
package rocket.widget.test.htmltemplatefactory.client;

import rocket.widget.client.Button;
import rocket.widget.client.CheckBox;
import rocket.widget.client.FormPanel;
import rocket.widget.client.Html;
import rocket.widget.client.HtmlTemplateFactory;
import rocket.widget.client.Hyperlink;
import rocket.widget.client.Image;
import rocket.widget.client.Label;
import rocket.widget.client.ListBox;
import rocket.widget.client.RadioButton;
import rocket.widget.client.TextArea;
import rocket.widget.client.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class HtmlTemplateFactoryGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.widget.test.htmltemplatefactory.HtmlTemplateFactory";
	}

	public void testTextBox() {
		final TestTemplate test = new TestTemplate() {

			@Override
			Element createElement() {
				return DOM.createInputText();
			}

			@Override
			String getId() {
				return "testTextBox";
			}

			@Override
			Widget getInstance() {
				final TextBoxHtmlTemplateFactory factory = (TextBoxHtmlTemplateFactory) GWT.create(TextBoxHtmlTemplateFactory.class);
				return factory.getTextBox();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final TextBox textBox = (TextBox) widget;
				final String value = "apple";
				element.setPropertyString("value", value);
				assertEquals("" + textBox, value, textBox.getText());
			}
		};
		test.run();
	}

	static interface TextBoxHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testTextBox
		 * @return
		 */
		TextBox getTextBox();
	}

	public void testPasswordTextBox() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createInputPassword();
			}

			@Override
			String getId() {
				return "testPasswordTextBox";
			}

			@Override
			Widget getInstance() {
				final PasswordTextBoxHtmlTemplateFactory factory = (PasswordTextBoxHtmlTemplateFactory) GWT
						.create(PasswordTextBoxHtmlTemplateFactory.class);
				return factory.getPasswordTextBox();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final TextBox password = (TextBox) widget;
				final String value = "apple";
				element.setPropertyString("value", value);
				assertEquals("" + password, value, password.getText());
			}
		};
		test.run();
	}

	static interface PasswordTextBoxHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testPasswordTextBox
		 * @return
		 */
		TextBox getPasswordTextBox();
	}

	public void testTextArea() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createTextArea();
			}

			@Override
			String getId() {
				return "testTextArea";
			}

			@Override
			Widget getInstance() {
				final TextAreaHtmlTemplateFactory factory = (TextAreaHtmlTemplateFactory) GWT.create(TextAreaHtmlTemplateFactory.class);
				return factory.getTextArea();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final TextArea textArea = (TextArea) widget;
				final String value = "apple";
				element.setPropertyString("value", value);
				assertEquals("" + textArea, value, textArea.getText());
			}
		};
		test.run();
	}

	static interface TextAreaHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testTextArea
		 * @return
		 */
		TextArea getTextArea();
	}

	public void testRadioButton() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createInputRadio("group");
			}

			@Override
			String getId() {
				return "testRadioButton";
			}

			@Override
			Widget getInstance() {
				final RadioButtonHtmlTemplateFactory factory = (RadioButtonHtmlTemplateFactory) GWT
						.create(RadioButtonHtmlTemplateFactory.class);
				return factory.getRadioButton();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final RadioButton radioButton = (RadioButton) widget;

				final String name = "testRadioButton";
				radioButton.setName(name);
				assertEquals("element: " + element.toString() + "\nradioButton: " + radioButton + "\n", name, radioButton.getName());
			}
		};
		test.run();
	}

	static interface RadioButtonHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testRadioButton
		 * @return
		 */
		RadioButton getRadioButton();
	}

	public void testCheckBox() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createInputCheck();
			}

			@Override
			String getId() {
				return "testCheckBox";
			}

			@Override
			Widget getInstance() {
				final CheckBoxHtmlTemplateFactory factory = (CheckBoxHtmlTemplateFactory) GWT.create(CheckBoxHtmlTemplateFactory.class);
				return factory.getCheckBox();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final CheckBox checkBox = (CheckBox) widget;
				final String name = "testCheckBox";
				checkBox.setName(name);
				assertEquals("element: " + element.toString() + "\ncheckBox: " + checkBox + "\n", name, checkBox.getName());
			}
		};
		test.run();
	}

	static interface CheckBoxHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testCheckBox
		 * @return
		 */
		CheckBox getCheckBox();
	}

	public void testListBox() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				final Element select = DOM.createSelect();
				final Element firstOption = DOM.createElement("option");
				firstOption.setPropertyString("value", "first");
				firstOption.setInnerHTML("first");
				select.appendChild(firstOption);

				final Element secondOption = DOM.createElement("option");
				secondOption.setPropertyBoolean("selected", true);
				secondOption.setPropertyString("value", "two");
				secondOption.setInnerHTML("two");
				select.appendChild(secondOption);

				return select;
			}

			@Override
			String getId() {
				return "testListBox";
			}

			@Override
			Widget getInstance() {
				final ListBoxHtmlTemplateFactory factory = (ListBoxHtmlTemplateFactory) GWT.create(ListBoxHtmlTemplateFactory.class);
				return factory.getListBox();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final ListBox listBox = (ListBox) widget;
				assertEquals("" + listBox, 2, listBox.getItemCount());
				assertEquals("" + listBox, 1, listBox.getSelectedIndex());
			}
		};
		test.run();
	}

	static interface ListBoxHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testListBox
		 * @return
		 */
		ListBox getListBox();
	}

	public void testLabel() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createDiv();
			}

			@Override
			String getId() {
				return "testLabel";
			}

			@Override
			Widget getInstance() {
				final LabelHtmlTemplateFactory factory = (LabelHtmlTemplateFactory) GWT.create(LabelHtmlTemplateFactory.class);
				return factory.getLabel();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final Label label = (Label) widget;
				final String value = "apple";
				element.setInnerHTML(value);
				assertEquals("" + label, value, label.getText());
			}
		};
		test.run();
	}

	static interface LabelHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testLabel
		 * @return
		 */
		Label getLabel();
	}

	public void testButton() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createButton();
			}

			@Override
			String getId() {
				return "testButton";
			}

			@Override
			Widget getInstance() {
				final ButtonHtmlTemplateFactory factory = (ButtonHtmlTemplateFactory) GWT.create(ButtonHtmlTemplateFactory.class);
				return factory.getButton();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final Button button = (Button) widget;
				final String value = "apple";
				element.setInnerHTML(value);
				assertEquals("" + button, value, button.getText());
			}
		};
		test.run();
	}

	static interface ButtonHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testButton
		 * @return
		 */
		Button getButton();
	}

	public void testImage() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createImg();
			}

			@Override
			String getId() {
				return "testImage";
			}

			@Override
			Widget getInstance() {
				final ImageHtmlTemplateFactory factory = (ImageHtmlTemplateFactory) GWT.create(ImageHtmlTemplateFactory.class);
				return factory.getImage();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final Image image = (Image) widget;
				final String value = "apple";
				element.setPropertyString("title", value);
				assertEquals("" + image, value, image.getTitle());
			}
		};
		test.run();
	}

	static interface ImageHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testImage
		 * @return
		 */
		Image getImage();
	}

	public void testHyperlink() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createAnchor();
			}

			@Override
			String getId() {
				return "testHyperlink";
			}

			@Override
			Widget getInstance() {
				final HyperlinkHtmlTemplateFactory factory = (HyperlinkHtmlTemplateFactory) GWT
						.create(HyperlinkHtmlTemplateFactory.class);
				return factory.getHyperlink();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final Hyperlink hyperlink = (Hyperlink) widget;
				final String value = "apple";
				element.setInnerHTML(value);
				assertTrue("HyperLink:" + hyperlink + "\nelement:" + element, hyperlink.getHtml().indexOf(value) != -1);
			}
		};
		test.run();
	}

	static interface HyperlinkHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testHyperlink
		 * @return
		 */
		Hyperlink getHyperlink();
	}

	public void testHtml() {
		final TestTemplate test = new TestTemplate() {
			@Override
			Element createElement() {
				return DOM.createDiv();
			}

			@Override
			String getId() {
				return "testHtml";
			}

			@Override
			Widget getInstance() {
				final HtmlHtmlTemplateFactory factory = (HtmlHtmlTemplateFactory) GWT.create(HtmlHtmlTemplateFactory.class);
				return factory.getHtml();
			}

			@Override
			void doRemainingTests(final Element element, final Widget widget) {
				final Html html = (Html) widget;
				final String value = "<b>apple</b>";
				element.setInnerHTML(value);
				assertEquals("" + html, value.toLowerCase(), html.getHtml().toLowerCase());
			}
		};
		test.run();

	}

	static interface HtmlHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testHtml
		 * @return
		 */
		Html getHtml();
	}

	public void testForm() {
		final TestTemplate test = new TestTemplate() {
			Element createElement() {
				final FormElement form = DOM.createForm().cast();
				form.setMethod("POST");
				form.setAction("test");

				// add some form elements.
				final InputElement text = DOM.createInputText().cast();
				text.setValue("apple");
				text.setId("testFormTextBox");
				form.appendChild(text);

				final InputElement password = DOM.createInputPassword().cast();
				password.setValue("banana");
				password.setId("testFormPasswordTextBox");
				form.appendChild(password);

				final SelectElement listBox = DOM.createSelect().cast();
				listBox.setId("testFormListBox");
				form.appendChild(listBox);

				final InputElement checkBox = DOM.createInputCheck().cast();
				checkBox.setId("testFormCheckBox");
				form.appendChild(checkBox);

				final InputElement radioBox = DOM.createInputRadio("group").cast();
				radioBox.setId("testFormRadioButton");
				form.appendChild(radioBox);

				return form.cast();
			}

			String getId() {
				return "testForm";
			}

			Widget getInstance() {
				final FormHtmlTemplateFactory factory = (FormHtmlTemplateFactory) GWT.create(FormHtmlTemplateFactory.class);
				return factory.getForm();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final FormPanel panel = (FormPanel) widget;

				assertTrue("method", "POST".equalsIgnoreCase(panel.getMethod()));
				assertTrue("action", panel.getAction().indexOf("test") != -1);

				assertEquals("widgetCount", 5, panel.getWidgetCount());

				final TextBox textBox = (TextBox) panel.get(0);
				assertNotNull(textBox);

				final TextBox password = (TextBox) panel.get(1);
				assertNotNull(password);

				final ListBox listBox = (ListBox) panel.get(2);
				assertNotNull(listBox);

				final CheckBox checkBox = (CheckBox) panel.get(3);
				assertNotNull(checkBox);

				final RadioButton radioButton = (RadioButton) panel.get(4);
				assertNotNull(radioButton);

				panel.remove(0);
				assertEquals("widgetCount", 4, panel.getWidgetCount());
				assertNull(DOM.getElementById("testFormTextBox"));

				panel.remove(0);
				assertEquals("widgetCount", 3, panel.getWidgetCount());
				assertNull(DOM.getElementById("testFormPasswordTextBox"));

				panel.remove(0);
				assertEquals("widgetCount", 2, panel.getWidgetCount());
				assertNull(DOM.getElementById("testFormListBox"));

				panel.remove(0);
				assertEquals("widgetCount", 1, panel.getWidgetCount());
				assertNull(DOM.getElementById("testFormCheckBox"));

				panel.remove(0);
				assertEquals("widgetCount", 0, panel.getWidgetCount());
				assertNull(DOM.getElementById("testFormRadioButton"));
			}
		};
		test.run();

	}

	static interface FormHtmlTemplateFactory extends HtmlTemplateFactory {
		/**
		 * @id testForm
		 * @return The form bound to a FORM element within the DOM
		 */
		FormPanel getForm();
	}

	public void testIncludesHtmlFile() {
		final HasOnlyHtml template = (HasOnlyHtml) GWT.create(HasOnlyHtml.class);
		final Html html = template.getHtml();
		final String expected = "Lorem";
		final String actual = html.getHtml();
		assertEquals(expected, actual);
	}

	static interface HasOnlyHtml extends HtmlTemplateFactory {
		/**
		 * @file html.txt
		 * @return A new Html file containing the entire contents of the text
		 *         file.
		 */
		Html getHtml();
	}

	public void testTemplateIncludesAValue() {
		final HasHtmlAndValue template = (HasHtmlAndValue) GWT.create(HasHtmlAndValue.class);
		final Html html = template.getHtml();
		final String expected = "Lorem";
		final String actual = html.getHtml();
		assertEquals(expected, actual);
	}

	static interface HasHtmlAndValue extends HtmlTemplateFactory {
		/**
		 * @file value.txt
		 * @return A new Html file containing the entire contents of the text
		 *         file.
		 */
		Html getHtml();
	}

	public void testTemplateFileIncludesJavaCode() {
		final HasHtmlAndJava template = (HasHtmlAndJava) GWT.create(HasHtmlAndJava.class);
		final Html html = template.getHtml();
		final String expected = "Lorem 12345 Ipsum";
		final String actual = html.getHtml();
		assertEquals(expected, actual);
	}

	static interface HasHtmlAndJava extends HtmlTemplateFactory {
		/**
		 * @file html-with-java.txt
		 * @return A new Html file containing the entire contents of the text
		 *         file.
		 */
		Html getHtml();
	}

	public void testTemplateReferencesASingleParameter() {
		final HasHtmlAndReferencesParameter template = (HasHtmlAndReferencesParameter) GWT.create(HasHtmlAndReferencesParameter.class);

		for (int i = 0; i < 10; i++) {
			final Html html = template.getHtml(i);
			final String expected = "" + i;
			final String actual = html.getHtml();
			assertEquals(expected, actual);
		}
	}

	static interface HasHtmlAndReferencesParameter extends HtmlTemplateFactory {
		/**
		 * @file parameter.txt
		 * @return A new Html file containing the entire contents of the text
		 *         file.
		 */
		Html getHtml(int value);
	}

	public void testTemplateReferencesParameters() {
		final HasHtmlAndReferencesParameters template = (HasHtmlAndReferencesParameters) GWT
				.create(HasHtmlAndReferencesParameters.class);

		final boolean booleanValue = true;
		final byte byteValue = 1;
		final short shortValue = 2;
		final int intValue = 3;
		final long longValue = 4;
		final float floatValue = 5;
		final double doubleValue = 6;
		final char charValue = 7;
		final String string = "apple";
		final Object object = "banana";

		final Html html = template.getHtml(booleanValue, byteValue, shortValue, intValue, longValue, floatValue, doubleValue, charValue,
				string, object);
		final String expected = "Lorem " + booleanValue + byteValue + shortValue + intValue + longValue + floatValue + doubleValue
				+ charValue + string + object;
		final String actual = html.getHtml();
		assertEquals(expected, actual);
	}

	static interface HasHtmlAndReferencesParameters extends HtmlTemplateFactory {
		/**
		 * @file parameters.txt
		 * @return A new Html file containing the entire contents of the text
		 *         file.
		 */
		Html getHtml(boolean booleanValue, byte byteValue, short shortValue, int intValue, long longValue, float floatValue,
				double doubleValue, char charValue, String string, Object object);
	}

	public void testComplexTemplate() {
		final Complex template = (Complex) GWT.create(Complex.class);

		for (int i = 0; i < 10; i++) {
			final Html html = template.getHtml(i);
			final String actual = html.getHtml().replaceAll("\n\r", "\n").replace('\r', '\n');

			assertTrue(actual, actual.indexOf("Lorem") != -1);

			for (int j = 0; j < i; j++) {
				assertTrue(actual, actual.indexOf(String.valueOf(j)) != -1);
			}

		}
	}

	static interface Complex extends HtmlTemplateFactory {
		/**
		 * @file complex.txt
		 * @return A new Html file containing the entire contents of the text
		 *         file.
		 */
		Html getHtml(int i);
	}

	/**
	 * A simple template that makes it easy to test wrapping of an elemetn
	 */
	static abstract class TestTemplate {

		public void run() {
			final Element element = this.createElement();
			element.setPropertyString("id", this.getId());

			final Element parent = RootPanel.getBodyElement();
			parent.appendChild(element);
			final int index = DOM.getChildIndex(parent, element);

			final Widget widget = this.getInstance();

			assertTrue("isAttached", widget.isAttached());
			assertSame("parentPanel", RootPanel.get(), widget.getParent());

			final Element widgetElement = widget.getElement();
			assertTrue("element is attached", DOM.isOrHasChild(parent, widgetElement));

			assertTrue("parent", parent == DOM.getParent(widgetElement));
			assertEquals("child index", index, DOM.getChildIndex(parent, widgetElement));

			this.doRemainingTests(element, widget);

			this.removeFromParent(widget);
			this.addToAnotherPanel(widget);
		}

		/**
		 * Creates the element which will be allocated an id and ultimately
		 * wrapped inside a widget by the HtmlTemplateFactory
		 * 
		 * @return
		 */
		abstract Element createElement();

		/**
		 * The id allocated to the test element.
		 * 
		 * @return
		 */
		abstract String getId();

		/**
		 * Sub classes must use deferred binding to create the
		 * HtmlTemplateFactory and retrieve the widget.
		 * 
		 * @return The widget instance being tested.
		 */
		abstract Widget getInstance();

		/**
		 * Further tests to ensure that the wrapped element is functioning
		 * correct.
		 * 
		 * @param element
		 *            The element
		 * @param widget
		 *            The wrapper.
		 */
		abstract void doRemainingTests(Element element, Widget widget);

		void removeFromParent(final Widget widget) {
			assertTrue(widget.isAttached());
			assertNotNull("parent before removing from parent", widget.getParent());

			final Element element = widget.getElement();
			assertNotNull("" + element, element.getParentElement());

			widget.removeFromParent();

			assertFalse(widget.isAttached());
			assertNull("parent after removing from parent", widget.getParent());
			assertNull(element.getParentElement());
		}

		void addToAnotherPanel(final Widget widget) {
			assertFalse(widget.isAttached());
			assertNull("before adding to RootPanel", widget.getParent());

			final Element element = widget.getElement();
			assertNull(element.getParentElement());

			final RootPanel rootPanel = RootPanel.get();
			rootPanel.add(widget);

			assertTrue(widget.isAttached());
			assertSame("parent after adding to RootPanel", rootPanel, widget.getParent());
			assertNotNull(element.getParentElement());
		}
	}

	static void log(final String message) {
		System.out.println(message);
	}
}
