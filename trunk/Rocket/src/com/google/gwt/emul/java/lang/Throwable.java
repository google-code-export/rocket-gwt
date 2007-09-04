/*
 * Copyright 2006 Google Inc.
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
package java.lang;

import rocket.util.client.ObjectHelper;
import rocket.util.client.ThrowableHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * This class has been modified as part of the Rocket-gwt framework to capturing
 * stacktraces from JavaScript
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Throwable {

	/**
	 * This field is no longer used as real stack traces are being built and
	 * returned in {@link #getStackTrace() }
	 * 
	 */
	// private static final StackTraceElement[] NO_STACK_TRACE = new
	// StackTraceElement[0];
	public Throwable() {
		super();

		this.saveCallStack();
	}

	public Throwable(String message) {
		super();
		fMessage = message;

		this.saveCallStack();
	}

	public Throwable(String message, Throwable cause) {
		super();

		fCause = cause;
		fMessage = message;

		this.saveCallStack();
	}

	public Throwable(Throwable cause) {
		super();

		fMessage = (cause == null) ? null : cause.toString();
		fCause = cause;

		this.saveCallStack();
	}

	/**
	 * Stack traces are not currently supported.
	 * 
	 * @return this
	 */
	public Throwable fillInStackTrace() {
		return this;
	}

	public Throwable getCause() {
		return fCause;
	}

	public String getLocalizedMessage() {
		return getMessage();
	}

	public String getMessage() {
		return fMessage;
	}

	/**
	 * Stack traces are not currently supported.
	 * 
	 * @return An array of StackTraceElements one for each level of the stack
	 */
	public StackTraceElement[] getStackTrace() {
		if (this.stackTrace == null) {
			final String[] functionNames = ThrowableHelper.getCallStackFunctionNames(this.getCallStack());
			this.stackTrace = ThrowableHelper.buildStackTraceElements(functionNames);
		}

		return this.stackTrace;
	}

	public Throwable initCause(Throwable cause) {
		if (fCause != null)
			throw new IllegalStateException("Can't overwrite cause");
		if (cause == this)
			throw new IllegalArgumentException("Self-causation not permitted");
		fCause = cause;
		return this;
	}

	public void printStackTrace() {
		StringBuffer msg = new StringBuffer();
		Throwable currentCause = this;
		while (currentCause != null) {
			String causeMessage = currentCause.getMessage();
			if (currentCause != this) {
				msg.append("Caused by: ");
			}
			msg.append(GWT.getTypeName(currentCause));
			msg.append(": ");
			msg.append(causeMessage == null ? "(No exception detail)" : causeMessage);
			msg.append("\n");
			currentCause = currentCause.getCause();
		}
		System.err.println(msg);
	}

	/**
	 * Stack traces are not currently supported.
	 */
	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}

	/**
	 * An array of populated StackTraceElements
	 */
	transient private StackTraceElement[] stackTrace;

	public String toString() {
		String className = GWT.getTypeName(this);
		String msg = getMessage();
		if (msg != null) {
			return className + ": " + msg;
		} else {
			return className;
		}
	}

	private Throwable fCause;

	private String fMessage;

	// ----------------------ROCKET-------------
	/**
	 * Saves the callStack at the time that this Exception was created.
	 */
	private void saveCallStack() {
		this.setCallStack(this.setCallStack0(ThrowableHelper.getCallStackFunctions()));
	}

	/**
	 * This method is used to drop the two topmost frames from the stacktrace.
	 * 
	 * @param array
	 * @return
	 */
	native private JavaScriptObject setCallStack0(final JavaScriptObject array)/*-{
	 array.shift(); // remove the StackTrace.getCallStackFunctions() frame
	 array.shift(); // remove the Throwable constructor frame
	 return array;
	 }-*/;

	/**
	 * This field holds the callStack object of the function that created this
	 * exception.
	 */
	transient private JavaScriptObject callStack;

	private JavaScriptObject getCallStack() {
		ObjectHelper.checkNotNull("field:callStack", callStack);
		return this.callStack;
	}

	private void setCallStack(final JavaScriptObject callStack) {
		ObjectHelper.checkNotNull("parameter:callStack", callStack);
		this.callStack = callStack;
	}
}
