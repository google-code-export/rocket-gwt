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
package rocket.logging.server;

import org.apache.log4j.Priority;

import rocket.logging.client.Logger;

/**
 * This logging services sends all incoming logging events to log4j sourced
 * loggers.
 * 
 * @author Miroslav Pokorny
 */
public class Log4jLoggingService extends LoggingServerService {

	@Override
	protected Logger createLoggerAdapter(final String loggerName) {
		final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(loggerName);
		return new Logger() {
			public void debug(final String message) {
				logger.debug(message);
			}

			public void debug(final String message, final Throwable throwable) {
				logger.debug(message, throwable);
			}

			public void info(final String message) {
				logger.info(message);
			}

			public void info(final String message, final Throwable throwable) {
				logger.info(message, throwable);
			}

			public void warn(final String message) {
				logger.warn(message);
			}

			public void warn(final String message, final Throwable throwable) {
				logger.warn(message, throwable);
			}

			public void error(final String message) {
				logger.error(message);
			}

			public void error(final String message, final Throwable throwable) {
				logger.error(message, throwable);
			}

			public void fatal(final String message) {
				logger.fatal(message);
			}

			public void fatal(final String message, final Throwable throwable) {
				logger.fatal(message, throwable);
			}

			public boolean isDebugEnabled() {
				return logger.isDebugEnabled();
			}

			public boolean isInfoEnabled() {
				return logger.isInfoEnabled();
			}

			public boolean isWarnEnabled() {
				return logger.isEnabledFor(Priority.WARN);
			}

			public boolean isErrorEnabled() {
				return logger.isEnabledFor(Priority.ERROR);
			}

			public boolean isFatalEnabled() {
				return logger.isEnabledFor(Priority.FATAL);
			}
		};
	}

}
