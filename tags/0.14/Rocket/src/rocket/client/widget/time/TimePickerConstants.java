package rocket.client.widget.time;

import rocket.client.dom.StyleHelper;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TimePickerConstants {
	public final static int SECONDS = 1000;
	public final static int MINUTES = 60 * SECONDS;
	public final static int HOURS = 60 * MINUTES;
	
	public final static int SECONDS_IN_A_MINUTE = 60;
	public final static int MINUTES_IN_A_HOUR = 60;
	public final static int HOURS_IN_A_DAY = 24;
	
	public final static String AM_TEXT = "AM";
	public final static String PM_TEXT = "PM";

	public final static String TEXTBOX_TIME_PICKER_STYLE = "textBoxTimePicker";
	public final static String LISTBOX_TIME_PICKER_STYLE = "listBoxTimePicker";
	
	public final static String TEXTBOX_HOURS_STYLE = StyleHelper.buildCompound( TEXTBOX_TIME_PICKER_STYLE, "hours" );
	public final static String TEXTBOX_MINUTES_STYLE = StyleHelper.buildCompound( TEXTBOX_TIME_PICKER_STYLE, "minutes" );
	public final static String TEXTBOX_SECONDS_STYLE = StyleHelper.buildCompound( TEXTBOX_TIME_PICKER_STYLE, "seconds" );
	public final static String TEXTBOX_AM_PM_STYLE = StyleHelper.buildCompound( TEXTBOX_TIME_PICKER_STYLE, "amPm" );
	
	public final static String LISTBOX_HOURS_STYLE = StyleHelper.buildCompound( LISTBOX_TIME_PICKER_STYLE, "hours" );
	public final static String LISTBOX_MINUTES_STYLE = StyleHelper.buildCompound( LISTBOX_TIME_PICKER_STYLE, "minutes" );
	public final static String LISTBOX_SECONDS_STYLE = StyleHelper.buildCompound( LISTBOX_TIME_PICKER_STYLE, "seconds" );
	public final static String LISTBOX_AM_PM_STYLE = StyleHelper.buildCompound( LISTBOX_TIME_PICKER_STYLE, "amPm" );
}
