package rocket.widget.client.viewport;

/**
 * This interface defines a number of methods that allow fine grained control over the viewport
 * and attempts by the user to scroll the viewport by drag its contents.
 * @author Miroslav Pokorny
 */
public interface ViewportListener {
	/**
	 * This method is invoked before dragging starts. It gives an opportunity for listeners to
	 * potentially cancel any drag.
	 * @param viewport The viewport
	 * @return Returning true cancels the attempt to drag.
	 */
	boolean onBeforeDragStarted( Viewport viewport );
	
	
	/**
	 * This method is invoked whenever the user starts a drag.
	 * @param viewport The viewport
	 */
	void onDragStarted( Viewport viewport );

	/**
	 * This method is invoked each time the user moves or drags a tile.
	 * @param viewport The viewport
	 * @return Returning true cancels or ignores the pending scrolling update.
	 */
	boolean onBeforeDragMove( Viewport viewport );
	
	/**
	 * Each time the viewport moves this method is called.
	 * @param viewport
	 */
	void onDragMoved( Viewport viewport );
	
	/**
	 * This method is invoked whenever the user stops a drag.
	 * @param viewport The viewport
	 */
	void onDragStopped( Viewport viewport );
	
}
