package rocket.client.widget.time;


import rocket.client.util.ObjectHelper;
import rocket.client.widget.HorizontalPanel;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract base class for any TimePicker.
 * 
 * This class defines setters/getters between time and hours/minutes/seconds.
 * @author Miroslav Pokorny (mP)
 */
public abstract class AbstractTimePicker extends Composite implements TimePicker {

	protected AbstractTimePicker(){
		this.createChangeListeners();
	}
	
	public long getTime(){
		final int hours = this.getHours() * TimePickerConstants.HOURS;
		final int minutes = this.getMinutes() * TimePickerConstants.MINUTES;
		final int seconds = this.getSeconds() * TimePickerConstants.SECONDS;
		return hours + minutes + seconds;
	}
	
	public void setTime( final long time ){
		final int hours = (int) (time / TimePickerConstants.HOURS) % TimePickerConstants.HOURS_IN_A_DAY;
		final int minutes = (int) ( time / TimePickerConstants.MINUTES ) %TimePickerConstants.MINUTES_IN_A_HOUR;
		final int seconds = (int) ( time / TimePickerConstants.SECONDS ) % TimePickerConstants.SECONDS_IN_A_MINUTE;
		
		this.setHours( hours );
		this.setMinutes( minutes );
		this.setSeconds( seconds );
	}
	
	/**
	 * THe container that houses the hours/minutes/seconds widgets.
	 */
	private HorizontalPanel horizontalPanel;
	
	protected HorizontalPanel getHorizontalPanel(){
		ObjectHelper.checkNotNull("field:horizontalPanel", horizontalPanel);
		return horizontalPanel;
	}
	protected boolean hasHorizontalPanel(){
		return null != horizontalPanel;
	}
	protected void setHorizontalPanel(final HorizontalPanel horizontalPanel){
		ObjectHelper.checkNotNull("parameter:horizontalPanel", horizontalPanel);
		this.horizontalPanel = horizontalPanel;
	}


	/**
	 * This listbox contains either AM or PM.
	 */
	private ListBox amPmListBox;
	
	protected ListBox getAmPmListBox(){
		ObjectHelper.checkNotNull("field:amPmListBox", this.amPmListBox );
		return this.amPmListBox;
	}
	protected void setAmPmListBox( final ListBox amPmListBox ){
		ObjectHelper.checkNotNull("parameter:amPmListBox", amPmListBox );
		this.amPmListBox = amPmListBox;
	}
	
	protected ListBox createAmPmListBox(){
		WidgetHelper.checkNotAlreadyCreated( "amPmListBox", null != this.amPmListBox);
		
		final ListBox listBox = new ListBox();
		listBox.addItem( TimePickerConstants.AM_TEXT );
		listBox.addItem( TimePickerConstants.PM_TEXT );
		this.setAmPmListBox( listBox );
		return listBox;
	}
	
	public boolean is24HourMode(){
		return ! this.getAmPmListBox().isVisible();
	}
	
	public void set24HourMode( final boolean new24HourMode ){
		final boolean in24HourMode = this.is24HourMode();
		if( in24HourMode != new24HourMode ){
			final int hours = this.getHours();
			this.getAmPmListBox().setVisible( ! new24HourMode );// hide if 24 / else in 12 show.
			this.setHours( hours );
		}//if
	}
	
	// LISTENERS :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	private ChangeListenerCollection changeListeners;
	
	protected ChangeListenerCollection getChangeListeners(){
		ObjectHelper.checkNotNull("field:changeListeners", changeListeners );
		return this.changeListeners;
	}
	protected void setChangeListeners(final ChangeListenerCollection changeListeners ){
		ObjectHelper.checkNotNull("parameter:changeListeners", changeListeners );
		this.changeListeners = changeListeners;
	}
	
	public void addChangeListener( final ChangeListener changeListener ){
		ObjectHelper.checkNotNull( "parameter:changeListener", changeListener);
		this.getChangeListeners().add( changeListener );
	}
	public void removeChangeListener( final ChangeListener changeListener ){
		ObjectHelper.checkNotNull( "parameter:changeListener", changeListener);
		this.getChangeListeners().remove( changeListener );
	}
	protected void createChangeListeners(){
		this.setChangeListeners( new ChangeListenerCollection() );
	}
	
	public String toString(){
		return super.toString() + ", horizontalPanel: " + horizontalPanel + ", amPmListBox: " + amPmListBox + ", changeListeners: "+ changeListeners;
	}
}
