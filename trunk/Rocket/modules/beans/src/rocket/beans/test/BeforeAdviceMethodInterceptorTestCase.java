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
import rocket.beans.client.aop.BeforeAdvice;
import rocket.beans.client.aop.InterceptorChain;
import rocket.beans.client.aop.MethodInterceptor;
import rocket.beans.client.aop.MethodInvocation;

public class BeforeAdviceMethodInterceptorTestCase extends TestCase {

	public void testDoNothingBeforeAdvice() throws Throwable {
		final Object parameter = "input";
		final Object returning = "returning";

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addBeforeAdvice(new BeforeAdvice() {
			public void before(Object[] parameters) {
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

	public void testDecoratingBeforeAdvice() throws Throwable {
		final Object parameter = "initial-input";
		final String beforeAdviceDecoratedParameter = "decorated-input";
		final Object returning = "returning";

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addBeforeAdvice(new BeforeAdvice() {
			public void before(Object[] parameters) {
				final String string = (String) parameters[0];
				assertEquals(parameter, string);
				parameters[0] = beforeAdviceDecoratedParameter;
			}
		});
		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				TestCase.assertSame(beforeAdviceDecoratedParameter, input);
				return returning;
			}
		};
		chain.setTarget(target);

		final Object returned = chain.proceed();
		assertSame(returning, returned);
	}

	public void testBeforeAdviceThatThrows() throws Throwable {
		final Object parameter = "initial-input";
		final RuntimeException beforeAdviceThrows = new RuntimeException();

		final InterceptorChain chain = new InterceptorChain();

		final Object[] parameters = new Object[1];
		parameters[0] = parameter;
		chain.setParameters(parameters);

		chain.addBeforeAdvice(new BeforeAdvice() {
			public void before(Object[] parameters) {
				throw beforeAdviceThrows;
			}
		});
		chain.addMethodInterceptor(this.createTargetMethodInvoker());

		final Target target = new Target() {
			Object invoke(final Object input) {
				fail("The target should not have been executed because the beforeAdvice threw an exception.");
				return null;
			}
		};
		chain.setTarget(target);

		try {
			final Object returned = chain.proceed();
			fail("The before advice should have thrown an exception and not returned " + returned );
		} catch (final RuntimeException caught) {
			assertSame(beforeAdviceThrows, caught);
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
