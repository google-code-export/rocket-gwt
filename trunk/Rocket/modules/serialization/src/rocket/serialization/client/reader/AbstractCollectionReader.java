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

import java.util.Collection;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectReader;
import rocket.util.client.Checker;

/**
 * ObjectReader that provides common serialization of collection and its sub classes.
 * @author Miroslav Pokorny
 * @serialization-type java.util.AbstractCollection
 */
public class AbstractCollectionReader extends ObjectReaderImpl implements ObjectReader {

	static public final ObjectReader instance = new AbstractCollectionReader();
	
	public void read(final Object collection, final ObjectInputStream objectInputStream){
		this.readCollection( (Collection) collection, objectInputStream);
	}
	
	protected void readCollection(final Collection collection, final ObjectInputStream objectInputStream) {
		Checker.notNull("parameter:collection", collection);
		Checker.notNull("parameter:objectInputStream", objectInputStream);

		final int elementCount = objectInputStream.readInt();

		for (int i = 0; i < elementCount; i++) {
			final Object element = objectInputStream.readObject();
			collection.add(element);
		}
	}
}
