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
package rocket.remoting.test.webrequest.client;

import java.util.List;

import rocket.dom.client.Dom;
import rocket.remoting.client.Headers;
import rocket.remoting.client.RequestParameters;
import rocket.remoting.client.WebRequest;
import rocket.remoting.client.WebRequestService;
import rocket.remoting.client.WebRequestServiceAsync;
import rocket.remoting.client.WebResponse;
import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestRunner;
import rocket.testing.client.WebPageTestRunner;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Unfortunately the bundled tomcat doesnt seem to support includes with custom
 * HttpServletRequest and HttpServletResponse which means the WebRequest always
 * fails.
 * 
 * The actual classes themselves do work in a real container.
 * 
 * <h6>Gotchas</h6>
 * This test fails as it appears the attempt to include another resource via the
 * RequestDispatcher always fails because because the service servlet is called
 * again rather than the actual target resource. This is evident as the
 * stacktrace reveals methods from the rpc service servlet.
 * 
 * The strange thing is any attempt to access the resource by changing to the
 * appropriate url in the hosted mode browser works!
 */
public class WebRequestTest extends WebPageTestRunner implements EntryPoint {

	static final String REQUEST_PARAMETER_ECHO_SERVLET_URL = "/requestParameterEchoServlet";

	static final String POST_DATA_ECHO_SERVLET_URL = "/postDataEchoServlet";

	static final String WEB_REQUEST_SERVICE_ENTRY_POINT_URL = "webRequestRpc";

	static final int POSTPONE_TEST = 5000;

