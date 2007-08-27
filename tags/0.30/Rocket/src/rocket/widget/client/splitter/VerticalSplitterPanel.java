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

import java.util.Iterator;
import java.util.List;

import rocket.browser.client.BrowserHelper;
import rocket.dom.client.DomHelper;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * A VerticalSplitterPanel is a panel that lays out its widget in a vertical manner. The user can drag the splitter to change the size
 * allocated for each widget.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class VerticalSplitterPanel extends SplitterPanel {

    public VerticalSplitterPanel() {
        super();

        this.createItems();
        this.initWidget((Widget) this.createPanel());
        this.setStyleName(SplitterConstants.VERTICAL_SPLITTER_PANEL_STYLE);

        // this fix is necessary to make the widget appear correct in IE6x
        if (BrowserHelper.isInternetExplorer6()) {
            DeferredCommand.add(new Command() {
                public void execute() {
                    setStyleName(SplitterConstants.VERTICAL_SPLITTER_PANEL_STYLE);
                }
            });
        }
    }

    /**
     * This factory method creates a new splitter on demand.
     * 
     * @return A new Vertical Splitter
     */
    protected Widget createSplitter() {
        return new VerticalSplitter();
    }

    class VerticalSplitter extends Splitter {

        VerticalSplitter() {
            super();

            this.setWidth("100%");
            this.setHeight(VerticalSplitterPanel.this.getSplitterSize() + "px");
            this.setStyleName(SplitterConstants.VERTICAL_SPLITTER_PANEL_SPLITTER_STYLE);
        }

        protected String getDraggingStyleName() {
            return SplitterConstants.VERTICAL_SPLITTER_PANEL_SPLITTER_DRAGGING_STYLE;
        }
    }

    /**
     * This is the most important event handler that takes care of adjusting the widths of the widgets before and after the splitter being
     * moved.
     * 
     * @param splitter
     * @param event
     */
    protected void handleMouseMove(final Splitter splitter, final Event event) {
        ObjectHelper.checkNotNull("parameter:splitter", splitter);
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            // need to figure out if mouse has moved to the right or left...
            final int mouseY = DOM.eventGetClientY(event) + BrowserHelper.getScrollY();
            final int splitterY = DomHelper.getAbsoluteTop(splitter.getElement());

            // if the mouse has not moved vertically but vertically so exit...
            int delta = mouseY - splitterY;
            if (0 == delta) {
                break;
            }

            // grab the widgets before and after the splitter being dragged...
            final Panel panel = this.getPanel();
            final int panelIndex = panel.indexOf(splitter);
            final Widget beforeWidget = panel.get(panelIndex - 1);
            int beforeWidgetHeight = beforeWidget.getOffsetHeight() + delta;

            final Widget afterWidget = panel.get(panelIndex + 1);
            int afterWidgetHeight = afterWidget.getOffsetHeight() - delta;

            final int heightSum = beforeWidgetHeight + afterWidgetHeight;

            final List items = this.getItems();

            // if the mouse moved left make sure the beforeWidget width is not less than its minimumHeight.
            if (delta < 0) {
                final SplitterItem beforeWidgetItem = (SplitterItem) items.get(panelIndex / 2);
                final int minimumHeight = beforeWidgetItem.getMinimumSize();

                if (beforeWidgetHeight < minimumHeight) {
                    delta = minimumHeight - (beforeWidgetHeight - delta);
                    beforeWidgetHeight = minimumHeight;
                    afterWidgetHeight = heightSum - beforeWidgetHeight;
                }
            }

            // since the mouse moved right make sure the afterWidget width is not less than its minimumHeight
            if (delta > 0) {
                final SplitterItem afterWidgetItem = (SplitterItem) items.get(panelIndex / 2 + 1);
                final int minimumHeight = afterWidgetItem.getMinimumSize();
                if (afterWidgetHeight < minimumHeight) {
                    delta = afterWidgetHeight + delta - minimumHeight;
                    afterWidgetHeight = minimumHeight;
                    beforeWidgetHeight = heightSum - afterWidgetHeight;
                }
            }

            // save!
            beforeWidget.setHeight(beforeWidgetHeight + "px");
            afterWidget.setHeight(afterWidgetHeight + "px");

            // update the coordinates of both the splitter and after widget...
            adjustYCoordinate(splitter, delta);
            adjustYCoordinate(afterWidget, delta);

            beforeWidget.setWidth("100%");
            splitter.setWidth("100%");
            afterWidget.setWidth("100%");
            break;
        }
    }

    protected void adjustYCoordinate(final Widget widget, final int delta) {
        final Element element = widget.getElement();
        final int y = DOM.getIntStyleAttribute(element, StyleConstants.TOP);
        DomHelper.setAbsolutePosition(element, 0, y + delta);
    }

    /**
     * Lays out all added widgets summing their individual weights and then assigns widths to each.
     */
    protected void layoutWidgets0() {
        final int weightSum = this.sumWeights();
        final Panel panel = this.getPanel();
        final int availableHeight = DOM.getIntAttribute(panel.getParentElement(), "offsetHeight");

        final int splitterCount = (panel.getWidgetCount() - 1) / 2;
        final int splitterHeight = this.getSplitterSize();
        final int allocatedWidgetHeight = availableHeight - splitterCount * splitterHeight;
        final float ratio = (float) allocatedWidgetHeight / weightSum;

        int top = 0;
        final Iterator items = this.getItems().iterator();
        final Iterator widgets = panel.iterator();

        boolean more = widgets.hasNext();

        while (more) {
            final Widget widget = (Widget) widgets.next();
            final SplitterItem item = (SplitterItem) items.next();

            // set the widget position...
            final Element widgetElement = widget.getElement();
            DomHelper.setAbsolutePosition(widgetElement, 0, top);

            // overflow...
            DOM.setStyleAttribute(widgetElement, StyleConstants.OVERFLOW, "hidden");

            // set the size(width/height)...
            widget.setWidth("100%");

            // is the last widget ???
            if (false == widgets.hasNext()) {
                widget.setHeight((availableHeight - top) + "px");
                break;
            }

            // calculate the new width...
            final int weight = item.getSizeShare();
            final int height = (int) (ratio * weight);
            widget.setHeight(height + "px");

            top = top + height;

            final Widget splitter = (Widget) widgets.next();

            // set the splitter position...
            DomHelper.setAbsolutePosition(splitter.getElement(), 0, top);

            // overflow...
            DOM.setStyleAttribute(widgetElement, StyleConstants.OVERFLOW, "hidden");

            // set the splitters size...
            splitter.setWidth("100%");
            splitter.setHeight(splitterHeight + "px");

            top = top + splitterHeight;

            more = widgets.hasNext();
        }
    }
}