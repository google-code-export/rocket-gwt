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
package rocket.widget.test.resizablepanel.client;

import rocket.util.client.StringHelper;
import rocket.widget.client.ResizablePanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * This test involves creating three ResizablePanels the first with a TextArea, the second with an Image and the last with a SPAN containing
 * some arbitrary html.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ResizablePanelTest implements EntryPoint {

    final String TEXT = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, fringilla vel, urna. Aliquam commodo ullamcorper erat. Nullam vel justo in neque porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. Morbi nunc est, dignissim non, ornare sed, luctus eu, massa.";

    final int MINIMUM_WIDTH = 150;

    final int MAXIMUM_WIDTH = 300;

    final int MINIMUM_HEIGHT = 150;

    final int MAXIMUM_HEIGHT = 300;

    public void onModuleLoad() {
        // GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
        // public void onUncaughtException(final Throwable caught) {
        // caught.printStackTrace();
        // Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
        // }
        // });

        final RootPanel rootPanel = RootPanel.get();

        final FlexTable panel = new FlexTable();
        rootPanel.add(panel);

        int row = 0;
        panel.setText(row, 0, "Visible handles.");
        panel.setText(row, 1, "Invisible handles.");
        row++;

        panel.setWidget(row, 0, this.createResizableTextAreaWithVisibleHandles());
        panel.setWidget(row, 1, this.createResizableTextAreaWithInvisibleHandles());
        row++;

        panel.setWidget(row, 0, this.createResizableImageWithVisibleHandles());
        panel.setWidget(row, 1, this.createResizableImageWithInvisibleHandles());
        row++;

        panel.setWidget(row, 0, this.createResizableSpanWithVisibleHandles());
        panel.setWidget(row, 1, this.createResizableSpanWithInvisibleHandles());

        final FlexCellFormatter cellFormatter = panel.getFlexCellFormatter();
        for (int r = 1; r < row; r++) {
            for (int c = 0; c < 2; c++) {
                cellFormatter.setAlignment(r, c, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
            }
        }
    }

    protected ResizablePanel createResizableTextAreaWithVisibleHandles() {
        final ResizablePanel panel = this.createResizablePanel();
        panel.setWidget(this.createTextArea());
        panel.setAlwaysShowHandles(true);
        return panel;
    }

    protected ResizablePanel createResizableTextAreaWithInvisibleHandles() {
        final ResizablePanel panel = this.createResizablePanel();
        panel.setWidget(this.createTextArea());
        panel.setAlwaysShowHandles(false);
        return panel;
    }

    protected TextArea createTextArea() {
        final TextArea textArea = new TextArea();
        textArea.setText(TEXT);
        return textArea;
    }

    protected ResizablePanel createResizableImageWithVisibleHandles() {
        final ResizablePanel panel = this.createResizablePanel();
        panel.setWidget(this.createImage());
        panel.setAlwaysShowHandles(true);
        return panel;
    }

    protected ResizablePanel createResizableImageWithInvisibleHandles() {
        final ResizablePanel panel = this.createResizablePanel();
        panel.setWidget(this.createImage());
        panel.setAlwaysShowHandles(false);
        return panel;
    }

    protected Image createImage() {
        final Image image = new Image();
        image.setUrl("./flies.jpg");
        return image;
    }

    protected ResizablePanel createResizableSpanWithVisibleHandles() {
        final ResizablePanel panel = this.createResizablePanel();
        panel.setWidget(this.createSpan());
        panel.setAlwaysShowHandles(true);
        return panel;
    }

    protected ResizablePanel createResizableSpanWithInvisibleHandles() {
        final ResizablePanel panel = this.createResizablePanel();
        panel.setWidget(this.createSpan());
        panel.setAlwaysShowHandles(false);
        return panel;
    }

    protected HTML createSpan() {
        final HTML html = new HTML();
        html.setHTML("<span>" + TEXT + "</span>");
        return html;
    }

    protected ResizablePanel createResizablePanel() {
        final ResizablePanel panel = new ResizablePanel();

        panel.setMinimumWidth(MINIMUM_WIDTH);
        panel.setMaximumWidth(MAXIMUM_WIDTH);
        panel.setWidth(MINIMUM_WIDTH + "px");

        panel.setMinimumHeight(MINIMUM_HEIGHT);
        panel.setMaximumHeight(MAXIMUM_HEIGHT);
        panel.setHeight(MINIMUM_HEIGHT + "px");

        panel.setKeepAspectRatio(false);

        panel.setNorthWestHandle(createHandleWidget("NW"));
        panel.setNorthHandle(createHandleWidget("N"));
        panel.setNorthEastHandle(createHandleWidget("NE"));
        panel.setSouthWestHandle(createHandleWidget("SW"));
        panel.setSouthHandle(createHandleWidget("S"));
        panel.setSouthEastHandle(createHandleWidget("SE"));
        panel.setWestHandle(createHandleWidget("W"));
        panel.setEastHandle(createHandleWidget("E"));
        return panel;
    }

    protected Widget createHandleWidget(final String text) {
        StringHelper.checkNotEmpty("parameter:text", text);

        final HTML html = new HTML();
        html.setText(text);
        html.setWidth("20px");
        html.setHeight("20px");
        return html;
    }
}
