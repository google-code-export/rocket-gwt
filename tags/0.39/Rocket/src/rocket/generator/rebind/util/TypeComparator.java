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
package rocket.generator.rebind.util;

import java.util.Comparator;

import rocket.generator.rebind.type.Type;

/**
 * This comparator is used to sort types based on name.
 * @author Miroslav Pokorny
 */
public class TypeComparator implements Comparator {

	public final static Comparator INSTANCE = new TypeComparator();

	private TypeComparator() {
		super();
	}

	public int compare(final Object object, final Object otherObject) {
		return this.compare((Type) object, (Type) otherObject);
	}

	int compare(final Type type, final Type otherType) {
		return type.getName().compareTo(otherType.getName());
	}
}
