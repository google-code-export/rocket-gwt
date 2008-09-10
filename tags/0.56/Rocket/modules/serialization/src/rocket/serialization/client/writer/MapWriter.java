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
package rocket.serialization.client.writer;

import java.util.Iterator;
import java.util.Map;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectWriter;

/**
 * Handles the serialization of any class that implements Map
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type java.util.Map
 */
public class MapWriter extends ObjectWriterImpl implements ObjectWriter {

	static public final ObjectWriter instance = new MapWriter();

	protected MapWriter() {
	}

	protected void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		this.writeMap((Map) object, objectOutputStream);
	}

	protected void writeMap(final Map map, final ObjectOutputStream objectOutputStream) {
		objectOutputStream.writeInt(map.size());

		final Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();

			objectOutputStream.writeObject(entry.getKey());
			objectOutputStream.writeObject(entry.getValue());
		}
	}

}
