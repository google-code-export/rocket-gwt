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
package rocket.beans.rebind.aop;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the proxy intercepted method template
 * 
 * @author Miroslav Pokorny
 */
public class ProxyInterceptedMethodTemplatedFile extends TemplatedCodeBlock {

	public ProxyInterceptedMethodTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The outter bean factory class
	 */
	private Type beanFactory;

	protected Type getBeanFactory() {
		ObjectHelper.checkNotNull("field:beanFactory", beanFactory);
		return this.beanFactory;
	}

	public void setBeanFactory(final Type beanFactory) {
		ObjectHelper.checkNotNull("parameter:beanFactory", beanFactory);
		this.beanFactory = beanFactory;
	}

	/**
	 * The method being proxied
	 */
	private NewMethod method;

	protected NewMethod getMethod() {
		ObjectHelper.checkNotNull("field:method", method);
		return this.method;
	}

	public void setMethod(final NewMethod method) {
		ObjectHelper.checkNotNull("parameter:method", method);
		this.method = method;
	}

	/**
	 * The target method parameter
	 */
	private MethodParameter target;

	protected MethodParameter getTarget() {
		ObjectHelper.checkNotNull("field:target", target);
		return this.target;
	}

	public void setTarget(final MethodParameter target) {
		ObjectHelper.checkNotNull("parameter:target", target);
		this.target = target;
	}

	/**
	 * A list containing the advices that apply to this method.
	 */
	private List advices;

	protected List getAdvices() {
		ObjectHelper.checkNotNull("field:advices", advices);
		return this.advices;
	}

	public void setAdvices(final List advices) {
		ObjectHelper.checkNotNull("parameter:advices", advices);
		this.advices = advices;
	}

	protected CodeBlock getAddAdvices() {
		final Type beanFactory = this.getBeanFactory();
		final AddAdviceTemplatedFile addAdvice = new AddAdviceTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return addAdvice.getInputStream();
			}

			protected Object getValue0(final String name) {
				return addAdvice.getValue0(name);
			}

			protected Collection getCollection() {
				return ProxyInterceptedMethodTemplatedFile.this.getAdvices();
			}

			protected void prepareToWrite(Object element) {
				final Advice advice = (Advice) element;
				addAdvice.setBeanId(advice.getAdvisorBeanId());
				addAdvice.setBeanFactory(beanFactory);
			}

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

			public InputStream getInputStream() {
				return wrap.getInputStream();
			}

			protected Object getValue0(final String name) {
				return wrap.getValue0(name);
			}

			protected Collection getCollection() {
				return ProxyInterceptedMethodTemplatedFile.this.getMethod().getParameters();
			}

			protected void prepareToWrite(Object element) {
				wrap.setParameter((MethodParameter) element);
			}

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
		final List alreadyCaughts = new ArrayList();
		final Type exception = context.getType(Constants.EXCEPTION);
		final Type runtimeException = context.getType(Constants.RUNTIME_EXCEPTION);

		final List catchAndRethrow = new ArrayList();
		final Iterator thrown = this.getMethod().getThrownTypes().iterator();

		while (thrown.hasNext()) {
			final Type expected = (Type) thrown.next();

			if (expected.isAssignableTo(runtimeException)) {
				continue;
			}

			if (false == expected.isAssignableTo(exception)) {
				continue;
			}

			boolean dontCatch = false;
			final Iterator alreadyCaughtsIterator = alreadyCaughts.iterator();
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

			public InputStream getInputStream() {
				return rethrow.getInputStream();
			}

			protected Object getValue0(final String name) {
				return rethrow.getValue0(name);
			}

			protected Collection getCollection() {
				return catchAndRethrow;
			}

			protected void prepareToWrite(Object element) {
				rethrow.setException((Type) element);
			}

			protected void writeBetweenElements(SourceWriter writer) {
				writer.println();
			}
		};
	}

	/**
	 * The actual template file is selected depending on whether the method
	 * returns void or not.
	 * 
	 * @return
	 */
	protected String getFileName() {
		return Constants.PROXY_INTERCEPTED_METHOD_TEMPLATE;
	}

	protected InputStream getInputStream() {
		final String filename = this.getFileName();
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file \"" + filename + "\".");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.PROXY_INTERCEPTED_METHOD_ADD_ADVICES.equals(name)) {
				value = this.getAddAdvices();
				break;
			}
			if (Constants.PROXY_INTERCEPTED_METHOD_TARGET.equals(name)) {
				value = this.getTarget();
				break;
			}

			if (Constants.PROXY_INTERCEPTED_METHOD_WRAP_PARAMETERS.equals(name)) {
				value = this.getWrapParameters();
				break;
			}
			if (Constants.PROXY_INTERCEPTED_METHOD_INVOKE_TARGET_METHOD.equals(name)) {
				value = this.getInvokeTargetMethod();
				break;
			}
			if (Constants.PROXY_INTERCEPTED_METHOD_INTERCEPTOR_CHAIN_INVOKE_PROCEED.equals(name)) {
				value = this.getInterceptorChainInvokeProceed();
				break;
			}
			if (Constants.PROXY_INTERCEPTED_METHOD_RETHROW_EXPECTED_EXCEPTIONS.equals(name)) {
				value = this.getRethrowExpectedExceptions();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder \"" + name + "\" not found, template file \"" + this.getFileName() + "\".");
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
