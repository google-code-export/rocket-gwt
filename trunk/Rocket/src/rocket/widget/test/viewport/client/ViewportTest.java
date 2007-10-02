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
package rocket.widget.test.viewport.client;

import java.util.Date;

import rocket.browser.client.Browser;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.Colour;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.widget.client.viewport.BeforeViewportDragStartEvent;
import rocket.widget.client.viewport.BeforeViewportMoveEvent;
import rocket.widget.client.viewport.CancelledViewportDragStartEvent;
import rocket.widget.client.viewport.IgnoredViewportMoveEvent;
import rocket.widget.client.viewport.Viewport;
import rocket.widget.client.viewport.ViewportDragStartEvent;
import rocket.widget.client.viewport.ViewportDragStopEvent;
import rocket.widget.client.viewport.ViewportListener;
import rocket.widget.client.viewport.ViewportMoveEvent;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ViewportTest implements EntryPoint {
	/**
	 * For simplicity's sake the image width and height on the server are
	 * hardcoded.
	 */
	static final int IMAGE_WIDTH = 2000;

	static final int IMAGE_HEIGHT = 1000;

	/**
	 * The width of each individual tile.
	 */
	static final int TILE_WIDTH = 100;

	static final int TILE_HEIGHT = 100;

	static final int TILES_ACROSS = IMAGE_WIDTH / TILE_WIDTH;

	static final int TILES_DOWN = IMAGE_HEIGHT / TILE_HEIGHT;

	/**
	 * The initial zoom factor of the viewport.
	 */
	static final int ZOOM = 100;

	public void onModuleLoad() {
		final RootPanel rootPanel = RootPanel.get();

		final Counter beforeDragStarted = new Counter( "Before Drag start: ");
		rootPanel.add( beforeDragStarted );

		final Counter dragStarted = new Counter( "Drag started: ");
		rootPanel.add( dragStarted );
		
		final Counter cancelledDragStart = new Counter( "Cancelled drag: ");
		rootPanel.add( cancelledDragStart );
		
		final Counter beforeMoveStarted = new Counter( "Before move start: ");
		rootPanel.add( beforeMoveStarted );

		final Counter moved = new Counter( "Move started: ");
		rootPanel.add( moved );
		
		final Counter ignoredMove = new Counter( "Ignored move: ");
		rootPanel.add( ignoredMove );
		
		final Counter dragStopped = new Counter( "Move stopped: ");
		rootPanel.add( dragStopped );
				
		final Label coordinates = new Label();
		rootPanel.add(coordinates);

		rootPanel.add(new Label("Zoom"));

		final TextBox zoom = new TextBox();
		zoom.setText("" + 100);
		rootPanel.add(zoom);

		final ZoomingViewport viewport = new ZoomingViewport();
		viewport.setWidth(IMAGE_WIDTH / 2 + "px");
		viewport.setHeight(IMAGE_HEIGHT / 2 + "px");
		viewport.setTileWidth(TILE_WIDTH);
		viewport.setTileHeight(TILE_HEIGHT);
		viewport.setOriginX(IMAGE_WIDTH / 4);
		viewport.setOriginY(IMAGE_HEIGHT / 4);
		viewport.setZoom(ZOOM);

		final Element element = viewport.getElement();
		InlineStyle.setInteger(element, StyleConstants.BORDER_WIDTH, 2, CssUnit.PX);
		InlineStyle.setColour(element, StyleConstants.BORDER_COLOR, Colour.getColour("orange"));
		InlineStyle.setString(element, StyleConstants.BORDER_STYLE, "solid");

		rootPanel.add(viewport);

		viewport.addViewportListener(new ViewportListener() {

			public void onBeforeDragStart( final BeforeViewportDragStartEvent event){
				ObjectHelper.checkNotNull( "parameter:event", event );
				ObjectHelper.checkNotNull( "BeforeViewportDragStartEvent.tile", event.getTile() );
				ObjectHelper.checkNotNull( "BeforeViewportDragStartEvent.viewport", event.getViewport() );
				
				beforeDragStarted.increment();
			}

			public void onDragStart(final ViewportDragStartEvent event){
				ObjectHelper.checkNotNull( "parameter:event", event );
				ObjectHelper.checkNotNull( "ViewportDragStartEvent.tile", event.getTile() );
				ObjectHelper.checkNotNull( "ViewportDragStartEvent.viewport", event.getViewport() );
				
				dragStarted.increment();
			}
			
			public void onCancelledDragStart( CancelledViewportDragStartEvent event ){
				ObjectHelper.checkNotNull( "parameter:event", event );
				ObjectHelper.checkNotNull( "CancelledViewportDragStartEvent.tile", event.getTile() );
				ObjectHelper.checkNotNull( "CancelledViewportDragStartEvent.viewport", event.getViewport() );
				
				cancelledDragStart.increment();
			}
			
			public void onBeforeMove(final BeforeViewportMoveEvent event){
				ObjectHelper.checkNotNull( "parameter:event", event );
				ObjectHelper.checkNotNull( "BeforeViewportMoveEvent.tile", event.getTile() );
				ObjectHelper.checkNotNull( "BeforeViewportMoveEvent.viewport", event.getViewport() );
				
				beforeMoveStarted.increment();
			}
			
			public void onMoved(final ViewportMoveEvent event){
				ObjectHelper.checkNotNull( "parameter:event", event );
				ObjectHelper.checkNotNull( "ViewportMoveEvent.tile", event.getTile() );
				ObjectHelper.checkNotNull( "ViewportMoveEvent.viewport", event.getViewport() );
				
				System.out.println("CLIENT drag moving, timestamp: " + new Date());
				ViewportTest.this.updateCoordinatesLabel(coordinates, viewport);
				
				moved.increment();
			}

			public void onIgnoredMove(final IgnoredViewportMoveEvent event){
				ObjectHelper.checkNotNull( "parameter:event", event );
				ObjectHelper.checkNotNull( "IgnoredViewportMoveEvent.tile", event.getTile() );
				ObjectHelper.checkNotNull( "IgnoredViewportMoveEvent.viewport", event.getViewport() );
				
				System.out.println("CLIENT drag moving, timestamp: " + new Date());
				ViewportTest.this.updateCoordinatesLabel(coordinates, viewport);
				
				ignoredMove.increment();
			}
			
			public void onDragStop(final ViewportDragStopEvent event){
				ObjectHelper.checkNotNull( "parameter:event", event );
				ObjectHelper.checkNotNull( "ViewportDragStopEvent.tile", event.getTile() );
				ObjectHelper.checkNotNull( "ViewportDragStopEvent.viewport", event.getViewport() );
				
				dragStopped.increment();
			}			
		});

		this.updateCoordinatesLabel(coordinates, viewport);

		zoom.addKeyboardListener(new KeyboardListenerAdapter() {
			public void onKeyDown(final Widget sender, final char keyCode, final int modifiers) {
				if (keyCode == KeyboardListenerAdapter.KEY_ENTER) {
					viewport.setZoom(Integer.parseInt(zoom.getText()));
					viewport.redraw();
					ViewportTest.this.updateCoordinatesLabel(coordinates, viewport);
				}
			}
		});
	}
		
	class Counter extends Label{
		Counter( String prefix ){
			this.prefix = prefix;
			this.setText( prefix + "?");
		}
		
		String prefix;
		void increment(){
			counter++;
			
			this.setText( this.prefix + counter );
		}
		
		int counter;
	}

	class ZoomingViewport extends Viewport {

		protected Widget createTile0(final int column, final int row) {
			final Image image = new Image();
			final int x = column * TILE_WIDTH;
			final int y = row * TILE_HEIGHT;

			image.setUrl(Browser.getContextPath() + "/tiles?" + Constants.X + '=' + x + '&' + Constants.Y + '=' + y + '&' + Constants.WIDTH
					+ '=' + TILE_WIDTH + '&' + Constants.HEIGHT + '=' + TILE_HEIGHT + '&' + Constants.ZOOM + '=' + 100);

			ViewportTest.log("retrieving new tile at " + x + ", " + y);
			return image;
		}

		private int zoom = 100;

		int getZoom() {
			return zoom;
		}

		void setZoom(final int zoom) {
			final int oldZoom = this.zoom;
			this.zoom = zoom;

			this.zoomAdjust(oldZoom / 100f, zoom / 100f);
		}

		void zoomAdjust(final float oldZoom, final float newZoom) {
			final int width = this.getOffsetWidth();
			final int height = this.getOffsetHeight();

			final float centerX = this.getOriginX() + (width / 2 * oldZoom);
			final float centerY = this.getOriginY() + (height / 2 * oldZoom);

			final float newCenterX = centerX - (newZoom * width / 2);
			final float newCenterY = centerY - (newZoom * height / 2);
			this.setOriginX((int) newCenterX);
			this.setOriginY((int) newCenterY);
		}
	}

	void updateCoordinatesLabel(final Label coordinates, final Viewport viewport) {
		final int x = viewport.getOriginX();
		final int y = viewport.getOriginY();
		final String text = "Viewport origin: " + StringHelper.padLeft(String.valueOf(x), 5) + " "
				+ StringHelper.padLeft(String.valueOf(y), 5);
		coordinates.setText(text);
	}

	static void log(final String message) {
		System.out.println("CLIENT " + message);
	}
}
