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
package rocket.widget.client.accordion;

import rocket.style.client.StyleHelper;
import rocket.widget.client.WidgetConstants;

class AccordionConstants {
    final static String SELECTED = "selected";

    final static String CAPTIONS = "captions";

    final static String CAPTION = "caption";

    final static String CONTENT = "content";

    final static String CONTENTS = "contents";

    public final static String VERTICAL_ACCORDION_PANEL_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET,
            "verticalAccordionPanel");

    public final static String VERTICAL_ACCORDION_PANEL_ITEM_STYLE = StyleHelper.buildCompound(
            VERTICAL_ACCORDION_PANEL_STYLE, "item");

    public final static String VERTICAL_ACCORDION_PANEL_ITEM_SELECTED_STYLE = StyleHelper.buildCompound(
            VERTICAL_ACCORDION_PANEL_ITEM_STYLE, SELECTED);

    public final static String VERTICAL_ACCORDION_PANEL_ITEM_CAPTION_STYLE = StyleHelper.buildCompound(
            VERTICAL_ACCORDION_PANEL_ITEM_STYLE, CAPTION);

    public final static String VERTICAL_ACCORDION_PANEL_ITEM_CONTENT_STYLE = StyleHelper.buildCompound(
            VERTICAL_ACCORDION_PANEL_ITEM_STYLE, CONTENT);

    public final static String LEFT_SIDE_ACCORDION_PANEL_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET,
            "leftSideAccordionPanel");

    public final static String LEFT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE = StyleHelper.buildCompound(
            LEFT_SIDE_ACCORDION_PANEL_STYLE, CAPTIONS);

    public final static String LEFT_SIDE_ACCORDION_PANEL_CONTENTS_STYLE = StyleHelper.buildCompound(
            LEFT_SIDE_ACCORDION_PANEL_STYLE, CONTENTS);

    public final static String LEFT_SIDE_ACCORDION_PANEL_ITEM_STYLE = StyleHelper.buildCompound(
            LEFT_SIDE_ACCORDION_PANEL_STYLE, "item");

    public final static String LEFT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_STYLE = StyleHelper.buildCompound(
            LEFT_SIDE_ACCORDION_PANEL_ITEM_STYLE, CAPTION);

    public final static String LEFT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_SELECTED_STYLE = StyleHelper.buildCompound(
            LEFT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_STYLE, SELECTED);

    public final static String LEFT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_STYLE = StyleHelper.buildCompound(
            LEFT_SIDE_ACCORDION_PANEL_ITEM_STYLE, CONTENT);

    public final static String LEFT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_SELECTED_STYLE = StyleHelper.buildCompound(
            LEFT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_STYLE, SELECTED);

    public final static String RIGHT_SIDE_ACCORDION_PANEL_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET,
            "rightSideAccordionPanel");

    public final static String RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE = StyleHelper.buildCompound(
            RIGHT_SIDE_ACCORDION_PANEL_STYLE, CAPTIONS);

    public final static String RIGHT_SIDE_ACCORDION_PANEL_CONTENTS_STYLE = StyleHelper.buildCompound(
            RIGHT_SIDE_ACCORDION_PANEL_STYLE, CONTENTS);

    public final static String RIGHT_SIDE_ACCORDION_PANEL_ITEM_STYLE = StyleHelper.buildCompound(
            RIGHT_SIDE_ACCORDION_PANEL_STYLE, "item");

    public final static String RIGHT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_STYLE = StyleHelper.buildCompound(
            RIGHT_SIDE_ACCORDION_PANEL_ITEM_STYLE, CAPTION);

    public final static String RIGHT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_SELECTED_STYLE = StyleHelper.buildCompound(
            RIGHT_SIDE_ACCORDION_PANEL_ITEM_CAPTION_STYLE, SELECTED);

    public final static String RIGHT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_STYLE = StyleHelper.buildCompound(
            RIGHT_SIDE_ACCORDION_PANEL_ITEM_STYLE, CONTENT);

    public final static String RIGHT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_SELECTED_STYLE = StyleHelper.buildCompound(
            RIGHT_SIDE_ACCORDION_PANEL_ITEM_CONTENT_STYLE, SELECTED);

}
