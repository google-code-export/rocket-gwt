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
 * Convenient start for any DragNDropListener in which all its methods do
 * nothing. This is particularly useful when only one method needs to be
 * implemented.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DragNDropListenerAdapter implements DragNDropListener {

	public boolean onBeforeDragStart(final DraggablePanel widget) {
		return true;
	}

	public void onDragStart(final DraggablePanel widget) {
	}

	public boolean onBeforeDragMove(final Event event, final DraggablePanel widget) {
		return true;
	}

	public void onDragMove(final Event event, final DraggablePanel widget) {
	}

	public boolean onBeforeDrop(final DraggablePanel dragged, final DropTargetPanel target) {
		return true;
	}

	public void onDrop(final DraggablePanel dragged, final DropTargetPanel target) {
	}

	public void onDragCancelled(final DraggablePanel widget) {

	}

	public void onInvalidDrop(final Event event, final DraggablePanel widget) {
	}
}
