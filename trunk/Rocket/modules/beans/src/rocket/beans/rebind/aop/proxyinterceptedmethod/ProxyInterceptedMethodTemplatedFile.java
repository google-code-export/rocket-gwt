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
package rocket.beans.rebind.aop.proxyinterceptedmethod;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import rocket.beans.rebind.Aspect;
import rocket.beans.rebind.aop.addadvice.AddAdviceTemplatedFile;
import rocket.beans.rebind.aop.interceptorchainproceed.InvokeInterceptorChainProceedTemplatedFile;
import rocket.beans.rebind.aop.invoketarget.InvokeTargetMethodTemplatedFile;
import rocket.beans.rebind.aop.rethrowdeclaredexception.RethrowDeclaredExceptionTemplatedFile;
import rocket.beans.rebind.aop.wrapparameter.WrapParameterTemplatedFile;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.BooleanLiteral;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the proxy intercepted method template
 * 
 * @author Miroslav Pokorny
 */
public class ProxyInterceptedMethodTemplatedFile extends TemplatedFileCodeBlock {

	public ProxyInterceptedMethodTemplatedFile() {
		super();
	}

	/**
	 * The outter bean factory class
	 */
	private Type beanFactory;

	protected Type getBeanFactory() {
		Checker.notNull("field:beanFactory", beanFactory);
		return this.beanFactory;
	}

	public void setBeanFactory(final Type beanFactory) {
		Checker.notNull("parameter:beanFactory", beanFactory);
		this.beanFactory = beanFactory;
	}

	/**
	 * The method being proxied
	 */
	private NewMethod method;

	protected NewMethod getMethod() {
		Checker.notNull("field:method", method);
		return this.method;
	}

	public void setMethod(final NewMethod method) {
		Checker.notNull("parameter:method", method);
		this.method = method;
	}

	/**
	 * The method that is the final target of the interceptor
	 */
	private Method targetMethod;

	protected Method getTargetMethod() {
		Checker.notNull("field:targetMethod", targetMethod);
		return this.targetMethod;
	}

	public void setTargetMethod(final Method targetMethod) {
		Checker.notNull("parameter:targetMethod", targetMethod);
		this.targetMethod = targetMethod;
	}

	protected BooleanLiteral isMethodNative(){
		return new BooleanLiteral( this.getTargetMethod().isNative() );
	}

	protected StringLiteral getEnclosingType(){
		return new StringLiteral( this.getTargetMethod().getEnclosingType().getName() );
	}
	
	/**
	 * The target method parameter
	 */
	private MethodParameter target;

	protected MethodParameter getTarget() {
		Checker.notNull("field:target", target);
		return this.target;
	}

	public void setTarget(final MethodParameter target) {
		Checker.notNull("parameter:target", target);
		this.target = target;
	}

	/**
	 * A list containing the aspects that apply to this method.
	 */
	private List<Aspect> aspects;

	protected List<Aspect> getAspects() {
		Checker.notNull("field:aspects", aspects);
		return this.aspects;
	}

	public void setAspects(final List<Aspect> aspects) {
		Checker.notNull("parameter:aspects", aspects);
		this.aspects = aspects;
	}

