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
import rocket.beans.client.aop.AfterFinallyAdvice;
import rocket.beans.client.aop.InterceptorChain;
import rocket.beans.client.aop.MethodInterceptor;
import rocket.beans.client.aop.MethodInvocation;

public class AfterFinallyAdviceMethodInterceptorTestCase extends TestCase {

	public void testAfterFinallyAdvice() throws Throwable {
		final Object parameter = "initial-input";
		final Object returning = "returning";

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		final TestFinallyAdvice finallyAdvice = new TestFinallyAdvice();
		chain.addAfterFinallyAdvice(finallyAdvice);
		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				TestCase.assertSame(parameter, input);
				return returning;
			}
		};
		chain.setTarget(target);

		final Object returned = chain.proceed();
		assertTrue("FinallyAdvice executed", finallyAdvice.executed);
		assertSame(returning, returned);
	}

	static class TestFinallyAdvice implements AfterFinallyAdvice {
		public void afterFinally() {
			executed = true;
		}

		boolean executed = false;
	}

	public void testAfterFinallyAdviceThatThrows() throws Throwable {
		final Object parameter = "initial-input";
		final RuntimeException finallyAdviceThrows = new RuntimeException();
		final String targetReturned = "returned";

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addAfterFinallyAdvice(new AfterFinallyAdvice() {
			public void afterFinally() {
				throw finallyAdviceThrows;
			}
		});

		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				return targetReturned;
			}
		};
		chain.setTarget(target);

		try {
			final Object returned = chain.proceed();
			fail("A exception should have been thrown by the FinallyAdvice");
		} catch (final RuntimeException caught) {
			assertSame(finallyAdviceThrows, caught);
		}
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
