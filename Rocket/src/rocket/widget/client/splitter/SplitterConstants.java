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
package rocket.widget.client.splitter;

import rocket.style.client.StyleHelper;
import rocket.widget.client.WidgetConstants;

/**
 * A variety of constants relating to both the horizontal and vertical splitter panels
 * 
 * @author mP
 * 
 */
public class SplitterConstants extends WidgetConstants {

    // HORIZONTAL SPLITTER PANEL
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * This style is applied to the container element of the HorizontalSplitterPanel
     * {@link rocket.widget.client.splitter.HorizontalSplitterPanel}
     */
    public final static String HORIZONTAL_SPLITTER_PANEL_STYLE = StyleHelper.buildCompound(ROCKET,
            "horizontalSplitterPanel");

    /**
     * This style is applied to the element that contains any of the added widgets within a HorizontalSlider
     */
    public final static String HORIZONTAL_SPLITTER_PANEL_WIDGET_HOLDER_STYLE = StyleHelper.buildCompound(
            HORIZONTAL_SPLITTER_PANEL_STYLE, "widgetHolder");

    /**
     * This style is applied to any slider that appears between widgets belonging to a HorizontalSlider
     */
    public final static String HORIZONTAL_SPLITTER_PANEL_SPLITTER_STYLE = StyleHelper.buildCompound(
            HORIZONTAL_SPLITTER_PANEL_STYLE, "splitter");

    /**
     * This style is applied to a slider when it is being dragged...
     */
    public final static String HORIZONTAL_SPLITTER_PANEL_SPLITTER_DRAGGING_STYLE = StyleHelper.buildCompound(
            HORIZONTAL_SPLITTER_PANEL_SPLITTER_STYLE, "dragging");

    // VERTICAL SPLITTER PANEL
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * This style is applied to the container element of the VerticalSplitterPanel
     * {@link rocket.widget.client.splitter.VerticalSplitterPanel}
     */
    public final static String VERTICAL_SPLITTER_PANEL_STYLE = StyleHelper.buildCompound(ROCKET,
            "verticalSplitterPanel");

    /**
     * This style is applied to the element that contains any of the added widgets within a VerticalSlider
     */
    public final static String VERTICAL_SPLITTER_PANEL_WIDGET_HOLDER_STYLE = StyleHelper.buildCompound(
            VERTICAL_SPLITTER_PANEL_STYLE, "widgetHolder");

    /**
     * This style is applied to any slider that appears between widgets belonging to a VerticalSlider
     */
    public final static String VERTICAL_SPLITTER_PANEL_SPLITTER_STYLE = StyleHelper.buildCompound(
            VERTICAL_SPLITTER_PANEL_STYLE, "splitter");

    /**
     * This style is applied to a slider when it is being dragged...
     */
    public final static String VERTICAL_SPLITTER_PANEL_SPLITTER_DRAGGING_STYLE = StyleHelper.buildCompound(
            VERTICAL_SPLITTER_PANEL_SPLITTER_STYLE, "dragging");
}
