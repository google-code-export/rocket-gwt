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
package rocket.remoting.test.java.rpcserviceclient.client;

import rocket.remoting.client.RpcException;
import rocket.remoting.client.support.rpc.JavaRpcServiceClient;
import rocket.remoting.client.support.rpc.JavaRpcServiceMethodInvoker;
import rocket.serialization.client.SerializationFactory;
import rocket.serialization.client.SerializationFactoryComposer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class JavaRpcServiceClientGwtTestCase extends GWTTestCase {

	static final int DELAY = 100 * 1000;

	static final String SERVICE_ENTRY_POINT = "./service";

	public String getModuleName() {
		return "rocket.remoting.test.java.rpcserviceclient.JavaRpcServiceClient";
	}

	public void testRpcCompletedSuccessfully() {
		final ServiceAsync client = this.createClient();
		((ServiceDefTarget) client).setServiceEntryPoint(SERVICE_ENTRY_POINT);

		final Payload payload = new Payload();
		payload.value = 123;

		client.echo(payload, new AsyncCallback<Payload>() {
			public void onSuccess(final Payload result) {
				assertTrue("" + result, result instanceof Payload);

				final Payload payloadResult = result;
				assertEquals("" + result, payload, payloadResult);
				finishTest();
			}

			public void onFailure(final Throwable cause) {
				cause.printStackTrace();
				fail(cause.getMessage());
			}
		});

		this.delayTestFinish(DELAY);
	}

	public void testInvalidServiceEntryPointSet() {
		final ServiceAsync client = this.createClient();
		((ServiceDefTarget) client).setServiceEntryPoint("./invalid");

		final Payload payload = new Payload();
		payload.value = 123;

		client.echo(payload, new AsyncCallback<Object>() {
			public void onSuccess(final Object result) {
				fail("This rpc should have failed because the service entry point is invalid, and not have returned: " + result);
			}

			public void onFailure(final Throwable cause) {
				finishTest();
			}
		});

		this.delayTestFinish(DELAY);
	}

	public void testServerThrowsDeclaredException() {
		final ServiceAsync client = this.createClient();
		((ServiceDefTarget) client).setServiceEntryPoint(SERVICE_ENTRY_POINT);

		client.throwsDeclaredException(new AsyncCallback<Object>() {
			public void onSuccess(final Object result) {
				fail("This rpc should have failed because the server service method threw an declared exception.");
			}

			public void onFailure(final Throwable cause) {
				assertTrue("" + cause, cause instanceof DeclaredException);
				finishTest();
			}
		});

		this.delayTestFinish(DELAY);
	}

	public void testServerThrowsUndeclaredException() {
		final ServiceAsync client = this.createClient();
		((ServiceDefTarget) client).setServiceEntryPoint(SERVICE_ENTRY_POINT);

		client.throwsUndeclaredException(new AsyncCallback<Object>() {
			public void onSuccess(final Object result) {
				fail("This rpc should have failed because the server service method threw an undeclared exception.");
			}

			public void onFailure(final Throwable cause) {
				assertTrue("" + cause, cause instanceof RpcException);
				finishTest();
			}
		});

		this.delayTestFinish(DELAY);
	}

	public void testServerReturnsUnserializableType() {
		final ServiceAsync client = this.createClient();
		((ServiceDefTarget) client).setServiceEntryPoint(SERVICE_ENTRY_POINT);

		client.returnsUnserializableType(new AsyncCallback<Object>() {
			public void onSuccess(final Object result) {
				fail("This rpc should have failed because the server returned an unserializable type.");
			}

			public void onFailure(final Throwable cause) {
				finishTest();
			}
		});

		this.delayTestFinish(DELAY);
	}

	ServiceAsync createClient() {
		return new TestJavaRpcServiceClient();
	}

	static class TestJavaRpcServiceClient extends JavaRpcServiceClient implements ServiceAsync {

		public void echo(final Payload payload, final AsyncCallback callback) {
			final JavaRpcServiceMethodInvoker methodInvoker = new JavaRpcServiceMethodInvoker();
			methodInvoker.setSerializationFactory(this.createSerializationFactory());
			methodInvoker.setInterfaceName(this.getServiceInterfaceName());
			methodInvoker.setMethodName("echo");

			methodInvoker.addParameterType("rocket.remoting.test.java.rpcserviceclient.client.Payload");
			methodInvoker.addParameter(payload);

			methodInvoker.setCallback(callback);
			methodInvoker.setUrl(this.getServiceEntryPoint());
			methodInvoker.makeRequest();
		}

		public void throwsDeclaredException(AsyncCallback callback) {
			final JavaRpcServiceMethodInvoker methodInvoker = new JavaRpcServiceMethodInvoker();
			methodInvoker.setSerializationFactory(this.createSerializationFactory());
			methodInvoker.setInterfaceName(this.getServiceInterfaceName());
			methodInvoker.setMethodName("throwsDeclaredException");
			methodInvoker.setCallback(callback);
			methodInvoker.setUrl(this.getServiceEntryPoint());
			methodInvoker.makeRequest();

		}

		public void throwsUndeclaredException(AsyncCallback callback) {
			final JavaRpcServiceMethodInvoker methodInvoker = new JavaRpcServiceMethodInvoker();
			methodInvoker.setSerializationFactory(this.createSerializationFactory());
			methodInvoker.setInterfaceName(this.getServiceInterfaceName());
			methodInvoker.setMethodName("throwsUndeclaredException");
			methodInvoker.setCallback(callback);
			methodInvoker.setUrl(this.getServiceEntryPoint());
			methodInvoker.makeRequest();
		}

		public void returnsUnserializableType(AsyncCallback callback) {
			final JavaRpcServiceMethodInvoker methodInvoker = new JavaRpcServiceMethodInvoker();
			methodInvoker.setSerializationFactory(this.createSerializationFactory());
			methodInvoker.setInterfaceName(this.getServiceInterfaceName());
			methodInvoker.setMethodName("returnsUnserializableType");
			methodInvoker.setCallback(callback);
			methodInvoker.setUrl(this.getServiceEntryPoint());
			methodInvoker.makeRequest();
		}

		String getServiceInterfaceName() {
			return "rocket.remoting.test.java.rpcserviceclient.client.Service";
		}

		/**
		 * Use deferred binding to create a serialization factory.
		 * 
		 * @return The new SerializationFactory
		 */
		SerializationFactory createSerializationFactory() {
			return (SerializationFactory) GWT.create(TestSerializationFactoryComposer.class);
		}
	}

	static interface ServiceAsync {
		void echo(Payload payload, AsyncCallback callback);

		void throwsDeclaredException(AsyncCallback callback);

		void throwsUndeclaredException(AsyncCallback callback);

		void returnsUnserializableType(AsyncCallback callback);
	}

	/**
	 * @serialization-readableTypes rocket.remoting.test.java.rpcserviceclient.client.Payload
	 * @serialization-readableTypes rocket.remoting.test.java.rpcserviceclient.client.DeclaredException
	 * @serialization-readableTypes rocket.remoting.client.RpcException
	 * @serialization-writableTypes rocket.remoting.test.java.rpcserviceclient.client.Payload
	 */
	static interface TestSerializationFactoryComposer extends SerializationFactoryComposer {
	}
}
