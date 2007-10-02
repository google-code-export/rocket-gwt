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
package rocket.widget.client.menu;

import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseOutEvent;
import rocket.event.client.MouseOverEvent;
import rocket.widget.client.Html;

import com.google.gwt.user.client.ui.Widget;

/**
 * Menu spacers may be used to add a break within a menu list.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MenuSpacer extends MenuWidget {
	public MenuSpacer() {
	}

	protected String getInitialStyleName() {
		return Constants.SPACER_STYLE;
	}

	protected void handleMouseClick(final MouseClickEvent event) {
	}

	protected void handleMouseOver(final MouseOverEvent event) {
	}

	protected void handleMouseOut(final MouseOutEvent event) {
	}

	// ACTIONS
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	public void open() {
		throw new UnsupportedOperationException("open()");
	}

	/**
	 * MenuSpacers do nothing when asked to hide...
	 */
	public void hide() {
	}

	protected void addHighlight() {
		throw new UnsupportedOperationException("addHighlight()");
	}

	protected void removeHighlight() {
		throw new UnsupportedOperationException("removeHighlight()");
	}

	protected String getSelectedStyle() {
		throw new UnsupportedOperationException("getSelectedStyle()");
	}

	protected String getDisabledStyle() {
		throw new UnsupportedOperationException("getDisabledStyle()");
	}

	protected Widget createWidget() {
		return new Html(Constants.SPACER_HTML);
	}

	String toString0() {
		return "<spacer>";
	}
}
