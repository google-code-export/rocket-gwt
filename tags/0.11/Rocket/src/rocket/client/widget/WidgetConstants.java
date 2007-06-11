/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.widget;

import rocket.client.browser.BrowserHelper;
import rocket.client.dom.StyleHelper;

import com.google.gwt.user.client.ui.KeyboardListener;

public class WidgetConstants {
    // AUTO COMPLETE TEXT BOX :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String AUTO_COMPLETE_TEXT_BOX_STYLE = "autoCompleteTextBox";
    public final static String AUTO_COMPLETE_TEXT_BOX_TEXT_BOX_STYLE = StyleHelper.buildCompound( AUTO_COMPLETE_TEXT_BOX_STYLE, "textBox" );
    public final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE = StyleHelper.buildCompound( AUTO_COMPLETE_TEXT_BOX_STYLE, "dropDownList" );
    public final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_SELECTED_STYLE = StyleHelper.buildCompound( AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE, "selected" );

    public final static char AUTO_COMPLETE_TEXT_BOX_CANCEL_KEY = KeyboardListener.KEY_ESCAPE;
    public final static char AUTO_COMPLETE_TEXT_BOX_ACCEPT_KEY = KeyboardListener.KEY_ENTER;
    public final static char AUTO_COMPLETE_TEXT_BOX_DOWN_KEY = KeyboardListener.KEY_DOWN;
    public final static char AUTO_COMPLETE_TEXT_BOX_UP_KEY = KeyboardListener.KEY_UP;

	// BLOCKY PIXEL :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public final static String BLOCKY_GRID_STYLE = "blockyGrid";
    public final static int TRANSPARENT = -1;

	// LIFE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public final static String LIFE_STYLE = "life";

	// CSS PICKER ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String CSS_PICKER_STYLE = "cssPicker";
    public final static String CSS_PICKER_HORIZONTAL_PANEL_STYLE = StyleHelper.buildCompound( CSS_PICKER_STYLE, "horizontalPanel" );
    public final static String CSS_PICKER_LABEL_STYLE = StyleHelper.buildCompound( CSS_PICKER_STYLE, "label" );
    public final static String CSS_PICKER_LABEL_TEXT = "Text Size";
    public final static String CSS_ITEM_STYLE = StyleHelper.buildCompound( CSS_PICKER_STYLE, "item" );
    public final static String CSS_ITEM_SELECTED_STYLE = StyleHelper.buildCompound( CSS_ITEM_STYLE, "selected" );


	// CARD ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String CARD_STYLE = "card";
    public final static String CARD_FLEXTABLE_STYLE = StyleHelper.buildCompound( CARD_STYLE, "flexTable" );

    public final static String CARD_TITLE_STYLE = StyleHelper.buildCompound( CARD_STYLE, "title" );
    public final static int CARD_TITLE_ROW = 0;
    public final static int CARD_TITLE_COLUMN = 0;

    public final static String CARD_TITLE_FLEXTABLE_STYLE = CARD_TITLE_STYLE;

    public final static String CARD_CONTENT_STYLE = StyleHelper.buildCompound( CARD_STYLE, "content" );
    public final static int CARD_CONTENT_ROW = CARD_TITLE_ROW + 1;
    public final static int CARD_CONTENT_COLUMN = 0;

    public final static String CARD_TITLE_WIDGET_STYLE =  StyleHelper.buildCompound( CARD_STYLE, "titleWidget" );
    public final static String CARD_MINIMIZE_IMAGE_URL = BrowserHelper.buildImageUrl( "/card/minimize.gif" );
    public final static String CARD_MAXIMIZE_IMAGE_URL = BrowserHelper.buildImageUrl( "/card/maximize.gif");
    public final static String CARD_CLOSE_IMAGE_URL = BrowserHelper.buildImageUrl( "/card/close.gif");

	// BREADCRUMB ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String BREADCRUMBS_STYLE = "breadcrumbs";

    public final static String BREADCRUMB_ITEM_STYLE = StyleHelper.buildCompound( BREADCRUMBS_STYLE, "item" );
    public final static String BREADCRUMB_LAST_ITEM_STYLE = StyleHelper.buildCompound( BREADCRUMB_ITEM_STYLE, "last" );

    public final static String BREADCRUMB_SEPARATOR_HTML = ">";
    public final static String BREADCRUMB_SEPARATOR_STYLE = StyleHelper.buildCompound( BREADCRUMBS_STYLE, "separator");

	// GRID ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String GRID_STYLE = "grid";
    public final static String GRID_FLEXTABLE_STYLE = StyleHelper.buildCompound( GRID_STYLE, "flexTable" );
    public final static String GRID_CELL_STYLE = StyleHelper.buildCompound( GRID_STYLE, "cell" );
    public final static String GRID_FILLER_STYLE = StyleHelper.buildCompound( GRID_CELL_STYLE, "filler" );

	// SORTED TABLE ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String SORTED_TABLE_STYLE ="sortedTable";
    public final static String SORTED_COLUMN_STYLE = StyleHelper.buildCompound( SORTED_TABLE_STYLE, "sortedColumn" );

