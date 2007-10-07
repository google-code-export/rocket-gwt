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

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.ui.Widget;

class DragEvent {
	/**
	 * The draggable panel housing the widget being dragged.
	 */
	private DraggablePanel draggablePanel;

	public DraggablePanel getDraggablePanel() {
		ObjectHelper.checkNotNull("field:draggablePanel", draggablePanel);
		return this.draggablePanel;
	}

	void setDraggablePanel(final DraggablePanel draggablePanel) {
		ObjectHelper.checkNotNull("parameter:draggablePanel", draggablePanel);
		this.draggablePanel = draggablePanel;
	}

	/**
	 * The draggable panel housing the widget being dragged.
	 */
	private Widget dragged;

	public Widget getDragged() {
		ObjectHelper.checkNotNull("field:dragged", dragged);
		return this.dragged;
	}

	public void setDragged(final Widget dragged) {
		ObjectHelper.checkNotNull("parameter:dragged", dragged);
		this.dragged = dragged;
	}

	/**
	 * The xoffset in pixels of the mouse compared to the dragged
	 */
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

	private boolean cancelled;

	boolean isCancelled() {
		return this.cancelled;
	}

	void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Invoking this method causes event not happen. For DragStartEvents this
	 * method cancels the drag, whilst for DragMoveEvents the move itself is
	 * ignored and the dragged not moved.
	 */
	public void stop() {
		this.setCancelled(true);
	}

	/**
	 * The x coordinates of the mouse
	 */
	private int mousePageX;

	public int getMousePageX() {
		return this.mousePageX;
	}

	void setMousePageX(final int mousePageXOffset) {
		this.mousePageX = mousePageXOffset;
	}

	/**
	 * The Y coordinates of the mouse
	 */
	private int mousePageY;

	public int getMousePageY() {
		return this.mousePageY;
	}

	void setMousePageY(final int mousePageYOffset) {
		this.mousePageY = mousePageYOffset;
	}
}
