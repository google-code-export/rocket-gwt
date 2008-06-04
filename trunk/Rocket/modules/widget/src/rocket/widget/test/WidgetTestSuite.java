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
package rocket.widget.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import rocket.widget.test.divpanel.DivPanelGwtTestCase;
import rocket.widget.test.htmltemplatefactory.client.HtmlTemplateFactoryGwtTestCase;
import rocket.widget.test.hyperlinkpanel.HyperlinkPanelGwtTestCase;
import rocket.widget.test.orderedlistpanel.OrderedListPanelGwtTestCase;
import rocket.widget.test.spanpanel.SpanPanelGwtTestCase;
import rocket.widget.test.unorderedlistpanel.UnorderedListPanelGwtTestCase;

/**
 * TestSuite that executes all unit tests relating to the Widget module
 * 
 * @author Miroslav Pokorny
 */
public class WidgetTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for rocket.Beans");
		addTests(suite);
		return suite;
	}

	public static void addTests(TestSuite suite) {
		suite.addTestSuite(DivPanelGwtTestCase.class);
		suite.addTestSuite(OrderedListPanelGwtTestCase.class);
		suite.addTestSuite(SpanPanelGwtTestCase.class);
		suite.addTestSuite(UnorderedListPanelGwtTestCase.class);

		suite.addTestSuite(HyperlinkPanelGwtTestCase.class);

		suite.addTestSuite(HtmlTemplateFactoryGwtTestCase.class);
	}
}
