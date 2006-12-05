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
package rocket.dragndrop.client;

import rocket.style.client.StyleHelper;

public class DragNDropConstants {
    public final static String DRAG_N_DROP_STYLE = StyleHelper.buildCompound("rocket", "dragNDrop");

    public final static String DRAG_N_DROP_DRAGGABLE_WIDGET_STYLE = StyleHelper.buildCompound(DRAG_N_DROP_STYLE,
            "widget");

    public final static String DRAG_N_DROP_BOX_OUTLINE_STYLE = StyleHelper.buildCompound(DRAG_N_DROP_STYLE,
            "boxOutline");

    public final static String DRAG_N_DROP_CLONE_STYLE = StyleHelper.buildCompound(DRAG_N_DROP_STYLE, "clone");

    public final static String DRAG_N_DROP_DRAGGING_STYLE = StyleHelper.buildCompound(DRAG_N_DROP_STYLE, "dragging");

    public final static String DRAG_N_DROP_DROP_TARGET_STYLE = StyleHelper.buildCompound(DRAG_N_DROP_STYLE,
            "dropTarget");
}
