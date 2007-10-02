/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.widget.client.viewport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.EventPreviewAdapter;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseOverEvent;
import rocket.event.client.MouseUpEvent;
import rocket.selection.client.Selection;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.widget.client.CompositeWidget;
import rocket.widget.client.DivPanel;
import rocket.widget.client.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * A ViewPort is a widget that is a widget that consists of many tiles. The user
 * may scroll around the viewport by dragging its contents in any direction with
 * the widget automatically updating itself creating tiles using the
 * {@link #createTile0(int, int)} method.
 * </p>
 * 
 * <p>
 * Usage and performance tips.
 * <ul>
 * <li>The more tiles that appear inthe viewport the slower the update when
 * scrolling.</li>
 * <li>After updating any origin, or tile size property call {@link #redraw}.</li>
 * <li>The larger each gutter the better tile performance is at a cost of more
 * memory usage.A good starting value is one tile</li>
 * <li>The {@link #createTile0(int, int)} does not need to attempt to cache it
 * is only called when required.</li>
 * </p>
 * 
 * <h3>More about Tile caching</h3>
 * <p>
 * A cache is kept of all tiles that are within the viewable viewport as well as
 * the tiles that surround and touch a visible tile. This means that if the
 * viewport scrolls to the left 1 pixel and then scrolls back to the right no
 * new tile is created. The actual size of the gutter (in pixels) may be
 * controlled by overriding the following methods
 * <ul>
 * <li>{@link getLeftGutter()}</li>
 * <li>{@link getRightGutter()}</li>
 * <li>{@link getTopGutter()}</li>
 * <li>{@link getBottomGutter()}</li>
 * </li>
 * </p>
 * 
 * @author Miroslav Pokorny
 */
abstract public class Viewport extends CompositeWidget {

	public Viewport() {
		super();
	}


	protected void beforeCreateWidget() {
		super.beforeCreateWidget();
		
		this.setViewportListeners(this.createViewportListeners());
		this.getEventListenerDispatcher().addMouseEventListener( new MouseEventAdapter(){
			public void onMouseDown( final MouseDownEvent event ){
				Viewport.this.handleMouseDown( event );
			}
		});
	}
	
	protected Widget createWidget() {
		final TileDivPanel widget = this.createInnerPanel();
		this.setInnerPanel(widget);

		final SimplePanel simplePanel = new SimplePanel();
		simplePanel.setWidget(widget);

		final Element element = simplePanel.getElement();
		InlineStyle.setString(element, StyleConstants.OVERFLOW, "hidden");
		InlineStyle.setString(element, StyleConstants.POSITION, "relative"); // required
		// to
		// make
		// the
		// inner
		// div
		// not
		// overflow.
		return simplePanel;
	}

	protected String getInitialStyleName(){
	  return Constants.VIEWPORT_STYLE;
	}
	
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.MOUSE_DOWN;
	}

	protected Element getSunkEventsTarget() {
		return this.getInnerPanel().getElement();
	}

	/**
	 * Called when this viewport is attached to the DOM.
	 */
	protected void onAttach() {
		super.onAttach();
		this.redraw();
	}

	/**
	 * This method is fired whenever a mouse down occurs.
	 * 
	 * @param event
	 */
	protected void handleMouseDown(final MouseDownEvent event) {
		Selection.clearAnySelectedText();
		Selection.disableTextSelection();

		final Widget tile = this.getTileContainingElement( event.getTarget() );		
		
		final BeforeViewportDragStartEvent beforeDragStartedEvent = new BeforeViewportDragStartEvent();
		beforeDragStartedEvent.setTile(tile);
		beforeDragStartedEvent.setViewport(this);

		final ViewportListenerCollection listeners = this.getViewportListeners();
		listeners.fireBeforeDragStarted(beforeDragStartedEvent);
		if ( false == beforeDragStartedEvent.isCancelled() ) {

			final ViewportEventPreviewAdapter dragger = this.createDraggingEventPreview();
			dragger.setMouseX( event.getPageX());
			dragger.setMouseY( event.getPageY() );
			
			dragger.setOriginX(this.getOriginX());
			dragger.setOriginY(this.getOriginY());
			this.setDraggingEventPreview(dragger);

			dragger.install();

			final ViewportDragStartEvent dragStartedEvent = new ViewportDragStartEvent();
			dragStartedEvent.setViewport(this);
			dragStartedEvent.setTile(tile );
			
			listeners.fireDragStarted(dragStartedEvent);
		} else {
			final CancelledViewportDragStartEvent cancelled = new CancelledViewportDragStartEvent();
			cancelled.setTile(tile);
			cancelled.setViewport( this );
			
			listeners.fireCancelledDragStart(cancelled);
		}
		event.cancelBubble( true );
		event.stop();
	}

	/**
	 * The EventPreview object that monitors dragging of a tile.
	 */
	private ViewportEventPreviewAdapter draggingEventPreview;

	protected ViewportEventPreviewAdapter getDraggingEventPreview() {
		ObjectHelper.checkNotNull("field:draggingEventPreview", draggingEventPreview);
		return this.draggingEventPreview;
	}

	protected boolean hasDraggingEventPreview() {
		return null != this.draggingEventPreview;
	}

	protected void setDraggingEventPreview(final ViewportEventPreviewAdapter draggingEventPreview) {
		ObjectHelper.checkNotNull("parameter:draggingEventPreview", draggingEventPreview);
		this.draggingEventPreview = draggingEventPreview;
	}

	protected void clearDraggingEventPreview() {
		this.draggingEventPreview = null;
	}

	protected ViewportEventPreviewAdapter createDraggingEventPreview() {
		final ViewportEventPreviewAdapter draggingEventPreview = new ViewportEventPreviewAdapter();
		this.setDraggingEventPreview(draggingEventPreview);
		return draggingEventPreview;
	}

	/**
	 * This inner class is an adapter as well as captures state about the mouse
	 * at the start of the drag.
	 */
	private class ViewportEventPreviewAdapter extends EventPreviewAdapter {
		protected void handleMouseMoveEvent( final MouseMoveEvent event ){
			Viewport.this.handleDragMouseMove( event );
		}
		protected void handleMouseOutEvent( final MouseOutEvent event ){
			Viewport.this.handleDragMouseOut( event );
		}
		protected void handleMouseOverEvent( final MouseOverEvent event ){
			Viewport.this.handleDragMouseOver( event );
		}
		
		protected void handleMouseUpEvent( final MouseUpEvent event ){
			Viewport.this.handleDragMouseUp( event );
		}

		/**
		 * The x coordinates of the origin when the drag started
		 */
		private int originX;

		protected int getOriginX() {
			return this.originX;
		}

		protected void setOriginX(final int originX) {
			this.originX = originX;
		}

		/**
		 * The y coordinates of the origin when the drag started.
		 */
		private int originY;

		protected int getOriginY() {
			return this.originY;
		}

		protected void setOriginY(final int originY) {
			this.originY = originY;
		}

		/**
		 * The x coordinates of the mouse when the drag started
		 */
		private int mouseX;

		protected int getMouseX() {
			return this.mouseX;
		}

		protected void setMouseX(final int mouseX) {
			this.mouseX = mouseX;
		}

		/**
		 * The y coordinates of the mouse when the drag started
		 */
		private int mouseY;

		protected int getMouseY() {
			return this.mouseY;
		}

		protected void setMouseY(final int mouseY) {
			this.mouseY = mouseY;
		}
	}

	/**
	 * This method is called when mouse dragging is active and the user lets go
	 * of the mouse.
	 * 
	 * @param event
	 *            The event
	 */
	protected void handleDragMouseUp(final MouseUpEvent event) {
		this.getDraggingEventPreview().uninstall();
		this.clearDraggingEventPreview();

		Selection.enableTextSelection();

		final Widget tile = this.getTileContainingElement(event.getTarget() );
		
		final ViewportDragStopEvent viewportDragStopEvent = new ViewportDragStopEvent();
		viewportDragStopEvent.setTile(tile);
		viewportDragStopEvent.setViewport( this );
		
		this.getViewportListeners().fireMoveStopped(viewportDragStopEvent);
		
		event.cancelBubble( true );
	}

	/**
	 * This method is called when mouse dragging is active and the mouse moves
	 * outside the viewport area.
	 * 
	 * Currently this action does nothing, the viewport is not moved nor is
	 * dragging cancelled.
	 * 
	 * @param event
	 *            The event
	 */
	protected void handleDragMouseOut(final MouseOutEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		final Element element = this.getElement();
		final Element eventTarget = event.getTarget();
		if (false == DOM.isOrHasChild(element, eventTarget)) {
			this.addStyleName( this.getOutOfBoundsStyle() );
		}
		
		event.cancelBubble( true );
	}
	
	/**
	 * This method is called when a dragging mouse moves back over the viewport
	 * 
	 * @param event
	 *            The event
	 */
	protected void handleDragMouseOver(final MouseOverEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		final Element element = this.getElement();
		final Element eventTarget = event.getTarget();
		if (DOM.isOrHasChild(element, eventTarget)) {
			this.removeStyleName(this.getOutOfBoundsStyle());
		}
		event.cancelBubble( true );
	}

	protected String getOutOfBoundsStyle(){
		return Constants.VIEWPORT_OUT_OF_BOUNDS_STYLE;
	}

	
	/**
	 * This method is called each time a dragging mouse is moved within the
	 * viewport.
	 */
	protected void handleDragMouseMove(final MouseMoveEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final ViewportEventPreviewAdapter previewer = this.getDraggingEventPreview();
			final int originalMouseX = previewer.getMouseX(); 
			final int currentMouseX = event.getPageX();

			final int originalMouseY = previewer.getMouseY();
			final int currentMouseY = event.getPageY();
			
			// mouse has not moved do nothing.
			final int deltaX = currentMouseX - originalMouseX;
			final int deltaY = currentMouseY - originalMouseY;
			if ( deltaX == 0 && deltaY == 0 ) {
				break;
			}
			
			final Widget tile = this.getTileContainingElement( event.getTarget() );
			
			// give viewportListeners a chance to veto the drag move
			final BeforeViewportMoveEvent beforeMoveEvent = new BeforeViewportMoveEvent();
			beforeMoveEvent.setDeltaX( deltaX );
			beforeMoveEvent.setDeltaY( deltaY );
			beforeMoveEvent.setTile(tile);
			beforeMoveEvent.setViewport(this);
			
			final ViewportListenerCollection listeners = this.getViewportListeners();
			listeners.fireBeforeMove( beforeMoveEvent );
			if ( beforeMoveEvent.isCancelled() ) {
				
				final IgnoredViewportMoveEvent ignored = new IgnoredViewportMoveEvent();
				ignored.setDeltaX( deltaX );
				ignored.setDeltaY( deltaY );
				ignored.setTile(tile);
				ignored.setViewport(this);
				
				listeners.fireIgnoredMove(ignored);
				break;
			}

			final int originX = originalMouseX - currentMouseX + previewer.getOriginX();
			final int originY = originalMouseY - currentMouseY + previewer.getOriginY();
			this.setOriginX(originX);
			this.setOriginY(originY);

			this.redraw();

			final ViewportMoveEvent moveEvent = new ViewportMoveEvent();
			moveEvent.setDeltaX( deltaX );
			moveEvent.setDeltaY( deltaY );
			moveEvent.setTile(tile);
			moveEvent.setViewport(this);
			
			listeners.fireMoved( moveEvent );
			break;
		}
		
		event.cancelBubble( true );
	}

	/**
	 * This method is called each time the viewport needs to update or repaint
	 * itself. Anytime the origin, width or height is changed this method should
	 * be invoked.
	 */
	public void redraw() {
		this.removeTilesThatAreOutOfView();
		this.createMissingTiles();
		this.updateInnerPanelOffset();
	}

	/**
	 * Visits all cells that belong to the inner panel removing those that are
	 * not within the viewport
	 */
	protected void removeTilesThatAreOutOfView() {
		final Iterator iterator = this.getInnerPanel().iterator();
		while (iterator.hasNext()) {
			final Widget tile = (Widget) iterator.next();
			if (this.isOutOfView(tile)) {
				iterator.remove();
			}
		}
	}

	/**
	 * Tests if the given widget should be removed from the inner area of the
	 * viewport. The actual coordinates of the tiles that are kept are defined
	 * by the following methods:
	 * <ul>
	 * <li>{@link #getLeftBoundary()}></li>
	 * <li>{@link #getRightBoundary()}></li>
	 * <li>{@link #getTopBoundary()}></li>
	 * <li>{@link #getBottomBoundary()}></li>
	 * </ul>
	 * 
	 * @param tile
	 *            The tile being tested.
	 * @return True if the tile should be removed and lost
	 */
	protected boolean isOutOfView(final Widget tile) {
		ObjectHelper.checkNotNull("parameter:tile", tile);

		boolean remove = false;

		while (true) {
			final int x = this.getTileLeft(tile);
			if (x < this.getLeftBoundary()) {
				remove = true;
				break;
			}
			if (x > this.getRightBoundary()) {
				remove = true;
				break;
			}

			final int y = this.getTileTop(tile);
			if (y < this.getTopBoundary()) {
				remove = true;
				break;
			}
			if (y > this.getBottomBoundary()) {
				remove = true;
				break;
			}

			remove = false;
			break;
		}

		return remove;
	}

	protected int getLeftBoundary() {
		return this.getOriginX() - this.getLeftGutter();
	}

	protected int getLeftGutter() {
		return this.getTileWidth() - 1;
	}

	protected int getRightBoundary() {
		return this.getOriginX() + this.getOffsetWidth() + this.getRightGutter();
	}

	protected int getRightGutter() {
		return this.getTileWidth() - 1;
	}

	protected int getTopBoundary() {
		return this.getOriginY() - this.getTopGutter();
	}

	protected int getTopGutter() {
		return this.getTileHeight() - 1;
	}

	protected int getBottomBoundary() {
		return this.getOriginY() + this.getOffsetHeight() + this.getBottomGutter();
	}

	protected int getBottomGutter() {
		return this.getTileHeight() - 1;
	}

	/**
	 * Loops thru all the cells that are now visible creating tiles for any
	 * cells that require a tile.
	 */
	protected void createMissingTiles() {
		final TileDivPanel innerPanel = this.getInnerPanel();

		final int left = this.getLeftBoundary();
		final int right = this.getRightBoundary();
		final int tileWidth = this.getTileWidth();
		int columnStart = left / tileWidth;
		int columnEnd = right / tileWidth;

		final int top = this.getTopBoundary();
		final int bottom = this.getBottomBoundary();
		final int tileHeight = this.getTileHeight();
		int rowStart = top / tileHeight;
		int rowEnd = bottom / tileHeight;

		// loop thru all cells and try and locate a tile or create...
		for (int row = rowStart; row < rowEnd; row++) {
			for (int column = columnStart; column < columnEnd; column++) {
				Widget tile = innerPanel.getTile(column, row);

				// tile not found request a new one...
				if (null == tile) {
					tile = this.createTile(column, row);
					innerPanel.add(tile);
				}
			}
		}
	}

	/**
	 * This factory method is called each tile a tile needs to be created.
	 * 
	 * Note that the location of the tile is passed as a column row coordinate
	 * and not actual pixels coordinates.
	 * 
	 * @param column
	 *            The column that the tile appears in.
	 * @param row
	 *            The row that the tile appears in.
	 * @return
	 */
	protected Widget createTile(final int column, final int row) {
		final Widget tile = this.createTile0(column, row);

		tile.addStyleName(this.getTileStyle());

		InlineStyle.setString(tile.getElement(), StyleConstants.POSITION, "absolute");
		this.setTileLeft(tile, column * this.getTileWidth());
		this.setTileTop(tile, row * this.getTileHeight());

		return tile;
	}

	protected String getTileStyle(){
		return Constants.VIEWPORT_TILE_STYLE;		
	}
	
	/**
	 * Sub classes must override this method to return the appropriate widget
	 * for the given tile coordinates
	 * 
	 * @param column
	 *            The column
	 * @param row
	 *            The row
	 * @return A new tile.
	 */
	abstract protected Widget createTile0(int column, int row);

	protected void updateInnerPanelOffset() {
		final Element element = this.getInnerPanel().getElement();
		InlineStyle.setInteger(element, StyleConstants.LEFT, -Constants.X_OFFSET - this.getOriginX(), CssUnit.PX);
		InlineStyle.setInteger(element, StyleConstants.TOP, -Constants.Y_OFFSET - this.getOriginY(), CssUnit.PX);
	}

	protected int getTileLeft(final Widget tile) {
		return ObjectHelper.getInteger(tile.getElement(), Constants.TILE_LEFT_ATTRIBUTE);
	}

	protected int getTileTop(final Widget tile) {
		return ObjectHelper.getInteger(tile.getElement(), Constants.TILE_TOP_ATTRIBUTE);
	}

	protected void setTileLeft(final Widget tile, final int x) {
		final Element element = tile.getElement();
		InlineStyle.setInteger(element, StyleConstants.LEFT, Constants.X_OFFSET + x, CssUnit.PX);
		ObjectHelper.setInteger(element, Constants.TILE_LEFT_ATTRIBUTE, x);
	}

	protected void setTileTop(final Widget tile, final int y) {
		final Element element = tile.getElement();
		InlineStyle.setInteger(element, StyleConstants.TOP, Constants.Y_OFFSET + y, CssUnit.PX);
		ObjectHelper.setInteger(element, Constants.TILE_TOP_ATTRIBUTE, y);
	}

	/**
	 * A div panel is used to host the contents of a viewport.
	 */
	private TileDivPanel innerPanel;

	protected TileDivPanel getInnerPanel() {
		ObjectHelper.checkNotNull("file:innerPanel", innerPanel);
		return this.innerPanel;
	}

	protected void setInnerPanel(final TileDivPanel innerPanel) {
		ObjectHelper.checkNotNull("parameter:innerPanel", innerPanel);
		this.innerPanel = innerPanel;
	}

	protected TileDivPanel createInnerPanel() {
		final TileDivPanel panel = new TileDivPanel();
		final Element element = panel.getElement();
		
		InlineStyle.setString(element, StyleConstants.OVERFLOW, "hidden");
		
		InlineStyle.setString(element, StyleConstants.POSITION, "relative");
		InlineStyle.setInteger(element, StyleConstants.TOP, 0, CssUnit.PX);
		InlineStyle.setInteger(element, StyleConstants.LEFT, 0, CssUnit.PX);

		InlineStyle.setInteger(element, StyleConstants.WIDTH, Short.MAX_VALUE, CssUnit.PX);
		InlineStyle.setInteger(element, StyleConstants.HEIGHT, Short.MAX_VALUE, CssUnit.PX);
		return panel;
	}

	/**
	 * This DivPanel includes a helper method that adds the ability to retrieve
	 * a tile given its column and row coordinates.
	 */
	class TileDivPanel extends DivPanel {

		TileDivPanel() {
			this.setWidgets(this.createWidgets());
		}

		public Widget getTile(final int column, final int row) {
			return (Widget) this.getWidgets().get(this.buildKey(column, row));
		}

		public void add(final Widget widget) {
			super.add(widget);

			final String key = this.buildKey(widget);
			this.getWidgets().put(key, widget);
		}

		public boolean remove(final int index) {
			final Widget widget = this.get(index);
			this.getWidgets().remove(this.buildKey(widget));
			return super.remove(index);
		}

		protected String buildKey(final Widget widget) {
			return this.buildKey(Viewport.this.getTileLeft(widget) / Viewport.this.getTileWidth(), Viewport.this.getTileTop(widget)
					/ Viewport.this.getTileHeight());
		}

		protected String buildKey(final int column, final int row) {
			return "" + column + "," + row;
		}

		Map widgets;

		Map getWidgets() {
			return widgets;
		}

		void setWidgets(final Map widgets) {
			this.widgets = widgets;
		}

		Map createWidgets() {
			return new HashMap();
		}
	}

	/**
	 * The x coordinate of the origin of this viewport.
	 */
	private int originX;

	public int getOriginX() {
		return this.originX;
	}

	public void setOriginX(final int originX) {
		this.originX = originX;
	}

	/**
	 * The y coordinate of the origin of this viewport.
	 */
	private int originY;

	public int getOriginY() {
		return this.originY;
	}

	public void setOriginY(final int originY) {
		this.originY = originY;
	}

	/**
	 * The common width of each tile in pixels.
	 */
	private int tileWidth;

	public int getTileWidth() {
		return this.tileWidth;
	}

	public void setTileWidth(final int tileWidth) {
		this.tileWidth = tileWidth;
	}

	/**
	 * The common height of each tile in pixels.
	 */
	private int tileHeight;

	public int getTileHeight() {
		return this.tileHeight;
	}

	public void setTileHeight(final int tileHeight) {
		this.tileHeight = tileHeight;
	}

	/**
	 * A collection of viewportListeners which are registered to receive
	 * viewport events.
	 */
	private ViewportListenerCollection viewportListeners;

	protected ViewportListenerCollection getViewportListeners() {
		ObjectHelper.checkNotNull("field:viewportListeners", viewportListeners);
		return this.viewportListeners;
	}

	protected void setViewportListeners(final ViewportListenerCollection viewportListeners) {
		ObjectHelper.checkNotNull("parameter:viewportListeners", viewportListeners);
		this.viewportListeners = viewportListeners;
	}

	protected ViewportListenerCollection createViewportListeners() {
		return new ViewportListenerCollection();
	}

	public void addViewportListener(final ViewportListener viewportListeners) {
		ObjectHelper.checkNotNull("parameter:viewportListeners", viewportListeners);

		this.getViewportListeners().add(viewportListeners);
	}

	public boolean removeViewportListener(final ViewportListener viewportListeners) {
		ObjectHelper.checkNotNull("parameter:viewportListeners", viewportListeners);
		return this.getViewportListeners().remove(viewportListeners);
	}
	
	
	private Widget getTileContainingElement( final Element element ){
		final Iterator tiles = this.getInnerPanel().iterator();
		return WidgetHelper.findWidget(element, tiles );
	}
}
