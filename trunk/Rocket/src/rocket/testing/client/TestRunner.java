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

import java.util.Iterator;
import java.util.List;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * A TestRunner is the main class which coordinates the execution of tests. It
 * is also responsible for updating the ui or producing a report to detail the
 * results of executing some tests.
 * 
 * <h6>Advantages compared with GwtTestCase</h6>
 * <ul>
 * <li>Tests are not limited to purely the hosted mode browser of your
 * development platform.</li>
 * <li>Tests are run are actually a regular module and thus a browser is always
 * visible</li>
 * <li>One can take advantage of other tools to inspect the dom of the browser</li>
 * <li>The test can prompt the user to confirm if something looks right</li>
 * </ul>
 * 
 * <h6>Disadvantages compared with GwtTestCase</h6>
 * <li> Because reflection is not available the user must build a list of
 * available test methods. This is the reason for the
 * {@link TestBuilder#buildCandidates()} class and method. </li>
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class TestRunner {

	/**
	 * The TestRunner instance.
	 */
	private static TestRunner testRunner;

	static TestRunner getTestRunner() {
		return testRunner;
	}

	static void setTestRunner(final TestRunner testRunner) {
		TestRunner.testRunner = testRunner;
	}

	public static void skipRemainingTests() {
		TestRunner.getTestRunner().setSkipRemaining(true);
	}

	public static void postponeCurrentTest(final int delay) {
		TestRunner.getTestRunner().getCurrentTest().postponeFinish(delay);
	}

	public static void finishTest() {
		final Test test = TestRunner.getTestRunner().getCurrentTest();
		if (null != test) {
			test.finish();
		}
	}

	public static void log(final String message) {
		final Test test = TestRunner.getTestRunner().getCurrentTest();
		if (null != test) {
			test.log(message);
		}
	}

	protected TestRunner() {
		super();

		TestRunner.setTestRunner(this);
		registerUncaughtExceptionHandler();
	}

	/**
	 * Executes all the tests returned by the given TestBuilder.
	 * 
	 * It is possible to automatically execute all public methods whose name
	 * starts with test, manually or via deferred binding.
	 * 
	 * <pre>
	 * TestBuilder tests = (TestBuilder) GWT.create( TestFinder.class );
	 * ...
	 * static interface TestFinder extends TestBuilder{
	 *      /**
	 *       * @test test class name.
	 *       * /
	 * 		abstract public List buildCandidates();
	 * }
	 * </pre>
	 * 
	 * Each test method must also include an order annotation with a number
	 * value. Methods with are sorted small to large numbers.
	 * 
	 * @param testBuilder
	 */
	public void executeTests(final TestBuilder testBuilder) {
		ObjectHelper.checkNotNull("parameter:testBuilder", testBuilder);

		final List tests = testBuilder.buildCandidates();
		if (tests.size() == 0) {
			this.handleNoTestsToExecute();
		}

		this.setSkipRemaining(false);
		this.setTests(tests);

		this.executeNextTest();
	}

	/**
	 * A list returned by the test builder containing all the lists that will be
	 * executed.
	 */
	private List tests;

	private List getTests() {
		return this.tests;
	}

	private void setTests(final List tests) {
		this.tests = tests;
		this.setTestIndex(0);
	}

	private int testIndex;

	private int getTestIndex() {
		return this.testIndex;
	}

	private void setTestIndex(final int testCursor) {
		this.testIndex = testCursor;
	}

	protected void executeNextTest() {
		boolean completed = true;

		if (false == this.isSkipRemaining()) {
			final int testIndex = this.getTestIndex();
			final List tests = this.getTests();

			completed = testIndex == tests.size();
			if (false == completed) {
				this.execute((Test) tests.get(testIndex));
			}
		}

		if (completed) {
			this.fireTestsCompleted();
		}
	}

	/**
	 * Prepares to execute a single test.
	 * 
	 * @param test
	 */
	protected void execute(final Test test) {
		ObjectHelper.checkNotNull("parameter:test", test);

		final Timer timer = new Timer() {
			public void run() {
				TestRunner.this.execute0(test);
			}
		};
		timer.schedule(100);
	}

	protected void execute0(final Test test) {
		ObjectHelper.checkNotNull("parameter:test", test);

		test.prepare();
		try {
			test.execute();
			if (test.hasPostponedFinishDelay()) {
				this.waitForPendingTest();
			} else {
				test.finish();
			}

		} catch (final Throwable caught) {
			caught.printStackTrace();
			test.finish(caught);
		}
	}

	/**
	 * Sub-classes can override this method to be notified of a new test
	 * starting.
	 * 
	 * @param test
	 */
	abstract protected void onTestStarted(final Test test);

	protected void fireTestPassed(final Test test) {
		this.setTestIndex(this.getTestIndex() + 1);
		this.onTestPassed(test);
		this.executeNextTest();
	}

	/**
	 * Sub-classes can override this method to be notified of a test finishing
	 * successfully
	 * 
	 * @param test
	 */
	abstract protected void onTestPassed(final Test test);

	protected void fireTestFailed(final Test test) {
		this.setTestIndex(this.getTestIndex() + 1);
		this.onTestFailed(test);
		this.executeNextTest();
	}

	/**
	 * Sub-classes can override this method to be notified of a test failed
	 * 
	 * @param test
	 */
	abstract protected void onTestFailed(final Test test);

	protected void fireTestAborted(final Test test) {
		this.setTestIndex(this.getTestIndex() + 1);
		this.onTestAborted(test);
		this.executeNextTest();
	}

	/**
	 * Sub-classes can override this method to be notified of a test aborted
	 * 
	 * @param test
	 */
	abstract protected void onTestAborted(final Test test);

	protected void fireTestsCompleted() {
		int started = 0;
		int passed = 0;
		int failed = 0;
		int aborted = 0;

		final Iterator tests = this.getTests().iterator();
		while (tests.hasNext()) {
			final Test test = (Test) tests.next();
			started++;
			if (test.hasPassed()) {
				passed++;
				continue;
			}
			if (test.hasFailed()) {
				failed++;
				continue;
			}
			if (test.hasAborted()) {
				aborted++;
				continue;
			}
		}

		this.onCompletion(started, passed, failed, aborted);
	}

	/**
	 * Sub-classes can override this method to be notified of all tests
	 * completing.
	 * 
	 * @param test
	 */
	abstract protected void onCompletion(int started, int passed, int failed, int aborted);

	/**
	 * Sub-classes may override this method to do something different when a
	 * testSuite returns no tests.
	 */
	protected void handleNoTestsToExecute() {
		Window.alert("No tests found!");
	}

	protected void registerUncaughtExceptionHandler() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				TestRunner.this.handleUncaughtException(caught);
			}
		});
	}

	/**
	 * If a test is still running mark it as having failed and finish it.
	 * 
	 * @param caught
	 */
	protected void handleUncaughtException(final Throwable caught) {
		final Test test = (Test) this.getCurrentTest();
		if (null != test) {
			test.finish(caught);
		}
	}

	Test getCurrentTest() {
		Test test = null;
		while (true) {
			final List tests = this.getTests();
			final int cursor = this.getTestIndex();
			if (cursor >= tests.size()) {
				break;
			}
			test = (Test) tests.get(cursor);
			if (test.hasCompleted()) {
				test = null;
			}
			break;
		}
		return test;
	}

	public String getCurrentTestName() {
		return this.getCurrentTest().getName();
	}

	/**
	 * When this flag is true and a test suite is being executed all remaining
	 * tests will be skipped and the results tabulated.
	 */
	private boolean skipRemaining;

	private void setSkipRemaining(final boolean skipRemaining) {
		this.skipRemaining = skipRemaining;
	}

	private boolean isSkipRemaining() {
		return this.skipRemaining;
	}

	/**
	 * This method is called by the test dispatcher when it has been instructed
	 * to wait for a pending test.
	 * 
	 * A timer is executed which constantly checks every so often if the current
	 * test has finished. When the test has finish
	 */
	protected void waitForPendingTest() {
		final Test test = this.getCurrentTest();
		if (null != test) {
			final long waitUntil = System.currentTimeMillis() + test.getPostponedFinishDelay();

			final Timer timer = new Timer() {
				public void run() {
					while (true) {
						if (test.hasCompleted()) {
							this.cancel();
							break;
						}
						final long now = System.currentTimeMillis();
						if (now > waitUntil) {
							this.cancel();
							TestRunner.this.handleTimeOut();
						}
						break;
					}
				}
			};
			timer.scheduleRepeating(50);
		}
	}

	protected void handleTimeOut() {
		final Test test = this.getCurrentTest();
		test.setThrowable(new AssertionError("Timeout"));
		test.finish();
	}

}
