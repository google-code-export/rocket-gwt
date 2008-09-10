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

import rocket.widget.client.WidgetConstants;

class Constants {
	final static String TAB_CLOSE_BUTTON_IMAGE_URL = "./tab/close.gif";

	final static String TAB_BAR_STYLE = "tabBar";
	final static String BEFORE_SPACER_STYLE = "beforeSpacer";
	final static String AFTER_SPACER_STYLE = "afterSpacer";
	final static String ITEM_STYLE = "item";
	final static String ITEM_LABEL_STYLE = "label";
	final static String ITEM_WIDGET_STYLE = "widget";
	final static String ITEM_SELECTED_STYLE = "item-selected";
	final static String CONTENT_STYLE = "content";

	final static String TOP_TAB_PANEL_STYLE = WidgetConstants.ROCKET + "-topTabPanel";
	final static String BOTTOM_TAB_PANEL_STYLE = WidgetConstants.ROCKET + "-bottomTabPanel";
	/*
	 * TAB PANEL ITERATOR
	 * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 */
	final static int ITERATOR_TITLES_VIEW = 0;

	final static int ITERATOR_CONTENTS_VIEW = ITERATOR_TITLES_VIEW + 1;

	final static int ITERATOR_TAB_PANELS_VIEW = ITERATOR_CONTENTS_VIEW + 1;

	final static int TAB_BAR_PANEL_INDEX = 0;
	final static int DECK_PANEL_INDEX = TAB_BAR_PANEL_INDEX + 1;
}
