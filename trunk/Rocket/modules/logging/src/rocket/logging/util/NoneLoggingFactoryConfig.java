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
import rocket.logging.client.NoneLevelLogger;

/**
 * This factory returns the NoneLevelLogger.
 * 
 * The eventual goal is that all logging statements will eventually be removed
 * by the GWT compiler because all {@link NoneLevelLogger} logging statements
 * are empty.
 * 
 * @author Miroslav Pokorny
 */
public class NoneLoggingFactoryConfig implements LoggingFactoryConfig {

	public Iterator<String> getNames() {
		return new Iterator<String>() {

			public boolean hasNext() {
				return false;
			}

			public String next() {
				throw new UnsupportedOperationException();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public LoggingLevel getLoggingLevel(String category) {
		return LoggingLevel.NONE;
	}

	public String getTypeName(String category) {
		return NoneLevelLogger.class.getName();
	}
}
