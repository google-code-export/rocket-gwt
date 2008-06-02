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
package rocket.logging.test.serverlogger.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import rocket.logging.client.LoggingLevel;

/**
 * A simple appender that simply records all log4j LoggedEvents as rocket
 * LoggedEvents. The parent GwtTestCase will then fetch the recorded events for
 * some asserts.
 */
public class TestAppender implements Appender {

	static void clear() {
		TestAppender.events = new ArrayList<rocket.logging.client.LoggingEvent>();
	}

	static List<rocket.logging.client.LoggingEvent> events;

	static List<rocket.logging.client.LoggingEvent> getEvents() {
		return TestAppender.events;
	}

	static void addEvent(final String loggerName, final LoggingLevel level, final Object object, final Throwable throwable) {
		final rocket.logging.client.LoggingEvent event = new rocket.logging.client.LoggingEvent();
		event.setName(loggerName);
		event.setLoggingLevel(level);
		event.setMessage(object.toString());
		event.setThrowable(throwable);

		TestAppender.getEvents().add(event);
	}

	public void doAppend(final LoggingEvent event) {
		LoggingLevel loggingLevel = null;

		while (true) {
			final Level level = event.getLevel();

			if (Level.DEBUG == level) {
				loggingLevel = LoggingLevel.DEBUG;
				break;
			}

			if (Level.INFO == level) {
				loggingLevel = LoggingLevel.INFO;
				break;
			}

			if (Level.WARN == level) {
				loggingLevel = LoggingLevel.WARN;
				break;
			}
			if (Level.ERROR == level) {
				loggingLevel = LoggingLevel.ERROR;
				break;
			}
			if (Level.FATAL == level) {
				loggingLevel = LoggingLevel.FATAL;
				break;
			}
			if (Level.OFF == level) {
				loggingLevel = LoggingLevel.NONE;
				break;
			}

			throw new IllegalArgumentException("Unknown logging level: " + level);
		}

		final ThrowableInformation throwableInformation = event.getThrowableInformation();
		final Throwable throwable = null == throwableInformation ? null : throwableInformation.getThrowable();

		final String loggerName = this.getName();
		addEvent(loggerName, loggingLevel, String.valueOf(event.getMessage()), throwable);
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void addFilter(final Filter newFilter) {
		throw new UnsupportedOperationException();
	}

	public Filter getFilter() {
		return null;
	}

	public void clearFilters() {
	}

	public void close() {
	}

	private ErrorHandler errorHandler;

	public ErrorHandler getErrorHandler() {
		return this.errorHandler;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	private Layout layout;

	public Layout getLayout() {
		return this.layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public boolean requiresLayout() {
		return false;
	}
}
