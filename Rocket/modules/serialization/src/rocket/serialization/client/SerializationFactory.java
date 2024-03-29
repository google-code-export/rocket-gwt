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
package rocket.serialization.client;

/**
 * Base class for any of the generated serialization factories classes.
 * 
 * @author Miroslav Pokorny
 */
abstract public class SerializationFactory {

	public ObjectInputStream createObjectInputStream(final String stream) {
		final ClientObjectInputStream objectInputStream = new ClientObjectInputStream() {
			protected ObjectReader getObjectReader(final String typeName) {
				return SerializationFactory.this.getObjectReader(typeName);
			}
		};
		objectInputStream.prepare(stream);
		return objectInputStream;
	}

	abstract protected ObjectReader getObjectReader(String typeName);

	public ObjectOutputStream createObjectOutputStream() {
		return new ClientObjectOutputStream() {
			protected ObjectWriter getObjectWriter(final String typeName) {
				return SerializationFactory.this.getObjectWriter(typeName);
			}
		};
	}

	abstract protected ObjectWriter getObjectWriter(String typeName);
}
