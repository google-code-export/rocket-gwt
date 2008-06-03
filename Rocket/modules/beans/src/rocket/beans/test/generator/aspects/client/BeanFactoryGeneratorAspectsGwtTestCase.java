/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.beans.test.generator.aspects.client;

import rocket.beans.client.BeanFactory;
import rocket.beans.client.aop.MethodInvocation;
import rocket.beans.test.generator.aspects.client.finalclass.FinalClassBeanFactory;
import rocket.beans.test.generator.aspects.client.finalpublicmethod.FinalPublicMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedbooleanmethod.HasBooleanMethod;
import rocket.beans.test.generator.aspects.client.interceptedbooleanmethod.InterceptedBooleanMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedbytemethod.HasByteMethod;
import rocket.beans.test.generator.aspects.client.interceptedbytemethod.InterceptedByteMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedcharmethod.HasCharMethod;
import rocket.beans.test.generator.aspects.client.interceptedcharmethod.InterceptedCharMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedcomplex.Complex;
import rocket.beans.test.generator.aspects.client.interceptedcomplex.InterceptedComplexBeanFactory;
import rocket.beans.test.generator.aspects.client.intercepteddoublemethod.HasDoubleMethod;
import rocket.beans.test.generator.aspects.client.intercepteddoublemethod.InterceptedDoubleMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedfloatmethod.HasFloatMethod;
import rocket.beans.test.generator.aspects.client.interceptedfloatmethod.InterceptedFloatMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedintmethod.HasIntMethod;
import rocket.beans.test.generator.aspects.client.interceptedintmethod.InterceptedIntMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedlongmethod.HasLongMethod;
import rocket.beans.test.generator.aspects.client.interceptedlongmethod.InterceptedLongMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedobjectmethod.HasObjectMethod;
import rocket.beans.test.generator.aspects.client.interceptedobjectmethod.InterceptedObjectMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedshortmethod.HasShortMethod;
import rocket.beans.test.generator.aspects.client.interceptedshortmethod.InterceptedShortMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.interceptedvoidmethod.HasVoidMethod;
import rocket.beans.test.generator.aspects.client.interceptedvoidmethod.InterceptedVoidMethodBeanFactory;
import rocket.beans.test.generator.aspects.client.methodinvocation.MethodInterceptorImpl;
import rocket.beans.test.generator.aspects.client.methodinvocation.MethodInvocationBeanFactory;
import rocket.beans.test.generator.aspects.client.methodinvocation.MethodInvocationTestTarget;
import rocket.beans.test.generator.aspects.client.notanadvice.NotAnAdviceBeanFactory;
import rocket.beans.test.generator.aspects.client.rpc.AdvisedGwtRpcAsync;
import rocket.beans.test.generator.aspects.client.rpc.AdvisedRpcBeanFactory;
import rocket.beans.test.generator.aspects.client.singleton.Singleton;
import rocket.beans.test.generator.aspects.client.singleton.SingletonBeanFactory;
import rocket.beans.test.generator.aspects.client.throwscheckedexception.CheckedException;
import rocket.beans.test.generator.aspects.client.throwscheckedexception.HasMethodThatThrowsCheckedException;
import rocket.beans.test.generator.aspects.client.throwscheckedexception.ThrowsCheckedExceptionBeanFactory;
import rocket.beans.test.generator.aspects.client.throwscheckedexception.ThrowsCheckedExceptionMethodInterceptor;
import rocket.beans.test.generator.aspects.client.throwsuncheckedexception.HasMethodThatThrowsUncheckedException;
import rocket.beans.test.generator.aspects.client.throwsuncheckedexception.ThrowsUncheckedExceptionBeanFactory;
import rocket.beans.test.generator.aspects.client.throwsuncheckedexception.UncheckedException;
import rocket.generator.client.FailedGenerateAttemptException;
import rocket.generator.client.GeneratorGwtTestCase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A series of tests for the BeanFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryGeneratorAspectsGwtTestCase extends GeneratorGwtTestCase {

	final static String SAX_PARSE_EXCEPTION = "org.xml.sax.SAXParseException";

	final static String BEAN_FACTORY_GENERATOR_EXCEPTION = "rocket.beans.rebind.BeanFactoryGeneratorException";

	final static String BEAN = "bean";

	final static String ADVISOR = "advisor";

	final static byte BYTE = 1;

	final static short SHORT = 23;

	final static int INT = 345;

	final static long LONG = 6789;

	final static float FLOAT = 12.34f;

	final static double DOUBLE = 56.789;

	final static int DELAY_TIMEOUT = 10 * 1000;

	public String getModuleName() {
		return "rocket.beans.test.generator.aspects.BeanFactoryGeneratorAspects";
	}

	public void testNotAnAdvice() {
		try {
			assertBindingFailed(GWT.create(NotAnAdviceBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testAttemptToAdviseFinalClass() throws Exception {
		try {
			assertBindingFailed(GWT.create(FinalClassBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testAttemptToAdviseClassWithFinalPublicMethod() throws Exception {
		try {
			assertBindingFailed(GWT.create(FinalPublicMethodBeanFactory.class));
		} catch (final FailedGenerateAttemptException failed) {
			assertTrue("" + failed, failed.getCauseType().equals(BEAN_FACTORY_GENERATOR_EXCEPTION));
		}
	}

	public void testInterceptedMethodWithBooleanParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedBooleanMethodBeanFactory.class);
		final HasBooleanMethod proxy = (HasBooleanMethod) factory.getBean(BEAN);
		assertEquals("proxy true ^ false", true ^ false, proxy.xor(true, false));
		assertEquals("proxy true ^ true", true ^ true, proxy.xor(true, true));
		assertEquals("proxy false ^ false", false ^ false, proxy.xor(false, false));

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(3, advisor.executedCount);
	}

	public void testInterceptedMethodWithByteParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedByteMethodBeanFactory.class);
		final HasByteMethod proxy = (HasByteMethod) factory.getBean(BEAN);
		assertEquals("proxy sum of " + BYTE + "+" + BYTE, BYTE + BYTE, proxy.add(BYTE, BYTE));

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithShortParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedShortMethodBeanFactory.class);
		final HasShortMethod proxy = (HasShortMethod) factory.getBean(BEAN);
		assertEquals("proxy sum of " + SHORT + "+" + SHORT, SHORT + SHORT, proxy.add(SHORT, SHORT));

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithIntParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedIntMethodBeanFactory.class);
		final HasIntMethod proxy = (HasIntMethod) factory.getBean(BEAN);
		assertEquals("proxy sum of " + INT + "+" + INT, INT + INT, proxy.add(INT, INT));

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithLongParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedLongMethodBeanFactory.class);
		final HasLongMethod proxy = (HasLongMethod) factory.getBean(BEAN);
		assertEquals("proxy sum of " + LONG + "+" + LONG, LONG + LONG, proxy.add(LONG, LONG));

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithFloatParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedFloatMethodBeanFactory.class);
		final HasFloatMethod proxy = (HasFloatMethod) factory.getBean(BEAN);
		assertEquals("proxy sum of " + FLOAT + "+" + FLOAT, FLOAT + FLOAT, proxy.add(FLOAT, FLOAT), 0.1f);

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithDoubleParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedDoubleMethodBeanFactory.class);
		final HasDoubleMethod proxy = (HasDoubleMethod) factory.getBean(BEAN);
		assertEquals("proxy sum of " + DOUBLE + "+" + DOUBLE, DOUBLE + DOUBLE, proxy.add(DOUBLE, DOUBLE), 0.1f);

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithCharParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedCharMethodBeanFactory.class);
		final HasCharMethod proxy = (HasCharMethod) factory.getBean(BEAN);
		assertEquals("proxy.toUpperCase()", Character.toUpperCase('a'), proxy.toUpperCase('a'));

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedMethodWithObjectParameterAndReturnType() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedObjectMethodBeanFactory.class);
		final HasObjectMethod proxy = (HasObjectMethod) factory.getBean(BEAN);

		final String object = "apple";
		assertSame("proxy passes object to target and back...", object, proxy.returnParameter(object));

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testInterceptedVoidMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedVoidMethodBeanFactory.class);
		final HasVoidMethod proxy = (HasVoidMethod) factory.getBean(BEAN);

		proxy.returnsVoid();

		final InvokingCountingMethodInterceptor advisor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testAdvisedRpc() {
		final String string = "apple";

		final BeanFactory factory = (BeanFactory) GWT.create(AdvisedRpcBeanFactory.class);
		final AdvisedGwtRpcAsync proxy = (AdvisedGwtRpcAsync) factory.getBean(BEAN);
		proxy.addStar(string, new AsyncCallback() {
			public void onSuccess(final Object result) {
				assertEquals(string + "*", result);

				final InvokingCountingMethodInterceptor interceptor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
				assertEquals(1, interceptor.executedCount);
				finishTest();
			}

			public void onFailure(final Throwable cause) {
				fail(cause.getMessage());
			}
		});

		this.delayTestFinish(DELAY_TIMEOUT);
	}

	public void testMethodInvocation() {
		final BeanFactory factory = (BeanFactory) GWT.create(MethodInvocationBeanFactory.class);
		final MethodInvocationTestTarget proxy = (MethodInvocationTestTarget) factory.getBean(BEAN);
		proxy.method(false, (byte) 0, (short) 1, (int) 2, (long) 3, 4f, 5.0, 'a', "string");

		final MethodInterceptorImpl interceptor = (MethodInterceptorImpl) factory.getBean(ADVISOR);
		final MethodInvocation methodInvocation = interceptor.methodInvocation;
		assertEquals("method", methodInvocation.getMethod());
		assertEquals(false, methodInvocation.isNative());
		assertEquals("java.lang.Object", methodInvocation.getReturnType());
		assertEquals("rocket.beans.test.generator.aspects.client.methodinvocation.SuperClassOfMethodInvocationTestTarget",
				methodInvocation.getEnclosingType());

		final String[] parameterTypes = methodInvocation.getParameterTypes();
		assertEquals(9, parameterTypes.length);
		assertEquals("Z", parameterTypes[0]);
		assertEquals("B", parameterTypes[1]);
		assertEquals("S", parameterTypes[2]);
		assertEquals("I", parameterTypes[3]);
		assertEquals("J", parameterTypes[4]);
		assertEquals("F", parameterTypes[5]);
		assertEquals("D", parameterTypes[6]);
		assertEquals("C", parameterTypes[7]);
		assertEquals("java.lang.String", parameterTypes[8]);
	}

	public void testThrowsCheckedException() {
		final BeanFactory factory = (BeanFactory) GWT.create(ThrowsCheckedExceptionBeanFactory.class);
		final HasMethodThatThrowsCheckedException proxy = (HasMethodThatThrowsCheckedException) factory.getBean(BEAN);

		try {
			proxy.throwCheckedException();
			fail("Was expecting a CheckedException to be thrown");
		} catch (final CheckedException expected) {
		}

		final ThrowsCheckedExceptionMethodInterceptor advisor = (ThrowsCheckedExceptionMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(1, advisor.executedCount);
	}

	public void testThrowsCheckedExceptionOnUnadvicedMethod() {
		final BeanFactory factory = (BeanFactory) GWT.create(ThrowsCheckedExceptionBeanFactory.class);
		final HasMethodThatThrowsCheckedException proxy = (HasMethodThatThrowsCheckedException) factory.getBean(BEAN);

		try {
			proxy.unadvicedThrowCheckedException();
			fail("Was expecting a CheckedException to be thrown");
		} catch (final CheckedException expected) {
		}

		final ThrowsCheckedExceptionMethodInterceptor advisor = (ThrowsCheckedExceptionMethodInterceptor) factory.getBean(ADVISOR);
		assertEquals(0, advisor.executedCount);
	}

	public void testThrowsCheckedExceptionLetsRuntimeExceptionsThrough() {
		final BeanFactory factory = (BeanFactory) GWT.create(ThrowsCheckedExceptionBeanFactory.class);
		final HasMethodThatThrowsCheckedException proxy = (HasMethodThatThrowsCheckedException) factory.getBean(BEAN);

		try {
			proxy.throwRuntimeException();
			fail("Was expecting a RuntimeException to be thrown");
		} catch (final RuntimeException expected) {
		}
	}

	public void testThrowsUncheckedException() {
		final BeanFactory factory = (BeanFactory) GWT.create(ThrowsUncheckedExceptionBeanFactory.class);
		final HasMethodThatThrowsUncheckedException proxy = (HasMethodThatThrowsUncheckedException) factory.getBean(BEAN);

		try {
			proxy.throwUncheckedException();
			fail("Was expecting a UncheckedException to be thrown");
		} catch (final UncheckedException expected) {
		}
	}

	public void testMixtureOfAdvisedAndUnadvisedMethods() {
		final BeanFactory factory = (BeanFactory) GWT.create(InterceptedComplexBeanFactory.class);
		final Complex proxy = (Complex) factory.getBean(BEAN);
		final InvokingCountingMethodInterceptor interceptor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);

		interceptor.executedCount = 0;
		int expectedInterceptorExecutedCount = 1;
		assertEquals("advised", true ^ false, proxy.advisedXor(true, false));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", BYTE + BYTE, proxy.advisedAdd(BYTE, BYTE));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", SHORT + SHORT, proxy.advisedAdd(SHORT, SHORT));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", INT + INT, proxy.advisedAdd(INT, INT));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", LONG + LONG, proxy.advisedAdd(LONG, LONG));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", FLOAT + FLOAT, proxy.advisedAdd(FLOAT, FLOAT), 0.1f);
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", DOUBLE + DOUBLE, proxy.advisedAdd(DOUBLE, DOUBLE), 0.1f);
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", Character.toUpperCase('a'), proxy.advisedToUpperCase('a'));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", "APPLE".toLowerCase(), proxy.advisedToLowerCase("APPLE"));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		assertEquals("advised", null, proxy.advisedReturnsNull(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, new Object()));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		final Object object = new Object();
		assertSame("advised", object, proxy.advisedReturnsObject(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, object));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		try {
			proxy.advisedThrowCheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final Exception expected) {
		}
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
		expectedInterceptorExecutedCount++;

		try {
			proxy.advisedThrowUncheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final RuntimeException expected) {
		}
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", true ^ false, proxy.unadvisedXor(true, false));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", BYTE + BYTE, proxy.unadvisedAdd(BYTE, BYTE));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", SHORT + SHORT, proxy.unadvisedAdd(SHORT, SHORT));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", INT + INT, proxy.unadvisedAdd(INT, INT));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", LONG + LONG, proxy.unadvisedAdd(LONG, LONG));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", FLOAT + FLOAT, proxy.unadvisedAdd(FLOAT, FLOAT), 0.1f);
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", DOUBLE + DOUBLE, proxy.unadvisedAdd(DOUBLE, DOUBLE), 0.1f);
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", Character.toUpperCase('a'), proxy.unadvisedToUpperCase('a'));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", "APPLE".toLowerCase(), proxy.unadvisedToLowerCase("APPLE"));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertEquals("unadvised", null, proxy.unadvisedReturnsNull(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, new Object()));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		assertSame("unadvised", object, proxy.unadvisedReturnsObject(true, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, object));
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		try {
			proxy.unadvisedThrowCheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final Exception expected) {
		}
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);

		try {
			proxy.unadvisedThrowUncheckedException();
			fail("A checked exception should have been thrown.");
		} catch (final RuntimeException expected) {
		}
		assertEquals(expectedInterceptorExecutedCount, interceptor.executedCount);
	}

	public void testSingleton() {
		final SingletonBeanFactory factory = (SingletonBeanFactory) GWT.create(SingletonBeanFactory.class);
		final Singleton first = (Singleton) factory.getBean(BEAN);
		final Singleton second = (Singleton) factory.getBean(BEAN);
		assertSame(first, second);

		final InvokingCountingMethodInterceptor interceptor = (InvokingCountingMethodInterceptor) factory.getBean(ADVISOR);
		final int firstNumber = first.getNumber();
		final int secondNumber = second.getNumber();
		assertEquals(firstNumber, secondNumber);

		assertEquals(2, interceptor.executedCount);

	}
}
