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
import java.util.Collection;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the invoke target method template
 * 
 * @author Miroslav Pokorny
 */
public class InvokeTargetMethodTemplatedFile extends TemplatedCodeBlock {

	public InvokeTargetMethodTemplatedFile() {
		super();
		setNative(false);
	}

	/**
	 * The target method
	 */
	private NewMethod method;

	protected NewMethod getMethod() {
		ObjectHelper.checkNotNull("field:method", method);
		return this.method;
	}

	public void setMethod(final NewMethod proxy) {
		ObjectHelper.checkNotNull("parameter:method", proxy);
		this.method = proxy;
	}

	/**
	 * The actual template file is selected depending on whether the method
	 * returns void or not.
	 * 
	 * @return The file name
	 */
	protected String getFileName() {
		return this.getMethod().returnsVoid() ? Constants.INVOKE_TARGET_METHOD_VOID_TEMPLATE : Constants.INVOKE_TARGET_METHOD_TEMPLATE;
	}

	protected InputStream getInputStream() {
		final String filename = this.getFileName();
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.INVOKE_TARGET_METHOD_METHOD.equals(name)) {
				value = this.getMethod();
				break;
			}
			if (Constants.INVOKE_TARGET_METHOD_UNWRAP_PARAMETERS.equals(name)) {
				value = this.getUnwrapParameters();
				break;
			}
			break;
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found, template file [" + this.getFileName() + "]");
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
