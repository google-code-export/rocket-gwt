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
package rocket.client.widget.tree;

import com.google.gwt.user.client.ui.Widget;

public interface TreeListener {
    /**
     * Called when a requested to collapse a particular node.
     * 
     * @return False cancels the collapse, true lets it continue
     */
    boolean onBeforeCollapse(Widget widget);

    /**
     * Called after a node has finished collapsing.
     */
    void onCollapse(Widget widget);

    /**
     * Called when a request comes thru to expand a particular node.
     * 
     * @return False cancels the expand, true lets it continue
     */
    boolean onBeforeExpand(Widget widget);

    /**
     * Called after a node has finished expanding
     * 
     */
    void onExpand(Widget widget);
}