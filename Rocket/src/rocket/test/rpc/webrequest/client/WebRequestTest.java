/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.test.rpc.webrequest.client;

import rocket.client.rpc.Headers;
import rocket.client.rpc.RequestParameters;
import rocket.client.rpc.WebRequest;
import rocket.client.rpc.WebRequestService;
import rocket.client.rpc.WebRequestServiceAsync;
import rocket.client.rpc.WebResponse;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Unfortunately the bundled tomcat doesnt seem to support includes with custom HttpServletRequest and HttpServletResponse which means the
 * WebRequest always fails.
 * 
 * The actual classes themselves do work in a real container.
 */
public class WebRequestTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                if (false == (caught instanceof TestAssertionFailedException)) {
                    addFailure("Uncaught exception", caught);
                }
            }
        });

        this.testGet0WithNoRequestParameters();
        this.testGet1WithRequestParameters();
        this.testGet2ANonExistingUrlWhichShouldReturnA404();
        this.testPost0WithNoRequestParameters();
        this.testPost1WithRequestParameters();
    }

    static final String REQUEST_PARAMETER_ECHO_SERVLET_URL = "/requestParameterEchoServlet";

    static final String POST_DATA_ECHO_SERVLET_URL = "/postDataEchoServlet";

    static final String WEB_REQUEST_SERVICE_ENTRY_POINT_URL = "/webRequestRpc";

    public void testGet0WithNoRequestParameters() {
        final WebRequest request = new WebRequest();
        request.setHeaders(new Headers());
        request.setMethod("GET");
        request.setUrl(REQUEST_PARAMETER_ECHO_SERVLET_URL);
        request.setParameters(new RequestParameters());

        final WebRequestServiceAsync service = this.createService();
        service.doRequest(request, new AsyncCallback() {
            public void onFailure(final Throwable caught) {
                addFailure("testGet0WithRequestParameters", caught);
            }

            public void onSuccess(final Object result) {
                final WebResponse response = (WebResponse) result;
                final int code = response.getCode();
                if (200 != code) {
                    addFailure("The response returned an incorrect code, code:" + code);
                }
                final String message = response.getMessage();
                if (!"OK".equals(message)) {
                    addFailure("The response returned an incorrect message, message[" + code + "]");
                }
                final String actualBody = response.getBody();
                final String expectedBody = "";
                if (!expectedBody.equals(expectedBody)) {
                    addFailure("The response returned an incorrect content, expected[" + expectedBody + "], actual["
                            + actualBody + "]");
                }
            }
        });
    }

    public void testGet1WithRequestParameters() {
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

        final WebRequestServiceAsync service = this.createService();
        service.doRequest(request, new AsyncCallback() {
            public void onFailure(final Throwable caught) {
                addFailure("testGet0WithRequestParameters", caught);
            }

            public void onSuccess(final Object result) {
                final WebResponse response = (WebResponse) result;
                final int code = response.getCode();
                if (200 != code) {
                    addFailure("The response returned an incorrect code, code:" + code);
                }
                final String message = response.getMessage();
                if (!"OK".equals(message)) {
                    addFailure("The response returned an incorrect message, message[" + code + "]");
                }
                final String actualBody = response.getBody();
                final String expectedBody = NAME0 + '=' + VALUE0 + "&" + NAME1 + '=' + VALUE1;
                if (!expectedBody.equals(expectedBody)) {
                    addFailure("The response returned an incorrect content, expected[" + expectedBody + "], actual["
                            + actualBody + "]");
                }
            }
        });
    }

    public void testGet2ANonExistingUrlWhichShouldReturnA404() {
        final WebRequest request = new WebRequest();
        request.setHeaders(new Headers());
        request.setMethod("GET");
        request.setUrl("/InvokingThisUrlShouldResultInA404");
        request.setParameters(new RequestParameters());

        final WebRequestServiceAsync service = this.createService();
        service.doRequest(request, new AsyncCallback() {
            public void onFailure(final Throwable caught) {
                addFailure("testGet0WithRequestParameters", caught);
            }

            public void onSuccess(final Object result) {
                final WebResponse response = (WebResponse) result;
                final int code = response.getCode();
                if (404 != code) {
                    addFailure("The response returned an incorrect code, code:" + code + ", response: " + response);
                }
            }
        });
    }

    public void testPost0WithNoRequestParameters() {
        final WebRequest request = new WebRequest();
        request.setHeaders(new Headers());
        request.setMethod("POST");
        request.setUrl(REQUEST_PARAMETER_ECHO_SERVLET_URL);
        request.setParameters(new RequestParameters());

        final WebRequestServiceAsync service = this.createService();
        service.doRequest(request, new AsyncCallback() {
            public void onFailure(final Throwable caught) {
                addFailure("testPost0WithRequestParameters", caught);
            }

            public void onSuccess(final Object result) {
                final WebResponse response = (WebResponse) result;
                final int code = response.getCode();
                if (200 != code) {
                    addFailure("The response returned an incorrect code, code:" + code);
                }
                final String message = response.getMessage();
                if (!"OK".equals(message)) {
                    addFailure("The response returned an incorrect message, message[" + code + "]");
                }
                final String actualBody = response.getBody();
                final String expectedBody = "";
                if (!expectedBody.equals(expectedBody)) {
                    addFailure("The response returned an incorrect content, expected[" + expectedBody + "], actual["
                            + actualBody + "]");
                }
            }
        });
    }

    public void testPost1WithRequestParameters() {
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
        service.doRequest(request, new AsyncCallback() {
            public void onFailure(final Throwable caught) {
                addFailure("testPost0WithRequestParameters", caught);
            }

            public void onSuccess(final Object result) {
                final WebResponse response = (WebResponse) result;
                final int code = response.getCode();
                if (200 != code) {
                    addFailure("The response returned an incorrect code, code:" + code);
                }
                final String message = response.getMessage();
                if (!"OK".equals(message)) {
                    addFailure("The response returned an incorrect message, message[" + code + "]");
                }
                final String actualBody = response.getBody();
                final String expectedBody = NAME + '=' + VALUE;
                if (!expectedBody.equals(expectedBody)) {
                    addFailure("The response returned an incorrect content, expected[" + expectedBody + "], actual["
                            + actualBody + "]");
                }
            }
        });
    }

    protected WebRequestServiceAsync createService() {
        final WebRequestServiceAsync service = (WebRequestServiceAsync) GWT.create(WebRequestService.class);
        final ServiceDefTarget endpoint = (ServiceDefTarget) service;
        endpoint.setServiceEntryPoint(WEB_REQUEST_SERVICE_ENTRY_POINT_URL);
        return service;
    }

    static void addPass(final String testName) {
        RootPanel.get().add(new HTML("PASS: " + testName + "<br/>"));
    }

    static void addFailure(final String testName) {
        RootPanel.get().add(new HTML("FAILED: " + testName + "<br/>"));
        throw new TestAssertionFailedException();
    }

    static void addFailure(final String testName, final Throwable cause) {
        cause.printStackTrace();
        RootPanel.get().add(new HTML("FAILED: " + testName + ", cause: " + cause + "<br/>"));
        throw new TestAssertionFailedException();
    }

    static class TestAssertionFailedException extends RuntimeException {
        TestAssertionFailedException() {
        }
    }

}
