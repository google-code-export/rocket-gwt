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

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectReader;

/**
 * A reader for short arrays.
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type short[]
 */
public class ShortArrayReader extends ObjectReaderImpl implements ObjectReader {
	static public final ObjectReader instance = new ShortArrayReader();

	public Object newInstance(final String typeName, final ObjectInputStream objectInputStream) {
		final int elementCount = objectInputStream.readInt();
		final short[] array = new short[elementCount];
		for (int i = 0; i < elementCount; i++) {
			array[i] = objectInputStream.readShort();
		}
		return array;
	}
}
