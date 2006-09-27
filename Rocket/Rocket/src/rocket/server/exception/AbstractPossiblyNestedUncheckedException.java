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
package rocket.server.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

/**
 * This class is identical to AbstractPossiblyNestedCheckedException except it
 * extends RuntimeException and not Exception.
 *
 * @author Miroslav Pokorny
 * @version 1.0
 */
public abstract class AbstractPossiblyNestedUncheckedException extends
		RuntimeException {
	/**
	 * Constructor which accepts a throwable object as an parameter.
	 *
	 * @param cause
	 *            The original exception which is wrapped within this exception.
	 */
	protected AbstractPossiblyNestedUncheckedException() {
		super();
	}

	/**
	 * Constructor which accepts a throwable object as an parameter.
	 *
	 * @param cause
	 *            The original exception which is wrapped within this exception.
	 */
	protected AbstractPossiblyNestedUncheckedException(final Throwable cause) {
		super();
		this.setCause(cause);
	}

	protected AbstractPossiblyNestedUncheckedException(final String message) {
		super();
		this.setMessage(message);
	}

	/**
	 * Constructor which accepts a throwable object and a message as an
	 * argument.
	 *
	 * @param message
	 *            The message associated with this exception.
	 * @param cause
	 *            The original exception which will be wrapped within this
	 *            exception.
	 */
	protected AbstractPossiblyNestedUncheckedException(final String message,
			final Throwable cause) {
		super();
		this.setMessage(message);
		this.setCause(cause);
	}

	/**
	 * The cause or exceptions being wrapped
	 */
	private Throwable cause;

	/**
	 * It is necessary to keep a copy of the message within this class because
	 * toString would be unable to include the message
	 */
	private String message;

	/**
	 * Combines the message belonging to this exception along with the cause of
	 * the exception
	 *
	 * @return the message.
	 */
	public String getMessage() {
		this.guardAgainstMandatoryCauseNotMissing();

		return ExceptionHelper.buildExceptionMessageWithPossibleNullCause(
				message, this.getCause());
	} // getMessage

	public boolean hasMessage() {
		return this.message != null;
	}

	public void setMessage(final String message) {
		StringHelper.checkNotEmpty("parameter:message", message);
		this.message = message;
	}

	public void printStackTrace(final PrintWriter printer) {
		ObjectHelper.checkNotNull("parameter:printer", printer);

		guardAgainstMandatoryCauseNotMissing();

		if (this.hasCause()) {
			printer.print(this.getCausePrintStackTraceHeader());
			this.getCause().printStackTrace(printer);
			printer.print(this.getCausePrintStackTraceFooter());
		}
		super.printStackTrace(printer);
	}

	public void printStackTrace(final PrintStream printer) {
		ObjectHelper.checkNotNull("parameter:printer", printer);

		guardAgainstMandatoryCauseNotMissing();

		if (this.hasCause()) {
			printer.print(this.getCausePrintStackTraceHeader());
			this.getCause().printStackTrace(printer);
			printer.print(this.getCausePrintStackTraceFooter());
		}
		super.printStackTrace(printer);
	}

	/**
	 * Get the wrapped cause if one was given
	 *
	 * @return cause The cause exception if one was given during construction or
	 *         possibly null
	 */
	public Throwable getCause() {
		this.guardAgainstMandatoryCauseNotMissing();
		return this.cause;
	} // getCause

	public boolean hasCause() {
		return this.cause != null && this.cause != this;
	}

	/**
	 * Set the cause associated with this exception.
	 *
	 * @param cause
	 *            The cause
	 */
	protected void setCause(final Throwable cause) {
		ObjectHelper.checkNotNull("parameter:cause", cause);
		this.cause = cause;
	} //setCause

	protected final void guardAgainstMandatoryCauseNotMissing() {
		if (this.mustHaveCause() && false == this.hasCause()) {
			SystemHelper.handleAssertFailure("cause",
					"The cause being wrapped is missing, this: " + this);
		}
	}

	protected abstract boolean mustHaveCause();

	protected final static String EOL = System.getProperty("line.separator");

	protected String getCausePrintStackTraceHeader() {
		return "---beginning of cause stacktrace---" + EOL;
	}

	protected String getCausePrintStackTraceFooter() {
		return "---end of cause stacktrace---" + EOL;
	}

	/**
	 * Returns the classname, hashcode and the string returned by the
	 * getMessage() method. have to append any extra fields that they add.
	 *
	 * @return The string representation of this object.
	 */
	public String toString() {
		return ObjectHelper.defaultToString(this) + ", message [" + message
				+ "]" + (this.hasCause() ? ", cause: " + this.getCause() : "");
	} //toString
}