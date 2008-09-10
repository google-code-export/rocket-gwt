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

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * A custom serializer to help with serialization of logging levels.
 * 
 * @author Miroslav Pokorny
 */
public class LoggingEvent_FieldSerializer {
	
  public static LoggingEvent instantiate(final SerializationStreamReader streamReader) throws SerializationException{
	  return new LoggingEvent();
  };

	
	public static void serialize(final SerializationStreamWriter streamWriter, final LoggingEvent loggingEvent)
			throws SerializationException {
		streamWriter.writeString(loggingEvent.getName());
		streamWriter.writeInt(loggingEvent.getLoggingLevel().getValue());
		streamWriter.writeString(loggingEvent.getMessage());
		streamWriter.writeObject(loggingEvent.getThrowable());
	}

	public static void deserialize(final SerializationStreamReader streamReader, final LoggingEvent loggingEvent) throws SerializationException {
		loggingEvent.setName(streamReader.readString());
		loggingEvent.setLoggingLevel(LoggingLevel.getLoggingLevel(streamReader.readInt()));
		loggingEvent.setMessage(streamReader.readString());
		loggingEvent.setThrowable((Throwable) streamReader.readObject());
	}

}
