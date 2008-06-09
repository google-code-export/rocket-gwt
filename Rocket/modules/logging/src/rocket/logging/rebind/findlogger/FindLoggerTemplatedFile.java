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
package rocket.logging.rebind.findlogger;

import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.util.client.Checker;

/**
 * An abstraction for the find-logger template
 * 
 * @author Miroslav Pokorny
 */
public class FindLoggerTemplatedFile extends TemplatedFileCodeBlock {

	public FindLoggerTemplatedFile() {
		super();
		this.setLoggers(this.createLoggers());
	}

	/**
	 * Aggregates categories to target logger constructors.
	 */
	private Set loggers;

	protected Set getLoggers() {
		Checker.notNull("field:loggers", loggers);
		return this.loggers;
	}

	protected void setLoggers(final Set loggers) {
		Checker.notNull("parameter:loggers", loggers);
		this.loggers = loggers;
	}

	protected Set createLoggers() {
		return new TreeSet(new Comparator() {
			public int compare(final Object logger, final Object otherLogger) {
				return compare((Logger) logger, (Logger) otherLogger);
			}

			int compare(final Logger logger, final Logger otherLogger) {
				return logger.getCategory().compareTo(otherLogger.getCategory());
			}
		});
	}

	public void register(final String category, final Constructor loggingLevelLogger, final Constructor logger) {
		Checker.notNull("parameter:category", category);
		Checker.notNull("parameter:loggingLevelLogger", loggingLevelLogger);
		Checker.notNull("parameter:logger", logger);

		this.getLoggers().add(new Logger(category, loggingLevelLogger, logger));
	}

	static class Logger {
		Logger(final String category, final Constructor loggingLevelLogger, final Constructor logger) {
			super();

			this.setCategory(category);
			this.setLoggingLevelLogger(loggingLevelLogger);
			this.setLogger(logger);
		}

		String category;

		String getCategory() {
			return this.category;
		}

		void setCategory(final String category) {
			this.category = category;
		}

		Constructor loggingLevelLogger;

		Constructor getLoggingLevelLogger() {
			return this.loggingLevelLogger;
		}

		void setLoggingLevelLogger(final Constructor loggingLevelLogger) {
			this.loggingLevelLogger = loggingLevelLogger;
		}

		Constructor logger;

		Constructor getLogger() {
			return this.logger;
		}

		void setLogger(final Constructor logger) {
			this.logger = logger;
		}
	}

	protected CodeBlock getCaseStatementsAsCodeBlock() {
		final IfThenCreateLoggerTemplatedFile template = new IfThenCreateLoggerTemplatedFile();

		return new CollectionTemplatedCodeBlock<Logger>() {

			@Override
			public boolean isNative() {
				return template.isNative();
			}

			@Override
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			@Override
			protected Collection<Logger> getCollection() {
				return FindLoggerTemplatedFile.this.getLoggers();
			}

			@Override
			protected void prepareToWrite(final Logger logger ) {
				template.setName(logger.getCategory());
				template.setLogger(logger.getLogger());
				template.setLoggingLevelLogger(logger.getLoggingLevelLogger());
			}

			@Override
			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}

	protected String getResourceName() {
		return Constants.FIND_LOGGER_TEMPLATE;
	}

	protected Object getValue0(final String name) {
		Object value = null;

		while (true) {
			if (Constants.FIND_LOGGER_IF_THEN_STATEMENTS.equals(name)) {
				value = this.getCaseStatementsAsCodeBlock();
				break;
			}
			break;
		}

		return value;
	}
};
