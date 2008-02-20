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
package rocket.widget.test.autocompletetextbox.client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.Utilities;
import rocket.widget.client.AutoCompleteTextBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class AutoCompleteTextBoxTest implements EntryPoint {

	final static String WORDS = "New Caledonia, New England,New France,New Jersey,New Mexico,New Orleans,New Spain,New South Wales,New York,New York State,New Zealand";

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage\"" + caught.getMessage() + "\".");
			}
		});
		final RootPanel rootPanel = RootPanel.get();

		final List matchCandidates = Arrays.asList(Utilities.split(WORDS, ",", true));

		final StringBuffer buf = new StringBuffer();
		buf.append("<ul>");

		final Iterator iterator = matchCandidates.iterator();
		while (iterator.hasNext()) {
			buf.append("<li>");
			buf.append(iterator.next());
			buf.append("</li>");
		}
		buf.append("</ul>");

		rootPanel.add(new HTML(buf.toString()));

		final AutoCompleteTextBox autoCompleteTextBox = new AutoCompleteTextBox() {
			public void buildDropDownList() {
				final String text = this.getText();
				this.clear();

				if (text.length() > 0) {
					final Iterator iterator = matchCandidates.iterator();
					while (iterator.hasNext()) {
						final String test = (String) iterator.next();
						if (Utilities.startsWithIgnoringCase(test, text)) {
							this.add(test);
						}
					}
				}
			}
		};
		autoCompleteTextBox.setWidth("200px");
		rootPanel.add(autoCompleteTextBox);
		autoCompleteTextBox.setFocus(true);
	}
}
