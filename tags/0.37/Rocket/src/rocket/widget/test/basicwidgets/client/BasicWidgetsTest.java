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
package rocket.widget.test.basicwidgets.client;

import rocket.browser.client.Browser;
import rocket.event.client.BlurEvent;
import rocket.event.client.ChangeEvent;
import rocket.event.client.ChangeEventListener;
import rocket.event.client.FocusEvent;
import rocket.event.client.FocusEventListener;
import rocket.event.client.ImageLoadEventListener;
import rocket.event.client.ImageLoadFailedEvent;
import rocket.event.client.ImageLoadSuccessEvent;
import rocket.event.client.KeyDownEvent;
import rocket.event.client.KeyEventListener;
import rocket.event.client.KeyPressEvent;
import rocket.event.client.KeyUpEvent;
import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseDoubleClickEvent;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseEventListener;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseOverEvent;
import rocket.event.client.MouseUpEvent;
import rocket.event.client.MouseWheelEvent;
import rocket.event.client.ScrollEvent;
import rocket.event.client.ScrollEventListener;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StackTrace;
import rocket.widget.client.Button;
import rocket.widget.client.CheckBox;
import rocket.widget.client.FileUpload;
import rocket.widget.client.FormPanel;
import rocket.widget.client.Hidden;
import rocket.widget.client.Html;
import rocket.widget.client.Hyperlink;
import rocket.widget.client.Image;
import rocket.widget.client.Label;
import rocket.widget.client.ListBox;
import rocket.widget.client.RadioButton;
import rocket.widget.client.TextArea;
import rocket.widget.client.TextBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;

public class BasicWidgetsTest implements EntryPoint {

	final static String TEXTBOX_ID = "textBox";

	final static String TEXTBOX_HTML_FRAGMENT = "<input id=\"" + TEXTBOX_ID + "\" type=\"text\" value=\"hijacked\">";

	final static String PASSWORD_ID = "password";

	final static String PASSWORD_HTML_FRAGMENT = "<input id=\"" + PASSWORD_ID + "\" type=\"password\" value=\"hijacked\">";

	final static String TEXTAREA_ID = "textArea";

	final static String TEXTAREA_HTML_FRAGMENT = "<textarea id=\"" + TEXTAREA_ID + "\" rows=\"3\" cols=\"10\">Lorem</textarea>";

	final static String RADIO_BUTTON_ID = "radioButton";

	final static String RADIO_BUTTON_HTML_FRAGMENT = "<input id=\"" + RADIO_BUTTON_ID + "\" type=\"radio\" value=\"hijacked\">";

	final static String CHECKBOX_ID = "checkBox";

	final static String CHECKBOX_HTML_FRAGMENT = "<input id=\"" + CHECKBOX_ID + "\" type=\"checkbox\" value=\"hijacked\">";

	final static String LISTBOX_ID = "listBox";

	final static String LISTBOX_HTML_FRAGMENT = "<select id=\"" + LISTBOX_ID
			+ "\">\n<option>foo</option>\n<option>bar</option>\n<option>baz</option>\n</select>";

	final static String IMAGE_ID = "image";

	final static String IMAGE1_URL = "kiwi.jpg"; // Thanx wikipedia!

	final static String IMAGE2_URL = "tomato.jpg"; // Thanx wikipedia!

	final static String IMAGE_HTML_FRAGMENT = "<img id=\"" + IMAGE_ID + "\" src=\"" + IMAGE1_URL + "\">";

	final static String HTML_ID = "html";

	final static String HTML_HTML_FRAGMENT = "<div id=\"" + HTML_ID
			+ "\">\n\tLorem <b>ipsum <i>dolor</i> sit amet</b>, consectetur <u>adipisicing</u> elit\n</div>";

	final static String LABEL_ID = "label";

	final static String LABEL_HTML_FRAGMENT = "<span id=\"" + LABEL_ID
			+ "\">\tLorem ipsum dolor sit amet, consectetur adipisicing elit</span>";

	final static String HYPERLINK_ID = "hyperlink";

	final static String HYPERLINK_HTML_FRAGMENT = "<a id=\"" + HYPERLINK_ID + "\">click here</a>";

	final static String BUTTON_ID = "button";

	final static String RESET_BUTTON_ID = "reset";

	final static String SUBMIT_BUTTON_ID = "submit";

	final static String BUTTON_HTML_FRAGMENT = "<button id=\"" + BUTTON_ID
			+ "\">click here(true button)</button>\n<input type=\"reset\" id=\"" + RESET_BUTTON_ID
			+ "\" value=\"click here(reset)\" />\n<input type=\"submit\" id=\"" + SUBMIT_BUTTON_ID + "\" value=\"click here(submit)\" />";

	final static String FORM_PANEL_ID = "form";

	final static String HIDDEN_ID = "hidden";

	final static String HIDDEN_HTML_FRAGMENT = "<input id=\"" + HIDDEN_ID + "\" name=\"" + HIDDEN_ID
			+ "\" type=\"hidden\" value=\"hidden\">";

	final static String FILE_ID = "file";

	final static String FILE_HTML_FRAGMENT = "<input id=\"" + FILE_ID + "\" name=\"" + FILE_ID + "\" type=\"file\">";

	final static String SUBMIT_URL = Browser.getContextPath() + "/echo";

	final static String FP_TEXTBOX_ID = "formTextBox";

	final static String FP_TEXTBOX_HTML_FRAGMENT = "<input id=\"" + FP_TEXTBOX_ID + "\" name=\"" + FP_TEXTBOX_ID
			+ "\" type=\"text\" value=\"text\">";

