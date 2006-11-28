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

package rocket.widget.client.tree;

import rocket.style.client.StyleHelper;
import rocket.widget.client.WidgetConstants;

public class TreeConstants extends WidgetConstants {

    public final static String TREE_STYLE = StyleHelper.buildCompound(ROCKET, "tree");

    public final static String TREE_ITEM_STYLE = StyleHelper.buildCompound(ROCKET, "treeItem");

    public final static String TREE_EXPANDER_COLLAPSER_STYLE = StyleHelper.buildCompound(TREE_ITEM_STYLE,
            "expanderCollapser");

    public final static String TREE_WIDGET_STYLE = StyleHelper.buildCompound(TREE_ITEM_STYLE, "widget");

    public final static String TREE_CHILDREN_STYLE = StyleHelper.buildCompound(TREE_ITEM_STYLE, "children");
}
