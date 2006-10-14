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

import rocket.client.browser.BrowserHelper;
import rocket.client.style.StyleHelper;
import rocket.client.widget.WidgetConstants;

/**
 * @author Miroslav Pokorny (mP)
 */
public class MenuConstants extends WidgetConstants {
    public final static String MENU_STYLE = StyleHelper.buildCompound(ROCKET, "menu");

    final static String SELECTED = "selected";

    final static String DISABLED = "disabled";

    // menu bars...
    public final static String HORIZONTAL_MENU_BAR_STYLE = StyleHelper.buildCompound(MENU_STYLE, "horizontalMenuBar");

    public final static String VERTICAL_MENU_BAR_STYLE = StyleHelper.buildCompound(MENU_STYLE, "verticalMenuBar");

    public final static String CONTEXT_MENU_STYLE = StyleHelper.buildCompound(MENU_STYLE, "contextMenu");

    // menu widgets...
    public final static String SUB_MENU_ITEM_STYLE = StyleHelper.buildCompound(MENU_STYLE, "subMenuItem");

    public final static String SUB_MENU_ITEM_SELECTED_STYLE = StyleHelper.buildCompound(SUB_MENU_ITEM_STYLE, SELECTED);

    public final static String SUB_MENU_ITEM_DISABLED_STYLE = StyleHelper.buildCompound(SUB_MENU_ITEM_STYLE, DISABLED);

    public final static String MENU_ITEM_STYLE = StyleHelper.buildCompound(MENU_STYLE, "menuItem");

    public final static String MENU_ITEM_SELECTED_STYLE = StyleHelper.buildCompound(MENU_ITEM_STYLE, SELECTED);

    public final static String MENU_ITEM_DISABLED_STYLE = StyleHelper.buildCompound(MENU_ITEM_STYLE, DISABLED);

    public final static String SPACER_STYLE = StyleHelper.buildCompound(MENU_STYLE, "menuSpacer");

    public final static String SPACER_HTML = "<hr>";

    // menu lists....
    public final static String HORIZONTAL_MENU_LIST_STYLE = StyleHelper.buildCompound(MENU_STYLE, "horizontalMenuList");

    public final static String VERTICAL_MENU_LIST_STYLE = StyleHelper.buildCompound(MENU_STYLE, "verticalMenuList");

    public final static String VERTICAL_MENU_LIST_EXPANDER_STYLE = StyleHelper.buildCompound(MENU_STYLE,
            "verticalMenuListExpander");

    public final static String EXPANDER_IMAGE_URL = BrowserHelper.buildImageUrl("/menu/expander.gif");

    public final static int DOWN_OVERLAP = -1;

    public final static int RIGHT_OVERLAP = -1;

    /**
     * This message is shown within an alert whenever the ContextMenu class is unable to setup a function to return false to override the
     * default behaviour of the browser for oncontextmenu events.
     */
    static final String UNABLE_INSTALL_ONCONTEXTMENU_EVENT_DIVERTER = "Unable to divert oncontextmenu event to GWT.";
}
