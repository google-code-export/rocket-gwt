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
package rocket.remoting.test.comet.client;

import rocket.remoting.client.CometClient;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StackTrace;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class CometTest implements EntryPoint {
	/**
	 * This is the same url as the one used to map the test servlet in the
	 * accompanying *.gwt.xml file.
	 */
	final String COMET_SERVER_URL = "./cometServer";

	final String INVALID_COMET_SERVER_URL = "./invalid";

	final long TOO_MUCH_LAG = 1000;

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert(StackTrace.asString(caught));
			}
		});
		this.createComet();

		final Button start = new Button("Start");
		start.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final CometClient client = CometTest.this.getCometClient();
				client.setUrl(COMET_SERVER_URL);
				client.start();
			}
		});
		RootPanel.get().add(start);

		final Button startUsingInvalidUrl = new Button("Start w/ bad Url(Log should include exceptions)");
		startUsingInvalidUrl.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final CometClient client = CometTest.this.getCometClient();
				client.setUrl(INVALID_COMET_SERVER_URL);
				client.start();
			}
		});
		RootPanel.get().add(startUsingInvalidUrl);

		final Button stop = new Button("Stop");
		stop.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				CometTest.this.getCometClient().stop();
			}
		});
		RootPanel.get().add(stop);

		final Button clearLog = new Button("ClearLog");
		clearLog.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				DOM.setInnerHTML(DOM.getElementById("log"), "");
			}
		});
		RootPanel.get().add(clearLog);
	}

	private CometClient cometClient;

	CometClient getCometClient() {
		ObjectHelper.checkNotNull("field:cometClient", cometClient);
		return this.cometClient;
	}

	void setCometClient(final CometClient cometClient) {
		ObjectHelper.checkNotNull("parameter:cometClient", cometClient);
		this.cometClient = cometClient;
	}

	protected void createComet() {
		final CometClient client = (CometClient) GWT.create(TestCometClient.class);

		client.setCallback(new AsyncCallback() {
			public void onSuccess(final Object result) {
				log("<b>CALLBACK</b> Entering onSuccess() - payload type " + GWT.getTypeName(result) + "=[" + result + "]");

				final TestCometPayload payload = (TestCometPayload) result;
				final long now = System.currentTimeMillis();
				final long serverTime = payload.getDate().getTime();
				final long timeDifference = now - serverTime;
				final boolean deliveredImmediately = timeDifference < TOO_MUCH_LAG;
				log("<b>CALLBACK</b> Calculated time difference between client and server " + timeDifference + "(ms)"
						+ ", deliveredImmediately???: " + deliveredImmediately + ", clientTime: " + now + ", serverTime: " + serverTime);
			}

			public void onFailure(Throwable throwable) {
				String stackTrace = StackTrace.asString(throwable);
				stackTrace = stackTrace.replaceAll("\n", "<br>");
				log("<b>CALLBACK</b> Handling exception<br>" + stackTrace);
			}
		});
		this.setCometClient(client);
	}

	/**
	 * This test class sets the width/height of the hidden iframe so its
	 * contents are visible. All other behaviour remains unchanged.
	 * 
	 * @author Miroslav Pokorny (mP)
	 */
	abstract static public class TestCometClient extends CometClient {

		/**
		 * @comet-payloadType rocket.remoting.test.comet.client.TestCometPayload
		 */
		abstract protected Object createProxy();

		protected Element createFrame() {
			final Element frame = super.createFrame();

			InlineStyle.setInteger(frame, Css.WIDTH, 100, CssUnit.PX);
			InlineStyle.setInteger(frame, Css.HEIGHT, 100, CssUnit.PX);
			InlineStyle.setInteger(frame, Css.BORDER, 1, CssUnit.PX);

			return frame;
		}

		public void start() {
			super.start();
			log("<b>CLIENT</b> Starting new session...");
		}

		public void stop() {
			super.stop();
			log("<b>CLIENT</b> Stopping existing session...");
		}

		public void dispatch(final String serializedForm) throws SerializationException {
			log("<b>CLIENT</b> Dispatching serializedForm [" + serializedForm + "]");
			super.dispatch(serializedForm);
		}

		public void restart() {
			log("<b>CLIENT</b> Restarting new connection to server...");
			super.restart();
		}
	}

	static void log(final Object object) {
		final Element element = DOM.getElementById("log");
		DOM.setInnerHTML(element, DOM.getInnerHTML(element) + "<br>" + object);
	}
}
