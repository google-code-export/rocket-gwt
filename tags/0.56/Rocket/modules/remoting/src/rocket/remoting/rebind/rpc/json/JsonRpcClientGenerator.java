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
package rocket.remoting.rebind.rpc.json;

import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.remoting.client.support.rpc.JsonServiceMethodInvoker;
import rocket.remoting.rebind.rpc.RpcClientGenerator;
import rocket.remoting.rebind.rpc.json.post.JsonConstants;
import rocket.remoting.rebind.rpc.json.post.JsonRpcInvokerTemplatedFile;
import rocket.remoting.rebind.rpc.json.requestparameters.RequestParametersConstants;
import rocket.remoting.rebind.rpc.json.requestparameters.RequestParametersInvokerTemplatedFile;
import rocket.util.client.Checker;
import rocket.util.client.Tester;

/**
 * This generator generates a client proxy that may be used to invoke either a
 * json encoded rpc or a java rpc on the server.
 * 
 * @author Miroslav Pokorny
 */
public class JsonRpcClientGenerator extends RpcClientGenerator {

	@Override
	protected NewConcreteType assembleNewType(final Type serviceInterface, final String newTypeName) {
		Checker.notNull("parameter:serviceInterface", serviceInterface);
		GeneratorHelper.checkJavaTypeName("parameter:TypeName", newTypeName);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();

		this.verifyServiceInterface(serviceInterface);
		this.verifyAsyncServiceInterface(serviceInterface);

		final NewConcreteType client = this.createRpcServiceClient(newTypeName, serviceInterface, this.getJsonRpcServiceClient());
		this.implementPublicMethods(serviceInterface, client);

		context.unbranch();
		return client;
	}

	/**
	 * Adds a method to the jsonServiceClient that prepares an invoker etc.
	 * 
	 * @param method
	 *            The method being implemented
	 * @param serviceInterface
	 *            The service interface being implemented
	 * @param asyncServiceInterface
	 *            The matching async service interface
	 * @param remoteJsonClient
	 *            The remote
	 */
	protected void implementPublicMethod(final Method method, final Type serviceInterface, final Type asyncServiceInterface,
			final NewConcreteType remoteJsonClient) {
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:serviceInterface", serviceInterface);
		Checker.notNull("parameter:asyncServiceInterface", asyncServiceInterface);
		Checker.notNull("parameter:remoteJsonClient", remoteJsonClient);

		while (true) {
			final String inputArguments = this.getInputArgumentEncodingFromMethodAnnotation(method);
			if (Constants.INPUT_ARGUMENTS_JSON_RPC.equals(inputArguments)) {
				this.implementJsonRpcMethod(method, asyncServiceInterface, remoteJsonClient);
				break;
			}
			if (Constants.INPUT_ARGUMENTS_REQUEST_PARAMETERS.equals(inputArguments)) {
				this.implementGetOrPostRequestParameters(method, asyncServiceInterface, remoteJsonClient);
				break;
			}

			throw new RuntimeException();
		}
	}

	/**
	 * Helper which fetches the inputArguments value from an annotation
	 * belonging to the given method. It also validates that only valid values
	 * are accepted.
	 * 
	 * @param method
	 * @return
	 */
	protected String getInputArgumentEncodingFromMethodAnnotation(final Method method) {
		Checker.notNull("parameter:method", method);

		final List values = method.getMetadataValues(Constants.INPUT_ARGUMENTS_ANNOTATION);
		if (values.size() != 1) {
			throwInputArgumentEncodingMissing(method);
		}

		final String inputArguments = (String) values.get(0);
		if (false == Constants.INPUT_ARGUMENTS_JSON_RPC.equals(inputArguments)
				&& false == Constants.INPUT_ARGUMENTS_REQUEST_PARAMETERS.equals(inputArguments)) {
			this.throwInvalidInputArgumentEncoding(method, inputArguments);
		}

		return inputArguments;
	}