	protected CodeBlock getAddAdvices() {
		final Type beanFactory = this.getBeanFactory();
		final AddAdviceTemplatedFile addAdvice = new AddAdviceTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			@Override
			public InputStream getInputStream() {
				return addAdvice.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return addAdvice.getValue0(name);
			}

			@Override
			protected Collection getCollection() {
				return ProxyInterceptedMethodTemplatedFile.this.getAspects();
			}

			@Override
			protected void prepareToWrite(Object element) {
				final Aspect advice = (Aspect) element;
				addAdvice.setBeanId(advice.getAdvisor());
				addAdvice.setBeanFactory(beanFactory);
			}

			@Override
			protected void writeBetweenElements(SourceWriter writer) {
				writer.println();
			}
		};
	}

	/**
	 * Returns a code block that adds statements that wraps if necessary each
	 * and every method parameter.
	 * 
	 * @return The built CodeBlock
	 */

	protected CodeBlock getWrapParameters() {
		final WrapParameterTemplatedFile wrap = new WrapParameterTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			@Override
			public InputStream getInputStream() {
				return wrap.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return wrap.getValue0(name);
			}

			@Override
			protected Collection getCollection() {
				return ProxyInterceptedMethodTemplatedFile.this.getMethod().getParameters();
			}

			@Override
			protected void prepareToWrite(Object element) {
				wrap.setParameter((MethodParameter) element);
			}

			@Override
			protected void writeBetweenElements(SourceWriter writer) {
				writer.print(",");
			}
		};
	}

	/**
	 * Builds a code block that catches and rethrows expected exceptions.
	 * 
	 * @return
	 */
	protected CodeBlock getRethrowExpectedExceptions() {
		// first build up a list of the exceptions that should be handled.
		final Method method = this.getMethod();
		final GeneratorContext context = method.getGeneratorContext();
		final List<Type> alreadyCaughts = new ArrayList<Type>();
		final Type exception = context.getType(Constants.EXCEPTION);
		final Type runtimeException = context.getType(Constants.RUNTIME_EXCEPTION);

		final List<Type> catchAndRethrow = new ArrayList<Type>();
		final Iterator<Type> thrown = this.getMethod().getThrownTypes().iterator();

		while (thrown.hasNext()) {
			final Type expected = thrown.next();

			if (expected.isAssignableTo(runtimeException)) {
				continue;
			}

			if (false == expected.isAssignableTo(exception)) {
				continue;
			}

			boolean dontCatch = false;
			final Iterator<Type> alreadyCaughtsIterator = alreadyCaughts.iterator();
			while (alreadyCaughtsIterator.hasNext()) {
				final Type alreadyCaught = (Type) alreadyCaughtsIterator.next();
				if (expected.isAssignableTo(alreadyCaught)) {
					dontCatch = true;
					break;
				}
			}
			if (dontCatch) {
				continue;
			}
			catchAndRethrow.add(expected);
		}

		// return the code block.
		final RethrowDeclaredExceptionTemplatedFile rethrow = new RethrowDeclaredExceptionTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			@Override
			public InputStream getInputStream() {
				return rethrow.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return rethrow.getValue0(name);
			}

			@Override
			protected Collection getCollection() {
				return catchAndRethrow;
			}

			@Override
			protected void prepareToWrite(Object element) {
				rethrow.setException((Type) element);
			}

			@Override
			protected void writeBetweenElements(SourceWriter writer) {
				writer.println();
			}
		};
	}

	protected StringLiteral getMethodName(){
		return new StringLiteral( this.getMethod().getName() );
	}
		
	protected StringLiteral getMethodReturnType(){
		return new StringLiteral( this.getMethod().getReturnType().getName() );
	}
	
	protected CodeBlock getMethodParameterTypes(){
		final List<MethodParameter> parameters = this.getMethod().getParameters();
		
		// this code block creates a String array holding the parameter type names.
		return new CodeBlock(){
			public boolean isEmpty(){
				return false;
			}
			public void write(final SourceWriter writer){
				writer.print( "new String[]{");
				
				final Iterator<MethodParameter> iterator = parameters.iterator();
				while( iterator.hasNext() ){
					final MethodParameter parameter = iterator.next();
					
					// write a quoted string holding the parameter type
					new StringLiteral( parameter.getType().getRuntimeName() ).write(writer);
					
					if( iterator.hasNext() ){
						writer.print( ",");
					}
				}
				
				writer.print( "}");
			}
		};
	}
	
	/**
	 * The actual template file is selected depending on whether the method
	 * returns void or not.
	 * 
	 * @return The name of the template resource
	 */
	@Override
	protected String getResourceName() {
		return Constants.TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.ADD_ADVICES.equals(name)) {
				value = this.getAddAdvices();
				break;
			}
			if (Constants.TARGET.equals(name)) {
				value = this.getTarget();
				break;
			}

			if (Constants.WRAP_PARAMETERS.equals(name)) {
				value = this.getWrapParameters();
				break;
			}
			if (Constants.INVOKE_TARGET_METHOD.equals(name)) {
				value = this.getInvokeTargetMethod();
				break;
			}
			if (Constants.INTERCEPTOR_CHAIN_INVOKE_PROCEED.equals(name)) {
				value = this.getInterceptorChainInvokeProceed();
				break;
			}
			if (Constants.RETHROW_EXPECTED_EXCEPTIONS.equals(name)) {
				value = this.getRethrowExpectedExceptions();
				break;
			}
			if (Constants.METHOD_NAME.equals(name)) {
				value = this.getMethodName();
				break;
			}
			if (Constants.IS_METHOD_NATIVE.equals(name)) {
				value = this.isMethodNative();
				break;
			}
			if (Constants.ENCLOSING_TYPE.equals(name)) {
				value = this.getEnclosingType();
				break;
			}
			if (Constants.METHOD_RETURN_TYPE.equals(name)) {
				value = this.getMethodReturnType();
				break;
			}
			if (Constants.METHOD_PARAMETER_TYPENAMES.equals(name)) {
				value = this.getMethodParameterTypes();
				break;
			}
			
			break;
		}
		return value;
	}

	protected CodeBlock getInvokeTargetMethod() {
		final InvokeTargetMethodTemplatedFile template = new InvokeTargetMethodTemplatedFile();
		template.setMethod(this.getMethod());
		return template;
	}

	protected CodeBlock getInterceptorChainInvokeProceed() {
		final InvokeInterceptorChainProceedTemplatedFile template = new InvokeInterceptorChainProceedTemplatedFile();
		template.setMethodReturnType(this.getMethod().getReturnType());
		return template;
	}
}
