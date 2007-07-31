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
package rocket.beans.client.aop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.ObjectHelper;
import rocket.util.client.SystemHelper;

/**
 * Provides a chain of methodInterceptors for a aspect.
 * 
 * @author Miroslav Pokorny
 */
public class InterceptorChain {

	public InterceptorChain(){
		super();
		
		this.setMethodInterceptors( new ArrayList() );
	}
	
	/**
	 * Invokes all methodInterceptors and possibly the target proxy.
	 * 
	 * @return The value returned by the chain. Should be null for void methods.
	 * @throws Throwable Any exception that is thrown.
	 */
	public Object proceed() throws Throwable {
		final MethodInvocation methodInvocation = this.createMethodInvocation();
		return methodInvocation.proceed();
	}
	
	/**
	 * Invokes the chain and automatically unwraps the wrapped boolean result
	 * Similar methods exist to handle all the other primitive types.
	 * @return
	 * @throws Throwable
	 */
	public boolean proceedReturningBoolean() throws Throwable{
		final Boolean wrapper = (Boolean) this.proceed();
		return wrapper.booleanValue();
	}
	public byte proceedReturningByte() throws Throwable{
		final Byte wrapper = (Byte) this.proceed();
		return wrapper.byteValue();
	}
	public short proceedReturningShort() throws Throwable{
		final Short wrapper = (Short) this.proceed();
		return wrapper.shortValue();
	}
	public int proceedReturningInt() throws Throwable{
		final Integer wrapper = (Integer) this.proceed();
		return wrapper.intValue();
	}
	public long proceedReturningLong() throws Throwable{
		final Long wrapper = (Long) this.proceed();
		return wrapper.longValue();
	}
	public float proceedReturningFloat() throws Throwable{
		final Float wrapper = (Float) this.proceed();
		return wrapper.floatValue();
	}
	public double proceedReturningDouble() throws Throwable{
		final Double wrapper = (Double) this.proceed();
		return wrapper.doubleValue();
	}
	public char proceedReturningChar() throws Throwable{
		final Character wrapper = (Character) this.proceed();
		return wrapper.charValue();
	}
	/**
	 * Factory method which creates a MethodInvocation which can be used to pass
	 * control onto the next interceptor and eventually the proxy target.
	 * 
	 * @return The new MethodInvocation
	 */
	protected MethodInvocation createMethodInvocation() {
		final Iterator interceptors = this.getMethodInterceptors().iterator();
		return new MethodInvocation() {
			/**
			 * Returns the this reference for the object being proxied
			 * 
			 * @return
			 */
			public Object getTarget() {
				return InterceptorChain.this.getTarget();
			}

			public Object[] getParameters() {
				return InterceptorChain.this.getParameters();
			}

			public Object proceed() throws Throwable {
				final MethodInterceptor interceptor = (MethodInterceptor)interceptors.next();
				return interceptor.invoke(this);
			}
		};
	}

	/**
	 * The parameters of the method being proxied.
	 */
	private Object[] parameters;

	protected Object[] getParameters() {
		ObjectHelper.checkNotNull("field:parameters", parameters);
		return parameters;
	}

	public void setParameters(final Object[] parameters) {
		ObjectHelper.checkNotNull("parameter:parameters", parameters);
		this.parameters = parameters;
	}

	/**
	 * Overloaded method that helps with wrapping primitives into wrappers. 
	 * @param booleanValue
	 * @return A Boolean wrapper
	 */
	public Object asObject( final boolean booleanValue ){
		return Boolean.valueOf( booleanValue );
	}
	public Object asObject( final byte byteValue ){
		return new Byte( byteValue );
	}
	public Object asObject( final short shortValue ){
		return new Short( shortValue );
	}
	public Object asObject( final int intValue ){
		return new Integer( intValue );
	}
	public Object asObject( final long longValue ){
		return new Long( longValue );
	}
	public Object asObject( final float floatValue ){
		return new Float( floatValue );
	}
	public Object asObject( final double doubleValue ){
		return new Double( doubleValue );
	}
	public Object asObject( final char charValue ){
		return new Character( charValue );
	}
	public Object asObject( final Object object ){
		return object;
	}
	
	/**
	 * A list containing all method interceptors
	 */
	private List methodInterceptors;

	protected List getMethodInterceptors() {
		ObjectHelper.checkNotNull("field:methodInterceptors", methodInterceptors);
		return this.methodInterceptors;
	}

