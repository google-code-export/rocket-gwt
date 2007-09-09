/* * Copyright Miroslav Pokorny
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
package rocket.widget.client;

import rocket.browser.client.Browser;
import rocket.style.client.StyleHelper;

import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * This class contains primarily constants related to Widgets. Most constants
 * here are the names of various styles for the available widgets.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class WidgetConstants {

	/**
	 * The top level (project name) that prefixes all widget classNames.
	 */
	public final static String ROCKET = "rocket";

	// AUTO COMPLETE TEXT BOX
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a AutoCompleteTextBox
	 * {@link AutoCompleteTextBox}
	 */
	public final static String AUTO_COMPLETE_TEXT_BOX_STYLE = StyleHelper.buildCompound(ROCKET, "autoCompleteTextBox");

	public final static String AUTO_COMPLETE_TEXT_BOX_TEXT_BOX_STYLE = StyleHelper.buildCompound(AUTO_COMPLETE_TEXT_BOX_STYLE, "textBox");

	public final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE = StyleHelper.buildCompound(AUTO_COMPLETE_TEXT_BOX_STYLE,
			"dropDownList");

	public final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_ODD_ROW_STYLE = StyleHelper.buildCompound(
			AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE, "oddRow");

	public final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_EVEN_ROW_STYLE = StyleHelper.buildCompound(
			AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE, "evenRow");

	public final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_SELECTED_STYLE = StyleHelper.buildCompound(
			AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE, "selected");

	public final static char AUTO_COMPLETE_TEXT_BOX_CANCEL_KEY = KeyboardListener.KEY_ESCAPE;

	public final static char AUTO_COMPLETE_TEXT_BOX_ACCEPT_KEY = KeyboardListener.KEY_ENTER;

	public final static char AUTO_COMPLETE_TEXT_BOX_DOWN_KEY = KeyboardListener.KEY_DOWN;

	public final static char AUTO_COMPLETE_TEXT_BOX_UP_KEY = KeyboardListener.KEY_UP;

	// BLOCKY PIXEL
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public final static String BLOCKY_PIXEL_STYLE = StyleHelper.buildCompound(ROCKET, "blockyPixel");

	public final static int TRANSPARENT = -1;

	// LIFE
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public final static String LIFE_STYLE = StyleHelper.buildCompound(ROCKET, "life");

	// STYLESHEET PICKER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public final static String STYLESHEET_PICKER_STYLE = StyleHelper.buildCompound(ROCKET, "styleSheetPicker");

	public final static String STYLESHEET_PICKER_LABEL_STYLE = StyleHelper.buildCompound(STYLESHEET_PICKER_STYLE, "label");

	public final static String STYLESHEET_PICKER_LABEL_TEXT = "Text Size";

	public final static String STYLESHEET_ITEM_STYLE = StyleHelper.buildCompound(STYLESHEET_PICKER_STYLE, "item");

	public final static String STYLESHEET_ITEM_SELECTED_STYLE = StyleHelper.buildCompound(STYLESHEET_ITEM_STYLE, "selected");

	// CARD
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a CollapsablePanel
	 * {@link CollapsablePanel}
	 */
	public final static String COLLAPSABLE_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "collapsablePanel");

	public final static String COLLAPSABLE_PANEL_TITLE_STYLE = StyleHelper.buildCompound(COLLAPSABLE_PANEL_STYLE, "title");

	public final static int COLLAPSABLE_PANEL_TITLE_ROW = 0;

	public final static int COLLAPSABLE_PANEL_TITLE_COLUMN = 0;

	public final static String COLLAPSABLE_PANEL_TITLE_FLEXTABLE_STYLE = COLLAPSABLE_PANEL_TITLE_STYLE;

	public final static String COLLAPSABLE_PANEL_CONTENT_STYLE = StyleHelper.buildCompound(COLLAPSABLE_PANEL_STYLE, "content");

	public final static int COLLAPSABLE_PANEL_CONTENT_ROW = COLLAPSABLE_PANEL_TITLE_ROW + 1;

	public final static int COLLAPSABLE_PANEL_CONTENT_COLUMN = 0;

	public final static String COLLAPSABLE_PANEL_TITLE_WIDGET_STYLE = StyleHelper.buildCompound(COLLAPSABLE_PANEL_STYLE, "titleWidget");

	public final static String COLLAPSABLE_PANEL_MINIMIZE_IMAGE_URL = Browser.buildImageUrl("/collapsablePanel/minimize.gif");

	public final static String COLLAPSABLE_PANEL_MAXIMIZE_IMAGE_URL = Browser.buildImageUrl("/collapsablePanel/maximize.gif");

	public final static String COLLAPSABLE_PANEL_CLOSE_IMAGE_URL = Browser.buildImageUrl("/collapsablePanel/close.gif");

	// BREADCRUMB
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a BreadcrumbPanel
	 * {@link BreadcrumbPanel}
	 */
	public final static String BREADCRUMB_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "breadcrumbPanel");

	public final static String BREADCRUMB_PANEL_ITEM_STYLE = StyleHelper.buildCompound(BREADCRUMB_PANEL_STYLE, "item");

	public final static String BREADCRUMB_PANEL_LAST_ITEM_STYLE = StyleHelper.buildCompound(BREADCRUMB_PANEL_ITEM_STYLE, "last");

	public final static String BREADCRUMB_PANEL_SEPARATOR_HTML = ">";

	public final static String BREADCRUMB_PANEL_SEPARATOR_STYLE = StyleHelper.buildCompound(BREADCRUMB_PANEL_STYLE, "separator");

	// GRID
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a Grid {@link Grid}
	 */
	public final static String GRID_STYLE = StyleHelper.buildCompound(ROCKET, "grid");

	public final static String GRID_CELL_STYLE = StyleHelper.buildCompound(GRID_STYLE, "cell");

	public final static String GRID_FILLER_STYLE = StyleHelper.buildCompound(GRID_CELL_STYLE, "filler");

	// SORTABLD TABLE
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a SortableTable
	 * {@link SortableTable}
	 */
	public final static String SORTABLE_TABLE_STYLE = StyleHelper.buildCompound(ROCKET, "sortableTable");

	public final static String SORTABLE_TABLE_COLUMN_HEADER_STYLE = StyleHelper.buildCompound(SORTABLE_TABLE_STYLE, "columnHeader");

	public final static String SORTABLE_TABLE_SORTABLE_COLUMN_HEADER_STYLE = StyleHelper.buildCompound(SORTABLE_TABLE_COLUMN_HEADER_STYLE,
			"sortable");

	public final static String SORTABLE_TABLE_SORTED_COLUMN_HEADER_STYLE = StyleHelper.buildCompound(SORTABLE_TABLE_COLUMN_HEADER_STYLE,
			"sorted");

	public final static String SORTABLE_TABLE_SORTED_COLUMN_STYLE = StyleHelper.buildCompound(SORTABLE_TABLE_STYLE, "sortedColumn");

	public final static String SORTABLE_TABLE_SORT_DIRECTIONS_ARROWS_STYLE = StyleHelper.buildCompound(SORTABLE_TABLE_STYLE,
			"sortDirectionArrows");

	// ZEBRA
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a ZebraFlexTable
	 */
	public final static String ZEBRA_FLEX_TABLE_STYLE = StyleHelper.buildCompound(ROCKET, "zebraFlexTable");

	public final static String ZEBRA_FLEX_TABLE_HEADING_STYLE = StyleHelper.buildCompound(ZEBRA_FLEX_TABLE_STYLE, "heading");

	public final static String ZEBRA_FLEX_TABLE_ODD_ROW_STYLE = StyleHelper.buildCompound(ZEBRA_FLEX_TABLE_STYLE, "oddRow");

	public final static String ZEBRA_FLEX_TABLE_EVEN_ROW_STYLE = StyleHelper.buildCompound(ZEBRA_FLEX_TABLE_STYLE, "evenRow");

	// SPINNER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a SpinnerWidget
	 */
	public final static String SPINNER_STYLE = StyleHelper.buildCompound(ROCKET, "spinner");

	public final static String SPINNER_UP_STYLE = StyleHelper.buildCompound(SPINNER_STYLE, "up");

	public final static String SPINNER_DOWN_STYLE = StyleHelper.buildCompound(SPINNER_STYLE, "down");

	// SUPER SPINNER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a SuperSpinnerWidget
	 */
	public final static String SUPER_SPINNER_STYLE = StyleHelper.buildCompound(ROCKET, "superSpinner");

	public final static String SUPER_SPINNER_UP_STYLE = StyleHelper.buildCompound(SUPER_SPINNER_STYLE, "up");

	public final static String SUPER_SPINNER_DOWN_STYLE = StyleHelper.buildCompound(SUPER_SPINNER_STYLE, "down");

	public final static String SUPER_SPINNER_BIG_UP_STYLE = StyleHelper.buildCompound(SUPER_SPINNER_STYLE, "bigUp");

	public final static String SUPER_SPINNER_BIG_DOWN_STYLE = StyleHelper.buildCompound(SUPER_SPINNER_STYLE, "bigDown");

	// PAGER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a Pager widget
	 * {@link rocket.widget.client.Pager}
	 */
	public final static String PAGER_STYLE = StyleHelper.buildCompound(ROCKET, "pager");

	/**
	 * The default text that appears on the previous button
	 */
	public final static String PAGER_PREVIOUS_BUTTON_TEXT = "Previous";

	/**
	 * This style is applied to the previous button
	 */
	public final static String PAGER_PREVIOUS_BUTTON_STYLE = StyleHelper.buildCompound(PAGER_STYLE, "previous");

	/**
	 * The default text that appears on the next button.
	 */
	public final static String PAGER_NEXT_BUTTON_TEXT = "Next";

	/**
	 * This style is applied to the next button
	 */
	public final static String PAGER_NEXT_BUTTON_STYLE = StyleHelper.buildCompound(PAGER_STYLE, "next");

	/**
	 * This style is applied to each of the page buttons that appear along the
	 * pager widget
	 */
	public final static String PAGER_GOTO_PAGE_STYLE = StyleHelper.buildCompound(PAGER_STYLE, "goto");

	/**
	 * This style is applied to the current page.
	 */
	public final static String PAGER_CURRENT_PAGE_STYLE = StyleHelper.buildCompound(PAGER_STYLE, "current");

	// SPAN PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This style is applied to the container SPAN element of a SpanPanel
	 * {@link rocket.widget.client.SpanPanel}
	 */
	public final static String SPAN_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "spanPanel");

	// DIV PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This style is applied to the container DIV element of a DivPanel
	 * {@link rocket.widget.client.DivPanel}
	 */
	public final static String DIV_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "divPanel");

	// ORDERED LIST PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This style is applied to the container OL element of a OrderedListPanel
	 * {@link rocket.widget.client.OrderedListPanel}
	 */
	public final static String ORDERED_LIST_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "orderedListPanel");

	public final static String ORDERED_LIST = "ol";

	public final static String ORDERED_LIST_ITEM = "li";

	// UNORDERED LIST PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This style is applied to the container UL element of a UnOrderedListPanel
	 * {@link rocket.widget.client.UnorderedListPanel}
	 */
	public final static String UNORDERED_LIST_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "unorderedListPanel");

	public final static String UNORDERED_LIST = "ul";

	public final static String UNORDERED_LIST_ITEM = "li";

	// HYPERLINK PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a HyperlinkPanel widget
	 * {@link rocket.widget.client.HyperlinkPanel}
	 */
	public final static String HYPERLINK_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "hyperlinkPanel");

	/**
	 * This style is applied to the container element of a ResizablePanel widget
	 * {@link rocket.widget.client.ResizablePanel}
	 */
	public final static String RESIZABLE_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "resizablePanel");

	public final static String RESIZABLE_PANEL_EAST_HANDLE_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "eastHandle");

	public final static String RESIZABLE_PANEL_NORTHEAST_HANDLE_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "northEastHandle");

	public final static String RESIZABLE_PANEL_NORTH_HANDLE_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "northHandle");

	public final static String RESIZABLE_PANEL_NORTHWEST_HANDLE_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "northWestHandle");

	public final static String RESIZABLE_PANEL_WEST_HANDLE_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "leftHandle");

	public final static String RESIZABLE_PANEL_SOUTHEAST_HANDLE_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "southEastHandle");

	public final static String RESIZABLE_PANEL_SOUTH_HANDLE_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "southHandle");

	public final static String RESIZABLE_PANEL_SOUTHWEST_HANDLE_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "southWestHandle");

	public final static String RESIZABLE_PANEL_HANDLE_SELECTED_STYLE = StyleHelper.buildCompound(RESIZABLE_PANEL_STYLE, "selected");

	final static String TEXTBOX_TYPE_ATTRIBUTE = "text";

	final static String PASSWORD_TEXTBOX_TYPE_ATTRIBUTE = "password";

	final static String TEXTAREA_TAG = "textarea";

	final static String RADIO_BUTTON_TYPE_ATTRIBUTE = "radio";

	final static String RADIO_BUTTON_GROUP_ATTRIBUTE = "group";

	final static String CHECKBOX_TYPE_ATTRIBUTE = "checkbox";

	final static String LISTBOX_TAG = "select";

	final static String LABEL_TAG = "div";

	final static String BUTTON_TAG = "button";

	final static String IMAGE_TAG = "img";

	final static String HYPERLINK_TAG = "a";

	final static String HTML_TAG = "div";

	final static String FORM_TAG = "form";
}
