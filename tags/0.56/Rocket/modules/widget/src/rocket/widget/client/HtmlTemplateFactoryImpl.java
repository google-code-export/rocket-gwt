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
package rocket.widget.client;

import java.util.HashMap;
import java.util.Map;

import rocket.util.client.Checker;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for all HtmlTemplateFactory implementations generated by the
 * HtmlTemplateFactory generator.
 * 
 * The generator adds public getter methods for each method defined in the
 * presented interface.
 * 
 * @author Miroslav Pokorny
 */
abstract public class HtmlTemplateFactoryImpl {

	protected HtmlTemplateFactoryImpl() {
		this.setWidgets(this.createWidgets());
	}

	/**
	 * Binds the input element with a type of text identified with the given id
	 * to a TextBox widget instance.
	 * 
	 * @param id
	 *            The id
	 * @return The new TextBox wrapper.
	 */
	protected TextBox bindTextBox(final String id) {
		return new TextBox(this.getElementById(id));
	}

	/**
	 * Binds the input element with a type of password identified with the given
	 * id to a TextBox widget instance.
	 * 
	 * @param id
	 *            The id
	 * @return A PasswordTextBox wrapper
	 */
	protected TextBox bindPasswordTextBox(final String id) {
		return new TextBox(this.getElementById(id));
	}

	/**
	 * Binds the textarea element identified with the given id to a TextArea
	 * widget instance.
	 * 
	 * @param id
	 *            The id
	 * @return A TextArea wrapper
	 */
	protected TextArea bindTextArea(final String id) {
		return new TextArea(this.getElementById(id));
	}

	/**
	 * Binds the input element with type radio, identified with the given id to
	 * a RadioButton widget instance.
	 * 
	 * @param id
	 *            The id
	 * @return A RadioButton wrapper
	 */
	protected RadioButton bindRadioButton(final String id) {
		return new RadioButton(this.getElementById(id));
	}

	/**
	 * Binds the input element with type checkbox, identified with the given id
	 * to a CheckBox widget instance.
	 * 
	 * @param id
	 *            The id
	 * @return A CheckBox wrapper
	 */
	protected CheckBox bindCheckBox(final String id) {
		return new CheckBox(this.getElementById(id));
	}

	/**
	 * Binds the select element, identified with the given id to a ListBox
	 * widget instance.
	 * 
	 * @param id
	 *            The id
	 * @return A ListBox wrapper
	 */
	protected ListBox bindListBox(final String id) {
		return new ListBox(this.getElementById(id));

	}

	/**
	 * Binds the element, identified with the given id to a Label widget
	 * instance.
	 * 
	 * @param id
	 *            The id
	 * @return A Label wrapper
	 */
	protected Label bindLabel(final String id) {
		return new Label(this.getElementById(id));
	}

	/**
	 * Binds the element, identified with the given id to a HTML widget
	 * instance.
	 * 
	 * @param id
	 *            The id
	 * @return A HTML wrapper
	 */
	protected Html bindHtml(final String id) {
		return new Html(this.getElementById(id));
	}

	/**
	 * Binds the button element, identified with the given id to a Button widget
	 * instance.
	 * 
	 * @param id
	 *            The id
	 * @return A Button wrapper
	 */
	protected Button bindButton(final String id) {
		return new Button(this.getElementById(id));
	}

	/**
	 * Binds the element, identified with the given id to a Image widget
	 * instance.
	 * 
	 * @param id
	 *            The id
	 * @return A Image wrapper
	 */
	protected Image bindImage(final String id) {
		return new Image(this.getElementById(id));
	}

	/**
	 * Binds the anchor element, identified with the given id to a Hyperlink
	 * widget instance.
	 * 
	 * @param id
	 *            The id
	 * @return A Hyperlink wrapper
	 */
	protected Hyperlink bindHyperlink(final String id) {
		return new Hyperlink(this.getElementById(id));
	}