	final static String FP_TEXTBOX_UPLOAD_URL = Browser.getContextPath() + "/fileUpload";

	final static String FORM_PANEL_HTML_FRAGMENT = "<form id=\"" + FORM_PANEL_ID + "\">" + FILE_HTML_FRAGMENT + HIDDEN_HTML_FRAGMENT
			+ FP_TEXTBOX_HTML_FRAGMENT + "</form>";

	static final String ENCODING_URLENCODED = "application/x-www-form-urlencoded";

	static final String MULTIPART_FORM_DATA = "multipart/form-data";

	static final String FORM_ENCODING = MULTIPART_FORM_DATA;

	final static String FORM_PANEL_TEXTBOX_ID = "formTextBox";

	static final int WIDGET_LABEL = 0;

	static final int WIDGET = WIDGET_LABEL + 1;

	static final int HTML_FRAGMENT = WIDGET + 1;

	static final int HIJACKED_WIDGET = HTML_FRAGMENT + 1;

	static final int ACTION_BUTTON_COLUMN = HIJACKED_WIDGET + 1;

	static final int CHANGE_LISTENER = ACTION_BUTTON_COLUMN + 1;

	static final int FOCUS_LISTENER = CHANGE_LISTENER + 1;

	static final int BLUR_LISTENER = FOCUS_LISTENER + 1;

	static final int KEY_DOWN_LISTENER = BLUR_LISTENER + 1;

	static final int KEY_PRESS_LISTENER = KEY_DOWN_LISTENER + 1;

	static final int KEY_UP_LISTENER = KEY_PRESS_LISTENER + 1;

	static final int IMAGE_LOAD_SUCCESS_LISTENER = KEY_UP_LISTENER + 1;

	static final int IMAGE_LOAD_FAILED_LISTENER = IMAGE_LOAD_SUCCESS_LISTENER + 1;

	static final int MOUSE_CLICK_LISTENER = IMAGE_LOAD_FAILED_LISTENER + 1;

	static final int MOUSE_DOUBLE_CLICK_LISTENER = MOUSE_CLICK_LISTENER + 1;

	static final int MOUSE_DOWN_LISTENER = IMAGE_LOAD_FAILED_LISTENER + 1;

	static final int MOUSE_MOVE_LISTENER = MOUSE_DOWN_LISTENER + 1;

	static final int MOUSE_OUT_LISTENER = MOUSE_MOVE_LISTENER + 1;

	static final int MOUSE_OVER_LISTENER = MOUSE_OUT_LISTENER + 1;

	static final int MOUSE_UP_LISTENER = MOUSE_OVER_LISTENER + 1;

	static final int MOUSE_WHEEL_LISTENER = MOUSE_UP_LISTENER + 1;

	static final int SCROLL_LISTENER = MOUSE_WHEEL_LISTENER + 1;

	static final int FORM_PANEL_SUBMIT_LISTENER = SCROLL_LISTENER + 1;

	static final int FORM_PANEL_SUBMIT_COMPLETED_LISTENER = FORM_PANEL_SUBMIT_LISTENER + 1;

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();

