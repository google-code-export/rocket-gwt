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

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectWriter;
import rocket.serialization.client.writer.ObjectWriterImpl;

/**
 * A custom serializer that handles serializing of LoggingLevel instances
 * 
 * Even though only actually used by tests included for the sake of
 * completeness.
 * 
 * @serialization-type rocket.logging.client.LoggingLevel
 */
public class LoggingLevelWriter extends ObjectWriterImpl implements ObjectWriter {

	static public final ObjectWriter instance = new LoggingLevelWriter();

	protected LoggingLevelWriter() {
		super();
	}

	public void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		this.writeLoggingLevel((LoggingLevel) object, objectOutputStream);
	}

	void writeLoggingLevel(final LoggingLevel value, final ObjectOutputStream objectOutputStream) {
		objectOutputStream.writeInt(value.getValue());
	}
}