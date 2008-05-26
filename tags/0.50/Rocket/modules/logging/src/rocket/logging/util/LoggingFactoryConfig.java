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

import java.util.Iterator;

import rocket.logging.client.LoggingLevel;

/**
 * This class is responsible for loading a properties file and making available
 * the rocket.logging level and rocket.logging implementation class for any
 * queried category
 * 
 * @author Miroslav Pokorny
 */
public interface LoggingFactoryConfig {

	Iterator getNames();

	LoggingLevel getLoggingLevel(String category);

	String getTypeName(String category);
}
