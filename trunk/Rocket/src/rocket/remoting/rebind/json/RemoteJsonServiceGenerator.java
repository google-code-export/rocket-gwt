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
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.AllMethodsVisitor;
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

		final NewConcreteType client = this.createJsonServiceClient(newTypeName);
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
	 * @param method A serviceInterface method.
	 */
	protected void implementPublicMethod(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		while( true ){
			final String inputTransport = this.getInputTransportFromMethodAnnotation(method);
			if( Constants.INPUT_TRANSPORT_JSON_RPC.equals( inputTransport )){
				this.implementJsonRpcMethod( method );
				break;
			}
			if( Constants.INPUT_TRANSPORT_REQUEST_PARAMETERS.equals( inputTransport ) ){
				this.implementGetOrPostRequestParameters(method);
				break;
			}
			
			throw new RuntimeException();
		}
		
	}
	
	/**
	 * Helper which fetches the inputTransport value from an annotation belonging to the given method.
	 * It also validates that only valid values are accepted.
	 * @param method
	 * @return
	 */
	protected String getInputTransportFromMethodAnnotation(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final List values = method.getMetadataValues(Constants.INPUT_TRANSPORT_ANNOTATION );
		if( values.size() != 1 ){
			throwInputTransportMissing(method);
		}
		
		final String inputTransport = (String) values.get( 0 );
		if( false == Constants.INPUT_TRANSPORT_JSON_RPC.equals( inputTransport ) && false == Constants.INPUT_TRANSPORT_REQUEST_PARAMETERS.equals( inputTransport ) ){
			this.throwInvalidInputTransport(method, inputTransport );
		}
		
		return inputTransport;
	}

	protected void throwInputTransportMissing(final Method method ) {
		throw new RemoteJsonServiceGeneratorException("Unable to find the [" + Constants.INPUT_TRANSPORT_ANNOTATION + "] annotation for the method " + method );
	}
	
	protected void throwInvalidInputTransport(final Method method, final String value ) {
		throw new RemoteJsonServiceGeneratorException("The [" + Constants.INPUT_TRANSPORT_ANNOTATION + "] annotation for the method " + method + " contains an invalid value [" + value + "]");
	}
	
	/**
	 * Implements a method that is a bridge that invokes a json rpc encoded response and deserializes responses calling
	 * the provided callback.
	 * @param method
	 */
	protected void implementJsonRpcMethod( final Method method ){
		ObjectHelper.checkNotNull("parameter:method", method );
		
		this.getGeneratorContext().debug("Implementing json rpc method, method: " + method);
		
		final List parameters = method.getParameters();
		if( parameters.size() != 1 ){
			throwInvalidJsonRpcMethod( method );
		}
		
		final NewMethod asyncMethod = this.createCorrespondingAsyncServiceInterfaceMethod(method);

		final JsonRpcInvokerTemplatedFile body = new JsonRpcInvokerTemplatedFile();
		
		final MethodParameter parameter = (MethodParameter)parameters.get( 0 );
		body.setParameterType( parameter.getType() );
		
		body.setPayloadType(method.getReturnType());

		asyncMethod.setBody(body);			
	}
	
	protected void throwInvalidJsonRpcMethod( final Method method ){
		throw new RemoteJsonServiceGeneratorException("A method marked with " + Constants.INPUT_TRANSPORT_ANNOTATION + "=" + Constants.INPUT_TRANSPORT_JSON_RPC + " can only have a single JsonSerializable parameter, method: " + method );
	}

	/**
	 * This method satisfy the currently being generated method passing simple parameters
	 * via request parameters
	 * @param method
	 */
	protected void implementGetOrPostRequestParameters( final Method method ){
		ObjectHelper.checkNotNull("parameter:method", method );
		
		this.getGeneratorContext().debug("Implementing method that sends parameters as request parameters, method: " + method);
		
		final Iterator parameters = method.getParameters().iterator();
		while( parameters.hasNext() ){
			final MethodParameter methodParameter = (MethodParameter) parameters.next();
			final Type parameterType = methodParameter.getType();		
			if ( false == this.isSimpleType( parameterType )) {
				this.throwUnsupportedParameterTypeException(methodParameter);
			}
		}
		
		final NewMethod asyncMethod = this.createCorrespondingAsyncServiceInterfaceMethod(method);

		final RequestParametersTransportInvokerTemplatedFile body = new RequestParametersTransportInvokerTemplatedFile();
		body.setHttpRequestParameterNames(this.getHttpRequestParameterNamesFromMethodAnnotation(method));
		body.setInvokerType(this.getInvokerTypeFromMethodAnnotation(method));
		body.setNewMethod(asyncMethod);
		body.setPayloadType(method.getReturnType());

		asyncMethod.setBody(body);		
	}
	
	protected void throwUnsupportedParameterTypeException(final MethodParameter parameter) {
		throw new RemoteJsonServiceGeneratorException("The parameter " + parameter + " contains an invalid type " + parameter.getType());
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
		final List asyncMethodParameters = new ArrayList();
		final Iterator parameters = method.getParameters().iterator();
		while( parameters.hasNext() ){
			final MethodParameter methodParameter = (MethodParameter) parameters.next();
			asyncMethodParameters.add( methodParameter.getType() );
		}
		asyncMethodParameters.add( this.getAsyncCallback() );
		
		final Method asyncMethod = this.getAsyncServiceInterface().findMostDerivedMethod(methodName, asyncMethodParameters);
		if (null == asyncMethod) {
			this.throwMatchingAsyncInterfaceMethodNotFoundException(method);
		}
		if (false == asyncMethod.returnsVoid()) {
			this.throwIncompatibleMethodFound(asyncMethod);
		}

		final NewMethod newMethod = asyncMethod.copy(this.getJsonServiceClient());
		newMethod.setAbstract( false );
		newMethod.setFinal( true );
		GeneratorHelper.renameParametersToParameterN( newMethod );

		return newMethod;
	}
	
	protected void throwMatchingAsyncInterfaceMethodNotFoundException(final Method method) {
		throw new RemoteJsonServiceGeneratorException("Unable to find corresponding async service interface method " + method );
	}

	protected void throwIncompatibleMethodFound(final Method method) {
		throw new RemoteJsonServiceGeneratorException("The async service method " + method + " should return void and not " + method.getReturnType().getName());
	}

	protected List getHttpRequestParameterNamesFromMethodAnnotation(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		return method.getMetadataValues(Constants.HTTP_REQUEST_PARAMETER_NAME_ANNOTATION);
	}

	protected void throwHttpRequestParameterNameMissing(final MethodParameter parameter) {
		throw new RemoteJsonServiceGeneratorException("Unable to find the [" + Constants.HTTP_REQUEST_PARAMETER_NAME_ANNOTATION
				+ "] annotation for the parameter " + parameter);
	}
	
	/**
	 * Tests if the given type is a simple type ie a primitive or String.
	 * @param type
	 * @return
	 */
	protected boolean isSimpleType( final Type type ){
		ObjectHelper.checkNotNull( "parameter:type", type );
		
		return type.isPrimitive() || type.equals( type.getGeneratorContext().getString() );
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

		this.getGeneratorContext().debug("After reading annotation the method " + method + " will use the invoker [" + type.getName() + "].");
		return type;
	}

	protected void throwHttpRequestMethodAnnotationException(final Method method) {
		throw new RemoteJsonServiceGeneratorException("The method " + method + " is missing the " + Constants.HTTP_REQUEST_METHOD_ANNOTATION + " or contains an invalid value. GET or POST");
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
			this.throwIncompatibleInterfacesException("The type [" + serviceInterface + "] does not implement " + remoteJsonServiceType.getName());
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

	public Type getAsyncCallback() {
		return this.getGeneratorContext().getType(Constants.ASYNC_CALLBACK);
	}

	public Type getRemoteJsonServiceClient() {
		return this.getGeneratorContext().getType(Constants.REMOTE_JSON_SERVICE_CLIENT_SUPER_TYPE);
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
	protected NewConcreteType createJsonServiceClient(final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		final NewConcreteType type = context.newConcreteType();
		type.setAbstract(false);
		type.setFinal(true);
		type.setName(newTypeName);
		type.setSuperType(this.getRemoteJsonServiceClient());
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
