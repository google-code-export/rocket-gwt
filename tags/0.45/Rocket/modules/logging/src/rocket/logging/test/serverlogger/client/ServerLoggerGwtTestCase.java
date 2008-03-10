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
package rocket.logging.test.serverlogger.client;

import java.util.List;

import rocket.logging.client.LoggingEvent;
import rocket.logging.client.LoggingLevel;
import rocket.logging.client.ServerLogger;
import rocket.remoting.client.Rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This test consists of a logger which uses an rpc to send logging events to the server.
 * A customised Log4J Appender then logs the events to a list whose contents can be fetched by a second service.
 * The tests then query the second service to determine what was actually logged by LOG4J.
 * 
 * @author Miroslav Pokorny
 */
public class ServerLoggerGwtTestCase extends GWTTestCase {

	final static String LOGGED_EVENTS_SERVICE_URL = "loggedEvents";

	final static String LOGGING_SERVICE_URL = "logging";

	final static int TIMEOUT = 3600 * 1000;

	final static String LOGGER_NAME = "logger";

	final static String MESSAGE = "hello";

	final static int LOGGED_EVENT_DELAY = 100;

	public String getModuleName() {
		return "rocket.logging.test.serverlogger.ServerLogger";
	}

	public void testLoggingEventIsIgnored() {
		this.delayTestFinish(TIMEOUT);

		// first clear any logged events...
		final LoggedEventsServiceAsync events = this.createEventsService();
		events.clearLoggedEvents(new AsyncCallback() {
			public void onSuccess(final Object result) {

				final ServerLogger logger = ServerLoggerGwtTestCase.this.createLogger();
				logger.debug(MESSAGE);

				new Timer() {
					public void run() {
						events.getLoggedEvents(new AsyncCallback() {

							public void onSuccess(final Object result) {
								final List loggedEvents = (List) result;
								assertEquals(loggedEvents + "", 0, loggedEvents.size());

								ServerLoggerGwtTestCase.this.finishTest();
							}

							public void onFailure(final Throwable caught) {
								caught.printStackTrace();
								fail("Failure to get logged events, message: " + caught.getMessage());
							}
						});
					}
				}.schedule(LOGGED_EVENT_DELAY);
			}

			public void onFailure(final Throwable caught) {
				caught.printStackTrace();
				fail("Failure to clear logged events, message: " + caught.getMessage());
			}
		});
	}

	public void testLoggingEventIsRecorded() {
		this.delayTestFinish(TIMEOUT);

		// first clear any logged events...
		final LoggedEventsServiceAsync events = this.createEventsService();
		events.clearLoggedEvents(new AsyncCallback() {
			public void onSuccess(final Object result) {

				final ServerLogger logger = ServerLoggerGwtTestCase.this.createLogger();
				logger.info(MESSAGE);

				new Timer() {
					public void run() {
						events.getLoggedEvents(new AsyncCallback() {

							public void onSuccess(final Object result) {
								final List loggedEvents = (List) result;
								assertEquals(loggedEvents + "", 1, loggedEvents.size());

								final LoggingEvent event = (LoggingEvent) loggedEvents.get(0);
								// assertEquals( "logger name", LOGGER_NAME,
								// event.getName() );
								assertSame(LoggingLevel.INFO, event.getLoggingLevel());
								assertEquals("message", MESSAGE, event.getMessage());
								assertNull(event.getThrowable());

								ServerLoggerGwtTestCase.this.finishTest();
							}

							public void onFailure(final Throwable caught) {
								caught.printStackTrace();
								fail("Failure to get logged events, message: " + caught.getMessage());
							}
						});
					}
				}.schedule(LOGGED_EVENT_DELAY);
			}

			public void onFailure(final Throwable caught) {
				caught.printStackTrace();
				fail("Failure to clear logged events, message: " + caught.getMessage());
			}
		});
	}

	LoggedEventsServiceAsync createEventsService() {
		final LoggedEventsServiceAsync service = (LoggedEventsServiceAsync) GWT.create(LoggedEventsService.class);
		Rpc.setServiceDefTarget(service, LOGGED_EVENTS_SERVICE_URL);
		return service;
	}

	ServerLogger createLogger() {
		return new TestServerLogger( LOGGER_NAME );
	}

	static class TestServerLogger extends ServerLogger {

		public TestServerLogger(final String name) {
			super(name);
		}

		protected String getServiceEntryPoint() {
			return LOGGING_SERVICE_URL;
		}
	}
}