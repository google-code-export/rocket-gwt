package rocket.client.widget.time;

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.widget.HorizontalPanel;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget allows time to be entered both as 12 Hours with AM/PM component or 24 Hour time.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * FIX ChangeListener not firing...Not sure when changeListeners fire for TextBoxs.
 * FIX Need to handle textbox rejection of keystrokes that make it contain an invalid value.
 */
public class TextBoxTimePicker extends AbstractTimePicker {

	public TextBoxTimePicker(){
		this.setWidget( this.createHorizontalPanel() );
		this.addStyleName( TimePickerConstants.TEXTBOX_TIME_PICKER_STYLE );
	}
	
	// HOURS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	public int getHours(){
		int hours = this.getTextBoxAsIntegerValue( this.getHoursTextBox() );
		
		if( false == this.is24HourMode() ){
			if( this.getAmPmListBox().getSelectedIndex() == 1 ){
				hours = hours + 12;
			}
		}
		return hours;
	}
	
	public void setHours( final int hours ){
		int newHours = hours;
		if( false == this.is24HourMode() ){
			int index = 0;
			if( hours > 12 ){
				index = 1;
				newHours = hours - 12;
			}
			
			this.getAmPmListBox().setSelectedIndex( index );			
		}
		this.getHoursTextBox().setText( String.valueOf( newHours ));
	}
	
	/**
	 * This textBox that contains the hours component of time.
	 */
	private TextBox hoursTextBox;
	
	protected TextBox getHoursTextBox(){
		ObjectHelper.checkNotNull("field:hoursTextBox", hoursTextBox);
		return hoursTextBox;
	}
	protected boolean hasHoursTextBox(){
		return null != hoursTextBox;
	}
	protected void setHoursTextBox(final TextBox hoursTextBox){
		ObjectHelper.checkNotNull("parameter:hoursTextBox", hoursTextBox);
		this.hoursTextBox = hoursTextBox;
	}
	
	protected TextBox createHoursTextBox(){
		final TextBox textBox = new TextBox();
		this.setHoursTextBox( textBox );
		textBox.addStyleName( TimePickerConstants.TEXTBOX_HOURS_STYLE );
		return textBox;
	}

	// MINUTES ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	public int getMinutes(){
		return this.getTextBoxAsIntegerValue( this.getMinutesTextBox() );
	}
	
	public void setMinutes( final int minutes ){
		PrimitiveHelper.checkBetween( "parameter:minutes", minutes, 0, TimePickerConstants.MINUTES_IN_A_HOUR );
		this.getMinutesTextBox().setText( String.valueOf( minutes ));
	}
	/**
	 * This textBox that contains the minutes component of time.
	 */
	private TextBox minutesTextBox;
	
	protected TextBox getMinutesTextBox(){
		ObjectHelper.checkNotNull("field:minutesTextBox", minutesTextBox);
		return minutesTextBox;
	}
	protected boolean hasMinutesTextBox(){
		return null != minutesTextBox;
	}
	protected void setMinutesTextBox(final TextBox minutesTextBox){
		ObjectHelper.checkNotNull("parameter:minutesTextBox", minutesTextBox);
		this.minutesTextBox = minutesTextBox;
	}	
	
	protected TextBox createMinutesTextBox(){
		WidgetHelper.checkNotAlreadyCreated( "minutesTextBox", hasMinutesTextBox());
		
		final TextBox textBox = this.createBoundedNumberTextBox( 0, TimePickerConstants.MINUTES_IN_A_HOUR );
		textBox.addStyleName( TimePickerConstants.TEXTBOX_MINUTES_STYLE );
		this.setMinutesTextBox( textBox );
		return textBox;
	}

	// SECONDS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	public int getSeconds(){
		return this.getTextBoxAsIntegerValue( this.getSecondsTextBox() );
	}
	
	public void setSeconds( final int seconds ){
		PrimitiveHelper.checkBetween( "parameter:seconds", seconds, 0, TimePickerConstants.SECONDS_IN_A_MINUTE );
		this.getSecondsTextBox().setText( String.valueOf( seconds ));
	}
	/**
	 * This textBox that contains the seconds component of time.
	 */
	private TextBox secondsTextBox;
	
	protected TextBox getSecondsTextBox(){
		ObjectHelper.checkNotNull("field:secondsTextBox", secondsTextBox);
		return secondsTextBox;
	}
	protected boolean hasSecondsTextBox(){
		return null != secondsTextBox;
	}
	protected void setSecondsTextBox(final TextBox secondsTextBox){
		ObjectHelper.checkNotNull("parameter:secondsTextBox", secondsTextBox);
		this.secondsTextBox = secondsTextBox;
	}
	
	protected TextBox createSecondsTextBox(){
		WidgetHelper.checkNotAlreadyCreated( "secondsTextBox", hasSecondsTextBox());
		
		final TextBox textBox = this.createBoundedNumberTextBox( 0, TimePickerConstants.SECONDS_IN_A_MINUTE );
		this.setSecondsTextBox( textBox );
		textBox.addStyleName( TimePickerConstants.TEXTBOX_SECONDS_STYLE );
		return textBox;
	}

	
	public boolean isSecondsVisible() {
		return this.getSecondsTextBox().isVisible();
	}

	public void setSecondsVisible(boolean secondsVisible) {
		this.getSecondsTextBox().setVisible( secondsVisible );
	}
	
	// HELPER ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	protected HorizontalPanel createHorizontalPanel(){
		WidgetHelper.checkNotAlreadyCreated( "horizontalPanel", hasHorizontalPanel());
		
		final HorizontalPanel panel = new HorizontalPanel();
		panel.add( this.createHoursTextBox() );
		panel.add( this.createMinutesTextBox() );
		panel.add( this.createSecondsTextBox() );
		panel.add( this.createAmPmListBox() );
		this.setHorizontalPanel( panel );
		return panel;
	}	

	protected int getTextBoxAsIntegerValue( final TextBox textBox ){
		ObjectHelper.checkNotNull( "parameter:textBox", textBox );
		return Integer.parseInt( textBox.getText() );
	}
	
	/**
	 * Factory method which creates a TextBox which only accepts a number value
	 * between the lowerBounds and upperBounds values.
	 * @param lowerBounds
	 * @param upperBounds
	 * @return The created TextBox widget.
	 */
	protected TextBox createBoundedNumberTextBox( final int lowerBounds, final int upperBounds ){
		final TextBoxTimePicker that = this;
		
		final TextBox textBox = new TextBox();
		textBox.addKeyboardListener( new KeyboardListenerAdapter(){
			public void onKeyPress(final Widget sender, final char c, final int modifier ){
				if( that.handleBoundedNumberTextBoxChange( textBox, lowerBounds, upperBounds )){
					that.getChangeListeners().fireChange( that );
				}
			}
		});
		return textBox;
	}
	
	/**
	 * Checks that the value held by the TextBox has not left the range between lowerBounds and upperBounds
	 * (both inclusive).
	 * If the value is outside the range the current event is cancelled.
	 * 
	 * @param textBox
	 * @param lowerBounds
	 * @param upperBounds
	 * @retun Returns false to indicate the event was cancelled;
	 */
	protected boolean handleBoundedNumberTextBoxChange( final TextBox textBox, final int lowerBounds, final int upperBounds ){
		ObjectHelper.checkNotNull("parameter:textBox", textBox );
	
		boolean cancel = false;
		try{
			final int value = Integer.parseInt( textBox.getText() );
			if( value < lowerBounds || value >= upperBounds ){
				cancel = true;
			}
		} catch ( final NumberFormatException badNumber ){
			cancel = true;
		}
		if( cancel ){
			textBox.cancelKey();
		}
		return !cancel;
	}
	
	public String toString(){
		return super.toString() + ", hoursTextBox: " + hoursTextBox + 
		", minutesTextBox: " + this.minutesTextBox +
		", secondsTextBox: " + this.secondsTextBox;
	}
}
