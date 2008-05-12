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

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectReader;
import rocket.serialization.client.SerializationException;
import rocket.serialization.client.reader.ObjectReaderImpl;
import rocket.util.client.Checker;

/**
 * A custom reader for the LoggingLevel enum.
 * 
 * @author Miroslav Pokorny
 * @serialization-type rocket.logging.client.LoggingLevel
 */
public class LoggingLevelReader extends ObjectReaderImpl implements ObjectReader {

	static public final ObjectReader instance = new LoggingLevelReader();

	protected LoggingLevelReader() {
		super();
	}

	public Object newInstance(final String typeName, final ObjectInputStream objectInputStream) {
		Checker.equals("newInstance", "rocket.logging.client.LoggingLevel", typeName);

		LoggingLevel level = null;

		while (true) {
			final int value = objectInputStream.readInt();

			if (value == LoggingLevel.DEBUG.getValue()) {
				level = LoggingLevel.DEBUG;
				break;
			}
			if (value == LoggingLevel.INFO.getValue()) {
				level = LoggingLevel.INFO;
				break;
			}
			if (value == LoggingLevel.WARN.getValue()) {
				level = LoggingLevel.WARN;
				break;
			}
			if (value == LoggingLevel.ERROR.getValue()) {
				level = LoggingLevel.ERROR;
				break;
			}
			if (value == LoggingLevel.FATAL.getValue()) {
				level = LoggingLevel.FATAL;
				break;
			}

			throw new SerializationException("Unable to unmarshal LoggingLevel instance.");
		}

		return level;
	}

	public void read(final Object instance, final ObjectInputStream objectInputStream) {
	}
}
