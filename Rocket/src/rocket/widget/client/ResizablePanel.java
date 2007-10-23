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

import rocket.dom.client.Dom;
import rocket.event.client.ChangeEventListener;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.EventListenerAdapter;
import rocket.event.client.EventPreviewAdapter;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseUpEvent;
import rocket.style.client.ComputedStyle;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.Css;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

/**
 * A ResizablePanel is a special panel that contains a single child widget which
 * can be resized by the user dragging any of the handles.
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

	protected void beforeCreatePanel() {
		final EventListenerDispatcher dispatcher = this.createEventListenerDispatcher();
		dispatcher.prepareListenerCollections(EventBitMaskConstants.CHANGE | EventBitMaskConstants.FOCUS | EventBitMaskConstants.MOUSE_OUT
				| EventBitMaskConstants.MOUSE_OVER);
		this.setEventListenerDispatcher(dispatcher);
	}

	protected void checkPanel(final Panel panel) {
		throw new UnsupportedOperationException("checkPanel");
	}

	protected Panel createPanel() {
		return new Grid(2, 2);
	}

	protected Grid getGrid() {
		return (Grid) this.getPanel();
	}

	protected void afterCreatePanel() {
		final Grid grid = this.getGrid();
		grid.setBorderWidth(0);
		grid.setCellPadding(0);
		grid.setCellSpacing(0);

		final String handleWidth = "";
		final String handleHeight = "";

		this.prepareTd(0, 0, "center", "middle", "100%", "100%", this.getWidgetStyle());
		this.prepareTd(0, 1, "right", "middle", handleWidth, "100%", this.getRightHandleStyle());
		this.prepareTd(1, 0, "right", "bottom", handleWidth, handleHeight, this.getBottomHandleStyle());
		this.prepareTd(1, 1, "center", "bottom", "100%", handleHeight, this.getCornerHandleStyle());
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

	protected void prepareTd(final int row, final int column, final String horizontalAlign, final String verticalAlign, final String width,
			final String height, final String styleName) {

		final Element element = this.getGrid().getCellFormatter().getElement(row, column);

		// DOM.setElementProperty(element, "align", horizontalAlign);
		// DOM.setStyleAttribute(element, "verticalAlign", verticalAlign);
		DOM.setElementProperty(element, "width", width);
		DOM.setElementProperty(element, "height", height);
		DOM.setElementProperty(element, "className", styleName);

		DOM.sinkEvents(element, EventBitMaskConstants.MOUSE_DOWN);
		DOM.setEventListener(element, new EventListenerAdapter() {
			protected void onMouseDown(final MouseDownEvent event) {
				ResizablePanel.this.onMouseDown(event);
			}
		});
		DOM.setInnerHTML(element, " ");
	}

	protected String getInitialStyleName() {
		return WidgetConstants.RESIZABLE_PANEL_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.CHANGE | EventBitMaskConstants.FOCUS | EventBitMaskConstants.MOUSE_OVER
				| EventBitMaskConstants.MOUSE_OUT;
	}

	public Widget getWidget() {
		return this.getGrid().getWidget(0, 0);
	}

	public void setWidget(final Widget widget) {
		this.getGrid().setWidget(0, 0, widget);

		final Element element = widget.getElement();
		final String inlineWidth =InlineStyle.getString(element, Css.WIDTH);
		final String inlineHeight = InlineStyle.getString(element, Css.HEIGHT);
		ObjectHelper.setString(element, "__width", inlineWidth);
		ObjectHelper.setString(element, "__height", inlineHeight);
		
		final String overflowX = InlineStyle.getString(element, Css.OVERFLOW_X);
		final String overflowY = InlineStyle.getString(element, Css.OVERFLOW_Y);
		ObjectHelper.setString(element, "__overflowX", overflowX);
		ObjectHelper.setString(element, "__overflowY", overflowY);
		
		widget.setWidth("100%");
		widget.setHeight("100%");
		InlineStyle.setString(element, Css.OVERFLOW, "hidden");
	}

	public void insert(final Widget widget, final int indexBefore) {
		if (this.getWidgetCount() > 0 || indexBefore > 0) {
			throw new IllegalArgumentException(GWT.getTypeName(this) + " can only have one widget.");
		}
		this.setWidget(widget);
	}

	public boolean remove(final Widget widget) {
		final boolean removed = this.getGrid().remove(widget);

		if (removed) {
			final Element element = widget.getElement();
			final String inlineWidth = ObjectHelper.getString(element, "__width");
			final String inlineHeight = ObjectHelper.getString(element, "__height");
			InlineStyle.setString(element, Css.WIDTH, inlineWidth);
			InlineStyle.setString(element, Css.HEIGHT, inlineHeight);
			
			final String inlineOverflowX = ObjectHelper.getString(element, "__overflowX");
			final String inlineOverflowY = ObjectHelper.getString(element, "__overflowY");
			InlineStyle.setString(element, Css.OVERFLOW_X, inlineOverflowX);
			InlineStyle.setString(element, Css.OVERFLOW_Y, inlineOverflowY);
		}

		return removed;
	}

	public Iterator iterator() {
		return new Iterator() {

			int state = 0;

			public boolean hasNext() {
				return this.state == 0 && ResizablePanel.this.getWidget() != null;
			}

			public Object next() {
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

	protected void onMouseDown(final MouseDownEvent event) {
		final Element panel = this.getElement();
		final int panelWidth = ComputedStyle.getInteger(panel, Css.WIDTH, CssUnit.PX, 0);
		final int panelHeight = ComputedStyle.getInteger(panel, Css.HEIGHT, CssUnit.PX, 0);

		final Hijacker hijacker = new Hijacker(panel);

		// create the dragged ghost...
		final Element draggedWidget = Dom.cloneElement(panel, true);
		ObjectHelper.setString(draggedWidget, "className", this.getDraggedWidgetStyle());
		InlineStyle.setString(draggedWidget, Css.POSITION, "absolute");
		InlineStyle.setInteger(draggedWidget, Css.LEFT, 0, CssUnit.PX);
		InlineStyle.setInteger(draggedWidget, Css.TOP, 0, CssUnit.PX);
		InlineStyle.setInteger(draggedWidget, Css.Z_INDEX, 10000, CssUnit.NONE);
		InlineStyle.setString(draggedWidget, Css.USER_SELECT, Css.USER_SELECT_DISABLED);

		final Element parent = hijacker.getParent();
		final int childIndex = hijacker.getChildIndex();

		// insert a div that will be parent of the panel and the ghost.
		final Element container = DOM.createDiv();
		InlineStyle.setString(container, Css.POSITION, "relative");
		;
		DOM.appendChild(container, panel);
		DOM.insertChild(parent, container, childIndex);

		DOM.appendChild(container, draggedWidget);

		// record the coordinates of the mouse
		final int initialMousePageX = event.getPageX();
		final int initialMousePageY = event.getPageY();

		// install a previewer...

		boolean updateWidth = false;
		boolean updateHeight = false;

		while (true) {
			final Element target = event.getTarget();
			final CellFormatter cellFormatter = ResizablePanel.this.getGrid().getCellFormatter();
			final Element right = cellFormatter.getElement(0, 1);
			if (DOM.isOrHasChild(right, target)) {
				updateWidth = true;
				break;
			}

			final Element bottom = cellFormatter.getElement(1, 0);
			if (DOM.isOrHasChild(bottom, target)) {
				updateHeight = true;
				break;
			}
			final Element corner = cellFormatter.getElement(1, 1);
			// ObjectHelper.checkSame( "should be corner handle", corner, target
			// );
			updateWidth = true;
			updateHeight = true;
			break;
		}

		final boolean updateWidth0 = updateWidth;
		final boolean updateHeight0 = updateHeight;

		final EventPreviewAdapter previewer = new EventPreviewAdapter() {
			protected void onMouseMove(final MouseMoveEvent event) {
				if (updateWidth0) {
					final int deltaX = event.getPageX() - initialMousePageX;
					int newWidth = panelWidth + deltaX;

					newWidth = Math.min(Math.max(newWidth, ResizablePanel.this.getMinimumWidth()), ResizablePanel.this.getMaximumWidth());
					InlineStyle.setInteger(draggedWidget, Css.WIDTH, newWidth, CssUnit.PX);
				}

				if (updateHeight0) {
					final int deltaY = event.getPageY() - initialMousePageY;
					int newHeight = panelHeight + deltaY;
					newHeight = Math.min(Math.max(newHeight, ResizablePanel.this.getMinimumHeight()), ResizablePanel.this
							.getMaximumHeight());
					InlineStyle.setInteger(draggedWidget, Css.HEIGHT, newHeight, CssUnit.PX);
				}
				InlineStyle.setString(draggedWidget, Css.OVERFLOW, "hidden");
				event.cancelBubble(true);
			}

			protected void onMouseUp(final MouseUpEvent event) {
				this.uninstall();

				int newWidth = 0;
				int newHeight = 0;

				while (true) {
					if (false == ResizablePanel.this.isKeepAspectRatio()) {
						newWidth = ComputedStyle.getInteger(draggedWidget, Css.WIDTH, CssUnit.PX, 0);
						newHeight = ComputedStyle.getInteger(draggedWidget, Css.HEIGHT, CssUnit.PX, 0);
						break;
					}

					if (updateWidth0 && false == updateHeight0) {
						final float ratio = panelWidth * 1.0f / panelHeight;
						newWidth = ComputedStyle.getInteger(draggedWidget, Css.WIDTH, CssUnit.PX, 0);
						newHeight = (int) (newWidth * ratio);
						break;
					}
					if (false == updateWidth0 && updateHeight0) {
						final float ratio = panelHeight * 1.0f / panelWidth;
						newHeight = ComputedStyle.getInteger(draggedWidget, Css.HEIGHT, CssUnit.PX, 0);
						newWidth = (int) (newHeight * ratio);
						break;
					}
					newWidth = ComputedStyle.getInteger(draggedWidget, Css.WIDTH, CssUnit.PX, 0);
					newHeight = ComputedStyle.getInteger(draggedWidget, Css.HEIGHT, CssUnit.PX, 0);

					final float originalDiagonalLength = (float) Math.sqrt(panelWidth * panelWidth + panelHeight * panelHeight);
					final float newDiagonalLength = (float) Math.sqrt(newWidth * newWidth + newHeight * newHeight);
					final float changeRatio = newDiagonalLength / originalDiagonalLength;

					newWidth = (int) (changeRatio * panelWidth);
					newHeight = (int) (changeRatio * panelHeight);
					break;
				}

				// remove the ghost from the dom.
				Dom.removeFromParent(container);
				hijacker.restore();

				ResizablePanel.this.setWidth(newWidth + "px");
				ResizablePanel.this.setHeight(newHeight + "px");
				event.cancelBubble(true);

				ResizablePanel.this.getEventListenerDispatcher().getChangeEventListeners().fireChange(ResizablePanel.this);
			}
		};
		previewer.install();
	}

	protected String getDraggedWidgetStyle() {
		return WidgetConstants.RESIZABLE_PANEL_DRAGGED_WIDGET_STYLE;
	}

	/**
	 * The minimum width in pixels that the child widget may be set.
	 */
	private int minimumWidth;

	public int getMinimumWidth() {
		PrimitiveHelper.checkGreaterThan("field:minimumWidth", 0, minimumWidth);
		return this.minimumWidth;
	}

	public void setMinimumWidth(final int minimumWidth) {
		PrimitiveHelper.checkGreaterThan("parameter:minimumWidth", 0, minimumWidth);
		this.minimumWidth = minimumWidth;
	}

	/**
	 * The maximum width in pixels that the child widget may be set.
	 */
	private int maximumWidth;

	public int getMaximumWidth() {
		PrimitiveHelper.checkGreaterThan("field:maximumWidth", 0, maximumWidth);
		return this.maximumWidth;
	}

	public void setMaximumWidth(final int maximumWidth) {
		PrimitiveHelper.checkGreaterThan("parameter:maximumWidth", 0, maximumWidth);
		this.maximumWidth = maximumWidth;
	}

	/**
	 * The minimum height in pixels that the child widget may be set.
	 */
	private int minimumHeight;

	public int getMinimumHeight() {
		PrimitiveHelper.checkGreaterThan("field:minimumHeight", 0, minimumHeight);
		return this.minimumHeight;
	}

	public void setMinimumHeight(final int minimumHeight) {
		PrimitiveHelper.checkGreaterThan("parameter:minimumHeight", 0, minimumHeight);
		this.minimumHeight = minimumHeight;
	}

	/**
	 * The maximum height in pixels that the child widget may be set.
	 */
	private int maximumHeight;

	public int getMaximumHeight() {
		PrimitiveHelper.checkGreaterThan("field:maximumHeight", 0, maximumHeight);
		return this.maximumHeight;
	}

	public void setMaximumHeight(final int maximumHeight) {
		PrimitiveHelper.checkGreaterThan("parameter:maximumHeight", 0, maximumHeight );
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
	}

	public void addChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().addChangeEventListener(changeEventListener);
	}

	public void removeChangeEventListener(final ChangeEventListener changeEventListener) {
		this.getEventListenerDispatcher().removeChangeEventListener(changeEventListener);
	}

	public String toString() {
		return super.toString() + ", minimumWidth: " + this.minimumWidth + ", maximumWidth: " + maximumWidth + ", minimumHeight: "
				+ this.minimumHeight + ", maximumHeight: " + maximumHeight + ", keepAspectRatio: " + this.keepAspectRatio;
	}

}