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

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.util.client.Checker;

/**
 * An abstraction for the if-then-create-logger.txt template
 * 
 * @author Miroslav Pokorny
 */
class IfThenCreateLoggerTemplatedFile extends TemplatedFileCodeBlock {

	public IfThenCreateLoggerTemplatedFile() {
		super();
	}

	public boolean isNative() {
		return false;
	}

	public void setNative(final boolean ignored) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The name
	 */
	private String name;

	protected String getName() {
		Checker.notEmpty("field:name", name);
		return this.name;
	}

	public void setName(final String name) {
		Checker.notEmpty("parameter:name ", name);
		this.name = name;
	}

	/**
	 * The target logger being instantiated.
	 */
	private Constructor logger;

	protected Constructor getLogger() {
		Checker.notNull("field:logger", logger);
		return this.logger;
	}

	public void setLogger(final Constructor logger) {
		Checker.notNull("parameter:logger", logger);
		this.logger = logger;
	}

	/**
	 * The constructor from one of the LoggingLevelLoggers that will filter out
	 * messages below the configured threashhold.
	 */
	private Constructor loggingLevelLogger;

	protected Constructor getLoggingLevelLogger() {
		Checker.notNull("field:loggingLevelLogger", loggingLevelLogger);
		return this.loggingLevelLogger;
	}

	public void setLoggingLevelLogger(final Constructor loggingLevelLogger) {
		Checker.notNull("parameter:loggingLevelLogger", loggingLevelLogger);
		this.loggingLevelLogger = loggingLevelLogger;
	}

	protected String getResourceName() {
		return Constants.IF_THEN_CREATE_LOGGER_TEMPLATE;
	}

	public InputStream getInputStream() {
		return super.getInputStream();
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.IF_THEN_CREATE_LOGGER_NAME.equals(name)) {
				value = new StringLiteral(this.getName());
				break;
			}
			if (Constants.IF_THEN_CREATE_LOGGER_LOGGER.equals(name)) {
				value = this.getLogger();
				break;
			}
			if (Constants.IF_THEN_CREATE_LOGGING_LEVEL_LOGGER_LOGGER.equals(name)) {
				value = this.getLoggingLevelLogger();
				break;
			}

			break;
		}
		return value;
	}
}