				Window.alert("Caught " + StackTrace.asString(caught));
			}
		});

		final RootPanel rootPanel = RootPanel.get();

		final Grid grid = new Grid(12, FORM_PANEL_SUBMIT_COMPLETED_LISTENER + 1);
		grid.setBorderWidth(1);
		this.setGrid(grid);

		final RowFormatter rowFormatter = grid.getRowFormatter();
		for (int i = 0; i < grid.getRowCount(); i++) {
			rowFormatter.addStyleName(i, (0 == (i & 1) ? "evenRow" : "oddRow"));

		}

		rootPanel.add(grid);

		int row = 0;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("TextBox"));
		final TextBox textBox = createTextBox(row);
		grid.setWidget(row, WIDGET, textBox);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(TEXTBOX_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(TEXTBOX_HTML_FRAGMENT));
		final TextBox hijackedTextBox = hijackTextBox(row);
		this.buildTextBoxActions(row, textBox, hijackedTextBox);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("PasswordTextBox"));
		final TextBox passwordTextBox = createPasswordTextBox(row);
		grid.setWidget(row, WIDGET, passwordTextBox);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(PASSWORD_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(PASSWORD_HTML_FRAGMENT));
		final TextBox hijackedPasswordTextBox = hijackPasswordTextBox(row);
		this.buildPasswordTextBoxActions(row, passwordTextBox, hijackedPasswordTextBox);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("TextArea"));
		final TextArea textArea = createTextArea(row);
		grid.setWidget(row, WIDGET, textArea);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(TEXTAREA_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(TEXTAREA_HTML_FRAGMENT));
		final TextArea hijackedTextArea = hijackTextArea(row);
		this.buildTextAreaActions(row, textArea, hijackedTextArea);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("RadioButton"));
		final RadioButton radioButton = createRadioButton(row);
		grid.setWidget(row, WIDGET, radioButton);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(RADIO_BUTTON_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(RADIO_BUTTON_HTML_FRAGMENT));
		final RadioButton hijackedRadioButton = hijackRadioButton(row);
		this.buildRadioButtonActions(row, radioButton, hijackedRadioButton);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("CheckBox"));
		final CheckBox checkBox = createCheckBox(row);
		grid.setWidget(row, WIDGET, checkBox);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(CHECKBOX_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(CHECKBOX_HTML_FRAGMENT));
		final CheckBox hijackedCheckBox = hijackCheckBox(row);
		this.buildCheckBoxActions(row, checkBox, hijackedCheckBox);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("listBox"));
		final ListBox listBox = createListBox(row);
		grid.setWidget(row, WIDGET, listBox);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(LISTBOX_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(LISTBOX_HTML_FRAGMENT));
		final ListBox hijackedListBox = hijackListBox(row);
		this.buildListBoxActions(row, listBox, hijackedListBox);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("Image"));
		final Image image = createImage(row);
		grid.setWidget(row, WIDGET, image);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(IMAGE_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(IMAGE_HTML_FRAGMENT));
		final Image hijackedImage = hijackImage(row);
		this.buildImageActions(row, image, hijackedImage);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("Label"));
		final Label label = createLabel(row);
		grid.setWidget(row, WIDGET, label);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(LABEL_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(LABEL_HTML_FRAGMENT));
		final Label hijackedLabel = hijackLabel(row);
		this.buildLabelActions(row, label, hijackedLabel);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("Html"));
		final Html html = createHtml(row);
		grid.setWidget(row, WIDGET, html);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(HTML_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(HTML_HTML_FRAGMENT));
		final Html hijackedHtml = hijackHtml(row);
		this.buildHtmlActions(row, html, hijackedHtml);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("Hyperlink"));
		final Hyperlink hyperlink = createHyperlink(row);
		grid.setWidget(row, WIDGET, hyperlink);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(HYPERLINK_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(HYPERLINK_HTML_FRAGMENT));
		final Hyperlink hijackedHyperlink = hijackHyperlink(row);
		this.buildHyperlinkActions(row, hyperlink, hijackedHyperlink);

		row++;

		grid.setWidget(row, WIDGET_LABEL, createGwtHTML("Button"));
		final Button button = createButton(row);
		grid.setWidget(row, WIDGET, button);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(BUTTON_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(BUTTON_HTML_FRAGMENT));
		final Button hijackedButton = hijackButton(row);
		final Button hijackedResetButton = hijackResetButton(row);
		final Button hijackedSubmitButton = hijackSubmitButton(row);
		this.buildButtonActions(row, button, hijackedButton, hijackedResetButton, hijackedSubmitButton);

		row++;

		grid
				.setWidget(row, WIDGET_LABEL,
						createGwtHTML("FormPanel with three fields<ul><li>TextBox</li><li>Hidden</li><li>File</li></ul>"));
		final FormPanel formPanel = createFormPanel(row);
		grid.setWidget(row, WIDGET, formPanel);
		grid.setWidget(row, HTML_FRAGMENT, createHtmlFragment(FORM_PANEL_HTML_FRAGMENT));
		grid.setWidget(row, HIJACKED_WIDGET, createGwtHTML(FORM_PANEL_HTML_FRAGMENT));
		final FormPanel hijackedFormPanel = hijackFormPanel(row);
		this.buildFormPanelActions(row, formPanel, hijackedFormPanel);

		row++;

	}

	/**
	 * The grid that houses all the widgets.
	 */
	Grid grid;

	Grid getGrid() {
		return grid;
	}

	void setGrid(final Grid grid) {
		this.grid = grid;
	}

	TextBox createTextBox(final int row) {
		final TextBox textBox = new TextBox();
		textBox.setText("Lorem");
		this.addTextBoxEventListeners(textBox, row);
		return textBox;
	}

	TextBox hijackTextBox(final int row) {
		final TextBox textBox = new TextBox(this.getElementById(TEXTBOX_ID));
		this.addTextBoxEventListeners(textBox, row);
		return textBox;
	}

	void addTextBoxEventListeners(final TextBox textBox, final int row) {
		textBox.addChangeEventListener(this.createChangeEventListener(row));
		textBox.addFocusEventListener(this.createFocusEventListener(row));
		textBox.addKeyEventListener(this.createKeyEventListener(row));
	}

	void buildTextBoxActions(final int row, final TextBox textBox, final TextBox hijackedTextBox) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button textBoxSelectAll = new com.google.gwt.user.client.ui.Button("selectAll");
		textBoxSelectAll.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				textBox.selectAll();
			}
		});
		panel.add(textBoxSelectAll);

		final com.google.gwt.user.client.ui.Button textBoxGetSelectedText = new com.google.gwt.user.client.ui.Button("getSelectedText");
		textBoxGetSelectedText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert(textBox.getSelectedText());
			}
		});
		panel.add(textBoxGetSelectedText);

		final com.google.gwt.user.client.ui.Button hijackedTextBoxSelectAll = new com.google.gwt.user.client.ui.Button("selectAll (*)");
		hijackedTextBoxSelectAll.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedTextBox.selectAll();
			}
		});
		panel.add(hijackedTextBoxSelectAll);

		final com.google.gwt.user.client.ui.Button hijackedTextBoxGetSelectedText = new com.google.gwt.user.client.ui.Button(
				"getSelectedText (*)");
		hijackedTextBoxGetSelectedText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert(hijackedTextBox.getSelectedText());
			}
		});
		panel.add(hijackedTextBoxGetSelectedText);
	}

	TextBox createPasswordTextBox(final int row) {
		final TextBox passwordTextBox = new TextBox();
		passwordTextBox.setText("Lorem");
		this.addPasswordTextBoxEventListeners(passwordTextBox, row);
		return passwordTextBox;
	}

	TextBox hijackPasswordTextBox(final int row) {
		final TextBox passwordTextBox = new TextBox(this.getElementById(PASSWORD_ID));
		this.addPasswordTextBoxEventListeners(passwordTextBox, row);
		return passwordTextBox;
	}

	void addPasswordTextBoxEventListeners(final TextBox passwordTextBox, final int row) {
		passwordTextBox.addChangeEventListener(this.createChangeEventListener(row));
		passwordTextBox.addFocusEventListener(this.createFocusEventListener(row));
		passwordTextBox.addKeyEventListener(this.createKeyEventListener(row));
	}

	void buildPasswordTextBoxActions(final int row, final TextBox passwordTextBox, final TextBox hijackedPasswordTextBox) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button passwordTextBoxSelectAll = new com.google.gwt.user.client.ui.Button("selectAll");
		passwordTextBoxSelectAll.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				passwordTextBox.selectAll();
			}
		});
		panel.add(passwordTextBoxSelectAll);

		final com.google.gwt.user.client.ui.Button passwordTextBoxGetSelectedText = new com.google.gwt.user.client.ui.Button(
				"getSelectedText");
		passwordTextBoxGetSelectedText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert(passwordTextBox.getSelectedText());
			}
		});
		panel.add(passwordTextBoxGetSelectedText);

		final com.google.gwt.user.client.ui.Button hijackedPasswordTextBoxSelectAll = new com.google.gwt.user.client.ui.Button(
				"selectAll(*)");
		hijackedPasswordTextBoxSelectAll.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedPasswordTextBox.selectAll();
			}
		});
		panel.add(hijackedPasswordTextBoxSelectAll);

		final com.google.gwt.user.client.ui.Button hijackedPasswordTextBoxGetSelectedText = new com.google.gwt.user.client.ui.Button(
				"getSelectedText (*)");
		hijackedPasswordTextBoxGetSelectedText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert(hijackedPasswordTextBox.getSelectedText());
			}
		});
		panel.add(hijackedPasswordTextBoxGetSelectedText);
	}

	TextArea createTextArea(final int row) {
		final TextArea textArea = new TextArea();
		textArea
				.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		this.addTextAreaEventListeners(textArea, row);
		return textArea;
	}

	TextArea hijackTextArea(final int row) {
		final TextArea textArea = new TextArea(this.getElementById(TEXTAREA_ID));
		this.addTextAreaEventListeners(textArea, row);
		return textArea;
	}

	void addTextAreaEventListeners(final TextArea textArea, final int row) {
		textArea.addChangeEventListener(this.createChangeEventListener(row));
		textArea.addFocusEventListener(this.createFocusEventListener(row));
		textArea.addKeyEventListener(this.createKeyEventListener(row));
	}

	void buildTextAreaActions(final int row, final TextArea textArea, final TextArea hijackedTextArea) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button textAreaSelectAll = new com.google.gwt.user.client.ui.Button("selectAll");
		textAreaSelectAll.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				textArea.selectAll();
			}
		});
		panel.add(textAreaSelectAll);

		final com.google.gwt.user.client.ui.Button textAreaGetSelectedText = new com.google.gwt.user.client.ui.Button("getSelectedText");
		textAreaGetSelectedText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert(textArea.getSelectedText());
			}
		});
		panel.add(textAreaGetSelectedText);

		final com.google.gwt.user.client.ui.Button hijackedTextAreaSelectAll = new com.google.gwt.user.client.ui.Button("selectAll (*)");
		hijackedTextAreaSelectAll.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedTextArea.selectAll();
			}
		});
		panel.add(hijackedTextAreaSelectAll);

		final com.google.gwt.user.client.ui.Button hijackedTextAreaGetSelectedText = new com.google.gwt.user.client.ui.Button(
				"getSelectedText (*)");
		hijackedTextAreaGetSelectedText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Window.alert(hijackedTextArea.getSelectedText());
			}
		});
		panel.add(hijackedTextAreaGetSelectedText);
	}

	RadioButton createRadioButton(final int row) {
		final RadioButton radioButton = new RadioButton("group");
		this.addRadioButtonEventListeners(radioButton, row);
		return radioButton;
	}

	RadioButton hijackRadioButton(final int row) {
		final RadioButton radioButton = new RadioButton(this.getElementById(RADIO_BUTTON_ID));
		this.addRadioButtonEventListeners(radioButton, row);
		return radioButton;
	}

	void addRadioButtonEventListeners(final RadioButton radioButton, final int row) {
		radioButton.addChangeEventListener(this.createChangeEventListener(row));
		radioButton.addFocusEventListener(this.createFocusEventListener(row));
	}

	void buildRadioButtonActions(final int row, final RadioButton radioButton, final RadioButton hijackedRadioButton) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button radioButtonChecked = new com.google.gwt.user.client.ui.Button("check");
		radioButtonChecked.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				radioButton.setChecked(true);
			}
		});
		panel.add(radioButtonChecked);

		final com.google.gwt.user.client.ui.Button radioButtonUnchecked = new com.google.gwt.user.client.ui.Button("uncheck");
		radioButtonUnchecked.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				radioButton.setChecked(false);
			}
		});
		panel.add(radioButtonUnchecked);

		final com.google.gwt.user.client.ui.Button hijackedRadioButtonChecked = new com.google.gwt.user.client.ui.Button("check (*)");
		hijackedRadioButtonChecked.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedRadioButton.setChecked(true);
			}
		});
		panel.add(hijackedRadioButtonChecked);

		final com.google.gwt.user.client.ui.Button hijackedRadioButtonUnchecked = new com.google.gwt.user.client.ui.Button("uncheck (*)");
		hijackedRadioButtonUnchecked.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedRadioButton.setChecked(false);
			}
		});
		panel.add(hijackedRadioButtonUnchecked);
	}

	CheckBox createCheckBox(final int row) {
		final CheckBox checkBox = new CheckBox();
		this.addCheckBoxEventListeners(checkBox, row);
		return checkBox;
	}

	CheckBox hijackCheckBox(final int row) {
		final CheckBox checkBox = new CheckBox(this.getElementById(CHECKBOX_ID));
		this.addCheckBoxEventListeners(checkBox, row);
		return checkBox;
	}

	void addCheckBoxEventListeners(final CheckBox checkBox, final int row) {
		checkBox.addChangeEventListener(this.createChangeEventListener(row));
		checkBox.addFocusEventListener(this.createFocusEventListener(row));
	}

	void buildCheckBoxActions(final int row, final CheckBox checkBox, final CheckBox hijackedCheckBox) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button checkBoxChecked = new com.google.gwt.user.client.ui.Button("check");
		checkBoxChecked.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				checkBox.setChecked(true);
			}
		});
		panel.add(checkBoxChecked);

		final com.google.gwt.user.client.ui.Button checkBoxUnchecked = new com.google.gwt.user.client.ui.Button("uncheck");
		checkBoxUnchecked.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				checkBox.setChecked(false);
			}
		});
		panel.add(checkBoxUnchecked);

		final com.google.gwt.user.client.ui.Button hijackedCheckBoxChecked = new com.google.gwt.user.client.ui.Button("check (*)");
		hijackedCheckBoxChecked.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedCheckBox.setChecked(true);
			}
		});
		panel.add(hijackedCheckBoxChecked);

		final com.google.gwt.user.client.ui.Button hijackedCheckBoxUnchecked = new com.google.gwt.user.client.ui.Button("uncheck (*)");
		hijackedCheckBoxUnchecked.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedCheckBox.setChecked(false);
			}
		});
		panel.add(hijackedCheckBoxUnchecked);
	}

	ListBox createListBox(final int row) {
		final ListBox listBox = new ListBox();
		listBox.addItem("Foo");
		listBox.addItem("Bar");
		listBox.addItem("Baz");
		this.addListBoxEventListeners(listBox, row);
		return listBox;
	}

	ListBox hijackListBox(final int row) {
		final ListBox listBox = new ListBox(this.getElementById(LISTBOX_ID));
		this.addListBoxEventListeners(listBox, row);
		return listBox;
	}

	void addListBoxEventListeners(final ListBox listBox, final int row) {
		listBox.addChangeEventListener(this.createChangeEventListener(row));
		listBox.addFocusEventListener(this.createFocusEventListener(row));
	}

	void buildListBoxActions(final int row, final ListBox listBox, final ListBox hijackedListBox) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button listBoxGetItem = new com.google.gwt.user.client.ui.Button("getItem");
		listBoxGetItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int index = Integer.parseInt(Browser.prompt("index ( 0..." + listBox.getItemCount() + ")", "0"));
				Window.alert("" + listBox.getValue(index));
			}
		});
		panel.add(listBoxGetItem);

		final com.google.gwt.user.client.ui.Button listBoxSetItem = new com.google.gwt.user.client.ui.Button("setItemText");
		listBoxSetItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int count = listBox.getItemCount();
				final int index = Integer.parseInt(Browser.prompt("index ( 0..." + count + ")", "0"));
				final String text = Browser.prompt("item text", "item-" + count);
				listBox.setItemText(index, text);
			}
		});
		panel.add(listBoxSetItem);

		final com.google.gwt.user.client.ui.Button listBoxAddItem = new com.google.gwt.user.client.ui.Button("addItem");
		listBoxAddItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int count = listBox.getItemCount();
				final String text = Browser.prompt("item text", "item-" + count);
				listBox.addItem(text);
			}
		});
		panel.add(listBoxAddItem);

		final com.google.gwt.user.client.ui.Button listBoxRemoveItem = new com.google.gwt.user.client.ui.Button("removeItem");
		listBoxRemoveItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int count = listBox.getItemCount();
				final int index = Integer.parseInt(Browser.prompt("index ( 0..." + count + ")", "0"));
				listBox.removeItem(index);
			}
		});
		panel.add(listBoxRemoveItem);

		final com.google.gwt.user.client.ui.Button listBoxSelectItem = new com.google.gwt.user.client.ui.Button("setSelectItem");
		listBoxSelectItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int count = listBox.getItemCount();
				final int index = Integer.parseInt(Browser.prompt("index ( 0..." + count + ")", "0"));
				listBox.setItemSelected(index, true );
			}
		});
		panel.add(listBoxSelectItem);
		
		final com.google.gwt.user.client.ui.Button hijackedListBoxGetItem = new com.google.gwt.user.client.ui.Button("getItem (*)");
		hijackedListBoxGetItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int index = Integer.parseInt(Browser.prompt("index ( 0..." + hijackedListBox.getItemCount() + ")", "0"));
				Window.alert("" + hijackedListBox.getValue(index));
			}
		});
		panel.add(hijackedListBoxGetItem);

		final com.google.gwt.user.client.ui.Button hijackedListBoxSetItem = new com.google.gwt.user.client.ui.Button("setItemText (*)");
		hijackedListBoxSetItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int count = hijackedListBox.getItemCount();
				final int index = Integer.parseInt(Browser.prompt("index ( 0..." + count + ")", "0"));
				final String text = Browser.prompt("item text", "item-" + count);
				hijackedListBox.setItemText(index, text);
			}
		});
		panel.add(hijackedListBoxSetItem);

		final com.google.gwt.user.client.ui.Button hijackedListBoxAddItem = new com.google.gwt.user.client.ui.Button("addItem (*)");
		hijackedListBoxAddItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int count = hijackedListBox.getItemCount();
				final String text = Browser.prompt("item text", "item-" + count);
				hijackedListBox.addItem(text);
			}
		});
		panel.add(hijackedListBoxAddItem);

		final com.google.gwt.user.client.ui.Button hijackedListBoxRemoveItem = new com.google.gwt.user.client.ui.Button("removeItem (*)");
		hijackedListBoxRemoveItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int count = listBox.getItemCount();
				final int index = Integer.parseInt(Browser.prompt("index ( 0..." + count + ")", "0"));
				hijackedListBox.removeItem(index);
			}
		});
		panel.add(hijackedListBoxRemoveItem);

		final com.google.gwt.user.client.ui.Button hijackedListBoxSelectItem = new com.google.gwt.user.client.ui.Button("setSelectItem (*)");
		hijackedListBoxSelectItem.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final int count = listBox.getItemCount();
				final int index = Integer.parseInt(Browser.prompt("index ( 0..." + count + ")", "0"));
				hijackedListBox.setItemSelected(index, true);
			}
		});
		panel.add(hijackedListBoxSelectItem);
	}

	Image createImage(final int row) {
		final Image image = new Image();
		image.setUrl(IMAGE1_URL);
		this.addImageLoadEventListeners(image, row);
		return image;
	}

	Image hijackImage(final int row) {
		final Image image = new Image(this.getElementById(IMAGE_ID));
		this.addImageLoadEventListeners(image, row);
		return image;
	}

	void addImageLoadEventListeners(final Image image, final int row) {
		image.addFocusEventListener(this.createFocusEventListener(row));
		image.addImageLoadEventListener(this.createImageLoadEventListener(row));
		image.addMouseEventListener(this.createMouseEventListener(row));
	}

	void buildImageActions(final int row, final Image image, final Image hijackedImage) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button imageSetUrl = new com.google.gwt.user.client.ui.Button("load other image");
		imageSetUrl.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final String url = image.getUrl();
				final boolean first = url.toLowerCase().endsWith(IMAGE1_URL.toLowerCase());
				image.setUrl(first ? IMAGE2_URL : IMAGE1_URL);
			}
		});
		panel.add(imageSetUrl);

		final com.google.gwt.user.client.ui.Button hijackedImageSetUrl = new com.google.gwt.user.client.ui.Button("load other image(*)");
		hijackedImageSetUrl.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final String url = hijackedImage.getUrl();
				final boolean first = url.toLowerCase().endsWith(IMAGE1_URL.toLowerCase());
				hijackedImage.setUrl(first ? IMAGE2_URL : IMAGE1_URL);
			}
		});
		panel.add(hijackedImageSetUrl);
	}

	Label createLabel(final int row) {
		final Label label = new Label();
		label.setText("Lorem");
		this.addLabelEventListeners(label, row);
		return label;
	}

	Label hijackLabel(final int row) {
		final Label label = new Label(this.getElementById(LABEL_ID));
		this.addLabelEventListeners(label, row);
		return label;
	}

	void addLabelEventListeners(final Label label, final int row) {
		label.addFocusEventListener(this.createFocusEventListener(row));
		label.addMouseEventListener(this.createMouseEventListener(row));
	}

	void buildLabelActions(final int row, final Label label, final Label hijackedLabel) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button labelSetText = new com.google.gwt.user.client.ui.Button("setText");
		labelSetText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				label.setText(Browser.prompt("Text", label.getText()));
			}
		});
		panel.add(labelSetText);

		final com.google.gwt.user.client.ui.Button hijackedLabelSetText = new com.google.gwt.user.client.ui.Button("setText (*)");
		hijackedLabelSetText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedLabel.setText(Browser.prompt("Text", hijackedLabel.getText()));
			}
		});
		panel.add(hijackedLabelSetText);
	}

	Html createHtml(final int row) {
		final Html html = new Html();
		html.setText("Lorem");
		this.addHtmlEventListeners(html, row);
		return html;
	}

	Html hijackHtml(final int row) {
		final Html html = new Html(this.getElementById(HTML_ID));
		this.addHtmlEventListeners(html, row);
		return html;
	}

	void addHtmlEventListeners(final Html html, final int row) {
		html.addFocusEventListener(this.createFocusEventListener(row));
		html.addMouseEventListener(this.createMouseEventListener(row));
	}

	void buildHtmlActions(final int row, final Html html, final Html hijackedHtml) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button htmlSetText = new com.google.gwt.user.client.ui.Button("setText");
		htmlSetText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				html.setText(Browser.prompt("Text", html.getText()));
			}
		});
		panel.add(htmlSetText);

		final com.google.gwt.user.client.ui.Button htmlSetHtml = new com.google.gwt.user.client.ui.Button("setHtml");
		htmlSetHtml.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				html.setHtml(Browser.prompt("Html", html.getHtml()));
			}
		});
		panel.add(htmlSetHtml);

		final com.google.gwt.user.client.ui.Button hijackedHtmlSetText = new com.google.gwt.user.client.ui.Button("setText (*)");
		hijackedHtmlSetText.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedHtml.setText(Browser.prompt("Text", hijackedHtml.getText()));
			}
		});
		panel.add(hijackedHtmlSetText);

		final com.google.gwt.user.client.ui.Button hijackedHtmlSetHijackedHtml = new com.google.gwt.user.client.ui.Button("setHtml (*)");
		hijackedHtmlSetHijackedHtml.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedHtml.setHtml(Browser.prompt("Html", hijackedHtml.getHtml()));
			}
		});
		panel.add(hijackedHtmlSetHijackedHtml);
	}

	Hyperlink createHyperlink(final int row) {
		final Hyperlink hyperlink = new Hyperlink();
		hyperlink.setText("click here!");
		this.addHyperlinkEventListeners(hyperlink, row);
		return hyperlink;
	}

	Hyperlink hijackHyperlink(final int row) {
		final Hyperlink hyperlink = new Hyperlink(this.getElementById(HYPERLINK_ID));
		this.addHyperlinkEventListeners(hyperlink, row);
		return hyperlink;
	}

	void addHyperlinkEventListeners(final Hyperlink hyperlink, final int row) {
		hyperlink.addFocusEventListener(this.createFocusEventListener(row));
		hyperlink.addMouseEventListener(this.createMouseEventListener(row));
		hyperlink.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				Window.alert("clicked on Hyperlink with innerHTML of \n" + hyperlink.getHtml());
			}
		});
	}

	void buildHyperlinkActions(final int row, final Hyperlink hyperlink, final Hyperlink hijackedHyperlink) {
	}

	Button createButton(final int row) {
		final Button button = new Button();
		button.setHtml("Lorem");
		this.addButtonEventListeners(button, row);
		return button;
	}

	Button hijackButton(final int row) {
		final Button button = new Button(this.getElementById(BUTTON_ID));
		this.addButtonEventListeners(button, row);
		return button;
	}

	Button hijackResetButton(final int row) {
		final Button button = new Button(this.getElementById(RESET_BUTTON_ID));
		this.addButtonEventListeners(button, row);
		return button;
	}

	Button hijackSubmitButton(final int row) {
		final Button button = new Button(this.getElementById(SUBMIT_BUTTON_ID));
		this.addButtonEventListeners(button, row);
		return button;
	}

	void addButtonEventListeners(final Button button, final int row) {
		button.addFocusEventListener(this.createFocusEventListener(row));
		button.addMouseEventListener(this.createMouseEventListener(row));

		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				Window.alert("clicked on Button with innerHTML of \n" + button.getHtml());
			}
		});
	}

	void buildButtonActions(final int row, final Button button, final Button hijackedButton, final Button hijackedResetButton,
			final Button hijackedSubmitButton) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button buttonSetHtml = new com.google.gwt.user.client.ui.Button("setHtml");
		buttonSetHtml.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				button.setHtml(Browser.prompt("Html", button.getText()));
			}
		});
		panel.add(buttonSetHtml);

		final com.google.gwt.user.client.ui.Button hijackedButtonSetHtml = new com.google.gwt.user.client.ui.Button("setHtml (button*)");
		hijackedButtonSetHtml.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedButton.setHtml(Browser.prompt("Html", hijackedButton.getText()));
			}
		});
		panel.add(hijackedButtonSetHtml);

		final com.google.gwt.user.client.ui.Button hijackedResetButtonSetHtml = new com.google.gwt.user.client.ui.Button("setHtml (reset*)");
		hijackedResetButtonSetHtml.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedResetButton.setHtml(Browser.prompt("Html", hijackedResetButton.getText()));
			}
		});
		panel.add(hijackedResetButtonSetHtml);

		final com.google.gwt.user.client.ui.Button hijackedSubmitButtonSetHtml = new com.google.gwt.user.client.ui.Button(
				"setHtml (submit*)");
		hijackedSubmitButtonSetHtml.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedSubmitButton.setHtml(Browser.prompt("Html", hijackedSubmitButton.getText()));
			}
		});
		panel.add(hijackedSubmitButtonSetHtml);
	}

	FormPanel createFormPanel(final int row) {
		final FormPanel formPanel = new FormPanel();
		this.setupFormPanel(formPanel);

		// Order which widgets is important as they fetched by order in addForm*
		final FileUpload fileUpload = new FileUpload();
		fileUpload.setName("fileUpload1");
		formPanel.add(fileUpload);

		final Hidden hidden = new Hidden();
		hidden.setName("hidden");
		hidden.setValue("hidden");
		formPanel.add(hidden);

		final TextBox textBox = new TextBox();
		textBox.setText("Lorem");
		textBox.setName("textBox");
		formPanel.add(textBox);

		this.addFormHandler(formPanel, row);
		return formPanel;
	}

	FormPanel hijackFormPanel(final int row) {
		final FormPanel formPanel = new FormPanel(this.getElementById(FORM_PANEL_ID), true);
		this.setupFormPanel(formPanel);
		this.addFormHandler(formPanel, row);
		return formPanel;
	}

	void setupFormPanel(final FormPanel formPanel) {
		formPanel.setAction(SUBMIT_URL);
		formPanel.setEncoding(FORM_ENCODING);
		formPanel.setMethod("POST");
	}

	void addFormHandler(final FormPanel formPanel, final int row) {
		PrimitiveHelper.checkEquals("formPanel.widget count", 3, formPanel.getWidgetCount());
		int i = 0;
		final FileUpload fileUpload = (FileUpload) formPanel.get(i++);
		final Hidden hidden = (Hidden) formPanel.get(i++);
		final TextBox textBox = (TextBox) formPanel.get(i++);

		formPanel.addFormHandler(new FormHandler() {
			public void onSubmit(final FormSubmitEvent event) {
				BasicWidgetsTest.this.increment(FORM_PANEL_SUBMIT_LISTENER, row);
			}

			public void onSubmitComplete(final FormSubmitCompleteEvent event) {
				BasicWidgetsTest.this.increment(FORM_PANEL_SUBMIT_COMPLETED_LISTENER, row);

				ObjectHelper.checkSame("formPanel", formPanel, event.getSource());

				final String actual = event.getResults();
				final String expected = "" + textBox.getText();

				if (-1 == actual.indexOf(expected)) {
					Window
							.alert("The text echod back after submitting the form(to a hidden iframe) do not contain the text within the textBox.\nexpected: ["
									+ expected + "]\nactual[" + actual + "]");

				}
			}
		});
	}

	void buildFormPanelActions(final int row, final FormPanel formPanel, final FormPanel hijackedFormPanel) {

		final VerticalPanel panel = new VerticalPanel();
		this.getGrid().setWidget(row, ACTION_BUTTON_COLUMN, panel);

		final com.google.gwt.user.client.ui.Button submitFormPanel = new com.google.gwt.user.client.ui.Button("submit");
		submitFormPanel.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				formPanel.submit();
			}
		});
		panel.add(submitFormPanel);

		final com.google.gwt.user.client.ui.Button hijackedSubmitFormPanel = new com.google.gwt.user.client.ui.Button("submit (*)");
		hijackedSubmitFormPanel.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				hijackedFormPanel.submit();
			}
		});

		panel.add(hijackedSubmitFormPanel);
	}

	ChangeEventListener createChangeEventListener(final int row) {
		return new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				BasicWidgetsTest.this.increment(CHANGE_LISTENER, row);
			}
		};
	}

	FocusEventListener createFocusEventListener(final int row) {
		return new FocusEventListener() {
			public void onFocus(final FocusEvent event) {
				BasicWidgetsTest.this.increment(FOCUS_LISTENER, row);
			}

			public void onBlur(final BlurEvent event) {
				BasicWidgetsTest.this.increment(BLUR_LISTENER, row);
			}
		};
	}

	KeyEventListener createKeyEventListener(final int row) {
		return new KeyEventListener() {
			public void onKeyDown(final KeyDownEvent event) {
				BasicWidgetsTest.this.increment(KEY_DOWN_LISTENER, row);
			}

			public void onKeyPress(final KeyPressEvent event) {
				BasicWidgetsTest.this.increment(KEY_PRESS_LISTENER, row);
			}

			public void onKeyUp(final KeyUpEvent event) {
				BasicWidgetsTest.this.increment(KEY_UP_LISTENER, row);
			}
		};
	}

	ImageLoadEventListener createImageLoadEventListener(final int row) {
		return new ImageLoadEventListener() {
			public void onSuccess(final ImageLoadSuccessEvent event) {
				BasicWidgetsTest.this.increment(IMAGE_LOAD_SUCCESS_LISTENER, row);
			}

			public void onFailed(final ImageLoadFailedEvent event) {
				BasicWidgetsTest.this.increment(IMAGE_LOAD_FAILED_LISTENER, row);
			}
		};
	}

	MouseEventListener createMouseEventListener(final int row) {
		return new MouseEventListener() {
			public void onClick(final MouseClickEvent event) {
				BasicWidgetsTest.this.increment(MOUSE_CLICK_LISTENER, row);
			}

			public void onDoubleClick(final MouseDoubleClickEvent event) {
				BasicWidgetsTest.this.increment(MOUSE_DOUBLE_CLICK_LISTENER, row);
			}

			public void onMouseDown(final MouseDownEvent event) {
				BasicWidgetsTest.this.increment(MOUSE_DOWN_LISTENER, row);
			}

			public void onMouseMove(final MouseMoveEvent event) {
				BasicWidgetsTest.this.increment(MOUSE_MOVE_LISTENER, row);
			}

			public void onMouseOut(final MouseOutEvent event) {
				BasicWidgetsTest.this.increment(MOUSE_OUT_LISTENER, row);
			}

			public void onMouseOver(final MouseOverEvent event) {
				BasicWidgetsTest.this.increment(MOUSE_OVER_LISTENER, row);
			}

			public void onMouseUp(final MouseUpEvent event) {
				BasicWidgetsTest.this.increment(MOUSE_UP_LISTENER, row);
			}

			public void onMouseWheel(final MouseWheelEvent event) {
				BasicWidgetsTest.this.increment(MOUSE_WHEEL_LISTENER, row);
			}
		};
	}

	ScrollEventListener createScrollEventListener(final int row) {
		return new ScrollEventListener() {
			public void onScroll(final ScrollEvent event) {
				BasicWidgetsTest.this.increment(SCROLL_LISTENER, row);
			}
		};
	}

	com.google.gwt.user.client.ui.Widget createHtmlFragment(final String html) {
		return new com.google.gwt.user.client.ui.Label(html);
	}

	com.google.gwt.user.client.ui.Label createGwtLabel(final String text) {
		return new com.google.gwt.user.client.ui.Label(text);
	}

	com.google.gwt.user.client.ui.HTML createGwtHTML(final String html) {
		return new com.google.gwt.user.client.ui.HTML(html);
	}

	void increment(final int column, final int row) {
		final Grid grid = this.getGrid();
		Counter counter = (Counter) grid.getWidget(row, column);
		if (null == counter) {
			counter = new Counter();
			grid.setWidget(row, column, counter);
		}
		counter.increment();
	}

	static class Counter extends com.google.gwt.user.client.ui.Label {
		Counter() {
			this.setText("0");
		}

		void increment() {
			this.counter++;
			this.setText("" + counter);
		}

		int counter = 0;
	}

	Element getElementById(final String id) {
		final Element element = DOM.getElementById(id);
		ObjectHelper.checkNotNull("element with id of [" + id + "]", element);
		return element;
	}

}
