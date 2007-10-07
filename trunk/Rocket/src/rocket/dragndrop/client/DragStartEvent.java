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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A DragStartEvent is fired when a user starts a drag of a draggable widget.
 * 
 * Registered listeners may cancel a drag by calling {@link #stop}. The initial
 * ghost is a copy of the draggable widget, this may in turn be changed by
 * calling {@link #setDragged(com.google.gwt.user.client.ui.Widget)}
 * 
 * @author Miroslav Pokorny
 */
public class DragStartEvent extends DragEvent {
	/**
	 * The widget being dragged.
	 */
	private Widget widget;

	public Widget getWidget() {
		ObjectHelper.checkNotNull("field:widget", widget);
		return this.widget;
	}

	public void setWidget(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);
		this.widget = widget;
	}

	/**
	 * The element that the was clicked to start the drag
	 */
	private Element draggedElement;

	public Element getDraggedElement() {
		return this.draggedElement;
	}

	void setDraggedElement(final Element dropOverElement) {
		this.draggedElement = dropOverElement;
	}
}
