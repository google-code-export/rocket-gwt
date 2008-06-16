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
package rocket.beans.rebind.aop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.beans.rebind.BeanFactoryGeneratorException;
import rocket.generator.rebind.method.Method;
import rocket.util.client.Checker;
import rocket.util.client.Utilities;

/**
 * This factory takes an expression and returns a MethodMatcher
 * 
 * @author Miroslav Pokorny
 */
public class MethodMatcherFactory {

	/**
	 * Factory method which takes an string which may hold more than one
	 * expression.
	 * 
	 * @param expression
	 *            The text
	 * @return A new MethodMatcher
	 */
	public MethodMatcher create(final String expression) {
		Checker.notEmpty("parameter:expression", expression);

		final String[] components = Utilities.split(expression, ",", true);
		final int count = components.length;
		final List<MethodMatcher> methodMatchers = new ArrayList<MethodMatcher>();

		for (int i = 0; i < count; i++) {
			methodMatchers.add(this.createMethodMatcher(components[i]));
		}

		return new MethodMatcher() {

			@Override
			public boolean matches(Method method) {
				boolean matched = false;

				final Iterator<MethodMatcher> iterator = methodMatchers.iterator();
				while (iterator.hasNext()) {
					final MethodMatcher matcher = iterator.next();
					if (matcher.matches(method)) {
						matched = true;
						break;
					}
				}

				return matched;
			}

			@Override
			public String toString() {
				return methodMatchers.toString();
			}
		};
	}

	protected void throwMethodExpressionException(final String expression) {
		throw new BeanFactoryGeneratorException(expression);
	}

	protected MethodMatcher createMethodMatcher(final String pattern) {
		final MethodMatcher matcher = new MethodMatcher();
		matcher.setPattern(pattern);
		return matcher;
	}
}
