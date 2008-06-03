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
import rocket.serialization.client.SerializationException;

abstract public class ObjectReaderImpl implements ObjectReader {

	/**
	 * This method is overridden by the generator for concrete types. The
	 * generator will not override this method for abstract types, thus any
	 * attempt to deserialize an abstract type will result in an exception being
	 * thrown.
	 */
	public Object newInstance(final String typeName, final ObjectInputStream objectInputStream) {
		throw new SerializationException("Unable to deserialize \"" + typeName + "\".");
	}

	public void read(final Object instance, final ObjectInputStream objectInputStream) {
	}

	public void readFields(final Object instance, final ObjectInputStream objectInputStream) {
	}
}
