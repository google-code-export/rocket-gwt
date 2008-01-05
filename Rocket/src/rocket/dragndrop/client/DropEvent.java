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

import rocket.util.client.Checker;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A drop event is fired whenever a dragged widget is dropped.
 * 
 * @author Miroslav Pokorny
 */
public class DropEvent {
	/**
	 * The panel that the draggable was released over.
	 * 
	 * For invalid drops this value will be null.
	 */
	private DropTargetPanel dropTargetPanel;

	public DropTargetPanel getDropTargetPanel() {
		return this.dropTargetPanel;
	}

	void setDropTargetPanel(final DropTargetPanel dropTargetPanel) {
		this.dropTargetPanel = dropTargetPanel;
	}

	/**
	 * The element that the drop occured over.
	 */
	private Element droppedOverElement;

	public Element getDroppedOverElement() {
		return this.droppedOverElement;
	}

	void setDroppedOverElement(final Element dropOverElement) {
		this.droppedOverElement = dropOverElement;
	}

	/**
	 * The source of the dragged widget.
	 */
	private DraggablePanel draggablePanel;

	public DraggablePanel getDraggablePanel() {
		Checker.notNull("field:draggablePanel", draggablePanel);
		return this.draggablePanel;
	}

	void setDraggablePanel(final DraggablePanel draggablePanel) {
		Checker.notNull("parameter:draggablePanel", draggablePanel);
		this.draggablePanel = draggablePanel;
	}

	/**
	 * The actual widget that was dragged.
	 */
	private Widget widget;

	public Widget getWidget() {
		Checker.notNull("field:widget", widget);
		return this.widget;
	}

	public void setWidget(final Widget widget) {
		Checker.notNull("parameter:widget", widget);
		this.widget = widget;
	}

	public String toString() {
		return super.toString() + ", droppedOver: " + dropTargetPanel;
	}
}
