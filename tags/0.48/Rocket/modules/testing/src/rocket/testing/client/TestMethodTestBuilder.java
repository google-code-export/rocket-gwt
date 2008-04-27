package rocket.testing.client;

/**
 * This TestBuilder only exists to provide a method to access the executing
 * TestRunner
 * 
 * @author Miroslav Pokorny
 */
abstract public class TestMethodTestBuilder implements TestBuilder {

	protected TestRunner getTestRunner() {
		return TestRunner.getTestRunner();
	}

}
