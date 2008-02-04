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
package rocket.beans.rebind.aop.invoketarget;

import java.io.InputStream;
import java.util.Collection;

import rocket.beans.rebind.aop.unwrapparameter.UnwrapParameterTemplatedFile;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.util.client.Checker;

/**
 * An abstraction for the invoke target method template
 * 
 * @author Miroslav Pokorny
 */
public class InvokeTargetMethodTemplatedFile extends TemplatedFileCodeBlock {

	public InvokeTargetMethodTemplatedFile() {
		super();
	}
	
	public boolean isNative(){
		return false;
	}
	
	public void setNative( final boolean ignored ){
		throw new UnsupportedOperationException();
	}

	/**
	 * The target method
	 */
	private Method method;

	protected Method getMethod() {
		Checker.notNull("field:method", method);
		return this.method;
	}

	public void setMethod(final Method proxy) {
		Checker.notNull("parameter:method", proxy);
		this.method = proxy;
	}

	/**
	 * The actual template file is selected depending on whether the method
	 * returns void or not.
	 * 
	 * @return The file name
	 */
	protected String getResourceName() {
		return this.getMethod().returnsVoid() ? Constants.VOID_TEMPLATE : Constants.TEMPLATE;
	}

	public InputStream getInputStream(){
		return super.getInputStream();
	}
	
	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.METHOD.equals(name)) {
				value = this.getMethod();
				break;
			}
			if (Constants.UNWRAP_PARAMETERS.equals(name)) {
				value = this.getUnwrapParameters();
				break;
			}
			break;
		}
		return value;
	}

	/**
	 * Returns a code block that adds statements to unwrap each of the wrapped
	 * parameters from the interceptor chain.
	 * 
	 * @return
	 */
	protected CodeBlock getUnwrapParameters() {
		final UnwrapParameterTemplatedFile unwrap = new UnwrapParameterTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return unwrap.getInputStream();
			}

			protected Object getValue0(final String name) {
				return unwrap.getValue0(name);
			}

			protected Collection getCollection() {
				return InvokeTargetMethodTemplatedFile.this.getMethod().getParameters();
			}

			protected void prepareToWrite(Object element) {
				unwrap.setParameter((MethodParameter) element);
			}

			protected void writeBetweenElements(SourceWriter writer) {
				writer.print(",");
			}
		};
	}
}
