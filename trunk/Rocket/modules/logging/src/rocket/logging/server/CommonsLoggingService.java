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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rocket.logging.client.Logger;

/**
 * This logging services sends all incoming logging events to loggers sourced from commons logging
 * 
 * @author Miroslav Pokorny
 */
public class CommonsLoggingService extends LoggingServerService {

	@Override
	protected Logger createLoggerAdapter(final String loggerName) {
		final Log log = LogFactory.getLog(loggerName);
		return new Logger() {
			public void debug(final String message) {
				log.debug(message);
			}

			public void debug(final String message, final Throwable throwable) {
				log.debug(message, throwable);
			}

			public void info(final String message) {
				log.info(message);
			}

			public void info(final String message, final Throwable throwable) {
				log.info(message, throwable);
			}

			public void warn(final String message) {
				log.warn(message);
			}

			public void warn(final String message, final Throwable throwable) {
				log.warn(message, throwable);
			}

			public void error(final String message) {
				log.error(message);
			}

			public void error(final String message, final Throwable throwable) {
				log.error(message, throwable);
			}

			public void fatal(final String message) {
				log.fatal(message);
			}

			public void fatal(final String message, final Throwable throwable) {
				log.fatal(message, throwable);
			}

			public boolean isDebugEnabled() {
				return log.isDebugEnabled();
			}

			public boolean isInfoEnabled() {
				return log.isInfoEnabled();
			}

			public boolean isWarnEnabled() {
				return log.isWarnEnabled();
			}

			public boolean isErrorEnabled() {
				return log.isErrorEnabled();
			}

			public boolean isFatalEnabled() {
				return log.isFatalEnabled();
			}
		};
	}

}
