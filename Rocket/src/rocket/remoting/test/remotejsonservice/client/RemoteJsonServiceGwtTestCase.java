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
package rocket.remoting.test.remotejsonservice.client;

import java.io.Serializable;

import rocket.browser.client.BrowserHelper;
import rocket.remoting.client.json.RemoteJsonService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A series of unit tests that invokes a number of server assests (Servlets)
 * that return json encoded responses.
 * 
 * @author Miroslav Pokorny
 */
public class RemoteJsonServiceGwtTestCase extends GWTTestCase {

	final int RPC_TIMEOUT = 60 * 1000;

	final String CLASS_WITH_STRING_JSON_ENCODED_SERVICE_URL = "/ClassWithStringJsonEncodedBuilderServlet";

	final String BROKEN_JSON_RESPONSE_URL = "/BrokenJsonResponse";

	final String INTERNAL_SERVER_ERROR_URL = "/InternalServerError";

	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {
		return "rocket.remoting.test.remotejsonservice.RemoteJsonServiceGwtTestCase";
	}

	/**
	 * This test should do nothing because IsClassNotInterface is not a
	 * interface extending RemoteJsonService
	 */
	public void testTypeIsNotAnInterface() {
		try {
			final Object service = GWT.create(IsNotAnInterface.class);
			fail("An IncompatibleInterfacesException should have been thrown because IsNotAnInterface is not an interface.");
		} catch (final Exception expected) {
		}
	}

	/**
	 * This test should result in an exception being thrown because the given
	 * service interface does not extend RemoteJsonService.
	 */
	public void testInterfaceDoesntImplementRemoteJsonService() {
		try {
			final Object service = GWT.create(InterfaceThatDoesntImplementRemoteJsonService.class);
			fail("An IncompatibleInterfacesException should have been thrown because InterfaceThatDoesntImplementRemoteJsonService doesnt implement RemoteJsonService.");
		} catch (final Exception expected) {
		}
	}

