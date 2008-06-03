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

import java.util.Comparator;
import java.util.TreeSet;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.server.ServerObjectReader;

/**
 * A reader for TreeSet implementations
 * 
 * @author Miroslav Pokorny
 */
public class TreeSetReader extends rocket.serialization.client.reader.SetReader implements ServerObjectReader {

	static public final ServerObjectReader instance = new TreeSetReader();

	protected TreeSetReader() {
	}

	public boolean canRead(final Class classs) {
		return TreeSet.class.isAssignableFrom(classs);
	}

	public Object newInstance(final String typeName, final ObjectInputStream objectInputStream) {
		final Comparator comparator = (Comparator) objectInputStream;
		return null == comparator ? new TreeSet() : new TreeSet(comparator);
	}

}
