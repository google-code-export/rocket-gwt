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
package rocket.widget.client.menu;

import rocket.util.client.ObjectHelper;

/**
 * Base class for all MenuItem type classes including MenuItem, SubMenuItem and MenuSpacer.
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class AbstractMenuItem extends MenuWidget {

    protected AbstractMenuItem() {
        super();
    }

    protected void onDetach() {
        super.onDetach();

        this.hide();
    }

    // EVENT HANDLING ::::::::::::::::::::::::::::::::::::

    // ACTIONS
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    protected void addHighlight() {
        this.addStyleName(this.getSelectedStyle());
    }

    protected void removeHighlight() {
        this.removeStyleName(this.getSelectedStyle());
    }

    /**
     * This style is added or removed depending on whether the widget is highlighted.
     * 
     * @return
     */
    protected abstract String getSelectedStyle();

    /**
     * WHen a widget is disabled it all events are ignored.
     */
    private boolean disabled;

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(final boolean disabled) {
        final String disabledStyle = this.getDisabledStyle();
        if (this.isDisabled()) {
            this.removeStyleName(disabledStyle);
        }
        this.disabled = disabled;
        if (disabled) {
            this.addStyleName(disabledStyle);
        }
    }

    /**
     * Retrieves the style that is added or removed depending on whether the widget is disabled.
     * 
     * @return
     */
    protected abstract String getDisabledStyle();

    /**
     * The parent menuList that contains this MenuItem.
     */
    private MenuList parentMenuList;

    protected MenuList getParentMenuList() {
        ObjectHelper.checkNotNull("field:parentMenuList", parentMenuList);
        return this.parentMenuList;
    }

    public void setParentMenuList(final MenuList parentMenuList) {
        ObjectHelper.checkNotNull("parameter:parentMenuList", parentMenuList);
        this.parentMenuList = parentMenuList;
    }
}
