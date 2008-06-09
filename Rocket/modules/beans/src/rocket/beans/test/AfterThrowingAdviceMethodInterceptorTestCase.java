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
import rocket.beans.client.aop.AfterThrowingAdvice;
import rocket.beans.client.aop.InterceptorChain;
import rocket.beans.client.aop.MethodInterceptor;
import rocket.beans.client.aop.MethodInvocation;

public class AfterThrowingAdviceMethodInterceptorTestCase extends TestCase {

	public void testAfterThrowingAdvice() throws Throwable {
		final Object parameter = "input";
		final TestException exception = new TestException();

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		final TestThrowingAdvice throwingAdvice = new TestThrowingAdvice();
		chain.addAfterThrowingAdvice(throwingAdvice);
		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				TestCase.assertSame(parameter, input);
				throw exception;
			}
		};
		chain.setTarget(target);

		try {
			final Object returned = chain.proceed();
			fail("A TestException should have been thrown by the target.");
		} catch (final TestException expected) {

		}
		assertTrue("ThrowingAdvice executed", throwingAdvice.executed);
	}

	static class TestThrowingAdvice implements AfterThrowingAdvice {
		public void afterThrowing(Throwable caught) throws Throwable {
			executed = true;
			throw caught;
		}

		boolean executed = false;
	}

	public void testAfterThrowingAdviceThatThrowsAnotherException() throws Throwable {
		final Object parameter = "input";
		final TestException throwingAdviceThrows = new TestException();
		final TestException targetThrows = new TestException();

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addAfterThrowingAdvice(new AfterThrowingAdvice() {
			public void afterThrowing(Throwable caught) throws Throwable {
				assertSame(targetThrows, caught);
				throw throwingAdviceThrows;
			}
		});

		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				throw targetThrows;
			}
		};
		chain.setTarget(target);

		try {
			final Object returned = chain.proceed();
			fail("The chain returned an object when an exception should have been thrown by the target, returned object: " + returned );
		} catch (final TestException caught) {
			assertSame(throwingAdviceThrows, caught);
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

	class TestException extends RuntimeException {
	}

	class TestException2 extends RuntimeException {
	}
}
