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

import rocket.util.client.StringHelper;

/**
 * This enum represents the possible opening directions for MenuLists.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MenuListOpenDirection {

    /**
     * MenuLists with an UP direction open upwards.
     */
    public final static MenuListOpenDirection UP = new MenuListOpenDirection("UP");

    public final static MenuListOpenDirection RIGHT = new MenuListOpenDirection("RIGHT");

    public final static MenuListOpenDirection DOWN = new MenuListOpenDirection("DOWN");

    public final static MenuListOpenDirection LEFT = new MenuListOpenDirection("LEFT");

    /**
     * Private constructor to control instance creation.
     * 
     * @param description
     */
    private MenuListOpenDirection(final String description) {
        super();

        this.setDescription(description);
    }

    /**
     * A textual description of this enum.
     */
    private String description;

    public String getDescription() {
        StringHelper.checkNotEmpty("field:description", description);
        return description;
    }

    protected void setDescription(final String description) {
        StringHelper.checkNotEmpty("parameter:description", description);
        this.description = description;
    }

    public String toString() {
        return this.getDescription();
    }
}
