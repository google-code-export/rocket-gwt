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

/**
 * This logger should not be used and only exists to support the logging
 * infrastructure. By having all logging statements less than none are empty and
 * thus will be removed from the generated output by the compiler.
 * 
 * @author Miroslav Pokorny
 */
public class NoneLevelLogger extends FatalLevelLogger implements Logger {

	public NoneLevelLogger() {
		super();
	}

	public NoneLevelLogger(final Logger logger) {
		super(logger);
	}

	public NoneLevelLogger(final String category) {
		super();
	}

	@Override
	final public void fatal(final String message) {
	}

	@Override
	final public void fatal(final String message, final Throwable throwable) {
	}
}
