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
 * A HorizontalSplitterPanel is a panel that lays out its widget in a horizontal manner. The user can drag the splitter to change the size
 * allocated for each widget.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HorizontalSplitterPanel extends SplitterPanel {

    public HorizontalSplitterPanel() {
        super();

        this.createItems();
        this.initWidget((Widget) this.createPanel());
        this.setStyleName(SplitterConstants.HORIZONTAL_SPLITTER_PANEL_STYLE);

        // this fix is necessary to make the widget appear correct in IE6x
        if (BrowserHelper.isInternetExplorer6()) {
            DeferredCommand.add(new Command() {
                public void execute() {
                    setStyleName(SplitterConstants.HORIZONTAL_SPLITTER_PANEL_STYLE);
                }
            });
        }
    }

    /**
     * This factory method creates a new splitter on demand.
     * 
     * @return
     */
    protected Widget createSplitter() {
        return new HorizontalSplitter();
    }

    class HorizontalSplitter extends Splitter {

        HorizontalSplitter() {
            super();

            this.setWidth(HorizontalSplitterPanel.this.getSplitterSize() + "px");
            this.setHeight("100%");
            this.setStyleName(SplitterConstants.HORIZONTAL_SPLITTER_PANEL_SPLITTER_STYLE);
        }

        protected String getDraggingStyleName() {
            return SplitterConstants.HORIZONTAL_SPLITTER_PANEL_SPLITTER_DRAGGING_STYLE;
        }
    }

    /**
     * This is the most important event handler that takes care of adjusting the widths of the widgets before and after the splitter being
     * moved.
     * 
     * @param widget
     * @param event
     */
    protected void handleMouseMove(final Splitter splitter, final Event event) {
        ObjectHelper.checkNotNull("parameter:splitter", splitter);
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            // need to figure out if mouse has moved to the right or left...
            final int mouseX = DOM.eventGetClientX(event) + BrowserHelper.getScrollX();
            final int splitterX = DomHelper.getAbsoluteLeft(splitter.getElement());

            // if the mouse has not moved horizontally but vertically so exit...
            int delta = mouseX - splitterX;
            if (0 == delta) {
                break;
            }

            // grab the widgets before and after the splitter being dragged...
            final Panel panel = this.getPanel();
            final int panelIndex = panel.indexOf(splitter);
            final Widget beforeWidget = panel.get(panelIndex - 1);
            int beforeWidgetWidth = beforeWidget.getOffsetWidth() + delta;

            final Widget afterWidget = panel.get(panelIndex + 1);
            int afterWidgetWidth = afterWidget.getOffsetWidth() - delta;

            final int widthSum = beforeWidgetWidth + afterWidgetWidth;

            final List items = this.getItems();

            // if the mouse moved left make sure the beforeWidget width is not less than its minimumWidth.
            if (delta < 0) {
                final SplitterItem beforeWidgetItem = (SplitterItem) items.get(panelIndex / 2);
                final int minimumWidth = beforeWidgetItem.getMinimumSize();

                if (beforeWidgetWidth < minimumWidth) {
                    delta = minimumWidth - (beforeWidgetWidth - delta);
                    beforeWidgetWidth = minimumWidth;
                    afterWidgetWidth = widthSum - beforeWidgetWidth;
                }
            }

            // since the mouse moved right make sure the afterWidget width is not less than its minimumWidth
            if (delta > 0) {
                final SplitterItem afterWidgetItem = (SplitterItem) items.get(panelIndex / 2 + 1);
                final int minimumWidth = afterWidgetItem.getMinimumSize();
                if (afterWidgetWidth < minimumWidth) {
                    delta = afterWidgetWidth + delta - minimumWidth;
                    afterWidgetWidth = minimumWidth;
                    beforeWidgetWidth = widthSum - afterWidgetWidth;
                }
            }

            // save!
            beforeWidget.setWidth(beforeWidgetWidth + "px");
            afterWidget.setWidth(afterWidgetWidth + "px");

            // update the coordinates of both the splitter and after widget...
            adjustXCoordinate(splitter, delta);
            adjustXCoordinate(afterWidget, delta);

            beforeWidget.setHeight("100%");
            splitter.setHeight("100%");
            afterWidget.setHeight("100%");
            break;
        }
    }

    protected void adjustXCoordinate(final Widget widget, final int delta) {
        final Element element = widget.getElement();
        final int x = DOM.getIntStyleAttribute(element, StyleConstants.LEFT);
        DomHelper.setAbsolutePosition(element, x + delta, 0);
    }

    /**
     * Lays out all added widgets summing their individual weights and then assigns widths to each.
     */
    protected void layoutWidgets0() {
        final int weightSum = this.sumWeights();
        final Panel panel = this.getPanel();
        final int availableWidth = DOM.getIntAttribute(panel.getParentElement(), "offsetWidth");

        final int splitterCount = (panel.getWidgetCount() - 1) / 2;
        final int splitterWidth = this.getSplitterSize();
        final int allocatedWidgetWidth = availableWidth - splitterCount * splitterWidth;
        final float ratio = (float) allocatedWidgetWidth / weightSum;

        int left = 0;
        final Iterator items = this.getItems().iterator();
        final Iterator widgets = panel.iterator();

        boolean more = widgets.hasNext();

        while (more) {
            final Widget widget = (Widget) widgets.next();
            final SplitterItem item = (SplitterItem) items.next();

            // set the widget position...
            final Element widgetElement = widget.getElement();
            DomHelper.setAbsolutePosition(widgetElement, left, 0);

            // overflow...
            DOM.setStyleAttribute(widgetElement, StyleConstants.OVERFLOW, "hidden");

            // set the size(width/height)...
            widget.setHeight("100%");

            // is the last widget ???
            if (false == widgets.hasNext()) {
                widget.setWidth((availableWidth - left) + "px");
                break;
            }

            // calculate the new width...
            final int weight = item.getSizeShare();
            final int width = (int) (ratio * weight);
            widget.setWidth(width + "px");

            left = left + width;

            final Widget splitter = (Widget) widgets.next();

            // set the splitter position...
            DomHelper.setAbsolutePosition(splitter.getElement(), left, 0);

            // overflow...
            DOM.setStyleAttribute(widgetElement, StyleConstants.OVERFLOW, "hidden");

            // set the splitters size...
            splitter.setWidth(splitterWidth + "px");
            splitter.setHeight("100%");

            left = left + splitterWidth;

            more = widgets.hasNext();
        }
    }
}
