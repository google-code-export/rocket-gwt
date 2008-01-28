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
package rocket.logging.rebind;

import rocket.logging.client.DebugLevelLogger;
import rocket.logging.client.ErrorLevelLogger;
import rocket.logging.client.FatalLevelLogger;
import rocket.logging.client.InfoLevelLogger;
import rocket.logging.client.Logger;
import rocket.logging.client.LoggerFactoryImpl;
import rocket.logging.client.NoneLevelLogger;
import rocket.logging.client.WarnLevelLogger;

/**
 * A collection of constants used by classes within this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	final static String LOGGER_FACTORY_IMPL = LoggerFactoryImpl.class.getName();

	final static String FIND_LOGGER_METHOD = "findLogger";

	final static String FIND_LOGGER_NAME_PARAMETER = "name";

	final static String CREATE_ROOT_LOGGER_METHOD = "createRootLogger";

	final static String CREATE_ROOT_NAME_PARAMETER = "name";

	final static String DEBUG_LEVEL_LOGGER = DebugLevelLogger.class.getName();

	final static String INFO_LEVEL_LOGGER = InfoLevelLogger.class.getName();

	final static String WARN_LEVEL_LOGGER = WarnLevelLogger.class.getName();

	final static String ERROR_LEVEL_LOGGER = ErrorLevelLogger.class.getName();

	final static String FATAL_LEVEL_LOGGER = FatalLevelLogger.class.getName();

	final static String NONE_LEVEL_LOGGER = NoneLevelLogger.class.getName();

	final static String LOGGER = Logger.class.getName();
}
