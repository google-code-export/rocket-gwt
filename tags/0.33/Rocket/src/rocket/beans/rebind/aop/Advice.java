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

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Contains an advice for a bean.
 * 
 * @author Miroslav Pokorny
 */
public class Advice {
	private MethodMatcher methodMatcher;

	public MethodMatcher getMethodMatcher() {
		ObjectHelper.checkNotNull("field:methodMatcher", methodMatcher);
		return this.methodMatcher;
	}

	public void setMethodMatcher(final MethodMatcher methodMatcher) {
		ObjectHelper.checkNotNull("parameter:methodMatcher", methodMatcher);
		this.methodMatcher = methodMatcher;
	}

	/**
	 * The id of the advisor bean
	 */
	private String advisorBeanId;

	public String getAdvisorBeanId() {
		StringHelper.checkNotEmpty("field:advisorBeanId", advisorBeanId);
		return this.advisorBeanId;
	}

	public void setAdvisorBeanId(final String advisorBeanId) {
		StringHelper.checkNotEmpty("parameter:advisorBeanId", advisorBeanId);
		this.advisorBeanId = advisorBeanId;
	}

	/**
	 * The id of the target bean
	 */
	private String targetBeanId;

	public String getTargetBeanId() {
		StringHelper.checkNotEmpty("field:targetBeanId", targetBeanId);
		return this.targetBeanId;
	}

	public void setTargetBeanId(final String targetBeanId) {
		StringHelper.checkNotEmpty("parameter:targetBeanId", targetBeanId);
		this.targetBeanId = targetBeanId;
	}

	public String toString() {
		return super.toString() + ", methodMatcher: " + methodMatcher
				+ ", advisorBeanId: " + advisorBeanId + ", targetBeanId["
				+ targetBeanId + "]";
	}
}
