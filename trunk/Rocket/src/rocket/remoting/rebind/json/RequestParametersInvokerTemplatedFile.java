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
package rocket.remoting.rebind.json;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import rocket.generator.rebind.SourceWriter;

/**
 * An abstraction for the request-parameters-transport-invoker template file.
 * 
 * @author Miroslav Pokorny
 */
class RequestParametersInvokerTemplatedFile extends TemplatedCodeBlock {

	public RequestParametersInvokerTemplatedFile() {
		super();
		setNative(false);
	}

	private Type invokerType;

	protected Type getInvokerType() {
		ObjectHelper.checkNotNull("invokerType:invokerType", invokerType);
		return this.invokerType;
	}

	public void setInvokerType(final Type invokerType) {
		ObjectHelper.checkNotNull("parameter:invokerType", invokerType);
		this.invokerType = invokerType;
	}

	/**
	 * THe return type of the service.
	 */
	private Type payloadType;

	protected Type getPayloadType() {
		ObjectHelper.checkNotNull("returnType:payloadType", payloadType);
		return this.payloadType;
	}

	public void setPayloadType(final Type payloadType) {
		ObjectHelper.checkNotNull("parameter:payloadType", payloadType);
		this.payloadType = payloadType;
	}

	/**
	 * THe async service method being implemented
	 */
	private NewMethod newMethod;

	protected NewMethod getNewMethod() {
		ObjectHelper.checkNotNull("newMethod:newMethod", newMethod);
		return this.newMethod;
	}

	public void setNewMethod(final NewMethod newMethod) {
		ObjectHelper.checkNotNull("parameter:newMethod", newMethod);
		this.newMethod = newMethod;
	}

	private List httpRequestParameterNames;

	protected List getHttpRequestParameterNames() {
		ObjectHelper.checkNotNull("field:httpRequestParameterNames", httpRequestParameterNames);
		return this.httpRequestParameterNames;
	}

	public void setHttpRequestParameterNames(final List httpRequestParameterNames) {
		ObjectHelper.checkNotNull("parameter:httpRequestParameterNames", httpRequestParameterNames);
		this.httpRequestParameterNames = httpRequestParameterNames;
	}

	protected CodeBlock getAddParameters() {
		final List parameters = new ArrayList();
		parameters.addAll(this.getNewMethod().getParameters());
		parameters.remove(parameters.size() - 1); // remove the callback
		// parameter

		final RequestParametersInvokerAddParameterTemplatedFile repeated = new RequestParametersInvokerAddParameterTemplatedFile();
		final List httpRequestParameterNames = this.getHttpRequestParameterNames();

		final CollectionTemplatedCodeBlock template = new CollectionTemplatedCodeBlock() {
			protected Collection getCollection() {
				return parameters;
			}

			protected void prepareToWrite(Object element) {
				final MethodParameter methodParameter = (MethodParameter) element;

				final String name = (String) httpRequestParameterNames.get(this.getIndex());
				repeated.setHttpRequestParameterName(name);
				repeated.setParameter(methodParameter);
			}

			protected void write0(final SourceWriter writer) {
				repeated.write(writer);
			}

			protected void writeBetweenElements(SourceWriter writer) {
				writer.println();
			}

			protected InputStream getInputStream() {
				return repeated.getInputStream();
			}

			protected Object getValue0(final String name) {
				return repeated.getValue0(name);
			}
		};

		return template;
	}

	protected InputStream getInputStream() {
		final String filename = Constants.REQUEST_PARAMETERS_INVOKER_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.REQUEST_PARAMETERS_INVOKER_ADD_PARAMETERS.equals(name)) {
				value = this.getAddParameters();
				break;
			}
			if (Constants.REQUEST_PARAMETERS_INVOKER_INVOKER_TYPE.equals(name)) {
				value = this.getInvokerType();
				break;
			}
			if (Constants.REQUEST_PARAMETERS_INVOKER_PAYLOAD_TYPE.equals(name)) {
				value = this.getPayloadType();
				break;
			}
			break;
		}
		return value;
	}
}
