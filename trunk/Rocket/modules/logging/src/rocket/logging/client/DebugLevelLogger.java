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
 * infrastructure.
 * 
 * @author Miroslav Pokorny
 */
public class DebugLevelLogger implements Logger {

	public DebugLevelLogger(final Logger logger) {
		this.setLogger(logger);
	}

	protected DebugLevelLogger() {
		super();
	}

	private Logger logger;

	/**
	 * This method is only public to assist testing.
	 * 
	 * @return
	 */
	public Logger getLogger() {
		return this.logger;
	}

	protected void setLogger(final Logger logger) {
		this.logger = logger;
	}

	public void debug(final String message) {
		this.getLogger().debug(message);
	}

	public void debug(final String message, final Throwable throwable) {
		this.getLogger().debug(message, throwable);
	}

	public void info(final String message) {
		this.getLogger().info(message);
	}

	public void info(final String message, final Throwable throwable) {
		this.getLogger().info(message, throwable);
	}

	public void warn(final String message) {
		this.getLogger().warn(message);
	}

	public void warn(final String message, final Throwable throwable) {
		this.getLogger().warn(message, throwable);
	}

	public void error(final String message) {
		this.getLogger().error(message);
	}

	public void error(final String message, final Throwable throwable) {
		this.getLogger().error(message, throwable);
	}

	public void fatal(final String message) {
		this.getLogger().fatal(message);
	}

	public void fatal(final String message, final Throwable throwable) {
		this.getLogger().fatal(message, throwable);
	}

	public boolean isDebugEnabled() {
		return true;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public boolean isWarnEnabled() {
		return true;
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public boolean isFatalEnabled() {
		return true;
	}
}
