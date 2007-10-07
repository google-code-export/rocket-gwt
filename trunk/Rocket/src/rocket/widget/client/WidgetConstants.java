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

	final static String SELECTED = "-selected";

	// AUTO COMPLETE TEXT BOX
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a AutoCompleteTextBox
	 * {@link AutoCompleteTextBox}
	 */
	final static String AUTO_COMPLETE_TEXT_BOX_STYLE = ROCKET + "-autoCompleteTextBox";

	final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE = AUTO_COMPLETE_TEXT_BOX_STYLE + "-dropDownList";

	final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_ODD_ROW_STYLE = AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE + "-oddRow";

	final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_EVEN_ROW_STYLE = AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE + "-evenRow";

	final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_SELECTED_STYLE = AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE + SELECTED;

	final static char AUTO_COMPLETE_TEXT_BOX_CANCEL_KEY = KeyboardListener.KEY_ESCAPE;

	final static char AUTO_COMPLETE_TEXT_BOX_ACCEPT_KEY = KeyboardListener.KEY_ENTER;

	final static char AUTO_COMPLETE_TEXT_BOX_DOWN_KEY = KeyboardListener.KEY_DOWN;

	final static char AUTO_COMPLETE_TEXT_BOX_UP_KEY = KeyboardListener.KEY_UP;

	// BLOCKY PIXEL
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	final static String BLOCKY_PIXEL_STYLE = ROCKET + "-blockyPixel";

	final static int TRANSPARENT = -1;

	// STYLESHEET PICKER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	final static String STYLESHEET_PICKER_STYLE = ROCKET + "-styleSheetPicker";

	final static String STYLESHEET_PICKER_LABEL_STYLE = STYLESHEET_PICKER_STYLE + "-label";

	final static String STYLESHEET_PICKER_LABEL_TEXT = "Text Size";

	final static String STYLESHEET_ITEM_STYLE = STYLESHEET_PICKER_STYLE + "-item";

	final static String STYLESHEET_ITEM_SELECTED_STYLE = STYLESHEET_ITEM_STYLE + SELECTED;

	// CARD
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a CollapsablePanel
	 * {@link CollapsablePanel}
	 */
	final static String COLLAPSABLE_PANEL_STYLE = ROCKET + "-collapsablePanel";

	final static String COLLAPSABLE_PANEL_TITLE_STYLE = COLLAPSABLE_PANEL_STYLE + "-title";

	final static int COLLAPSABLE_PANEL_TITLE_ROW = 0;

	final static int COLLAPSABLE_PANEL_TITLE_COLUMN = 0;

	final static String COLLAPSABLE_PANEL_TITLE_FLEXTABLE_STYLE = COLLAPSABLE_PANEL_TITLE_STYLE;

	final static String COLLAPSABLE_PANEL_CONTENT_STYLE = COLLAPSABLE_PANEL_STYLE + "-content";

	final static int COLLAPSABLE_PANEL_CONTENT_ROW = COLLAPSABLE_PANEL_TITLE_ROW + 1;

	final static int COLLAPSABLE_PANEL_CONTENT_COLUMN = 0;

	final static String COLLAPSABLE_PANEL_TITLE_WIDGET_STYLE = COLLAPSABLE_PANEL_STYLE + "-titleWidget";

	final static String COLLAPSABLE_PANEL_MINIMIZE_IMAGE_URL = Browser.buildImageUrl("/collapsablePanel/minimize.gif");

	final static String COLLAPSABLE_PANEL_MAXIMIZE_IMAGE_URL = Browser.buildImageUrl("/collapsablePanel/maximize.gif");

	final static String COLLAPSABLE_PANEL_CLOSE_IMAGE_URL = Browser.buildImageUrl("/collapsablePanel/close.gif");

	// BREADCRUMB
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a BreadcrumbPanel
	 * {@link BreadcrumbPanel}
	 */
	final static String BREADCRUMB_PANEL_STYLE = ROCKET + "-breadcrumbPanel";

	final static String BREADCRUMB_PANEL_CRUMB_STYLE = BREADCRUMB_PANEL_STYLE + "-item";

	final static String BREADCRUMB_PANEL_LAST_CRUMB_STYLE = BREADCRUMB_PANEL_CRUMB_STYLE + "-last";

	final static String BREADCRUMB_PANEL_SEPARATOR_HTML = ">";

	final static String BREADCRUMB_PANEL_SEPARATOR_STYLE = BREADCRUMB_PANEL_STYLE + "-separator";

	// GRID
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a GridView
	 * {@link GridView}
	 */
	final static String GRIDVIEW_STYLE = ROCKET + "-gridview";

	final static String GRIDVIEW_CELL_STYLE = GRIDVIEW_STYLE + "-cell";

	final static String GRIDVIEW_FILLER_STYLE = GRIDVIEW_CELL_STYLE + "-filler";

	// SORTABLD TABLE
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a SortableTable
	 * {@link SortableTable}
	 */
	final static String SORTABLE_TABLE_STYLE = ROCKET + "-sortableTable";

	final static String SORTABLE_TABLE_HEADER_ROW_STYLE = SORTABLE_TABLE_STYLE + "-headerRow";

	final static String SORTABLE_TABLE_SORTABLE_COLUMN_STYLE = SORTABLE_TABLE_STYLE + "-sortableColumn";

	final static String SORTABLE_TABLE_SORTED_COLUMN_STYLE = SORTABLE_TABLE_STYLE + "-sortedColumn";

	final static String SORTABLE_TABLE_SORT_DIRECTIONS_ARROWS_STYLE = SORTABLE_TABLE_STYLE + "-sortDirectionArrows";

	final static String SORTABLE_TABLE_ODD_ROW_STYLE = SORTABLE_TABLE_STYLE + "-oddRow";

	final static String SORTABLE_TABLE_EVEN_ROW_STYLE = SORTABLE_TABLE_STYLE + "-evenRow";

	// ZEBRA
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a ZebraFlexTable
	 */
	final static String ZEBRA_FLEX_TABLE_STYLE = ROCKET + "-zebraFlexTable";

	final static String ZEBRA_FLEX_TABLE_HEADING_STYLE = ZEBRA_FLEX_TABLE_STYLE + "-heading";

	final static String ZEBRA_FLEX_TABLE_ODD_ROW_STYLE = ZEBRA_FLEX_TABLE_STYLE + "-oddRow";

	final static String ZEBRA_FLEX_TABLE_EVEN_ROW_STYLE = ZEBRA_FLEX_TABLE_STYLE + "-evenRow";

	// SPINNER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a SpinnerWidget
	 */
	final static String SPINNER_STYLE = ROCKET + "-spinner";

	final static String SPINNER_UP_STYLE = SPINNER_STYLE + "-up";

	final static String SPINNER_DOWN_STYLE = SPINNER_STYLE + "-down";

	// SUPER SPINNER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a SuperSpinnerWidget
	 */
	final static String SUPER_SPINNER_STYLE = ROCKET + "-superSpinner";

	final static String SUPER_SPINNER_UP_STYLE = SUPER_SPINNER_STYLE + "-up";

	final static String SUPER_SPINNER_DOWN_STYLE = SUPER_SPINNER_STYLE + "-down";

	final static String SUPER_SPINNER_BIG_UP_STYLE = SUPER_SPINNER_STYLE + "-bigUp";

	final static String SUPER_SPINNER_BIG_DOWN_STYLE = SUPER_SPINNER_STYLE + "-bigDown";

	// PAGER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a Pager widget
	 * {@link rocket.widget.client.Pager}
	 */
	final static String PAGER_STYLE = ROCKET + "-pager";

	/**
	 * The default text that appears on the previous button
	 */
	final static String PAGER_PREVIOUS_BUTTON_TEXT = "Previous";

	/**
	 * This style is applied to the previous button
	 */
	final static String PAGER_PREVIOUS_BUTTON_STYLE = PAGER_STYLE + "-previous";

	/**
	 * The default text that appears on the next button.
	 */
	final static String PAGER_NEXT_BUTTON_TEXT = "Next";

	/**
	 * This style is applied to the next button
	 */
	final static String PAGER_NEXT_BUTTON_STYLE = PAGER_STYLE + "-next";

	/**
	 * This style is applied to each of the page buttons that appear along the
	 * pager widget
	 */
	final static String PAGER_GOTO_PAGE_STYLE = PAGER_STYLE + "-goto";

	/**
	 * This style is applied to the current page.
	 */
	final static String PAGER_CURRENT_PAGE_STYLE = PAGER_STYLE + "-current";

	// SPAN PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This style is applied to the container SPAN element of a SpanPanel
	 * {@link rocket.widget.client.SpanPanel}
	 */
	final static String SPAN_PANEL_STYLE = ROCKET + "-spanPanel";

	final static String SPAN_TAG = "span";

	// DIV PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This style is applied to the container DIV element of a DivPanel
	 * {@link rocket.widget.client.DivPanel}
	 */
	final static String DIV_PANEL_STYLE = ROCKET + "-divPanel";

	final static String DIV_TAG = "div";

	// ORDERED LIST PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This style is applied to the container OL element of a OrderedListPanel
	 * {@link rocket.widget.client.OrderedListPanel}
	 */
	final static String ORDERED_LIST_PANEL_STYLE = ROCKET + "-orderedListPanel";

	final static String ORDERED_LIST_TAG = "ol";

	final static String ORDERED_LIST_ITEM_TAG = "li";

	// UNORDERED LIST PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**
	 * This style is applied to the container UL element of a UnorderedListPanel
	 * {@link rocket.widget.client.UnorderedListPanel}
	 */
	final static String UNORDERED_LIST_PANEL_STYLE = ROCKET + "-unorderedListPanel";

	final static String UNORDERED_LIST_TAG = "ul";

	final static String UNORDERED_LIST_ITEM_TAG = "li";

	// HYPERLINK_STYLE PANEL
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a HyperlinkPanel widget
	 * {@link rocket.widget.client.HyperlinkPanel}
	 */
	final static String HYPERLINK_PANEL_STYLE = ROCKET + "-hyperlinkPanel";

	final static String HYPERLINK_PANEL_PREVIOUS_SUNK_EVENTS_BIT_MASK = "__previousSunkEventsBitMask";

	/**
	 * This style is applied to the container element of a ResizablePanel widget
	 * {@link rocket.widget.client.ResizablePanel}
	 */
	final static String RESIZABLE_PANEL_STYLE = ROCKET + "-resizablePanel";

	final static String RESIZABLE_PANEL_WIDGET_STYLE = RESIZABLE_PANEL_STYLE + "-widget";

	final static String RESIZABLE_PANEL_RIGHT_HANDLE_STYLE = RESIZABLE_PANEL_STYLE + "-rightHandle";

	final static String RESIZABLE_PANEL_CORNER_HANDLE_STYLE = RESIZABLE_PANEL_STYLE + "-cornerHandle";

	final static String RESIZABLE_PANEL_BOTTOM_HANDLE_STYLE = RESIZABLE_PANEL_STYLE + "-bottomHandle";

	final static String RESIZABLE_PANEL_GHOST_STYLE = RESIZABLE_PANEL_STYLE + "-ghost";

	// DATEPICKER

	final static int DATEPICKER_ROWS = 6;

	final static int DATEPICKER_COLUMNS = 7;

	final static int DATEPICKER_MILLISECONDS_IN_A_DAY = 24 * 60 * 60 * 1000;

	final static int DATEPICKER_MILLISECONDS_IN_A_WEEK = DATEPICKER_MILLISECONDS_IN_A_DAY * 7;

	final static int DATEPICKER_YEAR_BIAS = 1900;

	final static String DATEPICKER_STYLE = ROCKET + "-datePicker";

	final static String DATEPICKER_HEADING_STYLE = DATEPICKER_STYLE + "-heading";

	final static String DATEPICKER_DAY_STYLE = DATEPICKER_STYLE + "-day";

	final static String DATEPICKER_PREVIOUS_MONTH_STYLE = DATEPICKER_STYLE + "-previousMonth";

	final static String DATEPICKER_CURRENT_MONTH_STYLE = DATEPICKER_STYLE + "-currentMonth";

	final static String DATEPICKER_NEXT_MONTH_STYLE = DATEPICKER_STYLE + "-nextMonth";

	final static String READONLY = "-readOnly";

	// PRIMITIVE WIDGET CONSTANTS FOLLOW
	// ::::::::::::::::::::::::::::::::::::::::::::

	final static String TEXTBOX_STYLE = ROCKET + "-textBox";

	final static String TEXTBOX_READONLY = TEXTBOX_STYLE + READONLY;

	final static String TEXTBOX_INPUT_TYPE = "text";

	final static String PASSWORD = ROCKET + "-passwordTextBox";

	final static String PASSWORD_READONLY = PASSWORD + READONLY;

	final static String PASSWORD_TEXTBOX_INPUT_TYPE = "password";

	final static String CHECKBOX_STYLE = ROCKET + "checkBox";

	final static String CHECKBOX_READONLY = CHECKBOX_STYLE + READONLY;

	final static String CHECKBOX_INPUT_TYPE = "checkbox";

	final static String LABEL_STYLE = ROCKET + "-label";

	final static String HTML = ROCKET + "-html";

	final static String TEXTAREA_STYLE = ROCKET + "-textArea";

	final static String TEXTAREA_TAG = "textarea";

	final static String TEXTAREA_READONLY = CHECKBOX_STYLE + READONLY;

	final static String LISTBOX_STYLE = ROCKET + "-listBox";

	final static String LISTBOX_TAG = "select";

	final static String LISTBOX_READONLY = LISTBOX_STYLE + READONLY;

	final static int LISTBOX_INSERT_AT_END = -1;

	final static String BUTTON_STYLE = ROCKET + "-button";

	final static String BUTTON_TAG = "button";

	final static String BUTTON_READONLY = BUTTON_STYLE + READONLY;

	final static String BUTTON_INPUT_RESET_TYPE = "reset";

	final static String BUTTON_INPUT_SUBMIT_TYPE = "submit";

	final static String RADIO_BUTTON_STYLE = ROCKET + "-radioButton";

	final static String RADIO_BUTTON_READONLY = RADIO_BUTTON_STYLE + READONLY;

	final static String RADIO_BUTTON_INPUT_TYPE = "radio";

	final static String IMAGE_STYLE = ROCKET + "-image";

	final static String IMAGE_TAG = "img";

	final static String HYPERLINK_STYLE = ROCKET + "-hyperlink";

	final static String HYPERLINK_TAG = "a";

	final static String FILE_UPLOAD_STYLE = ROCKET + "-fileUpload";

	final static String FILE_UPLOAD_READONLY = FILE_UPLOAD_STYLE + READONLY;

	final static String FILE_UPLOAD_INPUT_TYPE = "file";

	final static String HIDDEN_INPUT_TYPE = "hidden";

	final static String IFRAME_TARGET = "iframe";

	final static String FORM_TAG = "form";

	final static String FORM_PANEL_TARGET_PREFIX = "__FormPanel";

	final static String FORM_PANEL_STYLE = ROCKET + "-formPanel";

	/**
	 * Used with {@link #setEncoding(String)} to specify that the form will be
	 * submitted using MIME encoding (necessary for {@link FileUpload} to work
	 * properly).
	 */
	static final String ENCODING_MULTIPART = "multipart/form-data";

	/**
	 * Used with {@link #setEncoding(String)} to specify that the form will be
	 * submitted using traditional URL encoding.
	 */
	static final String ENCODING_URLENCODED = "application/x-www-form-urlencoded";

	final static String VIEWPORT_STYLE = WidgetConstants.ROCKET + "-viewport";

	final static String VIEWPORT_TILE_STYLE = VIEWPORT_STYLE + "-tile";

	final static String VIEWPORT_OUT_OF_BOUNDS_STYLE = VIEWPORT_STYLE + "-outOfBounds";

	final static String VIEWPORT_TILE_LEFT_ATTRIBUTE = "__tileLeft";

	final static String VIEWPORT_TILE_TOP_ATTRIBUTE = "__tileTop";

	final static int VIEWPORT_X_OFFSET = 16384;

	final static int VIEWPORT_Y_OFFSET = 16384;

}
