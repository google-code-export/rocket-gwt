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

import rocket.util.client.ObjectHelper;
import rocket.widget.client.Composite;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * Base class for all Menu widgets including Menus(Menu,ContextMenu),
 * MenuItems(MenuItem, SubMenuItem, MenuSpacer) and
 * MenuLists(HorizontalMenuList, VerticalMenuList).
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class MenuWidget extends Composite {

	protected MenuWidget() {
		super();
	}

	protected int getSunkEventsBitMask() {
		return Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT;
	}

	// ACTION :::::::::::::::::::::::::::::::::::::::::::::::::::

	public abstract void hide();

	// EVENT HANDLING ::::::::::::::::::::::::::::::::::::::::::

	/**
	 * Dispatches to the appropriate method depending on the event type.
	 * 
	 * @param event
	 */
	public void onBrowserEvent(final Event event) {
		ObjectHelper.checkNotNull("parameter:event", event);

		while (true) {
			final int eventType = DOM.eventGetType(event);
			if (Event.ONCLICK == eventType) {
				this.handleMouseClick(event);
				break;
			}
			if (Event.ONMOUSEOVER == eventType) {
				this.handleMouseOver(event);
				break;
			}
			if (Event.ONMOUSEOUT == eventType) {
				this.handleMouseOut(event);
				break;
			}
			break;
		}
	}

	/**
	 * This method is fired whenever a menuWidget receives a mouse click
	 * 
	 * @param event
	 */
	protected abstract void handleMouseClick(final Event event);

	/**
	 * This method is fired whenever this menu widget receives a mouse over
	 * event
	 * 
	 * @param event
	 */
	protected abstract void handleMouseOver(final Event event);

	/**
	 * This method is fired whenever this menu widget receives a mouse out event
	 * 
	 * @param event
	 */
	protected abstract void handleMouseOut(final Event event);
}
