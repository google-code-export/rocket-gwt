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
package rocket.remoting.rebind.rpc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.type.TypeNotFoundException;
import rocket.generator.rebind.visitor.AllMethodsVisitor;
import rocket.util.client.Checker;

/**
 * This generator generates a client proxy that may be used to invoke either a
 * json encoded rpc or a java rpc on the server.
 * 
 * @author Miroslav Pokorny
 */
abstract public class RpcClientGenerator extends Generator {

	protected RpcClientGenerator() {
		super();
	}

	/**
	 * Verifies that the incoming serviceInterface is an interface and extends
	 * JsonRpcService
	 */
	protected void verifyServiceInterface(final Type serviceInterface) {
		final GeneratorContext context = this.getGeneratorContext();
		context.info("Verifying service interface: " + serviceInterface);

		// verify serviceInterface is an interface
		if (false == serviceInterface.isInterface()) {
			this.throwServiceInterfaceIsNotAnInterface(serviceInterface);
		}

		// verify serviceInterface implements JsonRpcService
		final Type requiredServiceInterface = this.getRequiredInterface();
		if (false == serviceInterface.isAssignableTo(requiredServiceInterface)) {
			this.throwDoesntImplementRemoteJsonService(serviceInterface);
		}

		context.debug(serviceInterface.toString());
	}

	protected void throwServiceInterfaceIsNotAnInterface(final Type type) {
		this.throwException("The type: " + type + " is not an interface.");
	}

	protected void throwDoesntImplementRemoteJsonService(final Type type) {
		this.throwException("The type: " + type + " does not implement " + this.getRequiredInterface());
	}

	/**
	 * Verifies that a Async interface actually exists.
	 * 
	 * @param serviceInterface
	 */
	protected void verifyAsyncServiceInterface(final Type serviceInterface) {
		Checker.notNull("parameter:serviceInterface", serviceInterface);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Verifying async interface for : " + serviceInterface);

		try {
			final Type asyncServiceInterface = this.getAsyncServiceInterface(serviceInterface);

			if (false == asyncServiceInterface.isInterface()) {
				this.throwAsyncTypeIsNotAnInterface(asyncServiceInterface);
			}

			context.debug(asyncServiceInterface.toString());

		} catch (final TypeNotFoundException notFound) {
			this.throwUnableToFindAsyncType(serviceInterface);
		}

		context.unbranch();
	}

	protected void throwUnableToFindAsyncType(final Type serviceInterface) {
		this.throwException("Unable to find the async interface for " + serviceInterface);
	}

	protected void throwAsyncTypeIsNotAnInterface(final Type asyncInterface) {
		this.throwException("The type " + asyncInterface + " is not actually an interface.");
	}

	/**
	 * Factory method which creates a new NewConcreteType settings it name,
	 * super class and implemented interfaces
	 * 
	 * @param newTypeName
	 * @param serviceInterface
	 * @param superType
	 * @return A new NewConcreteType
	 */
	protected NewConcreteType createRpcServiceClient(final String newTypeName, final Type serviceInterface, final Type superType) {
		final GeneratorContext context = this.getGeneratorContext();
		final NewConcreteType type = context.newConcreteType(newTypeName);
		type.setAbstract(false);
		type.setFinal(true);
		type.setSuperType(superType);
		type.setVisibility(Visibility.PUBLIC);

		type.addInterface(this.getAsyncServiceInterface(serviceInterface));
		type.addInterface(this.getRequiredInterface());
		type.addInterface(this.getServiceDefTarget());

		return type;
	}

