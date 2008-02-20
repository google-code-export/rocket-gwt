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
package rocket.serialization.client.reader;

import java.util.HashSet;

import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectReader;

/**
 * A reader for set implementations
 * 
 * @author Miroslav Pokorny
 * 
 * @serialization-type java.util.Set
 */
public class SetReader extends AbstractCollectionReader {
	static public final ObjectReader instance = new SetReader();

	public Object newInstance(String typeName, ObjectInputStream objectInputStream){
		return new HashSet();
	}
}
