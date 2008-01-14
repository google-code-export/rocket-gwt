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
package rocket.serialization.test.clientobjectinputstream.client;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.reader.ObjectReaderImpl;

public class ConcreteClassObjectReader extends ObjectReaderImpl {

	public ConcreteClassObjectReader() {
	}

	public Object newInstance(String typeName, final ObjectInputStream objectInputStream) {
		return new ConcreteClass();
	}

	public void read(final Object object, final ObjectInputStream objectInputStream) {
		final ConcreteClass concreteClass = (ConcreteClass) object;
		concreteClass.value = objectInputStream.readInt();
	}
}