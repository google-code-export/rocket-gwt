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
package rocket.widget.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

/**
 * A template class that provides most of the functionality to build a calendar
 * widget. THe widget itself is a grid containing optional headings and
 * potentially rows of weeks. A number of methods remain to be implemented by
 * sub classes allowing the picker to be customised.
 * <ul>
 * <li>{@link #hasHeadings()} Should be true if the date picker grid contains
 * headings</li>
 * <li>{@link #createHeading(int)} This method will be called if the grid has
 * headings.</li>
 * <li>{@link #createDayTile(int, int, int)} This method is called each time a
 * day tile is required during a redraw. Any type of widget may be created.</li>
 * </ul>
 * 
 * The date picker itself does not contain any listener methods. If a developer
 * needs to add something along the lines of a {@link ClickListener} to each day
 * tile that is created to determine which day was selected. A Calendar is
 * typically decorated by other buttons that allow the user to move forward or
 * back a month. Moving the month forward by one may be achieved by the
 * following
 * 
 * <pre>
 * datePicker.setMonth(datePicker.getMonth() + 1);
 * </pre>
 * 
 * Each time the date value of the Calendar is changed the {@link #redraw()}
 * must be called after values are completed. The methods that change the date
 * are
 * <ul>
 * <li>{@link #setMonth(int)}</li>
 * <li>{@link #setYear(int)}</li>
 * </ul>
 * 
 * The date picker is always positioned so that the start of the current month
 * is within the first week belonging to the calendar.
 */
abstract public class Calendar extends CompositeWidget {

	public Calendar() {
		super();
	}

	protected Widget createWidget() {
		this.setDate(this.createDate());
		return this.createCalendarGrid();
	}

	protected String getInitialStyleName() {
		return WidgetConstants.CALENDAR_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	public void onAttach() {
		super.onAttach();
		
		this.redraw();
	}

	/**
	 * This method should be called whenever there is a need to repaint or
	 * redraw all the day cells making up the calendar.
	 */
	public void redraw() {
		final CalendarGrid grid = this.getCalendarGrid();

		final Date date = this.getDate();
		long ticks = this.getDate().getTime();
		ticks = ticks - date.getDay() * WidgetConstants.CALENDAR_MILLISECONDS_IN_A_DAY;
		date.setTime(ticks);

		final String dayStyle = this.getDayStyle();
		final String currentMonthStyle = this.getCurrentMonthStyle();
		final String nextMonthStyle = this.getNextMonthStyle();
		String monthStyle = date.getDate() != 1 ? this.getPreviousMonthStyle() : currentMonthStyle;

		final int rowOffset = this.hasHeadings() ? 1 : 0;
		final int lastRow = grid.getRowCount();

		final CellFormatter cellFormatter = grid.getCellFormatter();

		for (int row = rowOffset; row < lastRow; row++) {
			for (int column = 0; column < WidgetConstants.CALENDAR_COLUMNS; column++) {

				final int year = date.getYear() + WidgetConstants.CALENDAR_YEAR_BIAS;
				final int month = date.getMonth();
				final int dayOfMonth = date.getDate();

				cellFormatter.setStyleName(row, column, dayStyle);
				cellFormatter.addStyleName(row, column, monthStyle);

				final Widget widget = this.createDayTile(year, month, dayOfMonth);
				grid.setWidget(row, column, widget, year, month, dayOfMonth);

				date.setDate(dayOfMonth + 1);

				if (date.getDate() < dayOfMonth) {
					monthStyle = monthStyle.equals(currentMonthStyle) ? nextMonthStyle : currentMonthStyle;
				}
			}
		}
	}

	protected String getDayStyle() {
		return WidgetConstants.CALENDAR_DAY_STYLE;
	}

	protected String getPreviousMonthStyle() {
		return WidgetConstants.CALENDAR_PREVIOUS_MONTH_STYLE;
	}

	protected String getCurrentMonthStyle() {
		return WidgetConstants.CALENDAR_CURRENT_MONTH_STYLE;
	}

	protected String getNextMonthStyle() {
		return WidgetConstants.CALENDAR_NEXT_MONTH_STYLE;
	}

	/**
	 * A calendarGrid is used to hold all the cells that make up the calendar
	 */
	protected CalendarGrid getCalendarGrid() {
		return (CalendarGrid) this.getWidget();
	}

	/**
	 * Creates a calendarGrid widget with the necessary number of rows and
	 * columns.
	 * 
	 * @return The new calendarGrid.
	 */
	protected CalendarGrid createCalendarGrid() {
		final boolean hasHeadings = this.hasHeadings();
		final int rows = hasHeadings ? WidgetConstants.CALENDAR_ROWS + 1 : WidgetConstants.CALENDAR_ROWS;
		final CalendarGrid grid = new CalendarGrid(rows, WidgetConstants.CALENDAR_COLUMNS);
		if (hasHeadings) {
			this.addHeadings(grid);
		}
		grid.setCellPadding(0);
		grid.setCellSpacing(0);

		final CellFormatter cellFormatter = grid.getCellFormatter();
		final String dayStyle = this.getDayStyle();

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < WidgetConstants.CALENDAR_COLUMNS; c++) {
				cellFormatter.setAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
				cellFormatter.setWidth(r, c, "100%");
				cellFormatter.setHeight(r, c, "100%");
				cellFormatter.setStyleName(r, c, dayStyle);
			}
		}

