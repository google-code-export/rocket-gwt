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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.Checker;

/**
 * A collection of listeners which are interested in subscribing to various drag
 * events.
 * 
 * @author Miroslav Pokorny (mP)
 */
class DragNDropListenerCollection {

	public DragNDropListenerCollection() {
		this.setListeners(new ArrayList<DragNDropListener>());
	}

	/**
	 * A list containing listeners to the various drag events.
	 */
	private List<DragNDropListener> listeners;

	public List<DragNDropListener> getListeners() {
		Checker.notNull("field:listeners", listeners);
		return listeners;
	}

	public void setListeners(final List<DragNDropListener> listeners) {
		Checker.notNull("parameter:listeners", listeners);
		this.listeners = listeners;
	}

	public void add(final DragNDropListener dragNDropListener) {
		Checker.notNull("parameter:dragNDropListener", dragNDropListener);

		this.getListeners().add(dragNDropListener);
	}

	public void remove(final DragNDropListener dragNDropListener) {
		Checker.notNull("parameter:dragNDropListener", dragNDropListener);

		this.getListeners().remove(dragNDropListener);
	}

	// FIRE EVENTS ::::::::::::::::::::::::::::::::::::::

	public void fireDragStarted(final DragStartEvent event) {
		Checker.notNull("parameter:event", event);

		final Iterator<DragNDropListener> listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = listeners.next();
			listener.onDragStart(event);
		}
	}

	public void fireDragMoveStarted(final DragMoveEvent event) {
		Checker.notNull("parameter:event", event);

		final Iterator<DragNDropListener> listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = listeners.next();
			listener.onDragMove(event);
		}
	}

	public void fireDropped(final DropEvent event) {
		Checker.notNull("parameter:event", event);

		final Iterator<DragNDropListener> listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = listeners.next();
			listener.onDrop(event);
		}
	}
}
