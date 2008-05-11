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
package rocket.widget.client.menu;

import rocket.widget.client.WidgetConstants;

class Constants {
	final static String MENU_STYLE = WidgetConstants.ROCKET + "-menu";

	final static String SELECTED = "selected";

	final static String DISABLED = "disabled";

	// menu bars...
	final static String HORIZONTAL_MENU_BAR_STYLE = MENU_STYLE + "-horizontalMenuBar";

	final static String VERTICAL_MENU_BAR_STYLE = MENU_STYLE + "-verticalMenuBar";

	final static String CONTEXT_MENU_STYLE = MENU_STYLE + "-contextMenu";

	// menu widgets...
	final static String SUB_MENU_ITEM_STYLE = MENU_STYLE + "-subMenuItem";

	final static String SUB_MENU_ITEM_SELECTED_STYLE = SELECTED;

	final static String SUB_MENU_ITEM_DISABLED_STYLE = DISABLED;

	final static String MENU_ITEM_STYLE = MENU_STYLE + "-menuItem";

	final static String MENU_ITEM_SELECTED_STYLE = SELECTED;

	final static String MENU_ITEM_DISABLED_STYLE = DISABLED;

	final static String SPACER_STYLE = MENU_STYLE + "-menuSpacer";

	final static String SPACER_HTML = "<hr>";

	// menu lists....
	final static String HORIZONTAL_MENU_LIST_STYLE = MENU_STYLE + "-horizontalMenuList";

	final static String VERTICAL_MENU_LIST_STYLE = MENU_STYLE + "-verticalMenuList";
}
