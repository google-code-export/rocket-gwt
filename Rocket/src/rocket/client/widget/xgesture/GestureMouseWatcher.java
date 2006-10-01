package rocket.client.widget.xgesture;

import java.util.ArrayList;
import java.util.List;

import rocket.client.collection.CyclicList;
import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;

/**
 * This mouse watcher watches all mouse events and records new CapturedGestureComponents as they happen 
 * 
 * @author Miroslav Pokorny (mP)
 */
public class GestureMouseWatcher implements EventPreview {

	public GestureMouseWatcher() {
		this.createCapturedGestureComponents();
	}

	public boolean onEventPreview(final Event event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final int eventType = DOM.eventGetType(event);
			if (Event.ONMOUSEMOVE == eventType ) {
				this.handleMouseMove( event );
				break;
			}
			if( Event.ONCLICK == eventType ){
				this.handleMouseClick( event );
				break;
			}
		}
		return true;
	}

	protected void handleMouseClick(final Event event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while( true ){
			if( false == this.hasCapturedGestureComponent() ){
				this.setCapturedGestureComponent( this.createCapturedGestureComponent(event ));
				break;
			}
		}
	}

	protected void handleMouseMove(final Event event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while( true ){
			if( false == this.hasCapturedGestureComponent() ){
				this.setCapturedGestureComponent( this.createCapturedGestureComponent(event ));
				break;
			}
			
			// calculate the distance of the mouse with the starting coordinates held by the most recent capturedGestureComponent.
			final int currentMouseX = DOM.eventGetScreenX( event );
			final int currentMouseY = DOM.eventGetScreenY( event );
			
			final CapturedGestureComponent capturedGestureComponent = this.getCapturedGestureComponent();
			final MouseState startMouseState = capturedGestureComponent.getStart();
			final int startMouseX = startMouseState.getX();
			final int startMouseY = startMouseState.getY();
			
			final int movementDeltaX = startMouseX - currentMouseX;
			final int movementDeltaY = startMouseY - currentMouseY;
			
			// calculate how far the mouse has moved from the start coordinates.
			final int distanceMovedSquared = movementDeltaX * movementDeltaX + movementDeltaY * movementDeltaY;
			final int delta = this.getDelta();
			final int deltaSquared = delta * delta;
			
			// if mouse hasnt moved enough do nothing...
			if( distanceMovedSquared > deltaSquared ){
				break;
			}
			
			// get the quadrant of what the capturedGestureComponent reports.
			
			// get the quadrant of this mouse movement
			break;
		}		
	}
	
	/**
	 * A CapturedGestureComponent which is being built.
	 */
	private CapturedGestureComponent capturedGestureComponent;
	
	protected CapturedGestureComponent getCapturedGestureComponent(){
		ObjectHelper.checkNotNull("field:capturedGestureComponent", capturedGestureComponent );
		return this.capturedGestureComponent;
	}
	
	protected boolean hasCapturedGestureComponent(){
		return null != this.capturedGestureComponent;
	}
	
	protected void setCapturedGestureComponent(final CapturedGestureComponent capturedGestureComponent ){
		ObjectHelper.checkNotNull("parameter:capturedGestureComponent", capturedGestureComponent );
		this.capturedGestureComponent = capturedGestureComponent;
	}
	
	/**
	 * Factory which creats an apparently complete CapturedGestureComponent.
	 * The start and end properties will be the equal for the moment. The end MouseState property will get updated by future events. 
	 * @param event
	 * @return
	 */
	protected CapturedGestureComponent createCapturedGestureComponent(final Event event ){
		ObjectHelper.checkNotNull("parameter:event", event);
		
		final CapturedGestureComponent record = new CapturedGestureComponent();
		
		record.setStart( this.createMouseState( event ));		
		final long now = System.currentTimeMillis();
		record.setStartTime( now );
		
		record.setEnd( this.createMouseState( event ));
		record.setEndTime( now );
		
		return record;
	}

	/**
	 * Factory method which creates a new MouseState object which represents a snapshot of the mouse as reported by the given event object.
	 * @param event
	 * @return
	 */
	protected MouseState createMouseState( final Event event ){
		ObjectHelper.checkNotNull("parameter:event", event);
		
		final MouseState mouseState = new MouseState();
		mouseState.setX( DOM.eventGetScreenX( event ));
		mouseState.setY( DOM.eventGetScreenY( event ));
		
		final int button = DOM.eventGetButton( event );
		mouseState.setLeftButton( button == Event.BUTTON_LEFT );
		mouseState.setMiddleButton( button == Event.BUTTON_MIDDLE );
		mouseState.setRightButton( button == Event.BUTTON_RIGHT );

		return mouseState;
	}
	
	/**
	 * The minimum distance the mouse must move before the watcher records a new direction move.
	 * 
	 * The value should not be too small otherwise too many events will be recorded.
	 */
	private int delta;

	public int getDelta() {
		PrimitiveHelper.checkGreaterThan( "field:delta", delta, 0 );
		return this.delta;
	}

	public void setDelta(final int delta) {
		PrimitiveHelper.checkGreaterThan( "parameter:delta", delta, 0 );
		this.delta = delta;
	}

	/**
	 * This list is filled with captured gesture components.
	 * 
	 * TODO write a List that is cyclic and includes a maximum capacity.
	 */
	private List capturedGestureComponents;

	public List getCapturedGestureComponents() {
		ObjectHelper.checkNotNull("field:capturedGestureComponents",
				capturedGestureComponents);
		return this.capturedGestureComponents;
	}

	public void setCapturedGestureComponents(
			final List capturedGestureComponents) {
		ObjectHelper.checkNotNull("parameter:capturedGestureComponents",
				capturedGestureComponents);
		this.capturedGestureComponents = capturedGestureComponents;
	}

	protected void createCapturedGestureComponents() {
		this.setCapturedGestureComponents(new CyclicList( 100 ));
	}
}
