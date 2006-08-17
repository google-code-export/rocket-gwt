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
/*
 * Copyright 2006 Google Inc.
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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Miroslav Pokorny: The class below is the GWT DeckPanel class with some fixes due to the visibleWidget index going haywire. It was
 * basically pointed to the wrong/non existant children when children were removed. I have changed visibleWidget from being an int to
 * containing the actual widget.
 *
 * Scenario Create three children named A and B and C and then remove B, followed by an attempt to remove C. Because visibleWidget was never
 * updated showWidget throws an ArrayIndexOutOfBOundsException.
 *
 *
 * A panel that displays all of its child widgets in a 'deck', where only one can be visible at a time. It is used by
 * {@link com.google.gwt.user.client.ui.TabPanel}.
 */
public class DeckPanel extends ComplexPanel {

    // private int visibleWidget = -1;
    private Widget visibleWidget = null;

    /**
     * Creates an empty deck panel.
     */
    public DeckPanel() {
        setElement(DOM.createDiv());
    }

    public boolean add(Widget w) {
        return insert(w, getWidgetCount());
    }

    /**
     * Gets the index of the currently-visible widget.
     *
     * @return the visible widget's index
     */
    // public int getVisibleWidget() {
    // return getWidgetIndex( visibleWidget );
    // }
    public Widget getVisibleWidget() {
        return visibleWidget;
    }

    public boolean insert(Widget w, int beforeIndex) {
        super.add(w);
        DOM.appendChild(getElement(), w.getElement());

        DOM.setStyleAttribute(w.getElement(), "width", "100%");
        DOM.setStyleAttribute(w.getElement(), "height", "100%");
        UIObject.setVisible(w.getElement(), false);
        return true;
    }

    public boolean remove(Widget w) {
        if (!super.remove(w)) {
            return false;
        }

        // if removing the visible Widget pick another.
        // if( visibleWidget == w ){
        // int newVisibleIndex = getWidget( w );
        // final int count = getWidgetCount();
        // newVisibleIndex++;
        // if( newVisibleIndex == count ){
        // newVisibleIndex = 0;
        //
        // if( count == 1 ){
        // newVisibleIndex == -1;
        // }
        // }
        //
        // if( -1 != newVisibleIndex ){
        // showWidget( newVisibleIndex );
        // }
        // }

        if (visibleWidget == w) {
            final int count = getWidgetCount();
            if (count > 1) {
                final int visibleIndex = getWidgetIndex(visibleWidget);
                final int newVisibleIndex = (visibleIndex + 1) % count;
                showWidget(newVisibleIndex);
            }// if the last
        }

        DOM.removeChild(getElement(), w.getElement());

        return true;
    }

    /**
     * Shows the widget at the specified index. This causes the currently- visible widget to be hidden.
     *
     * @param index
     *            the index of the widget to be shown
     */
    public void showWidget(int index) {
        // if (visibleWidget != -1) {
        // Widget oldWidget = getWidget(visibleWidget);
        // UIObject.setVisible(oldWidget.getElement(), false);
        // }
        // visibleWidget = index;

        // Widget newWidget = getWidget(visibleWidget);
        // UIObject.setVisible(newWidget.getElement(), true);

        // make the old widget invisible.
        if (null != visibleWidget) {
            final Widget oldWidget = visibleWidget;
            UIObject.setVisible(oldWidget.getElement(), false);
        }

        // use index to pick another widget...
        if (-1 != index) {
            Widget newWidget = getWidget(index);
            UIObject.setVisible(newWidget.getElement(), true);
            this.visibleWidget = newWidget;
        }
        // visibleWidget = index;

        // Widget newWidget = getWidget(visibleWidget);
        // UIObject.setVisible(newWidget.getElement(), true);
    }
}
