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
package rocket.client.widget;

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * A DivPanel is a panel that uses a div as the primary container widget. Each widget that is added is again wrapped in their own div.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DivPanel extends AbstractPanel implements HasWidgets {

    public DivPanel() {
        super();

        this.setElement(this.createPanelElement());
        this.addStyleName(WidgetConstants.DIV_PANEL_STYLE);
    }

    /**
     * Factory method which creates the parent DIV element for this entire panel
     * 
     * @return
     */
    protected Element createPanelElement() {
        return DOM.createDiv();
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
        return DOM.createDiv();
    }

    protected void remove0(final Element element, final int index) {
        ObjectHelper.checkNotNull("parameter:element", element);

        final Element parent = this.getParentElement();
        final Element child = DOM.getChild(parent, index);
        DOM.removeChild(parent, child);
    }
}
