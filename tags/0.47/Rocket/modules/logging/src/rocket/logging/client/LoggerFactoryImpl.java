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

import java.util.HashMap;
import java.util.Map;

/**
 * Base class which is completed by the {@link LoggingFactoryGenerator}
 * 
 * This class includes logic that caches category queries, so that multiple
 * queries for a particular category will return the same Logger instance.
 * 
 * @author Miroslav Pokorny
 */
abstract public class LoggerFactoryImpl {

	public LoggerFactoryImpl() {
		super();

		this.setLoggers(this.createLoggers());
	}

	public Logger getLogger(final String name) {
		final Map loggers = this.getLoggers();
		Logger logger = (Logger) loggers.get(name);

		// not found in cache try and find...
		if (null == logger) {
			String key = name;
			while (true) {
				logger = this.findLogger(key);
				if (null != logger) {
					break;
				}

				final int dot = key.lastIndexOf('.');
				if (-1 == dot) {
					break;
				}
				key = key.substring(0, dot);
			}

			if (null == logger) {
				logger = this.createRootLogger(name);
			}

			loggers.put(name, logger);
		}
		return logger;
	}

	/**
	 * A map containing cached answers for a category to Logger singleton.
	 */
	private Map loggers;

	protected Map getLoggers() {
		return this.loggers;
	}

	protected void setLoggers(final Map loggers) {
		this.loggers = loggers;
	}

	protected Map createLoggers() {
		return new HashMap();
	}

	/**
	 * This method attempts to locate a Logger implementation for the given
	 * category.
	 * 
	 * @param name
	 * @return
	 */
	abstract protected Logger findLogger(String name);

	/**
	 * Factory which creates a new default logger for the given category.
	 * 
	 * @param name
	 * @return A new logger.
	 */
	abstract protected Logger createRootLogger(String name);
}
