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
 * Handles the serialization of a Long wrapper
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type java.lang.Long
 */
public class LongWriter extends NumberWriter implements ObjectWriter {

	static public final ObjectWriter instance = new LongWriter();

	protected LongWriter() {
	}

	protected void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		this.writeLong((Long) object, objectOutputStream);
	}

	protected void writeLong(final Long wrapper, final ObjectOutputStream objectOutputStream) {
		objectOutputStream.writeLong(wrapper.longValue());
	}

}
