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

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.Event;

/**
 * A collection of listeners which are interested in subscribing to various drag
 * events.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DragNDropListenerCollection {

	public DragNDropListenerCollection() {
		this.setListeners(new ArrayList());
	}

	/**
	 * A list containing listeners to the various drag events.
	 */
	private List listeners;

	public List getListeners() {
		ObjectHelper.checkNotNull("field:listeners", listeners);
		return listeners;
	}

	public void setListeners(final List listeners) {
		ObjectHelper.checkNotNull("parameter:listeners", listeners);
		this.listeners = listeners;
	}

	public void add(final DragNDropListener dragNDropListener) {
		ObjectHelper.checkNotNull("parameter:dragNDropListener", dragNDropListener);

		this.getListeners().add(dragNDropListener);
	}

	public void remove(final DragNDropListener dragNDropListener) {
		ObjectHelper.checkNotNull("parameter:dragNDropListener", dragNDropListener);

		this.getListeners().remove(dragNDropListener);
	}

	// FIRE EVENTS ::::::::::::::::::::::::::::::::::::::

	public boolean fireBeforeDragStarted(final DraggablePanel widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		boolean startDrag = true;
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = (DragNDropListener) listeners.next();
			startDrag = listener.onBeforeDragStart(widget);
			if (false == startDrag) {
				this.fireDragCancelled(widget);
				break;
			}
		}
		return startDrag;
	}

	public void fireDragStarted(final DraggablePanel widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = (DragNDropListener) listeners.next();
			listener.onBeforeDragStart(widget);
		}
	}

	public boolean fireBeforeDragMoved(final Event event, final DraggablePanel widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		boolean allowMove = true;
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = (DragNDropListener) listeners.next();
			allowMove = listener.onBeforeDragMove(event, widget);
			if (!allowMove) {
				break;
			}
		}
		return allowMove;
	}

	public void fireDragMoved(final Event event, final DraggablePanel widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = (DragNDropListener) listeners.next();
			listener.onDragMove(event, widget);
		}
	}

	public boolean fireBeforeDrop(final DraggablePanel widget, final DropTargetPanel target) {
		ObjectHelper.checkNotNull("parameter:widget", widget);
		ObjectHelper.checkNotNull("parameter:target", target);

		boolean drop = true;
		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = (DragNDropListener) listeners.next();
			drop = listener.onBeforeDrop(widget, target);
			if (false == drop) {
				break;
			}
		}

		return drop;
	}

	public void fireDrop(final DraggablePanel widget, final DropTargetPanel target) {
		ObjectHelper.checkNotNull("parameter:widget", widget);
		ObjectHelper.checkNotNull("parameter:target", target);

		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = (DragNDropListener) listeners.next();
			listener.onDrop(widget, target);
		}
	}

	public void fireDragCancelled(final DraggablePanel widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = (DragNDropListener) listeners.next();
			listener.onDragCancelled(widget);
		}
	}

	public void fireInvalidDrop(final Event event, final DraggablePanel widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		final Iterator listeners = this.getListeners().iterator();

		while (listeners.hasNext()) {
			final DragNDropListener listener = (DragNDropListener) listeners.next();
			listener.onInvalidDrop(event, widget);
		}
	}
}
