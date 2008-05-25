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

	final static String SELECTED = "selected";
	
	final static String ODD_ROW = "oddRow";
	
	final static String EVEN_ROW = "evenRow";

	// AUTO COMPLETE TEXT BOX
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a AutoCompleteTextBox
	 * {@link AutoCompleteTextBox}
	 */
	final static String AUTO_COMPLETE_TEXT_BOX_STYLE = ROCKET + "-autoCompleteTextBox";

	final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_STYLE = AUTO_COMPLETE_TEXT_BOX_STYLE + "-dropDownList";

	final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_ODD_ROW_STYLE = ODD_ROW;

	final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_EVEN_ROW_STYLE = EVEN_ROW;

	final static String AUTO_COMPLETE_TEXT_BOX_DROP_DOWN_LIST_SELECTED_STYLE = SELECTED;

	// STYLESHEET PICKER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	final static String STYLESHEET_PICKER_STYLE = ROCKET + "-styleSheetPicker";

	final static String STYLESHEET_PICKER_LABEL_STYLE = "label";

	final static String STYLESHEET_PICKER_LABEL_TEXT = "Text Size";

	final static String STYLESHEET_ITEM_STYLE = "item";

	final static String STYLESHEET_ITEM_SELECTED_STYLE = SELECTED;

	// BREADCRUMB
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a BreadcrumbPanel
	 * {@link BreadcrumbPanel}
	 */
	final static String BREADCRUMB_PANEL_STYLE = ROCKET + "-breadcrumbPanel";

	final static String BREADCRUMB_PANEL_CRUMB_STYLE = "item";

	final static String BREADCRUMB_PANEL_LAST_CRUMB_STYLE = "last";

	final static String BREADCRUMB_PANEL_SEPARATOR_HTML = ">";

	final static String BREADCRUMB_PANEL_SEPARATOR_STYLE = "separator";

	// GRID
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a GridView
	 * {@link GridView}
	 */
	final static String GRIDVIEW_STYLE = ROCKET + "-gridview";

	final static String GRIDVIEW_CELL_STYLE = "cell";

	final static String GRIDVIEW_FILLER_STYLE = "filler";

	// SORTABLD TABLE
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a SortableTable
	 * {@link SortableTable}
	 */
	final static String SORTABLE_TABLE_STYLE = ROCKET + "-sortableTable";

	final static String SORTABLE_TABLE_HEADER_ROW_STYLE = "headerRow";

	final static String SORTABLE_TABLE_SORTABLE_COLUMN_STYLE = "sortableColumn";

	final static String SORTABLE_TABLE_SORTED_COLUMN_STYLE = "sortedColumn";

	final static String SORTABLE_TABLE_SORT_DIRECTIONS_ARROWS_STYLE = "sortDirectionArrows";

	final static String SORTABLE_TABLE_ODD_ROW_STYLE = ODD_ROW;

	final static String SORTABLE_TABLE_EVEN_ROW_STYLE = EVEN_ROW;

	// ZEBRA
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a ZebraFlexTable
	 */
	final static String ZEBRA_FLEX_TABLE_STYLE = ROCKET + "-zebraFlexTable";

	final static String ZEBRA_FLEX_TABLE_HEADING_STYLE = "heading";

	final static String ZEBRA_FLEX_TABLE_ODD_ROW_STYLE = ODD_ROW;

	final static String ZEBRA_FLEX_TABLE_EVEN_ROW_STYLE = EVEN_ROW;

	// SPINNER
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * This style is applied to the container element of a SpinnerWidget
	 */
	final static String SPINNER_STYLE = ROCKET + "-spinner";

	final static String SPINNER_UP_STYLE = "up";

	final static String SPINNER_DOWN_STYLE = "down";

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
	final static String PAGER_PREVIOUS_BUTTON_TEXT = "<< Previous";

	/**
	 * This style is applied to the previous button
	 */
	final static String PAGER_PREVIOUS_BUTTON_STYLE = "previous";

	/**
	 * The default text that appears on the next button.
	 */
	final static String PAGER_NEXT_BUTTON_TEXT = "Next >>";

	/**
	 * This style is applied to the next button
	 */
	final static String PAGER_NEXT_BUTTON_STYLE = "next";

	/**
	 * This style is applied to each of the page buttons that appear along the
	 * pager widget
	 */
	final static String PAGER_GOTO_PAGE_STYLE = "goto";

	/**
	 * This style is applied to the current page.
	 */
	final static String PAGER_CURRENT_PAGE_STYLE = "current";

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

	final static String RESIZABLE_PANEL_WIDGET_STYLE = "widget";

	final static String RESIZABLE_PANEL_RIGHT_HANDLE_STYLE = "rightHandle";

	final static String RESIZABLE_PANEL_CORNER_HANDLE_STYLE = "cornerHandle";

	final static String RESIZABLE_PANEL_BOTTOM_HANDLE_STYLE = "bottomHandle";

	final static String RESIZABLE_PANEL_DRAGGED_WIDGET_STYLE = "draggedWidget";

	// CALENDAR

	final static int CALENDAR_ROWS = 6;

	final static int CALENDAR_COLUMNS = 7;

	final static int CALENDAR_MILLISECONDS_IN_A_DAY = 24 * 60 * 60 * 1000;

	final static int CALENDAR_MILLISECONDS_IN_A_WEEK = CALENDAR_MILLISECONDS_IN_A_DAY * 7;

	final static int CALENDAR_YEAR_BIAS = 1900;

	final static String CALENDAR_STYLE = ROCKET + "-calendar";

	final static String CALENDAR_HEADING_STYLE = "heading";

	final static String CALENDAR_DAY_STYLE = "day";

	final static String CALENDAR_PREVIOUS_MONTH_STYLE = "previousMonth";

	final static String CALENDAR_CURRENT_MONTH_STYLE = "currentMonth";

	final static String CALENDAR_NEXT_MONTH_STYLE = "nextMonth";

	final static String READONLY = "readOnly";

	// BASIC WIDGET CONSTANTS FOLLOW
	// ::::::::::::::::::::::::::::::::::::::::::::

	final static String TEXTBOX_STYLE = ROCKET + "-textBox";

	final static String TEXTBOX_READONLY = TEXTBOX_STYLE + READONLY;

	final static String TEXTBOX_INPUT_TYPE = "text";

	final static String PASSWORD = ROCKET + "-passwordTextBox";

	final static String PASSWORD_READONLY = PASSWORD + READONLY;

	final static String PASSWORD_TEXTBOX_INPUT_TYPE = "password";

	final static String CHECKBOX_STYLE = ROCKET + "-checkBox";

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

	/**
	 * The start of a collection of viewport widget constants.
	 */
	final static String VIEWPORT_STYLE = WidgetConstants.ROCKET + "-viewport";

	final static String VIEWPORT_TILE_STYLE = "tile";

	final static String VIEWPORT_OUT_OF_BOUNDS_STYLE = "outOfBounds";

	final static String VIEWPORT_TILE_LEFT_ATTRIBUTE = "__tileLeft";

	final static String VIEWPORT_TILE_TOP_ATTRIBUTE = "__tileTop";

	final static int VIEWPORT_X_OFFSET = 16384;

	final static int VIEWPORT_Y_OFFSET = 16384;

}
