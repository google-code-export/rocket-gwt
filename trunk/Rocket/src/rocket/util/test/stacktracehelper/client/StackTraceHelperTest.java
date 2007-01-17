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
package rocket.util.test.stacktracehelper.client;

import rocket.util.client.StackTraceHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class StackTraceHelperTest implements EntryPoint {

    final static String SERVICE_WHICH_THROWS_EXCEPTION_URL = "http://localhost:8888/rocket.util.test.stacktracehelper.StackTraceHelper/serviceWhichThrowsAnException";

    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                System.err.println("caught:" + caught.getMessage());
                caught.printStackTrace();
            }
        });

        final RootPanel rootPanel = RootPanel.get();

        final Button throwCatchAndPrintStackTrace = new Button("Throw,Catch and PrintStackTrace");
        throwCatchAndPrintStackTrace.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StackTraceHelperTest.this.throwCatchAndPrintStackTrace();
            }
        });
        rootPanel.add(throwCatchAndPrintStackTrace);

        final Button testThrowableSerialization = new Button("Invoke server service which throws Exception");
        testThrowableSerialization.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                StackTraceHelperTest.this.invokeServiceServiceWhichThrowsException();
            }
        });
        rootPanel.add(testThrowableSerialization);

        final Button clearLog = new Button("ClearLog");
        clearLog.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                DOM.setInnerHTML(DOM.getElementById("log"), "");
            }
        });
        rootPanel.add(clearLog);
    }

    protected void invokeServiceServiceWhichThrowsException() {
        final StackTraceHelperTestServiceAsync service = (StackTraceHelperTestServiceAsync) GWT
                .create(StackTraceHelperTestService.class);
        final ServiceDefTarget endpoint = (ServiceDefTarget) service;
        endpoint.setServiceEntryPoint(SERVICE_WHICH_THROWS_EXCEPTION_URL);

        service.invoke(null, new AsyncCallback() {
            public void onSuccess(final Object result) {
                StackTraceHelperTest.this.handleUnexpectedResult(result);
            }

            public void onFailure(final Throwable expected) {
                StackTraceHelperTest.this.handleExpectedException(expected);
            }
        });
    }

    protected void handleUnexpectedResult(final Object result) {
        this.log("Service unexpectedly returned " + GWT.getTypeName(result));
    }

    protected void handleExpectedException(final Throwable throwable) {
        this.log("Service returned " + GWT.getTypeName(throwable));

        this.log("<b>StackTraceHelper.getStackTraceAsString()</b>");
        final String className = "rocket.util.test.stacktracehelper.server.StackTraceHelperServiceImplServlet";

        final String expectedStackTrace = "java.lang.Exception:\n" + "\tat " + className + ".throwException()\n"
                + "\tat " + className + ".twoFramesAwayFromMethodWhichThrowsException()\n" + "\tat " + className
                + ".oneFrameAwayFromMethodWhichThrowsException()\n" + "\tmore...";
        this.log(expectedStackTrace);

        final String stackTrace = StackTraceHelper.getStackTraceAsString(throwable);
        this.log(stackTrace);
        this.log("---END OF STACKTRACE---");
    }

    protected void throwCatchAndPrintStackTrace() {
        RuntimeException caught = null;
        try {
            this.log("<b>Expected stacktrace</b>");

            final String className = GWT.getTypeName(this);

            final String expectedStackTrace = "java.lang.RuntimeException:\n" + "\tat " + className
                    + ".throwRuntimeException()\n" + "\tat " + className
                    + ".twoFramesAwayFromMethodWhichThrowsException()\n" + "\tat " + className
                    + ".oneFrameAwayFromMethodWhichThrowsException()\n" + "\tat " + className
                    + ".throwCatchAndPrintStackTrace()\n" + "\tmore...";
            this.log(expectedStackTrace);

            this.log("About to throw a RuntimeException.");

            this.twoFramesAwayFromMethodWhichThrowsException();
        } catch (final RuntimeException expected) {
            caught = expected;
            expected.printStackTrace();
        }
        final String stackTrace = StackTraceHelper.getStackTraceAsString(caught);
        this.log("<b>StackTraceHelper.getStackTraceAsString()</b>");
        this.log(stackTrace);
        this.log("---END OF STACKTRACE---");
    }

    protected void twoFramesAwayFromMethodWhichThrowsException() {
        this.oneFrameAwayFromMethodWhichThrowsException();
    }

    protected void oneFrameAwayFromMethodWhichThrowsException() {
        this.throwRuntimeException();
    }

    protected void throwRuntimeException() {
        throw new RuntimeException();
    }

    protected void log(final Object object) {
        final Element log = DOM.getElementById("log");
        final String string = String.valueOf(object).replaceAll("\n", "<br>").replaceAll("\t",
                "&nbsp;&nbsp;&nbsp;&nbsp;");
        DOM.setInnerHTML(log, DOM.getInnerHTML(log) + string + "<br>");
    }
}
