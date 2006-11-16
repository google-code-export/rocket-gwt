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
package rocket.test.widget.divpanel.client;

import java.util.Iterator;

import rocket.client.util.SystemHelper;
import rocket.client.widget.DivPanel;
import rocket.client.widget.test.InteractivePanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class DivPanelTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();
                Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
            }
        });

        final RootPanel rootPanel = RootPanel.get();
        final DivPanel panel = new DivPanel();
        rootPanel.add(panel);

        final InteractivePanel interactivePanel = new InteractivePanel() {
            protected String getCollectionTypeName() {
                return GWT.getTypeName(panel);
            }

            protected int getPanelWidgetCount() {
                return panel.getWidgetCount();
            }

            protected void panelAdd(final Widget widget) {
                panel.add(widget);
            }

            protected void panelInsert(final Widget widget, final int index) {
                panel.insert(widget, index);
            }

            protected Widget panelGet(final int index) {
                return panel.get(index);
            }

            protected void panelRemove(final Widget widget) {
                panel.remove(widget);
            }

            protected Widget createElement() {
                return new HTML("" + System.currentTimeMillis());
            }

            protected Iterator panelIterator() {
                return panel.iterator();
            }

            protected void checkType(Object element) {
                if (false == (element instanceof HTML)) {
                    SystemHelper.handleAssertFailure("Unknown element type type:" + GWT.getTypeName(element));
                }
            }

            protected int getMessageLineCount() {
                return 10;
            }

            protected String toString(final Object element) {
                final HTML html = (HTML) element;
                return html.getText();
            }
        };
        rootPanel.add(interactivePanel);
    }
}
