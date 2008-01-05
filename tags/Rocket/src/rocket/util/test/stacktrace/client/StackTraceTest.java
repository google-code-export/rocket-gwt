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
package rocket.util.test.stacktrace.client;

import rocket.browser.client.Browser;
import rocket.util.client.StackTrace;
import rocket.util.client.Utilities;

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

public class StackTraceTest implements EntryPoint {

	final static String THROWS_EXCEPTION_URL = Browser.getContextPath() + "/throwsException";

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
				StackTraceTest.this.throwCatchAndPrintStackTrace();
			}
		});
		rootPanel.add(throwCatchAndPrintStackTrace);

		final Button testThrowableSerialization = new Button("Invoke server service which throws Exception");
		testThrowableSerialization.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				StackTraceTest.this.invokeServiceServiceWhichThrowsException();
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
		final StackTraceTestServiceAsync service = (StackTraceTestServiceAsync) GWT.create(StackTraceTestService.class);
		final ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(THROWS_EXCEPTION_URL);

		service.invoke(null, new AsyncCallback() {
			public void onSuccess(final Object result) {
				StackTraceTest.this.handleUnexpectedResult(result);
			}

			public void onFailure(final Throwable expected) {
				StackTraceTest.this.handleExpectedException(expected);
			}
		});
	}

	protected void handleUnexpectedResult(final Object result) {
		this.log("Service unexpectedly returned " + GWT.getTypeName(result));
	}

	protected void handleExpectedException(final Throwable throwable) {
		this.log("<b>Service threw </b>");

		final String stackTrace = StackTrace.asString(throwable);

		final String[] frames = Utilities.split(stackTrace, "\n", true);
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < frames.length; i++) {
			String frame = frames[i];

			final int classNameStart = frame.indexOf("rocket.util.test.stacktracehelper.server");
			if (classNameStart != -1) {
				frame = "<i>" + frame + "</i>";
			}
			buf.append(frame);
			buf.append("\n");
		}

		this.log(buf.toString());
		this.log("<b>---END OF STACKTRACE---</b>");
	}

	protected void throwCatchAndPrintStackTrace() {
		RuntimeException caught = null;
		try {
			this.log("<b>Expected stacktrace</b>");

			final String className = GWT.getTypeName(this);

			final String expectedStackTrace = "java.lang.RuntimeException:\n" + "\tat " + className + ".throwRuntimeException()\n"
					+ "\tat " + className + ".twoFramesAwayFromMethodWhichThrowsException()\n" + "\tat " + className
					+ ".oneFrameAwayFromMethodWhichThrowsException()\n" + "\tat " + className + ".throwCatchAndPrintStackTrace()\n"
					+ "\tmore...";
			this.log(expectedStackTrace);

			this.log("About to throw a RuntimeException.");

			this.twoFramesAwayFromMethodWhichThrowsException();
		} catch (final RuntimeException expected) {
			caught = expected;
			expected.printStackTrace();
		}
		final String stackTrace = StackTrace.asString(caught);
		this.log("<b>StackTrace.getStackTraceAsString()</b>");
		this.log(stackTrace);
		this.log("<b>---END OF STACKTRACE---</b>");
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
		final String string = String.valueOf(object).replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		DOM.setInnerHTML(log, DOM.getInnerHTML(log) + string + "<br>");
	}
}
