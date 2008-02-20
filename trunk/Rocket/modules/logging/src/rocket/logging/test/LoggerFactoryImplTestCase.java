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

import java.io.IOException;

import junit.framework.TestCase;
import rocket.logging.client.Logger;
import rocket.logging.client.LoggerFactoryImpl;
import rocket.logging.test.shared.client.LoggedEventCapturer;

public class LoggerFactoryImplTestCase extends TestCase {

	static String A = "a";

	static String ABC = "a.b.c";

	public void testExactCategoryMatch() throws IOException {
		final LoggerFactoryImpl loader = new TestLoggerFactoryImpl();

		final String category = ABC;
		final Logger logger = loader.getLogger(category);
		assertTrue(logger.toString(), logger instanceof ABCLogger);

		final Logger secondLogger = loader.getLogger(category);
		assertSame(logger, secondLogger);
	}

	public void testHeirarchicalCategoryMatch() throws IOException {
		final LoggerFactoryImpl loader = new TestLoggerFactoryImpl();

		final String category = A + ".b";
		final Logger logger = loader.getLogger(category);
		assertTrue(logger.toString(), logger instanceof ALogger);

		final Logger secondLogger = loader.getLogger(category);
		assertSame(logger, secondLogger);
	}

	public void testCategoryThatShouldResultInDefault() throws IOException {
		final LoggerFactoryImpl loader = new TestLoggerFactoryImpl();

		final String category = "unknown";
		final Logger logger = loader.getLogger(category);
		assertTrue(logger.toString(), logger instanceof DefaultLogger);

		final Logger secondLogger = loader.getLogger(category);
		assertSame(logger, secondLogger);
	}

	static class TestLoggerFactoryImpl extends LoggerFactoryImpl {
		protected Logger findLogger(final String category) {
			Logger logger = null;

			while (true) {
				if (A.equals(category)) {
					logger = new ALogger(category);
					break;
				}
				if (ABC.equals(category)) {
					logger = new ABCLogger(category);
					break;
				}
				break;
			}

			return logger;
		}

		protected Logger createRootLogger(final String category) {
			return new DefaultLogger(category);
		}
	}

	static class ALogger extends LoggedEventCapturer {
		public ALogger(String category) {
			super(category);
		}
	}

	static class ABCLogger extends LoggedEventCapturer {
		public ABCLogger(String category) {
			super(category);
		}
	}

	static class DefaultLogger extends LoggedEventCapturer {
		public DefaultLogger(String category) {
			super(category);
		}
	}
}
