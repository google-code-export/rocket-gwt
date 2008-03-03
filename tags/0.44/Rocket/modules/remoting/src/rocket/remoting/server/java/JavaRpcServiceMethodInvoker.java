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
package rocket.remoting.server.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rocket.remoting.client.RpcException;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.util.client.Checker;

/**
 * This class takes a stream and attempts to invoke a service method including
 * parameters upon a service implementation. The result (either the returned value or an exception) of invoking method is then serialized.
 * 
 * The located method and parameters are actually executed at {@link #invoke(Object, Method, Object[])
 * which provides an opportunity to change behaviour. The other {@link #invoke(String, Object)} method takes
 * care of marshalling/unmarshalling.
 *
 * Instances of this class are threadsafe and idepotent.
 * 
 * @author Miroslav Pokorny
 */
public class JavaRpcServiceMethodInvoker {
	/**
	 * Processes an incoming Http request which contains a rpc service that
	 * needs to be executed.
	 * 
	 * @param input The serialized form of the request.
	 * @param serviceProvider the instance that contains the service method which will be executed once parameters are unmarshalled.
	 * @return A string containing the serialized result of invoking the given method.	
	 */
	public String invoke(final String input, final Object serviceProvider ) {
		Checker.notNull( "parameter:serviceProvider", serviceProvider);
		
		ServerSerializationFactory serializationFactory = createSerializationFactory();

		final ObjectInputStream inputStream = serializationFactory.createObjectInputStream(input);
		final ObjectOutputStream outputStream = serializationFactory.createObjectOutputStream();

		Object result = null;
		boolean exceptionWasThrown = false;
		Method method = null;

		// read in the interface...
		final String interfaceName = (String) inputStream.readObject();
		final Class interfacee = this.getRequestedInterface(interfaceName);

		// verify the serviceProvider actually implements $interface
		this.checkServiceProvider(interfacee, serviceProvider);

		// the method name...
		final String methodName = (String) inputStream.readObject();

		// the parameter types...
		final int parameterCount = inputStream.readInt();
		final String[] parameterTypes = new String[parameterCount];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterTypes[i] = (String) inputStream.readObject();
		}

		// attempt to find a method on the given interface that matches the
		// method signature...
		method = this.getMethod(serviceProvider.getClass(), methodName, parameterTypes);

		// deserialize parameters...
		final Object[] parameters = new Object[parameterCount];
		for (int i = 0; i < parameterCount; i++) {
			parameters[i] = inputStream.readObject();
		}

		// any exceptions that are thrown will be serialized and included in the
		// response...
		try {
			// prepare to invoke method...
			result = this.invoke(serviceProvider, method, parameters);
		} catch (final InvocationTargetException invocationTargetException) {
			final Throwable caught = invocationTargetException.getTargetException();

			// if method throws exception leave...
			boolean thrown = false;
			final Class caughtType = caught.getClass();
			final Class[] thrownExceptions = method.getExceptionTypes();
			for (int i = 0; i < thrownExceptions.length; i++) {
				final Class thrownException = thrownExceptions[i];
				if (caughtType.equals(thrownException)) {
					thrown = true;
					result = caught;
					break;
				}
			}

			if (false == thrown) {
				result = new RpcException(caught.getMessage());
			}
			exceptionWasThrown = true;

		} catch (final RuntimeException runtimeException) {
			exceptionWasThrown = true;
			result = new RpcException(runtimeException.getMessage());
		} catch (final Throwable throwable) {
			exceptionWasThrown = true;
			result = new RpcException(throwable.getMessage());
		}

		// write the result...
		outputStream.writeBoolean(exceptionWasThrown);
		outputStream.writeObject(result);

		return outputStream.getText();
	}
	
	/**
	 * Invokes the located method belonging to the given provider.
	 * @param provider The service provider which contains the method about to be executed.
	 * @param method The method to invoke
	 * @param parameters An array holding parameters 
	 * @return The result returned by the method
	 * @throws Throwable The exception that was thrown.
	 */
	protected Object invoke( final Object serviceProvider, final Method method, final Object[] parameters ) throws Throwable{
		return method.invoke(serviceProvider, parameters);
	}

	/**
	 * Fetches the class object for the given requested interface name.
	 * 
	 * @param interfaceName
	 * @return
	 */
	protected Class getRequestedInterface(final String interfaceName) {
		final Class classs = this.getClass(interfaceName);
		if (false == classs.isInterface()) {
			this.throwNotAnInterface(classs);
		}
		return classs;
	}

	protected void throwNotAnInterface(final Class classs) {
		throw new RpcException("The requested interface is not an interface, class: " + classs);
	}

	/**
	 * Checks and complains if the service provider instance doesnt implement
	 * the request interface.
	 * 
	 * @param interfacee
	 * @param serviceProvider
	 */
	protected void checkServiceProvider(final Class interfacee, final Object serviceProvider) {
		final Class serviceProviderClass = serviceProvider.getClass();
		if (false == interfacee.isAssignableFrom(serviceProviderClass)) {
			this.throwInterfaceNotImplemented(interfacee, serviceProviderClass);
		}
	}

	protected void throwInterfaceNotImplemented(final Class interfacee, final Class serviceProvider) {
		throw new RpcException("The service provider " + serviceProvider + " does not implement the interface " + interfacee);
	}

	/**
	 * Fetches the method on the interface type that matches the given method
	 * signature.
	 * 
	 * @param interfacee
	 * @param methodName
	 * @param parameterTypeNames
	 * @return
	 * @throws RpcException
	 *             if a method matching the given signature cannot be located
	 */
	protected Method getMethod(final Class interfacee, final String methodName, final String[] parameterTypeNames) throws RpcException {
		try {
			final int parameterCount = parameterTypeNames.length;
			final Class[] parameterTypes = new Class[parameterCount];
			for (int i = 0; i < parameterCount; i++) {
				parameterTypes[i] = this.getClass(parameterTypeNames[i]);
			}
			return interfacee.getMethod(methodName, parameterTypes);
		} catch (final NoSuchMethodException noSuchMethodException) {
			throw new RpcException(noSuchMethodException);
		}
	}

	/**
	 * Helper which takes a type name and returns the corresponding Class
	 * object, converting any exceptions into RpcExceptions
	 * 
	 * @param typeName
	 * @return
	 */
	protected Class getClass(final String typeName) {
		try {
			return Class.forName(typeName);
		} catch (final ExceptionInInitializerError error) {
			throw new RpcException(error);
		} catch (final LinkageError error) {
			throw new RpcException(error);
		} catch (final ClassNotFoundException exception) {
			throw new RpcException(exception);
		}
	}

	/**
	 * Factory which creates a serialization factory which will be responsible
	 * for deserializing/serializing any rpc.
	 * 
	 * @return
	 */
	protected ServerSerializationFactory createSerializationFactory() {
		return new ServerSerializationFactory();
	}
}
