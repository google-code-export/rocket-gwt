/**
 * 
 */
package rocket.beans.test.beans.client.proxyclasswithfinalpublicmethod;

import rocket.beans.client.aop.MethodInvocation;

/**
 * This method interceptor does nothing but invoke the next interceptor in the
 * chain.
 * 
 * @author Miroslav Pokorny
 */
public class MethodInterceptor implements rocket.beans.client.aop.MethodInterceptor {

	public MethodInterceptor() {
		super();
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		return invocation.proceed();
	}
}
