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

import rocket.browser.client.BrowserHelper;
import rocket.dom.client.DomHelper;
import rocket.selection.client.SelectionHelper;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A DraggablePanel is a container for a single widget and marks a particular widget as draggable.
 * 
 * Events are fired at different times during the dragging process. THe recipient will have to handle removing the dragged widget from its
 * parent and adding it to its own panel.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DraggablePanel extends SimplePanel {

    public DraggablePanel() {
        super();

        this.setDragNDropListeners(createDragNDropListeners() );
        this.setStyleName(DragNDropConstants.DRAG_N_DROP_DRAGGABLE_WIDGET_STYLE);
    }

    protected void onAttach() {
        super.onAttach();

        this.unsinkEvents(Event.FOCUSEVENTS | Event.KEYEVENTS | Event.MOUSEEVENTS);
        this.sinkEvents(Event.ONMOUSEDOWN);
        DOM.setEventListener(this.getElement(), this);
    }

    // DND LISTENER HANDLING
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

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
        ObjectHelper.checkNotNull("parameter:listener", listener);
        this.getDragNDropListeners().add(listener);
    }

    public void removeDragNDropListener(final DragNDropListener listener) {
        this.getDragNDropListeners().remove(listener);
    }

    /**
     * This event listener watches out for mouseDown events that occur on this widget.
     */
    public void onBrowserEvent(final Event event) {
        final int eventType = DOM.eventGetType(event);
        if (Event.ONMOUSEDOWN == eventType) {
            this.handleDragStart(event);

            DOM.eventCancelBubble(event, true);
        }
    }

    /**
     * This event is fired whenever a user starts a drag of a draggable widget.
     * 
     * @param event
     */
    protected void handleDragStart(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        boolean cancelled = true;

        final DragNDropListenerCollection listeners = this.getDragNDropListeners();
        try {
            while (true) {
                // first verify that all listeners are ok with a drag start...
                if (false == listeners.fireBeforeDragStarted(this)) {
                    break;
                }

                // create the widget that will be handle that follows the moving mouse...
                this.createDragHandle(event);

                SelectionHelper.disableTextSelection(DomHelper.getBody());
                SelectionHelper.clearAnySelectedText();

                // register an EventPreview listener to follow the mouse...
                final EventPreview preview = this.createDraggingEventPreview();
                this.setDraggingEventPreview(preview);
                DOM.addEventPreview(preview);

                cancelled = false;
                break;
            }
        } finally {
            if (cancelled) {
                listeners.fireDragCancelled(this);
            }
        }
    }

    /**
     * The widget being dragged. It exists only whilst a drag operation is underway and cleared when the drop finishes.
     */
    private DragHandle dragHandle;

    protected DragHandle getDragHandle() {
        ObjectHelper.checkNotNull("field:dragHandle", dragHandle);
        return this.dragHandle;
    }

    protected void setDragHandle(final DragHandle dragHandle) {
        ObjectHelper.checkNotNull("parameter:dragHandle", dragHandle);
        this.dragHandle = dragHandle;
    }

    protected void clearDragHandle() {
        this.dragHandle = null;
    }

    /**
     * Factory method which creates the element which will be dragged along with the moving mouse.
     */
    protected void createDragHandle(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        final Widget widget = this.getWidget();
        final Element element = widget.getElement();
        final Element draggedElement = this.createDragHandle0( widget );

        final DragHandle handle = new DragHandle(draggedElement);
        RootPanel.get().add(handle, 0, 0);

        handle.addStyleName(DragNDropConstants.DRAG_N_DROP_DRAGGING_STYLE);

        // calculate the offset of the mouse relative to the widget.
        final int widgetX = DomHelper.getAbsoluteLeft(element);
        final int widgetY = DomHelper.getAbsoluteTop(element);

        final int mouseX = DOM.eventGetClientX(event) + BrowserHelper.getScrollX();
        final int mouseY = DOM.eventGetClientY(event) + BrowserHelper.getScrollY();

        handle.setXOffset(mouseX - widgetX);
        handle.setYOffset(mouseY - widgetY);

        handle.followMouse(event);

        this.setDragHandle(handle);
    }
    
    /**
     * Factory method that clones the element belonging to the given widget.
     * @param widget
     * @return
     */
    protected Element createDragHandle0( final Widget widget ){
    	return DragNDropHelper.createClone(widget);
    }

    /**
     * The EventPreview object that is following the handle whilst it is being dragged.
     */
    private EventPreview draggingEventPreview;

    protected EventPreview getDraggingEventPreview() {
        ObjectHelper.checkNotNull("field:draggingEventPreview", draggingEventPreview);
        return this.draggingEventPreview;
    }

    protected boolean hasDraggingEventPreview() {
        return null != this.draggingEventPreview;
    }

    protected void setDraggingEventPreview(final EventPreview draggingEventPreview) {
        ObjectHelper.checkNotNull("parameter:draggingEventPreview", draggingEventPreview);
        this.draggingEventPreview = draggingEventPreview;
    }

    protected void clearDraggingEventPreview() {
        this.draggingEventPreview = null;
    }

    /**
     * This EventPreview anonymous class merely delegates to {@link #handleDraggingEventPreview(Event)}
     * 
     * @return
     */
    protected EventPreview createDraggingEventPreview() {
        final EventPreview draggingEventPreview = new EventPreview() {
            public boolean onEventPreview(final Event event) {
                return handleDraggingEventPreview(event);
            }
        };
        return draggingEventPreview;
    }

    /**
     * Receives any onEventPreview events.
     * 
     * @param event
     */
    protected boolean handleDraggingEventPreview(final Event event) {
        boolean cancelEvent = true;

        while (true) {
            final int eventType = DOM.eventGetType(event);

            if (Event.ONMOUSEUP == eventType) {
                this.handleMouseUp(event);
                cancelEvent = false;
                break;
            }

            if (Event.ONMOUSEMOVE == eventType) {
                this.handleMouseMove(event);
                break;
            }
            break;
        }
        return !cancelEvent;
    }

    /**
     * This method is called whenever the mouse button is released.
     * 
     * @param event
     */
    protected void handleMouseUp(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        final DragNDropListenerCollection listeners = this.getDragNDropListeners();

        // find a target...
        final DropTargetPanel target = this.findDropTarget(event);
        while (true) {
            // if no target was found fire a invalidDropTarget event...
            if (null == target) {
                listeners.fireInvalidDrop(event, this);
                break;
            }

            if (listeners.fireBeforeDrop(this, target)) {
                listeners.fireDrop(this, target);
                break;
            }
            listeners.fireDragCancelled(this);
            break;
        }
        this.cancelDrag();
    }

    /**
     * This method cancels any pending drag and also cleans up any associated resources.
     * 
     */
    protected void cancelDrag() {
        final DragHandle handle = this.getDragHandle();
        handle.removeStyleName(DragNDropConstants.DRAG_N_DROP_DRAGGING_STYLE);
        handle.removeFromParent();

        DOM.removeEventPreview(this.getDraggingEventPreview());
        this.clearDraggingEventPreview();

        SelectionHelper.enableTextSelection(DomHelper.getBody());
        SelectionHelper.clearAnySelectedText();
    }

    /**
     * This method gets the current mouse coordinates and calls {@link #findDropTarget(int, int)}
     * 
     * @param event
     * @return The found DropTargetPanel or nunll if none was found.
     */
    protected DropTargetPanel findDropTarget(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        final int x = DOM.eventGetClientX(event) + BrowserHelper.getScrollX();
        final int y = DOM.eventGetClientY(event) + BrowserHelper.getScrollY();
        return this.findDropTarget(x, y);
    }

    /**
     * This method visits all registered drop targets and attempts to find one that includes the given coordinates.
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

            final int left = DomHelper.getAbsoluteLeft(otherElement);
            final int right = left + possibleTarget.getOffsetWidth();

            if (x < left || x > right) {
                continue;
            }
            final int top = DomHelper.getAbsoluteTop(otherElement);
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
     * This method is called whenever the mouse is moved whilst in drag mode.
     * 
     * @param event
     */
    protected void handleMouseMove(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        // ask listeners if the drag should be ignored ?
        final DragNDropListenerCollection listeners = this.getDragNDropListeners();
        if (listeners.fireBeforeDragMoved(event, this)) {

            // update the coordinates of the handle...
            this.getDragHandle().followMouse(event);

            // fired after drag handle has been moved...
            listeners.fireBeforeDragMoved(event, this);
        }
    }

    public String toString() {
        return super.toString() + ", dragNDropListeners: " + dragNDropListeners;
    }

    /**
     * This class exists only whilst a drag operation is underway.
     */
    class DragHandle extends SimplePanel {

        public DragHandle(final Element element) {
            super();

            this.setElement(element);
        }

        /**
         * Updates the position of the element after reading the mouses current position.
         * 
         * @param event
         */
        void followMouse(final Event event) {
            final int mouseX = DOM.eventGetClientX(event);
            final int mouseY = DOM.eventGetClientY(event);

            final int newX = mouseX - this.getXOffset();
            final int newY = mouseY - this.getYOffset();
            DomHelper.setAbsolutePosition(this.getElement(), newX, newY);
        }

        /**
         * THe x offset in pixels of the mouse relative to the top left corner of the widget being dragged
         */
        int xOffset;

        int getXOffset() {
            return this.xOffset;
        }

        void setXOffset(final int xOffset) {
            this.xOffset = xOffset;
        }

        /**
         * The y offset in pixels of the mouse relative to the top left corner of the widget being dragged
         */
        int yOffset;

        int getYOffset() {
            return this.yOffset;
        }

        void setYOffset(final int yOffset) {
            this.yOffset = yOffset;
        }
    }
}