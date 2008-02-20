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
package rocket.widget.test.spinner.client;

import rocket.event.client.ChangeEvent;
import rocket.event.client.ChangeEventListener;
import rocket.widget.client.Spinner;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class SpinnerTest implements EntryPoint {

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage\"" + caught.getMessage() + "\".");
			}
		});
		final RootPanel rootPanel = RootPanel.get();

		final Label value = new Label("0");
		rootPanel.add(value);

		final Label changeEventCounter = new Label("?");
		rootPanel.add(changeEventCounter);

		final Spinner spinner = new Spinner();
		spinner.setDelta(1);
		spinner.setLowerBounds(0);
		spinner.setUpperBounds(100);
		spinner.setDownImageUrl("down.gif");
		spinner.setUpImageUrl("up.gif");
		spinner.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				value.setText("" + spinner.getValue());
			}
		});
		spinner.addChangeEventListener(new ChangeEventListener() {
			public void onChange(final ChangeEvent event) {
				this.counter++;
				changeEventCounter.setText("ChangeEvent counter: " + this.counter);
			}

			int counter = 0;
		});

		rootPanel.add(spinner);
	}
}
