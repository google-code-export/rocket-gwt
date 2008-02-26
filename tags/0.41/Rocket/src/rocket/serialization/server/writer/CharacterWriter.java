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

import rocket.serialization.server.ServerObjectWriter;

/**
 * Handles the serialization of a Character wrapper
 * 
 * @author Miroslav Pokorny
 */
public class CharacterWriter extends rocket.serialization.client.writer.CharacterWriter implements ServerObjectWriter {

	static public final ServerObjectWriter instance = new CharacterWriter();

	protected CharacterWriter() {
	}

	public boolean canWrite(final Object object) {
		return object instanceof Character;
	}

	protected String getTypeName(final Object object) {
		return object.getClass().getName();
	}
}