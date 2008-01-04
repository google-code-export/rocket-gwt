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
package rocket.remoting.rebind.rpc.java.servicemethodinvoker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * An abstraction for the request-parameters-transport-invoker template file.
 * 
 * @author Miroslav Pokorny
 */
public class ServiceMethodInvokerTemplatedFile extends TemplatedFileCodeBlock {

	public ServiceMethodInvokerTemplatedFile() {
		super();
	}

	/**
	 * The serialization factory composer.
	 */
	private Type serializationFactoryComposer;

	protected Type getSerializationFactoryComposer() {
		ObjectHelper.checkNotNull("field:serializationFactoryComposer", serializationFactoryComposer);
		return this.serializationFactoryComposer;
	}

	public void setSerializationFactoryComposer(final Type serializationFactoryComposer) {
		ObjectHelper.checkNotNull("parameter:serializationFactoryComposer", serializationFactoryComposer);
		this.serializationFactoryComposer = serializationFactoryComposer;
	}

	/**
	 * The service interface being implemented.
	 */
	private Type serviceInterface;

	protected Type getServiceInterface() {
		ObjectHelper.checkNotNull("field:serviceInterface", serviceInterface);
		return this.serviceInterface;
	}

	public void setServiceInterface(final Type serviceInterface) {
		ObjectHelper.checkNotNull("parameter:serviceInterface", serviceInterface);
		this.serviceInterface = serviceInterface;
	}

	/**
	 * The service method being executed
	 */
	private Method method;

	protected Method getMethod() {
		ObjectHelper.checkNotNull("field:method", method);
		return this.method;
	}

	public void setMethod(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);
		this.method = method;
	}

	/**
	 * The method parameters for the method being implemented.
	 */
	private List parameters;

	protected List getParameters() {
		ObjectHelper.checkNotNull("parameters:parameters", parameters);
		return this.parameters;
	}

	public void setParameters(final List parameters) {
		ObjectHelper.checkNotNull("parameter:parameters", parameters);
		this.parameters = parameters;
	}

	protected CodeBlock getAddParameters() {
		final List parameters = this.getParameters();
		final List parametersLessCallback = parameters.subList(0, parameters.size() - 1);

		// parameter

		final AddParameterTemplatedFile repeated = new AddParameterTemplatedFile();

		final CollectionTemplatedCodeBlock template = new CollectionTemplatedCodeBlock() {
			protected Collection getCollection() {
				return parametersLessCallback;
			}

			protected void prepareToWrite(final Object element) {
				final MethodParameter methodParameter = (MethodParameter) element;
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

	protected CodeBlock getAddParameterTypeNames() {
		final List parameters = new ArrayList();
		parameters.addAll(this.getParameters());
		parameters.remove(parameters.size() - 1); // remove the callback
		// parameter

		final AddParameterTypeNameTemplatedFile repeated = new AddParameterTypeNameTemplatedFile();

		final CollectionTemplatedCodeBlock template = new CollectionTemplatedCodeBlock() {
			protected Collection getCollection() {
				return parameters;
			}

			protected void prepareToWrite(final Object element) {
				final MethodParameter methodParameter = (MethodParameter) element;
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

	protected String getResourceName() {
		return ServiceMethodInvokerConstants.SERVICE_METHOD_INVOKER_TEMPLATE;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (ServiceMethodInvokerConstants.SERVICE_METHOD_INVOKER_SERIALIZATION_FACTORY_COMPOSER.equals(name)) {
				value = this.getSerializationFactoryComposer();
				break;
			}
			if (ServiceMethodInvokerConstants.SERVICE_METHOD_INVOKER_INTERFACE_TYPENAME.equals(name)) {
				value = new StringLiteral(this.getServiceInterface().getName());
				break;
			}
			if (ServiceMethodInvokerConstants.SERVICE_METHOD_INVOKER_METHOD_NAME.equals(name)) {
				value = new StringLiteral(this.getMethod().getName());
				break;
			}
			if (ServiceMethodInvokerConstants.SERVICE_METHOD_INVOKER_ADD_PARAMETER_TYPENAMES.equals(name)) {
				value = this.getAddParameterTypeNames();
				break;
			}
			if (ServiceMethodInvokerConstants.SERVICE_METHOD_INVOKER_ADD_PARAMETERS.equals(name)) {
				value = this.getAddParameters();
				break;
			}
			break;
		}
		return value;
	}
}
