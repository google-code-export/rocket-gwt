package rocket.generator.test;

import junit.framework.TestCase;
import rocket.generator.rebind.RebindHelper;

public class RebindHelperTestCase extends TestCase {
	public void testisValidJavascriptIdentiferAgainstValidIdentifier() {
		assertTrue("apple", RebindHelper.isValidJavascriptIdentifier("apple"));
	}

	public void testisValidJavascriptIdentiferAgainstInvalidIdentifier() {
		assertFalse("for", RebindHelper.isValidJavascriptIdentifier("for"));
	}

}
