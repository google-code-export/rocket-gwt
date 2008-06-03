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
package rocket.logging.test.application.client;

import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.logging.client.Logger;
import rocket.logging.client.LoggerFactory;
import rocket.util.client.JavaScript;
import rocket.widget.client.Button;
import rocket.widget.client.Html;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class LoggingApplication implements EntryPoint {

	public void onModuleLoad() {
		this.addReloadButtons();
		this.addDoLogButtons();
		this.addFetchJavascriptSourceButton();
	}

	protected void addReloadButtons() {
		final HorizontalPanel horizontalPanel = new HorizontalPanel();

		final String baseUrl = GWT.getHostPageBaseURL() + "LoggingApplication.html";

		horizontalPanel.add(this.createButton("Reload with logging", baseUrl + "?logging=yes"));
		horizontalPanel.add(this.createButton("Reload without logging", baseUrl + "?logging=no"));

		RootPanel.get().add(horizontalPanel);
	}

	Button createButton(final String text, final String url) {
		final Button button = new Button(text);
		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				Location.assign(url);
			}
		});

		button.setTitle(url);

		return button;
	}

	protected void addDoLogButtons() {
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(this.createDebugButton());
		horizontalPanel.add(this.createInfoButton());
		horizontalPanel.add(this.createWarnButton());
		horizontalPanel.add(this.createErrorButton());
		horizontalPanel.add(this.createFatalButton());

		RootPanel.get().add(horizontalPanel);
	}

	protected Button createDebugButton() {
		final Logger logger = LoggerFactory.getLogger("DEBUG");

		final Button button = new Button("DEBUG");
		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				logger.debug("A DEBUG level message");
			}
		});
		return button;
	}

	protected Button createInfoButton() {
		final Logger logger = LoggerFactory.getLogger("INFO");

		final Button button = new Button("INFO");
		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				logger.info("A INFO level message");
			}
		});
		return button;
	}

	protected Button createWarnButton() {
		final Logger logger = LoggerFactory.getLogger("WARN");

		final Button button = new Button("WARN");
		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				logger.warn("A WARN level message");
			}
		});
		return button;
	}

	protected Button createErrorButton() {
		final Logger logger = LoggerFactory.getLogger("ERROR");

		final Button button = new Button("ERROR");
		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				logger.error("A ERROR level message");
			}
		});
		return button;
	}

	protected Button createFatalButton() {
		final Logger logger = LoggerFactory.getLogger("FATAL");

		final Button button = new Button("FATAL");
		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				logger.fatal("A FATAL level message");
			}
		});
		return button;
	}

	protected void addFetchJavascriptSourceButton() {
		final Button button = new Button("Fetch generated javascript");
		button.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				LoggingApplication.this.fetchJavascriptSource();
			}
		});

		RootPanel.get().add(button);
	}

	// FIXME Unable to read src from the iframe, therefore the code fails when
	// attempting to build a request to the server for the appropriate file.
	protected void fetchJavascriptSource() {
		try {
			final Element iframe = DOM.getElementById(GWT.getModuleName());
			final String iframeSourceUrl = JavaScript.getString(iframe, "src");
			final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, iframeSourceUrl);
			requestBuilder.sendRequest("", new RequestCallback() {
				public void onResponseReceived(final Request request, final Response response) {
					RootPanel.get().add(new Html("<textarea style=\"width: 99%; height: 50em\" >" + response.getText() + "</textarea>"));
				}

				public void onError(final Request request, final Throwable caught) {
					caught.printStackTrace();
				}
			});
		} catch (final RequestException caught) {
			caught.printStackTrace();
		}
	}
}
