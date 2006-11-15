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
package rocket.client.widget.slider;

import rocket.client.style.StyleHelper;
import rocket.client.widget.WidgetConstants;

public class SliderConstants extends WidgetConstants {
    // SLIDER :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public final static String HORIZONTAL_SLIDER_STYLE = StyleHelper.buildCompound(ROCKET, "horizontalSlider");

    public final static String HORIZONTAL_SLIDER_DRAGGING_STYLE = StyleHelper.buildCompound(HORIZONTAL_SLIDER_STYLE,
            "dragging");

    public final static String VERTICAL_SLIDER_STYLE = StyleHelper.buildCompound(ROCKET, "verticalSlider");

    public final static String VERTICAL_SLIDER_DRAGGING_STYLE = StyleHelper.buildCompound(VERTICAL_SLIDER_STYLE,
            "dragging");

}
