/**
 * 
 */
package rocket.beans.test.beans.client.proxydoubleparameterreturntype;

import rocket.beans.client.aop.MethodInvocation;

/**
 * This method interceptor does nothing but invoke the next interceptor in the
 * chain.
 * 
 * @author Miroslav Pokorny
 */
public class ProxyDoubleMethodInterceptor implements rocket.beans.client.aop.MethodInterceptor {

	public ProxyDoubleMethodInterceptor() {
		super();
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		this.executed = true;
		return invocation.proceed();
	}

	public boolean executed = false;
}
