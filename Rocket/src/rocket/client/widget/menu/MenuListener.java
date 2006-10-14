/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.widget.menu;

import com.google.gwt.user.client.ui.Widget;

/**
 * This listener allows observers to receive a variety of menu events.
 * 
 * Once strategy of using the {@link #onBeforeMenuOpened(Widget)} event a listener may wish to rebuild/update child menuItems.
 * 
 * @author Miroslav Pokorny (mP)
 */
public interface MenuListener {

    /**
     * This method is called when a menu is cancelled. Cancelling a menu involves a user navigating over menus submenus and so on and then
     * clicking outside the menu area.
     * 
     * @param widget
     */
    void onMenuCancelled(Widget widget);

    /**
     * This event is fired before a subMenu/ menuItem is opened. This allows a listener to cancel or veto an open event.
     * 
     * @param widget
     * @return Return false to cancel the open( works for both subMenus / menuItems) action or true to let it go.
     */
    boolean onBeforeMenuOpened(Widget widget);

    /**
     * This event is fired after a subMenu or menuItem is opened.
     * 
     * @param widget
     */
    void onMenuOpened(Widget widget);
}
