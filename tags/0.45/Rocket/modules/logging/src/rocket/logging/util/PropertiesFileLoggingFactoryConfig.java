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
package rocket.logging.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import rocket.logging.client.LoggingConstants;
import rocket.logging.client.LoggingLevel;
import rocket.util.client.Checker;
import rocket.util.server.UncheckedIOException;

/**
 * This class is responsible for loading a properties file and making available
 * the level and Logger implementation class for any queried name
 * 
 * @author Miroslav Pokorny
 */
public class PropertiesFileLoggingFactoryConfig implements LoggingFactoryConfig {

	public void load() {
		try {
			this.load0();
		} catch (final IOException ioException) {
			throw new UncheckedIOException(ioException);
		}
	}

	protected void load0() throws IOException {
		final InputStream inputStream = this.getInputStream();
		this.setEntries(this.loadFromInputStream(inputStream));
	}

	protected InputStream getInputStream() throws IOException {
		String resourceName = System.getProperty(Constants.CONFIG_SYSTEM_PROPERTY);
		if (null == resourceName) {
			resourceName = Constants.PROPERTIES_FILENAME;
		}

		final InputStream inputStream = this.getClass().getResourceAsStream(resourceName);
		if (null == inputStream) {
			throw new RuntimeException("Unable to locate \"" + resourceName + "\".");
		}
		return inputStream;
	}

	/**
	 * 
	 * name=level,logger
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	protected Map loadFromInputStream(final InputStream inputStream) throws IOException {
		Checker.notNull("parameter:inputStream", inputStream);

		final Properties properties = new Properties();
		properties.load(inputStream);

		final Map entries = new TreeMap();

		final Iterator propertiesFileEntries = properties.entrySet().iterator();
		while (propertiesFileEntries.hasNext()) {
			final Map.Entry entry = (Map.Entry) propertiesFileEntries.next();
			final String name = (String) entry.getKey();
			final String levelAndLogger = (String) entry.getValue();

			final ConfigEntry configEntry = new ConfigEntry();
			configEntry.setName(name);

			final int separator = levelAndLogger.indexOf(',');
			if (-1 == separator) {
				throwInvalidEntry(name + "=" + levelAndLogger);
			}

			configEntry.setLoggingLevel(LoggingLevel.getLoggingLevel(levelAndLogger.substring(0, separator)));
			configEntry.setTypeName(levelAndLogger.substring(separator + 1));

			entries.put(name, configEntry);
		}

		return entries;
	}

	void throwInvalidEntry(final String line) {
		throw new RuntimeException(line);
	}

	public Iterator getNames() {
		return Collections.unmodifiableSet(this.getEntries().keySet()).iterator();
	}

	public LoggingLevel getLoggingLevel(final String name) {
		Checker.notEmpty("parameter:name", name);
		return this.getEntry(name).getLoggingLevel();
	}

	public String getTypeName(final String name) {
		Checker.notEmpty("parameter:name", name);
		return this.getEntry(name).getTypeName();
	}

	protected ConfigEntry getEntry(final String name) {
		Checker.notEmpty("parameter:name", name);
		ConfigEntry entry = null;

		String key = name;
		final Map entries = this.getEntries();
		while (true) {
			entry = (ConfigEntry) entries.get(key);
			if (null != entry) {
				break;
			}

			final int dot = key.lastIndexOf('.');
			if (-1 == dot) {
				break;
			}
			key = key.substring(0, dot);
		}

		if (null == entry) {
			entry = (ConfigEntry) entries.get(LoggingConstants.ROOT_LOGGER_NAME);
			if (entry == null) {
				entry = this.createDefault(name);
			}
		}

		return entry;
	}

	protected ConfigEntry createDefault(final String name) {
		final ConfigEntry entry = new ConfigEntry();

		entry.setName(name);
		entry.setLoggingLevel(LoggingLevel.ERROR);
		entry.setTypeName(LoggingConstants.ROOT_LOGGER_TYPENAME);

		return entry;
	}

	/**
	 * This map aggregates all entries found within the properties file.
	 */
	private Map entries;

	protected Map getEntries() {
		Checker.notNull("field:entries", entries);
		return this.entries;
	}

	public void setEntries(final Map entries) {
		Checker.notNull("parameter:entries", entries);
		this.entries = entries;
	}

	public String toString() {
		return super.toString() + ", entries: " + entries;
	}

	static class ConfigEntry {
		String name;

		String getName() {
			Checker.notNull("field:name", name);
			return this.name;
		}

		void setName(final String name) {
			Checker.notNull("parameter:name", name);
			this.name = name;
		}

		LoggingLevel loggingLevel;

		LoggingLevel getLoggingLevel() {
			Checker.notNull("field:loggingLevel", loggingLevel);
			return this.loggingLevel;
		}

		void setLoggingLevel(final LoggingLevel loggingLevel) {
			Checker.notNull("parameter:loggingLevel", loggingLevel);
			this.loggingLevel = loggingLevel;
		}

		String typeName;

		String getTypeName() {
			Checker.notNull("field:typeName", typeName);
			return this.typeName;
		}

		void setTypeName(final String typeName) {
			Checker.notNull("parameter:typeName", typeName);
			this.typeName = typeName;
		}

		public String toString() {
			return name + "=" + loggingLevel + "," + typeName;
		}
	}
}
