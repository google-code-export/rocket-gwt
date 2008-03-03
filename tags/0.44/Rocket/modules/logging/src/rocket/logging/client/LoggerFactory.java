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

import com.google.gwt.core.client.GWT;

/**
 * Factory which fetches a Logger given its category.
 * 
 * A hack has been made to the compiler to replace all LoggerFactory.getLogger()
 * statements with actual resolved loggers taken from the logging properties
 * file providing String literals are passed to the factory method.
 * 
 * @author Miroslav Pokorny
 */
abstract public class LoggerFactory {
	static private LoggerFactoryImpl loggerFactory = (LoggerFactoryImpl) GWT.create(LoggerFactoryImpl.class);

	static public Logger getLogger(final String category) {
		return LoggerFactory.loggerFactory.getLogger(category);
	}
}
