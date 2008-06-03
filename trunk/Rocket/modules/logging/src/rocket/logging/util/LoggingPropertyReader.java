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

/**
 * A simple template that takes care of handling both known and unknown logging
 * factory property files.
 * 
 * @author Miroslav Pokorny
 */
abstract public class LoggingPropertyReader {

	public Object readProperty() {
		Object object = null;

		String propertyValue = null;
		try {
			propertyValue = this.getPropertyValue();
		} catch (final Exception caught) {
			propertyValue = Constants.EXCLUDE;
		}

		while (true) {
			if (Constants.EXCLUDE.equals(propertyValue)) {
				object = this.handleDisableLoggingValue();
				break;
			}
			if (Constants.INCLUDE.equals(propertyValue)) {
				object = this.handleEnableLoggingValue();
				break;
			}

			this.throwInvalidPropertyValue(propertyValue);
		}

		return object;
	}

	protected String getPropertyName() {
		return Constants.LOGGING_ENABLED_PROPERTY;
	}

	abstract protected String getPropertyValue();

	abstract protected void throwInvalidPropertyValue(String propertyValue);

	abstract protected Object handleDisableLoggingValue();

	abstract protected Object handleEnableLoggingValue();

	protected PropertiesFileLoggingFactoryConfig createPropertiesFileLoggingConfig() {
		final PropertiesFileLoggingFactoryConfig propertiesFile = new PropertiesFileLoggingFactoryConfig();
		propertiesFile.load();
		return propertiesFile;
	}
}
