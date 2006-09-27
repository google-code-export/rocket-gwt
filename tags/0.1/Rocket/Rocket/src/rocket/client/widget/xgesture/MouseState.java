package rocket.client.widget.xgesture;

import rocket.client.util.ObjectHelper;

/**
 * Represents a snapshot in time of the mouse.
 * @author Miroslav Pokorny (mP)
 *
 */
public class MouseState {
	/**
	 * The x coordinate of the mouse
	 */
	private int x;
	private boolean xSet;

	public int getX() {
		ObjectHelper.checkPropertySet( "x", this, xSet );
		return this.x;
	}

	public void setX(final int x) {
		this.x = x;
		this.xSet = true;
	}

	/**
	 * The y coordinate of the mouse
	 */
	private int y;
	private boolean ySet;

	public int getY() {
		ObjectHelper.checkPropertySet( "y", this, ySet );
		return this.y;
	}

	public void setY(final int y) {
		this.y = y;
		this.ySet = true;
	}
	
	private boolean leftButton;
	private boolean leftButtonSet;
	
	public boolean isLeftButton(){
		ObjectHelper.checkPropertySet("leftButton", this, this.leftButtonSet);
		return this.leftButton;
	}
	public void setLeftButton( final boolean leftButton ){
		this.leftButton = leftButton;
		this.leftButtonSet = true;
	}
	private boolean middleButton;
	private boolean middleButtonSet;
	
	public boolean isMiddleButton(){
		ObjectHelper.checkPropertySet("middleButton", this, this.middleButtonSet);
		return this.middleButton;
	}
	public void setMiddleButton( final boolean middleButton ){
		this.middleButton = middleButton;
		this.middleButtonSet = true;
	}
	private boolean rightButton;
	private boolean rightButtonSet;
	
	public boolean isRightButton(){
		ObjectHelper.checkPropertySet("rightButton", this, this.rightButtonSet);
		return this.rightButton;
	}
	public void setRightButton( final boolean rightButton ){
		this.rightButton = rightButton;
		this.rightButtonSet = true;
	}
	
	public String toString(){
		return super.toString() + ", x: " + x + ", y: " + y + ", leftButton: " + leftButton + 
		", middleButton: " + middleButton + ", rightButton: " + rightButton;
	}
}
