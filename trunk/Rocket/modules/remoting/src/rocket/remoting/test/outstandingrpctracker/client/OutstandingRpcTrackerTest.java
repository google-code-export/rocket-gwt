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
package rocket.remoting.test.outstandingrpctracker.client;

import rocket.remoting.client.OustandingRpcTracker;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class OutstandingRpcTrackerTest implements EntryPoint {

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				System.err.println("caught:" + caught.getMessage());
				caught.printStackTrace();
			}
		});

		RootPanel root = RootPanel.get();

		final HTML loading = new HTML("LOADING...");
		final Element loadingElement = loading.getElement();
		final InlineStyle loadingInlineStyle = InlineStyle.getInlineStyle(loadingElement);
		loadingInlineStyle.setString( Css.BACKGROUND_COLOR, "red");
		loadingInlineStyle.setString( Css.COLOR, "white");
		loadingInlineStyle.setString( Css.POSITION, "absolute");
		loadingInlineStyle.setInteger( Css.RIGHT, 0, CssUnit.PX);
		loadingInlineStyle.setInteger( Css.TOP, 0, CssUnit.PX);

		root.add(loading);

		final OustandingRpcTracker tracker = new OustandingRpcTracker();
		tracker.setWidget(loading);

		root.add(createFakeRpcButton(tracker, 500));
		root.add(createFakeRpcButton(tracker, 1000));
		root.add(createFakeRpcButton(tracker, 2000));
		root.add(createFakeRpcButton(tracker, 5000));
		root.add(createFakeRpcButton(tracker, 10000));
	}

	Button createFakeRpcButton(final OustandingRpcTracker tracker, final int delay) {
		final Button button = new Button("" + delay);
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final AsyncCallback callback = tracker.prepare(new AsyncCallback() {
					public void onSuccess(final Object result) {
						log("\tRPC - " + delay + " request succeeded...");
					}

					public void onFailure(final Throwable cause) {
						log("\tRPC - " + delay + " request failed...");
					}
				});

				final Timer timer = new Timer() {
					public void run() {
						if (Random.nextBoolean()) {
							callback.onSuccess(null);
						} else {
							callback.onFailure(null);
						}

					}
				};
				timer.schedule(delay);
				log("RPC - " + delay + " request initiated...");
			}
		});
		return button;
	}

	void log(String message) {
		System.out.println(message);
	}
}
