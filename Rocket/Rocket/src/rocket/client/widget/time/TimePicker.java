package rocket.client.widget.time;

/**
 * Defines an interface for a widget that allows a user to enter a time.
 * Hours are 24 hour.
 * 
 * @author Miroslav Pokorny (mP)
 */
public interface TimePicker {
	
	long getTime();
	
	void setTime( long time );
	
	int getHours();
	
	void setHours( int hours );
	
	int getMinutes();
	
	void setMinutes( int minutes );
	
	int getSeconds();
	
	void setSeconds( int seconds );
	
	boolean isSecondsVisible();
	
	void setSecondsVisible( boolean secondsVisible );
}
