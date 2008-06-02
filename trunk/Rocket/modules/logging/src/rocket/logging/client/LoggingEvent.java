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
package rocket.logging.client;

import java.io.Serializable;

import rocket.util.client.Checker;

/**
 * This class is a holder for any logged events that will eventually travel to
 * the server.
 * 
 * @author Miroslav Pokorny
 */
public class LoggingEvent implements Serializable {

	public LoggingEvent() {
		super();
	}

	public LoggingEvent(final String name, final LoggingLevel loggingLevel, final String message, final Throwable throwable) {
		this();

		this.setName(name);
		this.setLoggingLevel(loggingLevel);
		this.setMessage(message);
		this.setThrowable(throwable);
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		Checker.notEmpty("parameter:name", name);
		this.name = name;
	}

	private LoggingLevel loggingLevel;

	public LoggingLevel getLoggingLevel() {
		return this.loggingLevel;
	}

	public void setLoggingLevel(final LoggingLevel loggingLevel) {
		Checker.notNull("parameter:loggingLevel", loggingLevel);
		this.loggingLevel = loggingLevel;
	}

	private String message;

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	private Throwable throwable;

	public Throwable getThrowable() {
		return this.throwable;
	}

	/**
	 * Sets the throwable that may accompany this particular message
	 * 
	 * @param throwable
	 *            May be null.
	 */
	public void setThrowable(final Throwable throwable) {
		this.throwable = throwable;
	}

	@Override
	public String toString() {
		return loggingLevel + "-" + message + "-" + throwable;
	}
}
