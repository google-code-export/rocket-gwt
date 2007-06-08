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

import com.google.gwt.user.client.Event;

/**
 * This listener interface allows clients to register, react and control any dragging operations performed by the user.
 * 
 * @author Miroslav Pokorny (mP)
 */
public interface DragNDropListener {
    /**
     * This event is fired just before the user attempts a drag start.
     * 
     * @param widget
     * @return false to cancel the drag otherwise true lets it happen. If any listener returns false the drag is vetoed.
     */
    boolean onBeforeDragStart(DraggablePanel widget);

    /**
     * This event is fired each time the user attempts to start a drag of a draggable widget.
     * 
     * @param widget
     */
    void onDragStart(DraggablePanel widget);

    /**
     * This event is fired just before the dragged widget is moved.
     * 
     * @param widget
     * @return false to ignore the move. This is useful if the widget has been dragged into an invalid region.
     */
    boolean onBeforeDragMove(Event event, DraggablePanel widget);

    /**
     * This event is fired each time the dragged widget is moved.
     * 
     * @param widget
     */
    void onDragMove(Event event, DraggablePanel widget);

    /**
     * This event is fired each time a dragged widget is dropped or let go upon a target.
     * 
     * @param dragged
     *            The widget being dragged
     * @param target
     *            The drop target about to receive the widget
     * @return false to cancel the drop otherwise true lets it happen
     */
    boolean onBeforeDrop(DraggablePanel dragged, DropTargetPanel target);

    /**
     * This event is fired after all listeners have been queried and the drop is allowed to happen.
     * 
     * @param dragged
     *            The widget being dragged
     * @param target
     *            The drop target that just received the widget
     */
    void onDrop(DraggablePanel dragged, DropTargetPanel target);

    /**
     * This method is fired whenever a drag operation is cancelled by a fellow listener.
     * 
     * @param widget
     */
    void onDragCancelled(DraggablePanel widget);

    /**
     * This event is fired each time a widget is dropped over an invalid non drop zone.
     * 
     * @param event
     * @param widget
     */
    void onInvalidDrop(Event event, DraggablePanel widget);
}
