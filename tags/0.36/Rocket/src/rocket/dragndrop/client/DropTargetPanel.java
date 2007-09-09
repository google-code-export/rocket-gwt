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

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A DropTargetPanel is a panel that can accept any Draggable widget.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DropTargetPanel extends SimplePanel {

	public DropTargetPanel() {
		super();

		this.setStyleName(Constants.DRAG_N_DROP_DROP_TARGET_STYLE);
	}

	/**
	 * This method is invoked whenever a Draggable widget is dropped over this
	 * target. The default behaviour is to simply accept the widget overwriting
	 * the previous widget
	 * 
	 * @param widget
	 */
	protected void accept(final Widget widget) {
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
