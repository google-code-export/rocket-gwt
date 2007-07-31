package rocket.testing.test.webpagetestrunner.client;

import java.util.ArrayList;
import java.util.List;

import rocket.testing.client.Test;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestRunner;
import rocket.testing.client.WebPageTestRunner;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test may be used to execute a set of tests returned by a TestBuilder. The outcome of each test is written to a table.
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
        final TestBuilder builder = new TestBuilder() {
            public List buildCandidates() {
                return WebPageTestRunnerTest.this.buildCandidates();
            }
        };
        return builder;
    }

    protected List buildCandidates() {
        final List tests = new ArrayList();

        tests.add(new Test() {
            public String getName() {
                return "testWhichPasses";
            }

            public void execute() {
                for (int i = 0; i < 10000; i++) {
                    Math.cos(1.23f);
                }
            }
        });

        tests.add(new Test() {
            public String getName() {
                return "testWhichPassesAfterBeingBusyFor100ms";
            }

            public void execute() {
                final long started = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - started >= 100) {
                        break;
                    }
                }
            }
        });

        tests.add(new Test() {
            public String getName() {
                return "testWhichFailsAnAssertEquals";
            }

            public void execute() {
                Test.assertEquals(1, 2);
            }
        });

        tests.add(new Test() {
            public String getName() {
                return "testWhichFailsBecauseItThrowsARuntimeExceptionAfterLoggingAMessage";
            }

            public void execute() {
                this.log("About to throw a RuntimeException...");
                throw new RuntimeException("RuntimeException thrown.");
            }
        });

        tests.add(new Test() {
            public String getName() {
                return "testWhichFailsBecauseItThrowsAnException";
            }

            public void execute() throws Exception {
                throw new Exception("Exception thrown.");
            }
        });

        tests.add(new Test() {
            public String getName() {
                return "testWhichPostponesItsFinishAndThenPasses";
            }

            public void execute() throws Exception {
                this.log("about to postpone finish for 1000ms");
                this.postponeFinish(10000);

                final Test thisTest = this;

                final Timer timer = new Timer() {
                    public void run() {
                        log("continuing...");
                        Window.alert("an alert within the postponed code!");
                        thisTest.finish();
                    }
                };
                timer.schedule(500);
                this.log("timer scheduled to run in 500ms.");
            }
        });

        tests.add(new Test() {
            public String getName() {
                return "testWhichPostponesItsFinishAndThenFails";
            }

            public void execute() throws Exception {
                this.log("about to postpone finish for 1000ms");
                this.postponeFinish(10000000);
                final Timer timer = new Timer() {
                    public void run() {
                        fail("Failure within postponed code");
                    }
                };
                timer.schedule(500);
                this.log("timer scheduled to run in 500ms.");
            }
        });

        tests.add(new Test() {
            public String getName() {
                return "testWhichSetsSkipRemainingTests";
            }

            public void execute() throws Exception {
                this.log("about to skip remaining");
                TestRunner.skipRemainingTests();
            }
        });

        tests.add(new Test() {
            public String getName() {
                return "testWhichShouldHaveBeenSkipped";
            }

            public void execute() throws Exception {
                fail("This test should *NOT* have been executed it should have been skipped");
            }
        });

        return tests;
    }

    /**
     * If the test name includes the word stop(the second last one does) ask the TestRunner to skip remaining tests.
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
