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

/**
 * This listener interface allows clients to register, react and control any
 * dragging operations performed by the user.
 * 
 * @author Miroslav Pokorny (mP)
 */
public interface DragNDropListener {

	/**
	 * This event is fired when the user attempts to initiate and drag a
	 * draggable widget.
	 * 
	 * @param event
	 */
	void onDragStart(DragStartEvent event);

	/**
	 * This event is fired each time the dragged widget is moved.
	 * 
	 * @param widget
	 */
	void onDragMove(DragMoveEvent event);

	/**
	 * This event is fired after when the user attempts to drop a draggable
	 * widget. allowed to happen.
	 * 
	 * @param event
	 */
	void onDrop(DropEvent event);
}
