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
package rocket.logging.test.shared.client;

import java.util.ArrayList;
import java.util.List;

import rocket.logging.client.Logger;
import rocket.logging.client.LoggerImpl;
import rocket.logging.client.LoggingEvent;
import rocket.logging.client.LoggingLevel;

/**
 * This class exists purely for testing purposes.
 * 
 * @author Miroslav Pokorny
 */
public class LoggedEventCapturer extends LoggerImpl implements Logger {

	static {
		reset();
	}

	/**
	 * Clears or resets the accumulated messages. Typically this is done at the
	 * beginning of a test.
	 */
	static public void reset() {
		messages = new ArrayList();
	}

	public static List messages;

	public LoggedEventCapturer(String category) {
		super(category);
	}

	// @Override
	protected void log(LoggingLevel level, String message) {
		this.log(level, message, null);
	}

	// @Override
	protected void log(LoggingLevel level, String message, Throwable throwable) {
		LoggedEventCapturer.messages.add(new LoggingEvent(this.getName(), level, message, throwable));
	}
}
