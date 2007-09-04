package rocket.widget.client;

import rocket.style.client.StyleHelper;

public class Constants {
	final static int DATEPICKER_ROW = 6;

	final static int DATEPICKER_COLUMNS = 7;

	final static int DATEPICKER_MILLISECONDS_IN_A_DAY = 24 * 60 * 60 * 1000;

	final static int DATEPICKER_MILLISECONDS_IN_A_WEEK = DATEPICKER_MILLISECONDS_IN_A_DAY * 7;

	final static int YEAR_BIAS = 1900;

	final static String DATE_PICKER_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET, "datePicker");

	final static String DATEPICKER_HEADING_STYLE = StyleHelper.buildCompound(DATE_PICKER_STYLE, "heading");

	final static String DATEPICKER_DAY_STYLE = StyleHelper.buildCompound(DATE_PICKER_STYLE, "day");

	final static String DATEPICKER_PREVIOUS_MONTH_STYLE = StyleHelper.buildCompound(DATE_PICKER_STYLE, "previousMonth");

	final static String DATEPICKER_CURRENT_MONTH_STYLE = StyleHelper.buildCompound(DATE_PICKER_STYLE, "currentMonth");

	final static String DATEPICKER_NEXT_MONTH_STYLE = StyleHelper.buildCompound(DATE_PICKER_STYLE, "nextMonth");
}
