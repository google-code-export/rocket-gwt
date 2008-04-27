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
package rocket.logging.server;

import rocket.logging.client.LoggingLevel;
import rocket.serialization.server.ServerObjectReader;

public class LoggingLevelReader extends rocket.logging.client.LoggingLevelReader implements ServerObjectReader {

	public boolean canRead(final Class klass) {
		return klass.getName().equals(LoggingLevel.class.getName());
	}
}
