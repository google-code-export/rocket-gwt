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
package rocket.dragndrop.test.client;

import rocket.browser.client.BrowserHelper;
import rocket.dragndrop.client.DragNDropListener;
import rocket.dragndrop.client.DraggablePanel;
import rocket.dragndrop.client.DropTargetPanel;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A test which allows the user to test the drag n drop functionality.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DragNDropTest implements EntryPoint {

    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();

                final String text = "FAIL Caught:" + caught + "\nmessage[" + caught.getMessage() + "]";
                DragNDropTest.this.log(text);
            }
        });

        final RootPanel rootPanel = RootPanel.get();

        final CheckBox stopWidgetDrag = new CheckBox("Cancel any widget drag starts");
        rootPanel.add(stopWidgetDrag);

        final CheckBox stopWidgetDrops = new CheckBox("Cancel any widget drops");
        rootPanel.add(stopWidgetDrops);

        final CheckBox limitDragToMoveZone = new CheckBox(
                "Limit drag movements within move zone(box with yellow background)");
        rootPanel.add(limitDragToMoveZone);

        final HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("moveZone");
        rootPanel.add(panel);

        final DraggablePanel source = new DraggablePanel();
        source.setWidget(this.createWidget());
        panel.add(source);

        final DropTargetPanel acceptingTarget = new DropTargetPanel();
        acceptingTarget.setWidget(new HTML("<span>Target which </span><span>accepts drops.</span>"));
        acceptingTarget.addStyleName("accepting");
        panel.add(acceptingTarget);

        final DropTargetPanel rejectingTarget = new DropTargetPanel();
        rejectingTarget.setWidget(new HTML("<span>Target</span><span>which</span><span>rejects drops.</span>"));
        rejectingTarget.addStyleName("rejecting");
        panel.add(rejectingTarget);

        source.addDragNDropListener(new DragNDropListener() {
            public boolean onBeforeDragStart(final DraggablePanel widget) {
                ObjectHelper.checkNotNull("parameter:widget", widget);

                final boolean allowDrag = !stopWidgetDrag.isChecked();
                DragNDropTest.this.log("<b>onBeforeDragStart</b> allowDrag: " + allowDrag + ", widget: "
                        + StringHelper.htmlEncode(widget.toString()));
                return allowDrag;
            }

            public void onDragStart(final DraggablePanel widget) {
                ObjectHelper.checkNotNull("parameter:widget", widget);

                DragNDropTest.this.log("<b>onDragStart</b>...widget: " + StringHelper.htmlEncode(widget.toString()));
            }

            public boolean onBeforeDragMove(Event event, DraggablePanel widget) {
                ObjectHelper.checkNotNull("parameter:event", event);
                ObjectHelper.checkNotNull("parameter:widget", widget);

                boolean allowMove = true;
                if (limitDragToMoveZone.isChecked()) {
                    final int x = DOM.eventGetClientX(event) + BrowserHelper.getScrollX();
                    final int y = DOM.eventGetClientY(event) + BrowserHelper.getScrollY();

                    final int left = panel.getAbsoluteLeft();
                    final int top = panel.getAbsoluteTop();
                    final int width = panel.getOffsetWidth();
                    final int height = panel.getOffsetHeight();

                    allowMove = x > left && x < (left + width) && y > top && y < top + height;
                }

                return allowMove;
            }

            public void onDragMove(final Event event, final DraggablePanel widget) {
                ObjectHelper.checkNotNull("parameter:event", event);
                ObjectHelper.checkNotNull("parameter:widget", widget);

                DragNDropTest.this.log("<b>onDragMove</b>...event: " + event + ", widget: "
                        + StringHelper.htmlEncode(widget.toString()));
            }

            public boolean onBeforeDrop(final DraggablePanel widget, final DropTargetPanel target) {
                ObjectHelper.checkNotNull("parameter:widget", widget);
                ObjectHelper.checkNotNull("parameter:target", target);

                final boolean accept = target == acceptingTarget;

                DragNDropTest.this.log("<b>onBeforeDrop</b>...accept: " + accept + ", widget: "
                        + StringHelper.htmlEncode(widget.toString()) + ", target: "
                        + StringHelper.htmlEncode(target.toString()));
                return accept;
            }

            public void onDrop(final DraggablePanel widget, final DropTargetPanel target) {
                ObjectHelper.checkNotNull("parameter:widget", widget);
                ObjectHelper.checkNotNull("parameter:target", target);

                DragNDropTest.this.log("<b>onDrop</b>...widget: " + StringHelper.htmlEncode(widget.toString())
                        + ", target: " + StringHelper.htmlEncode(target.toString()));
            }

            public void onDragCancelled(final DraggablePanel widget) {
                ObjectHelper.checkNotNull("parameter:widget", widget);

                DragNDropTest.this
                        .log("<b>onDragCancelled</b>...widget: " + StringHelper.htmlEncode(widget.toString()));
            }

            public void onInvalidDrop(final Event event, final DraggablePanel widget) {
                ObjectHelper.checkNotNull("parameter:event", event);
                ObjectHelper.checkNotNull("parameter:widget", widget);

                DragNDropTest.this.log("<b>onInvalidDrop</b>...event: " + event + ", widget: "
                        + StringHelper.htmlEncode(widget.toString()));
            }
        });
    }

    Widget createWidget() {
        final HTML html = new HTML("Drag Me!");
        html.setStyleName("draggable");
        return html;
    }

    /**
     * Adds a new log message to the log div's contents...
     * 
     * @param message
     */
    protected void log(final String message) {
        final Element log = DOM.getElementById("log");
        DOM.setInnerHTML(log, DOM.getInnerHTML(log) + message + "<br>");
    }
}