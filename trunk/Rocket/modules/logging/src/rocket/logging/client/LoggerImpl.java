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

import rocket.util.client.Checker;

/**
 * Logger which funnels all logged messages thru
 * {@link #log(LoggingLevel, String)} or
 * {@link #log(LoggingLevel, String, Throwable)}.
 * 
 * All sub classes are required to have a constructor that takes a String
 * parameter in order to be useable by a LoggerFactory.
 * 
 * @author Miroslav Pokorny
 */
abstract public class LoggerImpl implements Logger {

	protected LoggerImpl() {
		super();
	}

	protected LoggerImpl(final String name) {
		super();

		this.setName(name);
	}

	public void debug(final String message) {
		this.log(LoggingLevel.DEBUG, message);
	}

	public void debug(String message, final Throwable throwable) {
		this.log(LoggingLevel.DEBUG, message, throwable);
	}

	public void info(final String message) {
		this.log(LoggingLevel.INFO, message);
	}

	public void info(String message, final Throwable throwable) {
		this.log(LoggingLevel.INFO, message, throwable);
	}

	public void warn(final String message) {
		this.log(LoggingLevel.WARN, message);
	}

	public void warn(String message, final Throwable throwable) {
		this.log(LoggingLevel.WARN, message, throwable);
	}

	public void error(final String message) {
		this.log(LoggingLevel.ERROR, message);
	}

	public void error(String message, final Throwable throwable) {
		this.log(LoggingLevel.ERROR, message, throwable);
	}

	public void fatal(final String message) {
		this.log(LoggingLevel.FATAL, message);
	}

	public void fatal(String message, final Throwable throwable) {
		this.log(LoggingLevel.FATAL, message, throwable);
	}

	abstract protected void log(final LoggingLevel level, String message);

	abstract protected void log(final LoggingLevel level, String message, final Throwable throwable);

	public boolean isDebugEnabled() {
		return true;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public boolean isWarnEnabled() {
		return true;
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public boolean isFatalEnabled() {
		return true;
	}

	private String name;

	public String getName() {
		Checker.notEmpty("field:name", name);
		return this.name;
	}

	public void setName(final String name) {
		Checker.notEmpty("parameter:name", name);
		this.name = name;
	}
}
