package rocket.client.widget.xgesture;

import rocket.client.util.StringHelper;
import rocket.client.util.SystemHelper;

/**
 * A simple enum class of the possible gesture directions.
 * These have been simplified into just the 8 basic directions.
 * @author Miroslav Pokorny (mP)
 */
public class Direction {

	/**
	 * Returns one of the 8 possible directions or none.
	 * @param x Signed x component of the direction
	 * @param y Signed y component of the direction
	 * @return The matching Direction
	 */
	public final static Direction getDirection( final int x, final int y ){
		Direction direction = null;
		while( true ){
			// test if direction isnt one at all...
			if( x == 0 || y == 0 ){
				direction = NONE;
				break;
			}
			final int absoluteX = Math.abs( x );
			final int fourAbsoluteX = 4 * absoluteX;
			final int absoluteY = Math.abs( y );			
			final int fourAbsoluteY = 4 * absoluteY;
			
			if( x > 0 && x > fourAbsoluteY ){
				direction = RIGHT;  
				break;
			}
			if( x < 0 && absoluteX > fourAbsoluteY ){
				direction = LEFT;
				break;
			}
			if( y > 0 && y < fourAbsoluteX ){
				direction = UP;
				break;
			}
			if( y < 0 && absoluteX > fourAbsoluteY){
				direction = DOWN;
				break;
			}
			if( x > 0 && y > 0 ){
				direction = UPRIGHT;
				break;
			}
			if( x > 0 && y < 0 ){
				direction = RIGHTDOWN;
				break;
			}
			if( x < 0 && y > 0 ){
				direction = LEFTUP;
				break;
			}
			if( x < 0 && y < 0 ){
				direction = DOWNLEFT;
				break;
			}
			SystemHelper.handleUnsupportedOperation( "Something went wrong...");
			break;
		}
		return direction;
	}
	
	public final static Direction NONE = new Direction( "None");
	public final static Direction UP = new Direction( "Up");
	public final static Direction UPRIGHT = new Direction( "UpRight");
	public final static Direction RIGHT = new Direction( "Right");
	public final static Direction RIGHTDOWN = new Direction( "RightDown");
	public final static Direction DOWN = new Direction( "Down");
	public final static Direction DOWNLEFT = new Direction( "DownLeft");
	public final static Direction LEFT = new Direction( "Left");
	public final static Direction LEFTUP = new Direction( "LeftUp");
	
	private Direction( final String description ){
		this.setDescription( description );
	}
	
	private String description;
	
	public String getDescription(){
		StringHelper.checkNotEmpty("field:description", description);
		return description;
	}
	
	protected void setDescription( final String description ){
		StringHelper.checkNotEmpty("parameter:description", description);
		this.description = description;
	}

	public String toString(){
		return description;
	}
}