	/**
	 * This method locates a formPanel and binds the element to a FormPanel
	 * instance.
	 * 
	 * @param id
	 *            The id of the formPanel.
	 * @return The new FormPanel wrapper.
	 */
	protected FormPanel bindFormPanel(final String id) {
		return new FormPanel(this.getElementById(id), true);
	}

	protected Element getElementById(final String id) {
		final Element element = DOM.getElementById(id);
		if (null == element) {
			throwUnableToGetElementById(id);
		}
		return element;
	}

	private void throwUnableToGetElementById(final String id) {
		throw new RuntimeException("Unable to find an element in the DOM with an id of \"" + id + "\".");
	}

	/**
	 * Fetches a widget given an id from the cache of previous binded and
	 * created widgets.
	 * 
	 * @param id
	 *            The id of the widget
	 * @return The widget instance of null if one was not found.
	 */
	protected Widget getWidget(final String id) {
		Checker.notEmpty("parameter:id", id);

		return (Widget) this.getWidgets().get(id);
	}

	/**
	 * A map containing widgets that have already been created keyed by id.
	 */
	private Map widgets;

	private Map getWidgets() {
		Checker.notNull("field:widgets", widgets);
		return this.widgets;
	}

	private void setWidgets(final Map widgets) {
		Checker.notNull("parameter:widgets", widgets);
		this.widgets = widgets;
	}

	private Map createWidgets() {
		return new HashMap();
	}

	/**
	 * This inner class is used within embedded text files containing code. Code
	 * within <% %> may print to the writer variable.
	 */
	static public class HtmlWriter {

		public HtmlWriter() {
			super();

			this.setStringBuffer(new StringBuffer());
		}

		public void print(final boolean booleanValue) {
			this.getStringBuffer().append(booleanValue);
		}

		public void print(final byte byteValue) {
			this.getStringBuffer().append(byteValue);
		}

		public void print(final short shortValue) {
			this.getStringBuffer().append(shortValue);
		}

		public void print(final int intValue) {
			this.getStringBuffer().append(intValue);
		}

		public void print(final long longValue) {
			this.getStringBuffer().append(longValue);
		}

		public void print(final float floatValue) {
			this.getStringBuffer().append(floatValue);
		}

		public void print(final double doubleValue) {
			this.getStringBuffer().append(doubleValue);
		}

		public void print(final char charValue) {
			this.getStringBuffer().append(charValue);
		}

		public void print(final Object object) {
			this.getStringBuffer().append(object);
		}

		public void print(final String string) {
			this.getStringBuffer().append(string);
		}

		public void println(final boolean booleanValue) {
			this.print(booleanValue);
			this.println();
		}

		public void println(final byte byteValue) {
			this.print(byteValue);
			this.println();
		}

		public void println(final short shortValue) {
			this.print(shortValue);
			this.println();
		}

		public void println(final int intValue) {
			this.print(intValue);
			this.println();
		}

		public void println(final long longValue) {
			this.print(longValue);
			this.println();
		}

		public void println(final float floatValue) {
			this.print(floatValue);
			this.println();
		}

		public void println(final double doubleValue) {
			this.print(doubleValue);
			this.println();
		}

		public void println(final char charValue) {
			this.print(charValue);
			this.println();
		}

		public void println(final Object object) {
			this.print(object);
			this.println();
		}

		public void println(final String string) {
			this.print(string);
			this.println();
		}

		public void println() {
			this.print("\n");
		}

		/**
		 * This string buffer aggregates all printed text. Its contents are then
		 * used to build a Html widget.
		 */
		private StringBuffer stringBuffer;

		private StringBuffer getStringBuffer() {
			return this.stringBuffer;
		}

		private void setStringBuffer(final StringBuffer stringBuffer) {
			this.stringBuffer = stringBuffer;
		}

		public String getString() {
			return this.getStringBuffer().toString();
		}
	}
}
