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
package rocket.serialization.server.writer;

import java.util.HashSet;
import java.util.Set;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.server.ServerObjectWriter;

/**
 * Handles the serialization of any type that implements Set
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type: java.util.Set
 */
public class SetWriter extends rocket.serialization.client.writer.SetWriter implements ServerObjectWriter {

	static public final ServerObjectWriter instance = new SetWriter();

	protected SetWriter() {
	}

	public boolean canWrite(final Object object) {
		return object instanceof Set;
	}

	protected void writeTypeName(final Object object, final ObjectOutputStream objectOutputStream) {
		objectOutputStream.writeObject(HashSet.class.getName());
	}

	protected String getTypeName(final Object object) {
		throw new UnsupportedOperationException();
	}
}
