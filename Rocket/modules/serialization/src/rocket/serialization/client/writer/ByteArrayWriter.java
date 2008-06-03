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

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.ObjectWriter;

/**
 * Handles the serialization of a byte array
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type byte[]
 */
public class ByteArrayWriter extends ObjectWriterImpl {

	static public final ObjectWriter instance = new ByteArrayWriter();

	protected ByteArrayWriter() {
	}

	protected void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		this.writeByteArray((byte[]) object, objectOutputStream);
	}

	protected void writeByteArray(final byte[] array, final ObjectOutputStream objectOutputStream) {
		final int elementCount = array.length;
		objectOutputStream.writeInt(elementCount);

		for (int i = 0; i < elementCount; i++) {
			objectOutputStream.writeByte(array[i]);
		}
	}
}
