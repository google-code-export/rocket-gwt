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

import java.util.TreeSet;

import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.server.ServerObjectWriter;

/**
 * Handles the serialization of any type that implements TreeSet
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type: java.util.TreeSet
 */
public class TreeSetWriter extends rocket.serialization.client.writer.SetWriter implements ServerObjectWriter {

	static public final ServerObjectWriter instance = new TreeSetWriter();

	protected TreeSetWriter() {
	}

	public boolean canWrite(final Object object) {
		return object instanceof TreeSet;
	}

	protected void writeTypeName(final Object object, final ObjectOutputStream objectOutputStream) {
		objectOutputStream.writeObject(TreeSet.class.getName());
	}

	protected void write0(final Object object, final ObjectOutputStream objectOutputStream) {
		this.writeTreeSet((TreeSet) object, objectOutputStream);
	}

	protected void writeTreeSet(final TreeSet treeSet, final ObjectOutputStream objectOutputStream) {
		objectOutputStream.writeObject(treeSet.comparator());
		this.writeCollection(treeSet, objectOutputStream);
	}
}
