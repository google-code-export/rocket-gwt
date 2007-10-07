package rocket.widget.test.htmltemplatefactory.client;

import rocket.dom.client.Dom;
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
			Element createElement() {
				return DOM.createInputText();
			}

			String getId() {
				return "testTextBox";
			}

			Widget getInstance() {
				final TextBoxHtmlTemplateFactory factory = (TextBoxHtmlTemplateFactory) GWT.create(TextBoxHtmlTemplateFactory.class);
				return factory.getTextBox();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final TextBox textBox = (TextBox) widget;
				final String value = "apple";
				DOM.setElementProperty(element, "value", value);
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
			Element createElement() {
				return DOM.createInputPassword();
			}

			String getId() {
				return "testPasswordTextBox";
			}

			Widget getInstance() {
				final PasswordTextBoxHtmlTemplateFactory factory = (PasswordTextBoxHtmlTemplateFactory) GWT
						.create(PasswordTextBoxHtmlTemplateFactory.class);
				return factory.getPasswordTextBox();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final TextBox password = (TextBox) widget;
				final String value = "apple";
				DOM.setElementProperty(element, "value", value);
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
			Element createElement() {
				return DOM.createTextArea();
			}

			String getId() {
				return "testTextArea";
			}

			Widget getInstance() {
				final TextAreaHtmlTemplateFactory factory = (TextAreaHtmlTemplateFactory) GWT.create(TextAreaHtmlTemplateFactory.class);
				return factory.getTextArea();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final TextArea textArea = (TextArea) widget;
				final String value = "apple";
				DOM.setElementProperty(element, "value", value);
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
			Element createElement() {
				return DOM.createInputRadio("group");
			}

			String getId() {
				return "testRadioButton";
			}

			Widget getInstance() {
				final RadioButtonHtmlTemplateFactory factory = (RadioButtonHtmlTemplateFactory) GWT
						.create(RadioButtonHtmlTemplateFactory.class);
				return factory.getRadioButton();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final RadioButton radioButton = (RadioButton) widget;

				final String name = "testRadioButton";
				radioButton.setName(name);
				assertEquals("element: " + DOM.toString(element) + "\nradioButton: " + radioButton + "\n", name, radioButton.getName());
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
			Element createElement() {
				return DOM.createInputCheck();
			}

			String getId() {
				return "testCheckBox";
			}

			Widget getInstance() {
				final CheckBoxHtmlTemplateFactory factory = (CheckBoxHtmlTemplateFactory) GWT.create(CheckBoxHtmlTemplateFactory.class);
				return factory.getCheckBox();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final CheckBox checkBox = (CheckBox) widget;
				final String name = "testCheckBox";
				checkBox.setName(name);
				assertEquals("element: " + DOM.toString(element) + "\ncheckBox: " + checkBox + "\n", name, checkBox.getName());
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
			Element createElement() {
				final Element select = DOM.createSelect();
				final Element firstOption = DOM.createElement("option");
				DOM.setElementProperty(firstOption, "value", "first");
				DOM.setInnerHTML(firstOption, "first");
				DOM.appendChild(select, firstOption);

				final Element secondOption = DOM.createElement("option");
				DOM.setElementPropertyBoolean(secondOption, "selected", true);
				DOM.setElementProperty(secondOption, "value", "two");
				DOM.setInnerHTML(secondOption, "two");
				DOM.appendChild(select, secondOption);

				return select;
			}

			String getId() {
				return "testListBox";
			}

			Widget getInstance() {
				final ListBoxHtmlTemplateFactory factory = (ListBoxHtmlTemplateFactory) GWT.create(ListBoxHtmlTemplateFactory.class);
				return factory.getListBox();
			}

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
			Element createElement() {
				return DOM.createDiv();
			}

			String getId() {
				return "testLabel";
			}

			Widget getInstance() {
				final LabelHtmlTemplateFactory factory = (LabelHtmlTemplateFactory) GWT.create(LabelHtmlTemplateFactory.class);
				return factory.getLabel();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final Label label = (Label) widget;
				final String value = "apple";
				DOM.setInnerHTML(element, value);
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
			Element createElement() {
				return DOM.createButton();
			}

			String getId() {
				return "testButton";
			}

			Widget getInstance() {
				final ButtonHtmlTemplateFactory factory = (ButtonHtmlTemplateFactory) GWT.create(ButtonHtmlTemplateFactory.class);
				return factory.getButton();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final Button button = (Button) widget;
				final String value = "apple";
				DOM.setInnerHTML(element, value);
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
			Element createElement() {
				return DOM.createImg();
			}

			String getId() {
				return "testImage";
			}

			Widget getInstance() {
				final ImageHtmlTemplateFactory factory = (ImageHtmlTemplateFactory) GWT.create(ImageHtmlTemplateFactory.class);
				return factory.getImage();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final Image image = (Image) widget;
				final String value = "apple";
				DOM.setElementProperty(element, "title", value);
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
			Element createElement() {
				return DOM.createAnchor();
			}

			String getId() {
				return "testHyperlink";
			}

			Widget getInstance() {
				final HyperlinkHtmlTemplateFactory factory = (HyperlinkHtmlTemplateFactory) GWT.create(HyperlinkHtmlTemplateFactory.class);
				return factory.getHyperlink();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final Hyperlink hyperlink = (Hyperlink) widget;
				final String value = "apple";
				DOM.setInnerHTML(element, value);
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
			Element createElement() {
				return DOM.createDiv();
			}

			String getId() {
				return "testHtml";
			}

			Widget getInstance() {
				final HtmlHtmlTemplateFactory factory = (HtmlHtmlTemplateFactory) GWT.create(HtmlHtmlTemplateFactory.class);
				return factory.getHtml();
			}

			void doRemainingTests(final Element element, final Widget widget) {
				final Html html = (Html) widget;
				final String value = "<b>apple</b>";
				DOM.setInnerHTML(element, value);
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
				final Element form = DOM.createForm();
				DOM.setElementAttribute(form, "method", "POST");
				DOM.setElementAttribute(form, "action", "test");

				// add some form elements.
				final Element text = DOM.createInputText();
				DOM.setElementAttribute(text, "value", "apple");
				DOM.setElementAttribute(text, "id", "testFormTextBox");
				DOM.appendChild(form, text);

				final Element password = DOM.createInputPassword();
				DOM.setElementAttribute(password, "value", "banana");
				DOM.setElementAttribute(password, "id", "testFormPasswordTextBox");
				DOM.appendChild(form, password);

				final Element listBox = DOM.createSelect();
				DOM.setElementAttribute(listBox, "id", "testFormListBox");
				DOM.appendChild(form, listBox);

				final Element checkBox = DOM.createInputCheck();
				DOM.setElementAttribute(checkBox, "id", "testFormCheckBox");
				DOM.appendChild(form, checkBox);

				final Element radioBox = DOM.createInputRadio("group");
				DOM.setElementAttribute(checkBox, "id", "testFormRadioButton");
				DOM.appendChild(form, radioBox);

				return form;
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
		 * @return
		 */
		FormPanel getForm();
	}

	/**
	 * A simple template that makes it easy to test wrapping of an elemetn
	 */
	static abstract class TestTemplate {
		public void run() {
			final Element element = this.createElement();
			DOM.setElementProperty(element, "id", this.getId());

			final Element parent = Dom.getBody();
			DOM.appendChild(parent, element);
			final int index = DOM.getChildIndex(parent, element);

			final Widget widget = this.getInstance();

			assertTrue("isAttached", widget.isAttached());
			assertSame("parentPanel", RootPanel.get(), widget.getParent());

			final Element widgetElement = widget.getElement();
			assertTrue("element is attached", DOM.isOrHasChild(parent, widgetElement));

			assertTrue("parent", DOM.compare(parent, DOM.getParent(widgetElement)));
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
			assertNotNull("" + element, DOM.getParent(element));

			widget.removeFromParent();

			assertFalse(widget.isAttached());
			assertNull("parent after removing from parent", widget.getParent());
			assertNull(DOM.getParent(element));
		}

		void addToAnotherPanel(final Widget widget) {
			assertFalse(widget.isAttached());
			assertNull("before adding to RootPanel", widget.getParent());

			final Element element = widget.getElement();
			assertNull(DOM.getParent(element));

			final RootPanel rootPanel = RootPanel.get();
			rootPanel.add(widget);

			assertTrue(widget.isAttached());
			assertSame("parent after adding to RootPanel", rootPanel, widget.getParent());
			assertNotNull(DOM.getParent(element));
		}
	}

	static void log(final String message) {
		// System.out.println( message );
	}
}
