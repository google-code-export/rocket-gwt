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

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.ui.Composite;

public class AbstractMenuWidget extends Composite {
    // MENU WIDGET ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void open() {
    }

    public void close() {
    }

    public void select() {
    }

    public void unselect() {
    }

    /**
     * A priority is used to determine which items from a MenuList are shown when a menu is not expanded.
     */
    private int priority;

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    /**
     * When true this particular MenuItem is always shown in a MenuList regardless of its relative priority.
     */
    private boolean alwaysShown;

    public boolean isAlwaysShown() {
        return this.alwaysShown;
    }

    public void setAlwaysShown(final boolean alwaysShown) {
        this.alwaysShown = alwaysShown;
    }

    /**
     * WHen a widget is disabled it may not be selected and appears grayed out etc.
     */
    private boolean disabled;

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    private MenuList parentMenuList;

    protected MenuList getParentMenuList() {
        ObjectHelper.checkNotNull("field:parentMenuList", parentMenuList);
        return this.parentMenuList;
    }

    public void setParentMenuList(MenuList parentMenuList) {
        ObjectHelper.checkNotNull("parameter:parentMenuList", parentMenuList);
        this.parentMenuList = parentMenuList;
    }

    public String toString() {
        return super.toString() + ", priority: " + priority + ", alwaysShown: " + alwaysShown + ", disabled: "
                + disabled;
    }
}
