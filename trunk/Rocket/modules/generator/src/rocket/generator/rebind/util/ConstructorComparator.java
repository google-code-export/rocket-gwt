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
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;

/**
 * This comparator is used to sort constructors based on argument types
 * 
 * @author Miroslav Pokorny
 */
public class ConstructorComparator implements Comparator {

	public final static Comparator INSTANCE = new ConstructorComparator();

	private ConstructorComparator() {
		super();
	}

	public int compare(final Object object, final Object otherObject) {
		return this.compareConstructors((Constructor) object, (Constructor) otherObject);
	}

	int compareConstructors(final Constructor constructor, final Constructor otherConstructor) {
		return this.compareConstructorArguments(constructor.getParameters(), otherConstructor.getParameters());
	}

	public int compareConstructorArguments(final List<ConstructorParameter> parameters, final List<ConstructorParameter> otherParameters) {
		int value = 0;

		while (true) {
			// short argument list comes first...
			value = parameters.size() - otherParameters.size();
			if (value != 0) {
				break;
			}
			// compare argument types one by one...
			final Iterator parametersIterator = parameters.iterator();
			final Iterator otherParametersIterator = otherParameters.iterator();
			final Comparator typeComparator = TypeComparator.INSTANCE;

			while (parametersIterator.hasNext()) {
				final ConstructorParameter parameter = (ConstructorParameter) parametersIterator.next();
				final ConstructorParameter otherParameter = (ConstructorParameter) otherParametersIterator.next();

				value = typeComparator.compare(parameter.getType(), otherParameter.getType());

				if (0 != value) {
					break;
				}
			}
			break;
		}

		return value;
	}
}
