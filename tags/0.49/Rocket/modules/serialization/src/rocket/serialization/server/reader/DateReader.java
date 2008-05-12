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
package rocket.serialization.server.reader;

import java.util.Date;

import rocket.serialization.server.ServerObjectReader;

/**
 * A reader for dates
 * 
 * @author Miroslav Pokorny
 */
public class DateReader extends rocket.serialization.client.reader.DateReader implements ServerObjectReader {

	static public final ServerObjectReader instance = new DateReader();

	protected DateReader() {
	}

	public boolean canRead(Class classs) {
		return classs.equals(Date.class);
	}

}
