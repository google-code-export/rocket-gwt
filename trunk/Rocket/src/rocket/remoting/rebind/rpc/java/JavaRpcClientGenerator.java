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
package rocket.remoting.rebind.rpc.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedInterfaceType;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;
import rocket.remoting.rebind.rpc.RpcClientGenerator;
import rocket.remoting.rebind.rpc.java.servicemethodinvoker.ServiceMethodInvokerTemplatedFile;
import rocket.serialization.rebind.SerializationConstants;
import rocket.util.client.ObjectHelper;

/**
 * This generator generates a client proxy that may be used to invoke either a
 * json encoded rpc or a java rpc on the server.
 * 
 * @author Miroslav Pokorny
 */
public class JavaRpcClientGenerator extends RpcClientGenerator {

	protected NewConcreteType assembleNewType(final Type serviceInterface, final String newTypeName) {
		ObjectHelper.checkNotNull("parameter:serviceInterface", serviceInterface);
		GeneratorHelper.checkJavaTypeName("parameter:TypeName", newTypeName);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();

		this.verifyServiceInterface(serviceInterface);
		this.verifyAsyncServiceInterface(serviceInterface);

		final NewConcreteType client = this.createRpcServiceClient(newTypeName, serviceInterface, this.getJavaRpcServiceClient());
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
	 * @param asyncServiceInterface
	 *            The matching async service interface
	 * @param client
	 *            The client being assembled
	 */
	protected void implementPublicMethod(final Method method, final Type serviceInterface, final Type asyncServiceInterface,
			final NewConcreteType client) {
		ObjectHelper.checkNotNull("parameter:method", method);
		ObjectHelper.checkNotNull("parameter:serviceInterface", serviceInterface);
		ObjectHelper.checkNotNull("parameter:asyncServiceInterface", asyncServiceInterface);
		ObjectHelper.checkNotNull("parameter:client", client);

		final GeneratorContext context = this.getGeneratorContext();
		context.info("Implementing " + method);
		context.branch();

		final NewMethod newMethod = this.createCorrespondingAsyncServiceInterfaceMethod(method, asyncServiceInterface, client);

		final ServiceMethodInvokerTemplatedFile body = new ServiceMethodInvokerTemplatedFile();
		newMethod.setBody(body);
		body.setMethod(newMethod);
		body.setParameters(newMethod.getParameters());
		body.setServiceInterface(serviceInterface);

		final Type serializationFactoryComposer = this.createSerializationFactoryComposer(method, client, serviceInterface);
		body.setSerializationFactoryComposer(serializationFactoryComposer);

		context.debug("Completed.");
		context.unbranch();
	}

	/**
	 * This method creates a new nested type that will be fed and realised to be
	 * a real SerializationFactory.
	 * 
	 * @param method
	 *            The method being implemented
	 * @param client
	 *            The client type housing the method
	 * @return The new SerializationFactoryComposer that will be realised.
	 */
	protected Type createSerializationFactoryComposer(final Method method, final NewConcreteType client, final Type serviceInterface) {
		ObjectHelper.checkNotNull("parameter:method", method);
		ObjectHelper.checkNotNull("parameter:client", client);
		ObjectHelper.checkNotNull("parameter:serviceInterface", serviceInterface);

		final NewNestedInterfaceType serializationFactoryComposer = client.newNestedInterfaceType();

		final List parameterTypes = new ArrayList();
		final Iterator methodParameters = method.getParameters().iterator();
		while (methodParameters.hasNext()) {
			final MethodParameter parameter = (MethodParameter) methodParameters.next();
			parameterTypes.add(parameter.getType());
		}

		final Method serviceMethod = serviceInterface.findMethod(method.getName(), parameterTypes);
		final List serviceMethods = new ArrayList();
		serviceMethods.addAll(serviceInterface.getMethods());
		final int methodNumber = serviceMethods.indexOf(serviceMethod);
		final String newNestedName = Constants.NESTED_SERIALIZATION_FACTORY_COMPOSER + methodNumber;

		serializationFactoryComposer.setNestedName(newNestedName);
		serializationFactoryComposer.setSuperType(this.getSerializationFactoryComposer());
		serializationFactoryComposer.setStatic(false);
		serializationFactoryComposer.setVisibility(Visibility.PUBLIC);

		// build up a set containing of readableType which will contain all
		// throwable types and the method return type.
		final Set readableTypes = this.buildReadableTypes(method);
		readableTypes.add(this.getRpcException());
		this.addAnnotations(SerializationConstants.SERIALIZABLE_READABLE_TYPES, readableTypes, serializationFactoryComposer);

		final Set writableTypes = this.buildWritableTypes(method);
		this.addAnnotations(SerializationConstants.SERIALIZABLE_WRITABLE_TYPES, writableTypes, serializationFactoryComposer);

		return serializationFactoryComposer;
	}

	/**
	 * Builds a set of types that need to be readable, these are taken by
	 * combining the method return and thrown types.
	 * 
	 * @param method
	 *            The method to be processed
	 * @return The set
	 */
	protected Set buildReadableTypes(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final Set readableTypes = new HashSet();

		// add return type
		final Type returnType = method.getReturnType();
		final Type voidd = this.getGeneratorContext().getVoid();

		// dont add if its primitive or void...
		if (false == (returnType.equals(voidd) || returnType.isPrimitive())) {
			readableTypes.add(returnType);
		}

		// iterate over thrown types...
		final Iterator thrownTypes = method.getThrownTypes().iterator();
		while (thrownTypes.hasNext()) {
			final Type type = (Type) thrownTypes.next();
			readableTypes.add(type);
		}

		return readableTypes;
	}

	/**
	 * Builds a set of types that need to be writable, these are taken from the
	 * parameter types of this method. Primitive parameter types are not
	 * included in the set.
	 * 
	 * @param method
	 *            The method to be processed
	 * @return The set
	 */
	protected Set buildWritableTypes(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final Set writableTypes = new HashSet();

		final Iterator parameters = method.getParameters().iterator();
		while (parameters.hasNext()) {
			final MethodParameter parameter = (MethodParameter) parameters.next();
			final Type parameterType = parameter.getType();

			// skip primitive types...
			if (parameterType.isPrimitive()) {
				continue;
			}

			writableTypes.add(parameterType);
		}

		return writableTypes;
	}

	/**
	 * Loops thru and creates annotation entries for each and every type.
	 * 
	 * @param annotationName
	 *            The name of the annotation that will be created
	 * @param types
	 *            A set of types
	 * @param target
	 *            The type that will receive the annotations.
	 */
	protected void addAnnotations(final String annotationName, final Set types, final NewType target) {
		final Iterator iterator = types.iterator();
		while (iterator.hasNext()) {
			final Type type = (Type) iterator.next();
			this.addAnnotation(annotationName, type, target);
		}
	}

	protected void addAnnotation(final String annotationName, final Type type, final NewType target) {
		target.addMetaData(annotationName, type.getName());
	}

	protected String getGeneratedTypeNameSuffix() {
		return Constants.CLIENT_SUFFIX;
	}

	public Type getRemoteJavaService() {
		return this.getGeneratorContext().getType(Constants.REMOTE_JAVA_SERVICE);
	}

	public Type getJavaRpcServiceClient() {
		return this.getGeneratorContext().getType(Constants.JAVA_RPC_SERVICE_CLIENT);
	}

	protected Type getRequiredInterface() {
		return this.getRemoteJavaService();
	}

	protected Type getSerializationFactoryComposer() {
		return this.getGeneratorContext().getType(Constants.SERIALIZATION_FACTORY_COMPOSER);
	}

	protected Type getRpcException() {
		return this.getGeneratorContext().getType(Constants.RPC_EXCEPTION);
	}

	protected void throwException(final String message) {
		throw new JavaRpcClientGeneratorException(message);
	}
}