	protected void setMethodInterceptors(final List methodInterceptors) {
		ObjectHelper.checkNotNull("parameter:methodInterceptors", methodInterceptors);
		this.methodInterceptors = methodInterceptors;
	}

	/**
	 * Adds an advice to this chain, creating an interceptor if the advice is not an interceptor.
	 * @param advice A new advice.
	 */
	public void addAdvice(final Advice advice) {
		while (true) {
			if (advice instanceof AfterFinallyAdvice) {
				this.addAfterFinallyAdvice((AfterFinallyAdvice) advice);
				break;
			}
			if (advice instanceof AfterReturningAdvice) {
				this.addAfterReturningAdvice((AfterReturningAdvice) advice);
				break;
			}
			if (advice instanceof AfterThrowingAdvice) {
				this.addAfterThrowingAdvice((AfterThrowingAdvice) advice);
				break;
			}
			if (advice instanceof BeforeAdviceMethodInterceptor) {
				this.addBeforeAdvice((BeforeAdvice) advice);
				break;
			}
			
			this.addMethodInterceptor((MethodInterceptor) advice);			
			break;
		}
	}
	
	public void addAfterFinallyAdvice( final AfterFinallyAdvice advice ){
		final AfterFinallyAdviceMethodInterceptor interceptor = new AfterFinallyAdviceMethodInterceptor();
		interceptor.setAfterFinallyAdvice(advice);
		this.addMethodInterceptor( interceptor );
	}
	
	public void addAfterReturningAdvice( final AfterReturningAdvice advice ){
		final AfterReturningAdviceMethodInterceptor interceptor = new AfterReturningAdviceMethodInterceptor();
		interceptor.setAfterReturningAdvice(advice);
		this.addMethodInterceptor( interceptor );
	}
	
	public void addAfterThrowingAdvice( final AfterThrowingAdvice advice ){
		final AfterThrowingAdviceMethodInterceptor interceptor = new AfterThrowingAdviceMethodInterceptor();
		interceptor.setAfterThrowingAdvice(advice);
		this.addMethodInterceptor( interceptor );
	}
	
	public void addBeforeAdvice( final BeforeAdvice advice ){
		final BeforeAdviceMethodInterceptor interceptor = new BeforeAdviceMethodInterceptor();
		interceptor.setBeforeAdvice(advice);
		this.addMethodInterceptor( interceptor );
	}
	
	public void addMethodInterceptor( final MethodInterceptor methodInterceptor ){
		this.getMethodInterceptors().add( methodInterceptor );
	}
	
	/**
	 * The target of the proxy
	 */
	private Object target;

	protected Object getTarget() {
		ObjectHelper.checkNotNull("field:target", target);
		return target;
	}

	public void setTarget(final Object target) {
		ObjectHelper.checkNotNull("parameter:target", target);
		this.target = target;
	}
	
	/**
	 * Helper method which assists with unwrapping wrapped values. This is typically used when
	 * invoking the target method.
	 * Similar methods exist for all the primitive types.
	 * @param index An index within the array of parameters.
	 * @return The boolean value taken from the wrapper.
	 */
	public boolean getBooleanParameter(final int index ){
		final Boolean wrapper =(Boolean) this.getObjectParameter( index );
		return wrapper.booleanValue();
	}
	public byte getByteParameter(final int index ){
		final Byte wrapper =(Byte) this.getObjectParameter( index );
		return wrapper.byteValue();
	}
	public short getShortParameter(final int index ){
		final Short wrapper =(Short) this.getObjectParameter( index );
		return wrapper.shortValue();
	}
	public int getIntParameter(final int index ){
		final Integer wrapper =(Integer) this.getObjectParameter( index );
		return wrapper.intValue();
	}
	public long getLongParameter(final int index ){
		final Long wrapper =(Long) this.getObjectParameter( index );
		return wrapper.longValue();
	}
	public float getFloatParameter(final int index ){
		final Float wrapper =(Float) this.getObjectParameter( index );
		return wrapper.floatValue();
	}
	public double getDoubleParameter(final int index ){
		final Double wrapper =(Double) this.getObjectParameter( index );
		return wrapper.doubleValue();
	}
	public char getCharParameter(final int index ){
		final Character wrapper =(Character) this.getObjectParameter( index );
		return wrapper.charValue();
	}
	public Object getObjectParameter(final int index ){
		return this.getParameters()[ index ];
	}
}
