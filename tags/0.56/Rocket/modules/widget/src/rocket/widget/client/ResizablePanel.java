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
package rocket.widget.client;

import java.util.Iterator;
import java.util.NoSuchElementException;

import rocket.event.client.ChangeEventListener;
import rocket.event.client.Event;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.EventListenerAdapter;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseUpEvent;
import rocket.selection.client.Selection;
import rocket.style.client.ComputedStyle;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;
import rocket.util.client.Utilities;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

/**
 * A ResizablePanel is a special panel that contains a single child widget which
 * can be resized by the user dragging any of the handles.
 * 
 * The width and height of the panel cannot be implicitly set but must be
 * controlled by sizing the child widget which will cause the panel to surround
 * it to expand /shrink to accomodate the new child widget's dimensions.
 * 
 * The width/height may be constrained by the following properties
 * <ul>
 * <li>minimum width/height</li>
 * <li>maximum width/height</li>
 * </ul>
 * 
 * When resizing elements such as images it is often useful to keep the aspect
 * ratio when the element is resized. This may be enabled by setting the
 * keepAspectRatio property to true via {@link #setKeepAspectRatio(boolean)}
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ResizablePanel extends CompositePanel {

	public ResizablePanel() {
		super();
	}

	@Override
	protected void beforeCreatePanel() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		dispatcher.prepareListenerCollections(EventBitMaskConstants.CHANGE | EventBitMaskConstants.FOCUS
				| EventBitMaskConstants.MOUSE_OUT | EventBitMaskConstants.MOUSE_OVER);
		this.setEventListenerDispatcher(dispatcher);
	}

	@Override
	protected void checkPanel(final Panel panel) {
		throw new UnsupportedOperationException("checkPanel");
	}

	@Override
	protected Panel createPanel() {
		final Grid grid = new Grid(2, 2);

		grid.setBorderWidth(0);
		grid.setCellPadding(0);
		grid.setCellSpacing(0);
		grid.setStyleName("");

		return grid;
	}

	protected Grid getGrid() {
		return (Grid) this.getPanel();
	}

	/**
	 * Prepares the 4 cells, adding the right style
	 */
	@Override
	protected void afterCreatePanel() {
		this.prepareCell(0, 0, this.getWidgetStyle());
		this.prepareCell(0, 1, this.getRightHandleStyle());
		this.prepareCell(1, 0, this.getBottomHandleStyle());
		this.prepareCell(1, 1, this.getCornerHandleStyle());
	}

	protected String getWidgetStyle() {
		return WidgetConstants.RESIZABLE_PANEL_WIDGET_STYLE;
	}

	protected String getRightHandleStyle() {
		return WidgetConstants.RESIZABLE_PANEL_RIGHT_HANDLE_STYLE;
	}

	protected String getBottomHandleStyle() {
		return WidgetConstants.RESIZABLE_PANEL_BOTTOM_HANDLE_STYLE;
	}

	protected String getCornerHandleStyle() {
		return WidgetConstants.RESIZABLE_PANEL_CORNER_HANDLE_STYLE;
	}

	/**
	 * Prepares one of the cells that belong to the grid that is used to create
	 * and display the ResizablePanel.
	 * 
	 * @param row
	 * @param column
	 * @param styleName
	 */
	protected void prepareCell(final int row, final int column, final String styleName) {
		Checker.notNull("parameter:styleName", styleName);

		final Element element = this.getGrid().getCellFormatter().getElement(row, column);
		element.setClassName(styleName);

		element.setInnerHTML(" ");
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		// only attach listeners when widget is attached.
		this.setHandleEventListeners(this.createMouseListener(true, false), this.createMouseListener(true, true), this
				.createMouseListener(false, true));
	}

	@Override
	protected void onDetach() {
		super.onDetach();

		// remove event listeners from handles..
		this.setHandleEventListeners(null, null, null);
	}

	/**
	 * Helper which sets or removes out the listeners for the right, corner and
	 * bottom resizable panel handles.
	 * 
	 * Null parameters actually remove the event listener for that particular
	 * element.
	 * 
	 * @param right
	 * @param corner
	 * @param bottom
	 */
	protected void setHandleEventListeners(final EventListener right, final EventListener corner, final EventListener bottom) {
		final CellFormatter formatter = this.getGrid().getCellFormatter();
		final Element rightElement = formatter.getElement(0, 1);
		DOM.setEventListener(rightElement, right);
		DOM.sinkEvents(rightElement, EventBitMaskConstants.MOUSE_DOWN);

		final Element cornerElement = formatter.getElement(1, 1);
		DOM.setEventListener(cornerElement, corner);
		DOM.sinkEvents(cornerElement, EventBitMaskConstants.MOUSE_DOWN);

		final Element bottomElement = formatter.getElement(1, 0);
		DOM.setEventListener(bottomElement, bottom);
		DOM.sinkEvents(bottomElement, EventBitMaskConstants.MOUSE_DOWN);
	}

	/**
	 * Factory which creates the appropriate mouse listener for a particular
	 * handle.
	 * 
	 * @param canUpdateWidth
	 * @param canUpdateHeight
	 * @return
	 */
	protected HandleMouseDragger createMouseListener(final boolean canUpdateWidth, final boolean canUpdateHeight) {
		return new HandleMouseDragger() {
			@Override
			protected boolean canUpdateWidth() {
				return canUpdateWidth;
			}

			@Override
			protected boolean canUpdateHeight() {
				return canUpdateHeight;
			}
		};
	}

	/**
	 * Abstract class with several methods that tell whether or not the panel
	 * should stretch in that particular direction.
	 * 
	 * @author n9834386
	 */
	abstract class HandleMouseDragger extends EventListenerAdapter implements com.google.gwt.user.client.EventPreview {

		/**
		 * This method is fired when the user initiates a resize by pressing the
		 * mouse button.
		 */
		@Override
		protected void onMouseDown(final MouseDownEvent event) {
			Checker.notNull("parameter:event", event);

			// stop mouse selection...
			Selection.disableTextSelection();
			event.stop();

			// save the panel's width/height for later usage, particularly when
			// attempting to maintain the aspect ratio.
			final Widget widget = ResizablePanel.this.getWidget();
			widget.addStyleName(ResizablePanel.this.getDraggedWidgetStyle());

			final Element element = widget.getElement();
			final ComputedStyle elementComputedStyle = ComputedStyle.getComputedStyle(element);
			
			final int width = elementComputedStyle.getInteger( Css.WIDTH, CssUnit.PX, 0);
			this.setInitialWidgetWidth(width);

			final int height = elementComputedStyle.getInteger(Css.HEIGHT, CssUnit.PX, 0);
			this.setInitialWidgetHeight(height);

			// record the initial mouse position
			this.setInitialMouseX(event.getPageX());
			this.setInitialMouseY(event.getPageY());

			// install self as a event preview
			this.install();
		}

		/**
		 * Routes the event preview event.
		 */
		public boolean onEventPreview(final com.google.gwt.user.client.Event rawEvent) {
			Event event = null;
			boolean cancelled = false;

			try {
				event = Event.getEvent(rawEvent);

				this.beforeDispatching(event);
				this.dispatch(event);
				this.afterDispatching(event);

				cancelled = event.isCancelled();

			} finally {
				Utilities.destroyIfNecessary(event);
			}

			return !cancelled;
		}

		void install() {
			DOM.addEventPreview(this);
		}

		void uninstall() {
			DOM.removeEventPreview(this);
		}

		/**
		 * The position of the mouse when the drag was initiated.
		 */
		int initialMouseX;

		int getInitialMouseX() {
			return this.initialMouseX;
		}

		void setInitialMouseX(final int initialMouseX) {
			this.initialMouseX = initialMouseX;
		}

		int initialMouseY;

		int getInitialMouseY() {
			return this.initialMouseY;
		}

		void setInitialMouseY(final int initialMouseY) {
			this.initialMouseY = initialMouseY;
		}

		protected void onMouseMove(final MouseMoveEvent event) {
			boolean changed = false;
			if (this.canUpdateWidth()) {
				changed = this.updateWidth(event);
			}
			if (this.canUpdateHeight()) {
				this.updateHeight(event);
				changed = true;
			}

			if (changed) {
				ResizablePanel.this.getEventListenerDispatcher().getChangeEventListeners().fireChange(ResizablePanel.this);
			}

			event.cancelBubble(true);
			event.stop();
		}

		protected boolean updateWidth(final MouseMoveEvent event) {
			final int initialMousePageX = this.getInitialMouseX();

			final int deltaX = event.getPageX() - initialMousePageX;

			final int width = this.getInitialWidgetWidth();
			int newWidth = width + deltaX;

			newWidth = Math.min(Math.max(newWidth, ResizablePanel.this.getMinimumWidth()), ResizablePanel.this.getMaximumWidth());

			final Widget widget = ResizablePanel.this.getWidget();
			widget.setWidth(newWidth + "px");

			// if aspect ratio is enabled also update the height...
			if (ResizablePanel.this.isKeepAspectRatio()) {
				final float ratio = this.getAspectRatio();
				final int newHeight = (int) (newWidth / ratio);
				widget.setHeight(newHeight + "px");
			}
			return deltaX != 0;
		}

		protected boolean updateHeight(final MouseMoveEvent event) {
			final int initialMousePageY = this.getInitialMouseY();

			final int deltaY = event.getPageY() - initialMousePageY;

			final int height = this.getInitialWidgetHeight();
			int newHeight = height + deltaY;

			newHeight = Math.min(Math.max(newHeight, ResizablePanel.this.getMinimumHeight()), ResizablePanel.this.getMaximumHeight());

			final Widget widget = ResizablePanel.this.getWidget();
			widget.setHeight(newHeight + "px");

			// if aspect ratio is enabled also update the height...
			if (ResizablePanel.this.isKeepAspectRatio()) {
				final float ratio = this.getAspectRatio();
				final int newWidth = (int) (newHeight * ratio);
				widget.setWidth(newWidth + "px");
			}

			return deltaY != 0;
		}

		/**
		 * When the mouse is released fire interested event listeners, uninstall
		 * the evnet previewer and reenable text selection.
		 */
		protected void onMouseUp(final MouseUpEvent event) {
			final Widget widget = ResizablePanel.this.getWidget();
			widget.removeStyleName(ResizablePanel.this.getDraggedWidgetStyle());

			Selection.enableTextSelection();
			event.cancelBubble(true);
			event.stop();

			this.uninstall();
		}

		/**
		 * The initial width of the ResizablePanel's only child widget
		 */
		int initialWidgetWidth;

		protected int getInitialWidgetWidth() {
			return initialWidgetWidth;
		}

		protected void setInitialWidgetWidth(final int initialWidgetWidth) {
			this.initialWidgetWidth = initialWidgetWidth;
		}

		/**
		 * The initial height of the ResizablePanel's only child widget
		 */
		int initialWidgetHeight;

		protected int getInitialWidgetHeight() {
			return this.initialWidgetHeight;
		}

		protected void setInitialWidgetHeight(final int initialWidgetHeight) {
			this.initialWidgetHeight = initialWidgetHeight;
		}

		/**
		 * Returns the aspect ratio of the resizable panel expressed as a ratio
		 * of width/height. A value of 2.0 indicates the width is twice as wide
		 * as the height.
		 * 
		 * @return
		 */
		protected float getAspectRatio() {
			return this.getInitialWidgetWidth() * 1.0f / this.getInitialWidgetHeight();
		}

		/**
		 * Corner and Right handle sub classes will return true otherwise
		 * returns false.
		 * 
		 * @return
		 */
		abstract protected boolean canUpdateWidth();

		/**
		 * Corner and bottom handle sub classes will return true otherwise
		 * returns false.
		 * 
		 * @return
		 */
		abstract protected boolean canUpdateHeight();
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.RESIZABLE_PANEL_STYLE;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return 0;
	}

	public Widget getWidget() {
		return this.getGrid().getWidget(0, 0);
	}

	public void setWidget(final Widget widget) {
		this.getGrid().setWidget(0, 0, widget);
	}

	@Override
	public void insert(final Widget widget, final int indexBefore) {
		if (this.getWidgetCount() > 0 || indexBefore > 0) {
			throw new IllegalArgumentException(this.getClass().getName() + " can only have one widget.");
		}
		this.setWidget(widget);
	}

	@Override
	public boolean remove(final Widget widget) {
		return this.getGrid().remove(widget);
	}

	/**
	 * This iterator may be used to iterate over the solitary widget contained
	 * by this ResizablePanel
	 */
	@Override
	public Iterator iterator() {
		return new Iterator<Widget>() {

			int state = 0;

			public boolean hasNext() {
				return this.state == 0 && ResizablePanel.this.getWidget() != null;
			}

			public Widget next() {
				if (false == this.hasNext()) {
					throw new NoSuchElementException();
				}
				this.state = 1;
				return ResizablePanel.this.getWidget();
			}

			public void remove() {
				if (this.state != 1) {
					throw new IllegalStateException();
				}
				this.state = 2;
			}
		};
	}

	protected String getDraggedWidgetStyle() {
		return WidgetConstants.RESIZABLE_PANEL_DRAGGED_WIDGET_STYLE;
	}

	@Override
	public void setWidth(final String width) {
		throw new UnsupportedOperationException("setWidth()");
	}

	@Override
	public void setHeight(final String height) {
		throw new UnsupportedOperationException("setHeight()");
	}

	/**
	 * The minimum width in pixels that the child widget may be set.
	 */
	private int minimumWidth;

	public int getMinimumWidth() {
		Checker.greaterThan("field:minimumWidth", 0, minimumWidth);
		return this.minimumWidth;
	}

	public void setMinimumWidth(final int minimumWidth) {
		Checker.greaterThan("parameter:minimumWidth", 0, minimumWidth);
		this.minimumWidth = minimumWidth;
	}

	/**
	 * The maximum width in pixels that the child widget may be set.
	 */
	private int maximumWidth;

	public int getMaximumWidth() {
		Checker.greaterThan("field:maximumWidth", 0, maximumWidth);
		return this.maximumWidth;
	}

	public void setMaximumWidth(final int maximumWidth) {
		Checker.greaterThan("parameter:maximumWidth", 0, maximumWidth);
		this.maximumWidth = maximumWidth;
	}

	/**
	 * The minimum height in pixels that the child widget may be set.
	 */
	private int minimumHeight;

	public int getMinimumHeight() {
		Checker.greaterThan("field:minimumHeight", 0, minimumHeight);
		return this.minimumHeight;
	}

	public void setMinimumHeight(final int minimumHeight) {
		Checker.greaterThan("parameter:minimumHeight", 0, minimumHeight);
		this.minimumHeight = minimumHeight;
	}

	/**
	 * The maximum height in pixels that the child widget may be set.
	 */
	private int maximumHeight;

	public int getMaximumHeight() {
		Checker.greaterThan("field:maximumHeight", 0, maximumHeight);
		return this.maximumHeight;
	}

	public void setMaximumHeight(final int maximumHeight) {
		Checker.greaterThan("parameter:maximumHeight", 0, maximumHeight);
		this.maximumHeight = maximumHeight;
	}

	/**
	 * When this flag is true any increases/decreases in width or height also
	 * change the other coordinate maintaining the aspect ratio for the child
	 * widget. This is especially useful for images where the aspect ratio is
	 * especially important.
	 */
	private boolean keepAspectRatio;

	public boolean isKeepAspectRatio() {
		return this.keepAspectRatio;
	}

	public void setKeepAspectRatio(final boolean keepAspectRatio) {
		this.keepAspectRatio = keepAspectRatio;
		this.setCornerHandleVisibility(!keepAspectRatio);
	}

	/**
	 * The corner handle should not be visible when keeping the aspect ratio.
	 * 
	 * @param visible
	 */
	protected void setCornerHandleVisibility(final boolean visible) {
		final Element element = this.getGrid().getCellFormatter().getElement(1, 1);
		InlineStyle.getInlineStyle( element ).setString(Css.VISIBILITY, visible ? "visible" : "hidden");
	}

	public void addChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().addChangeEventListener(changeEventListener);
	}

	public void removeChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().removeChangeEventListener(changeEventListener);
	}

	@Override
	public String toString() {
		return super.toString() + ", minimumWidth: " + this.minimumWidth + ", maximumWidth: " + maximumWidth + ", minimumHeight: "
				+ this.minimumHeight + ", maximumHeight: " + maximumHeight + ", keepAspectRatio: " + this.keepAspectRatio;
	}
}