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
package rocket.serialization.test.clientobjectoutputstream.client;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectWriter;
import rocket.serialization.client.writer.ObjectWriterImpl;

public class ConcreteClassObjectWriter extends ObjectWriterImpl implements ObjectWriter {

	public ConcreteClassObjectWriter() {
	}

	protected void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		final ConcreteClass concreteClass = (ConcreteClass) object;
		objectOutputStream.writeInt(concreteClass.value);

		super.write0(object, objectOutputStream);
	}

}
