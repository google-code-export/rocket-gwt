/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.remoting.test.json.rpc.client;

import java.io.Serializable;

import rocket.browser.client.Browser;

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
public class JsonRpcGwtTestCase extends GWTTestCase {

	final int RPC_TIMEOUT = 60 * 1000;

	final String CLASS_WITH_STRING_FIELD_JSON_SERVICE_URL = "/ClassWithStringFieldJsonResponse";

	final String CLASS_WITH_3_STRINGS_FIELD_JSON_SERVICE_URL = "/ClassWith3StringFieldsJsonResponse";

	final String BROKEN_JSON_RESPONSE_URL = "/brokenJsonResponse";

	final String INTERNAL_SERVER_ERROR_URL = "/internalServerError";

	final String JSON_RPC_URL = "/jsonRpc";
	
	final String EXCEPTION = "rocket.remoting.rebind.rpc.json.JsonRpcClientGeneratorException";

	public String getModuleName() {
		return "rocket.remoting.test.json.rpc.JsonRpcGwtTestCase";
	}

	/**
	 * This test should do nothing because NotAnInterface does not extend
	 * JsonRpcService.
	 */
	public void testTypeIsNotAnInterface() {
		try {
			final Object proxy = GWT.create(NotAnInterface.class);
			fail("An Exception should have been thrown because NotAnInterface is not an interface, proxy: " + proxy);
		} catch (final Exception expected) {
		}
	}