	// ZEBRA ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String ZEBRA_FLEX_TABLE_STYLE ="zebraFlexTable";
    public final static String ZEBRA_FLEX_TABLE_HEADING_STYLE = StyleHelper.buildCompound( ZEBRA_FLEX_TABLE_STYLE, "heading" );
    public final static String ZEBRA_FLEX_TABLE_ODD_ROW_STYLE =StyleHelper.buildCompound( ZEBRA_FLEX_TABLE_STYLE, "oddRow" );
    public final static String ZEBRA_FLEX_TABLE_EVEN_ROW_STYLE =StyleHelper.buildCompound( ZEBRA_FLEX_TABLE_STYLE, "evenRow" );

    // NUMBER BOX ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String NUMBER_TEXTBOX_STYLE = "numberTextBox";
    public final static String TEXTBOX_STYLE = "textBox";

	// SPINNER ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String SPINNER_STYLE = "spinner";
    public final static String SPINNER_VERTICAL_PANEL_STYLE = StyleHelper.buildCompound( SPINNER_STYLE, "verticalPanel" );
    public final static String SPINNER_UP_STYLE = StyleHelper.buildCompound( SPINNER_STYLE, "up" );
    public final static String SPINNER_DOWN_STYLE = StyleHelper.buildCompound( SPINNER_STYLE, "down" );
    public final static String SPINNER_BASE_URL = BrowserHelper.buildImageUrl("/spinner" );
    public final static String SPINNER_UP_IMAGE_URL = SPINNER_BASE_URL + "/up.gif";
    public final static String SPINNER_DOWN_IMAGE_URL = SPINNER_BASE_URL + "/down.gif";

	// SUPER SPINNER ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String SUPER_SPINNER_STYLE = "superSpinner";
    public final static String SUPER_SPINNER_HORIZONTAL_PANEL = StyleHelper.buildCompound( SUPER_SPINNER_STYLE, "horizontalPanel" );
    public final static String SUPER_SPINNER_UP_STYLE = StyleHelper.buildCompound( SUPER_SPINNER_STYLE, "up" );

    public final static String SUPER_SPINNER_DOWN_STYLE = StyleHelper.buildCompound( SUPER_SPINNER_STYLE, "down" );

    public final static String SUPER_SPINNER_BIG_UP_STYLE = StyleHelper.buildCompound( SUPER_SPINNER_STYLE, "bigUp" );

    public final static String SUPER_SPINNER_BIG_DOWN_STYLE = StyleHelper.buildCompound( SUPER_SPINNER_STYLE, "bigDown" );

    public final static String SUPER_SPINNER_BASE_URL = BrowserHelper.buildImageUrl("/superSpinner" );

    public final static String SUPER_SPINNER_UP_IMAGE_URL = SUPER_SPINNER_BASE_URL + "/up.gif";

    public final static String SUPER_SPINNER_DOWN_IMAGE_URL = SUPER_SPINNER_BASE_URL + "/down.gif";

    public final static String SUPER_SPINNER_BIG_UP_IMAGE_URL = SUPER_SPINNER_BASE_URL + "/bigUp.gif";

    public final static String SUPER_SPINNER_BIG_DOWN_IMAGE_URL = SUPER_SPINNER_BASE_URL + "/bigDown.gif";

    // PAGER ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String PAGER_STYLE = "pager";

    public final static String PAGER_HORIZONTAL_PANEL_STYLE = PAGER_STYLE;

    public final static String PAGER_PREVIOUS_BUTTON_TEXT = "Previous";
    public final static String PAGER_PREVIOUS_BUTTON_STYLE = StyleHelper.buildCompound( PAGER_STYLE, "previous" );

    public final static String PAGER_NEXT_BUTTON_TEXT = "Next";
    public final static String PAGER_NEXT_BUTTON_STYLE = StyleHelper.buildCompound( PAGER_STYLE, "next" );

    public final static String PAGER_GOTO_PAGE_STYLE = StyleHelper.buildCompound( PAGER_STYLE, "goto" );
    public final static String PAGER_CURRENT_PAGE_STYLE = StyleHelper.buildCompound( PAGER_STYLE, "current" );

	// TAB :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String TAB_CLOSE_BUTTON_IMAGE_URL = BrowserHelper.buildImageUrl("/tab/close.gif");

    public final static String TAB_PANEL_STYLE = "tabPanel";

    public final static String TAB_PANEL_VERTICAL_PANEL_STYLE = TAB_PANEL_STYLE;

    public final static String TAB_BAR_STYLE = StyleHelper.buildCompound( TAB_PANEL_STYLE, "tabBar" );

    public final static String TAB_BAR_FIRST_STYLE = StyleHelper.buildCompound( TAB_BAR_STYLE, "first" );

    public final static String TAB_BAR_REST_STYLE = StyleHelper.buildCompound( TAB_BAR_STYLE, "rest" );

    public final static String TAB_BAR_ITEM_STYLE = StyleHelper.buildCompound( TAB_BAR_STYLE, "item" );

    public final static String TAB_BAR_ITEM_LABEL_STYLE = StyleHelper.buildCompound( TAB_BAR_ITEM_STYLE, "label" );

    public final static String TAB_BAR_ITEM_CLOSE_BUTTON_STYLE = StyleHelper.buildCompound( TAB_BAR_ITEM_STYLE, "closeButton" );

    public final static String TAB_BAR_ITEM_SELECTED_STYLE = StyleHelper.buildCompound( TAB_BAR_ITEM_STYLE ,"selected" );

    public final static String TAB_CONTENT_STYLE = StyleHelper.buildCompound( TAB_PANEL_STYLE ,"content" );
}