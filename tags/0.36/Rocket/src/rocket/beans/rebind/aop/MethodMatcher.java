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

import java.util.regex.Pattern;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.Method;
import rocket.util.client.ObjectHelper;

/**
 * A method matcher is used to filter methods belonging to a type against an
 * advisor.
 * 
 * @author Miroslav Pokorny
 */
public class MethodMatcher {

	/**
	 * Tests if a method matches the rules for this MethodMatcher
	 * 
	 * @param method
	 * @return
	 */
	public boolean matches(final Method method) {
		return
		// instance
		false == method.isStatic() &&
		// public
				method.getVisibility() == Visibility.PUBLIC &&
				// name matches pattern
				this.getPattern().matcher(method.getName()).matches();
	}

	/**
	 * The matcher to match.
	 */
	private Pattern pattern;

	protected Pattern getPattern() {
		ObjectHelper.checkNotNull("field:pattern", pattern);
		return this.pattern;
	}

	public void setPattern(final Pattern pattern) {
		ObjectHelper.checkNotNull("parameter:pattern", pattern);
		this.pattern = pattern;
	}

	/**
	 * Overloaded method which takes a regular expression as a String
	 * 
	 * @param pattern
	 */
	public void setPattern(final String pattern) {
		ObjectHelper.checkNotNull("parameter:pattern", pattern);

		this.setPattern(Pattern.compile(pattern));
	}

	public String toString() {
		return this.pattern.pattern();
	}
}
