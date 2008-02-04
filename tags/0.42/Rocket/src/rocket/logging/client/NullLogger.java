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

public class NullLogger extends FatalLevelLogger implements Logger {

	public NullLogger(Logger ignored) {
		super(ignored);
	}

	final public void fatal(final String message) {
	}

	final public void fatal(final String message, final Throwable throwable) {
	}

	protected void log(final LoggingLevel level, String message) {
	}

	protected void log(final LoggingLevel level, String message, Throwable throwable) {
	}

	public boolean isFatalEnabled() {
		return false;
	}
}
