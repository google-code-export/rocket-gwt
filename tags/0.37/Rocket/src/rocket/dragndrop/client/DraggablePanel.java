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
package rocket.dragndrop.client;

import java.util.Iterator;

import rocket.browser.client.Browser;
import rocket.dom.client.Dom;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.EventPreviewAdapter;
import rocket.event.client.MouseDownEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.event.client.MouseMoveEvent;
import rocket.event.client.MouseUpEvent;
import rocket.selection.client.Selection;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.widget.client.Hijacker;
import rocket.widget.client.Html;
import rocket.widget.client.SimplePanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A DraggablePanel is a container for a single widget and marks a particular
 * widget as draggable.
 * 
 * Events are fired at different times during the dragging process. THe
 * recipient will have to handle removing the dragged widget from its parent and
 * adding it to its own panel.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DraggablePanel extends SimplePanel {

	public DraggablePanel() {
		super();
	}

	protected void beforeCreatePanelElement() {
		super.beforeCreatePanelElement();

		this.setDragNDropListeners(createDragNDropListeners());
	}

	protected Element createPanelElement() {
		return DOM.createDiv();
	}

	protected void checkElement(final Element element) {
		throw new UnsupportedOperationException("checkElement");
	}

	protected void afterCreatePanelElement() {
		super.afterCreatePanelElement();

		this.getEventListenerDispatcher().addMouseEventListener(new MouseEventAdapter() {
			public void onMouseDown(final MouseDownEvent event) {
				DraggablePanel.this.handleDragStart(event);
			}
		});
	}

	protected String getInitialStyleName() {
		return Constants.DRAG_N_DROP_DRAGGABLE_WIDGET_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.MOUSE_DOWN;
	}

	protected Element insert0(final Element element, final int index) {
		DOM.appendChild(this.getElement(), element);
		return element;
	}

	protected void remove0(final Element element, final int index) {
		Dom.removeFromParent(element);
	}

	int q = 0;

	/**
	 * This event is fired whenever a user starts a drag of a draggable widget.
	 * 
	 * @param mouseDownEvent
	 */
	protected void handleDragStart(final MouseDownEvent mouseDownEvent) {
		ObjectHelper.checkNotNull("parameter:mouseDownEvent", mouseDownEvent);

		if (q == 1) {
			int qq = 0;
		}
		q++;

		while (true) {
			mouseDownEvent.stop();

			Widget dragged = createDraggedWidget();
			dragged.setStyleName(this.getGhostStyle());

			final DragStartEvent dragStartEvent = new DragStartEvent();
			dragStartEvent.setDraggablePanel(this);
			dragStartEvent.setDragged(dragged);
			dragStartEvent.setDraggedElement(mouseDownEvent.getTarget());

			Widget widget = this.getWidget();
			final Element widgetElement = widget.getElement();
			final int widgetLeft = Dom.getAbsoluteLeft(widgetElement);
			final int widgetTop = Dom.getAbsoluteTop(widgetElement);
			dragStartEvent.setWidget(widget);

			final int mouseLeft = mouseDownEvent.getPageX();
			final int mouseTop = mouseDownEvent.getPageY();
			dragStartEvent.setMousePageX(mouseLeft);
			dragStartEvent.setMousePageY(mouseTop);

			int xOffset = widgetLeft - mouseLeft;
			int yOffset = widgetTop - mouseTop;
			dragStartEvent.setXOffset(xOffset);
			dragStartEvent.setYOffset(yOffset);

			this.getDragNDropListeners().fireDragStarted(dragStartEvent);

			// if cancelled good bye!
			if (dragStartEvent.isCancelled()) {
				break;
			}

			Selection.disableTextSelection();
			Selection.clearAnySelectedText();

			// update ui,
			dragged = dragStartEvent.getDragged();
			xOffset = dragStartEvent.getXOffset();
			yOffset = dragStartEvent.getYOffset();
			widget = dragStartEvent.getWidget();

			final DraggedPanel draggedPanel = this.createDraggedPanel();
			draggedPanel.prepare(widget);
			draggedPanel.setWidget(dragged);
			draggedPanel.setXOffset(xOffset);
			draggedPanel.setYOffset(yOffset);
			draggedPanel.setActualWidget(widget);

			// register an event previewer to handle mouse events(watch for drag
			// move, drop ).
			final EventPreviewAdapter greedy = new EventPreviewAdapter() {
				public void onMouseMove(final MouseMoveEvent event) {
					DraggablePanel.this.handleDragMove(event, draggedPanel);
				}

				public void onMouseUp(final MouseUpEvent event) {
					DraggablePanel.this.handleDropped(event, draggedPanel, this);
				}
			};
			greedy.install();

			final Element dragPanelElement = draggedPanel.getElement();
			final int elementPageX = Dom.getAbsoluteLeft(dragPanelElement);
			final int elementPageY = Dom.getAbsoluteTop(dragPanelElement);

			// reposition the $dragged so it follows the mouse.
			final int mousePageX = mouseDownEvent.getPageX();
			final int mousePageY = mouseDownEvent.getPageY();
			dragStartEvent.setMousePageX(mousePageX);
			dragStartEvent.setMousePageY(mousePageY);

			final int newX = mousePageX + xOffset - elementPageX;
			final int newY = mousePageY + yOffset - elementPageY;

			final Element draggedElement = dragged.getElement();
			InlineStyle.setString(draggedElement, StyleConstants.POSITION, "absolute");
			InlineStyle.setInteger(draggedElement, StyleConstants.LEFT, newX, CssUnit.PX);
			InlineStyle.setInteger(draggedElement, StyleConstants.TOP, newY, CssUnit.PX);
			break;
		}
	}

	protected void handleDragMove(final MouseMoveEvent mouseMoveEvent, final DraggedPanel draggedPanel) {
		while (true) {
			mouseMoveEvent.stop();

			// prepare drag move event
			final DragMoveEvent dragMoveEvent = new DragMoveEvent();
			dragMoveEvent.setDraggablePanel(this);

			Widget draggedWidget = draggedPanel.getWidget();
			dragMoveEvent.setDragged(draggedWidget);

			final int xOffset = draggedPanel.getXOffset();
			final int yOffset = draggedPanel.getYOffset();
			dragMoveEvent.setXOffset(xOffset);
			dragMoveEvent.setYOffset(yOffset);

			// fire!
			this.getDragNDropListeners().fireDragMoveStarted(dragMoveEvent);

			if (dragMoveEvent.isCancelled()) {
				break;
			}

			draggedWidget = dragMoveEvent.getDragged();

			// find the absolute position of the DraggablePanel.
			final Element element = draggedPanel.getElement();
			final int elementPageX = Dom.getAbsoluteLeft(element);
			final int elementPageY = Dom.getAbsoluteTop(element);

			// reposition the $dragged so it follows the mouse.
			final int mousePageX = mouseMoveEvent.getPageX();
			final int mousePageY = mouseMoveEvent.getPageY();
			dragMoveEvent.setMousePageX(mousePageX);
			dragMoveEvent.setMousePageY(mousePageY);

			final int newX = mousePageX + xOffset - elementPageX;
			final int newY = mousePageY + yOffset - elementPageY;

			final Element draggedElement = draggedWidget.getElement();
			InlineStyle.setString(draggedElement, StyleConstants.POSITION, "absolute");
			InlineStyle.setInteger(draggedElement, StyleConstants.LEFT, newX, CssUnit.PX);
			InlineStyle.setInteger(draggedElement, StyleConstants.TOP, newY, CssUnit.PX);

			draggedPanel.setWidget(draggedWidget);
			break;
		}
	}

	protected void handleDropped(final MouseUpEvent mouseUpEvent, final DraggedPanel draggedPanel, EventPreviewAdapter previewer) {
		// stop bubbling...
		mouseUpEvent.cancelBubble(true);

		previewer.uninstall();

		// restore widget to its original parent element...
		final Hijacker hijacker = draggedPanel.getHijacker();
		hijacker.restore();

		// reenable selections
		Selection.enableTextSelection();
		Selection.clearAnySelectedText();

		// try and find a drop target...
		final int mouseX = mouseUpEvent.getPageX() + Browser.getScrollX();
		final int mouseY = mouseUpEvent.getPageY() + Browser.getScrollY();

		final DropTargetPanel droppedOver = this.findDropTarget(mouseX, mouseY);

		// create the event...
		final DropEvent dropEvent = new DropEvent();
		dropEvent.setDraggablePanel(this);
		dropEvent.setWidget(draggedPanel.getActualWidget());

		final Element droppedOverElement = this.findDroppedOverElement(mouseX, mouseY, droppedOver.getElement());
		dropEvent.setDroppedOverElement(droppedOverElement);
		dropEvent.setDropTargetPanel(droppedOver);

		this.getDragNDropListeners().fireDropped(dropEvent);

		// the dragged and its panel will disappear...
		draggedPanel.removeFromParent();

		// let the drop target add the widget.
		droppedOver.accept(dropEvent);
	}

	protected DraggedPanel createDraggedPanel() {
		return new DraggedPanel();
	}

	/**
	 * This panel houses the ghost and is moved around each time the user moves
	 * the dragged widget
	 */
	class DraggedPanel extends rocket.widget.client.SimplePanel {

		protected void checkElement(Element element) {
			throw new UnsupportedOperationException("checkElement");
		}

		protected Element createPanelElement() {
			return DOM.createDiv();
		}

		protected String getInitialStyleName() {
			return "";
		}

		protected int getSunkEventsBitMask() {
			return 0; // never receives any events as these are handled by a
						// eventpreview(er)
		}

		protected Element insert0(final Element element, final int index) {
			DOM.insertChild(this.getElement(), element, 0);
			return element;
		}

		protected void remove0(final Element element, final int index) {
			Dom.removeFromParent(element);
		}

		public void setWidget(final Widget widget) {
			super.setWidget(widget);
			if (null != widget) {
				widget.addStyleName(DraggablePanel.this.getGhostStyle());
			}
		}

		public Widget getWidget() {
			final Widget widget = super.getWidget();
			if (null != widget) {
				widget.removeStyleName(DraggablePanel.this.getGhostStyle());
			}
			return widget;
		}

		protected void prepare(final Widget widget) {
			final Element widgetElement = widget.getElement();

			final Hijacker hijacker = new Hijacker(widgetElement);
			this.setHijacker(hijacker);

			RootPanel.get().add(this);

			// insert the root elementof this panel in the same spot were widget
			// was.
			final Element element = this.getElement();
			InlineStyle.setString(element, StyleConstants.POSITION, "relative");
			InlineStyle.setInteger(element, StyleConstants.LEFT, 0, CssUnit.PX);
			InlineStyle.setInteger(element, StyleConstants.TOP, 0, CssUnit.PX);

			hijacker.replace(element);

			DOM.appendChild(element, widgetElement);
		}

		/**
		 * A hijacker is used to store and eventually restore the widget element
		 * to its rightful spot in the dom after a drop.
		 */
		private Hijacker hijacker;

		protected Hijacker getHijacker() {
			ObjectHelper.checkNotNull("field:hijacker", hijacker);
			return this.hijacker;
		}

		public void setHijacker(final Hijacker hijacker) {
			ObjectHelper.checkNotNull("parameter:hijacker", hijacker);
			this.hijacker = hijacker;
		}

		private int xOffset;

		public int getXOffset() {
			return this.xOffset;
		}

		public void setXOffset(final int xOffset) {
			this.xOffset = xOffset;
		}

		private int yOffset;

		public int getYOffset() {
			return this.yOffset;
		}

		public void setYOffset(final int yOffset) {
			this.yOffset = yOffset;
		}

		/**
		 * The widget thats having its ghost being dragged
		 */
		private Widget actualWidget;

		public Widget getActualWidget() {
			ObjectHelper.checkNotNull("field:actualWidget", actualWidget);
			return this.actualWidget;
		}

		public void setActualWidget(final Widget actualWidget) {
			ObjectHelper.checkNotNull("parameter:actualWidget", actualWidget);
			this.actualWidget = actualWidget;
		}
	}

	protected String getGhostStyle() {
		return Constants.DRAG_N_DROP_DRAGGED_WIDGET_STYLE;
	}

	/**
	 * Factory method that creates the initial ghost, by simply cloning the
	 * contents of the current widget.
	 * 
	 * @return
	 */
	protected Widget createDraggedWidget() {
		final Element element = this.getWidget().getElement();
		return new Html(Dom.cloneElement(element, true));
	}

	/**
	 * This method visits all registered drop targets and attempts to find one
	 * that includes the given coordinates.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	protected DropTargetPanel findDropTarget(final int x, final int y) {
		DropTargetPanel found = null;
		final Iterator possibleTargets = DropTargetPanelCollection.getInstance().getDropTargetPanels().iterator();
		while (possibleTargets.hasNext()) {
			final DropTargetPanel possibleTarget = (DropTargetPanel) possibleTargets.next();
			final Element otherElement = possibleTarget.getElement();

			final int left = Dom.getAbsoluteLeft(otherElement);
			final int right = left + possibleTarget.getOffsetWidth();

			if (x < left || x > right) {
				continue;
			}

			final int top = Dom.getAbsoluteTop(otherElement);
			final int bottom = top + possibleTarget.getOffsetHeight();
			if (y < top || y > bottom) {
				continue;
			}

			found = possibleTarget;
			break;
		}

		return found;
	}

	/**
	 * Using the given mouse coordinates and starting at the given element(
	 * which is the root of the dropTarget) find the element that the mouse was
	 * over when the drop occured.
	 * 
	 * Unfortunately the mouseUpEvent target cannot be read as it gives the
	 * ghost rather than the true element.
	 * 
	 * @param x
	 * @param y
	 * @param element
	 * @return
	 */
	protected Element findDroppedOverElement(final int x, final int y, final Element element) {
		Element between = element;

		final int childCount = DOM.getChildCount(element);

		for (int i = 0; i < childCount; i++) {
			final Element child = DOM.getChild(element, i);
			if (Dom.isTag(child, "colgroup")) {
				continue;
			}

			final int childOffsetLeft = Dom.getAbsoluteLeft(child);
			if (x < childOffsetLeft) {
				continue;
			}

			final int childOffsetTop = Dom.getAbsoluteTop(child);
			if (y < childOffsetTop) {
				continue;
			}

			final int childOffsetRight = childOffsetLeft + ObjectHelper.getInteger(child, "offsetWidth");
			if (x > childOffsetRight) {
				continue;
			}
			final int childOffsetBottom = childOffsetTop + ObjectHelper.getInteger(child, "offsetHeight");
			if (y > childOffsetBottom) {
				continue;
			}

			between = this.findDroppedOverElement(x, y, child);
			break;
		}

		return between;
	}

	/**
	 * A list of listeners interested in dragNDrop events.
	 */
	private DragNDropListenerCollection dragNDropListeners;

	protected DragNDropListenerCollection getDragNDropListeners() {
		ObjectHelper.checkNotNull("field:dragNDropListeners", this.dragNDropListeners);
		return this.dragNDropListeners;
	}

	protected void setDragNDropListeners(final DragNDropListenerCollection dragNDropListeners) {
		ObjectHelper.checkNotNull("parameter:dragNDropListeners", dragNDropListeners);
		this.dragNDropListeners = dragNDropListeners;
	}

	protected DragNDropListenerCollection createDragNDropListeners() {
		return new DragNDropListenerCollection();
	}

	public void addDragNDropListener(final DragNDropListener listener) {
		this.getDragNDropListeners().add(listener);
	}

	public void removeDragNDropListener(final DragNDropListener listener) {
		this.getDragNDropListeners().remove(listener);
	}
}