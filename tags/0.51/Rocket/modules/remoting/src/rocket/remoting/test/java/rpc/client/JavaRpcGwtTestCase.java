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
package rocket.remoting.test.java.rpc.client;

import rocket.remoting.client.RpcException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class JavaRpcGwtTestCase extends GWTTestCase {

	static final int DELAY = 100 * 1000;

	static final String SERVICE_ENTRY_POINT = "./service";

	public String getModuleName() {
		return "rocket.remoting.test.java.rpc.JavaRpcGwtTestCase";
	}

	public void testRpcCompletedSuccessfully() {
		final ServiceAsync client = this.createClient();
		((ServiceDefTarget) client).setServiceEntryPoint(SERVICE_ENTRY_POINT);

		final Payload payload = new Payload();
		payload.value = 123;

		client.echo(payload, new AsyncCallback() {
			public void onSuccess(final Object result) {
				assertTrue("" + result, result instanceof Payload);

				final Payload payloadResult = (Payload) result;
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

		client.echo(payload, new AsyncCallback() {
			public void onSuccess(final Object result) {
				fail("This rpc should have failed because the service entry point is invalid.");
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

		client.throwsDeclaredException(new AsyncCallback() {
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

		client.throwsUndeclaredException(new AsyncCallback() {
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

	ServiceAsync createClient() {
		return (ServiceAsync) GWT.create(Service.class);
	}
}