		return grid;
	}

	/**
	 * A specialised Grid that maintains caches that map dates to widgets and
	 * widgets back to date making it easy to find a widget by date and vice
	 * versa.
	 */
	static class CalendarGrid extends Grid {
		CalendarGrid(final int rows, final int columns) {
			super(rows, columns);

			this.setWidgetsToDates(createWidgetsToDates());
			this.setDatesToWidgets(createDatesToWidgets());
		}

		/**
		 * This mapping uses the year/month/dayOfMonth as the key with the value
		 * being the widget itself.
		 */
		private Map datesToWidgets;

		Map getDatesToWidgets() {
			return this.datesToWidgets;
		}

		void setDatesToWidgets(final Map datesToWidgets) {
			this.datesToWidgets = datesToWidgets;
		}

		Map createDatesToWidgets() {
			return new HashMap();
		}

		/**
		 * This mapping uses the widget as the key and the value is the
		 * year/month/dayOfMonth
		 */
		private Map widgetsToDates;

		Map getWidgetsToDates() {
			return this.widgetsToDates;
		}

		void setWidgetsToDates(final Map widgetsToDates) {
			this.widgetsToDates = widgetsToDates;
		}

		Map createWidgetsToDates() {
			return new HashMap();
		}

		Widget getWidget(final int year, final int month, final int dayOfMonth) {
			final Object key = buildKey(year, month, dayOfMonth);

			return (Widget) this.getDatesToWidgets().get(key);
		}

		void setWidget(final int row, final int column, final Widget widget, final int year, final int month, final int dayOfMonth) {
			final Map widgetsToDates = this.getWidgetsToDates();
			final Map datesToWidgets = this.getDatesToWidgets();

			// remove the previous widget the caches.
			final Widget previous = this.getWidget(row, column);
			if (null != previous) {
				final Object previousKey = widgetsToDates.remove(widget);
				datesToWidgets.remove(previousKey);
			}

			// update the calendarGrid itself
			this.setWidget(row, column, widget);

			// update the caches.
			final Object key = buildKey(year, month, dayOfMonth);
			widgetsToDates.put(key, widget);
			datesToWidgets.put(widget, key);
		}

		protected Object buildKey(final int year, final int month, final int dayOfMonth) {
			return year + "/" + month + "/" + dayOfMonth;
		}
	}

	/**
	 * Adds all headings for this calendar to the first row of the calendarGrid.
	 * This method should only be invoked once usually as part of the
	 * {@link #createWidget()} method.
	 * 
	 * @param calendarGrid
	 *            the calendarGrid being constructed.
	 */
	protected void addHeadings(final Grid grid) {
		for (int dayOfWeek = 0; dayOfWeek < WidgetConstants.CALENDAR_COLUMNS; dayOfWeek++) {
			final Widget heading = this.createHeading(dayOfWeek);
			heading.addStyleName(WidgetConstants.CALENDAR_HEADING_STYLE);
			grid.setWidget(0, dayOfWeek, heading);
		}
	}

	/**
	 * Sub classes should override this method to return true or false depending
	 * on whether a heading row should be included when the calendarGrid is
	 * built.
	 * 
	 * @return
	 */
	abstract protected boolean hasHeadings();

	/**
	 * Sub classes must implement this factory method to return a widget that
	 * will be used as a heading. Typically this might include the day of the
	 * week.
	 * 
	 * @param dayOfWeek
	 *            0 = Sunday, 1 = Monday etc.
	 * @return
	 */
	abstract protected Widget createHeading(final int dayOfWeek);

	/**
	 * Sub classes must implement this factory method to create and return a
	 * widget that will be used to display an individual day within a cell
	 * within the calendar.
	 * 
	 * @param year
	 *            The year.
	 * @param month
	 *            The month starting at 0 = January.
	 * @param day
	 *            The day of the month starting at 1
	 * @return
	 */
	abstract protected Widget createDayTile(final int year, final int month, final int day);

	/**
	 * Retrieves the widget at the given coordinates.
	 * 
	 * @param column
	 * @param row
	 * @return
	 */
	public Widget getDay(final int column, final int row) {
		final int row0 = this.hasHeadings() ? row + 1 : row;
		return this.getCalendarGrid().getWidget(row0, column);
	}

	/**
	 * Getter that makes it possible to find the corresponding widget for a
	 * particular date. If that date does not exist within the shown datepicker
	 * null is returned.
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @return The found day.
	 */
	public Widget getDay(final int year, final int month, final int dayOfMonth) {
		return this.getCalendarGrid().getWidget(year, month, dayOfMonth);
	}

	public void setDay(final int column, final int row, final int year, final int month, final int dayOfMonth, final Widget widget) {
		this.getCalendarGrid().setWidget(row, column, widget, year, month, dayOfMonth);
	}

	/**
	 * This Date object holds the date of the first day that appears in the
	 * calendar.
	 */
	private Date date;;

	protected Date getDate() {
		ObjectHelper.checkNotNull("field:date", date);
		return new Date(this.date.getTime());
	}

	protected void setDate(final Date date) {
		ObjectHelper.checkNotNull("parameter:date", date);
		this.date = new Date( date.getTime() );		
	}
	
	/**
	 * Factory which creates the date that will become the starting point for
	 * this calendar.
	 * 
	 * @return
	 */
	protected Date createDate() {
		final Date date = new Date();
		date.setDate( 1 );
		return date;
	}

	public int getYear() {
		return this.getDate().getYear() + WidgetConstants.CALENDAR_YEAR_BIAS;
	}

	public void setYear(final int year) {
		final Date date = this.getDate();
		date.setYear(year - WidgetConstants.CALENDAR_YEAR_BIAS);
		this.setDate(date);
	}

	public int getMonth() {
		return this.getDate().getMonth();
	}

	public void setMonth(final int month) {
		final Date date = this.getDate();
		date.setMonth(month);
		this.setDate(date);
	}
	
	public String toString(){
		return ObjectHelper.defaultToString( this) + ", date: " + this.getDate();
	}
}
