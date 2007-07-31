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
package rocket.widget.client;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * A OrderedListPanel is a panel that creates a list as the primary container widget. Each widget that is added is again wrapped in their
 * own list item.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class OrderedListPanel extends AbstractPanel implements HasWidgets {

    public OrderedListPanel() {
        super();

        this.setElement(this.createPanelElement());
        this.setStyleName(WidgetConstants.ORDERED_LIST_PANEL_STYLE);
    }

    /**
     * Factory method which creates the parent OL element for this entire panel
     * 
     * @return
     */
    protected Element createPanelElement() {
        return DOM.createElement(WidgetConstants.ORDERED_LIST);
    }

    /**
     * Returns the element which will house each of the new widget's elements.
     * 
     * @return
     */
    public Element getParentElement() {
        return this.getElement();
    }

    protected Element insert0(final Element element, final int indexBefore) {
        ObjectHelper.checkNotNull("parameter:element", element);

        final Element child = this.createElement();
        DOM.insertChild(this.getParentElement(), child, indexBefore);
        DOM.appendChild(child, element);
        return child;
    }

    protected Element createElement() {
        return DOM.createElement(WidgetConstants.ORDERED_LIST_ITEM);
    }

    protected void remove0(final Element element, final int index) {
        ObjectHelper.checkNotNull("parameter:element", element);

        final Element parent = this.getParentElement();
        final Element child = DOM.getChild(parent, index);
        DOM.removeChild(parent, child);
    }
}
