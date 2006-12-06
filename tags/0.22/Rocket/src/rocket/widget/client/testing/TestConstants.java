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
 */package rocket.widget.client.testing;

import rocket.style.client.StyleHelper;
import rocket.widget.client.WidgetConstants;

public class TestConstants extends WidgetConstants {
    /**
     * This style is applied to the container element of the InteractiveList {@see rocket.widget.client.test.InteractiveList}
     */
    public final static String INTERACTIVE_LIST_STYLE = StyleHelper.buildCompound(ROCKET, "interactiveList");

    /**
     * This style is applied to the accompanying log.
     */
    public final static String INTERACTIVE_LIST_WIDGET_LOG_STYLE = StyleHelper.buildCompound(INTERACTIVE_LIST_STYLE,
            "log");

    /**
     * This style is applied to the container element of the InteractivePanel {@see rocket.widget.client.test.InteractivePanel}
     */
    public final static String INTERACTIVE_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "interactivePanel");

    /**
     * This style is applied to the accompanying log.
     */
    public final static String INTERACTIVE_PANEL_WIDGET_LOG_STYLE = StyleHelper.buildCompound(INTERACTIVE_PANEL_STYLE,
            "log");
}