	protected void throwInputArgumentEncodingMissing(final Method method) {
		this.throwException("Unable to find the \"" + Constants.INPUT_ARGUMENTS_ANNOTATION + "\" annotation for the method " + method);
	}

	protected void throwInvalidInputArgumentEncoding(final Method method, final String value) {
		this.throwException("The \"" + Constants.INPUT_ARGUMENTS_ANNOTATION + "\" annotation for the method " + method
				+ " contains an invalid value \"" + value + "\".");
	}

	/**
	 * Implements a method that is a bridge that invokes a json rpc encoded
	 * response and deserializes responses calling the provided callback.
	 * 
	 * @param method
	 */
	protected void implementJsonRpcMethod(final Method method, final Type asyncServiceInterface, final NewConcreteType remoteJsonClient) {
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:asyncServiceInterface", asyncServiceInterface);
		Checker.notNull("parameter:remoteJsonClient", remoteJsonClient);

		this.getGeneratorContext().info("Implementing json rpc method, method: " + method);

		final NewMethod asyncMethod = this.createCorrespondingAsyncServiceInterfaceMethod(method, asyncServiceInterface,
				remoteJsonClient);
		final List parameters = asyncMethod.getParameters();
		if (parameters.size() != 2) {
			throwInvalidJsonRpcMethod(method);
		}

		final NewMethodParameter parameter = (NewMethodParameter) parameters.get(0);
		parameter.setName(JsonConstants.INVOKER_PARAMETER_PARAMETER);
		final NewMethodParameter callback = (NewMethodParameter) parameters.get(1);
		callback.setName(JsonConstants.INVOKER_CALLBACK_PARAMETER);

		final JsonRpcInvokerTemplatedFile body = new JsonRpcInvokerTemplatedFile();
		body.setParameterType(parameter.getType());
		body.setReturnType(method.getReturnType());
		asyncMethod.setBody(body);
	}

	protected void throwInvalidJsonRpcMethod(final Method method) {
		this.throwException("The method " + method + " marked with " + Constants.INPUT_ARGUMENTS_ANNOTATION + "="
				+ Constants.INPUT_ARGUMENTS_JSON_RPC + " can only have a single JsonSerializable parameter, method: " + method);
	}

	/**
	 * This method satisfy the currently being generated method passing simple
	 * parameters via request parameters
	 * 
	 * @param method
	 */
	protected void implementGetOrPostRequestParameters(final Method method, final Type asyncServiceInterface,
			final NewConcreteType remoteJsonClient) {
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:asyncServiceInterface", asyncServiceInterface);
		Checker.notNull("parameter:remoteJsonClient", remoteJsonClient);

		this.getGeneratorContext().debug("Implementing method that sends method parameters as request parameters, method: " + method);

		final Iterator parameters = method.getParameters().iterator();
		while (parameters.hasNext()) {
			final MethodParameter parameter = (MethodParameter) parameters.next();
			final Type parameterType = parameter.getType();
			if (false == this.isSimpleType(parameterType)) {
				this.throwUnsupportedParameterTypeException(parameter);
			}
		}

		final NewMethod asyncMethod = this.createCorrespondingAsyncServiceInterfaceMethod(method, asyncServiceInterface,
				remoteJsonClient);

		final RequestParametersInvokerTemplatedFile body = new RequestParametersInvokerTemplatedFile();
		body.setHttpRequestParameterNames(this.getHttpRequestParameterNamesFromMethodAnnotation(method));
		body.setInvokerType(this.getInvokerTypeFromMethodAnnotation(method));
		body.setParameters(asyncMethod.getParameters());
		body.setReturnType(method.getReturnType());

		final Iterator newMethodParameters = asyncMethod.getParameters().iterator();
		while (newMethodParameters.hasNext()) {
			final NewMethodParameter parameter = (NewMethodParameter) newMethodParameters.next();
			parameter.setFinal(true);

			if (false == newMethodParameters.hasNext()) {
				parameter.setName(RequestParametersConstants.REQUEST_PARAMETERS_CALLBACK_PARAMETER);
			}
		}

		asyncMethod.setBody(body);
	}

