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
package rocket.serialization.client.reader;

import java.util.HashMap;
import java.util.Map;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectReader;
import rocket.util.client.Checker;

/**
 * A reader for Map implementations
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type java.util.Map
 */
public class MapReader extends ObjectReaderImpl implements ObjectReader {
	static public final ObjectReader instance = new MapReader();

	public Object newInstance(final String typeName, final ObjectInputStream objectInputStream) {
		return new HashMap();
	}

	public void read( final Object map, final ObjectInputStream objectInputStream ){
		this.readMap( (Map) map, objectInputStream);
	}
	
	protected void readMap(final Map map, final ObjectInputStream objectInputStream) {
		Checker.notNull("parameter:map", map);
		Checker.notNull("parameter:objectInputStream", objectInputStream);

		final int elementCount = objectInputStream.readInt();

		for (int i = 0; i < elementCount; i++) {
			final Object key = objectInputStream.readObject();
			final Object value = objectInputStream.readObject();
			map.put(key, value);
		}
	}

}
