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
package rocket.test.widget.grid.client;

import rocket.client.util.PrimitiveHelper;
import rocket.client.widget.Grid;
import rocket.client.widget.WidgetProvider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class GridTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        try {
            final RootPanel rootPanel = RootPanel.get();
            final Grid grid = new Grid();
            grid.setColumns(1);
            grid.setCursor(0);
            grid.setRows(1);
            grid.setFiller("<span style='background-color: #eeeeee'></span>");
            grid.setWidgetProvider(new WidgetProvider() {
                public int getFirst() {
                    return 0;
                }

                public int getLast() {
                    return 10;
                }

                public int getCount() {
                    return this.getLast() - getFirst();
                }

                public Widget getWidget(final int index) {
                    PrimitiveHelper.checkBetween("parameter:index", index, this.getFirst(), this.getLast());

                    final Image image = new Image();
                    image.setUrl("image-" + index + ".JPG");
                    return image;
                }
            });
            grid.setAutoRedraw(true);
            grid.redraw();

            final TextBox rows = new TextBox();
            rootPanel.add(new Label("Rows: "));
            rootPanel.add(rows);

            final TextBox columns = new TextBox();
            columns.addKeyboardListener(new KeyboardListenerAdapter() {
                public void onKeyDown(final Widget sender, final char key, final int modifier) {
                    if (key == KeyboardListener.KEY_ENTER) {

                    }
                }
            });
            rootPanel.add(new Label("Columns: "));
            rootPanel.add(columns);

            final TextBox cursor = new TextBox();
            rootPanel.add(new Label("Cursor: "));
            rootPanel.add(cursor);

            final Button update = new Button("Update grid");
            update.addClickListener(new ClickListener() {
                public void onClick(Widget ignored) {
                    final String rowsText = rows.getText();
                    try {
                        grid.setRows(Integer.parseInt(rowsText));
                    } catch (final NumberFormatException nfe) {
                        Window.alert("rows textBox contains an invalid number [" + rowsText + "]");
                    }

                    final String columnsText = columns.getText();
                    try {
                        grid.setColumns(Integer.parseInt(columnsText));
                    } catch (final NumberFormatException nfe) {
                        Window.alert("columns textBox contains an invalid number [" + columnsText + "]");
                    }

                    final String cursorText = cursor.getText();
                    try {
                        grid.setCursor(Integer.parseInt(cursorText));
                    } catch (final NumberFormatException nfe) {
                        Window.alert("cursor textBox contains an invalid number [" + cursorText + "]");
                    }

                }
            });
            rootPanel.add(update);

            rootPanel.add(grid);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