	/**
	 * Visits all the public methods on the service interface implementing a
	 * proxy for each service method.
	 */
	protected void implementPublicMethods(final Type serviceInterface, final NewConcreteType client) {
		Checker.notNull("parameter:serviceInterface", serviceInterface);
		Checker.notNull("parameter:client", client);

		this.getGeneratorContext().info("Implementing async service interface methods");

		final Type asyncServiceInterface = this.getAsyncServiceInterface(serviceInterface);

		final AllMethodsVisitor publicMethodFinder = new AllMethodsVisitor() {
			protected boolean visit(final Method method) {
				RpcClientGenerator.this.implementPublicMethod(method, serviceInterface, asyncServiceInterface, client);
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		publicMethodFinder.start(serviceInterface);
	}

	abstract protected void implementPublicMethod(Method method, Type serviceInterface, Type asyncServiceInterface,
			NewConcreteType client);

	/**
	 * Attempts to find the corresponding method on the async interface.
	 * 
	 * @param method
	 *            A service interface method
	 * @param asyncServiceInterface
	 *            The async service interface
	 * @param client
	 *            The client being generated.
	 * @return The matching async service interface method.
	 */
	protected NewMethod createCorrespondingAsyncServiceInterfaceMethod(final Method method, final Type asyncServiceInterface,
			final NewConcreteType client) {
		Checker.notNull("parameter:method", method);
		Checker.notNull("parameter:asyncServiceInterface", asyncServiceInterface);
		Checker.notNull("parameter:remoteJsonClient", client);

		final GeneratorContext context = this.getGeneratorContext();

		final String methodName = method.getName();

		// build up a list of the parameter types for the async method...
		final List<Type> asyncMethodParameters = new ArrayList<Type>();
		final Iterator<MethodParameter> serviceMethodParameters = method.getParameters().iterator();
		while (serviceMethodParameters.hasNext()) {
			final MethodParameter methodParameter = serviceMethodParameters.next();
			asyncMethodParameters.add(methodParameter.getType());
		}
		asyncMethodParameters.add(this.getAsyncCallback());

		// make sure that a method with the same signature actually exists on
		// the async interface...
		Method asyncMethod = asyncServiceInterface.findMostDerivedMethod(methodName, asyncMethodParameters);
		if (null == asyncMethod) {
			this.throwMatchingAsyncInterfaceMethodNotFoundException(method);
		}
		if (false == asyncMethod.returnsVoid()) {
			this.throwIncompatibleMethodFound(asyncMethod);
		}
		context.debug("Found matching async interface method: " + asyncMethod);

		// create the method on client...
		final NewMethod newMethod = asyncMethod.copy(client);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);

		// rename all parameters to parameterN
		GeneratorHelper.renameParametersToParameterN(newMethod);

		// the last parameter must be called "callback"
		final List parameters = newMethod.getParameters();
		final NewMethodParameter callback = (NewMethodParameter) parameters.get(parameters.size() - 1);
		callback.setName(Constants.CALLBACK_PARAMETER);

		context.debug("Finishing renaming parameters, parameters: " + parameters);

		return newMethod;
	}

	protected void throwMatchingAsyncInterfaceMethodNotFoundException(final Method method) {
		this.throwException("Unable to find corresponding async service interface method " + method);
	}

	protected void throwIncompatibleMethodFound(final Method method) {
		this.throwException("The async service method " + method + " should return void and not " + method.getReturnType().getName());
	}

	public Type getAsyncCallback() {
		return this.getGeneratorContext().getType(Constants.ASYNC_CALLBACK);
	}

	/**
	 * Helper which returns the corresponding async service interface for the
	 * given service interface.
	 * 
	 * @param serviceInterface
	 * @return The corresponding Async service interface type
	 */
	protected Type getAsyncServiceInterface(final Type serviceInterface) {
		final String serviceInterfaceName = serviceInterface.getName() + Constants.ASYNC_INTERFACE_SUFFIX;
		return this.getGeneratorContext().getType(serviceInterfaceName);
	}

	protected Type getServiceDefTarget() {
		return this.getGeneratorContext().getType(Constants.SERVICE_DEF_TARGET);
	}

	abstract protected Type getRequiredInterface();

	abstract protected void throwException(final String message);
}
