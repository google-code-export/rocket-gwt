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
package rocket.beans.test;

import junit.framework.TestCase;
import rocket.beans.client.aop.InterceptorChain;
import rocket.beans.client.aop.MethodInterceptor;
import rocket.beans.client.aop.MethodInvocation;

public class InterceptorChainTestCase extends TestCase {

	public void testChainHasOnlyProxyMethodInterceptor() throws Throwable {
		final Object parameter = "input";
		final Object returning = "returning";

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				TestCase.assertSame(parameter, input);
				return returning;
			}
		};
		chain.setTarget(target);

		final Object returned = chain.proceed();
		assertSame(returning, returned);
	}

	public void testChainHasOnlyProxyMethodInterceptorWithTargetThrowingException() throws Throwable {
		final Object parameter = "input";
		final RuntimeException throwing = new RuntimeException();

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				throw throwing;
			}
		};
		chain.setTarget(target);

		try {
			chain.proceed();
			fail("An exception should have been thrown by the target");
		} catch (final RuntimeException expected) {
			assertSame(throwing, expected);
		}
	}

	public void testChainWithSeveralInterceptorsThatDoNothing() throws Throwable {
		final Object parameter = "input";
		final Object returning = "returning";

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addMethodInterceptor(new MethodInterceptor() {
			public Object invoke(MethodInvocation invocation) throws Throwable {
				return invocation.proceed();
			}
		});

		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				TestCase.assertSame(parameter, input);
				return returning;
			}
		};
		chain.setTarget(target);

		final Object returned = chain.proceed();
		assertSame(returning, returned);
	}

	public void testChainWithDecoratingInterceptors() throws Throwable {
		final Object parameter = "initial-input";
		final String parameterAfterDecoration = "decorated-input";
		final Object returning = "returning";

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addMethodInterceptor(new MethodInterceptor() {
			public Object invoke(MethodInvocation invocation) throws Throwable {
				final Object[] invokeParameters = invocation.getParameters();

				final String string = (String) invokeParameters[0];
				assertEquals(parameter, string);
				invokeParameters[0] = parameterAfterDecoration;

				return invocation.proceed();
			}
		});

		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				TestCase.assertSame(parameterAfterDecoration, input);
				return returning;
			}
		};
		chain.setTarget(target);

		final Object returned = chain.proceed();
		assertSame(returning, returned);
	}

	public void testChainWithInterceptorThatShortCircuitsByNotCallingProceed() throws Throwable {
		final Object parameter = "input";
		final Object returning = "returning";

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addMethodInterceptor(new MethodInterceptor() {
			public Object invoke(MethodInvocation invocation) throws Throwable {
				return returning;
			}
		});
		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				TestCase.fail("The target should not have been invoked.");
				return null;
			}
		};
		chain.setTarget(target);

		final Object returned = chain.proceed();
		assertSame(returning, returned);
	}

	/**
	 * Instances of this class are used as pretend proxy targets.
	 */
	static abstract class Target {
		abstract Object invoke(Object input);
	}

	/**
	 * Factory method that creates a {@link MethodInterceptor} that prepares and
	 * executes the invoke method of the Target class.
	 * 
	 * @return The value returned by {@link Target#invoke(Object)}
	 */
	MethodInterceptor createTargetMethodInvoker() {
		return new MethodInterceptor() {
			public Object invoke(MethodInvocation invocation) throws Throwable {
				final Object object = invocation.getTarget();
				assertTrue("" + object, object instanceof Target);

				final Object[] invokeParameters = invocation.getParameters();
				assertNotNull(invokeParameters);
				assertEquals(1, invokeParameters.length);

				final Target invokeTarget = (Target) object;
				return invokeTarget.invoke(invokeParameters[0]);
			}
		};
	}
}
