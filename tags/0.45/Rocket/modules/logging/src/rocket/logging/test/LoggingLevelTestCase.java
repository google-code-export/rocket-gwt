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
package rocket.logging.test;

import java.util.List;

import junit.framework.TestCase;
import rocket.logging.client.DebugLevelLogger;
import rocket.logging.client.ErrorLevelLogger;
import rocket.logging.client.FatalLevelLogger;
import rocket.logging.client.InfoLevelLogger;
import rocket.logging.client.Logger;
import rocket.logging.client.LoggingEvent;
import rocket.logging.client.LoggingLevel;
import rocket.logging.client.WarnLevelLogger;
import rocket.logging.test.shared.client.LoggedEventCapturer;

public class LoggingLevelTestCase extends TestCase {
	final static String DEBUG = "debug";

	final static String INFO = "info";

	final static String WARN = "warn";

	final static String ERROR = "error";

	final static String FATAL = "fatal";

	public void testDebugLevelLogger() {
		LoggedEventCapturer.reset();

		final Logger logger = new DebugLevelLogger(new LoggedEventCapturer("DEBUG"));

		logger.debug(DEBUG);
		logger.info(INFO);
		logger.warn(WARN);
		logger.error(ERROR);
		logger.fatal(FATAL);

		assertTrue(logger.isDebugEnabled());
		assertTrue(logger.isInfoEnabled());
		assertTrue(logger.isWarnEnabled());
		assertTrue(logger.isErrorEnabled());
		assertTrue(logger.isFatalEnabled());

		final List messages = LoggedEventCapturer.messages;
		assertEquals(messages.toString(), 5, messages.size());

		int i = 0;
		assertSame(messages.toString(), LoggingLevel.DEBUG, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.INFO, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.WARN, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.ERROR, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.FATAL, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
	}

	public void testInfoLevelLogger() {
		LoggedEventCapturer.reset();

		final Logger logger = new InfoLevelLogger(new LoggedEventCapturer("INFO"));

		logger.debug(DEBUG);
		logger.info(INFO);
		logger.warn(WARN);
		logger.error(ERROR);
		logger.fatal(FATAL);

		assertFalse(logger.isDebugEnabled());
		assertTrue(logger.isInfoEnabled());
		assertTrue(logger.isWarnEnabled());
		assertTrue(logger.isErrorEnabled());
		assertTrue(logger.isFatalEnabled());

		final List messages = LoggedEventCapturer.messages;
		assertEquals(messages.toString(), 4, messages.size());

		int i = 0;
		assertSame(messages.toString(), LoggingLevel.INFO, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.WARN, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.ERROR, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.FATAL, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
	}

	public void testWarnLevelLogger() {
		LoggedEventCapturer.reset();

		final Logger logger = new WarnLevelLogger(new LoggedEventCapturer("WARN"));

		logger.debug(DEBUG);
		logger.info(INFO);
		logger.warn(WARN);
		logger.error(ERROR);
		logger.fatal(FATAL);

		assertFalse(logger.isDebugEnabled());
		assertFalse(logger.isInfoEnabled());
		assertTrue(logger.isWarnEnabled());
		assertTrue(logger.isErrorEnabled());
		assertTrue(logger.isFatalEnabled());

		final List messages = LoggedEventCapturer.messages;
		assertEquals(messages.toString(), 3, messages.size());

		int i = 0;
		assertSame(messages.toString(), LoggingLevel.WARN, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.ERROR, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.FATAL, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
	}

	public void testErrorLevelLogger() {
		LoggedEventCapturer.reset();

		final Logger logger = new ErrorLevelLogger(new LoggedEventCapturer("ERROR"));

		logger.debug(DEBUG);
		logger.info(INFO);
		logger.warn(WARN);
		logger.error(ERROR);
		logger.fatal(FATAL);

		assertFalse(logger.isDebugEnabled());
		assertFalse(logger.isInfoEnabled());
		assertFalse(logger.isWarnEnabled());
		assertTrue(logger.isErrorEnabled());
		assertTrue(logger.isFatalEnabled());

		final List messages = LoggedEventCapturer.messages;
		assertEquals(messages.toString(), 2, messages.size());

		int i = 0;
		assertSame(messages.toString(), LoggingLevel.ERROR, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
		assertSame(messages.toString(), LoggingLevel.FATAL, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
	}

	public void testFatalLevelLogger() {
		LoggedEventCapturer.reset();

		final Logger logger = new FatalLevelLogger(new LoggedEventCapturer("FATAL"));

		logger.debug(DEBUG);
		logger.info(INFO);
		logger.warn(WARN);
		logger.error(ERROR);
		logger.fatal(FATAL);

		assertFalse(logger.isDebugEnabled());
		assertFalse(logger.isInfoEnabled());
		assertFalse(logger.isWarnEnabled());
		assertFalse(logger.isErrorEnabled());
		assertTrue(logger.isFatalEnabled());

		final List messages = LoggedEventCapturer.messages;
		assertEquals(messages.toString(), 1, messages.size());

		int i = 0;
		assertSame(messages.toString(), LoggingLevel.FATAL, ((LoggingEvent) messages.get(i++)).getLoggingLevel());
	}

}
