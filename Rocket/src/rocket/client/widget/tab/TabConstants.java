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
package rocket.client.widget.tab;

import rocket.client.browser.BrowserHelper;
import rocket.client.style.StyleHelper;
import rocket.client.widget.WidgetConstants;

public class TabConstants {
    /*
     * TAB
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    public final static String TAB_CLOSE_BUTTON_IMAGE_URL = BrowserHelper.buildImageUrl("/tab/close.gif");

    final static String TAB_BAR_STYLE = "tabBar";

    final static String BEFORE_SPACER_STYLE = "beforeSpacer";

    final static String AFTER_SPACER_STYLE = "afterSpacer";

    final static String ITEM_STYLE = "item";

    final static String ITEM_LABEL_STYLE = "label";

    final static String WIDGET_STYLE = "widget";

    final static String SELECTED_STYLE = "selected";

    final static String CONTENT_STYLE = "content";

    // TOP :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String TOP_TAB_PANEL_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET, "topTabPanel");

    public final static String TOP_TAB_BAR_STYLE = StyleHelper.buildCompound(TOP_TAB_PANEL_STYLE, TAB_BAR_STYLE);

    public final static String TOP_TAB_BAR_BEFORE_SPACER_STYLE = StyleHelper.buildCompound(TOP_TAB_BAR_STYLE,
            BEFORE_SPACER_STYLE);

    public final static String TOP_TAB_BAR_AFTER_SPACER_STYLE = StyleHelper.buildCompound(TOP_TAB_BAR_STYLE,
            AFTER_SPACER_STYLE);

    public final static String TOP_TAB_BAR_ITEM_STYLE = StyleHelper.buildCompound(TOP_TAB_BAR_STYLE, ITEM_STYLE);

    public final static String TOP_TAB_BAR_ITEM_LABEL_STYLE = StyleHelper.buildCompound(TOP_TAB_BAR_ITEM_STYLE,
            ITEM_LABEL_STYLE);

    public final static String TOP_TAB_BAR_ITEM_WIDGET_STYLE = StyleHelper.buildCompound(TOP_TAB_BAR_ITEM_STYLE,
            WIDGET_STYLE);

    public final static String TOP_TAB_BAR_ITEM_SELECTED_STYLE = StyleHelper.buildCompound(TOP_TAB_BAR_ITEM_STYLE,
            SELECTED_STYLE);

    public final static String TOP_TAB_CONTENT_STYLE = StyleHelper.buildCompound(TOP_TAB_PANEL_STYLE, "content");

    /*
     * BOTTOM
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    public final static String BOTTOM_TAB_PANEL_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET,
            "bottomTabPanel");

    public final static String BOTTOM_TAB_BAR_STYLE = StyleHelper.buildCompound(BOTTOM_TAB_PANEL_STYLE, TAB_BAR_STYLE);

    public final static String BOTTOM_TAB_BAR_BEFORE_SPACER_STYLE = StyleHelper.buildCompound(BOTTOM_TAB_BAR_STYLE,
            BEFORE_SPACER_STYLE);

    public final static String BOTTOM_TAB_BAR_AFTER_SPACER_STYLE = StyleHelper.buildCompound(BOTTOM_TAB_BAR_STYLE,
            AFTER_SPACER_STYLE);

    public final static String BOTTOM_TAB_BAR_ITEM_STYLE = StyleHelper.buildCompound(BOTTOM_TAB_BAR_STYLE, ITEM_STYLE);

    public final static String BOTTOM_TAB_BAR_ITEM_LABEL_STYLE = StyleHelper.buildCompound(BOTTOM_TAB_BAR_ITEM_STYLE,
            ITEM_LABEL_STYLE);

    public final static String BOTTOM_TAB_BAR_ITEM_WIDGET_STYLE = StyleHelper.buildCompound(BOTTOM_TAB_BAR_ITEM_STYLE,
            WIDGET_STYLE);

    public final static String BOTTOM_TAB_BAR_ITEM_SELECTED_STYLE = StyleHelper.buildCompound(
            BOTTOM_TAB_BAR_ITEM_STYLE, SELECTED_STYLE);

    public final static String BOTTOM_TAB_CONTENT_STYLE = StyleHelper.buildCompound(BOTTOM_TAB_PANEL_STYLE, "content");

    // LEFT
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String LEFT_TAB_PANEL_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET, "leftTabPanel");

    public final static String LEFT_TAB_BAR_STYLE = StyleHelper.buildCompound(LEFT_TAB_PANEL_STYLE, TAB_BAR_STYLE);

    public final static String LEFT_TAB_BAR_BEFORE_SPACER_STYLE = StyleHelper.buildCompound(LEFT_TAB_BAR_STYLE,
            BEFORE_SPACER_STYLE);

    public final static String LEFT_TAB_BAR_AFTER_SPACER_STYLE = StyleHelper.buildCompound(LEFT_TAB_BAR_STYLE,
            AFTER_SPACER_STYLE);

    public final static String LEFT_TAB_BAR_ITEM_STYLE = StyleHelper.buildCompound(LEFT_TAB_BAR_STYLE, ITEM_STYLE);

    public final static String LEFT_TAB_BAR_ITEM_LABEL_STYLE = StyleHelper.buildCompound(LEFT_TAB_BAR_ITEM_STYLE,
            ITEM_LABEL_STYLE);

    public final static String LEFT_TAB_BAR_ITEM_WIDGET_STYLE = StyleHelper.buildCompound(LEFT_TAB_BAR_ITEM_STYLE,
            WIDGET_STYLE);

    public final static String LEFT_TAB_BAR_ITEM_SELECTED_STYLE = StyleHelper.buildCompound(LEFT_TAB_BAR_ITEM_STYLE,
            SELECTED_STYLE);

    public final static String LEFT_TAB_CONTENT_STYLE = StyleHelper.buildCompound(LEFT_TAB_PANEL_STYLE, "content");

    /*
     * RIGHT
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    public final static String RIGHT_TAB_PANEL_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET,
            "rightTabPanel");

    public final static String RIGHT_TAB_BAR_STYLE = StyleHelper.buildCompound(RIGHT_TAB_PANEL_STYLE, TAB_BAR_STYLE);

    public final static String RIGHT_TAB_BAR_BEFORE_SPACER_STYLE = StyleHelper.buildCompound(RIGHT_TAB_BAR_STYLE,
            BEFORE_SPACER_STYLE);

    public final static String RIGHT_TAB_BAR_AFTER_SPACER_STYLE = StyleHelper.buildCompound(RIGHT_TAB_BAR_STYLE,
            AFTER_SPACER_STYLE);

    public final static String RIGHT_TAB_BAR_ITEM_STYLE = StyleHelper.buildCompound(RIGHT_TAB_BAR_STYLE, ITEM_STYLE);

    public final static String RIGHT_TAB_BAR_ITEM_LABEL_STYLE = StyleHelper.buildCompound(RIGHT_TAB_BAR_ITEM_STYLE,
            ITEM_LABEL_STYLE);

    public final static String RIGHT_TAB_BAR_ITEM_WIDGET_STYLE = StyleHelper.buildCompound(RIGHT_TAB_BAR_ITEM_STYLE,
            WIDGET_STYLE);

    public final static String RIGHT_TAB_BAR_ITEM_SELECTED_STYLE = StyleHelper.buildCompound(RIGHT_TAB_BAR_ITEM_STYLE,
            SELECTED_STYLE);

    public final static String RIGHT_TAB_CONTENT_STYLE = StyleHelper.buildCompound(RIGHT_TAB_PANEL_STYLE, "content");

    /*
     * TAB PANEL ITERATOR
     * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     */
    final static int ITERATOR_TITLES_VIEW = 0;

    final static int ITERATOR_CONTENTS_VIEW = ITERATOR_TITLES_VIEW + 1;

    final static int ITERATOR_TAB_PANELS_VIEW = ITERATOR_CONTENTS_VIEW + 1;

}
