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
package rocket.logging.rebind.createrootlogger;

import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.util.client.Checker;

/**
 * An abstraction for the create-root-logger template
 * 
 * @author Miroslav Pokorny
 */
public class CreateRootLoggerTemplatedFile extends TemplatedFileCodeBlock {

	public CreateRootLoggerTemplatedFile() {
		super();
	}

	private Constructor levelLogger;

	protected Constructor getLevelLogger() {
		Checker.notNull("field:levelLogger", levelLogger);
		return this.levelLogger;
	}

	public void setLevelLogger(final Constructor levelLogger) {
		Checker.notNull("parameter:levelLogger", levelLogger);
		this.levelLogger = levelLogger;
	}

	private Constructor logger;

	protected Constructor getLogger() {
		Checker.notNull("field:logger", logger);
		return this.logger;
	}

	public void setLogger(final Constructor targetLogger) {
		Checker.notNull("parameter:logger", targetLogger);
		this.logger = targetLogger;
	}

	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.LEVEL_LOGGER.equals(name)) {
				value = this.getLevelLogger();
				break;
			}
			if (Constants.LOGGER.equals(name)) {
				value = this.getLogger();
				break;
			}
			break;
		}
		return value;
	}
}
