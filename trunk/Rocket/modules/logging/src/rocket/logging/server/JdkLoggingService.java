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


import java.util.logging.Level;

import rocket.logging.client.Logger;

/**
 * This logging services sends all incoming logging events to log4j sourced loggers.
 * Fatal level messages are piped to severe.
 * 
 * @author Miroslav Pokorny
 */
public class JdkLoggingService extends LoggingServerService {

	@Override
	protected Logger createLoggerAdapter(final String loggerName) {
		final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(loggerName);
		return new Logger() {
			public void debug(final String message) {
				logger.fine(message);
			}

			public void debug(final String message, final Throwable throwable) {
				logger.log( Level.FINE, message, throwable);
			}

			public void info(final String message) {
				logger.info(message);
			}

			public void info(final String message, final Throwable throwable) {
				logger.log( Level.INFO, message, throwable);
			}

			public void warn(final String message) {
				logger.warning(message);
			}

			public void warn(final String message, final Throwable throwable) {
				logger.log( Level.WARNING, message, throwable);
			}

			public void error(final String message) {
				logger.severe(message);
			}

			public void error(final String message, final Throwable throwable) {
				logger.log( Level.SEVERE, message, throwable);
			}

			public void fatal(final String message) {
				logger.severe(message);
			}

			public void fatal(final String message, final Throwable throwable) {
				logger.log( Level.INFO, message, throwable);
			}

			public boolean isDebugEnabled() {
				return logger.isLoggable( Level.FINE );
			}

			public boolean isInfoEnabled() {
				return logger.isLoggable( Level.INFO );
			}

			public boolean isWarnEnabled() {
				return logger.isLoggable( Level.WARNING );
			}

			public boolean isErrorEnabled() {
				return logger.isLoggable( Level.SEVERE );
			}

			public boolean isFatalEnabled() {
				return logger.isLoggable( Level.SEVERE );
			}
		};
	}

}
