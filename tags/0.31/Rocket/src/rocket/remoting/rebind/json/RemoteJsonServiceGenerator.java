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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.AllMethodsVisitor;
import rocket.generator.rebind.visitor.MethodParameterVisitor;
import rocket.remoting.client.json.RemoteJsonServiceInvoker;
import rocket.util.client.HttpHelper;
import rocket.util.client.ObjectHelper;

/**
 * This Generator generates RemoteJsonService clients for any given
 * RemoteService interface
 * 
 * @author Miroslav Pokorny
 */
public class RemoteJsonServiceGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type serviceInterface, final String newTypeName) {
		ObjectHelper.checkNotNull("parameter:serviceInterface", serviceInterface);
		GeneratorHelper.checkJavaTypeName("parameter:TypeName", newTypeName);

		this.setServiceInterface(serviceInterface);
		this.verifyServiceInterface();
		this.verifyAsyncServiceInterface();

		final NewConcreteType client = this.createJsonServiceClientType(newTypeName);
		this.setJsonServiceClient(client);
		this.implementPublicMethods();

		return client;
	}

	/**
	 * Visits all the public methods on the service interface creating json
	 * client methods.
	 */
	protected void implementPublicMethods() {
		this.getGeneratorContext().info("Implementing async service interface methods");

		final AllMethodsVisitor publicMethodFinder = new AllMethodsVisitor() {
			protected boolean visit(final Method method) {
				RemoteJsonServiceGenerator.this.implementPublicMethod(method);
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		publicMethodFinder.start(this.getServiceInterface());
	}

	/**
	 * Adds a method to the jsonServiceClient that prepares an invoker etc.
	 * 
	 * @param method
	 *            A serviceInterface method.
	 */
	protected void implementPublicMethod(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		this.getGeneratorContext().debug("Implementing method " + method);

		final NewMethod asyncMethod = this.createCorrespondingAsyncServiceInterfaceMethod(method);

		final InvokerTemplatedFile body = new InvokerTemplatedFile();
		body.setHttpRequestParameterNames(this.getHttpRequestParameterNamesFromMethodAnnotation(method));
		body.setInvokerType(this.getInvokerTypeFromMethodAnnotation(method));
		body.setNewMethod(asyncMethod);
		body.setReturnType( method.getReturnType());
		
		asyncMethod.setBody(body);
	}

	protected List getHttpRequestParameterNamesFromMethodAnnotation(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		return method.getMetadataValues(Constants.HTTP_REQUEST_PARAMETER_NAME);
	}

	protected void throwHttpRequestParameterNameMissing(final MethodParameter parameter) {
		throw new RemoteJsonServiceGeneratorException("Unable to find the [" + Constants.HTTP_REQUEST_PARAMETER_NAME + "] annotation for the parameter "
				+ parameter);
	}

	/**
	 * Retrieves the appropriate invoker type based on the http request method
	 * annotation on the given method
	 * 
	 * @param method
	 *            A service method
	 * @return A sub class of {@link RemoteJsonServiceInvoker}
	 */
	protected Type getInvokerTypeFromMethodAnnotation(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final List values = method.getMetadataValues(Constants.HTTP_REQUEST_METHOD);
		if ( values.size() == 0 ) {
			throwHttpRequestMethodAnnotationException( method );
		}
		final String httpRequestMethod = (String) values.get(0);
		if (null == httpRequestMethod) {
			throwHttpRequestMethodAnnotationException( method );
		}

		Type type = null;
		while (true) {
			if (HttpHelper.isGet(httpRequestMethod)) {
				type = this.getRemoteGetJsonServiceInvoker();
				break;
			}
			if (HttpHelper.isPost(httpRequestMethod)) {
				type = this.getRemotePostJsonServiceInvoker();
				break;
			}

			this.throwHttpRequestMethodAnnotationException(method);
		}

		this.getGeneratorContext().debug("After reading annotation will be using invoker [" + type.getName() + "].");
		return type;
	}

	protected void throwHttpRequestMethodAnnotationException(final Method method) {
		throw new RemoteJsonServiceGeneratorException("The method " + method + " is missing the " + Constants.HTTP_REQUEST_METHOD
				+ " or contains an invalid value. GET or POST");
	}

	/**
	 * Attempts to find the corresponding method on the async interface.
	 * 
	 * @param method
	 *            A service interface method
	 * @return The matching async service interface method.
	 */
	protected NewMethod createCorrespondingAsyncServiceInterfaceMethod(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String methodName = method.getName();
		final List parameterTypes = this.buildASyncServiceInterfaceMethodParameters(method);
		final Method asyncMethod = this.getAsyncServiceInterface().findMostDerivedMethod(methodName, parameterTypes);
		if (null == asyncMethod) {
			this.throwMatchingAsyncInterfaceMethodNotFoundException(method);
		}
		if (false == asyncMethod.returnsVoid() ) {
			this.throwIncompatibleMethodFound(asyncMethod);
		}

		final NewMethod newMethod = asyncMethod.copy(this.getJsonServiceClient());

		final MethodParameterVisitor parameters = new MethodParameterVisitor() {
			protected boolean visit(final MethodParameter parameter) {
				final boolean skipRemaining = RemoteJsonServiceGenerator.this.visitMethodParameter((NewMethodParameter) parameter,
						this.counter);
				this.counter++;
				return skipRemaining;
			}

			int counter = 0;
		};
		parameters.start(newMethod);

		return newMethod;
	}

	protected boolean visitMethodParameter(final NewMethodParameter parameter, final int index) {
		parameter.setName("parameter" + index);
		parameter.setFinal(true);
		return false;
	}

	protected void throwMatchingAsyncInterfaceMethodNotFoundException(final Method method) {
		throw new RemoteJsonServiceGeneratorException("Unable to find corresponding async service interface method " + method.getName()
				+ " with parameters " + method.getParameters());
	}

	protected void throwIncompatibleMethodFound(final Method method) {
		throw new RemoteJsonServiceGeneratorException("The async service method " + method + " should return void and not "
				+ method.getReturnType().getName());
	}

	/**
	 * Builds a list that holds the parameter types for the async method given
	 * its service interface counterpart.
	 * 
	 * @param method
	 * @return
	 */
	protected List buildASyncServiceInterfaceMethodParameters(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final List parameterTypes = new ArrayList();

		final MethodParameterVisitor parameterVisitor = new MethodParameterVisitor() {
			protected boolean visit(final MethodParameter parameter) {
				RemoteJsonServiceGenerator.this.verifyParameterType(parameter);
				parameterTypes.add(parameter.getType());
				return false;
			}
		};
		parameterVisitor.start(method);
		parameterTypes.add(this.getAsyncCallbackType());

		return parameterTypes;
	}

	/**
	 * Verifies that the parameter for this service interface method is one of
	 * the supported types. Currently these are limited to all primitive types
	 * and String.
	 * 
	 * @param methodParameter
	 */
	protected void verifyParameterType(final MethodParameter methodParameter) {
		ObjectHelper.checkNotNull("parameter:methodParameter", methodParameter);

		final Type string = this.getGeneratorContext().getString();
		final Type parameterType = methodParameter.getType();

		final Type invoker = this.getRemoteJsonServiceInvoker();
		if (null == invoker.findMethod("addParameter", Arrays.asList(new Type[]{string, parameterType}))) {
			RemoteJsonServiceGenerator.this.throwUnsupportedParameterTypeException(methodParameter);
		}
	}

	protected void throwUnsupportedParameterTypeException(final MethodParameter parameter) {
		throw new RemoteJsonServiceGeneratorException("The parameter " + parameter + " contains an invalid type " + parameter.getType());
	}

	/**
	 * Verifies that a Async interface exists.
	 * 
	 * @param serviceInterface
	 */
	protected void verifyAsyncServiceInterface() {
		final String asyncServiceInterfaceTypeName = this.getASyncServiceInterfaceTypeName();
		this.getGeneratorContext().debug("Verifying async service interface: " + asyncServiceInterfaceTypeName);

		final Type async = this.getGeneratorContext().findType(asyncServiceInterfaceTypeName);
		if (null == async) {
			this.throwVerifyingAsyncInterfaceException("Unable to find type [" + asyncServiceInterfaceTypeName + "]");
		}

		if (false == async.isInterface()) {
			this.throwVerifyingAsyncInterfaceException("The type [" + asyncServiceInterfaceTypeName + "] is not an interface.");
		}
	}

	protected void throwVerifyingAsyncInterfaceException(final String message) {
		throw new RemoteJsonServiceGeneratorException(message);
	}

	/**
	 * Verifies that the incoming serviceInterface is an interface and extends
	 * RemoteJsonService
	 */
	protected void verifyServiceInterface() {
		final Type serviceInterface = this.getServiceInterface();

		this.getGeneratorContext().debug("Verifying service interface: " + serviceInterface.getName());

		// verify serviceInterface is an interface
		if (false == serviceInterface.isInterface()) {
			this.throwIncompatibleInterfacesException("The type [" + serviceInterface.getName() + "] is not an interface.");
		}

		// verify serviceInterface implements RemoteJsonService
		final Type remoteJsonServiceType = this.getRemoteJsonService();
		if (false == serviceInterface.isAssignableTo(remoteJsonServiceType)) {
			this.throwIncompatibleInterfacesException("The type [" + serviceInterface + "] does not implement "
					+ remoteJsonServiceType.getName());
		}
	}

	/**
	 * Throws an exception with the given message to notify that the service and
	 * async interfaces are not GWT rpc compatible.
	 * 
	 * @param message
	 */
	protected void throwIncompatibleInterfacesException(final String message) {
		throw new RemoteJsonServiceGeneratorException(message);
	}

	protected GeneratorContext createGeneratorContext() {
		return new GeneratorContext() {
			protected String getGeneratedTypeNameSuffix() {
				return Constants.CLIENT_SUFFIX;
			}
		};
	}

	public Type getRemoteJsonService() {
		return this.getGeneratorContext().getType(Constants.REMOTE_JSON_SERVICE);
	}

	public Type getAsyncCallbackType() {
		return this.getGeneratorContext().getType(Constants.ASYNC_CALLBACK);
	}

	public Type getClientSuperType() {
		return this.getGeneratorContext().getType(Constants.CLIENT_SUPER_TYPE);
	}

	private Type serviceInterface;

	protected Type getServiceInterface() {
		ObjectHelper.checkNotNull("field:serviceInterface", serviceInterface);
		return serviceInterface;
	}

	protected void setServiceInterface(final Type serviceInterface) {
		ObjectHelper.checkNotNull("parameter:serviceInterface", serviceInterface);
		this.serviceInterface = serviceInterface;
	}

	/**
	 * Helper which returns the corresponding async service interface for the
	 * given service interface.
	 * 
	 * @param serviceInterface
	 * @return
	 */
	protected Type getAsyncServiceInterface() {
		return this.getGeneratorContext().getType(this.getASyncServiceInterfaceTypeName());
	}

	protected String getASyncServiceInterfaceTypeName() {
		return this.getServiceInterface().getName() + Constants.ASYNC_INTERFACE_SUFFIX;
	}

	/**
	 * The NewConcreteType jsonServiceClient being generated
	 */
	private NewConcreteType jsonServiceClient;

	protected NewConcreteType getJsonServiceClient() {
		ObjectHelper.checkNotNull("field:jsonServiceClient", jsonServiceClient);
		return jsonServiceClient;
	}

	protected void setJsonServiceClient(final NewConcreteType jsonServiceClient) {
		ObjectHelper.checkNotNull("parameter:jsonServiceClient", jsonServiceClient);
		this.jsonServiceClient = jsonServiceClient;
	}

	/**
	 * Factory method which creates a new NewConcreteType settings it name,
	 * super class and implemented interfaces
	 * 
	 * @param newTypeName
	 * @return A new NewConcreteType
	 */
	protected NewConcreteType createJsonServiceClientType(final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		final NewConcreteType type = context.newConcreteType();
		type.setAbstract(false);
		type.setFinal(true);
		type.setName(newTypeName);
		type.setSuperType(this.getClientSuperType());
		type.addInterface(this.getAsyncServiceInterface());
		type.addInterface(this.getRemoteJsonService());
		type.addInterface(this.getServiceDefTarget());

		return type;
	}

	protected Type getServiceDefTarget() {
		return this.getGeneratorContext().getType(Constants.SERVICE_DEF_TARGET);
	}

	protected Type getRemoteJsonServiceInvoker() {
		return this.getGeneratorContext().getType(Constants.REMOTE_JSON_SERVICE_INVOKER_TYPE);
	}

	protected Type getRemoteGetJsonServiceInvoker() {
		return this.getGeneratorContext().getType(Constants.REMOTE_GET_JSON_SERVICE_INVOKER_TYPE);
	}

	protected Type getRemotePostJsonServiceInvoker() {
		return this.getGeneratorContext().getType(Constants.REMOTE_POST_JSON_SERVICE_INVOKER_TYPE);
	}
}
