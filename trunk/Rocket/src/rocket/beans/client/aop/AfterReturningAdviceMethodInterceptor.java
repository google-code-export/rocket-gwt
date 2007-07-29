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
package rocket.beans.client.aop;

import rocket.util.client.ObjectHelper;

/**
 * A simple adapter between a {@link MethodInterceptor} and an after advice
 * 
 * This class is typically used by generated proxies as a bridge between a
 * {@link AfterReturningAdvice} and a proxy.
 * 
 * @author Miroslav Pokorny
 */
public class AfterReturningAdviceMethodInterceptor implements MethodInterceptor {
	
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object returned = invocation.proceed();
		final AfterReturningAdvice advice = this.getAfterReturningAdvice();
		return advice.afterReturn(returned);
	}

	private AfterReturningAdvice afterReturningAdvice;

	protected AfterReturningAdvice getAfterReturningAdvice() {
		ObjectHelper.checkNotNull("field:afterReturningAdvice", afterReturningAdvice);
		return afterReturningAdvice;
	}

	public void setAfterReturningAdvice(final AfterReturningAdvice afterAdvice) {
		ObjectHelper.checkNotNull("parameter:afterReturningAdvice", afterAdvice);
		this.afterReturningAdvice = afterAdvice;
	}

}
