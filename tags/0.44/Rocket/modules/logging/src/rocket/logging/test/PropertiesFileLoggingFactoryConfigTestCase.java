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
import java.io.InputStream;
import java.io.StringBufferInputStream;

import junit.framework.TestCase;
import rocket.logging.client.LoggingConstants;
import rocket.logging.client.LoggingLevel;
import rocket.logging.util.PropertiesFileLoggingFactoryConfig;

public class PropertiesFileLoggingFactoryConfigTestCase extends TestCase {

	public void testExactCategoryMatch() throws IOException {
		final PropertiesFileLoggingFactoryConfig loader = new TestPropertiesFileLoggingFactoryConfig();
		loader.load();

		final LoggingLevel level = loader.getLoggingLevel("a");
		assertSame(LoggingLevel.ERROR, level);

		final String typeName = loader.getTypeName("a");
		assertEquals("ALogger", typeName);
	}

	public void testHeirarchicalCategoryMatch() throws IOException {
		final PropertiesFileLoggingFactoryConfig loader = new TestPropertiesFileLoggingFactoryConfig();
		loader.load();

		final LoggingLevel level = loader.getLoggingLevel("a.b.c");
		assertSame(LoggingLevel.DEBUG, level);

		final String typeName = loader.getTypeName("a.b.c");
		assertEquals("BLogger", typeName);
	}

	public void testCategoryThatShouldResultInRoot() throws IOException {
		final PropertiesFileLoggingFactoryConfig loader = new TestPropertiesFileLoggingFactoryConfig();
		loader.load();

		final LoggingLevel level = loader.getLoggingLevel("unknown");
		assertSame(LoggingLevel.FATAL, level);

		final String typeName = loader.getTypeName("unknown");
		assertEquals("DLogger", typeName);
	}

	static class TestPropertiesFileLoggingFactoryConfig extends PropertiesFileLoggingFactoryConfig {
		protected InputStream getInputStream() throws IOException {
			final String source =
			/* first entry */
			"a=Error,ALogger\n" +
			/* 2nd entry */
			"a.b=Debug,BLogger\n" +
			/* default entry */
			LoggingConstants.ROOT_LOGGER_NAME + "=Fatal,DLogger\n";

			return new StringBufferInputStream(source);
		}
	}
}
