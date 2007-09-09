/**
 * 
 */
package rocket.beans.test.beans.client.proxylongparameterreturntype;

import rocket.beans.client.aop.MethodInvocation;

/**
 * This method interceptor does nothing but invoke the next interceptor in the
 * chain.
 * 
 * @author Miroslav Pokorny
 */
public class ProxyLongMethodInterceptor implements rocket.beans.client.aop.MethodInterceptor {

	public ProxyLongMethodInterceptor() {
		super();
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		this.executed = true;
		return invocation.proceed();
	}

	public boolean executed = false;
}
