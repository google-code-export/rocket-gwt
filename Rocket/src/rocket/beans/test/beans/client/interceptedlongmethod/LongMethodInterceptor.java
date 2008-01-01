/**
 * 
 */
package rocket.beans.test.beans.client.interceptedlongmethod;

import rocket.beans.client.aop.MethodInvocation;

/**
 * This method interceptor does nothing but invoke the next interceptor in the
 * chain.
 * 
 * @author Miroslav Pokorny
 */
public class LongMethodInterceptor implements rocket.beans.client.aop.MethodInterceptor {

	public LongMethodInterceptor() {
		super();
	}

	public Object invoke(final MethodInvocation invocation) throws Throwable {
		this.executedCount++;
		return invocation.proceed();
	}

	public int executedCount = 0;
}
