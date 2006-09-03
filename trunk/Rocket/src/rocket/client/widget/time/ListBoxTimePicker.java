package rocket.client.widget.time;

import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.widget.HorizontalPanel;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.ui.*;

/**
 * Convenient base class for any widget that uses ListBoxes to allow a user to select a time.
 * @author Miroslav Pokorny (mP)
 */
public class ListBoxTimePicker extends AbstractTimePicker {

	public ListBoxTimePicker(){
		this.setWidget( this.createHorizontalPanel() );
	}
	
	// HOURS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	public int getHours(){
		return this.getHoursListBox().getSelectedIndex();
	}
	
	public void setHours( final int hours ){		
		this.getHoursListBox().setSelectedIndex( hours );
	}
	
	/**
	 * This textBox that contains the hours component of time.
	 */
	private ListBox hoursListBox;
	
	protected ListBox getHoursListBox(){
		ObjectHelper.checkNotNull("field:hoursListBox", hoursListBox);
		return hoursListBox;
	}
	protected boolean hasHoursListBox(){
		return null != hoursListBox;
	}
	protected void setHoursListBox(final ListBox hoursListBox){
		ObjectHelper.checkNotNull("parameter:hoursListBox", hoursListBox);
		this.hoursListBox = hoursListBox;
	}
	
	protected ListBox createHoursListBox(){
		WidgetHelper.checkNotAlreadyCreated( "hoursListBox", this.hasHoursListBox() );
		
		final ListBox listBox = this.createListBox( 0, 12, 1 );
		this.setHoursListBox( listBox );
		return listBox;
	}
	
	// MINUTES ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	public int getMinutes(){
		return this.getListBoxSelectedItemAsIntegerValue( this.getMinutesListBox() );
	}
	
	public void setMinutes( final int minutes ){
		PrimitiveHelper.checkBetween( "parameter:minutes", minutes, 0, TimePickerConstants.MINUTES_IN_A_HOUR );
		
		final ListBox listBox = this.getMinutesListBox();
		final int count = listBox.getItemCount();
		int delta = Integer.MAX_VALUE;
		int newIndex = -1;
		
		for( int i = 0; i < count; i++ ){
			final int value = Integer.parseInt( listBox.getItemText( i ));
			final int delta0 = Math.abs( minutes - value );
			if( delta0 < delta ){
				delta = delta0;
				newIndex = i;
			}
		}
		
		listBox.setSelectedIndex( newIndex );
	}
	/**
	 * This textBox that contains the minutes component of time.
	 */
	private ListBox minutesListBox;
	
	protected ListBox getMinutesListBox(){
		ObjectHelper.checkNotNull("field:minutesListBox", minutesListBox);
		return minutesListBox;
	}
	protected boolean hasMinutesListBox(){
		return null != minutesListBox;
	}
	protected void setMinutesListBox(final ListBox minutesListBox){
		ObjectHelper.checkNotNull("parameter:minutesListBox", minutesListBox);
		this.minutesListBox = minutesListBox;
	}	
	
	protected ListBox createMinutesListBox(){
		WidgetHelper.checkNotAlreadyCreated( "minutesListBox", hasMinutesListBox());
		
		final ListBox listBox = this.createListBox( 0, TimePickerConstants.MINUTES_IN_A_HOUR, 1 );
		this.setMinutesListBox( listBox );
		return listBox;
	}

	// SECONDS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	public int getSeconds(){
		return this.getListBoxSelectedItemAsIntegerValue( this.getSecondsListBox() );
	}
	
	public void setSeconds( final int seconds ){
		PrimitiveHelper.checkBetween( "parameter:seconds", seconds, 0, TimePickerConstants.SECONDS_IN_A_MINUTE );
		
		this.getSecondsListBox().setSelectedIndex( seconds );
	}
	/**
	 * This textBox that contains the seconds component of time.
	 */
	private ListBox secondsListBox;
	
	protected ListBox getSecondsListBox(){
		ObjectHelper.checkNotNull("field:secondsListBox", secondsListBox);
		return secondsListBox;
	}
	protected boolean hasSecondsListBox(){
		return null != secondsListBox;
	}
	protected void setSecondsListBox(final ListBox secondsListBox){
		ObjectHelper.checkNotNull("parameter:secondsListBox", secondsListBox);
		this.secondsListBox = secondsListBox;
	}
	
	protected ListBox createSecondsListBox(){
		WidgetHelper.checkNotAlreadyCreated( "secondsListBox", hasSecondsListBox());
		
		final ListBox listBox = this.createListBox( 0, TimePickerConstants.SECONDS_IN_A_MINUTE, 1 );
		this.setSecondsListBox( listBox );
		return listBox;
	}

	
	public boolean isSecondsVisible() {
		return this.getSecondsListBox().isVisible();
	}

	public void setSecondsVisible(boolean secondsVisible) {
		this.getSecondsListBox().setVisible( secondsVisible );
	}
	
	// HELPER ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	protected HorizontalPanel createHorizontalPanel(){
		WidgetHelper.checkNotAlreadyCreated( "horizontalPanel", hasHorizontalPanel());
		
		final HorizontalPanel panel = new HorizontalPanel();
		panel.add( this.createHoursListBox() );
		panel.add( this.createMinutesListBox() );
		panel.add( this.createSecondsListBox() );
		panel.add( this.createAmPmListBox() );
		this.setHorizontalPanel( panel );
		return panel;
	}	
	
	/**
	 * Creates a ListBox and populates it with items.
	 * @param lowerBounds
	 * @param upperBounds
	 * @param step
	 * @return
	 */
	protected ListBox createListBox( final int lowerBounds, final int upperBounds, final int step ){
		final ListBox listBox = new ListBox();
		
		this.populateListBox( listBox, lowerBounds, upperBounds, step );
		return listBox;
	}
	
	protected void populateListBox(final ListBox listBox, final int lowerBounds, final int upperBounds, final int step ){
		ObjectHelper.checkNotNull( "parameter:listBox", listBox );
		
		listBox.clear();
		for( int i = lowerBounds; i < upperBounds; i = i + step ){
			listBox.addItem( String.valueOf( i ));
		}		
	}
	
	protected int getListBoxSelectedItemAsIntegerValue( final ListBox listBox ){
		ObjectHelper.checkNotNull( "parameter:listBox", listBox );
		return Integer.parseInt( listBox.getItemText( listBox.getSelectedIndex() ) );
	}
	
	public String toString(){
		return super.toString() + ", hoursListBox: " + hoursListBox + 
		", minutesListBox: " + this.minutesListBox +
		", secondsListBox: " + this.secondsListBox;
	}
}
