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

import com.google.gwt.user.client.ui.Widget;

/**
 * This listener allows observers to receive a variety of menu events.
 * 
 * Once strategy of using the {@link #onOpen(Widget)} event a listener may wish
 * to rebuild/update child menuItems.
 * 
 * @author Miroslav Pokorny (mP)
 */
public interface MenuListener {

	/**
	 * This event is fired after a subMenu or menuItem is opened.
	 * 
	 * @param event
	 */
	void onOpen(MenuOpenEvent event);
}
