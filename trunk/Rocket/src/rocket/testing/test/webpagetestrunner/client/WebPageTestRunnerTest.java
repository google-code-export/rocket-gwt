package rocket.testing.test.webpagetestrunner.client;

import java.util.List;

import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestRunner;
import rocket.testing.client.WebPageTestRunner;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test may be used to execute a set of tests returned by a TestBuilder.
 * The outcome of each test is written to a table.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WebPageTestRunnerTest extends WebPageTestRunner implements EntryPoint {

	public WebPageTestRunnerTest() {
		super();
	}

	public void onModuleLoad() {
		final Button button = new Button("Run Tests");
		button.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				WebPageTestRunnerTest.this.executeTests(WebPageTestRunnerTest.this.getTestBuilder());
			}
		});
		RootPanel.get().add(button);
	}

	protected TestBuilder getTestBuilder() {
		return (TestBuilder) GWT.create(TestMethodFinder.class);
	}

	static interface TestMethodFinder extends TestBuilder {
		/**
		 * @testing-testRunner rocket.testing.test.webpagetestrunner.client.WebPageTestRunnerTest
		 */
		abstract public List buildCandidates();
	}

	/**
	 * @testing-testMethodOrder 0
	 */
	public void testWhichPasses() {
		for (int i = 0; i < 10000; i++) {
			Math.cos(1.23f);
		}
	}

	/**
	 * @testing-testMethodOrder 1
	 */
	public void testWhichPassesAfterBeingBusyFor100ms() {
		final long started = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - started >= 100) {
				break;
			}
		}
	}

	/**
	 * @testing-testMethodOrder 2
	 */
	public void testWhichFailsAnAssertEquals() {
		Test.assertEquals(1, 2);
	}

	/**
	 * @testing-testMethodOrder 3
	 */
	public void testWhichFailsBecauseItThrowsARuntimeExceptionAfterLoggingAMessage() {
		log("About to throw a RuntimeException...");
		throw new RuntimeException("RuntimeException thrown.");
	}

	/**
	 * @testing-testMethodOrder 4
	 */
	public void testWhichFailsBecauseItThrowsAnException() throws Exception {
		throw new Exception("Exception thrown.");
	}

	/**
	 * @testing-testMethodOrder 5
	 */
	public void testWhichPostponesItsFinishAndThenPasses() {
		log("about to postpone finish for 1000ms");
		postponeCurrentTest(10000);

		final Timer timer = new Timer() {
			public void run() {
				log("continuing...");
				Window.alert("an alert within the postponed code!");
				finishTest();
			}
		};
		timer.schedule(500);
		log("timer scheduled to run in 500ms.");
	}

	/**
	 * @testing-testMethodOrder 6
	 */
	public void testWhichPostponesItsFinishAndThenFails() {
		log("about to postpone finish for 1000ms");
		postponeCurrentTest(10000000);

		final Timer timer = new Timer() {
			public void run() {
				Test.fail("Failure within postponed code");
			}
		};
		timer.schedule(500);
		log("timer scheduled to run in 500ms.");
	}

	/**
	 * @testing-testMethodOrder 7
	 */
	public void testWhichSetsSkipRemainingTests() {
		log("about to skip remaining");
		skipRemainingTests();
	}

	/**
	 * @testing-testMethodOrder 8
	 */
	public void testWhichShouldHaveBeenSkipped() {
		log("about to skip remaining");
		Test.fail("This test should *NOT* have been executed it should have been skipped");
	}

	/**
	 * If the test name includes the word stop(the second last one does) ask the
	 * TestRunner to skip remaining tests.
	 * 
	 * @param test
	 */
	protected void onFailed(final Test test) {
		super.onTestFailed(test);

		if (test.getName().indexOf("Skip") != -1) {
			TestRunner.skipRemainingTests();
		}
	}
}
