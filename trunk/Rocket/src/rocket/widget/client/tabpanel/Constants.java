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
package rocket.widget.client.tabpanel;

import rocket.browser.client.Browser;
import rocket.widget.client.WidgetConstants;

class Constants {
	/*
	 * TAB
	 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 */
	final static String TAB_CLOSE_BUTTON_IMAGE_URL = Browser.buildImageUrl("/tab/close.gif");

	private final static String TAB_BAR_STYLE = "-tabBar";

	private final static String BEFORE_SPACER_STYLE = "-beforeSpacer";

	private final static String AFTER_SPACER_STYLE = "-afterSpacer";

	private final static String ITEM_STYLE = "-item";

	private final static String ITEM_LABEL_STYLE = "-label";

	private final static String WIDGET_STYLE = "-widget";

	private final static String SELECTED_STYLE = "-selected";

	private final static String CONTENT_STYLE = "-content";

	// TOP
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	final static String TOP_TAB_PANEL_STYLE = WidgetConstants.ROCKET + "-topTabPanel";

	final static String TOP_TAB_BAR_STYLE = TOP_TAB_PANEL_STYLE + TAB_BAR_STYLE;

	final static String TOP_TAB_BAR_BEFORE_SPACER_STYLE = TOP_TAB_BAR_STYLE + BEFORE_SPACER_STYLE;

	final static String TOP_TAB_BAR_AFTER_SPACER_STYLE = TOP_TAB_BAR_STYLE + AFTER_SPACER_STYLE;

	final static String TOP_TAB_BAR_ITEM_STYLE = TOP_TAB_BAR_STYLE + ITEM_STYLE;

	final static String TOP_TAB_BAR_ITEM_LABEL_STYLE = TOP_TAB_BAR_ITEM_STYLE +ITEM_LABEL_STYLE;

	final static String TOP_TAB_BAR_ITEM_WIDGET_STYLE = TOP_TAB_BAR_ITEM_STYLE + WIDGET_STYLE;

	final static String TOP_TAB_BAR_ITEM_SELECTED_STYLE = TOP_TAB_BAR_ITEM_STYLE + SELECTED_STYLE;

	final static String TOP_TAB_CONTENT_STYLE = TOP_TAB_PANEL_STYLE + CONTENT_STYLE;

	/*
	 * BOTTOM
	 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 */
	final static String BOTTOM_TAB_PANEL_STYLE = WidgetConstants.ROCKET + "-bottomTabPanel";

	final static String BOTTOM_TAB_BAR_STYLE = BOTTOM_TAB_PANEL_STYLE +TAB_BAR_STYLE;

	final static String BOTTOM_TAB_BAR_BEFORE_SPACER_STYLE = BOTTOM_TAB_BAR_STYLE +BEFORE_SPACER_STYLE;

	final static String BOTTOM_TAB_BAR_AFTER_SPACER_STYLE = BOTTOM_TAB_BAR_STYLE +AFTER_SPACER_STYLE;

	final static String BOTTOM_TAB_BAR_ITEM_STYLE = BOTTOM_TAB_BAR_STYLE +ITEM_STYLE;

	final static String BOTTOM_TAB_BAR_ITEM_LABEL_STYLE = BOTTOM_TAB_BAR_ITEM_STYLE +ITEM_LABEL_STYLE;

	final static String BOTTOM_TAB_BAR_ITEM_WIDGET_STYLE = BOTTOM_TAB_BAR_ITEM_STYLE +WIDGET_STYLE;

	final static String BOTTOM_TAB_BAR_ITEM_SELECTED_STYLE = BOTTOM_TAB_BAR_ITEM_STYLE +SELECTED_STYLE;

	final static String BOTTOM_TAB_CONTENT_STYLE = BOTTOM_TAB_PANEL_STYLE + CONTENT_STYLE;

	// LEFT
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	final static String LEFT_TAB_PANEL_STYLE = WidgetConstants.ROCKET + "-leftTabPanel";

	final static String LEFT_TAB_BAR_STYLE = LEFT_TAB_PANEL_STYLE +TAB_BAR_STYLE;

	final static String LEFT_TAB_BAR_BEFORE_SPACER_STYLE = LEFT_TAB_BAR_STYLE +BEFORE_SPACER_STYLE;

	final static String LEFT_TAB_BAR_AFTER_SPACER_STYLE = LEFT_TAB_BAR_STYLE +AFTER_SPACER_STYLE;

	final static String LEFT_TAB_BAR_ITEM_STYLE = LEFT_TAB_BAR_STYLE +ITEM_STYLE;

	final static String LEFT_TAB_BAR_ITEM_LABEL_STYLE = LEFT_TAB_BAR_ITEM_STYLE +ITEM_LABEL_STYLE;

	final static String LEFT_TAB_BAR_ITEM_WIDGET_STYLE = LEFT_TAB_BAR_ITEM_STYLE +WIDGET_STYLE;

	final static String LEFT_TAB_BAR_ITEM_SELECTED_STYLE = LEFT_TAB_BAR_ITEM_STYLE +SELECTED_STYLE;

	final static String LEFT_TAB_CONTENT_STYLE = LEFT_TAB_PANEL_STYLE + CONTENT_STYLE;

	/*
	 * RIGHT
	 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 */
	final static String RIGHT_TAB_PANEL_STYLE = WidgetConstants.ROCKET + "-rightTabPanel";

	final static String RIGHT_TAB_BAR_STYLE = RIGHT_TAB_PANEL_STYLE +TAB_BAR_STYLE;

	final static String RIGHT_TAB_BAR_BEFORE_SPACER_STYLE = RIGHT_TAB_BAR_STYLE +BEFORE_SPACER_STYLE;

	final static String RIGHT_TAB_BAR_AFTER_SPACER_STYLE = RIGHT_TAB_BAR_STYLE +AFTER_SPACER_STYLE;

	final static String RIGHT_TAB_BAR_ITEM_STYLE = RIGHT_TAB_BAR_STYLE +ITEM_STYLE;

	final static String RIGHT_TAB_BAR_ITEM_LABEL_STYLE = RIGHT_TAB_BAR_ITEM_STYLE +ITEM_LABEL_STYLE;

	final static String RIGHT_TAB_BAR_ITEM_WIDGET_STYLE = RIGHT_TAB_BAR_ITEM_STYLE +WIDGET_STYLE;

	final static String RIGHT_TAB_BAR_ITEM_SELECTED_STYLE = RIGHT_TAB_BAR_ITEM_STYLE +SELECTED_STYLE;

	final static String RIGHT_TAB_CONTENT_STYLE = RIGHT_TAB_PANEL_STYLE + CONTENT_STYLE;

	/*
	 * TAB PANEL ITERATOR
	 * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 */
	final static int ITERATOR_TITLES_VIEW = 0;

	final static int ITERATOR_CONTENTS_VIEW = ITERATOR_TITLES_VIEW + 1;

	final static int ITERATOR_TAB_PANELS_VIEW = ITERATOR_CONTENTS_VIEW + 1;

}
