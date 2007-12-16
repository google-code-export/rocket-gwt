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

import rocket.dom.client.Dom;
import rocket.util.client.ObjectHelper;
import rocket.widget.client.SimplePanel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A DropTargetPanel is a panel that can accept any Draggable widget.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DropTargetPanel extends SimplePanel {

	public DropTargetPanel() {
		super();

	}

	protected Element createPanelElement() {
		return DOM.createDiv();
	}

	protected void checkElement(final Element element) {
	}

	protected String getInitialStyleName() {
		return Constants.DRAG_N_DROP_DROP_TARGET_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	protected void insert0(final Element element, final int index) {
		DOM.appendChild(this.getElement(), element);
	}

	protected void remove0(final Element element, final int index) {
		Dom.removeFromParent(element);
	}

	/**
	 * This method simply removes the dropped widget from its parent and makes
	 * it the widget. Sub-classes may wish to override this method when
	 * attempting to do things like drop a widget into a cell when the housed
	 * widget is a Grid.
	 * 
	 * @param event
	 */
	protected void accept(final DropEvent event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		final Widget widget = event.getWidget();
		widget.removeFromParent();
		this.setWidget(widget);
	}

	protected void onAttach() {
		super.onAttach();
		DropTargetPanelCollection.getInstance().add(this);
	}

	protected void onDetach() {
		super.onDetach();
		DropTargetPanelCollection.getInstance().remove(this);
	}
}
