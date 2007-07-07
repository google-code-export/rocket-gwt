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

import java.util.ArrayList;
import java.util.List;

import rocket.util.client.ObjectHelper;

/**
 * A collection of all DropTargetPanels that are attached to the dom.
 * 
 * @author Miroslav Pokorny (mP)
 */
class DropTargetPanelCollection {
    /**
     * The DropTargetPanel collection singleton.
     * 
     * As new DropTargetPanels are added to the dom they are also added to this collection. They are in turn removed from this list when
     * they are removed from the dom.
     */
    static DropTargetPanelCollection instance = new DropTargetPanelCollection();

    static DropTargetPanelCollection getInstance() {
        return instance;
    }

    public DropTargetPanelCollection() {
        this.setDropTargetPanels(new ArrayList());
    }

    /**
     * A list which contains all available drop target panels
     */
    private List dropTargetPanels;

    public List getDropTargetPanels() {
        ObjectHelper.checkNotNull("field:dropTargetPanels", dropTargetPanels);
        return dropTargetPanels;
    }

    public void setDropTargetPanels(final List dropTargetPanels) {
        ObjectHelper.checkNotNull("parameter:dropTargetPanels", dropTargetPanels);
        this.dropTargetPanels = dropTargetPanels;
    }

    public void add(final DropTargetPanel dropTargetPanel) {
        ObjectHelper.checkNotNull("parameter:dropTargetPanel", dropTargetPanel);

        this.getDropTargetPanels().add(dropTargetPanel);
    }

    public void remove(final DropTargetPanel dropTargetPanel) {
        ObjectHelper.checkNotNull("parameter:dropTargetPanel", dropTargetPanel);

        this.getDropTargetPanels().remove(dropTargetPanel);
    }
}
