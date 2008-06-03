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
package rocket.remoting.test.java.server;

import java.io.Serializable;

import junit.framework.TestCase;
import rocket.remoting.client.RpcException;
import rocket.remoting.server.java.JavaRpcServiceMethodInvoker;
import rocket.remoting.server.java.ServerSerializationFactory;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;

public class JavaRpcServiceMethodInvokerTestCase extends TestCase {

	public void testNotAnInterface() {
		final ObjectOutputStream outputStream = this.createObjectOutputStream();
		outputStream.writeObject(NotAnInterface.class.getName());
		final String input = outputStream.getText();

		final JavaRpcServiceMethodInvoker invoker = this.createRpcServiceMethodInvoker();
		try {
			invoker.invoke(input, new NotAnInterface());
			fail("An exception should have been thrown because the requested interface is not actually an interface.");
		} catch (final RpcException expected) {
		}
	}

	static class NotAnInterface {
	}

	public void testServiceProviderDoesntImplementInterface() {
		final ObjectOutputStream outputStream = this.createObjectOutputStream();
		outputStream.writeObject(ServiceInterface.class.getName());
		outputStream.writeObject("methodWhichIsNotImportant");
		outputStream.writeInt(0);

		final String input = outputStream.getText();
		final JavaRpcServiceMethodInvoker invoker = this.createRpcServiceMethodInvoker();
		try {
			invoker.invoke(input, new Object());
			fail("An exception should have been thrown the service provider doesnt implement the service interface.");
		} catch (final RpcException expected) {
		}
	}

	public void testRequestMethodDoesntExist() {
		final ObjectOutputStream outputStream = this.createObjectOutputStream();
		outputStream.writeObject(ServiceInterface.class.getName());
		outputStream.writeObject("methodThatDoesntExist");
		outputStream.writeInt(0);

		final String input = outputStream.getText();
		final JavaRpcServiceMethodInvoker invoker = this.createRpcServiceMethodInvoker();
		try {
			invoker.invoke(input, new ConcreteServiceInterface());
			fail("An exception should have been thrown because the stream interface entry is not an Interface.");
		} catch (final RpcException expected) {
		}
	}

	public void testInvokeMethod() {
		final ObjectOutputStream outputStream = this.createObjectOutputStream();
		outputStream.writeObject(ServiceInterface.class.getName());
		outputStream.writeObject("method");
		outputStream.writeInt(1);
		outputStream.writeObject("apple".getClass().getName());
		outputStream.writeObject("apple");

		final String input = outputStream.getText();
		final JavaRpcServiceMethodInvoker invoker = this.createRpcServiceMethodInvoker();
		final String response = invoker.invoke(input, new ConcreteServiceInterface());

		// deserialize the response and check its correct...
		final ObjectInputStream objectInputStream = this.createObjectInputStream(response);
		final boolean exception = objectInputStream.readBoolean();
		assertFalse(exception);

		final Object result = objectInputStream.readObject();
		assertEquals("apple", result);
	}

	public void testInvokedMethodWhichThrowsDeclaredException() {
		final ObjectOutputStream outputStream = this.createObjectOutputStream();
		outputStream.writeObject(ServiceInterface.class.getName());
		outputStream.writeObject("throwsDeclaredException");
		outputStream.writeInt(0);

		final String input = outputStream.getText();
		final JavaRpcServiceMethodInvoker invoker = this.createRpcServiceMethodInvoker();
		final String response = invoker.invoke(input, new ConcreteServiceInterface());

		// deserialize the response and check its correct...
		final ObjectInputStream objectInputStream = this.createObjectInputStream(response);
		final boolean exception = objectInputStream.readBoolean();
		assertTrue(exception);

		final Object result = objectInputStream.readObject();
		assertTrue("" + result, result instanceof DeclaredException);
	}

	public void testInvokedMethodWhichThrowsUndeclaredException() {
		final ObjectOutputStream outputStream = this.createObjectOutputStream();
		outputStream.writeObject(ServiceInterface.class.getName());
		outputStream.writeObject("throwsUndeclaredException");
		outputStream.writeInt(0);

		final String input = outputStream.getText();
		final JavaRpcServiceMethodInvoker invoker = this.createRpcServiceMethodInvoker();
		final String response = invoker.invoke(input, new ConcreteServiceInterface());

		// deserialize the response and check its correct...
		final ObjectInputStream objectInputStream = this.createObjectInputStream(response);
		final boolean exception = objectInputStream.readBoolean();
		assertTrue(exception);

		final Object result = objectInputStream.readObject();
		assertTrue("" + result, result instanceof RpcException);
	}

	static public interface ServiceInterface {
		String method(String string);

		void throwsDeclaredException() throws DeclaredException;

		void throwsUndeclaredException();
	}

	static public class ConcreteServiceInterface implements ServiceInterface {
		public String method(final String string) {
			assertEquals("apple", string);
			return string;
		}

		public void throwsDeclaredException() throws DeclaredException {
			throw new DeclaredException();
		}

		public void throwsUndeclaredException() {
			throw new UndeclaredException();
		}
	}

	static public class DeclaredException extends Exception implements Serializable {

	}

	static public class UndeclaredException extends RuntimeException {

	}

	ObjectInputStream createObjectInputStream(final String input) {
		return new ServerSerializationFactory().createObjectInputStream(input);
	}

	ObjectOutputStream createObjectOutputStream() {
		return new ServerSerializationFactory().createObjectOutputStream();
	}

	JavaRpcServiceMethodInvoker createRpcServiceMethodInvoker() {
		return new JavaRpcServiceMethodInvoker();
	}
}
