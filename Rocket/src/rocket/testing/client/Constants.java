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
 */package rocket.testing.client;

import rocket.widget.client.WidgetConstants;

class Constants {
	/**
	 * This style is applied to the container element of the InteractiveList
	 * {@see rocket.testing.client.InteractiveList}
	 */
	final static String INTERACTIVE_LIST_STYLE = WidgetConstants.ROCKET + "-interactiveList";

	/**
	 * This style is applied to the accompanying log.
	 */
	final static String INTERACTIVE_LIST_WIDGET_LOG_STYLE = INTERACTIVE_LIST_STYLE + "-log";

	/**
	 * This style is applied to the container element of the InteractivePanel
	 * {@see rocket.testing.client.InteractivePanel}
	 */
	final static String INTERACTIVE_PANEL_STYLE = WidgetConstants.ROCKET + "-interactivePanel";

	/**
	 * This style is applied to the accompanying log.
	 */
	final static String INTERACTIVE_PANEL_WIDGET_LOG_STYLE = INTERACTIVE_PANEL_STYLE + "-log";

	/**
	 * This is the style that is applied to the table used to display on going test results.
	 */
	public final static String WEBPAGE_TESTRUNNER_TABLE = "rocket-testing-webPageTestRunner-table";
}
