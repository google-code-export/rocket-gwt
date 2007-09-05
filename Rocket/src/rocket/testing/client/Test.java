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
package rocket.testing.client;

import java.util.ArrayList;
import java.util.List;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.GWT;

/**
 * A WebPageTestCase includes a list of TestMethods. Because GWT does not
 * support reflection tests must be assembled by a TestSuiteBuilder which in
 * turn returns a List of Test instances.
 * 
 * <h6>Features</h6>
 * <ul>
 * <li>A variety of static fail() and assertXXX() methods are available and can
 * be used just like Junits equivalent. </li>
 * <li> A test does not need to complete when the test method finishes. Its
 * completion may be delayed via {@link #postponeFinish(int)}. This is useful
 * if something in the near future needs to happen. That mehtod must either fail
 * as normal or call the {@link #finish()} to notify that it has finished. </li>
 * </ul>
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class Test {

	public static void fail() {
		throw new AssertionError();
	}

	public static void fail(final String message) {
		throw new AssertionError(message);
	}

	public static void fail(final String message, final Throwable cause) {
		throw new AssertionError(cause) {
			public String getMessage() {
				return message;
			}
		};
	}

	public static void assertTrue(final boolean actual) {
		if (false == actual) {
			Test.fail("assertTrue()\nexpected: " + true + "\nactual: " + actual);
		}
	}

	public static void assertTrue(final String message, final boolean actual) {
		if (false == actual) {
			Test.fail("assertTrue()\nmessage[" + message + "]\nexpected: " + true + "\n" + "actual: " + actual);
		}
	}

	public static void assertFalse(final boolean actual) {
		if (true == actual) {
			Test.fail("assertFalse\nexpected: " + false + "\nactual: " + actual);
		}
	}

	public static void assertFalse(final String message, final boolean actual) {
		if (true == actual) {
			Test.fail("assertFalse()\nmessage[" + message + "]\nexpected: " + false + "\n" + "actual: " + actual);
		}
	}

	public static void assertEquals(final boolean expected, final boolean actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertEquals(final String message, final boolean expected, final boolean actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertEquals(final byte expected, final byte actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertEquals(final String message, final byte expected, final byte actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertEquals(final char expected, final char actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertEquals(final String message, final char expected, final char actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertEquals(final double expected, final double actual, final double delta) {
		if (actual > (expected + delta) || actual < (expected - delta)) {
			Test.fail("assertEquals()\nexpected: " + expected + "\nactual: " + actual + "\ndelta: " + delta);
		}
	}

	public static void assertEquals(final String message, final double expected, final double actual, final double delta) {
		if (actual > (expected + delta) || actual < (expected - delta)) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\nactual: " + actual + "\ndelta: " + delta);
		}
	}

	public static void assertEquals(final float expected, final float actual, final float delta) {
		if (actual > (expected + delta) || actual < (expected - delta)) {
			Test.fail("assertEquals()\nexpected: " + expected + "\nactual: " + actual + "\ndelta: " + delta);
		}
	}

	public static void assertEquals(final String message, final float expected, final float actual, final float delta) {
		if (actual > (expected + delta) || actual < (expected - delta)) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\nactual: " + actual + "\ndelta: " + delta);
		}
	}

	public static void assertEquals(final int expected, final int actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertEquals(final String message, final int expected, final int actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertEquals(final long expected, final long actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertEquals(final String message, final long expected, final long actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertEquals(final Object expected, final Object actual) {
		if (false == expected.equals(actual)) {
			Test.fail("assertEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertEquals(final String message, final Object expected, final Object actual) {
		if (false == expected.equals(actual)) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertEquals(final short expected, final short actual) {
		if (expected != actual) {
			Test.fail("assertTrue()\nexpected: " + true + "\nactual: " + actual);
		}
	}

	public static void assertEquals(final String message, final short expected, final short actual) {
		if (expected != actual) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertEquals(final String expected, final String actual) {
		if (false == expected.equals(actual)) {
			Test.fail("assertEquals()\nexpected[" + expected + "]\nactual: " + actual);
		}
	}

	public static void assertEquals(final String message, final String expected, final String actual) {
		if (false == expected.equals(actual)) {
			Test.fail("assertEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNotEquals(final boolean expected, final boolean actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertNotEquals(final String message, final boolean expected, final boolean actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNotEquals(final byte expected, final byte actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertNotEquals(final String message, final byte expected, final byte actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNotEquals(final char expected, final char actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertNotEquals(final String message, final char expected, final char actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNotEquals(final double expected, final double actual, final double delta) {
		if (actual > (expected + delta) || actual < (expected - delta)) {
			Test.fail("assertNotEquals()\nexpected: " + expected + "\nactual: " + actual + "\ndelta: " + delta);
		}
	}

	public static void assertNotEquals(final String message, final double expected, final double actual, final double delta) {
		if (actual > (expected + delta) || actual < (expected - delta)) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\nactual: " + actual + "\ndelta: " + delta);
		}
	}

	public static void assertNotEquals(final float expected, final float actual, final float delta) {
		if (actual > (expected + delta) || actual < (expected - delta)) {
			Test.fail("assertNotEquals()\nexpected: " + expected + "\nactual: " + actual + "\ndelta: " + delta);
		}
	}

	public static void assertNotEquals(final String message, final float expected, final float actual, final float delta) {
		if (actual > (expected + delta) || actual < (expected - delta)) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\nactual: " + actual + "\ndelta: " + delta);
		}
	}

	public static void assertNotEquals(final int expected, final int actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertNotEquals(final String message, final int expected, final int actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNotEquals(final long expected, final long actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertNotEquals(final String message, final long expected, final long actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNotEquals(final Object expected, final Object actual) {
		if (false == expected.equals(actual)) {
			Test.fail("assertNotEquals()\nexpected: " + expected + "\nactual: " + actual);
		}
	}

	public static void assertNotEquals(final String message, final Object expected, final Object actual) {
		if (false == expected.equals(actual)) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNotEquals(final short expected, final short actual) {
		if (expected == actual) {
			Test.fail("assertTrue()\nexpected: " + true + "\nactual: " + actual);
		}
	}

	public static void assertNotEquals(final String message, final short expected, final short actual) {
		if (expected == actual) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNotEquals(final String expected, final String actual) {
		if (false == expected.equals(actual)) {
			Test.fail("assertNotEquals()\nexpected[" + expected + "]\nactual: " + actual);
		}
	}

	public static void assertNotEquals(final String message, final String expected, final String actual) {
		if (false == expected.equals(actual)) {
			Test.fail("assertNotEquals()\nmessage[" + message + "]\nexpected: " + expected + "\n" + "actual: " + actual);
		}
	}

	public static void assertNull(final Object object) {
		if (null != object) {
			Test.fail("assertNull\nobject: " + object);
		}
	}

	public static void assertNull(final String message, Object object) {
		if (null != object) {
			Test.fail("assertNull\nmessage[" + message + "]\nobject: " + object + "\n");
		}
	}

	public static void assertNotNull(final Object object) {
		if (null == object) {
			Test.fail("assertNotNull\nobject: " + object);
		}
	}

	public static void assertNotNull(final String message, Object object) {
		if (null == object) {
			Test.fail("assertNotNull\nmessage[" + message + "]\nobject: " + object);
		}
	}

	public static void assertSame(final Object object, final Object otherObject) {
		if (object != otherObject) {
			Test.fail("assertSame\nexpected: " + object + "\nactual: " + otherObject);
		}
	}

	public static void assertSame(final String message, final Object object, final Object otherObject) {
		if (object != otherObject) {
			Test.fail("assertSame\nmessage[" + message + "]\nexpected: " + object + "\n" + "actual: " + otherObject);
		}
	}

	public static void assertNotSame(final Object object, final Object otherObject) {
		if (object == otherObject) {
			Test.fail("assertNotSame\nexpected: " + object + "\nactual: " + otherObject);
		}
	}

	public static void assertNotSame(final String message, final Object object, final Object otherObject) {
		if (object == otherObject) {
			Test.fail("assertNotSame\nmessage[" + message + "]\nexpected: " + object + "\n" + "actual: " + otherObject);
		}
	}

	protected Test() {
		super();
		this.setState(READY);
		this.clearPostponedFinishDelay();
		this.setMessages(this.createMessages());
	}

	/**
	 * Prepares a test to be executed.
	 */
	public void prepare() {
		this.setState(STARTED);
		this.setStartTimestap(System.currentTimeMillis());
		TestRunner.getTestRunner().onTestStarted(this);
	}

	/**
	 * The test method. If it completes it will be marked as a success whilst
	 * failures are noted by invoking any of the assert* methods or throwing an
	 * exception.
	 * 
	 * @throws Exception
	 *             if anything goes wrong. The test will be marked as having
	 *             failed
	 */
	abstract protected void execute() throws Exception;

	/**
	 * This method may be invoked to mark a test as completed. This method is
	 * automatically called if the test completion was not postponed via
	 * {@link #postponeFinish(int)}.
	 */
	public void finish() {
		if (this.hasCompleted()) {
			GWT.log("Test has already completed", null);
		}
		this.setEndTimestap(System.currentTimeMillis());
		this.setState(PASSED);
		TestRunner.getTestRunner().fireTestPassed(this);
	}

	public void finish(final Throwable cause) {
		ObjectHelper.checkNotNull("parameter:cause", cause);
		this.setThrowable(cause);
		this.setEndTimestap(System.currentTimeMillis());
		this.setState(FAILED);
		TestRunner.getTestRunner().fireTestFailed(this);
	}

	/**
	 * Returns the name of the test. This will appear in the output of the
	 * TestRunner and is used to identify a test with its outcome.
	 * 
	 * @return
	 */
	abstract public String getName();

	final Object READY = "ready";

	final Object STARTED = "started";

	final Object PASSED = "passed";

	final Object FAILED = "failed";

	final Object ABORTED = "aborted";

	private Object state;

	private Object getState() {
		ObjectHelper.checkNotNull("field:state", state);
		return this.state;
	}

	private void setState(final Object state) {
		ObjectHelper.checkNotNull("parameter:state", state);
		this.state = state;
	}

	public boolean isReady() {
		return this.getState() == READY;
	}

	public boolean hasPassed() {
		return this.getState() == PASSED;
	}

	public boolean hasFailed() {
		return this.getState() == FAILED;
	}

	public boolean hasAborted() {
		return this.getState() == ABORTED;
	}

	public boolean hasCompleted() {
		return this.hasPassed() || this.hasFailed() || this.hasAborted();
	}

	/**
	 * If the test failed this field will contain the exception.
	 */
	Throwable throwable;

	Throwable getThrowable() {
		return throwable;
	}

	boolean hasThrowable() {
		return null != throwable;
	}

	void setThrowable(final Throwable throwable) {
		this.throwable = throwable;
	}

	/**
	 * The value in ticks when this particular test was started.
	 */
	long startTimestamp;

	long getStartTimestap() {
		return this.startTimestamp;
	}

	void setStartTimestap(final long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	/**
	 * The value in ticks when this particular test ended regularless of the
	 * outcome.
	 */
	long endTimestamp;

	long getEndTimestap() {
		return this.endTimestamp;
	}

	void setEndTimestap(final long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	/**
	 * This method may be used to postpone the end of the current test method
	 * being executed.
	 * 
	 * @param milliseconds
	 */
	protected void postponeFinish(final int milliseconds) {
		this.setPostponedFinishDelay(milliseconds);
	}

	/**
	 * A value in milliseconds for how long the current test has been postponed
	 * for. A zero value indicates that when the current test method returns the
	 * next test should be dispatched.
	 */
	private int postponedFinishDelay = 0;

	int getPostponedFinishDelay() {
		return this.postponedFinishDelay;
	}

	boolean hasPostponedFinishDelay() {
		return this.postponedFinishDelay > 0;
	}

	void setPostponedFinishDelay(final int postponedFinishDelay) {
		this.postponedFinishDelay = postponedFinishDelay;
	}

	void clearPostponedFinishDelay() {
		this.postponedFinishDelay = 0;
	}

	/**
	 * A list of messages accumulated whilst the test was run. Typically these
	 * will be shown if a test fails.
	 */
	private List messages;

	public List getMessages() {
		ObjectHelper.checkNotNull("field:messages", messages);
		return this.messages;
	}

	private void setMessages(final List messages) {
		ObjectHelper.checkNotNull("field:messages", messages);
		this.messages = messages;
	}

	private List createMessages() {
		return new ArrayList();
	}

	public void log(final String message) {
		this.getMessages().add(message);
	}

	public String toString() {
		return super.toString() + ", name: " + this.getName() + ", startTimestamp: " + this.startTimestamp + ", endTimestamp: "
				+ this.endTimestamp + ", state: " + state + ", postponedFinishDelay: " + this.postponedFinishDelay;
	}
}