	public void onModuleLoad() {
		final Button button = new Button("Run Tests");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				WebRequestTest.this.executeTests((TestBuilder) GWT.create(TestFinder.class));
			}
		});
		RootPanel.get().add(button);

		Dom.setFocus(button.getElement());
	}

	static interface TestFinder extends TestBuilder {
		/**
		 * @testing-testRunner rocket.remoting.test.webrequest.client.WebRequestTest
		 */
		abstract public List buildCandidates();
	}

	/**
	 * @testing-testMethodOrder 0
	 */
	public void testGetWithNoRequestParameters() {
		final WebRequest request = new WebRequest();
		request.setHeaders(new Headers());
		request.setMethod("GET");
		request.setUrl(REQUEST_PARAMETER_ECHO_SERVLET_URL);
		request.setParameters(new RequestParameters());

		TestRunner.postponeCurrentTest(POSTPONE_TEST);

		final WebRequestServiceAsync service = this.createService();
		service.doRequest(request, new AsyncCallback() {
			public void onFailure(final Throwable caught) {
				Test.fail("testGetWithRequestParameters", caught);
			}

			public void onSuccess(final Object result) {
				final WebResponse response = (WebResponse) result;
				final int code = response.getCode();
				Test.assertEquals("The response returned an incorrect code, response: " + response, 200, code);

				final String message = response.getMessage();
				Test.assertEquals("The response returned an incorrect message, code: " + code, "OK", message);

				final String actualBody = response.getBody();
				final String expectedBody = "";
				if (!expectedBody.equals(expectedBody)) {
					Test.fail("The response returned an incorrect content, expected[" + expectedBody + "], actual[" + actualBody + "]");
				}
				TestRunner.finishTest();
			}
		});
	}

	/**
	 * @testing-testMethodOrder 1
	 */
	public void testGetWithRequestParameters() {
		final WebRequest request = new WebRequest();
		request.setHeaders(new Headers());
		request.setMethod("GET");
		request.setUrl(REQUEST_PARAMETER_ECHO_SERVLET_URL);

		final RequestParameters parameters = new RequestParameters();
		final String NAME0 = "apple";
		final String VALUE0 = "green";
		parameters.add(NAME0, VALUE0);

		final String NAME1 = "banana";
		final String VALUE1 = "yellow";
		parameters.add(NAME1, VALUE1);

		request.setParameters(parameters);

		TestRunner.postponeCurrentTest(POSTPONE_TEST);

		final WebRequestServiceAsync service = this.createService();
		service.doRequest(request, new AsyncCallback() {
			public void onFailure(final Throwable caught) {
				Test.fail("testGetWithRequestParameters", caught);
			}

			public void onSuccess(final Object result) {
				final WebResponse response = (WebResponse) result;

				final int code = response.getCode();
				Test.assertEquals("The response returned an incorrect code, response: " + response, 200, code);

				final String message = response.getMessage();
				Test.assertEquals("The response returned an incorrect message, code: " + code, "OK", message);

				final String actualBody = response.getBody();
				final String expectedBody = NAME0 + '=' + VALUE0 + "&" + NAME1 + '=' + VALUE1;
				if (!expectedBody.equals(expectedBody)) {
					Test.fail("The response returned an incorrect content, expected[" + expectedBody + "], actual[" + actualBody + "]");
				}

				TestRunner.finishTest();
			}
		});
	}

	/**
	 * @testing-testMethodOrder 2
	 */
	public void testGetANonExistingUrlWhichShouldReturnA404() {
		final WebRequest request = new WebRequest();
		request.setHeaders(new Headers());
		request.setMethod("GET");
		request.setUrl("/InvokingThisUrlShouldResultInA404");
		request.setParameters(new RequestParameters());

		TestRunner.postponeCurrentTest(POSTPONE_TEST);

		final WebRequestServiceAsync service = this.createService();
		service.doRequest(request, new AsyncCallback() {
			public void onFailure(final Throwable caught) {
				Test.fail("testGetWithRequestParameters", caught);
			}

			public void onSuccess(final Object result) {
				final WebResponse response = (WebResponse) result;
				final int code = response.getCode();
				Test.assertEquals("The response returned an incorrect code, response: " + response, 404, code);

				TestRunner.finishTest();
			}
		});
	}

	/**
	 * @testing-testMethodOrder 3
	 */
	public void testPostWithNoRequestParameters() {
		final WebRequest request = new WebRequest();
		request.setHeaders(new Headers());
		request.setMethod("POST");
		request.setUrl(REQUEST_PARAMETER_ECHO_SERVLET_URL);
		request.setParameters(new RequestParameters());

		TestRunner.postponeCurrentTest(5000);

		final WebRequestServiceAsync service = this.createService();
		service.doRequest(request, new AsyncCallback() {
			public void onFailure(final Throwable caught) {
				Test.fail("testPostWithRequestParameters", caught);
			}

			public void onSuccess(final Object result) {
				final WebResponse response = (WebResponse) result;
				final int code = response.getCode();
				Test.assertEquals("The response returned an incorrect code, response: " + response, 200, code);

				final String message = response.getMessage();
				Test.assertEquals("The response returned an incorrect message, code: " + code, "OK", message);

				final String actualBody = response.getBody();
				final String expectedBody = "";
				if (!expectedBody.equals(expectedBody)) {
					Test.fail("The response returned an incorrect content, expected[" + expectedBody + "], actual[" + actualBody + "]");
				}
				TestRunner.finishTest();
			}
		});
	}

	/**
	 * @testing-testMethodOrder 4
	 */
	public void testPostWithRequestParameters() {
		final WebRequest request = new WebRequest();
		request.setHeaders(new Headers());
		request.setMethod("POST");
		request.setUrl(REQUEST_PARAMETER_ECHO_SERVLET_URL);

		final RequestParameters parameters = new RequestParameters();
		final String NAME = "apple";
		final String VALUE = "green";
		parameters.add(NAME, VALUE);

		request.setParameters(parameters);

		final WebRequestServiceAsync service = this.createService();

		TestRunner.postponeCurrentTest(POSTPONE_TEST);

		service.doRequest(request, new AsyncCallback() {
			public void onFailure(final Throwable caught) {
				Test.fail("testPostWithRequestParameters", caught);
			}

			public void onSuccess(final Object result) {
				final WebResponse response = (WebResponse) result;

				final int code = response.getCode();
				Test.assertEquals("The response returned an incorrect code, response: " + response, 200, code);

				final String message = response.getMessage();
				Test.assertEquals("The response returned an incorrect message, code: " + code, "OK", message);

				final String actualBody = response.getBody();
				final String expectedBody = NAME + '=' + VALUE;
				if (!expectedBody.equals(expectedBody)) {
					Test.fail("The response returned an incorrect content, expected[" + expectedBody + "], actual[" + actualBody + "]");
				}
				TestRunner.finishTest();
			}
		});
	}

	protected WebRequestServiceAsync createService() {
		final WebRequestServiceAsync service = (WebRequestServiceAsync) GWT.create(WebRequestService.class);
		final ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(WEB_REQUEST_SERVICE_ENTRY_POINT_URL);
		return service;
	}
}
