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
package rocket.widget.test.sortabletable.client;

import java.util.Comparator;
import java.util.Date;

import rocket.browser.client.BrowserHelper;
import rocket.util.client.StringComparator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This test creates a SortableTable and a number of buttons that allow the user to add / remove etc files to the table for viewing.
 */
public class SortableTableTest implements EntryPoint {

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

        final SortableFileTable table = new SortableFileTable();
        table.setAscendingSortImageSource(GWT.getModuleBaseURL() + "/up.gif");
        table.setDescendingSortImageSource(GWT.getModuleBaseURL() + "/down.gif");
        rootPanel.add(table);
        table.setColumnComparator(StringComparator.IGNORE_CASE_COMPARATOR, 0, true);

        table.setColumnComparator(new Comparator() {
            public int compare(Object first, Object second) {
                final int firstValue = ((Integer) first).intValue();
                final int secondValue = ((Integer) second).intValue();
                return firstValue - secondValue;
            }
        }, 1, true);
        table.makeColumnUnsortable(2);
        table.makeColumnUnsortable(3);
        table.makeColumnUnsortable(4);
        table.makeColumnUnsortable(5);
        table.makeColumnUnsortable(6);

        table.setSortedColumn(1);

        final File file0 = new File();
        file0.setCreateDate(new Date());
        file0.setDescription("none");
        file0.setFilename("apple.txt");
        file0.setServerId("file://apple.txt");
        file0.setSize(1000);
        table.addDownloadFile(file0);

        final File file1 = new File();
        file1.setCreateDate(new Date());
        file1.setDescription("none");
        file1.setFilename("big.txt");
        file1.setServerId("file://big.txt");
        file1.setSize(123456);
        table.addDownloadFile(file1);

        final File file2 = new File();
        file2.setCreateDate(new Date());
        file2.setDescription("none");
        file2.setFilename("small.txt");
        file2.setServerId("file://small.txt");
        file2.setSize(12);
        table.addDownloadFile(file2);

        final File file3 = new File();
        file3.setCreateDate(new Date());
        file3.setDescription("none");
        file3.setFilename("huge.txt");
        file3.setServerId("file://huge.txt");
        file3.setSize(123456789);
        table.addDownloadFile(file3);

        rootPanel.add(new HTML("<br>"));

        rootPanel.add(new Label("Description"));
        final TextBox description = new TextBox();
        description.setText("Description goes in here...");
        rootPanel.add(description);

        rootPanel.add(new Label("Filename"));
        final TextBox filename = new TextBox();
        filename.setText("file://" + Random.nextInt(1357913579) + ".txt");
        rootPanel.add(filename);

        rootPanel.add(new Label("ServerId"));
        final TextBox serverId = new TextBox();
        serverId.setText("id-" + Random.nextInt(246802468));
        rootPanel.add(serverId);

        rootPanel.add(new Label("Size"));
        final TextBox size = new TextBox();
        size.setText("" + Random.nextInt(1234567890));
        rootPanel.add(size);

        rootPanel.add(new HTML("<br>"));
        final Button adder = new Button("Add new File");
        adder.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                try {
                    final File newFile = new File();
                    newFile.setDescription(description.getText());
                    newFile.setFilename(filename.getText());
                    newFile.setServerId(serverId.getText());
                    newFile.setSize(Integer.parseInt(size.getText()));
                    newFile.setCreateDate(new Date());
                    table.addDownloadFile(newFile);

                    description.setText("Description goes in here...");
                    filename.setText("file://" + Random.nextInt(1357913579) + ".txt");
                    serverId.setText("id-" + Random.nextInt(246802468));
                    size.setText("" + Random.nextInt(1234567890));

                } catch (Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });
        rootPanel.add(adder);

        rootPanel.add(new HTML("<br>"));

        final Button remover = new Button("Remove an existing File");
        rootPanel.add(remover);
        remover.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                try {
                    final int row = Integer.parseInt(BrowserHelper.prompt("Remove cursor", "0"));
                    table.removeRow(row);

                } catch (Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });

        rootPanel.add(new HTML("<br>"));

        final Button tableRowGetter = new Button("Get row object");
        rootPanel.add(tableRowGetter);
        tableRowGetter.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                try {
                    final int row = Integer.parseInt(BrowserHelper.prompt("Row cursor", "0"));
                    final Object file = table.getTableRow(row);
                    Window.alert("row: " + row + "\n" + file);

                } catch (Exception caught) {
                    caught.printStackTrace();
                    Window.alert("" + caught);
                }
            }
        });
    }
}
