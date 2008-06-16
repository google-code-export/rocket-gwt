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

import java.util.Collection;
import java.util.Iterator;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectWriter;

/**
 * ObjectWriter for java.util.AbstractCollection
 * 
 * @author Miroslav Pokorny
 * @serialization-type java.util.AbstractCollection
 */
public class AbstractCollectionWriter extends ObjectWriterImpl implements ObjectWriter {

	static public final ObjectWriter instance = new AbstractCollectionWriter();

	protected void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		this.writeCollection((Collection) object, objectOutputStream);
	}

	protected void writeCollection(final Collection collection, final ObjectOutputStream objectOutputStream) {
		// element count
		objectOutputStream.writeInt(collection.size());

		// write out the individual elements now...
		final Iterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			objectOutputStream.writeObject(iterator.next());
		}
	}
}