	protected void throwUnsupportedParameterTypeException(final MethodParameter parameter) {
		this.throwException("The parameter " + parameter + " contains an invalid type " + parameter.getType());
	}

	protected List getHttpRequestParameterNamesFromMethodAnnotation(final Method method) {
		Checker.notNull("parameter:method", method);

		return method.getMetadataValues(Constants.HTTP_REQUEST_PARAMETER_NAME_ANNOTATION);
	}

	protected void throwHttpRequestParameterNameMissing(final MethodParameter parameter) {
		this.throwException("Unable to find the \"" + Constants.HTTP_REQUEST_PARAMETER_NAME_ANNOTATION
				+ "\" annotation for the parameter " + parameter);
	}

	/**
	 * Tests if the given type is a simple type ie a primitive or String.
	 * 
	 * @param type
	 * @return
	 */
	protected boolean isSimpleType(final Type type) {
		Checker.notNull("parameter:type", type);

		return type.isPrimitive() || type.equals(type.getGeneratorContext().getString());
	}

	/**
	 * Retrieves the appropriate invoker type based on the http request method
	 * annotation on the given method
	 * 
	 * @param method
	 *            A service method
	 * @return A sub class of {@link JsonServiceMethodInvoker}
	 */
	protected Type getInvokerTypeFromMethodAnnotation(final Method method) {
		Checker.notNull("parameter:method", method);

		final List values = method.getMetadataValues(Constants.HTTP_REQUEST_METHOD_ANNOTATION);
		if (values.size() == 0) {
			throwHttpRequestMethodAnnotationException(method);
		}
		final String httpRequestMethod = (String) values.get(0);
		if (null == httpRequestMethod) {
			throwHttpRequestMethodAnnotationException(method);
		}

		Type type = null;
		while (true) {
			if (Tester.isGet(httpRequestMethod)) {
				type = this.getGetJsonRpcServiceInvoker();
				break;
			}
			if (Tester.isPost(httpRequestMethod)) {
				type = this.getPostJsonRpcServiceInvoker();
				break;
			}

			this.throwHttpRequestMethodAnnotationException(method);
		}

		this.getGeneratorContext().debug(
				"After reading annotation the method " + method + " will use the invoker \"" + type.getName() + "\".");
		return type;
	}

	protected void throwHttpRequestMethodAnnotationException(final Method method) {
		this.throwException("The method " + method + " is missing the " + Constants.HTTP_REQUEST_METHOD_ANNOTATION
				+ " or contains an invalid value. GET or POST");
	}

	protected String getGeneratedTypeNameSuffix() {
		return Constants.CLIENT_SUFFIX;
	}

	public Type getRemoteJsonService() {
		return this.getGeneratorContext().getType(Constants.REMOTE_JSON_SERVICE);
	}

	public Type getJsonRpcServiceClient() {
		return this.getGeneratorContext().getType(Constants.JSON_RPC_SERVICE_CLIENT);
	}

	protected Type getRequiredInterface() {
		return this.getRemoteJsonService();
	}

	protected Type getJsonRpcServiceMethodInvoker() {
		return this.getGeneratorContext().getType(Constants.JSON_SERVICE_METHOD_INVOKER);
	}

	protected Type getGetJsonRpcServiceInvoker() {
		return this.getGeneratorContext().getType(Constants.GET_JSON_RPC_SERVICE_INVOKER);
	}

	protected Type getPostJsonRpcServiceInvoker() {
		return this.getGeneratorContext().getType(Constants.POST_JSON_RPC_SERVICE_INVOKER);
	}

	protected void throwException(final String message) {
		throw new JsonRpcClientGeneratorException(message);
	}
}