	/**
	 * The attempt to create a proxy should fail because the matching Async
	 * interface does not exist.
	 */
	public void testASyncTypeCannotBeFound() {
		try {
			final Object service = GWT.create(InterfaceWithMissingAsync.class);
			fail("An AsyncInterfaceNotFoundException should have been thrown because InterfaceWithMissingAsyncAsync doesnt exist.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.endsWith("AsyncInterfaceNotFoundException"));
		}
	}

	static public interface InterfaceWithMissingAsync extends RemoteJsonService {
		void method(int intValue);
	}

	/**
	 * This test uses two interfaces where the parameters from the service
	 * interface are not present in corresponding Async interface. AN exception
	 * should be thrown by the generator.
	 */
	public void testUnmatchedMethodParameters() {
		try {
			final Object service = GWT.create(InterfaceWithMethodWhereParametersDontMatchUp.class);
			fail("An IncompatibleInterfacesException should have been thrown because the async is not compatible with the service interface.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.endsWith("IncompatibleInterfacesException"));
		}
	}

	static public interface InterfaceWithMethodWhereParametersDontMatchUp extends RemoteJsonService {
		void method(int missingFromAsync);
	}

	static public interface InterfaceWithMethodWhereParametersDontMatchUpAsync {
		void method(final AsyncCallback callback);
	}

	/**
	 * This test tests that the generator complains that the ASync interface is
	 * invalid because it doesnt return void.
	 */
	public void testAsyncMethodDoesntReturnVoid() {
		try {
			final Object service = GWT.create(AsyncInterfaceDoesntReturnVoid.class);
			fail("An IncompatibleInterfacesException should have been thrown because the async is not compatible with the service interface.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.endsWith("IncompatibleInterfacesException"));
		}
	}

	static public interface AsyncInterfaceDoesntReturnVoid extends RemoteJsonService {
		void method();
	}

	static public interface AsyncInterfaceDoesntReturnVoidAsync {
		Object method(final AsyncCallback callback);
	}

	/**
	 * This test attempts to create a proxy but fails because the two interfaces
	 * dont have matching methods.
	 */
	public void testInterfaceMethodsDontMatchUp() {
		try {
			final Object service = GWT.create(InterfaceWithUnmatchedMethod.class);
			fail("An IncompatibleInterfacesException should have been thrown because the async is not compatible with the service interface.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.endsWith("IncompatibleInterfacesException"));
		}
	}

	static public interface InterfaceWithUnmatchedMethod extends RemoteJsonService {
		void method();
	}

	static public interface InterfaceWithUnmatchedMethodAsync {
		void aDifferentMethod(final AsyncCallback callback);
	}

	/**
	 * This test attempst to create a proxy with valid interfaces but fails
	 * because the only method present does not have the required httpMethod
	 * annotation.
	 */
	public void testHttpMethodAnnotationsIsMissing() {
		try {
			final Object service = GWT.create(MissingHttpMethodAnnotations.class);
			fail("An MissingAnnotationException should have been thrown because the httpMethod annotation is missing.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.endsWith("MissingAnnotationException"));
		}
	}

	static public interface MissingHttpMethodAnnotations extends RemoteJsonService {
		SerializableType missingHttpRequestMethodAnnotations();
	}

	static public interface MissingHttpMethodAnnotationsAsync {
		void missingHttpRequestMethodAnnotations(AsyncCallback callback);
	}

	static class SerializableType implements Serializable {
	}

	public void testSuccessfulServerCall() {
		final ClassWithStringJsonEncodedServiceAsync service = (ClassWithStringJsonEncodedServiceAsync) GWT
				.create(ClassWithStringJsonEncodedService.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(BrowserHelper.getContextPath() + CLASS_WITH_STRING_JSON_ENCODED_SERVICE_URL);

		final String value = "apple";

		service.invokeServer(value, new AsyncCallback() {
			public void onSuccess(final Object result) {
				RemoteJsonServiceGwtTestCase.assertNotNull("result", result);
				RemoteJsonServiceGwtTestCase.assertTrue("result: " + GWT.getTypeName(result), result instanceof ClassWithString);

				final ClassWithString instance = (ClassWithString) result;
				RemoteJsonServiceGwtTestCase.assertEquals(value, instance.field);

				RemoteJsonServiceGwtTestCase.this.finishTest();
			}

			public void onFailure(final Throwable cause) {
				cause.printStackTrace();
				fail("Service failed unexpectantly " + cause);
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	public void testDeserializingServerJsonEncodedResponseFails() {
		final ClassWithStringJsonEncodedServiceAsync service = (ClassWithStringJsonEncodedServiceAsync) GWT
				.create(ClassWithStringJsonEncodedService.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(BrowserHelper.getContextPath() + BROKEN_JSON_RESPONSE_URL);

		final String value = "apple";

		service.invokeServer(value, new AsyncCallback() {
			public void onSuccess(final Object result) {
				RemoteJsonServiceGwtTestCase
						.fail("The onSuccess method should not have been called because deserializing of the json response should have failed.");
			}

			public void onFailure(final Throwable cause) {
				RemoteJsonServiceGwtTestCase.this.finishTest();
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	public void testServerRespondsWithInternalServerError() {
		final ClassWithStringJsonEncodedServiceAsync service = (ClassWithStringJsonEncodedServiceAsync) GWT
				.create(ClassWithStringJsonEncodedService.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(BrowserHelper.getContextPath() + INTERNAL_SERVER_ERROR_URL);

		final String value = "apple";

		service.invokeServer(value, new AsyncCallback() {
			public void onSuccess(final Object result) {
				RemoteJsonServiceGwtTestCase
						.fail("The onSuccess method should not have been called because the server returned a non 200 code.");
			}

			public void onFailure(final Throwable cause) {
				RemoteJsonServiceGwtTestCase.this.finishTest();
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	static public interface ClassWithStringJsonEncodedService extends RemoteJsonService {
		/**
		 * 
		 * @param stringField
		 * @return
		 * 
		 * @httpRequestMethod GET
		 * @httpRequestParameterName stringField
		 */
		ClassWithString invokeServer(String stringField);
	}

	static public interface ClassWithStringJsonEncodedServiceAsync {
		void invokeServer(String stringField, AsyncCallback callback);
	}
}
