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
package rocket.beans.rebind;

import rocket.beans.rebind.aop.MethodMatcher;
import rocket.util.client.ObjectHelper;

/**
 * Represents a single aspect found within a bean factory xml file
 * @author Miroslav Pokorny
 */
public class Aspect {

	/**
	 * The id of the advisor bean
	 */
	private String advisor;

	public String getAdvisor() {
		return this.advisor;
	}

	public void setAdvisor(final String advisor) {
		this.advisor = advisor;
	}

	/**
	 * The id of the target bean
	 */
	private String target;

	public String getTarget() {
		return this.target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	/**
	 * An expression that will be used to match public methods as candidates for the advisor.
	 */
	private String methodExpression;

	public String getMethodExpression() {
		return this.methodExpression;
	}

	public void setMethodExpression(final String methodExpression) {
		this.methodExpression = methodExpression;
	}

	private MethodMatcher methodMatcher;

	public MethodMatcher getMethodMatcher() {
		ObjectHelper.checkNotNull("field:methodMatcher", methodMatcher);
		return this.methodMatcher;
	}

	public void setMethodMatcher(final MethodMatcher methodMatcher) {
		ObjectHelper.checkNotNull("parameter:methodMatcher", methodMatcher);
		this.methodMatcher = methodMatcher;
	}

	public String toString(){
		return super.toString() + ", advisor: \"" + this.advisor + "\", target: \"" + target + "\", methodExpression: \"" + methodExpression + "\"";
	}
}
