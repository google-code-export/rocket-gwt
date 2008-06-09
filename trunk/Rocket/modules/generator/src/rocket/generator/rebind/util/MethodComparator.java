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

import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;

/**
 * This comparator is used to sort method based on name and staticness
 * 
 * @author Miroslav Pokorny
 */
public class MethodComparator implements Comparator<Method> {

	public final static Comparator<Method> INSTANCE = new MethodComparator();

	private MethodComparator() {
		super();
	}

	public int compare(final Method method, final Method otherMethod) {
		int value = 0;

		while (true) {
			final boolean staticMethod = method.isStatic();
			final boolean otherStaticMethod = otherMethod.isStatic();
			if (staticMethod && false == otherStaticMethod) {
				value = 1;
				break;
			}
			if (false == staticMethod && otherStaticMethod) {
				value = -1;
				break;
			}

			value = method.getName().compareTo(otherMethod.getName());

			if (0 == value) {
				value = this.compareMethodArguments(method.getParameters(), otherMethod.getParameters());
			}
			break;
		}

		return value;
	}

	public int compareMethodArguments(final List<MethodParameter> arguments, final List<MethodParameter> otherArguments) {
		int value = 0;

		while (true) {
			// short argument list comes first...
			value = arguments.size() - otherArguments.size();
			if (value != 0) {
				break;
			}
			// compare argument types one by one...
			final Iterator<MethodParameter> parameters = arguments.iterator();
			final Iterator<MethodParameter> otherParameters = otherArguments.iterator();
			final Comparator typeComparator = TypeComparator.INSTANCE;

			while (parameters.hasNext()) {
				final MethodParameter parameter = parameters.next();
				final MethodParameter otherParameter = otherParameters.next();

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
