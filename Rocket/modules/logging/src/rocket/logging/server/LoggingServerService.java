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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rocket.logging.client.Logger;
import rocket.logging.client.LoggingEvent;
import rocket.logging.client.LoggingLevel;
import rocket.logging.client.LoggingService;
import rocket.remoting.server.java.JavaRpcServiceMethodInvoker;
import rocket.remoting.server.java.JavaRpcServiceServlet;
import rocket.remoting.server.java.ServerSerializationFactory;
import rocket.serialization.client.ObjectReader;
import rocket.util.client.Checker;

/**
 * This abstract class provides the majority of the code that involves logging
 * an incoming LoggingEvent to a logging target.
 * 
 * Implementations only need to implement {@link #createLoggerAdapter(String)},
 * caching of loggers and the bridge between the level of the message and the
 * logger log method are already implemented.
 * 
 * @author Miroslav Pokorny
 */
abstract public class LoggingServerService extends JavaRpcServiceServlet implements LoggingService {

	protected LoggingServerService() {
		super();

		this.setLoggers(this.createLoggers());
	}

	public void log(final LoggingEvent event) {
		Checker.notNull("parameter:event", event);

		final String loggerName = event.getName();
		final Throwable throwable = event.getThrowable();
		final String text = event.getMessage();

		while (true) {
			final LoggingLevel level = event.getLoggingLevel();

			if (LoggingLevel.DEBUG == level) {
				this.debug(loggerName, text, throwable);
				break;
			}

			if (LoggingLevel.INFO == level) {
				this.info(loggerName, text, throwable);
				break;
			}

			if (LoggingLevel.WARN == level) {
				this.warn(loggerName, text, throwable);
				break;
			}

			if (LoggingLevel.ERROR == level) {
				this.error(loggerName, text, throwable);
				break;
			}

			if (LoggingLevel.FATAL == level) {
				this.fatal(loggerName, text, throwable);
				break;
			}
			break;
		}
	}

	protected Logger getLogger(final String name) {
		final Map loggers = this.getLoggers();
		Logger logger = (Logger) loggers.get(name);
		if (null == logger) {
			logger = this.createLoggerAdapter(name);
			loggers.put(name, logger);
		}

		Checker.notNull(name, logger);
		return logger;
	}

	/**
	 * Sub classes should provide a bridge between the Logger returned by the
	 * LoggerFactory and the Logger interface.
	 * 
	 * @param loggerName
	 * @return
	 */
	abstract protected Logger createLoggerAdapter(String loggerName);

	protected void debug(final String loggerName, final String message, final Throwable throwable) {
		final Logger logger = this.getLogger(loggerName);

		if (null == throwable) {
			logger.debug(message);
		} else {
			logger.debug(message, throwable);
		}
	}

	protected void info(final String loggerName, final String message, final Throwable throwable) {
		final Logger logger = this.getLogger(loggerName);

		if (null == throwable) {
			logger.info(message);
		} else {
			logger.info(message, throwable);
		}
	}

	protected void warn(final String loggerName, final String message, final Throwable throwable) {
		final Logger logger = this.getLogger(loggerName);

		if (null == throwable) {
			logger.warn(message);
		} else {
			logger.warn(message, throwable);
		}
	}

	protected void error(final String loggerName, final String message, final Throwable throwable) {
		final Logger logger = this.getLogger(loggerName);

		if (null == throwable) {
			logger.error(message);
		} else {
			logger.error(message, throwable);
		}
	}

	protected void fatal(final String loggerName, final String message, final Throwable throwable) {
		final Logger logger = this.getLogger(loggerName);

		if (null == throwable) {
			logger.fatal(message);
		} else {
			logger.fatal(message, throwable);
		}
	}

	/**
	 * This cache holds adapters between logger to logging implementation.
	 */
	private Map<String,Logger> loggers;

	protected Map<String,Logger> getLoggers() {
		return this.loggers;
	}

	protected void setLoggers(final Map<String,Logger> loggers) {
		this.loggers = loggers;
	}

	protected Map<String,Logger> createLoggers() {
		return new HashMap<String,Logger>();
	}

	protected JavaRpcServiceMethodInvoker createRpcServiceMethodInvoker() {
		return new JavaRpcServiceMethodInvoker() {
			@Override
			protected ServerSerializationFactory createSerializationFactory() {
				return new ServerSerializationFactory() {
					protected List<ObjectReader> createObjectReaders() {
						final List<ObjectReader> readers = new ArrayList<ObjectReader>();
						readers.add(new LoggingLevelReader());
						readers.addAll(super.createObjectReaders());
						return readers;
					}
				};
			}
		};
	}
}
