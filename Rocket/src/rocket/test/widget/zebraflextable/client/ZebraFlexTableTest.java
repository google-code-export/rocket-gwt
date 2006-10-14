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
package rocket.test.widget.zebraflextable.client;

import rocket.client.widget.ZebraFlexTable;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ZebraFlexTableTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        try {
            final RootPanel rootPanel = RootPanel.get();
            final ZebraFlexTable table = new ZebraFlexTable();

            for (int r = 0; r < 10; r++) {
                for (int c = 0; c < 5; c++) {
                    table.setText(r, c, "" + r + "," + c);
                }
            }
            table.addHeadingStyleToFirstRow();
            rootPanel.add(table);

            final TextBox row = new TextBox();
            final TextBox column = new TextBox();
            rootPanel.add(new Label("Row"));
            rootPanel.add(row);
            rootPanel.add(new Label("Column"));
            rootPanel.add(column);

            rootPanel.add(new HTML("<br/>"));

            final Button insertRow = new Button("insertRow");
            insertRow.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    final int rowNumber = Integer.parseInt(row.getText());
                    table.insertRow(rowNumber);

                    for (int i = 0; i < 10; i++) {
                        table.setText(rowNumber, i, "" + rowNumber + "," + i);
                    }

                }

            });
            rootPanel.add(insertRow);

            final Button insertCell = new Button("insertCell");
            insertCell.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    table.insertCell(Integer.parseInt(row.getText()), Integer.parseInt(column.getText()));
                }

            });
            rootPanel.add(insertCell);

            final Button removeRow = new Button("removeRow");
            removeRow.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    table.removeRow(Integer.parseInt(row.getText()));
                }

            });
            rootPanel.add(removeRow);

            final Button removeCell = new Button("removeCell");
            removeCell.addClickListener(new ClickListener() {
                public void onClick(final Widget sender) {
                    table.removeCell(Integer.parseInt(row.getText()), Integer.parseInt(column.getText()));
                }

            });
            rootPanel.add(removeCell);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
