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
package rocket.beans.test.beans.client.proxyshortparameterreturntype;

import rocket.beans.client.aop.MethodInvocation;

/**
 * This method interceptor does nothing but invoke the next interceptor in the
 * chain.
 * 
 * @author Miroslav Pokorny
 */
public class ProxyShortMethodInterceptor implements rocket.beans.client.aop.MethodInterceptor {

	public ProxyShortMethodInterceptor() {
		super();
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		this.executed = true;
		return invocation.proceed();
	}

	public boolean executed = false;
}
