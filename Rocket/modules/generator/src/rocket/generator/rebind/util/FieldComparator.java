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

import rocket.generator.rebind.field.Field;

/**
 * This comparator is used to sort fields based on name and staticness
 * 
 * @author Miroslav Pokorny
 */
public class FieldComparator implements Comparator<Field> {

	public final static Comparator INSTANCE = new FieldComparator();

	private FieldComparator() {
		super();
	}

	public int compare(final Field field, final Field otherField) {
		int value = 0;

		while (true) {
			final boolean staticField = field.isStatic();
			final boolean otherStaticField = otherField.isStatic();
			if (staticField && false == otherStaticField) {
				value = 1;
				break;
			}
			if (false == staticField && otherStaticField) {
				value = -1;
				break;
			}

			value = field.getName().compareTo(otherField.getName());
			break;
		}

		return value;
	}
}
