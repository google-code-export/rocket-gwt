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
package rocket.logging.test.loggerfactorygenerator.client;

import java.util.List;

import rocket.logging.client.DebugLevelLogger;
import rocket.logging.client.ErrorLevelLogger;
import rocket.logging.client.FatalLevelLogger;
import rocket.logging.client.InfoLevelLogger;
import rocket.logging.client.Logger;
import rocket.logging.client.LoggerFactory;
import rocket.logging.client.LoggingEvent;
import rocket.logging.client.LoggingLevel;
import rocket.logging.client.NoneLevelLogger;
import rocket.logging.client.WarnLevelLogger;
import rocket.logging.test.shared.client.LoggedEventCapturer;
import rocket.logging.test.shared.client.LoggedEventCapturer2;
import rocket.logging.test.shared.client.LoggedEventCapturer3;

import com.google.gwt.junit.client.GWTTestCase;

public class LoggerFactoryGeneratorGwtTestCase extends GWTTestCase {

	final static String DEBUG = "debug";

	final static String INFO = "info";

	final static String WARN = "warn";

	final static String ERROR = "error";

	final static String FATAL = "fatal";

	public String getModuleName() {
		return "rocket.logging.test.loggerfactorygenerator.LoggerFactoryGenerator";
	}

	public void testDebugLoggerFromFactory() {
		LoggedEventCapturer.reset();

		final Logger logger = LoggerFactory.getLogger("Debug");

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

	public void testDebugLoggerFromGeneratedFactory() {
		final Logger logger = LoggerFactory.getLogger(this.returnsPassedString("Debug"));

		assertTrue("" + logger, logger instanceof DebugLevelLogger);
		final DebugLevelLogger debugLevelLogger = (DebugLevelLogger) logger;

		final Logger target = debugLevelLogger.getLogger();
		assertTrue("" + target, target instanceof LoggedEventCapturer);

		final LoggedEventCapturer loggedEventCapturer = (LoggedEventCapturer) target;
		final String category = loggedEventCapturer.getName();
		assertEquals("Debug", category);
	}

	public void testInfoLoggerFromFactory() {
		LoggedEventCapturer.reset();

		final Logger logger = LoggerFactory.getLogger("Info");

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

	public void testInfoLoggerFromGeneratedFactory() {
		final Logger logger = LoggerFactory.getLogger(this.returnsPassedString("Info"));

		assertTrue("" + logger, logger instanceof InfoLevelLogger);
		final InfoLevelLogger infoLevelLogger = (InfoLevelLogger) logger;

		final Logger target = infoLevelLogger.getLogger();
		assertTrue("" + target, target instanceof LoggedEventCapturer2);

		final LoggedEventCapturer2 loggingMessageCapturer = (LoggedEventCapturer2) target;
		final String category = loggingMessageCapturer.getName();
		assertEquals("Info", category);
	}

	public void testWarnLoggerFromFactory() {
		LoggedEventCapturer.reset();

		final Logger logger = LoggerFactory.getLogger("Warn");

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

	public void testWarnLoggerFromGeneratedFactory() {
		final Logger logger = LoggerFactory.getLogger(this.returnsPassedString("Warn"));

		assertTrue("" + logger, logger instanceof WarnLevelLogger);
		final WarnLevelLogger warnLevelLogger = (WarnLevelLogger) logger;

		final Logger target = warnLevelLogger.getLogger();
		assertTrue("" + target, target instanceof LoggedEventCapturer3);

		final LoggedEventCapturer3 loggingMessageCapturer = (LoggedEventCapturer3) target;
		final String category = loggingMessageCapturer.getName();
		assertEquals("Warn", category);
	}

	public void testErrorLoggerFromFactory() {
		LoggedEventCapturer.reset();

		final Logger logger = LoggerFactory.getLogger("Error");
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
		assertSame(messages.toString(), LoggingLevel.ERROR, ((LoggingEvent) messages.get(i)).getLoggingLevel());
		assertEquals(messages.toString(), "Error", ((LoggingEvent) messages.get(i++)).getName());
		assertSame(messages.toString(), LoggingLevel.FATAL, ((LoggingEvent) messages.get(i)).getLoggingLevel());
		assertEquals(messages.toString(), "Error", ((LoggingEvent) messages.get(i++)).getName());
	}

	public void testErrorLoggerFromGeneratedFactory() {
		final Logger logger = LoggerFactory.getLogger(this.returnsPassedString("Error"));

		assertTrue("" + logger, logger instanceof ErrorLevelLogger);
		final ErrorLevelLogger errorLevelLogger = (ErrorLevelLogger) logger;

		final Logger target = errorLevelLogger.getLogger();
		assertTrue("" + target, target instanceof LoggedEventCapturer3);

		final LoggedEventCapturer3 loggingMessageCapturer = (LoggedEventCapturer3) target;
		final String category = loggingMessageCapturer.getName();
		assertEquals("Error", category);
	}

	public void testFatalLoggerFromFactory() {
		LoggedEventCapturer.reset();

		final Logger logger = LoggerFactory.getLogger("Fatal");

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

	public void testFatalLoggerFromGeneratedFactory() {
		final Logger logger = LoggerFactory.getLogger(this.returnsPassedString("Fatal"));

		assertTrue("" + logger, logger instanceof FatalLevelLogger);
		final FatalLevelLogger fatalLevelLogger = (FatalLevelLogger) logger;

		final Logger target = fatalLevelLogger.getLogger();
		assertTrue("" + target, target instanceof LoggedEventCapturer3);

		final LoggedEventCapturer3 loggingMessageCapturer = (LoggedEventCapturer3) target;
		final String category = loggingMessageCapturer.getName();
		assertEquals("Fatal", category);
	}

	public void testNoneLoggerFromFactory() {
		LoggedEventCapturer.reset();

		final Logger logger = LoggerFactory.getLogger("None");

		logger.debug(DEBUG);
		logger.info(INFO);
		logger.warn(WARN);
		logger.error(ERROR);
		logger.fatal(FATAL);

		final List messages = LoggedEventCapturer.messages;
		assertEquals(messages.toString(), 0, messages.size());
	}

	public void testUnknownLoggerFromGeneratedFactory() {
		final Logger logger = LoggerFactory.getLogger(this.returnsPassedString("Unknown"));

		assertTrue("" + logger, logger instanceof NoneLevelLogger);
		final NoneLevelLogger noneLevelLogger = (NoneLevelLogger) logger;

		final Logger target = noneLevelLogger.getLogger();
		assertTrue("" + target, target instanceof LoggedEventCapturer3);

		final LoggedEventCapturer3 loggingMessageCapturer = (LoggedEventCapturer3) target;
		final String category = loggingMessageCapturer.getName();
		assertEquals("Unknown", category);
	}

	/**
	 * This method is delibrately constructed in this contrived manner so that
	 * the GWT compiler will not remove this method and inline the actual
	 * string.
	 * 
	 * @param string
	 *            the input string
	 * @return The same string
	 */
	String returnsPassedString(final String string) {
		return new StringBuffer(string).toString();
	}
}