	/**
	 * This test should result in an exception being thrown because the given
	 * service interface does not extend JsonRpcService.
	 */
	public void testInterfaceDoesntImplementRemoteJsonService() {
		try {
			final Object proxy = GWT.create(DoesntImplementRemoteJsonService.class);
			fail("An Exception should have been thrown because InterfaceThatDoesntImplementRemoteJsonService doesnt implement JsonRpcService, and not: "
					+ proxy);
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
			fail("A \"" + EXCEPTION + "\" should have been thrown because InterfaceWithMissingAsyncAsync doesnt exist.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.equals(EXCEPTION));
		}
	}

	static public interface InterfaceWithMissingAsync extends JsonRpcService {
		void method(int intValue);
	}

	/**
	 * This test uses two interfaces where the parameters from the service
	 * interface are not present in corresponding Async interface. AN exception
	 * should be thrown by the generator.
	 */
	public void testUnmatchedMethodParameters() {
		try {
			final Object proxy = GWT.create(InterfaceWithMethodWhereParametersDontMatchUp.class);
			fail("A \"" + EXCEPTION + "\" should have been thrown because the async is not compatible with the proxy interface.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.equals(EXCEPTION));
		}
	}

	static public interface InterfaceWithMethodWhereParametersDontMatchUp extends JsonRpcService {
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
			fail("A \"" + EXCEPTION + "\" should have been thrown because the async is not compatible with the service interface.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.equals(EXCEPTION));
		}
	}

	static public interface AsyncInterfaceDoesntReturnVoid extends JsonRpcService {
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
			final Object proxy = GWT.create(InterfaceWithUnmatchedMethod.class);
			fail("A \"" + EXCEPTION + "\" should have been thrown because the async is not compatible with the proxy interface.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.equals(EXCEPTION));
		}
	}

	static public interface InterfaceWithUnmatchedMethod extends JsonRpcService {
		void method();
	}

	static public interface InterfaceWithUnmatchedMethodAsync {
		void aDifferentMethod(final AsyncCallback callback);
	}

	/**
	 * This test attempts to create a proxy with valid interfaces but fails
	 * because the only method present does not have the required httpMethod
	 * annotation.
	 */
	public void testHttpMethodAnnotationsIsMissing() {
		try {
			final Object service = GWT.create(MissingHttpMethodAnnotations.class);
			fail("A \"" + EXCEPTION + "\" should have been thrown because the httpMethod annotation is missing.");
		} catch (final Exception expected) {
			final String causeType = GWT.getTypeName(expected.getCause());
			assertTrue(causeType, causeType.equals(EXCEPTION));
		}
	}

	static public interface MissingHttpMethodAnnotations extends JsonRpcService {
		SerializableType missingHttpRequestMethodAnnotations();
	}

	static public interface MissingHttpMethodAnnotationsAsync {
		void missingHttpRequestMethodAnnotations(AsyncCallback callback);
	}

	static class SerializableType implements Serializable {
	}

	public void testGetRequest() {
		final GetJsonServiceAsync service = (GetJsonServiceAsync) GWT.create(GetJsonService.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(Browser.getContextPath() + CLASS_WITH_STRING_FIELD_JSON_SERVICE_URL);

		final String value = "apple";

		service.makeGetRequest(value, new AsyncCallback() {
			public void onSuccess(final Object result) {
				JsonRpcGwtTestCase.assertNotNull("result", result);
				JsonRpcGwtTestCase.assertTrue("result: " + GWT.getTypeName(result), result instanceof ClassWithStringField);

				final ClassWithStringField instance = (ClassWithStringField) result;
				JsonRpcGwtTestCase.assertEquals(value, instance.field);

				JsonRpcGwtTestCase.this.finishTest();
			}

			public void onFailure(final Throwable cause) {
				cause.printStackTrace();
				fail("Service failed unexpectantly " + cause);
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	public void testPostRequest() {
		final PostJsonServiceAsync service = (PostJsonServiceAsync) GWT.create(PostJsonService.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(Browser.getContextPath() + CLASS_WITH_STRING_FIELD_JSON_SERVICE_URL);

		final String value = "apple";

		service.makePostRequest(value, new AsyncCallback() {
			public void onSuccess(final Object result) {
				JsonRpcGwtTestCase.assertNotNull("result", result);
				JsonRpcGwtTestCase.assertTrue("result: " + GWT.getTypeName(result), result instanceof ClassWithStringField);

				final ClassWithStringField instance = (ClassWithStringField) result;
				JsonRpcGwtTestCase.assertEquals(value, instance.field);

				JsonRpcGwtTestCase.this.finishTest();
			}

			public void onFailure(final Throwable cause) {
				cause.printStackTrace();
				fail("Service failed unexpectantly " + cause);
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	public void testGetRequestWith3Parameters() {
		final GetJsonService3ParametersAsync service = (GetJsonService3ParametersAsync) GWT.create(GetJsonService3Parameters.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(Browser.getContextPath() + CLASS_WITH_3_STRINGS_FIELD_JSON_SERVICE_URL);

		final String value1 = "apple";
		final String value2 = "banana";
		final String value3 = "carrot";

		service.makeGetRequest(value1, value2, value3, new AsyncCallback() {
			public void onSuccess(final Object result) {
				JsonRpcGwtTestCase.assertNotNull("result", result);
				JsonRpcGwtTestCase.assertTrue("result: " + GWT.getTypeName(result), result instanceof ClassWith3StringFields);

				final ClassWith3StringFields instance = (ClassWith3StringFields) result;
				JsonRpcGwtTestCase.assertEquals(value1, instance.field1);
				JsonRpcGwtTestCase.assertEquals(value2, instance.field2);
				JsonRpcGwtTestCase.assertEquals(value3, instance.field3);

				JsonRpcGwtTestCase.this.finishTest();
			}

			public void onFailure(final Throwable cause) {
				cause.printStackTrace();
				fail("Service failed unexpectantly " + cause);
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	public void testDeserializingJsonEncodedResponseFails() {
		final GetJsonServiceAsync service = (GetJsonServiceAsync) GWT.create(GetJsonService.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(Browser.getContextPath() + BROKEN_JSON_RESPONSE_URL);

		final String value = "apple";

		service.makeGetRequest(value, new AsyncCallback() {
			public void onSuccess(final Object result) {
				JsonRpcGwtTestCase
						.fail("The onSuccess method should not have been called because deserializing of the json response should have failed.");
			}

			public void onFailure(final Throwable cause) {
				JsonRpcGwtTestCase.this.finishTest();
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	public void testServerRespondsWithInternalServerError() {
		final GetJsonServiceAsync service = (GetJsonServiceAsync) GWT.create(GetJsonService.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(Browser.getContextPath() + INTERNAL_SERVER_ERROR_URL);

		final String value = "apple";

		service.makeGetRequest(value, new AsyncCallback() {
			public void onSuccess(final Object result) {
				JsonRpcGwtTestCase
						.fail("The onSuccess method should not have been called because the server returned a non 200 code.");
			}

			public void onFailure(final Throwable cause) {
				JsonRpcGwtTestCase.this.finishTest();
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	static public interface GetJsonService extends JsonRpcService {
		/**
		 * 
		 * @param string
		 * @return
		 * 
		 * @jsonRpc-inputArguments requestParameters
		 * @jsonRpc-httpMethod GET
		 * @jsonRpc-parameterName string
		 */
		ClassWithStringField makeGetRequest(String string);
	}

	static public interface GetJsonServiceAsync {
		void makeGetRequest(String string, AsyncCallback callback);
	}

	static public interface PostJsonService extends JsonRpcService {
		/**
		 * 
		 * @param string
		 * @return
		 * 
		 * @jsonRpc-inputArguments requestParameters
		 * @jsonRpc-httpMethod POST
		 * @jsonRpc-parameterName string
		 */
		ClassWithStringField makePostRequest(String string);
	}

	static public interface PostJsonServiceAsync {
		void makePostRequest(String string, AsyncCallback callback);
	}

	static public interface GetJsonService3Parameters extends JsonRpcService {
		/**
		 * 
		 * @param string1
		 * @param string2
		 * @param string3
		 * @return
		 * 
		 * @jsonRpc-inputArguments requestParameters
		 * @jsonRpc-httpMethod GET
		 * @jsonRpc-parameterName string1
		 * @jsonRpc-parameterName string2
		 * @jsonRpc-parameterName string3
		 */
		ClassWith3StringFields makeGetRequest(String string1, String string2, String string3);
	}

	static public interface GetJsonService3ParametersAsync {
		void makeGetRequest(String string1, String string2, String string3, AsyncCallback callback);
	}

	public void testJsonRpc() {
		final JsonRpcServiceAsync service = (JsonRpcServiceAsync) GWT.create(JsonRpcService.class);
		final ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(Browser.getContextPath() + JSON_RPC_URL);

		final ClassWithStringField input = new ClassWithStringField();
		input.field = "apple";

		service.makeRequest(input, new AsyncCallback() {
			public void onSuccess(final Object result) {
				final ClassWithStringField output = (ClassWithStringField) result;
				assertEquals(input.field, output.field);
				JsonRpcGwtTestCase.this.finishTest();
			}

			public void onFailure(final Throwable cause) {
				cause.printStackTrace();
				JsonRpcGwtTestCase.this.fail("JsonRpc failed, message: " + cause.getMessage());
			}
		});

		this.delayTestFinish(RPC_TIMEOUT);
	}

	static interface JsonRpcService extends rocket.remoting.client.JsonRpcService {
		/**
		 * 
		 * @param input
		 * @return
		 * 
		 * @jsonRpc-inputArguments jsonRpc
		 */
		ClassWithStringField makeRequest(ClassWithStringField input);
	}

	static interface JsonRpcServiceAsync {
		void makeRequest(ClassWithStringField input, AsyncCallback callback);
	}
}
