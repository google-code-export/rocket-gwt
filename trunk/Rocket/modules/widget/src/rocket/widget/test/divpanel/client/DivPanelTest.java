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
package rocket.widget.test.divpanel.client;

import java.util.Iterator;

import rocket.testing.client.InteractivePanel;
import rocket.util.client.Checker;
import rocket.widget.client.DivPanel;
import rocket.widget.client.Html;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class DivPanelTest implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage\"" + caught.getMessage() + "\".");
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		final DivPanel panel = new DivPanel();
		rootPanel.add(panel);

		final InteractivePanel interactivePanel = new InteractivePanel<Html>() {
			protected String getCollectionTypeName() {
				return panel.getClass().getName();
			}

			protected int getPanelWidgetCount() {
				return panel.getWidgetCount();
			}

			protected void panelAdd(final Html widget) {
				panel.add(widget);
			}

			protected void panelInsert(final Html widget, final int index) {
				panel.insert(widget, index);
			}

			protected Html panelGet(final int index) {
				return (Html) panel.get(index);
			}

			protected void panelRemove(final Html widget) {
				panel.remove(widget);
			}

			protected Html createElement() {
				return new Html("" + System.currentTimeMillis());
			}

			protected Iterator<Html> panelIterator() {
				return (Iterator) panel.iterator();
			}

			protected void checkType(final Html element) {
				if (false == (element instanceof Html)) {
					Checker.fail("Unknown element type type:" + element.getClass().getName());
				}
			}

			protected String toString(final Html html) {
				return html.getText();
			}
		};
		rootPanel.add(interactivePanel);
	}
}
